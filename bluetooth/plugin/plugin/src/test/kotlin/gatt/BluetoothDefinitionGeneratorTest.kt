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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
        val value = generator.generateValueClass(characteristic()).singleType()

        assertEquals("EnvironmentalSampleValue", value.name)
        assertTrue(KModifier.DATA in value.modifiers)
        assertNotNull(value.annotation("Serializable"))

        // sint16 + decimal scaling -> Int with @Size(16_BIT) + @Scalar(decimalExponent = -2)
        val temperature = checkNotNull(value.property("temperature"))
        assertEquals("Int", temperature.type.simpleName)
        assertTrue(checkNotNull(temperature.annotation("Size")).argument.endsWith("Length.`16_BIT`"))
        assertEquals("decimalExponent = -2", checkNotNull(temperature.annotation("Scalar")).argument)

        // uint16 -> unsigned Int
        val humidity = checkNotNull(value.property("humidity"))
        assertEquals("Int", humidity.type.simpleName)
        assertTrue("Unsigned" in humidity.annotationNames)

        // uint32 -> Long (overflows a signed Int), @Scalar(multiplier = 10)
        val pressure = checkNotNull(value.property("pressure"))
        assertEquals("Long", pressure.type.simpleName)
        assertTrue(checkNotNull(pressure.annotation("Size")).argument.endsWith("Length.`32_BIT`"))
        assertEquals("multiplier = 10", checkNotNull(pressure.annotation("Scalar")).argument)
    }

    @Test
    fun parsesConditionalCharacteristicVariants() {
        val characteristic = characteristic("/gatt/sensor_reading.xml")
        assertTrue(characteristic.isVariant, "expected a variant characteristic")
        assertEquals(listOf("Temperature" to 1, "Humidity" to 2), characteristic.variants.map { it.name to it.discriminator })
    }

    @Test
    fun generatesSealedClassForConditionalCharacteristic() {
        val sealed = generator.generateValueClass(characteristic("/gatt/sensor_reading.xml")).singleType()

        assertEquals("SensorReadingValue", sealed.name)
        assertTrue(KModifier.SEALED in sealed.modifiers)
        assertNotNull(sealed.annotation("Serializable"))

        val temperature = checkNotNull(sealed.nestedType("Temperature"))
        assertTrue(KModifier.DATA in temperature.modifiers)
        assertEquals("value = 1", checkNotNull(temperature.annotation("SerializedByteValue")).argument)
        // the variant extends the sealed value class
        assertEquals("SensorReadingValue", temperature.superclass.simpleName)
        // and still carries the field annotations
        assertEquals("decimalExponent = -2", checkNotNull(temperature.property("temperature")).annotation("Scalar")?.argument)

        val humidity = checkNotNull(sealed.nestedType("Humidity"))
        assertEquals("value = 2", checkNotNull(humidity.annotation("SerializedByteValue")).argument)
        assertEquals("SensorReadingValue", humidity.superclass.simpleName)
    }

    @Test
    fun generatesDeviceServiceAndCharacteristicStructure() {
        val types = generator.generate(
            deviceName = "Environmental Sensor",
            services = listOf(service()),
            characteristics = listOf(characteristic()),
        ).types()

        // device: @Bluetooth, exposes the service as an advertised property typed as the service interface
        val device = types.getValue("EnvironmentalSensor")
        assertNotNull(device.annotation("Bluetooth"))
        val serviceProperty = checkNotNull(device.property("environmentalSensingSample"))
        assertEquals("EnvironmentalSensingSample", serviceProperty.type.simpleName)
        assertTrue("Advertising" in serviceProperty.annotationNames)

        // service: @BluetoothService("181A"), links the characteristic by its interface type
        val service = types.getValue("EnvironmentalSensingSample")
        assertEquals("\"181A\"", checkNotNull(service.annotation("BluetoothService")).argument)
        val characteristicProperty = checkNotNull(service.property("environmentalSample"))
        assertEquals("EnvironmentalSample", characteristicProperty.type.simpleName)

        // characteristic: @BluetoothCharacteristic("2BCE"), value typed and carrying the service's access (read + notify)
        val characteristic = types.getValue("EnvironmentalSample")
        assertEquals("\"2BCE\"", checkNotNull(characteristic.annotation("BluetoothCharacteristic")).argument)
        val valueProperty = checkNotNull(characteristic.property("value"))
        assertEquals("EnvironmentalSampleValue", valueProperty.type.simpleName)
        assertEquals(setOf("Readable", "Notifiable"), valueProperty.annotationNames)
    }
}
