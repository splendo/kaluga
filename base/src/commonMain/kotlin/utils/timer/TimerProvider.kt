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

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/** Provides timer instances. */
@ExperimentalTime
object TimerProvider {
    /**
     * @param duration timer duration
     * @return a timer based on the system clock. */
    fun monotonic(
        duration: Duration,
        coroutineContext: CoroutineContext = Dispatchers.Main.immediate
    ): Timer = MonotonicTimer(duration, coroutineContext)

    fun instant(duration: Duration): Timer = InstantTimer(duration)
}