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

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.annotations.ByteStuffed
import com.splendo.kaluga.bytes.annotations.ByteStuffedCobs
import com.splendo.kaluga.bytes.annotations.ByteStuffedXor
import com.splendo.kaluga.bytes.annotations.Encoded
import com.splendo.kaluga.bytes.annotations.Terminal
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.annotations.Unsized
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.test.Test
import kotlin.test.assertFailsWith
import com.splendo.kaluga.bytes.layout.UnsupportedByteStuffing

// Mixing 1-byte and 2-byte @SerializedByteValue on the same sealed class must fail at descriptor build time
@Serializable
private sealed interface BadCmd

@Serializable
@SerializedByteValue(0x01)
private data class Good(@Size(Length.`8_BIT`) @Unsigned val x: Int) : BadCmd

@Serializable
@SerializedByteValue(0x02, 0x00)
private data class AlsoBad(@Size(Length.`8_BIT`) @Unsigned val x: Int) : BadCmd

// Used in the ambiguous-size test - two subtypes with identical computed byte sizes.
@Serializable
@SizePolymorphic
private sealed interface AmbiguousSizeSealed

@Serializable
private data class AmbiguousTypeA(@Unsigned val value: UInt) : AmbiguousSizeSealed // 4 bytes

@Serializable
private data class AmbiguousTypeB(@Unsigned val value: UInt) : AmbiguousSizeSealed // also 4 bytes

// ── Failure: two variable-size subtypes ───────────────────────────────────

@Serializable
@SizePolymorphic
private sealed interface VariableSizeSealed

@Serializable
private data class VariableSizeSubtypeA(@NullTerminated val name: String) : VariableSizeSealed

@Serializable
private data class VariableSizeSubtypeB(@Unsized val data: List<UByte>) : VariableSizeSealed

@Serializable
@Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
private enum class ChecksumEnum { A, B }

@Serializable
@Postfix([0x01])
private enum class FlagEnum { A, B }

@Serializable
@ByteStuffed
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
private data class BigEndianStuffed(@Size(Length.`8_BIT`) @Unsigned val value: Int)

@Serializable
private data class WithSiblingAfter(val payload: SizeDispatchedPayload, val trailing: UByte)

/**
 * Tests pinning configuration errors that the descriptor registry rejects the first time a type is encoded or
 * decoded (a runtime [SerializationException] or [UnsupportedByteStuffing], built from inspecting the type's
 * [kotlinx.serialization.descriptors.SerialDescriptor]). These configurations are static - independent of any
 * particular value - so once the layout is declared on an annotated interface and read by KSP, the equivalent
 * check becomes a *compile* error instead. [Capability.RUNTIME_LAYOUT_VALIDATION] marks that difference
 * explicitly: a view backend rejects these at KSP time and never implements this capability, so it cannot run
 * this class either.
 */
class BytesFormatDescriptorErrorTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    init {
        check(Capability.RUNTIME_LAYOUT_VALIDATION in createBackend().capabilities) {
            "BytesFormatDescriptorErrorTest requires a backend with runtime layout validation"
        }
    }

    @Test
    fun inconsistentDiscriminatorLengthsThrows() {
        assertFailsWith<SerializationException> {
            BytesFormat {
                serializersModule = SerializersModule {
                    polymorphic(BadCmd::class) {
                        subclass(Good::class)
                        subclass(AlsoBad::class)
                    }
                }
            }.encodeToByteArray(BadCmd.serializer(), Good(1))
        }
    }

    @Test
    fun sizePolymorphic_failsWhenSubtypesHaveDuplicateSizes() {
        // AmbiguousTypeA and AmbiguousTypeB both compute to 4 bytes - dispatch would be impossible.
        assertFailsWith<SerializationException> {
            BytesFormat.encodeToByteArray(
                BytesFormat.serializer<AmbiguousSizeSealed>(),
                AmbiguousTypeA(0u),
            )
        }
    }

    @Test
    fun sizePolymorphic_failsWithMultipleVariableSizeSubtypes() {
        // Two variable-size subtypes - ambiguous, must be rejected at encode time.
        assertFailsWith<SerializationException> {
            BytesFormat.encodeToByteArray(
                BytesFormat.serializer<VariableSizeSealed>(),
                VariableSizeSubtypeA("hello"),
            )
        }
    }

    @Test
    fun sizeTagDispatch_throwsDataAfterUnconstrainedWhenSiblingFollows() {
        // A field AFTER the size-polymorphic sealed cannot have its length determined - encoding must fail.
        // Fields BEFORE the sealed (or the sealed as the last/only field) are fine.
        assertFailsWith<DataAfterUnconstrainedData> {
            BytesFormat.encodeToByteArray(
                BytesFormat.serializer<WithSiblingAfter>(),
                WithSiblingAfter(FourBytePayload(0u), 0u),
            )
        }
    }

    @Test
    fun enumWithChecksumThrowsAtRegistryTime() {
        @Serializable
        data class Container(val value: ChecksumEnum)

        assertFailsWith<SerializationException> {
            BytesFormat.encodeToByteArray(Container.serializer(), Container(ChecksumEnum.A))
        }
    }

    @Test
    fun flagPackedEnumWithPrefixThrowsAtRegistryTime() {
        @Serializable
        data class Container(@FlagIndex(0) val value: FlagEnum)

        assertFailsWith<SerializationException> {
            BytesFormat.encodeToByteArray(Container.serializer(), Container(FlagEnum.A))
        }
    }

    @Test
    fun flagPackedEnumWithPostfixThrowsAtRegistryTime() {
        @Serializable
        data class Container(@FlagIndex(0) val value: FlagEnum)

        assertFailsWith<SerializationException> {
            BytesFormat.encodeToByteArray(Container.serializer(), Container(FlagEnum.A))
        }
    }

    @Test
    fun byteStuffingRequiresLeastSignificantFirst() {
        // Full-reversal MSB (accumulation direction) is rejected; big-endian field values via excludeStructure are not.
        assertFailsWith<UnsupportedByteStuffing> {
            BytesFormat.encodeToByteArray(BigEndianStuffed.serializer(), BigEndianStuffed(0x01))
        }
    }

    private object DelimiterSchemeRejectsExplicitTerminalFixtures {
        // A delimiter scheme's terminator is fixed by the scheme, so an accompanying @Terminal is rejected.
        @Serializable
        data class ConflictingTerminalHolder(
            @Encoded(Encoding.ASCII)
            @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
            @Terminal(0x0A)
            val name: String,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )
    }

    @Test
    fun delimiterSchemeRejectsExplicitTerminal() {
        assertFailsWith<UnsupportedByteStuffing> {
            BytesFormat.encodeToByteArray(
                DelimiterSchemeRejectsExplicitTerminalFixtures.ConflictingTerminalHolder.serializer(),
                DelimiterSchemeRejectsExplicitTerminalFixtures.ConflictingTerminalHolder(name = "A", trailer = 0x09),
            )
        }
    }

    private object CobsRejectsExplicitTerminalFixtures {
        // COBS's 0x00 delimiter is definitional, so an accompanying @Terminal is rejected.
        @Serializable
        data class CobsTerminalHolder(
            @ByteStuffedCobs
            @Terminal(0x7E)
            val values: List<Byte>,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )
    }

    @Test
    fun cobsRejectsExplicitTerminal() {
        // COBS only ever eliminates 0x00, so 0x00 is the only byte that can unambiguously terminate it.
        assertFailsWith<UnsupportedByteStuffing> {
            BytesFormat.encodeToByteArray(
                CobsRejectsExplicitTerminalFixtures.CobsTerminalHolder.serializer(),
                CobsRejectsExplicitTerminalFixtures.CobsTerminalHolder(values = listOf(0x01), trailer = 0x09),
            )
        }
    }

    private object CSafeStringRequiresExplicitTerminalFixtures {
        @Serializable
        data class CSafeStringWithoutTerminalHolder(
            @Encoded(Encoding.ASCII)
            @ByteStuffed
            val name: String,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )
    }

    @Test
    fun cSafeStringRequiresExplicitTerminal() {
        // CSafe has no delimiter of its own, so a String using it must name a terminator it escapes.
        assertFailsWith<UnsupportedByteStuffing> {
            BytesFormat.encodeToByteArray(
                CSafeStringRequiresExplicitTerminalFixtures.CSafeStringWithoutTerminalHolder.serializer(),
                CSafeStringRequiresExplicitTerminalFixtures.CSafeStringWithoutTerminalHolder(name = "hi", trailer = 0x09),
            )
        }
    }

    private object FieldAfterUnsizedStuffedStructureFailsFixtures {
        @Serializable
        data class StuffChild(@Size(Length.`8_BIT`) @Unsigned val a: Int, @Size(Length.`8_BIT`) @Unsigned val b: Int)

        // A field after an unterminated nested stuffed structure has no boundary to decode against.
        @Serializable
        data class FieldAfterUnsizedStuffedChild(@ByteStuffed val child: StuffChild, @Size(Length.`8_BIT`) @Unsigned val trailer: Int)
    }

    @Test
    fun fieldAfterUnsizedStuffedStructureFails() {
        assertFailsWith<DataAfterUnconstrainedData> {
            BytesFormat.encodeToByteArray(
                FieldAfterUnsizedStuffedStructureFailsFixtures.FieldAfterUnsizedStuffedChild.serializer(),
                FieldAfterUnsizedStuffedStructureFailsFixtures.FieldAfterUnsizedStuffedChild(
                    child = FieldAfterUnsizedStuffedStructureFailsFixtures.StuffChild(a = 0x01, b = 0x02),
                    trailer = 0x09,
                ),
            )
        }
    }
}
