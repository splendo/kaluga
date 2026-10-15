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

import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.serialization.DataAfterUnconstrainedData
import com.splendo.kaluga.bytes.toHexString
import com.splendo.kaluga.bytes.crc.CRC16
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer

/**
 * The enum and polymorphism tests every backend runs; [BytesFormatEnumsPolymorphismTest] adds the ones the views do not support yet.
 */
abstract class EnumsPolymorphismSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    @Test
    fun encodeEnumField() {
        // A root enum is no layout; as the only property of one it encodes to the same bytes.
        validateEncoding(EncodeEnumFieldFixturesHolderData(SomeEnum.A), byteArrayOf(0x01))
    }

    @Test
    fun byOrdinalZeroBased() {
        validateEncoding(PacketData(0x10, ResponseCode.ACCEPTED), byteArrayOf(0x10, 0x00))
        validateEncoding(PacketData(0x10, ResponseCode.INVALID_STATE), byteArrayOf(0x10, 0x03))
    }

    @Test
    fun byOrdinalWithOffset() {
        validateEncoding(OffsetOrdinalPacketData(OffsetOrdinal.FIRST), byteArrayOf(0x01))
        validateEncoding(OffsetOrdinalPacketData(OffsetOrdinal.SECOND), byteArrayOf(0x02))
        validateEncoding(OffsetOrdinalPacketData(OffsetOrdinal.THIRD), byteArrayOf(0x03))
    }

    @Test
    fun byOrdinalTwoByte() {
        // NONE=0 -> [0x00 0x00], IDLE=2 -> [0x02 0x00] (LE)
        validateEncoding(StatePacketData(State.NONE), byteArrayOf(0x00, 0x00))
        validateEncoding(StatePacketData(State.IDLE), byteArrayOf(0x02, 0x00))
        validateEncoding(StatePacketData(State.ERROR), byteArrayOf(0x04, 0x00))
    }

    @Test
    fun byOrdinalPerVariantOverride() {
        validateEncoding(ByOrdinalPerVariantOverrideFixturesPacketData(OverrideOrdinal.FIRST), byteArrayOf(0x01))
        validateEncoding(ByOrdinalPerVariantOverrideFixturesPacketData(OverrideOrdinal.SECOND), byteArrayOf(0x02))
        validateEncoding(ByOrdinalPerVariantOverrideFixturesPacketData(OverrideOrdinal.THIRD), byteArrayOf(0x04))
    }

    @Test
    fun byOrdinalInDataClass() {
        validateEncoding(OrdinalFieldPacketData(0x0F, TriStateOrdinal.THIRD), byteArrayOf(0x0F, 0x02))
    }

    @Test
    fun encodeObject() {
        validateEncoding(ObjectData, byteArrayOf(0x22, 0x44, 0x33))
    }

    @Test
    fun multiByteEnumDiscriminator() {
        validateEncoding(MultiByteEnumDiscriminatorFixturesFrameData(OpCode.CONNECT), byteArrayOf(0x00, 0x01))
        validateEncoding(MultiByteEnumDiscriminatorFixturesFrameData(OpCode.DISCONNECT), byteArrayOf(0x01, 0x00))
    }

    @Test
    fun encodeHeartRate() {
        validateEncoding(
            EncodeHeartRateFixturesHeartRateData(
                heartRate = 85,
                contactSupported = true,
                contactDetected = false,
                energyExpended = null,
                rrIntervals = emptyList(),
            ),
            byteArrayOf(0x04, 0x55),
        )
        validateEncoding(
            EncodeHeartRateFixturesHeartRateData(
                heartRate = 300,
                contactSupported = true,
                contactDetected = false,
                energyExpended = 500,
                rrIntervals = listOf(RRInterval(1.seconds), RRInterval(0.5.seconds)),
            ),
            byteArrayOf(0x1D, 0x2C, 0x01, 0xF4.toByte(), 0x01, 0x00, 0x04, 0x00, 0x02),
        )
    }

    @Test
    fun encodeSealed() {
        validateEncoding(SomeSealedClassAData(4), SomeSealedClassData.serializer(), byteArrayOf(0x11, 0x55, 0x01, 0x04, 0x00, 0x00, 0x00, 0x66, 0xAA.toByte()))
        validateEncoding(SomeSealedClassBData(600.0), SomeSealedClassData.serializer(), byteArrayOf(0x11, 0x55, 0x02, 0x06, 0x00, 0x00, 0x00, 0x66, 0xAA.toByte()))
    }

    @Test
    fun encodeUnmarkedSealed() {
        // No @SerializedByteValue: each option is identified by its subclass serialName as a
        // raw UTF-8 string (no length prefix), followed by the subclass body.
        val oneName = UnmarkedSealedOneData.serializer().descriptor.serialName
            .encodeToByteArray()
        val threeName = UnmarkedSealedThreeData.serializer().descriptor.serialName
            .encodeToByteArray()

        validateEncoding(
            UnmarkedSealedContainerData(UnmarkedSealedOneData(4)),
            UnmarkedSealedContainerData.serializer(),
            buildByteArray {
                add(oneName)
                add(4)
            },
        )

        validateEncoding(
            UnmarkedSealedContainerData(UnmarkedSealedThreeData(1, 2, 3)),
            UnmarkedSealedContainerData.serializer(),
            buildByteArray {
                add(threeName)
                add(byte = 1)
                add(byte = 2)
                add(byte = 3)
            },
        )
    }

    @Test
    fun multiByteSealedDiscriminator() {
        val backend = createBackend(
            BackendOptions(
                serializersModule = SerializersModule {
                    polymorphic(TwoByteCmdData::class) {
                        subclass(CmdAData::class)
                        subclass(CmdBData::class)
                    }
                },
            ),
        )

        validateRoundTrip(
            CmdAData(0x42),
            TwoByteCmdData.serializer(),
            byteArrayOf(0x01, 0x00, 0x42),
            backend,
        )
        validateRoundTrip(
            CmdBData(0x0300),
            TwoByteCmdData.serializer(),
            byteArrayOf(0x02, 0x00, 0x00, 0x03),
            backend,
        )
    }

    @Test
    fun classNameDiscriminatorsSkipLengthValidation() {
        // Class-name discriminators naturally have different byte lengths and must NOT trigger
        // the length-consistency check (the decoder uses forward peek, not fixed-width reads).
        // UnmarkedSealedData already exercises this: One has a shorter name than Three.
        val container = UnmarkedSealedContainerData(UnmarkedSealedOneData(42))
        val bytes = createBackend().encode(UnmarkedSealedContainerData.serializer(), container)
        val decoded = createBackend().decode(UnmarkedSealedContainerData.serializer(), bytes)
        assertEquals(container, decoded)
    }

    @Test
    fun sizeTagDispatch_fourBytePayload() {
        // No type discriminator bytes - just the content; 4 bytes total.
        // validateEncoding also verifies Nested<T> round-trips correctly: the sealed is the only
        // field in Nested, so no sibling fires DataAfterUnconstrainedData, and Prefix/Postfix on
        // Nested still work because they are written via build(), not addAction().
        validateEncoding(
            FourBytePayloadData(0x01020304u) as SizeDispatchedPayloadData,
            BytesFormat.serializer<SizeDispatchedPayloadData>(),
            byteArrayOf(0x04, 0x03, 0x02, 0x01),
        )
    }

    @Test
    fun sizeTagDispatch_nineBytePayload() {
        validateEncoding(
            NineBytePayloadData(0x01020304u, 0x05060708u, 0x09u) as SizeDispatchedPayloadData,
            BytesFormat.serializer<SizeDispatchedPayloadData>(),
            byteArrayOf(0x04, 0x03, 0x02, 0x01, 0x08, 0x07, 0x06, 0x05, 0x09),
        )
    }

    @Test
    fun sizePolymorphic_fixedSubtypeMatchedBeforeFallback() {
        // 4-byte payload -> FixedMixedData; variable remainder -> VariableMixedData (catch-all).
        validateEncoding(
            FixedMixedData(0x01020304u) as MixedSizeSealedData,
            BytesFormat.serializer<MixedSizeSealedData>(),
            byteArrayOf(0x04, 0x03, 0x02, 0x01),
        )
    }

    @Test
    fun sizePolymorphic_fallbackDecodesVariablePayload() {
        // 3 bytes don't match FixedMixedData(4) -> falls through to VariableMixedData.
        val bytes = byteArrayOf(0x0A, 0x0B, 0x0C)
        val result = createBackend().decode(BytesFormat.serializer<MixedSizeSealedData>(), bytes)
        assertEquals(VariableMixedData(listOf(0x0A.toUByte(), 0x0B.toUByte(), 0x0C.toUByte())), result)
    }

    @Test
    fun sizeTagDispatch_throwsOnUnrecognisedSize() {
        // 6 bytes matches neither 4 nor 9; a plain SerializationException, not one of its subclasses for truncated input.
        val failure = assertFailsWith<SerializationException> {
            createBackend().decode(BytesFormat.serializer<SizeDispatchedPayloadData>(), ByteArray(6))
        }
        assertEquals(SerializationException::class, failure::class)
        assertTrue("No @SizePolymorphic subtype registered for size 6" in failure.message.orEmpty(), failure.message)
    }

    @Test
    fun sizeTagDispatch_allowsPrefixPostfixOnContainingClass() {
        // Prefix/Postfix on the containing class are written in build(), not addAction(), so they
        // are unaffected by the makeUnconstrained() that @SizePolymorphic sets on the parent builder.
        val value = WrappedWithPrefixPostfixData(FourBytePayloadData(0x01020304u))
        val bytes = createBackend().encode(BytesFormat.serializer<WrappedWithPrefixPostfixData>(), value)
        assertTrue(bytes.contentEquals(byteArrayOf(0xAA.toByte(), 0x04, 0x03, 0x02, 0x01, 0xBB.toByte())), bytes.toHexString(separator = " "))
        assertEquals(value, createBackend().decode(BytesFormat.serializer<WrappedWithPrefixPostfixData>(), bytes))
    }

    @Test
    fun flagPackedEnumInSizePolymorphicSubtypeDispatchesBySize() {
        validateRoundTrip(
            FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixturesFlagEnumSizeHolderData(
                FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixturesFlagEnumPackedData(
                    mode = FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixtures.PackedMode.HIGH,
                    x = 300,
                ),
            ),
            FlagPackedEnumInSizePolymorphicSubtypeDispatchesBySizeFixturesFlagEnumSizeHolderData.serializer(),
        )
    }

    @Test
    fun charFieldGivesSizePolymorphicSubtypesDistinctFixedSizes() {
        // A Char's wire width is fixed by its encoding, so both subtypes are fixed-size (1 and 2 bytes).
        validateRoundTrip(
            CharFieldGivesSizePolymorphicSubtypesDistinctFixedSizesFixturesCharSizeHolderData(
                CharFieldGivesSizePolymorphicSubtypesDistinctFixedSizesFixturesCharSizeSmallData('A'),
            ),
            CharFieldGivesSizePolymorphicSubtypesDistinctFixedSizesFixturesCharSizeHolderData.serializer(),
            byteArrayOf(0x41),
        )
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
            SizePolymorphicPayloadWithChecksumRoundTripsFixturesSizePolymorphicWithChecksumData(FourBytePayloadData(0x01020304u)),
            SizePolymorphicPayloadWithChecksumRoundTripsFixturesSizePolymorphicWithChecksumData.serializer(),
            expectedBytes,
        )
    }
}

class BytesFormatEnumsPolymorphismTest : EnumsPolymorphismSuite(BytesFormatBackend.Factory) {

    @Test
    fun encodeHeartRateRoot() {
        // A root list is no layout, so only BytesFormat encodes one.
        validateEncoding(
            listOf(
                EncodeHeartRateFixturesHeartRateData(50, contactSupported = true, contactDetected = true, energyExpended = null, rrIntervals = emptyList()),
                EncodeHeartRateFixturesHeartRateData(
                    500,
                    contactSupported = true,
                    contactDetected = false,
                    energyExpended = null,
                    rrIntervals = listOf(RRInterval(2.seconds), RRInterval(0.25.seconds)),
                ),
            ),
            ListSerializer(EncodeHeartRateFixturesHeartRateData.serializer()),
            byteArrayOf(0x02, 0x06, 0x32, 0x15, 0xF4.toByte(), 0x01, 0x00, 0x08, 0x00, 0x01),
        )
    }

    @Test
    fun encodeEnum() {
        validateEncoding(SomeEnum.A, SomeEnum.serializer(), byteArrayOf(0x01))
    }

    // ── Multi-byte @SerializedByteValue ───────────────────────────────────────

    // ── @SizePolymorphic dispatch ─────────────────────────────────────────────
    // The sealed class declares the dispatch mode; subtypes carry no size annotation.
    // Sizes are computed automatically from field annotations at descriptor construction time.
}

class ViewEnumsPolymorphismTest : EnumsPolymorphismSuite(ViewBackend.Factory)
