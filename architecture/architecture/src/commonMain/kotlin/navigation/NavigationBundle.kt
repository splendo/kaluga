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
import kotlinx.serialization.json.Json

/**
 * Value of a [NavigationBundleSpecRow] where [T] describes the type of the value.
 * @param T the type of the value.
 */
sealed class NavigationBundleValue<T> {

    /**
     * The [T] value of this [NavigationBundleValue]
     */
    abstract val value: T

    /**
     * A [NavigationBundleValue] containing a non-nullable [T]
     * @param T the type of the value.
     */
    sealed class NonNullableNavigationBundleValue<T : Any> : NavigationBundleValue<T>()

    /**
     * A [NonNullableNavigationBundleValue] containing a List of [T]
     * @param T the type of the values in the list.
     */
    sealed class ListNavigationBundleValue<T : Any> : NonNullableNavigationBundleValue<List<T>>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [Unit]
     */
    data object UnitValue : NonNullableNavigationBundleValue<Unit>() {
        override val value: Unit = Unit
    }

    /**
     * A [NonNullableNavigationBundleValue] containing a [Boolean]
     * @property value The [Boolean] value of this [NavigationBundleValue]
     */
    data class BooleanValue internal constructor(override val value: Boolean) : NonNullableNavigationBundleValue<Boolean>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [BooleanArray]
     * @property value The [BooleanArray] value of this [NavigationBundleValue]
     */
    data class BooleanArrayValue internal constructor(override val value: BooleanArray) : NonNullableNavigationBundleValue<BooleanArray>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as BooleanArrayValue

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [NonNullableNavigationBundleValue] containing a [Byte]
     * @property value The [Byte] value of this [NavigationBundleValue]
     */
    data class ByteValue internal constructor(override val value: Byte) : NonNullableNavigationBundleValue<Byte>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [ByteArray]
     * @property value The [ByteArray] value of this [NavigationBundleValue]
     */
    data class ByteArrayValue internal constructor(override val value: ByteArray) : NonNullableNavigationBundleValue<ByteArray>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as ByteArrayValue

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [NonNullableNavigationBundleValue] containing a [NavigationBundle]
     * @property value The [NavigationBundle] value of this [NavigationBundleValue]
     */
    data class BundleValue<R : NavigationBundleSpecRow<*>> internal constructor(override val value: NavigationBundle<R>) : NonNullableNavigationBundleValue<NavigationBundle<R>>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [Char]
     * @property value The [Char] value of this [NavigationBundleValue]
     */
    data class CharValue internal constructor(override val value: Char) : NonNullableNavigationBundleValue<Char>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [CharArray]
     * @property value The [CharArray] value of this [NavigationBundleValue]
     */
    data class CharArrayValue internal constructor(override val value: CharArray) : NonNullableNavigationBundleValue<CharArray>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as CharArrayValue

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [NonNullableNavigationBundleValue] containing a [CharSequence]
     * @property value The [CharSequence] value of this [NavigationBundleValue]
     */
    data class CharSequenceValue internal constructor(override val value: CharSequence) : NonNullableNavigationBundleValue<CharSequence>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [Double]
     * @property value The [Double] value of this [NavigationBundleValue]
     */
    data class DoubleValue internal constructor(override val value: Double) : NonNullableNavigationBundleValue<Double>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [DoubleArray]
     * @property value The [DoubleArray] value of this [NavigationBundleValue]
     */
    data class DoubleArrayValue internal constructor(override val value: DoubleArray) : NonNullableNavigationBundleValue<DoubleArray>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as DoubleArrayValue

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [NonNullableNavigationBundleValue] containing a [Float]
     * @property value The [Float] value of this [NavigationBundleValue]
     */
    data class FloatValue internal constructor(override val value: Float) : NonNullableNavigationBundleValue<Float>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [FloatArray]
     * @property value The [FloatArray] value of this [NavigationBundleValue]
     */
    data class FloatArrayValue internal constructor(override val value: FloatArray) : NonNullableNavigationBundleValue<FloatArray>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as FloatArrayValue

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [NonNullableNavigationBundleValue] containing an [Int]
     * @property value The [Int] value of this [NavigationBundleValue]
     */
    data class IntegerValue internal constructor(override val value: Int) : NonNullableNavigationBundleValue<Int>()

    /**
     * A [NonNullableNavigationBundleValue] containing an [IntArray]
     * @property value The [IntArray] value of this [NavigationBundleValue]
     */
    data class IntegerArrayValue internal constructor(override val value: IntArray) : NonNullableNavigationBundleValue<IntArray>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as IntegerArrayValue

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [NonNullableNavigationBundleValue] containing a [Long]
     * @property value The [Long] value of this [NavigationBundleValue]
     */
    data class LongValue internal constructor(override val value: Long) : NonNullableNavigationBundleValue<Long>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [LongArray]
     * @property value The [LongArray] value of this [NavigationBundleValue]
     */
    data class LongArrayValue internal constructor(override val value: LongArray) : NonNullableNavigationBundleValue<LongArray>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as LongArrayValue

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [NavigationBundleValue] containing a value of a serializable class [T]
     * @param T the serializable type of the value
     * @property serializer The [KSerializer] to (de)serialize [value]
     * @property value The [T] value of this [NavigationBundleValue]
     */
    data class SerializedValue<T> internal constructor(val serializer: KSerializer<T>, override val value: T) : NavigationBundleValue<T>() {

        companion object {
            private val json = Json
        }

        val valueString: String = json.encodeToString(serializer, value)
    }

    /**
     * A [NonNullableNavigationBundleValue] containing a [Short]
     * @property value The [Short] value of this [NavigationBundleValue]
     */
    data class ShortValue internal constructor(override val value: Short) : NonNullableNavigationBundleValue<Short>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [ShortArray]
     * @property value The [ShortArray] value of this [NavigationBundleValue]
     */
    data class ShortArrayValue internal constructor(override val value: ShortArray) : NonNullableNavigationBundleValue<ShortArray>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as ShortArrayValue

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [NonNullableNavigationBundleValue] containing a [String]
     * @property value The [String] value of this [NavigationBundleValue]
     */
    data class StringValue internal constructor(override val value: String) : NonNullableNavigationBundleValue<String>()

    /**
     * A [ListNavigationBundleValue] containing a List of [String]
     * @property value The List of [String] value of this [NavigationBundleValue]
     */
    data class StringArrayValue internal constructor(override val value: List<String>) : ListNavigationBundleValue<String>()

    /**
     * A [NonNullableNavigationBundleValue] containing a [KalugaDate]
     * @property value The [KalugaDate] value of this [NavigationBundleValue]
     */
    data class DateValue internal constructor(override val value: KalugaDate) : NonNullableNavigationBundleValue<KalugaDate>()

    /**
     * A [ListNavigationBundleValue] containing a List of [KalugaDate]
     * @property value The List of [KalugaDate] value of this [NavigationBundleValue]
     */
    data class DateArrayValue internal constructor(override val value: List<KalugaDate>) : ListNavigationBundleValue<KalugaDate>()

    /**
     * A [NavigationBundleValue] containing a nullable [T]
     * @param T the non-nullable type of the value
     * @property optionalValue The [NavigationBundleValue] associated with a non-nullable [T], or `null` if the value is to be `null`.
     */
    data class OptionalValue<T : Any> internal constructor(val optionalValue: NonNullableNavigationBundleValue<T>?) : NavigationBundleValue<T?>() {
        override val value: T? = optionalValue?.value
    }
}

/**
 * [Exception] to be thrown when [NavigationBundle.get] is called with a [NavigationBundleSpecRow] that does not match the [NavigationBundleSpec] of the bundle.
 */
class NavigationBundleGetError : Exception()

/**
 * A container class that bundles a set of data for sharing between classes. Only takes values in a [NavigationBundleSpec] [R]
 * @param R the [NavigationBundleSpecRow] associated with this bundle.
 * @property spec The [NavigationBundleSpec] describing the bundle.
 * @property values A map of the [NavigationBundleValue] for each [NavigationBundleSpecRow] in [spec].
 */
class NavigationBundle<R : NavigationBundleSpecRow<*>> internal constructor(val spec: NavigationBundleSpec<R>, val values: Map<R, NavigationBundleValue<*>>) {

    /**
     * Tries to get data of a given [NavigationBundleSpecRow] from this bundle
     * @param row The [NavigationBundleSpecRow] for which to get the value from the bundle
     * @return The value for the [row] according to its type
     * @throws [NavigationBundleGetError] if the [row] does not match the [spec]
     */
    @Suppress("UNCHECKED_CAST")
    fun <V, S : NavigationBundleSpecRow<V>> get(row: S): V {
        row as? R ?: throw NavigationBundleGetError()
        val value = values[row] as? NavigationBundleValue<V> ?: throw NavigationBundleGetError()
        return value.value
    }

    /**
     * Convenience getter for a [NavigationBundle] described using a [SingleValueNavigationSpec]
     * @param type The [NavigationBundleSpecType] associated with the [SingleValueNavigationSpec]
     * @return The value stored in this bundle according to its type
     * @throws [NavigationBundleGetError] if [spec] is note [SingleValueNavigationSpec] associated with [type]
     */
    fun <V> get(type: NavigationBundleSpecType<V>) = get(SingleValueNavigationSpec.Row(type))
}
