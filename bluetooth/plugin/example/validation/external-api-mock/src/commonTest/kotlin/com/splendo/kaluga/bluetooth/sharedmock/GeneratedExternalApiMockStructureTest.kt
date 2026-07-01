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

package com.splendo.kaluga.bluetooth.sharedmock

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.verify
import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.sharedcontract.SharedCharacteristicReadResponse
import com.splendo.kaluga.bluetooth.sharedcontract.SharedDeviceClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.withTimeout

// Proves the recommended split: the API lives in :validation:contract (apiOnly) and the MOCK variant is generated here
// against that imported API (useExternalApi). The generated mocks implement the contract interfaces, so they are usable
// as test doubles in any module that consumes the contract.
class GeneratedExternalApiMockStructureTest : BaseTest() {

    @Test
    fun mockClientStubsAndVerifiesThroughTheExternalApi() = testRunBlocking {
        val client = SharedDeviceClient.mock()

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
}
