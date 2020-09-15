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
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.ConnectivityManager.EXTRA_NETWORK_INFO
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

actual class Network(
    private val context: Context,
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    private val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityCallbacks: ConnectivityCallbacks
    private lateinit var connectivityReceiver: ConnectivityReceiver

    /**
     * Subscribe to Network.
     */
    actual fun subscribe() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            debug { "DEBUG_KALUGA_SYSTEM: subscribing to ConnectivityCallbacks" }
            connectivityCallbacks = ConnectivityCallbacks()
            connectivityManager.registerDefaultNetworkCallback(connectivityCallbacks)
        } else {
            debug { "DEBUG_KALUGA_SYSTEM: subscribing to ConnectivityReceiver" }
            connectivityReceiver = ConnectivityReceiver()
            context.registerReceiver(connectivityReceiver, IntentFilter(CONNECTIVITY_ACTION))
        }
    }

    /**
     * Unsubscribe from Network.
     */
    actual fun unsubscribe() {
        if (Build.VERSION.SDK_INT >= 24) {
            connectivityManager.unregisterNetworkCallback(connectivityCallbacks)
        }
    }

    private val _isConnectivityAvailable = HotFlowable<NetworkState?>(null)

    /**
     * Returns true when the device is connected to a network and it has connection,
     * otherwise false when the device is connected to a network but has no connection.
     *
     * @return A boolean value that identify the availability of connection.
     */
    actual val isConnectivityAvailable: Flow<Boolean>
        get() {
            return if (Build.VERSION.SDK_INT >= 24) {
                _isConnectivityAvailable.flow().map {
                    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    val isWifiEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
                    val isDataEnabled = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
                    println("Network Availability isWifiEnabled ($isWifiEnabled) || isDataEnabled ($isDataEnabled)")
                    isWifiEnabled || isDataEnabled
                }
            } else {
                _isConnectivityAvailable.flow().map { it == NetworkState.Available }
            }
        }

    inner class ConnectivityCallbacks : NetworkCallback() {
        override fun onAvailable(network: Network) {
            launch {
                debug { "DEBUG_KALUGA_SYSTEM: network is available from ConnectivityCallbacks" }

                _isConnectivityAvailable.set(NetworkState.Available)
            }
        }

        override fun onUnavailable() {
            launch {
                debug { "DEBUG_KALUGA_SYSTEM: network is unavailable from ConnectivityCallbacks" }
                _isConnectivityAvailable.set(NetworkState.Unavailable)
            }
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            launch {
                debug { "DEBUG_KALUGA_SYSTEM: network is losing from ConnectivityCallbacks" }

                _isConnectivityAvailable.set(NetworkState.Losing)
            }
        }

        override fun onLost(network: Network) {
            launch {
                debug { "DEBUG_KALUGA_SYSTEM: network is Lost from ConnectivityCallbacks" }
                _isConnectivityAvailable.set(NetworkState.Lost)
            }
        }
    }

    private inner class ConnectivityReceiver : BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            launch {
                debug { "DEBUG_KALUGA_SYSTEM: onReceive ConnectivityReceiver" }
                val networkInfo = connectivityManager.activeNetworkInfo
                debug { "DEBUG_KALUGA_SYSTEM: networkInfo is $networkInfo" }
                val fallbackNetworkInfo: NetworkInfo? = intent.getParcelableExtra(EXTRA_NETWORK_INFO)
                if (networkInfo?.isConnectedOrConnecting == true) {
                    _isConnectivityAvailable.set(NetworkState.Available)
                    debug { "DEBUG_KALUGA_SYSTEM: network is available from ConnectivityReceiver" }
                } else {
                    _isConnectivityAvailable.set(NetworkState.Unavailable)
                    debug { "DEBUG_KALUGA_SYSTEM: network is not available from ConnectivityReceiver" }
                }
            }
        }
    }
}
