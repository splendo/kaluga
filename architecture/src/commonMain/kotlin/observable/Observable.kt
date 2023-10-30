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

import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A read-only property that can be observed.
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param OO the type of [ObservableOptional] to store the result in.
 */
interface BasicObservable<R : T, T, OO : ObservableOptional<R>> :
    ReadOnlyProperty<Any?, OO>,
    Initial<R, T>

/**
 * A [BaseObservable] that is [Initialized].
 * @param T the type of value to expect.
 */
interface InitializedObservable<T> : BasicObservable<T, T, Value<T>>, Initialized<T, T>

/**
 * A [BasicObservable] that is [Uninitialized].
 * @param T the type of value to expect.
 */
interface UninitializedObservable<T> :
    BasicObservable<T, T, ObservableOptional<T>>,
    Uninitialized<T>

/**
 * A [BasicObservable] that is [DefaultInitialized]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 */
interface DefaultObservable<R : T?, T> :
    BasicObservable<R, T?, Value<R>>,
    DefaultInitialized<R, T?>

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
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param OO the type of [ObservableOptional] to store the result in.
 * @param observation The [Observation] to handle observing the value.
 */
abstract class BaseObservable<R : T, T, OO : ObservableOptional<R>>(
    protected open val observation: Observation<R, T, OO>,
) : BasicObservable<R, T, OO>,
    Initial<R, T> by observation {
        constructor(
            initialValue: ObservableOptional<T>,
        ) : this(Observation(initialValue))
    }

/**
 * A [BaseObservable] that implements [InitializedObservable]
 * @param T the type of value to expect.
 * @param observation The [ObservationInitialized] to handle observing the value.
 */
@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class BaseInitializedObservable<T>(
    observation: ObservationInitialized<T>,
) : BaseObservable<T, T, Value<T>>(observation),
    InitializedObservable<T>,
    Initialized<T, T> by observation {

        override fun getValue(thisRef: Any?, property: KProperty<*>): Value<T> =
            observation.currentObserved

        constructor(
            initialValue: Value<T>,
        ) : this(ObservationInitialized(initialValue))
    }

/**
 * An abstract class extending [BaseObservable] that implements [UninitializedObservable]
 * @param T the type of value to expect.
 * @param observation The [ObservationUninitialized] to handle observing the value.
 */
@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
abstract class BaseUninitializedObservable<T>(
    override val observation: ObservationUninitialized<T>,
) : BaseObservable<T, T, ObservableOptional<T>>(
    observation,
),
    UninitializedObservable<T>,
    Uninitialized<T> by observation {

        override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableOptional<T> =
            observation.currentObserved
    }

/**
 * An abstract class extending [BaseObservable] that implements [DefaultObservable]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param observation The [ObservationDefault] to handle observing the value.
 */
@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE") // we want our delegate to override
abstract class BaseDefaultObservable<R : T?, T>(
    override val observation: ObservationDefault<R, T?>,
) :
    BaseObservable<R, T?, Value<R>>(observation),
    DefaultObservable<R, T?>,
    DefaultInitialized<R, T?> by observation {

        /**
         * Constructor
         * @param defaultValue The default value of [R] to return if the current value is [ObservableOptional.Nothing] or [ObservableOptional.Value] containing `null`.
         * @param initialValue A [Value] of [T] to use as the initial value
         */
        constructor(
            defaultValue: R,
            initialValue: Value<T?>,
        ) : this(ObservationDefault<R, T?>(Value(defaultValue), initialValue))

        override fun getValue(thisRef: Any?, property: KProperty<*>): Value<R> =
            observation.currentObserved
    }

/**
 * A [BaseInitializedObservable] that has a fixed value.
 * @param T the type of value to expect.
 * @param value The fixed value of the observable
 */
class SimpleInitializedObservable<T>(
    value: T,
) : BaseInitializedObservable<T>(Value(value))

/**
 * Creates a [SimpleInitializedObservable] from a value.
 * @param T the type of value to expect.
 * @param value The fixed value to observe.
 */
fun <T> observableOf(value: T) = SimpleInitializedObservable(value)
