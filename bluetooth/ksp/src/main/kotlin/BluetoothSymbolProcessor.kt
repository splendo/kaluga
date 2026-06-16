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
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.processing.UnknownPlatformInfo
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Origin
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothClientName
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothServerName
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

class BluetoothSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger

    private val commonSources = environment.options["commonSource"].orEmpty().split(":")

    private val options = Options(environment)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("------ PROCESSING for ${environment.platforms.joinToString { it.platformName }} ----")
        logger.warn("Options:")
        for ((key, value) in environment.options.entries) {
            logger.warn("\t$key = $value")
        }
        val apiDiffersFromOutput = (options.apiPackage ?: options.generatedPackage) != options.generatedPackage
        if (options.generateApi && (options.generateBluetoothImplementation || options.generateSimulatorImplementation) && apiDiffersFromOutput) {
            logger.error(
                "apiPackage must equal generatedPackage when a module generates both the API and an implementation; use apiOnly() or useExternalApi() to split them across modules.",
            )
            return emptyList()
        }
        val bluetoothDeclarations = resolver.getSymbolsWithAnnotation(Bluetooth::class.java.name).filterIsInstance<KSClassDeclaration>().filter {
            it.includeInGeneration && it.parentDeclaration == null
        }
        bluetoothDeclarations.forEach { bluetoothDeclaration ->
            bluetoothDeclaration.generateBluetoothClientFile()
            bluetoothDeclaration.generateBluetoothServerFile()
        }
        val serviceDeclarations = resolver.getSymbolsWithAnnotation(BluetoothService::class.java.name).filterIsInstance<KSClassDeclaration>().filter {
            it.includeInGeneration && it.parentDeclaration == null
        }
        serviceDeclarations.forEach { serviceDeclaration ->
            serviceDeclaration.generateBluetoothServiceFile(serviceDeclaration.getAnnotationsByType(BluetoothService::class).first())
        }
        val characteristicDeclarations = resolver.getSymbolsWithAnnotation(BluetoothCharacteristic::class.java.name).filterIsInstance<KSClassDeclaration>().filter {
            it.includeInGeneration && it.parentDeclaration ==
                null
        }
        characteristicDeclarations.forEach { characteristicDeclaration ->
            characteristicDeclaration.generateBluetoothCharacteristicFile(characteristicDeclaration.getAnnotationsByType(BluetoothCharacteristic::class).first())
        }
        val descriptorDeclarations = resolver.getSymbolsWithAnnotation(BluetoothDescriptor::class.java.name).filterIsInstance<KSClassDeclaration>().filter {
            it.includeInGeneration && it.parentDeclaration ==
                null
        }
        descriptorDeclarations.forEach { descriptorDeclaration ->
            descriptorDeclaration.generateBluetoothDescriptorFile(descriptorDeclaration.getAnnotationsByType(BluetoothDescriptor::class).first())
        }
        logger.warn("------ DONE ----")
        return emptyList()
    }

    private fun KSClassDeclaration.generateBluetoothClientFile() {
        val clientClass = ClassName(NameHelper.filePackage(this, options), clientName())
        FileSpec.builder(clientClass).generate(
            GenerationType.Side.CLIENT,
            BluetoothClientBuilder(this, options, logger),
        )
    }

    private fun KSClassDeclaration.generateBluetoothServerFile() {
        val serverClass = ClassName(NameHelper.filePackage(this, options), serverName())
        FileSpec.builder(serverClass).generate(
            GenerationType.Side.SERVER,
            BluetoothServerBuilder(this, options, logger),
        )
    }

    private fun KSClassDeclaration.generateBluetoothServiceFile(service: BluetoothService) {
        val remoteServiceClass = ClassName(NameHelper.filePackage(this, options), clientName(prefix = "Remote", postFix = ""))
        FileSpec.builder(remoteServiceClass).generate(
            GenerationType.Side.CLIENT,
            BluetoothRemoteServiceBuilder(this, service, options, logger),
        )
        val localServiceClass = ClassName(NameHelper.filePackage(this, options), serverName(prefix = "Local", postFix = ""))
        FileSpec.builder(localServiceClass).generate(
            GenerationType.Side.SERVER,
            BluetoothLocalServiceBuilder(this, service, options, logger),
        )
    }

    private fun KSClassDeclaration.generateBluetoothCharacteristicFile(characteristic: BluetoothCharacteristic) {
        BluetoothResultTypeBuilder.fromClassDeclaration(this, options, logger)?.let { resultTypeBuilder ->
            if (resultTypeBuilder.hasCustomResult && options.generateApi) {
                FileSpec.builder(resultTypeBuilder.responseClassName)
                    .apply {
                        resultTypeBuilder.generateType()?.let {
                            addType(it)
                        }
                    }
                    .generate()
            }
        }
        val remoteCharacteristicClass = ClassName(NameHelper.filePackage(this, options), clientName(prefix = "Remote", postFix = ""))
        FileSpec.builder(remoteCharacteristicClass).generate(
            GenerationType.Side.CLIENT,
            BluetoothRemoteCharacteristicBuilder(this, characteristic, options, logger),
        )
        val localCharacteristicClass = ClassName(NameHelper.filePackage(this, options), serverName(prefix = "Local", postFix = ""))
        FileSpec.builder(localCharacteristicClass).generate(
            GenerationType.Side.SERVER,
            BluetoothLocalCharacteristicBuilder(this, characteristic, options, logger),
        )
    }

    private fun KSClassDeclaration.generateBluetoothDescriptorFile(descriptor: BluetoothDescriptor) {
        BluetoothResultTypeBuilder.fromClassDeclaration(this, options, logger)?.let { resultTypeBuilder ->
            if (resultTypeBuilder.hasCustomResult && options.generateApi) {
                FileSpec.builder(resultTypeBuilder.responseClassName)
                    .apply {
                        resultTypeBuilder.generateType()?.let {
                            addType(it)
                        }
                    }
                    .generate()
            }
        }
        val remoteDescriptorClass = ClassName(NameHelper.filePackage(this, options), clientName(prefix = "Remote", postFix = ""))
        FileSpec.builder(remoteDescriptorClass).generate(
            GenerationType.Side.CLIENT,
            BluetoothRemoteDescriptorBuilder(this, descriptor, options, logger),
        )
        val localDescriptorClass = ClassName(NameHelper.filePackage(this, options), serverName(prefix = "Local", postFix = ""))
        FileSpec.builder(localDescriptorClass).generate(
            GenerationType.Side.SERVER,
            BluetoothLocalDescriptorBuilder(this, descriptor, options, logger),
        )
    }

    private fun FileSpec.Builder.generate(side: GenerationType.Side, builder: AbstractBluetoothClassBuilder) {
        val typesToAdd = when (side) {
            GenerationType.Side.CLIENT -> {
                listOfNotNull(
                    GenerationType.CLIENT_API.takeIf { options.generateApi },
                    GenerationType.CLIENT_BLUETOOTH.takeIf { options.generateBluetoothImplementation },
                    GenerationType.CLIENT_SIMULATOR.takeIf { options.generateSimulatorImplementation },
                ).takeIf { options.generateClient }
            }

            GenerationType.Side.SERVER -> {
                listOfNotNull(
                    GenerationType.SERVER_API.takeIf { options.generateApi },
                    GenerationType.SERVER_BLUETOOTH.takeIf { options.generateBluetoothImplementation },
                    GenerationType.SERVER_SIMULATOR.takeIf { options.generateSimulatorImplementation },
                ).takeIf { options.generateServer }
            }
        }
        typesToAdd?.let {
            addTypes(
                typesToAdd.map { generationType ->
                    builder.generate(generationType)
                },
            )
            typesToAdd.forEach { generationType ->
                builder.generateExtensionFactories(generationType).forEach { addFunction(it) }
            }
            generate()
        }
    }

    private fun FileSpec.Builder.generate() = apply {
        indent("    ").build().writeTo(codeGenerator, Dependencies.ALL_FILES)
    }

    private fun KSClassDeclaration.clientName(prefix: String = "", postFix: String = "Client") =
        getAnnotationsByType(BluetoothClientName::class).firstOrNull()?.name ?: "$prefix${simpleName.asString()}$postFix"
    private fun KSClassDeclaration.serverName(prefix: String = "", postFix: String = "Server") =
        getAnnotationsByType(BluetoothServerName::class).firstOrNull()?.name ?: "$prefix${simpleName.asString()}$postFix"

    private val KSClassDeclaration.includeInGeneration: Boolean get() = when {
        environment.options["isSingleTarget"] == "true" -> true

        environment.platforms.size > 1 -> true

        else -> {
            containingFile?.filePath?.let { filePath ->
                commonSources.none { filePath.startsWith(it) }
            } ?: true
        }
    }
}

class BluetoothSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = BluetoothSymbolProcessor(environment)
}
