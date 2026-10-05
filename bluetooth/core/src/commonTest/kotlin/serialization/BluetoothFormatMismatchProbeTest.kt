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

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.toHexString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

// ── Probe (a): @LengthPrefix / @NullTerminated String inside an excludeStructure MSB frame ────────────
@Serializable
@com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private data class ProbeMsbStringFrame(@LengthPrefix val prefixed: String, @NullTerminated val terminated: String)

// ── Probe (b): List / Map inside an excludeStructure MSB frame ────────────────────────────────────────
@Serializable
@com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private data class ProbeMsbCollectionFrame(val items: List<Byte>, val pairs: Map<Byte, Byte>)

// ── Probe (c): @Unsized / @SizePolymorphic payload combined with @Checksum ────────────────────────────
@Serializable
@Prefix([0x11])
@Postfix([0x22])
@Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
private data class ProbeUnsizedWithChecksum(@Unsized val payload: List<Byte>)

@Serializable
@SizePolymorphic
private sealed interface ProbeSizeDispatchedPayload

@Serializable
private data class ProbeFourBytePayload(@Unsigned val value: UInt) : ProbeSizeDispatchedPayload // 4 bytes

@Serializable
@Prefix([0x11])
@Postfix([0x22])
@Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
private data class ProbeSizePolymorphicWithChecksum(val payload: ProbeSizeDispatchedPayload)

// ── Probe (d): @PresentWhenAllSet referencing a bit that no field owns ────────────────────────────────
@Serializable
private data class ProbeUnownedGateContainer(@Size(Length.`8_BIT`) @FlagIndex(0) val a: Byte?, @Size(Length.`8_BIT`) @PresentWhenAllSet(10) val b: Byte?)

/**
 * Round-trip probes for encode/decode mismatches in [BluetoothFormat]. Each is written as the behaviour the
 * annotations and their KDoc describe: a failing probe marks a bug, and a fix should turn it green.
 */
class BluetoothFormatMismatchProbeTest {

    // Some of these probes throw partway through decode rather than producing a mismatched value; surfacing the
    // encoded hex either way keeps the failure message self-contained.
    private fun <T> decodeOrFail(serializer: KSerializer<T>, encoded: ByteArray): T = try {
        BluetoothFormat.decodeFromByteArray(serializer, encoded)
    } catch (e: Exception) {
        fail("decode threw ${e::class.simpleName}(\"${e.message}\") for encoded=${encoded.toHexString(separator = " ")}")
    }

    // Known bug: String length prefix and null terminator are written in a different order than the decoder reads them when the structure is MSB with excludeStructure.
    // Fails with: ByteArrayEndedBeforeSerializationCompleted "String size 5 does not match encoded size 66" for encoded=42 41 02 00 44 43.
    @Test
    fun msbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTrips() {
        val value = ProbeMsbStringFrame(prefixed = "AB", terminated = "CD")
        val encoded = BluetoothFormat.encodeToByteArray(ProbeMsbStringFrame.serializer(), value)
        val decoded = decodeOrFail(ProbeMsbStringFrame.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    // Known bug: List and Map items come back in reverse order in an MSB excludeStructure frame because the item blocks are accumulated and then re-wrapped in reverse.
    // Fails with: expected [1, 2, 3] but was [3, 2, 1], encoded=03 03 02 01 03 1e 03 14 02 0a 01.
    @Test
    fun msbExcludeStructureListAndMapPreserveItemOrder() {
        val value = ProbeMsbCollectionFrame(
            items = listOf(1, 2, 3),
            pairs = linkedMapOf(1.toByte() to 10.toByte(), 2.toByte() to 20.toByte(), 3.toByte() to 30.toByte()),
        )
        val encoded = BluetoothFormat.encodeToByteArray(ProbeMsbCollectionFrame.serializer(), value)
        val decoded = BluetoothFormat.decodeFromByteArray(ProbeMsbCollectionFrame.serializer(), encoded)
        assertEquals(value.items, decoded.items, "encoded=${encoded.toHexString(separator = " ")}")
        assertEquals(
            value.pairs.entries.map { it.key to it.value },
            decoded.pairs.entries.map { it.key to it.value },
            "encoded=${encoded.toHexString(separator = " ")}",
        )
    }

    // Known bug: @Unsized payload with @Checksum cannot decode because the remaining-payload size does not subtract the checksum width, so the list swallows the CRC.
    // Fails with: ByteArrayEndedBeforeSerializationCompleted "Not enough bytes remaining" for encoded=11 01 02 03 10 a1 22.
    @Test
    fun unsizedPayloadWithChecksumRoundTrips() {
        val value = ProbeUnsizedWithChecksum(payload = listOf(0x01, 0x02, 0x03))
        val encoded = BluetoothFormat.encodeToByteArray(ProbeUnsizedWithChecksum.serializer(), value)
        val decoded = decodeOrFail(ProbeUnsizedWithChecksum.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    // Known bug: @SizePolymorphic payload with @Checksum dispatches on the wrong size because the remaining-payload size does not subtract the checksum width.
    // Fails with: SerializationException "No @SizePolymorphic subtype registered for size 6 ... Expected one of [4]" for encoded=11 04 03 02 01 31 90 22.
    @Test
    fun sizePolymorphicPayloadWithChecksumRoundTrips() {
        val value = ProbeSizePolymorphicWithChecksum(ProbeFourBytePayload(0x01020304u))
        val encoded = BluetoothFormat.encodeToByteArray(ProbeSizePolymorphicWithChecksum.serializer(), value)
        val decoded = decodeOrFail(ProbeSizePolymorphicWithChecksum.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    // Known bug: A @PresentWhenAllSet index owned by no field makes the encoder write a 1-byte flag header while the decoder reads 2 bytes.
    // Fails with: IndexOutOfBoundsException "toIndex (3) is greater than size (2)." for encoded=01 05.
    @Test
    fun presentWhenAllSetReferencingUnownedBitRoundTrips() {
        val value = ProbeUnownedGateContainer(a = 5, b = null)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeUnownedGateContainer.serializer(), value)
        val decoded = decodeOrFail(ProbeUnownedGateContainer.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }
}
