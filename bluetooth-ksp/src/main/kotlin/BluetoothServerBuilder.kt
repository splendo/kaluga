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
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.COROUTINE_CONTEXT
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
import com.splendo.kaluga.bluetooth.ksp.helpers.SIMULATED
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.delegateParameterName
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

internal class BluetoothServerBuilder(declaration: KSClassDeclaration, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, logger) {

    companion object {
        const val SERVER = "server"
        const val SETTINGS_BUILDER = "settingsBuilder"
        const val PERMISSIONS = "permissions"
        const val GENERATE_CLIENT = "generateClient"
    }

    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
        val delegateParameter = ParameterSpec(
            "$SERVER$DELEGATE".replaceFirstChar {
                it.lowercase()
            },
            interfaceName.nestedClass(DELEGATE),
        )
        return TypeSpec.interfaceBuilder(interfaceName)
            .addSuperinterface(References.Kotlin.autoCloseable)
            .addType(
                generateAPICompanionObject(delegateParameter, interfaceName),
            )
            .addTypes(nested)
            .addType(
                generateDelegate(),
            )
            .generateBody(declarations, GenerationType.Type.API)
            .build()
    }

    private fun generateAPICompanionObject(delegateParameter: ParameterSpec, interfaceName: ClassName): TypeSpec = TypeSpec.companionObjectBuilder()
        .addFunction(
            generateAPICreateBluetoothMethod(delegateParameter, interfaceName),
        )
        .addFunction(
            generateAPICreateSimulatorMethod(delegateParameter, interfaceName),
        )
        .build()

    private fun generateAPICreateBluetoothMethod(delegateParameter: ParameterSpec, interfaceName: ClassName): FunSpec = FunSpec.builder(
        BLUETOOTH,
    ).apply {
        val serverNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER)
        val delegateNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER_DSL)
        val returnType = NameHelper.nameFor(declaration, GenerationType.SERVER_BLUETOOTH)
        returns(returnType)
            .addModifiers(KModifier.SUSPEND)
            .addParameters(
                listOfNotNull(
                    ParameterSpec(BUILDER, References.Bluetooth.bluetoothBuilder),
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
                    ).defaultValue("{ %T(permissions = it) }", References.Bluetooth.Server.serverSettings)
                        .build(),
                    ParameterSpec.builder(
                        "serverName",
                        STRING.copy(nullable = true),
                    ).defaultValue(
                        declaration.getAnnotationsByType(AdvertisingName::class).firstOrNull()?.let {
                            CodeBlock.of("%S", it.name)
                        } ?: CodeBlock.of("null"),
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
                    .add("$BUILDER.createServer(")
                    .indent()
                    .addStatement("settingsBuilder = $SETTINGS_BUILDER,")
                    .addStatement("coroutineContext = $COROUTINE_CONTEXT,")
                    .unindent()
                    .addStatement(") {")
                    .indent()
                    .beginControlFlow("advertise")
                    .apply {
                        addStatement("localName = serverName")
                        val advertisingUUIDs = declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull {
                            val resolvedDeclaration = it.type.resolve().declaration
                            if (resolvedDeclaration is KSClassDeclaration &&
                                resolvedDeclaration.isAnnotationPresent(BluetoothService::class) &&
                                it.isAnnotationPresent(Advertising::class)
                            ) {
                                CodeBlock.of("%T.$UUID", NameHelper.nameFor(resolvedDeclaration, GenerationType.SERVER_API))
                            } else {
                                null
                            }
                        }.toList()
                        if (advertisingUUIDs.isNotEmpty()) {
                            addStatement("serviceUUIDs(%L)", advertisingUUIDs.joinToCode(separator = ", "))
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

    private fun generateAPICreateSimulatorMethod(delegateParameter: ParameterSpec, interfaceName: ClassName): FunSpec = FunSpec.builder(SIMULATED).apply {
        val returnType = NameHelper.nameFor(declaration, GenerationType.SERVER_SIMULATOR)
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
                        NameHelper.nameFor(typeDeclaration, GenerationType.SERVER_API).nestedClass(DELEGATE),
                    ).build()
                } else {
                    logger.error("A BluetoothServer should only have @${BluetoothService::class.simpleName} properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList(),
        )
        .build()

    override fun generateBluetooth(nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(declaration, GenerationType.SERVER_BLUETOOTH)
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
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
                FunSpec.builder("close")
                    .addModifiers(KModifier.OVERRIDE)
                    .addCode(CodeBlock.of("$SERVER.close()"))
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
                                            addStatement(
                                                "%T.$CONFIGURE($BUILDER, $delegateParameterName.${propertyDeclaration.delegateParameterName}${delegateNeedsFormatter.functionArgument})",
                                                NameHelper.nameFor(typeDeclaration, GenerationType.SERVER_BLUETOOTH),
                                            )
                                        }

                                        else -> {
                                            logger.error("Only @${BluetoothService::class.simpleName} properties can be declared")
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
        val className = NameHelper.nameFor(declaration, GenerationType.SERVER_SIMULATOR)
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
        val delegateName = interfaceName.nestedClass(DELEGATE)
        val remote = NameHelper.nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
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
            .addProperty(
                PropertySpec.builder(REMOTES, MUTABLE_MAP.parameterizedBy(References.Bluetooth.Device.identifier, remote))
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("mutableMapOf()")
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(IS_CLOSED, References.KotlinX.Coroutines.completableDeferred.parameterizedBy(UNIT))
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("%T()", References.KotlinX.Coroutines.completableDeferred)
                    .build(),
            )
            .addFunction(
                generateSimulatorGenerateRemoteMethod(remote, properties),
            )
            .addTypes(nested)
            .addFunction(
                FunSpec.builder("close")
                    .addModifiers(KModifier.OVERRIDE)
                    .addCode(
                        CodeBlock.builder()
                            .addStatement("$IS_CLOSED.complete(Unit)")
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
                .add("$RETURN $REMOTES.getOrPut($IDENTIFIER) {\n")
                .indent()
                .beginControlFlow("$WITH (${declaration.delegateParameterName})")
                .apply {
                    addStatement("%T(", remote)
                    indent()
                    val services = properties.mapNotNull { propertyDeclaration ->
                        val typeDeclaration = propertyDeclaration.type.resolve().declaration
                        if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                            CodeBlock.of("${propertyDeclaration.simpleName.asString()} = ${propertyDeclaration.simpleName.asString()}.$GENERATE_REMOTE($IDENTIFIER)")
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
                        logger.error("A BluetoothServer should only have @${BluetoothService::class.simpleName} properties $typeDeclaration ${typeDeclaration.annotations}")
                        null
                    }
                }
            }.toList(),
        )
    }

    private fun generateServiceProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec =
        PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            NameHelper.serverName(typeDeclaration, type),
        ).addModifiers(*type.additionalModifiers.toTypedArray())
            .apply {
                val serviceNeedsFormat = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration)
                when (type) {
                    GenerationType.Type.API -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        delegate(
                            "lazy { %L }",
                            CodeBlock.of(
                                "%T($SERVER.services.value.%M(%T.$UUID)${serviceNeedsFormat.functionArgument})",
                                NameHelper.serverName(typeDeclaration, type),
                                References.Bluetooth.get,
                                NameHelper.serverName(typeDeclaration, GenerationType.Type.API),
                            ),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> {
                        initializer(
                            CodeBlock.of(
                                "%T(${declaration.delegateParameterName}.${propertyDeclaration.delegateParameterName}, $COROUTINE_SCOPE, $IS_CLOSED)",
                                NameHelper.serverName(typeDeclaration, type),
                            ),
                        )
                    }
                }
            }
            .build()
}
