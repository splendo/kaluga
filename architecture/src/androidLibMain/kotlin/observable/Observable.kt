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
@file:JvmName("AndroidObservable")

package com.splendo.kaluga.architecture.observable

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty

actual interface WithState<T> {
    actual val stateFlow: StateFlow<T>
    actual val valueDelegate: ReadOnlyProperty<Any?, T>
    val liveData: LiveData<T>
        get() = stateFlow.asLiveData()
}

val <R : T, T, OO : ObservableOptional<R>> BasicSubject<R, T, OO>.liveDataObserver: Observer<T>
    get() = Observer<T> { this.post(it) }

private fun <B, R : T, T, OO : ObservableOptional<R>> B.mutableLiveData(): MutableLiveData<R> where B : BasicSubject<R, T, OO>, B : WithMutableState<R> {
    val mediatorLiveData = MediatorLiveData<R>()
    mediatorLiveData.addSource(stateFlow.asLiveData()) { value ->
        mediatorLiveData.postValue(value)
    }

    return mediatorLiveData
}

fun <T> LiveData<T>.observeOnCoroutine(
    coroutineScope: CoroutineScope,
    observer: Observer<T>
) {
    // Live Data mutations should only ever be done from the main thread, so we don't (any longer) allow passing a context
    coroutineScope.launch(Dispatchers.Main.immediate) {
        if (value != null) observer.onChanged(value) // due to slight delay in launch we could miss value changes
        observeForever(observer)
        awaitCancellation()
    }.invokeOnCompletion {
        removeObserver(observer)
    }
}

actual abstract class BaseSubject<R : T, T, OO : ObservableOptional<R>> actual constructor(
    observation: Observation<R, T, OO>,
    stateFlowToBind: suspend () -> StateFlow<R?>
) : AbstractBaseSubject<R, T, OO>(observation, stateFlowToBind) {

    private var coroutineScope: CoroutineScope? = null
    private val mutableLiveDataDelegate = lazy {
        createLiveData().also {
            coroutineScope?.let { coroutineScope ->
                it.observeOnCoroutine(coroutineScope, observer = liveDataObserver())
            }
        }
    }
    val mutableLiveData by mutableLiveDataDelegate
    protected abstract fun createLiveData(): MutableLiveData<R>
    abstract fun liveDataObserver(): Observer<R>

    final override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext) {
        super.bind(coroutineScope, context)
        this.coroutineScope = coroutineScope
        if (mutableLiveDataDelegate.isInitialized()) {
            mutableLiveData.observeOnCoroutine(coroutineScope, observer = liveDataObserver())
        }
    }
}

actual abstract class BaseUninitializedSubject<T> actual constructor(
    observation: ObservationUninitialized<T>
) : AbstractBaseUninitializedSubject<T>(observation) {

    override fun createLiveData(): MutableLiveData<T> {
        val mediatorLiveData = MediatorLiveData<T>()
        mediatorLiveData.addSource(stateFlow.asLiveData()) { value ->
            mediatorLiveData.postValue(value)
        }
        return mediatorLiveData
    }

    override fun liveDataObserver() = Observer<T> { value -> value?.let { post(it) } }
}

actual abstract class BaseInitializedSubject<T> actual constructor(observation: ObservationInitialized<T>) :
    AbstractBaseInitializedSubject<T>(observation) {

    override fun createLiveData(): MutableLiveData<T> = this.mutableLiveData()
    override fun liveDataObserver() = liveDataObserver

    actual constructor(
        initialValue: ObservableOptional.Value<T>
    ) : this (ObservationInitialized(initialValue))
}

actual abstract class BaseDefaultSubject<R : T?, T> actual constructor(
    observation: ObservationDefault<R, T?>
) : AbstractBaseDefaultSubject<R, T>(observation) {

    override fun createLiveData(): MutableLiveData<R> = this.mutableLiveData()
    override fun liveDataObserver() = Observer<R> { post(it) }

    actual constructor(
        defaultValue: ObservableOptional.Value<R>,
        initialValue: ObservableOptional.Value<T?>
    ) : this(observation = ObservationDefault<R, T?>(defaultValue, initialValue))
}

fun <T> WithState<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    filter: suspend (T) -> Boolean = { true },
    onNext: (T) -> Unit
) =
    observeOnLifecycle(lifecycleOwner, filter = filter, transform = { it }, onNext = onNext)

fun <T> WithState<T?>.observeNotNullOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    filter: suspend (T) -> Boolean = { true },
    onNext: (T) -> Unit
) =
    observeOnLifecycle(
        lifecycleOwner,
        filter = { value -> value?.let { filter(it) } ?: false },
        transform = { it!! },
        onNext = onNext
    )

fun <T, R> WithState<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    filter: suspend (T) -> Boolean = { true },
    transform: suspend (T) -> R,
    onNext: (R) -> Unit
) = lifecycleOwner.lifecycleScope.launch {
    stateFlow.filter { filter(it) }.map { transform(it) }.collect {
        onNext(it)
    }
}
