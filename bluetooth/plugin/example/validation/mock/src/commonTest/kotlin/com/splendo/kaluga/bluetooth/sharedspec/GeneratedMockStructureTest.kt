/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.sharedspec

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.verify
import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.GattResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.withTimeout

// Exercises the generated MOCK variant: stubs the leaf function members on the nested mock characteristic, calls through
// the generated client/server API, asserts the stubbed results and verifies the recorded calls.
class GeneratedMockStructureTest : BaseTest() {

    @Test
    fun mockClientStubsAndVerifiesThroughTheGeneratedApi() = testRunBlocking {
        val client = SharedDeviceClient.mock()

        // Reach the leaf characteristic mock through the generated mock tree (nested properties are typed as their mocks).
        val characteristic = client.sharedService.sharedCharacteristic
        characteristic.readLevelMock.on().doReturn(SharedCharacteristicReadResponse.Success(42))
        characteristic.writeTargetMock.on().doExecuteSuspended { GattResponse.WriteSuccess.Acknowledged }

        withTimeout(5.seconds) {
            val read = client.sharedService.sharedCharacteristic.readLevel()
            assertIs<SharedCharacteristicReadResponse.Success>(read)
            assertEquals(42, read.response)

            val write = client.sharedService.sharedCharacteristic.writeTarget(7)
            assertIs<GattResponse.WriteSuccess>(write)
        }

        characteristic.readLevelMock.verify()
        characteristic.writeTargetMock.verify(eq(7))
    }

    @Test
    fun mockServerRecordsAndVerifiesNotifyAllStateChanged() = testRunBlocking {
        val server = MockSharedDeviceServer()

        val characteristic = server.sharedService.sharedCharacteristic
        characteristic.notifyAllStateChangedMock.on().doReturn(true)

        withTimeout(5.seconds) {
            assertEquals(true, server.sharedService.sharedCharacteristic.notifyAllStateChanged(3))
        }

        characteristic.notifyAllStateChangedMock.verify(eq(3.toShort()))
    }
}
