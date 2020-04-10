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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

interface NavigationBundleSpecRow<T> {
    val key: String
    val associatedType: NavigationBundleSpecType<T>
}

sealed class NavigationBundleSpecType<T> {

    abstract fun convertValue(value: T): NavigationBundleValue<T>

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
    class BundleType<R: NavigationBundleSpecRow<*>> internal constructor(val spec: NavigationBundleSpec<R>) : NavigationBundleSpecType<NavigationBundle<R>>() {
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
    class SerializedType<T>(private val serializer: KSerializer<T>): NavigationBundleSpecType<T>() {
        companion object {
            private val json = Json(JsonConfiguration.Stable)
        }

        override fun convertValue(value: T): NavigationBundleValue<T> {
            return NavigationBundleValue.SerializedValue(serializer, value)
        }

        fun generateValue(stringValue: String): NavigationBundleValue<T> {
            return NavigationBundleValue.SerializedValue(serializer, json.parse(serializer, stringValue))
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

    class OptionalType<T>(val type: NavigationBundleSpecType<T>) : NavigationBundleSpecType<T?>() {
        override fun convertValue(value: T?): NavigationBundleValue<T?> {
            return value?.let {
                NavigationBundleValue.OptionalValue(type.convertValue(it))
            } ?: NavigationBundleValue.OptionalValue<T>(null)
        }
    }

}

open class NavigationBundleSpec<R : NavigationBundleSpecRow<*>>(internal val rows: Set<R>)

fun  <R : NavigationBundleSpecRow<*>> NavigationBundleSpec<R>.toBundle(mapper: (R) -> NavigationBundleValue<*>): NavigationBundle<R> {
    return NavigationBundle(this, rows.associateWith { mapper.invoke(it) })
}