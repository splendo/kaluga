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

import com.splendo.kaluga.base.utils.firstInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformWhile
import kotlin.time.Duration

/** A timer ticking a certain [duration]. */
interface Timer {
    /** Timer [Duration]. */
    val duration: Duration

    /** Flow of [State] of the timer. */
    val state: Flow<State>

    /** The current [State] of the timer. */
    val currentState: State

    /**
     * The state of a [Timer]
     */
    sealed interface State {
        /**
         * A [Flow] of the [Duration] that has elapsed while the timer was in a [Running] state
         */
        val elapsed: Flow<Duration>

        /**
         * A [State] that indicates the [Timer] is currently running. This may result in changes to [elapsed]
         */
        interface Running : State

        /**
         * A [State] that indicates that a [Timer] is not currently running.
         */
        sealed interface NotRunning : State {

            /**
             * A [NotRunning] state to indicate that the [Timer] has been paused
             */
            interface Paused : NotRunning

            /**
             * A [NotRunning] state to indicate that the [Timer] has finished running.
             */
            interface Finished : NotRunning
        }
    }
}

/** A [Timer] ticking a certain [duration] with the ability to [start], [pause] and [stop]. */
interface ControllableTimer : Timer {
    /**
     * Starts the timer.
     * @throws [IllegalStateException] if the timer has finished.
     * */
    suspend fun start()

    /**
     * Pauses the timer. Calling [start] again will make it resume.
     * @throws [IllegalStateException] if the timer has finished.
     * */
    suspend fun pause()

    /**
     * Stops the timer causing it to finish. Calling [start] again will throw [IllegalStateException]
     * */
    suspend fun stop()
}

/** [Duration] that has elapsed while [Timer.state] was [Timer.State.Running]. */
fun Timer.elapsed(): Flow<Duration> = state
    .transformWhile { stateValue ->
        // [Finished] is the final state, ensure the consumer's [collect] would be able to exit
        emit(stateValue)
        stateValue !is Timer.State.NotRunning.Finished
    }
    .flatMapLatest { state -> state.elapsed }

/** Awaits for the [Timer] to reach the [Timer.State.NotRunning.Finished] state. */
suspend fun Timer.awaitFinish() {
    state.firstInstance<Timer.State.NotRunning.Finished>()
}
