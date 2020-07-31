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

package com.splendo.kaluga.beacons

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Identifier

actual class Beacon(actual var beaconID: BeaconID, actual var txPower: Int) {
    actual companion object {
        actual fun init(identifier: Identifier, serviceData: ServiceData): Beacon? {
            val data = serviceData[UUID.UUIDWithString(Eddystone.ServiceUUID)]
            if (data != null && data.size == 18) {
                return Beacon(Eddystone.UID("ns", "123"), -12)
            }
            return null
        }
    }
}
