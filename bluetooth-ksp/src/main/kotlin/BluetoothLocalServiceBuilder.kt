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
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.DSL
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT

internal class BluetoothLocalServiceBuilder(declaration: KSClassDeclaration, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, logger) {
    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType)).addModifiers(KModifier.SEALED)
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder(DSL)
                    .addFunctions(
                        declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (
                                typeDeclaration is KSClassDeclaration &&
                                (
                                    typeDeclaration.isAnnotationPresent(BluetoothService::class) ||
                                        typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class)
                                    )
                            ) {
                                val lambdaType = LambdaTypeName.get(
                                    receiver = NameHelper.nameFor(typeDeclaration, generationType).nestedClass(DSL),
                                    returnType = UNIT,
                                )
                                FunSpec.builder(propertyDeclaration.simpleName.asString()).addModifiers(KModifier.ABSTRACT)
                                    .addParameter(ACTION, lambdaType)
                                    .build()
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
                            propertyDeclaration.simpleName.asString(),
                            NameHelper.nameFor(typeDeclaration, generationType),
                        ).build()
                    } else {
                        logger.error("A BluetoothService should only have @${BluetoothService::class.simpleName} and @${BluetoothCharacteristic::class.simpleName} properties $typeDeclaration ${typeDeclaration.annotations}")
                        null
                    }
                }.toList(),
            )
        return Generated(listOf(typeSpec.build()))
    }

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): Generated = Generated(emptyList())
    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated = Generated(emptyList())
}
