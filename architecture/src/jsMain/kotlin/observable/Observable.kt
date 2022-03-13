/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty

actual interface WithState<T> {
    actual val stateFlow: StateFlow<T>
    actual val valueDelegate: ReadOnlyProperty<Any?, T>
}

actual abstract class BaseSubject<R : T, T, OO : ObservableOptional<R>> actual constructor(
    observation: Observation<R, T, OO>,
    stateFlowToBind: () -> StateFlow<R?>
) : AbstractBaseSubject<R, T, OO>(observation, stateFlowToBind) {

    final override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext) {
        super.bind(coroutineScope, context)
    }
}

actual abstract class BaseUninitializedSubject<T> actual constructor(
    observation: ObservationUninitialized<T>
) : AbstractBaseUninitializedSubject<T>(observation)

actual abstract class BaseInitializedSubject<T> actual constructor(observation: ObservationInitialized<T>) : AbstractBaseInitializedSubject<T>(observation) {

    actual constructor(
        initialValue: ObservableOptional.Value<T>
    ) : this (ObservationInitialized(initialValue))
}

actual abstract class BaseDefaultSubject<R : T?, T> actual constructor(
    observation: ObservationDefault<R, T?>
) : AbstractBaseDefaultSubject<R, T>(observation) {

    actual constructor(
        defaultValue: ObservableOptional.Value<R>,
        initialValue: ObservableOptional.Value<T?>
    ) : this(observation = ObservationDefault<R, T?>(defaultValue, initialValue))
}
