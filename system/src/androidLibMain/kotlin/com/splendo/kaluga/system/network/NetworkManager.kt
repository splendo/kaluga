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
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.system.network.services.ConnectivityCallbackNetworkManager
import com.splendo.kaluga.system.network.services.ConnectivityReceiverNetworkManager

interface NetworkHelper {
    fun determineNetworkType(): Network
}

actual class NetworkManager (
    private val context: Context,
    override val onNetworkStateChange: NetworkStateChange
) : BaseNetworkManager(onNetworkStateChange) {

    class Builder(private val context: Context = ApplicationHolder.applicationContext) : BaseNetworkManager.Builder {
        override fun create(onNetworkStateChange: NetworkStateChange): BaseNetworkManager =
            NetworkManager(context, onNetworkStateChange)
    }

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkConnectivityCallbacks: ConnectivityCallbackNetworkManager
    private lateinit var networkConnectivityReceiver: ConnectivityReceiverNetworkManager

    override suspend fun startMonitoringNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkConnectivityCallbacks = ConnectivityCallbackNetworkManager(onNetworkStateChange, connectivityManager)
            connectivityManager.registerDefaultNetworkCallback(networkConnectivityCallbacks)
        } else {
            networkConnectivityReceiver = ConnectivityReceiverNetworkManager(onNetworkStateChange, connectivityManager)
            context.registerReceiver(networkConnectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    override suspend fun stopMonitoringNetwork() {
        if (Build.VERSION.SDK_INT >= 24) {
            connectivityManager.unregisterNetworkCallback(networkConnectivityCallbacks)
        } else {
            context.unregisterReceiver(networkConnectivityReceiver)
        }
    }
}
