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
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.ksp.BluetoothServerBuilder.Companion.GENERATE_CLIENT
import com.splendo.kaluga.bluetooth.ksp.helpers.BLUETOOTH
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SIMULATED
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

internal class BluetoothClientBuilder(declaration: KSClassDeclaration, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, logger) {

    companion object {
        const val IDENTIFIER = "identifier"
        const val DISCOVERED_SERVICES = "discoveredServices"
    }

    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec {
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this)
        return TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(
                            BLUETOOTH,
                        ).apply {
                            val returnType = NameHelper.nameFor(this@generateAPI, generationType.copy(type = GenerationType.Type.BLUETOOTH))
                            returns(returnType)
                                .addModifiers(KModifier.SUSPEND)
                                .addParameters(
                                    listOfNotNull(
                                        ParameterSpec(BLUETOOTH, References.Bluetooth.bluetoothService),
                                        ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                                        ParameterSpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                                            .defaultValue("%T", References.Bluetooth.Serialization.bluetoothFormat)
                                            .build().takeIf { needsFormatter },
                                    ),
                                )
                                .addStatement(
                                    "$RETURN %T($BLUETOOTH.allDevices().%M($IDENTIFIER).%M().%M()${if (needsFormatter) ", $FORMAT" else ""})",
                                    returnType,
                                    References.Bluetooth.get,
                                    References.Bluetooth.discoveredServices,
                                    References.KotlinX.Coroutines.Flow.first,
                                )
                        }
                            .build(),
                    )
                    .addFunction(
                        FunSpec.builder(SIMULATED).apply {
                            val returnType = NameHelper.nameFor(this@generateAPI, generationType.copy(type = GenerationType.Type.SIMULATOR))
                            val serverType = NameHelper.nameFor(this@generateAPI, GenerationType(GenerationType.Side.SERVER, GenerationType.Type.SIMULATOR))
                            returns(returnType)
                                .addParameters(
                                    listOf(
                                        ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                                        ParameterSpec("server", serverType),
                                    ),
                                )
                                .addStatement("$RETURN server.$GENERATE_CLIENT($IDENTIFIER)")
                        }.build(),
                    )
                    .build(),
            )
            .addTypes(nested)
            .generateBody(declarations, generationType)
            .build()
    }

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec {
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this)
        val className = NameHelper.nameFor(this, generationType)

        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DISCOVERED_SERVICES, LIST.parameterizedBy(References.Bluetooth.remoteService)),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                        ),
                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(DISCOVERED_SERVICES, LIST.parameterizedBy(References.Bluetooth.remoteService))
                        .initializer(DISCOVERED_SERVICES).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT).build().takeIf { needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, generationType).build()
    }

    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec {
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        return TypeSpec.classBuilder(NameHelper.nameFor(this, generationType))
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        properties.mapNotNull { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                                ParameterSpec(
                                    propertyDeclaration.simpleName.asString(),
                                    NameHelper.nameFor(typeDeclaration, generationType),
                                )
                            } else {
                                null
                            }
                        }.toList(),
                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addTypes(nested)
            .generateBody(declarations, generationType)
            .build()
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType): TypeSpec.Builder = apply {
        addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        NameHelper.nameFor(typeDeclaration, generationType),
                    )
                        .addModifiers(
                            *generationType.additionalModifiers.toTypedArray(),
                        )
                        .apply {
                            when (generationType.type) {
                                GenerationType.Type.API -> {}

                                GenerationType.Type.BLUETOOTH -> {
                                    initializer(
                                        "%T.${BluetoothRemoteServiceBuilder.FROM_DISCOVERED_SERVICES}(${DISCOVERED_SERVICES}${if (NeedsFormatterHelper.needsBluetoothFormatter(
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
                        }
                        .build()
                } else {
                    logger.error("A BluetoothClient should only have @${BluetoothService::class.simpleName} properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList(),
        )
    }
}
