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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

    /**
     * The [LiveData] matching [stateFlow]
     */
    val liveData: LiveData<T>
        get() = stateFlow.asLiveData()
}

/**
 * Observes [WithState] on the lifecycle of a [LifecycleOwner]
 * @param lifecycleOwner The [LifecycleOwner] on which to observe.
 * @param filter A filtering function to filter out certain values.
 * @param onNext Action to execute when a new value is observed.
 */
fun <T> WithState<T>.observeOnLifecycle(lifecycleOwner: LifecycleOwner, filter: suspend (T) -> Boolean = { true }, onNext: (T) -> Unit) =
    observeOnLifecycle(lifecycleOwner, filter = filter, transform = { it }, onNext = onNext)

/**
 * Observes [WithState] of a nullable value on the lifecycle of a [LifecycleOwner]
 * @param lifecycleOwner The [LifecycleOwner] on which to observe.
 * @param filter A filtering function to filter out certain values.
 * @param onNext Action to execute when a new non-null value is observed.
 */
fun <T> WithState<T?>.observeNotNullOnLifecycle(lifecycleOwner: LifecycleOwner, filter: suspend (T) -> Boolean = { true }, onNext: (T) -> Unit) = observeOnLifecycle(
    lifecycleOwner,
    filter = { value -> value?.let { filter(it) } ?: false },
    transform = { it!! },
    onNext = onNext,
)

/**
 * Observes [WithState] on the lifecycle of a [LifecycleOwner] by transforming the observed value.
 * @param lifecycleOwner The [LifecycleOwner] on which to observe.
 * @param filter A filtering function to filter out certain values.
 * @param transform Transforms the next value into a different value of type [R].
 * @param onNext Action to execute when a new value is observed.
 */
fun <T, R> WithState<T>.observeOnLifecycle(lifecycleOwner: LifecycleOwner, filter: suspend (T) -> Boolean = { true }, transform: suspend (T) -> R, onNext: (R) -> Unit) =
    lifecycleOwner.lifecycleScope.launch {
        stateFlow.filter { filter(it) }.map { transform(it) }.collect {
            onNext(it)
        }
    }
