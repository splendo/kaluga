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

import com.splendo.kaluga.base.state.HandleAfterNewStateIsSet
import com.splendo.kaluga.base.state.HandleBeforeOldStateIsRemoved
import com.splendo.kaluga.base.state.HotStateFlowRepo
import com.splendo.kaluga.base.state.KalugaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

/** A coroutine delay function. */
typealias DelayFunction = suspend (Duration) -> Unit

/**
 * [Timer] based on the system clock.
 * @property duration timer duration
 * @param interval The [Duration] between timer ticks
 * @param timeSource The [TimeSource] for measuring intervals
 * @param delayFunction Method for delaying a given [Duration]
 * @param coroutineScope a parent coroutine scope for the timer
 */
class RecurringTimer(
    override val duration: Duration,
    interval: Duration = 100.milliseconds,
    timeSource: TimeSource = TimeSource.Monotonic,
    delayFunction: DelayFunction = { delayDuration -> delay(delayDuration) },
    coroutineScope: CoroutineScope = MainScope(),
) : ControllableTimer {

    private sealed class State {
        data class Active(val repo: TimerStateRepo) : State()
        data class Finished(val state: Timer.State.NotRunning.Finished) : State()
    }

    private val stateRepo: StateFlow<State>

    init {
        val repo = TimerStateRepo(duration, interval, timeSource, delayFunction, coroutineScope)
        val stateRepo = MutableStateFlow<State>(State.Active(repo))
        this.stateRepo = stateRepo.asStateFlow()
        coroutineScope.launch {
            val finishedState = repo.stateFlow.filterIsInstance<Timer.State.NotRunning.Finished>().first()
            stateRepo.compareAndSet(State.Active(repo), State.Finished(finishedState))
            repo.cancel()
        }
    }

    override val state: Flow<Timer.State> = stateRepo.flatMapLatest { state ->
        when (state) {
            is State.Active -> state.repo.stateFlow.map { it.timerState }
            is State.Finished -> flowOf(state.state)
        }
    }
    override val currentState: Timer.State get() = when (val state = stateRepo.value) {
        is State.Active -> state.repo.stateFlow.value.timerState
        is State.Finished -> state.state
    }

    override suspend fun start(): Boolean = stateRepo.transformLatest { state ->
        when (state) {
            is State.Active -> emit(state.repo.start())
            is State.Finished -> emit(false)
        }
    }.first()

    override suspend fun pause() = stateRepo.transformLatest { state ->
        when (state) {
            is State.Active -> emit(state.repo.pause())
            is State.Finished -> emit(false)
        }
    }.first()

    override suspend fun stop() {
        stateRepo.transformLatest { state ->
            when (state) {
                is State.Active -> state.repo.stop()
                is State.Finished -> emit(Unit)
            }
        }.first()
    }
}

/** Timer state machine. */
private class TimerStateRepo(
    totalDuration: Duration,
    private val interval: Duration,
    private val timeSource: TimeSource,
    private val delayFunction: DelayFunction,
    private val coroutineScope: CoroutineScope,
) : HotStateFlowRepo<TimerStateRepo.State>(
    coroutineScope.coroutineContext,
    {
        State.NotRunning.Paused(elapsedSoFar = Duration.ZERO, totalDuration = totalDuration)
    },
) {
    suspend fun start(): Boolean = withContext(coroutineScope.coroutineContext) {
        takeAndChangeState { state ->
            when (state) {
                is State.NotRunning.Paused -> suspend {
                    state.start(interval, timeSource, delayFunction, coroutineScope, ::finish)
                }
                is State.NotRunning.Finished, is State.Running -> state.remain()
            }
        }
    } is State.Running

    suspend fun pause() = withContext(coroutineScope.coroutineContext) {
        takeAndChangeState { state ->
            when (state) {
                is State.Running -> state::pause
                is State.NotRunning -> state.remain()
            }
        }
    } is State.NotRunning.Paused

    suspend fun stop() {
        withContext(coroutineScope.coroutineContext) {
            takeAndChangeState { state ->
                when (state) {
                    is State.Running -> state::stop
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
    sealed class State : KalugaState {
        abstract val totalDuration: Duration
        abstract val timerState: Timer.State

        /** Timer is not running. */
        sealed class NotRunning(protected val elapsedSoFar: Duration) : State() {
            val elapsed = flowOf(elapsedSoFar)

            /** Timer is paused. */
            class Paused(elapsedSoFar: Duration, override val totalDuration: Duration) :
                NotRunning(elapsedSoFar),
                Timer.State.NotRunning.Paused {

                override val timerState: Timer.State get() = this

                fun start(interval: Duration, timeSource: TimeSource, delayFunction: DelayFunction, coroutineScope: CoroutineScope, finishCallback: suspend () -> Unit): Running =
                    Running(
                        elapsedSoFar = elapsedSoFar,
                        totalDuration = totalDuration,
                        interval = interval,
                        timeSource = timeSource,
                        delayFunction = delayFunction,
                        coroutineScope = coroutineScope,
                        finishCallback = finishCallback,
                    )
            }

            /** Timer is finished. */
            class Finished(override val totalDuration: Duration) :
                NotRunning(totalDuration),
                Timer.State.NotRunning.Finished {
                override val timerState: Timer.State get() = this
            }
        }

        /** Timer is running. */
        class Running(
            elapsedSoFar: Duration,
            override val totalDuration: Duration,
            interval: Duration,
            timeSource: TimeSource,
            private val delayFunction: DelayFunction,
            private val coroutineScope: CoroutineScope,
            private val finishCallback: suspend () -> Unit,
        ) : State(),
            Timer.State.Running,
            HandleAfterNewStateIsSet<State>,
            HandleBeforeOldStateIsRemoved<State> {
            override val elapsed: Flow<Duration> = tickProvider(
                offset = elapsedSoFar,
                max = totalDuration,
                interval = interval,
                timeSource = timeSource,
                delayFunction = delayFunction,
            )

            override val timerState: Timer.State get() = this

            private val supervisor = SupervisorJob()

            internal suspend fun pause(): NotRunning.Paused = NotRunning.Paused(elapsed.first(), totalDuration)
            internal suspend fun stop(): NotRunning.Finished = NotRunning.Finished(elapsed.first())
            internal fun finish(): NotRunning.Finished = NotRunning.Finished(totalDuration)

            override suspend fun beforeOldStateIsRemoved(oldState: State) {
                CoroutineScope(supervisor + coroutineScope.coroutineContext).launch {
                    delayFunction(totalDuration - elapsed.first())
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
private fun tickProvider(offset: Duration, max: Duration, interval: Duration, timeSource: TimeSource, delayFunction: suspend (Duration) -> Unit): Flow<Duration> {
    val mark = timeSource.markNow()

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
            delayFunction(delay)
            expectedElapsed += interval
        }
    }
}
