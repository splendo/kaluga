/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.observable

import com.splendo.kaluga.base.KalugaThread
import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.isOnMainThread
import com.splendo.kaluga.base.runBlocking
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Class that handles observing value changes for a value of a given type
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param OO the type of [ObservableOptional] to store the result in.
 * @param initialValue The initial value this observation should contain.
 */
open class Observation<R : T, T, OO : ObservableOptional<R>>(
    override val initialValue: ObservableOptional<T>,
) : Initial<R, T> {

    // this is not used by iOS
    internal val observers by lazy { concurrentMutableListOf<(R) -> Unit>() }

    /**
     * The default [ObservableOptional.Value] to return if the current value is [ObservableOptional.Nothing] or [ObservableOptional.Value] containing `null`.
     */
    open val defaultValue: ObservableOptional.Value<R>? = null

    /**
     * Action to execute when this observable is observed for the first time.
     */
    var onFirstObservation: (() -> Unit)? = null
        set(value) {
            // if an observation took place before this field was set, invoke immediately
            if (firstObservation.value) {
                value?.invoke()
            } else {
                field = value
            }
        }

    private val firstObservation = atomic(false)

    /**
     * Transformation to apply to the latest value when [observedValue] is called.
     */
    var beforeObservedValueGet: ((OO) -> ObservableOptional<T>)? = null

    @Suppress("SENSELESS_COMPARISON") // if not initialized this can still happen
    private var backingInternalValue: ObservableOptional<R>? = null
        get() =
            field ?: if (initialValue == null) {
                throw RuntimeException("Observing before class is initialized. Are you observing from the constructor of your observable?")
            } else {
                initialValue.asResult(defaultValue)
            }
        set(value) {
            checkNotNull(value) { "internal value can not be set to null" }
            field = value
        }

    /**
     *  set the value of this Observable from a suspended context.
     */
    suspend fun setValue(value: ObservableOptional<T>): ObservableOptional<T> = if (isOnMainThread) {
        setValueUnconfined(value)
    } else {
        withContext(Dispatchers.Main) {
            setValueUnconfined(value)
        }
    }

    @Suppress("UNCHECKED_CAST") // should always downcast as R extends T
    /**
     * Used internally to set the [observedValue]. Make sure you are in [Dispatchers.Main] or the main thread
     *
     * @param value the new value that will be observed
     */
    private fun setValueUnconfined(value: ObservableOptional<T>): ObservableOptional<T> {
        val v = value.asResult(defaultValue)
        val before = backingInternalValue

        if (before != v) {
            backingInternalValue = v

            val result = (v as? ObservableOptional.Value<*>)?.value as R

            observers(this@Observation).forEach {
                it(result)
            }
            return v as ObservableOptional<T>
        }
        return before as ObservableOptional<T>
    }

    private fun getValue(): OO {
        if (firstObservation.compareAndSet(expect = false, update = true)) {
            onFirstObservation?.invoke()
        }

        @Suppress("UNCHECKED_CAST")
        return handleOnMain {
            backingInternalValue ?: error("unexpected null")
        } as OO
    }

    /**
     * Adds an observing function to the Observable to be notified on each change to the observable.
     * If there is a current value, there will be an immediate notification
     *
     * @param onNext Function to be called each time the value of the Observable changes
     * @return [Disposable] that removes the observing function when disposed
     */
    @Suppress("UNCHECKED_CAST")
    override fun observe(onNext: (R?) -> Unit): Disposable = handleOnMain {
        // send the value before adding
        val lastResult = currentObserved
        onNext((lastResult as? ObservableOptional.Value<*>)?.value as R)
        addObserver(this@Observation, onNext)
        // adding an observer often happens concurrently with initialization,
        // if we detect a change in the current observed value we re-send it to the added observer
        val newResult = currentObserved
        if (newResult != lastResult) {
            onNext((newResult as? ObservableOptional.Value<*>)?.value as R)
        }
        SimpleDisposable {
            handleOnMain {
                removeObserver(this@Observation, onNext)
            }
        }
    }

    /**
     * The currently observed [OO]
     */
    val currentObserved: OO
        @Suppress("UNCHECKED_CAST")
        get() = observedValue as OO

    /**
     * The [ObservableOptional] or [T] that is currently being observed.
     */
    @Suppress("UNCHECKED_CAST")
    var observedValue: ObservableOptional<T>
        // use getter and setter to avoid lazy initialization of observable
        get() {
            beforeObservedValueGet?.let { beforeObservedValueGet ->
                val current = getValue()
                val new = beforeObservedValueGet(current)
                return if (new != current) {
                    // state not events
                    handleOnMain {
                        setValueUnconfined(new)
                    }
                } else {
                    new
                }
            }
            return getValue() as ObservableOptional<T>
        }
        set(v) {
            handleOnMain {
                setValueUnconfined(v)
            }
        }

    override val currentOrNull: R?
        get() = currentObserved.valueOrNull

    private fun <T> handleOnMain(block: () -> T): T = if (KalugaThread.currentThread.isMainThread) {
        block()
    } else {
        runBlocking(Dispatchers.Main) {
            block()
        }
    }
}

/**
 * An [Observation] that implements [MutableInitialized]
 * @param T the type of value to expect.
 * @param initialValue The initial [ObservableOptional.Value] this observation should contain.
 */
open class ObservationInitialized<T>(
    override val initialValue: ObservableOptional.Value<T>,
) : Observation<T, T, ObservableOptional.Value<T>>(initialValue),
    ReadWriteProperty<Any?, T>,
    MutableInitialized<T, T> {

    /**
     * Constructor
     * @param initialValue The initial [T] this observation should contain.
     */
    constructor(initialValue: T) : this(ObservableOptional.Value(initialValue))

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        observedValue = ObservableOptional.Value(value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = current
    override val current: T
        get() = currentObserved.value

    override val stateFlow: MutableStateFlow<T> by lazy {
        MutableStateFlow(current).also { stateFlow ->
            observeInitialized { stateFlow.value = it }
        }
    }

    override fun observeInitialized(onNext: (T) -> Unit): Disposable {
        @Suppress("UNCHECKED_CAST")
        return observe { onNext(it as T) }
    }

    override val valueDelegate: ReadWriteProperty<Any?, T>
        get() = this
}

/**
 * An [Observation] that implements [MutableUninitialized]
 * @param T the type of value to expect.
 */
class ObservationUninitialized<T> :
    Observation<T, T, ObservableOptional<T>>(ObservableOptional.Nothing()),
    MutableUninitialized<T>,
    ReadWriteProperty<Any?, T?> {

    override val initialValue: ObservableOptional.Nothing<T> = ObservableOptional.Nothing()

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = currentObserved.valueOrNull

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        observedValue = value?.let { ObservableOptional.Value(value) } ?: ObservableOptional.Nothing()
    }

    override val stateFlow: MutableStateFlow<T?> by lazy {
        MutableStateFlow(currentOrNull).also { stateFlow ->
            observe { stateFlow.value = it }
        }
    }
    override val valueDelegate: ReadWriteProperty<Any?, T?>
        get() = this
}

/**
 * An [Observation] that implements [MutableDefaultInitialized]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param defaultValue The default [ObservableOptional.Value] to return if the current value is [ObservableOptional.Nothing] or [ObservableOptional.Value] containing `null`.
 * @param initialValue The initial [ObservableOptional.Value] this observation should contain.
 */
open class ObservationDefault<R : T?, T>(
    override val defaultValue: ObservableOptional.Value<R>,
    override val initialValue: ObservableOptional.Value<T?>,
) : Observation<R, T?, ObservableOptional.Value<R>>(initialValue),
    ReadWriteProperty<Any?, R>,
    MutableDefaultInitialized<R, T?> {

    /**
     * Constructor
     * @param defaultValue The default [R] to return if the current value is [ObservableOptional.Nothing] or [ObservableOptional.Value] containing `null`.
     * @param initialValue The initial [ObservableOptional.Value] this observation should contain.
     */
    constructor(
        defaultValue: R,
        initialValue: ObservableOptional.Value<T?>,
    ) : this(ObservableOptional.Value<R>(defaultValue), initialValue)

    override fun getValue(thisRef: Any?, property: KProperty<*>): R = current
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: R) {
        observedValue = ObservableOptional.Value(value)
    }

    override val current: R
        get() = currentObserved.value

    override val stateFlow: MutableStateFlow<R> by lazy {
        MutableStateFlow(current).also { stateFlow ->
            this.observeInitialized {
                stateFlow.value = it
            }
        }
    }

    override val valueDelegate: ReadWriteProperty<Any?, R>
        get() = this

    override fun observeInitialized(onNext: (R) -> Unit): Disposable {
        @Suppress("UNCHECKED_CAST")
        return observe { onNext(it as R) }
    }
}

/**
 * Observes a [Flow] and sets updates value to an [Observation] while it is being observed
 * @param observation The [Observation] to observe the [Flow]
 * @param coroutineScope The [CoroutineScope] on which to observe the [Flow]
 * @param context The [CoroutineContext] in which to observe the [Flow]
 * @param flow The [Flow] to observe.
 */
fun <R : T, T, OO : ObservableOptional<R>> observeFlow(
    observation: Observation<R, T, OO>,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    flow: Flow<T>,
) {
    observation.onFirstObservation = {
        coroutineScope.launch(context) {
            flow.collect {
                observation.setSuspendedIfNot(it)
            }
        }
    }
}

internal fun <R : T, T, OO : ObservableOptional<R>> Observation<R, T, OO>.setIfNot(value: T) {
    if (when (val current = observedValue) {
            is ObservableOptional.Value<T> -> current.value != value
            is ObservableOptional.Nothing<T> -> true
        }
    ) {
        observedValue = ObservableOptional.Value(value)
    }
}

internal suspend fun <R : T, T, OO : ObservableOptional<R>> Observation<R, T, OO>.setSuspendedIfNot(value: T) {
    if (when (val current = observedValue) {
            is ObservableOptional.Value<T> -> current.value != value
            is ObservableOptional.Nothing<T> -> true
        }
    ) {
        setValue(ObservableOptional.Value(value))
    }
}
