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

import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class ObservableResult<T> : ReadOnlyProperty<Any?, T?> {
    data class Result<T>(val value: T) : ObservableResult<T>() {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return value
        }
    }
    class Nothing<T> : ObservableResult<T>() {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return null
        }
    }
}

expect abstract class Observable<T> : ReadOnlyProperty<Any?, ObservableResult<T>>

expect abstract class Subject<T> : Observable<T>, ReadWriteProperty<Any?, ObservableResult<T>>

expect fun <T> ReadOnlyProperty<Any?, T>.toObservable(): Observable<T>

expect fun <T> ObservableProperty<T>.toSubject(coroutineScope: CoroutineScope): Subject<T>

expect fun <T> Flow<T>.toObservable(coroutineScope: CoroutineScope): Observable<T>

expect fun <T> BaseFlowable<T>.toObservable(coroutineScope: CoroutineScope): Observable<T>

expect fun <T> BaseFlowable<T>.toSubject(coroutineScope: CoroutineScope): Subject<T>

expect fun <T> observableOf(initialValue: T) : Observable<T>

expect fun <T> subjectOf(initialValue: T, coroutineScope: CoroutineScope) : Subject<T>
