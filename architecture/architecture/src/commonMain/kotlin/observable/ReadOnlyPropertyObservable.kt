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

import kotlinx.coroutines.flow.Flow
import kotlin.properties.ReadOnlyProperty

private fun <R : T, T> readOnlyPropertyObservableHelper(readOnlyProperty: ReadOnlyProperty<Any?, T>, observation: Observation<R, T, ObservableOptional.Value<R>>) {
    val readOnlyValue by readOnlyProperty

    observation.beforeObservedValueGet = { current ->
        @Suppress("UnnecessaryVariable")
        val new = readOnlyValue // variable actually needed due to delegation
        if (new != current.valueOrNull) {
            ObservableOptional.Value(new)
        } else {
            @Suppress("UNCHECKED_CAST")
            current as ObservableOptional<T>
        }
    }
}

/**
 * A [BaseInitializedObservable] that matches its value to a [ReadOnlyProperty].
 * When the [ReadOnlyProperty] updates, changes to the property are not immediately delegated back to the observable.
 * Use a [Flow] based observable if synchronized values are required.
 * @param T the type of value to expect.
 * @param readOnlyProperty The [ReadOnlyProperty] to track.
 * @param observation The [ObservationInitialized] to handle observation.
 */
class ReadOnlyPropertyInitializedObservable<T>(
    readOnlyProperty: ReadOnlyProperty<Any?, T>,
    observation: ObservationInitialized<T> = ObservationInitialized(
        ObservableOptional.Value(
            run {
                val v by readOnlyProperty
                v
            },
        ),
    ),
) : BaseInitializedObservable<T>(observation),
    InitializedObservable<T> {

    init {
        readOnlyPropertyObservableHelper(readOnlyProperty, observation)
    }
}

/**
 * A [BaseDefaultObservable] that matches its value to a [ReadOnlyProperty].
 * When the [ReadOnlyProperty] updates, changes to the property are not immediately delegated back to the observable.
 * Use a [Flow] based observable if synchronized values are required
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param defaultValue The default [R] to return if the value of the [ReadOnlyProperty] is `null`.
 * @param readOnlyProperty The [ReadOnlyProperty] to track.
 * @param observation The [ObservationInitialized] to handle observation.
 */
class ReadOnlyPropertyDefaultObservable<R : T?, T>(
    defaultValue: R,
    readOnlyProperty: ReadOnlyProperty<Any?, T>,
    observation: ObservationDefault<R, T?> = ObservationDefault(
        defaultValue,
        ObservableOptional.Value(
            run {
                val v by readOnlyProperty
                v
            },
        ),
    ),
) : BaseDefaultObservable<R, T>(observation) { // no default is needed, since this is for use with non-optionals

    init {
        readOnlyPropertyObservableHelper(readOnlyProperty, observation)
    }
}

/**
 * Converts a [ReadOnlyProperty] into a [ReadOnlyPropertyInitializedObservable]
 */
fun <T> ReadOnlyProperty<Any?, T>.toInitializedObservable() = ReadOnlyPropertyInitializedObservable(this)

/**
 * Converts a [ReadOnlyProperty] into a [ReadOnlyPropertyDefaultObservable]
 * @param defaultValue The default value of [R] to observe if the [ReadOnlyProperty] has a value of `null`.
 */
fun <R : T, T> ReadOnlyProperty<Any?, T?>.toDefaultObservable(defaultValue: R) = ReadOnlyPropertyDefaultObservable(defaultValue, this)
