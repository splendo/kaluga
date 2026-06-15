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

package com.splendo.kaluga.bluetooth.beacons

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.TxPower
import com.splendo.kaluga.bluetooth.device.Identifier

/**
 * Unique identifier of a Beacon
 */
typealias BeaconID = Eddystone.UID

/**
 * Info describing a Beacon
 * @property identifier the [Identifier] of the beacon. Note this may not be unique when beacons are grouped
 * @property beaconID the [BeaconID] of the beacon
 * @property txPower the current [TxPower] of the beacon
 * @property rssi the current [RSSI] of the beacon
 * @property lastSeen the [KalugaDate] at which the beacon was last seen
 */
data class BeaconInfo(val identifier: Identifier, val beaconID: BeaconID, val txPower: TxPower, val rssi: RSSI, val lastSeen: KalugaDate)

/**
 * Returns the [kotlin.time.Duration] since the beacon was last seen
 */
val BeaconInfo.timeSinceLastSeen get() = DefaultKalugaDate.now() - lastSeen
