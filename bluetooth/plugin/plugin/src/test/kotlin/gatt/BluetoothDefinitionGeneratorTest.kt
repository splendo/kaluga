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

    private fun generate(resource: String): String {
        val xml = checkNotNull(javaClass.getResourceAsStream(resource)) { "missing fixture $resource" }
        val characteristic = GattXmlParser.parse(xml)
        return BluetoothDefinitionGenerator("com.example.generated").generateValueClass(characteristic).toString()
    }

    @Test
    fun parsesFieldsAndScaling() {
        val characteristic = GattXmlParser.parse(checkNotNull(javaClass.getResourceAsStream("/gatt/environmental_sample.xml")))
        assertEquals("Environmental Sample", characteristic.name)
        assertEquals("2BCE", characteristic.uuid)
        assertEquals(listOf("Temperature", "Humidity", "Pressure"), characteristic.fields.map { it.name })
        assertEquals(-2, characteristic.fields[0].decimalExponent)
        assertEquals(10, characteristic.fields[2].multiplier)
    }

    @Test
    fun generatesSerializableValueClassWithFieldAnnotations() {
        val code = generate("/gatt/environmental_sample.xml")

        // structure
        assertTrue("@Serializable" in code, code)
        assertTrue("data class EnvironmentalSample" in code, code)

        // sint16 + decimal scaling -> Int with @Size(16) + @Scalar(decimalExponent = -2)
        assertTrue("public val temperature: Int" in code, code)
        assertTrue("@Size(Length.`16_BIT`)" in code, code)
        assertTrue("@Scalar(decimalExponent = -2)" in code, code)

        // uint16 -> Int + @Unsigned (humidity)
        assertTrue("public val humidity: Int" in code, code)
        assertTrue("@Unsigned" in code, code)

        // uint32 -> Long (range can't fit a signed Int) + @Scalar(multiplier = 10)
        assertTrue("public val pressure: Long" in code, code)
        assertTrue("@Size(Length.`32_BIT`)" in code, code)
        assertTrue("@Scalar(multiplier = 10)" in code, code)
    }
}
