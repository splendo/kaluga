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
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CHANGED
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.EXCEPTION
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFY
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFY_ALL
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_FAILED_TO_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_READ
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_SUBSCRIBE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_UNSUBSCRIBE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SUBSCRIBERS
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.splendo.kaluga.bluetooth.ksp.helpers.isNotifiable
import com.splendo.kaluga.bluetooth.ksp.helpers.isReadable
import com.splendo.kaluga.bluetooth.ksp.helpers.isWritable
import com.splendo.kaluga.bluetooth.ksp.helpers.onReadMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.onWriteMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.serializer
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal class BluetoothLocalCharacteristicBuilder(
    declaration: KSClassDeclaration,
    private val characteristic: BluetoothCharacteristic,
    logger: KSPLogger
) : AbstractBluetoothClassBuilder(declaration, logger) {

    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType))
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
                                                    ParameterSpec(OFFSET, INT).takeIf { !resultType.hasCustomResult }
                                                )
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
                                if (propertyDeclaration.isAnnotationPresent(Writable::class) || propertyDeclaration.isAnnotationPresent(WritableWithoutResponse::class) || propertyDeclaration.isAnnotationPresent(
                                        WritableSigned::class)) {
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
                                        logger.error("Only one @${Writable::class.simpleName} / @${WritableWithoutResponse::class.simpleName} / @${WritableSigned::class.simpleName} property can be declared")
                                    }
                                }

                                if (propertyDeclaration.isAnnotationPresent(Notifiable::class) || propertyDeclaration.isAnnotationPresent(Indicatable::class)) {
                                    if (!hasNotifyMethods) {
                                        hasNotifyMethods = true

                                        val subscribeMethod = "$ON_SUBSCRIBE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                        val unsubscribeMethod = "$ON_UNSUBSCRIBE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                                        addFunctions(
                                            listOf(
                                                FunSpec.builder(subscribeMethod).addModifiers(KModifier.ABSTRACT)
                                                    .receiver(receiver)
                                                    .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                                    .build(),
                                                FunSpec.builder(unsubscribeMethod).addModifiers(KModifier.ABSTRACT)
                                                    .receiver(receiver)
                                                    .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                                                    .build()
                                            )
                                        )
                                    } else {
                                        logger.error("Only one @${Notifiable::class.simpleName} / @${Indicatable::class.simpleName} property can be declared")
                                    }

                                }
                            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                addProperty("${propertyDeclaration.simpleName.asString()}$DELEGATE",NameHelper.nameFor(typeDeclaration, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE))
                            } else {
                                logger.error("Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared")
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
                        )

                    )
                    .build()
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
                                    NameHelper.nameFor(this@generateBluetooth, generationType.copy(type = GenerationType.Type.API)).nestedClass(DELEGATE)
                                )
                                val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER_DSL)
                                if (needsFormatter) {
                                    addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                                }
                                addCode(
                                    CodeBlock.builder()
                                        .beginControlFlow("$RETURN $BUILDER.characteristic(%M(%S)) {", References.Bluetooth.uuidFrom, characteristic.uuid)
                                        .apply {
                                            var hasReadMethod = false
                                            var hasWriteMethod = false
                                            var hasNotifyMethods = false
                                            val constructorFormat = if (NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER)) ", $FORMAT" else ""
                                            fun castingMethod(scope: String): CodeBlock {
                                                val scopeMethod = if (scope.isNotEmpty()) "$THIS@$scope" else THIS
                                                return if (declaration.isNotifiable()) CodeBlock.of("%T($scopeMethod as %T$constructorFormat)", NameHelper.nameFor(declaration, generationType), References.Bluetooth.Server.localCharacteristicNotifiable) else CodeBlock.of("%T($scopeMethod$constructorFormat)", NameHelper.nameFor(declaration, generationType))
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
                                                                .add(resultType.parseBluetoothResult(CodeBlock.of("%L.${readMethod}(device.identifier", castingMethod("readable"))))
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
                                                                References.Bluetooth.writeWithoutResponseProperty.takeIf { propertyDeclaration.isAnnotationPresent(WritableWithoutResponse::class) },
                                                                References.Bluetooth.signedWriteProperty.takeIf { propertyDeclaration.isAnnotationPresent(WritableSigned::class) },
                                                            )
                                                            val propertiesCode = properties.joinToCode(prefix = "setOf(", suffix = ")") { CodeBlock.of("%T", it)  }

                                                            if (propertyDeclaration.isByteArray) {
                                                                beginControlFlow("writable(%L, ${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, value, $OFFSET ->", propertiesCode)
                                                                    .beginControlFlow("$WITH($delegateName)")
                                                                    .addStatement("%L.${writeMethod}(value, $OFFSET, device.identifier)", castingMethod("writable"))
                                                                    .endControlFlow()
                                                                    .endControlFlow()
                                                            } else {
                                                                val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                                                addStatement("writable(")
                                                                    .indent()
                                                                    .addStatement("properties = %L,", propertiesCode)
                                                                    .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                                                                    .addStatement("deserializationStrategy = %L,", propertyDeclaration.type.resolve().toTypeName().serializer(logger))
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
                                                            logger.error("Only one @${Writable::class.simpleName} / @${WritableWithoutResponse::class.simpleName} / @${WritableSigned::class.simpleName} property can be declared")
                                                        }
                                                    }

                                                    if (propertyDeclaration.isNotifiable) {
                                                        if (!hasNotifyMethods) {
                                                            hasNotifyMethods = true
                                                            val properties = listOfNotNull(
                                                                References.Bluetooth.notifyProperty.takeIf { propertyDeclaration.isAnnotationPresent(Notifiable::class) },
                                                                References.Bluetooth.indicatableProperty.takeIf { propertyDeclaration.isAnnotationPresent(Indicatable::class) },
                                                            )
                                                            val propertiesCode = properties.joinToCode(prefix = "setOf(", suffix = ")") { CodeBlock.of("%T", it)  }

                                                            val subscribeMethod = "$ON_SUBSCRIBE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                                            val unsubscribeMethod = "$ON_UNSUBSCRIBE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                                                            addStatement("notifiable(")
                                                                .indent()
                                                                .addStatement("properties = %L,", propertiesCode)
                                                                .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                                                                .addStatement("onSubscribe = { device ->")
                                                                .indent()
                                                                .beginControlFlow("$WITH($delegateName)")
                                                                .addStatement("%T($THIS@notifiable$constructorFormat).$subscribeMethod(device.identifier)", NameHelper.nameFor(declaration, generationType))
                                                                .endControlFlow()
                                                                .unindent()
                                                                .addStatement("},")
                                                                .addStatement("onUnsubscribe = { device ->")
                                                                .indent()
                                                                .beginControlFlow("$WITH($delegateName)")
                                                                .addStatement("%T($THIS@notifiable$constructorFormat).$unsubscribeMethod(device.identifier)", NameHelper.nameFor(declaration, generationType))
                                                                .endControlFlow()
                                                                .unindent()
                                                                .addStatement("}")
                                                                .unindent()
                                                                .addStatement(")")

                                                        }else {
                                                            logger.error("Only one @${Notifiable::class.simpleName} / @${Indicatable::class.simpleName} property can be declared")
                                                        }

                                                    }
                                                } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                                    val descriptorNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, NeedsFormatterHelper.Target.SERVER_DSL)
                                                    addStatement("%T.$CONFIGURE($THIS, $delegateName.${propertyDeclaration.simpleName.asString()}$DELEGATE${if (descriptorNeedsFormatter) ", $FORMAT" else ""})", NameHelper.nameFor(typeDeclaration, generationType))
                                                } else {
                                                    logger.error("Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared")
                                                }
                                            }
                                        }
                                        .endControlFlow()
                                        .build()
                                )
                            }
                            .build()
                    )
                    .build()
            )
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(CHARACTERISTIC, characteristicClass())
                        .initializer(CHARACTERISTIC).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT)
                        .build().takeIf { needsFormatter },
                )
            )
            .addTypes(nested)
            .generateBody(declarations, generationType, imports)
        return Generated(listOf(typeSpec.build()), imports)
    }

    override fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): Generated = Generated(emptyList())

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
                        logger.error("Only one @${Writable::class.simpleName} / @${WritableWithoutResponse::class.simpleName} / @${WritableSigned::class.simpleName} property can be declared")
                    }
                }

                if (propertyDeclaration.isNotifiable) {
                    if (!hasNotifyMethods) {
                        hasNotifyMethods = true

                        addProperty(
                            PropertySpec.builder("${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS", References.KotlinX.Coroutines.Flow.flow.parameterizedBy(LIST.parameterizedBy(References.Bluetooth.Device.identifier)))
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
                                                    .build()
                                            )
                                        }
                                        GenerationType.Type.SIMULATOR -> {}
                                    }
                                }
                                .build()
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
                                                    addStatement("$RETURN $CHARACTERISTIC.notifyAll(${propertyDeclaration.simpleName.asString()}, %L, $FORMAT)", resolvedType.toTypeName().serializer(logger))
                                                }
                                            }
                                            GenerationType.Type.SIMULATOR -> {}
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
                                                    CodeBlock.of("$CHARACTERISTIC.notify(it, ${propertyDeclaration.simpleName.asString()}, %L, $FORMAT)", resolvedType.toTypeName().serializer(logger))
                                                }
                                                addCode(
                                                    CodeBlock.builder()
                                                        .add("$RETURN $CHARACTERISTIC.subscribedDevices.value.find { it.identifier == $IDENTIFIER }?.let {\n")
                                                        .indent()
                                                        .add(notifyCode)
                                                        .unindent()
                                                        .add("} ?: false\n")
                                                        .build()
                                                )
                                            }
                                            GenerationType.Type.SIMULATOR -> {}
                                        }
                                    }
                                    .build(),
                            )
                        )

                    } else {
                        logger.error("Only one @${Notifiable::class.simpleName} / @${Indicatable::class.simpleName} property can be declared")
                    }

                }
            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                addProperty(
                    PropertySpec.builder(propertyDeclaration.simpleName.asString(),NameHelper.nameFor(typeDeclaration, generationType))
                        .addModifiers(*generationType.additionalModifiers.toTypedArray())
                        .apply {
                            val descriptor = typeDeclaration.getAnnotationsByType(BluetoothDescriptor::class).first()
                            when (generationType.type) {
                             GenerationType.Type.API -> {}
                             GenerationType.Type.BLUETOOTH -> {
                                 delegate(
                                     "lazy { %L }",
                                     CodeBlock.of("%T($CHARACTERISTIC.descriptors.%M(%M(%S)))",
                                     NameHelper.nameFor(typeDeclaration, generationType),
                                     References.Bluetooth.get,
                                     References.Bluetooth.uuidFrom,
                                     descriptor.uuid
                                     )
                                 )
                             }
                                GenerationType.Type.SIMULATOR -> {}
                            }
                        }
                        .build()
                )
            } else {
                logger.error("Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared")
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
