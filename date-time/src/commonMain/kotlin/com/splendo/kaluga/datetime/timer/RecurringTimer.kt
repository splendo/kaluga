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

import com.splendo.kaluga.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.state.HotStateFlowRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.TimeSource
import com.splendo.kaluga.state.State as KalugaState

/**
 * Timer based on the system clock.
 * @param duration timer duration
 * @param interval timer tick interval
 * @param coroutineScope a parent coroutine scope for the timer
 */
class RecurringTimer(
    override val duration: Duration,
    interval: Duration,
    coroutineScope: CoroutineScope = MainScope()
) : Timer {

    private val stateRepo = TimerStateRepo(duration, interval, coroutineScope)
    override val state: StateFlow<Timer.State> = stateRepo.stateFlow

    override suspend fun start() = stateRepo.start()

    override suspend fun pause() = stateRepo.pause()
}

/** Timer state machine. */
private class TimerStateRepo(
    totalDuration: Duration,
    private val interval: Duration,
    private val coroutineScope: CoroutineScope
) : HotStateFlowRepo<TimerStateRepo.State>(
    coroutineScope.coroutineContext,
    {
        State.NotRunning.Paused(elapsedSoFar = Duration.ZERO, totalDuration = totalDuration)
    }
) {
    suspend fun start() {
        withContext(coroutineScope.coroutineContext) {
            takeAndChangeState { state ->
                when (state) {
                    is State.NotRunning.Paused -> suspend {
                        state.start(interval, coroutineScope, ::finish)
                    }
                    is State.NotRunning.Finished, is State.Running -> state.remain()
                }
            }
        }
    }

    suspend fun pause() {
        withContext(coroutineScope.coroutineContext) {
            takeAndChangeState { state ->
                when (state) {
                    is State.Running -> state::pause
                    is State.NotRunning -> state.remain()
                }
            }
        }
    }

    private suspend fun finish() {
        withContext(coroutineScope.coroutineContext) {
            takeAndChangeState { state ->
                when (state) {
                    is State.NotRunning.Finished -> state.remain()
                    is State.Running -> state::finish
                    is State.NotRunning.Paused -> state::finish
                }
            }
        }
    }

    /** Timer state. */
    sealed class State : KalugaState(), Timer.State {
        abstract val totalDuration: Duration
        /** Timer is not running. */
        sealed class NotRunning(override val elapsed: Duration) : State(), Timer.State.NotRunning {
            /** Timer is paused. */
            class Paused(
                elapsedSoFar: Duration,
                override val totalDuration: Duration
            ) : NotRunning(elapsedSoFar), Timer.State.NotRunning.Paused {
                fun start(
                    interval: Duration,
                    coroutineScope: CoroutineScope,
                    finishCallback: suspend () -> Unit
                ): Running = Running(
                    elapsedSoFar = elapsed,
                    totalDuration = totalDuration,
                    interval = interval,
                    coroutineScope = coroutineScope,
                    finishCallback = finishCallback
                )
                internal fun finish(): Finished = Finished(totalDuration)
            }

            /** Timer is finished. */
            class Finished(
                override val totalDuration: Duration
            ) : NotRunning(totalDuration), Timer.State.NotRunning.Finished
        }

        /** Timer is running. */
        class Running(
            elapsedSoFar: Duration,
            override val totalDuration: Duration,
            interval: Duration,
            private val coroutineScope: CoroutineScope,
            private val finishCallback: suspend () -> Unit
        ) : State(), Timer.State.Running, HandleAfterNewStateIsSet<State>, HandleBeforeOldStateIsRemoved<State> {
            override val elapsed: Flow<Duration> = tickProvider(
                offset = elapsedSoFar,
                max = totalDuration,
                interval = interval
            )
            private lateinit var finishJob: Job

            internal suspend fun pause(): NotRunning.Paused {
                return NotRunning.Paused(elapsed.first(), totalDuration)
            }
            internal fun finish(): NotRunning.Finished = NotRunning.Finished(totalDuration)

            override suspend fun beforeOldStateIsRemoved(oldState: State) {
                finishJob = coroutineScope.launch {
                    delay(totalDuration - elapsed.first())
                    finishCallback()
                }
            }
            override suspend fun afterNewStateIsSet(newState: State) {
                finishJob.cancel()
            }
        }
    }
}

private fun tickProvider(
    offset: Duration,
    max: Duration,
    interval: Duration
): Flow<Duration> {
    val mark = TimeSource.Monotonic.markNow()

    fun newElapsed() = offset + mark.elapsedNow()

    return flow {
        var expectedElapsed = offset
        while (true) {
            // obtain and deliver a new elapsed time value
            val elapsed = newElapsed()
            emit(elapsed.coerceAtMost(max))
            // quit if reached the max
            if (max <= elapsed) break
            // adaptive correction reduces the interval by time spent delivering emitted value
            val correction = expectedElapsed - newElapsed()
            // wait for the next iteration
            delay(interval + correction)
            expectedElapsed += interval
        }
    }
}
