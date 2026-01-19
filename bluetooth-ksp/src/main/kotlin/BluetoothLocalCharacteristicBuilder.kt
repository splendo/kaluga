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
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.Encrypted
import com.splendo.kaluga.bluetooth.annotations.Indicatable
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.annotations.WritableSigned
import com.splendo.kaluga.bluetooth.annotations.WritableWithoutResponse
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CHANGED
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.COROUTINE_SCOPE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.EXCEPTION
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.GENERATE_REMOTE
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.IS_CLOSED
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFY
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFY_ALL
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_FAILED_TO_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.REMOTES
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SUBSCRIBERS
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.delegateName
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.splendo.kaluga.bluetooth.ksp.helpers.isNotifiable
import com.splendo.kaluga.bluetooth.ksp.helpers.isReadable
import com.splendo.kaluga.bluetooth.ksp.helpers.isWritable
import com.splendo.kaluga.bluetooth.ksp.helpers.onReadMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.onWriteMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.serializer
import com.splendo.kaluga.bluetooth.ksp.helpers.subscribeMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.unsubscribeMethodName
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MUTABLE_MAP
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.toTypeName

internal class BluetoothLocalCharacteristicBuilder(declaration: KSClassDeclaration, private val characteristic: BluetoothCharacteristic, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, logger) {

    companion object {
        private const val MUTABLE_FLOW = "mutableFlow"
    }

    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder(UUID, References.Bluetooth.uuid)
                            .initializer("%M(%S)", References.Bluetooth.uuidFrom, characteristic.uuid)
                            .build(),
                    )
                    .build(),
            )
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder(DELEGATE)
                    .apply {
                        val receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API))
                        var hasReadMethod = false
                        var hasWriteMethod = false
                        var hasNotifyMethods = false
                        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (propertyDeclaration.isReadable ||
                                propertyDeclaration.isWritable ||
                                propertyDeclaration.isNotifiable
                            ) {
                                if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                                    if (!hasReadMethod) {
                                        hasReadMethod = true
                                        val readMethod = propertyDeclaration.onReadMethodName
                                        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, logger)
                                        val onReadFunSpec = FunSpec.builder(readMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                                            .receiver(receiver)
                                            .addParameters(
                                                listOfNotNull(
                                                    ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                                                    ParameterSpec(OFFSET, INT).takeIf { !resultType.hasCustomResult },
                                                ),
                                            )
                                            .returns(resultType.responseClassName)

                                        addFunction(
                                            onReadFunSpec
                                                .build(),
                                        )
                                    } else {
                                        logger.error("Only one @${Readable::class.simpleName} property can be declared")
                                    }
                                }
                                if (propertyDeclaration.isAnnotationPresent(Writable::class) || propertyDeclaration.isAnnotationPresent(WritableWithoutResponse::class) ||
                                    propertyDeclaration.isAnnotationPresent(WritableSigned::class)
                                ) {
                                    if (!hasWriteMethod) {
                                        hasWriteMethod = true

                                        val writeMethod = propertyDeclaration.onWriteMethodName

                                        val onWriteFunSpec = FunSpec.builder(writeMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                                            .receiver(receiver)
                                            .addParameter(propertyDeclaration.simpleName.asString(), propertyDeclaration.type.resolve().toTypeName())
                                            .apply {
                                                if (propertyDeclaration.isByteArray) {
                                                    addParameter(OFFSET, INT)
                                                }
                                            }
                                            .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                            .returns(References.Bluetooth.writeResponse)

                                        addFunction(
                                            onWriteFunSpec
                                                .build(),
                                        )
                                        if (!propertyDeclaration.isByteArray) {
                                            val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                                            val onFailedToWriteFunSpec = FunSpec.builder(failedToWriteMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                                                .receiver(receiver)
                                                .addParameter(EXCEPTION, References.Kotlin.exception)
                                                .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                                .returns(References.Bluetooth.writeResponse)

                                            addFunction(
                                                onFailedToWriteFunSpec
                                                    .build(),
                                            )
                                        }
                                    } else {
                                        logger.error(
                                            "Only one @${Writable::class.simpleName} / @${WritableWithoutResponse::class.simpleName} / @${WritableSigned::class.simpleName} property can be declared",
                                        )
                                    }
                                }

                                if (propertyDeclaration.isAnnotationPresent(Notifiable::class) || propertyDeclaration.isAnnotationPresent(Indicatable::class)) {
                                    if (!hasNotifyMethods) {
                                        hasNotifyMethods = true

                                        val subscribeMethod = propertyDeclaration.subscribeMethodName
                                        val unsubscribeMethod = propertyDeclaration.unsubscribeMethodName

                                        addFunctions(
                                            listOf(
                                                FunSpec.builder(subscribeMethod).addModifiers(KModifier.ABSTRACT)
                                                    .receiver(receiver)
                                                    .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                                    .build(),
                                                FunSpec.builder(unsubscribeMethod).addModifiers(KModifier.ABSTRACT)
                                                    .receiver(receiver)
                                                    .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                                    .build(),
                                            ),
                                        )
                                    } else {
                                        logger.error("Only one @${Notifiable::class.simpleName} / @${Indicatable::class.simpleName} property can be declared")
                                    }
                                }
                            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                addProperty(
                                    propertyDeclaration.delegateName,
                                    NameHelper.nameFor(typeDeclaration, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE),
                                )
                            } else {
                                logger.error(
                                    "Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared",
                                )
                            }
                        }
                    }
                    .build(),
            )
            .generateBody(declarations, generationType, imports)
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
                            ParameterSpec(CHARACTERISTIC, characteristicClass()),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addFunction(
                        FunSpec.builder(CONFIGURE)
                            .apply {
                                val delegateName = "${declaration.simpleName.asString().replaceFirstChar { it.lowercase() }}$DELEGATE"
                                addParameter(BUILDER, References.Bluetooth.Server.localServiceDSL)
                                addParameter(
                                    delegateName,
                                    NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE),
                                )
                                val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER_DSL)
                                if (needsFormatter) {
                                    addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                                }
                                addCode(
                                    CodeBlock.builder()
                                        .beginControlFlow(
                                            "$RETURN $BUILDER.characteristic(%T.$UUID) {",
                                            NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)),
                                        )
                                        .apply {
                                            var hasReadMethod = false
                                            var hasWriteMethod = false
                                            var hasNotifyMethods = false
                                            val constructorFormat = if (NeedsFormatterHelper.needsBluetoothFormatter(
                                                    declaration,
                                                    NeedsFormatterHelper.Target.SERVER,
                                                )
                                            ) {
                                                ", $FORMAT"
                                            } else {
                                                ""
                                            }
                                            fun castingMethod(scope: String): CodeBlock {
                                                val scopeMethod = if (scope.isNotEmpty()) "$THIS@$scope" else THIS
                                                return if (declaration.isNotifiable()) {
                                                    CodeBlock.of(
                                                        "%T($scopeMethod as %T$constructorFormat)",
                                                        NameHelper.nameFor(declaration, generationType),
                                                        References.Bluetooth.Server.localCharacteristicNotifiable,
                                                    )
                                                } else {
                                                    CodeBlock.of("%T($scopeMethod$constructorFormat)", NameHelper.nameFor(declaration, generationType))
                                                }
                                            }
                                            declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                                                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                                if (propertyDeclaration.isReadable ||
                                                    propertyDeclaration.isWritable ||
                                                    propertyDeclaration.isNotifiable
                                                ) {
                                                    if (propertyDeclaration.isReadable) {
                                                        if (!hasReadMethod) {
                                                            hasReadMethod = true
                                                            val readMethod = propertyDeclaration.onReadMethodName
                                                            val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, logger)
                                                            beginControlFlow("readable(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, $OFFSET ->")
                                                                .beginControlFlow("$WITH($delegateName)")
                                                                .add(resultType.parseBluetoothResult(CodeBlock.of("%L.$readMethod(device.identifier", castingMethod("readable"))))
                                                                .endControlFlow()
                                                                .endControlFlow()
                                                        } else {
                                                            logger.error("Only one @${Readable::class.simpleName} property can be declared")
                                                        }
                                                    }
                                                    if (propertyDeclaration.isWritable) {
                                                        if (!hasWriteMethod) {
                                                            hasWriteMethod = true
                                                            val writeMethod = propertyDeclaration.onWriteMethodName

                                                            val properties = listOfNotNull(
                                                                References.Bluetooth.writeProperty.takeIf { propertyDeclaration.isAnnotationPresent(Writable::class) },
                                                                References.Bluetooth.writeWithoutResponseProperty.takeIf {
                                                                    propertyDeclaration.isAnnotationPresent(WritableWithoutResponse::class)
                                                                },
                                                                References.Bluetooth.signedWriteProperty.takeIf { propertyDeclaration.isAnnotationPresent(WritableSigned::class) },
                                                            )
                                                            val propertiesCode = properties.joinToCode(prefix = "setOf(", suffix = ")") { CodeBlock.of("%T", it) }

                                                            if (propertyDeclaration.isByteArray) {
                                                                beginControlFlow(
                                                                    "writable(%L, ${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, value, $OFFSET ->",
                                                                    propertiesCode,
                                                                )
                                                                    .beginControlFlow("$WITH($delegateName)")
                                                                    .addStatement("%L.$writeMethod(value, $OFFSET, device.identifier)", castingMethod("writable"))
                                                                    .endControlFlow()
                                                                    .endControlFlow()
                                                            } else {
                                                                val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar {
                                                                    it.uppercase()
                                                                }}"
                                                                addStatement("writable(")
                                                                    .indent()
                                                                    .addStatement("properties = %L,", propertiesCode)
                                                                    .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                                                                    .addStatement(
                                                                        "deserializationStrategy = %L,",
                                                                        propertyDeclaration.type.resolve().toTypeName().serializer(logger),
                                                                    )
                                                                    .addStatement("bluetoothFormat = $FORMAT,")
                                                                    .addStatement("onFailedToWrite = { device, exception ->")
                                                                    .indent()
                                                                    .beginControlFlow("with($delegateName)")
                                                                    .addStatement("%L.$failedToWriteMethod(exception, device.identifier)", castingMethod("writable"))
                                                                    .endControlFlow()
                                                                    .unindent()
                                                                    .addStatement("},")
                                                                    .addStatement("onWrite = { device, value ->")
                                                                    .indent()
                                                                    .beginControlFlow("$WITH($delegateName)")
                                                                    .addStatement("%L.$writeMethod(value, device.identifier)", castingMethod("writable"))
                                                                    .endControlFlow()
                                                                    .unindent()
                                                                    .addStatement("}")
                                                                    .unindent()
                                                                    .addStatement(")")
                                                            }
                                                        } else {
                                                            logger.error(
                                                                "Only one @${Writable::class.simpleName} / @${WritableWithoutResponse::class.simpleName} / @${WritableSigned::class.simpleName} property can be declared",
                                                            )
                                                        }
                                                    }

                                                    if (propertyDeclaration.isNotifiable) {
                                                        if (!hasNotifyMethods) {
                                                            hasNotifyMethods = true
                                                            val properties = listOfNotNull(
                                                                References.Bluetooth.notifyProperty.takeIf { propertyDeclaration.isAnnotationPresent(Notifiable::class) },
                                                                References.Bluetooth.indicatableProperty.takeIf { propertyDeclaration.isAnnotationPresent(Indicatable::class) },
                                                            )
                                                            val propertiesCode = properties.joinToCode(prefix = "setOf(", suffix = ")") { CodeBlock.of("%T", it) }

                                                            val subscribeMethod = propertyDeclaration.subscribeMethodName
                                                            val unsubscribeMethod = propertyDeclaration.unsubscribeMethodName
                                                            addStatement("notifiable(")
                                                                .indent()
                                                                .addStatement("properties = %L,", propertiesCode)
                                                                .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                                                                .addStatement("onSubscribe = { device ->")
                                                                .indent()
                                                                .beginControlFlow("$WITH($delegateName)")
                                                                .addStatement(
                                                                    "%T($THIS@notifiable$constructorFormat).$subscribeMethod(device.identifier)",
                                                                    NameHelper.nameFor(declaration, generationType),
                                                                )
                                                                .endControlFlow()
                                                                .unindent()
                                                                .addStatement("},")
                                                                .addStatement("onUnsubscribe = { device ->")
                                                                .indent()
                                                                .beginControlFlow("$WITH($delegateName)")
                                                                .addStatement(
                                                                    "%T($THIS@notifiable$constructorFormat).$unsubscribeMethod(device.identifier)",
                                                                    NameHelper.nameFor(declaration, generationType),
                                                                )
                                                                .endControlFlow()
                                                                .unindent()
                                                                .addStatement("}")
                                                                .unindent()
                                                                .addStatement(")")
                                                        } else {
                                                            logger.error("Only one @${Notifiable::class.simpleName} / @${Indicatable::class.simpleName} property can be declared")
                                                        }
                                                    }
                                                } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                                    val descriptorNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(
                                                        typeDeclaration,
                                                        NeedsFormatterHelper.Target.SERVER_DSL,
                                                    )
                                                    addStatement(
                                                        "%T.$CONFIGURE($THIS, $delegateName.${propertyDeclaration.delegateName}${if (descriptorNeedsFormatter) ", $FORMAT" else ""})",
                                                        NameHelper.nameFor(typeDeclaration, generationType),
                                                    )
                                                } else {
                                                    logger.error(
                                                        "Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared",
                                                    )
                                                }
                                            }
                                        }
                                        .endControlFlow()
                                        .build(),
                                )
                            }
                            .build(),
                    )
                    .build(),
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(CHARACTERISTIC, characteristicClass())
                        .initializer(CHARACTERISTIC).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT)
                        .build().takeIf { needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }

    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val className = NameHelper.nameFor(this, generationType)
        val delegate = NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE)
        val remote = NameHelper.nameFor(this, generationType.copy(side = GenerationType.Side.CLIENT))
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        val notifiable = properties.firstOrNull { it.isNotifiable }
        val typeSpec = TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(delegateName, delegate),
                            ParameterSpec(COROUTINE_SCOPE, References.KotlinX.Coroutines.coroutineScope),
                            ParameterSpec(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT)),
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)))
            .addProperty(
                PropertySpec.builder(delegateName, delegate)
                    .initializer(delegateName)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(
                    REMOTES,
                    MUTABLE_MAP.parameterizedBy(
                        References.Bluetooth.Device.identifier,
                        if (notifiable != null) {
                            References.Kotlin.pair.parameterizedBy(
                                remote,
                                References.KotlinX.Coroutines.Flow.mutableSharedFlow.parameterizedBy(notifiable.type.resolve().toTypeName()),
                            )
                        } else {
                            remote
                        },
                    ),
                )
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("mutableMapOf()")
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT))
                    .addModifiers(KModifier.PRIVATE)
                    .initializer(IS_CLOSED)
                    .build(),
            )
            .apply {
                if (notifiable != null) {
                    addProperties(
                        listOf(
                            PropertySpec.builder(
                                COROUTINE_SCOPE,
                                References.KotlinX.Coroutines.coroutineScope,
                            ).initializer(COROUTINE_SCOPE).build(),
                            PropertySpec.builder(
                                "_${notifiable.simpleName.asString()}$SUBSCRIBERS",
                                References.KotlinX.Coroutines.Flow.mutableStateFlow.parameterizedBy(LIST.parameterizedBy(References.Bluetooth.Device.identifier)),
                            ).initializer("%T(emptyList())", References.KotlinX.Coroutines.Flow.mutableStateFlow)
                                .addModifiers(KModifier.PRIVATE)
                                .build(),
                        ),
                    )
                }
            }
            .addFunction(
                FunSpec.builder(GENERATE_REMOTE)
                    .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                    .returns(remote)
                    .addCode(
                        CodeBlock.builder()
                            .add("$RETURN $REMOTES.getOrPut($IDENTIFIER) {\n")
                            .indent()
                            .beginControlFlow("$WITH ($delegateName)")
                            .apply {
                                if (notifiable != null) {
                                    addStatement("val $MUTABLE_FLOW = %T<%T>()", References.KotlinX.Coroutines.Flow.mutableSharedFlow, notifiable.type.resolve().toTypeName())
                                    beginControlFlow("$COROUTINE_SCOPE.%M", References.KotlinX.Coroutines.launch)
                                    addStatement(
                                        "$MUTABLE_FLOW.subscriptionCount.%M { it >= 1 }.%M()",
                                        References.KotlinX.Coroutines.Flow.map,
                                        References.KotlinX.Coroutines.Flow.distinctUntilChanged,
                                    )
                                        .indent()
                                    addStatement(
                                        ".%M { _${notifiable.simpleName.asString()}$SUBSCRIBERS.%M { emptyList() } }",
                                        References.KotlinX.Coroutines.Flow.onCompletion,
                                        References.KotlinX.Coroutines.Flow.update,
                                    )
                                    beginControlFlow(".%M { hasSubscribed ->", References.KotlinX.Coroutines.Flow.collect)
                                    beginControlFlow("if (hasSubscribed)")
                                    addStatement("_${notifiable.simpleName.asString()}$SUBSCRIBERS.%M { it + $IDENTIFIER }", References.KotlinX.Coroutines.Flow.update)
                                    addStatement("${notifiable.subscribeMethodName}($IDENTIFIER)")
                                    nextControlFlow("else")
                                    addStatement("_${notifiable.simpleName.asString()}$SUBSCRIBERS.%M { it - $IDENTIFIER }", References.KotlinX.Coroutines.Flow.update)
                                    addStatement("${notifiable.unsubscribeMethodName}($IDENTIFIER)")
                                    endControlFlow()
                                    endControlFlow()
                                        .unindent()
                                    endControlFlow()
                                }
                                addStatement("%T(", remote)
                                indent()
                                properties.firstOrNull { it.isReadable }?.let { readProperty ->
                                    addStatement("${readProperty.onReadMethodName}$ACTION = {")
                                        .indent()
                                        .beginControlFlow("%M", References.KotlinX.Coroutines.coroutineScopeMethod)
                                        .beginControlFlow("%M", References.KotlinX.Coroutines.Selects.select)
                                        .addStatement("%M {", References.KotlinX.Coroutines.async)
                                        .indent()
                                        .beginControlFlow("$WITH($delegateName)")
                                        .addStatement("${readProperty.onReadMethodName}($IDENTIFIER${if (readProperty.isByteArray) ", 0" else ""})")
                                        .endControlFlow()
                                        .unindent()
                                        .addStatement("}.onAwait { it }")
                                        .apply {
                                            val resultType = BluetoothResultTypeBuilder(declaration, readProperty, logger)
                                            val result = if (resultType.hasCustomResult) {
                                                CodeBlock.of("%T.Failure(%T)", resultType.responseClassName, References.Bluetooth.deviceUnavailable)
                                            } else {
                                                CodeBlock.of("%T", References.Bluetooth.deviceUnavailable)
                                            }
                                            addStatement("$IS_CLOSED.onAwait { %L }", result)
                                        }
                                        .endControlFlow()
                                        .addStatement(".also { coroutineContext.%M() }", References.KotlinX.Coroutines.cancelChildren)
                                        .endControlFlow()
                                        .unindent()
                                        .addStatement("},")
                                }
                                properties.firstOrNull { it.isAnnotationPresent(Writable::class) }?.let { writeProperty ->
                                    addStatement("${writeProperty.onWriteMethodName}$ACTION = { ${writeProperty.simpleName.asString()} ->")
                                        .indent()
                                        .beginControlFlow("%M", References.KotlinX.Coroutines.coroutineScopeMethod)
                                        .beginControlFlow("%M", References.KotlinX.Coroutines.Selects.select)
                                        .addStatement("%M {", References.KotlinX.Coroutines.async)
                                        .indent()
                                        .beginControlFlow("$WITH($delegateName)")
                                        .addStatement(
                                            "${writeProperty.onWriteMethodName}(${writeProperty.simpleName.asString()}, $IDENTIFIER${if (writeProperty.isByteArray) ", 0" else ""})",
                                        )
                                        .endControlFlow()
                                        .unindent()
                                        .addStatement("}.onAwait { it }")
                                        .addStatement("$IS_CLOSED.onAwait { %T }", References.Bluetooth.deviceUnavailable)
                                        .endControlFlow()
                                        .addStatement(".also { coroutineContext.%M() }", References.KotlinX.Coroutines.cancelChildren)
                                        .endControlFlow()
                                        .unindent()
                                        .addStatement("},")
                                }
                                if (notifiable != null) {
                                    addStatement("${notifiable.simpleName.asString()} = $MUTABLE_FLOW.%M(),", References.KotlinX.Coroutines.Flow.asSharedFlow)
                                }

                                val descriptorCode =
                                    properties.mapNotNull { propertyDeclaration ->
                                        val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                        if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                            CodeBlock.of(
                                                "${propertyDeclaration.simpleName.asString()} = ${propertyDeclaration.simpleName.asString()}.$GENERATE_REMOTE($IDENTIFIER)",
                                            )
                                        } else {
                                            null
                                        }
                                    }.toList()
                                if (descriptorCode.isNotEmpty()) {
                                    add(
                                        descriptorCode.joinToCode(separator = ",\n", suffix = ",\n"),
                                    )
                                }
                                unindent()
                                addStatement(")${if (notifiable != null) " to $MUTABLE_FLOW" else ""}")
                            }
                            .endControlFlow()
                            .unindent()
                            .apply {
                                if (notifiable != null) {
                                    add("}.first\n")
                                } else {
                                    add("}\n")
                                }
                            }
                            .build(),
                    )
                    .build(),
            )
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }
    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType, imports: Generated.Imports): TypeSpec.Builder = apply {
        var hasReadMethod = false
        var hasWriteMethod = false
        var hasNotifyMethods = false
        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
            val typeDeclaration = propertyDeclaration.type.resolve().declaration
            if (propertyDeclaration.isReadable ||
                propertyDeclaration.isWritable ||
                propertyDeclaration.isNotifiable
            ) {
                if (propertyDeclaration.isReadable) {
                    if (!hasReadMethod) {
                        hasReadMethod = true
                    } else {
                        logger.error("Only one @${Readable::class.simpleName} property can be declared")
                    }
                }
                if (propertyDeclaration.isWritable) {
                    if (!hasWriteMethod) {
                        hasWriteMethod = true
                    } else {
                        logger.error(
                            "Only one @${Writable::class.simpleName} / @${WritableWithoutResponse::class.simpleName} / @${WritableSigned::class.simpleName} property can be declared",
                        )
                    }
                }

                if (propertyDeclaration.isNotifiable) {
                    if (!hasNotifyMethods) {
                        hasNotifyMethods = true

                        addProperty(
                            PropertySpec.builder(
                                "${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS",
                                References.KotlinX.Coroutines.Flow.flow.parameterizedBy(LIST.parameterizedBy(References.Bluetooth.Device.identifier)),
                            )
                                .addModifiers(*generationType.additionalModifiers.toTypedArray())
                                .apply {
                                    when (generationType.type) {
                                        GenerationType.Type.API -> {}

                                        GenerationType.Type.BLUETOOTH -> {
                                            initializer(
                                                CodeBlock.builder()
                                                    .beginControlFlow("$CHARACTERISTIC.subscribedDevices.%M { devices ->", References.KotlinX.Coroutines.Flow.map)
                                                    .addStatement("devices.map(%T::identifier)", References.Bluetooth.Server.connectedDevice)
                                                    .endControlFlow()
                                                    .build(),
                                            )
                                        }

                                        GenerationType.Type.SIMULATOR -> {
                                            initializer("_${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS.%M()", References.KotlinX.Coroutines.Flow.asStateFlow)
                                        }
                                    }
                                }
                                .build(),
                        )
                        val resolvedType = propertyDeclaration.type.resolve()
                        addFunctions(
                            listOf(
                                FunSpec.builder("$NOTIFY_ALL${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}$CHANGED")
                                    .addModifiers(KModifier.SUSPEND, *generationType.additionalModifiers.toTypedArray())
                                    .addParameter(propertyDeclaration.simpleName.asString(), resolvedType.toTypeName())
                                    .returns(BOOLEAN)
                                    .apply {
                                        when (generationType.type) {
                                            GenerationType.Type.API -> {}

                                            GenerationType.Type.BLUETOOTH -> {
                                                if (propertyDeclaration.isByteArray) {
                                                    addStatement("$RETURN $CHARACTERISTIC.notifyAll(${propertyDeclaration.simpleName.asString()})")
                                                } else {
                                                    addStatement(
                                                        "$RETURN $CHARACTERISTIC.notifyAll(${propertyDeclaration.simpleName.asString()}, %L, $FORMAT)",
                                                        resolvedType.toTypeName().serializer(logger),
                                                    )
                                                }
                                            }

                                            GenerationType.Type.SIMULATOR -> {
                                                addCode(
                                                    CodeBlock.builder()
                                                        .beginControlFlow("$RETURN _${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS.value.all")
                                                        .addStatement(
                                                            "$NOTIFY${propertyDeclaration.simpleName.asString().replaceFirstChar {
                                                                it.uppercase()
                                                            }}$CHANGED(it, ${propertyDeclaration.simpleName.asString()})",
                                                        )
                                                        .endControlFlow()
                                                        .build(),
                                                )
                                            }
                                        }
                                    }
                                    .build(),
                                FunSpec.builder("$NOTIFY${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}$CHANGED")
                                    .addModifiers(KModifier.SUSPEND, *generationType.additionalModifiers.toTypedArray())
                                    .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                    .addParameter(propertyDeclaration.simpleName.asString(), resolvedType.toTypeName())
                                    .returns(BOOLEAN)
                                    .apply {
                                        when (generationType.type) {
                                            GenerationType.Type.API -> {}

                                            GenerationType.Type.BLUETOOTH -> {
                                                val notifyCode = if (propertyDeclaration.isByteArray) {
                                                    CodeBlock.of("$CHARACTERISTIC.notify(it, ${propertyDeclaration.simpleName.asString()})")
                                                } else {
                                                    CodeBlock.of(
                                                        "$CHARACTERISTIC.notify(it, ${propertyDeclaration.simpleName.asString()}, %L, $FORMAT)",
                                                        resolvedType.toTypeName().serializer(logger),
                                                    )
                                                }
                                                addCode(
                                                    CodeBlock.builder()
                                                        .add("$RETURN $CHARACTERISTIC.subscribedDevices.value.find { it.identifier == $IDENTIFIER }?.let {\n")
                                                        .indent()
                                                        .add(notifyCode)
                                                        .unindent()
                                                        .add("} ?: false\n")
                                                        .build(),
                                                )
                                            }

                                            GenerationType.Type.SIMULATOR -> {
                                                addCode(
                                                    CodeBlock.builder()
                                                        .add("$RETURN $REMOTES[$IDENTIFIER]?.let { (_, $MUTABLE_FLOW) ->\n")
                                                        .indent()
                                                        .beginControlFlow("if (_${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS.value.contains($IDENTIFIER))")
                                                        .addStatement("$MUTABLE_FLOW.emit(${propertyDeclaration.simpleName.asString()})")
                                                        .addStatement("true")
                                                        .nextControlFlow("else")
                                                        .addStatement("false")
                                                        .endControlFlow()
                                                        .unindent()
                                                        .addStatement("} ?: false")
                                                        .build(),
                                                )
                                            }
                                        }
                                    }
                                    .build(),
                            ),
                        )
                    } else {
                        logger.error("Only one @${Notifiable::class.simpleName} / @${Indicatable::class.simpleName} property can be declared")
                    }
                }
            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                addProperty(
                    PropertySpec.builder(propertyDeclaration.simpleName.asString(), NameHelper.nameFor(typeDeclaration, generationType))
                        .addModifiers(*generationType.additionalModifiers.toTypedArray())
                        .apply {
                            val descriptor = typeDeclaration.getAnnotationsByType(BluetoothDescriptor::class).first()
                            when (generationType.type) {
                                GenerationType.Type.API -> {}

                                GenerationType.Type.BLUETOOTH -> {
                                    delegate(
                                        "lazy { %L }",
                                        CodeBlock.of(
                                            "%T($CHARACTERISTIC.descriptors.%M(%T.$UUID))",
                                            NameHelper.nameFor(typeDeclaration, generationType),
                                            References.Bluetooth.get,
                                            NameHelper.nameFor(typeDeclaration, generationType.copy(type = GenerationType.Type.API)),
                                        ),
                                    )
                                }

                                GenerationType.Type.SIMULATOR -> {
                                    initializer(
                                        CodeBlock.of(
                                            "%T(${declaration.delegateName}.${propertyDeclaration.delegateName}, $IS_CLOSED)",
                                            NameHelper.nameFor(typeDeclaration, generationType),
                                        ),
                                    )
                                }
                            }
                        }
                        .build(),
                )
            } else {
                logger.error(
                    "Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared",
                )
            }
        }
    }

    fun KSClassDeclaration.isNotifiable() = declarations.filterIsInstance<KSPropertyDeclaration>().any { it.isAnnotationPresent(Notifiable::class) }
    fun KSClassDeclaration.characteristicClass(): ClassName = if (isNotifiable()) {
        References.Bluetooth.Server.localCharacteristicNotifiable
    } else {
        References.Bluetooth.Server.localCharacteristic
    }
}
