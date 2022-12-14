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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

actual class DefaultNetworkManager internal constructor(
    private val androidNetworkManager: AndroidNetworkManager
) : BaseNetworkManager() {

    override val network: Flow<NetworkConnectionType> get() = androidNetworkManager.network
    override suspend fun startMonitoring() = androidNetworkManager.startMonitoring()
    override suspend fun stopMonitoring() = androidNetworkManager.stopMonitoring()

    class Builder(private val context: Context) : BaseNetworkManager.Builder {
        override fun create(): BaseNetworkManager {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val androidNetworkManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                AndroidConnectivityCallbackManager(
                    connectivityManager
                )
            } else {
                @Suppress("DEPRECATION")
                AndroidConnectivityReceiverManager(
                    connectivityManager,
                    context
                )
            }
            return DefaultNetworkManager(androidNetworkManager)
        }
    }

    internal interface AndroidNetworkManager : NetworkManager

    @RequiresApi(Build.VERSION_CODES.N)
    internal class AndroidConnectivityCallbackManager(
        private val connectivityManager: ConnectivityManager
    ) : AndroidNetworkManager {

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
                        NetworkConnectionType.Unknown.Reason.LOSING
                    )
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
            val isCellularDataEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
            val isWifiEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false

            return when {
                isWifiEnabled -> {
                    NetworkConnectionType.Known.Wifi()
                }
                isCellularDataEnabled -> {
                    NetworkConnectionType.Known.Cellular()
                }
                else -> {
                    NetworkConnectionType.Known.Absent
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated on Android")
    internal class AndroidConnectivityReceiverManager(
        private val connectivityManager: ConnectivityManager,
        private val context: Context
    ) : AndroidNetworkManager {

        private val networkChannel = Channel<NetworkConnectionType>(Channel.UNLIMITED)
        override val network: Flow<NetworkConnectionType> = networkChannel.receiveAsFlow()
        private val networkHandler = object : BroadcastReceiver() {
            override fun onReceive(c: Context, intent: Intent) {
                networkChannel.trySend(determineNetworkType())
            }
        }

        override suspend fun startMonitoring() {
            context.registerReceiver(networkHandler, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            networkChannel.trySend(determineNetworkType())
        }

        override suspend fun stopMonitoring() {
            context.unregisterReceiver(networkHandler)
        }

        private fun determineNetworkType(): NetworkConnectionType {
            val isMetered = connectivityManager.isActiveNetworkMetered
            return when {
                connectivityManager.activeNetworkInfo?.isConnectedOrConnecting != true -> NetworkConnectionType.Known.Absent
                (!isMetered && connectivityManager.isDefaultNetworkActive) -> NetworkConnectionType.Known.Wifi()
                isMetered -> NetworkConnectionType.Known.Cellular()
                else -> NetworkConnectionType.Known.Absent
            }
        }
    }
}
