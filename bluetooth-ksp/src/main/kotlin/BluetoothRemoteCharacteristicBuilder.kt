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

    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType))
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this)
        val className = NameHelper.nameFor(this, generationType)
        val typeSpec = TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(CHARACTERISTIC, References.Bluetooth.remoteCharacteristic),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(FROM_SERVICE)
                            .addParameters(
                                listOfNotNull(
                                    ParameterSpec(SERVICE, References.Bluetooth.remoteService),
                                    ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                                ),
                            )
                            .returns(className)
                            .addStatement(
                                "$RETURN %T($SERVICE.characteristics.%M(%M(%S))${if (needsFormatter) ", $FORMAT" else ""})",
                                className,
                                References.Bluetooth.get,
                                References.Bluetooth.uuidFrom,
                                characteristic.uuid,
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
                        .initializer(FORMAT).build().takeIf { needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }

    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        val readProperty = properties.firstOrNull { it.isReadable }
        val writeProperty = properties.firstOrNull { it.isWritable }
        val notifiableProperty = properties.firstOrNull { it.isNotifiable }

        val typeSpec = TypeSpec.classBuilder(NameHelper.nameFor(this, generationType))
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
                                    NameHelper.nameFor(typeDeclaration, generationType),
                                )
                            } else {
                                null
                            }
                        },
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
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType, imports: Generated.Imports): TypeSpec.Builder = apply {
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
                            FunSpec.builder(readMethod).addModifiers(KModifier.SUSPEND, *generationType.additionalModifiers.toTypedArray()).returns(
                                resultType.responseClassName,
                            ).apply {
                                when (generationType.type) {
                                    GenerationType.Type.API -> {}

                                    GenerationType.Type.BLUETOOTH -> {
                                        resultType.generateBluetoothResult(this, CHARACTERISTIC)
                                    }

                                    GenerationType.Type.SIMULATOR -> {
                                        addStatement("$RETURN ${propertyDeclaration.onReadMethodName}$ACTION()")
                                    }
                                }
                            }.build(),
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
                            FunSpec.builder(
                                writeMethod,
                            ).addParameter(
                                propertyDeclaration.simpleName.asString(),
                                propertyDeclaration.type.resolve().toClassName(),
                            ).addModifiers(KModifier.SUSPEND, *generationType.additionalModifiers.toTypedArray()).returns(
                                References.Bluetooth.writeResponse,
                            ).apply {
                                when (generationType.type) {
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
                            }.build(),
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
                            PropertySpec.builder(
                                propertyDeclaration.simpleName.asString(),
                                References.KotlinX.Coroutines.Flow.flow.parameterizedBy(propertyDeclaration.type.resolve().toTypeName()),
                            ).addModifiers(
                                *generationType.additionalModifiers.toTypedArray(),
                            )
                                .apply {
                                    when (generationType.type) {
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
                                }.build(),
                        )
                    } else {
                        logger.error("Only one @${Notifiable::class.simpleName} or @${Indicatable::class.simpleName} property can be declared")
                    }
                }
            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                addProperty(
                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        NameHelper.nameFor(typeDeclaration, generationType),
                    ).addModifiers(
                        *generationType.additionalModifiers.toTypedArray(),
                    )
                        .apply {
                            when (generationType.type) {
                                GenerationType.Type.API -> {}

                                GenerationType.Type.BLUETOOTH -> {
                                    initializer(
                                        "%T.${BluetoothRemoteDescriptorBuilder.FROM_CHARACTERISTIC}($CHARACTERISTIC${if (NeedsFormatterHelper.needsBluetoothFormatter(
                                                typeDeclaration,
                                            )
                                        ) {
                                            ", $FORMAT"
                                        } else {
                                            ""
                                        } })",
                                        NameHelper.nameFor(typeDeclaration, generationType),
                                    )
                                }

                                GenerationType.Type.SIMULATOR -> {
                                    initializer(propertyDeclaration.simpleName.asString())
                                }
                            }
                        }.build(),
                )
            } else {
                logger.error(
                    "Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared",
                )
            }
        }
    }
}
