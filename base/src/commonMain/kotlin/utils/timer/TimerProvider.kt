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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/** Provides timer instances. */
object TimerProvider {
    /**
     * @param duration timer duration
     * @param coroutineScope a parent coroutine scope for the timer
     * @param coroutineContext a coroutine context for the timer state machine
     * @return a timer based on the system clock
     */
    fun monotonic(
        duration: Duration,
        coroutineScope: CoroutineScope = MainScope(),
        coroutineContext: CoroutineContext = Dispatchers.Main.immediate
    ): Timer = MonotonicTimer(duration, coroutineScope, coroutineContext)

    /**
     * @param duration timer duration
     * @return a timer that finishes instantly
     */
    fun instant(duration: Duration): Timer = InstantTimer(duration)
}
