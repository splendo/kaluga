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
        FileSpec.builder(clientClass).generate(
            GenerationType.Side.CLIENT,
            BluetoothClientBuilder(this, logger),
        )
    }

    private fun KSClassDeclaration.generateBluetoothServerFile() {
        val serverClass = ClassName(packageName.asString(), serverName())
        FileSpec.builder(serverClass).generate(
            GenerationType.Side.SERVER,
            BluetoothServerBuilder(this, logger),
        )
    }

    private fun KSClassDeclaration.generateBluetoothServiceFile(service: BluetoothService) {
        val remoteServiceClass = ClassName(packageName.asString(), clientName(prefix = "Remote", postFix = ""))
        FileSpec.builder(remoteServiceClass).generate(
            GenerationType.Side.CLIENT,
            BluetoothRemoteServiceBuilder(this, service, logger),
        )
        val localServiceClass = ClassName(packageName.asString(), serverName(prefix = "Local", postFix = ""))
        FileSpec.builder(localServiceClass).generate(
            GenerationType.Side.SERVER,
            BluetoothLocalServiceBuilder(this, service, logger),
        )
    }

    private fun KSClassDeclaration.generateBluetoothCharacteristicFile(characteristic: BluetoothCharacteristic) {
        BluetoothResultTypeBuilder.fromClassDeclaration(this, logger)?.let { resultTypeBuilder ->
            if (resultTypeBuilder.hasCustomResult) {
                FileSpec.builder(resultTypeBuilder.responseClassName)
                    .apply {
                        resultTypeBuilder.generateType()?.let {
                            addType(it)
                        }
                    }
                    .generate()
            }
        }
        val remoteCharacteristicClass = ClassName(packageName.asString(), clientName(prefix = "Remote", postFix = ""))
        FileSpec.builder(remoteCharacteristicClass).generate(
            GenerationType.Side.CLIENT,
            BluetoothRemoteCharacteristicBuilder(this, characteristic, logger),
        )
        val localCharacteristicClass = ClassName(packageName.asString(), serverName(prefix = "Local", postFix = ""))
        FileSpec.builder(localCharacteristicClass).generate(
            GenerationType.Side.SERVER,
            BluetoothLocalCharacteristicBuilder(this, characteristic, logger),
        )
    }

    private fun KSClassDeclaration.generateBluetoothDescriptorFile(descriptor: BluetoothDescriptor) {
        BluetoothResultTypeBuilder.fromClassDeclaration(this, logger)?.let { resultTypeBuilder ->
            if (resultTypeBuilder.hasCustomResult) {
                FileSpec.builder(resultTypeBuilder.responseClassName)
                    .apply {
                        resultTypeBuilder.generateType()?.let {
                            addType(it)
                        }
                    }
                    .generate()
            }
        }
        val remoteDescriptorClass = ClassName(packageName.asString(), clientName(prefix = "Remote", postFix = ""))
        FileSpec.builder(remoteDescriptorClass).generate(
            GenerationType.Side.CLIENT,
            BluetoothRemoteDescriptorBuilder(this, descriptor, logger),
        )
        val localDescriptorClass = ClassName(packageName.asString(), serverName(prefix = "Local", postFix = ""))
        FileSpec.builder(localDescriptorClass).generate(
            GenerationType.Side.SERVER,
            BluetoothLocalDescriptorBuilder(this, descriptor, logger),
        )
    }

    private fun FileSpec.Builder.generate(side: GenerationType.Side, builder: AbstractBluetoothClassBuilder) = apply {
        addTypes(
            when (side) {
                GenerationType.Side.CLIENT -> listOf(GenerationType.CLIENT_API, GenerationType.CLIENT_BLUETOOTH, GenerationType.CLIENT_SIMULATOR)
                GenerationType.Side.SERVER -> listOf(GenerationType.SERVER_API, GenerationType.SERVER_BLUETOOTH, GenerationType.SERVER_SIMULATOR)
            }.map { generationType ->
                builder.generate(generationType)
            },
        )
    }.generate()

    private fun FileSpec.Builder.generate() = apply {
        indent("    ").build().writeTo(codeGenerator, Dependencies.ALL_FILES)
    }

    private fun KSClassDeclaration.clientName(prefix: String = "", postFix: String = "Client") =
        getAnnotationsByType(BluetoothClientName::class).firstOrNull()?.name ?: "$prefix${simpleName.asString()}$postFix"
    private fun KSClassDeclaration.serverName(prefix: String = "", postFix: String = "Server") =
        getAnnotationsByType(BluetoothServerName::class).firstOrNull()?.name ?: "$prefix${simpleName.asString()}$postFix"
}

class BluetoothSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = BluetoothSymbolProcessor(environment)
}
