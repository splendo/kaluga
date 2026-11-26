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
import com.splendo.kaluga.bluetooth.device.mtuFailure
import com.splendo.kaluga.bluetooth.device.readFailure
import com.splendo.kaluga.bluetooth.device.writeFailure
import org.junit.Test
import kotlin.test.assertEquals

class AndroidGattFailureTest {

    private companion object {
        const val GATT_ERROR = 0x85
    }

    @Test
    fun failureOnALinkThatIsDownIsDeviceUnavailable() {
        listOf(BluetoothProfile.STATE_DISCONNECTED, BluetoothProfile.STATE_DISCONNECTING).forEach { state ->
            val expected = GattResponse.DeviceUnavailable(GattResponse.ApplicationError(GATT_ERROR))
            assertEquals(expected, readFailure(GATT_ERROR) { state })
            assertEquals(expected, writeFailure(GATT_ERROR) { state })
        }
    }

    @Test
    fun failureOnALinkThatStaysIsThePlainError() {
        assertEquals(GattResponse.ApplicationError(GATT_ERROR), readFailure(GATT_ERROR) { BluetoothProfile.STATE_CONNECTED })
        assertEquals(GattResponse.ReadNotPermitted, readFailure(0x02) { BluetoothProfile.STATE_CONNECTED })
        assertEquals(GattResponse.WriteNotPermitted, writeFailure(0x03) { BluetoothProfile.STATE_CONNECTED })
    }

    @Test
    fun failureWithoutAConnectionStateIsThePlainError() {
        assertEquals(GattResponse.ApplicationError(GATT_ERROR), readFailure(GATT_ERROR) { null })
        assertEquals(GattResponse.ApplicationError(GATT_ERROR), writeFailure(GATT_ERROR) { null })
        assertEquals(GattResponse.MTUFailure(100, GattResponse.ApplicationError(GATT_ERROR)), mtuFailure(100, GATT_ERROR) { null })
    }

    @Test
    fun mtuRequestFailedAsTheLinkDropsIsMtuDeviceUnavailable() {
        assertEquals(
            GattResponse.MTUDeviceUnavailable(100, GattResponse.ApplicationError(GATT_ERROR)),
            mtuFailure(100, GATT_ERROR) { BluetoothProfile.STATE_DISCONNECTED },
        )
        assertEquals(GattResponse.MTUFailure(100, GattResponse.ApplicationError(GATT_ERROR)), mtuFailure(100, GATT_ERROR) { BluetoothProfile.STATE_CONNECTED })
    }
}
