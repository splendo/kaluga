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

import android.os.Bundle
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import kotlin.time.Duration.Companion.milliseconds

/**
 * Converts a [NavigationBundle] to a [Bundle]
 * @return The converted [Bundle]
 */
fun <Row : NavigationBundleSpecRow<*>> NavigationBundle<Row>.toBundle(): Bundle {
    val bundle = Bundle()

    values.entries.forEach { (key, value) ->
        mapValue(key.key ?: key.javaClass.simpleName, value, bundle)
    }

    return bundle
}

internal fun mapValue(key: String, value: NavigationBundleValue<*>, bundle: Bundle) {
    when (value) {
        is NavigationBundleValue.UnitValue -> {}
        is NavigationBundleValue.BooleanValue -> bundle.putBoolean(key, value.value)
        is NavigationBundleValue.BooleanArrayValue -> bundle.putBooleanArray(key, value.value)
        is NavigationBundleValue.ByteValue -> bundle.putByte(key, value.value)
        is NavigationBundleValue.ByteArrayValue -> bundle.putByteArray(key, value.value)
        is NavigationBundleValue.BundleValue<*> -> bundle.putBundle(key, value.value.toBundle())
        is NavigationBundleValue.CharValue -> bundle.putChar(key, value.value)
        is NavigationBundleValue.CharArrayValue -> bundle.putCharArray(key, value.value)
        is NavigationBundleValue.CharSequenceValue -> bundle.putCharSequence(key, value.value)
        is NavigationBundleValue.DoubleValue -> bundle.putDouble(key, value.value)
        is NavigationBundleValue.DoubleArrayValue -> bundle.putDoubleArray(key, value.value)
        is NavigationBundleValue.FloatValue -> bundle.putFloat(key, value.value)
        is NavigationBundleValue.FloatArrayValue -> bundle.putFloatArray(key, value.value)
        is NavigationBundleValue.IntegerValue -> bundle.putInt(key, value.value)
        is NavigationBundleValue.IntegerArrayValue -> bundle.putIntArray(key, value.value)
        is NavigationBundleValue.LongValue -> bundle.putLong(key, value.value)
        is NavigationBundleValue.LongArrayValue -> bundle.putLongArray(key, value.value)
        is NavigationBundleValue.SerializedValue<*> -> bundle.putString(key, value.valueString)
        is NavigationBundleValue.ShortValue -> bundle.putShort(key, value.value)
        is NavigationBundleValue.ShortArrayValue -> bundle.putShortArray(key, value.value)
        is NavigationBundleValue.StringValue -> bundle.putString(key, value.value)
        is NavigationBundleValue.StringArrayValue -> bundle.putStringArrayList(key, ArrayList(value.value))
        is NavigationBundleValue.OptionalValue<*> -> value.optionalValue?.let { mapValue(key, it, bundle) }
        is NavigationBundleValue.DateValue -> bundle.putLong(key, value.value.durationSinceEpoch.inWholeMilliseconds)
        is NavigationBundleValue.DateArrayValue ->
            bundle.putLongArray(key, LongArray(value.value.size) { value.value[it].durationSinceEpoch.inWholeMilliseconds })
    }
}

/**
 * Converts a [Bundle] to a [NavigationBundle] matching a given [NavigationBundleSpec]
 * @param spec The [NavigationBundleSpec] used to create the [NavigationBundle]
 * @throws [BundleConversionError] if the [Bundle] does not contain the correct keys or values associated with the [NavigationBundleSpec]
 */
fun <Row : NavigationBundleSpecRow<*>> Bundle.toNavigationBundle(spec: NavigationBundleSpec<Row>): NavigationBundle<Row> {
    return NavigationBundle(
        spec,
        spec.rows.associate { row ->
            mapValue(row.key ?: row.javaClass.simpleName, row.associatedType)?.let {
                Pair(row, it)
            } ?: throw BundleConversionError()
        },
    )
}

/**
 * Error indicating a [Bundle] could not be converted to a [NavigationBundle]
 */
class BundleConversionError : Exception()

internal fun Bundle.mapValue(key: String, specType: NavigationBundleSpecType<*>): NavigationBundleValue<*>? {
    return when (specType) {
        is NavigationBundleSpecType.UnitType -> specType.convertValue(Unit)
        is NavigationBundleSpecType.BooleanType -> specType.convertValue(getBoolean(key))
        is NavigationBundleSpecType.BooleanArrayType -> getBooleanArray(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.ByteType -> specType.convertValue(getByte(key))
        is NavigationBundleSpecType.ByteArrayType -> getByteArray(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.BundleType<*> -> {
            getBundle(key)?.let {
                NavigationBundleValue.BundleValue(it.toNavigationBundle(specType.spec))
            }
        }
        is NavigationBundleSpecType.CharType -> specType.convertValue(getChar(key))
        is NavigationBundleSpecType.CharArrayType -> getCharArray(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.CharSequenceType -> getCharSequence(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.DoubleType -> specType.convertValue(getDouble(key))
        is NavigationBundleSpecType.DoubleArrayType -> getDoubleArray(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.FloatType -> specType.convertValue(getFloat(key))
        is NavigationBundleSpecType.FloatArrayType -> getFloatArray(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.IntegerType -> specType.convertValue(getInt(key))
        is NavigationBundleSpecType.IntegerArrayType -> getIntArray(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.LongType -> specType.convertValue(getLong(key))
        is NavigationBundleSpecType.LongArrayType -> getLongArray(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.SerializedType<*> -> getString(key)?.let { specType.generateValue(it) }
        is NavigationBundleSpecType.ShortType -> specType.convertValue(getShort(key))
        is NavigationBundleSpecType.ShortArrayType -> getShortArray(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.StringType -> getString(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.StringArrayType -> getStringArrayList(key)?.let { specType.convertValue(it) }
        is NavigationBundleSpecType.OptionalType<*> -> {
            val optionalType = specType.type
            if (containsKey(key)) {
                mapValue(key, optionalType)
            } else {
                specType.convertValue(null)
            }
        }
        is NavigationBundleSpecType.DateType -> getLong(key).let { value ->
            specType.convertValue(DefaultKalugaDate.epoch(value.milliseconds))
        }
        is NavigationBundleSpecType.DateArrayType -> getLongArray(key)?.let { array ->
            specType.convertValue(array.map { DefaultKalugaDate.epoch(it.milliseconds) })
        }
    }
}
