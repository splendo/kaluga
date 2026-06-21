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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

// Compiling the generated sources only proves they parse; it does not prove the plugin emitted the @Bluetooth device,
// its @BluetoothService interfaces and their @BluetoothCharacteristic members with the names and value types we expect.
// Implementing every generated interface here does: a missing, renamed or wrongly-typed member fails to compile, and the
// runtime walk reads a value through the whole device -> service -> characteristic -> value tree.
class GeneratedStructureTest {

    private object InternalTemperatureImpl : InternalTemperature {
        override val value = InternalTemperatureValue(InternalTemperatureValue.Temperature(20.0))
    }

    private object PressureImpl : Pressure {
        override val value = PressureValue(PressureValue.Pressure(1000.0))
    }

    private object WideCountersImpl : WideCounters {
        override val value = WideCountersValue(packetCounter = 1L, signedOffset = -1L)
    }

    private object ReadingImpl : Reading {
        override val value = ReadingValue(42)
    }

    private object ReadingPairImpl : ReadingPair {
        override val value = ReadingPairValue(first = ReadingValue(1), second = ReadingValue(2))
    }

    private object DualFlagsImpl : DualFlags {
        override val value = DualFlagsValue(valueA = 1, valueB = 2)
    }

    private object CompoundFlagsImpl : CompoundFlags {
        override val value = CompoundFlagsValue(valueA = 1, valueB = 2, combined = 3)
    }

    private object HeartRateMeasurementImpl : HeartRateMeasurement {
        override val value = HeartRateMeasurementValue(
            sensorContactStatus = HeartRateMeasurementValue.SensorContactStatus.SENSOR_CONTACT_FEATURE_IS_3,
            heartRateMeasurementValue = 70,
            energyExpended = null,
            rRInterval = null,
        )
    }

    private object BodySensorLocationImpl : BodySensorLocation {
        override val value = BodySensorLocationValue(BodySensorLocationValue.BodySensorLocation.CHEST)
    }

    private object HeartRateControlPointImpl : HeartRateControlPoint {
        override val value = HeartRateControlPointValue(HeartRateControlPointValue.HeartRateControlPoint.RESET_ENERGY_EXPENDED_RESETS)
    }

    private object ThermometerServiceImpl : ThermometerService {
        override val internalTemperature = InternalTemperatureImpl
        override val pressure = PressureImpl
        override val wideCounters = WideCountersImpl
        override val reading = ReadingImpl
        override val readingPair = ReadingPairImpl
        override val dualFlags = DualFlagsImpl
        override val compoundFlags = CompoundFlagsImpl
    }

    private object HeartRateImpl : HeartRate {
        override val heartRateMeasurement = HeartRateMeasurementImpl
        override val bodySensorLocation = BodySensorLocationImpl
        override val heartRateControlPoint = HeartRateControlPointImpl
    }

    private object ThermometerImpl : Thermometer {
        override val thermometerService = ThermometerServiceImpl
        override val heartRate = HeartRateImpl
    }

    @Test
    fun deviceExposesEveryServiceAndCharacteristic() {
        val device: Thermometer = ThermometerImpl

        // device -> services
        assertNotNull(device.thermometerService)
        assertNotNull(device.heartRate)

        // service -> characteristic -> value, read through the whole tree
        assertEquals(42, device.thermometerService.reading.value.reading)
        assertEquals(20.0, device.thermometerService.internalTemperature.value.temperature.value)
        assertEquals(1L, device.thermometerService.wideCounters.value.packetCounter)
        assertEquals(1, device.thermometerService.dualFlags.value.valueA)
        assertEquals(2, device.thermometerService.readingPair.value.second.reading)

        assertEquals(70, device.heartRate.heartRateMeasurement.value.heartRateMeasurementValue)
        assertEquals(
            HeartRateMeasurementValue.SensorContactStatus.SENSOR_CONTACT_FEATURE_IS_3,
            device.heartRate.heartRateMeasurement.value.sensorContactStatus,
        )
        assertEquals(
            BodySensorLocationValue.BodySensorLocation.CHEST,
            device.heartRate.bodySensorLocation.value.bodySensorLocation,
        )
    }
}
