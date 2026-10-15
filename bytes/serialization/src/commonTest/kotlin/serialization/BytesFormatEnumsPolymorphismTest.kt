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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.toHexString
import com.splendo.kaluga.bytes.annotations.ByOrdinal
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
import com.splendo.kaluga.bytes.crc.CRC16
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@Serializable
private enum class SomeEnum {
    @SerializedByteValue(0x01)
    A,

    @SerializedByteValue(0x02)
    B,
}

// Not private: kotlinx-serialization resolves a bare validateEncoding(value, expected) call via
// T::class.serializer(), which reflectively reads this object's INSTANCE field and needs it visible.
@Serializable
@Prefix([0x22, 0x44])
@Postfix([0x33])
data object Object

@Serializable
@Prefix([0x11, 0x55])
@Postfix([0x66, 0xAA.toByte()])
private sealed class SomeSealedClass {
    @Serializable
    @SerializedByteValue(0x01)
    data class A(val a: Int) : SomeSealedClass()

    @Serializable
    @SerializedByteValue(0x02)
    data class B(
        @Scalar(decimalExponent = -2)
        val b: Double,
    ) : SomeSealedClass()
}

// A sealed hierarchy WITHOUT @SerializedByteValue, with subclasses that have a
// DIFFERENT number of properties. The fallback identifies each option by its
// subclass serialName encoded as a raw UTF-8 string (no length prefix).
@Serializable
private sealed class UnmarkedSealed {
    @Serializable
    data class One(val a: Int) : UnmarkedSealed()

    @Serializable
    data class Three(val x: Byte, val y: Byte, val z: Byte) : UnmarkedSealed()
}

@Serializable
private data class UnmarkedSealedContainer(val value: UnmarkedSealed)

// 2-byte discriminators on a sealed class (e.g. LE command codes where high byte is always 0x00)
@Serializable
private sealed interface TwoByteCmd

@Serializable
@SerializedByteValue(0x01, 0x00)
private data class CmdA(@Size(Length.`8_BIT`) @Unsigned val param: Int) : TwoByteCmd

@Serializable
@SerializedByteValue(0x02, 0x00)
private data class CmdB(@Size(Length.`16_BIT`) @Unsigned val param: Int) : TwoByteCmd

// 2-byte discriminators on an enum; distinguishes [0x01 0x00] from [0x00 0x01] (true multi-byte)
@Serializable
private enum class OpCode {
    @SerializedByteValue(0x00, 0x01)
    CONNECT,

    @SerializedByteValue(0x01, 0x00)
    DISCONNECT,
}

// Default @ByOrdinal: encodes ordinal as 1-byte (ACCEPTED=0x00, REJECTED=0x01, ...)
@Serializable
@ByOrdinal
private enum class ResponseCode { ACCEPTED, REJECTED, INVALID_CRC, INVALID_STATE }

@Serializable
private data class Packet(@Size(Length.`8_BIT`) @Unsigned val id: Int, val code: ResponseCode)

// offset = 1: first variant -> 0x01, second -> 0x02, ...
@Serializable
@ByOrdinal(offset = 1)
private enum class OffsetOrdinal { FIRST, SECOND, THIRD }

@Serializable
private data class OffsetOrdinalPacket(val value: OffsetOrdinal)

// size = 16_BIT: ordinals encoded as 2-byte LE UShort (with default byteOrder)
@Serializable
@ByOrdinal(size = Length.`16_BIT`)
private enum class State { NONE, INITIALIZING, IDLE, MEASUREMENT, ERROR }

@Serializable
private data class StatePacket(val state: State)

// A per-variant @SerializedByteValue takes precedence over @ByOrdinal for that specific entry.
// Useful when one variant has a non-contiguous code while others are ordinal-based.
@Serializable
@ByOrdinal(offset = 1)
private enum class OverrideOrdinal {
    FIRST, // ordinal 0 + 1 = 0x01 (from @ByOrdinal)
    SECOND,

    // ordinal 1 + 1 = 0x02 (from @ByOrdinal)
    @SerializedByteValue(0x04) // explicit override - skips 0x03
    THIRD,
}

// @ByOrdinal enum used as a field inside a larger structure
@Serializable
@ByOrdinal
private enum class TriStateOrdinal { FIRST, SECOND, THIRD }

@Serializable
private data class OrdinalFieldPacket(@Size(Length.`8_BIT`) @Unsigned val code: Int, val state: TriStateOrdinal)

@Serializable
@SizePolymorphic
sealed interface SizeDispatchedPayload

@Serializable
data class FourBytePayload(@Unsigned val value: UInt) : SizeDispatchedPayload // 4 bytes

@Serializable
data class NineBytePayload(@Unsigned val a: UInt, @Unsigned val b: UInt, val c: UByte) : SizeDispatchedPayload // 4 + 4 + 1 = 9 bytes

// ── Variable-size fallback ────────────────────────────────────────────────
// One fixed-size subtype + one variable-size catch-all.

@Serializable
@SizePolymorphic
private sealed interface MixedSizeSealed

@Serializable
private data class FixedMixed(@Unsigned val value: UInt) : MixedSizeSealed // 4 bytes - matched first

@Serializable
private data class VariableMixed(@Unsized val items: List<UByte>) : MixedSizeSealed // variable - catch-all

@Serializable
@Prefix([0xAA.toByte()])
@Postfix([0xBB.toByte()])
private data class WrappedWithPrefixPostfix(val payload: SizeDispatchedPayload)

@Serializable
@JvmInline
private value class RRInterval(
    @Size(Length.`16_BIT`)
    @Scalar(binaryExponent = 10)
    val seconds: Double,
) {
    constructor(duration: Duration) : this(duration.toDouble(DurationUnit.SECONDS))
}

class BytesFormatEnumsPolymorphismTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    @Test
    fun encodeEnum() {
        validateEncoding(SomeEnum.A, SomeEnum.serializer(), byteArrayOf(0x01))
    }

    // ── @ByOrdinal ────────────────────────────────────────────────────────────

    @Test
    fun byOrdinalZeroBased() {
        validateEncoding(Packet(0x10, ResponseCode.ACCEPTED), byteArrayOf(0x10, 0x00))
        validateEncoding(Packet(0x10, ResponseCode.INVALID_STATE), byteArrayOf(0x10, 0x03))
    }

    @Test
    fun byOrdinalWithOffset() {
        validateEncoding(OffsetOrdinalPacket(OffsetOrdinal.FIRST), byteArrayOf(0x01))
        validateEncoding(OffsetOrdinalPacket(OffsetOrdinal.SECOND), byteArrayOf(0x02))
        validateEncoding(OffsetOrdinalPacket(OffsetOrdinal.THIRD), byteArrayOf(0x03))
    }

    @Test
    fun byOrdinalTwoByte() {
        // NONE=0 -> [0x00 0x00], IDLE=2 -> [0x02 0x00] (LE)
        validateEncoding(StatePacket(State.NONE), byteArrayOf(0x00, 0x00))
        validateEncoding(StatePacket(State.IDLE), byteArrayOf(0x02, 0x00))
        validateEncoding(StatePacket(State.ERROR), byteArrayOf(0x04, 0x00))
    }

    private object ByOrdinalPerVariantOverrideFixtures {
        @Serializable
        data class Packet(val value: OverrideOrdinal)
    }

    @Test
    fun byOrdinalPerVariantOverride() {
        validateEncoding(ByOrdinalPerVariantOverrideFixtures.Packet(OverrideOrdinal.FIRST), byteArrayOf(0x01))
        validateEncoding(ByOrdinalPerVariantOverrideFixtures.Packet(OverrideOrdinal.SECOND), byteArrayOf(0x02))
        validateEncoding(ByOrdinalPerVariantOverrideFixtures.Packet(OverrideOrdinal.THIRD), byteArrayOf(0x04))
    }

    @Test
    fun byOrdinalInDataClass() {
        validateEncoding(OrdinalFieldPacket(0x0F, TriStateOrdinal.THIRD), byteArrayOf(0x0F, 0x02))
    }

    @Test
    fun encodeObject() {
        validateEncoding(Object, byteArrayOf(0x22, 0x44, 0x33))
    }

    @Test
    fun encodeSealed() {
        validateEncoding(SomeSealedClass.A(4), SomeSealedClass.serializer(), byteArrayOf(0x11, 0x55, 0x01, 0x04, 0x00, 0x00, 0x00, 0x66, 0xAA.toByte()))
        validateEncoding(SomeSealedClass.B(600.0), SomeSealedClass.serializer(), byteArrayOf(0x11, 0x55, 0x02, 0x06, 0x00, 0x00, 0x00, 0x66, 0xAA.toByte()))
    }

    @Test
    fun encodeUnmarkedSealed() {
        // No @SerializedByteValue: each option is identified by its subclass serialName as a
        // raw UTF-8 string (no length prefix), followed by the subclass body.
        val oneName = UnmarkedSealed.One.serializer().descriptor.serialName
            .encodeToByteArray()
        val threeName = UnmarkedSealed.Three.serializer().descriptor.serialName
            .encodeToByteArray()

        validateEncoding(
            UnmarkedSealedContainer(UnmarkedSealed.One(4)),
            UnmarkedSealedContainer.serializer(),
            buildByteArray {
                add(oneName)
                add(4)
            },
        )

        validateEncoding(
            UnmarkedSealedContainer(UnmarkedSealed.Three(1, 2, 3)),
            UnmarkedSealedContainer.serializer(),
            buildByteArray {
                add(threeName)
                add(byte = 1)
                add(byte = 2)
                add(byte = 3)
            },
        )
    }

    // ── Multi-byte @SerializedByteValue ───────────────────────────────────────

    @Test
    fun multiByteSealedDiscriminator() {
        val backend = createBackend(
            BackendOptions(
                serializersModule = SerializersModule {
                    polymorphic(TwoByteCmd::class) {
                        subclass(CmdA::class)
                        subclass(CmdB::class)
                    }
                },
            ),
        )

        validateRoundTrip(
            CmdA(0x42),
            TwoByteCmd.serializer(),
            byteArrayOf(0x01, 0x00, 0x42),
            backend,
        )
        validateRoundTrip(
            CmdB(0x0300),
            TwoByteCmd.serializer(),
            byteArrayOf(0x02, 0x00, 0x00, 0x03),
            backend,
        )
    }

    private object MultiByteEnumDiscriminatorFixtures {
        @Serializable
        data class Frame(val op: OpCode)
    }

    @Test
    fun multiByteEnumDiscriminator() {
        validateEncoding(MultiByteEnumDiscriminatorFixtures.Frame(OpCode.CONNECT), byteArrayOf(0x00, 0x01))
        validateEncoding(MultiByteEnumDiscriminatorFixtures.Frame(OpCode.DISCONNECT), byteArrayOf(0x01, 0x00))
    }

    @Test
    fun classNameDiscriminatorsSkipLengthValidation() {
        // Class-name discriminators naturally have different byte lengths and must NOT trigger
        // the length-consistency check (the decoder uses forward peek, not fixed-width reads).
        // UnmarkedSealed already exercises this: One has a shorter name than Three.
        val container = UnmarkedSealedContainer(UnmarkedSealed.One(42))
        val bytes = BytesFormat.encodeToByteArray(container)
        val decoded = BytesFormat.decodeFromByteArray(UnmarkedSealedContainer.serializer(), bytes)
        assertEquals(container, decoded)
    }

    // ── @SizePolymorphic dispatch ─────────────────────────────────────────────
    // The sealed class declares the dispatch mode; subtypes carry no size annotation.
    // Sizes are computed automatically from field annotations at descriptor construction time.

    @Test
    fun sizeTagDispatch_fourBytePayload() {
        // No type discriminator bytes - just the content; 4 bytes total.
        // validateEncoding also verifies Nested<T> round-trips correctly: the sealed is the only
        // field in Nested, so no sibling fires DataAfterUnconstrainedData, and Prefix/Postfix on
        // Nested still work because they are written via build(), not addAction().
        validateEncoding(
            FourBytePayload(0x01020304u) as SizeDispatchedPayload,
            BytesFormat.serializer<SizeDispatchedPayload>(),
            byteArrayOf(0x04, 0x03, 0x02, 0x01),
        )
    }

    @Test
    fun sizeTagDispatch_nineBytePayload() {
        validateEncoding(
            NineBytePayload(0x01020304u, 0x05060708u, 0x09u) as SizeDispatchedPayload,
            BytesFormat.serializer<SizeDispatchedPayload>(),
            byteArrayOf(0x04, 0x03, 0x02, 0x01, 0x08, 0x07, 0x06, 0x05, 0x09),
        )
    }

    @Test
    fun sizePolymorphic_fixedSubtypeMatchedBeforeFallback() {
        // 4-byte payload -> FixedMixed; variable remainder -> VariableMixed (catch-all).
        validateEncoding(
            FixedMixed(0x01020304u) as MixedSizeSealed,
            BytesFormat.serializer<MixedSizeSealed>(),
            byteArrayOf(0x04, 0x03, 0x02, 0x01),
        )
    }

    @Test
    fun sizePolymorphic_fallbackDecodesVariablePayload() {
        // 3 bytes don't match FixedMixed(4) -> falls through to VariableMixed.
        val bytes = byteArrayOf(0x0A, 0x0B, 0x0C)
        val result = BytesFormat.decodeFromByteArray(BytesFormat.serializer<MixedSizeSealed>(), bytes)
        assertEquals(VariableMixed(listOf(0x0A.toUByte(), 0x0B.toUByte(), 0x0C.toUByte())), result)
    }

    @Test
    fun sizeTagDispatch_throwsOnUnrecognisedSize() {
        // 6 bytes matches neither 4 nor 9.
        assertFailsWith<SerializationException> {
            BytesFormat.decodeFromByteArray(BytesFormat.serializer<SizeDispatchedPayload>(), ByteArray(6))
        }
    }

    @Test
    fun sizeTagDispatch_allowsPrefixPostfixOnContainingClass() {
        // Prefix/Postfix on the containing class are written in build(), not addAction(), so they
        // are unaffected by the makeUnconstrained() that @SizePolymorphic sets on the parent builder.
        val value = WrappedWithPrefixPostfix(FourBytePayload(0x01020304u))
        val bytes = BytesFormat.encodeToByteArray(BytesFormat.serializer<WrappedWithPrefixPostfix>(), value)
        assertTrue(bytes.contentEquals(byteArrayOf(0xAA.toByte(), 0x04, 0x03, 0x02, 0x01, 0xBB.toByte())), bytes.toHexString(separator = " "))
        assertEquals(value, BytesFormat.decodeFromByteArray(BytesFormat.serializer<WrappedWithPrefixPostfix>(), bytes))
    }

    private object EncodeHeartRateFixtures {
        @Serializable
        data class HeartRate(
            @Size(Length.`8_BIT`)
            @Size(Length.`16_BIT`)
            @Unsigned
            val heartRate: Int,
            @FlagIndex(2)
            val contactSupported: Boolean,
            @FlagIndex(1)
            val contactDetected: Boolean = !contactSupported,
            @Unsigned
            @Size(Length.`16_BIT`)
            val energyExpended: Int? = null,
            @NullIfEmpty
            @Unsized
            val rrIntervals: List<RRInterval> = emptyList(),
        )
    }

    @Test
    fun encodeHeartRate() {
        validateEncoding(
            EncodeHeartRateFixtures.HeartRate(
                heartRate = 85,
                contactSupported = true,
                contactDetected = false,
                rrIntervals = emptyList(),
            ),
            byteArrayOf(0x04, 0x55),
        )
        validateEncoding(
            EncodeHeartRateFixtures.HeartRate(
                heartRate = 300,
                contactSupported = true,
                contactDetected = false,
                energyExpended = 500,
                rrIntervals = listOf(RRInterval(1.seconds), RRInterval(0.5.seconds)),
            ),
            byteArrayOf(0x1D, 0x2C, 0x01, 0xF4.toByte(), 0x01, 0x00, 0x04, 0x00, 0x02),
        )

        validateEncoding(
            listOf(
                EncodeHeartRateFixtures.HeartRate(50, contactSupported = true, contactDetected = true),
                EncodeHeartRateFixtures.HeartRate(500, contactSupported = true, contactDetected = false, rrIntervals = listOf(RRInterval(2.seconds), RRInterval(0.25.seconds))),
            ),
            ListSerializer(EncodeHeartRateFixtures.HeartRate.serializer()),
            byteArrayOf(0x02, 0x06, 0x32, 0x15, 0xF4.toByte(), 0x01, 0x00, 0x08, 0x00, 0x01),
        )
    }

    private object FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixtures {
        @Serializable
        enum class PackedMode { OFF, LOW, MED, HIGH }

        @Serializable
        @SizePolymorphic
        sealed interface FlagEnumSizeVariant

        // header(1) + body(2) = 3 bytes: the flag-packed enum adds header bits, no body bytes.
        @Serializable
        data class FlagEnumPacked(@FlagIndex(0) val mode: PackedMode, @Size(Length.`16_BIT`) val x: Short) : FlagEnumSizeVariant

        @Serializable
        data class FlagEnumWide(@Size(Length.`8_BIT`) val x: Byte, @Size(Length.`8_BIT`) val y: Byte) : FlagEnumSizeVariant

        @Serializable
        data class FlagEnumSizeHolder(val payload: FlagEnumSizeVariant)
    }

    @Test
    fun flagPackedEnumInSizePolymorphicSubtypeDispatchesBySize() {
        validateRoundTrip(
            FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixtures.FlagEnumSizeHolder(
                FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixtures.FlagEnumPacked(
                    mode = FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixtures.PackedMode.HIGH,
                    x = 300,
                ),
            ),
            FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixtures.FlagEnumSizeHolder.serializer(),
        )
    }

    private object CharFieldGivesSizePolymorphicSubtypesDistinctFixedSizesFixtures {
        @Serializable
        @SizePolymorphic
        sealed interface CharSizeVariant

        @Serializable
        data class CharSizeSmall(val c: Char) : CharSizeVariant

        @Serializable
        data class CharSizeLarge(val c: Char, @Size(Length.`8_BIT`) val x: Byte) : CharSizeVariant

        @Serializable
        data class CharSizeHolder(val payload: CharSizeVariant)
    }

    @Test
    fun charFieldGivesSizePolymorphicSubtypesDistinctFixedSizes() {
        // A Char's wire width is fixed by its encoding, so both subtypes are fixed-size (1 and 2 bytes).
        validateRoundTrip(
            CharFieldGivesSizePolymorphicSubtypesDistinctFixedSizesFixtures.CharSizeHolder(CharFieldGivesSizePolymorphicSubtypesDistinctFixedSizesFixtures.CharSizeSmall('A')),
            CharFieldGivesSizePolymorphicSubtypesDistinctFixedSizesFixtures.CharSizeHolder.serializer(),
            byteArrayOf(0x41),
        )
    }

    private object SizePolymorphicPayloadWithChecksumRoundTripsFixtures {
        @Serializable
        @Prefix([0x11])
        @Postfix([0x22])
        @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
        data class SizePolymorphicWithChecksum(val payload: SizeDispatchedPayload)
    }

    @Test
    fun sizePolymorphicPayloadWithChecksumRoundTrips() {
        // The checksum is part of the footer, so it is not counted in the payload size dispatched on.
        val body = byteArrayOf(0x04, 0x03, 0x02, 0x01)
        val expectedBytes = buildByteArray {
            add(0x11.toByte())
            add(body)
            add(CRC16.compute(body).toUShort())
            add(0x22.toByte())
        }
        validateEncoding(
            SizePolymorphicPayloadWithChecksumRoundTripsFixtures.SizePolymorphicWithChecksum(FourBytePayload(0x01020304u)),
            SizePolymorphicPayloadWithChecksumRoundTripsFixtures.SizePolymorphicWithChecksum.serializer(),
            expectedBytes,
        )
    }
}
