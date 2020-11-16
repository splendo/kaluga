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
import com.splendo.kaluga.system.network.services.NetworkManagerHandler

actual class NetworkManager(
    override val onNetworkStateChange: NetworkStateChange
) : BaseNetworkManager(onNetworkStateChange) {

    class Builder : BaseNetworkManager.Builder {
        override fun create(onNetworkStateChange: NetworkStateChange): BaseNetworkManager =
            NetworkManager(onNetworkStateChange)
    }

    private var _networkManagerHandler = AtomicReference<NetworkManagerHandler?>(null)
    private var networkManagerHandler: NetworkManagerHandler?
        get() = _networkManagerHandler.get()
        set(value) = _networkManagerHandler.set(value)

    override suspend fun startMonitoringNetwork() {
        if (IOSVersion.systemVersion > IOSVersion(12)) {
            networkManagerHandler = NetworkManagerHandler.NWPathNetworkManager(onNetworkStateChange)
            networkManagerHandler?.startNotifier()
        } else {
            networkManagerHandler = NetworkManagerHandler.SCNetworkManager(onNetworkStateChange)
            networkManagerHandler?.startNotifier()
        }
    }

    override suspend fun stopMonitoringNetwork() {
        if (IOSVersion.systemVersion >= IOSVersion(12)) {
            networkManagerHandler?.stopNotifier()
            networkManagerHandler = null
        } else {
            networkManagerHandler?.stopNotifier()
            networkManagerHandler = null
        }
    }
}
