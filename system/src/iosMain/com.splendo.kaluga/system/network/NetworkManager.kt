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

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.objcPtr
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
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual class NetworkManager actual constructor(
    private val networkStateRepo: NetworkStateRepo,
    context: Any?
) : BaseNetworkManager(networkStateRepo) {

    private val reachability = SCNetworkReachabilityCreateWithName(null, "www.appleiphonecell.com")
    private var isListening = false

    private val callback: SCNetworkReachabilityCallBack = staticCFunction { ref: SCNetworkReachabilityRef?, flags: SCNetworkReachabilityFlags, info: COpaquePointer? ->
        println("DEBUG: Inside SCNetworkReachabilityCallBack staticCFunction")

        if (info == null) {
            println("DEBUG: info is null, returning.")
            return@staticCFunction
        }
        val networkManager = info.asStableRef<NetworkManager>().get()

        dispatch_async(dispatch_get_main_queue()) {
            networkManager.checkReachability(networkManager, flags)
        }
    }

    override suspend fun startMonitoringNetwork() {
        // val currentVersion = UIDevice.currentDevice.systemVersion.split(".")[0].toInt()
        // if (currentVersion >= 12) {
        //     println("current ios version is > 12 $currentVersion")
        //     // configureNwPath()
        // } else {
        //     println("current ios version is < 12 $currentVersion")
        //     configureSCNetworkReachability()
        // }
        configureSCNetworkReachability()
    }

    override suspend fun stopMonitoringNetwork() {
        TODO("Not yet implemented")
    }

    private fun configureSCNetworkReachability() {
        if (isListening) {
            return
        }

        val context = nativeHeap.alloc<SCNetworkReachabilityContext>()
        context.info = interpretCPointer(this.objcPtr())

        if (!areParametersSet(context)) {
            println("Something went wrong setting the parameters")
        }

        dispatch_async(dispatch_get_main_queue()) {
            val flag = nativeHeap.alloc<SCNetworkReachabilityFlagsVar>()
            SCNetworkReachabilityGetFlags(reachability, flag.ptr)
            nativeHeap.free(flag.rawPtr)
        }

        nativeHeap.free(context.rawPtr)
        isListening = true
    }

    private fun areParametersSet(context: SCNetworkReachabilityContext): Boolean {
        if (!SCNetworkReachabilitySetCallback(reachability, callback, context.ptr)) {
            return false
        }

        if (!SCNetworkReachabilitySetDispatchQueue(reachability, dispatch_get_main_queue())) {
            return false
        }
        return true
    }

    private fun checkReachability(networkManager: NetworkManager, flags: SCNetworkReachabilityFlags) {
        println("DEBUG: in checkReachability()")
            println("DEBUG: in currentReachabilityFlags != flags")
            when (flags) {
                kSCNetworkReachabilityFlagsReachable -> {
                    println("DEBUG: in kSCNetworkReachabilityFlagsReachable")
                    if (flags == kSCNetworkReachabilityFlagsIsWWAN) {
                        println("DEBUG: in cellar case")
                        handleNetworkStateChanged(Network.Cellular())
                    } else {
                        println("DEBUG: in wifi case")
                        handleNetworkStateChanged(Network.Wifi())
                    }
                }
                else -> {
                    println("DEBUG: in absent case")
                    networkManager.handleNetworkStateChanged(Network.Absent)
                }
            }
    }
}
