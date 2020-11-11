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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.system.network.Network
import platform.Network.nw_interface_type_cellular
import platform.Network.nw_interface_type_wifi
import platform.Network.nw_path_get_status
import platform.Network.nw_path_is_expensive
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_monitor_t
import platform.Network.nw_path_monitor_update_handler_t
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_status_unsatisfied
import platform.Network.nw_path_t
import platform.Network.nw_path_uses_interface_type

class NWPathNetworkManager(
    private val networkManagerService: NetworkManagerService
) : NetworkManagerHandler {

    private var _nwPathMonitor = AtomicReference<nw_path_monitor_t>(null)
    private var nwPathMonitor: nw_path_monitor_t
        get() = _nwPathMonitor.get()
        set(value) = _nwPathMonitor.set(value)

    private val onNetworkStateChanged = object : nw_path_monitor_update_handler_t {
        override fun invoke(network: nw_path_t) {
            checkReachability(network)
        }
    }

    override fun startNotifier() {
        nwPathMonitor = nw_path_monitor_create()
        nw_path_monitor_set_update_handler(nwPathMonitor, onNetworkStateChanged)
        nw_path_monitor_start(nwPathMonitor)
    }

    override fun stopNotifier() {
        nwPathMonitor = null
    }

    private fun checkReachability(network: nw_path_t) {
        when (nw_path_get_status(network)) {
            nw_path_status_satisfied -> {
                if (nw_path_uses_interface_type(network, nw_interface_type_wifi)) {
                    if(nw_path_is_expensive(network)) {
                        // connected to hotspot
                        networkManagerService.handleStateChanged(Network.Wifi(isExpensive = true))
                    } else {
                        networkManagerService.handleStateChanged(Network.Wifi())
                    }
                }
                else if (nw_path_uses_interface_type(network, nw_interface_type_cellular)) {
                    networkManagerService.handleStateChanged(Network.Cellular())
                }
            }
            nw_path_status_unsatisfied -> {
                networkManagerService.handleStateChanged(Network.Absent)
            }
        }
    }
}