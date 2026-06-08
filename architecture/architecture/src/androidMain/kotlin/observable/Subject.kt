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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * The [Observer] that observes the observable value.
 */
val <R : T, T, OO : ObservableOptional<R>> BasicSubject<R, T, OO>.liveDataObserver: Observer<T>
    get() = Observer<T> { this.post(it) }

private fun <B, R : T, T, OO : ObservableOptional<R>> B.mutableLiveData(): MutableLiveData<R> where B : BasicSubject<R, T, OO>, B : WithMutableState<R> {
    val mediatorLiveData = MediatorLiveData<R>()
    mediatorLiveData.addSource(stateFlow.asLiveData()) { value ->
        mediatorLiveData.postValue(value)
    }

    return mediatorLiveData
}

/**
 * Observes [LiveData] using an [Observer] on a [CoroutineScope]
 * @param coroutineScope The [CoroutineScope] on which to observe.
 * @param observer The [Observer] to observe the [LiveData]
 */
fun <T> LiveData<T>.observeOnCoroutine(coroutineScope: CoroutineScope, observer: Observer<T>) {
    // Live Data mutations should only ever be done from the main thread, so we don't (any longer) allow passing a context
    coroutineScope.launch(Dispatchers.Main.immediate) {
        val currentValue = value
        if (currentValue != null) observer.onChanged(currentValue) // due to slight delay in launch we could miss value changes
        observeForever(observer)
        awaitCancellation()
    }.invokeOnCompletion {
        removeObserver(observer)
    }
}

actual interface PlatformSubjectObserver<R> {
    fun createLiveData(): MutableLiveData<R>

    /**
     * Gets an [Observer] to observe changes to this subject.
     */
    fun liveDataObserver(): Observer<R>
}

/**
 * An abstract class that extends [AbstractBaseSubject].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T].
 * @param OO the type of [ObservableOptional] to store the result in.
 * @param observation The [Observation] to handle observing the value.
 * @param stateFlowToBind A function to get the [StateFlow] that will automatically call [SuspendableSetter.set] when a new value is posted after [BasicSubject.post] has been called.
 */
actual abstract class BaseSubject<R : T, T, OO : ObservableOptional<R>> actual constructor(observation: Observation<R, T, OO>, stateFlowToBind: suspend () -> StateFlow<R?>) :
    AbstractBaseSubject<R, T, OO>(observation, stateFlowToBind) {

    private var coroutineScope: CoroutineScope? = null
    private val mutableLiveDataDelegate = lazy {
        platformSubjectObserver.createLiveData().also {
            coroutineScope?.let { coroutineScope ->
                it.observeOnCoroutine(coroutineScope, observer = platformSubjectObserver.liveDataObserver())
            }
        }
    }

    /**
     * The [MutableLiveData] synchronizing the value of the subject.
     */
    val mutableLiveData by mutableLiveDataDelegate
    protected actual abstract val platformSubjectObserver: PlatformSubjectObserver<R>

    /**
     * Gets an [Observer] to observe changes to this subject.
     */
    fun liveDataObserver(): Observer<R> = platformSubjectObserver.liveDataObserver()

    actual final override fun bind(coroutineScope: CoroutineScope, context: CoroutineContext) {
        super.bind(coroutineScope, context)
        this.coroutineScope = coroutineScope
        if (mutableLiveDataDelegate.isInitialized()) {
            mutableLiveData.observeOnCoroutine(coroutineScope, observer = platformSubjectObserver.liveDataObserver())
        }
    }
}

/**
 * An abstract class extending [AbstractBaseUninitializedSubject].
 * @param T the type of value to expect.
 * @param observation The [ObservationUninitialized] to handle value being observed
 */
actual abstract class BaseUninitializedSubject<T> actual constructor(observation: ObservationUninitialized<T>) : AbstractBaseUninitializedSubject<T>(observation) {

    actual final override val platformSubjectObserver: PlatformSubjectObserver<T> = object : PlatformSubjectObserver<T> {
        override fun createLiveData(): MutableLiveData<T> {
            val mediatorLiveData = MediatorLiveData<T>()
            mediatorLiveData.addSource(stateFlow.asLiveData()) { value ->
                mediatorLiveData.postValue(value)
            }
            return mediatorLiveData
        }
        override fun liveDataObserver() = Observer<T> { value -> value?.let { post(it) } }
    }
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

    actual final override val platformSubjectObserver: PlatformSubjectObserver<T> = object : PlatformSubjectObserver<T> {
        override fun createLiveData(): MutableLiveData<T> = mutableLiveData()
        override fun liveDataObserver() = liveDataObserver
    }
}

/**
 * An abstract class extending [AbstractBaseDefaultSubject].
 * @param T the type of value to expect.
 * @param R the type of result to expect. Must be a subclass of [T]
 * @param observation The [ObservationUninitialized] to handle value being observed
 */
actual abstract class BaseDefaultSubject<R : T?, T> actual constructor(observation: ObservationDefault<R, T?>) : AbstractBaseDefaultSubject<R, T>(observation) {

    /**
     * Constructor
     * @param defaultValue The default [Value] of [R] to return if the current value is [ObservableOptional.Nothing] or [ObservableOptional.Value] containing `null`.
     * @param initialValue The initial [Value] of [T].
     */
    actual constructor(
        defaultValue: Value<R>,
        initialValue: Value<T?>,
    ) : this(observation = ObservationDefault<R, T?>(defaultValue, initialValue))

    actual final override val platformSubjectObserver: PlatformSubjectObserver<R> = object : PlatformSubjectObserver<R> {
        override fun createLiveData(): MutableLiveData<R> = mutableLiveData()
        override fun liveDataObserver() = Observer<R> { post(it) }
    }
}
