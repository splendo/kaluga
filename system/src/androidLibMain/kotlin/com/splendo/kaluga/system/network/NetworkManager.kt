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
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.system.network.services.NetworkManagerHandler

interface NetworkHelper<T> {
    fun determineNetworkType(): Network
    val networkHandler: T
}

actual class NetworkManager (
    private val context: Context,
    override val onNetworkStateChange: NetworkStateChange
) : BaseNetworkManager(onNetworkStateChange) {

    class Builder(private val context: Context = ApplicationHolder.applicationContext) : BaseNetworkManager.Builder {
        override fun create(onNetworkStateChange: NetworkStateChange): BaseNetworkManager {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                NetworkManager(context, onNetworkStateChange)
            } else {
                NetworkManager(context, onNetworkStateChange)
            }
        }

    }

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkManagerHandler: NetworkManagerHandler

    override suspend fun startMonitoringNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkManagerHandler = NetworkManagerHandler.ConnectivityCallbackNetworkManager(onNetworkStateChange, connectivityManager)
            connectivityManager.registerDefaultNetworkCallback(
                (networkManagerHandler as NetworkHelper<ConnectivityManager.NetworkCallback>).networkHandler
            )
        } else {
            networkManagerHandler = NetworkManagerHandler.ConnectivityReceiverNetworkManager(onNetworkStateChange, connectivityManager)
            context.registerReceiver((networkManagerHandler as NetworkHelper<BroadcastReceiver>).networkHandler, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    override suspend fun stopMonitoringNetwork() {
        if (Build.VERSION.SDK_INT >= 24) {
            connectivityManager.unregisterNetworkCallback((networkManagerHandler as NetworkHelper<ConnectivityManager.NetworkCallback>).networkHandler)
        } else {
            context.unregisterReceiver((networkManagerHandler as NetworkHelper<BroadcastReceiver>).networkHandler)
        }
    }
}
