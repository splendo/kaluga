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
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

internal class BluetoothRemoteCharacteristicBuilder(declaration: KSClassDeclaration, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, logger) {
    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType)).addModifiers(KModifier.SEALED)
            .addTypes(nested)
            .apply {
                var hasNotifiableProperty = false
                var hasReadMethod = false
                var hasWriteMethod = false
                declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                    val typeDeclaration = propertyDeclaration.type.resolve().declaration
                    when {
                        propertyDeclaration.isAnnotationPresent(Readable::class) -> {
                            if (!hasReadMethod) {
                                hasReadMethod = true
                                val responseType = propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }
                                val readMethod = "read$responseType"
                                val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration)
                                addFunction(
                                    FunSpec.builder(readMethod).addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT).returns(
                                        resultType.responseClassName,
                                    ).build(),
                                )
                            } else {
                                logger.error("Only one @Readable property can be declared")
                            }
                        }

                        propertyDeclaration.isAnnotationPresent(Writable::class) -> {
                            if (!hasWriteMethod) {
                                hasWriteMethod = true
                                val writeMethod = "write${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                addFunction(
                                    FunSpec.builder(
                                        writeMethod,
                                    ).addParameter(
                                        propertyDeclaration.simpleName.asString(),
                                        propertyDeclaration.type.resolve().toClassName(),
                                    ).addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT).returns(
                                        ClassName("com.splendo.kaluga.bluetooth", "GattResponse", "WriteResponse"),
                                    ).build(),
                                )
                            } else {
                                logger.error("Only one @Readable property can be declared")
                            }
                        }

                        propertyDeclaration.isAnnotationPresent(Notifiable::class) -> {
                            if (!hasNotifiableProperty) {
                                hasNotifiableProperty = true
                                addProperty(
                                    PropertySpec.builder(
                                        propertyDeclaration.simpleName.asString(),
                                        ClassName("kotlinx.coroutines.flow", "Flow").parameterizedBy(propertyDeclaration.type.resolve().toClassName()),
                                    ).build(),
                                )
                            } else {
                                logger.error("Only one @Notifiable property can be declared")
                            }
                        }

                        typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class) -> {
                            addProperty(
                                PropertySpec.builder(
                                    propertyDeclaration.simpleName.asString(),
                                    NameHelper.nameFor(typeDeclaration, generationType),
                                ).build(),
                            )
                        }

                        else -> {
                            logger.error("Only @Readable, @Writable, @Notifiable, or @BluetoothDescriptor properties can be declared")
                        }
                    }
                }
            }
        return Generated(listOf(typeSpec.build()))
    }

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        TODO()
    }
}
