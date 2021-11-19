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

package com.splendo.kaluga.base.utils.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
/** This timer finishes instantly. */
internal class InstantTimer(override val duration: Duration) : Timer {
    override val state: StateFlow<Timer.State> = MutableStateFlow(
        object : Timer.State.NotRunning.Finished {
            override val elapsed: Duration = duration
        }
    )

    override suspend fun start() { }
    override suspend fun pause() { }
}
