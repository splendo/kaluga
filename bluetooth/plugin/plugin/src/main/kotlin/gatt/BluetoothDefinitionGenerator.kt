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

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import kotlin.math.abs

/**
 * Generates Kaluga `@Bluetooth` definitions from parsed GATT XML: the `@Serializable` value class for each
 * characteristic (translating field formats/scaling into `BluetoothFormat` annotations), the `@BluetoothCharacteristic`
 * and `@BluetoothService` interfaces, and the `@Bluetooth` device that ties them together.
 *
 * A leading flags byte becomes a flat class where each bit either selects a field's width (multiple `@Size`), gates a
 * field's presence (nullable, or an `@Unsized` list for a `repeated` field) or carries an enumerated value
 * (`@FlagIndex` enum), all keyed by `@FlagIndex`.
 *
 * @param packageName the package the generated definitions are placed in.
 */
class BluetoothDefinitionGenerator(private val packageName: String, private val useScientificUnits: Boolean = false) {

    /**
     * Generates all definitions for [deviceName] exposing [services], whose characteristic value structures are taken
     * from [characteristics] (linked by UUID). Descriptors a service references are resolved against [descriptors] (the
     * descriptor type definitions) for their UUID and value structure. Returns one [FileSpec] per characteristic and
     * service plus the device.
     */
    fun generate(
        deviceName: String,
        services: List<GattService>,
        characteristics: List<GattCharacteristic>,
        descriptors: List<GattDescriptorDefinition> = emptyList(),
    ): List<FileSpec> {
        // Characteristics are referenced by UUID or by SIG type, so index by both.
        val byKey = buildMap {
            characteristics.forEach { characteristic ->
                characteristic.uuid.takeIf(String::isNotBlank)?.let { put(it, characteristic) }
                characteristic.type?.takeIf(String::isNotBlank)?.let { put(it, characteristic) }
            }
        }
        val accessByUuid = services.flatMap { it.characteristics }
            .mapNotNull { ref -> ref.resolve(byKey)?.let { it.uuid to ref.properties } }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, sets) -> sets.flatten().toSet() }

        // A service references descriptors by type; resolve each to the parsed definition (UUID + value structure) and
        // attach the resolved descriptors to the characteristic they belong to, keyed by its UUID.
        val descriptorsByType = descriptors.associateBy { it.type }
        val descriptorsByUuid = services.flatMap { it.characteristics }
            .mapNotNull { ref -> ref.resolve(byKey)?.let { it.uuid to resolveDescriptors(ref, descriptorsByType) } }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, lists) -> lists.flatten().distinctBy { it.uuid } }

        val characteristicFiles = characteristics.map { characteristic ->
            val resolved = descriptorsByUuid[characteristic.uuid].orEmpty()
            val withDescriptors = if (resolved.isEmpty()) characteristic else characteristic.copy(descriptors = resolved)
            characteristicFile(withDescriptors, accessByUuid[characteristic.uuid].orEmpty(), byKey)
        }
        val serviceFiles = services.map { service -> serviceFile(service, byKey) }
        return characteristicFiles + serviceFiles + deviceFile(deviceName, services)
    }

    // Resolves a characteristic's descriptor references to concrete descriptors: the service ref grants the access, the
    // descriptor's own type definition supplies the UUID and value structure. The Client Characteristic Configuration
    // descriptor is never generated (see below). A reference whose type has no definition (its type XML was not provided)
    // cannot be resolved to a UUID, so it is skipped with a warning.
    private fun resolveDescriptors(ref: GattServiceCharacteristic, descriptorsByType: Map<String, GattDescriptorDefinition>): List<GattDescriptor> =
        ref.descriptorReferences.mapNotNull { descriptorRef ->
            val definition = descriptorsByType[descriptorRef.type]
            when {
                // The Client Characteristic Configuration descriptor (CCCD, 0x2902) enables notify/indicate and is
                // managed by Kaluga's notification layer, so it is deliberately never surfaced as a @BluetoothDescriptor.
                descriptorRef.type == CCCD_TYPE || definition?.let { isCccdUuid(it.uuid) } == true -> null
                definition == null -> {
                    System.err.println("Warning: descriptor '${descriptorRef.type}' has no definition file; skipping (no UUID/value to generate).")
                    null
                }
                else -> GattDescriptor(definition.name, definition.uuid, descriptorRef.properties, definition.fields, definition.flagFields)
            }
        }

    // The CCCD identified by its standard type or 0x2902 UUID (16-bit shorthand or the full 128-bit base form).
    private fun isCccdUuid(uuid: String): Boolean =
        uuid.equals(CCCD_UUID, ignoreCase = true) || uuid.equals("0000$CCCD_UUID-0000-1000-8000-00805f9b34fb", ignoreCase = true)

    /** The `@BluetoothCharacteristic` interface for [characteristic] plus its `@Serializable` value class. */
    fun characteristicFile(
        characteristic: GattCharacteristic,
        access: Set<GattProperty>,
        characteristicsByUuid: Map<String, GattCharacteristic> = emptyMap(),
    ): FileSpec {
        val interfaceName = characteristic.name.toPascalCase()
        val valueType = ClassName(packageName, interfaceName + VALUE_SUFFIX)
        val value = PropertySpec.builder("value", valueType)
            .addModifiers(KModifier.ABSTRACT)
            .apply { access.ifEmpty { setOf(GattProperty.READ) }.forEach { addAnnotation(accessAnnotation(it)) } }
            .build()
        val interfaceBuilder = TypeSpec.interfaceBuilder(interfaceName)
            .addAnnotation(annotation("BluetoothCharacteristic", characteristic.uuid))
            .addProperty(value)
        // Each descriptor becomes a nested @BluetoothDescriptor interface plus a property exposing it.
        characteristic.descriptors.forEach { descriptor ->
            val descriptorName = descriptor.name.toPascalCase()
            interfaceBuilder.addType(descriptorInterface(descriptor, interfaceName, descriptorName))
            interfaceBuilder.addProperty(
                PropertySpec.builder(descriptorName.toCamelCase(), ClassName(packageName, interfaceName, descriptorName))
                    .addModifiers(KModifier.ABSTRACT)
                    .build(),
            )
        }
        return FileSpec.builder(packageName, interfaceName)
            .addType(interfaceBuilder.build())
            .addType(valueType(characteristic, valueType.simpleName, characteristicsByUuid))
            .build()
    }

    // A nested @BluetoothDescriptor interface whose `value` carries the descriptor's structure (a generated value class
    // when it declares fields, otherwise a raw ByteArray), exposed with the descriptor's access.
    private fun descriptorInterface(descriptor: GattDescriptor, enclosingInterfaceName: String, descriptorName: String): TypeSpec {
        val builder = TypeSpec.interfaceBuilder(descriptorName)
            .addAnnotation(annotation("BluetoothDescriptor", descriptor.uuid))
        val valueType: TypeName = if (descriptor.fields.isEmpty() && descriptor.flagFields.isEmpty()) {
            BYTE_ARRAY
        } else {
            val valueClassName = descriptorName + VALUE_SUFFIX
            builder.addType(dataValueType(descriptor.fields, valueClassName, descriptor.flagFields))
            ClassName(packageName, enclosingInterfaceName, descriptorName, valueClassName)
        }
        builder.addProperty(
            PropertySpec.builder("value", valueType)
                .addModifiers(KModifier.ABSTRACT)
                .apply { descriptor.properties.ifEmpty { setOf(GattProperty.READ) }.forEach { addAnnotation(accessAnnotation(it)) } }
                .build(),
        )
        return builder.build()
    }

    /** The `@BluetoothService` interface for [service], referencing each of its characteristic interfaces. */
    fun serviceFile(
        service: GattService,
        characteristics: Map<String, GattCharacteristic>,
    ): FileSpec {
        val serviceName = service.name.toPascalCase()
        val builder = TypeSpec.interfaceBuilder(serviceName)
            .addAnnotation(annotation("BluetoothService", service.uuid))
        service.characteristics.forEach { ref ->
            val characteristic = ref.resolve(characteristics)
                ?: error("Service '${service.name}' references unknown characteristic '${ref.uuid.ifBlank { ref.type }}'")
            val interfaceName = characteristic.name.toPascalCase()
            builder.addProperty(
                PropertySpec.builder(interfaceName.replaceFirstChar { it.lowercaseChar() }, ClassName(packageName, interfaceName))
                    .addModifiers(KModifier.ABSTRACT)
                    .build(),
            )
        }
        return FileSpec.builder(packageName, serviceName).addType(builder.build()).build()
    }

    /** The `@Bluetooth` device named [deviceName] exposing each of [services] (advertised). */
    fun deviceFile(deviceName: String, services: List<GattService>): FileSpec {
        val name = deviceName.toPascalCase()
        val builder = TypeSpec.interfaceBuilder(name)
            .addAnnotation(ClassName(ANNOTATIONS, "Bluetooth"))
        services.forEach { service ->
            val serviceName = service.name.toPascalCase()
            builder.addProperty(
                PropertySpec.builder(serviceName.replaceFirstChar { it.lowercaseChar() }, ClassName(packageName, serviceName))
                    .addModifiers(KModifier.ABSTRACT)
                    .addAnnotation(ClassName(ANNOTATIONS, "Advertising"))
                    .build(),
            )
        }
        return FileSpec.builder(packageName, name).addType(builder.build()).build()
    }

    /** Standalone value class (no characteristic interface); used to exercise the field → serialization mapping. */
    fun generateValueClass(characteristic: GattCharacteristic, characteristicsByUuid: Map<String, GattCharacteristic> = emptyMap()): FileSpec {
        val className = characteristic.name.toPascalCase() + VALUE_SUFFIX
        return FileSpec.builder(packageName, className).addType(valueType(characteristic, className, characteristicsByUuid)).build()
    }

    private fun valueType(characteristic: GattCharacteristic, className: String, characteristicsByUuid: Map<String, GattCharacteristic> = emptyMap()): TypeSpec =
        dataValueType(characteristic.fields, className, characteristic.flagFields, characteristicsByUuid)

    private fun dataValueType(
        fields: List<GattField>,
        className: String,
        flagFields: List<GattFlagField> = emptyList(),
        characteristicsByUuid: Map<String, GattCharacteristic> = emptyMap(),
    ): TypeSpec {
        val constructor = FunSpec.constructorBuilder()
        val type = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addAnnotation(SERIALIZABLE)

        // Values carried in the leading flags byte: a (nested) enum stored at its bit, by ordinal.
        flagFields.forEach { flag ->
            val enumName = flag.name.toPascalCase()
            type.addType(flagEnum(flag, enumName))
            val propertyName = flag.name.toCamelCase()
            val enumType = ClassName(packageName, className, enumName)
            constructor.addParameter(ParameterSpec.builder(propertyName, enumType).build())
            type.addProperty(
                PropertySpec.builder(propertyName, enumType)
                    .initializer(propertyName)
                    .addAnnotation(flagIndex(flag.index))
                    .addAnnotation(flagWidth(flag.size))
                    .apply { flag.description?.let { addKdoc("%L", it) } }
                    .build(),
            )
        }

        fields.forEach { field ->
            val propertyName = field.name.toCamelCase()
            val (propertyType, annotations) = when {
                // A field carrying top-level enumerations becomes a (nested) enum encoded as its byte value.
                field.enumCases.isNotEmpty() -> {
                    val enumName = field.name.toPascalCase()
                    type.addType(bodyEnum(field, enumName))
                    val enumType = ClassName(packageName, className, enumName)
                    (if (field.optional) enumType.copy(nullable = true) else enumType) to field.gatingAnnotations()
                }

                // A field that references another characteristic embeds that characteristic's value class (no wire
                // format of its own); the nested structure handles its own encoding.
                field.reference != null -> {
                    val referenced = characteristicsByUuid[field.reference]
                        ?: error("Field '${field.name}' references unknown characteristic UUID '${field.reference}'")
                    val referencedType = ClassName(packageName, referenced.name.toPascalCase() + VALUE_SUFFIX)
                    (if (field.optional) referencedType.copy(nullable = true) else referencedType) to field.gatingAnnotations()
                }

                else -> {
                    val mapping = field.toMapping()
                    when {
                        // A field with a known unit becomes a (nested) ScientificValue value class carrying the wire format;
                        // only the presence flag stays on the property here.
                        field.scientificUnit() != null -> {
                            val valueClassName = field.name.toPascalCase()
                            type.addType(scientificValueClass(valueClassName, mapping, checkNotNull(field.scientificUnit())))
                            val valueClass = ClassName(packageName, className, valueClassName)
                            (if (field.optional) valueClass.copy(nullable = true) else valueClass) to field.gatingAnnotations()
                        }

                        // A repeated field fills the rest of the packet as an unsized list; the element formatting moves onto
                        // the list via the @Item* annotations. When gated by a flag bit, presence is encoded via @NullIfEmpty.
                        field.repeated -> LIST.parameterizedBy(mapping.type) to
                            mapping.annotations.map { it.asItemAnnotation() } + UNSIZED +
                            (field.flagIndex?.let { listOf(flagIndex(it), NULL_IF_EMPTY) } ?: emptyList())

                        else -> (if (field.optional) mapping.type.copy(nullable = true) else mapping.type) to
                            mapping.annotations + field.gatingAnnotations()
                    }
                }
            }
            constructor.addParameter(ParameterSpec.builder(propertyName, propertyType).build())
            type.addProperty(
                PropertySpec.builder(propertyName, propertyType)
                    .initializer(propertyName)
                    .apply {
                        annotations.forEach(::addAnnotation)
                        field.description?.let { addKdoc("%L", it) }
                    }
                    .build(),
            )
        }
        return type.primaryConstructor(constructor.build()).build()
    }

    // The Kaluga Scientific unit this field maps onto, or null to keep a plain numeric. Only plain single fields
    // (not repeated, not width-selected) are wrapped; presence/nullability is handled by the containing property.
    private fun GattField.scientificUnit(): ScientificUnit? =
        if (useScientificUnits && !repeated && alternateFormats.isEmpty()) unit?.let { bluetoothScientificUnits[it] } else null

    // A @Serializable value class that is a ScientificValue<Quantity, Unit> and carries the wire format on its value.
    private fun scientificValueClass(name: String, mapping: Mapping, scientificUnit: ScientificUnit): TypeSpec {
        val quantity = ClassName(SCIENTIFIC, "PhysicalQuantity").nestedClass(scientificUnit.quantity)
        val unitType = ClassName(SCIENTIFIC_UNIT, scientificUnit.unitType)
        // Unit objects are referenced by type (data objects); `per`/`x` are infix members joining a compound unit.
        val tokens = scientificUnit.unit.split(" ").map { token ->
            if (token == "per" || token == "x") "%M" to MemberName(SCIENTIFIC_UNIT, token) else "%T" to ClassName(SCIENTIFIC_UNIT, token)
        }
        val unitExpression = CodeBlock.of(tokens.joinToString(" ") { it.first }, *tokens.map { it.second }.toTypedArray())
        return TypeSpec.classBuilder(name)
            .addModifiers(KModifier.VALUE)
            .addAnnotation(SERIALIZABLE)
            .addAnnotation(JVM_INLINE)
            .addSuperinterface(ClassName(SCIENTIFIC, "ScientificValue").parameterizedBy(quantity, unitType))
            .primaryConstructor(FunSpec.constructorBuilder().addParameter("value", mapping.type).build())
            .addProperty(
                PropertySpec.builder("value", mapping.type, KModifier.OVERRIDE)
                    .initializer("value")
                    .apply { mapping.annotations.forEach(::addAnnotation) }
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("unit", unitType, KModifier.OVERRIDE)
                    .getter(FunSpec.getterBuilder().addStatement("return %L", unitExpression).build())
                    .build(),
            )
            .build()
    }

    // The element of a generated list carries its format through the @Item* variant of each field annotation.
    private fun AnnotationSpec.asItemAnnotation(): AnnotationSpec {
        val builder = AnnotationSpec.builder(ClassName(SERIALIZATION, "Item${(typeName as ClassName).simpleName}"))
        members.forEach(builder::addMember)
        return builder.build()
    }

    private fun flagEnum(flag: GattFlagField, enumName: String): TypeSpec {
        val builder = TypeSpec.enumBuilder(enumName).addAnnotation(SERIALIZABLE)
        val used = mutableSetOf<String>()
        val kdoc = StringBuilder()
        flag.cases.sortedBy { it.key }.forEach { case ->
            val caseName = enumCaseName(case.description, case.key, used)
            builder.addEnumConstant(caseName)
            case.description?.let { kdoc.appendLine("[$caseName]: $it") }
        }
        kdoc.toString().trim().takeIf { it.isNotEmpty() }?.let { builder.addKdoc("%L", it) }
        return builder.build()
    }

    // An enum whose constants are encoded as their byte value via @SerializedByteValue, for a field with top-level
    // enumerations (e.g. Body Sensor Location). Reserved/unlisted byte values have no constant.
    private fun bodyEnum(field: GattField, enumName: String): TypeSpec {
        val builder = TypeSpec.enumBuilder(enumName).addAnnotation(SERIALIZABLE)
        val used = mutableSetOf<String>()
        val kdoc = StringBuilder()
        field.enumCases.sortedBy { it.key }.forEach { case ->
            val caseName = enumCaseName(case.description, case.key, used)
            builder.addEnumConstant(caseName, TypeSpec.anonymousClassBuilder().addAnnotation(serializedByteValue(case.key)).build())
            case.description?.let { kdoc.appendLine("[$caseName]: $it") }
        }
        kdoc.toString().trim().takeIf { it.isNotEmpty() }?.let { builder.addKdoc("%L", it) }
        return builder.build()
    }

    // A readable UPPER_SNAKE constant from the spec's case text, falling back to the wire key; disambiguated on clash.
    private fun enumCaseName(description: String?, key: Int, used: MutableSet<String>): String {
        val slug = description?.uppercase()?.replace(Regex("[^A-Z0-9]+"), "_")?.trim('_')
            ?.split("_")?.filter { it.isNotEmpty() }?.take(4)?.joinToString("_")
            ?.takeIf { it.isNotEmpty() && it.first().isLetter() }
            ?: "VALUE_$key"
        return if (used.add(slug)) slug else "${slug}_$key".also { used.add(it) }
    }

    private fun flagIndex(index: Int) = AnnotationSpec.builder(ClassName(SERIALIZATION, "FlagIndex")).addMember("%L", index).build()

    private fun presentWhenAllSet(indices: List<Int>) = AnnotationSpec.builder(ClassName(SERIALIZATION, "PresentWhenAllSet"))
        .apply { indices.forEach { addMember("%L", it) } }
        .build()

    // The flag annotation gating a field's presence: a compound condition becomes @PresentWhenAllSet, a single bit
    // becomes @FlagIndex, and an ungated field gets neither.
    private fun GattField.gatingAnnotations(): List<AnnotationSpec> = when {
        presenceFlagIndices.isNotEmpty() -> listOf(presentWhenAllSet(presenceFlagIndices))
        flagIndex != null -> listOf(flagIndex(flagIndex))
        else -> emptyList()
    }

    private fun flagWidth(bits: Int) = AnnotationSpec.builder(ClassName(SERIALIZATION, "FlagWidth")).addMember("bits = %L", bits).build()

    private fun serializedByteValue(discriminator: Int) = AnnotationSpec.builder(ClassName(SERIALIZATION, "SerializedByteValue"))
        .addMember("value = %L", discriminator)
        .build()

    private class Mapping(val type: TypeName, val annotations: List<AnnotationSpec>)

    private fun GattField.toMapping(): Mapping {
        val annotations = mutableListOf<AnnotationSpec>()
        val type: TypeName = when {
            format == "boolean" -> BOOLEAN

            format == "utf8s" -> STRING

            // UTF-8 is the @Encoded default

            format == "utf16s" -> STRING.also { annotations += encoded("UTF_16") }

            format == "SFLOAT" -> DOUBLE.also {
                annotations += size(16)
                annotations += MED_FLOAT
            }

            format == "FLOAT" -> DOUBLE.also {
                annotations += size(32)
                annotations += MED_FLOAT
            }

            format == "float32" -> FLOAT

            format == "float64" -> DOUBLE

            // A bare bit-width token (e.g. `8bit`, `16bit`) is an unsigned integer of that width.
            Regex("\\d+bit").matches(format) -> {
                val bits = integerWidth(format)
                annotations += size(bits)
                annotations += UNSIGNED
                integerType(bits, signed = false)
            }

            format.startsWith("uint") || format.startsWith("sint") -> {
                val signed = format.startsWith("sint")
                // A flags bit may select between widths (e.g. uint8/uint16); emit a @Size for each, picking the widest type.
                val widths = (listOf(format) + alternateFormats).map { integerWidth(it) }
                widths.sorted().forEach { annotations += size(it) }
                if (!signed) annotations += UNSIGNED
                integerType(widths.max(), signed)
            }

            else -> error("Unsupported GATT format '$format' for field '$name'")
        }
        val scaled = multiplier != 1 || decimalExponent != 0 || binaryExponent != 0
        if (scaled) annotations += scalar()
        // A scaled field holds a fractional physical value, so it is represented as a Double regardless of wire width.
        return Mapping(if (scaled) DOUBLE else type, annotations)
    }

    // The wire width (bits) of an integer or bare bit-width format token, validated against the @Size widths the
    // serializer's Length supports. A width with no Length (e.g. sub-byte 2/4-bit, the non-aligned 12-bit, or 128-bit
    // which has no Kotlin primitive) is rejected loudly rather than emitting a non-compiling @Size or overflowing.
    private fun GattField.integerWidth(format: String): Int {
        val bits = Regex("\\d+").find(format)?.value?.toIntOrNull()
            ?: error("Unsupported integer format '$format' for field '$name'")
        require(bits in SUPPORTED_INTEGER_WIDTHS) {
            "Unsupported integer width '$format' for field '$name'; supported widths (bits): ${SUPPORTED_INTEGER_WIDTHS.sorted()}"
        }
        return bits
    }

    // A signed Kotlin type wide enough to hold the format's value range; the wire width is fixed by @Size.
    private fun integerType(bits: Int, signed: Boolean): TypeName = when {
        signed -> if (bits <= 32) INT else LONG
        else -> if (bits <= 24) INT else LONG
    }

    private fun size(bits: Int) = AnnotationSpec.builder(ClassName(SERIALIZATION, "Size"))
        .addMember("%T.%L", ClassName(SERIALIZATION, "Length"), "`${bits}_BIT`")
        .build()

    // GATT defines the physical value as `raw * multiplier * 10^decimalExponent * 2^binaryExponent` (GATT Specification
    // Supplement; the multiplier is an integer in [-10, 10] and there is no offset term). @Scalar encodes the inverse
    // (physical -> raw), so each factor is reciprocated: the exponents negate, the multiplier's sign survives as the
    // integer @Scalar multiplier, and its magnitude folds into the exponents. A magnitude that is not a product of powers
    // of 10 and 2 has no integer reciprocal, so it cannot be represented and is rejected.
    private fun GattField.scalar(): AnnotationSpec {
        var magnitude = abs(multiplier)
        var decimalFromMultiplier = 0
        var binaryFromMultiplier = 0
        while (magnitude > 1 && magnitude % 10 == 0) { magnitude /= 10; decimalFromMultiplier++ }
        while (magnitude > 1 && magnitude % 2 == 0) { magnitude /= 2; binaryFromMultiplier++ }
        require(magnitude == 1) { "Unsupported GATT multiplier '$multiplier' for field '$name': magnitude is not a product of powers of 10 and 2" }
        val scalarMultiplier = if (multiplier < 0) -1 else 1
        val scalarDecimalExponent = -(decimalExponent + decimalFromMultiplier)
        val scalarBinaryExponent = -(binaryExponent + binaryFromMultiplier)
        return AnnotationSpec.builder(ClassName(SERIALIZATION, "Scalar")).apply {
            if (scalarMultiplier != 1) addMember("multiplier = %L", scalarMultiplier)
            if (scalarDecimalExponent != 0) addMember("decimalExponent = %L", scalarDecimalExponent)
            if (scalarBinaryExponent != 0) addMember("binaryExponent = %L", scalarBinaryExponent)
        }.build()
    }

    private fun encoded(encoding: String) = AnnotationSpec.builder(ClassName(SERIALIZATION, "Encoded"))
        .addMember("%T.%L", ClassName(BASE_BYTES, "Encoding"), encoding)
        .build()

    private fun annotation(simpleName: String, uuid: String) = AnnotationSpec.builder(ClassName(ANNOTATIONS, simpleName))
        .addMember("%S", uuid)
        .build()

    private fun accessAnnotation(property: GattProperty) = AnnotationSpec.builder(
        ClassName(
            ANNOTATIONS,
            when (property) {
                GattProperty.READ -> "Readable"
                GattProperty.WRITE -> "Writable"
                GattProperty.WRITE_WITHOUT_RESPONSE -> "WritableWithoutResponse"
                GattProperty.SIGNED_WRITE -> "WritableSigned"
                GattProperty.NOTIFY -> "Notifiable"
                GattProperty.INDICATE -> "Indicatable"
            },
        ),
    ).build()

    private companion object {
        const val SERIALIZATION = "com.splendo.kaluga.bluetooth.serialization"
        const val BASE_BYTES = "com.splendo.kaluga.base.bytes"
        const val ANNOTATIONS = "com.splendo.kaluga.bluetooth.annotations"
        const val SCIENTIFIC = "com.splendo.kaluga.scientific"
        const val SCIENTIFIC_UNIT = "com.splendo.kaluga.scientific.unit"
        const val VALUE_SUFFIX = "Value"
        // The Client Characteristic Configuration descriptor; not generated as it is managed by the notify/indicate layer.
        const val CCCD_TYPE = "org.bluetooth.descriptor.gatt.client_characteristic_configuration"
        const val CCCD_UUID = "2902"
        // The integer wire widths the serializer's `Length` enum can encode via @Size (byte-aligned, up to 64-bit).
        val SUPPORTED_INTEGER_WIDTHS = setOf(8, 16, 24, 32, 40, 48, 64)
        val JVM_INLINE = AnnotationSpec.builder(ClassName("kotlin.jvm", "JvmInline")).build()
        val SERIALIZABLE = AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable")).build()
        val UNSIGNED = AnnotationSpec.builder(ClassName(SERIALIZATION, "Unsigned")).build()
        val MED_FLOAT = AnnotationSpec.builder(ClassName(SERIALIZATION, "MedFloat")).build()
        val NULL_IF_EMPTY = AnnotationSpec.builder(ClassName(SERIALIZATION, "NullIfEmpty")).build()
        val UNSIZED = AnnotationSpec.builder(ClassName(SERIALIZATION, "Unsized")).build()
    }
}
