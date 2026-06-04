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

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * The current Web Bluetooth permission state — `"granted"`, `"denied"` or `"prompt"`. Backed by the
 * async `navigator.permissions.query({ name: 'bluetooth' })`, which lives per-target (its `Promise`
 * can't satisfy Kotlin/Wasm's `JsAny` bound from shared code).
 */
internal expect suspend fun queryBluetoothPermissionState(): String

/**
 * The default [BasePermissionManager] for [BluetoothPermission] on the JS family (js + wasmJs).
 *
 * Backed by the [Web Bluetooth](https://developer.mozilla.org/en-US/docs/Web/API/Web_Bluetooth_API)
 * permission state from the [Permissions API](https://developer.mozilla.org/en-US/docs/Web/API/Permissions),
 * polled on the monitoring interval. There is no programmatic permission request on the web — access is
 * granted per-device through `navigator.bluetooth.requestDevice` (a user-gesture device picker driven by
 * the scanner), so [requestPermissionDidStart] is a no-op. The peripheral/server role does not exist on
 * the web, so [BluetoothPermission.Type.Server] is always reported denied.
 * @param bluetoothPermission the [BluetoothPermission] to manage
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultBluetoothPermissionManager(bluetoothPermission: BluetoothPermission, settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<BluetoothPermission>(bluetoothPermission, settings, coroutineScope) {

    private val isServer = permission.type is BluetoothPermission.Type.Server
    private var monitoringJob: Job? = null
    private var lastState: String? = null

    actual override fun requestPermissionDidStart() {
        // No programmatic request on the web; the grant is obtained per-device via the scanner's picker.
    }

    actual override fun monitoringDidStart(interval: Duration) {
        if (monitoringJob != null) return
        if (isServer) {
            // No peripheral/GATT-server role exists in Web Bluetooth.
            emitEvent(PermissionManager.Event.PermissionDenied(locked = true))
            return
        }
        monitoringJob = launch {
            while (isActive) {
                emitForState(queryBluetoothPermissionState())
                delay(interval)
            }
        }
    }

    actual override fun monitoringDidStop() {
        monitoringJob?.cancel()
        monitoringJob = null
        lastState = null
    }

    private fun emitForState(state: String) {
        if (state == lastState) return
        lastState = state
        when (state) {
            // A browser "denied" cannot be re-prompted programmatically, so treat it as locked.
            "denied" -> emitEvent(PermissionManager.Event.PermissionDenied(locked = true))

            // "granted" and "prompt" both mean Bluetooth is usable: there is no upfront permission on the
            // web, access is granted per-device when the user picks one through the scanner's picker.
            else -> emitEvent(PermissionManager.Event.PermissionGranted)
        }
    }
}

/**
 * A [BaseBluetoothPermissionManagerBuilder] for the JS family. The [context] is unused — the browser
 * has no ambient permission context.
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class BluetoothPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseBluetoothPermissionManagerBuilder {
    actual override fun create(bluetoothPermission: BluetoothPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): BluetoothPermissionManager =
        DefaultBluetoothPermissionManager(bluetoothPermission, settings, coroutineScope)
}
