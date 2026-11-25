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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.NetsimPeripheral.Command
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

/**
 * The peripheral answers reads and writes with an ATT error, and the client hands it back as a [GattResponse].
 */
class NetsimErrorResponseTest : NetsimTest() {

    @Test
    fun readNotPermitted() = answersWith(0x02, GattResponse.ReadNotPermitted)

    @Test
    fun writeNotPermitted() = answersWith(0x03, GattResponse.WriteNotPermitted)

    // Android answers this by encrypting the link and retrying, which needs pairing. The peripheral refuses to pair,
    // and the read fails with Android's own GATT_AUTH_FAIL instead of the code the peripheral sent.
    @Test
    fun insufficientAuthenticationOnARead() = netsimTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.AnswerWithError(0x05))
        assertEquals<GattResponse>(GattResponse.ApplicationError(0x89), within(20.seconds, "read") { attributes.error.read() })
        assertEquals<GattResponse>(GattResponse.ReadSuccess(NetsimPeripheral.initialValue), within(10.seconds, "read after the error") { attributes.data.read() })
    }

    // On a write Android tries to pair the same way, but after the refusal it never answers the write: it drops the
    // idle link about three seconds later, and the disconnect answers the write.
    @Test
    fun insufficientAuthenticationOnAWrite() = netsimTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.AnswerWithError(0x05))
        assertEquals<GattResponse>(GattResponse.DeviceUnavailable, within(20.seconds, "write") { attributes.error.write(byteArrayOf(1)) })
        awaitDisconnected()
    }

    @Test
    fun invalidAttributeValueLength() = answersWith(0x0D, GattResponse.InvalidAttributeValueLength)

    @Test
    fun applicationError() = answersWith(0x80, GattResponse.ApplicationError(0x80))

    // The same value as Android's own GATT_ERROR, which it also reports when it fails a request itself.
    @Test
    fun applicationErrorWithTheCodeOfAnAndroidGattError() = answersWith(0x85, GattResponse.ApplicationError(0x85))

    private fun answersWith(code: Int, expected: GattResponse.Error) = netsimTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.AnswerWithError(code))
        assertEquals<GattResponse>(expected, within(40.seconds, "read") { attributes.error.read() })
        assertEquals<GattResponse>(expected, within(40.seconds, "write") { attributes.error.write(byteArrayOf(1)) })
        // An error answer completes the action, so the queue goes on.
        assertEquals<GattResponse>(GattResponse.ReadSuccess(NetsimPeripheral.initialValue), within(10.seconds, "read after the errors") { attributes.data.read() })
    }
}
