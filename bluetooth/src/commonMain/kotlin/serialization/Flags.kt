/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.Encoding
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.utils.Int24
import com.splendo.kaluga.base.utils.UInt24
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

@Suppress("EnumEntryName")
enum class Length(val bytes: Int) {
    `8_BIT`(1),
    `16_BIT`(2),
    `24_BIT`(3),
    `32_BIT`(4),
    `64_BIT`(8),
    ;

    fun fits(number: Number, signed: Boolean) = when (this) {
        `8_BIT` -> if (signed) {
            number.toInt() in Byte.MIN_VALUE.toInt()..Byte.MAX_VALUE.toInt()
        } else {
            number.toInt().toUInt() in UByte.MIN_VALUE.toUInt()..UByte.MAX_VALUE.toUInt()
        }
        `16_BIT` -> if (signed) {
            number.toInt() in Short.MIN_VALUE.toInt()..Short.MAX_VALUE.toInt()
        } else {
            number.toInt().toUInt() in UShort.MIN_VALUE.toUInt()..UShort.MAX_VALUE.toUInt()
        }
        `24_BIT` -> if (signed) {
            number.toLong() in Int24.MIN_VALUE.value.toLong()..Int24.MAX_VALUE.value.toLong()
        } else {
            number.toLong().toULong() in UInt24.MIN_VALUE.value.toULong()..UInt24.MAX_VALUE.value.toULong()
        }
        `32_BIT` -> if (signed) {
            number.toLong() in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()
        } else {
            number.toLong().toULong() in UInt.MIN_VALUE.toULong()..Int.MAX_VALUE.toULong()
        }
        `64_BIT` -> true // Always fits as it is the max we support
    }
}

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class Prefix(val value: ByteArray)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class Postfix(val value: ByteArray)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class FlagIndex(val index: Int)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class FlagWidth(val bits: Int = 1)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class ByteOrder(val order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class LengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte())

internal fun LengthPrefix.asLengthPrefix() = when {
    lengthAsShort -> StringEncodingSettings.LengthPrefix.ShortLength
    canOverflow -> StringEncodingSettings.LengthPrefix.WithOverflow(sentinel)
    else -> StringEncodingSettings.LengthPrefix.ByteLength
}

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Encoded(val encoding: Encoding = Encoding.UTF_8)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class NullTerminated

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Unsigned

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Scalar(val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0, val offset: Int = 0)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class MedFloat

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class Sizing(val length: Length)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Unsized

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class NullIfEmpty

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class Checksum(val width: Int, val polynomial: ULong, val init: ULong, val xorOut: ULong = 0u, val reflectIn: Boolean = false, val reflectOut: Boolean = false)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class SerializedByteValue(val value: Byte)
