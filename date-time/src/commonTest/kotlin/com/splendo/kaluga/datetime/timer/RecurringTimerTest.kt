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
import com.splendo.kaluga.test.captureFor
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration

class RecurringTimerTest {

    @Test
    fun stateTransitions(): Unit = runBlocking {
        val timer = RecurringTimer(
            duration = Duration.milliseconds(100),
            interval = Duration.milliseconds(10)
        )
        assertIs<Timer.State.NotRunning.Paused>(timer.state.value, "timer was not paused after creation")
        timer.start()
        assertIs<Timer.State.Running>(timer.state.value, "timer is not running after start")
        timer.pause()
        assertIs<Timer.State.NotRunning.Paused>(timer.state.value, "timer was not paused after pause")
        delay(200)
        assertIs<Timer.State.NotRunning.Paused>(timer.state.value, "timer pause is not working")
        timer.start()
        assertIs<Timer.State.Running>(timer.state.value, "timer is not running after start")
        delay(200)
        assertIs<Timer.State.NotRunning.Finished>(timer.state.value, "timer was not finished after time elapsed")
        timer.start()
        assertIs<Timer.State.NotRunning.Finished>(timer.state.value, "was able to start timer after finish")
        timer.pause()
        assertIs<Timer.State.NotRunning.Finished>(timer.state.value, "was able to pause timer after finish")
    }

    @Test
    fun awaitFinish(): Unit = runBlocking {
        val timer = RecurringTimer(
            duration = Duration.milliseconds(100),
            interval = Duration.milliseconds(10)
        )

        withTimeout(Duration.milliseconds(500)) {
            timer.start()
            timer.awaitFinish()
        }
    }

    @Test
    fun elapsedFlow(): Unit = runBlocking {
        fun List<Duration>.isAscending(): Boolean =
            windowed(size = 2).map { it[0] <= it[1] }.all { it }

        val duration = Duration.milliseconds(1000)
        val timer = RecurringTimer(
            duration = duration,
            interval = Duration.milliseconds(50)
        )

        // capture and validate an initial state
        val initial = timer.elapsed().captureFor(Duration.milliseconds(100))
        assertEquals(listOf(Duration.ZERO), initial, "timer was not started in paused state")

        // capture and validate a first chunk of data
        timer.start()
        val result0 = timer.elapsed().captureFor(Duration.milliseconds(200))
        timer.pause()
        assertTrue(result0.isNotEmpty(), "values not emitted")
        assertTrue(initial.last() <= result0.first(), "values are not in ascending order")
        assertTrue(result0.isAscending(), "values are not in ascending order")

        // capture and validate the rest of the data
        timer.start()
        val result1 = timer.elapsed().captureFor(Duration.milliseconds(1000))
        assertTrue(result1.isNotEmpty(), "values not emitted")
        assertTrue(result0.last() <= result1.first(), "values are not in ascending order")
        assertTrue(result1.isAscending(), "values are not in ascending order")

        // capture from a finished timer
        val final = timer.elapsed().captureFor(Duration.milliseconds(100))
        assertEquals(listOf(duration), final, "timer did not finish in the right state")
        assertTrue(result1.last() <= final.first(), "values are not in ascending order")
    }
}
