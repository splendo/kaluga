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

package com.splendo.kaluga.bluetooth.generated

import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.scientific.unit.Celsius
import com.splendo.kaluga.scientific.unit.Pascal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Validates that the XML-generated definitions don't just compile but serialize to the bytes a GATT peer expects:
// scalar inversion (Temperature), a folded multiplier (Pressure) and the Heart Rate Measurement flags structure
// (a width-selecting/optional/repeated flags byte, an enum in the flags, and a scaled repeated value).
class GeneratedSerializationTest {

    @Test
    fun temperatureRoundTripsThroughScaledBytes() {
        // 23.45 °C -> raw 2345 (0x0929), little-endian. A verbatim copy of the GATT exponent (-2) would instead
        // encode round(23.45 * 10^-2) = 0, so these bytes pin down the scaling direction.
        validateTemperature(23.45, byteArrayOf(0x29, 0x09))
        // negative value exercises the sint16 sign: -12.5 °C -> raw -1250 (0xFB1E)
        validateTemperature(-12.5, byteArrayOf(0x1E, 0xFB.toByte()))
    }

    @Test
    fun pressureRoundTripsThroughFoldedMultiplier() {
        // The Pressure field has a GATT Multiplier of 10 (physical = raw * 10), which has no integer reciprocal, so the
        // generator folds it into @Scalar(decimalExponent = -1): 5000 Pa encodes as raw 500 (0x01F4), little-endian.
        val value = PressureValue(PressureValue.Pressure(5000.0))
        val bytes = BluetoothFormat.encodeToByteArray(PressureValue.serializer(), value)
        assertTrue(
            bytes.contentEquals(byteArrayOf(0xF4.toByte(), 0x01)),
            "Expected f4 01 but got ${bytes.hex()}",
        )
        val decoded = BluetoothFormat.decodeFromByteArray(PressureValue.serializer(), bytes)
        assertEquals(value, decoded)
        assertEquals(5000.0, decoded.pressure.value)
        assertEquals(Pascal, decoded.pressure.unit)
    }

    // The Heart Rate service (180D) and its characteristics are the unmodified SIG XML (oesmith), generated as a
    // standalone service alongside the Thermometer. These round-trips exercise its notify/read/write characteristics.

    @Test
    fun heartRateMeasurementMinimalRoundTrips() {
        // 8-bit rate, sensor contact detected (ordinal 3 -> bits 1-2 = 0b0110), no energy, no RR interval.
        val value = HeartRateMeasurementValue(
            sensorContactStatus = HeartRateMeasurementValue.SensorContactStatus.SENSOR_CONTACT_FEATURE_IS_3,
            heartRateMeasurementValue = HeartRateMeasurementValue.Value(70),
            energyExpended = null,
            rRInterval = emptyList(),
        )
        validateHeartRate(value, byteArrayOf(0x06, 0x46))
    }

    @Test
    fun heartRateMeasurementFullRoundTrips() {
        // 16-bit rate (bit 0), contact "supported, not detected" (ordinal 2 -> 0b0100), energy (bit 3) and RR (bit 4)
        // present: flags 0x1D. Body: rate 300, energy 500 J, RR 512 (raw 1/1024 s units, no machine-readable scaling).
        val value = HeartRateMeasurementValue(
            sensorContactStatus = HeartRateMeasurementValue.SensorContactStatus.SENSOR_CONTACT_FEATURE_IS_2,
            heartRateMeasurementValue = HeartRateMeasurementValue.Value(300),
            energyExpended = HeartRateMeasurementValue.EnergyExpended(500),
            rRInterval = listOf(HeartRateMeasurementValue.RRInterval(512)),
        )
        validateHeartRate(value, byteArrayOf(0x1D, 0x2C, 0x01, 0xF4.toByte(), 0x01, 0x00, 0x02))
    }

    @Test
    fun bodySensorLocationEnumRoundTrips() {
        // 8-bit field with top-level <Enumerations> -> a byte-valued enum (read-only characteristic).
        validateRoundTrip(
            BodySensorLocationValue.serializer(),
            BodySensorLocationValue(BodySensorLocationValue.BodySensorLocation.CHEST),
            byteArrayOf(0x01),
        )
        validateRoundTrip(
            BodySensorLocationValue.serializer(),
            BodySensorLocationValue(BodySensorLocationValue.BodySensorLocation.WRIST),
            byteArrayOf(0x02),
        )
    }

    @Test
    fun heartRateControlPointEnumRoundTrips() {
        // write-only enum characteristic; its single defined value is 1.
        validateRoundTrip(
            HeartRateControlPointValue.serializer(),
            HeartRateControlPointValue(HeartRateControlPointValue.HeartRateControlPoint.RESET_ENERGY_EXPENDED_RESETS),
            byteArrayOf(0x01),
        )
    }

    @Test
    fun wideCountersRoundTripThrough40And48BitWidths() {
        // uint40 -> 5 bytes, sint48 -> 6 bytes, both little-endian. 0x123456789A and -42 (sign-extended across 6 bytes).
        val value = WideCountersValue(packetCounter = 0x123456789AL, signedOffset = -42L)
        val bytes = BluetoothFormat.encodeToByteArray(WideCountersValue.serializer(), value)
        val expected = byteArrayOf(
            0x9A.toByte(), 0x78, 0x56, 0x34, 0x12,
            0xD6.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
        )
        assertTrue(bytes.contentEquals(expected), "Expected ${expected.hex()} but got ${bytes.hex()}")
        val decoded = BluetoothFormat.decodeFromByteArray(WideCountersValue.serializer(), bytes)
        assertEquals(value, decoded)
        assertEquals(0x123456789AL, decoded.packetCounter)
        assertEquals(-42L, decoded.signedOffset)
    }

    @Test
    fun referenceFieldsEmbedTheReferencedValue() {
        // Reading Pair's two fields each reference the Reading characteristic (2BE0), so they embed ReadingValue.
        // Each Reading is a uint16; 100 -> 0x0064, 200 -> 0x00C8, both little-endian and concatenated.
        val value = ReadingPairValue(first = ReadingValue(100), second = ReadingValue(200))
        val bytes = BluetoothFormat.encodeToByteArray(ReadingPairValue.serializer(), value)
        assertTrue(
            bytes.contentEquals(byteArrayOf(0x64, 0x00, 0xC8.toByte(), 0x00)),
            "Expected 64 00 c8 00 but got ${bytes.hex()}",
        )
        assertEquals(value, BluetoothFormat.decodeFromByteArray(ReadingPairValue.serializer(), bytes))
    }

    @Test
    fun dualFlagFieldsSpanA16BitFlagsRegion() {
        // Two Flags bytes: Value A's presence bit is index 0, Value B's is index 8 (second flags byte). The flags
        // region is therefore 2 bytes, followed by whichever values are present.
        validateDualFlags(DualFlagsValue(valueA = 10, valueB = 300), byteArrayOf(0x01, 0x01, 0x0A, 0x2C, 0x01))
        validateDualFlags(DualFlagsValue(valueA = 7, valueB = null), byteArrayOf(0x01, 0x00, 0x07))
        validateDualFlags(DualFlagsValue(valueA = null, valueB = 500), byteArrayOf(0x00, 0x01, 0xF4.toByte(), 0x01))
        validateDualFlags(DualFlagsValue(valueA = null, valueB = null), byteArrayOf(0x00, 0x00))
    }

    @Test
    fun compoundRequirementGatesOnAllConditions() {
        // "Combined" requires both C1 (bit 0) and C2 (bit 1): it is present only when Value A and Value B both are.
        validateCompoundFlags(CompoundFlagsValue(valueA = 10, valueB = 20, combined = 30), byteArrayOf(0x03, 0x0A, 0x14, 0x1E))
        // only A present -> bit 1 clear -> Combined necessarily absent
        validateCompoundFlags(CompoundFlagsValue(valueA = 10, valueB = null, combined = null), byteArrayOf(0x01, 0x0A))
        validateCompoundFlags(CompoundFlagsValue(valueA = null, valueB = null, combined = null), byteArrayOf(0x00))
    }

    private fun validateCompoundFlags(value: CompoundFlagsValue, expectedBytes: ByteArray) {
        val bytes = BluetoothFormat.encodeToByteArray(CompoundFlagsValue.serializer(), value)
        assertTrue(bytes.contentEquals(expectedBytes), "Expected ${expectedBytes.hex()} but got ${bytes.hex()}")
        assertEquals(value, BluetoothFormat.decodeFromByteArray(CompoundFlagsValue.serializer(), bytes))
    }

    private fun validateDualFlags(value: DualFlagsValue, expectedBytes: ByteArray) {
        val bytes = BluetoothFormat.encodeToByteArray(DualFlagsValue.serializer(), value)
        assertTrue(bytes.contentEquals(expectedBytes), "Expected ${expectedBytes.hex()} but got ${bytes.hex()}")
        assertEquals(value, BluetoothFormat.decodeFromByteArray(DualFlagsValue.serializer(), bytes))
    }

    private fun validateHeartRate(value: HeartRateMeasurementValue, expectedBytes: ByteArray) {
        val bytes = BluetoothFormat.encodeToByteArray(HeartRateMeasurementValue.serializer(), value)
        assertTrue(
            bytes.contentEquals(expectedBytes),
            "Expected ${expectedBytes.hex()} but got ${bytes.hex()}",
        )
        assertEquals(value, BluetoothFormat.decodeFromByteArray(HeartRateMeasurementValue.serializer(), bytes))
    }

    private fun validateTemperature(celsius: Double, expectedBytes: ByteArray) {
        val value = InternalTemperatureValue(InternalTemperatureValue.Temperature(celsius))
        val bytes = BluetoothFormat.encodeToByteArray(InternalTemperatureValue.serializer(), value)
        assertTrue(
            bytes.contentEquals(expectedBytes),
            "Expected ${expectedBytes.hex()} but got ${bytes.hex()}",
        )
        val decoded = BluetoothFormat.decodeFromByteArray(InternalTemperatureValue.serializer(), bytes)
        assertEquals(value, decoded)
        assertEquals(celsius, decoded.temperature.value)
        assertEquals(Celsius, decoded.temperature.unit)
    }

    private fun <T> validateRoundTrip(serializer: kotlinx.serialization.KSerializer<T>, value: T, expectedBytes: ByteArray) {
        val bytes = BluetoothFormat.encodeToByteArray(serializer, value)
        assertTrue(bytes.contentEquals(expectedBytes), "Expected ${expectedBytes.hex()} but got ${bytes.hex()}")
        assertEquals(value, BluetoothFormat.decodeFromByteArray(serializer, bytes))
    }

    private fun ByteArray.hex() = joinToString(" ") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }
}
