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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
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
    coroutineScope: CoroutineScope = MainScope(),
    timeProvider: TimeProvider = TimeProvider()
) : ControllableTimer {

    constructor(
        duration: Duration,
        interval: Duration,
        coroutineScope: CoroutineScope = MainScope()
    ) : this (duration, interval, coroutineScope, TimeProvider())

    private val stateRepo = TimerStateRepo(duration, interval, timeProvider, coroutineScope)
    override val state: StateFlow<Timer.State> = stateRepo.stateFlow

    override suspend fun start() = stateRepo.start()

    override suspend fun pause() = stateRepo.pause()
}

/** Provides a time source and delay implementation. */
data class TimeProvider(
    val timeSource: TimeSource = TimeSource.Monotonic,
    val delayFunction: suspend (Duration) -> Unit = { duration -> delay(duration) }
)

/** Timer state machine. */
private class TimerStateRepo(
    totalDuration: Duration,
    private val interval: Duration,
    private val timeProvider: TimeProvider,
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
                        state.start(interval, timeProvider, coroutineScope, ::finish)
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
                    is State.Running -> state::finish
                    is State.NotRunning -> state.remain()
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
                    timeProvider: TimeProvider,
                    coroutineScope: CoroutineScope,
                    finishCallback: suspend () -> Unit
                ): Running = Running(
                    elapsedSoFar = elapsed,
                    totalDuration = totalDuration,
                    interval = interval,
                    timeProvider = timeProvider,
                    coroutineScope = coroutineScope,
                    finishCallback = finishCallback
                )
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
            private val timeProvider: TimeProvider,
            private val coroutineScope: CoroutineScope,
            private val finishCallback: suspend () -> Unit
        ) : State(), Timer.State.Running, HandleAfterNewStateIsSet<State>, HandleBeforeOldStateIsRemoved<State> {
            override val elapsed: Flow<Duration> = tickProvider(
                offset = elapsedSoFar,
                max = totalDuration,
                interval = interval,
                timeProvider = timeProvider
            )

            private val supervisor = SupervisorJob()

            internal suspend fun pause(): NotRunning.Paused {
                return NotRunning.Paused(elapsed.first(), totalDuration)
            }
            internal fun finish(): NotRunning.Finished = NotRunning.Finished(totalDuration)

            override suspend fun beforeOldStateIsRemoved(oldState: State) {
                CoroutineScope(supervisor + coroutineScope.coroutineContext).launch {
                    timeProvider.delayFunction(totalDuration - elapsed.first())
                    finishCallback()
                }
            }
            override suspend fun afterNewStateIsSet(newState: State) {
                supervisor.cancelChildren()
            }
        }
    }
}

/**
 * @return flow of ticks as close to [interval] intervals as possible. starting from [offset] to the [max]
 */
private fun tickProvider(
    offset: Duration,
    max: Duration,
    interval: Duration,
    timeProvider: TimeProvider
): Flow<Duration> {
    val mark = timeProvider.timeSource.markNow()

    fun newElapsed() = offset + mark.elapsedNow()

    return flow {
        var expectedElapsed = offset
        while (true) {
            // obtain and deliver a new elapsed time value
            emit(newElapsed().coerceAtMost(max))

            // obtain a new elapsed marker, as consuming
            val elapsed = newElapsed()

            // quit if the timer reached the max
            if (max <= elapsed) break

            // adaptive correction reduces the interval by time spent delivering emitted value
            var delay = interval + expectedElapsed - elapsed
            // in case consumer processed the value too long, skip to the next interval
            while (delay.isNegative()) {
                delay += interval
                expectedElapsed += interval
            }

            // wait for the next iteration
            timeProvider.delayFunction(delay)
            expectedElapsed += interval
        }
    }
}
