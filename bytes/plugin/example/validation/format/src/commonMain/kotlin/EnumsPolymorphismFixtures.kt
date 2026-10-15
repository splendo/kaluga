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

import com.splendo.kaluga.bytes.annotations.ByOrdinal
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.NullIfEmpty
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.Scalar
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.annotations.Unsized
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.subclass

@Serializable
enum class SomeEnum {
    @SerializedByteValue(0x01)
    A,

    @SerializedByteValue(0x02)
    B,
}

@BytesLayout
@Prefix([0x22, 0x44])
@Postfix([0x33])
interface Object

@BytesLayout
@Prefix([0x11, 0x55])
@Postfix([0x66, 0xAA.toByte()])
sealed interface SomeSealedClass {
    @BytesLayout
    @SerializedByteValue(0x01)
    interface A : SomeSealedClass {
        val a: Int
    }

    @BytesLayout
    @SerializedByteValue(0x02)
    interface B : SomeSealedClass {
        @Scalar(decimalExponent = -2)
        val b: Double
    }
}

// A sealed hierarchy WITHOUT @SerializedByteValue, with subclasses that have a
// DIFFERENT number of properties. The fallback identifies each option by its
// subclass serialName encoded as a raw UTF-8 string (no length prefix).
@BytesLayout
sealed interface UnmarkedSealed {
    @BytesLayout
    interface One : UnmarkedSealed {
        val a: Int
    }

    @BytesLayout
    interface Three : UnmarkedSealed {
        val x: Byte
        val y: Byte
        val z: Byte
    }
}

@BytesLayout
interface UnmarkedSealedContainer {
    val value: UnmarkedSealed
}

// 2-byte discriminators on a sealed class (e.g. LE command codes where high byte is always 0x00)
@BytesLayout
sealed interface TwoByteCmd

@BytesLayout
@SerializedByteValue(0x01, 0x00)
interface CmdA : TwoByteCmd {
    @Size(Length.`8_BIT`)
    @Unsigned
    val param: Int
}

@BytesLayout
@SerializedByteValue(0x02, 0x00)
interface CmdB : TwoByteCmd {
    @Size(Length.`16_BIT`)
    @Unsigned
    val param: Int
}

// 2-byte discriminators on an enum; distinguishes [0x01 0x00] from [0x00 0x01] (true multi-byte)
@Serializable
enum class OpCode {
    @SerializedByteValue(0x00, 0x01)
    CONNECT,

    @SerializedByteValue(0x01, 0x00)
    DISCONNECT,
}

// Default @ByOrdinal: encodes ordinal as 1-byte (ACCEPTED=0x00, REJECTED=0x01, ...)
@Serializable
@ByOrdinal
enum class ResponseCode { ACCEPTED, REJECTED, INVALID_CRC, INVALID_STATE }

@BytesLayout
interface Packet {
    @Size(Length.`8_BIT`)
    @Unsigned
    val id: Int
    val code: ResponseCode
}

// offset = 1: first variant -> 0x01, second -> 0x02, ...
@Serializable
@ByOrdinal(offset = 1)
enum class OffsetOrdinal { FIRST, SECOND, THIRD }

@BytesLayout
interface OffsetOrdinalPacket {
    val value: OffsetOrdinal
}

// size = 16_BIT: ordinals encoded as 2-byte LE UShort (with default byteOrder)
@Serializable
@ByOrdinal(size = Length.`16_BIT`)
enum class State { NONE, INITIALIZING, IDLE, MEASUREMENT, ERROR }

@BytesLayout
interface StatePacket {
    val state: State
}

// A per-variant @SerializedByteValue takes precedence over @ByOrdinal for that specific entry.
// Useful when one variant has a non-contiguous code while others are ordinal-based.
@Serializable
@ByOrdinal(offset = 1)
enum class OverrideOrdinal {
    FIRST, // ordinal 0 + 1 = 0x01 (from @ByOrdinal)
    SECOND,

    // ordinal 1 + 1 = 0x02 (from @ByOrdinal)
    @SerializedByteValue(0x04) // explicit override - skips 0x03
    THIRD,
}

// @ByOrdinal enum used as a field inside a larger structure
@Serializable
@ByOrdinal
enum class TriStateOrdinal { FIRST, SECOND, THIRD }

@BytesLayout
interface OrdinalFieldPacket {
    @Size(Length.`8_BIT`)
    @Unsigned
    val code: Int
    val state: TriStateOrdinal
}

@BytesLayout
@SizePolymorphic
sealed interface SizeDispatchedPayload

@BytesLayout
interface FourBytePayload : SizeDispatchedPayload { // 4 bytes
    @Unsigned val value: UInt
}

@BytesLayout
interface NineBytePayload : SizeDispatchedPayload { // 4 + 4 + 1 = 9 bytes
    @Unsigned val a: UInt

    @Unsigned val b: UInt
    val c: UByte
}

// ── Variable-size fallback ────────────────────────────────────────────────
// One fixed-size subtype + one variable-size catch-all.

@BytesLayout
@SizePolymorphic
sealed interface MixedSizeSealed

@BytesLayout
interface FixedMixed : MixedSizeSealed { // 4 bytes - matched first
    @Unsigned val value: UInt
}

@BytesLayout
interface VariableMixed : MixedSizeSealed { // variable - catch-all
    @Unsized val items: List<UByte>
}

@BytesLayout
@Prefix([0xAA.toByte()])
@Postfix([0xBB.toByte()])
interface WrappedWithPrefixPostfix {
    val payload: SizeDispatchedPayload
}

@Serializable
@JvmInline
value class RRInterval(
    @Size(Length.`16_BIT`)
    @Scalar(binaryExponent = 10)
    val seconds: Double,
) {
    constructor(duration: Duration) : this(duration.toDouble(DurationUnit.SECONDS))
}

object EncodeEnumFieldFixtures {
    @BytesLayout
    interface Holder {
        val value: SomeEnum
    }
}

object ByOrdinalPerVariantOverrideFixtures {
    @BytesLayout
    interface Packet {
        val value: OverrideOrdinal
    }
}

object MultiByteEnumDiscriminatorFixtures {
    @BytesLayout
    interface Frame {
        val op: OpCode
    }
}

object EncodeHeartRateFixtures {
    @BytesLayout
    interface HeartRate {
        @Size(Length.`8_BIT`)
        @Size(Length.`16_BIT`)
        @Unsigned
        val heartRate: Int

        @FlagIndex(2)
        val contactSupported: Boolean

        @FlagIndex(1)
        val contactDetected: Boolean

        @Unsigned
        @Size(Length.`16_BIT`)
        val energyExpended: Int?

        @NullIfEmpty
        @Unsized
        val rrIntervals: List<RRInterval>
    }
}

object FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixtures {
    @Serializable
    enum class PackedMode { OFF, LOW, MED, HIGH }

    @BytesLayout
    @SizePolymorphic
    sealed interface FlagEnumSizeVariant

    // header(1) + body(2) = 3 bytes: the flag-packed enum adds header bits, no body bytes.
    @BytesLayout
    interface FlagEnumPacked : FlagEnumSizeVariant {
        @FlagIndex(0)
        val mode: PackedMode

        @Size(Length.`16_BIT`)
        val x: Short
    }

    @BytesLayout
    interface FlagEnumWide : FlagEnumSizeVariant {
        @Size(Length.`8_BIT`)
        val x: Byte

        @Size(Length.`8_BIT`)
        val y: Byte
    }

    @BytesLayout
    interface FlagEnumSizeHolder {
        val payload: FlagEnumSizeVariant
    }
}

object CharFieldGivesSizePolymorphicSubtypesDistinctFixedSizesFixtures {
    @BytesLayout
    @SizePolymorphic
    sealed interface CharSizeVariant

    @BytesLayout
    interface CharSizeSmall : CharSizeVariant {
        val c: Char
    }

    @BytesLayout
    interface CharSizeLarge : CharSizeVariant {
        val c: Char

        @Size(Length.`8_BIT`)
        val x: Byte
    }

    @BytesLayout
    interface CharSizeHolder {
        val payload: CharSizeVariant
    }
}

object SizePolymorphicPayloadWithChecksumRoundTripsFixtures {
    @BytesLayout
    @Prefix([0x11])
    @Postfix([0x22])
    @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
    interface SizePolymorphicWithChecksum {
        val payload: SizeDispatchedPayload
    }
}
