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
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.CHANGED
import com.splendo.kaluga.bluetooth.ksp.helpers.DSL
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFY
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFY_ALL
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_READ
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_SUBSCRIBE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_UNSUBSCRIBE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SUBSCRIBERS
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.ksp.toClassName

internal class BluetoothLocalCharacteristicBuilder(declaration: KSClassDeclaration, logger: KSPLogger) : AbstractBluetoothClassBuilder(declaration, logger) {
    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType)).addModifiers(KModifier.SEALED)
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder(DSL)
                    .apply {
                        var hasReadMethod = false
                        var hasWriteMethod = false
                        var hasNotifyMethods = false
                        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (propertyDeclaration.isAnnotationPresent(Readable::class) || propertyDeclaration.isAnnotationPresent(Writable::class) || propertyDeclaration.isAnnotationPresent(
                                    Notifiable::class)) {
                                if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                                    if (!hasReadMethod) {
                                        hasReadMethod = true
                                        val readMethod = "$ON_READ${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration)

                                        val lambdaType = LambdaTypeName.get(
                                            receiver = NameHelper.nameFor(this@generateAPI, generationType),
                                            parameters = listOf(
                                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier)
                                            ),
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

                                if (propertyDeclaration.isAnnotationPresent(Writable::class)) {
                                    if (!hasWriteMethod) {
                                        hasWriteMethod = true
                                        val writeMethod = "$ON_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                                        val lambdaType = LambdaTypeName.get(
                                            receiver = NameHelper.nameFor(this@generateAPI, generationType),
                                            parameters = listOf(
                                                ParameterSpec(propertyDeclaration.simpleName.asString(), propertyDeclaration.type.resolve().toClassName()),
                                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier)
                                            ),
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

                                if (propertyDeclaration.isAnnotationPresent(Notifiable::class)) {
                                    if (!hasNotifyMethods) {
                                        hasNotifyMethods = true

                                        val lambdaType = LambdaTypeName.get(
                                            receiver = NameHelper.nameFor(this@generateAPI, generationType),
                                            parameters = listOf(
                                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier)
                                            ),
                                            returnType = UNIT,
                                        ).copy(suspending = true)
                                        addFunctions(
                                            listOf(ON_SUBSCRIBE, ON_UNSUBSCRIBE).map { method ->
                                                FunSpec.builder("$method${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}").addModifiers(KModifier.ABSTRACT)
                                                    .addParameter(ACTION, lambdaType)
                                                    .build()
                                            }
                                        )
                                    } else {
                                        logger.error("Only one @${Notifiable::class.simpleName} property can be declared")
                                    }
                                }
                            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                val lambdaType = LambdaTypeName.get(
                                    receiver = NameHelper.nameFor(typeDeclaration, generationType).nestedClass(DSL),
                                    returnType = UNIT,
                                )
                                addFunction(
                                    FunSpec.builder(propertyDeclaration.simpleName.asString()).addModifiers(KModifier.ABSTRACT)
                                        .addParameter(ACTION, lambdaType)
                                        .build(),
                                )
                            }

                            else {
                                logger.error("Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${Notifiable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared")
                            }
                        }
                    }
                    .build(),
            )
            .apply {
                declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                    val resolvedType = propertyDeclaration.type.resolve()
                    val typeDeclaration = resolvedType.declaration
                    if (propertyDeclaration.isAnnotationPresent(Readable::class) || propertyDeclaration.isAnnotationPresent(Writable::class) || propertyDeclaration.isAnnotationPresent(
                            Notifiable::class)) {
                        if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                            // Do Nothing
                        }

                        if (propertyDeclaration.isAnnotationPresent(Writable::class)) {
                            // Do Nothing
                        }

                        if (propertyDeclaration.isAnnotationPresent(Notifiable::class)) {
                            addProperty(
                                PropertySpec.builder("${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS", References.KotlinX.Coroutines.Flow.flow.parameterizedBy(
                                    LIST.parameterizedBy(References.Bluetooth.Device.identifier)
                                    )).addModifiers(KModifier.ABSTRACT).build())
                            addFunctions(
                                listOf(
                                FunSpec.builder("$NOTIFY_ALL${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}$CHANGED")
                                    .addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT)
                                    .addParameter(propertyDeclaration.simpleName.asString(), resolvedType.toClassName())
                                    .returns(BOOLEAN)
                                    .build(),
                                    FunSpec.builder("$NOTIFY${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}$CHANGED")
                                        .addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT)
                                        .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                        .addParameter(propertyDeclaration.simpleName.asString(), resolvedType.toClassName())
                                        .returns(BOOLEAN)
                                        .build(),
                                )
                            )
                        }
                    } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                        addProperty(
                            PropertySpec.builder(propertyDeclaration.simpleName.asString(), NameHelper.nameFor(typeDeclaration, generationType))
                                .addModifiers(KModifier.ABSTRACT).build(),
                        )
                    }

                    else {
                        logger.error("Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${Notifiable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared")
                    }
                }
            }
        return Generated(listOf(typeSpec.build()))
    }

    override fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType)).addModifiers(KModifier.SEALED)
            .addTypes(nested)
        return Generated(listOf(typeSpec.build()))
    }
    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated = Generated(emptyList())
}
