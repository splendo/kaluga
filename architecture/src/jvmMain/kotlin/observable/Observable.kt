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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty

/**
 * Interface indicating an observable has a state of [T]
 * @param T the type of the state.
 */
actual interface WithState<T> {

    /**
     * [StateFlow] that expresses the content from the observable.
     *
     * This can be initialized lazily.
     *
     * Accessing this from property outside the main thread might cause a race condition,
     * since observing the initial value needed for the stateflow has to happen on the main thread.
     *
     */
    actual val stateFlow: StateFlow<T>

    /**
     * A [ReadOnlyProperty] of [T]
     */
    actual val valueDelegate: ReadOnlyProperty<Any?, T>
}

actual interface PlatformSubjectObserver<R>

/**
 * An abstract class that extends [AbstractBaseSubject].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param OO the type of [ObservableOptional] to store the result in.
 * @param observation The [Observation] to handle observing the value.
 * @param stateFlowToBind A function to get the [StateFlow] that will automatically call [SuspendableSetter.set] when a new value is posted after [BasicSubject.post] has been called.
 */
actual abstract class BaseSubject<R : T, T, OO : ObservableOptional<R>> actual constructor(
    observation: Observation<R, T, OO>,
    stateFlowToBind: suspend () -> StateFlow<R?>,
) : AbstractBaseSubject<R, T, OO>(observation, stateFlowToBind) {

    actual final override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext) {
        super.bind(coroutineScope, context)
    }

    protected actual abstract val platformSubjectObserver: PlatformSubjectObserver<R>
}

/**
 * An abstract class extending [AbstractBaseUninitializedSubject].
 * @param T the type of value to expect.
 * @param observation The [ObservationUninitialized] to handle value being observed
 */
actual abstract class BaseUninitializedSubject<T> actual constructor(
    observation: ObservationUninitialized<T>,
) : AbstractBaseUninitializedSubject<T>(observation) {
    actual final override val platformSubjectObserver: PlatformSubjectObserver<T> = object : PlatformSubjectObserver<T> {}
}

/**
 * Abstract class implementing [AbstractBaseInitializedSubject]
 * @param T the type of value to expect.
 * @param observation The [ObservationInitialized] to handle value being observed
 */
actual abstract class BaseInitializedSubject<T> actual constructor(observation: ObservationInitialized<T>) : AbstractBaseInitializedSubject<T>(observation) {

    /**
     * Constructor using an inital value.
     * @param initialValue The [Value] to use as the initial value.
     */
    actual constructor(
        initialValue: Value<T>,
    ) : this (ObservationInitialized(initialValue))

    actual final override val platformSubjectObserver: PlatformSubjectObserver<T> = object : PlatformSubjectObserver<T> {}
}

/**
 * An abstract class extending [AbstractBaseDefaultSubject].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param observation The [ObservationUninitialized] to handle value being observed
 */
actual abstract class BaseDefaultSubject<R : T?, T> actual constructor(
    observation: ObservationDefault<R, T?>,
) : AbstractBaseDefaultSubject<R, T>(observation) {

    /**
     * Constructor
     * @param defaultValue The default [Value] of [R] to return if the current value is [ObservableOptional.Nothing] or [ObservableOptional.Value] containing `null`.
     * @param initialValue The initial [Value] of [T].
     */
    actual constructor(
        defaultValue: Value<R>,
        initialValue: Value<T?>,
    ) : this(observation = ObservationDefault<R, T?>(defaultValue, initialValue))

    actual final override val platformSubjectObserver: PlatformSubjectObserver<R> = object : PlatformSubjectObserver<R> {}
}
