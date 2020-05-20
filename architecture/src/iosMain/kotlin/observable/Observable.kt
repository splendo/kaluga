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

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.architecture.viewmodel.addLifecycleManager
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

actual abstract class Observable<T>: ReadOnlyProperty<Any?, ObservableResult<T>> {

    private val observers = mutableListOf<(T) -> Unit>()
    protected var value: ObservableResult<T> by Delegates.observable(ObservableResult.Nothing()) { _, _, new ->
        val result = new as? ObservableResult.Result<T> ?: return@observable
        observers.forEach { it.invoke(result.value) }
    }

    /**
     * Adds an observing function to the Observable to be notified on each change to the observable
     * @param onNext Function to be called each time the value of the Observable changes
     * @return [Disposable] that removes the observing function when disposed
     */
    fun observe(onNext: (T) -> Unit): Disposable {
        observers.add(onNext)
        val lastResult = value
        if (lastResult is ObservableResult.Result<T>) {
            onNext.invoke(lastResult.value)
        }
        return Disposable { observers.remove(onNext) }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): ObservableResult<T> {
        return value
    }
}

/**
 * Simple [Observable] that takes an initial value
 * @param initialValue The initial value of the Observable
 */
class DefaultObservable<T>(initialValue: T) : Observable<T>() {
    init {
        value = ObservableResult.Result(initialValue)
    }
}

/**
 * [Observable] whose initial value matches a given [ReadOnlyProperty]
 * @param readOnlyProperty The [ReadOnlyProperty] to match with the initial value
 */
class ReadOnlyPropertyObservable<T>(readOnlyProperty: ReadOnlyProperty<Any?, T>): Observable<T>() {
    private val initialValue by readOnlyProperty
    init {
        value = ObservableResult.Result(initialValue)
    }
}

/**
 * [Observable] whose value matches a given [Flow]
 * @param flow The [Flow] whose value to match
 * @param coroutineScope The [CoroutineScope] on which the observe the [Flow]
 */
class FlowObservable<T>(private val flow: Flow<T>, coroutineScope: CoroutineScope) : Observable<T>() {

    init {
        coroutineScope.launch(MainQueueDispatcher) {
            flow.collect {
                value = ObservableResult.Result(it)
            }
        }
    }
}

actual abstract class Subject<T> : Observable<T>(), ReadWriteProperty<Any?, ObservableResult<T>> {

    /**
     * Updates the value of the [Subject]
     * @param newValue The new value of the subject
     */
    abstract fun post(newValue: T)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: ObservableResult<T>) {
        this.value = value
    }
}

/**
 * Simple [Subject] that takes an initial value
 * @param initialValue The initial value of the subject
 */
class DefaultSubject<T>(initialValue: T): Subject<T>() {

    init {
        value = ObservableResult.Result(initialValue)
    }

    override fun post(newValue: T) {
        value = ObservableResult.Result(newValue)
    }
}

/**
 * [Subject] that matches its value to a [ObservableProperty].
 * While the subject updated the [ObservableProperty], changes to the property are not delegated back to the subject.
 * Use [FlowSubject] if synchronized values are required
 */
class ObservablePropertySubject<T>(observableProperty: ObservableProperty<T>): Subject<T>() {

    private var remoteValue by observableProperty

    init {
        value = ObservableResult.Result(remoteValue)
    }

    override fun post(newValue: T) {
        remoteValue = newValue
        value = ObservableResult.Result(newValue)
    }
}

/**
 * [Subject] that synchronizes its value to a [BaseFlowable]
 * @param flowable The [BaseFlowable] to synchronize to
 * @param coroutineScope The [CoroutineScope] on which to observe changes to the [BaseFlowable]
 */
class FlowSubject<T>(private val flowable: BaseFlowable<T>, private val coroutineScope: CoroutineScope) : Subject<T>() {

    init {
        coroutineScope.launch(MainQueueDispatcher) {
            flowable.flow().collect {
                value = ObservableResult.Result(it)
            }
        }
    }

    override fun post(newValue: T) {
        coroutineScope.launch(MainQueueDispatcher) {
            flowable.set(newValue)
        }
    }

}
actual fun <T> ReadOnlyProperty<Any?, T>.toObservable(): Observable<T> = ReadOnlyPropertyObservable(this)

actual fun <T> ObservableProperty<T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = ObservablePropertySubject(this)

actual fun <T> Flow<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this, coroutineScope)

actual fun <T> BaseFlowable<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this.flow(), coroutineScope)

actual fun <T> BaseFlowable<T>.toSubject(coroutineScope: CoroutineScope): Subject<T> = FlowSubject(this, coroutineScope)

actual fun <T> observableOf(initialValue: T) : Observable<T> = DefaultObservable(initialValue)

actual fun <T> subjectOf(initialValue: T, coroutineScope: CoroutineScope) : Subject<T> = DefaultSubject(initialValue)