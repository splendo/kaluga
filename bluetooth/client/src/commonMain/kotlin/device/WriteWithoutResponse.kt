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

/**
 * Send policy for a write that has no completion callback (the Apple write-without-response path):
 * the value is sent exactly once. [write] runs when the peripheral can accept the write now
 * ([canSendNow]) or once [awaitReady] reports it became ready — both yield [GattResponse.WriteSuccess.Ready].
 * If the peripheral never became ready (e.g. [awaitReady] timed out) the write is still attempted
 * best-effort and [GattResponse.WriteSuccess.NotReady] is returned, since it may have been dropped.
 */
internal suspend fun sendWriteWithoutResponse(canSendNow: Boolean, write: () -> Unit, awaitReady: suspend () -> Boolean): GattResponse.WriteResponse = if (canSendNow) {
    write()
    GattResponse.WriteSuccess.Ready
} else if (awaitReady()) {
    write()
    GattResponse.WriteSuccess.Ready
} else {
    write()
    GattResponse.WriteSuccess.NotReady
}
