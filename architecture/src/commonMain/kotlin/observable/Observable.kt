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
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Result type for an [Observable]. Used to allow for the distinction between `null` and optional values
 */
sealed class ObservableOptional<T> : ReadOnlyProperty<Any?, T?> {

    /**
     * The [Observable] has a result
     */
    data class Value<T>(val value: T) : ObservableOptional<T>() {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return value
        }
    }

    /**
     * The Observable does not have a result
     */
    class Nothing<T> : ObservableOptional<T>() {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return null
        }
    }
}

/**
 * Property that can be observed
 */
expect abstract class Observable<T> : ReadOnlyProperty<Any?, ObservableOptional<T>>

/**
 * [Observable] that can change its data
 */
expect abstract class Subject<T> : Observable<T>, ReadWriteProperty<Any?, ObservableOptional<T>>

expect fun <T> ReadOnlyProperty<Any?, T>.toObservable(): Observable<T>

expect fun <T> ObservableProperty<T>.toSubject(coroutineScope: CoroutineScope): Subject<T>

expect fun <T> Flow<T>.toObservable(coroutineScope: CoroutineScope): Observable<T>

expect fun <T> HotFlowable<T>.toObservable(coroutineScope: CoroutineScope): Observable<T>

expect fun <T> HotFlowable<T>.toSubject(coroutineScope: CoroutineScope): Subject<T>

/**
 * Converts a value to an [Observable]
 * @param initialValue the value of the [Observable]
 */
expect fun <T> observableOf(initialValue: T): Observable<T>

/**
 * Converts a value to a [Subject]
 * @param initialValue the starting value of the [Subject]
 */
expect fun <T> subjectOf(initialValue: T, coroutineScope: CoroutineScope): Subject<T>
