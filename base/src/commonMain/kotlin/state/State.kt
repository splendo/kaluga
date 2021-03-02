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

import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.flow.SharedFlowCollectionEvent.FirstCollection
import com.splendo.kaluga.base.flow.SharedFlowCollectionEvent.LaterCollections
import com.splendo.kaluga.base.flow.SharedFlowCollectionEvent.NoMoreCollections
import com.splendo.kaluga.base.flow.onCollectionEvent
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
val remain: suspend() -> State = { error("This should never be called. It's only used to indicate the state should remain the same") }

/**
 * State to be represented in a state machine
 */
open class State {

    /**
     * Use this to indicate to the state machine the state should stay the same
     * @return a special continuation that will be recognized by the state machine. Running this continuation will cause an error.
     */
    @Suppress("UNCHECKED_CAST") // cast should normally work since the receiver uses one type of state
    fun <S : State> remain(): suspend() -> S = remain as suspend () -> S

    /**
     * Called when this state is the first state of the state machine
     */
    open suspend fun initialState() {}

    /**
     * Called when this state is the final state of the state machine, until a new initialState is made
     */
    open suspend fun finalState() {}
}

interface HandleBeforeCreating {
    /**
     * Called while transitioning to a new state before the new state is created
     */
    suspend fun beforeCreatingNewState()
}

interface HandleAfterCreating<S : State> {
    /**
     * Called while transitioning to a new state after the new state is created
     *
     * @param newState the newly created state
     */
    suspend fun afterCreatingNewState(newState: S)
}

interface HandleAfterNewStateIsSet<S : State> {

    /**
     * Called while transitioning to a new state after the new state is set.
     *
     * @param newState the newly set [State]
     */
    suspend fun afterNewStateIsSet(newState: S)
}

interface HandleBeforeOldStateIsRemoved<S : State> {

    /**
     * Called while transitioning from an old state before it is removed.
     *
     * @param oldState the [State] to be removed
     */
    suspend fun beforeOldStateIsRemoved(oldState: S)
}

interface HandleAfterOldStateIsRemoved<S : State> {

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
 * @param S the [State] represented by this repo.
 * @param coroutineContext the [CoroutineContext] used to create a coroutine scope for this state machine. Make sure that if you pass a coroutine context that has sequential execution if you do not want simultaneous state changes. The default Main dispatcher meets these criteria.
 */
abstract class StateRepo<S : State, F:MutableSharedFlow<S>>(coroutineContext: CoroutineContext = Dispatchers.Main) : CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("State Repo")) {

    private val stateMutex = Mutex()

    abstract val mutableFlow: F
    val subscriptionCount
        get() =  mutableFlow.subscriptionCount

    private val _changedState = AtomicReference<S?>(null)
    internal var changedState
        get() = _changedState.get()
        set(value) { _changedState.set(value) }

    private suspend fun setChangedState(value: S) {
        changedState = value
        mutableFlow.emit(value)
    }

    /**
     * Provides a [Flow] of the [State] of this repo.
     *
     * @return The [Flow]
     */
    fun flow(): Flow<S> {
        return mutableFlow
    }

    internal open suspend fun initialize(initialValue:S? = null): S = stateMutex.withLock {
        val value = initialValue ?: initialValue()
        setChangedState(value)
        value
    }.also {
        it.initialState() // let the state initialize outside of the mutex to avoid deadlocks
    }

    /**
     * Gets the initial value of the repo
     * @return the initial value of the repo
     */
    abstract suspend fun initialValue(): S

    internal fun state(): S {
        return changedState ?: error("StateRepo($this) not yet initialized.")
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
    suspend fun takeAndChangeState(action: suspend(S) -> suspend () -> S): S = coroutineScope { // scope around the mutex so asynchronously scheduled coroutines that also use this method can run before the scope completed without deadlocks
        stateMutex.withLock {
            val result = CompletableDeferred<S>()
            launch {
                try {
                    val beforeState = state()
                    val transition = action(beforeState)
                    if (beforeState.remain<S>() === transition) {
                        result.complete(beforeState)
                    } else {
                        (beforeState as? HandleBeforeCreating)?.beforeCreatingNewState()
                        val newState = transition()
                        (beforeState as? HandleAfterCreating<S>)?.afterCreatingNewState(newState)
                        (newState as? HandleBeforeOldStateIsRemoved<S>)?.beforeOldStateIsRemoved(beforeState)
                        setChangedState(newState)
                        (beforeState as? HandleAfterNewStateIsSet<S>)?.afterNewStateIsSet(newState)
                        (newState as? HandleAfterOldStateIsRemoved<S>)?.afterOldStateIsRemoved(beforeState)
                        result.complete(newState)
                    }
                } catch (t: Throwable) {
                    result.completeExceptionally(t)
                }
            }
            return@coroutineScope result.await()
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
// Somewhat similar to a ConflatedBroadcastChannel, which was used in the previous implementation
inline fun <S> defaultLazySharedFlow():Lazy<MutableSharedFlow<S>> = lazy { MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST) }

/**
 * A [StateRepo] that represents its [State] as a Hot flow.
 */
abstract class HotStateRepo<S : State>(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate,
    private val lazyMutableSharedFlow: Lazy<MutableSharedFlow<S>> = defaultLazySharedFlow()
) : StateRepo<S, MutableSharedFlow<S>>(coroutineContext) {

    // guards once only initialization across threads
    private val initialized = AtomicBoolean(false)

    override val mutableFlow: MutableSharedFlow<S>
    get() {
        val isInitialized = lazyMutableSharedFlow.isInitialized()
        val flow = lazyMutableSharedFlow.value
        if (!isInitialized && initialized.compareAndSet(expected = false, new = true))
            launch(coroutineContext) {
                mutableFlow.subscriptionCount.filter { it < 1 }.first()
                initialize()
            }
        return flow
    }
}

abstract class BaseColdStateRepo<S:State, F:MutableSharedFlow<S>>(
    context: CoroutineContext = Dispatchers.Main.immediate
) : StateRepo<S, F>(context) {

    abstract val lazyMutableFlow: Lazy<F>

    override val mutableFlow: F by lazy {
        val flow by lazyMutableFlow
        launch(coroutineContext) {
            flow.onCollectionEvent {
                when (it) {
                    NoMoreCollections -> deinitialize(state())
                    FirstCollection -> firstInitialization()
                    LaterCollections -> laterInitializations()
                }
            }
        }
        flow
    }

    abstract suspend fun firstInitialization()

    abstract suspend fun laterInitializations()

    abstract suspend fun deinitialize(state: S)
}

open class ColdStateFlowRepo<S:State>(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate,
    val init: suspend () -> S,
    val deinit: suspend (S) -> Unit
) : BaseColdStateRepo<S, MutableStateFlow<S>>(
    context = coroutineContext,
) {
    val stateflow: StateFlow<S>
        get() = mutableFlow

    // the first initialization is done in the lazy block below since StateFlow requires an initial value
    override suspend fun initialValue(): S = init()

    override suspend fun deinitialize(state: S) {
        deinit(state)
    }

    override val lazyMutableFlow: Lazy<MutableStateFlow<S>> =
        lazy {
            runBlocking(this.coroutineContext) {
                MutableStateFlow(
                    init()
                )
            }
        }

    override suspend fun firstInitialization() {
        initialize(stateflow.value)
    }

    override suspend fun laterInitializations() {
        initialize()
    }
}


/**
 * A [StateRepo] that represents its [State] as a Cold flow. Data will only be set when the State repo is observed
 * *
 * This implementation uses a [SharedFlow]. If you want to use a cold state repo based on StateFlow,
 * consider [ColdStateFlowRepo] or extending [BaseColdStateRepo]
 */
abstract class ColdStateRepo<S : State>(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate,
    override val lazyMutableFlow: Lazy<MutableSharedFlow<S>> = defaultLazySharedFlow()
) : BaseColdStateRepo<S, MutableSharedFlow<S>>(coroutineContext) {

    override suspend fun firstInitialization() {
        initialize()
    }

    override suspend fun laterInitializations() {
        initialize()
    }
}
