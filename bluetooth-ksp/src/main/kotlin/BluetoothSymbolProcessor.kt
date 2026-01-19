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
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothClient
import com.splendo.kaluga.bluetooth.annotations.BluetoothClientName
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothServer
import com.splendo.kaluga.bluetooth.annotations.BluetoothServerName
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

class BluetoothSymbolProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("------ PROCESSING ----")
        val bluetoothDeclarations = resolver.getSymbolsWithAnnotation(Bluetooth::class.java.name).filterIsInstance<KSClassDeclaration>().filter { it.parentDeclaration == null }
        bluetoothDeclarations.forEach { bluetoothDeclaration ->
            bluetoothDeclaration.generateBluetoothClientFile()
            bluetoothDeclaration.generateBluetoothServerFile()
        }
        val clientDeclarations = resolver.getSymbolsWithAnnotation(BluetoothClient::class.java.name).filter {
            !it.isAnnotationPresent(Bluetooth::class)
        }.filterIsInstance<KSClassDeclaration>().filter {
            it.parentDeclaration ==
                null
        }
        clientDeclarations.forEach { clientDeclaration ->
            clientDeclaration.generateBluetoothClientFile()
        }
        val serverDeclarations = resolver.getSymbolsWithAnnotation(BluetoothServer::class.java.name).filter {
            !it.isAnnotationPresent(Bluetooth::class)
        }.filterIsInstance<KSClassDeclaration>().filter {
            it.parentDeclaration ==
                null
        }
        serverDeclarations.forEach { serverDeclaration ->
            serverDeclaration.generateBluetoothServerFile()
        }
        val serviceDeclarations = resolver.getSymbolsWithAnnotation(BluetoothService::class.java.name).filterIsInstance<KSClassDeclaration>().filter {
            it.parentDeclaration == null
        }
        serviceDeclarations.forEach { serviceDeclaration ->
            serviceDeclaration.generateBluetoothServiceFile(serviceDeclaration.getAnnotationsByType(BluetoothService::class).first())
        }
        val characteristicDeclarations = resolver.getSymbolsWithAnnotation(BluetoothCharacteristic::class.java.name).filterIsInstance<KSClassDeclaration>().filter {
            it.parentDeclaration ==
                null
        }
        characteristicDeclarations.forEach { characteristicDeclaration ->
            characteristicDeclaration.generateBluetoothCharacteristicFile(characteristicDeclaration.getAnnotationsByType(BluetoothCharacteristic::class).first())
        }
        val descriptorDeclarations = resolver.getSymbolsWithAnnotation(BluetoothDescriptor::class.java.name).filterIsInstance<KSClassDeclaration>().filter {
            it.parentDeclaration ==
                null
        }
        descriptorDeclarations.forEach { descriptorDeclaration ->
            descriptorDeclaration.generateBluetoothDescriptorFile(descriptorDeclaration.getAnnotationsByType(BluetoothDescriptor::class).first())
        }
        logger.warn("------ DONE ----")
        return emptyList()
    }

    private fun KSClassDeclaration.generateBluetoothClientFile() {
        val clientClass = ClassName(packageName.asString(), clientName())
        val generated = listOf(
            BluetoothClientBuilder(this, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.API)),
            BluetoothClientBuilder(this, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.BLUETOOTH)),
            BluetoothClientBuilder(this, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.SIMULATOR)),
        )
        FileSpec.builder(clientClass).generate(generated)
    }

    private fun KSClassDeclaration.generateBluetoothServerFile() {
        val serverClass = ClassName(packageName.asString(), serverName())
        val generated = listOf(
            BluetoothServerBuilder(this, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.API)),
            BluetoothServerBuilder(this, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.BLUETOOTH)),
            BluetoothServerBuilder(this, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.SIMULATOR)),
        )
        FileSpec.builder(serverClass).generate(generated)
    }

    private fun KSClassDeclaration.generateBluetoothServiceFile(service: BluetoothService) {
        val serviceClass = ClassName(packageName.asString(), clientName(prefix = "RemoteAndLocal", postFix = ""))
        val generated = listOf(
            BluetoothRemoteServiceBuilder(this, service, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.API)),
            BluetoothLocalServiceBuilder(this, service, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.API)),
            BluetoothRemoteServiceBuilder(this, service, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.BLUETOOTH)),
            BluetoothLocalServiceBuilder(this, service, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.BLUETOOTH)),
            BluetoothRemoteServiceBuilder(this, service, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.SIMULATOR)),
            BluetoothLocalServiceBuilder(this, service, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.SIMULATOR)),
        )

        FileSpec.builder(serviceClass).generate(generated)
    }

    private fun KSClassDeclaration.generateBluetoothCharacteristicFile(characteristic: BluetoothCharacteristic) {
        val characteristicClass = ClassName(packageName.asString(), clientName(prefix = "RemoteAndLocal", postFix = ""))
        val generated = listOfNotNull(
            BluetoothResultTypeBuilder.fromClassDeclaration(this, logger)?.generateType(),
            BluetoothRemoteCharacteristicBuilder(this, characteristic, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.API)),
            BluetoothLocalCharacteristicBuilder(this, characteristic, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.API)),
            BluetoothRemoteCharacteristicBuilder(this, characteristic, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.BLUETOOTH)),
            BluetoothLocalCharacteristicBuilder(this, characteristic, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.BLUETOOTH)),
            BluetoothRemoteCharacteristicBuilder(this, characteristic, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.SIMULATOR)),
            BluetoothLocalCharacteristicBuilder(this, characteristic, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.SIMULATOR)),
        )

        FileSpec.builder(characteristicClass).generate(generated)
    }

    private fun KSClassDeclaration.generateBluetoothDescriptorFile(descriptor: BluetoothDescriptor) {
        val descriptorClass = ClassName(packageName.asString(), clientName(prefix = "RemoteAndLocal", postFix = ""))
        val generated = listOfNotNull(
            BluetoothResultTypeBuilder.fromClassDeclaration(this, logger)?.generateType(),
            BluetoothRemoteDescriptorBuilder(this, descriptor, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.API)),
            BluetoothLocalDescriptorBuilder(this, descriptor, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.API)),
            BluetoothRemoteDescriptorBuilder(this, descriptor, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.BLUETOOTH)),
            BluetoothLocalDescriptorBuilder(this, descriptor, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.BLUETOOTH)),
            BluetoothRemoteDescriptorBuilder(this, descriptor, logger).generate(GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.SIMULATOR)),
            BluetoothLocalDescriptorBuilder(this, descriptor, logger).generate(GenerationType(GenerationType.Side.SERVER, GenerationType.Type.SIMULATOR)),
        )

        FileSpec.builder(descriptorClass).generate(generated)
    }

    private fun FileSpec.Builder.generate(typeSpecs: List<TypeSpec>) = apply {
        addTypes(typeSpecs)
    }.indent("    ").build().writeTo(codeGenerator, Dependencies.ALL_FILES)

    private fun KSClassDeclaration.clientName(prefix: String = "", postFix: String = "Client") =
        getAnnotationsByType(BluetoothClientName::class).firstOrNull()?.name ?: "$prefix${simpleName.asString()}$postFix"
    private fun KSClassDeclaration.serverName(prefix: String = "", postFix: String = "Server") =
        getAnnotationsByType(BluetoothServerName::class).firstOrNull()?.name ?: "$prefix${simpleName.asString()}$postFix"
}

class BluetoothSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = BluetoothSymbolProcessor(environment)
}
