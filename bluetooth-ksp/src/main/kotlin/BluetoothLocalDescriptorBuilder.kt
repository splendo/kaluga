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
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

internal class BluetoothLocalDescriptorBuilder(declaration: KSClassDeclaration, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, logger) {
    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, NameHelper.Target.SERVER)).addModifiers(KModifier.SEALED)
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder("DSL")
                    .apply {
                        var hasReadMethod = false
                        var hasWriteMethod = false
                        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                            when {
                                propertyDeclaration.isAnnotationPresent(Readable::class) -> {
                                    if (!hasReadMethod) {
                                        hasReadMethod = true
                                        val readMethod = "onRead${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration)

                                        val lambdaType = LambdaTypeName.get(
                                            receiver = NameHelper.nameFor(this@generateAPI, NameHelper.Target.SERVER),
                                            returnType = resultType.responseClassName,
                                        ).copy(suspending = true)
                                        addFunction(
                                            FunSpec.builder(readMethod).addModifiers(KModifier.ABSTRACT)
                                                .addParameter("action", lambdaType)
                                                .build(),
                                        )
                                    } else {
                                        logger.error("Only one @Readable property can be declared")
                                    }
                                }

                                propertyDeclaration.isAnnotationPresent(Writable::class) -> {
                                    if (!hasWriteMethod) {
                                        hasWriteMethod = true
                                        val writeMethod = "onWrite${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                                        val lambdaType = LambdaTypeName.get(
                                            receiver = NameHelper.nameFor(this@generateAPI, NameHelper.Target.SERVER),
                                            parameters = listOf(ParameterSpec(propertyDeclaration.simpleName.asString(), propertyDeclaration.type.resolve().toClassName())),
                                            returnType = ClassName("com.splendo.kaluga.bluetooth", "GattResponse", "WriteResponse"),
                                        ).copy(suspending = true)
                                        addFunction(
                                            FunSpec.builder(writeMethod).addModifiers(KModifier.ABSTRACT)
                                                .addParameter("action", lambdaType)
                                                .build(),
                                        )
                                    } else {
                                        logger.error("Only one @Writable property can be declared")
                                    }
                                }

                                else -> {
                                    logger.error("Only @Readable and @Writable properties can be declared")
                                }
                            }
                        }
                    }
                    .build(),
            )
        return Generated(listOf(typeSpec.build()))
    }
}
