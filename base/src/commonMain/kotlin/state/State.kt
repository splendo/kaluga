/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.state

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.base.flow.ColdFlowable
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.flow.FlowConfig
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * State to be represented in a state machine
 */
open class State<S : State<S>> {

    val remain = suspend {
        this as S
    }

    /**
     * Called when this state is the first state of the state machine
     */
    open suspend fun initialState() {}

    /**
     * Called when this state is the final state of the state machine
     */
    open suspend fun finalState() {}
}

interface HandleBeforeCreating {
    /**
     * Called while transitioning to a new state before the new state is created
     */
    suspend fun beforeCreatingNewState()
}

interface HandleAfterCreating<S : State<S>> {
    /**
     * Called while transitioning to a new state after the new state is created
     *
     * @param newState the newly created state
     */
    suspend fun afterCreatingNewState(newState: S)
}

interface HandleAfterNewStateIsSet<S : State<S>> {

    /**
     * Called while transitioning to a new state after the new state is set.
     *
     * @param newState the newly set [State]
     */
    suspend fun afterNewStateIsSet(newState: S)
}

interface HandleBeforeOldStateIsRemoved<S : State<S>> {

    /**
     * Called while transitioning from an old state before it is removed.
     *
     * @param oldState the [State] to be removed
     */
    suspend fun beforeOldStateIsRemoved(oldState: S)
}

interface HandleAfterOldStateIsRemoved<S : State<S>> {

    /**
     * Called while transitioning from an old state after it is removed
     *
     * @param oldState the removed state
     */
    suspend fun afterOldStateIsRemoved(oldState: S)
}

/**
 * The state repo can change holds the current [State] (which can be accessed as a flow), and can be used to change the current state
 *
 * @param T the [State] represented by this repo.
 * @param coroutineContext the [CoroutineContext] used to create a coroutine scope for this state machine. Make sure that if you pass a coroutine context that has sequential execution if you do not want simultaneous state changes. The default Main dispatcher meets these criteria.
 */
abstract class StateRepo<S : State<S>>(coroutineContext: CoroutineContext = MainQueueDispatcher) : CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("State Repo")) {

    private val stateMutex = Mutex()

    abstract val flowable: BaseFlowable<S>

    internal lateinit var changedState: S
    private suspend fun setChangedState(value: S) {
        changedState = value
        flowable.set(value)
    }

    /**
     * Provides a [Flow] of the [State] of this repo.
     *
     * @param flowConfig the [FlowConfig] to apply to the returned [Flow]
     * @return a [Flow] of the [State] of this repo
     */
    fun flow(flowConfig: FlowConfig = FlowConfig.Conflate): Flow<S> {
        return flowable.flow(flowConfig)
    }

    internal suspend fun initialize(): S {
        stateMutex.withLock {
            val value = initialValue()
            changedState = value
            value.initialState()
            return value
        }
    }

    /**
     * Gets the initial value of the repo
     * @return the initial value of the repo
     */
    abstract suspend fun initialValue(): S

    internal fun state(): S {
        return changedState
    }

    /**
     * Peek the current state of the state machine. The current state could change immediately after it is returned.
     *
     * If any actions are taken based on the current state that affect the state machine you should not use this method.
     *
     * If your code relies on the state not changing use [useState].
     * If you want to change the state based on the current state use [takeAndChangeState]
     *
     * @return the current [State] of the [StateRepo]
     */
    fun peekState() = state()

    /**
     * Makes the current [State] available in [action]. The state is guaranteed not to change during the execution of [action].
     * This operation ensures atomic state observations, so the state will not change while the [action] is being executed.
     *
     * This method uses a separate coroutineScope, meaning it will suspend until all child Jobs are completed, including those that asynchronously call this method itself (however a different state might be current at that point).
     *
     * @param action the function for which will [State] receive the state, guaranteed to be unchanged for the duration of the function.
     */
    suspend fun useState(action: suspend (S) -> Unit) = coroutineScope {
        stateMutex.withLock {
            val result = EmptyCompletableDeferred()
            launch {
                try {
                    action(state())
                    result.complete()
                } catch (e: Throwable) {
                    result.completeExceptionally(e)
                }
            }
            return@coroutineScope result.await()
        }
    }

    /**
     * Changes from the current [State] to a new [State]. This operation ensures atomic state changes.
     * The new state is determined by an [action], which takes the current [State] upon starting the state transition and provides a deferred state creation.
     * You are strongly encouraged to use the [State] provided by the [action] to determine the new state, to ensure no illegal state transitions occur, as the state may have changed between calling [takeAndChangeState] and the execution of [action].
     * If the [action] returns [State.remain] no state transition will occur.
     * Since this operation is atomic, the [action] should not directly call [takeAndChangeState] itself. If required to do this, handle the additional transition in a separate coroutine.
     *
     * This method uses a separate coroutineScope, meaning it will suspend until all child Jobs are completed, including those that asynchronously call this method itself.
     *
     * @param action Function to determine the [State] to be transitioned to from the current [State]. If no state transition should occur, return [State.remain]
     */
    suspend fun takeAndChangeState(action: suspend (S) -> suspend () -> S): S = coroutineScope { // scope around the mutex so asynchronously scheduled coroutines that also use this method can run before the scope completed without deadlocks
        stateMutex.withLock {
            val result = CompletableDeferred<S>()
            launch {
                try {
                    val beforeState = state()
                    val transition = action(beforeState)
                    // No Need to Transition if remain is used
                    if (transition == beforeState.remain) {
                        result.complete(beforeState)
                        return@launch
                    }
                    (beforeState as? HandleBeforeCreating)?.beforeCreatingNewState()
                    val newState = transition()
                    (beforeState as? HandleAfterCreating<S>)?.afterCreatingNewState(newState)
                    (newState as? HandleBeforeOldStateIsRemoved<S>)?.beforeOldStateIsRemoved(beforeState)
                    setChangedState(newState)
                    (beforeState as? HandleAfterNewStateIsSet<S>)?.afterNewStateIsSet(newState)
                    (newState as? HandleAfterOldStateIsRemoved<S>)?.afterOldStateIsRemoved(beforeState)
                    result.complete(newState)
                } catch (e: Throwable) {
                    result.completeExceptionally(e)
                }
            }
            return@coroutineScope result.await()
        }
    }
}

/**
 * A [StateRepo] that represents its [State] as a Hot flow.
 */
abstract class HotStateRepo<S : State<S>>(coroutineContext: CoroutineContext = MainQueueDispatcher) : StateRepo<S>(coroutineContext) {

    private val hotFlowable = lazy {
        HotFlowable(runBlocking { initialize() })
    }
    override val flowable: BaseFlowable<S> get() = hotFlowable.value
}

/**
 * A [StateRepo] that represents its [State] as a Cold flow. Data will only be set when the State repo is observed
 */
abstract class ColdStateRepo<S : State<S>>(coroutineContext: CoroutineContext = MainQueueDispatcher) : StateRepo<S>(coroutineContext) {

    override val flowable: ColdFlowable<S> = ColdFlowable({
        initialize()
    }, {
            state ->
        state.finalState()
        deinitialize(state)
    })

    abstract suspend fun deinitialize(state: S)
}
