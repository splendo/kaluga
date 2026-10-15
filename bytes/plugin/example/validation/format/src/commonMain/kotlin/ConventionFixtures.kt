/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.annotations.ByteStuffedSlip
import com.splendo.kaluga.bytes.annotations.ByteStuffedXor
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.Scalar
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Unsigned
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ConventionValue<T>(val value: T)

@Serializable
enum class ConventionEnum {
    @SerializedByteValue(0x01)
    A,

    @SerializedByteValue(0xAA.toByte())
    B,
}

@BytesLayout
interface ConventionBase {
    @Size(Length.`8_BIT`)
    @Size(Length.`16_BIT`)
    val base: Int
}

@BytesLayout
interface ConventionKey {
    @Unsigned
    val key: UShort
}

@BytesLayout
@Prefix([0x42, 0xAA.toByte()])
interface ConventionStructure : ConventionBase {
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
    val order: Short

    @Scalar(decimalExponent = -2)
    @Size(Length.`16_BIT`)
    val scaled: Double
    val entry: ConventionEnum
    val wrapped: ConventionValue<Byte>?
    val nested: ConventionKey
    val nestedList: List<ConventionKey>
    val keyed: Map<ConventionKey, ConventionKey?>
}

@BytesLayout
@Prefix([0x11])
@Postfix([0x22])
sealed interface ConventionSealed

@BytesLayout
@SerializedByteValue(0x02)
interface ConventionSealedZ : ConventionSealed {
    val z: Byte
}

@BytesLayout
@SerializedByteValue(0x01)
interface ConventionSealedA : ConventionSealed {
    val a: Int
}

@BytesLayout
@SerializedByteValue(0x03)
interface ConventionSealedEmpty : ConventionSealed

@BytesLayout
interface ConventionHolder {
    val sealed: ConventionSealed
}

@BytesLayout
interface ConventionStuffing {
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00, additionalEscapedBytes = [0x7E, 0xAA.toByte()], xorKey = 0x10)
    val xor: List<Byte>

    @ByteStuffedSlip(end = 0xC1.toByte())
    val slip: String
    val trailer: Byte
}
