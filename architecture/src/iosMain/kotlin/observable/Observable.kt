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

import com.splendo.kaluga.base.MainQueueDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

sealed class ObservableResult<T> {
    data class Result<T>(val value: T) : ObservableResult<T>()
    class Nothing<T> : ObservableResult<T>()
}

actual abstract class Observable<T> {

    abstract fun observe(onNext: (T) -> Unit): Disposable

}

class FlowObservable<T>(private val flow: Flow<T>, coroutineScope: CoroutineScope) : Observable<T>(), CoroutineScope by coroutineScope {

    private val observers = mutableListOf<(T) -> Unit>()

    init {
        launch(MainQueueDispatcher) {
            flow.collect {
                value = ObservableResult.Result(it)
            }
        }
    }

    private var value: ObservableResult<T> by Delegates.observable(ObservableResult.Nothing()) { _, _, new ->
        val result = new as? ObservableResult.Result<T> ?: return@observable
        observers.forEach { it.invoke(result.value) }
    }

    override fun observe(onNext: (T) -> Unit): Disposable {
        observers.add(onNext)
        val lastResult = value
        if (lastResult is ObservableResult.Result<T>) {
            onNext.invoke(lastResult.value)
        }
        return Disposable { observers.remove(onNext) }
    }
}

actual fun <T> Flow<T>.toObservable(coroutineScope: CoroutineScope): Observable<T> = FlowObservable(this, coroutineScope)

