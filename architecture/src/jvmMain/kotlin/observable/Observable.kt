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

import com.splendo.kaluga.base.flow.HotFlowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

actual abstract class Observable<T> : BaseObservable<T>() {

    private val observers = mutableListOf<(T) -> Unit>()
    protected var value: ObservableOptional<T> by Delegates.observable(ObservableOptional.Nothing()) { _, _, new ->
        val result = new as? ObservableOptional.Value<T> ?: return@observable
        observers.forEach { it.invoke(result.value) }
    }

    fun observe(onNext: (T) -> Unit): Disposable {
        observers.add(onNext)
        val lastResult = value
        if (lastResult is ObservableOptional.Value<T>) {
            onNext.invoke(lastResult.value)
        }
        return Disposable { observers.remove(onNext) }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableOptional<T> {
        return value
    }
}

class DefaultObservable<T>(initialValue: T) : Observable<T>() {
    init {
        value = ObservableOptional.Value(initialValue)
    }
}

class ReadOnlyPropertyObservable<T>(readOnlyProperty: ReadOnlyProperty<Any?, T>) : Observable<T>() {
    private val initialValue by readOnlyProperty
    init {
        value = ObservableOptional.Value(initialValue)
    }
}

class FlowObservable<T>(private val flow: Flow<T>, coroutineScope: CoroutineScope) : Observable<T>() {

    init {
        coroutineScope.launch(Dispatchers.Main) {
            flow.collect {
                value = ObservableOptional.Value(it)
            }
        }
    }
}

actual abstract class Subject<T> : Observable<T>(), ReadWriteProperty<Any?, ObservableOptional<T>> {

    actual open fun post(newValue: T) {
        value = ObservableOptional.Value(newValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: ObservableOptional<T>) {
        this.value = value
    }
}

class DefaultSubject<T>(initialValue: T) : Subject<T>() {

    init {
        value = ObservableOptional.Value(initialValue)
    }
}

class ObservablePropertySubject<T>(observableProperty: ObservableProperty<T>) : Subject<T>() {

    private var remoteValue by observableProperty

    init {
        value = ObservableOptional.Value(remoteValue)
    }

    override fun post(newValue: T) {
        remoteValue = newValue
        super.post(newValue)
    }
}

class FlowableSubject<T>(private val flowable: HotFlowable<T>, private val coroutineScope: CoroutineScope) : Subject<T>() {

    init {
        coroutineScope.launch(Dispatchers.Main) {
            flowable.flow().collect {
                super.post(it)
            }
        }
    }

    override fun post(newValue: T) {
        coroutineScope.launch(Dispatchers.Main) {
            flowable.set(newValue)
        }
    }
}

/**
 * [Subject] that synchronizes its value to a [MutableStateFlow]
 * @param stateFlow The [MutableStateFlow] to synchronize to
 * @param coroutineScope The [CoroutineScope] on which to observe changes to the [HotFlowable]
 */
class StateFlowSubject<T>(private val stateFlow: MutableStateFlow<T>, private val coroutineScope: CoroutineScope) : Subject<T>() {

    init {
        coroutineScope.launch(Dispatchers.Main) {
            stateFlow.collect {
                super.post(it)
            }
        }
    }

    override fun post(newValue: T) {
        coroutineScope.launch(Dispatchers.Main) {
            stateFlow.value = newValue
        }
    }
}

actual fun <T> ReadOnlyProperty<Any?, T>.toObservable(): Observable<T> = ReadOnlyPropertyObservable(this)

actual fun <T> ObservableProperty<T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = ObservablePropertySubject(this)

actual fun <T> Flow<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this, coroutineScope)

actual fun <T> HotFlowable<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this.flow(), coroutineScope)

actual fun <T> HotFlowable<T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = FlowableSubject(this, coroutineScope)

actual fun <T> MutableStateFlow<T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = StateFlowSubject(this, coroutineScope)

actual fun <T> observableOf(initialValue: T): Observable<T> = DefaultObservable(initialValue)

actual fun <T> subjectOf(initialValue: T, coroutineScope: CoroutineScope): Subject<T> = DefaultSubject(initialValue)
