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

import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty

/**
 * A read-write property that can be observed.
 * The value that can be changed using the [post] method from the [Postable] interface
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param OO the type of [ObservableOptional] to store the result in.
 */
interface BasicSubject<R : T, T, OO : ObservableOptional<R>> :
    BasicObservable<R, T, OO>,
    SuspendableSetter<T> {

    /**
     * Bind changes in value to a [CoroutineScope] in a [CoroutineContext]
     * @param coroutineScope The [CoroutineScope] to bind to.
     * @param context The [CoroutineContext] to bind to.
     */
    fun bind(coroutineScope: CoroutineScope, context: CoroutineContext)
}

/**
 * Convenience method for [BasicSubject] that calls [BasicSubject.bind] using the [CoroutineContext] of the provided [CoroutineScope]
 * @param coroutineScope The [CoroutineScope] to bind to.
 */
fun <R : T, T, OO : ObservableOptional<R>> BasicSubject<R, T, OO>.bind(coroutineScope: CoroutineScope) = bind(coroutineScope, coroutineScope.coroutineContext)

/**
 * A [BasicSubject] that is [Initialized]
 * @param T the type of value to expect.
 */
interface InitializedSubject<T> :
    BasicSubject<T, T, Value<T>>,
    InitializedObservable<T>,
    MutableInitialized<T, T>

/**
 * A [BasicSubject] that is [Uninitialized]
 * @param T the type of value to expect.
 */
interface UninitializedSubject<T> :
    BasicSubject<T, T, ObservableOptional<T>>,
    UninitializedObservable<T>,
    MutableUninitialized<T>

/**
 * A [BasicSubject] that is [DefaultInitialized]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 */
interface DefaultSubject<R : T?, T> :
    BasicSubject<R, T?, Value<R>>,
    DefaultObservable<R, T>,
    MutableDefaultInitialized<R, T?>

/**
 * An abstract class extending [BaseObservable] that is a [BasicSubject].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param OO the type of [ObservableOptional] to store the result in.
 * @param observation The [Observation] to handle observing the value.
 * @param stateFlowToBind A function to get the [StateFlow] that will automatically call [SuspendableSetter.set] when a new value is posted after [BasicSubject.post] has been called.
 */
abstract class AbstractBaseSubject<R : T, T, OO : ObservableOptional<R>>(
    observation: Observation<R, T, OO>,
    private val stateFlowToBind: suspend () -> StateFlow<R?>,
) :
    BaseObservable<R, T, OO>(observation), BasicSubject<R, T, OO> {

        override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext) {
            coroutineScope.launch(context) {
                @Suppress("UNCHECKED_CAST")
                stateFlowToBind().collect {
                    set(it as T)
                }
            }
        }
    }

/**
 * An abstract class that extends [AbstractBaseSubject].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param OO the type of [ObservableOptional] to store the result in.
 * @param observation The [Observation] to handle observing the value.
 * @param stateFlowToBind A function to get the [StateFlow] that will automatically call [SuspendableSetter.set] when a new value is posted after [BasicSubject.post] has been called.
 */
expect abstract class BaseSubject<R : T, T, OO : ObservableOptional<R>>(
    observation: Observation<R, T, OO>,
    stateFlowToBind: suspend () -> StateFlow<R?>,
) :
    AbstractBaseSubject<R, T, OO> {
    final override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext)
}

/**
 * An abstract class extending [BaseSubject] that implements [InitializedSubject].
 * @param T the type of value to expect.
 * @param observation The [ObservationInitialized] to handle value being observed
 */
@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class AbstractBaseInitializedSubject<T>(override val observation: ObservationInitialized<T>) :
    BaseSubject<T, T, Value<T>>(
        observation,
        suspend {
            // switch context to Main since value observations for observable also happen on that thread
            withContext(Dispatchers.Main.immediate) {
                observation.stateFlow
            }
        },
    ),
    InitializedSubject<T>,
    MutableInitialized<T, T> by observation {

        /**
         * Constructor using an inital value.
         * @param initialValue The [Value] to use as the initial value.
         */
        constructor(
            initialValue: Value<T>,
        ) : this(ObservationInitialized(initialValue))

        override fun getValue(thisRef: Any?, property: KProperty<*>): Value<T> =
            observation.currentObserved
    }

/**
 * Abstract class implementing [AbstractBaseInitializedSubject]
 * @param T the type of value to expect.
 * @param observation The [ObservationInitialized] to handle value being observed
 */
expect abstract class BaseInitializedSubject<T>(observation: ObservationInitialized<T>) :
    AbstractBaseInitializedSubject<T> {

        /**
         * Constructor using an inital value.
         * @param initialValue The [Value] to use as the initial value.
         */
        constructor(
            initialValue: Value<T>,
        )
    }

/**
 * An abstract class extending [BaseSubject] that implements [UninitializedSubject].
 * @param T the type of value to expect.
 * @param observation The [ObservationUninitialized] to handle value being observed
 */
@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class AbstractBaseUninitializedSubject<T>(
    override val observation: ObservationUninitialized<T>,
) : BaseSubject<T, T, ObservableOptional<T>>(
    observation,
    { observation.stateFlow },
),
    UninitializedSubject<T>,
    MutableUninitialized<T> by observation {
        override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableOptional<T> =
            observation.observedValue
    }

/**
 * An abstract class extending [AbstractBaseUninitializedSubject].
 * @param T the type of value to expect.
 * @param observation The [ObservationUninitialized] to handle value being observed
 */
expect abstract class BaseUninitializedSubject<T>(
    observation: ObservationUninitialized<T>,
) : AbstractBaseUninitializedSubject<T>

/**
 * An abstract class extending [BaseSubject] that implements [DefaultSubject].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param observation The [ObservationDefault] to handle value being observed
 */
@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE") // deliberate
abstract class AbstractBaseDefaultSubject<R : T?, T>(
    override val observation: ObservationDefault<R, T?>,
) : BaseSubject<R, T?, Value<R>>(
    observation,
    { observation.stateFlow },
),
    DefaultSubject<R, T>,
    MutableDefaultInitialized<R, T?> by observation {
        constructor(
            defaultValue: Value<R>,
            initialValue: Value<T?>,
        ) : this(observation = ObservationDefault<R, T?>(defaultValue, initialValue))

        override fun getValue(thisRef: Any?, property: KProperty<*>): Value<R> =
            observation.currentObserved
    }

/**
 * An abstract class extending [AbstractBaseDefaultSubject].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param observation The [ObservationUninitialized] to handle value being observed
 */
expect abstract class BaseDefaultSubject<R : T?, T>(
    observation: ObservationDefault<R, T?>,
) : AbstractBaseDefaultSubject<R, T> {

    /**
     * Constructor
     * @param defaultValue The default [Value] of [R] to return if the current value is [ObservableOptional.Nothing] or [ObservableOptional.Value] containing `null`.
     * @param initialValue The initial [Value] of [T].
     */
    constructor(
        defaultValue: Value<R>,
        initialValue: Value<T?>,
    )
}

/**
 * A [BaseInitializedSubject] that initialized to a given value.
 * @param T the type of value to expect.
 * @param observation The [ObservationInitialized] to handle observation.
 */
@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
class SimpleInitializedSubject<T>(
    override val observation: ObservationInitialized<T>,
) : BaseInitializedSubject<T>(
    observation,
) {

    /**
     * Constructor
     * @param initialValue The initial value of [T]
     */
    constructor(
        initialValue: T,
    ) : this(Value(initialValue))

    /**
     * Constructor
     * @param initialValue The initial [Value] of [T]
     */
    constructor(
        initialValue: Value<T>,
    ) : this(ObservationInitialized(initialValue))

    override suspend fun set(newValue: T) {
        observation.setValue(Value(newValue))
    }

    override fun post(newValue: T) {
        observation.observedValue = Value(newValue)
    }
}

/**
 * A [BaseDefaultSubject] that initialized to a given value.
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param defaultValue The default [R] to return if the current value is [ObservableOptional.Nothing] or [ObservableOptional.Value] containing `null`.
 * @param initialValue The initial value of [T].
 */
class SimpleDefaultSubject<R : T?, T>(
    defaultValue: R,
    initialValue: T? = defaultValue,
) :
    BaseDefaultSubject<R, T?>(
        Value(defaultValue),
        Value(initialValue),
    ) {

        override fun post(newValue: T?) {
            observation.observedValue = Value(newValue)
        }

        override suspend fun set(newValue: T?) {
            observation.setValue(Value(newValue))
        }
    }

/**
 * Creates a [SimpleInitializedSubject] with an initial value
 * @param T the type of value to expect.
 * @param initialValue The initial value of [T].
 */
fun <T> subjectOf(initialValue: T) = SimpleInitializedSubject(initialValue)
