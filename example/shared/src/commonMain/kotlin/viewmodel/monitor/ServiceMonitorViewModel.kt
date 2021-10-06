/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.monitor

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.base.monitor.ServiceMonitorState
import com.splendo.kaluga.location.LocationMonitor
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ServiceMonitorViewModel(
    bltMonitorBuilder: BluetoothMonitor.Builder,
    locationMonitorBuilder: LocationMonitor.Builder
) : BaseViewModel() {

    private val bltMonitorRepo = bltMonitorBuilder.create(coroutineScope.coroutineContext)
    private val locationMonitorRepo = locationMonitorBuilder.create(coroutineScope.coroutineContext)

    val bluetoothServiceTitleText = "monitor_blt_services_title".localized()
    val locationServiceTitleText = "monitor_location_services_title".localized()


    private val _bluetoothServiceStatusText = MutableStateFlow("monitor_services_status_not_initialized".localized())
    val bluetoothServiceStatusText = _bluetoothServiceStatusText
        .toInitializedObservable(coroutineScope)

    private val _locationServiceStatusText = MutableStateFlow("monitor_services_status_not_initialized".localized())
    val locationServiceStatusText = _locationServiceStatusText
        .toInitializedObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        coroutineScope.launch {
            bltMonitorRepo.collect {
                _bluetoothServiceStatusText.value = it.toStringValues()
            }
        }

        coroutineScope.launch {
            locationMonitorRepo.collect {
                _locationServiceStatusText.value = it.toStringValues()
            }
        }
    }
}

fun ServiceMonitorState.toStringValues(): String = when (this) {
    is ServiceMonitorState.Initialized.Disabled -> "monitor_services_status_disabled".localized()
    is ServiceMonitorState.Initialized.Enabled -> "monitor_services_status_enabled".localized()
    is ServiceMonitorState.NotInitialized -> "monitor_services_status_not_initialized".localized()
    ServiceMonitorState.NotSupported -> "monitor_services_status_not_supported".localized()
}
