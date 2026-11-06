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
import com.splendo.kaluga.bluetooth.annotations.Indicatable
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bluetooth.annotations.WritableSigned
import com.splendo.kaluga.bluetooth.annotations.WritableWithoutResponse
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION
import com.splendo.kaluga.bluetooth.ksp.helpers.AttributeValue
import com.splendo.kaluga.bluetooth.ksp.helpers.attributeValue
import com.splendo.kaluga.bluetooth.ksp.helpers.BYTES
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.COLLECT
import com.splendo.kaluga.bluetooth.ksp.helpers.ACTION_PARAMETER
import com.splendo.kaluga.bluetooth.ksp.helpers.CHARACTERISTICS
import com.splendo.kaluga.bluetooth.ksp.helpers.DESCRIPTORS
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.FROM_CHARACTERISTIC
import com.splendo.kaluga.bluetooth.ksp.helpers.FROM_SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.IT
import com.splendo.kaluga.bluetooth.ksp.helpers.LAZY
import com.splendo.kaluga.bluetooth.ksp.helpers.LET
import com.splendo.kaluga.bluetooth.ksp.helpers.NeedsFormatterHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OR_NULL
import com.splendo.kaluga.bluetooth.ksp.helpers.READ
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SERVICE
import com.splendo.kaluga.bluetooth.ksp.helpers.UUID
import com.splendo.kaluga.bluetooth.ksp.helpers.WRITE
import com.splendo.kaluga.bluetooth.ksp.helpers.isNotifiable
import com.splendo.kaluga.bluetooth.ksp.helpers.isReadable
import com.splendo.kaluga.bluetooth.ksp.helpers.isWritable
import com.splendo.kaluga.bluetooth.ksp.helpers.nullIfPropertyIsNull
import com.splendo.kaluga.bluetooth.ksp.helpers.onReadMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.onWriteMethodName
import com.splendo.kaluga.bluetooth.ksp.helpers.orNullIfNullable
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT

internal class BluetoothRemoteCharacteristicBuilder(declaration: KSClassDeclaration, private val characteristic: BluetoothCharacteristic, options: Options, logger: KSPLogger) :
    AbstractBluetoothClassBuilder(declaration, options, logger) {

    override fun generateAPI(nested: List<TypeSpec>): TypeSpec {
        val interfaceName = nameFor(declaration, GenerationType.CLIENT_API)
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
            .generateBody(declarations, GenerationType.Type.API)
            .build()
    }

    override fun generateBluetooth(nested: List<TypeSpec>): TypeSpec {
        val className = nameFor(declaration, GenerationType.CLIENT_BLUETOOTH)
        val interfaceName = nameFor(declaration, GenerationType.CLIENT_API)
        val needsFormatter = NeedsFormatterHelper.needsBluetoothFormatter(declaration, options)
        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(CHARACTERISTIC, References.Bluetooth.remoteCharacteristic),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bytesFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addType(generateBluetoothCompanionObject(needsFormatter, className, interfaceName))
            .addProperties(
                listOfNotNull(
                    PropertySpec.builder(CHARACTERISTIC, References.Bluetooth.remoteCharacteristic)
                        .initializer(CHARACTERISTIC).build(),
                    PropertySpec.builder(FORMAT, References.Bluetooth.Serialization.bytesFormat)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(FORMAT).build().takeIf { needsFormatter.needsFormatter },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.BLUETOOTH)
            .build()
    }

    private fun generateBluetoothCompanionObject(needsFormatter: NeedsFormatterHelper.NeedsFormatter, className: ClassName, interfaceName: ClassName): TypeSpec =
        TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder(FROM_SERVICE)
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(SERVICE, References.Bluetooth.remoteService),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bytesFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .returns(className)
                    .addStatement(
                        "$RETURN %T($SERVICE.$CHARACTERISTICS.%M(%T.$UUID)${needsFormatter.functionArgument})",
                        className,
                        References.Bluetooth.get,
                        interfaceName,
                    )
                    .build(),
            )
            .addFunction(
                FunSpec.builder("$FROM_SERVICE$OR_NULL")
                    .addParameters(
                        listOfNotNull(
                            ParameterSpec(SERVICE, References.Bluetooth.remoteService),
                            ParameterSpec(FORMAT, References.Bluetooth.Serialization.bytesFormat).takeIf { needsFormatter.needsFormatter },
                        ),
                    )
                    .returns(className.copy(nullable = true))
                    .addCode(
                        CodeBlock.builder()
                            .beginControlFlow(
                                "$RETURN $SERVICE.$CHARACTERISTICS.%M(%T.$UUID)?.$LET",
                                References.Bluetooth.getOrNull,
                                interfaceName,
                            )
                            .addStatement("%T($IT${needsFormatter.functionArgument})", className)
                            .endControlFlow()
                            .build(),
                    )
                    .build(),
            )
            .build()

    override fun generateSimulated(nested: List<TypeSpec>): TypeSpec {
        val className = nameFor(declaration, GenerationType.CLIENT_SIMULATOR)
        val interfaceName = nameFor(declaration, GenerationType.CLIENT_API)
        val properties = declarations.filterIsInstance<KSPropertyDeclaration>()
        val readProperty = properties.firstOrNull { it.isReadable }
        val writeProperty = properties.firstOrNull { it.isWritable }
        val notifiableProperty = properties.firstOrNull { it.isNotifiable }

        return TypeSpec.classBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        listOfNotNull(
                            readProperty?.let { onRead ->
                                ParameterSpec(
                                    "${onRead.onReadMethodName}$ACTION",
                                    LambdaTypeName.get(
                                        returnType = BluetoothResultTypeBuilder(declaration, onRead, options, logger).responseClassName,
                                    ).copy(suspending = true),
                                )
                            },
                            writeProperty?.let { onWrite ->
                                ParameterSpec(
                                    "${onWrite.onWriteMethodName}$ACTION",
                                    LambdaTypeName.get(
                                        parameters = listOf(ParameterSpec(onWrite.simpleName.asString(), onWrite.attributeValue(options).typeName)),
                                        returnType = References.Bluetooth.writeResponse,
                                    ).copy(suspending = true),
                                )
                            },
                            notifiableProperty?.let { notifiable ->
                                ParameterSpec(
                                    notifiable.simpleName.asString(),
                                    References.KotlinX.Coroutines.Flow.flow.parameterizedBy(notifiable.attributeValue(options).typeName),
                                )
                            },
                        ) + properties.mapNotNull { propertyDeclaration ->
                            val typeDeclaration = propertyDeclaration.type.resolve().declaration
                            if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)) {
                                ParameterSpec(
                                    propertyDeclaration.simpleName.asString(),
                                    nameFor(typeDeclaration, GenerationType.CLIENT_SIMULATOR).nullIfPropertyIsNull(propertyDeclaration),
                                )
                            } else {
                                null
                            }
                        },
                    )
                    .build(),
            )
            .addSuperinterface(interfaceName)
            .addProperties(
                listOfNotNull(
                    readProperty?.let { onRead ->
                        PropertySpec.builder(
                            "${onRead.onReadMethodName}$ACTION",
                            LambdaTypeName.get(
                                returnType = BluetoothResultTypeBuilder(declaration, onRead, options, logger).responseClassName,
                            ).copy(suspending = true),
                        )
                            .addModifiers(KModifier.PRIVATE)
                            .initializer("${onRead.onReadMethodName}$ACTION").build()
                    },
                    writeProperty?.let { onWrite ->
                        PropertySpec.builder(
                            "${onWrite.onWriteMethodName}$ACTION",
                            LambdaTypeName.get(
                                parameters = listOf(ParameterSpec(onWrite.simpleName.asString(), onWrite.attributeValue(options).typeName)),
                                returnType = References.Bluetooth.writeResponse,
                            ).copy(suspending = true),
                        )
                            .addModifiers(KModifier.PRIVATE)
                            .initializer("${onWrite.onWriteMethodName}$ACTION").build()
                    },
                    // Backing field that stores the raw notifiable flow from the constructor.
                    notifiableProperty?.let { notifiable ->
                        PropertySpec.builder(
                            "_${notifiable.simpleName.asString()}",
                            References.KotlinX.Coroutines.Flow.flow.parameterizedBy(notifiable.attributeValue(options).typeName),
                        )
                            .addModifiers(KModifier.PRIVATE)
                            .initializer(notifiable.simpleName.asString())
                            .build()
                    },
                    // Tracks whether the notifiable flow currently has active collectors.
                    notifiableProperty?.let {
                        PropertySpec.builder(
                            "_isNotifying",
                            References.KotlinX.Coroutines.Flow.mutableStateFlow.parameterizedBy(BOOLEAN),
                        )
                            .addModifiers(KModifier.PRIVATE)
                            .initializer("%T(false)", References.KotlinX.Coroutines.Flow.mutableStateFlow)
                            .build()
                    },
                ),
            )
            .addTypes(nested)
            .generateBody(declarations, GenerationType.Type.SIMULATOR)
            .build()
    }

    override fun generateMock(nested: List<TypeSpec>): TypeSpec = buildMock(GenerationType.Side.CLIENT, nested)

    private fun TypeSpec.Builder.generateBody(declarations: Sequence<KSDeclaration>, type: GenerationType.Type): TypeSpec.Builder = apply {
        var hasNotifiableProperty = false
        var hasReadMethod = false
        var hasWriteMethod = false
        declarations.filterIsInstance<KSPropertyDeclaration>().forEach { propertyDeclaration ->
            val typeDeclaration = propertyDeclaration.type.resolve().declaration
            if (propertyDeclaration.isReadable ||
                propertyDeclaration.isWritable ||
                propertyDeclaration.isNotifiable
            ) {
                if (propertyDeclaration.isReadable) {
                    if (!hasReadMethod) {
                        hasReadMethod = true
                        val responseType = propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }
                        val readMethod = "$READ$responseType"
                        val resultType = BluetoothResultTypeBuilder(declaration, propertyDeclaration, options, logger)
                        addFunction(
                            generateReadMethod(readMethod, type, resultType, propertyDeclaration),
                        )
                    } else {
                        logOnlyOneProperty(Readable::class)
                    }
                }

                if (propertyDeclaration.isWritable) {
                    if (!hasWriteMethod) {
                        hasWriteMethod = true
                        generateWriteMethods(propertyDeclaration, type).forEach { addFunction(it) }
                    } else {
                        logOnlyOneProperty(Writable::class, WritableWithoutResponse::class, WritableSigned::class)
                    }
                }

                if (propertyDeclaration.isNotifiable) {
                    if (!hasNotifiableProperty) {
                        hasNotifiableProperty = true
                        generateNotifiableBytesProperty(propertyDeclaration, type)?.let { addProperty(it) }
                        generateNotifiableProperty(propertyDeclaration, type)?.let { addProperty(it) }
                        addProperty(generateIsNotifyingProperty(type))
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

    private fun generateReadMethod(readMethod: String, type: GenerationType.Type, resultType: BluetoothResultTypeBuilder, propertyDeclaration: KSPropertyDeclaration): FunSpec =
        FunSpec.builder(readMethod).addModifiers(KModifier.SUSPEND, *type.additionalModifiers.toTypedArray()).returns(
            resultType.responseClassName,
        ).apply {
            when (type) {
                GenerationType.Type.API, GenerationType.Type.MOCK -> {}

                GenerationType.Type.BLUETOOTH -> {
                    resultType.generateBluetoothResult(this, CHARACTERISTIC)
                }

                GenerationType.Type.SIMULATOR -> {
                    addStatement("$RETURN ${propertyDeclaration.onReadMethodName}$ACTION()")
                }
            }
        }.build()

    private data class WriteVariant(val methodSuffix: String, val writeType: MemberName?)

    private fun KSPropertyDeclaration.writeVariants(): List<WriteVariant> = buildList {
        if (isAnnotationPresent(Writable::class)) add(WriteVariant("", References.Bluetooth.writeTypeWithResponse))
        if (isAnnotationPresent(WritableWithoutResponse::class)) add(WriteVariant("WithoutResponse", References.Bluetooth.writeTypeWithoutResponse))
        if (isAnnotationPresent(WritableSigned::class)) add(WriteVariant("Signed", null))
    }

    private fun generateWriteMethods(propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): List<FunSpec> {
        val variants = propertyDeclaration.writeVariants()
        val propertyName = propertyDeclaration.simpleName.asString().replaceFirstChar { it.uppercase() }
        return variants.map { variant ->
            val suffix = if (variants.size == 1) "" else variant.methodSuffix
            generateWriteMethod("$WRITE$propertyName$suffix", propertyDeclaration, type, variant.writeType)
        }
    }

    private fun generateWriteMethod(writeMethod: String, propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type, writeType: MemberName?): FunSpec = FunSpec.builder(
        writeMethod,
    ).addParameter(
        propertyDeclaration.simpleName.asString(),
        propertyDeclaration.attributeValue(options).typeName,
    ).addModifiers(KModifier.SUSPEND, *type.additionalModifiers.toTypedArray()).returns(
        References.Bluetooth.writeResponse,
    ).apply {
        when (type) {
            GenerationType.Type.API, GenerationType.Type.MOCK -> {}

            GenerationType.Type.BLUETOOTH -> {
                val valueArgument = propertyDeclaration.attributeValue(options).encode(CodeBlock.of("%N", propertyDeclaration.simpleName.asString()))
                if (writeType != null) {
                    addStatement("$RETURN $CHARACTERISTIC.$WRITE(%L, %M)", valueArgument, writeType)
                } else {
                    addStatement("$RETURN $CHARACTERISTIC.$WRITE(%L)", valueArgument)
                }
            }

            GenerationType.Type.SIMULATOR -> {
                addStatement("$RETURN ${propertyDeclaration.onWriteMethodName}$ACTION(${propertyDeclaration.simpleName.asString()})")
            }
        }
    }.build()

    /**
     * The notifications of [propertyDeclaration] as its value. A value read through a view is derived from the undecoded
     * notifications of [generateNotifiableBytesProperty] by the API, so an implementation provides only those.
     */
    private fun generateNotifiableProperty(propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): PropertySpec? {
        val value = propertyDeclaration.attributeValue(options)
        val name = propertyDeclaration.simpleName.asString()
        val builder = PropertySpec.builder(name, References.KotlinX.Coroutines.Flow.flow.parameterizedBy(value.typeName))
        if (value is AttributeValue.View) {
            if (type != GenerationType.Type.API) return null
            return builder
                .getter(FunSpec.getterBuilder().addStatement("$RETURN %N$BYTES.%M { %L }", name, References.KotlinX.Coroutines.Flow.map, value.decode(CodeBlock.of(IT))).build())
                .build()
        }
        return builder.addModifiers(*type.additionalModifiers.toTypedArray())
            .apply {
                when (type) {
                    GenerationType.Type.API, GenerationType.Type.MOCK -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        getter(
                            FunSpec.getterBuilder()
                                .apply {
                                    val member = References.Bluetooth.value
                                    when (value) {
                                        AttributeValue.Bytes -> addStatement("$RETURN $CHARACTERISTIC.%M()", member)
                                        is AttributeValue.Serialized -> addStatement("$RETURN $CHARACTERISTIC.%M(%L, $FORMAT)", member, value.serializer)
                                        is AttributeValue.View -> error("A view is derived from the bytes")
                                    }
                                }
                                .build(),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> getter(simulatorNotifyingGetter(CodeBlock.of("_%N", name)))
                }
            }.build()
    }

    /**
     * The undecoded notifications of a [propertyDeclaration] read through a view: the one flow an implementation
     * provides, which [generateNotifiableProperty] wraps and [collectFunction] collects without boxing.
     */
    private fun generateNotifiableBytesProperty(propertyDeclaration: KSPropertyDeclaration, type: GenerationType.Type): PropertySpec? {
        val value = propertyDeclaration.attributeValue(options) as? AttributeValue.View ?: return null
        val name = propertyDeclaration.simpleName.asString()
        return PropertySpec.builder("$name$BYTES", References.KotlinX.Coroutines.Flow.flow.parameterizedBy(BYTE_ARRAY))
            .addModifiers(*type.additionalModifiers.toTypedArray())
            .apply {
                when (type) {
                    GenerationType.Type.API, GenerationType.Type.MOCK -> {}

                    GenerationType.Type.BLUETOOTH -> getter(FunSpec.getterBuilder().addStatement("$RETURN $CHARACTERISTIC.%M()", References.Bluetooth.value).build())

                    GenerationType.Type.SIMULATOR -> getter(
                        simulatorNotifyingGetter(CodeBlock.of("_%N.%M { %L }", name, References.KotlinX.Coroutines.Flow.map, value.encode(CodeBlock.of(IT)))),
                    )
                }
            }
            .build()
    }

    /** Hands out the simulator's [backing] flow, setting `_isNotifying` while it has a collector. */
    private fun simulatorNotifyingGetter(backing: CodeBlock): FunSpec = FunSpec.getterBuilder()
        .addStatement(
            "$RETURN %L.%M { _isNotifying.value = true }.%M { _isNotifying.value = false }",
            backing,
            References.KotlinX.Coroutines.Flow.onStart,
            References.KotlinX.Coroutines.Flow.onCompletion,
        )
        .build()

    override fun topLevelFunctions(generationType: GenerationType): List<FunSpec> = listOfNotNull(
        declarations.filterIsInstance<KSPropertyDeclaration>().firstOrNull { it.isNotifiable }?.takeIf { generationType == GenerationType.CLIENT_API }?.let(::collectFunction),
    )

    /**
     * Collects the notifications of a [notifiable] read through a view without boxing one per notification, which
     * collecting its `Flow` of views does: the view is created inside the inlined action, from the undecoded bytes.
     */
    private fun collectFunction(notifiable: KSPropertyDeclaration): FunSpec? {
        val view = notifiable.attributeValue(options) as? AttributeValue.View ?: return null
        val name = notifiable.simpleName.asString()
        return FunSpec.builder("$COLLECT${name.replaceFirstChar { it.uppercase() }}")
            .addKdoc("Collects [%N] like `%N.collect(action)` does, without allocating a [%T] per notification.", name, name, view.typeName)
            .addModifiers(KModifier.SUSPEND, KModifier.INLINE)
            .receiver(nameFor(declaration, GenerationType.CLIENT_API))
            .addParameter(
                ParameterSpec.builder(ACTION_PARAMETER, LambdaTypeName.get(parameters = arrayOf(view.typeName), returnType = UNIT).copy(suspending = true))
                    .addModifiers(KModifier.CROSSINLINE)
                    .build(),
            )
            .addStatement("%N$BYTES.collect { $ACTION_PARAMETER(%L) }", name, view.decode(CodeBlock.of(IT)))
            .build()
    }

    private fun generateIsNotifyingProperty(type: GenerationType.Type): PropertySpec = PropertySpec.builder(
        "isNotifying",
        References.KotlinX.Coroutines.Flow.flow.parameterizedBy(BOOLEAN),
    ).addModifiers(*type.additionalModifiers.toTypedArray())
        .apply {
            when (type) {
                GenerationType.Type.API, GenerationType.Type.MOCK -> {}

                GenerationType.Type.BLUETOOTH -> {
                    getter(
                        FunSpec.getterBuilder()
                            .addStatement("$RETURN $CHARACTERISTIC.isNotifying")
                            .build(),
                    )
                }

                GenerationType.Type.SIMULATOR -> {
                    getter(
                        FunSpec.getterBuilder()
                            .addStatement("$RETURN _isNotifying")
                            .build(),
                    )
                }
            }
        }.build()

    private fun generateDescriptorProperty(propertyDeclaration: KSPropertyDeclaration, typeDeclaration: KSClassDeclaration, type: GenerationType.Type): PropertySpec {
        val propertyType = clientName(typeDeclaration, type).nullIfPropertyIsNull(propertyDeclaration)
        val formatArg = NeedsFormatterHelper.needsBluetoothFormatter(typeDeclaration, options).functionArgument
        return PropertySpec.builder(
            propertyDeclaration.simpleName.asString(),
            propertyType,
        ).addModifiers(
            *type.additionalModifiers.toTypedArray(),
        )
            .apply {
                when (type) {
                    GenerationType.Type.API, GenerationType.Type.MOCK -> {}

                    GenerationType.Type.BLUETOOTH -> {
                        delegate(
                            "$LAZY { %T.$FROM_CHARACTERISTIC${propertyDeclaration.orNullIfNullable}($CHARACTERISTIC$formatArg) }",
                            clientName(typeDeclaration, type),
                        )
                    }

                    GenerationType.Type.SIMULATOR -> {
                        initializer(propertyDeclaration.simpleName.asString())
                    }
                }
            }.build()
    }
}
