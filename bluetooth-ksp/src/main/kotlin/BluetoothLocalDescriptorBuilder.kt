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
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.Encrypted
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.ksp.BluetoothRemoteDescriptorBuilder.Companion.DESCRIPTOR
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILD
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.DSL
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_FAILED_TO_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_READ
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

internal class BluetoothLocalDescriptorBuilder(
    declaration: KSClassDeclaration,
    private val descriptor: BluetoothDescriptor,
    logger: KSPLogger
) : AbstractBluetoothClassBuilder(declaration, logger) {
    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType)).addModifiers(KModifier.SEALED)
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder(DSL)
                    .generateDSLBody(this@generateAPI, declarations, generationType, imports, CodeBlock.builder())
                    .build(),
            )
            .generateBody(declarations, generationType, imports)
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
                TypeSpec.classBuilder(DSL)
                    .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)).nestedClass(DSL))
                    .apply {
                        val buildBody = CodeBlock.builder()

                        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER_DSL)
                        generateDSLBody(this@generateBluetooth, declarations, generationType, imports, buildBody)
                            .addFunction(
                                FunSpec.builder(BUILD)
                                    .addParameters(
                                        listOfNotNull(
                                            ParameterSpec(BUILDER, References.Bluetooth.Server.localCharacteristicDSL),
                                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                                        )
                                    )
                                    .addCode(
                                        CodeBlock.builder()
                                            .addStatement("$BUILDER.descriptor(%M(%S)) {", References.Bluetooth.uuidFrom, descriptor.uuid)
                                            .indent()
                                            .add(buildBody.build())
                                            .unindent()
                                            .addStatement("}")
                                            .build()
                                    )
                                    .build()
                            )
                    }
                    .build()
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(DESCRIPTOR, References.Bluetooth.Server.localDescriptor)
                        .initializer(DESCRIPTOR).build(),
                )
            )
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
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
            .addType(
                TypeSpec.classBuilder(DSL)
                    .generateDSLBody(this@generateSimulated, declarations, generationType, imports, CodeBlock.builder())
                    .build()
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(DESCRIPTOR, References.Bluetooth.Server.localDescriptor)
                        .initializer(DESCRIPTOR).build(),
                )
            )
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }


    private fun TypeSpec.Builder.generateDSLBody(
        declaration: KSClassDeclaration,
        declarations: Sequence<KSDeclaration>, generationType: GenerationType,
        imports: Generated.Imports,
        builderBody: CodeBlock.Builder
    ): TypeSpec.Builder = apply {
        var hasReadMethod = false
        var hasWriteMethod = false
        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
            if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                if (!hasReadMethod) {
                    hasReadMethod = true
                    val readMethod = "$ON_READ${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                    val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration)

                    val lambdaType = LambdaTypeName.get(
                        receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                        parameters = listOfNotNull(
                            ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                            ParameterSpec(OFFSET, INT).takeIf { !resultType.hasCustomResult }
                        ),
                        returnType = resultType.responseClassName,
                    ).copy(suspending = true)

                    val onReadFunSpec = FunSpec.builder(readMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                        .addParameter(ACTION, lambdaType)

                    when (generationType.type) {
                        GenerationType.Type.API -> {}

                        GenerationType.Type.BLUETOOTH -> {
                            val readAction = "${readMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                            addProperty(
                                PropertySpec.builder(readAction, lambdaType)
                                    .addModifiers(KModifier.PRIVATE)
                                    .mutable(true)
                                    .initializer(
                                        CodeBlock.builder()
                                            .addStatement("{ _ ->")
                                            .indent()
                                            .apply {
                                                resultType.generateDefaultResult(this)
                                            }
                                            .unindent()
                                            .addStatement("}")
                                            .build()
                                    )
                                    .build()
                            )

                            onReadFunSpec.addStatement(
                                "$readAction = $ACTION"
                            )

                            builderBody
                                .addStatement("readable(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, $OFFSET ->")
                                .indent()
                                .add(resultType.parseBluetoothResult(CodeBlock.of("%T(this).${readAction}(device.identifier)", NameHelper.nameFor(declaration, generationType))))
                                .unindent()
                                .addStatement("}")
                        }

                        GenerationType.Type.SIMULATOR -> {

                        }
                    }

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

                    if (propertyDeclaration.isByteArray) {
                        val writeMethod = "$ON_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                        val lambdaType = LambdaTypeName.get(
                            receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                            parameters = listOf(
                                ParameterSpec(propertyDeclaration.simpleName.asString(), BYTE_ARRAY),
                                ParameterSpec(IDENTIFIER, INT),
                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                            ),
                            returnType = References.Bluetooth.writeResponse,
                        ).copy(suspending = true)

                        val onWriteFunSpec = FunSpec.builder(writeMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                            .addParameter(ACTION, lambdaType)

                        when (generationType.type) {
                            GenerationType.Type.API -> {}

                            GenerationType.Type.BLUETOOTH -> {
                                val writeAction = "${writeMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                addProperty(
                                    PropertySpec.builder(writeAction, lambdaType)
                                        .addModifiers(KModifier.PRIVATE)
                                        .mutable(true)
                                        .initializer(
                                            CodeBlock.builder()
                                                .addStatement("{ _, _ ->")
                                                .indent()
                                                .addStatement("%T", References.Bluetooth.writeRequestRejected)
                                                .unindent()
                                                .addStatement("}")
                                                .build()
                                        )
                                        .build()
                                )

                                onWriteFunSpec.addStatement(
                                    "$writeAction = $ACTION"
                                )

                                builderBody
                                    .addStatement("writable(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, value, $OFFSET ->")
                                    .indent()
                                    .addStatement("%T(this).${writeAction}(value, $OFFSET, device.identifier)", NameHelper.nameFor(declaration, generationType))
                                    .unindent()
                                    .addStatement("}")
                            }

                            GenerationType.Type.SIMULATOR -> {

                            }
                        }

                        addFunction(
                            onWriteFunSpec
                                .build(),
                        )
                    } else {
                        val writeMethod = "$ON_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                        val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                        val writeLambdaType = LambdaTypeName.get(
                            receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                            parameters = listOf(
                                ParameterSpec(propertyDeclaration.simpleName.asString(), propertyDeclaration.type.resolve().toClassName()),
                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                            ),
                            returnType = References.Bluetooth.writeResponse,
                        ).copy(suspending = true)

                        val failedToWriteLambdaType = LambdaTypeName.get(
                            receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                            parameters = listOf(
                                ParameterSpec(propertyDeclaration.simpleName.asString(), References.Kotlin.exception),
                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                            ),
                            returnType = References.Bluetooth.writeResponse,
                        ).copy(suspending = true)

                        val onWriteFunSpec = FunSpec.builder(writeMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                            .addParameter(ACTION, writeLambdaType)
                        val onFailedToWriteFunSpec = FunSpec.builder(failedToWriteMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                            .addParameter(ACTION, writeLambdaType)

                        when (generationType.type) {
                            GenerationType.Type.API -> {}

                            GenerationType.Type.BLUETOOTH -> {
                                val writeAction = "${writeMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                val failedToWriteAction = "${failedToWriteMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                addProperty(
                                    PropertySpec.builder(writeAction, writeLambdaType)
                                        .addModifiers(KModifier.PRIVATE)
                                        .mutable(true)
                                        .initializer(
                                            CodeBlock.builder()
                                                .addStatement("{ _, _ ->")
                                                .indent()
                                                .addStatement("%T", References.Bluetooth.writeRequestRejected)
                                                .unindent()
                                                .addStatement("}")
                                                .build()
                                        )
                                        .build()
                                )
                                addProperty(
                                    PropertySpec.builder(failedToWriteAction, failedToWriteLambdaType)
                                        .addModifiers(KModifier.PRIVATE)
                                        .mutable(true)
                                        .initializer(
                                            CodeBlock.builder()
                                                .addStatement("{ _, _ ->")
                                                .indent()
                                                .addStatement("%T", References.Bluetooth.writeRequestRejected)
                                                .unindent()
                                                .addStatement("}")
                                                .build()
                                        )
                                        .build()
                                )

                                onWriteFunSpec.addStatement(
                                    "$writeAction = $ACTION"
                                )
                                onFailedToWriteFunSpec.addStatement(
                                    "$failedToWriteAction = $ACTION"
                                )

                                builderBody
                                    .addStatement("writable(")
                                    .indent()
                                    .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                                    .addStatement("deserializationStrategy = %T.%M(),", propertyDeclaration.type.resolve().toClassName(), References.KotlinX.Serialization.serializer)
                                    .addStatement("bluetoothFormat = $FORMAT,")
                                    .addStatement("onFailedToWrite = { device, exception ->")
                                    .indent()
                                    .addStatement("%T(this).$failedToWriteAction(exception, device.identifier)", NameHelper.nameFor(declaration, generationType))
                                    .unindent()
                                    .addStatement("},")
                                    .addStatement("onWrite = { device, value ->")
                                    .indent()
                                    .addStatement("%T(this).$writeAction(value, device.identifier)", NameHelper.nameFor(declaration, generationType))
                                    .unindent()
                                    .addStatement("}")
                                    .unindent()
                                    .addStatement(")")
                            }

                            GenerationType.Type.SIMULATOR -> {

                            }
                        }

                        addFunction(
                            onWriteFunSpec
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
    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType, imports: Generated.Imports): TypeSpec.Builder = apply {

    }
}
