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

package com.splendo.kaluga.permissions.camera

import kotlinx.coroutines.await
import kotlin.js.Promise

private external interface PermissionStatus {
    val state: String
}

private fun hasPermissionsApi(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.permissions && typeof navigator.permissions.query === 'function'")

private fun queryCamera(): Promise<PermissionStatus> = js("navigator.permissions.query({ name: 'camera' })")

internal actual suspend fun queryCameraPermissionState(): String = if (hasPermissionsApi()) {
    try {
        queryCamera().await().state
    } catch (_: Throwable) {
        if (hasMediaDevices()) "prompt" else "denied"
    }
} else {
    if (hasMediaDevices()) "prompt" else "denied"
}
