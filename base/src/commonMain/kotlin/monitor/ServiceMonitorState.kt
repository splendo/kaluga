/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.monitor

import com.splendo.kaluga.base.flow.SpecialFlowValue
import com.splendo.kaluga.state.State

/**
 * State for [DefaultServiceMonitor].
 */
sealed class ServiceMonitorState : State() {

    /**
     * Whether the status is on/off.
     * At this point all system callbacks are registered and repo is listening for changes.
     */
    sealed class Initialized : ServiceMonitorState() {

        fun deinitialize(): suspend () -> NotInitialized {
            return { NotInitialized }
        }

        object Enabled : Initialized()
        object Disabled : Initialized()

        /**
         * Describe when system's service is on, but permission for that haven't been granted yet.
         */
        object Unauthorized: Initialized()
    }

    /**
     * First and last state when system callbacks are unregistered and repo scope is cancelled.
     */
    object NotInitialized : ServiceMonitorState(), SpecialFlowValue.NotImportant

    /**
     * When the platform where the module is used is uncapable of providing this info,
     * an example is when [BluetoothMonitor] runs on simulators.
     */
    object NotSupported : ServiceMonitorState()
}
