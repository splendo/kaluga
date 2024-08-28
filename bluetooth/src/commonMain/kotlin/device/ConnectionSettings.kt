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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.device.ConnectionSettings.ReconnectionSettings
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger

/**
 * Settings to apply to a [Device] when connecting
 * @property reconnectionSettings the [ReconnectionSettings] to apply when reconnecting
 * @property logger the [Logger] to use for logging
 */
data class ConnectionSettings(val reconnectionSettings: ReconnectionSettings = ReconnectionSettings.Always, val logger: Logger = RestrictedLogger(RestrictedLogLevel.None)) {

    /**
     * Behaviour to apply when a [Device] disconnects unexpectedly
     */
    sealed class ReconnectionSettings {
        /**
         * Should always try to reconnect when an unexpected disconnect occurs
         */
        data object Always : ReconnectionSettings()

        /**
         * Should never try to reconnect when an unexpected disconnect occurs
         */
        data object Never : ReconnectionSettings()
    }
}
