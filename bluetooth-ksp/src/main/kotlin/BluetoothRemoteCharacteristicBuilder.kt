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
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.Indicatable
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.annotations.WritableSigned
import com.splendo.kaluga.bluetooth.annotations.WritableWithoutResponse
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.FROM_SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.READ
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.splendo.kaluga.bluetooth.ksp.helpers.isNotifiable
import com.splendo.kaluga.bluetooth.ksp.helpers.isReadable
import com.splendo.kaluga.bluetooth.ksp.helpers.isWritable
import com.splendo.kaluga.bluetooth.ksp.helpers.onReadMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.onWriteMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.serializer
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal class BluetoothRemoteCharacteristicBuilder(declaration: KSClassDeclaration, private val characteristic: BluetoothCharacteristic, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, logger) {

    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.CLIENT_API)
        return TypeSpec.interfaceBuilder(interfaceName)
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder(UUID, References.Bluetooth.uuid)
                            .initializer("%M(%S)", References.Bluetooth.uuidFrom, characteristic.uuid)
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
                            ParameterSpec(CHARACTERISTIC, References.Bluetooth.remoteCharacteristic),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(FROM_SERVICE)
                            .addParameters(
                                listOfNotNull(
                                    ParameterSpec(SERVICE, References.Bluetooth.remoteService),
                                    ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                                ),
                            )
                            .returns(className)
                            .addStatement(
                                "$RETURN %T($SERVICE.characteristics.%M(%T.$UUID)${needsFormatter.functionArgument})",
                                className,
                                References.Bluetooth.get,
                                interfaceName,
                            )
                            .build(),
                    )
                    .build(),
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(CHARACTERISTIC, References.Bluetooth.remoteCharacteristic)
                        .initializer(CHARACTERISTIC).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT).build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.BLUETOOTH)
            .build()
    }

    override fun generateSimulated(nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.CLIENT_API)
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        val readProperty = properties.firstOrNull { it.isReadable }
        val writeProperty = properties.firstOrNull { it.isWritable }
        val notifiableProperty = properties.firstOrNull { it.isNotifiable }

        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            readProperty?.let { onRead ->
                                ParameterSpec(
                                    "${onRead.onReadMethodName}$ACTION",
                                    LambdaTypeName.get(
                                        returnType = BluetoothResultTypeBuilder(declaration, onRead, logger).responseClassName,
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
                            notifiableProperty?.let { notifiable ->
                                ParameterSpec(
                                    notifiable.simpleName.asString(),
                                    References.KotlinX.Coroutines.Flow.flow.parameterizedBy(notifiable.type.resolve().toTypeName()),
                                )
                            },
                        ) + properties.mapNotNull { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                ParameterSpec(
                                    propertyDeclaration.simpleName.asString(),
                                    NameHelper.nameFor(typeDeclaration, GenerationType.CLIENT_SIMULATOR),
                                )
                            } else {
                                null
                            }
                        },
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
                                returnType = BluetoothResultTypeBuilder(declaration, onRead, logger).responseClassName,
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
        var hasNotifiableProperty = false
        var hasReadMethod = false
        var hasWriteMethod = false
        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
            val typeDeclaration = propertyDeclaration.type.resolve().declaration
            if (propertyDeclaration.isReadable ||
                propertyDeclaration.isWritable ||
                propertyDeclaration.isNotifiable
            ) {
                if (propertyDeclaration.isReadable) {
                    if (!hasReadMethod) {
                        hasReadMethod = true
                        val responseType = propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }
                        val readMethod = "$READ$responseType"
                        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, logger)
                        addFunction(
                            generateReadMethod(readMethod, type, resultType, propertyDeclaration),
                        )
                    } else {
                        logger.error("Only one @${Readable::class.simpleName} property can be declared")
                    }
                }

                if (propertyDeclaration.isWritable) {
                    if (!hasWriteMethod) {
                        hasWriteMethod = true
                        val writeMethod = "$WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                        addFunction(
                            generateWriteMethod(writeMethod, propertyDeclaration, type),
                        )
                    } else {
                        logger.error(
                            "Only one @${Writable::class.simpleName} / @${WritableWithoutResponse::class.simpleName } / @${WritableSigned::class.simpleName} property can be declared",
                        )
                    }
                }

                if (propertyDeclaration.isNotifiable) {
                    if (!hasNotifiableProperty) {
                        hasNotifiableProperty = true
                        addProperty(
                            generateNotifiableProperty(propertyDeclaration, type),
                        )
                    } else {
                        logger.error("Only one @${Notifiable::class.simpleName} or @${Indicatable::class.simpleName} property can be declared")
                    }
                }
            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                addProperty(
                    generateDescriptorProperty(propertyDeclaration, typeDeclaration, type),
                )
            } else {
                logger.error(
                    "Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared",
                )
            }
        }
    }

    private fun generateReadMethod(readMethod: String, type: GenerationType.Type, resultType: BluetoothResultTypeBuilder, propertyDeclaration: KSPropertyDeclaration): FunSpec =
        FunSpec.builder(readMethod).addModifiers(KModifier.SUSPEND, *type.additionalModifiers.toTypedArray()).returns(
            resultType.responseClassName,
        ).apply {
            when (type) {
                GenerationType.Type.API -> {}

                GenerationType.Type.BLUETOOTH -> {
                    resultType.generateBluetoothResult(this, CHARACTERISTIC)
                }

                GenerationType.Type.SIMULATOR -> {
                    addStatement("$RETURN ${propertyDeclaration.onReadMethodName}$ACTION()")
                }
            }
        }.build()

    private fun generateWriteMethod(writeMethod: String, propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): FunSpec = FunSpec.builder(
        writeMethod,
    ).addParameter(
        propertyDeclaration.simpleName.asString(),
        propertyDeclaration.type.resolve().toClassName(),
    ).addModifiers(KModifier.SUSPEND, *type.additionalModifiers.toTypedArray()).returns(
        References.Bluetooth.writeResponse,
    ).apply {
        when (type) {
            GenerationType.Type.API -> {}

            GenerationType.Type.BLUETOOTH -> {
                if (propertyDeclaration.isByteArray) {
                    addStatement("$RETURN $CHARACTERISTIC.$WRITE(${propertyDeclaration.simpleName.asString()})")
                } else {
                    addStatement(
                        "$RETURN $CHARACTERISTIC.$WRITE($FORMAT.encodeToByteArray(%L, ${propertyDeclaration.simpleName.asString()}))",
                        propertyDeclaration.type.resolve().toTypeName().serializer(logger),
                    )
                }
            }

            GenerationType.Type.SIMULATOR -> {
                addStatement("$RETURN ${propertyDeclaration.onWriteMethodName}$ACTION(${propertyDeclaration.simpleName.asString()})")
            }
        }
    }.build()

    private fun generateNotifiableProperty(propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): PropertySpec = PropertySpec.builder(
        propertyDeclaration.simpleName.asString(),
        References.KotlinX.Coroutines.Flow.flow.parameterizedBy(propertyDeclaration.type.resolve().toTypeName()),
    ).addModifiers(
        *type.additionalModifiers.toTypedArray(),
    )
        .apply {
            when (type) {
                GenerationType.Type.API -> {}

                GenerationType.Type.BLUETOOTH -> {
                    getter(
                        FunSpec.getterBuilder()
                            .apply {
                                val member = References.Bluetooth.value
                                if (propertyDeclaration.isByteArray) {
                                    addStatement("$RETURN $CHARACTERISTIC.%M()", member)
                                } else {
                                    addStatement(
                                        "$RETURN $CHARACTERISTIC.%M(%L, $FORMAT)",
                                        member,
                                        propertyDeclaration.type.resolve().toTypeName().serializer(logger),
                                    )
                                }
                            }
                            .build(),
                    )
                }

                GenerationType.Type.SIMULATOR -> {
                    initializer(propertyDeclaration.simpleName.asString())
                }
            }
        }.build()

    private fun generateDescriptorProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec =
        PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            NameHelper.clientName(typeDeclaration, type),
        ).addModifiers(
            *type.additionalModifiers.toTypedArray(),
        )
            .apply {
                when (type) {
                    GenerationType.Type.API -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        initializer(
                            "%T.${BluetoothRemoteDescriptorBuilder.FROM_CHARACTERISTIC}($CHARACTERISTIC${NeedsFormatterHelper.needsBluetoothFormatter(
                                typeDeclaration,
                            ).functionArgument})",
                            NameHelper.clientName(typeDeclaration, type),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> {
                        initializer(propertyDeclaration.simpleName.asString())
                    }
                }
            }.build()
}
