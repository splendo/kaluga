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

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothClient
import com.splendo.kaluga.bluetooth.annotations.BluetoothClientName
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothServer
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.sequences.forEach

@OptIn(KspExperimental::class)
class BluetoothSymbolProcessor(
    environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("------ PROCESSING ----")
        val bluetoothDeclarations = resolver.getSymbolsWithAnnotation(Bluetooth::class.java.name).filterIsInstance<KSClassDeclaration>().filter { it.parentDeclaration == null }
        bluetoothDeclarations.forEach { bluetoothDeclaration ->
            bluetoothDeclaration.generateBluetoothClientFile(GenerationType.CLIENT)
            bluetoothDeclaration.generateBluetoothServerFile(GenerationType.SERVER)
        }
        val clientDeclarations = resolver.getSymbolsWithAnnotation(BluetoothClient::class.java.name).filter { !it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>().filter { it.parentDeclaration == null }
        clientDeclarations.forEach { clientDeclaration ->
            clientDeclaration.generateBluetoothClientFile(GenerationType.BOTH)
        }
        val serverDeclarations = resolver.getSymbolsWithAnnotation(BluetoothServer::class.java.name).filter { !it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>().filter { it.parentDeclaration == null }
        serverDeclarations.forEach { serverDeclaration ->
            serverDeclaration.generateBluetoothServerFile(GenerationType.BOTH)
        }
        val serviceDeclarations = resolver.getSymbolsWithAnnotation(BluetoothService::class.java.name).filterIsInstance<KSClassDeclaration>().filter { it.parentDeclaration == null }
        serviceDeclarations.forEach { serviceDeclarations ->
            serviceDeclarations.generateBluetoothServiceFile()
        }
        val characteristicDeclarations = resolver.getSymbolsWithAnnotation(BluetoothCharacteristic::class.java.name).filterIsInstance<KSClassDeclaration>().filter { it.parentDeclaration == null }
        characteristicDeclarations.forEach { characteristicDeclaration ->
            characteristicDeclaration.generateBluetoothCharacteristicFile()
        }
        val descriptorDeclarations = resolver.getSymbolsWithAnnotation(BluetoothDescriptor::class.java.name).filterIsInstance<KSClassDeclaration>().filter { it.parentDeclaration == null }
        descriptorDeclarations.forEach { descriptorDeclaration ->
            descriptorDeclaration.generateBluetoothDescriptorFile()
        }
        logger.warn("------ DONE ----")
        return emptyList()
    }

    private fun KSClassDeclaration.generateBluetoothClientFile(generationType: GenerationType) {
        val clientClass = ClassName(packageName.asString(), clientName())
        FileSpec.builder(clientClass)
            .addType(generateBluetoothClient(generationType))
            .add
            .generate()
    }

    private fun KSClassDeclaration.generateBluetoothClient(generationType: GenerationType): TypeSpec = TypeSpec.interfaceBuilder(clientName())
        .addTypes(generateNested(generationType))
        .addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        ClassName(typeDeclaration.packageName.asString(), typeDeclaration.clientName("Remote", "")),
                    ).build()
                } else {
                    logger.error("A BluetoothClient should only have BluetoothService properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList()
        )
        .build()

    private fun KSClassDeclaration.generateBluetoothServerFile(generationType: GenerationType) {
        val serverClass = ClassName(packageName.asString(), serverName())
        FileSpec.builder(serverClass)
            .addType(generateBluetoothServer(generationType))
            .generate()
    }

    private fun KSClassDeclaration.generateBluetoothServer(generationType: GenerationType): TypeSpec = TypeSpec.interfaceBuilder(serverName())
        .addTypes(generateNested(generationType))
        .addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothService::class)) {
                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        ClassName(typeDeclaration.packageName.asString(), typeDeclaration.serverName("Local", "")),
                    ).build()
                } else {
                    logger.error("A BluetoothClient should only have BluetoothService properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList()
        )
        .build()

    private fun KSClassDeclaration.generateBluetoothServiceFile() {
        val serviceClass = ClassName(packageName.asString(), clientName(prefix = "RemoteAndLocal", postFix = ""))
        FileSpec.builder(serviceClass)
            .addType(generateBluetoothRemoteService(GenerationType.BOTH))
            .addType(generateBluetoothLocalService(GenerationType.BOTH))
            .generate()
    }

    private fun KSClassDeclaration.generateBluetoothRemoteService(generationType: GenerationType): TypeSpec = TypeSpec.interfaceBuilder(clientName(prefix = "Remote", postFix = ""))
        .addTypes(generateNested(generationType))
        .addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (
                    typeDeclaration is KSClassDeclaration &&
                    (typeDeclaration.isAnnotationPresent(BluetoothService::class) ||
                            typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class))
                    ) {
                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        ClassName(typeDeclaration.packageName.asString(), typeDeclaration.serverName("Remote", "")),
                    ).build()
                } else {
                    logger.error("A BluetoothService should only have BluetoothService and BluetoothCharacteristic properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList()
        )
        .build()

    private fun KSClassDeclaration.generateBluetoothLocalService(generationType: GenerationType): TypeSpec = TypeSpec.interfaceBuilder(clientName(prefix = "Local", postFix = ""))
        .addTypes(generateNested(generationType))
        .addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (
                    typeDeclaration is KSClassDeclaration &&
                    (typeDeclaration.isAnnotationPresent(BluetoothService::class) ||
                            typeDeclaration.isAnnotationPresent(BluetoothCharacteristic::class))
                ) {
                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        ClassName(typeDeclaration.packageName.asString(), typeDeclaration.serverName("Local", "")),
                    ).build()
                } else {
                    logger.error("A BluetoothService should only have BluetoothService and BluetoothCharacteristic properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList()
        )
        .build()

    private fun KSClassDeclaration.generateBluetoothCharacteristicFile() {
        val characteristicClass = ClassName(packageName.asString(), clientName(prefix = "RemoteAndLocal", postFix = ""))
        FileSpec.builder(characteristicClass)
            .addType(generateBluetoothRemoteCharacteristic(GenerationType.BOTH))
            .addType(generateBluetoothLocalCharacteristic(GenerationType.BOTH))
            .generate()
    }

    private fun KSClassDeclaration.generateBluetoothRemoteCharacteristic(generationType: GenerationType): TypeSpec = TypeSpec.interfaceBuilder(clientName(prefix = "Remote", postFix = ""))
        .addTypes(generateNested(generationType))
        .addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (
                    typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)
                ) {
                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        ClassName(typeDeclaration.packageName.asString(), typeDeclaration.serverName("Remote", "")),
                    ).build()
                } else {
                    logger.error("A BluetoothCharacteristic should only have BluetoothDescriptor properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList()
        )
        .build()

    private fun KSClassDeclaration.generateBluetoothLocalCharacteristic(generationType: GenerationType): TypeSpec = TypeSpec.interfaceBuilder(clientName(prefix = "Local", postFix = ""))
        .addTypes(generateNested(generationType))
        .addProperties(
            declarations.filterIsInstance<KSPropertyDeclaration>().mapNotNull { propertyDeclaration ->
                val typeDeclaration = propertyDeclaration.type.resolve().declaration
                if (
                    typeDeclaration is KSClassDeclaration && typeDeclaration.isAnnotationPresent(BluetoothDescriptor::class)
                ) {
                    PropertySpec.builder(
                        propertyDeclaration.simpleName.asString(),
                        ClassName(typeDeclaration.packageName.asString(), typeDeclaration.serverName("Local", "")),
                    ).build()
                } else {
                    logger.error("A BluetoothCharacteristic should only have BluetoothDescriptor properties $typeDeclaration ${typeDeclaration.annotations}")
                    null
                }
            }.toList()
        )
        .build()

    private fun KSClassDeclaration.generateBluetoothDescriptorFile() {
        val descriptorClass = ClassName(packageName.asString(), clientName(prefix = "RemoteAndLocal", postFix = ""))
        FileSpec.builder(descriptorClass)
            .addType(generateBluetoothRemoteDescriptor(GenerationType.BOTH))
            .addType(generateBluetoothLocalDescriptor(GenerationType.BOTH))
            .generate()
    }

    private fun KSClassDeclaration.generateBluetoothRemoteDescriptor(generationType: GenerationType): TypeSpec = TypeSpec.interfaceBuilder(clientName(prefix = "Remote", postFix = ""))
        .addTypes(generateNested(generationType))
        .build()

    private fun KSClassDeclaration.generateBluetoothLocalDescriptorFile(generationType: GenerationType) {
        val descriptorClass = ClassName(packageName.asString(), clientName(prefix = "Local", postFix = ""))
        FileSpec.builder(descriptorClass)
            .addType(generateBluetoothServer(generationType))
            .generate()
    }

    private fun KSClassDeclaration.generateBluetoothLocalDescriptor(generationType: GenerationType): TypeSpec = TypeSpec.interfaceBuilder(clientName(prefix = "Local", postFix = ""))
        .addTypes(generateNested(generationType))
        .build()


    private fun KSClassDeclaration.generateNested(generationType: GenerationType): List<TypeSpec> = buildList {
        val bluetoothDeclarations = declarations.filter { it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
        bluetoothDeclarations.forEach { bluetoothDeclaration ->
            if (generationType == GenerationType.BOTH || generationType == GenerationType.CLIENT) {
                add(bluetoothDeclaration.generateBluetoothClient(generationType))
            }
            if (generationType == GenerationType.BOTH || generationType == GenerationType.SERVER) {
                add(bluetoothDeclaration.generateBluetoothServer(generationType))
            }
        }

        if (generationType == GenerationType.BOTH || generationType == GenerationType.CLIENT) {
            val clientDeclarations =
                declarations.filter { it.isAnnotationPresent(BluetoothClient::class) && !it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
            addAll(
            clientDeclarations.map { clientDeclaration ->
                    clientDeclaration.generateBluetoothClient(generationType)
                }
            )
        }

        if (generationType == GenerationType.BOTH || generationType == GenerationType.SERVER) {
            val serverDeclarations =
                declarations.filter { it.isAnnotationPresent(BluetoothServer::class) && !it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
            addAll(
                serverDeclarations.map { serverDeclaration ->
                    serverDeclaration.generateBluetoothServer(generationType)
                }
            )

        }

        val serviceDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothService::class) }.filterIsInstance<KSClassDeclaration>()
        serviceDeclarations.forEach { serviceDeclaration ->
            if (generationType == GenerationType.BOTH || generationType == GenerationType.CLIENT) {
                add(serviceDeclaration.generateBluetoothRemoteService(generationType))
            }
            if (generationType == GenerationType.BOTH || generationType == GenerationType.SERVER) {
                add(serviceDeclaration.generateBluetoothLocalService(generationType))
            }
        }

        val characteristicDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothCharacteristic::class) }.filterIsInstance<KSClassDeclaration>()
        characteristicDeclarations.forEach { characteristicDeclaration ->
            if (generationType == GenerationType.BOTH || generationType == GenerationType.CLIENT) {
                add(characteristicDeclaration.generateBluetoothRemoteCharacteristic(generationType))
            }
            if (generationType == GenerationType.BOTH || generationType == GenerationType.SERVER) {
                add(characteristicDeclaration.generateBluetoothLocalCharacteristic(generationType))
            }
        }

        val descriptorDeclarations = declarations.filter { it.isAnnotationPresent(BluetoothDescriptor::class) }.filterIsInstance<KSClassDeclaration>()
        descriptorDeclarations.forEach { descriptorDeclaration ->
            if (generationType == GenerationType.BOTH || generationType == GenerationType.CLIENT) {
                add(descriptorDeclaration.generateBluetoothRemoteDescriptor(generationType))
            }
            if (generationType == GenerationType.BOTH || generationType == GenerationType.SERVER) {
                add(descriptorDeclaration.generateBluetoothLocalDescriptor(generationType))
            }
        }
    }

    private fun FileSpec.Builder.generate() = build().writeTo(codeGenerator, Dependencies.ALL_FILES)

    private fun KSClassDeclaration.clientName(prefix: String = "", postFix: String = "Client") = getAnnotationsByType(BluetoothClientName::class).firstOrNull()?.name ?: "$prefix${simpleName.asString()}$postFix"
    private fun KSClassDeclaration.serverName(prefix: String = "", postFix: String = "Server") = getAnnotationsByType(BluetoothClientName::class).firstOrNull()?.name ?: "$prefix${simpleName.asString()}$postFix"

}

class BluetoothSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = BluetoothSymbolProcessor(environment)
}
