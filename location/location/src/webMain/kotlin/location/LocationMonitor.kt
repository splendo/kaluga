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

import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor

/**
 * A [ServiceMonitor] that monitors whether the location service is enabled, shared by the JS family.
 */
actual interface LocationMonitor : ServiceMonitor {

    /**
     * Builder for creating a [LocationMonitor]
     */
    actual class Builder {

        /**
         * Creates the [LocationMonitor]
         * @return the created [LocationMonitor]
         */
        actual fun create(): LocationMonitor = DefaultLocationMonitor()
    }
}

/**
 * Default [LocationMonitor] for the JS family. The browser exposes location whenever
 * `navigator.geolocation` is present; there is no runtime enable/disable signal to observe.
 */
class DefaultLocationMonitor :
    DefaultServiceMonitor(),
    LocationMonitor {

    override val isServiceEnabled: Boolean get() = hasGeolocation()

    override fun monitoringDidStart() {
        // `startMonitoring` already calls `updateState()`; geolocation availability does not change at runtime.
    }

    override fun monitoringDidStop() {}
}
