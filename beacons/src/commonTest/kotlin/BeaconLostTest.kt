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

import BeaconMock.mockBeaconDevice
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BeaconLostTest : BeaconFlowTest(timeoutMs = 2_000) {

    @Test
    fun testLostOnTimeout() = testWithFlow {

        test {
            assertTrue(it.isEmpty())
        }

        action {
            start(MainScope())
            discoverDevices(
                mockBeaconDevice("f7826da6bc5b71e0893e4e4161460111", scope.coroutineContext)
            )
        }

        test {
            // One beacon has been found
            assertEquals(1, it.size)
        }

        action {
            delay(3_000)
        }

        test {
            // None beacons found
            assertTrue(it.isEmpty())
        }
    }

    @Test
    fun testLostOneByOneTimeout() = testWithFlow {

        test {
            assertTrue(it.isEmpty())
        }

        action {
            start(scope)
            discoverDevices(
                mockBeaconDevice("f7826da6bc5b71e0893e4e4161460111", scope.coroutineContext)
            )
        }

        test {
            // One beacon has been found
            assertEquals(1, it.size)
        }

        action {
            delay(1_000)
            discoverDevices(
                mockBeaconDevice("f7826da6bc5b71e0893e4e4161460222", scope.coroutineContext)
            )
        }

        test {
            // Two beacons have been found
            assertEquals(2, it.size)
        }

        action {
            delay(2_500)
        }

        test {
            // One beacon has been found
            assertEquals(1, it.size)
            // Last found cached
            assertEquals("f7826da6bc5b71e0893e4e4161460222", it.first().identifier)
        }

        action {
            delay(2_500)
        }

        test {
            // None beacons found
            assertTrue(it.isEmpty())
        }
    }
}
