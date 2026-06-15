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

internal actual class GeolocationWatcher actual constructor() {

    private var watchId: Int? = null

    actual fun start(onReading: GeolocationCallback) {
        if (watchId != null) return
        watchId = startWatch(onReading)
    }

    actual fun stop() {
        watchId?.let { clearWatch(it) }
        watchId = null
    }

    actual fun requestOnce(onReading: GeolocationCallback) {
        requestCurrentPosition(onReading)
    }
}

private fun startWatch(onReading: GeolocationCallback): Int = js(
    """navigator.geolocation.watchPosition(function (position) {
        var c = position.coords;
        onReading(
            c.latitude, c.longitude,
            c.altitude == null ? NaN : c.altitude,
            c.accuracy == null ? NaN : c.accuracy,
            c.altitudeAccuracy == null ? NaN : c.altitudeAccuracy,
            c.speed == null ? NaN : c.speed,
            c.heading == null ? NaN : c.heading,
            position.timestamp
        );
    }, function (error) {})""",
)

private fun clearWatch(id: Int) {
    js("navigator.geolocation.clearWatch(id)")
}

private fun requestCurrentPosition(onReading: GeolocationCallback) {
    js(
        """navigator.geolocation.getCurrentPosition(function (position) {
        var c = position.coords;
        onReading(
            c.latitude, c.longitude,
            c.altitude == null ? NaN : c.altitude,
            c.accuracy == null ? NaN : c.accuracy,
            c.altitudeAccuracy == null ? NaN : c.altitudeAccuracy,
            c.speed == null ? NaN : c.speed,
            c.heading == null ? NaN : c.heading,
            position.timestamp
        );
    }, function (error) {})""",
    )
}
