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

/**
 * The answer to a request the stack failed with [status]: the [GattResponse.Error] it maps to, answered through
 * [answered], or that error passed to [linkDown] when the platform no longer reports the device as connected. A failing
 * link makes the stack fail the pending request a few milliseconds before it reports the disconnect, with codes such as
 * `GATT_ERROR` (0x85) that a peripheral may also send as its own answer.
 * @param connectionState the device's GATT connection state as a [BluetoothProfile] state, or `null` if the platform
 * cannot be asked
 */
private inline fun <R : GattResponse> failure(status: Int, connectionState: () -> Int?, linkDown: (GattResponse.Error) -> R, answered: (GattResponse.Error) -> R): R {
    val error = GattResponse.Error.from(status)
    val state = connectionState() ?: return answered(error)
    return if (state == BluetoothProfile.STATE_CONNECTED) answered(error) else linkDown(error)
}

/**
 * The [GattResponse.ReadError] a read the stack failed with [status] answers, see [failure].
 */
internal fun readFailure(status: Int, connectionState: () -> Int?): GattResponse.ReadError = failure(status, connectionState, GattResponse::DeviceUnavailable) { it }

/**
 * The [GattResponse.WriteError] a write the stack failed with [status] answers, see [failure].
 */
internal fun writeFailure(status: Int, connectionState: () -> Int?): GattResponse.WriteError = failure(status, connectionState, GattResponse::DeviceUnavailable) { it }

/**
 * The [GattResponse.MTUError] a request for [mtu] the stack failed with [status] answers, see [failure].
 */
internal fun mtuFailure(mtu: MTU, status: Int, connectionState: () -> Int?): GattResponse.MTUError =
    failure(status, connectionState, { GattResponse.MTUDeviceUnavailable(mtu, it) }) { GattResponse.MTUFailure(mtu, it) }
