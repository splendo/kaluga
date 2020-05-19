/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import androidx.lifecycle.*
import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

actual abstract class Observable<T> :  ReadOnlyProperty<Any?, ObservableResult<T>> {

    /**
     * [LiveData] syncing with this observable
     */
    abstract val liveData: LiveData<T>

    /**
     * Adds an [Observer] to this Observable
     * @param owner The [LifecycleOwner] of the [Observer]
     * @param observer The [Observer] to observe this Observable
     */
    fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        liveData.observe(owner, observer)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableResult<T> {
        return liveData.value?.let {ObservableResult.Result(it)} ?: ObservableResult.Nothing()
    }
}

/**
 * Simple [Observable] that takes an initial value
 * @param initialValue The initial value of the Observable
 */
class DefaultObservable<T>(initialValue: T) : Observable<T>() {
    override val liveData = MutableLiveData(initialValue)
}

/**
 * [Observable] whose initial value matches a given [ReadOnlyProperty]
 * @param readOnlyProperty The [ReadOnlyProperty] to match with the initial value
 */
class ReadOnlyPropertyObservable<T>(readOnlyProperty: ReadOnlyProperty<Any?, T>): Observable<T>() {
    private val value by readOnlyProperty
    override val liveData: LiveData<T> = MutableLiveData(value)
}

/**
 * [Observable] whose value matches a given [Flow]
 * @param flow The [Flow] whose value to match
 * @param coroutineScope The [CoroutineScope] on which the observe the [Flow]
 */
class FlowObservable<T>(flow: Flow<T>, coroutineScope: CoroutineScope) : Observable<T>() {
    override val liveData: LiveData<T> = flow.asLiveData(coroutineScope.coroutineContext)
}

actual abstract class Subject<T>(private val coroutineScope: CoroutineScope): Observable<T>(), ReadWriteProperty<Any?, ObservableResult<T>> {

    protected abstract val providerLiveData: LiveData<T>
    private val mediatorLiveData = MediatorLiveData<T>()

    /**
     * [MutableLiveData] syncing with this Subject
     */
    override val liveData: MutableLiveData<T> = mediatorLiveData
    protected abstract val liveDataObserver: Observer<T>

    protected fun initialize() {
        mediatorLiveData.addSource(providerLiveData) {
            value -> mediatorLiveData.postValue(value)
        }
        coroutineScope.launch {
            liveData.observeForever(liveDataObserver)
            while(isActive) {
                delay(100)
            }
        }.invokeOnCompletion { liveData.removeObserver(liveDataObserver) }

    }

    /**
     * Posts a new value to the Subject
     * @param value New value to set
     */
    fun postValue(value: T) {
        liveData.postValue(value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableResult<T> {
        return liveData.value?.let {ObservableResult.Result(it)} ?: ObservableResult.Nothing()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: ObservableResult<T>) {
        val newValue = value as? ObservableResult.Result ?: return
        liveData.postValue(newValue.value)
    }
}

/**
 * Simple [Subject] that takes an initial value
 * @param initialValue The initial value of the subject
 */
class DefaultSubject<T>(initialValue: T, coroutineScope: CoroutineScope) : Subject<T>(coroutineScope) {
    override val providerLiveData: LiveData<T> = MutableLiveData(initialValue)
    override val liveDataObserver = Observer<T> {}

    init {
        initialize()
    }

}

/**
 * [Subject] that matches its value to a [ObservableProperty].
 * While the subject updated the [ObservableProperty], changes to the property are not delegated back to the subject.
 * Use [FlowSubject] if synchronized values are required
 */
class ObservablePropertySubject<T>(observableProperty: ObservableProperty<T>, coroutineScope: CoroutineScope): Subject<T>(coroutineScope) {

    private var value by observableProperty
    override val providerLiveData: LiveData<T> = MutableLiveData(value)
    override val liveDataObserver = Observer<T> { t ->
       value = t
    }

    init {
        initialize()
    }
}

/**
 * [Subject] that synchronizes its value to a [BaseFlowable]
 * @param flowable The [BaseFlowable] to synchronize to
 * @param coroutineScope The [CoroutineScope] on which to observe changes to the [BaseFlowable]
 */
class FlowSubject<T>(private val flowable: BaseFlowable<T>, private val coroutineScope: CoroutineScope) : Subject<T>(coroutineScope) {
    override val providerLiveData: LiveData<T> = flowable.flow().distinctUntilChanged().asLiveData(coroutineScope.coroutineContext)
    override val liveDataObserver = Observer<T> { t ->
        coroutineScope.launch {
            flowable.set(t)
        }
    }

    init {
        initialize()
    }

}

actual fun <T> ReadOnlyProperty<Any?, T>.toObservable(): Observable<T> = ReadOnlyPropertyObservable(this)

actual fun <T> ObservableProperty<T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = ObservablePropertySubject(this, coroutineScope)

actual fun <T> Flow<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this, coroutineScope)

actual fun <T> BaseFlowable<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this.flow(), coroutineScope)

actual fun <T> BaseFlowable<T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = FlowSubject(this, coroutineScope)

actual fun <T> observableOf(initialValue: T) : Observable<T> = DefaultObservable(initialValue)

actual fun <T> subjectOf(initialValue: T, coroutineScope: CoroutineScope) : Subject<T> = DefaultSubject(initialValue, coroutineScope)