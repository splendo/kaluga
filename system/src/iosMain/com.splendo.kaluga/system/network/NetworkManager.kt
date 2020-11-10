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

package com.splendo.kaluga.system.network

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.logging.debug
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
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

actual class NetworkManager actual constructor(
    networkStateRepo: NetworkStateRepo,
    context: Any?
) : BaseNetworkManager(networkStateRepo) {

    private val reachability = SCNetworkReachabilityCreateWithName(null, "www.appleiphonecell.com")

    private var _isListening = AtomicReference(false)
    private var isListening: Boolean
        get() = _isListening.get()
        set(value) = _isListening.set(value)

    private var _isNetworkEnabled = AtomicReference(false)
    private var isNetworkEnabled: Boolean
        get() = _isNetworkEnabled.get()
        set(value) = _isNetworkEnabled.set(value)

    private val onNetworkStateChanged: SCNetworkReachabilityCallBack = staticCFunction { ref: SCNetworkReachabilityRef?, flags: SCNetworkReachabilityFlags, info: COpaquePointer? ->
        if (info == null) {
            debug { "DEBUG: info is null, returning." }
            return@staticCFunction
        }
        val networkManager = info.asStableRef<NetworkManager>().get()
        networkManager.checkReachability(networkManager, flags)
    }

    override suspend fun isNetworkEnabled(): Boolean = isNetworkEnabled

    override suspend fun startMonitoringNetwork() {
        // val currentVersion = UIDevice.currentDevice.systemVersion.split(".")[0].toInt()
        // if (currentVersion >= 12) {
        //     println("current ios version is > 12 $currentVersion")
        //     // configureNwPath()
        // } else {
        //     println("current ios version is < 12 $currentVersion")
        //     configureSCNetworkReachability()
        // }
        startNotifier()
    }

    override suspend fun stopMonitoringNetwork() {
        stopNotifier()
    }

    private fun startNotifier() {
        if (isListening) {
            return
        }

        val context = nativeHeap.alloc<SCNetworkReachabilityContext>()
        context.info = StableRef.create(this@NetworkManager).asCPointer()

        if (!areParametersSet(context)) {
            debug { "Something went wrong setting the parameters" }
        }

        val flag = nativeHeap.alloc<SCNetworkReachabilityFlagsVar>()
        SCNetworkReachabilityGetFlags(reachability, flag.ptr)

        isListening = true
        nativeHeap.free(context.rawPtr)
        nativeHeap.free(flag.rawPtr)
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

    private fun stopNotifier() {
        isListening = false
    }

    private fun checkReachability(networkManager: NetworkManager, flags: SCNetworkReachabilityFlags) {
        when (flags) {
            kSCNetworkReachabilityFlagsReachable -> {
                if (flags == kSCNetworkReachabilityFlagsIsWWAN) {
                    isNetworkEnabled = true
                    networkManager.handleNetworkStateChanged(Network.Cellular())
                } else {
                    isNetworkEnabled = true
                    networkManager.handleNetworkStateChanged(Network.Wifi())
                }
            }
            else -> {
                isNetworkEnabled = false
                networkManager.handleNetworkStateChanged(Network.Absent)
            }
        }
    }
}
