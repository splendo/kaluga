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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.logging.debug

interface NetworkHelper {
    fun determineNetworkType(): com.splendo.kaluga.system.network.Network
}

actual class NetworkManager actual constructor(
    networkStateRepo: NetworkStateRepo,
    private val context: Any?
) : BaseNetworkManager(networkStateRepo) {

    private val connectivityManager = (context as Context).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkConnectivityCallbacks: NetworkConnectivityCallbacks
    private lateinit var networkConnectivityReceiver: NetworkConnectivityReceiver

    private var _isNetworkEnabled = AtomicReference(false)
    private var isNetworkEnabled: Boolean
        get() = _isNetworkEnabled.get()
        set(value) = _isNetworkEnabled.set(value)

    override suspend fun isNetworkEnabled(): Boolean = isNetworkEnabled

    override suspend fun startMonitoringNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            debug { "DEBUG_KALUGA_SYSTEM: subscribing to ConnectivityCallbacks" }
            networkConnectivityCallbacks = NetworkConnectivityCallbacks()
            connectivityManager.registerDefaultNetworkCallback(networkConnectivityCallbacks)
        } else {
            debug { "DEBUG_KALUGA_SYSTEM: subscribing to ConnectivityReceiver" }
            networkConnectivityReceiver = NetworkConnectivityReceiver()
            (context as Context).registerReceiver(networkConnectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    override suspend fun stopMonitoringNetwork() {
        if (Build.VERSION.SDK_INT >= 24) {
            connectivityManager.unregisterNetworkCallback(networkConnectivityCallbacks)
        } else {
            // connectivityReceiver.abortBroadcast()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private inner class NetworkConnectivityCallbacks : ConnectivityManager.NetworkCallback(), NetworkHelper {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isNetworkEnabled = true

            val networkType = determineNetworkType()
            handleNetworkStateChanged(networkType)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            isNetworkEnabled = false
            handleNetworkStateChanged(com.splendo.kaluga.system.network.Network.Absent)
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            isNetworkEnabled = false
            handleNetworkStateChanged(com.splendo.kaluga.system.network.Network.Absent)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isNetworkEnabled = false
            handleNetworkStateChanged(com.splendo.kaluga.system.network.Network.Absent)
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

    private inner class NetworkConnectivityReceiver : BroadcastReceiver(), NetworkHelper {
        override fun onReceive(c: Context, intent: Intent) {
            debug { "DEBUG_KALUGA_SYSTEM: onReceive ConnectivityReceiver" }
            val networkInfo = connectivityManager.activeNetworkInfo
            debug { "DEBUG_KALUGA_SYSTEM: networkInfo is $networkInfo" }
            if (networkInfo?.isConnectedOrConnecting == true) {
                val networkType = determineNetworkType()
                isNetworkEnabled = true
                handleNetworkStateChanged(networkType)
                debug { "DEBUG_KALUGA_SYSTEM: network is available from ConnectivityReceiver" }
            } else {
                isNetworkEnabled = false
                handleNetworkStateChanged(com.splendo.kaluga.system.network.Network.Absent)
                debug { "DEBUG_KALUGA_SYSTEM: network is not available from ConnectivityReceiver" }
            }
        }

        override fun determineNetworkType(): com.splendo.kaluga.system.network.Network {
            val isMetered = connectivityManager.isActiveNetworkMetered
            return when {
                (!isMetered && connectivityManager.isDefaultNetworkActive) -> com.splendo.kaluga.system.network.Network.Wifi()
                isMetered -> com.splendo.kaluga.system.network.Network.Cellular()
                else -> com.splendo.kaluga.system.network.Network.Absent
            }
        }
    }
}
