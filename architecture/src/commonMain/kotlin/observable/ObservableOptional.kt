/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.architecture.observable.ObservableOptional.Nothing
import com.splendo.kaluga.architecture.observable.ObservableOptional.Value
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Casts the value of an [ObservableOptional] to its subtype [R] or returns [defaultValue] if no such casting is possible.
 * @param defaultValue The default [R] to be returned if [ObservableOptional.valueOrNull] is not [R] or `null`.
 * @return The current value [R] if it exists or [defaultValue]
 */
inline fun <reified R : T, T> ObservableOptional<T>.resultValueOrDefault(defaultValue: R): R = try {
    valueOrNull?.let { it as? R } ?: defaultValue
} catch (e: ClassCastException) {
    defaultValue
}

/**
 * Casts an [ObservableOptional] to an [ObservableOptional] its subtype [R] or returns [defaultValue] if no such casting is possible.
 * @param defaultValue The default [Value] of [R] to be returned if [ObservableOptional] is not a [Value] of [R] or `null`.
 * @return An [ObservableOptional] containing the current [Value] of [R] if it exists or [defaultValue]
 */
fun <R : T, T> ObservableOptional<T>.asResult(defaultValue: Value<R>?): ObservableOptional<R> = try {
    if (this is Value<*> && (this.value != null || defaultValue == null)) {
        @Suppress("UNCHECKED_CAST")
        Value(this.value as R)
    } else {
        defaultValue ?: Nothing()
    }
} catch (e: ClassCastException) {
    defaultValue ?: Nothing()
}

/**
 * Result type for a [BaseObservable]. Used to allow for the distinction between `null` and optional values.
 * @param T the type this [ObservableOptional] represents. Can be a nullable.
 */
sealed class ObservableOptional<T> : ReadOnlyProperty<Any?, T?> {

    /**
     * The value of [T] or `null` if this result is [Nothing].
     */
    abstract val valueOrNull: T?

    /**
     * The [BaseObservable] has a result, this result could be `null`.
     * @param T the type of the [Value]
     * @property value the value of this result. Can be `null`.
     */
    data class Value<T>(val value: T) : ObservableOptional<T>() {

        override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
        override fun toString(): String = "Value[$value]"
        override val valueOrNull: T? = value
    }

    /**
     * The Observable does not have a result.
     * @param T the type of [Value] that corresponds to this empty result.
     */
    class Nothing<T> : ObservableOptional<T>() {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return null
        }

        override val valueOrNull: T? = null
        override fun toString(): String = "Nothing"

        override fun equals(other: Any?): Boolean {
            return other is Nothing<*>
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }
    }
}
