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

package com.splendo.kaluga.architecture.navigation

import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * A row within a [NavigationBundleSpec] that is associated with a given [NavigationBundleSpecType]
 * @param T the type of the [NavigationBundleSpecType]
 * @property associatedType The [NavigationBundleSpecType] associated with this row
 */
open class NavigationBundleSpecRow<T>(val associatedType: NavigationBundleSpecType<T>) {
    /**
     * Key for this row. Used for converting rows to data types.
     * Defaults to the name of the row, but can be overwritten if required
     */
    open val key: String? get() {
        return this::class.simpleName
    }

    /**
     * Converts a given value to the [NavigationBundleValue] associated with this row
     * @param value The value to convert
     * @return The [NavigationBundleValue] associated with the value
     */
    fun convertValue(value: T): NavigationBundleValue<T> = associatedType.convertValue(value)
}

/**
 * Types of data a [NavigationBundleSpecRow] can be associated with.
 * @param T the type of value this [NavigationBundleSpecType] is representing.
 */
sealed class NavigationBundleSpecType<T> {

    /**
     * Converts a given value [T] to the [NavigationBundleValue] associated with this type
     * @param value The value [T] to convert
     * @return The [NavigationBundleValue] associated with the value
     */
    abstract fun convertValue(value: T): NavigationBundleValue<T>

    /**
     * A [NavigationBundleSpecType] that takes a non-nullable [T]
     * @param T the type of value this [NavigationBundleSpecType] is representing.
     */
    sealed class NonNullableNavigationBundleSpecType<T : Any> : NavigationBundleSpecType<T>() {
        abstract override fun convertValue(value: T): NavigationBundleValue.NonNullableNavigationBundleValue<T>
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [Unit]
     */
    data object UnitType : NonNullableNavigationBundleSpecType<Unit>() {
        override fun convertValue(value: Unit): NavigationBundleValue.UnitValue = NavigationBundleValue.UnitValue
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [Boolean]
     */
    data object BooleanType : NonNullableNavigationBundleSpecType<Boolean>() {

        override fun convertValue(value: Boolean): NavigationBundleValue.BooleanValue = NavigationBundleValue.BooleanValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [BooleanArray]
     */
    data object BooleanArrayType : NonNullableNavigationBundleSpecType<BooleanArray>() {
        override fun convertValue(value: BooleanArray): NavigationBundleValue.BooleanArrayValue = NavigationBundleValue.BooleanArrayValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [NavigationBundle] with [NavigationBundleSpecRow] [R].
     * @param R the type of [NavigationBundleSpecRow] to represent
     * @property spec The [NavigationBundleSpec] of the [NavigationBundle] to represents.
     */
    class BundleType<R : NavigationBundleSpecRow<*>> internal constructor(val spec: NavigationBundleSpec<R>) : NonNullableNavigationBundleSpecType<NavigationBundle<R>>() {
        override fun convertValue(value: NavigationBundle<R>): NavigationBundleValue.BundleValue<R> = NavigationBundleValue.BundleValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [Byte]
     */
    data object ByteType : NonNullableNavigationBundleSpecType<Byte>() {
        override fun convertValue(value: Byte): NavigationBundleValue.ByteValue = NavigationBundleValue.ByteValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [ByteArray]
     */
    data object ByteArrayType : NonNullableNavigationBundleSpecType<ByteArray>() {
        override fun convertValue(value: ByteArray): NavigationBundleValue.ByteArrayValue = NavigationBundleValue.ByteArrayValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [Char]
     */
    data object CharType : NonNullableNavigationBundleSpecType<Char>() {
        override fun convertValue(value: Char): NavigationBundleValue.CharValue = NavigationBundleValue.CharValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [CharArray]
     */
    data object CharArrayType : NonNullableNavigationBundleSpecType<CharArray>() {
        override fun convertValue(value: CharArray): NavigationBundleValue.CharArrayValue = NavigationBundleValue.CharArrayValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [CharSequence]
     */
    data object CharSequenceType : NonNullableNavigationBundleSpecType<CharSequence>() {
        override fun convertValue(value: CharSequence): NavigationBundleValue.CharSequenceValue = NavigationBundleValue.CharSequenceValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [Double]
     */
    data object DoubleType : NonNullableNavigationBundleSpecType<Double>() {
        override fun convertValue(value: Double): NavigationBundleValue.DoubleValue = NavigationBundleValue.DoubleValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [DoubleArray]
     */
    data object DoubleArrayType : NonNullableNavigationBundleSpecType<DoubleArray>() {
        override fun convertValue(value: DoubleArray): NavigationBundleValue.DoubleArrayValue = NavigationBundleValue.DoubleArrayValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [Float]
     */
    data object FloatType : NonNullableNavigationBundleSpecType<Float>() {
        override fun convertValue(value: Float): NavigationBundleValue.FloatValue = NavigationBundleValue.FloatValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [FloatArray]
     */
    data object FloatArrayType : NonNullableNavigationBundleSpecType<FloatArray>() {
        override fun convertValue(value: FloatArray): NavigationBundleValue.FloatArrayValue = NavigationBundleValue.FloatArrayValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents an [Int]
     */
    data object IntegerType : NonNullableNavigationBundleSpecType<Int>() {
        override fun convertValue(value: Int): NavigationBundleValue.IntegerValue = NavigationBundleValue.IntegerValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents an [IntArray]
     */
    data object IntegerArrayType : NonNullableNavigationBundleSpecType<IntArray>() {
        override fun convertValue(value: IntArray): NavigationBundleValue.IntegerArrayValue = NavigationBundleValue.IntegerArrayValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [Long]
     */
    data object LongType : NonNullableNavigationBundleSpecType<Long>() {
        override fun convertValue(value: Long): NavigationBundleValue.LongValue = NavigationBundleValue.LongValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [LongArray]
     */
    data object LongArrayType : NonNullableNavigationBundleSpecType<LongArray>() {
        override fun convertValue(value: LongArray): NavigationBundleValue.LongArrayValue = NavigationBundleValue.LongArrayValue(value)
    }

    /**
     * A [NavigationBundleSpecType] that represents a serializable [T]
     * @param T the serializable type to represent.
     * @param serializer The [KSerializer] to (de)serialize [T]
     */
    data class SerializedType<T>(private val serializer: KSerializer<T>) : NavigationBundleSpecType<T>() {
        companion object {
            private val json = Json
        }

        override fun convertValue(value: T): NavigationBundleValue<T> = NavigationBundleValue.SerializedValue(serializer, value)

        /**
         * Generates a [NavigationBundleValue] from a given string
         * @param stringValue The string to describe [T]. Must match the provided serializer.
         */
        fun generateValue(stringValue: String): NavigationBundleValue<T>? = try {
            NavigationBundleValue.SerializedValue(serializer, json.decodeFromString(serializer, stringValue))
        } catch (e: SerializationException) {
            null
        }
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [Short]
     */
    data object ShortType : NonNullableNavigationBundleSpecType<Short>() {
        override fun convertValue(value: Short): NavigationBundleValue.ShortValue = NavigationBundleValue.ShortValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [ShortArray]
     */
    data object ShortArrayType : NonNullableNavigationBundleSpecType<ShortArray>() {
        override fun convertValue(value: ShortArray): NavigationBundleValue.ShortArrayValue = NavigationBundleValue.ShortArrayValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [String]
     */
    data object StringType : NonNullableNavigationBundleSpecType<String>() {
        override fun convertValue(value: String): NavigationBundleValue.StringValue = NavigationBundleValue.StringValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a List of [String]
     */
    data object StringArrayType : NonNullableNavigationBundleSpecType<List<String>>() {
        override fun convertValue(value: List<String>): NavigationBundleValue.StringArrayValue = NavigationBundleValue.StringArrayValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a [KalugaDate]
     */
    data object DateType : NonNullableNavigationBundleSpecType<KalugaDate>() {
        override fun convertValue(value: KalugaDate): NavigationBundleValue.DateValue = NavigationBundleValue.DateValue(value)
    }

    /**
     * A [NonNullableNavigationBundleSpecType] that represents a List of [KalugaDate]
     */
    data object DateArrayType : NonNullableNavigationBundleSpecType<List<KalugaDate>>() {
        override fun convertValue(value: List<KalugaDate>): NavigationBundleValue.DateArrayValue = NavigationBundleValue.DateArrayValue(value)
    }

    /**
     * A [NavigationBundleSpecType] that represents a nullable [T].
     * @param T the nullable type to represent.
     * @property type The [NonNullableNavigationBundleSpecType] describing the non-nullable version of [T].
     */
    data class OptionalType<T : Any>(val type: NonNullableNavigationBundleSpecType<T>) : NavigationBundleSpecType<T?>() {
        override fun convertValue(value: T?): NavigationBundleValue<T?> = value?.let {
            NavigationBundleValue.OptionalValue(type.convertValue(it))
        } ?: NavigationBundleValue.OptionalValue(null)
    }
}

/**
 * A set of [NavigationBundleSpecRow]s that can be used to form a [NavigationBundle] using [NavigationBundleSpec.toBundle]
 * @param R The [NavigationBundleSpecRow] associated with this [NavigationBundleSpec].
 * @property rows The set of [rows][R] in this [NavigationBundleSpec].
 */
open class NavigationBundleSpec<R : NavigationBundleSpecRow<*>>(val rows: Set<R>)

/**
 * A [NavigationBundleSpec] that only provides a single [NavigationBundleSpecRow]
 * @param T The type of value stored in this spec.
 * @param type The [NavigationBundleSpecType] used for the [NavigationBundleSpecRow]
 */
class SingleValueNavigationSpec<T>(type: NavigationBundleSpecType<T>) : NavigationBundleSpec<SingleValueNavigationSpec.Row<T>>(setOf(Row(type))) {
    /**
     * A [NavigationBundleSpecRow] that contains only a single value.
     * @param T the type to be stored in this row.
     * @param type The [NavigationBundleSpecType] used for the [SingleValueNavigationSpec.Row]
     */
    data class Row<T>(val type: NavigationBundleSpecType<T>) : NavigationBundleSpecRow<T>(type)
}

/**
 * Creates a [NavigationBundle] from the [NavigationBundleSpec]
 * @param R The type of [NavigationBundleSpecRow] associated with the [NavigationBundleSpec].
 * @param mapper Function mapping the [NavigationBundleSpecRow] in this spec to their respective [NavigationBundleValue]
 * @return A [NavigationBundle] matching this [NavigationBundleSpec]
 */
fun <R : NavigationBundleSpecRow<*>> NavigationBundleSpec<R>.toBundle(mapper: (R) -> NavigationBundleValue<*>): NavigationBundle<R> =
    NavigationBundle(this, rows.associateWith { mapper.invoke(it) })
