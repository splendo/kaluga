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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private class MutableFlowSubjectHelper<R : T, T, OO : ObservableOptional<R>>(
    val coroutineScope: CoroutineScope,
    val context: CoroutineContext,
    val flow: () -> Flow<T>,
    val setter: suspend (T) -> Unit,
    val poster: (T) -> Unit = { coroutineScope.launch(context) { setter(it) } },
    observation: Observation<R, T, OO>,
) : SuspendableSetter<T> {

    init {
        observeFlow(observation, coroutineScope, context, flow())
    }

    override suspend fun set(newValue: T) = setter(newValue)
    override fun post(newValue: T) = poster(newValue)
}

/**
 * A [BaseInitializedSubject] that matches the state of a [MutableStateFlow].
 * @param T the type of value to expect.
 * @property coroutineScope The [CoroutineScope] on which to observe the [MutableStateFlow]
 * @property context The [CoroutineContext] in which to observe the [MutableStateFlow]
 * @param observedStateFlow The [MutableStateFlow] to match the state of.
 * @param autoBind If `true` this will automatically call [bind].
 * @param observation The [ObservationInitialized] to handle value being observed
 */
open class StateFlowInitializedSubject<T>(
    val coroutineScope: CoroutineScope,
    val context: CoroutineContext = coroutineScope.coroutineContext,
    private val observedStateFlow: MutableStateFlow<T>,
    autoBind: Boolean = true,
    observation: ObservationInitialized<T> = ObservationInitialized(
        ObservableOptional.Value(
            observedStateFlow.value,
        ),
    ),
) : BaseInitializedSubject<T>(observation),
    SuspendableSetter<T> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { observedStateFlow },
        {
            observation.setSuspendedIfNot(it)
            observedStateFlow.value = it
        },
        {
            observation.setIfNot(it)
            observedStateFlow.value = it
        },
        observation,
    ) {
    init {
        if (autoBind) {
            bind(coroutineScope, context)
        }
    }
}

/**
 * A [BaseDefaultSubject] that matches the state of a [MutableStateFlow].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param defaultValue The default [R] to return if the value of the [MutableStateFlow] is `null`.
 * @property coroutineScope The [CoroutineScope] on which to observe the [MutableStateFlow]
 * @property context The [CoroutineContext] in which to observe the [MutableStateFlow]
 * @param observedStateFlow The [MutableStateFlow] to match the state of.
 * @param autoBind If `true` this will automatically call [bind].
 * @param observation The [ObservationDefault] to handle value being observed
 */
open class StateFlowDefaultSubject<R : T?, T>(
    defaultValue: R,
    val coroutineScope: CoroutineScope,
    val context: CoroutineContext = coroutineScope.coroutineContext,
    private val observedStateFlow: MutableStateFlow<T?>,
    autoBind: Boolean = true,
    observation: ObservationDefault<R, T?> = ObservationDefault(
        defaultValue,
        ObservableOptional.Value(observedStateFlow.value),
    ),
) : BaseDefaultSubject<R, T?>(observation),
    SuspendableSetter<T?> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { observedStateFlow },
        {
            observation.setSuspendedIfNot(it)
            observedStateFlow.value = it
        },
        {
            observation.setIfNot(it)
            observedStateFlow.value = it
        },
        observation,
    ) {
    init {
        if (autoBind) {
            bind(coroutineScope, context)
        }
    }
}

/**
 * A [BaseUninitializedSubject] that matches the state of a [MutableSharedFlow].
 * @param T the type of value to expect.
 * @param coroutineScope The [CoroutineScope] on which to observe the [MutableSharedFlow]
 * @param context The [CoroutineContext] in which to observe the [MutableSharedFlow]
 * @param sharedFlow The [MutableSharedFlow] to match the state of.
 * @param autoBind If `true` this will automatically call [bind].
 * @param observation The [ObservationUninitialized] to handle value being observed
 */
open class SharedFlowSubject<T>(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    sharedFlow: MutableSharedFlow<T>,
    autoBind: Boolean = true,
    observation: ObservationUninitialized<T> = ObservationUninitialized(),
) : BaseUninitializedSubject<T>(
    observation,
),
    SuspendableSetter<T> by MutableFlowSubjectHelper(
        coroutineScope = coroutineScope,
        context = context,
        flow = { sharedFlow },
        setter = {
            observation.setSuspendedIfNot(it)
            sharedFlow.emit(it)
        },
        observation = observation,
    ) {
    init {
        if (autoBind) {
            bind(coroutineScope, context)
        }
    }
}

/**
 * A [BaseInitializedSubject] that matches the state of a [MutableSharedFlow].
 * @param T the type of value to expect.
 * @param initialValue The initial value of [T]
 * @param coroutineScope The [CoroutineScope] on which to observe the [MutableSharedFlow]
 * @param context The [CoroutineContext] in which to observe the [MutableSharedFlow]
 * @param sharedFlow The [MutableSharedFlow] to match the state of.
 * @param autoBind If `true` this will automatically call [bind].
 * @param observation The [ObservationInitialized] to handle value being observed
 */
open class SharedFlowInitializedSubject<T>(
    initialValue: T,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    sharedFlow: MutableSharedFlow<T>,
    autoBind: Boolean = true,
    observation: ObservationInitialized<T> = ObservationInitialized(initialValue),
) : BaseInitializedSubject<T>(
    observation,
),
    SuspendableSetter<T> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { sharedFlow },
        {
            observation.setSuspendedIfNot(it)
            sharedFlow.emit(it)
        },
        observation = observation,
    ) {
    init {
        if (autoBind) {
            bind(coroutineScope, context)
        }
    }
}

/**
 * A [BaseDefaultSubject] that matches the state of a [MutableSharedFlow].
 * @param T the type of value to expect.
 * @param defaultValue The default value of [R] to observe if the [MutableSharedFlow] emits `null`.
 * @param initialValue The initial value of [T]
 * @param coroutineScope The [CoroutineScope] on which to observe the [MutableSharedFlow]
 * @param context The [CoroutineContext] in which to observe the [MutableSharedFlow]
 * @param sharedFlow The [MutableSharedFlow] to match the state of.
 * @param autoBind If `true` this will automatically call [bind].
 * @param observation The [ObservationInitialized] to handle value being observed
 */
open class SharedFlowDefaultSubject<R : T?, T>(
    defaultValue: R,
    initialValue: T? = defaultValue,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    sharedFlow: MutableSharedFlow<T?>,
    autoBind: Boolean = true,
    observation: ObservationDefault<R, T?> = ObservationDefault(
        defaultValue,
        ObservableOptional.Value(initialValue),
    ),
) : BaseDefaultSubject<R, T?>(
    observation,
),
    SuspendableSetter<T?> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { sharedFlow },
        {
            observation.setSuspendedIfNot(it)
            sharedFlow.emit(it)
        },
        observation = observation,
    ) {
    init {
        if (autoBind) {
            bind(coroutineScope, context)
        }
    }
}

/**
 * Converts a [MutableSharedFlow] to a [SharedFlowSubject]
 * @param coroutineScope The [CoroutineScope] on which to observe the [MutableSharedFlow]
 * @param context The [CoroutineContext] in which to observe the [MutableSharedFlow]
 */
fun <T> MutableSharedFlow<T>.toUninitializedSubject(coroutineScope: CoroutineScope, context: CoroutineContext = coroutineScope.coroutineContext) = SharedFlowSubject(
    coroutineScope,
    context,
    this,
)

/**
 * Converts a [MutableStateFlow] to a [StateFlowInitializedSubject]
 * @param coroutineScope The [CoroutineScope] on which to observe the [MutableStateFlow]
 * @param context The [CoroutineContext] in which to observe the [MutableStateFlow]
 */
fun <T> MutableStateFlow<T>.toInitializedSubject(coroutineScope: CoroutineScope, context: CoroutineContext = coroutineScope.coroutineContext) =
    StateFlowInitializedSubject(coroutineScope, context, this)

/**
 * Converts a [MutableSharedFlow] to a [SharedFlowInitializedSubject]
 * @param initialValue The initial value to observe until the [MutableSharedFlow] emits.
 * @param coroutineScope The [CoroutineScope] on which to observe the [MutableSharedFlow]
 * @param context The [CoroutineContext] in which to observe the [MutableSharedFlow]
 */
fun <T> MutableSharedFlow<T>.toInitializedSubject(initialValue: T, coroutineScope: CoroutineScope, context: CoroutineContext = coroutineScope.coroutineContext) =
    SharedFlowInitializedSubject(
        initialValue,
        coroutineScope,
        context,
        this,
    )

/**
 * Converts a [MutableStateFlow] to a [StateFlowDefaultSubject]
 * @param defaultValue The default value of [R] to observe if the [MutableStateFlow] emits `null`.
 * @param coroutineScope The [CoroutineScope] on which to observe the [MutableStateFlow]
 * @param context The [CoroutineContext] in which to observe the [MutableStateFlow]
 */
fun <R : T, T> MutableStateFlow<T?>.toDefaultSubject(defaultValue: R, coroutineScope: CoroutineScope, context: CoroutineContext = coroutineScope.coroutineContext) =
    StateFlowDefaultSubject(defaultValue, coroutineScope, context, this)

/**
 * Converts a [MutableSharedFlow] to a [SharedFlowDefaultSubject]
 * @param defaultValue The default value of [R] to observe if the [MutableSharedFlow] emits `null`.
 * @param initialValue The initial value to observe until the [MutableSharedFlow] emits.
 * @param coroutineScope The [CoroutineScope] on which to observe the [MutableSharedFlow]
 * @param context The [CoroutineContext] in which to observe the [MutableSharedFlow]
 */
fun <R : T?, T> MutableSharedFlow<T?>.toDefaultSubject(
    defaultValue: R,
    initialValue: T? = defaultValue,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
) = SharedFlowDefaultSubject(
    defaultValue,
    initialValue,
    coroutineScope,
    context,
    this,
)
