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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.base.test.testRunBlocking
import com.splendo.kaluga.bluetooth.GattResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class WriteWithoutResponseTest {

    @Test
    fun sendsOnceAsReadyWhenReady() = testRunBlocking {
        var writes = 0
        val response = sendWriteWithoutResponse(canSendNow = true, write = { writes++ }, awaitReady = { error("must not wait when ready") })
        assertEquals(1, writes)
        assertIs<GattResponse.WriteSuccess.Ready>(response)
    }

    @Test
    fun sendsOnceAsReadyWhenNotReadyThenBecomesReady() = testRunBlocking {
        var writes = 0
        val response = sendWriteWithoutResponse(canSendNow = false, write = { writes++ }, awaitReady = { true })
        assertEquals(1, writes)
        assertIs<GattResponse.WriteSuccess.Ready>(response)
    }

    @Test
    fun sendsOnceBestEffortAsNotReadyWhenNeverReady() = testRunBlocking {
        var writes = 0
        val response = sendWriteWithoutResponse(canSendNow = false, write = { writes++ }, awaitReady = { false })
        assertEquals(1, writes)
        assertIs<GattResponse.WriteSuccess.NotReady>(response)
    }
}
