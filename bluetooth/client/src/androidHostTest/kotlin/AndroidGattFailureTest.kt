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

import android.bluetooth.BluetoothProfile
import com.splendo.kaluga.bluetooth.device.GattFailure
import com.splendo.kaluga.bluetooth.device.mtuFailure
import com.splendo.kaluga.bluetooth.device.readFailure
import com.splendo.kaluga.bluetooth.device.writeFailure
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class AndroidGattFailureTest {

    private companion object {
        const val GATT_ERROR = 0x85
        val grace = 100.milliseconds
    }

    @Test
    fun failureOnALinkThatIsDownIsDeviceUnavailable() {
        listOf(BluetoothProfile.STATE_DISCONNECTED, BluetoothProfile.STATE_DISCONNECTING).forEach { state ->
            val expected = GattFailure.Answer(GattResponse.DeviceUnavailable(GattResponse.ApplicationError(GATT_ERROR)))
            assertEquals(expected, readFailure(GATT_ERROR, grace) { state })
            assertEquals(expected, writeFailure(GATT_ERROR, grace) { state })
        }
    }

    @Test
    fun gattErrorOnALinkThatIsStillUpAwaitsTheDisconnect() {
        val error = GattResponse.ApplicationError(GATT_ERROR)
        val expected = GattFailure.AwaitDisconnect(error, GattResponse.DeviceUnavailable(error))
        assertEquals(expected, readFailure(GATT_ERROR, grace) { BluetoothProfile.STATE_CONNECTED })
        assertEquals(expected, writeFailure(GATT_ERROR, grace) { BluetoothProfile.STATE_CONNECTED })
        assertEquals(
            GattFailure.AwaitDisconnect(GattResponse.MTUFailure(100, error), GattResponse.MTUDeviceUnavailable(100, error)),
            mtuFailure(100, GATT_ERROR, grace) { BluetoothProfile.STATE_CONNECTED },
        )
    }

    @Test
    fun gattErrorWithoutAGraceIsThePlainError() {
        assertEquals(GattFailure.Answer(GattResponse.ApplicationError(GATT_ERROR)), readFailure(GATT_ERROR, Duration.ZERO) { BluetoothProfile.STATE_CONNECTED })
    }

    @Test
    fun otherFailureOnALinkThatStaysIsThePlainError() {
        assertEquals(GattFailure.Answer(GattResponse.ReadNotPermitted), readFailure(0x02, grace) { BluetoothProfile.STATE_CONNECTED })
        assertEquals(GattFailure.Answer(GattResponse.WriteNotPermitted), writeFailure(0x03, grace) { BluetoothProfile.STATE_CONNECTED })
        assertEquals(GattFailure.Answer(GattResponse.ApplicationError(0x80)), readFailure(0x80, grace) { BluetoothProfile.STATE_CONNECTED })
    }

    @Test
    fun failureWithoutAConnectionStateIsThePlainError() {
        assertEquals(GattFailure.Answer(GattResponse.ApplicationError(GATT_ERROR)), readFailure(GATT_ERROR, grace) { null })
        assertEquals(GattFailure.Answer(GattResponse.ApplicationError(GATT_ERROR)), writeFailure(GATT_ERROR, grace) { null })
        assertEquals(GattFailure.Answer(GattResponse.MTUFailure(100, GattResponse.ApplicationError(GATT_ERROR))), mtuFailure(100, GATT_ERROR, grace) { null })
    }

    @Test
    fun mtuRequestFailedAsTheLinkDropsIsMtuDeviceUnavailable() {
        assertEquals(
            GattFailure.Answer(GattResponse.MTUDeviceUnavailable(100, GattResponse.ApplicationError(GATT_ERROR))),
            mtuFailure(100, GATT_ERROR, grace) { BluetoothProfile.STATE_DISCONNECTED },
        )
    }
}
