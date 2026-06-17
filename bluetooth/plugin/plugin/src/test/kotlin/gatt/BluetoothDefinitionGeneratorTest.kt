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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BluetoothDefinitionGeneratorTest {

    private fun characteristic(resource: String = "/gatt/environmental_sample.xml") =
        GattXmlParser.parseCharacteristic(checkNotNull(javaClass.getResourceAsStream(resource)) { "missing $resource" })

    private fun service(resource: String = "/gatt/environmental_sensing_service.xml") =
        GattXmlParser.parseService(checkNotNull(javaClass.getResourceAsStream(resource)) { "missing $resource" })

    private val generator = BluetoothDefinitionGenerator("com.example.generated")

    @Test
    fun parsesCharacteristicFieldsAndScaling() {
        val characteristic = characteristic()
        assertEquals("Environmental Sample", characteristic.name)
        assertEquals("2BCE", characteristic.uuid)
        assertEquals(listOf("Temperature", "Humidity", "Pressure"), characteristic.fields.map { it.name })
        assertEquals(-2, characteristic.fields[0].decimalExponent)
        assertEquals(10, characteristic.fields[2].multiplier)
    }

    @Test
    fun parsesServiceCharacteristicsAndProperties() {
        val service = service()
        assertEquals("181A", service.uuid)
        assertEquals(1, service.characteristics.size)
        assertEquals("2BCE", service.characteristics[0].uuid)
        assertEquals(setOf(GattProperty.READ, GattProperty.NOTIFY), service.characteristics[0].properties)
    }

    @Test
    fun generatesSerializableValueClassWithFieldAnnotations() {
        val code = generator.generateValueClass(characteristic()).toString()

        assertTrue("@Serializable" in code, code)
        assertTrue("data class EnvironmentalSampleValue" in code, code)
        // sint16 + decimal scaling -> Int with @Size(16) + @Scalar(decimalExponent = -2)
        assertTrue("public val temperature: Int" in code, code)
        assertTrue("@Size(Length.`16_BIT`)" in code, code)
        assertTrue("@Scalar(decimalExponent = -2)" in code, code)
        // uint16 -> Int + @Unsigned
        assertTrue("public val humidity: Int" in code, code)
        assertTrue("@Unsigned" in code, code)
        // uint32 -> Long (overflows a signed Int) + @Scalar(multiplier = 10)
        assertTrue("public val pressure: Long" in code, code)
        assertTrue("@Size(Length.`32_BIT`)" in code, code)
        assertTrue("@Scalar(multiplier = 10)" in code, code)
    }

    @Test
    fun generatesDeviceServiceAndCharacteristicStructure() {
        val files = generator.generate(
            deviceName = "Environmental Sensor",
            services = listOf(service()),
            characteristics = listOf(characteristic()),
        )
        val code = files.joinToString("\n") { it.toString() }

        // characteristic: interface + value, with the service's access applied
        assertTrue("@BluetoothCharacteristic(\"2BCE\")" in code, code)
        assertTrue("interface EnvironmentalSample" in code, code)
        assertTrue("@Readable" in code, code)
        assertTrue("@Notifiable" in code, code)
        assertTrue("public val `value`: EnvironmentalSampleValue" in code || "public val value: EnvironmentalSampleValue" in code, code)

        // service references the characteristic interface
        assertTrue("@BluetoothService(\"181A\")" in code, code)
        assertTrue("interface EnvironmentalSensingSample" in code, code)
        assertTrue("public val environmentalSample: EnvironmentalSample" in code, code)

        // device exposes the (advertised) service
        assertTrue("@Bluetooth" in code, code)
        assertTrue("interface EnvironmentalSensor" in code, code)
        assertTrue("@Advertising" in code, code)
        assertTrue("public val environmentalSensingSample: EnvironmentalSensingSample" in code, code)
    }
}
