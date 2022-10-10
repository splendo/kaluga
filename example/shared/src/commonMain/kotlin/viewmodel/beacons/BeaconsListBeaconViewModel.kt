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

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.bluetooth.beacons.BeaconInfo
import com.splendo.kaluga.bluetooth.beacons.Beacons
import com.splendo.kaluga.bluetooth.beacons.get
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.flow.map

class BeaconsListBeaconViewModel(identifier: Identifier, service: Beacons) : BaseLifecycleViewModel() {

    private val beacon = service.beacons[identifier]

    private fun <T> beaconInfoObservable(mapper: (BeaconInfo?) -> T) = beacon
        .map { mapper(it) }
        .toInitializedObservable(null, coroutineScope)

    val namespace = beaconInfoObservable { it?.beaconID?.namespace ?: "" }
    val instance = beaconInfoObservable { it?.beaconID?.instance ?: "" }
    val txPower = beaconInfoObservable {
        it?.txPower?.let { txPower ->
            "txPower".localized().format(txPower)
        } ?: ""
    }

    public override fun onCleared() = super.onCleared()
}
