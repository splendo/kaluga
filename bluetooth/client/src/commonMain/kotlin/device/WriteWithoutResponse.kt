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
 * the value must be delivered exactly once. [write] is invoked when the peripheral can accept the
 * write now ([canSendNow]) or, otherwise, once [awaitReady] reports it became ready again.
 * Returns [GattResponse.InsufficientResources] without sending if it never became ready.
 */
internal suspend fun sendWriteWithoutResponse(canSendNow: Boolean, write: () -> Unit, awaitReady: suspend () -> Boolean): GattResponse.WriteResponse = if (canSendNow) {
    write()
    GattResponse.WriteSuccess
} else if (awaitReady()) {
    write()
    GattResponse.WriteSuccess
} else {
    GattResponse.InsufficientResources
}
