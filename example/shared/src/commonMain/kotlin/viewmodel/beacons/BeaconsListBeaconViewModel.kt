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

import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.beacons.BeaconState
import com.splendo.kaluga.beacons.Beacons
import com.splendo.kaluga.beacons.get
import com.splendo.kaluga.beacons.state
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.flow.map

@ExperimentalStdlibApi
class BeaconsListBeaconViewModel(identifier: Identifier, beacons: Beacons) : BaseViewModel() {

    private val beacon = beacons.beacons()[identifier]

    private fun <T> beaconStateObservable(mapper: (BeaconState) -> T): Observable<T> =
        beacon.state().map { mapper(it) }.toObservable(coroutineScope)

    val namespace = beaconStateObservable { it.beaconID.namespace }
    val instance = beaconStateObservable { it.beaconID.instance }
    val txPower = beaconStateObservable { "txPower".localized().format(it.txPower) }

    public override fun onCleared() { super.onCleared() }
}
