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
import com.splendo.kaluga.bluetooth.annotations.Encrypted
import com.splendo.kaluga.bluetooth.annotations.Indicatable
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.annotations.WritableSigned
import com.splendo.kaluga.bluetooth.annotations.WritableWithoutResponse
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILD
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.DSL
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_FAILED_TO_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_READ
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_SUBSCRIBE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_UNSUBSCRIBE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.toClassName

internal class BluetoothLocalCharacteristicBuilder(
    declaration: KSClassDeclaration,
    private val characteristic: BluetoothCharacteristic,
    logger: KSPLogger
) : AbstractBluetoothClassBuilder(declaration, logger) {

    override fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated {
        val imports = Generated.Imports()
        val typeSpec = TypeSpec.interfaceBuilder(NameHelper.nameFor(this, generationType)).addModifiers(KModifier.SEALED)
            .addTypes(nested)
            .addType(
                TypeSpec.interfaceBuilder(DSL)
                    .generateDSLBody(this@generateAPI, declarations, generationType, imports, CodeBlock.builder())
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
                TypeSpec.classBuilder(DSL)
                    .addSuperinterface(NameHelper.nameFor(this, generationType.copy(type = GenerationType.Type.API)).nestedClass(DSL))
                    .apply {
                        val buildBody = CodeBlock.builder()

                        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(this@generateBluetooth, NeedsFormatterHelper.Target.SERVER_DSL)
                        generateDSLBody(this@generateBluetooth, declarations, generationType, imports, buildBody)
                            .addFunction(
                                FunSpec.builder(BUILD)
                                    .addParameters(
                                        listOfNotNull(
                                            ParameterSpec(BUILDER, References.Bluetooth.Server.localServiceDSL),
                                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter },
                                        )
                                    )
                                    .addCode(
                                        CodeBlock.builder()
                                            .beginControlFlow("$BUILDER.characteristic(%M(%S)) {", References.Bluetooth.uuidFrom, characteristic.uuid)
                                            .add(buildBody.build())
                                            .endControlFlow()
                                            .build()
                                    )
                                    .build()
                            )
                    }
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

    private fun TypeSpec.Builder.generateDSLBody(
        declaration: KSClassDeclaration,
        declarations: Sequence<KSDeclaration>, generationType: GenerationType,
        imports: Generated.Imports,
        builderBody: CodeBlock.Builder
    ): TypeSpec.Builder = apply {
        var hasReadMethod = false
        var hasWriteMethod = false
        var hasNotifyMethods = false
        val constructorFormat = if (NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER)) ", $FORMAT" else ""
        val castingMethod = if (declaration.isNotifiable()) CodeBlock.of("%T(this as %T$constructorFormat)", NameHelper.nameFor(declaration, generationType), References.Bluetooth.Server.localCharacteristicNotifiable) else CodeBlock.of("%T(this$constructorFormat)", NameHelper.nameFor(declaration, generationType))
        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
            val typeDeclaration = propertyDeclaration.type.resolve().declaration
            if (propertyDeclaration.isAnnotationPresent(Readable::class) ||
                propertyDeclaration.isAnnotationPresent(Writable::class) ||
                propertyDeclaration.isAnnotationPresent(WritableWithoutResponse::class) ||
                propertyDeclaration.isAnnotationPresent(WritableSigned::class) ||
                propertyDeclaration.isAnnotationPresent(Notifiable::class) ||
                propertyDeclaration.isAnnotationPresent(Indicatable::class)
            ) {
                if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                    if (!hasReadMethod) {
                        hasReadMethod = true
                        val readMethod = "$ON_READ${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration)

                        val lambdaType = LambdaTypeName.get(
                            receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                            parameters = listOfNotNull(
                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                                ParameterSpec(OFFSET, INT).takeIf { !resultType.hasCustomResult }
                            ),
                            returnType = resultType.responseClassName,
                        ).copy(suspending = true)

                        val onReadFunSpec = FunSpec.builder(readMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                            .addParameter(ACTION, lambdaType)

                        when (generationType.type) {
                            GenerationType.Type.API -> {}

                            GenerationType.Type.BLUETOOTH -> {
                                val readAction = "${readMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                addProperty(
                                    PropertySpec.builder(readAction, lambdaType)
                                        .addModifiers(KModifier.PRIVATE)
                                        .mutable(true)
                                        .initializer(
                                            CodeBlock.builder()
                                                .beginControlFlow("{ _ ->")
                                                .apply {
                                                    resultType.generateDefaultResult(this)
                                                }
                                                .endControlFlow()
                                                .build()
                                        )
                                        .build()
                                )

                                onReadFunSpec.addStatement(
                                    "$readAction = $ACTION"
                                )

                                builderBody
                                    .beginControlFlow("readable(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, $OFFSET ->")
                                    .add(resultType.parseBluetoothResult(CodeBlock.of("%L.${readAction}(device.identifier)", castingMethod)))
                                    .endControlFlow()
                            }

                            GenerationType.Type.SIMULATOR -> {

                            }
                        }

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

                        val properties = listOfNotNull(
                            References.Bluetooth.writeProperty.takeIf { propertyDeclaration.isAnnotationPresent(Writable::class) },
                                   References.Bluetooth.writeWithoutResponseProperty.takeIf { propertyDeclaration.isAnnotationPresent(WritableWithoutResponse::class) },
                            References.Bluetooth.signedWriteProperty.takeIf { propertyDeclaration.isAnnotationPresent(WritableSigned::class) },
                        )
                        val propertiesCode = properties.joinToCode(prefix = "setOf(", suffix = ")") { CodeBlock.of("%T", it)  }

                        if (propertyDeclaration.isByteArray) {
                            val writeMethod = "$ON_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                            val lambdaType = LambdaTypeName.get(
                                receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                                parameters = listOf(
                                    ParameterSpec(propertyDeclaration.simpleName.asString(), BYTE_ARRAY),
                                    ParameterSpec(IDENTIFIER, INT),
                                    ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                                ),
                                returnType = References.Bluetooth.writeResponse,
                            ).copy(suspending = true)

                            val onWriteFunSpec = FunSpec.builder(writeMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                                .addParameter(ACTION, lambdaType)

                            when (generationType.type) {
                                GenerationType.Type.API -> {}

                                GenerationType.Type.BLUETOOTH -> {
                                    val writeAction = "${writeMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                    addProperty(
                                        PropertySpec.builder(writeAction, lambdaType)
                                            .addModifiers(KModifier.PRIVATE)
                                            .mutable(true)
                                            .initializer(
                                                CodeBlock.builder()
                                                    .beginControlFlow("{ _, _ ->")
                                                    .addStatement("%T", References.Bluetooth.writeRequestRejected)
                                                    .endControlFlow()
                                                    .build()
                                            )
                                            .build()
                                    )

                                    onWriteFunSpec.addStatement(
                                        "$writeAction = $ACTION"
                                    )

                                    builderBody
                                        .beginControlFlow("writable(%L, ${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, value, $OFFSET ->", propertiesCode)
                                        .addStatement("%L.${writeAction}(value, $OFFSET, device.identifier)", castingMethod)
                                        .endControlFlow()
                                }

                                GenerationType.Type.SIMULATOR -> {

                                }
                            }

                            addFunction(
                                onWriteFunSpec
                                    .build(),
                            )
                        } else {
                            val writeMethod = "$ON_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                            val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                            val writeLambdaType = LambdaTypeName.get(
                                receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                                parameters = listOf(
                                    ParameterSpec(propertyDeclaration.simpleName.asString(), propertyDeclaration.type.resolve().toClassName()),
                                    ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                                ),
                                returnType = References.Bluetooth.writeResponse,
                            ).copy(suspending = true)

                            val failedToWriteLambdaType = LambdaTypeName.get(
                                receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                                parameters = listOf(
                                    ParameterSpec(propertyDeclaration.simpleName.asString(), References.Kotlin.exception),
                                    ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                                ),
                                returnType = References.Bluetooth.writeResponse,
                            ).copy(suspending = true)

                            val onWriteFunSpec = FunSpec.builder(writeMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                                .addParameter(ACTION, writeLambdaType)
                            val onFailedToWriteFunSpec = FunSpec.builder(failedToWriteMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                                .addParameter(ACTION, failedToWriteLambdaType)

                            when (generationType.type) {
                                GenerationType.Type.API -> {}

                                GenerationType.Type.BLUETOOTH -> {
                                    val writeAction = "${writeMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                    val failedToWriteAction = "${failedToWriteMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                    addProperty(
                                        PropertySpec.builder(writeAction, writeLambdaType)
                                            .addModifiers(KModifier.PRIVATE)
                                            .mutable(true)
                                            .initializer(
                                                CodeBlock.builder()
                                                    .beginControlFlow("{ _, _ ->")
                                                    .addStatement("%T", References.Bluetooth.writeRequestRejected)
                                                    .endControlFlow()
                                                    .build()
                                            )
                                            .build()
                                    )
                                    addProperty(
                                        PropertySpec.builder(failedToWriteAction, failedToWriteLambdaType)
                                            .addModifiers(KModifier.PRIVATE)
                                            .mutable(true)
                                            .initializer(
                                                CodeBlock.builder()
                                                    .beginControlFlow("{ _, _ ->")
                                                    .addStatement("%T", References.Bluetooth.writeRequestRejected)
                                                    .endControlFlow()
                                                    .build()
                                            )
                                            .build()
                                    )

                                    onWriteFunSpec.addStatement(
                                        "$writeAction = $ACTION"
                                    )
                                    onFailedToWriteFunSpec.addStatement(
                                        "$failedToWriteAction = $ACTION"
                                    )

                                    builderBody
                                        .addStatement("writable(")
                                        .indent()
                                        .addStatement("properties = %L,", propertiesCode)
                                        .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                                        .addStatement("deserializationStrategy = %T.%M(),", propertyDeclaration.type.resolve().toClassName(), References.KotlinX.Serialization.serializer)
                                        .addStatement("bluetoothFormat = $FORMAT,")
                                        .addStatement("onFailedToWrite = { device, exception ->")
                                        .indent()
                                        .addStatement("%L.$failedToWriteAction(exception, device.identifier)", castingMethod)
                                        .unindent()
                                        .addStatement("},")
                                        .addStatement("onWrite = { device, value ->")
                                        .indent()
                                        .addStatement("%L.$writeAction(value, device.identifier)",castingMethod)
                                        .unindent()
                                        .addStatement("}")
                                        .unindent()
                                        .addStatement(")")
                                }

                                GenerationType.Type.SIMULATOR -> {

                                }
                            }

                            addFunction(
                                onWriteFunSpec
                                    .build(),
                            )
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

                        val properties = listOfNotNull(
                            References.Bluetooth.notifyProperty.takeIf { propertyDeclaration.isAnnotationPresent(Notifiable::class) },
                            References.Bluetooth.indicatableProperty.takeIf { propertyDeclaration.isAnnotationPresent(Indicatable::class) },
                        )
                        val propertiesCode = properties.joinToCode(prefix = "setOf(", suffix = ")") { CodeBlock.of("%T", it)  }

                        val subscribeMethod = "$ON_SUBSCRIBE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"
                        val unsubscribeMethod = "$ON_UNSUBSCRIBE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

                        val subscriptionChangeLambdaType = LambdaTypeName.get(
                            receiver = NameHelper.nameFor(declaration, generationType.copy(type = GenerationType.Type.API)),
                            parameters = listOf(
                                ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                            ),
                            returnType = UNIT,
                        )

                        val onSubscribeFunSpec = FunSpec.builder(subscribeMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                            .addParameter(ACTION, subscriptionChangeLambdaType)
                        val onUnsubscribeFunSpec = FunSpec.builder(unsubscribeMethod).addModifiers(*generationType.additionalModifiers.toTypedArray())
                            .addParameter(ACTION, subscriptionChangeLambdaType)

                        when (generationType.type) {
                            GenerationType.Type.API -> {}

                            GenerationType.Type.BLUETOOTH -> {
                                val subscribeAction = "${subscribeMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                val unsubscribeAction = "${unsubscribeMethod}${ACTION.replaceFirstChar { it.uppercase() }}"
                                addProperty(
                                    PropertySpec.builder(subscribeAction, subscriptionChangeLambdaType)
                                        .addModifiers(KModifier.PRIVATE)
                                        .mutable(true)
                                        .initializer("{ }")
                                        .build()
                                )
                                addProperty(
                                    PropertySpec.builder(unsubscribeAction, subscriptionChangeLambdaType)
                                        .addModifiers(KModifier.PRIVATE)
                                        .mutable(true)
                                        .initializer("{ }")
                                        .build()
                                )

                                onSubscribeFunSpec.addStatement(
                                    "$subscribeAction = $ACTION"
                                )
                                onUnsubscribeFunSpec.addStatement(
                                    "$unsubscribeAction = $ACTION"
                                )

                                builderBody
                                    .addStatement("notifiable(")
                                    .indent()
                                    .addStatement("properties = %L,", propertiesCode)
                                    .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                                    .addStatement("onSubscribe = { device ->")
                                    .indent()
                                    .addStatement("%T(this$constructorFormat).$subscribeAction(device.identifier)", NameHelper.nameFor(declaration, generationType))
                                    .unindent()
                                    .addStatement("},")
                                    .addStatement("onUnsubscribe = { device ->")
                                    .indent()
                                    .addStatement("%T(this$constructorFormat).$unsubscribeAction(device.identifier)", NameHelper.nameFor(declaration, generationType))
                                    .unindent()
                                    .addStatement("}")
                                    .unindent()
                                    .addStatement(")")
                            }

                            GenerationType.Type.SIMULATOR -> {

                            }
                        }

                        addFunction(
                            onSubscribeFunSpec
                                .build(),
                        )
                        addFunction(
                            onUnsubscribeFunSpec
                                .build(),
                        )
                    } else {
                        logger.error("Only one @${Notifiable::class.simpleName} / @${Indicatable::class.simpleName} property can be declared")
                    }

                }
            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                val lambdaType = LambdaTypeName.get(
                    receiver = NameHelper.nameFor(typeDeclaration, generationType.copy(type = GenerationType.Type.API)).nestedClass(DSL),
                    returnType = UNIT,
                )
                addFunction(
                    FunSpec.builder(propertyDeclaration.simpleName.asString()).addModifiers(*generationType.additionalModifiers.toTypedArray())
                        .addParameter(ACTION, lambdaType)
                        .build(),
                )
            } else {
                logger.error("Only @${Readable::class.simpleName}, @${Writable::class.simpleName}, @${WritableWithoutResponse::class.simpleName}, @${WritableSigned::class.simpleName}, @${Notifiable::class.simpleName}, @${Indicatable::class.simpleName} and @${BluetoothDescriptor::class.simpleName} properties can be declared")
            }
        }
    }

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, generationType: GenerationType, imports: Generated.Imports): TypeSpec.Builder = apply {

    }

    fun KSClassDeclaration.isNotifiable() = declarations.filterIsInstance<KSPropertyDeclaration>().any { it.isAnnotationPresent(Notifiable::class) }
    fun KSClassDeclaration.characteristicClass(): ClassName = if (isNotifiable()) {
        References.Bluetooth.Server.localCharacteristicNotifiable
    } else {
        References.Bluetooth.Server.localCharacteristic
    }
}
