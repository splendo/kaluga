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

import com.splendo.kaluga.bluetooth.device.readFailure
import com.splendo.kaluga.bluetooth.device.writeFailure
import platform.CoreBluetooth.CBATTErrorDomain
import platform.CoreBluetooth.CBATTErrorInsufficientEncryption
import platform.CoreBluetooth.CBATTErrorReadNotPermitted
import platform.CoreBluetooth.CBErrorConnectionFailed
import platform.CoreBluetooth.CBErrorConnectionTimeout
import platform.CoreBluetooth.CBErrorDomain
import platform.CoreBluetooth.CBErrorInvalidHandle
import platform.CoreBluetooth.CBErrorInvalidParameters
import platform.CoreBluetooth.CBErrorNotConnected
import platform.CoreBluetooth.CBErrorOperationCancelled
import platform.CoreBluetooth.CBErrorOperationNotSupported
import platform.CoreBluetooth.CBErrorOutOfSpace
import platform.CoreBluetooth.CBErrorPeripheralDisconnected
import platform.CoreBluetooth.CBErrorUnknown
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorDomain
import kotlin.test.Test
import kotlin.test.assertEquals

class CoreBluetoothFailureTest {

    @Test
    fun attErrorIsThePeripheralsAnswer() {
        assertBoth(GattResponse.ReadNotPermitted, NSError.errorWithDomain(CBATTErrorDomain, CBATTErrorReadNotPermitted, null))
        assertBoth(GattResponse.InsufficientEncryption, NSError.errorWithDomain(CBATTErrorDomain, CBATTErrorInsufficientEncryption, null))
        assertBoth(GattResponse.ApplicationError(0x85), NSError.errorWithDomain(CBATTErrorDomain, 0x85, null))
        assertBoth(GattResponse.UnknownError(0), NSError.errorWithDomain(CBATTErrorDomain, 0, null))
    }

    @Test
    fun platformErrorOfALostLinkIsDeviceUnavailable() {
        listOf(CBErrorNotConnected, CBErrorConnectionTimeout, CBErrorPeripheralDisconnected, CBErrorConnectionFailed).forEach { code ->
            val error = NSError.errorWithDomain(CBErrorDomain, code, null)
            assertBoth(GattResponse.DeviceUnavailable(GattResponse.UnknownError(error.gattCode)), error)
        }
    }

    // CBError codes overlap ATT codes, such as CBErrorPeripheralDisconnected with InvalidOffset, and CBErrorUnknown is
    // the code of ATT success.
    @Test
    fun otherPlatformErrorIsNotReadAsAnAttError() {
        assertBoth(GattResponse.InvalidHandle, NSError.errorWithDomain(CBErrorDomain, CBErrorInvalidHandle, null))
        assertBoth(GattResponse.InsufficientResources, NSError.errorWithDomain(CBErrorDomain, CBErrorOutOfSpace, null))
        assertBoth(GattResponse.RequestNotSupported, NSError.errorWithDomain(CBErrorDomain, CBErrorOperationNotSupported, null))
        listOf(CBErrorUnknown, CBErrorInvalidParameters, CBErrorOperationCancelled).forEach { code ->
            val error = NSError.errorWithDomain(CBErrorDomain, code, null)
            assertBoth(GattResponse.UnknownError(error.gattCode), error)
        }
    }

    @Test
    fun errorOfAnotherDomainIsUnknown() {
        assertBoth(GattResponse.UnknownError(7), NSError.errorWithDomain(NSURLErrorDomain, 7, null))
    }

    private fun assertBoth(expected: GattResponse, error: NSError) {
        assertEquals(expected, error.readFailure(), "read failed with $error")
        assertEquals(expected, error.writeFailure(), "write failed with $error")
    }
}
