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

    @Test
    fun msbExcludeStructureLengthPrefixAndNullTerminatedStringRoundTrips() {
        val value = ProbeMsbStringFrame(prefixed = "AB", terminated = "CD")
        val encoded = BluetoothFormat.encodeToByteArray(ProbeMsbStringFrame.serializer(), value)
        val decoded = decodeOrFail(ProbeMsbStringFrame.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

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

    @Test
    fun unsizedPayloadWithChecksumRoundTrips() {
        val value = ProbeUnsizedWithChecksum(payload = listOf(0x01, 0x02, 0x03))
        val encoded = BluetoothFormat.encodeToByteArray(ProbeUnsizedWithChecksum.serializer(), value)
        val decoded = decodeOrFail(ProbeUnsizedWithChecksum.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun sizePolymorphicPayloadWithChecksumRoundTrips() {
        val value = ProbeSizePolymorphicWithChecksum(ProbeFourBytePayload(0x01020304u))
        val encoded = BluetoothFormat.encodeToByteArray(ProbeSizePolymorphicWithChecksum.serializer(), value)
        val decoded = decodeOrFail(ProbeSizePolymorphicWithChecksum.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun presentWhenAllSetReferencingUnownedBitRoundTrips() {
        val value = ProbeUnownedGateContainer(a = 5, b = null)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeUnownedGateContainer.serializer(), value)
        val decoded = decodeOrFail(ProbeUnownedGateContainer.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }
}
