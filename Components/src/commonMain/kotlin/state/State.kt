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

import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.runBlocking
import kotlinx.coroutines.*

open class State<T:State<T>>(open val repo:StateRepo<T>){
    open suspend fun beforeCreatingNewState() {}
    open suspend fun afterCreatingNewState() {}
    open suspend fun afterNewStateIsSet() {}
}

/**
 * The state repo can change holds the current state (which can be accessed as a flow), and can be used to change the current state
 *
 * Make sure that if you pass a coroutine scope that has sequential execution if you do not want simultaneous state changes. The default MainScope meets these criteria.
 */
abstract class StateRepo<T:State<T>>(private val coroutineScope: CoroutineScope = MainScope()): BaseFlowable<T>(), CoroutineScope by coroutineScope {

    @Suppress("LeakingThis") // we are using this method so we can hold an initial state that holds this repo as a reference.
    private var changedState:T = initialState()
        private set(value) {
            field = value
            launch {
                set(value)
            }
        }

    init {
        setBlocking(changedState)
    }

    abstract fun initialState():T

    private fun state():T {
        return changedState
    }

    fun changeStateBlocking(action: (state:T) -> T):T {
        return runBlocking {
            changeState(action)
        }
    }

    suspend fun changeState(action: (state:T) -> T):T {
        val result = CompletableDeferred<T>()
        coroutineScope {
            launch {
                val beforeState = state()
                beforeState.beforeCreatingNewState()
                if (beforeState is CoroutineScope)
                    beforeState.cancel("this state will end. New state will be created")
                val newState = action(beforeState)
                beforeState.afterCreatingNewState()
                changedState = newState
                beforeState.afterNewStateIsSet()
                result.complete(newState)
            }
        }
        return result.await()
    }

}