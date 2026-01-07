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
import com.splendo.kaluga.bluetooth.annotations.BluetoothClientName
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothServer
import com.splendo.kaluga.bluetooth.annotations.BluetoothServerName
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.squareup.kotlinpoet.Import
import com.squareup.kotlinpoet.TypeSpec
import kotlin.reflect.KClass

internal abstract class AbstractBluetoothClassBuilder(val declaration: KSClassDeclaration, val logger: KSPLogger) {

    data class Generated(val typeSpec: List<TypeSpec>)

    fun generate(generationType: GenerationType): Generated = with(declaration) {
        val nested = generateNested(generationType)
        val newGenerated = when (generationType.type) {
            GenerationType.Type.API -> generateAPI(generationType, nested.flatMap { it.typeSpec })
            GenerationType.Type.BLUETOOTH -> generateBluetooth(generationType, nested.flatMap { it.typeSpec })
            GenerationType.Type.SIMULATOR -> TODO()
        }
        Generated(newGenerated.typeSpec)
    }

    abstract fun KSClassDeclaration.generateAPI(generationType: GenerationType, nested: List<TypeSpec>): Generated
    abstract fun KSClassDeclaration.generateBluetooth(generationType: GenerationType, nested: List<TypeSpec>): Generated

    protected fun KSClassDeclaration.generateNested(generationType: GenerationType): List<Generated> = buildList {
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
            if (generationType.side == GenerationType.Side.CLIENT) {
                add(BluetoothRemoteServiceBuilder(serviceDeclaration, logger).generate(generationType))
            }
            if (generationType.side == GenerationType.Side.SERVER) {
                add(BluetoothLocalServiceBuilder(serviceDeclaration, logger).generate(generationType))
            }
        }

        val characteristicDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothCharacteristic::class) }.filterIsInstance<KSClassDeclaration>()
        characteristicDeclarations.forEach { characteristicDeclaration ->
            if (generationType.side == GenerationType.Side.CLIENT) {
                BluetoothResultTypeBuilder.fromClassDeclaration(characteristicDeclaration)?.let {
                    add(Generated(listOf(it.generateType())))
                }
                add(BluetoothRemoteCharacteristicBuilder(characteristicDeclaration, logger).generate(generationType))
            }
            if (generationType.side == GenerationType.Side.SERVER) {
                add(BluetoothLocalCharacteristicBuilder(characteristicDeclaration, logger).generate(generationType))
            }
        }

        val descriptorDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothDescriptor::class) }.filterIsInstance<KSClassDeclaration>()
        descriptorDeclarations.forEach { descriptorDeclaration ->
            if (generationType.side == GenerationType.Side.CLIENT) {
                BluetoothResultTypeBuilder.fromClassDeclaration(descriptorDeclaration)?.let {
                    add(Generated(listOf(it.generateType())))
                }
                add(BluetoothRemoteDescriptorBuilder(descriptorDeclaration, logger).generate(generationType))
            }
            if (generationType.side == GenerationType.Side.SERVER) {
                add(BluetoothLocalDescriptorBuilder(descriptorDeclaration, logger).generate(generationType))
            }
        }
    }
}
