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
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
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
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

internal class BluetoothRemoteDescriptorBuilder(declaration: KSClassDeclaration, private val descriptor: BluetoothDescriptor, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, logger) {

    companion object {
        const val FROM_CHARACTERISTIC = "fromCharacteristic"
    }

    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec =
        TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType))
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
            .generateBody(declarations, generationType)
            .build()

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec {
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this)
        val className = NameHelper.nameFor(this, generationType)
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DESCRIPTOR, References.Bluetooth.remoteDescriptor),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(FROM_CHARACTERISTIC)
                            .addParameters(
                                listOfNotNull(
                                    ParameterSpec(CHARACTERISTIC, References.Bluetooth.remoteCharacteristic),
                                    ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                                ),
                            )
                            .returns(className)
                            .addStatement(
                                "$RETURN %T($CHARACTERISTIC.descriptors.%M(%T.$UUID)${if (needsFormatter) ", $FORMAT" else ""})",
                                className,
                                References.Bluetooth.get,
                                NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)),
                            )
                            .build(),
                    )
                    .build(),
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(DESCRIPTOR, References.Bluetooth.remoteDescriptor)
                        .initializer(DESCRIPTOR).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT).build().takeIf { needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, generationType)
            .build()
    }

    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec {
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        val readProperty = properties.firstOrNull { it.isReadable }
        val writeProperty = properties.firstOrNull { it.isAnnotationPresent(Writable::class) }
        return TypeSpec.classBuilder(NameHelper.nameFor(this, generationType))
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            readProperty?.let { onRead ->
                                ParameterSpec(
                                    "${onRead.onReadMethodName}$ACTION",
                                    LambdaTypeName.get(
                                        returnType = BluetoothResultTypeBuilder(this, onRead, logger).responseClassName,
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
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addProperties(
                listOfNotNull(
                    readProperty?.let { onRead ->
                        PropertySpec.builder(
                            "${onRead.onReadMethodName}$ACTION",
                            LambdaTypeName.get(
                                returnType = BluetoothResultTypeBuilder(this, onRead, logger).responseClassName,
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
            .generateBody(declarations, generationType)
            .build()
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType): TypeSpec.Builder = apply {
        var hasReadMethod = false
        var hasWriteMethod = false
        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
            if (propertyDeclaration.isReadable) {
                if (!hasReadMethod) {
                    hasReadMethod = true
                    val responseType = propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }
                    val readMethod = "$READ$responseType"
                    val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, logger)
                    addFunction(
                        FunSpec.builder(readMethod).addModifiers(KModifier.SUSPEND, *generationType.additionalModifiers.toTypedArray()).returns(
                            resultType.responseClassName,
                        ).apply {
                            when (generationType.type) {
                                GenerationType.Type.API -> {}

                                GenerationType.Type.BLUETOOTH -> {
                                    resultType.generateBluetoothResult(this, DESCRIPTOR)
                                }

                                GenerationType.Type.SIMULATOR -> {
                                    addStatement("$RETURN ${propertyDeclaration.onReadMethodName}$ACTION()")
                                }
                            }
                        }
                            .build(),
                    )
                } else {
                    logger.error("Only one @${Readable::class.simpleName} property can be declared")
                }
            }

            if (propertyDeclaration.isAnnotationPresent(Writable::class)) {
                if (!hasWriteMethod) {
                    hasWriteMethod = true
                    val writeMethod = "$WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                    addFunction(
                        FunSpec.builder(
                            writeMethod,
                        ).addParameter(
                            propertyDeclaration.simpleName.asString(),
                            propertyDeclaration.type.resolve().toTypeName(),
                        ).addModifiers(KModifier.SUSPEND, *generationType.additionalModifiers.toTypedArray()).returns(
                            References.Bluetooth.writeResponse,
                        ).apply {
                            when (generationType.type) {
                                GenerationType.Type.API -> {}

                                GenerationType.Type.BLUETOOTH -> {
                                    if (propertyDeclaration.isByteArray) {
                                        addStatement("$RETURN $DESCRIPTOR.$WRITE(${propertyDeclaration.simpleName.asString()})")
                                    } else {
                                        addStatement(
                                            "$RETURN $DESCRIPTOR.$WRITE($FORMAT.encodeToByteArray(%L, ${propertyDeclaration.simpleName.asString()}))",
                                            propertyDeclaration.type.resolve().toTypeName().serializer(logger),
                                        )
                                    }
                                }

                                GenerationType.Type.SIMULATOR -> {
                                    addStatement("$RETURN ${propertyDeclaration.onWriteMethodName}$ACTION(${propertyDeclaration.simpleName.asString()})")
                                }
                            }
                        }
                            .build(),
                    )
                } else {
                    logger.error("Only one @${Readable::class.simpleName} property can be declared")
                }
            }

            if (!propertyDeclaration.isAnnotationPresent(Readable::class) && !propertyDeclaration.isAnnotationPresent(Writable::class)) {
                logger.error("Only @${Readable::class.simpleName} and @${Writable::class.simpleName} properties can be declared")
            }
        }
    }
}
