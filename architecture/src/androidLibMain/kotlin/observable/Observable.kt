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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

actual abstract class Observable<T>: ReadOnlyProperty<Any?, ObservableResult<T>> {

    abstract val liveData: LiveData<T>

    fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        liveData.observe(owner, observer)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableResult<T> {
        return liveData.value?.let {ObservableResult.Result(it)} ?: ObservableResult.Nothing()
    }
}

class DefaultObservable<T>(initialValue: T) : Observable<T>() {
    override val liveData = MutableLiveData(initialValue)
}


class ReadOnlyPropertyObservable<T>(readOnlyProperty: ReadOnlyProperty<Any?, T>): Observable<T>() {
    private val value by readOnlyProperty
    override val liveData: LiveData<T> = MutableLiveData(value)
}
class FlowObservable<T>(flow: Flow<T>, coroutineScope: CoroutineScope) : Observable<T>() {
    override val liveData: LiveData<T> = flow.asLiveData(coroutineScope.coroutineContext)
}

actual abstract class Subject<T>(private val coroutineScope: CoroutineScope): Observable<T>(), ReadWriteProperty<Any?, ObservableResult<T>> {

    protected abstract val providerLiveData: LiveData<T>
    private val mediatorLiveData = MediatorLiveData<T>()
    override val liveData: MutableLiveData<T> = mediatorLiveData
    protected abstract val liveDataObserver: Observer<T>

    protected fun initialize() {
        mediatorLiveData.addSource(providerLiveData) { value -> mediatorLiveData.postValue(value)}
        coroutineScope.launch {
            liveData.observeForever(liveDataObserver)
            while(isActive) {
                delay(100)
            }
        }.invokeOnCompletion { liveData.removeObserver(liveDataObserver) }

    }

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

class DefaultSubject<T>(initialValue: T, coroutineScope: CoroutineScope) : Subject<T>(coroutineScope) {
    override val providerLiveData: LiveData<T> = MutableLiveData(initialValue)
    override val liveDataObserver = Observer<T> {}

    init {
        initialize()
    }

}

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

class FlowSubject<T>(private val flowable: BaseFlowable<T>, private val coroutineScope: CoroutineScope) : Subject<T>(coroutineScope) {
    override val providerLiveData: LiveData<T> = flowable.flow().asLiveData(coroutineScope.coroutineContext)
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