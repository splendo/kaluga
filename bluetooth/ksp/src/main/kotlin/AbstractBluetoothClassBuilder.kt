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
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
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
        }
    }

    abstract fun generateAPI(nested: List<TypeSpec>): TypeSpec
    abstract fun generateBluetooth(nested: List<TypeSpec>): TypeSpec
    abstract fun generateSimulated(nested: List<TypeSpec>): TypeSpec

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
