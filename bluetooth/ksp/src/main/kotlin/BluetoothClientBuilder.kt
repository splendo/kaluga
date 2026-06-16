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
import com.splendo.kaluga.bluetooth.ksp.helpers.ALL_DEVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.BLUETOOTH
import com.splendo.kaluga.bluetooth.ksp.helpers.DISCOVERED_SERVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.FROM_DISCOVERED_SERVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.GENERATE_CLIENT
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVER
import com.splendo.kaluga.bluetooth.ksp.helpers.SIMULATED
import com.splendo.kaluga.bluetooth.ksp.helpers.nullIfPropertyIsNull
import com.splendo.kaluga.bluetooth.ksp.helpers.orNullIfNullable
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

internal class BluetoothClientBuilder(declaration: KSClassDeclaration, options: Options, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, options, logger) {

    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration)
        return TypeSpec.interfaceBuilder(NameHelper.nameFor(declaration, GenerationType.CLIENT_API))
            .addType(generateAPICompanionObject(needsFormatter))
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.API)
            .build()
    }

    private fun generateAPICompanionObject(needsFormatter: NeedsFormatterHelper.NeedsFormatter): TypeSpec = TypeSpec.companionObjectBuilder()
        .apply {
            if (options.generateBluetoothImplementation) {
                addFunction(generateAPIBluetoothMethod(needsFormatter))
            }
            if (options.generateSimulatorImplementation && options.generateServer) {
                addFunction(generateAPISimulatorMethod())
            }
        }
        .build()

    private fun generateAPIBluetoothMethod(needsFormatter: NeedsFormatterHelper.NeedsFormatter): FunSpec = FunSpec.builder(
        BLUETOOTH,
    ).apply {
        val returnType = NameHelper.nameFor(declaration, GenerationType.CLIENT_BLUETOOTH)
        returns(returnType)
            .addModifiers(KModifier.SUSPEND)
            .addParameters(
                listOfNotNull(
                    ParameterSpec(BLUETOOTH, References.Bluetooth.bluetoothClient),
                    ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                    ParameterSpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .defaultValue("%T", References.Bluetooth.Serialization.bluetoothFormat)
                        .build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addStatement(
                "$RETURN %T($BLUETOOTH.$ALL_DEVICES().%M($IDENTIFIER).%M().%M()${needsFormatter.functionArgument})",
                returnType,
                References.Bluetooth.get,
                References.Bluetooth.discoveredServices,
                References.KotlinX.Coroutines.Flow.first,
            )
    }
        .build()
    private fun generateAPISimulatorMethod(): FunSpec = FunSpec.builder(SIMULATED).apply {
        val returnType = NameHelper.nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val serverType = NameHelper.nameFor(declaration, GenerationType.SERVER_SIMULATOR)
        returns(returnType)
            .addParameters(
                listOf(
                    ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                    ParameterSpec(SERVER, serverType),
                ),
            )
            .addStatement("$RETURN $SERVER.$GENERATE_CLIENT($IDENTIFIER)")
    }.build()

    override fun generateBluetooth(nested: List<TypeSpec>): TypeSpec {
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration)
        val className = NameHelper.nameFor(declaration, GenerationType.CLIENT_BLUETOOTH)

        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DISCOVERED_SERVICES, LIST.parameterizedBy(References.Bluetooth.remoteService)),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(declaration, GenerationType.CLIENT_API))
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(DISCOVERED_SERVICES, LIST.parameterizedBy(References.Bluetooth.remoteService))
                        .initializer(DISCOVERED_SERVICES).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT).build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.BLUETOOTH).build()
    }

    override fun generateSimulated(nested: List<TypeSpec>): TypeSpec {
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        return TypeSpec.classBuilder(NameHelper.nameFor(declaration, GenerationType.CLIENT_SIMULATOR))
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        properties.mapNotNull { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                                ParameterSpec(
                                    propertyDeclaration.simpleName.asString(),
                                    NameHelper.nameFor(typeDeclaration, GenerationType.CLIENT_SIMULATOR).nullIfPropertyIsNull(propertyDeclaration),
                                )
                            } else {
                                null
                            }
                        }.toList(),
                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(declaration, GenerationType.CLIENT_API))
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.SIMULATOR)
            .build()
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, type: GenerationType.Type): TypeSpec.Builder = apply {
        addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                    generateServiceProperty(propertyDeclaration, typeDeclaration, type)
                } else {
                    invalidProperty(propertyDeclaration, BluetoothService::class)
                    null
                }
            }.toList(),
        )
    }

    private fun generateServiceProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec =
        PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            NameHelper.clientName(typeDeclaration, type).nullIfPropertyIsNull(propertyDeclaration),
        )
            .addModifiers(*type.additionalModifiers.toTypedArray())
            .apply {
                when (type) {
                    GenerationType.Type.API -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        initializer(
                            "%T.$FROM_DISCOVERED_SERVICES${propertyDeclaration.orNullIfNullable}(" +
                                "${DISCOVERED_SERVICES}${NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration).functionArgument}" +
                                ")",
                            NameHelper.clientName(typeDeclaration, type),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> {
                        initializer(propertyDeclaration.simpleName.asString())
                    }
                }
            }
            .build()
}
