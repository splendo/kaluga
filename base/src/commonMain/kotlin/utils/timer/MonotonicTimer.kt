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

import com.splendo.kaluga.state.HotStateFlowRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import com.splendo.kaluga.state.State as KalugaState

@ExperimentalTime
/** Timer based on the system clock. */
internal class MonotonicTimer(
    duration: Duration,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Timer, HotStateFlowRepo<MonotonicTimer.State>(
    coroutineContext,
    {
        State.NotRunning.Paused(elapsedSoFar = Duration.ZERO, totalDuration = duration)
    }
) {

    override val state: StateFlow<Timer.State> = stateFlow
    override val duration: Duration get() = stateFlow.value.totalDuration

    override suspend fun start() {
        takeAndChangeState { state ->
            when (state) {
                is State.NotRunning.Paused -> suspend {
                    val finishJob = launch {
                        // suspend for remaining duration and finish
                        delay(state.totalDuration - state.elapsed)
                        finish()
                    }
                    state.start(finishJob)
                }
                is State.NotRunning.Finished, is State.Running -> state.remain()
            }
        }
    }

    override suspend fun pause() {
        takeAndChangeState { state ->
            when (state) {
                is State.Running -> state::pause
                is State.NotRunning -> state.remain()
            }
        }
    }

    private suspend fun finish() {
        takeAndChangeState { state ->
            when (state) {
                is State.NotRunning.Finished -> state.remain()
                is State.Running -> state::finish
                is State.NotRunning.Paused -> state::finish
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
                fun start(finishJob: Job): Running =
                    Running(elapsed, totalDuration, finishJob)
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
            private val finishJob: Job
        ) : State(), Timer.State.Running {
            override val elapsed: Flow<Duration> = tickProvider(elapsedSoFar, totalDuration)

            internal suspend fun pause(): NotRunning.Paused {
                finishJob.cancel()
                return NotRunning.Paused(elapsed.first(), totalDuration)
            }
            internal fun finish(): NotRunning.Finished = NotRunning.Finished(totalDuration)
        }
    }
}

@ExperimentalTime
private fun tickProvider(offset: Duration, max: Duration): Flow<Duration> {
    val mark = TimeSource.Monotonic.markNow()

    return flow {
        while (true) {
            val elapsed = offset + mark.elapsedNow()
            emit(elapsed.coerceAtMost(max))
            if (max <= elapsed) break
            yield()
        }
    }
}
