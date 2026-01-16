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
import com.splendo.kaluga.bluetooth.ksp.BluetoothClientBuilder.Companion.BLUETOOTH
import com.splendo.kaluga.bluetooth.ksp.BluetoothClientBuilder.Companion.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT

internal class BluetoothServerBuilder(declaration: KSClassDeclaration, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, logger) {

    companion object {
        const val SERVER = "server"
        const val SETTINGS_BUILDER = "settingsBuilder"
        const val PERMISSIONS = "permissions"
        const val COROUTINE_CONTEXT = "coroutineContext"
        const val ADVERTISEMENT_BUILDER = "advertisementBuilder"
    }

    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(
                            BLUETOOTH
                        ).apply {
                            val serverNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateAPI, NeedsFormatterHelper.Target.SERVER)
                            val delegateNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateAPI, NeedsFormatterHelper.Target.SERVER_DSL)
                            val returnType = NameHelper.nameFor(this@generateAPI, generationType.copy(type = GenerationType.Type.BLUETOOTH))
                            returns(returnType)
                                .addModifiers(KModifier.SUSPEND)
                                .addParameters(
                                    listOfNotNull(
                                        ParameterSpec(BUILDER, References.Bluetooth.bluetoothBuilder),
                                        ParameterSpec("$SERVER$DELEGATE".replaceFirstChar { it.lowercase() }, NameHelper.nameFor(this@generateAPI, generationType).nestedClass(DELEGATE)),
                                        ParameterSpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                                            .defaultValue("%T", References.Bluetooth.Serialization.bluetoothFormat)
                                            .build().takeIf { serverNeedsFormatter || delegateNeedsFormatter },
                                        ParameterSpec.builder(SETTINGS_BUILDER,
                                            LambdaTypeName.get(parameters = listOf(ParameterSpec(PERMISSIONS, References.Permissions.permissions)), returnType = References.Bluetooth.Server.serverSettings)
                                        ).defaultValue("{ %T(permissions = it) }", References.Bluetooth.Server.serverSettings)
                                            .build(),
                                        ParameterSpec.builder(COROUTINE_CONTEXT, References.Kotlin.Coroutines.coroutineContext)
                                            .defaultValue("%M(%S)", References.Base.singleThreadDispatcher, "BluetoothServer")
                                            .build(),
                                        ParameterSpec.builder(
                                            ADVERTISEMENT_BUILDER,
                                            LambdaTypeName.get(
                                                receiver = References.Bluetooth.Server.advertiseDataBuilder,
                                                returnType = UNIT
                                            )
                                        ).defaultValue(CodeBlock.of("{ }"))
                                            .build()
                                    )
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
                                        .addStatement("advertise($ADVERTISEMENT_BUILDER)")
                                        .addStatement("%T.$CONFIGURE($THIS,$SERVER$DELEGATE${if (delegateNeedsFormatter) ", $FORMAT" else ""})", returnType)
                                        .unindent()
                                        .addStatement("},")
                                        .apply {
                                            if (serverNeedsFormatter) {
                                                addStatement("$FORMAT,")
                                            }
                                        }
                                        .unindent()
                                        .add(")\n")
                                        .build()
                                )
                        }
                            .build()
                    )
                    .build()
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
                                        typeDeclaration.isAnnotationPresent(BluetoothService::class)
                                        )
                            ) {
                                PropertySpec.builder(
                                    "${propertyDeclaration.simpleName.asString()}$DELEGATE",
                                    NameHelper.nameFor(typeDeclaration, generationType).nestedClass(DELEGATE),
                                ).build()
                            } else {
                                logger.error("A BluetoothServer should only have @${BluetoothService::class.simpleName} properties $typeDeclaration ${typeDeclaration.annotations}")
                                null
                            }
                        }.toList(),
                    )
                    .build()
            )
            .generateBody(declarations, generationType, Generated.Imports())
        return Generated(listOf(typeSpec.build()))
    }

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val className = NameHelper.nameFor(this, generationType)
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER)
        val typeSpec = TypeSpec.classBuilder(className).addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(SERVER, References.Bluetooth.Server.bluetoothServer),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
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
                                addParameter(BUILDER, References.Bluetooth.Server.bluetoothServerDSL)
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
                                        .apply {
                                            declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                                                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                                when {
                                                    typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class) -> {
                                                        val delegateNeedsFormatter =
                                                            NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, NeedsFormatterHelper.Target.SERVER_DSL)
                                                        addStatement(
                                                            "%T.$CONFIGURE($BUILDER, $delegateName.${propertyDeclaration.simpleName.asString()}$DELEGATE${if (delegateNeedsFormatter) ", $FORMAT" else ""})",
                                                            NameHelper.nameFor(typeDeclaration, generationType)
                                                        )
                                                    }
                                                    else -> {
                                                        logger.error("Only @${BluetoothService::class.simpleName} properties can be declared")
                                                    }
                                                }
                                            }
                                        }
                                        .build()
                                )
                            }
                            .build()
                    )
                    .build()
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(SERVER, References.Bluetooth.Server.bluetoothServer)
                        .initializer(SERVER).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT)
                        .build().takeIf { needsFormatter },
                )
            )
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }
    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated = Generated(emptyList())

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType, imports: Generated.Imports): TypeSpec.Builder = apply {
        addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                when {
                    typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class) -> {
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
                                                "%T($SERVER.services.value.%M(%M(%S))${if (serviceNeedsFormat) ", $FORMAT" else ""})",
                                                NameHelper.nameFor(typeDeclaration, generationType),
                                                References.Bluetooth.get,
                                                References.Bluetooth.uuidFrom,
                                                service.uuid
                                            )
                                        )
                                    }

                                    GenerationType.Type.SIMULATOR -> {}
                                }
                            }
                            .build()

                    }
                    else -> {
                        logger.error("A BluetoothServer should only have @${BluetoothService::class.simpleName} properties $typeDeclaration ${typeDeclaration.annotations}")
                        null
                    }
                }
            }.toList()
        )

    }
}
