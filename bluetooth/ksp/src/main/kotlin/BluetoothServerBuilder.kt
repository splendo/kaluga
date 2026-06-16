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
import com.splendo.kaluga.bluetooth.annotations.Advertising
import com.splendo.kaluga.bluetooth.annotations.AdvertisingName
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.ksp.helpers.BLUETOOTH
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CLOSE
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.COROUTINE_CONTEXT
import com.splendo.kaluga.bluetooth.ksp.helpers.COROUTINE_SCOPE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.GENERATE_CLIENT
import com.splendo.kaluga.bluetooth.ksp.helpers.GENERATE_REMOTE
import com.splendo.kaluga.bluetooth.ksp.helpers.GET_OR_PUT
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.IS_CLOSED
import com.splendo.kaluga.bluetooth.ksp.helpers.IT
import com.splendo.kaluga.bluetooth.ksp.helpers.LAZY
import com.splendo.kaluga.bluetooth.ksp.helpers.MUTABLE_MAP_OF
import com.splendo.kaluga.bluetooth.ksp.helpers.NULL
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.REMOTES
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVER
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.SIMULATED
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.VALUE
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.delegateParameterName
import com.splendo.kaluga.bluetooth.ksp.helpers.nullIfPropertyIsNull
import com.splendo.kaluga.bluetooth.ksp.helpers.optionalChainIfNullable
import com.splendo.kaluga.bluetooth.ksp.helpers.withLetIfNull
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MUTABLE_MAP
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.joinToCode

internal class BluetoothServerBuilder(declaration: KSClassDeclaration, options: Options, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, options, logger) {

    private companion object {
        const val CREATE_SERVER = "createServer"
        const val SERVER_NAME = "serverName"
        const val LOCAL_NAME = "localName"
        const val ADVERTISE = "advertise"
        const val SETTINGS_BUILDER = "settingsBuilder"
        const val PERMISSIONS = "permissions"
        const val SERVICE_UUIDS = "serviceUUIDs"
    }

    override fun generateAPI(nested: List<TypeSpec>): TypeSpec = TypeSpec.interfaceBuilder(nameFor(declaration, GenerationType.SERVER_API))
        .addSuperinterface(References.Kotlin.autoCloseable)
        .addType(companionObject())
        .addTypes(nested)
        .addType(
            generateDelegate(),
        )
        .generateBody(declarations, GenerationType.Type.API)
        .build()

    override val needsNamedCompanion: Boolean get() = true

    override fun factoryFor(generationType: GenerationType): FunSpec? = when (generationType) {
        GenerationType.SERVER_BLUETOOTH -> generateBluetoothFactory()
        GenerationType.SERVER_SIMULATOR -> generateSimulatorFactory()
        else -> null
    }

    private fun delegateParameter(interfaceName: ClassName): ParameterSpec = ParameterSpec(
        "$SERVER$DELEGATE".replaceFirstChar { it.lowercase() },
        interfaceName.nestedClass(DELEGATE),
    )

    private fun generateBluetoothFactory(): FunSpec = FunSpec.builder(
        BLUETOOTH,
    ).apply {
        val interfaceName = nameFor(declaration, GenerationType.SERVER_API)
        val delegateParameter = delegateParameter(interfaceName)
        val serverNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER)
        val delegateNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER_DSL)
        val returnType = nameFor(declaration, GenerationType.SERVER_BLUETOOTH)
        receiver(companionReceiver(GenerationType.SERVER_API))
        returns(returnType)
            .addModifiers(KModifier.SUSPEND)
            .addParameters(
                listOfNotNull(
                    ParameterSpec(BUILDER, References.Bluetooth.Server.baseBluetoothServerBuilder),
                    delegateParameter,
                    ParameterSpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .defaultValue("%T", References.Bluetooth.Serialization.bluetoothFormat)
                        .build().takeIf { serverNeedsFormatter.needsFormatter || delegateNeedsFormatter.needsFormatter },
                    ParameterSpec.builder(
                        SETTINGS_BUILDER,
                        LambdaTypeName.get(
                            parameters = listOf(ParameterSpec(PERMISSIONS, References.Permissions.permissions)),
                            returnType = References.Bluetooth.Server.serverSettings,
                        ),
                    ).defaultValue("{ %T($PERMISSIONS = $IT) }", References.Bluetooth.Server.serverSettings)
                        .build(),
                    ParameterSpec.builder(
                        SERVER_NAME,
                        STRING.copy(nullable = true),
                    ).defaultValue(
                        declaration.getAnnotationsByType(AdvertisingName::class).firstOrNull()?.let {
                            CodeBlock.of("%S", it.name)
                        } ?: CodeBlock.of(NULL),
                    )
                        .build(),
                    ParameterSpec.builder(COROUTINE_CONTEXT, References.Kotlin.Coroutines.coroutineContext)
                        .defaultValue("%M(%S)", References.Base.singleThreadDispatcher, interfaceName.simpleName)
                        .build(),
                ),
            )
            .addCode(
                CodeBlock.builder()
                    .add("$RETURN %T(\n", returnType)
                    .indent()
                    .add("$BUILDER.$CREATE_SERVER(")
                    .indent()
                    .addStatement("$SETTINGS_BUILDER = $SETTINGS_BUILDER,")
                    .addStatement("$COROUTINE_CONTEXT = $COROUTINE_CONTEXT,")
                    .unindent()
                    .addStatement(") {")
                    .indent()
                    .beginControlFlow(ADVERTISE)
                    .apply {
                        addStatement("$LOCAL_NAME = $SERVER_NAME")
                        val advertisingUUIDs = declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull {
                            val resolvedDeclaration = it.type.resolve().declaration
                            if (resolvedDeclaration is KSClassDeclaration &&
                                resolvedDeclaration.isAnnotationPresent(BluetoothService::class) &&
                                it.isAnnotationPresent(Advertising::class)
                            ) {
                                CodeBlock.of("%T.$UUID", nameFor(resolvedDeclaration, GenerationType.SERVER_API))
                            } else {
                                null
                            }
                        }.toList()
                        if (advertisingUUIDs.isNotEmpty()) {
                            addStatement("$SERVICE_UUIDS(%L)", advertisingUUIDs.joinToCode(separator = ", "))
                        }
                    }
                    .endControlFlow()
                    .addStatement("%T.$CONFIGURE($THIS,$SERVER$DELEGATE${delegateNeedsFormatter.functionArgument})", returnType)
                    .unindent()
                    .addStatement("}${delegateNeedsFormatter.functionArgument}")
                    .unindent()
                    .add(")\n")
                    .build(),
            )
    }
        .build()

    private fun generateSimulatorFactory(): FunSpec = FunSpec.builder(SIMULATED).apply {
        val interfaceName = nameFor(declaration, GenerationType.SERVER_API)
        val delegateParameter = delegateParameter(interfaceName)
        val returnType = nameFor(declaration, GenerationType.SERVER_SIMULATOR)
        receiver(companionReceiver(GenerationType.SERVER_API))
        addParameter(delegateParameter)
            .addParameter(
                ParameterSpec.builder(COROUTINE_CONTEXT, References.Kotlin.Coroutines.coroutineContext)
                    .defaultValue("%M(%S)", References.Base.singleThreadDispatcher, interfaceName.simpleName)
                    .build(),
            )
            .returns(returnType)
            .addCode(
                "$RETURN %T(${delegateParameter.name}, %T($COROUTINE_CONTEXT))",
                returnType,
                References.KotlinX.Coroutines.coroutineScope,
            )
    }
        .build()

    private fun generateDelegate(): TypeSpec = TypeSpec.interfaceBuilder(DELEGATE)
        .addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (
                    typeDeclaration is KSClassDeclaration &&
                    (
                        typeDeclaration.isAnnotationPresent(BluetoothService::class)
                        )
                ) {
                    PropertySpec.builder(
                        "${propertyDeclaration.simpleName.asString()}$DELEGATE",
                        nameFor(typeDeclaration, GenerationType.SERVER_API).nestedClass(DELEGATE).nullIfPropertyIsNull(propertyDeclaration),
                    ).build()
                } else {
                    invalidProperty(propertyDeclaration, BluetoothService::class)
                    null
                }
            }.toList(),
        )
        .build()

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
                            ParameterSpec(SERVER, References.Bluetooth.Server.bluetoothServer),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addType(
                generateBluetoothCompanionObject(delegateName),
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(SERVER, References.Bluetooth.Server.bluetoothServer)
                        .initializer(SERVER).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT)
                        .build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.BLUETOOTH)
            .addFunction(
                FunSpec.builder(CLOSE)
                    .addModifiers(KModifier.OVERRIDE)
                    .addCode(CodeBlock.of("$SERVER.$CLOSE()"))
                    .build(),
            )
            .build()
    }

    private fun generateBluetoothCompanionObject(delegateName: ClassName): TypeSpec = TypeSpec.companionObjectBuilder()
        .addFunction(
            FunSpec.builder(CONFIGURE)
                .apply {
                    val delegateParameterName = declaration.delegateParameterName
                    addParameter(BUILDER, References.Bluetooth.Server.bluetoothServerDSL)
                    addParameter(
                        delegateParameterName,
                        delegateName,
                    )
                    val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER_DSL)
                    if (needsFormatter.needsFormatter) {
                        addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                    }
                    addCode(
                        CodeBlock.builder()
                            .apply {
                                declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                                    val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                    when {
                                        typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class) -> {
                                            val delegateNeedsFormatter =
                                                NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, NeedsFormatterHelper.Target.SERVER_DSL)
                                            withLetIfNull(
                                                "$delegateParameterName.${propertyDeclaration.delegateParameterName}",
                                                property = propertyDeclaration,
                                            ) { property ->
                                                addStatement(
                                                    "%T.$CONFIGURE($BUILDER, $property${delegateNeedsFormatter.functionArgument})",
                                                    nameFor(typeDeclaration, GenerationType.SERVER_BLUETOOTH),
                                                )
                                            }
                                        }

                                        else -> {
                                            invalidProperty(propertyDeclaration, BluetoothService::class)
                                        }
                                    }
                                }
                            }
                            .build(),
                    )
                }
                .build(),
        )
        .build()

    override fun generateSimulated(nested: List<TypeSpec>): TypeSpec {
        val className = nameFor(declaration, GenerationType.SERVER_SIMULATOR)
        val interfaceName = nameFor(declaration, GenerationType.SERVER_API)
        val delegateName = interfaceName.nestedClass(DELEGATE)
        val remote = nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(declaration.delegateParameterName, delegateName),
                            ParameterSpec(COROUTINE_SCOPE, References.KotlinX.Coroutines.coroutineScope),
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addProperty(
                PropertySpec.builder(declaration.delegateParameterName, delegateName)
                    .initializer(declaration.delegateParameterName)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(COROUTINE_SCOPE, References.KotlinX.Coroutines.coroutineScope)
                    .addModifiers(KModifier.PRIVATE)
                    .initializer(COROUTINE_SCOPE)
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
                PropertySpec.builder(IS_CLOSED, References.KotlinX.Coroutines.completableDeferred.parameterizedBy(UNIT))
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("%T()", References.KotlinX.Coroutines.completableDeferred)
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
            .addFunction(
                FunSpec.builder(CLOSE)
                    .addModifiers(KModifier.OVERRIDE)
                    .addCode(
                        CodeBlock.builder()
                            .addStatement("$IS_CLOSED.%M()", References.Base.Utils.complete)
                            .addStatement("$COROUTINE_SCOPE.%M()", References.KotlinX.Coroutines.cancel)
                            .build(),
                    )
                    .build(),
            )
            .generateBody(declarations, GenerationType.Type.SIMULATOR)
            .build()
    }

    private fun generateSimulatorGenerateRemoteMethod(remote: ClassName, properties: Sequence<KSPropertyDeclaration>): FunSpec = FunSpec.builder(GENERATE_CLIENT)
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
                    val services = properties.mapNotNull { propertyDeclaration ->
                        val typeDeclaration = propertyDeclaration.type.resolve().declaration
                        if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                            CodeBlock.of(
                                "${propertyDeclaration.simpleName.asString()} = ${propertyDeclaration.simpleName.asString()}${propertyDeclaration.optionalChainIfNullable}.$GENERATE_REMOTE($IDENTIFIER)",
                            )
                        } else {
                            null
                        }
                    }.toList()
                    if (services.isNotEmpty()) {
                        add(
                            services.joinToCode(separator = ",\n", suffix = ",\n"),
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
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                when {
                    typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class) -> {
                        generateServiceProperty(propertyDeclaration, typeDeclaration, type)
                    }

                    else -> {
                        invalidProperty(propertyDeclaration, BluetoothService::class)
                        null
                    }
                }
            }.toList(),
        )
    }

    private fun generateServiceProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec =
        PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            serverName(typeDeclaration, type).nullIfPropertyIsNull(propertyDeclaration),
        ).addModifiers(*type.additionalModifiers.toTypedArray())
            .apply {
                val serviceNeedsFormat = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration)
                when (type) {
                    GenerationType.Type.API -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        delegate(
                            "$LAZY { %L }",
                            CodeBlock.of(
                                "%T($SERVER.$SERVICES.$VALUE.%M(%T.$UUID)${serviceNeedsFormat.functionArgument})",
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
}
