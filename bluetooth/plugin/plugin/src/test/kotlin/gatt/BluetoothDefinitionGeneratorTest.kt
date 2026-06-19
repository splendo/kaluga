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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
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
    fun parsesFlagsByteBitFieldAndRequirements() {
        val characteristic = characteristic("/gatt/rate_measurement.xml")
        assertFalse(characteristic.isVariant)

        // selector bit 0 (every value gates a field) -> one field with the format alternatives, placed at the bit
        val rate = characteristic.fields.single { it.name == "Rate Measurement Value" }
        assertEquals(0, rate.flagIndex)
        assertEquals("uint16", rate.format)
        assertEquals(listOf("uint8"), rate.alternateFormats)
        assertFalse(rate.optional)

        // presence bit 3 (some values gate, not trailing) -> optional single field
        val energy = characteristic.fields.single { it.name == "Energy Expended" }
        assertTrue(energy.optional)
        assertFalse(energy.repeated)
        assertEquals(3, energy.flagIndex)
        assertEquals("uint16", energy.format)

        // a <Repeated> field gated by bit 4 -> a list (fills the rest of the packet)
        val interval = characteristic.fields.single { it.name == "Interval" }
        assertTrue(interval.repeated)
        assertFalse(interval.optional)
        assertEquals(4, interval.flagIndex)

        // value bit 1 (no value gates) -> enum carried in the flags
        val contact = characteristic.flagFields.single()
        assertEquals("Contact Status", contact.name)
        assertEquals(1, contact.index)
        assertEquals(2, contact.size)
        assertEquals(listOf(0, 1, 2, 3), contact.cases.map { it.key })
    }

    @Test
    fun rejectsNonTrailingRepeatedField() {
        // a <Repeated> field has no length, so it cannot be followed by another field
        assertFailsWith<IllegalArgumentException> { characteristic("/gatt/non_trailing_repeated.xml") }
    }

    @Test
    fun generatesFlagsByteValueClass() {
        val value = generator.generateValueClass(characteristic("/gatt/rate_measurement.xml")).singleType()
        assertEquals("RateMeasurementValue", value.name)
        assertTrue(KModifier.DATA in value.modifiers)

        // dual-size selector: @FlagIndex(0), two @Size, @Unsigned, widest type
        val rate = checkNotNull(value.property("rateMeasurementValue"))
        assertEquals("Int", rate.type.simpleName)
        assertEquals("0", checkNotNull(rate.annotation("FlagIndex")).argument)
        assertEquals(2, rate.annotations.count { (it.typeName as? ClassName)?.simpleName == "Size" })
        assertTrue("Unsigned" in rate.annotationNames)

        // presence-gated field -> nullable + @FlagIndex(3)
        val energy = checkNotNull(value.property("energyExpended"))
        assertTrue(energy.type.isNullable)
        assertEquals("3", checkNotNull(energy.annotation("FlagIndex")).argument)

        // trailing presence-gated field -> unsized list with element formats, present via @NullIfEmpty
        val interval = checkNotNull(value.property("interval"))
        assertTrue(interval.type.toString().contains("List"), interval.type.toString())
        assertEquals("4", checkNotNull(interval.annotation("FlagIndex")).argument)
        assertTrue("NullIfEmpty" in interval.annotationNames)
        assertTrue("Unsized" in interval.annotationNames)
        assertTrue("ItemUnsigned" in interval.annotationNames)
        // the element format must carry its argument, e.g. @ItemSize(Length.`16_BIT`)
        assertTrue(checkNotNull(interval.annotation("ItemSize")).argument.endsWith("Length.`16_BIT`"))

        // enum-in-flags: nested enum + @FlagIndex(1) @FlagWidth(bits = 2)
        val contact = checkNotNull(value.property("contactStatus"))
        assertEquals("ContactStatus", contact.type.simpleName)
        assertEquals("1", checkNotNull(contact.annotation("FlagIndex")).argument)
        assertEquals("bits = 2", checkNotNull(contact.annotation("FlagWidth")).argument)
        val contactEnum = checkNotNull(value.nestedType("ContactStatus"))
        assertEquals(4, contactEnum.enumConstants.size)
        assertTrue("CONTACT_NOT_SUPPORTED" in contactEnum.enumConstants.keys, contactEnum.enumConstants.keys.toString())
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
