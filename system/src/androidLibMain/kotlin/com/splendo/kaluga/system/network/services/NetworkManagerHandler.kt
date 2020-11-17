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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.system.network.NetworkHelper
import com.splendo.kaluga.system.network.NetworkStateChange

sealed class NetworkManagerHandler {

    abstract val handleStateChange: NetworkStateChange
    abstract val connectivityManager: ConnectivityManager

    @RequiresApi(Build.VERSION_CODES.M)
    class ConnectivityCallbackNetworkManager(
        override val handleStateChange: NetworkStateChange,
        override val connectivityManager: ConnectivityManager
    ) : NetworkManagerHandler(), NetworkHelper<ConnectivityManager.NetworkCallback> {

        override val networkHandler = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val networkType = determineNetworkType()

                handleStateChange(networkType)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                handleStateChange(com.splendo.kaluga.system.network.Network.Known.Absent)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                handleStateChange(
                    com.splendo.kaluga.system.network.Network.Unknown.WithoutLastNetwork(
                        com.splendo.kaluga.system.network.Network.Unknown.Reason.LOSING
                    )
                )

            }

            override fun onLost(network: Network) {
                super.onLost(network)
                handleStateChange(com.splendo.kaluga.system.network.Network.Known.Absent)
            }
        }

        override fun determineNetworkType(): com.splendo.kaluga.system.network.Network {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val isCellularDataEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
            val isWifiEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

            return when {
                isWifiEnabled -> {
                    com.splendo.kaluga.system.network.Network.Known.Wifi()
                }
                isCellularDataEnabled -> {
                    com.splendo.kaluga.system.network.Network.Known.Cellular()
                }
                else -> {
                    com.splendo.kaluga.system.network.Network.Known.Absent
                }
            }
        }
    }

    class ConnectivityReceiverNetworkManager(
        override val handleStateChange: NetworkStateChange,
        override val connectivityManager: ConnectivityManager
    ) : NetworkManagerHandler(), NetworkHelper<BroadcastReceiver> {

        override val networkHandler = object : BroadcastReceiver() {
            override fun onReceive(c: Context, intent: Intent) {
                debug { "DEBUG_KALUGA_SYSTEM: onReceive ConnectivityReceiver" }
                val networkInfo = connectivityManager.activeNetworkInfo
                debug { "DEBUG_KALUGA_SYSTEM: networkInfo is $networkInfo" }
                if (networkInfo?.isConnectedOrConnecting == true) {
                    val networkType = determineNetworkType()
                    handleStateChange(networkType)
                    debug { "DEBUG_KALUGA_SYSTEM: network is available from ConnectivityReceiver" }
                } else {
                    handleStateChange(com.splendo.kaluga.system.network.Network.Known.Absent)
                    debug { "DEBUG_KALUGA_SYSTEM: network is not available from ConnectivityReceiver" }
                }
            }
        }

        override fun determineNetworkType(): com.splendo.kaluga.system.network.Network {
            val isMetered = connectivityManager.isActiveNetworkMetered
            return when {
                (!isMetered && connectivityManager.isDefaultNetworkActive) -> com.splendo.kaluga.system.network.Network.Known.Wifi()
                isMetered -> com.splendo.kaluga.system.network.Network.Known.Cellular()
                else -> com.splendo.kaluga.system.network.Network.Known.Absent
            }
        }
    }
}