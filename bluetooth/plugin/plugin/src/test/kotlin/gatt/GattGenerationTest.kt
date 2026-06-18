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

package com.splendo.kaluga.bluetooth.plugin.gatt

import com.squareup.kotlinpoet.KModifier
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GattGenerationTest {

    private fun resourceFile(path: String) = File(checkNotNull(javaClass.getResource(path)) { "missing $path" }.toURI())

    @Test
    fun generatesAndLinksDefinitionsFromMixedXmlFiles() {
        val types = GattGeneration.generate(
            listOf(resourceFile("/gatt/environmental_sample.xml"), resourceFile("/gatt/environmental_sensing_service.xml")),
            deviceName = "Environmental Sensor",
            packageName = "com.example.generated",
        ).types()

        assertNotNull(types.getValue("EnvironmentalSensor").annotation("Bluetooth"))
        // the service (one file) links the characteristic (the other file) by its interface type
        val service = types.getValue("EnvironmentalSensingSample")
        assertEquals("\"181A\"", checkNotNull(service.annotation("BluetoothService")).argument)
        assertEquals("EnvironmentalSample", checkNotNull(service.property("environmentalSample")).type.simpleName)
        // the linked characteristic and its value class both exist
        assertEquals("\"2BCE\"", checkNotNull(types.getValue("EnvironmentalSample").annotation("BluetoothCharacteristic")).argument)
        assertEquals("EnvironmentalSampleValue", checkNotNull(types.getValue("EnvironmentalSample").property("value")).type.simpleName)
        assertTrue(KModifier.DATA in types.getValue("EnvironmentalSampleValue").modifiers)
    }

    @Test
    fun generatesAndLinksDefinitionsFromDeviceYaml() {
        val types = GattGeneration.generateFromYaml(resourceFile("/gatt/environmental_sensor.yaml"), packageName = "com.example.generated").types()

        // device name comes from the YAML; it exposes the service
        val device = types.getValue("EnvironmentalSensor")
        assertNotNull(device.annotation("Bluetooth"))
        assertEquals("EnvironmentalSensing", checkNotNull(device.property("environmentalSensing")).type.simpleName)

        // the service links both characteristics, applying each one's access
        val service = types.getValue("EnvironmentalSensing")
        assertEquals("\"181A\"", checkNotNull(service.annotation("BluetoothService")).argument)
        assertEquals("EnvironmentalSample", checkNotNull(service.property("environmentalSample")).type.simpleName)
        assertEquals("SensorReading", checkNotNull(service.property("sensorReading")).type.simpleName)
        assertEquals(setOf("Readable", "Notifiable"), checkNotNull(types.getValue("EnvironmentalSample").property("value")).annotationNames)

        // plain value class with scaling, and the conditional characteristic as a sealed value class
        assertTrue(KModifier.DATA in types.getValue("EnvironmentalSampleValue").modifiers)
        val sensorReadingValue = types.getValue("SensorReadingValue")
        assertTrue(KModifier.SEALED in sensorReadingValue.modifiers)
        assertEquals("value = 1", checkNotNull(checkNotNull(sensorReadingValue.nestedType("Temperature")).annotation("SerializedByteValue")).argument)
    }

    @Test
    fun resolvesStandardUuidsFromNamesInDeviceYaml() {
        val types = GattGeneration.generateFromYaml(resourceFile("/gatt/heart_rate_monitor.yaml"), packageName = "com.example.generated").types()

        assertNotNull(types.getValue("HeartRateMonitor").annotation("Bluetooth"))

        // service names resolve to UUIDs and still link their characteristics
        val heartRate = types.getValue("HeartRate")
        assertEquals("\"180D\"", checkNotNull(heartRate.annotation("BluetoothService")).argument)
        assertEquals("HeartRateMeasurement", checkNotNull(heartRate.property("heartRateMeasurement")).type.simpleName)
        assertEquals("\"180F\"", checkNotNull(types.getValue("Battery").annotation("BluetoothService")).argument)

        // characteristic names resolve to UUIDs
        assertEquals("\"2A37\"", checkNotNull(types.getValue("HeartRateMeasurement").annotation("BluetoothCharacteristic")).argument)
        assertEquals("\"2A38\"", checkNotNull(types.getValue("BodySensorLocation").annotation("BluetoothCharacteristic")).argument)
        assertEquals("\"2A19\"", checkNotNull(types.getValue("BatteryLevel").annotation("BluetoothCharacteristic")).argument)
    }
}
