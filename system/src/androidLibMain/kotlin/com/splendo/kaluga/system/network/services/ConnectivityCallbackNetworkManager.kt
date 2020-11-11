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

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.splendo.kaluga.system.network.NetworkHelper

@RequiresApi(Build.VERSION_CODES.M)
class ConnectivityCallbackNetworkManager(
    private val networkManagerService: NetworkManagerService,
    private val connectivityManager: ConnectivityManager
) : ConnectivityManager.NetworkCallback(), NetworkHelper {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkManagerService.setIsNetworkEnabled(true)

        val networkType = determineNetworkType()
        networkManagerService.handleStateChanged(networkType)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        networkManagerService.setIsNetworkEnabled(false)
        networkManagerService.handleStateChanged(com.splendo.kaluga.system.network.Network.Absent)
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        networkManagerService.setIsNetworkEnabled(false)
        networkManagerService.handleStateChanged(com.splendo.kaluga.system.network.Network.Absent)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkManagerService.setIsNetworkEnabled(false)
        networkManagerService.handleStateChanged(com.splendo.kaluga.system.network.Network.Absent)
    }

    override fun determineNetworkType(): com.splendo.kaluga.system.network.Network {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isCellularDataEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
        val isWifiEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

        return when {
            isWifiEnabled -> {
                com.splendo.kaluga.system.network.Network.Wifi()
            }
            isCellularDataEnabled -> {
                com.splendo.kaluga.system.network.Network.Cellular()
            }
            else -> {
                com.splendo.kaluga.system.network.Network.Absent
            }
        }
    }
}