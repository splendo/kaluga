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

package com.splendo.kaluga.example.shared.viewmodel.beacons

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.bluetooth.beacons.Beacons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BeaconsListViewModel :
    BaseLifecycleViewModel(),
    KoinComponent {

    private val service: Beacons by inject()

    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.toInitializedObservable(coroutineScope)

    private val _beacons = MutableStateFlow<List<BeaconsListBeaconViewModel>>(emptyList())
    val beacons = _beacons.toInitializedObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            service
                .isMonitoring()
                .collect { _isScanning.value = it }
        }

        scope.launch {
            service.beacons
                .map { beacons ->
                    beacons.map { beacon ->
                        BeaconsListBeaconViewModel(beacon.identifier, service)
                    }
                }
                .collect { beacons ->
                    cleanDevices()
                    _beacons.value = beacons
                }
        }
    }

    fun onScanPressed() {
        coroutineScope.launch {
            if (_isScanning.value) {
                service.stopMonitoring()
            } else {
                service.startMonitoring()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanDevices()
    }

    private fun cleanDevices() {
        _beacons.value.forEach { it.onCleared() }
    }
}
