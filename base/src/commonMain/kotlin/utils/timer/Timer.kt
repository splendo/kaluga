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

import com.splendo.kaluga.base.utils.firstInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/** A timer ticking a certain [duration]. */
@ExperimentalTime
interface Timer {
    /** Timer duration. */
    val duration: Duration
    /** Current state of the timer. */
    val state: StateFlow<State>

    /** Starts the timer. */
    suspend fun start()
    /** Stops the timer. */
    suspend fun pause()

    sealed interface State {
        sealed interface Running: State {
            val elapsed: Flow<Duration>
        }

        sealed interface NotRunning: State {
            val elapsed: Duration

            interface Paused : NotRunning
            interface Finished: NotRunning
        }
    }
}

/** Time elapsed from the timer start. */
@ExperimentalTime
fun Timer.elapsed(): Flow<Duration> = state.flatMapLatest { state ->
    when (state) {
        is Timer.State.NotRunning -> flowOf(state.elapsed)
        is Timer.State.Running -> state.elapsed
    }
}

/** Awaits for the timer to finish. */
@ExperimentalTime
suspend fun Timer.awaitFinish() {
    state.firstInstance<Timer.State.NotRunning.Finished>()
}



