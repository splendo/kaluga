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
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.ksp.helpers.AS
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTICS
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.COROUTINE_SCOPE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.GENERATE_REMOTE
import com.splendo.kaluga.bluetooth.ksp.helpers.GET_OR_PUT
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.INCLUDED_SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.INCLUDED_SERVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.IS_CLOSED
import com.splendo.kaluga.bluetooth.ksp.helpers.LAZY
import com.splendo.kaluga.bluetooth.ksp.helpers.MUTABLE_MAP_OF
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.REMOTES
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.delegateParameterName
import com.splendo.kaluga.bluetooth.ksp.helpers.isNotifiable
import com.splendo.kaluga.bluetooth.ksp.helpers.nullIfPropertyIsNull
import com.splendo.kaluga.bluetooth.ksp.helpers.optionalChainIfNullable
import com.splendo.kaluga.bluetooth.ksp.helpers.withLetIfNull
import com.squareup.kotlinpoet.ClassName
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

internal class BluetoothLocalServiceBuilder(declaration: KSClassDeclaration, private val service: BluetoothService, options: Options, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, options, logger) {
    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val interfaceName = nameFor(declaration, GenerationType.SERVER_API)
        return TypeSpec.interfaceBuilder(interfaceName)
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
                                PropertySpec.builder(
                                    "${propertyDeclaration.simpleName.asString()}$DELEGATE",
                                    nameFor(typeDeclaration, GenerationType.SERVER_API).nestedClass(DELEGATE).nullIfPropertyIsNull(propertyDeclaration),
                                ).build()
                            } else {
                                invalidProperty(propertyDeclaration, BluetoothService::class, BluetoothCharacteristic::class)
                                null
                            }
                        }.toList(),
                    )
                    .build(),
            )
            .generateBody(declarations, GenerationType.Type.API)
            .build()
    }

    override fun generateBluetooth(nested: List<TypeSpec>): TypeSpec {
        val className = nameFor(declaration, GenerationType.SERVER_BLUETOOTH)
        val interfaceName = nameFor(declaration, GenerationType.SERVER_API)
        val delegateName = interfaceName.nestedClass(DELEGATE)
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER)
        return TypeSpec.classBuilder(className).addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(SERVICE, References.Bluetooth.Server.localService),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addType(
                generateBluetoothCompanionObject(delegateName, interfaceName),
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(SERVICE, References.Bluetooth.Server.localService)
                        .initializer(SERVICE).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT)
                        .build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.BLUETOOTH)
            .build()
    }

    private fun generateBluetoothCompanionObject(delegateName: ClassName, interfaceName: ClassName): TypeSpec = TypeSpec.companionObjectBuilder()
        // Every service can be configured as a top-level service of the server, or as a service included by another
        // service (which itself may be primary or secondary) — included services nest to any depth.
        .addFunction(generateConfigureFromBluetoothServerDSL(delegateName, interfaceName))
        .addFunction(generateConfigureFromServiceDSL(delegateName, interfaceName))
        .build()

    private fun generateConfigureFromBluetoothServerDSL(delegateName: ClassName, interfaceName: ClassName): FunSpec =
        generateConfigureFunction(delegateName, interfaceName, References.Bluetooth.Server.bluetoothServerDSL, SERVICE)

    private fun generateConfigureFromServiceDSL(delegateName: ClassName, interfaceName: ClassName): FunSpec =
        generateConfigureFunction(delegateName, interfaceName, References.Bluetooth.Server.localServiceDSL, INCLUDED_SERVICE)

    // Builds a `configure` overload that sets up this service on [builderType] via [openFunction] (either the server's
    // `service` builder, or another service's `includedService` builder). The body configures each child the same way,
    // so an included service can in turn include further services and characteristics, to any depth.
    private fun generateConfigureFunction(delegateName: ClassName, interfaceName: ClassName, builderType: ClassName, openFunction: String): FunSpec = FunSpec.builder(CONFIGURE)
        .apply {
            val delegateParameterName = declaration.delegateParameterName
            addParameter(BUILDER, builderType)
            addParameter(delegateParameterName, delegateName)
            val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER_DSL)
            if (needsFormatter.needsFormatter) {
                addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
            }
            addCode(
                CodeBlock.builder()
                    .beginControlFlow("$RETURN $BUILDER.$openFunction(%T.$UUID) {", interfaceName)
                    .apply { addChildConfigurations(delegateParameterName) }
                    .endControlFlow()
                    .build(),
            )
        }
        .build()

    // Emits a `Child.configure(this, delegate)` statement for every included-service and characteristic property,
    // recursing through the generated child `configure` overloads.
    private fun CodeBlock.Builder.addChildConfigurations(delegateParameterName: String) {
        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
            val typeDeclaration = propertyDeclaration.type.resolve().declaration
            when {
                typeDeclaration is KSClassDeclaration &&
                    (
                        typeDeclaration.isAnnotationPresent(BluetoothService::class) ||
                            typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)
                        ) -> {
                    val delegateNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, NeedsFormatterHelper.Target.SERVER_DSL)
                    withLetIfNull(
                        "$delegateParameterName.${propertyDeclaration.delegateParameterName}",
                        property = propertyDeclaration,
                    ) { property ->
                        addStatement(
                            "%T.$CONFIGURE($THIS, $property${delegateNeedsFormatter.functionArgument})",
                            nameFor(typeDeclaration, GenerationType.SERVER_BLUETOOTH),
                        )
                    }
                }

                else -> {
                    invalidProperty(propertyDeclaration, BluetoothService::class, BluetoothCharacteristic::class)
                }
            }
        }
    }

    override fun generateSimulated(nested: List<TypeSpec>): TypeSpec {
        val className = nameFor(declaration, GenerationType.SERVER_SIMULATOR)
        val interfaceName = nameFor(declaration, GenerationType.SERVER_API)
        val delegate = interfaceName.nestedClass(DELEGATE)
        val remote = nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(declaration.delegateParameterName, delegate),
                            ParameterSpec(COROUTINE_SCOPE, References.KotlinX.Coroutines.coroutineScope),
                            ParameterSpec(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT)),
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addProperty(
                PropertySpec.builder(declaration.delegateParameterName, delegate)
                    .initializer(declaration.delegateParameterName)
                    .build(),
            )
            .apply {
                if (options.generateClient) {
                    addProperty(
                        PropertySpec.builder(REMOTES, MUTABLE_MAP.parameterizedBy(References.Bluetooth.Device.identifier, remote))
                            .addModifiers(KModifier.PRIVATE)
                            .initializer("$MUTABLE_MAP_OF()")
                            .build(),
                    )
                }
            }
            .addProperty(
                PropertySpec.builder(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT))
                    .addModifiers(KModifier.PRIVATE)
                    .initializer(IS_CLOSED)
                    .build(),
            )
            .apply {
                if (options.generateClient) {
                    addFunction(
                        generateSimulatorGenerateRemoteMethod(remote, properties),
                    )
                }
            }
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.SIMULATOR)
            .build()
    }

    override fun generateMock(nested: List<TypeSpec>): TypeSpec = buildMock(GenerationType.Side.SERVER, nested)

    private fun generateSimulatorGenerateRemoteMethod(remote: ClassName, properties: Sequence<KSPropertyDeclaration>): FunSpec = FunSpec.builder(GENERATE_REMOTE)
        .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
        .returns(remote)
        .addCode(
            CodeBlock.builder()
                .add("$RETURN $REMOTES.$GET_OR_PUT($IDENTIFIER) {\n")
                .indent()
                .beginControlFlow("$WITH (${declaration.delegateParameterName})")
                .apply {
                    addStatement("%T(", remote)
                    indent()
                    val includedServicesCode = properties.mapNotNull { propertyDeclaration ->
                        val typeDeclaration = propertyDeclaration.type.resolve().declaration
                        if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                            CodeBlock.of(
                                "${propertyDeclaration.simpleName.asString()} = ${propertyDeclaration.simpleName.asString()}${propertyDeclaration.optionalChainIfNullable}.$GENERATE_REMOTE($IDENTIFIER)",
                            )
                        } else {
                            null
                        }
                    }.toList()
                    val characteristicsCode = properties.mapNotNull { propertyDeclaration ->
                        val typeDeclaration = propertyDeclaration.type.resolve().declaration
                        if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)) {
                            CodeBlock.of(
                                "${propertyDeclaration.simpleName.asString()} = ${propertyDeclaration.simpleName.asString()}${propertyDeclaration.optionalChainIfNullable}.$GENERATE_REMOTE($IDENTIFIER)",
                            )
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
        .build()

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, type: GenerationType.Type): TypeSpec.Builder = apply {
        addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                when (val typeDeclaration = propertyDeclaration.type.resolve().declaration) {
                    is KSClassDeclaration if typeDeclaration.isAnnotationPresent(BluetoothService::class) -> {
                        generateIncludedServiceProperty(propertyDeclaration, typeDeclaration, type)
                    }

                    is KSClassDeclaration if typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class) -> {
                        generateCharacteristicProperty(propertyDeclaration, typeDeclaration, type)
                    }

                    else -> {
                        invalidProperty(propertyDeclaration, BluetoothService::class, BluetoothCharacteristic::class)
                        null
                    }
                }
            }.toList(),
        )
    }

    private fun generateIncludedServiceProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec =
        PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            serverName(typeDeclaration, type).nullIfPropertyIsNull(propertyDeclaration),
        ).addModifiers(*type.additionalModifiers.toTypedArray())
            .apply {
                val serviceNeedsFormat = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, NeedsFormatterHelper.Target.SERVER)
                when (type) {
                    GenerationType.Type.API, GenerationType.Type.MOCK -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        delegate(
                            "$LAZY { %L }",
                            CodeBlock.of(
                                "%T($SERVICE.$INCLUDED_SERVICES.%M(%T.$UUID)${serviceNeedsFormat.functionArgument})",
                                serverName(typeDeclaration, type),
                                References.Bluetooth.get,
                                serverName(typeDeclaration, GenerationType.Type.API),
                            ),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> {
                        initializer(
                            CodeBlock.builder()
                                .withLetIfNull("${declaration.delegateParameterName}.${propertyDeclaration.delegateParameterName}", propertyDeclaration) { property ->
                                    addStatement(
                                        "%T($property, $COROUTINE_SCOPE, $IS_CLOSED)",
                                        serverName(typeDeclaration, type),
                                    )
                                }
                                .build(),
                        )
                    }
                }
            }
            .build()

    private fun generateCharacteristicProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec =
        PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            serverName(typeDeclaration, type).nullIfPropertyIsNull(propertyDeclaration),
        ).addModifiers(*type.additionalModifiers.toTypedArray())
            .apply {
                val characteristicNeedsFormat = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, NeedsFormatterHelper.Target.SERVER)
                when (type) {
                    GenerationType.Type.API, GenerationType.Type.MOCK -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        val isNotifiable = typeDeclaration.declarations.filterIsInstance<KSPropertyDeclaration>().any { it.isNotifiable }
                        val cast = if (isNotifiable) CodeBlock.of(" $AS %T", References.Bluetooth.Server.localCharacteristicNotifiable) else CodeBlock.of("")
                        delegate(
                            "$LAZY { %L }",
                            CodeBlock.of(
                                "%T($SERVICE.$CHARACTERISTICS.%M(%T.$UUID)%L${characteristicNeedsFormat.functionArgument})",
                                serverName(typeDeclaration, type),
                                References.Bluetooth.get,
                                serverName(typeDeclaration, GenerationType.Type.API),
                                cast,
                            ),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> {
                        initializer(
                            CodeBlock.builder()
                                .withLetIfNull("${declaration.delegateParameterName}.${propertyDeclaration.delegateParameterName}", propertyDeclaration) { property ->
                                    addStatement(
                                        "%T($property, $COROUTINE_SCOPE, $IS_CLOSED)",
                                        serverName(typeDeclaration, type),
                                    )
                                }
                                .build(),
                        )
                    }
                }
            }
            .build()
}
