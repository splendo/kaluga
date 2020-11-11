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

class SCNetworkManager(
    val networkManager: NetworkManagerService
) : NetworkManagerHandler {

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
                    scNetworkManager.networkManager.handleStateChanged(Network.Cellular())
                } else {
                    scNetworkManager.networkManager.handleStateChanged(Network.Wifi())
                }
            }
            else -> {
                scNetworkManager.networkManager.handleStateChanged(Network.Absent)
            }
        }
    }

}