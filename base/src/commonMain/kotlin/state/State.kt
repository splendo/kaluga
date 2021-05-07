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
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable
import kotlin.reflect.KClass

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
    @Deprecated("Use an initializer state rather than relying on this method, it might be called after the initial state is already changed")
    open suspend fun initialState() {}

    /**
     * Called when this state is the final state of the state machine
     */
    @Deprecated("This method is not always actually called (e.g. for a HotRepo) since there is not always a final state")
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
abstract class StateRepo<S : State, F:MutableSharedFlow<S>>(coroutineContext: CoroutineContext = Dispatchers.Main.immediate) : CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("State Repo")), Flow<S> {

    override suspend fun collect(collector: FlowCollector<S>) = mutableFlow.collect(collector)

    /**
     * Semaphare used as a mutex for changing state.
     *
     * By default the single permit is aquired, only when the initial state is set a release is done.
     */
    private val stateMutex = Semaphore(1, 1)

    protected abstract val mutableFlow: F
    val subscriptionCount
        get() =  mutableFlow.subscriptionCount

    /**
     * Provides a [Flow] of the [State] of this repo.
     *
     * @return The [Flow]
     */
    @Deprecated(message="StateRepo itself is now a Flow", replaceWith = ReplaceWith("StateFlow"))
    fun flow(): Flow<S> =
        mutableFlow

    internal open suspend fun initialize(initialValue:S? = null, first:Boolean = true): S {
        val value = initialValue ?: initialValue()
        mutableFlow.emit(value)
        if (first) stateMutex.release() // release the initial permit held
        println("released initial")
        // The mutex is already released above to let the state initialize afterwards, to avoid changeState mutex deadlocks
        // However releasing the above mutex might have already changed the state to a new state before initialState runs.
        // State machines that need initialization should rely on having an initialization state rather than using this method.
        value.initialState()
        return value
    }

    /**
     * Gets the initial value of the repo
     * @return the initial value of the repo
     */
    abstract suspend fun initialValue(): S

    internal suspend fun state(): S {
        // TODO: if this state machine is backed by a SharedFlow instead of a pure StateFlow this will suspend indefinitely if no state is set
        // this only occurs (normally) if the initial state is not set.
        // Perhaps an alternate way of throwing the exception (e.g. a boolean flag for the first emit) would be good,
        // however this does not guard manipulation of the SharedFlow before it is passed (e.g. MutableStateFlow's default value)
        // or directly on the flow (currently exposed to subclasses as a protected field)
        // in the meanwhile, there might also be legitimate use cases for suspending until first state.
        // So for now at least, we accept this possible deadlock
        return mutableFlow.first()
    }

    /**
     * Peek the current state of the state machine. The current state could change immediately after it is returned.
     *
     * Also no state could be set yet, in which case this method will block. Use [useState] for a suspending alternative.
     *
     * If any actions are taken based on the current state that affect the state machine you should not use this method.
     *
     * If your code relies on the state not changing use [useState].
     * If you want to change the state based on the current state use [takeAndChangeState]
     *
     * @return the current [State] of the [StateRepo]
     */
    fun peekState() = runBlocking { state() }

    /**
     * Makes the current [State] available in [action]. The state is guaranteed not to change during the execution of [action].
     * This operation ensures atomic state observations, so the state will not change while the [action] is being executed.
     *
     * This method uses a separate coroutineScope, meaning it will suspend until all child Jobs are completed, including those that asynchronously call this method itself (however a different state might be current at that point).
     *
     * @param action the function for which will [State] receive the state, guaranteed to be unchanged for the duration of the function.
     */
    suspend fun useState(action: suspend (S) -> Unit) = coroutineScope {
        stateMutex.withPermit {
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

    fun launchUseState(
        context:CoroutineContext = coroutineContext,
        action: suspend(S) -> Unit) = launch(context) {
            useState(action)
        }

    suspend fun takeAndChangeState(action: suspend(S) -> suspend () -> S) =
        doTakeAndChangeState(remainIfStateNot = null, action)

    /**
     * Changes from the current [State] to a new [State]. This operation ensures atomic state changes.
     * The new state is determined by an [action], which takes the current [State] upon starting the state transition and provides a deferred state creation.
     * You are strongly encouraged to use the [State] provided by the [action] to determine the new state, to ensure no illegal state transitions occur, as the state may have changed between calling [doTakeAndChangeState] and the execution of [action].
     * If the [action] returns [State.remain] no state transition will occur.
     * Since this operation is atomic, the [action] should not directly call [doTakeAndChangeState] itself. If required to do this, handle the additional transition in a separate coroutine.
     *
     * This method uses a separate coroutineScope, meaning it will suspend until all child Jobs are completed, including those that asynchronously call this method itself.
     *
     * @param remainIfStateNot If the current state at the time of Action is not an instance of this class, the state will automatically remain.
     * @param action Function to determine the [State] to be transitioned to from the current [State]. If no state transition should occur, return [State.remain]
     */
    suspend fun <K:S>takeAndChangeState(remainIfStateNot:KClass<K>, action: suspend(K) -> suspend () -> S) =
        doTakeAndChangeState(remainIfStateNot) {
            action(it)
        }

    fun <K:S>launchTakeAndChangeState(
        context:CoroutineContext = coroutineContext,
        remainIfStateNot:KClass<K>,
        action: suspend(K) -> suspend () -> S) = launch(context) {
            takeAndChangeState(remainIfStateNot) {
                action(it)
            }
        }

    fun launchTakeAndChangeState(
        context:CoroutineContext = coroutineContext,
        action: suspend(S) -> suspend () -> S) = launch(context) {
            takeAndChangeState(action)
        }

    private suspend inline fun <K:S>doTakeAndChangeState(remainIfStateNot: KClass<K>?, crossinline action: suspend(K) -> suspend () -> S): S = coroutineScope { // scope around the mutex so asynchronously scheduled coroutines that also use this method can run before the scope completed without deadlocks
        println("stateMutex available: ${stateMutex.availablePermits}")
        stateMutex.withPermit {
            val result = CompletableDeferred<S>()
            launch {
                try {
                    val beforeState = state()
                    // There are only two methods calling this private method.
                    // either K is the same as S (no `remainIfStateNot` parameter), or we do the isInstance check
                    @Suppress("UNCHECKED_CAST")
                    val transition = // if remainIfNot was passes, only execute action if the beforeState matches
                        when {
                            remainIfStateNot == null || remainIfStateNot.isInstance(beforeState) -> action(beforeState as K)
                            else -> beforeState.remain() // else just remain
                        }

                    if (beforeState.remain<S>() === transition) {
                        result.complete(beforeState)
                    } else {
                        (beforeState as? HandleBeforeCreating)?.beforeCreatingNewState()
                        val newState = transition()
                        (beforeState as? HandleAfterCreating<S>)?.afterCreatingNewState(newState)
                        (newState as? HandleBeforeOldStateIsRemoved<S>)?.beforeOldStateIsRemoved(beforeState)
                        mutableFlow.emit(newState)
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
abstract class HotStateRepo<S : State>(coroutineContext: CoroutineContext = Dispatchers.Main.immediate) :
    BaseHotStateRepo<S, MutableSharedFlow<S>>(coroutineContext) {
    final override val lazyMutableSharedFlow: Lazy<MutableSharedFlow<S>> = defaultLazySharedFlow()
}

abstract class HotStateFlowRepo<S : State>(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate,
    val initialState: (HotStateFlowRepo<S>) -> S
    ) : BaseHotStateRepo<S, MutableStateFlow<S>>(coroutineContext) {

    override val lazyMutableSharedFlow = lazy { MutableStateFlow(initialState(this)) }

    final override suspend fun initialValue(): S = mutableFlow.value
}

abstract class BaseHotStateRepo<S : State, F : MutableSharedFlow<S>>(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : StateRepo<S, F>(coroutineContext) {

    abstract val lazyMutableSharedFlow: Lazy<F>

    // guards once only initialization across threads
    private val initialized = AtomicBoolean(false)

    override val mutableFlow:F
    get() {
        val isInitialized = lazyMutableSharedFlow.isInitialized()
        val flow = lazyMutableSharedFlow.value
        if (!isInitialized && initialized.compareAndSet(expected = false, new = true))
            launch(coroutineContext) {
                initialize()
            }
        return flow
    }
}

abstract class BaseColdStateRepo<S:State, F:MutableSharedFlow<S>>(
    context: CoroutineContext = Dispatchers.Main.immediate
) : StateRepo<S, F>(context) {

    // guards once only initialization across threads
    private val initialized = AtomicBoolean(false)

    abstract val lazyMutableFlow: Lazy<F>

    override val mutableFlow:F
        get() {
            val isInitialized = lazyMutableFlow.isInitialized()
            val flow = lazyMutableFlow.value
            if (!isInitialized && initialized.compareAndSet(expected = false, new = true)) {
                launch(coroutineContext) {
                    flow.onCollectionEvent {
                        when (it) {
                            NoMoreCollections -> {
                                useState { finalState ->
                                    finalState.finalState()
                                    deinitialize(finalState)
                                }
                            }
                            FirstCollection -> firstInitialization()
                            LaterCollections -> laterInitializations()
                        }
                    }
                }
            }
            return flow
        }

    abstract suspend fun firstInitialization()

    abstract suspend fun laterInitializations()

    abstract suspend fun deinitialize(state: S)
}

open class ColdStateFlowRepo<S:State>(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate,
    val init: suspend (ColdStateFlowRepo<S>) -> S,
    val deinit: suspend (S, ColdStateFlowRepo<S>) -> Unit
) : BaseColdStateRepo<S, MutableStateFlow<S>>(
    context = coroutineContext,
) {
    val stateflow: StateFlow<S>
        get() = mutableFlow

    // the first initialization is done in the lazy block below since StateFlow requires an initial value
    final override suspend fun initialValue(): S = mutableFlow.value

    final override suspend fun deinitialize(state: S) {
        deinit(state, this)
    }

    override val lazyMutableFlow: Lazy<MutableStateFlow<S>> =
        lazy {
            runBlocking {
                MutableStateFlow(
                    init(this@ColdStateFlowRepo)
                )
            }
        }

    override suspend fun firstInitialization() {
        initialize(stateflow.value)
    }

    override suspend fun laterInitializations() {
        initialize(first = false)
    }
}


/**
 * A [StateRepo] that represents its [State] as a Cold flow. Data will only be set when the State repo is observed
 * *
 * This implementation uses a [MutableSharedFlow]. If you want to use a cold state repo based on StateFlow,
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
        initialize(first = false)
    }
}
