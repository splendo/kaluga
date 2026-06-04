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

// Geolocation has no programmatic request; calling `getCurrentPosition` is what triggers the browser prompt.
internal fun triggerGeolocationPrompt() {
    js("navigator.geolocation.getCurrentPosition(function(){}, function(){})")
}

internal fun hasGeolocation(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.geolocation")

/**
 * The current geolocation permission state — `"granted"`, `"denied"` or `"prompt"`.
 *
 * Backed by the async `navigator.permissions.query`, whose `Promise<PermissionStatus>` can't satisfy
 * Kotlin/Wasm's `JsAny` bound from shared code, so the query lives in the per-target source sets.
 */
internal expect suspend fun queryGeolocationPermissionState(): String
