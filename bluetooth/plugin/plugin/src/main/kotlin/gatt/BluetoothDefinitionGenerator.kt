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
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

/**
 * Generates Kaluga `@Bluetooth` definitions from parsed GATT XML: the `@Serializable` value class for each
 * characteristic (translating field formats/scaling into `BluetoothFormat` annotations), the `@BluetoothCharacteristic`
 * and `@BluetoothService` interfaces, and the `@Bluetooth` device that ties them together.
 *
 * Conditional characteristics map according to their shape: a leading discriminator byte becomes a sealed class
 * (one `@SerializedByteValue`-tagged subclass per variant); a leading flags byte becomes a flat class where each bit
 * either selects a field's width (multiple `@Size`), gates a field's presence (nullable, or an `@Unsized` list for a
 * `repeated` field) or carries an enumerated value (`@FlagIndex` enum), all keyed by `@FlagIndex`.
 *
 * @param packageName the package the generated definitions are placed in.
 */
class BluetoothDefinitionGenerator(private val packageName: String) {

    /**
     * Generates all definitions for [deviceName] exposing [services], whose characteristic value structures are taken
     * from [characteristics] (linked by UUID). Returns one [FileSpec] per characteristic and service plus the device.
     */
    fun generate(deviceName: String, services: List<GattService>, characteristics: List<GattCharacteristic>): List<FileSpec> {
        val byUuid = characteristics.associateBy { it.uuid }
        val accessByUuid = services.flatMap { it.characteristics }
            .groupBy { it.uuid }
            .mapValues { (_, refs) -> refs.flatMap { it.properties }.toSet() }

        val characteristicFiles = byUuid.values.map { characteristic ->
            characteristicFile(characteristic, accessByUuid[characteristic.uuid].orEmpty())
        }
        val serviceFiles = services.map { service -> serviceFile(service, byUuid) }
        return characteristicFiles + serviceFiles + deviceFile(deviceName, services)
    }

    /** The `@BluetoothCharacteristic` interface for [characteristic] plus its `@Serializable` value class. */
    fun characteristicFile(characteristic: GattCharacteristic, access: Set<GattProperty>): FileSpec {
        val interfaceName = characteristic.name.toPascalCase()
        val valueType = ClassName(packageName, interfaceName + VALUE_SUFFIX)
        val value = PropertySpec.builder("value", valueType)
            .addModifiers(KModifier.ABSTRACT)
            .apply { access.ifEmpty { setOf(GattProperty.READ) }.forEach { addAnnotation(accessAnnotation(it)) } }
            .build()
        val characteristicInterface = TypeSpec.interfaceBuilder(interfaceName)
            .addAnnotation(annotation("BluetoothCharacteristic", characteristic.uuid))
            .addProperty(value)
            .build()
        return FileSpec.builder(packageName, interfaceName)
            .addType(characteristicInterface)
            .addType(valueType(characteristic, valueType.simpleName))
            .build()
    }

    /** The `@BluetoothService` interface for [service], referencing the characteristic interfaces resolved from [characteristics]. */
    fun serviceFile(service: GattService, characteristics: Map<String, GattCharacteristic>): FileSpec {
        val serviceName = service.name.toPascalCase()
        val builder = TypeSpec.interfaceBuilder(serviceName)
            .addAnnotation(annotation("BluetoothService", service.uuid))
        service.characteristics.forEach { ref ->
            val characteristic = characteristics[ref.uuid] ?: error("Service '${service.name}' references unknown characteristic UUID '${ref.uuid}'")
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
    fun generateValueClass(characteristic: GattCharacteristic): FileSpec {
        val className = characteristic.name.toPascalCase() + VALUE_SUFFIX
        return FileSpec.builder(packageName, className).addType(valueType(characteristic, className)).build()
    }

    private fun valueType(characteristic: GattCharacteristic, className: String): TypeSpec =
        if (characteristic.isVariant) sealedValueType(characteristic, className) else dataValueType(characteristic.fields, className, characteristic.flagFields)

    // A conditional characteristic becomes a sealed class; each variant is a subclass selected on the wire by its
    // discriminator byte (@SerializedByteValue), which the BluetoothFormat already dispatches on.
    private fun sealedValueType(characteristic: GattCharacteristic, className: String): TypeSpec {
        val superType = ClassName(packageName, className)
        val sealed = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.SEALED)
            .addAnnotation(SERIALIZABLE)
        characteristic.variants.forEach { variant ->
            sealed.addType(
                dataValueType(variant.fields, variant.name.toPascalCase()).toBuilder()
                    .addAnnotation(serializedByteValue(variant.discriminator))
                    .superclass(superType)
                    .build(),
            )
        }
        return sealed.build()
    }

    private fun dataValueType(fields: List<GattField>, className: String, flagFields: List<GattFlagField> = emptyList()): TypeSpec {
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
            val mapping = field.toMapping()
            val (propertyType, annotations) = when {
                // A repeated field fills the rest of the packet as an unsized list; the element formatting moves onto
                // the list via the @Item* annotations. When gated by a flag bit, presence is encoded via @NullIfEmpty.
                field.repeated -> LIST.parameterizedBy(mapping.type) to
                    mapping.annotations.map { it.asItemAnnotation() } + UNSIZED +
                    (field.flagIndex?.let { listOf(flagIndex(it), NULL_IF_EMPTY) } ?: emptyList())

                else -> (if (field.optional) mapping.type.copy(nullable = true) else mapping.type) to
                    mapping.annotations + listOfNotNull(field.flagIndex?.let(::flagIndex))
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

    // A readable UPPER_SNAKE constant from the spec's case text, falling back to the wire key; disambiguated on clash.
    private fun enumCaseName(description: String?, key: Int, used: MutableSet<String>): String {
        val slug = description?.uppercase()?.replace(Regex("[^A-Z0-9]+"), "_")?.trim('_')
            ?.split("_")?.filter { it.isNotEmpty() }?.take(4)?.joinToString("_")
            ?.takeIf { it.isNotEmpty() && it.first().isLetter() }
            ?: "VALUE_$key"
        return if (used.add(slug)) slug else "${slug}_$key".also { used.add(it) }
    }

    private fun flagIndex(index: Int) = AnnotationSpec.builder(ClassName(SERIALIZATION, "FlagIndex")).addMember("%L", index).build()

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

            format.startsWith("uint") || format.startsWith("sint") -> {
                val signed = format.startsWith("sint")
                // A flags bit may select between widths (e.g. uint8/uint16); emit a @Size for each, picking the widest type.
                val widths = (listOf(format) + alternateFormats).map { it.drop(4).toIntOrNull() ?: error("Unsupported integer format '$it' for field '$name'") }
                widths.sorted().forEach { annotations += size(it) }
                if (!signed) annotations += UNSIGNED
                integerType(widths.max(), signed)
            }

            else -> error("Unsupported GATT format '$format' for field '$name'")
        }
        if (multiplier != 1 || decimalExponent != 0 || binaryExponent != 0) {
            annotations += scalar()
        }
        return Mapping(type, annotations)
    }

    // A signed Kotlin type wide enough to hold the format's value range; the wire width is fixed by @Size.
    private fun integerType(bits: Int, signed: Boolean): TypeName = when {
        signed -> if (bits <= 32) INT else LONG
        else -> if (bits <= 24) INT else LONG
    }

    private fun size(bits: Int) = AnnotationSpec.builder(ClassName(SERIALIZATION, "Size"))
        .addMember("%T.%L", ClassName(SERIALIZATION, "Length"), "`${bits}_BIT`")
        .build()

    private fun GattField.scalar(): AnnotationSpec = AnnotationSpec.builder(ClassName(SERIALIZATION, "Scalar")).apply {
        if (multiplier != 1) addMember("multiplier = %L", multiplier)
        if (decimalExponent != 0) addMember("decimalExponent = %L", decimalExponent)
        if (binaryExponent != 0) addMember("binaryExponent = %L", binaryExponent)
    }.build()

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
                GattProperty.NOTIFY -> "Notifiable"
                GattProperty.INDICATE -> "Indicatable"
            },
        ),
    ).build()

    private companion object {
        const val SERIALIZATION = "com.splendo.kaluga.bluetooth.serialization"
        const val BASE_BYTES = "com.splendo.kaluga.base.bytes"
        const val ANNOTATIONS = "com.splendo.kaluga.bluetooth.annotations"
        const val VALUE_SUFFIX = "Value"
        val SERIALIZABLE = AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable")).build()
        val UNSIGNED = AnnotationSpec.builder(ClassName(SERIALIZATION, "Unsigned")).build()
        val MED_FLOAT = AnnotationSpec.builder(ClassName(SERIALIZATION, "MedFloat")).build()
        val NULL_IF_EMPTY = AnnotationSpec.builder(ClassName(SERIALIZATION, "NullIfEmpty")).build()
        val UNSIZED = AnnotationSpec.builder(ClassName(SERIALIZATION, "Unsized")).build()
    }
}
