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
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.ksp.helpers.COMPANION
import com.splendo.kaluga.bluetooth.ksp.helpers.FACTORY
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.nullIfPropertyIsNull
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import kotlin.reflect.KClass

internal abstract class AbstractBluetoothClassBuilder(val declaration: KSClassDeclaration, val options: Options, val logger: KSPLogger) {

    val declarations get() = declaration.declarations

    protected fun nameFor(declaration: KSClassDeclaration, generationType: GenerationType): ClassName = NameHelper.nameFor(declaration, generationType, options)
    protected fun clientName(declaration: KSClassDeclaration, type: GenerationType.Type): ClassName = NameHelper.clientName(declaration, type, options)
    protected fun serverName(declaration: KSClassDeclaration, type: GenerationType.Type): ClassName = NameHelper.serverName(declaration, type, options)

    fun generate(generationType: GenerationType): TypeSpec = with(declaration) {
        val nested = generateNested(generationType)
        when (generationType.type) {
            GenerationType.Type.API -> generateAPI(nested)
            GenerationType.Type.BLUETOOTH -> generateBluetooth(nested)
            GenerationType.Type.SIMULATOR -> generateSimulated(nested)
            GenerationType.Type.MOCK -> generateMock(nested)
        }
    }

    abstract fun generateAPI(nested: List<TypeSpec>): TypeSpec
    abstract fun generateBluetooth(nested: List<TypeSpec>): TypeSpec
    abstract fun generateSimulated(nested: List<TypeSpec>): TypeSpec
    abstract fun generateMock(nested: List<TypeSpec>): TypeSpec

    /**
     * Builds a `base:test` mock-backed test double for [side]. Every function member of the generated API interface is
     * backed by a Kaluga mock (`::member.mock()`), with its override delegating to `memberMock.call(...)` so consumers can
     * stub via `.on()` and assert with `verify()`. Properties whose type is a nested generated interface are exposed as a
     * constructor parameter defaulting to a fresh child mock; all other (leaf) properties become a public settable backing.
     *
     * @param nested the already-generated child mock [TypeSpec]s to nest inside the mock.
     * @param additionalFunctions functions inherited from a superinterface (e.g. [AutoCloseable.close]) that are not
     * declared on the generated API interface but must still be mocked and overridden.
     */
    protected fun buildMock(side: GenerationType.Side, nested: List<TypeSpec>, additionalFunctions: List<FunSpec> = emptyList()): TypeSpec {
        val mockGenerationType = if (side == GenerationType.Side.CLIENT) GenerationType.CLIENT_MOCK else GenerationType.SERVER_MOCK
        fun nameForType(typeDeclaration: KSClassDeclaration): ClassName =
            if (side == GenerationType.Side.CLIENT) clientName(typeDeclaration, GenerationType.Type.MOCK) else serverName(typeDeclaration, GenerationType.Type.MOCK)

        val generationType = if (side == GenerationType.Side.CLIENT) GenerationType.CLIENT_API else GenerationType.SERVER_API
        val apiInterface = generate(generationType)
        val apiName = nameFor(declaration, generationType)
        val mockName = nameFor(declaration, mockGenerationType)

        // Classify each generated property as either a nested generated interface (child mock) or a leaf.
        val nestedProperties = declaration.declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
            val typeDeclaration = propertyDeclaration.type.resolve().declaration
            if (typeDeclaration is KSClassDeclaration &&
                (
                    typeDeclaration.isAnnotationPresent(Bluetooth::class) ||
                        typeDeclaration.isAnnotationPresent(BluetoothService::class) ||
                        typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class) ||
                        typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)
                    )
            ) {
                propertyDeclaration.simpleName.asString() to nameForType(typeDeclaration).nullIfPropertyIsNull(propertyDeclaration)
            } else {
                null
            }
        }.toMap()

        // Every generated property is exposed as a constructor-injectable backing:
        //  - a nested generated interface → a child mock (defaulting to a fresh child mock),
        //  - a leaf Flow → a MutableSharedFlow the test can drive (narrowing the API's read-only `Flow`).
        val constructorParameters = mutableListOf<ParameterSpec>()
        val overrideProperties = mutableListOf<PropertySpec>()
        apiInterface.propertySpecs.forEach { property ->
            val childMockType = nestedProperties[property.name]
            if (childMockType != null) {
                constructorParameters += ParameterSpec.builder(property.name, childMockType)
                    .defaultValue("%T()", childMockType.copy(nullable = false))
                    .build()
                overrideProperties += PropertySpec.builder(property.name, childMockType, KModifier.OVERRIDE)
                    .initializer(property.name)
                    .build()
            } else {
                val backingType = mutableSharedFlowBacking(property)
                constructorParameters += ParameterSpec.builder(property.name, backingType)
                    .defaultValue("%T()", backingType.rawType)
                    .build()
                overrideProperties += PropertySpec.builder(property.name, backingType, KModifier.OVERRIDE)
                    .initializer(property.name)
                    .build()
            }
        }

        val typeBuilder = TypeSpec.classBuilder(mockName)
            .apply { if (constructorParameters.isNotEmpty()) primaryConstructor(FunSpec.constructorBuilder().addParameters(constructorParameters).build()) }
            .addSuperinterface(apiName)
            .addTypes(nested)
            .addProperties(overrideProperties)

        // Functions: each backed by a Kaluga mock; the override delegates to `memberMock.call(...)`.
        (apiInterface.funSpecs.filterNot { it.isConstructor } + additionalFunctions).forEach { function ->
            val mockPropertyName = "${function.name}Mock"
            val suspended = KModifier.SUSPEND in function.modifiers
            val mockTypeArguments = function.parameters.map { it.type } + function.returnType.orUnit()
            val mockType = References.Base.Test.methodMock(function.parameters.size, suspended).parameterizedBy(mockTypeArguments)
            typeBuilder.addProperty(
                PropertySpec.builder(mockPropertyName, mockType)
                    .initializer("::%N.%M()", function.name, References.Base.Test.Parameters.mock)
                    .build(),
            )
            val arguments = function.parameters.joinToString(separator = ", ") { it.name }
            typeBuilder.addFunction(
                function.toBuilder()
                    .apply { modifiers.remove(KModifier.ABSTRACT) }
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("return %N.%M($arguments)", mockPropertyName, References.Base.Test.call)
                    .build(),
            )
        }

        return typeBuilder.build()
    }

    /**
     * The `MutableSharedFlow` backing type for a leaf Flow property: `Flow<E>` → `MutableSharedFlow<E>`. The override narrows
     * the API's read-only `Flow` to a mutable shared flow so tests can drive emissions, while the constructor parameter keeps
     * it injectable. A shared flow needs no initial value, so the parameter always defaults to an empty `MutableSharedFlow()`.
     */
    private fun mutableSharedFlowBacking(property: PropertySpec): ParameterizedTypeName {
        val type = property.type
        val elementTypes = if (type is ParameterizedTypeName && type.rawType == References.KotlinX.Coroutines.Flow.flow) {
            type.typeArguments
        } else {
            logger.error("Cannot generate a mock backing for leaf property ${property.name} of type $type; expected a Flow")
            listOf(type)
        }
        return References.KotlinX.Coroutines.Flow.mutableSharedFlow.parameterizedBy(elementTypes)
    }

    private fun TypeName?.orUnit(): TypeName = this ?: UNIT

    /**
     * The top-level creator function (an extension on the API's `Companion`) for the given implementation type,
     * or `null` if there is none. Generated alongside the implementation so it lives in the implementation module
     * even when the API is generated separately (see [Options.generateApi]).
     */
    open fun factoryFor(generationType: GenerationType): FunSpec? = null

    protected val hasNestedDevices: Boolean get() = declaration.declarations.any { it.isAnnotationPresent(Bluetooth::class) }

    /** Whether the API's companion object is given an explicit name (so it stays referenceable as a creator-extension receiver beside other nested types). */
    protected open val needsNamedCompanion: Boolean get() = hasNestedDevices

    private val companionName: String get() = if (needsNamedCompanion) FACTORY else COMPANION

    /** The API's companion object, the receiver for the creator extensions. */
    protected fun companionObject(): TypeSpec = TypeSpec.companionObjectBuilder(FACTORY.takeIf { needsNamedCompanion }).build()

    /** The companion object of the API of [apiType], the receiver for the creator extensions. */
    protected fun companionReceiver(apiType: GenerationType): ClassName = nameFor(declaration, apiType).nestedClass(companionName)

    fun generateExtensionFactories(generationType: GenerationType): List<FunSpec> = buildList {
        factoryFor(generationType)?.let(::add)
        declaration.declarations.filter { it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>().forEach { nested ->
            val nestedBuilder = when (generationType.side) {
                GenerationType.Side.CLIENT -> BluetoothClientBuilder(nested, options, logger)
                GenerationType.Side.SERVER -> BluetoothServerBuilder(nested, options, logger)
            }
            addAll(nestedBuilder.generateExtensionFactories(generationType))
        }
    }

    protected fun generateNested(generationType: GenerationType): List<TypeSpec> = buildList {
        val bluetoothDeclarations = declaration.declarations.filter { it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
        bluetoothDeclarations.forEach { bluetoothDeclaration ->
            when (generationType.side) {
                GenerationType.Side.CLIENT -> {
                    add(BluetoothClientBuilder(bluetoothDeclaration, options, logger).generate(generationType))
                }

                GenerationType.Side.SERVER -> {
                    add(BluetoothServerBuilder(bluetoothDeclaration, options, logger).generate(generationType))
                }
            }
        }

        val serviceDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothService::class) }.filterIsInstance<KSClassDeclaration>()
        serviceDeclarations.forEach { serviceDeclaration ->
            val service = serviceDeclaration.getAnnotationsByType(BluetoothService::class).first()
            when (generationType.side) {
                GenerationType.Side.CLIENT -> {
                    add(BluetoothRemoteServiceBuilder(serviceDeclaration, service, options, logger).generate(generationType))
                }

                GenerationType.Side.SERVER -> {
                    add(BluetoothLocalServiceBuilder(serviceDeclaration, service, options, logger).generate(generationType))
                }
            }
        }

        val characteristicDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothCharacteristic::class) }.filterIsInstance<KSClassDeclaration>()
        characteristicDeclarations.forEach { characteristicDeclaration ->
            val characteristic = characteristicDeclaration.getAnnotationsByType(BluetoothCharacteristic::class).first()
            if (generationType.type == GenerationType.Type.API && (generationType.side == GenerationType.Side.CLIENT || !options.generateClient)) {
                BluetoothResultTypeBuilder.fromClassDeclaration(characteristicDeclaration, options, logger)?.generateType()?.let {
                    add(it)
                }
            }
            when (generationType.side) {
                GenerationType.Side.CLIENT -> {
                    add(BluetoothRemoteCharacteristicBuilder(characteristicDeclaration, characteristic, options, logger).generate(generationType))
                }

                GenerationType.Side.SERVER -> {
                    add(BluetoothLocalCharacteristicBuilder(characteristicDeclaration, characteristic, options, logger).generate(generationType))
                }
            }
        }

        val descriptorDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothDescriptor::class) }.filterIsInstance<KSClassDeclaration>()
        descriptorDeclarations.forEach { descriptorDeclaration ->
            val descriptor = descriptorDeclaration.getAnnotationsByType(BluetoothDescriptor::class).first()
            if (generationType.type == GenerationType.Type.API && (generationType.side == GenerationType.Side.CLIENT || !options.generateClient)) {
                BluetoothResultTypeBuilder.fromClassDeclaration(descriptorDeclaration, options, logger)?.generateType()?.let {
                    add(it)
                }
            }
            when (generationType.side) {
                GenerationType.Side.CLIENT -> {
                    add(BluetoothRemoteDescriptorBuilder(descriptorDeclaration, descriptor, options, logger).generate(generationType))
                }

                GenerationType.Side.SERVER -> {
                    add(BluetoothLocalDescriptorBuilder(descriptorDeclaration, descriptor, options, logger).generate(generationType))
                }
            }
        }
    }

    protected val GenerationType.Type.additionalModifiers: List<KModifier> get() = listOfNotNull(
        KModifier.ABSTRACT.takeIf { this == GenerationType.Type.API },
        KModifier.OVERRIDE.takeIf { this != GenerationType.Type.API },
    )

    protected fun logOnlyOneProperty(classOne: KClass<*>, vararg classes: KClass<*>) {
        logger.error("Only one ${(listOf(classOne) + classes.toList()).joinToString(separator = " / ") { "@${it.simpleName}" }} property can be declared")
    }

    protected fun invalidProperty(declaration: KSPropertyDeclaration, validClassOne: KClass<*>, vararg validClasses: KClass<*>) {
        val received = "Received ${declaration.simpleName} with annotations: ${declaration.annotations.joinToString { it.shortName.asString() }}"
        if (validClasses.isEmpty()) {
            logger.error("Only a @${validClassOne.simpleName} property can be declared. $received")
        } else {
            val last = validClasses.last()
            logger.error(
                "Only ${(listOf(validClassOne) + validClasses.dropLast(1)).joinToString {
                    "@${it.simpleName}"
                }}, and @${last.simpleName} properties can be declared. $received",
            )
        }
    }
}
