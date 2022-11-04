/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

fun NetworkManager(context: Context, onNetworkStateChange: NetworkStateChange): NetworkManager {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val androidNetworkManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        NetworkManager.AndroidConnectivityCallbackManager(
            connectivityManager,
            onNetworkStateChange
        )
    } else {
        @Suppress("DEPRECATION")
        NetworkManager.AndroidConnectivityReceiverManager(
            connectivityManager,
            onNetworkStateChange,
            context
        )
    }
    return NetworkManager(androidNetworkManager)
}

actual class NetworkManager internal constructor(
    androidNetworkManager: AndroidNetworkManager
) : BaseNetworkManager by androidNetworkManager {

    internal interface AndroidNetworkManager : BaseNetworkManager

    @RequiresApi(Build.VERSION_CODES.N)
    internal class AndroidConnectivityCallbackManager(
        private val connectivityManager: ConnectivityManager,
        override val onNetworkStateChange: NetworkStateChange
    ) : AndroidNetworkManager {

        private val networkHandler = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                val networkType = determineNetworkType()

                onNetworkStateChange(networkType)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                onNetworkStateChange(Network.Known.Absent)
            }

            override fun onLosing(network: android.net.Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                onNetworkStateChange(
                    Network.Unknown.WithoutLastNetwork(
                        Network.Unknown.Reason.LOSING
                    )
                )
            }

            override fun onLost(network: android.net.Network) {
                super.onLost(network)
                onNetworkStateChange(Network.Known.Absent)
            }
        }

        init {
            connectivityManager.registerDefaultNetworkCallback(networkHandler)
        }

        private fun determineNetworkType(): Network {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val isCellularDataEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
            val isWifiEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

            return when {
                isWifiEnabled -> {
                    Network.Known.Wifi()
                }
                isCellularDataEnabled -> {
                    Network.Known.Cellular()
                }
                else -> {
                    Network.Known.Absent
                }
            }
        }

        override fun dispose() {
            connectivityManager.unregisterNetworkCallback(networkHandler)
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated on Android")
    internal class AndroidConnectivityReceiverManager(
        private val connectivityManager: ConnectivityManager,
        override val onNetworkStateChange: NetworkStateChange,
        private val context: Context
    ) : AndroidNetworkManager {

        private val networkHandler = object : BroadcastReceiver() {
            override fun onReceive(c: Context, intent: Intent) {
                val networkInfo = connectivityManager.activeNetworkInfo
                if (networkInfo?.isConnectedOrConnecting == true) {
                    val networkType = determineNetworkType()
                    onNetworkStateChange(networkType)
                } else {
                    onNetworkStateChange(Network.Known.Absent)
                }
            }
        }

        init {
            context.registerReceiver(networkHandler, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }

        private fun determineNetworkType(): Network {
            val isMetered = connectivityManager.isActiveNetworkMetered
            return when {
                (!isMetered && connectivityManager.isDefaultNetworkActive) -> Network.Known.Wifi()
                isMetered -> Network.Known.Cellular()
                else -> Network.Known.Absent
            }
        }

        override fun dispose() {
            context.unregisterReceiver(networkHandler)
        }
    }
}
