/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.lifecycle

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A [ActivityLifecycleSubscribable] that provides [ActivityLifecycleSubscribable.manager] as a [StateFlow]
 */
class LifecycleManagerObserver : DefaultActivityLifecycleSubscribable() {

    override var manager: ActivityLifecycleSubscribable.LifecycleManager?
        get() = super.manager
        set(value) {
            super.manager = value
            _managerState.value = value
        }

    private val _managerState = MutableStateFlow<ActivityLifecycleSubscribable.LifecycleManager?>(null)

    /**
     * A [StateFlow] of the current [manager]
     */
    val managerState: StateFlow<ActivityLifecycleSubscribable.LifecycleManager?> = _managerState
}
