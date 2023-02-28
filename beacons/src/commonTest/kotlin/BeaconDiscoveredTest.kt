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

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BeaconDiscoveredTest : BeaconFlowTest() {

    @Test
    fun testOneDiscoveredBeacon() = testWithFlow {

        test {
            assertTrue(it.isEmpty())
        }

        action {
            start()
            discoverDevices(
                BeaconMock.mockGenericDevice("AXA", scope),
                BeaconMock.mockBeaconDevice("f7826da6bc5b71e0893e4e4161460111", scope)
            )
        }

        test {
            assertEquals(1, it.size)
            assertEquals("f7826da6bc5b71e0893e4e4161460111", it.first().beaconID.asString())
        }

        action {
            stop()
        }
    }

    @Test
    fun testTwoDiscoveredBeacons() = testWithFlow {

        test {
            assertTrue(it.isEmpty())
        }

        action {
            start()
            discoverDevices(
                BeaconMock.mockBeaconDevice("f7826da6bc5b71e0893e4e4161460111", scope)
            )
        }

        test {
            // One beacon has been found
            assertEquals(1, it.size)
        }

        action {
            discoverDevices(
                BeaconMock.mockBeaconDevice("f7826da6bc5b71e0893e4e4161460222", scope)
            )
        }

        test {
            // Two beacons have been found
            assertEquals(2, it.size)
            assertContentEquals(
                listOf("f7826da6bc5b71e0893e4e4161460111", "f7826da6bc5b71e0893e4e4161460222"),
                it.map { beacon -> beacon.beaconID.asString() }
            )
        }

        action {
            stop()
        }
    }
}
