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

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import com.splendo.kaluga.system.network.services.ConnectivityCallbackNetworkManager
import com.splendo.kaluga.system.network.services.ConnectivityReceiverNetworkManager
import com.splendo.kaluga.system.network.services.NetworkManagerService

interface NetworkHelper {
    fun determineNetworkType(): Network
}

actual class NetworkManager actual constructor(
    networkStateRepo: NetworkStateRepo,
    private val context: Any?
) : BaseNetworkManager(networkStateRepo), NetworkManagerService {

    private val connectivityManager = (context as Context).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkConnectivityCallbacks: ConnectivityCallbackNetworkManager
    private lateinit var networkConnectivityReceiver: ConnectivityReceiverNetworkManager

    override suspend fun startMonitoringNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkConnectivityCallbacks = ConnectivityCallbackNetworkManager(this, connectivityManager)
            connectivityManager.registerDefaultNetworkCallback(networkConnectivityCallbacks)
        } else {
            networkConnectivityReceiver = ConnectivityReceiverNetworkManager(this, connectivityManager)
            (context as Context).registerReceiver(networkConnectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    override suspend fun stopMonitoringNetwork() {
        if (Build.VERSION.SDK_INT >= 24) {
            connectivityManager.unregisterNetworkCallback(networkConnectivityCallbacks)
        } else {
            (context as Context).unregisterReceiver(networkConnectivityReceiver)
        }
    }

    override fun handleStateChanged(network: Network) =
        handleNetworkStateChanged(network)
}
