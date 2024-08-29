/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.system.network

import com.splendo.kaluga.base.IOSVersion
import com.splendo.kaluga.logging.debug
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.Network.nw_interface_type_cellular
import platform.Network.nw_interface_type_wifi
import platform.Network.nw_path_get_status
import platform.Network.nw_path_is_expensive
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_monitor_t
import platform.Network.nw_path_monitor_update_handler_t
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_status_unsatisfied
import platform.Network.nw_path_t
import platform.Network.nw_path_uses_interface_type
import platform.SystemConfiguration.SCNetworkReachabilityCallBack
import platform.SystemConfiguration.SCNetworkReachabilityContext
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import platform.SystemConfiguration.SCNetworkReachabilityFlags
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.SCNetworkReachabilityRef
import platform.SystemConfiguration.SCNetworkReachabilitySetCallback
import platform.SystemConfiguration.SCNetworkReachabilitySetDispatchQueue
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsWWAN
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_attr_make_with_qos_class
import platform.darwin.dispatch_queue_create
import platform.posix.QOS_CLASS_UTILITY

/**
 * Default implementation of [NetworkManager]
 */
actual class DefaultNetworkManager internal constructor(private val appleNetworkManager: AppleNetworkManager) : NetworkManager {

    /**
     * Builder for creating a [DefaultNetworkManager]
     */
    class Builder : NetworkManager.Builder {
        override fun create(): NetworkManager {
            val appleNetworkManager = if (IOSVersion.systemVersion >= IOSVersion(12)) {
                NWPathNetworkManager()
            } else {
                SCNetworkManager()
            }
            return DefaultNetworkManager(appleNetworkManager)
        }
    }

    actual override val network: Flow<NetworkConnectionType> get() = appleNetworkManager.network
    actual override suspend fun startMonitoring() = appleNetworkManager.startMonitoring()
    actual override suspend fun stopMonitoring() = appleNetworkManager.stopMonitoring()

    internal interface AppleNetworkManager : NetworkManager

    internal class NWPathNetworkManager : AppleNetworkManager {

        private val networkChannel = Channel<NetworkConnectionType>(Channel.UNLIMITED)
        override val network: Flow<NetworkConnectionType> = networkChannel.receiveAsFlow()
        private val networkMonitor = object : nw_path_monitor_update_handler_t {
            override fun invoke(network: nw_path_t) {
                checkReachability(network)
            }
        }
        private val nwPathMonitor: nw_path_monitor_t = nw_path_monitor_create().apply {
            val queue = dispatch_queue_create(
                "com.splendo.kaluga.system.network",
                dispatch_queue_attr_make_with_qos_class(
                    null,
                    QOS_CLASS_UTILITY,
                    DISPATCH_QUEUE_PRIORITY_DEFAULT,
                ),
            )
            nw_path_monitor_set_queue(
                this,
                queue,
            )
            nw_path_monitor_set_update_handler(this, networkMonitor)
        }

        override suspend fun startMonitoring() {
            nw_path_monitor_start(nwPathMonitor)
        }

        override suspend fun stopMonitoring() {
            nw_path_monitor_cancel(nwPathMonitor)
        }

        private fun checkReachability(network: nw_path_t) {
            when (nw_path_get_status(network)) {
                nw_path_status_satisfied -> {
                    if (nw_path_uses_interface_type(network, nw_interface_type_wifi)) {
                        if (nw_path_is_expensive(network)) {
                            // connected to hotspot
                            networkChannel.trySend(NetworkConnectionType.Known.Wifi(isExpensive = true))
                        } else {
                            networkChannel.trySend(NetworkConnectionType.Known.Wifi())
                        }
                    } else if (nw_path_uses_interface_type(network, nw_interface_type_cellular)) {
                        networkChannel.trySend(NetworkConnectionType.Known.Cellular)
                    }
                }
                nw_path_status_unsatisfied -> {
                    networkChannel.trySend(NetworkConnectionType.Known.Absent)
                }
            }
        }
    }

    internal class SCNetworkManager : AppleNetworkManager {

        private val networkChannel = Channel<NetworkConnectionType>(Channel.UNLIMITED)
        override val network: Flow<NetworkConnectionType> = networkChannel.receiveAsFlow()
        private val lock = Mutex()
        private var reachability: SCNetworkReachabilityRef? = null

        private val onNetworkStateChanged: SCNetworkReachabilityCallBack = staticCFunction {
                _: SCNetworkReachabilityRef?,
                flags: SCNetworkReachabilityFlags,
                info: COpaquePointer?,
            ->
            if (info == null) {
                return@staticCFunction
            }

            val networkManager = info.asStableRef<SCNetworkManager>().get()
            networkManager.checkReachability(networkManager, flags)
        }

        override suspend fun startMonitoring() = lock.withLock {
            reachability = SCNetworkReachabilityCreateWithName(null, "www.appleiphonecell.com")
            val context = nativeHeap.alloc<SCNetworkReachabilityContext>()
            context.info = StableRef.create(this@SCNetworkManager).asCPointer()

            if (!areParametersSet(context)) {
                debug { "Something went wrong setting the parameters" }
            }

            val flag = nativeHeap.alloc<SCNetworkReachabilityFlagsVar>()
            SCNetworkReachabilityGetFlags(reachability, flag.ptr)

            nativeHeap.free(context.rawPtr)
            nativeHeap.free(flag.rawPtr)
        }

        override suspend fun stopMonitoring() = lock.withLock {
            reachability = null
        }

        private fun areParametersSet(context: SCNetworkReachabilityContext): Boolean {
            if (!SCNetworkReachabilitySetCallback(reachability, onNetworkStateChanged, context.ptr)) {
                return false
            }

            if (!SCNetworkReachabilitySetDispatchQueue(reachability, dispatch_get_main_queue())) {
                return false
            }
            return true
        }

        private fun checkReachability(scNetworkManager: SCNetworkManager, flags: SCNetworkReachabilityFlags) {
            when (flags) {
                kSCNetworkReachabilityFlagsReachable -> {
                    if (flags == kSCNetworkReachabilityFlagsIsWWAN) {
                        scNetworkManager.networkChannel.trySend(NetworkConnectionType.Known.Cellular)
                    } else {
                        scNetworkManager.networkChannel.trySend(NetworkConnectionType.Known.Wifi())
                    }
                }
                else -> {
                    scNetworkManager.networkChannel.trySend(NetworkConnectionType.Known.Absent)
                }
            }
        }
    }
}
