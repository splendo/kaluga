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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty

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
interface MutableDefaultInitialized<R : T?, T> :
    DefaultInitialized<R, T?>,
    WithMutableState<R>

/**
 * An [Initialized] that has a [defaultValue]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 */
interface DefaultInitialized<R : T?, T> : Initialized<R, T?> {
    /**
     * The default [Value] of [R]. Can be used in case [Nothing] is [Initial.initialValue]
     */
    val defaultValue: Value<R>
}

/**
 * An [Uninitialized] that implements [WithMutableState]
 * @param T the type of value to expect.
 */
interface MutableUninitialized<T> :
    Uninitialized<T>,
    WithMutableState<T?>

/**
 * An [Initial] that has [Nothing] as its [initialValue]
 * @param T the type of value to expect.
 */
interface Uninitialized<T> :
    Initial<T, T>,
    WithState<T?> {
    override val initialValue: ObservableOptional.Nothing<T>
}

/**
 * An [Initialized] that implements [WithMutableState]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 */
interface MutableInitialized<R : T, T> :
    Initialized<R, T>,
    WithMutableState<R>

/**
 * An [Initial] that has [Value] as its [initialValue]
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 */
interface Initialized<R : T, T> :
    Initial<R, T>,
    WithState<R> {
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
