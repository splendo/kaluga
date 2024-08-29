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

import android.app.Activity
import android.os.Bundle
import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.serialization.KSerializer

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Boolean].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanType]
 * @return The [Boolean] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanType].
 */
fun Activity.parseBoolean(): Boolean = intent.parseBoolean()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Boolean] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Boolean] stored in the bundle or null if no such value was found.
 */
fun Activity.parseBooleanOrNull(): Boolean? = intent.parseBooleanOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [BooleanArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanArrayType]
 * @return The [BooleanArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanArrayType].
 */
fun Activity.parseBooleanArray(): BooleanArray = intent.parseBooleanArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [BooleanArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.BooleanArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [BooleanArray] stored in the bundle or null if no such value was found.
 */
fun Activity.parseBooleanArrayOrNull(): BooleanArray? = intent.parseBooleanArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Byte].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteType]
 * @return The [Byte] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteType].
 */
fun Activity.parseByte(): Byte = intent.parseByte()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Byte] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Byte] stored in the bundle or null if no such value was found.
 */
fun Activity.parseByteOrNull(): Byte? = intent.parseByteOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [ByteArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteArrayType]
 * @return The [ByteArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType].
 */
fun Activity.parseByteArray(): ByteArray = intent.parseByteArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [ByteArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ByteArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [ByteArray] stored in the bundle or null if no such value was found.
 */
fun Activity.parseByteArrayOrNull(): ByteArray? = intent.parseByteArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Char].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharType]
 * @return The [Char] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharType].
 */
fun Activity.parseChar(): Char = intent.parseChar()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Char] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Char] stored in the bundle or null if no such value was found.
 */
fun Activity.parseCharOrNull(): Char? = intent.parseCharOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [CharArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharArrayType]
 * @return The [CharArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharArrayType].
 */
fun Activity.parseCharArray(): CharArray = intent.parseCharArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [CharArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [CharArray] stored in the bundle or null if no such value was found.
 */
fun Activity.parseCharArrayOrNull(): CharArray? = intent.parseCharArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [CharSequence] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.CharSequenceType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [CharSequence] stored in the bundle or null if no such value was found.
 */
fun Activity.parseCharSequenceOrNull(): CharSequence? = intent.parseCharSequenceOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [KalugaDate].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateType]
 * @return The [KalugaDate] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateType].
 */
fun Activity.parseDate(): KalugaDate = intent.parseDate()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [KalugaDate] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [KalugaDate] stored in the bundle or null if no such value was found.
 */
fun Activity.parseDateOrNull(): KalugaDate? = intent.parseDateOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [List] of [KalugaDate].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateArrayType]
 * @return The [List] of [KalugaDate] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateArrayType].
 */
fun Activity.parseDateArray(): List<KalugaDate> = intent.parseDateArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [List] of [KalugaDate] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DateArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [List] of [KalugaDate] stored in the bundle or null if no such value was found.
 */
fun Activity.parseDateArrayOrNull(): List<KalugaDate>? = intent.parseDateArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Double].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleType]
 * @return The [Double] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleType].
 */
fun Activity.parseDouble(): Double = intent.parseDouble()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Double] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Double] stored in the bundle or null if no such value was found.
 */
fun Activity.parseDoubleOrNull(): Double? = intent.parseDoubleOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [DoubleArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleArrayType]
 * @return The [DoubleArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleArrayType].
 */
fun Activity.parseDoubleArray(): DoubleArray = intent.parseDoubleArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [DoubleArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.DoubleArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [DoubleArray] stored in the bundle or null if no such value was found.
 */
fun Activity.parseDoubleArrayOrNull(): DoubleArray? = intent.parseDoubleArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Float].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatType]
 * @return The [Float] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatType].
 */
fun Activity.parseFloat(): Float = intent.parseFloat()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Float] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Float] stored in the bundle or null if no such value was found.
 */
fun Activity.parseFloatOrNull(): Float? = intent.parseFloatOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [FloatArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatArrayType]
 * @return The [FloatArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatArrayType].
 */
fun Activity.parseFloatArray(): FloatArray = intent.parseFloatArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [FloatArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.FloatArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [FloatArray] stored in the bundle or null if no such value was found.
 */
fun Activity.parseFloatArrayOrNull(): FloatArray? = intent.parseFloatArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Int].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerType]
 * @return The [Int] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerType].
 */
fun Activity.parseInt(): Int = intent.parseInt()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Int] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Int] stored in the bundle or null if no such value was found.
 */
fun Activity.parseIntOrNull(): Int? = intent.parseIntOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [IntArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerArrayType]
 * @return The [IntArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerArrayType].
 */
fun Activity.parseIntArray(): IntArray = intent.parseIntArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [IntArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.IntegerArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [IntArray] stored in the bundle or null if no such value was found.
 */
fun Activity.parseIntArrayOrNull(): IntArray? = intent.parseIntArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Long].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongType]
 * @return The [Long] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongType].
 */
fun Activity.parseLong(): Long = intent.parseLong()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Long] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Long] stored in the bundle or null if no such value was found.
 */
fun Activity.parseLongOrNull(): Long? = intent.parseLongOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [LongArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongArrayType]
 * @return The [LongArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongArrayType].
 */
fun Activity.parseLongArray(): LongArray = intent.parseLongArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [LongArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.LongArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [LongArray] stored in the bundle or null if no such value was found.
 */
fun Activity.parseLongArrayOrNull(): LongArray? = intent.parseLongArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Short].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortType]
 * @return The [Short] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortType].
 */
fun Activity.parseShort(): Short = intent.parseShort()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [Short] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [Short] stored in the bundle or null if no such value was found.
 */
fun Activity.parseShortOrNull(): Short? = intent.parseShortOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [ShortArray].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortArrayType]
 * @return The [ShortArray] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortArrayType].
 */
fun Activity.parseShortArray(): ShortArray = intent.parseShortArray()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [ShortArray] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.ShortArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [ShortArray] stored in the bundle or null if no such value was found.
 */
fun Activity.parseShortArrayOrNull(): ShortArray? = intent.parseShortArrayOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [String].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringType]
 * @return The [String] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringType].
 */
fun Activity.parseString(): String = intent.parseString()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [String] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [String] stored in the bundle or null if no such value was found.
 */
fun Activity.parseStringOrNull(): String? = intent.parseStringOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [List] of [String].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringArrayType]
 * @return The [List] of [String] stored in the bundle
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringArrayType].
 */
fun Activity.parseStringList(): List<String> = intent.parseStringList()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [List] of [String] if it is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.StringArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @return The [List] of [String] stored in the bundle or null if no such value was found.
 */
fun Activity.parseStringListOrNull(): List<String>? = intent.parseStringListOrNull()

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [T].
 * Requires that the [Bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer]
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle.
 * @throws NullPointerException if [android.content.Intent.getExtras] is `null`
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] with [serializer].
 */
fun <T> Activity.parseTypeOf(serializer: KSerializer<T>): T = intent.parseTypeOf(serializer)

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [T] if is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle or null if no such value was found.
 */
fun <T : Any> Activity.parseTypeOfOrNull(serializer: KSerializer<T>): T? = intent.parseTypeOfOrNull(serializer)

/**
 * Parses the [Bundle] of [android.app.Activity.getIntent] into [T] if is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType.SerializedType] with [serializer] either directly or wrapped by [NavigationBundleSpecType.OptionalType].
 * @param serializer The [KSerializer] to deserialize [T] from the bundle.
 * @return The [T] stored in the bundle or null if no such value was found.
 */
@JvmName("parseNullableTypeOfOrNull")
fun <T> Activity.parseTypeOfOrNull(serializer: KSerializer<T>): T? = intent.parseTypeOfOrNull(serializer)
