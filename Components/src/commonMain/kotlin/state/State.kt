package com.splendo.mpp.state

import com.splendo.mpp.flow.BaseFlowable
import kotlinx.coroutines.*

open class State<T:State<T>>(open val repo:StateRepo<T>) {
    open fun done() {}
}

abstract class StateRepo<T:State<T>>(private val coroutineScope: CoroutineScope = MainScope()): BaseFlowable<T>(), CoroutineScope by coroutineScope {

    private var changedState:T? = null
        private set(value) {
            field = value
            value?.let {
                launch { set(it) }
            }
        }
    abstract fun initialState():T

    private fun state():T {
        return changedState ?: initialState()
    }

    fun changeState(action: (state:T) -> T):T {
        val currentState = state()
        val newState = action(currentState)
        currentState.done()
        if (currentState is CoroutineScope)
            currentState.cancel("new state is active")
        this.changedState = newState
        return currentState
    }

}
