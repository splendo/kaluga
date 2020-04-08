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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

actual abstract class Observable<T>: ReadOnlyProperty<Any, ObservableResult<T>> {

    abstract val liveData: LiveData<T>

    override fun getValue(thisRef: Any, property: KProperty<*>): ObservableResult<T> {
        return liveData.value?.let {ObservableResult.Result(it)} ?: ObservableResult.Nothing()
    }
}

class ReadOnlyPropertyObservable<T>(readOnlyProperty: ReadOnlyProperty<Any, T>): Observable<T>() {
    val value by readOnlyProperty
    override val liveData: LiveData<T> = MutableLiveData(value)
}

class FlowObservable<T>(flow: Flow<T>, coroutineScope: CoroutineScope) : Observable<T>() {
    override val liveData: LiveData<T> = flow.asLiveData(coroutineScope.coroutineContext)
}

actual abstract class Subject<T>(private val coroutineScope: CoroutineScope): Observable<T>(), ReadWriteProperty<Any, ObservableResult<T>> {

    protected abstract val providerLiveData: LiveData<T>
    override val liveData: MutableLiveData<T> = MediatorLiveData<T>().apply { addSource(providerLiveData) {value -> setValue(value)} }
    protected abstract val liveDataObserver: Observer<T>?

    protected fun initialize() {
        val observer = liveDataObserver ?: return
        coroutineScope.launch {
            liveData.observeForever(observer)
        }.invokeOnCompletion {
            liveData.removeObserver(observer)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): ObservableResult<T> {
        return liveData.value?.let {ObservableResult.Result(it)} ?: ObservableResult.Nothing()
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: ObservableResult<T>) {
        val newValue = value as? ObservableResult.Result ?: return
        liveData.postValue(newValue.value)
    }
}

class ReadOnlyPropertySubject<T>(readOnlyProperty: ReadOnlyProperty<Any, T>, coroutineScope: CoroutineScope): Subject<T>(coroutineScope) {

    private val initialValue by readOnlyProperty
    override val providerLiveData: LiveData<T> = MutableLiveData(initialValue)
    override val liveDataObserver: Observer<T>? = null

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

actual fun <T> ReadOnlyProperty<Any, T>.toObservable(): Observable<T> = ReadOnlyPropertyObservable(this)

actual fun <T> ReadOnlyProperty<Any, T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = ReadOnlyPropertySubject(this, coroutineScope)

actual fun <T> Flow<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this, coroutineScope)

actual fun <T> BaseFlowable<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this.flow(), coroutineScope)

actual fun <T> BaseFlowable<T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = FlowSubject(this, coroutineScope)

