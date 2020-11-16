/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.system.network.services

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.system.network.Network
import com.splendo.kaluga.system.network.NetworkStateChange
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
import platform.Network.nw_interface_type_cellular
import platform.Network.nw_interface_type_wifi
import platform.Network.nw_path_get_status
import platform.Network.nw_path_is_expensive
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
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
import platform.darwin.dispatch_get_main_queue

sealed class NetworkManagerHandler {
    abstract val handleStateChange: NetworkStateChange

    abstract fun startNotifier()
    abstract fun stopNotifier()

    class NWPathNetworkManager(
        override val handleStateChange: NetworkStateChange,
    ) : NetworkManagerHandler() {

        private var _nwPathMonitor = AtomicReference<nw_path_monitor_t>(null)
        private var nwPathMonitor: nw_path_monitor_t
            get() = _nwPathMonitor.get()
            set(value) = _nwPathMonitor.set(value)

        private val onNetworkStateChanged = object : nw_path_monitor_update_handler_t {
            override fun invoke(network: nw_path_t) {
                checkReachability(network)
            }
        }

        override fun startNotifier() {
            nwPathMonitor = nw_path_monitor_create()
            nw_path_monitor_set_update_handler(nwPathMonitor, onNetworkStateChanged)
            nw_path_monitor_start(nwPathMonitor)
        }

        override fun stopNotifier() {
            nw_path_monitor_cancel(nwPathMonitor)
            nwPathMonitor = null
        }

        private fun checkReachability(network: nw_path_t) {
            when (nw_path_get_status(network)) {
                nw_path_status_satisfied -> {
                    if (nw_path_uses_interface_type(network, nw_interface_type_wifi)) {
                        if(nw_path_is_expensive(network)) {
                            // connected to hotspot
                            handleStateChange(Network.Known.Wifi(isExpensive = true))
                        } else {
                            handleStateChange(Network.Known.Wifi())
                        }
                    }
                    else if (nw_path_uses_interface_type(network, nw_interface_type_cellular)) {
                        handleStateChange(Network.Known.Cellular())
                    }
                }
                nw_path_status_unsatisfied -> {
                    handleStateChange(Network.Known.Absent)
                }
            }
        }
    }

    class SCNetworkManager(
        override val handleStateChange: NetworkStateChange
    ) : NetworkManagerHandler() {

        private var _isListening = AtomicReference(false)
        private var isListening: Boolean
            get() = _isListening.get()
            set(value) = _isListening.set(value)

        private var _reachability = AtomicReference<SCNetworkReachabilityRef?>(null)
        private var reachability: SCNetworkReachabilityRef?
            get() = _reachability.get()
            set(value) = _reachability.set(value)

        private val onNetworkStateChanged: SCNetworkReachabilityCallBack = staticCFunction { ref: SCNetworkReachabilityRef?, flags: SCNetworkReachabilityFlags, info: COpaquePointer? ->
            if (info == null) {
                return@staticCFunction
            }

            val networkManager = info.asStableRef<SCNetworkManager>().get()
            networkManager.checkReachability(networkManager, flags)
        }

        override fun startNotifier() {
            if (isListening) {
                return
            }
            reachability = SCNetworkReachabilityCreateWithName(null, "www.appleiphonecell.com")

            val context = nativeHeap.alloc<SCNetworkReachabilityContext>()
            context.info = StableRef.create(this@SCNetworkManager).asCPointer()

            if (!areParametersSet(context)) {
                debug { "Something went wrong setting the parameters" }
            }

            val flag = nativeHeap.alloc<SCNetworkReachabilityFlagsVar>()
            SCNetworkReachabilityGetFlags(reachability, flag.ptr)

            isListening = true
            nativeHeap.free(context.rawPtr)
            nativeHeap.free(flag.rawPtr)
        }

        override fun stopNotifier() {
            reachability = null
            isListening = false
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
                        scNetworkManager.handleStateChange(Network.Known.Cellular())
                    } else {
                        scNetworkManager.handleStateChange(Network.Known.Wifi())
                    }
                }
                else -> {
                    scNetworkManager.handleStateChange(Network.Known.Absent)
                }
            }
        }
    }
}
