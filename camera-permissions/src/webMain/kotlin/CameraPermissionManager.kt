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

internal fun hasMediaDevices(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.mediaDevices && !!navigator.mediaDevices.getUserMedia")

// The browser only prompts for camera access when a stream is opened; the tracks are stopped right
// away so the camera indicator does not stay lit. Fire-and-forget — the monitor picks up the result.
private fun requestCameraPermission() {
    js("navigator.mediaDevices.getUserMedia({ video: true }).then(function (stream) { stream.getTracks().forEach(function (track) { track.stop(); }); }).catch(function () {})")
}

/**
 * The current camera permission state — `"granted"`, `"denied"` or `"prompt"`. Backed by the async
 * `navigator.permissions.query`, which lives per-target (its `Promise` can't satisfy Kotlin/Wasm's
 * `JsAny` bound from shared code).
 */
internal expect suspend fun queryCameraPermissionState(): String

/**
 * The default [BasePermissionManager] for [CameraPermission] on the JS family (js + wasmJs).
 *
 * State comes from the [W3C Permissions API](https://developer.mozilla.org/en-US/docs/Web/API/Permissions)
 * (`navigator.permissions.query({ name: 'camera' })`), polled on the monitoring interval; the prompt is
 * surfaced by opening a `getUserMedia` video stream.
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultCameraPermissionManager(settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<CameraPermission>(CameraPermission, settings, coroutineScope) {

    private var monitoringJob: Job? = null
    private var lastState: String? = null

    actual override fun requestPermissionDidStart() {
        if (hasMediaDevices()) {
            requestCameraPermission()
        } else {
            emitEvent(PermissionManager.Event.PermissionDenied(locked = true))
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        if (monitoringJob != null) return
        monitoringJob = launch {
            while (isActive) {
                emitForState(queryCameraPermissionState())
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
            "granted" -> emitEvent(PermissionManager.Event.PermissionGranted)

            // A browser "denied" cannot be re-prompted programmatically, so treat it as locked.
            "denied" -> emitEvent(PermissionManager.Event.PermissionDenied(locked = true))

            else -> emitEvent(PermissionManager.Event.PermissionDenied(locked = false)) // "prompt" — still requestable
        }
    }
}

/**
 * A [BaseCameraPermissionManagerBuilder] for the JS family. The [context] is unused — the browser has
 * no ambient permission context.
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class CameraPermissionManagerBuilder actual constructor(context: PermissionContext) : BaseCameraPermissionManagerBuilder {
    actual override fun create(settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): CameraPermissionManager =
        DefaultCameraPermissionManager(settings, coroutineScope)
}
