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
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.system.network.Network
import com.splendo.kaluga.system.network.NetworkHelper

class ConnectivityReceiverNetworkManager(
    private val networkManagerService: NetworkManagerService,
    private val connectivityManager: ConnectivityManager
) : BroadcastReceiver(), NetworkHelper {

    override fun onReceive(c: Context, intent: Intent) {
        debug { "DEBUG_KALUGA_SYSTEM: onReceive ConnectivityReceiver" }
        val networkInfo = connectivityManager.activeNetworkInfo
        debug { "DEBUG_KALUGA_SYSTEM: networkInfo is $networkInfo" }
        if (networkInfo?.isConnectedOrConnecting == true) {
            val networkType = determineNetworkType()
            networkManagerService.handleStateChanged(networkType)
            debug { "DEBUG_KALUGA_SYSTEM: network is available from ConnectivityReceiver" }
        } else {
            networkManagerService.handleStateChanged(Network.Known.Absent)
            debug { "DEBUG_KALUGA_SYSTEM: network is not available from ConnectivityReceiver" }
        }
    }

    override fun determineNetworkType(): Network {
        val isMetered = connectivityManager.isActiveNetworkMetered
        return when {
            (!isMetered && connectivityManager.isDefaultNetworkActive) -> Network.Known.Wifi()
            isMetered -> Network.Known.Cellular()
            else -> Network.Known.Absent
        }
    }
}