package com.splendo.kaluga.state
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

import com.splendo.kaluga.base.flow.ColdFlowable
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.flow.FlowConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

interface StateTransitionAction<T:State<T>> {
    suspend fun action(fromState: T): T
}

/**
 * State to be represented in a state machine
 *
 * @param T type of the State
 * @param repoAccessor The [StateRepoAccesor] for accessing the [StateRepo] associated with this State
 */
open class State<T:State<T>>(open val repoAccessor:StateRepoAccesor<T>){

    /**
     * Called when this state is the first state of the state machine
     */
    open suspend fun initialState() {}

    /**
     * Called while transitioning to a new state before the new state is created
     */
    open suspend fun beforeCreatingNewState() {}
    /**
     * Called while transitioning to a new state after the new state is created
     *
     * @param newState the newly created state
     */
    open suspend fun afterCreatingNewState(newState: T) {}

    /**
     * Called while transitioning to a new state after the new state is set.
     */
    open suspend fun afterNewStateIsSet() {}

    /**
     * Called while transitioning from an old state before it is removed.
     */
    open suspend fun beforeOldStateIsRemoved() {}

    /**
     * Called while transitioning from an old state after it is removed
     *
     * @param oldState the removed state
     */
    open suspend fun afterOldStateIsRemoved(oldState: T) {}

    /**
     * Called when this state is the final state of the state machine
     */
    open suspend fun finalState() {}

    /**
     * Changes from this state to a new state. Only transitions if this State is the current state of the state machine.
     *
     * @param toState the state to which to transition
     */
    protected suspend fun createStateTransitionAction(toState: suspend () -> T) : StateTransitionAction<T> {
        return object : StateTransitionAction<T> {

            override suspend fun action(fromState: T): T {
                return if (fromState === this@State) {
                    toState()
                } else {
                    fromState
                }
            }
        }
    }

}

/**
 * Accessor for accessing the current state from a [StateRepo].
 *
 * @param T the [State] associated with the [StateRepo]
 * @param s The [StateRepo] accessed bt this accessor
 */
class StateRepoAccesor<T:State<T>>(private val s:StateRepo<T> ) : CoroutineScope by s {


    fun currentState() : T = s.state()

    suspend fun handleCurrentState(action: suspend (State<T>) -> StateTransitionAction<T>?) = s.handleCurrentState(action)

}

/**
 * The state repo can change holds the current [State] (which can be accessed as a flow), and can be used to change the current state
 *
 * @param T the [State] represented by this repo.
 * @param coroutineContext the [CoroutineContext] used to create a coroutine scope for this state machine. Make sure that if you pass a coroutine context that has sequential execution if you do not want simultaneous state changes. The default Main dispatcher meets these criteria.
 */
abstract class StateRepo<T:State<T>>(coroutineContext: CoroutineContext = Dispatchers.Main) : CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("State Repo")) {


    private val stateMutex = Mutex()

    abstract val flowable: Lazy<BaseFlowable<T>>

    @Suppress("LeakingThis") // we are using this method so we can hold an initial state that holds this repo as a reference.
    internal lateinit var changedState:T
    private suspend fun setChangedState(value: T) {
        changedState = value
        flowable.value.set(value)
    }

    /**
     * Provides a [Flow] of the [State] of this repo.
     *
     * @param flowConfig the [FlowConfig] to apply to the returned [Flow]
     * @return a [Flow] of the [State] of this repo
     */
    fun flow(flowConfig: FlowConfig = FlowConfig.Conflate): Flow<T> {
        return flowable.value.flow(flowConfig)
    }

    internal fun initialize() : T {
        val value = initialValue()
        changedState = value
        launch {
            value.initialState()
        }
        return value
    }

    /**
     * Gets the initial value of the repo
     * @return the initial value of the repo
     */
    abstract fun initialValue() : T

    internal fun state():T {
        return changedState
    }

    suspend fun handleCurrentState(action: suspend (State<T>) -> StateTransitionAction<T>?) {
        stateMutex.withLock {
            action(state())?.let {
                changeState(it)
            }
        }
    }

    private fun changeStateBlocking(action: StateTransitionAction<T>):T {
        return runBlocking {
            changeState(action)
        }
    }

    private suspend fun changeState(action: StateTransitionAction<T>):T {
        val result = CompletableDeferred<T>()
        coroutineScope {
            launch {
                val beforeState = state()
                beforeState.beforeCreatingNewState()
                val newState = action.action(beforeState)
                beforeState.afterCreatingNewState(newState)
                newState.beforeOldStateIsRemoved()
                setChangedState(newState)
                beforeState.afterNewStateIsSet()
                newState.afterOldStateIsRemoved(beforeState)
                result.complete(newState)
            }
        }
        return result.await()
    }

}

/**
 * A [StateRepo] that represents its [State] as a Hot flow.
 */
abstract class HotStateRepo<T:State<T>>(coroutineContext: CoroutineContext = Dispatchers.Main) : StateRepo<T>(coroutineContext) {

    override val flowable = lazy {
        HotFlowable(initialize())
    }

}

/**
 * A [StateRepo] that represents its [State] as a Cold flow. Data will only be set when the State repo is observed
 */
abstract class ColdStateRepo<T:State<T>>(coroutineContext: CoroutineContext = Dispatchers.Main) : StateRepo<T>(coroutineContext) {

    override val flowable = lazy {
        ColdFlowable({
            initialize()
        }, {
            state ->
            launch {
                state.finalState()
            }
            this.deinitialize(state)
        })
    }

    abstract fun deinitialize(state: T)

}