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
import com.splendo.kaluga.bluetooth.annotations.Encrypted
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.DESCRIPTOR
import com.splendo.kaluga.bluetooth.ksp.helpers.EXCEPTION
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.GENERATE_REMOTE
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.IS_CLOSED
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_FAILED_TO_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.REMOTES
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.delegateParameterName
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.splendo.kaluga.bluetooth.ksp.helpers.isReadable
import com.splendo.kaluga.bluetooth.ksp.helpers.onReadMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.onWriteMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.serializer
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MUTABLE_MAP
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.ksp.toTypeName

internal class BluetoothLocalDescriptorBuilder(declaration: KSClassDeclaration, private val descriptor: BluetoothDescriptor, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, logger) {
    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
        return TypeSpec.interfaceBuilder(interfaceName)
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder(UUID, References.Bluetooth.uuid)
                            .initializer("%M(%S)", References.Bluetooth.uuidFrom, descriptor.uuid)
                            .build(),
                    )
                    .build(),
            )
            .addTypes(nested)
            .addType(
                generateDelegate(interfaceName),
            ).build()
    }

    private fun generateDelegate(interfaceName: ClassName): TypeSpec = TypeSpec.interfaceBuilder(DELEGATE)
        .apply {
            var hasReadMethod = false
            var hasWriteMethod = false
            declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                    if (!hasReadMethod) {
                        hasReadMethod = true

                        addFunction(generateDelegateReadMethods(propertyDeclaration, interfaceName))
                    } else {
                        logger.error("Only one @${Readable::class.simpleName} property can be declared")
                    }
                }

                if (propertyDeclaration.isAnnotationPresent(Writable::class)) {
                    if (!hasWriteMethod) {
                        hasWriteMethod = true

                        addFunctions(generateDelegateWriteMethods(propertyDeclaration, interfaceName))
                    } else {
                        logger.error("Only one @${Writable::class.simpleName} property can be declared")
                    }
                }

                if (!propertyDeclaration.isAnnotationPresent(Readable::class) && !propertyDeclaration.isAnnotationPresent(Writable::class)) {
                    logger.error("Only @${Readable::class.simpleName} and @${Writable::class.simpleName} properties can be declared")
                }
            }
        }
        .build()

    private fun generateDelegateReadMethods(propertyDeclaration: KSPropertyDeclaration, interfaceName: ClassName): FunSpec {
        val readMethod = propertyDeclaration.onReadMethodName
        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, logger)

        return FunSpec.builder(readMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
            .receiver(interfaceName)
            .addParameters(
                listOfNotNull(
                    ParameterSpec(IDENTIFIER, References.Bluetooth.Device.identifier),
                    ParameterSpec(OFFSET, INT).takeIf { !resultType.hasCustomResult },
                ),
            )
            .returns(resultType.responseClassName)
            .build()
    }

    private fun generateDelegateWriteMethods(propertyDeclaration: KSPropertyDeclaration, interfaceName: ClassName): List<FunSpec> {
        val writeMethod = propertyDeclaration.onWriteMethodName

        val onWriteFun = FunSpec.builder(writeMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
            .receiver(interfaceName)
            .addParameter(propertyDeclaration.simpleName.asString(), propertyDeclaration.type.resolve().toTypeName())
            .apply {
                if (propertyDeclaration.isByteArray) {
                    addParameter(OFFSET, INT)
                }
            }
            .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
            .returns(References.Bluetooth.writeResponse)
            .build()

        return if (!propertyDeclaration.isByteArray) {
            val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}"

            val onFailedToWriteFun = FunSpec.builder(failedToWriteMethod).addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                .receiver(interfaceName)
                .addParameter(EXCEPTION, References.Kotlin.exception)
                .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                .returns(References.Bluetooth.writeResponse)
                .build()

            listOf(onWriteFun, onFailedToWriteFun)
        } else {
            listOf(onWriteFun)
        }
    }

    override fun generateBluetooth(nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(declaration, GenerationType.SERVER_BLUETOOTH)
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
        return TypeSpec.classBuilder(className).addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(DESCRIPTOR, References.Bluetooth.Server.localDescriptor),
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addType(generateBluetoothCompanionObject(interfaceName, className))
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(DESCRIPTOR, References.Bluetooth.Server.localDescriptor)
                        .initializer(DESCRIPTOR).build(),
                ),
            )
            .addTypes(nested)
            .build()
    }

    private fun generateBluetoothCompanionObject(interfaceName: ClassName, className: ClassName): TypeSpec = TypeSpec.companionObjectBuilder()
        .addFunction(
            FunSpec.builder(CONFIGURE)
                .apply {
                    val delegateName = "${declaration.simpleName.asString().replaceFirstChar { it.lowercase() }}$DELEGATE"
                    addParameter(BUILDER, References.Bluetooth.Server.localCharacteristicDSL)
                    addParameter(
                        delegateName,
                        interfaceName.nestedClass(DELEGATE),
                    )
                    val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER_DSL)
                    if (needsFormatter.needsFormatter) {
                        addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                    }
                    addCode(
                        CodeBlock.builder()
                            .beginControlFlow("$RETURN $BUILDER.descriptor(%T.$UUID) {", interfaceName)
                            .apply {
                                var hasReadMethod = false
                                var hasWriteMethod = false
                                declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                                    if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                                        if (!hasReadMethod) {
                                            hasReadMethod = true
                                            add(generateSetupReadableMethod(propertyDeclaration, delegateName, className))
                                        } else {
                                            logger.error("Only one @${Readable::class.simpleName} property can be declared")
                                        }
                                    }
                                    if (propertyDeclaration.isAnnotationPresent(Writable::class)) {
                                        if (!hasWriteMethod) {
                                            hasWriteMethod = true

                                            add(
                                                if (propertyDeclaration.isByteArray) {
                                                    generateSetupWritableByteArrayMethod(propertyDeclaration, delegateName, className)
                                                } else {
                                                    generateSetupWritableMethod(propertyDeclaration, delegateName, className)
                                                },
                                            )
                                        } else {
                                            logger.error("Only one @${Writable::class.simpleName} property can be declared")
                                        }
                                    }
                                    if (!propertyDeclaration.isAnnotationPresent(Readable::class) && !propertyDeclaration.isAnnotationPresent(Writable::class)) {
                                        logger.error("Only @${Readable::class.simpleName} and @${Writable::class.simpleName} properties can be declared")
                                    }
                                }
                            }
                            .endControlFlow()
                            .build(),
                    )
                }
                .build(),
        )
        .build()

    private fun generateSetupReadableMethod(propertyDeclaration: KSPropertyDeclaration, delegateName: String, className: ClassName): CodeBlock {
        val readMethod = propertyDeclaration.onReadMethodName
        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, logger)
        return CodeBlock.builder()
            .beginControlFlow("readable(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, $OFFSET ->")
            .beginControlFlow("$WITH($delegateName)")
            .add(
                resultType.parseBluetoothResult(
                    CodeBlock.of(
                        "%T($THIS@readable).$readMethod(device.identifier",
                        className,
                    ),
                ),
            )
            .endControlFlow()
            .endControlFlow()
            .build()
    }

    private fun generateSetupWritableByteArrayMethod(propertyDeclaration: KSPropertyDeclaration, delegateName: String, className: ClassName): CodeBlock {
        val writeMethod = propertyDeclaration.onWriteMethodName

        return CodeBlock.builder()
            .beginControlFlow("writable(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { device, value, $OFFSET ->")
            .beginControlFlow("$WITH($delegateName)")
            .addStatement(
                "%T($THIS@writable).$writeMethod(value, $OFFSET, device.identifier)",
                className,
            )
            .endControlFlow()
            .endControlFlow()
            .build()
    }

    private fun generateSetupWritableMethod(propertyDeclaration: KSPropertyDeclaration, delegateName: String, className: ClassName): CodeBlock {
        val writeMethod = propertyDeclaration.onWriteMethodName
        val failedToWriteMethod = "$ON_FAILED_TO_WRITE${propertyDeclaration.simpleName.asString().replaceFirstChar {
            it.uppercase()
        }}"
        return CodeBlock.builder()
            .addStatement("writable(")
            .indent()
            .addStatement("encrypted = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
            .addStatement("deserializationStrategy = %L,", propertyDeclaration.type.resolve().toTypeName().serializer(logger))
            .addStatement("bluetoothFormat = $FORMAT,")
            .addStatement("onFailedToWrite = { device, exception ->")
            .indent()
            .beginControlFlow("with($delegateName)")
            .addStatement(
                "%T($THIS@writable).$failedToWriteMethod(exception, device.identifier)",
                className,
            )
            .endControlFlow()
            .unindent()
            .addStatement("},")
            .addStatement("onWrite = { device, value ->")
            .indent()
            .beginControlFlow("$WITH($delegateName)")
            .addStatement(
                "%T($THIS@writable).$writeMethod(value, device.identifier)",
                className,
            )
            .endControlFlow()
            .unindent()
            .addStatement("}")
            .unindent()
            .addStatement(")")
            .build()
    }

    override fun generateSimulated(nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(declaration, GenerationType.SERVER_SIMULATOR)
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
        val delegate = interfaceName.nestedClass(DELEGATE)
        val remote = NameHelper.nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(declaration.delegateParameterName, delegate)
                    .addParameter(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT))
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addProperty(
                PropertySpec.builder(declaration.delegateParameterName, delegate)
                    .initializer(declaration.delegateParameterName)
                    .build(),
            )
            .addProperty(
                PropertySpec.builder(REMOTES, MUTABLE_MAP.parameterizedBy(References.Bluetooth.Device.identifier, remote))
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
            .addFunction(generateSimulatorGenerateRemoteMethod(remote, properties))
            .addTypes(nested)
            .build()
    }

    private fun generateSimulatorGenerateRemoteMethod(remote: ClassName, properties: Sequence<KSPropertyDeclaration>): FunSpec = FunSpec.builder(GENERATE_REMOTE)
        .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
        .returns(remote)
        .addCode(
            CodeBlock.builder()
                .beginControlFlow("$RETURN $REMOTES.getOrPut($IDENTIFIER)")
                .addStatement("%T(", remote)
                .indent()
                .apply {
                    properties.firstOrNull { it.isReadable }?.let { readProperty ->
                        add(generateGenerateRemoteOnReadAction(readProperty))
                    }
                    properties.firstOrNull { it.isAnnotationPresent(Writable::class) }?.let { writeProperty ->
                        add(generateGenerateRemoteOnWriteAction(writeProperty))
                    }
                }
                .unindent()
                .addStatement(")")
                .endControlFlow()
                .build(),
        ).build()

    private fun generateGenerateRemoteOnReadAction(readProperty: KSPropertyDeclaration): CodeBlock = CodeBlock.builder()
        .addStatement("${readProperty.onReadMethodName}$ACTION = {")
        .indent()
        .beginControlFlow("%M", References.KotlinX.Coroutines.coroutineScopeMethod)
        .beginControlFlow("%M", References.KotlinX.Coroutines.Selects.select)
        .addStatement("%M {", References.KotlinX.Coroutines.async)
        .indent()
        .beginControlFlow("$WITH(${declaration.delegateParameterName})")
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
        .build()

    private fun generateGenerateRemoteOnWriteAction(writeProperty: KSPropertyDeclaration): CodeBlock = CodeBlock.builder()
        .beginControlFlow("${writeProperty.onWriteMethodName}$ACTION = { ${writeProperty.simpleName.asString()} ->")
        .indent()
        .beginControlFlow("%M", References.KotlinX.Coroutines.coroutineScopeMethod)
        .beginControlFlow("%M", References.KotlinX.Coroutines.Selects.select)
        .addStatement("%M {", References.KotlinX.Coroutines.async)
        .indent()
        .beginControlFlow("$WITH(${declaration.delegateParameterName})")
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
        .build()
}
