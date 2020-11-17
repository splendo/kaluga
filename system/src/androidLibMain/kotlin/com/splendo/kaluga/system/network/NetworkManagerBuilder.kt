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
import android.net.ConnectivityManager
import android.os.Build
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.system.network.services.ConnectivityCallbackNetworkManager
import com.splendo.kaluga.system.network.services.ConnectivityReceiverNetworkManager

actual class NetworkManagerBuilder(
    private val context: Context = ApplicationHolder.applicationContext
) : BaseNetworkManager.Builder {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun create(onNetworkStateChange: NetworkStateChange): BaseNetworkManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityCallbackNetworkManager(onNetworkStateChange, connectivityManager)
        } else {
            ConnectivityReceiverNetworkManager(onNetworkStateChange, connectivityManager, context)
        }
    }
}