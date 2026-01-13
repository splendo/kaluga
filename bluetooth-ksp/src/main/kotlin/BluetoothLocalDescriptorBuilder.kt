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
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.DSL
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_READ
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

internal class BluetoothLocalDescriptorBuilder(declaration: KSClassDeclaration, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, logger) {
    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType)).addModifiers(KModifier.SEALED)
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder(DSL)
                    .apply {
                        var hasReadMethod = false
                        var hasWriteMethod = false
                        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                            when {
                                propertyDeclaration.isAnnotationPresent(Readable::class) -> {
                                    if (!hasReadMethod) {
                                        hasReadMethod = true
                                        val readMethod = "$ON_READ${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration)

                                        val lambdaType = LambdaTypeName.get(
                                            receiver = NameHelper.nameFor(this@generateAPI, generationType),
                                            returnType = resultType.responseClassName,
                                        ).copy(suspending = true)
                                        addFunction(
                                            FunSpec.builder(readMethod).addModifiers(KModifier.ABSTRACT)
                                                .addParameter(ACTION, lambdaType)
                                                .build(),
                                        )
                                    } else {
                                        logger.error("Only one @${Readable::class.simpleName} property can be declared")
                                    }
                                }

                                propertyDeclaration.isAnnotationPresent(Writable::class) -> {
                                    if (!hasWriteMethod) {
                                        hasWriteMethod = true
                                        val writeMethod = "$ON_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                                        val lambdaType = LambdaTypeName.get(
                                            receiver = NameHelper.nameFor(this@generateAPI, generationType),
                                            parameters = listOf(ParameterSpec(propertyDeclaration.simpleName.asString(), propertyDeclaration.type.resolve().toClassName())),
                                            returnType = References.Bluetooth.writeResponse,
                                        ).copy(suspending = true)
                                        addFunction(
                                            FunSpec.builder(writeMethod).addModifiers(KModifier.ABSTRACT)
                                                .addParameter(ACTION, lambdaType)
                                                .build(),
                                        )
                                    } else {
                                        logger.error("Only one @${Writable::class.simpleName} property can be declared")
                                    }
                                }

                                else -> {
                                    logger.error("Only @${Readable::class.simpleName} and @${Writable::class.simpleName} properties can be declared")
                                }
                            }
                        }
                    }
                    .build(),
            )
        return Generated(listOf(typeSpec.build()))
    }

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): Generated = Generated(emptyList())
    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated = Generated(emptyList())
}
