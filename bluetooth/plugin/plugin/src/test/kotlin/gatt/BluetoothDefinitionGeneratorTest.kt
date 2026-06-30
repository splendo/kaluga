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
import com.squareup.kotlinpoet.TypeSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BluetoothDefinitionGeneratorTest {

    private fun characteristic(resource: String = "/gatt/environmental_sample.xml") =
        GattXmlParser.parseCharacteristic(checkNotNull(javaClass.getResourceAsStream(resource)) { "missing $resource" })

    private fun service(resource: String = "/gatt/environmental_sensing_service.xml") =
        GattXmlParser.parseService(checkNotNull(javaClass.getResourceAsStream(resource)) { "missing $resource" })

    private fun descriptor(resource: String) = GattXmlParser.parseDescriptor(checkNotNull(javaClass.getResourceAsStream(resource)) { "missing $resource" })

    private val generator = BluetoothDefinitionGenerator("com.example.generated")

    private val scientificGenerator = BluetoothDefinitionGenerator("com.example.generated", useScientificUnits = true)

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

        // sint16 + decimal scaling -> Double with @Size(16_BIT) + @Scalar(decimalExponent = 2) (the inverse of GATT's -2)
        val temperature = checkNotNull(value.property("temperature"))
        assertEquals("Double", temperature.type.simpleName)
        assertTrue(checkNotNull(temperature.annotation("Size")).argument.endsWith("Length.`16_BIT`"))
        assertEquals("decimalExponent = 2", checkNotNull(temperature.annotation("Scalar")).argument)

        // uint16 + decimal scaling -> unsigned Double
        val humidity = checkNotNull(value.property("humidity"))
        assertEquals("Double", humidity.type.simpleName)
        assertTrue("Unsigned" in humidity.annotationNames)

        // uint32 with multiplier 10 -> Double; the multiplier folds into @Scalar(decimalExponent = -1)
        val pressure = checkNotNull(value.property("pressure"))
        assertEquals("Double", pressure.type.simpleName)
        assertTrue(checkNotNull(pressure.annotation("Size")).argument.endsWith("Length.`32_BIT`"))
        assertEquals("decimalExponent = -1", checkNotNull(pressure.annotation("Scalar")).argument)
    }

    @Test
    fun negativeMultiplierKeepsSignInScalarAndFoldsMagnitude() {
        // GATT multiplier is an integer in [-10, 10]; @Scalar encodes the inverse, so the sign survives as the @Scalar
        // multiplier (its own reciprocal) and the magnitude's powers fold into the exponents.
        val characteristic = GattCharacteristic(
            name = "Signed Scale",
            uuid = "2BCD",
            fields = listOf(
                GattField(name = "Inverted", format = "sint16", multiplier = -1),
                GattField(name = "InvertedDeci", format = "sint16", multiplier = -10),
            ),
        )
        val value = generator.generateValueClass(characteristic).singleType()

        // -1 is its own reciprocal: only the sign, no exponent
        val inverted = checkNotNull(value.property("inverted"))
        assertEquals("Double", inverted.type.simpleName)
        assertEquals("multiplier = -1", checkNotNull(inverted.annotation("Scalar")).argument)

        // -10 -> sign in the multiplier, the factor of ten folded into decimalExponent = -1
        val scalar = checkNotNull(value.property("invertedDeci")?.annotation("Scalar")).toString()
        assertTrue("multiplier = -1" in scalar, scalar)
        assertTrue("decimalExponent = -1" in scalar, scalar)
    }

    @Test
    fun rejectsMultiplierWithoutIntegerReciprocal() {
        // A magnitude that is not a product of powers of 10 and 2 (here 3) has no integer reciprocal, so @Scalar cannot
        // represent the inverse scaling and generation fails rather than emitting wrong bytes.
        val characteristic = GattCharacteristic(
            name = "Tripled Scale",
            uuid = "2BCC",
            fields = listOf(GattField(name = "Tripled", format = "sint16", multiplier = 3)),
        )
        assertFailsWith<IllegalArgumentException> { generator.generateValueClass(characteristic) }
    }

    @Test
    fun generatesNestedDescriptorInterface() {
        val characteristic = GattCharacteristic(
            name = "Reading",
            uuid = "2BE0",
            fields = listOf(GattField(name = "Reading", format = "uint16")),
            descriptors = listOf(
                GattDescriptor(
                    name = "Client Characteristic Configuration",
                    uuid = "2902",
                    properties = setOf(GattProperty.READ, GattProperty.WRITE),
                    fields = listOf(GattField(name = "Configuration", format = "uint16")),
                ),
            ),
        )
        val readingInterface = generator.characteristicFile(characteristic, setOf(GattProperty.READ))
            .members.filterIsInstance<TypeSpec>().single { it.name == "Reading" }

        // nested @BluetoothDescriptor interface with its own value class, exposed with the descriptor's access
        val descriptor = checkNotNull(readingInterface.nestedType("ClientCharacteristicConfiguration"))
        assertEquals("\"2902\"", checkNotNull(descriptor.annotation("BluetoothDescriptor")).argument)
        assertEquals(setOf("Readable", "Writable"), checkNotNull(descriptor.property("value")).annotationNames)
        assertNotNull(descriptor.nestedType("ClientCharacteristicConfigurationValue"))
        // the characteristic exposes the descriptor as a property
        assertEquals("ClientCharacteristicConfiguration", checkNotNull(readingInterface.property("clientCharacteristicConfiguration")).type.simpleName)
    }

    @Test
    fun resolvesServiceDescriptorAgainstDescriptorDefinition() {
        // The SIG service references a descriptor by type only; its UUID and value structure come from the descriptor's
        // own type XML, which is parsed separately and resolved here. The CCCD is referenced too but never generated:
        // it is managed by the notify/indicate layer.
        val types = generator.generate(
            deviceName = "Heart Rate Sensor",
            services = listOf(service("/gatt/sig_descriptor_service.xml")),
            characteristics = listOf(characteristic("/gatt/sig_heart_rate_measurement.xml")),
            descriptors = listOf(descriptor("/gatt/sig_user_description_descriptor.xml"), descriptor("/gatt/sig_cccd_descriptor.xml")),
        ).types()

        val measurement = types.getValue("HeartRateMeasurement")
        val userDescription = checkNotNull(measurement.nestedType("CharacteristicUserDescription"))
        assertEquals("\"2901\"", checkNotNull(userDescription.annotation("BluetoothDescriptor")).argument)
        assertEquals(setOf("Readable"), checkNotNull(userDescription.property("value")).annotationNames)
        assertNotNull(userDescription.nestedType("CharacteristicUserDescriptionValue"))
        assertEquals("CharacteristicUserDescription", checkNotNull(measurement.property("characteristicUserDescription")).type.simpleName)

        // the CCCD is deliberately omitted even though its definition was provided
        assertNull(measurement.nestedType("ClientCharacteristicConfiguration"))
        assertNull(measurement.property("clientCharacteristicConfiguration"))
    }

    @Test
    fun skipsServiceDescriptorWithoutDefinition() {
        // With no descriptor definition the reference can't be resolved to a UUID, so it is skipped rather than emitted.
        val types = generator.generate(
            deviceName = "Heart Rate Sensor",
            services = listOf(service("/gatt/sig_descriptor_service.xml")),
            characteristics = listOf(characteristic("/gatt/sig_heart_rate_measurement.xml")),
            descriptors = emptyList(),
        ).types()
        assertNull(types.getValue("HeartRateMeasurement").nestedType("CharacteristicUserDescription"))
    }

    @Test
    fun generatesUnsignedIntForBitWidthFormat() {
        // A bare bit-width token (e.g. `8bit`, `16bit`) is an unsigned integer of that width.
        val characteristic = GattCharacteristic(
            name = "Raw",
            uuid = "2B00",
            fields = listOf(GattField(name = "Byte", format = "8bit"), GattField(name = "Word", format = "16bit")),
        )
        val value = generator.generateValueClass(characteristic).singleType()
        val byte = checkNotNull(value.property("byte"))
        assertTrue("Unsigned" in byte.annotationNames)
        assertTrue(checkNotNull(byte.annotation("Size")).argument.endsWith("Length.`8_BIT`"))
        assertTrue(checkNotNull(value.property("word")?.annotation("Size")).argument.endsWith("Length.`16_BIT`"))
    }

    @Test
    fun generatesUnsignedIntForRawBitFieldValue() {
        // BitField bits with no enumerations are raw sub-byte numerics, packed in the flags via @FlagIndex/@FlagWidth.
        val value = generator.generateValueClass(characteristic("/gatt/numeric_in_flags.xml")).singleType()

        val counter = checkNotNull(value.property("counter"))
        assertEquals("Int", counter.type.simpleName)
        assertEquals("0", checkNotNull(counter.annotation("FlagIndex")).argument)
        assertEquals("bits = 4", checkNotNull(counter.annotation("FlagWidth")).argument)
        assertTrue("Unsigned" in counter.annotationNames)
        assertNull(value.nestedType("Counter")) // a raw numeric, not an enum

        val reading = checkNotNull(value.property("reading"))
        assertEquals("Int", reading.type.simpleName)
        assertEquals("4", checkNotNull(reading.annotation("FlagIndex")).argument)
        assertEquals("bits = 12", checkNotNull(reading.annotation("FlagWidth")).argument)
    }

    @Test
    fun rejectsUnsupportedIntegerWidth() {
        // 12-bit isn't byte-aligned and 128-bit has no Kotlin primitive; neither has a Length, so both fail loudly.
        assertFailsWith<IllegalArgumentException> {
            generator.generateValueClass(GattCharacteristic("Odd", "2B01", listOf(GattField("V", "uint12"))))
        }
        assertFailsWith<IllegalArgumentException> {
            generator.generateValueClass(GattCharacteristic("Big", "2B02", listOf(GattField("V", "uint128"))))
        }
    }

    @Test
    fun generatesMedicalFloatForSFloatAndFloat() {
        // SFLOAT/FLOAT are the IEEE-11073 16-/32-bit medical floats: a Double carrying @Size + @MedFloat.
        val value = generator.generateValueClass(
            GattCharacteristic("Medical", "2B03", listOf(GattField("Small", "SFLOAT"), GattField("Large", "FLOAT"))),
        ).singleType()
        val small = checkNotNull(value.property("small"))
        assertEquals("Double", small.type.simpleName)
        assertTrue("MedFloat" in small.annotationNames)
        assertTrue(checkNotNull(small.annotation("Size")).argument.endsWith("Length.`16_BIT`"))
        val large = checkNotNull(value.property("large"))
        assertEquals("Double", large.type.simpleName)
        assertTrue("MedFloat" in large.annotationNames)
        assertTrue(checkNotNull(large.annotation("Size")).argument.endsWith("Length.`32_BIT`"))
    }

    @Test
    fun generatesIeeeFloatTypesWithoutSize() {
        // float32/float64 are native IEEE-754 floats: width is intrinsic to the type, so no @Size/@MedFloat.
        val value = generator.generateValueClass(
            GattCharacteristic("Ieee", "2B04", listOf(GattField("Single", "float32"), GattField("Wide", "float64"))),
        ).singleType()
        val single = checkNotNull(value.property("single"))
        assertEquals("Float", single.type.simpleName)
        assertNull(single.annotation("Size"))
        assertFalse("MedFloat" in single.annotationNames)
        assertEquals("Double", checkNotNull(value.property("wide")).type.simpleName)
    }

    @Test
    fun generatesStringWithEncodingForUtf16() {
        // utf8s maps to String with the default (UTF-8) encoding; utf16s adds @Encoded(UTF_16).
        val value = generator.generateValueClass(
            GattCharacteristic("Text", "2B05", listOf(GattField("Name", "utf8s"), GattField("Wide", "utf16s"))),
        ).singleType()
        val name = checkNotNull(value.property("name"))
        assertEquals("String", name.type.simpleName)
        assertNull(name.annotation("Encoded"))
        val wide = checkNotNull(value.property("wide"))
        assertEquals("String", wide.type.simpleName)
        assertTrue(checkNotNull(wide.annotation("Encoded")).argument.endsWith("Encoding.UTF_16"))
    }

    @Test
    fun generatesBooleanField() {
        val value = generator.generateValueClass(
            GattCharacteristic("Switch", "2B06", listOf(GattField("Enabled", "boolean"))),
        ).singleType()
        assertEquals("Boolean", checkNotNull(value.property("enabled")).type.simpleName)
    }

    @Test
    fun generatesReferenceFieldTypedAsReferencedValue() {
        val reading = GattCharacteristic("Reading", "2BE0", listOf(GattField("Reading", "uint16")))
        val pair = GattCharacteristic(
            name = "Reading Pair",
            uuid = "2BE1",
            fields = listOf(
                GattField(name = "First", format = "", reference = "2BE0"),
                GattField(name = "Second", format = "", reference = "2BE0"),
            ),
        )
        val value = generator.generateValueClass(pair, mapOf("2BE0" to reading)).singleType()
        assertEquals("ReadingValue", checkNotNull(value.property("first")).type.simpleName)
        assertEquals("ReadingValue", checkNotNull(value.property("second")).type.simpleName)
    }

    @Test
    fun parsesMultipleFlagFieldsWithOffsetBitIndices() {
        // Two 8-bit Flags fields: the second field's bits are offset by 8, so Value B's presence bit is index 8.
        val characteristic = characteristic("/gatt/dual_flags.xml")
        assertEquals(0, checkNotNull(characteristic.fields.single { it.name == "Value A" }).flagIndex)
        val valueB = checkNotNull(characteristic.fields.single { it.name == "Value B" })
        assertEquals(8, valueB.flagIndex)
        assertTrue(valueB.optional)

        val value = generator.generateValueClass(characteristic).singleType()
        assertEquals("8", checkNotNull(value.property("valueB")?.annotation("FlagIndex")).argument)
    }

    @Test
    fun generatesPresentWhenAllSetForCompoundRequirement() {
        // "Combined" requires both C1 (bit 0) and C2 (bit 1) -> a compound presence over both bits.
        val characteristic = characteristic("/gatt/compound_flags.xml")
        val combined = checkNotNull(characteristic.fields.single { it.name == "Combined" })
        assertEquals(listOf(0, 1), combined.presenceFlagIndices)

        val value = generator.generateValueClass(characteristic).singleType()
        val property = checkNotNull(value.property("combined"))
        assertTrue(property.type.isNullable)
        val presence = checkNotNull(property.annotation("PresentWhenAllSet")).toString()
        assertTrue("0" in presence && "1" in presence, presence)
        assertNull(property.annotation("FlagIndex"))
    }

    @Test
    fun generatesByteValuedEnumForEnumerationField() {
        // The real SIG Body Sensor Location is an 8-bit field with top-level <Enumerations> -> a byte-valued enum.
        val value = generator.generateValueClass(characteristic("/gatt/sig_body_sensor_location.xml")).singleType()
        val enum = checkNotNull(value.nestedType("BodySensorLocation"))
        assertEquals(7, enum.enumConstants.size)
        assertEquals("value = 1", checkNotNull(enum.enumConstants.getValue("CHEST").annotation("SerializedByteValue")).argument)
        assertEquals("BodySensorLocation", checkNotNull(value.property("bodySensorLocation")).type.simpleName)
    }

    @Test
    fun rejectsUnresolvableEnumCaseNameClash() {
        // "Status" (key 5) decapitates to slug STATUS, clashes with key 0's STATUS, and disambiguates to STATUS_5 —
        // which collides with key 2's "Status 5". Rather than emit a duplicate enum constant, generation fails.
        val characteristic = GattCharacteristic(
            name = "Clashing",
            uuid = "2B07",
            fields = listOf(
                GattField(
                    name = "Sensor",
                    format = "8bit",
                    enumCases = listOf(GattFlagCase(0, "Status"), GattFlagCase(2, "Status 5"), GattFlagCase(5, "Status")),
                ),
            ),
        )
        assertFailsWith<IllegalArgumentException> { generator.generateValueClass(characteristic) }
    }

    @Test
    fun resolvesServiceCharacteristicsReferencedByType() {
        // The real SIG service references its characteristics by `type` (no uuid on the references).
        val types = generator.generate(
            deviceName = "Heart Rate Sensor",
            services = listOf(service("/gatt/sig_heart_rate_service.xml")),
            characteristics = listOf(
                characteristic("/gatt/sig_heart_rate_measurement.xml"),
                characteristic("/gatt/sig_body_sensor_location.xml"),
                characteristic("/gatt/sig_heart_rate_control_point.xml"),
            ),
        ).types()

        val service = types.getValue("HeartRate")
        assertEquals("\"180D\"", checkNotNull(service.annotation("BluetoothService")).argument)
        assertEquals("HeartRateMeasurement", checkNotNull(service.property("heartRateMeasurement")).type.simpleName)
        assertEquals("BodySensorLocation", checkNotNull(service.property("bodySensorLocation")).type.simpleName)
        // notify-only on the measurement, read-only on the location, write-only on the control point
        assertEquals(setOf("Notifiable"), checkNotNull(types.getValue("HeartRateMeasurement").property("value")).annotationNames)
        assertEquals(setOf("Readable"), checkNotNull(types.getValue("BodySensorLocation").property("value")).annotationNames)
        assertEquals(setOf("Writable"), checkNotNull(types.getValue("HeartRateControlPoint").property("value")).annotationNames)
    }

    @Test
    fun parsesFlagsByteBitFieldAndRequirements() {
        val characteristic = characteristic("/gatt/rate_measurement.xml")

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
    fun rejectsSelectorBitOverStructurallyDifferentFields() {
        // A selector bit may only choose between width alternatives of one field (same base name, e.g. `Value (uint8)`/
        // `Value (uint16)`). When it gates structurally different fields, collapsing them would drop data, so it fails.
        assertFailsWith<IllegalArgumentException> { characteristic("/gatt/structural_selector.xml") }
    }

    @Test
    fun generatesScientificValueClassForUnitField() {
        val value = scientificGenerator.generateValueClass(characteristic("/gatt/internal_temperature.xml")).singleType()

        // the unit field becomes a nested @JvmInline value class implementing ScientificValue
        val temperature = checkNotNull(value.nestedType("Temperature"))
        assertTrue(KModifier.VALUE in temperature.modifiers)
        assertNotNull(temperature.annotation("Serializable"))
        assertNotNull(temperature.annotation("JvmInline"))
        val superInterface = temperature.superinterfaces.keys.single().toString()
        assertTrue("ScientificValue" in superInterface, superInterface)
        assertTrue("PhysicalQuantity.Temperature" in superInterface, superInterface)
        assertTrue(superInterface.endsWith("Celsius>"), superInterface)
        // the wire format moves onto the value class's `value`
        val raw = checkNotNull(temperature.property("value"))
        assertEquals("Double", raw.type.simpleName)
        assertTrue(checkNotNull(raw.annotation("Size")).argument.endsWith("Length.`16_BIT`"))
        assertEquals("decimalExponent = 2", checkNotNull(raw.annotation("Scalar")).argument)
        // the data-class property is typed as the value class
        assertEquals("Temperature", checkNotNull(value.property("temperature")).type.simpleName)
    }

    @Test
    fun wrapsWidthSelectedFieldAsScientificValue() {
        // The Heart Rate Measurement Value field selects uint8/uint16 via a flag bit. With scientific units on it is still
        // wrapped: both @Size widths live on the value class while the selector @FlagIndex stays on the property (the
        // serializer merges them across the inline boundary). The nested type is renamed to `Value` because the field name
        // repeats the characteristic name and would otherwise collide with the containing `HeartRateMeasurementValue`.
        val value = scientificGenerator.generateValueClass(characteristic("/gatt/sig_heart_rate_measurement.xml")).singleType()

        val rate = checkNotNull(value.nestedType("Value"))
        assertTrue(KModifier.VALUE in rate.modifiers)
        val superInterface = rate.superinterfaces.keys.single().toString()
        assertTrue("ScientificValue" in superInterface, superInterface)
        assertTrue("PhysicalQuantity.Frequency" in superInterface, superInterface)
        assertTrue(superInterface.endsWith("BeatsPerMinute>"), superInterface)
        // both wire widths are carried on the value class' value
        val raw = rate.toString()
        assertTrue("Length.`8_BIT`" in raw && "Length.`16_BIT`" in raw, raw)
        // the selector flag stays on the property, which is typed as the value class
        val property = checkNotNull(value.property("heartRateMeasurementValue"))
        assertEquals("Value", property.type.simpleName)
        assertEquals("0", checkNotNull(property.annotation("FlagIndex")).argument)
    }

    @Test
    fun wrapsRepeatedUnitFieldAsListOfScientificValues() {
        // A repeated unit field (the Interval, resolution 1/1024 s) becomes a List of ScientificValue value classes: the
        // element value class carries its own @Size, so the list needs no @Item* annotations, only @Unsized + presence.
        val type = scientificGenerator.generateValueClass(characteristic("/gatt/rate_measurement.xml")).singleType()

        val interval = checkNotNull(type.nestedType("Interval"))
        assertTrue(KModifier.VALUE in interval.modifiers)
        val superInterface = interval.superinterfaces.keys.single().toString()
        assertTrue("ScientificValue" in superInterface, superInterface)
        assertTrue("PhysicalQuantity.Time" in superInterface, superInterface)
        assertTrue(superInterface.endsWith("Second>"), superInterface)
        assertTrue(checkNotNull(interval.property("value")).annotation("Size")?.argument?.endsWith("Length.`16_BIT`") == true)

        // the property is a List of that value class, gated by its flag bit, with no @Item* annotations
        val property = checkNotNull(type.property("interval"))
        assertEquals(setOf("Unsized", "FlagIndex", "NullIfEmpty"), property.annotationNames)
        assertEquals("4", checkNotNull(property.annotation("FlagIndex")).argument)
        val rendered = property.toString()
        assertTrue("List<" in rendered && "Interval>" in rendered, rendered)
    }

    @Test
    fun generatesCompoundUnitValueClass() {
        val code = scientificGenerator.generateValueClass(characteristic("/gatt/ground_speed.xml")).toString()

        assertTrue("ScientificValue<PhysicalQuantity.Speed, MetricSpeed>" in code, code)
        // the compound unit is built from imported objects + the `per` infix, not fully-qualified
        assertTrue("get() = Meter per Second" in code, code)
        assertTrue("import com.splendo.kaluga.scientific.unit.Meter" in code, code)
        assertTrue("import com.splendo.kaluga.scientific.unit.Second" in code, code)
    }

    @Test
    fun keepsPlainNumericWhenScientificUnitsDisabled() {
        // default generator (flag off) -> plain numeric, no nested value class
        val value = generator.generateValueClass(characteristic("/gatt/internal_temperature.xml")).singleType()
        assertEquals("Double", checkNotNull(value.property("temperature")).type.simpleName)
        assertNull(value.nestedType("Temperature"))
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
