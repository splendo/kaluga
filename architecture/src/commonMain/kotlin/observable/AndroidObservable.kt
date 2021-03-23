/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.architecture.observable.ObservableOptional.Nothing
import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified R:T, T> ObservableOptional<T>.resultValueOrDefault(defaultValue: R): R =
    try {
        valueOrNull?.let { it as? R } ?: defaultValue }
    catch(e:ClassCastException) {
        defaultValue
    }

fun <R:T, T> ObservableOptional<T>.asResult(defaultValue: Value<R>?): ObservableOptional<R> =
    try {
        if (this is Value<*> && (this.value != null || defaultValue == null))
            Value(this.value as R)
        else
            defaultValue ?: Nothing()
    } catch(e:ClassCastException) {
        defaultValue ?: Nothing()
    }

/**
 * Result type for an [BaseObservable]. Used to allow for the distinction between `null` and optional values
 */
sealed class ObservableOptional<T> : ReadOnlyProperty<Any?, T?> {

    abstract val valueOrNull:T?

    /**
     * The [BaseObservable] has a result, this result could be `null`
     */
    data class Value<T>(val value: T) : ObservableOptional<T>() {

        override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
        override fun toString(): String = "Value[$value]"
        override val valueOrNull: T? = value
    }

    /**
     * The Observable does not have a result
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

open class ObservationDefault<R:T?, T>(
    override val defaultValue: Value<R>,
    override val initialValue: Value<T?>
) : Observation<R, T?, Value<R>>(initialValue),
    ReadWriteProperty<Any?, R>,
    MutableDefaultInitialized<R, T?>
{
    constructor(
        defaultValue: R,
        initialValue: Value<T?>
    ) : this(Value(defaultValue), initialValue)

    override fun getValue(thisRef: Any?, property: KProperty<*>): R = current
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: R) { observedValue = Value(value) }

    override val current: R
        get() = currentObserved.value

    override val stateFlow: MutableStateFlow<R> by lazy {
        MutableStateFlow(current).also { stateFlow ->
            this.observeInitialized { stateFlow.value = it }
        }
    }

    override val valueDelegate: ReadWriteProperty<Any?, R>
        get() = this

    override fun observeInitialized(onNext: (R) -> Unit): Disposable {
        return observe { onNext(it as R) }
    }
}

class ObservationUnInitialized<T>: Observation<T, T, ObservableOptional<T>>(Nothing()), MutableUninitialized<T>, ReadWriteProperty<Any?, T?> {

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

open class ObservationInitialized<T> (
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
        return observe { onNext(it as T)  }
    }

    override val valueDelegate: ReadWriteProperty<Any?, T>
        get() = this
}

open class Observation<R:T, T, OO:ObservableOptional<R>> (
    override val initialValue: ObservableOptional<T>
) : Initial<R, T> {

    open val defaultValue: Value<R>? = null

    private val mutableObserverList:MutableList<(R)->Unit> by lazy { sharedMutableListOf() }

    var onFirstObservation: (() -> Unit)? = null
        set(value) {
            // if an observation took place before this field was set, invoke immediately
            if (firstObservation.value) {
                value?.invoke()
            }
            else {
                field = value
            }
        }
    val firstObservation = AtomicBoolean(false)

    var beforeObservedValueGet: ((OO) -> ObservableOptional<T>)? = null


    private val observers by lazy {
        mutableObserverList
    }

    internal val observable:ReadWriteProperty<Any?, OO> by lazy {
        val initialValue = this.initialValue

        @Suppress("SENSELESS_COMPARISON") // in case of incorrect initialization this can still be null
        if (initialValue == null)
            throw RuntimeException("Observing before class is initialized. Are you observing from the constructor of your observable?")

        object : ReadWriteProperty<Any?, OO> {

            val ref = AtomicReference(initialValue.asResult(defaultValue))

            fun refValue() = ref.get()

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: OO) {
                val oldValue = refValue()

                val newValue:OO = value.asResult(defaultValue) as OO

                if (oldValue != newValue) { // propagate state, not assignment
                    ref.set(newValue)
                    // @Suppress("UNCHECKED_CAST") // OO should always be R
                    val result = (newValue as? Value<*>)?.value as R

                    observers.forEach { it(result) }
                }
            }

            override fun getValue(thisRef: Any?, property: KProperty<*>): OO {
                if (firstObservation.compareAndSet(expected = false, new = true)) {
                    onFirstObservation?.invoke()
                }
                return refValue() as OO
            }
        }
    }

    /**
     * Adds an observing function to the Observable to be notified on each change to the observable.
     * If there is a current value, there will be an immediate notification
     *
     * @param onNext Function to be called each time the value of the Observable changes
     * @return [Disposable] that removes the observing function when disposed
     */
     override fun observe(onNext: (R?) -> Unit): Disposable {
        observers.add(onNext)
        val lastResult = currentObserved
        @Suppress("UNCHECKED_CAST") // OO<T> should be guaranteed
        onNext((lastResult as? Value<*>)?.value as R)
        return SimpleDisposable { observers.remove(onNext) }
    }

    val currentObserved: OO
        get() = observedValue as OO

    var observedValue:ObservableOptional<T>
        // use getter and setter to avoid lazy initialization of observable
        get() {
            var observable by observable
            beforeObservedValueGet?.let { beforeObservedValueGet ->
                val current = observable
                val new = beforeObservedValueGet(current)
                @Suppress("UNUSED_VALUE") // changing delegated value will have actual side effects
                if (new != current) { // state not events
                    observable = new.asResult(defaultValue) as OO
                    println("obs now: $observable")
                }
                @Suppress("UNCHECKED_CAST") // should always downcast as R extends T
                return observable as ObservableOptional<T>
            }
            @Suppress("UNCHECKED_CAST") // should always downcast as R extends T
            return observable as ObservableOptional<T>
        }
        set(v) {
            @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
            var observable by observable
            @Suppress("UNUSED_VALUE", "UNCHECKED_CAST") // should always downcast as R extends T
            observable = v as OO
        }

    override val currentOrNull: R?
        get() = currentObserved.valueOrNull

}

/**
 * An observable value.
 *
 * The value that van be observed is of the type [ObservableOptional].
 * This is used to distinguish between "no value" [ObservableOptional.Nothing] and Value [ObservableOptional.Value] (which could be null).
 *
 * An [initialValue] value can be set, which will remain until an actual value is observed.
 * The actual implementation of holding the observed value is done by [observation],
 * by default this is implemented by a [Observation] which can contain platform specific method of
 * observing (e.g LiveData on Android).
 *
 * Observable also implements [ReadOnlyProperty] so it can act as a delegate for an [ObservableOptional] val
 */
abstract class BaseObservable<R:T, T, OO : ObservableOptional<R>>(
    initialValue: ObservableOptional<T>,
    protected open val observation: Observation<R, T, OO> = Observation(initialValue)
) : BasicObservable<R, T, OO>,
    Initial<R, T> by observation
{
    constructor(observation: Observation<R, T, OO>) : this (observation.initialValue, observation)
}

class SimpleInitializedObservable<T>(
    initialValue: T
) : BaseInitializedObservable<T>(Value(initialValue))

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class BaseInitializedObservable<T>(
    override val initialValue: Value<T>,
    override val observation: ObservationInitialized<T> = ObservationInitialized(initialValue)
) : BaseObservable<T, T, Value<T>>(observation),
    InitializedObservable<T>,
    Initialized<T, T> by observation
    {
    constructor(observation: ObservationInitialized<T>): this (observation.initialValue, observation)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<T> = observation.currentObserved
}

interface BasicObservable<R:T, T, OO : ObservableOptional<R>>: ReadOnlyProperty<Any?, OO>, Initial<R, T>

interface InitializedObservable<T>:BasicObservable<T, T, Value<T>>, Initialized<T, T>

interface UninitializedObservable<T>:BasicObservable<T, T, ObservableOptional<T>>, Uninitialized<T>

interface BasicSubject<R:T, T, OO : ObservableOptional<R>>:BasicObservable<R, T, OO>, SuspendableSetter<T> {
    fun bind(coroutineScope: CoroutineScope, context: CoroutineContext)
}

interface UninitializedSubject<T>:BasicSubject<T, T, ObservableOptional<T>>, UninitializedObservable<T>, MutableUninitialized<T>

interface InitializedSubject<T>:BasicSubject<T, T, Value<T>>, InitializedObservable<T>, MutableInitialized<T, T>

interface DefaultObservable<R:T?, T>:BasicObservable<R, T?, Value<R>>, DefaultInitialized<R, T?>

interface DefaultSubject<R:T?, T>:BasicSubject<R, T?, Value<R>>, DefaultObservable<R, T>, MutableDefaultInitialized<R, T?>

interface WithState<T> {
    val stateFlow:StateFlow<T>
    val valueDelegate:ReadOnlyProperty<Any?, T>
}

interface WithMutableState<T>:WithState<T> {
    override val stateFlow: MutableStateFlow<T>
    override val valueDelegate:ReadWriteProperty<Any?, T>
}

interface MutableDefaultInitialized<R:T?, T>:DefaultInitialized<R, T?>, WithMutableState<R>

interface DefaultInitialized<R:T?, T>:Initialized<R, T?>, WithState<R> {
    val defaultValue:Value<R>
}

interface MutableUninitialized<T>:Uninitialized<T>, WithMutableState<T?>
interface Uninitialized<T>:Initial<T, T>, WithState<T?> {
    override val initialValue:Nothing<T>
}

interface MutableInitialized<R:T, T>:Initialized<R, T>, WithMutableState<R>
interface Initialized<R:T, T>:Initial<R, T>, WithState<R> {
    override val initialValue:Value<T>
    val current: R
    fun observeInitialized(onNext: (R) -> Unit): Disposable
}

interface Initial<R:T, T> {
    val initialValue:ObservableOptional<T>
    val currentOrNull:T?
    fun observe(onNext: (R?) -> Unit): Disposable
}

interface SuspendableSetter<T>:Postable<T> {
    /**
     * Updates the value of this [Postable]
     * @param newValue The new value of the postable
     */
    suspend fun set(newValue: T)
}


/**
 * A Postable can handle values being posted to it.
 */
interface Postable<T> {
    /**
     * Updates the value of this [Postable]
     * @param newValue The new value of the postable
     */

    fun post(newValue: T)
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE") // we want our delegate to override
abstract class BaseDefaultObservable<R:T?, T>(
    defaultValue: R,
    override val initialValue: Value<T?>,
    override val observation: ObservationDefault<R, T?> = ObservationDefault(Value(defaultValue), initialValue)
) :
    BaseObservable<R, T?, Value<R>>(observation.initialValue, observation),
    DefaultObservable<R, T?>,
    DefaultInitialized<R, T?> by observation
{
    constructor(observation: ObservationDefault<R, T?>): this(observation.defaultValue.value, observation.initialValue, observation)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<R> =
        observation.currentObserved
}

/**
 * A Subject is an [BaseObservable] with a value that can be changed using the [post] method from the [Postable] interface
 */
abstract class BaseSubject<R:T, T, OO : ObservableOptional<R>>(
    initialValue: ObservableOptional<T>,
    observation: Observation<R, T, OO>,
    private val stateFlowToBind:StateFlow<R?>)
    : BaseObservable<R, T, OO>(initialValue, observation), BasicSubject<R, T, OO> {

    final override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext) {
        coroutineScope.launch(context) {
            stateFlowToBind.collect { set(it as T) }
        }
    }
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class BaseInitializedSubject<T>(
    initialValue: Value<T>,
    override val observation: ObservationInitialized<T> = ObservationInitialized(initialValue)
) : BaseSubject<T, T, Value<T>>(
        observation.initialValue,
        observation,
        observation.stateFlow
    ),
    InitializedSubject<T>,
    MutableInitialized<T, T> by observation {

    constructor(initialValue: T, observation: ObservationInitialized<T> = ObservationInitialized(Value(initialValue))): this(observation)
    constructor(observation: ObservationInitialized<T>): this(observation.initialValue, observation)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<T> =
        observation.currentObserved
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class BaseUninitializedSubject<T>(
    override val observation: ObservationUnInitialized<T>
) : BaseSubject<T, T, ObservableOptional<T>> (
    Nothing(),
    observation,
    observation.stateFlow
),  UninitializedSubject<T>,
    MutableUninitialized<T> by observation
{
    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableOptional<T> =
        observation.observedValue

}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class BaseUninitializedObservable<T>(
    override val observation: ObservationUnInitialized<T>
) :BaseObservable<T, T, ObservableOptional<T>> (
    Nothing(),
    observation
),  UninitializedObservable<T>,
    Uninitialized<T> by observation {

    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableOptional<T> =
        observation.currentObserved
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
class SimpleInitializedSubject<T>(
    override val initialValue: Value<T>,
    override val observation: ObservationInitialized<T> = ObservationInitialized(initialValue)
) : BaseSubject<T, T, Value<T>>(
        initialValue,
        observation,
        observation.stateFlow
    ),
    InitializedSubject<T>,
    MutableInitialized<T, T> by observation {

    constructor(initialValue: T):this(Value(initialValue))

    override suspend fun set(newValue: T) = post(newValue)

    override fun post(newValue: T) {
        observation.observedValue = Value(newValue)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<T> =
        observation.currentObserved
}

class SimpleDefaultSubject<R:T?, T>(
    defaultValue: R,
    initialValue: T? = defaultValue,
    ) :
    BaseDefaultSubject<R, T?> (
        Value(defaultValue),
        Value(initialValue)) {

    override fun post(newValue: T?) {
        observation.observedValue = Value(newValue)
    }

    override suspend fun set(newValue: T?) = post(newValue)

}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE") // deliberate
abstract class BaseDefaultSubject<R:T?, T>(
    override val defaultValue: Value<R>,
    override val initialValue: Value<T?>,
    override val observation: ObservationDefault<R, T?> = ObservationDefault(defaultValue, initialValue)
) :
    BaseSubject<R, T?, Value<R>>(
        observation.initialValue,
        observation,
        observation.stateFlow
    ),
    DefaultSubject<R, T>,
    MutableDefaultInitialized<R, T?> by observation
{

    constructor(observation: ObservationDefault<R, T?>) : this (observation.defaultValue, observation.initialValue, observation)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Value<R> =
        observation.currentObserved

}
