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

@file:Suppress("DEPRECATION")

package com.splendo.kaluga.architecture.observable

import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadWriteProperty

private class ReadWritePropertyObservableHelper<R : T, T>(readWriteProperty: ReadWriteProperty<Any?, T>, private val observation: Observation<R, T, Value<R>>) :
    SuspendableSetter<T> {

    private var readWriteValue by readWriteProperty

    init {
        observation.beforeObservedValueGet = { current ->
            val new = readWriteValue
            if (new != current.valueOrNull) {
                Value(new)
            } else {
                @Suppress("UNCHECKED_CAST")
                current as ObservableOptional<T>
            }
        }
    }

    override suspend fun set(newValue: T) = post(newValue)

    override fun post(newValue: T) {
        readWriteValue = newValue
        observation.observedValue = Value(newValue)
    }
}

/**
 * A [BaseInitializedSubject] that matches its value to a [ReadWriteProperty].
 * When the [ReadWriteProperty] updates, changes to the property are not immediately delegated back to the subject.
 * Use a [Flow] based subject if synchronized values are required
 * @param T the type of value to expect.
 * @param readWriteProperty The [ReadWriteProperty] to track.
 * @param initialValue The initial value of [T]. Defaults to the current value of the [ReadWriteProperty].
 * @property coroutineScope The [CoroutineScope] on which to emit changes to the [ReadWriteProperty]
 * @property context The [CoroutineContext] in which to emit changes to the [ReadWriteProperty]
 * @param observation The [ObservationInitialized] to handle observation.
 */
class ReadWritePropertyInitializedSubject<T>(
    readWriteProperty: ReadWriteProperty<Any?, T>,
    initialValue: Value<T> = Value(
        run {
            val v by readWriteProperty
            v
        },
    ),
    coroutineScope: CoroutineScope?,
    context: CoroutineContext? = coroutineScope?.coroutineContext,
    observation: ObservationInitialized<T> = ObservationInitialized(initialValue),
) : BaseInitializedSubject<T>(
    observation,
),
    SuspendableSetter<T> by ReadWritePropertyObservableHelper(readWriteProperty, observation) {
    init {
        coroutineScope?.let { bind(it, context ?: coroutineScope.coroutineContext) }
    }
}

/**
 * A [BaseDefaultSubject] that matches its value to a [ReadWriteProperty].
 * When the [ReadWriteProperty] updates, changes to the property are not immediately delegated back to the subject.
 * Use a [Flow] based subject if synchronized values are required
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param defaultValue The default [R] to return if the value of the [ReadWriteProperty] is `null`.
 * @param readWriteProperty The [ReadWriteProperty] to track.
 * @param initialValue The initial value of [T]. Defaults to the current value of the [ReadWriteProperty].
 * @param coroutineScope The [CoroutineScope] on which to emit changes to the [ReadWriteProperty]
 * @param context The [CoroutineContext] in which to emit changes to the [ReadWriteProperty]
 * @param observation The [ObservationInitialized] to handle observation.
 */
class ReadWritePropertyDefaultSubject<R : T?, T>(
    defaultValue: R,
    readWriteProperty: ReadWriteProperty<Any?, T?>,
    initialValue: Value<T?> = Value(
        run {
            val v by readWriteProperty
            v
        },
    ),
    coroutineScope: CoroutineScope?,
    context: CoroutineContext? = coroutineScope?.coroutineContext,
    observation: ObservationDefault<R, T?> = ObservationDefault(defaultValue, initialValue),
) : BaseDefaultSubject<R, T>(
    observation,
),
    SuspendableSetter<T?> by ReadWritePropertyObservableHelper(readWriteProperty, observation) {
    init {
        coroutineScope?.let {
            bind(coroutineScope, context ?: coroutineScope.coroutineContext)
        }
    }
}

/**
 * Converts a [ReadWriteProperty] to a [ReadWritePropertyInitializedSubject]
 * @param coroutineScope The [CoroutineScope] on which to emit changes to the [ReadWriteProperty]
 * @param context The [CoroutineContext] in which to emit changes to the [ReadWriteProperty]
 */
fun <T> ReadWriteProperty<Any?, T>.toInitializedSubject(coroutineScope: CoroutineScope? = null, context: CoroutineContext? = coroutineScope?.coroutineContext) =
    ReadWritePropertyInitializedSubject(this, context = context, coroutineScope = coroutineScope)

/**
 * Converts a [ReadWriteProperty] to a [ReadWritePropertyDefaultSubject]
 * @param defaultValue The default value of [R] to observe if the [ReadWriteProperty] has a value of `null`.
 * @param coroutineScope The [CoroutineScope] on which to emit changes to the [ReadWriteProperty]
 * @param context The [CoroutineContext] in which to emit changes to the [ReadWriteProperty]
 */
fun <R : T, T> ReadWriteProperty<Any?, T?>.toDefaultSubject(
    defaultValue: R,
    coroutineScope: CoroutineScope? = null,
    context: CoroutineContext? = coroutineScope?.coroutineContext,
): ReadWritePropertyDefaultSubject<R, T?> = ReadWritePropertyDefaultSubject(
    defaultValue = defaultValue,
    readWriteProperty = this,
    coroutineScope = coroutineScope,
    context = context,
)
