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

package com.splendo.kaluga.bytes.ksp

import com.tschuchort.compiletesting.KotlinCompilation
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * The configuration errors `BytesFormat` reports the first time it encodes a type (see `BytesFormatDescriptorErrorTest`
 * in bytes/serialization) are KSP errors on the offending `@BytesLayout`, whether it generates DATA or only VIEW.
 */
@OptIn(ExperimentalCompilerApi::class)
class LayoutErrorTest {

    @Test
    fun `rejects inconsistent discriminator lengths`() = assertLayoutError(
        "test.BadCmd",
        "must have the same byte length",
        """
        @BytesLayout
        sealed interface BadCmd

        @BytesLayout
        @SerializedByteValue(0x01)
        interface Good : BadCmd {
            @Size(Length.`8_BIT`) @Unsigned val x: Int
        }

        @BytesLayout
        @SerializedByteValue(0x02, 0x00)
        interface AlsoBad : BadCmd {
            @Size(Length.`8_BIT`) @Unsigned val x: Int
        }
        """,
    )

    @Test
    fun `rejects size polymorphic subtypes with the same size`() = assertLayoutError(
        "test.AmbiguousSizeSealed",
        "is ambiguous",
        """
        @BytesLayout
        @SizePolymorphic
        sealed interface AmbiguousSizeSealed

        @BytesLayout
        interface AmbiguousTypeA : AmbiguousSizeSealed {
            @Unsigned val value: UInt
        }

        @BytesLayout
        interface AmbiguousTypeB : AmbiguousSizeSealed {
            @Unsigned val value: UInt
        }
        """,
    )

    @Test
    fun `rejects several variable size subtypes of a size polymorphic layout`() = assertLayoutError(
        "test.VariableSizeSealed",
        "more than one variable-size subtype",
        """
        @BytesLayout
        @SizePolymorphic
        sealed interface VariableSizeSealed

        @BytesLayout
        interface VariableSizeSubtypeA : VariableSizeSealed {
            @NullTerminated val name: String
        }

        @BytesLayout
        interface VariableSizeSubtypeB : VariableSizeSealed {
            @Unsized val data: List<UByte>
        }
        """,
    )

    @Test
    fun `rejects data after a size polymorphic property`() = assertLayoutError(
        "test.WithSiblingAfter",
        "'trailing' follows 'payload'",
        """
        @BytesLayout
        @SizePolymorphic
        sealed interface SizeDispatchedPayload

        @BytesLayout
        interface FourBytePayload : SizeDispatchedPayload {
            @Unsigned val value: UInt
        }

        @BytesLayout
        interface NineBytePayload : SizeDispatchedPayload {
            @Unsigned val a: UInt
            @Unsigned val b: UInt
            val c: UByte
        }

        @BytesLayout
        interface WithSiblingAfter {
            val payload: SizeDispatchedPayload
            val trailing: UByte
        }
        """,
    )

    @Test
    fun `rejects an enum with a checksum`() = assertLayoutError(
        "test.Container",
        "cannot use @Checksum",
        """
        @Serializable
        @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
        enum class ChecksumEnum { A, B }

        @BytesLayout
        interface Container {
            val value: ChecksumEnum
        }
        """,
    )

    @Test
    fun `rejects a flag packed enum with a prefix`() = assertLayoutError(
        "test.Container",
        "also has @Prefix",
        """
        @Serializable
        @Prefix([0x01])
        enum class FlagEnum { A, B }

        @BytesLayout
        interface Container {
            @FlagIndex(0) val value: FlagEnum
        }
        """,
    )

    @Test
    fun `rejects a flag packed enum with a postfix`() = assertLayoutError(
        "test.Container",
        "also has @Postfix",
        """
        @Serializable
        @Postfix([0x01])
        enum class FlagEnum { A, B }

        @BytesLayout
        interface Container {
            @FlagIndex(0) val value: FlagEnum
        }
        """,
    )

    @Test
    fun `rejects byte stuffing on a most significant first frame`() = assertLayoutError(
        "test.BigEndianStuffed",
        "byte stuffing is only supported for a frame accumulated LEAST_SIGNIFICANT_FIRST",
        """
        @BytesLayout
        @ByteStuffed
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
        interface BigEndianStuffed {
            @Size(Length.`8_BIT`) @Unsigned val value: Int
        }
        """,
    )

    @Test
    fun `rejects a terminal on a delimiter scheme`() = assertLayoutError(
        "test.ConflictingTerminalHolder",
        "has a fixed delimiter; @Terminal is not supported alongside it",
        """
        @BytesLayout
        interface ConflictingTerminalHolder {
            @Encoded(Encoding.ASCII)
            @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
            @Terminal(0x0A)
            val name: String
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int
        }
        """,
    )

    @Test
    fun `rejects a terminal on a COBS stuffed collection`() = assertLayoutError(
        "test.CobsTerminalHolder",
        "has a fixed delimiter; @Terminal is not supported alongside it",
        """
        @BytesLayout
        interface CobsTerminalHolder {
            @ByteStuffedCobs
            @Terminal(0x7E)
            val values: List<Byte>
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int
        }
        """,
    )

    @Test
    fun `rejects a CSafe stuffed string without a terminal`() = assertLayoutError(
        "test.CSafeStringWithoutTerminalHolder",
        "must be provided a terminal via @Terminal",
        """
        @BytesLayout
        interface CSafeStringWithoutTerminalHolder {
            @Encoded(Encoding.ASCII)
            @ByteStuffed
            val name: String
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int
        }
        """,
    )

    @Test
    fun `rejects data after an unterminated stuffed layout`() = assertLayoutError(
        "test.FieldAfterUnsizedStuffedChild",
        "'trailer' follows 'child'",
        """
        @BytesLayout
        interface StuffChild {
            @Size(Length.`8_BIT`) @Unsigned val a: Int
            @Size(Length.`8_BIT`) @Unsigned val b: Int
        }

        @BytesLayout
        interface FieldAfterUnsizedStuffedChild {
            @ByteStuffed val child: StuffChild
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int
        }
        """,
    )

    // The shared suite keeps these configurations as kotlinx classes because a layout rejects them.

    @Test
    fun `rejects a byte order change on a nested string`() = assertLayoutError(
        "test.InvalidStringByteOrder",
        "Nested string",
        """
        @BytesLayout
        interface InvalidStringByteOrder {
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val text: String
        }
        """,
    )

    @Test
    fun `rejects a byte after an unsized string`() = assertLayoutError(
        "test.ContentAfterUnsized",
        "'otherContent' follows 'string'",
        """
        @BytesLayout
        interface ContentAfterUnsized {
            @Unsized val string: String
            val otherContent: Byte
        }
        """,
    )

    @Test
    fun `rejects data after an unsized string`() = assertLayoutError(
        "test.BooleanAfterUnsized",
        "'flag' follows 'string'",
        """
        @BytesLayout
        interface BooleanAfterUnsized {
            @Unsized val string: String
            val flag: Boolean
        }
        """,
    )

    @Test
    fun `rejects data after an unsized collection`() = assertLayoutError(
        "test.ContentAfterUnsized",
        "'otherContent' follows 'map'",
        """
        @BytesLayout
        interface ContentAfterUnsized {
            @Unsized val map: Map<Byte, Byte>
            val otherContent: Byte
        }
        """,
    )

    @Test
    fun `accepts data after an unsized collection that may be absent`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface OptionalUnsized {
                    @Unsized @NullIfEmpty val items: List<Byte>
                    val trailing: Byte
                }
                """,
            ),
        )
        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
    }

    // Declaration errors.

    @Test
    fun `rejects a generic layout`() = assertLayoutError(
        "test.Wrapper",
        "cannot be generic; declare a concrete sub-interface instead",
        """
        @BytesLayout
        interface Wrapper<T> {
            val value: T
        }
        """,
    )

    @Test
    fun `rejects an open polymorphic property`() = assertLayoutError(
        "test.Holder",
        "open polymorphism is not supported",
        """
        interface Anything

        @BytesLayout
        interface Holder {
            val value: Anything
        }
        """,
    )

    @Test
    fun `rejects an enum without a serializer`() = assertLayoutError(
        "test.Holder",
        "test.Plain must be @Serializable",
        """
        enum class Plain { A }

        @BytesLayout
        interface Holder {
            val value: Plain
        }
        """,
    )

    @Test
    fun `rejects a sealed subtype that is not a layout`() = assertLayoutError(
        "test.Other",
        "so it must be a @BytesLayout interface",
        """
        @BytesLayout
        sealed interface Root

        interface Other : Root
        """,
    )

    @Test
    fun `rejects a DATA layout using a layout without DATA`() = assertLayoutError(
        "test.Outer",
        "the @BytesLayout test.Inner it uses must generate DATA too",
        """
        @BytesLayout(generate = [Generate.VIEW])
        interface Inner {
            val value: Byte
        }

        @BytesLayout
        interface Outer {
            val inner: Inner
        }
        """,
        modes = listOf("DATA"),
    )

    @Test
    fun `rejects a VIEW layout using a layout without VIEW`() = assertLayoutError(
        "test.Outer",
        "the @BytesLayout test.Inner it uses must generate VIEW too",
        """
        @BytesLayout(generate = [Generate.DATA])
        interface Inner {
            val value: Byte
        }

        @BytesLayout(generate = [Generate.DATA, Generate.VIEW])
        interface Outer {
            val inner: Inner
        }
        """,
        modes = listOf("DATA,VIEW"),
    )

    @Test
    fun `rejects a UTF-8 char`() = assertLayoutError(
        "test.Utf8Char",
        "cannot be UTF-8",
        """
        @BytesLayout
        interface Utf8Char {
            @Encoded(Encoding.UTF_8) val value: Char
        }
        """,
    )

    private fun assertLayoutError(declaration: String, expected: String, source: String, modes: List<String> = listOf("DATA", "VIEW")) = modes.forEach { generate ->
        val compilation = BytesCompilation(layoutSource(source), generate = generate)
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilation.result.exitCode, "$generate: ${compilation.result.messages}")
        val errors = compilation.result.messages.lines().filter { it.startsWith("e: [ksp]") }
        assertTrue(errors.any { expected in it }, "$generate: expected a KSP error containing \"$expected\", got:\n${compilation.result.messages}")
        assertTrue(errors.any { "Layouts.kt" in it }, "$generate: expected the error to point at the source, got:\n${errors.joinToString("\n")}")
        assertTrue(
            errors.any {
                declaration in it || declaration.substringAfter('.') in it
            },
            "$generate: expected the error to name $declaration, got:\n${errors.joinToString("\n")}",
        )
    }
}
