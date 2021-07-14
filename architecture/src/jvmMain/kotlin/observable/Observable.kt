package com.splendo.kaluga.architecture.observable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty

actual interface WithState<T> {
    actual val stateFlow: StateFlow<T>
    actual val valueDelegate: ReadOnlyProperty<Any?, T>
}

actual abstract class BaseSubject<R:T, T, OO : ObservableOptional<R>> actual constructor(
    observation: Observation<R, T, OO>,
    stateFlowToBind:()-> StateFlow<R?>
): AbstractBaseSubject<R, T, OO>(observation, stateFlowToBind) {

    final override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext) {
        super.bind(coroutineScope, context)
    }
}

actual abstract class BaseUninitializedSubject<T> actual constructor(
    observation: ObservationUninitialized<T>
): AbstractBaseUninitializedSubject<T>(observation) {}

actual abstract class BaseInitializedSubject<T> actual constructor(observation: ObservationInitialized<T>) : AbstractBaseInitializedSubject<T>(observation) {

    actual constructor(
        initialValue: ObservableOptional.Value<T>,
        coroutineContext: CoroutineContext,
    ) : this (ObservationInitialized(initialValue, coroutineContext))
}

actual abstract class BaseDefaultSubject<R:T?, T> actual constructor(
    observation: ObservationDefault<R, T?>
) : AbstractBaseDefaultSubject<R, T>(observation) {

    actual constructor(
        defaultValue: ObservableOptional.Value<R>,
        initialValue: ObservableOptional.Value<T?>,
        coroutineContext: CoroutineContext
    ) : this(observation = ObservationDefault<R, T?>(defaultValue, initialValue, coroutineContext))
}