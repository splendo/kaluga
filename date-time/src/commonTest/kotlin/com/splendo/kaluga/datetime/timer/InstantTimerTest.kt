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

package com.splendo.kaluga.datetime.timer

import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration

class InstantTimerTest {
    @Test
    fun awaitFinish(): Unit = runBlocking {
        val timer = TimerProvider.instant(duration = Duration.INFINITE)
        withTimeout(Duration.milliseconds(100)) {
            timer.awaitFinish()
        }
    }

    @Test
    fun stateTransitions(): Unit = runBlocking {
        val timer = TimerProvider.instant(duration = Duration.INFINITE)
        assertIs<Timer.State.NotRunning.Finished>(timer.state.value, "timer was not finished")
        timer.start()
        assertIs<Timer.State.NotRunning.Finished>(timer.state.value, "was able to start timer")
        timer.pause()
        assertIs<Timer.State.NotRunning.Finished>(timer.state.value, "was able to pause timer")
    }

    @Test
    fun elapsedFlow(): Unit = runBlocking {
        val duration = Duration.seconds(5)
        val timer = TimerProvider.instant(duration)

        val result = timer.elapsed().captureFor(Duration.milliseconds(500))
        assertEquals(listOf(duration), result)
    }
}
