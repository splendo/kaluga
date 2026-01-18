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
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.FROM_SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVICE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

internal class BluetoothRemoteServiceBuilder(
    declaration: KSClassDeclaration,
    private val service: BluetoothService,
    logger: KSPLogger
) : AbstractBluetoothClassBuilder(declaration, logger) {

    companion object {
        const val DISCOVERED_SERVICES = "discoveredServices"
        const val FROM_DISCOVERED_SERVICES = "fromDiscoveredServices"

    }

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
                            ParameterSpec(SERVICE, References.Bluetooth.remoteService),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                        )

                    )
                    .build()
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(FROM_DISCOVERED_SERVICES)
                            .addParameters(
                                listOfNotNull(
                                    ParameterSpec(DISCOVERED_SERVICES, LIST.parameterizedBy(References.Bluetooth.remoteService)),
                                    ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                                )
                            )
                            .returns(className)
                            .addStatement(
                                "return %T($DISCOVERED_SERVICES.%M(%M(%S))${if (needsFormatter) ", $FORMAT" else ""})",
                                className,
                                References.Bluetooth.get,
                                References.Bluetooth.uuidFrom,
                                service.uuid
                            )
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder(FROM_SERVICE)
                            .addParameters(
                                listOfNotNull(
                                    ParameterSpec(SERVICE, References.Bluetooth.remoteService),
                                    ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                                )
                            )
                            .returns(className)
                            .addStatement(
                                "$RETURN %T($SERVICE.includedServices.%M(%M(%S))${if (needsFormatter) ", $FORMAT" else ""})",
                                className,
                                References.Bluetooth.get,
                                References.Bluetooth.uuidFrom,
                                service.uuid
                            )
                            .build()
                    )
                    .build()
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(SERVICE, References.Bluetooth.remoteService)
                        .initializer(SERVICE).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT).build().takeIf { needsFormatter },
                )
            )
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }

    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        val typeSpec = TypeSpec.classBuilder(NameHelper.nameFor(this, generationType))
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
                        }.toList() +
                                properties.mapNotNull { propertyDeclaration ->
                                    val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                    if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)) {
                                        ParameterSpec(
                                            propertyDeclaration.simpleName.asString(),
                                            NameHelper.nameFor(typeDeclaration, generationType),
                                        )
                                    } else {
                                        null
                                    }
                                }.toList()
                    )
                    .build()
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType, imports: Generated.Imports): TypeSpec.Builder = apply {
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
                    if (typeDeclaration.isAnnotationPresent(BluetoothService::class) && typeDeclaration.declarations.filterIsInstance<KSPropertyDeclaration>().any { serviceProperties ->
                        serviceProperties.isAnnotationPresent(BluetoothService::class)
                        }) {
                        logger.error("An included @${BluetoothService::class} can not include its own services")
                    }

                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        NameHelper.nameFor(typeDeclaration, generationType),
                    )
                        .addModifiers(
                            *generationType.additionalModifiers.toTypedArray()
                        )
                        .apply {
                            when (generationType.type) {
                                GenerationType.Type.API -> {}
                                GenerationType.Type.BLUETOOTH -> {
                                    initializer("%T.$FROM_SERVICE($SERVICE${if (NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration)) ", $FORMAT" else "" })", NameHelper.nameFor(typeDeclaration, generationType))
                                }
                                GenerationType.Type.SIMULATOR -> {
                                    initializer(propertyDeclaration.simpleName.asString())
                                }
                            }
                        }
                        .build()
                } else {
                    logger.error("A BluetoothService should only have BluetoothService and BluetoothCharacteristic properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList(),
        )
    }
}
