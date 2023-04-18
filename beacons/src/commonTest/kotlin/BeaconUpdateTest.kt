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
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertTrue

class BeaconUpdateTest : BeaconFlowTest() {

    @Test
    fun testBeaconUpdatedTimestamp() = testWithFlow {
        test {
            assertTrue(it.isEmpty())
        }

        action {
            start()
            discoverDevices(
                BeaconMock.mockBeaconDevice("f7826da6bc5b71e0893e4e4161460111", scope)
            )
        }

        val lastSeen = DefaultKalugaDate.now()

        action {
            delay(1_000)
            discoverDevices(
                BeaconMock.mockBeaconDevice("f7826da6bc5b71e0893e4e4161460111", scope)
            )
        }

        // Skip first found beacon
        test(skip = 1) {
            // Last seen should be updated
            assertTrue {
                it.first().lastSeen.millisecondSinceEpoch > lastSeen.millisecondSinceEpoch
            }
        }
    }
}
