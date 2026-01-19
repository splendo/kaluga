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

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.COROUTINE_SCOPE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.GENERATE_REMOTE
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.IS_CLOSED
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.REMOTES
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.delegateName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MUTABLE_MAP
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.joinToCode

internal class BluetoothLocalServiceBuilder(declaration: KSClassDeclaration, private val service: BluetoothService, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, logger) {
    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec =
        TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder(UUID, References.Bluetooth.uuid)
                            .initializer("%M(%S)", References.Bluetooth.uuidFrom, service.uuid)
                            .build(),
                    )
                    .build(),
            )
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder(DELEGATE)
                    .addProperties(
                        declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (
                                typeDeclaration is KSClassDeclaration &&
                                (
                                    typeDeclaration.isAnnotationPresent(BluetoothService::class) ||
                                        typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)
                                    )
                            ) {
                                if (typeDeclaration.isAnnotationPresent(BluetoothService::class) && typeDeclaration.declarations.filterIsInstance<KSPropertyDeclaration>()
                                        .any { serviceProperties ->
                                            serviceProperties.isAnnotationPresent(BluetoothService::class)
                                        }
                                ) {
                                    logger.error("An included @${BluetoothService::class} can not include its own services")
                                }

                                PropertySpec.builder(
                                    "${propertyDeclaration.simpleName.asString()}$DELEGATE",
                                    NameHelper.nameFor(typeDeclaration, generationType).nestedClass(DELEGATE),
                                ).build()
                            } else {
                                logger.error(
                                    "A BluetoothService should only have @${BluetoothService::class.simpleName} and @${BluetoothCharacteristic::class.simpleName} properties $typeDeclaration ${typeDeclaration.annotations}",
                                )
                                null
                            }
                        }.toList(),
                    )
                    .build(),
            )
            .generateBody(declarations, generationType)
            .build()

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(this, generationType)
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER)
        return TypeSpec.classBuilder(className).addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(SERVICE, References.Bluetooth.Server.localService),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(CONFIGURE)
                            .apply {
                                val delegateName = "${declaration.simpleName.asString().replaceFirstChar { it.lowercase() }}$DELEGATE"
                                addParameter(BUILDER, References.Bluetooth.Server.bluetoothServerDSL)
                                addParameter(
                                    delegateName,
                                    NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE),
                                )
                                val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER_DSL)
                                if (needsFormatter) {
                                    addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                                }
                                addCode(
                                    CodeBlock.builder()
                                        .beginControlFlow(
                                            "$RETURN $BUILDER.service(%T.$UUID) {",
                                            NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)),
                                        )
                                        .apply {
                                            declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                                                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                                when {
                                                    typeDeclaration is KSClassDeclaration &&
                                                        (
                                                            typeDeclaration.isAnnotationPresent(BluetoothService::class) ||
                                                                typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)
                                                            ) -> {
                                                        val delegateNeedsFormatter =
                                                            NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, NeedsFormatterHelper.Target.SERVER_DSL)
                                                        addStatement(
                                                            "%T.$CONFIGURE($THIS, $delegateName.${propertyDeclaration.delegateName}${if (delegateNeedsFormatter) ", $FORMAT" else ""})",
                                                            NameHelper.nameFor(typeDeclaration, generationType),
                                                        )
                                                    }

                                                    else -> {
                                                        logger.error(
                                                            "Only @${BluetoothService::class.simpleName} and @${BluetoothCharacteristic::class.simpleName} properties can be declared",
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        .endControlFlow()
                                        .build(),
                                )
                            }
                            .build(),
                    )
                    .addFunction(
                        FunSpec.builder(CONFIGURE)
                            .apply {
                                val delegateName = "${declaration.simpleName.asString().replaceFirstChar { it.lowercase() }}$DELEGATE"
                                addParameter(BUILDER, References.Bluetooth.Server.localServiceDSLPrimary)
                                addParameter(
                                    delegateName,
                                    NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE),
                                )
                                val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER_DSL)
                                if (needsFormatter) {
                                    addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                                }
                                addCode(
                                    CodeBlock.builder()
                                        .beginControlFlow(
                                            "$RETURN $BUILDER.includedService(%T.$UUID) {",
                                            NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)),
                                        )
                                        .apply {
                                            declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                                                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                                when {
                                                    typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class) -> {
                                                        val characteristicNeedsFormatter =
                                                            NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, NeedsFormatterHelper.Target.SERVER_DSL)
                                                        addStatement(
                                                            "%T.$CONFIGURE($THIS, $delegateName.${propertyDeclaration.delegateName}${if (characteristicNeedsFormatter) ", $FORMAT" else ""})",
                                                            NameHelper.nameFor(typeDeclaration, generationType),
                                                        )
                                                    }

                                                    else -> {
                                                        logger.error("Only @${BluetoothCharacteristic::class.simpleName} properties can be declared in a nested BluetoothService")
                                                    }
                                                }
                                            }
                                        }
                                        .endControlFlow()
                                        .build(),
                                )
                            }
                            .build(),
                    )
                    .build(),
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(SERVICE, References.Bluetooth.Server.localService)
                        .initializer(SERVICE).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT)
                        .build().takeIf { needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, generationType)
            .build()
    }

    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(this, generationType)
        val delegate = NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE)
        val remote = NameHelper.nameFor(this, generationType.copy(side = GenerationType.Side.CLIENT))
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(delegateName, delegate),
                            ParameterSpec(COROUTINE_SCOPE, References.KotlinX.Coroutines.coroutineScope),
                            ParameterSpec(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT)),
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addProperty(
                PropertySpec.builder(delegateName, delegate)
                    .initializer(delegateName)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(REMOTES, MUTABLE_MAP.parameterizedBy(References.Bluetooth.Device.identifier, remote))
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("mutableMapOf()")
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT))
                    .addModifiers(KModifier.PRIVATE)
                    .initializer(IS_CLOSED)
                    .build(),
            )
            .addFunction(
                FunSpec.builder(GENERATE_REMOTE)
                    .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                    .returns(remote)
                    .addCode(
                        CodeBlock.builder()
                            .add("$RETURN $REMOTES.getOrPut($IDENTIFIER) {\n")
                            .indent()
                            .beginControlFlow("$WITH ($delegateName)")
                            .apply {
                                addStatement("%T(", remote)
                                indent()
                                val includedServicesCode = properties.mapNotNull { propertyDeclaration ->
                                    val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                    if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                                        CodeBlock.of("${propertyDeclaration.simpleName.asString()} = ${propertyDeclaration.simpleName.asString()}.$GENERATE_REMOTE($IDENTIFIER)")
                                    } else {
                                        null
                                    }
                                }.toList()
                                val characteristicsCode = properties.mapNotNull { propertyDeclaration ->
                                    val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                    if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)) {
                                        CodeBlock.of("${propertyDeclaration.simpleName.asString()} = ${propertyDeclaration.simpleName.asString()}.$GENERATE_REMOTE($IDENTIFIER)")
                                    } else {
                                        null
                                    }
                                }.toList()
                                val properties = includedServicesCode + characteristicsCode
                                if (properties.isNotEmpty()) {
                                    add(
                                        properties.joinToCode(separator = ",\n", suffix = ",\n"),
                                    )
                                }
                                unindent()
                                addStatement(")")
                            }
                            .endControlFlow()
                            .unindent()
                            .add("}\n")
                            .build(),
                    )
                    .build(),
            )
            .addTypes(nested)
            .generateBody(declarations, generationType)
            .build()
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType): TypeSpec.Builder = apply {
        addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                when {
                    typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class) -> {
                        if (typeDeclaration.isAnnotationPresent(BluetoothService::class) && typeDeclaration.declarations.filterIsInstance<KSPropertyDeclaration>()
                                .any { serviceProperties ->
                                    serviceProperties.isAnnotationPresent(BluetoothService::class)
                                }
                        ) {
                            logger.error("An included @${BluetoothService::class} can not include its own services")
                        }
                        PropertySpec.builder(
                            propertyDeclaration.simpleName.asString(),
                            NameHelper.nameFor(typeDeclaration, generationType),
                        ).addModifiers(*generationType.additionalModifiers.toTypedArray())
                            .apply {
                                val service = typeDeclaration.getAnnotationsByType(BluetoothService::class).first()
                                val serviceNeedsFormat = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration)
                                when (generationType.type) {
                                    GenerationType.Type.API -> {}

                                    GenerationType.Type.BLUETOOTH -> {
                                        delegate(
                                            "lazy { %L }",
                                            CodeBlock.of(
                                                "%T($SERVICE.includedServices.%M(%T.$UUID)${if (serviceNeedsFormat) ", $FORMAT" else ""})",
                                                NameHelper.nameFor(typeDeclaration, generationType),
                                                References.Bluetooth.get,
                                                NameHelper.nameFor(typeDeclaration, generationType.copy(type = GenerationType.Type.API)),
                                            ),
                                        )
                                    }

                                    GenerationType.Type.SIMULATOR -> {
                                        initializer(
                                            CodeBlock.of(
                                                "%T(${declaration.delegateName}.${propertyDeclaration.delegateName}, $COROUTINE_SCOPE, $IS_CLOSED)",
                                                NameHelper.nameFor(typeDeclaration, generationType),
                                            ),
                                        )
                                    }
                                }
                            }
                            .build()
                    }

                    typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class) -> {
                        PropertySpec.builder(
                            propertyDeclaration.simpleName.asString(),
                            NameHelper.nameFor(typeDeclaration, generationType),
                        ).addModifiers(*generationType.additionalModifiers.toTypedArray())
                            .apply {
                                val characteristic = typeDeclaration.getAnnotationsByType(BluetoothCharacteristic::class).first()
                                val characteristicNeedsFormat = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration)
                                when (generationType.type) {
                                    GenerationType.Type.API -> {}

                                    GenerationType.Type.BLUETOOTH -> {
                                        val isNotifiable = typeDeclaration.declarations.filterIsInstance<KSPropertyDeclaration>().any { it.isAnnotationPresent(Notifiable::class) }
                                        val cast = if (isNotifiable) CodeBlock.of(" as %T", References.Bluetooth.Server.localCharacteristicNotifiable) else CodeBlock.of("")
                                        delegate(
                                            "lazy { %L }",
                                            CodeBlock.of(
                                                "%T($SERVICE.characteristics.%M(%T.$UUID)%L${if (characteristicNeedsFormat) ", $FORMAT" else ""})",
                                                NameHelper.nameFor(typeDeclaration, generationType),
                                                References.Bluetooth.get,
                                                NameHelper.nameFor(typeDeclaration, generationType.copy(type = GenerationType.Type.API)),
                                                cast,
                                            ),
                                        )
                                    }

                                    GenerationType.Type.SIMULATOR -> {
                                        initializer(
                                            CodeBlock.of(
                                                "%T(${declaration.delegateName}.${propertyDeclaration.delegateName}, $COROUTINE_SCOPE, $IS_CLOSED)",
                                                NameHelper.nameFor(typeDeclaration, generationType),
                                            ),
                                        )
                                    }
                                }
                            }
                            .build()
                    }

                    else -> {
                        logger.error(
                            "A BluetoothService should only have @${BluetoothService::class.simpleName} and @${BluetoothCharacteristic::class.simpleName} properties $typeDeclaration ${typeDeclaration.annotations}",
                        )
                        null
                    }
                }
            }.toList(),
        )
    }
}
