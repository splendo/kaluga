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

package utils.timer

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.timer.Timer
import com.splendo.kaluga.base.utils.timer.TimerProvider
import com.splendo.kaluga.base.utils.timer.awaitFinish
import com.splendo.kaluga.base.utils.timer.elapsed
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class TimerTest {
    @Test
    fun testInstantTimerAwaitFinish(): Unit = runBlocking {
        val timer = TimerProvider.instant(duration = Duration.INFINITE)
        withTimeout(Duration.milliseconds(100)) {
            timer.awaitFinish()
        }
    }

    @Test
    fun testInstantTimerStates() = runBlocking {
        val timer = TimerProvider.instant(duration = Duration.INFINITE)
        assertTrue(timer.state.value is Timer.State.NotRunning.Finished, "timer was not finished")
        timer.start()
        assertTrue(timer.state.value is Timer.State.NotRunning.Finished, "was able to start timer")
        timer.pause()
        assertTrue(timer.state.value is Timer.State.NotRunning.Finished, "was able to pause timer")
    }

    @Test
    fun testInstantTimerElapsedFlow() = runBlocking {
        val duration = Duration.seconds(5)
        val timer = TimerProvider.instant(duration)

        val result = timer.elapsed().captureFor(Duration.milliseconds(1000))
        assertEquals(listOf(duration), result)
    }

    private suspend fun <T> Flow<T>.captureFor(duration: Duration): List<T> {
        val output = mutableListOf<T>()

        try {
            withTimeout(duration) {
                collect { output += it }
            }
        } catch (e: TimeoutCancellationException) {
            // ignore
        }
        return output
    }

    @Test
    fun testMonotonicTimerStates() = runBlocking {
        val timer = TimerProvider.monotonic(Duration.milliseconds(100))
        assertTrue(timer.state.value is Timer.State.NotRunning.Paused, "timer was not paused after creation" )
        timer.start()
        assertTrue(timer.state.value is Timer.State.Running, "timer is not running after start")
        timer.pause()
        assertTrue(timer.state.value is Timer.State.NotRunning.Paused, "timer was not paused after pause")
        delay(200)
        assertTrue(timer.state.value is Timer.State.NotRunning.Paused, "timer pause is not working")
        timer.start()
        assertTrue(timer.state.value is Timer.State.Running, "timer is not running after start")
        delay(200)
        assertTrue(timer.state.value is Timer.State.NotRunning.Finished, "timer was not finished after time elapsed")
        timer.start()
        assertTrue(timer.state.value is Timer.State.NotRunning.Finished, "was able to start timer after finish")
        timer.pause()
        assertTrue(timer.state.value is Timer.State.NotRunning.Finished, "was able to pause timer after finish")
    }

    @Test
    fun testMonotonicTimerAwaitFinish(): Unit = runBlocking {
        val timer = TimerProvider.monotonic(duration = Duration.milliseconds(100))

        withTimeout(Duration.milliseconds(200)) {
            timer.start()
            timer.awaitFinish()
        }
    }

    @Test
    fun testMonotonicTimerElapsedFlow() = runBlocking {
        fun List<Duration>.isAscending(): Boolean =
            windowed(size = 2).map { it[0] <= it[1] }.all { it }

        val duration = Duration.milliseconds(1000)
        val timer = TimerProvider.monotonic(duration)

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