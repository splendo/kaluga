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
import com.splendo.kaluga.bluetooth.ksp.helpers.ALL
import com.splendo.kaluga.bluetooth.ksp.helpers.ALSO
import com.splendo.kaluga.bluetooth.ksp.helpers.AS
import com.splendo.kaluga.bluetooth.ksp.helpers.BLUETOOTH_FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.BUILDER
import com.splendo.kaluga.bluetooth.ksp.helpers.CHANGED
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.CONFIGURE
import com.splendo.kaluga.bluetooth.ksp.helpers.CONTAINS
import com.splendo.kaluga.bluetooth.ksp.helpers.COROUTINE_CONTEXT
import com.splendo.kaluga.bluetooth.ksp.helpers.COROUTINE_SCOPE
import com.splendo.kaluga.bluetooth.ksp.helpers.DELEGATE
import com.splendo.kaluga.bluetooth.ksp.helpers.DESCRIPTORS
import com.splendo.kaluga.bluetooth.ksp.helpers.DESERIALIZATION_STRATEGY
import com.splendo.kaluga.bluetooth.ksp.helpers.DEVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.DEVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.ELSE
import com.splendo.kaluga.bluetooth.ksp.helpers.EMIT
import com.splendo.kaluga.bluetooth.ksp.helpers.EMPTY_LIST
import com.splendo.kaluga.bluetooth.ksp.helpers.ENCRYPTED
import com.splendo.kaluga.bluetooth.ksp.helpers.EXCEPTION
import com.splendo.kaluga.bluetooth.ksp.helpers.FAILURE
import com.splendo.kaluga.bluetooth.ksp.helpers.FALSE
import com.splendo.kaluga.bluetooth.ksp.helpers.FIND
import com.splendo.kaluga.bluetooth.ksp.helpers.FIRST
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.GENERATE_REMOTE
import com.splendo.kaluga.bluetooth.ksp.helpers.GET_OR_PUT
import com.splendo.kaluga.bluetooth.ksp.helpers.IDENTIFIER
import com.splendo.kaluga.bluetooth.ksp.helpers.IF
import com.splendo.kaluga.bluetooth.ksp.helpers.IS_CLOSED
import com.splendo.kaluga.bluetooth.ksp.helpers.IT
import com.splendo.kaluga.bluetooth.ksp.helpers.LAZY
import com.splendo.kaluga.bluetooth.ksp.helpers.LET
import com.splendo.kaluga.bluetooth.ksp.helpers.MAP
import com.splendo.kaluga.bluetooth.ksp.helpers.MUTABLE_FLOW
import com.splendo.kaluga.bluetooth.ksp.helpers.MUTABLE_MAP_OF
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFIABLE
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFY
import com.splendo.kaluga.bluetooth.ksp.helpers.NOTIFY_ALL
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_AWAIT
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_FAILED_TO_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_SUBSCRIBE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_UNSUBSCRIBE
import com.splendo.kaluga.bluetooth.ksp.helpers.ON_WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.PROPERTIES
import com.splendo.kaluga.bluetooth.ksp.helpers.READABLE
import com.splendo.kaluga.bluetooth.ksp.helpers.REMOTES
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SET_OF
import com.splendo.kaluga.bluetooth.ksp.helpers.SUBSCRIBED_DEVICES
import com.splendo.kaluga.bluetooth.ksp.helpers.SUBSCRIBERS
import com.splendo.kaluga.bluetooth.ksp.helpers.SUBSCRIPTION_COUNT
import com.splendo.kaluga.bluetooth.ksp.helpers.THIS
import com.splendo.kaluga.bluetooth.ksp.helpers.TO
import com.splendo.kaluga.bluetooth.ksp.helpers.TRUE
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.VAL
import com.splendo.kaluga.bluetooth.ksp.helpers.VALUE
import com.splendo.kaluga.bluetooth.ksp.helpers.WITH
import com.splendo.kaluga.bluetooth.ksp.helpers.WRITABLE
import com.splendo.kaluga.bluetooth.ksp.helpers.delegateParameterName
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.splendo.kaluga.bluetooth.ksp.helpers.isNotifiable
import com.splendo.kaluga.bluetooth.ksp.helpers.isReadable
import com.splendo.kaluga.bluetooth.ksp.helpers.isWritable
import com.splendo.kaluga.bluetooth.ksp.helpers.nullIfPropertyIsNull
import com.splendo.kaluga.bluetooth.ksp.helpers.onReadMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.onWriteMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.optionalChainIfNullable
import com.splendo.kaluga.bluetooth.ksp.helpers.serializer
import com.splendo.kaluga.bluetooth.ksp.helpers.subscribeMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.unsubscribeMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.withLetIfNull
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

internal class BluetoothLocalCharacteristicBuilder(declaration: KSClassDeclaration, private val characteristic: BluetoothCharacteristic, options: Options, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, options, logger) {

    private companion object {
        const val HAS_SUBSCRIBED = "hasSubscribed"
    }

    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
        return TypeSpec.interfaceBuilder(interfaceName)
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
                generateDelegate(interfaceName),
            )
            .generateBody(declarations, GenerationType.Type.API)
            .build()
    }

    private fun generateDelegate(interfaceName: ClassName): TypeSpec = TypeSpec.interfaceBuilder(DELEGATE)
        .apply {
            var hasReadMethod = false
            var hasWriteMethod = false
            var hasNotifyMethods = false
            declaration.declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (propertyDeclaration.isReadable ||
                    propertyDeclaration.isWritable ||
                    propertyDeclaration.isNotifiable
                ) {
                    if (propertyDeclaration.isAnnotationPresent(Readable::class)) {
                        if (!hasReadMethod) {
                            hasReadMethod = true

                            addFunction(generateDelegateReadMethods(interfaceName, propertyDeclaration))
                        } else {
                            logOnlyOneProperty(Readable::class)
                        }
                    }
                    if (propertyDeclaration.isAnnotationPresent(Writable::class) || propertyDeclaration.isAnnotationPresent(WritableWithoutResponse::class) ||
                        propertyDeclaration.isAnnotationPresent(WritableSigned::class)
                    ) {
                        if (!hasWriteMethod) {
                            hasWriteMethod = true

                            addFunctions(generateDelegateWriteMethods(interfaceName, propertyDeclaration))
                        } else {
                            logOnlyOneProperty(Writable::class, WritableWithoutResponse::class, WritableSigned::class)
                        }
                    }

                    if (propertyDeclaration.isAnnotationPresent(Notifiable::class) || propertyDeclaration.isAnnotationPresent(Indicatable::class)) {
                        if (!hasNotifyMethods) {
                            hasNotifyMethods = true

                            addFunctions(generateDelegateSubscriptionMethods(propertyDeclaration, interfaceName))
                        } else {
                            logOnlyOneProperty(Notifiable::class, Indicatable::class)
                        }
                    }
                } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                    addProperty(
                        propertyDeclaration.delegateParameterName,
                        NameHelper.nameFor(typeDeclaration, GenerationType.SERVER_API).nestedClass(DELEGATE).nullIfPropertyIsNull(propertyDeclaration),
                    )
                } else {
                    invalidProperty(
                        propertyDeclaration,
                        Readable::class,
                        Writable::class,
                        WritableWithoutResponse::class,
                        WritableSigned::class,
                        Notifiable::class,
                        Indicatable::class,
                        BluetoothDescriptor::class,
                    )
                }
            }
        }
        .build()

    private fun generateDelegateReadMethods(interfaceName: ClassName, propertyDeclaration: KSPropertyDeclaration): FunSpec {
        val readMethod = propertyDeclaration.onReadMethodName
        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, options, logger)
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
    private fun generateDelegateWriteMethods(interfaceName: ClassName, propertyDeclaration: KSPropertyDeclaration): List<FunSpec> {
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

    private fun generateDelegateSubscriptionMethods(propertyDeclaration: KSPropertyDeclaration, interfaceName: ClassName): List<FunSpec> {
        val subscribeMethod = propertyDeclaration.subscribeMethodName
        val unsubscribeMethod = propertyDeclaration.unsubscribeMethodName

        return listOf(
            FunSpec.builder(subscribeMethod).addModifiers(KModifier.ABSTRACT)
                .receiver(interfaceName)
                .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                .build(),
            FunSpec.builder(unsubscribeMethod).addModifiers(KModifier.ABSTRACT)
                .receiver(interfaceName)
                .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                .build(),
        )
    }

    override fun generateBluetooth(nested: List<TypeSpec>): TypeSpec {
        val className = NameHelper.nameFor(declaration, GenerationType.SERVER_BLUETOOTH)
        val interfaceName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER)
        return TypeSpec.classBuilder(className).addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(CHARACTERISTIC, characteristicClass()),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bluetoothFormat).takeIf { needsFormatter.needsFormatter },
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addType(generateBluetoothCompanionObject(declaration, className, interfaceName))
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(CHARACTERISTIC, characteristicClass())
                        .initializer(CHARACTERISTIC).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT)
                        .build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.BLUETOOTH)
            .build()
    }

    private fun generateBluetoothCompanionObject(declaration: KSClassDeclaration, className: ClassName, interfaceName: ClassName): TypeSpec = TypeSpec.companionObjectBuilder()
        .addFunction(
            FunSpec.builder(CONFIGURE)
                .apply {
                    val delegateParameterName = declaration.delegateParameterName
                    addParameter(BUILDER, References.Bluetooth.Server.localServiceDSL)
                    addParameter(
                        delegateParameterName,
                        interfaceName.nestedClass(DELEGATE),
                    )
                    val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER_DSL)
                    if (needsFormatter.needsFormatter) {
                        addParameter(FORMAT, References.Bluetooth.Serialization.bluetoothFormat)
                    }
                    addCode(
                        CodeBlock.builder()
                            .beginControlFlow(
                                "$RETURN $BUILDER.$CHARACTERISTIC(%T.$UUID) {",
                                NameHelper.nameFor(declaration, GenerationType.SERVER_API),
                            )
                            .apply {
                                var hasReadMethod = false
                                var hasWriteMethod = false
                                var hasNotifyMethods = false
                                val constructorFormat = NeedsFormatterHelper.needsBluetoothFormatter(declaration, NeedsFormatterHelper.Target.SERVER).functionArgument
                                fun castingMethod(scope: String): CodeBlock {
                                    val scopeMethod = if (scope.isNotEmpty()) "$THIS@$scope" else THIS
                                    return if (isNotifiable()) {
                                        CodeBlock.of(
                                            "%T($scopeMethod $AS %T$constructorFormat)",
                                            className,
                                            References.Bluetooth.Server.localCharacteristicNotifiable,
                                        )
                                    } else {
                                        CodeBlock.of("%T($scopeMethod$constructorFormat)", className)
                                    }
                                }
                                declaration.declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
                                    val typeDeclaration = propertyDeclaration.type.resolve().declaration
                                    if (propertyDeclaration.isReadable ||
                                        propertyDeclaration.isWritable ||
                                        propertyDeclaration.isNotifiable
                                    ) {
                                        if (propertyDeclaration.isReadable) {
                                            if (!hasReadMethod) {
                                                hasReadMethod = true

                                                add(generateSetupReadMethods(propertyDeclaration, delegateParameterName, ::castingMethod))
                                            } else {
                                                logOnlyOneProperty(Readable::class)
                                            }
                                        }
                                        if (propertyDeclaration.isWritable) {
                                            if (!hasWriteMethod) {
                                                hasWriteMethod = true

                                                add(generateSetupWriteMethods(propertyDeclaration, delegateParameterName, ::castingMethod))
                                            } else {
                                                logOnlyOneProperty(Writable::class, WritableWithoutResponse::class, WritableSigned::class)
                                            }
                                        }

                                        if (propertyDeclaration.isNotifiable) {
                                            if (!hasNotifyMethods) {
                                                hasNotifyMethods = true
                                                add(generateSetupNotificationMethods(propertyDeclaration, delegateParameterName, constructorFormat, className))
                                            } else {
                                                logOnlyOneProperty(Notifiable::class, Indicatable::class)
                                            }
                                        }
                                    } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                        val descriptorNeedsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(
                                            typeDeclaration,
                                            NeedsFormatterHelper.Target.SERVER_DSL,
                                        )
                                        withLetIfNull(
                                            "$delegateParameterName.${propertyDeclaration.delegateParameterName}",
                                            property = propertyDeclaration,
                                        ) { property ->
                                            addStatement(
                                                "%T.$CONFIGURE($THIS, $property${descriptorNeedsFormatter.functionArgument})",
                                                NameHelper.nameFor(typeDeclaration, GenerationType.SERVER_BLUETOOTH),
                                            )
                                        }
                                    } else {
                                        invalidProperty(
                                            propertyDeclaration,
                                            Readable::class,
                                            Writable::class,
                                            WritableWithoutResponse::class,
                                            WritableSigned::class,
                                            Notifiable::class,
                                            Indicatable::class,
                                            BluetoothDescriptor::class,
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
        .build()

    private fun generateSetupReadMethods(propertyDeclaration: KSPropertyDeclaration, delegateParameterName: String, castingMethod: (String) -> CodeBlock): CodeBlock {
        val readMethod = propertyDeclaration.onReadMethodName
        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, options, logger)
        return CodeBlock.builder()
            .beginControlFlow("$READABLE(${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { $DEVICE, $OFFSET ->")
            .beginControlFlow("$WITH($delegateParameterName)")
            .add(resultType.parseBluetoothResult(CodeBlock.of("%L.$readMethod($DEVICE.$IDENTIFIER", castingMethod(READABLE))))
            .endControlFlow()
            .endControlFlow()
            .build()
    }
    private fun generateSetupWriteMethods(propertyDeclaration: KSPropertyDeclaration, delegateParameterName: String, castingMethod: (String) -> CodeBlock): CodeBlock {
        val writeMethod = propertyDeclaration.onWriteMethodName
        val properties = listOfNotNull(
            References.Bluetooth.writeProperty.takeIf { propertyDeclaration.isAnnotationPresent(Writable::class) },
            References.Bluetooth.writeWithoutResponseProperty.takeIf {
                propertyDeclaration.isAnnotationPresent(WritableWithoutResponse::class)
            },
            References.Bluetooth.signedWriteProperty.takeIf { propertyDeclaration.isAnnotationPresent(WritableSigned::class) },
        )
        val propertiesCode = properties.joinToCode(prefix = "$SET_OF(", suffix = ")") { CodeBlock.of("%T", it) }

        return if (propertyDeclaration.isByteArray) {
            CodeBlock.builder()
                .beginControlFlow(
                    "$WRITABLE(%L, ${propertyDeclaration.isAnnotationPresent(Encrypted::class)}) { $DEVICE, $VALUE, $OFFSET ->",
                    propertiesCode,
                )
                .beginControlFlow("$WITH($delegateParameterName)")
                .addStatement("%L.$writeMethod($VALUE, $OFFSET, $DEVICE.$IDENTIFIER)", castingMethod(WRITABLE))
                .endControlFlow()
                .endControlFlow()
        } else {
            val failedToWriteMethod = "$ON_FAILED_TO_WRITE${
                propertyDeclaration.simpleName.asString().replaceFirstChar {
                    it.uppercase()
                }
            }"
            CodeBlock.builder()
                .addStatement("$WRITABLE(")
                .indent()
                .addStatement("$PROPERTIES = %L,", propertiesCode)
                .addStatement("$ENCRYPTED = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
                .addStatement(
                    "$DESERIALIZATION_STRATEGY = %L,",
                    propertyDeclaration.type.resolve().toTypeName().serializer(logger),
                )
                .addStatement("$BLUETOOTH_FORMAT = $FORMAT,")
                .addStatement("$ON_FAILED_TO_WRITE = { $DEVICE, $EXCEPTION ->")
                .indent()
                .beginControlFlow("$WITH($delegateParameterName)")
                .addStatement("%L.$failedToWriteMethod($EXCEPTION, $DEVICE.$IDENTIFIER)", castingMethod(WRITABLE))
                .endControlFlow()
                .unindent()
                .addStatement("},")
                .addStatement("$ON_WRITE = { $DEVICE, $VALUE ->")
                .indent()
                .beginControlFlow("$WITH($delegateParameterName)")
                .addStatement("%L.$writeMethod($VALUE, $DEVICE.$IDENTIFIER)", castingMethod(WRITABLE))
                .endControlFlow()
                .unindent()
                .addStatement("}")
                .unindent()
                .addStatement(")")
        }.build()
    }

    private fun generateSetupNotificationMethods(
        propertyDeclaration: KSPropertyDeclaration,
        delegateParameterName: String,
        constructorFormat: String,
        className: ClassName,
    ): CodeBlock {
        val properties = listOfNotNull(
            References.Bluetooth.notifyProperty.takeIf { propertyDeclaration.isAnnotationPresent(Notifiable::class) },
            References.Bluetooth.indicatableProperty.takeIf { propertyDeclaration.isAnnotationPresent(Indicatable::class) },
        )
        val propertiesCode = properties.joinToCode(prefix = "$SET_OF(", suffix = ")") { CodeBlock.of("%T", it) }

        val subscribeMethod = propertyDeclaration.subscribeMethodName
        val unsubscribeMethod = propertyDeclaration.unsubscribeMethodName
        return CodeBlock.builder()
            .addStatement("$NOTIFIABLE(")
            .indent()
            .addStatement("$PROPERTIES = %L,", propertiesCode)
            .addStatement("$ENCRYPTED = ${propertyDeclaration.isAnnotationPresent(Encrypted::class)},")
            .addStatement("$ON_SUBSCRIBE = { $DEVICE ->")
            .indent()
            .beginControlFlow("$WITH($delegateParameterName)")
            .addStatement(
                "%T($THIS@$NOTIFIABLE$constructorFormat).$subscribeMethod($DEVICE.$IDENTIFIER)",
                className,
            )
            .endControlFlow()
            .unindent()
            .addStatement("},")
            .addStatement("$ON_UNSUBSCRIBE = { $DEVICE ->")
            .indent()
            .beginControlFlow("$WITH($delegateParameterName)")
            .addStatement(
                "%T($THIS@$NOTIFIABLE$constructorFormat).$unsubscribeMethod($DEVICE.$IDENTIFIER)",
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
        val apiName = NameHelper.nameFor(declaration, GenerationType.SERVER_API)
        val delegate = apiName.nestedClass(DELEGATE)
        val remote = NameHelper.nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        val notifiable = properties.firstOrNull { it.isNotifiable }
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(declaration.delegateParameterName, delegate),
                            ParameterSpec(COROUTINE_SCOPE, References.KotlinX.Coroutines.coroutineScope),
                            ParameterSpec(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT)),
                        ),

                    )
                    .build(),
            )
            .addSuperinterface(apiName)
            .addProperties(generateSimulatorProperties(notifiable, delegate, remote))
            .apply {
                if (options.generateClient) {
                    addFunction(generateSimulatorGenerateRemoteMethod(remote, notifiable, properties))
                }
            }
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.SIMULATOR)
            .build()
    }

    private fun generateSimulatorProperties(notifiable: KSPropertyDeclaration?, delegate: ClassName, remote: ClassName): List<PropertySpec> = listOfNotNull(
        PropertySpec.builder(declaration.delegateParameterName, delegate)
            .initializer(declaration.delegateParameterName)
            .build(),
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
            .initializer("${MUTABLE_MAP_OF}()")
            .build().takeIf { options.generateClient },
        PropertySpec.builder(IS_CLOSED, References.KotlinX.Coroutines.deferred.parameterizedBy(UNIT))
            .addModifiers(KModifier.PRIVATE)
            .initializer(IS_CLOSED)
            .build(),
    ) + notifiable?.let {
        listOf(
            PropertySpec.builder(
                COROUTINE_SCOPE,
                References.KotlinX.Coroutines.coroutineScope,
            ).initializer(COROUTINE_SCOPE).build(),
            PropertySpec.builder(
                "_${notifiable.simpleName.asString()}$SUBSCRIBERS",
                References.KotlinX.Coroutines.Flow.mutableStateFlow.parameterizedBy(LIST.parameterizedBy(References.Bluetooth.Device.identifier)),
            ).initializer("%T($EMPTY_LIST)", References.KotlinX.Coroutines.Flow.mutableStateFlow)
                .addModifiers(KModifier.PRIVATE)
                .build(),
        )
    }.orEmpty()

    private fun generateSimulatorGenerateRemoteMethod(remote: ClassName, notifiable: KSPropertyDeclaration?, properties: Sequence<KSPropertyDeclaration>): FunSpec =
        FunSpec.builder(GENERATE_REMOTE)
            .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
            .returns(remote)
            .addCode(
                CodeBlock.builder()
                    .add("$RETURN $REMOTES.$GET_OR_PUT($IDENTIFIER) {\n")
                    .indent()
                    .beginControlFlow("$WITH (${declaration.delegateParameterName})")
                    .apply {
                        if (notifiable != null) {
                            add(generateGenerateRemoteMutableFlow(notifiable))
                        }
                        addStatement("%T(", remote)
                        indent()
                        properties.firstOrNull { it.isReadable }?.let { readProperty ->
                            add(generateGenerateRemoteOnReadAction(readProperty))
                        }
                        properties.firstOrNull { it.isWritable }?.let { writeProperty ->
                            add(generateGenerateRemoteOnWriteAction(writeProperty))
                        }
                        if (notifiable != null) {
                            addStatement("${notifiable.simpleName.asString()} = $MUTABLE_FLOW.%M(),", References.KotlinX.Coroutines.Flow.asSharedFlow)
                        }

                        val descriptorCode =
                            properties.mapNotNull { propertyDeclaration ->
                                val type = propertyDeclaration.type.resolve()
                                val typeDeclaration = type.declaration
                                if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                    CodeBlock.of(
                                        "${propertyDeclaration.simpleName.asString()} = ${propertyDeclaration.simpleName.asString()}${propertyDeclaration.optionalChainIfNullable}.$GENERATE_REMOTE($IDENTIFIER)",
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
                        addStatement(")${if (notifiable != null) " $TO $MUTABLE_FLOW" else ""}")
                    }
                    .endControlFlow()
                    .unindent()
                    .apply {
                        if (notifiable != null) {
                            add("}.$FIRST\n")
                        } else {
                            add("}\n")
                        }
                    }
                    .build(),
            )
            .build()

    private fun generateGenerateRemoteMutableFlow(notifiable: KSPropertyDeclaration): CodeBlock = CodeBlock.builder()
        .addStatement("$VAL $MUTABLE_FLOW = %T<%T>()", References.KotlinX.Coroutines.Flow.mutableSharedFlow, notifiable.type.resolve().toTypeName())
        .beginControlFlow("$COROUTINE_SCOPE.%M", References.KotlinX.Coroutines.launch)
        .addStatement(
            "$MUTABLE_FLOW.$SUBSCRIPTION_COUNT.%M { $IT >= 1 }.%M()",
            References.KotlinX.Coroutines.Flow.map,
            References.KotlinX.Coroutines.Flow.distinctUntilChanged,
        )
        .indent()
        .addStatement(
            ".%M { _${notifiable.simpleName.asString()}$SUBSCRIBERS.%M { $EMPTY_LIST } }",
            References.KotlinX.Coroutines.Flow.onCompletion,
            References.KotlinX.Coroutines.Flow.update,
        )
        .beginControlFlow(".%M { $HAS_SUBSCRIBED ->", References.KotlinX.Coroutines.Flow.collect)
        .beginControlFlow("$IF ($HAS_SUBSCRIBED)")
        .addStatement("_${notifiable.simpleName.asString()}$SUBSCRIBERS.%M { $IT + $IDENTIFIER }", References.KotlinX.Coroutines.Flow.update)
        .addStatement("${notifiable.subscribeMethodName}($IDENTIFIER)")
        .nextControlFlow(ELSE)
        .addStatement("_${notifiable.simpleName.asString()}$SUBSCRIBERS.%M { $IT - $IDENTIFIER }", References.KotlinX.Coroutines.Flow.update)
        .addStatement("${notifiable.unsubscribeMethodName}($IDENTIFIER)")
        .endControlFlow()
        .endControlFlow()
        .unindent()
        .endControlFlow()
        .build()

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
        .addStatement("}.$ON_AWAIT { $IT }")
        .apply {
            val resultType = BluetoothResultTypeBuilder(declaration, readProperty, options, logger)
            val result = if (resultType.hasCustomResult) {
                CodeBlock.of("%T.$FAILURE(%T)", resultType.responseClassName, References.Bluetooth.deviceUnavailable)
            } else {
                CodeBlock.of("%T", References.Bluetooth.deviceUnavailable)
            }
            addStatement("$IS_CLOSED.$ON_AWAIT { %L }", result)
        }
        .endControlFlow()
        .addStatement(".$ALSO { $COROUTINE_CONTEXT.%M() }", References.KotlinX.Coroutines.cancelChildren)
        .endControlFlow()
        .unindent()
        .addStatement("},")
        .build()

    private fun generateGenerateRemoteOnWriteAction(writeProperty: KSPropertyDeclaration): CodeBlock = CodeBlock.builder()
        .addStatement("${writeProperty.onWriteMethodName}$ACTION = { ${writeProperty.simpleName.asString()} ->")
        .indent()
        .beginControlFlow("%M", References.KotlinX.Coroutines.coroutineScopeMethod)
        .beginControlFlow("%M", References.KotlinX.Coroutines.Selects.select)
        .addStatement("%M {", References.KotlinX.Coroutines.async)
        .indent()
        .beginControlFlow("$WITH(${declaration.delegateParameterName})")
        .addStatement(
            "${writeProperty.onWriteMethodName}(${writeProperty.simpleName.asString()}${if (writeProperty.isByteArray) ", 0" else ""}, $IDENTIFIER)",
        )
        .endControlFlow()
        .unindent()
        .addStatement("}.$ON_AWAIT { $IT }")
        .addStatement("$IS_CLOSED.$ON_AWAIT { %T }", References.Bluetooth.deviceUnavailable)
        .endControlFlow()
        .addStatement(".$ALSO { $COROUTINE_CONTEXT.%M() }", References.KotlinX.Coroutines.cancelChildren)
        .endControlFlow()
        .unindent()
        .addStatement("},")
        .build()

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, type: GenerationType.Type): TypeSpec.Builder = apply {
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
                        logOnlyOneProperty(Readable::class)
                    }
                }
                if (propertyDeclaration.isWritable) {
                    if (!hasWriteMethod) {
                        hasWriteMethod = true
                    } else {
                        logOnlyOneProperty(Writable::class, WritableWithoutResponse::class, WritableSigned::class)
                    }
                }

                if (propertyDeclaration.isNotifiable) {
                    if (!hasNotifyMethods) {
                        hasNotifyMethods = true

                        addProperty(generateSubscribersProperty(propertyDeclaration, type))
                        addFunctions(generateNotifyMethods(propertyDeclaration, type))
                    } else {
                        logOnlyOneProperty(Notifiable::class, Indicatable::class)
                    }
                }
            } else if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                addProperty(
                    generateDescriptorProperty(propertyDeclaration, typeDeclaration, type),
                )
            } else {
                invalidProperty(
                    propertyDeclaration,
                    Readable::class,
                    Writable::class,
                    WritableWithoutResponse::class,
                    WritableSigned::class,
                    Notifiable::class,
                    Indicatable::class,
                    BluetoothDescriptor::class,
                )
            }
        }
    }

    private fun generateSubscribersProperty(propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): PropertySpec = PropertySpec.builder(
        "${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS",
        References.KotlinX.Coroutines.Flow.flow.parameterizedBy(LIST.parameterizedBy(References.Bluetooth.Device.identifier)),
    )
        .addModifiers(*type.additionalModifiers.toTypedArray())
        .apply {
            when (type) {
                GenerationType.Type.API -> {}

                GenerationType.Type.BLUETOOTH -> {
                    initializer(
                        CodeBlock.builder()
                            .beginControlFlow("$CHARACTERISTIC.$SUBSCRIBED_DEVICES.%M { $DEVICES ->", References.KotlinX.Coroutines.Flow.map)
                            .addStatement("$DEVICES.$MAP(%T::$IDENTIFIER)", References.Bluetooth.Server.connectedDevice)
                            .endControlFlow()
                            .build(),
                    )
                }

                GenerationType.Type.SIMULATOR -> {
                    initializer("_${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS.%M()", References.KotlinX.Coroutines.Flow.asStateFlow)
                }
            }
        }
        .build()

    private fun generateNotifyMethods(propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): List<FunSpec> {
        val resolvedType = propertyDeclaration.type.resolve()
        return listOf(
            FunSpec.builder("$NOTIFY_ALL${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}$CHANGED")
                .addModifiers(KModifier.SUSPEND, *type.additionalModifiers.toTypedArray())
                .addParameter(propertyDeclaration.simpleName.asString(), resolvedType.toTypeName())
                .returns(BOOLEAN)
                .apply {
                    when (type) {
                        GenerationType.Type.API -> {}

                        GenerationType.Type.BLUETOOTH -> {
                            if (propertyDeclaration.isByteArray) {
                                addStatement("$RETURN $CHARACTERISTIC.$NOTIFY_ALL(${propertyDeclaration.simpleName.asString()})")
                            } else {
                                addStatement(
                                    "$RETURN $CHARACTERISTIC.$NOTIFY_ALL(${propertyDeclaration.simpleName.asString()}, %L, $FORMAT)",
                                    resolvedType.toTypeName().serializer(logger),
                                )
                            }
                        }

                        GenerationType.Type.SIMULATOR -> {
                            addCode(
                                CodeBlock.builder()
                                    .beginControlFlow("$RETURN _${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS.$VALUE.$ALL")
                                    .addStatement(
                                        "$NOTIFY${
                                            propertyDeclaration.simpleName.asString().replaceFirstChar {
                                                it.uppercase()
                                            }
                                        }$CHANGED($IT, ${propertyDeclaration.simpleName.asString()})",
                                    )
                                    .endControlFlow()
                                    .build(),
                            )
                        }
                    }
                }
                .build(),
            FunSpec.builder("$NOTIFY${propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }}$CHANGED")
                .addModifiers(KModifier.SUSPEND, *type.additionalModifiers.toTypedArray())
                .addParameter(IDENTIFIER, References.Bluetooth.Device.identifier)
                .addParameter(propertyDeclaration.simpleName.asString(), resolvedType.toTypeName())
                .returns(BOOLEAN)
                .apply {
                    when (type) {
                        GenerationType.Type.API -> {}

                        GenerationType.Type.BLUETOOTH -> {
                            val notifyCode = if (propertyDeclaration.isByteArray) {
                                CodeBlock.of("$CHARACTERISTIC.$NOTIFY($IT, ${propertyDeclaration.simpleName.asString()})")
                            } else {
                                CodeBlock.of(
                                    "$CHARACTERISTIC.$NOTIFY($IT, ${propertyDeclaration.simpleName.asString()}, %L, $FORMAT)",
                                    resolvedType.toTypeName().serializer(logger),
                                )
                            }
                            addCode(
                                CodeBlock.builder()
                                    .add("$RETURN $CHARACTERISTIC.$SUBSCRIBED_DEVICES.$VALUE.$FIND { $IT.$IDENTIFIER == $IDENTIFIER }?.$LET {\n")
                                    .indent()
                                    .add(notifyCode)
                                    .unindent()
                                    .add("} ?: $FALSE\n")
                                    .build(),
                            )
                        }

                        GenerationType.Type.SIMULATOR -> {
                            if (options.generateClient) {
                                addCode(
                                    CodeBlock.builder()
                                        .add("$RETURN $REMOTES[$IDENTIFIER]?.$LET { (_, $MUTABLE_FLOW) ->\n")
                                        .indent()
                                        .beginControlFlow("$IF (_${propertyDeclaration.simpleName.asString()}$SUBSCRIBERS.$VALUE.$CONTAINS($IDENTIFIER))")
                                        .addStatement("$MUTABLE_FLOW.$EMIT(${propertyDeclaration.simpleName.asString()})")
                                        .addStatement(TRUE)
                                        .nextControlFlow(ELSE)
                                        .addStatement(FALSE)
                                        .endControlFlow()
                                        .unindent()
                                        .addStatement("} ?: $FALSE")
                                        .build(),
                                )
                            } else {
                                addStatement("$RETURN $FALSE")
                            }
                        }
                    }
                }
                .build(),
        )
    }

    private fun generateDescriptorProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec =
        PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            NameHelper.serverName(typeDeclaration, type).nullIfPropertyIsNull(propertyDeclaration),
        )
            .addModifiers(*type.additionalModifiers.toTypedArray())
            .apply {
                when (type) {
                    GenerationType.Type.API -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        delegate(
                            "$LAZY { %L }",
                            CodeBlock.of(
                                "%T($CHARACTERISTIC.$DESCRIPTORS.%M(%T.$UUID))",
                                NameHelper.serverName(typeDeclaration, type),
                                References.Bluetooth.get,
                                NameHelper.nameFor(typeDeclaration, GenerationType.SERVER_API),
                            ),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> {
                        initializer(
                            CodeBlock.builder()
                                .withLetIfNull("${declaration.delegateParameterName}.${propertyDeclaration.delegateParameterName}", propertyDeclaration) { property ->
                                    addStatement(
                                        "%T($property, $IS_CLOSED)",
                                        NameHelper.serverName(typeDeclaration, type),
                                    )
                                }
                                .build(),
                        )
                    }
                }
            }
            .build()

    fun isNotifiable() = declarations.filterIsInstance<KSPropertyDeclaration>().any { it.isNotifiable }
    fun characteristicClass(): ClassName = if (isNotifiable()) {
        References.Bluetooth.Server.localCharacteristicNotifiable
    } else {
        References.Bluetooth.Server.localCharacteristic
    }
}
