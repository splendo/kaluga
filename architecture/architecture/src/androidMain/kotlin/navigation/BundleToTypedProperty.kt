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

package com.splendo.kaluga.architecture.navigation

import android.os.Bundle
import com.splendo.kaluga.datetime.KalugaDate
import kotlinx.serialization.KSerializer

/**
 * Converts a [Bundle] to a [R] property associated with a [NavigationBundleSpecType]
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType]
 * @return The [R] value stored in the bundle
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec]
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun <R> Bundle.toTypedProperty(type: NavigationBundleSpecType<R>): R = toNavigationBundle(SingleValueNavigationSpec(type)).get(type)

/**
 * Converts a [Bundle] to a [R] property associated with a [NavigationBundleSpecType]
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType] either directly or wrapped in [NavigationBundleSpecType.OptionalType]
 * @return The [R] value stored in the bundle.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun <R : Any> Bundle.toTypedPropertyOrNull(type: NavigationBundleSpecType<R>): R? = try {
    toTypedProperty(type)
} catch (e: BundleConversionError) {
    try {
        (type as? NavigationBundleSpecType.NonNullableNavigationBundleSpecType)?.let {
            toTypedProperty(NavigationBundleSpecType.OptionalType(it))
        }
    } catch (e: BundleConversionError) {
        null
    }
}

/**
 * Converts a [Bundle] to a [R] property associated with a [NavigationBundleSpecType]
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType].
 * @return The [R] value stored in the bundle.
 */
@JvmName("toNullableTypedPropertyOrNull")
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun <R> Bundle.toTypedPropertyOrNull(type: NavigationBundleSpecType<R>): R? = try {
    toTypedProperty(type)
} catch (e: BundleConversionError) {
    null
}

/**
 * Converts a [Bundle] to a [Boolean].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanType]
 * @return The [Boolean] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asBoolean(): Boolean = toTypedProperty(NavigationBundleSpecType.BooleanType)

/**
 * Converts a [Bundle] to a [Boolean] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Boolean] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asBooleanOrNull(): Boolean? = toTypedPropertyOrNull(NavigationBundleSpecType.BooleanType)

/**
 * Converts a [Bundle] to a [BooleanArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanArrayType]
 * @return The [BooleanArray] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asBooleanArray(): BooleanArray = toTypedProperty(NavigationBundleSpecType.BooleanArrayType)

/**
 * Converts a [Bundle] to a [BooleanArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [BooleanArray] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asBooleanArrayOrNull(): BooleanArray? = toTypedPropertyOrNull(NavigationBundleSpecType.BooleanArrayType)

/**
 * Converts a [Bundle] to a [Byte].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteArrayType]
 * @return The [Byte] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asByte(): Byte = toTypedProperty(NavigationBundleSpecType.ByteType)

/**
 * Converts a [Bundle] to a [Byte] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Byte] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asByteOrNull(): Byte? = toTypedPropertyOrNull(NavigationBundleSpecType.ByteType)

/**
 * Converts a [Bundle] to a [ByteArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteArrayType]
 * @return The [ByteArray] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asByteArray(): ByteArray = toTypedProperty(NavigationBundleSpecType.ByteArrayType)

/**
 * Converts a [Bundle] to a [ByteArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [ByteArray] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asByteArrayOrNull(): ByteArray? = toTypedPropertyOrNull(NavigationBundleSpecType.ByteArrayType)

/**
 * Converts a [Bundle] to a [Char].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharType]
 * @return The [Char] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asChar(): Char = toTypedProperty(NavigationBundleSpecType.CharType)

/**
 * Converts a [Bundle] to a [Char] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Char] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asCharOrNull(): Char? = toTypedPropertyOrNull(NavigationBundleSpecType.CharType)

/**
 * Converts a [Bundle] to a [CharArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharArrayType]
 * @return The [CharArray] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asCharArray(): CharArray = toTypedProperty(NavigationBundleSpecType.CharArrayType)

/**
 * Converts a [Bundle] to a [CharArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [CharArray] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asCharArrayOrNull(): CharArray? = toTypedPropertyOrNull(NavigationBundleSpecType.CharArrayType)

/**
 * Converts a [Bundle] to a [CharSequence].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharSequenceType]
 * @return The [CharSequence] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharSequenceType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asCharSequence(): CharSequence = toTypedProperty(NavigationBundleSpecType.CharSequenceType)

/**
 * Converts a [Bundle] to a [CharSequence] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharSequenceType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [CharSequence] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asCharSequenceOrNull(): CharSequence? = toTypedPropertyOrNull(NavigationBundleSpecType.CharSequenceType)

/**
 * Converts a [Bundle] to a [KalugaDate].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateType]
 * @return The [KalugaDate] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asDate(): KalugaDate = toTypedProperty(NavigationBundleSpecType.DateType)

/**
 * Converts a [Bundle] to a [KalugaDate] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [KalugaDate] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asDateOrNull(): KalugaDate? = toTypedPropertyOrNull(NavigationBundleSpecType.DateType)

/**
 * Converts a [Bundle] to a [List] of [KalugaDate].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateArrayType]
 * @return The [List] of [KalugaDate] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asDateArray(): List<KalugaDate> = toTypedProperty(NavigationBundleSpecType.DateArrayType)

/**
 * Converts a [Bundle] to a [List] of [KalugaDate] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [List] of [KalugaDate] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asDateArrayOrNull(): List<KalugaDate>? = toTypedPropertyOrNull(NavigationBundleSpecType.DateArrayType)

/**
 * Converts a [Bundle] to a [Double].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleType]
 * @return The [Double] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asDouble(): Double = toTypedProperty(NavigationBundleSpecType.DoubleType)

/**
 * Converts a [Bundle] to a [Double] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Double] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asDoubleOrNull(): Double? = toTypedPropertyOrNull(NavigationBundleSpecType.DoubleType)

/**
 * Converts a [Bundle] to a [DoubleArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleArrayType]
 * @return The [DoubleArray] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asDoubleArray(): DoubleArray = toTypedProperty(NavigationBundleSpecType.DoubleArrayType)

/**
 * Converts a [Bundle] to a [DoubleArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [DoubleArray] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asDoubleArrayOrNull(): DoubleArray? = toTypedPropertyOrNull(NavigationBundleSpecType.DoubleArrayType)

/**
 * Converts a [Bundle] to a [Float].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatType]
 * @return The [Float] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asFloat(): Float = toTypedProperty(NavigationBundleSpecType.FloatType)

/**
 * Converts a [Bundle] to a [Float] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Float] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asFloatOrNull(): Float? = toTypedPropertyOrNull(NavigationBundleSpecType.FloatType)

/**
 * Converts a [Bundle] to a [FloatArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatArrayType]
 * @return The [FloatArray] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asFloatArray(): FloatArray = toTypedProperty(NavigationBundleSpecType.FloatArrayType)

/**
 * Converts a [Bundle] to a [FloatArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [FloatArray] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asFloatArrayOrNull(): FloatArray? = toTypedPropertyOrNull(NavigationBundleSpecType.FloatArrayType)

/**
 * Converts a [Bundle] to an [Int].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerType]
 * @return The [Int] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asInt(): Int = toTypedProperty(NavigationBundleSpecType.IntegerType)

/**
 * Converts a [Bundle] to an [Int] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Int] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asIntOrNull(): Int? = toTypedPropertyOrNull(NavigationBundleSpecType.IntegerType)

/**
 * Converts a [Bundle] to an [IntArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerArrayType]
 * @return The [IntArray] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asIntArray(): IntArray = toTypedProperty(NavigationBundleSpecType.IntegerArrayType)

/**
 * Converts a [Bundle] to an [IntArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [IntArray] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asIntArrayOrNull(): IntArray? = toTypedPropertyOrNull(NavigationBundleSpecType.IntegerArrayType)

/**
 * Converts a [Bundle] to a [Long].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongType]
 * @return The [Long] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asLong(): Long = toTypedProperty(NavigationBundleSpecType.LongType)

/**
 * Converts a [Bundle] to a [Long] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Long] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asLongOrNull(): Long? = toTypedPropertyOrNull(NavigationBundleSpecType.LongType)

/**
 * Converts a [Bundle] to a [LongArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongArrayType]
 * @return The [LongArray] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asLongArray(): LongArray = toTypedProperty(NavigationBundleSpecType.LongArrayType)

/**
 * Converts a [Bundle] to a [LongArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [LongArray] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asLongArrayOrNull(): LongArray? = toTypedPropertyOrNull(NavigationBundleSpecType.LongArrayType)

/**
 * Converts a [Bundle] to a [Short].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortType]
 * @return The [Short] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asShort(): Short = toTypedProperty(NavigationBundleSpecType.ShortType)

/**
 * Converts a [Bundle] to a [Short] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Short] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asShortOrNull(): Short? = toTypedPropertyOrNull(NavigationBundleSpecType.ShortType)

/**
 * Converts a [Bundle] to a [ShortArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortArrayType]
 * @return The [ShortArray] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asShortArray(): ShortArray = toTypedProperty(NavigationBundleSpecType.ShortArrayType)

/**
 * Converts a [Bundle] to a [ShortArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [ShortArray] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asShortArrayOrNull(): ShortArray? = toTypedPropertyOrNull(NavigationBundleSpecType.ShortArrayType)

/**
 * Converts a [Bundle] to a [String].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringType]
 * @return The [String] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asString(): String = toTypedProperty(NavigationBundleSpecType.StringType)

/**
 * Converts a [Bundle] to a [String] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [String] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asStringOrNull(): String? = toTypedPropertyOrNull(NavigationBundleSpecType.StringType)

/**
 * Converts a [Bundle] to a [List] of [String].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringArrayType]
 * @return The [List] of [String] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringArrayType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asStringList(): List<String> = toTypedProperty(NavigationBundleSpecType.StringArrayType)

/**
 * Converts a [Bundle] to a [List] of [String] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [List] of [String] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asStringListOrNull(): List<String>? = toTypedPropertyOrNull(NavigationBundleSpecType.StringArrayType)

/**
 * Converts a [Bundle] to [T].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer]
 * @param T the type of serialized property stored in the bundle
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] with [serializer].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun <T> Bundle.asTypeOf(serializer: KSerializer<T>): T = toTypedProperty(NavigationBundleSpecType.SerializedType(serializer))

/**
 * Converts a [Bundle] to [T] if is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @param T the type of serialized property stored in the bundle
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun <T : Any> Bundle.asTypeOfOrNull(serializer: KSerializer<T>): T? = toTypedPropertyOrNull(NavigationBundleSpecType.SerializedType(serializer))

/**
 * Converts a [Bundle] to [T] if is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer].
 * @param T the type of serialized property stored in the bundle
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle or null if no such value was found.
 */
@JvmName("asNullableTypeOfOrNull")
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun <T> Bundle.asTypeOfOrNull(serializer: KSerializer<T>): T? = toTypedPropertyOrNull(NavigationBundleSpecType.SerializedType(serializer))

/**
 * Converts a [Bundle] to a [Unit].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.UnitType]
 * @return The [String] stored in the bundle.
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.UnitType].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asUnit(): Unit = toTypedProperty(NavigationBundleSpecType.UnitType)

/**
 * Converts a [Bundle] to a [Unit] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.UnitType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Unit] stored in the bundle or null if no such value was found.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun Bundle.asUnitNull(): Unit? = toTypedPropertyOrNull(NavigationBundleSpecType.UnitType)
