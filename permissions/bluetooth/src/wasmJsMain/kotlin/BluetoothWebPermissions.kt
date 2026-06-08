/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions.bluetooth

import kotlinx.coroutines.await
import kotlin.js.Promise

private external interface PermissionStatus : JsAny {
    val state: String
}

private fun hasPermissionsApi(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.permissions && typeof navigator.permissions.query === 'function'")

private fun hasWebBluetooth(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.bluetooth")

private fun queryBluetooth(): Promise<PermissionStatus> = js("navigator.permissions.query({ name: 'bluetooth' })")

// Browsers without the (Chromium-only) bluetooth descriptor throw; fall back to requestable when Web
// Bluetooth exists at all, since the grant is obtained through the device picker.
internal actual suspend fun queryBluetoothPermissionState(): String = if (hasPermissionsApi()) {
    try {
        queryBluetooth().await().state
    } catch (_: Throwable) {
        if (hasWebBluetooth()) "prompt" else "denied"
    }
} else {
    if (hasWebBluetooth()) "prompt" else "denied"
}
