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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

/**
 * Applies [com.splendo.kaluga.bluetooth.serialization.ByteOrder] to any Item in a List.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemByteOrder(val order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)

/**
 * Applies [com.splendo.kaluga.bluetooth.serialization.ByteOrder] to any Key in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyByteOrder(val order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)

/**
 * Applies [com.splendo.kaluga.bluetooth.serialization.ByteOrder] to any Value in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueByteOrder(val order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST)

/**
 * Applies [LengthPrefix] to any Item in a List
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemLengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte())

/**
 * Applies [LengthPrefix] to any Key in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyLengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte())

/**
 * Applies [LengthPrefix] to any Value in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueLengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte())

/**
 * Applies [Encoded] to any Item in a List
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemEncoded(val encoding: Encoding = Encoding.UTF_8)

/**
 * Applies [Encoded] to any Key in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyEncoded(val encoding: Encoding = Encoding.UTF_8)

/**
 * Applies [Encoded] to any Value in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueEncoded(val encoding: Encoding = Encoding.UTF_8)

/**
 * Applies [NullTerminated] to any Item in a List
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemNullTerminated

/**
 * Applies [NullTerminated] to any Key in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyNullTerminated

/**
 * Applies [NullTerminated] to any Value in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueNullTerminated

/**
 * Applies [Unsigned] to any Item in a List
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemUnsigned

/**
 * Applies [Unsigned] to any Key in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyUnsigned

/**
 * Applies [Unsigned] to any Value in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueUnsigned

/**
 * Applies [Scalar] to any Item in a List
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemScalar(val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0, val offset: Int = 0)

/**
 * Applies [Scalar] to any Key in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyScalar(val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0, val offset: Int = 0)

/**
 * Applies [Scalar] to any Value in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueScalar(val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0, val offset: Int = 0)

/**
 * Applies [MedFloat] to any Item in a List
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ItemMedFloat

/**
 * Applies [MedFloat] to any Key in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class KeyMedFloat

/**
 * Applies [MedFloat] to any Value in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ValueMedFloat

/**
 * Applies [Size] to any Item in a List
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class ItemSize(val size: Length)

/**
 * Applies [Size] to any Key in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class KeySize(val size: Length)

/**
 * Applies [Size] to any Value in a Map
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class ValueSize(val size: Length)
