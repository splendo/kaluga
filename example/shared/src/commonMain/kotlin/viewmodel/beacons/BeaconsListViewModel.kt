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

package com.splendo.kaluga.example.shared.viewmodel.beacons

import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.beacons.BeaconService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ExperimentalStdlibApi
class BeaconsListViewModel(private val service: BeaconService) : BaseViewModel() {

    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.toObservable(coroutineScope)

    private val _beacons = MutableStateFlow<List<BeaconsListBeaconViewModel>>(emptyList())
    val beacons = _beacons.toObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            service
                .isMonitoring()
                .collect { _isScanning.value = true }
        }

        scope.launch { service.beacons()
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
        if (_isScanning.value) {
            service.stopMonitoring()
        } else {
            service.startMonitoring()
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
