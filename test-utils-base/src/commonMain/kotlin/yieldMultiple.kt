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

package com.splendo.kaluga.test.base

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

const val DEFAULT_MULTIPLE_TIMES_YIELDS = 10

/**
 * Calls yield for a number of times
 * @param times the number of times to call yield. [DEFAULT_MULTIPLE_TIMES_YIELDS] by default
 */
suspend fun yieldMultiple(times: Int = DEFAULT_MULTIPLE_TIMES_YIELDS) = repeat(times) {
    yield()
}

/**
 * Calls yield until a constraint is met. After yielding a number of times there is a delay to prevent maxing out the CPU.
 * @param timeout The duration after which to stop
 * @param timesPerTurn The number of times to call yield before enacting a delay. [DEFAULT_MULTIPLE_TIMES_YIELDS] by default.
 * @param delayPerTurn The delay enacted after yielding a number of times as defined by [timesPerTurn].
 * @param constraint The constraint to check for.
 */
suspend fun yieldUntil(
    timeout: Duration = Duration.INFINITE,
    timesPerTurn: Int = DEFAULT_MULTIPLE_TIMES_YIELDS,
    delayPerTurn: Duration = 10.milliseconds,
    constraint: () -> Boolean,
) = withTimeout(timeout) {
    while (!constraint()) {
        yieldMultiple(times = timesPerTurn)
        if (!constraint()) {
            delay(delayPerTurn)
        } else {
            return@withTimeout // fast exit to avoid extra constraint check
        }
    }
}
