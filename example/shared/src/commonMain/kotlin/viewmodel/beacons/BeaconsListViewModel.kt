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

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.beacons.Beacons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BeaconsListNavigation(bundle: NavigationBundle<BeaconDetailsSpecRow<*>>) : NavigationAction<BeaconDetailsSpecRow<*>>(bundle)

@ExperimentalStdlibApi
class BeaconsListViewModel(private val beaconz: Beacons, navigator: Navigator<BeaconsListNavigation>) : NavigatingViewModel<BeaconsListNavigation>(navigator) {

    private val _isScanning = ConflatedBroadcastChannel<Boolean>()
    val isScanning = _isScanning.toObservable(coroutineScope)

    private val _beacons = ConflatedBroadcastChannel<List<BeaconsListBeaconViewModel>>()
    val beacons = _beacons.toObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            beaconz
                .isMonitoring()
                .collect { _isScanning.send(it) }
        }

        scope.launch { beaconz.beacons()
            .map { beacons ->
                beacons.map { beacon ->
                    BeaconsListBeaconViewModel(beacon.identifier, beaconz, navigator)
                }
            }
            .collect { beacons ->
                 cleanDevices()
                _beacons.send(beacons)
            }
        }
    }

    fun onScanPressed() {
        if (_isScanning.valueOrNull == true) {
            beaconz.stopMonitoring()
        } else {
            beaconz.startMonitoring()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanDevices()
    }

    private fun cleanDevices() {
        _beacons.valueOrNull?.forEach { it.onCleared() }
    }
}
