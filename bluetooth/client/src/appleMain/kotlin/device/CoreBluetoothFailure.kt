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

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.gattCode
import platform.CoreBluetooth.CBATTErrorDomain
import platform.CoreBluetooth.CBErrorDomain
import platform.Foundation.NSError

// Codes of CBError in CBError.h. The CBError constants are NSInteger, which appleMain cannot reference (see AppleSeams).
private const val CB_ERROR_INVALID_HANDLE = 2
private const val CB_ERROR_NOT_CONNECTED = 3
private const val CB_ERROR_OUT_OF_SPACE = 4
private const val CB_ERROR_CONNECTION_TIMEOUT = 6
private const val CB_ERROR_PERIPHERAL_DISCONNECTED = 7
private const val CB_ERROR_CONNECTION_FAILED = 10
private const val CB_ERROR_OPERATION_NOT_SUPPORTED = 13

/**
 * The answer to a request CoreBluetooth failed with this error. A [CBATTErrorDomain] error is the peripheral's ATT
 * answer and maps to the [GattResponse.Error] of its code. A [CBErrorDomain] error comes from CoreBluetooth itself: one
 * saying the link is gone answers [linkLost], the others the [GattResponse.Error] closest to them, or
 * [GattResponse.UnknownError] with their code. An error of any other domain answers [GattResponse.UnknownError].
 */
private inline fun <R : GattResponse> NSError.failure(linkLost: (GattResponse.Error) -> R, answered: (GattResponse.Error) -> R): R {
    val code = gattCode
    return when (domain) {
        CBATTErrorDomain -> answered(if (code == 0) GattResponse.UnknownError(code) else GattResponse.Error.from(code))

        CBErrorDomain -> when (code) {
            CB_ERROR_NOT_CONNECTED,
            CB_ERROR_CONNECTION_TIMEOUT,
            CB_ERROR_PERIPHERAL_DISCONNECTED,
            CB_ERROR_CONNECTION_FAILED,
            -> linkLost(GattResponse.UnknownError(code))

            CB_ERROR_INVALID_HANDLE -> answered(GattResponse.InvalidHandle)

            CB_ERROR_OUT_OF_SPACE -> answered(GattResponse.InsufficientResources)

            CB_ERROR_OPERATION_NOT_SUPPORTED -> answered(GattResponse.RequestNotSupported)

            else -> answered(GattResponse.UnknownError(code))
        }

        else -> answered(GattResponse.UnknownError(code))
    }
}

/**
 * The [GattResponse.ReadError] a read CoreBluetooth failed with this error answers, see [failure].
 */
internal fun NSError.readFailure(): GattResponse.ReadError = failure(GattResponse::DeviceUnavailable) { it }

/**
 * The [GattResponse.WriteError] a write CoreBluetooth failed with this error answers, see [failure].
 */
internal fun NSError.writeFailure(): GattResponse.WriteError = failure(GattResponse::DeviceUnavailable) { it }
