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

import com.splendo.kaluga.logging.RestrictedLogLevel

data class ConnectionSettings(
    val eventBufferSize: Int = BaseDeviceConnectionManager.BUFFER_CAPACITY,
    val reconnectionSettings: ReconnectionSettings = ReconnectionSettings.Always,
    val logLevel: RestrictedLogLevel = RestrictedLogLevel.None
) {

    sealed class ReconnectionSettings {
        object Always : ReconnectionSettings()
        object Never : ReconnectionSettings()
        data class Limited(val attempts: Int) : ReconnectionSettings()
    }
}
