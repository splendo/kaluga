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
import com.splendo.kaluga.bluetooth.ksp.helpers.DISCOVERED_SERVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.FROM_DISCOVERED_SERVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.FROM_SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.INCLUDED_SERVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.IT
import com.splendo.kaluga.bluetooth.ksp.helpers.LET
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OR_NULL
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.nullIfPropertyIsNull
import com.splendo.kaluga.bluetooth.ksp.helpers.orNullIfNullable
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

internal class BluetoothRemoteServiceBuilder(declaration: KSClassDeclaration, private val service: BluetoothService, options: Options, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, options, logger) {

    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val interfaceName = nameFor(declaration, GenerationType.CLIENT_API)
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
            .generateBody(declarations, GenerationType.Type.API)
            .build()
    }

    override fun generateBluetooth(nested: List<TypeSpec>): TypeSpec {
        val className = nameFor(declaration, GenerationType.CLIENT_BLUETOOTH)
        val interfaceName = nameFor(declaration, GenerationType.CLIENT_API)
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration)
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(SERVICE, References.Bluetooth.remoteService),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addType(
                generateBluetoothCompanionObject(needsFormatter, className, interfaceName),
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(SERVICE, References.Bluetooth.remoteService)
                        .initializer(SERVICE).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT).build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.BLUETOOTH)
            .build()
    }

    private fun generateBluetoothCompanionObject(needsFormatter: NeedsFormatterHelper.NeedsFormatter, className: ClassName, interfaceName: ClassName): TypeSpec =
        TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder(FROM_DISCOVERED_SERVICES)
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DISCOVERED_SERVICES, LIST.parameterizedBy(References.Bluetooth.remoteService)),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .returns(className)
                    .addStatement(
                        "$RETURN %T($DISCOVERED_SERVICES.%M(%T.$UUID)${needsFormatter.functionArgument})",
                        className,
                        References.Bluetooth.get,
                        interfaceName,
                    )
                    .build(),
            )
            .addFunction(
                FunSpec.builder("$FROM_DISCOVERED_SERVICES$OR_NULL")
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DISCOVERED_SERVICES, LIST.parameterizedBy(References.Bluetooth.remoteService)),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .returns(className.copy(nullable = true))
                    .addCode(
                        CodeBlock.builder()
                            .beginControlFlow(
                                "$RETURN $DISCOVERED_SERVICES.%M(%T.$UUID)?.$LET",
                                References.Bluetooth.getOrNull,
                                interfaceName,
                            )
                            .addStatement("%T($IT${needsFormatter.functionArgument})", className)
                            .endControlFlow()
                            .build(),
                    )
                    .build(),
            )
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
                        "$RETURN %T($SERVICE.$INCLUDED_SERVICES.%M(%T.$UUID)${needsFormatter.functionArgument})",
                        className,
                        References.Bluetooth.get,
                        interfaceName,
                    )
                    .build(),
            )
            .addFunction(
                FunSpec.builder("$FROM_SERVICE$OR_NULL")
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(SERVICE, References.Bluetooth.remoteService),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .returns(className.copy(nullable = true))
                    .addCode(
                        CodeBlock.builder()
                            .beginControlFlow(
                                "$RETURN $SERVICE.$INCLUDED_SERVICES.%M(%T.$UUID)?.$LET",
                                References.Bluetooth.getOrNull,
                                interfaceName,
                            )
                            .addStatement("%T($IT${needsFormatter.functionArgument})", className)
                            .endControlFlow()
                            .build(),
                    )
                    .build(),
            )
            .build()

    override fun generateSimulated(nested: List<TypeSpec>): TypeSpec {
        val className = nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val interfaceName = nameFor(declaration, GenerationType.CLIENT_API)
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        properties.mapNotNull { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                                ParameterSpec(
                                    propertyDeclaration.simpleName.asString(),
                                    nameFor(typeDeclaration, GenerationType.CLIENT_SIMULATOR).nullIfPropertyIsNull(propertyDeclaration),
                                )
                            } else {
                                null
                            }
                        }.toList() +
                            properties.mapNotNull { propertyDeclaration ->
                                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)) {
                                    ParameterSpec(
                                        propertyDeclaration.simpleName.asString(),
                                        nameFor(typeDeclaration, GenerationType.CLIENT_SIMULATOR).nullIfPropertyIsNull(propertyDeclaration),
                                    )
                                } else {
                                    null
                                }
                            }.toList(),
                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.SIMULATOR)
            .build()
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, type: GenerationType.Type): TypeSpec.Builder = apply {
        addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (
                    typeDeclaration is KSClassDeclaration &&
                    (
                        typeDeclaration.isAnnotationPresent(BluetoothService::class) ||
                            typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)
                        )
                ) {
                    if (typeDeclaration.isAnnotationPresent(BluetoothService::class) &&
                        typeDeclaration.declarations.filterIsInstance<KSPropertyDeclaration>().any { serviceProperties ->
                            serviceProperties.isAnnotationPresent(BluetoothService::class)
                        }
                    ) {
                        logger.error("An included @${BluetoothService::class} can not include its own services")
                    }

                    generateServiceOrCharacteristicProperty(propertyDeclaration, typeDeclaration, type)
                } else {
                    invalidProperty(propertyDeclaration, BluetoothService::class, BluetoothCharacteristic::class)
                    null
                }
            }.toList(),
        )
    }

    private fun generateServiceOrCharacteristicProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec =
        PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            clientName(typeDeclaration, type).nullIfPropertyIsNull(propertyDeclaration),
        )
            .addModifiers(
                *type.additionalModifiers.toTypedArray(),
            )
            .apply {
                when (type) {
                    GenerationType.Type.API -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        initializer(
                            "%T.$FROM_SERVICE${propertyDeclaration.orNullIfNullable}($SERVICE${NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration).functionArgument})",
                            clientName(typeDeclaration, type),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> {
                        initializer(propertyDeclaration.simpleName.asString())
                    }
                }
            }
            .build()
}
