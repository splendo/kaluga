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
import com.splendo.kaluga.base.flow.HotFlowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty

fun <R : T, T, OO : ObservableOptional<R>> observeFlow(
    observation: Observation<R, T, OO>,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    flow: Flow<T>
) {

    observation.onFirstObservation = {
        coroutineScope.launch(context) {
            flow.collect {
                observation.setValue(Value(it))
            }
        }
    }
}

class DefaultFlowObservable<R : T?, T>(
    defaultValue: R,
    initialValue: T? = defaultValue,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    flow: Flow<T?>,
    observation: ObservationDefault<R, T?> = ObservationDefault(defaultValue, Value(initialValue)),
) : BaseDefaultObservable<R, T?>(observation) {
    init {
        observeFlow(observation, coroutineScope, context, flow)
    }
}

class FlowObservable<T>(
    val coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    val flow: Flow<T>,
    observation: ObservationUninitialized<T> = ObservationUninitialized()
) : BaseUninitializedObservable<T>(observation) {
    init {
        observeFlow(observation, coroutineScope, context, flow)
    }
}

class FlowInitializedObservable<T>(
    initialValue: T,
    val coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    val flow: Flow<T>,
    observation: ObservationInitialized<T> = ObservationInitialized(initialValue)
) : BaseInitializedObservable<T>(observation) {
    init {
        observeFlow(observation, coroutineScope, context, flow)
    }
}

class MutableFlowSubjectHelper<R : T, T, OO : ObservableOptional<R>>(
    val coroutineScope: CoroutineScope,
    val context: CoroutineContext,
    val flow: () -> Flow<T>,
    val setter: suspend (T) -> Unit,
    val poster: (T) -> Unit = { coroutineScope.launch(context) { setter(it) } },
    observation: Observation<R, T, OO>
) : SuspendableSetter<T> {

    init {
        observeFlow(observation, coroutineScope, context, flow())
    }

    override suspend fun set(newValue: T) = setter(newValue)
    override fun post(newValue: T) = poster(newValue)
}

class HotFlowableDefaultSubject<R : T?, T>(
    defaultValue: R,
    val coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    hotFlowable: HotFlowable<T?>,
    autoBind: Boolean = true,
    observation: ObservationDefault<R, T?> = ObservationDefault(defaultValue, Value(hotFlowable.initialValue))
) : BaseDefaultSubject<R, T?>(observation),
    SuspendableSetter<T?> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        flow = { hotFlowable.flow() },
        setter = { hotFlowable.set(it) },
        observation = observation
    ) {
    init {
        if (autoBind) bind(coroutineScope, context)
    }
}

class HotFlowableInitializedSubject<T>(
    val coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    hotFlowable: HotFlowable<T>,
    autoBind: Boolean = true,
    observation: ObservationInitialized<T> = ObservationInitialized(Value(hotFlowable.initialValue))
) : BaseInitializedSubject<T>(observation),
    SuspendableSetter<T> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        flow = { hotFlowable.flow() },
        setter = { hotFlowable.set(it) },
        observation = observation
    ) {
    init {
        if (autoBind) bind(coroutineScope, context)
    }
}

open class StateFlowSubject<T>(
    val coroutineScope: CoroutineScope,
    val context: CoroutineContext = coroutineScope.coroutineContext,
    private val observedStateFlow: MutableStateFlow<T>,
    autoBind: Boolean = true,
    observation: ObservationInitialized<T> = ObservationInitialized(Value(observedStateFlow.value)),
) : BaseInitializedSubject<T>(observation),
    SuspendableSetter<T> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { observedStateFlow },
        { observedStateFlow.value = it },
        { observedStateFlow.value = it },
        observation
    ) {
    init {
        if (autoBind) bind(coroutineScope, context)
    }
}

open class StateFlowDefaultSubject<R : T?, T>(
    defaultValue: R,
    val coroutineScope: CoroutineScope,
    val context: CoroutineContext = coroutineScope.coroutineContext,
    private val observedStateFlow: MutableStateFlow<T?>,
    autoBind: Boolean = true,
    observation: ObservationDefault<R, T?> = ObservationDefault(defaultValue, Value(observedStateFlow.value)),
) : BaseDefaultSubject<R, T?>(observation),
    SuspendableSetter<T?> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { observedStateFlow },
        { observedStateFlow.value = it },
        { observedStateFlow.value = it },
        observation
    ) {
    init {
        if (autoBind) bind(coroutineScope, context)
    }
}

/**
 * [BaseSubject] that synchronizes its value to a [MutableStateFlow]
 * @param observedStateFlow The [MutableStateFlow] to synchronize to
 * @param coroutineScope The [CoroutineScope] on which to observe changes to the [StateFlow]
 */
open class StateFlowInitializedSubject<T>(
    val coroutineScope: CoroutineScope,
    val context: CoroutineContext = coroutineScope.coroutineContext,
    private val observedStateFlow: MutableStateFlow<T>,
    autoBind: Boolean = true,
    observation: ObservationInitialized<T> = ObservationInitialized(Value(observedStateFlow.value)),
) : BaseInitializedSubject<T>(observation),
    SuspendableSetter<T> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { observedStateFlow },
        { observedStateFlow.value = it },
        { observedStateFlow.value = it },
        observation
    ) {
    init {
        if (autoBind) bind(coroutineScope, context)
    }
}

open class SharedFlowDefaultSubject<R : T?, T>(
    defaultValue: R,
    initialValue: T? = defaultValue,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    sharedFlow: MutableSharedFlow<T?>,
    autoBind: Boolean = true,
    observation: ObservationDefault<R, T?> = ObservationDefault(defaultValue, Value(initialValue)),
) : BaseDefaultSubject<R, T?>(
    observation
),
    SuspendableSetter<T?> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { sharedFlow },
        { sharedFlow.emit(it) },
        observation = observation
    ) {
    init {
        if (autoBind) bind(coroutineScope, context)
    }
}

open class SharedFlowSubject<T>(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    sharedFlow: MutableSharedFlow<T>,
    autoBind: Boolean = true,
    observation: ObservationUninitialized<T> = ObservationUninitialized()
) : BaseUninitializedSubject<T>(
    observation
),
    SuspendableSetter<T> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { sharedFlow },
        { sharedFlow.emit(it) },
        observation = observation
    ) {
    init {
        if (autoBind) bind(coroutineScope, context)
    }
}

open class SharedFlowInitializedSubject<T>(
    initialValue: T,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
    sharedFlow: MutableSharedFlow<T>,
    autoBind: Boolean = true,
    observation: ObservationInitialized<T> = ObservationInitialized(initialValue)
) : BaseInitializedSubject<T>(
    observation
),
    SuspendableSetter<T> by MutableFlowSubjectHelper(
        coroutineScope,
        context,
        { sharedFlow },
        { sharedFlow.emit(it) },
        observation = observation
    ) {
    init {
        if (autoBind) bind(coroutineScope, context)
    }
}

fun <R : T, T> readOnlyPropertyObservableHelper(readOnlyProperty: ReadOnlyProperty<Any?, T>, observation: Observation<R, T, Value<R>>) {

    val readOnlyValue by readOnlyProperty

    observation.beforeObservedValueGet = { current ->
        @Suppress("UnnecessaryVariable") val new = readOnlyValue // variable actually needed due to delegation
        if (new != current.valueOrNull)
            Value(new)
        else
            @Suppress("UNCHECKED_CAST")
            current as ObservableOptional<T>
    }
}

class ReadOnlyPropertyDefaultObservable<R : T, T>(
    defaultValue: R,
    readOnlyProperty: ReadOnlyProperty<Any?, T>,
    observation: ObservationDefault<R, T?> = ObservationDefault(
        defaultValue,
        Value(
            run {
                val v by readOnlyProperty
                v
            }
        )
    )
) :
    BaseDefaultObservable<R, T> (observation) { // no default is needed, since this is for use with non-optionals

    init {
        readOnlyPropertyObservableHelper(readOnlyProperty, observation)
    }
}

class ReadOnlyPropertyInitializedObservable<T>(
    readOnlyProperty: ReadOnlyProperty<Any?, T>,
    observation: ObservationInitialized<T> = ObservationInitialized(
        Value(
            run {
                val v by readOnlyProperty
                v
            }
        )
    )
) :
    BaseInitializedObservable<T> (observation), InitializedObservable<T> {

    init {
        readOnlyPropertyObservableHelper(readOnlyProperty, observation)
    }
}

class ReadWritePropertyObservableHelper<R : T, T>(
    readWriteProperty: ReadWriteProperty<Any?, T>,
    private val observation: Observation<R, T, Value<R>>,
) : SuspendableSetter<T> {

    private var readWriteValue by readWriteProperty

    init {
        observation.beforeObservedValueGet = { current ->
            val new = readWriteValue
            if (new != current.valueOrNull)
                Value(new)
            else
                @Suppress("UNCHECKED_CAST")
                current as ObservableOptional<T>
        }
    }

    override suspend fun set(newValue: T) = post(newValue)

    override fun post(newValue: T) {
        readWriteValue = newValue
        observation.observedValue = Value(newValue)
    }
}

/**
 * [Initialized] subject that matches its value to a [ReadWriteProperty].
 * While the subject updated the [ReadWriteProperty], changes to the property are not immediately delegated back to the subject.
 * Use a [Flow] based subject if synchronized values are required
 */
class ReadWritePropertyInitializedSubject<T>(
    readWriteProperty: ReadWriteProperty<Any?, T>,
    initialValue: Value<T> = Value(
        run {
            val v by readWriteProperty
            v
        }
    ),
    coroutineScope: CoroutineScope?,
    context: CoroutineContext? = coroutineScope?.coroutineContext,
    observation: ObservationInitialized<T> = ObservationInitialized(initialValue)
) :
    BaseInitializedSubject<T>(
        observation
    ),
    SuspendableSetter<T> by ReadWritePropertyObservableHelper(readWriteProperty, observation) {
    init {
        coroutineScope?.let { bind(it, context ?: coroutineScope.coroutineContext) }
    }
}

class ReadWritePropertyDefaultSubject<R : T?, T>(
    defaultValue: R,
    readWriteProperty: ReadWriteProperty<Any?, T?>,
    initialValue: Value<T?> = Value(
        run {
            val v by readWriteProperty
            v
        }
    ),
    coroutineScope: CoroutineScope?,
    context: CoroutineContext? = coroutineScope?.coroutineContext,
    observation: ObservationDefault<R, T?> = ObservationDefault(defaultValue, initialValue)
) :
    BaseDefaultSubject<R, T>(
        observation
    ),
    SuspendableSetter<T?> by ReadWritePropertyObservableHelper(readWriteProperty, observation) {
    init {
        coroutineScope?.let {
            bind(coroutineScope, context ?: coroutineScope.coroutineContext)
        }
    }
}

fun <R : T, T> ReadOnlyProperty<Any?, T?>.toDefaultObservable(defaultValue: R) =
    ReadOnlyPropertyDefaultObservable(defaultValue, this)

fun <T> ReadOnlyProperty<Any?, T>.toInitializedObservable() =
    ReadOnlyPropertyInitializedObservable(this)

fun <T> ReadWriteProperty<Any?, T>.toInitializedSubject(
    coroutineScope: CoroutineScope? = null,
    context: CoroutineContext? = coroutineScope?.coroutineContext
) =
    ReadWritePropertyInitializedSubject(this, context = context, coroutineScope = coroutineScope)

fun <R : T, T> ReadWriteProperty<Any?, T?>.toDefaultSubject(
    defaultValue: R,
    coroutineScope: CoroutineScope? = null,
    context: CoroutineContext? = coroutineScope?.coroutineContext
): ReadWritePropertyDefaultSubject<R, T?> =
    ReadWritePropertyDefaultSubject(
        defaultValue = defaultValue,
        readWriteProperty = this,
        coroutineScope = coroutineScope,
        context = context,
    )

fun <T> Flow<T>.toUninitializedObservable(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = FlowObservable(coroutineScope, context, this)

fun <T> MutableStateFlow<T>.toInitializedObservable(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = FlowInitializedObservable(this.value, coroutineScope, context, this)

fun <T> Flow<T>.toInitializedObservable(
    initialValue: T,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = FlowInitializedObservable(initialValue, coroutineScope, context, this)

fun <R : T, T> Flow<T?>.toDefaultObservable(
    defaultValue: R,
    initialValue: T? = defaultValue,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext,
) = DefaultFlowObservable(defaultValue, initialValue, coroutineScope, context, this)

fun <R : T?, T> HotFlowable<T?>.toDefaultSubject(
    defaultValue: R,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = HotFlowableDefaultSubject(defaultValue, coroutineScope, context, this)

fun <T> HotFlowable<T>.toInitializedSubject(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = HotFlowableInitializedSubject(coroutineScope, context, this)

fun <R : T, T> MutableStateFlow<T?>.toDefaultSubject(
    defaultValue: R,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = StateFlowDefaultSubject(defaultValue, coroutineScope, context, this)

fun <T> MutableStateFlow<T>.toInitializedSubject(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = StateFlowInitializedSubject(coroutineScope, context, this)

fun <T> MutableSharedFlow<T>.toUninitializedSubject(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = SharedFlowSubject(
    coroutineScope,
    context,
    this
)

fun <T> MutableSharedFlow<T>.toInitializedSubject(
    initialValue: T,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = SharedFlowInitializedSubject(
    initialValue,
    coroutineScope,
    context,
    this
)

fun <R : T?, T> MutableSharedFlow<T?>.toDefaultSubject(
    defaultValue: R,
    initialValue: T? = defaultValue,
    coroutineScope: CoroutineScope,
    context: CoroutineContext = coroutineScope.coroutineContext
) = SharedFlowDefaultSubject(
    defaultValue,
    initialValue,
    coroutineScope,
    context,
    this
)

fun <T> observableOf(initialValue: T) = SimpleInitializedObservable(initialValue)

fun <T> subjectOf(initialValue: T) = SimpleInitializedSubject(initialValue)
