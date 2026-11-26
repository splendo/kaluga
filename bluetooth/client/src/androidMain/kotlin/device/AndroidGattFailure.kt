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

import android.bluetooth.BluetoothProfile
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.MTU
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * The stack's own generic failure. Android fails every request pending on a link that goes down with it, a few
 * milliseconds before it reports the disconnect, and uses it for other failures of its own, such as a malformed
 * response. A peripheral may also answer a request with application error 0x85.
 */
private const val GATT_ERROR = 0x85

internal val defaultGattErrorDisconnectGrace = 100.milliseconds

/**
 * How a request the stack failed with an error status is answered.
 */
internal sealed interface GattFailure<out R : GattResponse> {

    /** Answered with [response] at once. */
    data class Answer<out R : GattResponse>(val response: R) : GattFailure<R>

    /**
     * Held until the device disconnects, answering [linkDown], or until the grace period has passed, answering [error].
     */
    data class AwaitDisconnect<out R : GattResponse>(val error: R, val linkDown: R) : GattFailure<R>
}

/**
 * The answer to a request the stack failed with [status]: the [GattResponse.Error] it maps to, answered through
 * [answered], or that error passed to [linkDown] when the platform no longer reports the device as connected. The stack
 * fails a request pending on a failing link with `GATT_ERROR` (0x85) before the platform reports the link as down,
 * so while it still reports the device as connected a request failed with that status waits for the disconnect, for
 * [disconnectGrace]. The other statuses on a request callback are the peripheral's ATT errors and failures the stack does
 * not report for a link going down, so they answer at once.
 * @param connectionState the device's GATT connection state as a [BluetoothProfile] state, or `null` if the platform
 * cannot be asked
 */
private inline fun <R : GattResponse> failure(
    status: Int,
    disconnectGrace: Duration,
    connectionState: () -> Int?,
    linkDown: (GattResponse.Error) -> R,
    answered: (GattResponse.Error) -> R,
): GattFailure<R> {
    val error = GattResponse.Error.from(status)
    val state = connectionState() ?: return GattFailure.Answer(answered(error))
    return when {
        state != BluetoothProfile.STATE_CONNECTED -> GattFailure.Answer(linkDown(error))
        status == GATT_ERROR && disconnectGrace > Duration.ZERO -> GattFailure.AwaitDisconnect(answered(error), linkDown(error))
        else -> GattFailure.Answer(answered(error))
    }
}

/**
 * How a read the stack failed with [status] is answered, see [failure].
 */
internal fun readFailure(status: Int, disconnectGrace: Duration, connectionState: () -> Int?): GattFailure<GattResponse.ReadError> =
    failure(status, disconnectGrace, connectionState, GattResponse::DeviceUnavailable) { it }

/**
 * How a write the stack failed with [status] is answered, see [failure].
 */
internal fun writeFailure(status: Int, disconnectGrace: Duration, connectionState: () -> Int?): GattFailure<GattResponse.WriteError> =
    failure(status, disconnectGrace, connectionState, GattResponse::DeviceUnavailable) { it }

/**
 * How a request for [mtu] the stack failed with [status] is answered, see [failure].
 */
internal fun mtuFailure(mtu: MTU, status: Int, disconnectGrace: Duration, connectionState: () -> Int?): GattFailure<GattResponse.MTUError> =
    failure(status, disconnectGrace, connectionState, { GattResponse.MTUDeviceUnavailable(mtu, it) }) { GattResponse.MTUFailure(mtu, it) }
