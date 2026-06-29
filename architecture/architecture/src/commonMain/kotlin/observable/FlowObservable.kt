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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

/**
 * A [BaseUninitializedObservable] that observes the state of a [Flow]
 * @param T the type of value to expect.
 * @param coroutineScope The [CoroutineScope] on which to observe the [Flow]
 * @param context The [CoroutineContext] in which to observe the [Flow]
 * @param flow The [Flow] to observe
 * @param observation The [ObservationUninitialized] to observe the Flow
 */
class FlowObservable<T>(
    val coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    val flow: Flow<T>,
    observation: ObservationUninitialized<T> = ObservationUninitialized(),
) : BaseUninitializedObservable<T>(observation) {
    init {
        observeFlow(observation, coroutineScope, context, flow)
    }
}

/**
 * A [BaseInitializedObservable] that observes the state of a [Flow]
 * @param T the type of value to expect.
 * @param initialValue The initial value of [T]
 * @param coroutineScope The [CoroutineScope] on which to observe the [Flow]
 * @param context The [CoroutineContext] in which to observe the [Flow]
 * @param flow The [Flow] to observe
 * @param observation The [ObservationInitialized] to observe the Flow
 */
class FlowInitializedObservable<T>(
    initialValue: T,
    val coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    val flow: Flow<T>,
    observation: ObservationInitialized<T> = ObservationInitialized(initialValue),
) : BaseInitializedObservable<T>(observation) {
    init {
        observeFlow(observation, coroutineScope, context, flow)
    }
}

/**
 * A [BaseDefaultObservable] that observes the state of a [Flow].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param defaultValue The default value of [R] to observe if the [Flow] emits `null`.
 * @param initialValue The initial value of [T]
 * @param coroutineScope The [CoroutineScope] on which to observe the [Flow]
 * @param context The [CoroutineContext] in which to observe the [Flow]
 * @param flow The [Flow] to observe
 * @param observation The [ObservationUninitialized] to observe the Flow
 */
class DefaultFlowObservable<R : T?, T>(
    defaultValue: R,
    initialValue: T? = defaultValue,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    flow: Flow<T?>,
    observation: ObservationDefault<R, T?> = ObservationDefault(
        defaultValue,
        ObservableOptional.Value(initialValue),
    ),
) : BaseDefaultObservable<R, T?>(observation) {
    init {
        observeFlow(observation, coroutineScope, context, flow)
    }
}

/**
 * Converts a [Flow] into a [FlowObservable]
 * @param coroutineScope The [CoroutineScope] on which to observe the [Flow]
 * @param context The [CoroutineContext] in which to observe the [Flow]
 */
fun <T> Flow<T>.toUninitializedObservable(coroutineScope: CoroutineScope, context: CoroutineContext = coroutineScope.coroutineContext) =
    FlowObservable(coroutineScope, context, this)

/**
 * Converts a [MutableStateFlow] into a [FlowInitializedObservable]
 * @param coroutineScope The [CoroutineScope] on which to observe the [Flow]
 * @param context The [CoroutineContext] in which to observe the [Flow]
 */
fun <T> MutableStateFlow<T>.toInitializedObservable(coroutineScope: CoroutineScope, context: CoroutineContext = coroutineScope.coroutineContext) =
    FlowInitializedObservable(this.value, coroutineScope, context, this)

/**
 * Converts a [Flow] into a [FlowInitializedObservable]
 * @param initialValue The initial value to observe until the [Flow] emits.
 * @param coroutineScope The [CoroutineScope] on which to observe the [Flow]
 * @param context The [CoroutineContext] in which to observe the [Flow]
 */
fun <T> Flow<T>.toInitializedObservable(initialValue: T, coroutineScope: CoroutineScope, context: CoroutineContext = coroutineScope.coroutineContext) =
    FlowInitializedObservable(initialValue, coroutineScope, context, this)

/**
 * Converts a [Flow] into a [DefaultFlowObservable]
 * @param defaultValue The default value to observe if the [Flow] emits `null`.
 * @param initialValue The initial value to observe until the [Flow] emits.
 * @param coroutineScope The [CoroutineScope] on which to observe the [Flow]
 * @param context The [CoroutineContext] in which to observe the [Flow]
 */
fun <R : T, T> Flow<T?>.toDefaultObservable(
    defaultValue: R,
    initialValue: T? = defaultValue,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
) = DefaultFlowObservable(defaultValue, initialValue, coroutineScope, context, this)
