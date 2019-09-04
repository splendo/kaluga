package com.splendo.mpp.state

import com.splendo.mpp.flow.BaseFlowable
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
