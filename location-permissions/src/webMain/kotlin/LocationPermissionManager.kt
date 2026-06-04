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

package com.splendo.kaluga.permissions.location

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
 * The default [BasePermissionManager] for [LocationPermission] on the JS family (js + wasmJs).
 *
 * Backed by the [W3C Permissions API](https://developer.mozilla.org/en-US/docs/Web/API/Permissions):
 * `navigator.permissions.query({ name: 'geolocation' })` is polled on the monitoring interval (the
 * `onchange` callback can't be marshaled cleanly across the JS family). The browser exposes no
 * programmatic permission request, so [requestPermissionDidStart] triggers the prompt by issuing a
 * one-shot `getCurrentPosition`. `background`/`precise` distinctions don't exist on the web.
 * @param locationPermission the [LocationPermission] to manage
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultLocationPermissionManager(locationPermission: LocationPermission, settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<LocationPermission>(locationPermission, settings, coroutineScope) {

    private var monitoringJob: Job? = null
    private var lastState: String? = null

    // A grant obtained through the prompt; remembered so a browser that keeps reporting "prompt" after a
    // one-time "Allow" (e.g. Firefox) does not downgrade the permission back to requestable.
    private var grantedViaRequest = false

    actual override fun requestPermissionDidStart() {
        if (hasGeolocation()) {
            requestGeolocationPermission {
                grantedViaRequest = true
                emitForState("granted")
            }
        } else {
            emitEvent(PermissionManager.Event.PermissionDenied(locked = true))
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        if (monitoringJob != null) return
        monitoringJob = launch {
            while (isActive) {
                emitForState(queryGeolocationPermissionState())
                delay(interval)
            }
        }
    }

    actual override fun monitoringDidStop() {
        monitoringJob?.cancel()
        monitoringJob = null
        lastState = null
        grantedViaRequest = false
    }

    private fun emitForState(state: String) {
        val resolved = if (state == "prompt" && grantedViaRequest) "granted" else state
        if (resolved == lastState) return
        lastState = resolved
        when (resolved) {
            "granted" -> emitEvent(PermissionManager.Event.PermissionGranted)

            // Once a browser denies geolocation it cannot be re-prompted programmatically, so it is locked.
            "denied" -> {
                grantedViaRequest = false
                emitEvent(PermissionManager.Event.PermissionDenied(locked = true))
            }

            else -> emitEvent(PermissionManager.Event.PermissionDenied(locked = false)) // "prompt" — not yet granted, still requestable
        }
    }
}

/**
 * A [BaseLocationPermissionManagerBuilder] for the JS family. The [context] is unused — the browser
 * has no ambient permission context.
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class LocationPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseLocationPermissionManagerBuilder {
    actual override fun create(locationPermission: LocationPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): LocationPermissionManager =
        DefaultLocationPermissionManager(locationPermission, settings, coroutineScope)
}
