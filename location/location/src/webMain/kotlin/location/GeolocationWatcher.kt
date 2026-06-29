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

package com.splendo.kaluga.location

internal fun hasGeolocation(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.geolocation")

/**
 * A reading delivered by `navigator.geolocation.watchPosition`. Optional fields the browser omits
 * are passed as `NaN` (so the callback stays a flat list of primitives that marshals across the JS
 * family without external-interface interop) and mapped back to `null` by the caller.
 */
internal typealias GeolocationCallback = (
    latitude: Double,
    longitude: Double,
    altitude: Double,
    horizontalAccuracy: Double,
    verticalAccuracy: Double,
    speed: Double,
    course: Double,
    timestampMillis: Double,
) -> Unit

/**
 * Wraps the browser `navigator.geolocation` API. The actual lives per-target because passing the
 * Kotlin callback into the `js(...)` success handler differs between js and wasmJs.
 */
internal expect class GeolocationWatcher() {
    /** Starts continuous updates via `watchPosition`, delivering each reading to [onReading]. */
    fun start(onReading: GeolocationCallback)

    /** Stops the continuous updates started by [start]. */
    fun stop()

    /**
     * Fires a one-shot `getCurrentPosition`, delivering the reading to [onReading]. This is also what
     * surfaces the browser permission prompt, so it doubles as the "enable location" action.
     */
    fun requestOnce(onReading: GeolocationCallback)
}
