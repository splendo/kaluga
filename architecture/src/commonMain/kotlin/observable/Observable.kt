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

package com.splendo.kaluga.architecture.observable

import com.splendo.kaluga.architecture.observable.ObservableOptional.Nothing
import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import com.splendo.kaluga.base.KalugaThread
import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.isOnMainThread
import com.splendo.kaluga.base.runBlocking
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Casts the value of an [ObservableOptional] to its subtype [R] or returns [defaultValue] if no such casting is possible.
 * @param defaultValue The default [R] to be returned if [ObservableOptional.valueOrNull] is not [R] or `null`.
 * @return The current value [R] if it exists or [defaultValue]
 */
inline fun <reified R : T, T> ObservableOptional<T>.resultValueOrDefault(defaultValue: R): R =
    try {
        valueOrNull?.let { it as? R } ?: defaultValue
    } catch (e: ClassCastException) {
        defaultValue
    }

/**
 * Casts an [ObservableOptional] to an [ObservableOptional] its subtype [R] or returns [defaultValue] if no such casting is possible.
 * @param defaultValue The default [Value] of [R] to be returned if [ObservableOptional] is not a [Value] of [R] or `null`.
 * @return An [ObservableOptional] containing the current [Value] of [R] if it exists or [defaultValue]
 */
fun <R : T, T> ObservableOptional<T>.asResult(defaultValue: Value<R>?): ObservableOptional<R> =
    try {
        if (this is Value<*> && (this.value != null || defaultValue == null))
            @Suppress("UNCHECKED_CAST")
            Value(this.value as R)
        else
            defaultValue ?: Nothing()
    } catch (e: ClassCastException) {
        defaultValue ?: Nothing()
    }

/**
 * Result type for an [BaseObservable]. Used to allow for the distinction between `null` and optional values.
 * @param T the type this [ObservableOptional] represents. Can be a nullable.
 */
sealed class ObservableOptional<T> : ReadOnlyProperty<Any?, T?> {

    /**
     * The value of [T] or `null` if this result is [Nothing].
     */
    abstract val valueOrNull: T?

    /**
     * The [BaseObservable] has a result, this result could be `null`.
     * @param T the type of the [Value]
     * @property value the value of this result. Can be `null`.
     */
    data class Value<T>(val value: T) : ObservableOptional<T>() {

        override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
        override fun toString(): String = "Value[$value]"
        override val valueOrNull: T? = value
    }

    /**
     * The Observable does not have a result.
     * @param T the type of [Value] that corresponds to this empty result.
     */
    class Nothing<T> : ObservableOptional<T>() {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return null
        }

        override val valueOrNull: T? = null
        override fun toString(): String = "Nothing"

        override fun equals(other: Any?): Boolean {
            return other is Nothing<*>
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }
    }
}

/**
 * An [Observation] that implements [MutableDefaultInitialized]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 */
open class ObservationDefault<R : T?, T : Any>(
    override val defaultValue: Value<R>,
    override val initialValue: Value<T?>
) : Observation<R, T?, Value<R>>(initialValue),
    ReadWriteProperty<Any?, R>,
    MutableDefaultInitialized<R, T> {

    constructor(
        defaultValue: R,
        initialValue: Value<T?>
    ) : this(Value<R>(defaultValue), initialValue)

    override fun getValue(thisRef: Any?, property: KProperty<*>): R = current
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: R) {
        observedValue = Value(value)
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
 * An [Observation] that implements [MutableUninitialized]
 */
class ObservationUninitialized<T> :
    Observation<T, T, ObservableOptional<T>>(Nothing()),
    MutableUninitialized<T>,
    ReadWriteProperty<Any?, T?> {

    override val initialValue: Nothing<T> = Nothing()

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? =
        currentObserved.valueOrNull

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        observedValue = value?.let { Value(value) } ?: Nothing()
    }

    override val stateFlow: MutableStateFlow<T?> by lazy {
        MutableStateFlow(currentOrNull).also { stateFlow ->
            observe { stateFlow.value = it }
        }
    }
    override val valueDelegate: ReadWriteProperty<Any?, T?>
        get() = this
}

open class ObservationInitialized<T>(
    override val initialValue: Value<T>
) : Observation<T, T, Value<T>>(initialValue),
    ReadWriteProperty<Any?, T>,
    MutableInitialized<T, T> {

    constructor(initialValue: T) : this(Value(initialValue))

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        observedValue = Value(value)
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

open class Observation<R : T, T, OO : ObservableOptional<R>>(
    override val initialValue: ObservableOptional<T>
) : Initial<R, T> {

    // this is not used by iOS
    internal val observers by lazy { concurrentMutableListOf<(R) -> Unit>() }

    open val defaultValue: Value<R>? = null

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

    var beforeObservedValueGet: ((OO) -> ObservableOptional<T>)? = null

    @Suppress("SENSELESS_COMPARISON") // if not initialized this can still happen
    private var backingInternalValue: ObservableOptional<R>? = null
        get() =
            field ?: if (initialValue == null) // "lazy" var
                throw RuntimeException("Observing before class is initialized. Are you observing from the constructor of your observable?")
            else
                initialValue.asResult(defaultValue)
        set(value) {
            checkNotNull(value) { "internal value can not be set to null" }
            field = value
        }

    /**
     *  set the value of this Observable from a suspended context.
     */
    suspend fun setValue(value: ObservableOptional<T>): ObservableOptional<T> =
        if (isOnMainThread) {
            setValueUnconfined(value)
        } else withContext(Dispatchers.Main) {
            setValueUnconfined(value)
        }

    @Suppress("UNUSED_VALUE", "UNCHECKED_CAST") // should always downcast as R extends T
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

            val result = (v as? Value<*>)?.value as R

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
        onNext((lastResult as? Value<*>)?.value as R)
        addObserver(this@Observation, onNext)
        // adding an observer often happens concurrently with initialization,
        // if we detect a change in the current observed value we re-send it to the added observer
        val newResult = currentObserved
        if (newResult != lastResult)
            onNext((newResult as? Value<*>)?.value as R)
        SimpleDisposable {
            handleOnMain {
                removeObserver(this@Observation, onNext)
            }
        }
    }

    val currentObserved: OO
        @Suppress("UNCHECKED_CAST")
        get() = observedValue as OO

    @Suppress("UNCHECKED_CAST")
    var observedValue: ObservableOptional<T>
        // use getter and setter to avoid lazy initialization of observable
        get() {
            beforeObservedValueGet?.let { beforeObservedValueGet ->
                val current = getValue()
                val new = beforeObservedValueGet(current)
                return if (new != current) // state not events
                    handleOnMain {
                        setValueUnconfined(new)
                    }
                else new
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
 * An observable value.
 *
 * The value that can be observed is of the type [ObservableOptional].
 * This is used to distinguish between "no value" [ObservableOptional.Nothing] and Value [ObservableOptional.Value] (which could be null).
 *
 * An [initialValue] value can be set, which will remain until an actual value is observed.
 * The actual implementation of holding the observed value is done by [Observation].
 *
 * Observable also implements [ReadOnlyProperty] so it can act as a delegate for an [ObservableOptional] val
 */
abstract class BaseObservable<R : T, T, OO : ObservableOptional<R>>(
    protected open val observation: Observation<R, T, OO>
) : BasicObservable<R, T, OO>,
    Initial<R, T> by observation {
    constructor(
        initialValue: ObservableOptional<T>,
    ) : this(Observation(initialValue))
}

class SimpleInitializedObservable<T>(
    initialValue: T
) : BaseInitializedObservable<T>(Value(initialValue))

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class BaseInitializedObservable<T>(
    observation: ObservationInitialized<T>
) : BaseObservable<T, T, Value<T>>(observation),
    InitializedObservable<T>,
    Initialized<T, T> by observation {

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<T> =
        observation.currentObserved

    constructor(
        initialValue: Value<T>
    ) : this(ObservationInitialized(initialValue))
}

interface BasicObservable<R : T, T, OO : ObservableOptional<R>> :
    ReadOnlyProperty<Any?, OO>,
    Initial<R, T>

interface InitializedObservable<T> : BasicObservable<T, T, Value<T>>, Initialized<T, T>

interface UninitializedObservable<T> :
    BasicObservable<T, T, ObservableOptional<T>>,
    Uninitialized<T>

interface BasicSubject<R : T, T, OO : ObservableOptional<R>> :
    BasicObservable<R, T, OO>,
    SuspendableSetter<T> {
    fun bind(coroutineScope: CoroutineScope)
    fun bind(coroutineScope: CoroutineScope, context: CoroutineContext)
}

interface UninitializedSubject<T> :
    BasicSubject<T, T, ObservableOptional<T>>,
    UninitializedObservable<T>,
    MutableUninitialized<T>

interface InitializedSubject<T> :
    BasicSubject<T, T, Value<T>>,
    InitializedObservable<T>,
    MutableInitialized<T, T>

interface DefaultObservable<R : T?, T : Any> :
    BasicObservable<R, T?, Value<R>>,
    DefaultInitialized<R, T>

interface DefaultSubject<R : T?, T : Any> :
    BasicSubject<R, T?, Value<R>>,
    DefaultObservable<R, T>,
    MutableDefaultInitialized<R, T>

/**
 * Interface indicating an observable has a state of [T]
 * @param T the type of the state.
 */
expect interface WithState<T> {

    /**
     * [StateFlow] that expresses the content from the observable.
     *
     * This can be initialized lazily.
     *
     * Accessing this from property outside the main thread might cause a race condition,
     * since observing the initial value needed for the stateflow has to happen on the main thread.
     *
     */
    val stateFlow: StateFlow<T>

    /**
     * A [ReadOnlyProperty] of [T]
     */
    val valueDelegate: ReadOnlyProperty<Any?, T>
}

/**
 * A [WithState] where [stateFlow] is mutable
 */
interface WithMutableState<T> : WithState<T> {

    /**
     * [MutableStateFlow] that expresses the content from the observable.
     *
     * This can be initialized lazily.
     *
     * Accessing this from property outside the main thread might cause a race condition,
     * since observing the initial value needed for the stateflow has to happen on the main thread.
     *
     */
    override val stateFlow: MutableStateFlow<T>

    /**
     * A [ReadWriteProperty] of [T]
     */
    override val valueDelegate: ReadWriteProperty<Any?, T>
}

/**
 * A [DefaultInitialized] that implements [WithMutableState]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 */
interface MutableDefaultInitialized<R : T?, T : Any> : DefaultInitialized<R, T>, WithMutableState<R>

/**
 * An [Initialized] that has a [defaultValue]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 */
interface DefaultInitialized<R : T?, T : Any> : Initialized<R, T?> {
    /**
     * The default [Value] of [R]. Can be used in case [Nothing] is [Initial.initialValue]
     */
    val defaultValue: Value<R>
}

interface MutableUninitialized<T> : Uninitialized<T>, WithMutableState<T?>

/**
 * An [Initial] that has [Nothing] as its [initialValue]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 */
interface Uninitialized<T> : Initial<T, T>, WithState<T?> {
    override val initialValue: Nothing<T>
}

/**
 * An [Initialized] that implements [WithMutableState].
 */
interface MutableInitialized<R : T, T> : Initialized<R, T>, WithMutableState<R>

/**
 * An [Initial] that has [Value] as its [initialValue]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 */
interface Initialized<R : T, T> : Initial<R, T>, WithState<R> {
    override val initialValue: Value<T>

    /**
     * The current value of [R]
     */
    val current: R
    fun observeInitialized(onNext: (R) -> Unit): Disposable
}

/**
 * Marks a class that has an initial [ObservableOptional] value, where [T] is the expected input value and [R] is its result.
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 */
interface Initial<R : T, T> {
    /**
     * The initial [ObservableOptional] value of [T].
     */
    val initialValue: ObservableOptional<T>

    /**
     * The current value [T] or `null` being observed
     */
    val currentOrNull: T?

    /**
     * Creates an observation that calls [onNext] each time a new value is observed until the resulting [Disposable] is [disposed][Disposable.dispose]
     */
    fun observe(onNext: (R?) -> Unit): Disposable
}

/**
 * A [Postable] that can be set in a suspended manner.
 * @param T the type of value that can be posted.
 */
interface SuspendableSetter<T> : Postable<T> {
    /**
     * Updates the value of this [Postable] in a suspended manner.
     * @param newValue The new value of the postable
     */
    suspend fun set(newValue: T)
}

/**
 * A Postable can handle values being posted to it.
 * @param T the type of value that can be posted.
 */
interface Postable<T> {
    /**
     * Updates the value of this [Postable]
     * @param newValue The new value of the postable
     */

    fun post(newValue: T)
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE") // we want our delegate to override
abstract class BaseDefaultObservable<R : T?, T : Any>(
    override val observation: ObservationDefault<R, T>
) :
    BaseObservable<R, T?, Value<R>>(observation),
    DefaultObservable<R, T>,
    DefaultInitialized<R, T> by observation {
    constructor(
        defaultValue: R,
        initialValue: Value<T?>
    ) : this(ObservationDefault<R, T>(Value<R>(defaultValue), initialValue))

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<R> =
        observation.currentObserved
}

/**
 * A Subject is an [BaseObservable] with a value that can be changed using the [post] method from the [Postable] interface
 */
abstract class AbstractBaseSubject<R : T, T, OO : ObservableOptional<R>>(
    observation: Observation<R, T, OO>,
    private val stateFlowToBind: suspend () -> StateFlow<R?>
) :
    BaseObservable<R, T, OO>(observation), BasicSubject<R, T, OO> {

    final override fun bind(coroutineScope: CoroutineScope) =
        bind(coroutineScope, coroutineScope.coroutineContext)

    override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext) {
        coroutineScope.launch(context) {
            @Suppress("UNCHECKED_CAST")
            stateFlowToBind().collect { set(it as T) }
        }
    }
}

expect abstract class BaseSubject<R : T, T, OO : ObservableOptional<R>>(
    observation: Observation<R, T, OO>,
    stateFlowToBind: suspend () -> StateFlow<R?>
) :
    AbstractBaseSubject<R, T, OO>

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class AbstractBaseInitializedSubject<T>(override val observation: ObservationInitialized<T>) :
    BaseSubject<T, T, Value<T>>(
        observation,
        suspend {
            // switch context to Main since value observations for observable also happen on that thread
            withContext(Dispatchers.Main.immediate) {
                observation.stateFlow
            }
        }
    ),
    InitializedSubject<T>,
    MutableInitialized<T, T> by observation {

    constructor(
        initialValue: Value<T>,
    ) : this(ObservationInitialized(initialValue))

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<T> =
        observation.currentObserved
}

expect abstract class BaseInitializedSubject<T>(observation: ObservationInitialized<T>) :
    AbstractBaseInitializedSubject<T> {
    constructor(
        initialValue: Value<T>
    )
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class AbstractBaseUninitializedSubject<T>(
    override val observation: ObservationUninitialized<T>
) : BaseSubject<T, T, ObservableOptional<T>>(
    observation,
    { observation.stateFlow }
),
    UninitializedSubject<T>,
    MutableUninitialized<T> by observation {
    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableOptional<T> =
        observation.observedValue
}

expect abstract class BaseUninitializedSubject<T>(
    observation: ObservationUninitialized<T>
) : AbstractBaseUninitializedSubject<T>

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class BaseUninitializedObservable<T>(
    override val observation: ObservationUninitialized<T>
) : BaseObservable<T, T, ObservableOptional<T>>(
    observation
),
    UninitializedObservable<T>,
    Uninitialized<T> by observation {

    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableOptional<T> =
        observation.currentObserved
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
class SimpleInitializedSubject<T>(
    override val observation: ObservationInitialized<T>
) : BaseInitializedSubject<T>(
    observation
) {

    constructor(
        initialValue: T
    ) : this(Value(initialValue))

    constructor(
        initialValue: Value<T>
    ) : this(ObservationInitialized(initialValue))

    override suspend fun set(newValue: T) {
        observation.setValue(Value(newValue))
    }

    override fun post(newValue: T) {
        observation.observedValue = Value(newValue)
    }
}

class SimpleDefaultSubject<R : T?, T : Any>(
    defaultValue: R,
    initialValue: T? = defaultValue
) :
    BaseDefaultSubject<R, T>(
        Value(defaultValue),
        Value(initialValue)
    ) {

    override fun post(newValue: T?) {
        observation.observedValue = Value(newValue)
    }

    override suspend fun set(newValue: T?) {
        observation.setValue(Value(newValue))
    }
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE") // deliberate
abstract class AbstractBaseDefaultSubject<R : T?, T : Any>(
    override val observation: ObservationDefault<R, T>
) : BaseSubject<R, T?, Value<R>>(
    observation,
    { observation.stateFlow }
),
    DefaultSubject<R, T>,
    MutableDefaultInitialized<R, T> by observation {
    constructor(
        defaultValue: Value<R>,
        initialValue: Value<T?>
    ) : this(observation = ObservationDefault<R, T>(defaultValue, initialValue))

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<R> =
        observation.currentObserved
}

expect abstract class BaseDefaultSubject<R : T?, T : Any>(
    observation: ObservationDefault<R, T>
) : AbstractBaseDefaultSubject<R, T> {

    constructor(
        defaultValue: Value<R>,
        initialValue: Value<T?>
    )
}
