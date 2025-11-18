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

import com.splendo.kaluga.base.utils.ByteOrder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

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
annotation class LengthPrefix(val bytes: Int = 1, val canOverflow: Boolean = false, val sentinel: Short = 0xFF)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class FixedLength(val bytes: Int = 2)

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
@Target(AnnotationTarget.PROPERTY)
annotation class AutoSizing(val minByteSize: Int = 1, val maxByteSize: Int = 2, val steps: Int = 1)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Checksum(val algorithm: ChecksumAlgorithm = ChecksumAlgorithm.CRC16)


