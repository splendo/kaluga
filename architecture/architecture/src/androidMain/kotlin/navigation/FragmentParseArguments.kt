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
import androidx.fragment.app.Fragment
import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.serialization.KSerializer

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [T] using a convert method.
 * @param convert The method for converting [Bundle] into [T]
 * @return The [T] value extracted from the [Bundle]
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 */
fun <T> Fragment.parseExtra(convert: Bundle.() -> T): T = arguments!!.convert()

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [T] using a convert method.
 * @param convert The method for converting [Bundle] into [T]
 * @return The [T] value extracted from the [Bundle] or `null` if [android.content.Intent.getExtras] is `null`
 */
fun <T> Fragment.parseExtraOrNull(convert: Bundle.() -> T?): T? = arguments?.convert()

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Boolean].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanType]
 * @return The [Boolean] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanType].
 */
fun Fragment.parseBoolean(): Boolean = parseExtra { asBoolean() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Boolean] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Boolean] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseBooleanOrNull(): Boolean? = parseExtra { asBooleanOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [BooleanArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanArrayType]
 * @return The [BooleanArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanArrayType].
 */
fun Fragment.parseBooleanArray(): BooleanArray = parseExtra { asBooleanArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [BooleanArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [BooleanArray] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseBooleanArrayOrNull(): BooleanArray? = parseExtra { asBooleanArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Byte].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteType]
 * @return The [Byte] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteType].
 */
fun Fragment.parseByte(): Byte = parseExtra { asByte() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Byte] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Byte] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseByteOrNull(): Byte? = parseExtra { asByteOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [ByteArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteArrayType]
 * @return The [ByteArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType].
 */
fun Fragment.parseByteArray(): ByteArray = parseExtra { asByteArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [ByteArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [ByteArray] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseByteArrayOrNull(): ByteArray? = parseExtra { asByteArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Char].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharType]
 * @return The [Char] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharType].
 */
fun Fragment.parseChar(): Char = parseExtra { asChar() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Char] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Char] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseCharOrNull(): Char? = parseExtra { asCharOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [CharArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharArrayType]
 * @return The [CharArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharArrayType].
 */
fun Fragment.parseCharArray(): CharArray = parseExtra { asCharArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [CharArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [CharArray] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseCharArrayOrNull(): CharArray? = parseExtra { asCharArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [CharSequence] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharSequenceType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [CharSequence] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseCharSequenceOrNull(): CharSequence? = parseExtra { asCharSequenceOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [KalugaDate].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateType]
 * @return The [KalugaDate] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateType].
 */
fun Fragment.parseDate(): KalugaDate = parseExtra { asDate() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [KalugaDate] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [KalugaDate] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseDateOrNull(): KalugaDate? = parseExtra { asDateOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [List] of [KalugaDate].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateArrayType]
 * @return The [List] of [KalugaDate] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateArrayType].
 */
fun Fragment.parseDateArray(): List<KalugaDate> = parseExtra { asDateArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [List] of [KalugaDate] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [List] of [KalugaDate] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseDateArrayOrNull(): List<KalugaDate>? = parseExtra { asDateArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Double].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleType]
 * @return The [Double] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleType].
 */
fun Fragment.parseDouble(): Double = parseExtra { asDouble() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Double] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Double] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseDoubleOrNull(): Double? = parseExtra { asDoubleOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [DoubleArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleArrayType]
 * @return The [DoubleArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleArrayType].
 */
fun Fragment.parseDoubleArray(): DoubleArray = parseExtra { asDoubleArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [DoubleArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [DoubleArray] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseDoubleArrayOrNull(): DoubleArray? = parseExtra { asDoubleArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Float].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatType]
 * @return The [Float] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatType].
 */
fun Fragment.parseFloat(): Float = parseExtra { asFloat() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Float] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Float] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseFloatOrNull(): Float? = parseExtra { asFloatOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [FloatArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatArrayType]
 * @return The [FloatArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatArrayType].
 */
fun Fragment.parseFloatArray(): FloatArray = parseExtra { asFloatArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [FloatArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [FloatArray] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseFloatArrayOrNull(): FloatArray? = parseExtra { asFloatArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Int].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerType]
 * @return The [Int] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerType].
 */
fun Fragment.parseInt(): Int = parseExtra { asInt() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Int] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Int] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseIntOrNull(): Int? = parseExtra { asIntOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [IntArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerArrayType]
 * @return The [IntArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerArrayType].
 */
fun Fragment.parseIntArray(): IntArray = parseExtra { asIntArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [IntArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [IntArray] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseIntArrayOrNull(): IntArray? = parseExtra { asIntArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Long].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongType]
 * @return The [Long] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongType].
 */
fun Fragment.parseLong(): Long = parseExtra { asLong() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Long] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Long] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseLongOrNull(): Long? = parseExtra { asLongOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [LongArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongArrayType]
 * @return The [LongArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongArrayType].
 */
fun Fragment.parseLongArray(): LongArray = parseExtra { asLongArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [LongArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [LongArray] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseLongArrayOrNull(): LongArray? = parseExtra { asLongArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Short].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortType]
 * @return The [Short] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortType].
 */
fun Fragment.parseShort(): Short = parseExtra { asShort() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [Short] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Short] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseShortOrNull(): Short? = parseExtra { asShortOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [ShortArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortArrayType]
 * @return The [ShortArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortArrayType].
 */
fun Fragment.parseShortArray(): ShortArray = parseExtra { asShortArray() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [ShortArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [ShortArray] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseShortArrayOrNull(): ShortArray? = parseExtra { asShortArrayOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [String].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringType]
 * @return The [String] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringType].
 */
fun Fragment.parseString(): String = parseExtra { asString() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [String] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [String] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseStringOrNull(): String? = parseExtra { asStringOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [List] of [String].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringArrayType]
 * @return The [List] of [String] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringArrayType].
 */
fun Fragment.parseStringList(): List<String> = parseExtra { asStringList() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [List] of [String] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [List] of [String] stored in the bundle or null if no such value was found.
 */
fun Fragment.parseStringListOrNull(): List<String>? = parseExtra { asStringListOrNull() }

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [T].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer]
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle.
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] with [serializer].
 */
fun <T> Fragment.parseTypeOf(serializer: KSerializer<T>): T = parseExtra {
    toTypedProperty(
        NavigationBundleSpecType.SerializedType(serializer),
    )
}

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [T] if is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle or null if no such value was found.
 */
fun <T : Any> Fragment.parseTypeOfOrNull(serializer: KSerializer<T>): T? = parseExtraOrNull {
    toTypedPropertyOrNull(
        NavigationBundleSpecType.SerializedType(serializer),
    )
}

/**
 * Parses the [Bundle] of [androidx.fragment.app.Fragment.getArguments] into [T] if is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle or null if no such value was found.
 */
@JvmName("parseNullableTypeOfOrNull")
fun <T> Fragment.parseTypeOfOrNull(serializer: KSerializer<T>): T? = parseExtraOrNull {
    toTypedPropertyOrNull(
        NavigationBundleSpecType.SerializedType(serializer),
    )
}
