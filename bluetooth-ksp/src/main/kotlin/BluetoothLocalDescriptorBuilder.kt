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

package com.splendo.kaluga.bluetooth.ksp

import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.Encrypted
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.DESCRIPTOR
import com.splendo.kaluga.bluetooth.ksp.helpers.EXCEPTION
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_FAILED_TO_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_READ
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.splendo.kaluga.bluetooth.ksp.helpers.serializer
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal class BluetoothLocalDescriptorBuilder(
    declaration: KSClassDeclaration,
    private val descriptor: BluetoothDescriptor,
    logger: KSPLogger
) : AbstractBluetoothClassBuilder(declaration, logger) {
    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType))
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder(DELEGATE)
                    .apply {
                        val receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API))
                        var hasReadMethod = false
                        var hasWriteMethod = false
                        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                            if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                                if (!hasReadMethod) {
                                    hasReadMethod = true
                                    val readMethod = "$ON_READ${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                    val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, logger)

                                    val onReadFunSpec = FunSpec.builder(readMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                                        .receiver(receiver)
                                        .addParameters(
                                            listOfNotNull(
                                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                                                ParameterSpec(OFFSET, INT).takeIf { !resultType.hasCustomResult }
                                            )
                                        )
                                        .returns(resultType.responseClassName)

                                    addFunction(
                                        onReadFunSpec
                                            .build(),
                                    )
                                } else {
                                    logger.error("Only one @${Readable::class.simpleName} property can be declared")
                                }
                            }

                            if (propertyDeclaration.isAnnotationPresent(Writable::class)) {
                                if (!hasWriteMethod) {
                                    hasWriteMethod = true

                                    val writeMethod = "$ON_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                                    val onWriteFunSpec = FunSpec.builder(writeMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                                        .receiver(receiver)
                                        .addParameter(propertyDeclaration.simpleName.asString(), propertyDeclaration.type.resolve().toTypeName())
                                        .apply {
                                            if (propertyDeclaration.isByteArray) {
                                                addParameter(OFFSET, INT)
                                            }
                                        }
                                        .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                        .returns(References.Bluetooth.writeResponse)

                                    addFunction(
                                        onWriteFunSpec
                                            .build(),
                                    )
                                    if (!propertyDeclaration.isByteArray) {
                                        val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                                        val onFailedToWriteFunSpec = FunSpec.builder(failedToWriteMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                                            .receiver(receiver)
                                            .addParameter(EXCEPTION, References.Kotlin.exception)
                                            .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                            .returns(References.Bluetooth.writeResponse)

                                        addFunction(
                                            onFailedToWriteFunSpec
                                                .build(),
                                        )
                                    }
                                } else {
                                    logger.error("Only one @${Writable::class.simpleName} property can be declared")
                                }
                            }

                            if (!propertyDeclaration.isAnnotationPresent(Readable::class) && !propertyDeclaration.isAnnotationPresent(Writable::class)) {
                                logger.error("Only @${Readable::class.simpleName} and @${Writable::class.simpleName} properties can be declared")
                            }
                        }
                    }
                    .build(),
            )
        return Generated(listOf(typeSpec.build()))
    }

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val className = NameHelper.nameFor(this, generationType)
        val typeSpec = TypeSpec.classBuilder(className).addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DESCRIPTOR, References.Bluetooth.Server.localDescriptor),
                        )

                    )
                    .build()
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(CONFIGURE)
                            .apply {
                                val delegateName = "${declaration.simpleName.asString().replaceFirstChar { it.lowercase() }}$DELEGATE"
                                addParameter(BUILDER, References.Bluetooth.Server.localCharacteristicDSL)
                                addParameter(
                                    delegateName,
                                    NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE)
                                )
                                val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER_DSL)
                                    if (needsFormatter) {
                                        addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                                    }
                                addCode(
                                    CodeBlock.builder()
                                        .beginControlFlow("$RETURN $BUILDER.descriptor(%M(%S)) {", References.Bluetooth.uuidFrom, descriptor.uuid)
                                        .apply {
                                            var hasReadMethod = false
                                            var hasWriteMethod = false
                                            declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                                                if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                                                    if (!hasReadMethod) {
                                                        hasReadMethod = true
                                                        val readMethod = "$ON_READ${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                                        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, logger)
                                                        beginControlFlow("readable(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, $OFFSET ->")
                                                            .beginControlFlow("$WITH($delegateName)")
                                                            .add(resultType.parseBluetoothResult(CodeBlock.of("%T($THIS@readable).${readMethod}(device.identifier", NameHelper.nameFor(declaration, generationType))))
                                                            .endControlFlow()
                                                            .endControlFlow()
                                                    } else {
                                                        logger.error("Only one @${Readable::class.simpleName} property can be declared")
                                                    }
                                                }
                                                if (propertyDeclaration.isAnnotationPresent(Writable::class)) {
                                                    if (!hasWriteMethod) {
                                                        hasWriteMethod = true
                                                        val writeMethod = "$ON_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                                        if (propertyDeclaration.isByteArray) {
                                                            beginControlFlow("writable(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, value, $OFFSET ->")
                                                                .beginControlFlow("$WITH($delegateName)")
                                                                .addStatement("%T($THIS@writable).${writeMethod}(value, $OFFSET, device.identifier)", NameHelper.nameFor(declaration, generationType))
                                                                .endControlFlow()
                                                                .endControlFlow()
                                                        } else {
                                                            val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                                                addStatement("writable(")
                                                                .indent()
                                                                .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                                                                .addStatement("deserializationStrategy = %L,", propertyDeclaration.type.resolve().toTypeName().serializer(logger))
                                                                .addStatement("bluetoothFormat = $FORMAT,")
                                                                .addStatement("onFailedToWrite = { device, exception ->")
                                                                .indent()
                                                                    .beginControlFlow("with($delegateName)")
                                                                    .addStatement("%T($THIS@writable).$failedToWriteMethod(exception, device.identifier)", NameHelper.nameFor(declaration, generationType))
                                                                    .endControlFlow()
                                                                    .unindent()
                                                                .addStatement("},")
                                                                .addStatement("onWrite = { device, value ->")
                                                                .indent()
                                                                    .beginControlFlow("$WITH($delegateName)")
                                                                .addStatement("%T($THIS@writable).$writeMethod(value, device.identifier)", NameHelper.nameFor(declaration, generationType))
                                                                    .endControlFlow()
                                                                .unindent()
                                                                .addStatement("}")
                                                                .unindent()
                                                                .addStatement(")")
                                                        }

                                                    } else {
                                                        logger.error("Only one @${Writable::class.simpleName} property can be declared")
                                                    }
                                                }
                                                if (!propertyDeclaration.isAnnotationPresent(Readable::class) && !propertyDeclaration.isAnnotationPresent(Writable::class)) {
                                                    logger.error("Only @${Readable::class.simpleName} and @${Writable::class.simpleName} properties can be declared")
                                                }
                                            }
                                        }
                                        .endControlFlow()
                                        .build()
                                )
                            }
                            .build()
                    )
                    .build()
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(DESCRIPTOR, References.Bluetooth.Server.localDescriptor)
                        .initializer(DESCRIPTOR).build(),
                )
            )
            .addTypes(nested)
        return Generated(listOf(typeSpec.build()), imports)
    }

    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val className = NameHelper.nameFor(this, generationType)
        val typeSpec = TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DESCRIPTOR, References.Bluetooth.Server.localDescriptor),
                        )

                    )
                    .build()
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addTypes(nested)
        return Generated(listOf(typeSpec.build()), imports)
    }
}
