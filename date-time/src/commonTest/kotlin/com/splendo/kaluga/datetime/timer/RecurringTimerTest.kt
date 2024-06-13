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
package com.splendo.kaluga.datetime.timer

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.assertEmits
import com.splendo.kaluga.test.base.captureFor
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class RecurringTimerTest {

    @Test
    fun stateTransitions(): Unit = runBlocking {
        val timerScope = CoroutineScope(Dispatchers.Default)
        val timer = RecurringTimer(
            duration = 100.milliseconds,
            interval = 10.milliseconds,
            coroutineScope = timerScope,
        )
        timer.state.assertEmits("timer was not paused after creation") { it is Timer.State.NotRunning.Paused }
        timer.start()
        timer.state.assertEmits("timer is not running after start") { it is Timer.State.Running }
        timer.pause()
        timer.state.assertEmits("timer was not paused after pause") { it is Timer.State.NotRunning.Paused }
        delay(500)
        timer.state.assertEmits("timer pause is not working") { it is Timer.State.NotRunning.Paused }
        timer.start()
        timer.state.assertEmits("timer is not running after start") { it is Timer.State.Running }
        delay(500)
        timer.state.assertEmits("timer was not finished after time elapsed") { it is Timer.State.NotRunning.Finished }
        timer.start()
        timer.state.assertEmits("was able to start timer after finish") { it is Timer.State.NotRunning.Finished }
        timer.pause()
        timer.state.assertEmits("was able to pause timer after finish") { it is Timer.State.NotRunning.Finished }
        timerScope.cancel()
    }

    @Test
    fun awaitFinish(): Unit = runBlocking {
        val timerScope = CoroutineScope(Dispatchers.Default)
        val timer = RecurringTimer(
            duration = 100.milliseconds,
            interval = 10.milliseconds,
            coroutineScope = timerScope,
        )

        withTimeout(500.milliseconds) {
            timer.start()
            timer.awaitFinish()
        }
        timerScope.cancel()
    }

    @Test
    fun elapsedFlow(): Unit = runBlocking {
        val timerScope = CoroutineScope(Dispatchers.Default)
        fun List<Duration>.isAscending(): Boolean = windowed(size = 2).map { it[0] <= it[1] }.all { it }

        val duration = 500.milliseconds
        val timer = RecurringTimer(
            duration = duration,
            interval = 50.milliseconds,
            coroutineScope = timerScope,
        )

        // capture and validate an initial state
        timer.elapsed().first()
        val initial = timer.elapsed().captureFor(100.milliseconds)
        assertEquals(listOf(Duration.ZERO), initial, "timer was not started in paused state")

        // capture and validate a first chunk of data
        timer.start()
        val result0 = timer.elapsed().captureFor(200.milliseconds)
        timer.pause()
        assertTrue(result0.isNotEmpty(), "values not emitted")
        assertTrue(initial.last() <= result0.first(), "values are not in ascending order")
        assertTrue(result0.isAscending(), "values are not in ascending order")

        // capture and validate the rest of the data
        timer.start()
        val result1 = timer.elapsed().captureFor(1000.milliseconds)
        assertTrue(result1.isNotEmpty(), "values not emitted")
        assertTrue(result0.last() <= result1.first(), "values are not in ascending order")
        assertTrue(result1.isAscending(), "values are not in ascending order")

        // capture from a finished timer
        val final = timer.elapsed().captureFor(100.milliseconds)
        assertEquals(listOf(duration), final, "timer did not finish in the right state")
        assertTrue(result1.last() <= final.first(), "values are not in ascending order")
        timerScope.cancel()
    }

    // MARK - elapsedIrregularFlow test
    /** Provides mock time ticks. */
    private class PredefinedTimeSource(val ticks: List<Duration>) : TimeSource {
        override fun markNow(): TimeMark = object : TimeMark {
            var index = 0
            override fun elapsedNow(): Duration = if (index < ticks.size) {
                ticks[index].also { index++ }
            } else {
                throw IllegalStateException("Unexpected elapsedNow() call")
            }
        }
    }

    /** Validates requested delays. */
    private class PredefinedDelayHandler(val delays: List<Duration>) {
        private val timerFinish = CompletableDeferred<Unit>()
        private var index = -1 // -1 to capture overall timer finish delay

        suspend fun delay(delay: Duration) {
            if (index < 0) {
                // capture timer finish delay
                index++
                timerFinish.await()
            } else {
                if (index < delays.size) {
                    assertEquals(delays[index], delay, "Unexpected delay #$index")
                    index++
                } else {
                    throw IllegalStateException("Unexpected waitFor($delay) call")
                }
            }
        }

        fun finishTimer() {
            timerFinish.complete(Unit)
        }
    }

    @Test
    fun elapsedIrregularFlow(): Unit = runBlocking {
        val totalDuration = 500.milliseconds

        class Timings(emit: Int, afterEmit: Int, correction: Int) {
            val emit = emit.milliseconds
            val afterEmit = afterEmit.milliseconds
            val correction = correction.milliseconds
        }

        val timings = listOf(
            // 1ms delivery lag, 20ms processing lag, correction for 20ms
            Timings(1, 20, 80),
            // spot on, no correction
            Timings(100, 100, 100),
            // -1ms delivery lag, skipped a frame + 40ms in processing, correction for 40ms
            Timings(199, 350, 50),
            // -7ms delivery lag, -4ms af undershoot spot on, correction for 4ms
            Timings(393, 396, 104),
            // last interval: no delivery lag, 5 ms processing lag, no correction
            Timings(500, 505, 0),
        )

        val timeSource = PredefinedTimeSource(
            listOf(Duration.ZERO) + // a tick for the timer auto finish
                timings.flatMap { listOf(it.emit, it.afterEmit) },
        )
        val delayHandler = PredefinedDelayHandler(timings.map(Timings::correction))

        val timer = RecurringTimer(
            duration = totalDuration,
            interval = 100.milliseconds,
            timeSource = timeSource,
            delayFunction = delayHandler::delay,
            coroutineScope = this,
        )

        // capture and validate an initial state
        val initial = timer.elapsed().captureFor(200.milliseconds)
        assertEquals(listOf(Duration.ZERO), initial, "timer was not started in paused state")
        // capture and validate a first chunk of data
        timer.start()
        val result = timer.elapsed().captureFor(500.milliseconds)
        assertEquals(timings.map(Timings::emit), result, "Emitted incorrect values")

        withTimeout(500.milliseconds) {
            delayHandler.finishTimer()
            timer.awaitFinish()
        }
        // capture from a finished timer
        val final = timer.elapsed().captureFor(100.milliseconds)
        assertEquals(listOf(totalDuration), final, "timer did not finish in the right state")
    }
}
