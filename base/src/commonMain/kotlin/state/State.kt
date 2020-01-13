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
import kotlin.coroutines.CoroutineContext

open class State<T:State<T>>(open val repoAccessor:StateRepoAccesor<T>){
    open suspend fun initialState() {}
    open suspend fun beforeCreatingNewState() {}
    open suspend fun afterCreatingNewState(newState: T) {}
    open suspend fun afterNewStateIsSet() {}
    open suspend fun beforeOldStateIsRemoved() {}
    open suspend fun afterOldStateIsRemoved(oldState: T) {}
    open suspend fun finalState() {}

    protected suspend fun changeState(toState: T) {
        repoAccessor.changeState {
            if (it === this)
                toState
            else
                it
        }
    }

}

class StateRepoAccesor<T:State<T>>(private val s:StateRepo<T> ) : CoroutineScope by s {

    fun currentState() =  s.state()

    internal suspend fun changeState(action: (T) -> T) {
        s.changeState(action)
    }
}

/**
 * The state repo can change holds the current state (which can be accessed as a flow), and can be used to change the current state
 *
 * Make sure that if you pass a coroutine scope that has sequential execution if you do not want simultaneous state changes. The default MainScope meets these criteria.
 */
abstract class StateRepo<T:State<T>>(coroutineContext: CoroutineContext = Dispatchers.Main) : CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("State Repo")) {

    abstract val flowable: Lazy<BaseFlowable<T>>

    @Suppress("LeakingThis") // we are using this method so we can hold an initial state that holds this repo as a reference.
    internal lateinit var changedState:T
    private suspend fun setChangedState(value: T) {
        changedState = value
        flowable.value.set(value)
    }

    fun flow(flowConfig: FlowConfig = FlowConfig.Conflate): Flow<T> {
        return flowable.value.flow(flowConfig)
    }

    internal fun state():T {
        return changedState
    }

    internal fun changeStateBlocking(action: (state:T) -> T):T {
        return runBlocking {
            changeState(action)
        }
    }

    internal suspend fun changeState(action: (state:T) -> T):T {
        val result = CompletableDeferred<T>()
        coroutineScope {
            launch {
                val beforeState = state()
                beforeState.beforeCreatingNewState()
                val newState = action(beforeState)
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

abstract class HotStateRepo<T:State<T>>(coroutineContext: CoroutineContext = Dispatchers.Main) : StateRepo<T>(coroutineContext) {

    override val flowable = lazy {
        val value = initialValue
        changedState = initialValue
        launch {
            value.initialState()
        }
        HotFlowable(value)
    }

    protected abstract val initialValue: T

}

abstract class ColdStateRepo<T:State<T>>(coroutineContext: CoroutineContext = Dispatchers.Main) : StateRepo<T>(coroutineContext) {

    override val flowable = lazy {
        ColdFlowable({
            val value = initialize()
            changedState = value
            launch {
                value.initialState()
            }
            value
        }, {
            state -> this.deinitialize(state)
        })
    }

    abstract fun initialize() : T
    abstract fun deinitialize(state: T)

}