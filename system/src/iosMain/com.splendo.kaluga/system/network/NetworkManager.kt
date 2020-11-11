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
import com.splendo.kaluga.base.IOSVersion
import com.splendo.kaluga.system.network.services.NetworkManagerService
import com.splendo.kaluga.system.network.services.SCNetworkManager
import platform.SystemConfiguration.SCNetworkReachabilityFlags
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsWWAN
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable

actual class NetworkManager actual constructor(
    networkStateRepo: NetworkStateRepo,
    context: Any?
) : BaseNetworkManager(networkStateRepo), NetworkManagerService {

    private var _isListening = AtomicReference(false)
    private var isListening: Boolean
        get() = _isListening.get()
        set(value) = _isListening.set(value)

    private var _isNetworkEnabled = AtomicReference(false)
    private var isNetworkEnabled: Boolean
        get() = _isNetworkEnabled.get()
        set(value) = _isNetworkEnabled.set(value)

    private var _scNetworkManager = AtomicReference<SCNetworkManager?>(null)
    private var scNetworkManager: SCNetworkManager?
        get() = _scNetworkManager.get()
        set(value) = _scNetworkManager.set(value)

    override suspend fun isNetworkEnabled(): Boolean = isNetworkEnabled

    override suspend fun startMonitoringNetwork() {
        if (IOSVersion.systemVersion > IOSVersion(12)) {

        } else {
            scNetworkManager = SCNetworkManager(this)
            scNetworkManager?.startNotifier()
        }
    }

    override suspend fun stopMonitoringNetwork() {
        if (IOSVersion.systemVersion > IOSVersion(12)) {

        } else {
            scNetworkManager = null
            scNetworkManager?.stopNotifier()
        }
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

    override fun setIsListening(newValue: Boolean) {
        isListening = newValue
    }

    override fun setIsNetworkEnabled(newValue: Boolean) {
        isNetworkEnabled = newValue
    }

    override fun getIsListening(): Boolean = isListening

    override fun getIsNetworkEnabled(): Boolean = isNetworkEnabled

    override fun handleStateChanged(network: Network) = handleNetworkStateChanged(network)
}
