/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Default implementation of [NetworkManager]
 */
actual class DefaultNetworkManager internal constructor(private val androidNetworkManager: AndroidNetworkManager) : NetworkManager {

    actual override val network: Flow<NetworkConnectionType> get() = androidNetworkManager.network
    actual override suspend fun startMonitoring() = androidNetworkManager.startMonitoring()
    actual override suspend fun stopMonitoring() = androidNetworkManager.stopMonitoring()

    /**
     * Builder for creating a [DefaultNetworkManager]
     * @param context the [Context] in which to monitor the network
     */
    class Builder(private val context: Context) : NetworkManager.Builder {
        override fun create(): NetworkManager {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val androidNetworkManager =
                AndroidConnectivityCallbackManager(
                    connectivityManager,
                )
            return DefaultNetworkManager(androidNetworkManager)
        }
    }

    internal interface AndroidNetworkManager : NetworkManager

    internal class AndroidConnectivityCallbackManager(private val connectivityManager: ConnectivityManager) : AndroidNetworkManager {

        private val networkChannel = Channel<NetworkConnectionType>(Channel.UNLIMITED)
        override val network: Flow<NetworkConnectionType> = networkChannel.receiveAsFlow()
        private val networkHandler = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                val networkType = determineNetworkType()

                networkChannel.trySend(networkType)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                networkChannel.trySend(NetworkConnectionType.Known.Absent)
            }

            override fun onLosing(network: android.net.Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                networkChannel.trySend(
                    NetworkConnectionType.Unknown.WithoutLastNetwork(
                        NetworkConnectionType.Unknown.Reason.LOSING,
                    ),
                )
            }

            override fun onLost(network: android.net.Network) {
                super.onLost(network)
                networkChannel.trySend(NetworkConnectionType.Known.Absent)
            }
        }

        override suspend fun startMonitoring() {
            connectivityManager.registerDefaultNetworkCallback(networkHandler)
        }

        override suspend fun stopMonitoring() {
            connectivityManager.unregisterNetworkCallback(networkHandler)
        }

        private fun determineNetworkType(): NetworkConnectionType {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return when {
                capabilities == null -> NetworkConnectionType.Known.Absent
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    NetworkConnectionType.Known.Wifi(!capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED))
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnectionType.Known.Cellular
                else -> NetworkConnectionType.Known.Absent
            }
        }
    }
}
