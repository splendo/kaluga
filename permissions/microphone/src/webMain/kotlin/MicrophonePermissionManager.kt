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

package com.splendo.kaluga.permissions.microphone

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

internal fun hasMediaDevices(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.mediaDevices && !!navigator.mediaDevices.getUserMedia")

// The browser only prompts for microphone access when a stream is opened; the tracks are stopped right
// away so the recording indicator does not stay lit. Fire-and-forget — the monitor picks up the result.
private fun requestMicrophonePermission() {
    js("navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) { stream.getTracks().forEach(function (track) { track.stop(); }); }).catch(function () {})")
}

/**
 * The current microphone permission state — `"granted"`, `"denied"` or `"prompt"`. Backed by the async
 * `navigator.permissions.query`, which lives per-target (its `Promise` can't satisfy Kotlin/Wasm's
 * `JsAny` bound from shared code).
 */
internal expect suspend fun queryMicrophonePermissionState(): String

/**
 * The default [BasePermissionManager] for [MicrophonePermission] on the JS family (js + wasmJs).
 *
 * State comes from the [W3C Permissions API](https://developer.mozilla.org/en-US/docs/Web/API/Permissions)
 * (`navigator.permissions.query({ name: 'microphone' })`), polled on the monitoring interval; the prompt
 * is surfaced by opening a `getUserMedia` audio stream.
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultMicrophonePermissionManager(settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<MicrophonePermission>(MicrophonePermission, settings, coroutineScope) {

    private var monitoringJob: Job? = null
    private var lastState: String? = null

    actual override fun requestPermissionDidStart() {
        if (hasMediaDevices()) {
            requestMicrophonePermission()
        } else {
            emitEvent(PermissionManager.Event.PermissionDenied(locked = true))
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        if (monitoringJob != null) return
        monitoringJob = launch {
            while (isActive) {
                emitForState(queryMicrophonePermissionState())
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
 * A [BaseMicrophonePermissionManagerBuilder] for the JS family. The [context] is unused — the browser
 * has no ambient permission context.
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class MicrophonePermissionManagerBuilder actual constructor(context: PermissionContext) : BaseMicrophonePermissionManagerBuilder {
    actual override fun create(settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): MicrophonePermissionManager =
        DefaultMicrophonePermissionManager(settings, coroutineScope)
}
