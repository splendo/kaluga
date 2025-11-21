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
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemByteOrder(val order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyByteOrder(val order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueByteOrder(val order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemLengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte())

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyLengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte())

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueLengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte())

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemEncoded(val encoding: StringEncodingSettings.Encoding = StringEncodingSettings.Encoding.UTF_8)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyEncoded(val encoding: StringEncodingSettings.Encoding = StringEncodingSettings.Encoding.UTF_8)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueEncoded(val encoding: StringEncodingSettings.Encoding = StringEncodingSettings.Encoding.UTF_8)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemNullTerminated

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyNullTerminated

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueNullTerminated

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemUnsigned()

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyUnsigned()

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueUnsigned()

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemScalar(val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0, val offset: Int = 0)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyScalar(val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0, val offset: Int = 0)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueScalar(val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0, val offset: Int = 0)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemMedFloat

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyMedFloat

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueMedFloat

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class ItemSize(val size: Length)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class KeySize(val size: Length)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class ValueSize(val size: Length)