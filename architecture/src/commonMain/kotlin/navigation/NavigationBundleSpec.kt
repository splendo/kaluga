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

package com.splendo.kaluga.architecture.navigation

import com.splendo.kaluga.base.utils.Date
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * A row within a [NavigationBundleSpec] that is associated with a given [NavigationBundleSpecType]
 * @param associatedType The [NavigationBundleSpecType] associated with this row
 */
open class NavigationBundleSpecRow<T>(internal val associatedType: NavigationBundleSpecType<T>) {
    /**
     * Key for this row. Used for converting rows to data types.
     * Defaults to the name of the row, but can be overwritten if required
     */
    open val key: String? get() { return this::class.simpleName }

    /**
     * Converts a given value to the [NavigationBundleValue] associated with this row
     * @param value The value to convert
     * @return The [NavigationBundleValue] associated with the value
     */
    fun convertValue(value: T): NavigationBundleValue<T> = associatedType.convertValue(value)
}

/**
 * Types of data a [NavigationBundleSpecRow] can be associated with
 */
sealed class NavigationBundleSpecType<T> {

    /**
     * Converts a given value to the [NavigationBundleValue] associated with this type
     * @param value The value to convert
     * @return The [NavigationBundleValue] associated with the value
     */
    abstract fun convertValue(value: T): NavigationBundleValue<T>

    object UnitType : NavigationBundleSpecType<Unit>() {
        override fun convertValue(value: Unit): NavigationBundleValue<Unit> {
            return NavigationBundleValue.UnitValue
        }
    }

    object BooleanType : NavigationBundleSpecType<Boolean>() {

        override fun convertValue(value: Boolean): NavigationBundleValue<Boolean> {
            return NavigationBundleValue.BooleanValue(value)
        }
    }

    object BooleanArrayType : NavigationBundleSpecType<BooleanArray>() {
        override fun convertValue(value: BooleanArray): NavigationBundleValue<BooleanArray> {
            return NavigationBundleValue.BooleanArrayValue(value)
        }
    }

    object ByteType : NavigationBundleSpecType<Byte>() {
        override fun convertValue(value: Byte): NavigationBundleValue<Byte> {
            return NavigationBundleValue.ByteValue(value)
        }
    }
    class BundleType<R : NavigationBundleSpecRow<*>> internal constructor(val spec: NavigationBundleSpec<R>) : NavigationBundleSpecType<NavigationBundle<R>>() {
        override fun convertValue(value: NavigationBundle<R>): NavigationBundleValue<NavigationBundle<R>> {
            return NavigationBundleValue.BundleValue(value)
        }
    }

    object ByteArrayType : NavigationBundleSpecType<ByteArray>() {
        override fun convertValue(value: ByteArray): NavigationBundleValue<ByteArray> {
            return NavigationBundleValue.ByteArrayValue(value)
        }
    }

    object CharType : NavigationBundleSpecType<Char>() {
        override fun convertValue(value: Char): NavigationBundleValue<Char> {
            return NavigationBundleValue.CharValue(value)
        }
    }

    object CharArrayType : NavigationBundleSpecType<CharArray>() {
        override fun convertValue(value: CharArray): NavigationBundleValue<CharArray> {
            return NavigationBundleValue.CharArrayValue(value)
        }
    }

    object CharSequenceType : NavigationBundleSpecType<CharSequence>() {
        override fun convertValue(value: CharSequence): NavigationBundleValue<CharSequence> {
            return NavigationBundleValue.CharSequenceValue(value)
        }
    }

    object DoubleType : NavigationBundleSpecType<Double>() {
        override fun convertValue(value: Double): NavigationBundleValue<Double> {
            return NavigationBundleValue.DoubleValue(value)
        }
    }

    object DoubleArrayType : NavigationBundleSpecType<DoubleArray>() {
        override fun convertValue(value: DoubleArray): NavigationBundleValue<DoubleArray> {
            return NavigationBundleValue.DoubleArrayValue(value)
        }
    }

    object FloatType : NavigationBundleSpecType<Float>() {
        override fun convertValue(value: Float): NavigationBundleValue<Float> {
            return NavigationBundleValue.FloatValue(value)
        }
    }

    object FloatArrayType : NavigationBundleSpecType<FloatArray>() {
        override fun convertValue(value: FloatArray): NavigationBundleValue<FloatArray> {
            return NavigationBundleValue.FloatArrayValue(value)
        }
    }

    object IntegerType : NavigationBundleSpecType<Int>() {
        override fun convertValue(value: Int): NavigationBundleValue<Int> {
            return NavigationBundleValue.IntegerValue(value)
        }
    }

    object IntegerArrayType : NavigationBundleSpecType<IntArray>() {
        override fun convertValue(value: IntArray): NavigationBundleValue<IntArray> {
            return NavigationBundleValue.IntegerArrayValue(value)
        }
    }

    object LongType : NavigationBundleSpecType<Long>() {
        override fun convertValue(value: Long): NavigationBundleValue<Long> {
            return NavigationBundleValue.LongValue(value)
        }
    }

    object LongArrayType : NavigationBundleSpecType<LongArray>() {
        override fun convertValue(value: LongArray): NavigationBundleValue<LongArray> {
            return NavigationBundleValue.LongArrayValue(value)
        }
    }
    data class SerializedType<T>(private val serializer: KSerializer<T>) : NavigationBundleSpecType<T>() {
        companion object {
            private val json = Json { }
        }

        override fun convertValue(value: T): NavigationBundleValue<T> {
            return NavigationBundleValue.SerializedValue(serializer, value)
        }

        fun generateValue(stringValue: String): NavigationBundleValue<T>? {
            return try {
                NavigationBundleValue.SerializedValue(serializer, json.decodeFromString(serializer, stringValue))
            } catch (e: SerializationException) {
                null
            }
        }
    }

    object ShortType : NavigationBundleSpecType<Short>() {
        override fun convertValue(value: Short): NavigationBundleValue<Short> {
            return NavigationBundleValue.ShortValue(value)
        }
    }

    object ShortArrayType : NavigationBundleSpecType<ShortArray>() {
        override fun convertValue(value: ShortArray): NavigationBundleValue<ShortArray> {
            return NavigationBundleValue.ShortArrayValue(value)
        }
    }

    object StringType : NavigationBundleSpecType<String>() {
        override fun convertValue(value: String): NavigationBundleValue<String> {
            return NavigationBundleValue.StringValue(value)
        }
    }

    object StringArrayType : NavigationBundleSpecType<List<String>>() {
        override fun convertValue(value: List<String>): NavigationBundleValue<List<String>> {
            return NavigationBundleValue.StringArrayValue(value)
        }
    }

    object DateType : NavigationBundleSpecType<Date>() {
        override fun convertValue(value: Date): NavigationBundleValue<Date> {
            return NavigationBundleValue.DateValue(value)
        }
    }

    object DateArrayType : NavigationBundleSpecType<List<Date>>() {
        override fun convertValue(value: List<Date>): NavigationBundleValue<List<Date>> {
            return NavigationBundleValue.DateArrayValue(value)
        }
    }

    data class OptionalType<T>(val type: NavigationBundleSpecType<T>) : NavigationBundleSpecType<T?>() {
        override fun convertValue(value: T?): NavigationBundleValue<T?> {
            return value?.let {
                NavigationBundleValue.OptionalValue(type.convertValue(it))
            } ?: NavigationBundleValue.OptionalValue(null)
        }
    }
}

/**
 * A set of [NavigationBundleSpecRow]s that can be used to form a [NavigationBundle] using [NavigationBundleSpec.toBundle]
 */
open class NavigationBundleSpec<R : NavigationBundleSpecRow<*>>(internal val rows: Set<R>)

/**
 * A [NavigationBundleSpec] that only provides a single [NavigationBundleSpecRow]
 * @param type The [NavigationBundleSpecType] used for the [NavigationBundleSpecRow]
 */
class SingleValueNavigationSpec<T>(type: NavigationBundleSpecType<T>) : NavigationBundleSpec<SingleValueNavigationSpec.Row<T>>(setOf(Row(type))) {
    data class Row<T>(val type: NavigationBundleSpecType<T>) : NavigationBundleSpecRow<T>(type)
}

/**
 * Creates a [NavigationBundle] from the [NavigationBundleSpec]
 * @param mapper Function mapping the [NavigationBundleSpecRow] in this spec to their respective [NavigationBundleValue]
 * @return A [NavigationBundle] matching this [NavigationBundleSpec]
 */
fun <R : NavigationBundleSpecRow<*>> NavigationBundleSpec<R>.toBundle(mapper: (R) -> NavigationBundleValue<*>): NavigationBundle<R> {
    return NavigationBundle(this, rows.associateWith { mapper.invoke(it) })
}
