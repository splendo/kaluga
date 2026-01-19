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
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothClient
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothServer
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

internal abstract class AbstractBluetoothClassBuilder(val declaration: KSClassDeclaration, val logger: KSPLogger) {

    fun generate(generationType: GenerationType): TypeSpec = with(declaration) {
        val nested = generateNested(generationType)
        when (generationType.type) {
            GenerationType.Type.API -> generateAPI(generationType, nested)
            GenerationType.Type.BLUETOOTH -> generateBluetooth(generationType, nested)
            GenerationType.Type.SIMULATOR -> generateSimulated(generationType, nested)
        }
    }

    abstract fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec
    abstract fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec
    abstract fun KSClassDeclaration.generateSimulated(generationType: GenerationType, nested: List<TypeSpec>): TypeSpec

    protected fun KSClassDeclaration.generateNested(generationType: GenerationType): List<TypeSpec> = buildList {
        val bluetoothDeclarations = declarations.filter { it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
        bluetoothDeclarations.forEach { bluetoothDeclaration ->
            if (generationType.side == GenerationType.Side.CLIENT) {
                add(BluetoothClientBuilder(bluetoothDeclaration, logger).generate(generationType))
            }
            if (generationType.side == GenerationType.Side.SERVER) {
                add(BluetoothServerBuilder(bluetoothDeclaration, logger).generate(generationType))
            }
        }

        if (generationType.side == GenerationType.Side.CLIENT) {
            val clientDeclarations =
                declarations.filter { it.isAnnotationPresent(BluetoothClient::class) && !it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
            addAll(
                clientDeclarations.map { clientDeclaration ->
                    BluetoothClientBuilder(clientDeclaration, logger).generate(generationType)
                },
            )
        }

        if (generationType.side == GenerationType.Side.SERVER) {
            val serverDeclarations =
                declarations.filter { it.isAnnotationPresent(BluetoothServer::class) && !it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
            addAll(
                serverDeclarations.map { serverDeclaration ->
                    BluetoothServerBuilder(serverDeclaration, logger).generate(generationType)
                },
            )
        }

        val serviceDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothService::class) }.filterIsInstance<KSClassDeclaration>()
        serviceDeclarations.forEach { serviceDeclaration ->
            val service = serviceDeclaration.getAnnotationsByType(BluetoothService::class).first()

            if (generationType.side == GenerationType.Side.CLIENT) {
                add(BluetoothRemoteServiceBuilder(serviceDeclaration, service, logger).generate(generationType))
            }
            if (generationType.side == GenerationType.Side.SERVER) {
                add(BluetoothLocalServiceBuilder(serviceDeclaration, service, logger).generate(generationType))
            }
        }

        val characteristicDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothCharacteristic::class) }.filterIsInstance<KSClassDeclaration>()
        characteristicDeclarations.forEach { characteristicDeclaration ->
            val characteristic = characteristicDeclaration.getAnnotationsByType(BluetoothCharacteristic::class).first()
            if (generationType.side == GenerationType.Side.CLIENT) {
                if (generationType.type == GenerationType.Type.API) {
                    BluetoothResultTypeBuilder.fromClassDeclaration(characteristicDeclaration, logger)?.generateType()?.let {
                        add(it)
                    }
                }
                add(BluetoothRemoteCharacteristicBuilder(characteristicDeclaration, characteristic, logger).generate(generationType))
            }
            if (generationType.side == GenerationType.Side.SERVER) {
                add(BluetoothLocalCharacteristicBuilder(characteristicDeclaration, characteristic, logger).generate(generationType))
            }
        }

        val descriptorDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothDescriptor::class) }.filterIsInstance<KSClassDeclaration>()
        descriptorDeclarations.forEach { descriptorDeclaration ->
            val descriptor = descriptorDeclaration.getAnnotationsByType(BluetoothDescriptor::class).first()
            if (generationType.side == GenerationType.Side.CLIENT) {
                if (generationType.type == GenerationType.Type.API) {
                    BluetoothResultTypeBuilder.fromClassDeclaration(descriptorDeclaration, logger)?.generateType()?.let {
                        add(it)
                    }
                }
                add(BluetoothRemoteDescriptorBuilder(descriptorDeclaration, descriptor, logger).generate(generationType))
            }
            if (generationType.side == GenerationType.Side.SERVER) {
                add(BluetoothLocalDescriptorBuilder(descriptorDeclaration, descriptor, logger).generate(generationType))
            }
        }
    }

    protected val GenerationType.additionalModifiers: List<KModifier> get() = listOfNotNull(
        KModifier.ABSTRACT.takeIf { type == GenerationType.Type.API },
        KModifier.OVERRIDE.takeIf { type != GenerationType.Type.API },
    )
}
