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
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.DESCRIPTOR
import com.splendo.kaluga.bluetooth.ksp.helpers.DESCRIPTORS
import com.splendo.kaluga.bluetooth.ksp.helpers.ENCODE_TO_BYTE_ARRAY
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.FROM_CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.IT
import com.splendo.kaluga.bluetooth.ksp.helpers.LET
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OR_NULL
import com.splendo.kaluga.bluetooth.ksp.helpers.READ
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.splendo.kaluga.bluetooth.ksp.helpers.isReadable
import com.splendo.kaluga.bluetooth.ksp.helpers.onReadMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.onWriteMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.serializer
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

internal class BluetoothRemoteDescriptorBuilder(declaration: KSClassDeclaration, private val descriptor: BluetoothDescriptor, options: Options, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, options, logger) {

    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.CLIENT_API)
        return TypeSpec.interfaceBuilder(interfaceName)
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder(UUID, References.Bluetooth.uuid)
                            .initializer("%M(%S)", References.Bluetooth.uuidFrom, descriptor.uuid)
                            .build(),
                    )
                    .build(),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.API)
            .build()
    }

    override fun generateBluetooth(nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(declaration, GenerationType.CLIENT_BLUETOOTH)
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.CLIENT_API)
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration)
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DESCRIPTOR, References.Bluetooth.remoteDescriptor),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addType(
                generateBluetoothCompanionObject(needsFormatter, className, interfaceName),
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(DESCRIPTOR, References.Bluetooth.remoteDescriptor)
                        .initializer(DESCRIPTOR).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT).build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.BLUETOOTH)
            .build()
    }

    private fun generateBluetoothCompanionObject(needsFormatter: NeedsFormatterHelper.NeedsFormatter, className: ClassName, interfaceName: ClassName): TypeSpec =
        TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder(FROM_CHARACTERISTIC)
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(CHARACTERISTIC, References.Bluetooth.remoteCharacteristic),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .returns(className)
                    .addStatement(
                        "$RETURN %T($CHARACTERISTIC.$DESCRIPTORS.%M(%T.$UUID)${needsFormatter.functionArgument})",
                        className,
                        References.Bluetooth.get,
                        interfaceName,
                    )
                    .build(),
            )
            .addFunction(
                FunSpec.builder("$FROM_CHARACTERISTIC$OR_NULL")
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(CHARACTERISTIC, References.Bluetooth.remoteCharacteristic),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .returns(className.copy(nullable = true))
                    .addCode(
                        CodeBlock.builder()
                            .beginControlFlow(
                                "$RETURN $CHARACTERISTIC.$DESCRIPTORS.%M(%T.$UUID)?.$LET",
                                References.Bluetooth.getOrNull,
                                interfaceName,
                            )
                            .addStatement("%T($IT${needsFormatter.functionArgument})", className)
                            .endControlFlow()
                            .build(),
                    )
                    .build(),
            )
            .build()

    override fun generateSimulated(nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.CLIENT_API)
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        val readProperty = properties.firstOrNull { it.isReadable }
        val writeProperty = properties.firstOrNull { it.isAnnotationPresent(Writable::class) }
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            readProperty?.let { onRead ->
                                ParameterSpec(
                                    "${onRead.onReadMethodName}$ACTION",
                                    LambdaTypeName.get(
                                        returnType = BluetoothResultTypeBuilder(declaration, onRead, options, logger).responseClassName,
                                    ).copy(suspending = true),
                                )
                            },
                            writeProperty?.let { onWrite ->
                                ParameterSpec(
                                    "${onWrite.onWriteMethodName}$ACTION",
                                    LambdaTypeName.get(
                                        parameters = listOf(ParameterSpec(onWrite.simpleName.asString(), onWrite.type.resolve().toTypeName())),
                                        returnType = References.Bluetooth.writeResponse,
                                    ).copy(suspending = true),
                                )
                            },
                        ),
                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addProperties(
                listOfNotNull(
                    readProperty?.let { onRead ->
                        PropertySpec.builder(
                            "${onRead.onReadMethodName}$ACTION",
                            LambdaTypeName.get(
                                returnType = BluetoothResultTypeBuilder(declaration, onRead, options, logger).responseClassName,
                            ).copy(suspending = true),
                        )
                            .addModifiers(KModifier.PRIVATE)
                            .initializer("${onRead.onReadMethodName}$ACTION").build()
                    },
                    writeProperty?.let { onWrite ->
                        PropertySpec.builder(
                            "${onWrite.onWriteMethodName}$ACTION",
                            LambdaTypeName.get(
                                parameters = listOf(ParameterSpec(onWrite.simpleName.asString(), onWrite.type.resolve().toTypeName())),
                                returnType = References.Bluetooth.writeResponse,
                            ).copy(suspending = true),
                        )
                            .addModifiers(KModifier.PRIVATE)
                            .initializer("${onWrite.onWriteMethodName}$ACTION").build()
                    },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.SIMULATOR)
            .build()
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, type: GenerationType.Type): TypeSpec.Builder = apply {
        var hasReadMethod = false
        var hasWriteMethod = false
        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
            if (propertyDeclaration.isReadable) {
                if (!hasReadMethod) {
                    hasReadMethod = true
                    addFunction(generateReadMethod(propertyDeclaration, type))
                } else {
                    logOnlyOneProperty(Readable::class)
                }
            }

            if (propertyDeclaration.isAnnotationPresent(Writable::class)) {
                if (!hasWriteMethod) {
                    hasWriteMethod = true
                    addFunction(generateWriteMethod(propertyDeclaration, type))
                } else {
                    logOnlyOneProperty(Writable::class)
                }
            }

            if (!propertyDeclaration.isAnnotationPresent(Readable::class) && !propertyDeclaration.isAnnotationPresent(Writable::class)) {
                invalidProperty(propertyDeclaration, Readable::class, Writable::class)
            }
        }
    }

    private fun generateReadMethod(propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): FunSpec {
        val responseType = propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }
        val readMethod = "$READ$responseType"
        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, options, logger)
        return FunSpec.builder(readMethod).addModifiers(KModifier.SUSPEND, *type.additionalModifiers.toTypedArray()).returns(
            resultType.responseClassName,
        ).apply {
            when (type) {
                GenerationType.Type.API -> {}

                GenerationType.Type.BLUETOOTH -> {
                    resultType.generateBluetoothResult(this, DESCRIPTOR)
                }

                GenerationType.Type.SIMULATOR -> {
                    addStatement("$RETURN ${propertyDeclaration.onReadMethodName}$ACTION()")
                }
            }
        }
            .build()
    }

    private fun generateWriteMethod(propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): FunSpec {
        val writeMethod = "$WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
        return FunSpec.builder(
            writeMethod,
        ).addParameter(
            propertyDeclaration.simpleName.asString(),
            propertyDeclaration.type.resolve().toTypeName(),
        ).addModifiers(KModifier.SUSPEND, *type.additionalModifiers.toTypedArray()).returns(
            References.Bluetooth.writeResponse,
        ).apply {
            when (type) {
                GenerationType.Type.API -> {}

                GenerationType.Type.BLUETOOTH -> {
                    if (propertyDeclaration.isByteArray) {
                        addStatement("$RETURN $DESCRIPTOR.$WRITE(${propertyDeclaration.simpleName.asString()})")
                    } else {
                        addStatement(
                            "$RETURN $DESCRIPTOR.$WRITE($FORMAT.$ENCODE_TO_BYTE_ARRAY(%L, ${propertyDeclaration.simpleName.asString()}))",
                            propertyDeclaration.type.resolve().toTypeName().serializer(logger),
                        )
                    }
                }

                GenerationType.Type.SIMULATOR -> {
                    addStatement("$RETURN ${propertyDeclaration.onWriteMethodName}$ACTION(${propertyDeclaration.simpleName.asString()})")
                }
            }
        }
            .build()
    }
}
