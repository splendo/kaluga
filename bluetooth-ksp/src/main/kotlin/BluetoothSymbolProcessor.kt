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
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothClient
import com.splendo.kaluga.bluetooth.annotations.BluetoothServer
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.math.log

class BluetoothSymbolProcessor(
    environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("Process")
        val bluetoothSymbols = resolver.getSymbolsWithAnnotation(Bluetooth::class.java.name).filterIsInstance<KSClassDeclaration>()
        bluetoothSymbols.forEach { bluetoothSymbol ->
            bluetoothSymbol.generateBluetooth()
        }
        val clientSymbols = resolver.getSymbolsWithAnnotation(BluetoothClient::class.java.name).filter { !it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
        clientSymbols.forEach { clientSymbols ->
            clientSymbols.generateBluetoothClient()
        }
        val serverSymbols = resolver.getSymbolsWithAnnotation(BluetoothServer::class.java.name).filter { !it.isAnnotationPresent(Bluetooth::class) }.filterIsInstance<KSClassDeclaration>()
        serverSymbols.forEach { serverSymbol ->
            serverSymbol.generateBluetoothServer()
        }
        return emptyList()
    }

    private fun KSClassDeclaration.generateBluetooth() {
        generateBluetoothClient()
        generateBluetoothServer()
    }

    private fun KSClassDeclaration.generateBluetoothClient() {
        logger.info("Generate Client")
        val clientClass = ClassName(packageName.asString(), simpleName.asString() + "Client")
        FileSpec.builder(clientClass)
            .addType(
                TypeSpec.interfaceBuilder(clientClass.simpleName).build()
            )
            .generate()
    }

    private fun KSClassDeclaration.generateBluetoothServer() {
        logger.info("generate Server")
        val serverClass = ClassName(packageName.asString(), simpleName.asString() + "Server")
        FileSpec.builder(serverClass)
            .addType(
                TypeSpec.interfaceBuilder(serverClass.simpleName).build()
            )
            .generate()
    }

    private fun FileSpec.Builder.generate() = build().writeTo(codeGenerator, Dependencies.ALL_FILES)
}

class BluetoothSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = BluetoothSymbolProcessor(environment)
}
