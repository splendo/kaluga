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
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

/**
 * Generates the `@Serializable` value class for a [GattCharacteristic], translating each GATT field's format and
 * scaling into the Kaluga `BluetoothFormat` annotations (`@Size`, `@Unsigned`, `@Scalar`, `@MedFloat`, `@Encoded`).
 *
 * Prototype: handles plain (non flag-conditional) characteristics only; conditional characteristics will map to sealed
 * classes in a later increment.
 */
class BluetoothDefinitionGenerator(private val packageName: String) {

    fun generateValueClass(characteristic: GattCharacteristic): FileSpec {
        val className = characteristic.name.toPascalCase()
        val constructor = FunSpec.constructorBuilder()
        val type = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addAnnotation(SERIALIZABLE)

        characteristic.fields.forEach { field ->
            val propertyName = field.name.toCamelCase()
            val mapping = field.toMapping()
            constructor.addParameter(ParameterSpec.builder(propertyName, mapping.type).build())
            type.addProperty(
                PropertySpec.builder(propertyName, mapping.type)
                    .initializer(propertyName)
                    .apply { mapping.annotations.forEach(::addAnnotation) }
                    .build(),
            )
        }
        return FileSpec.builder(packageName, className)
            .addType(type.primaryConstructor(constructor.build()).build())
            .build()
    }

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
                val bits = format.drop(4).toIntOrNull() ?: error("Unsupported integer format '$format' for field '$name'")
                annotations += size(bits)
                if (!signed) annotations += UNSIGNED
                integerType(bits, signed)
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

    private companion object {
        const val SERIALIZATION = "com.splendo.kaluga.bluetooth.serialization"
        const val BASE_BYTES = "com.splendo.kaluga.base.bytes"
        val SERIALIZABLE = AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable")).build()
        val UNSIGNED = AnnotationSpec.builder(ClassName(SERIALIZATION, "Unsigned")).build()
        val MED_FLOAT = AnnotationSpec.builder(ClassName(SERIALIZATION, "MedFloat")).build()
    }
}
