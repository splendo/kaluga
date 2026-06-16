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

package com.splendo.kaluga.bluetooth.ksp.helpers

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothClientName
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothServerName
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.ksp.GenerationType
import com.splendo.kaluga.bluetooth.ksp.Options
import com.squareup.kotlinpoet.ClassName

internal object NameHelper {

    fun clientName(declaration: KSClassDeclaration, type: GenerationType.Type, options: Options) = nameFor(
        declaration,
        when (type) {
            GenerationType.Type.API -> GenerationType.CLIENT_API
            GenerationType.Type.BLUETOOTH -> GenerationType.CLIENT_BLUETOOTH
            GenerationType.Type.SIMULATOR -> GenerationType.CLIENT_SIMULATOR
        },
        options,
    )
    fun serverName(declaration: KSClassDeclaration, type: GenerationType.Type, options: Options) = nameFor(
        declaration,
        when (type) {
            GenerationType.Type.API -> GenerationType.SERVER_API
            GenerationType.Type.BLUETOOTH -> GenerationType.SERVER_BLUETOOTH
            GenerationType.Type.SIMULATOR -> GenerationType.SERVER_SIMULATOR
        },
        options,
    )

    fun nameFor(declaration: KSClassDeclaration, generationType: GenerationType, options: Options): ClassName {
        val names = mutableListOf(declaration.simpleName(generationType))
        var current = declaration.parentDeclaration
        while (current != null) {
            names.add(0, current.simpleName(generationType))
            current = current.parentDeclaration
        }
        return ClassName(packageFor(declaration, generationType, options), names)
    }

    private fun packageFor(declaration: KSClassDeclaration, generationType: GenerationType, options: Options): String {
        val default = declaration.packageName.asString()
        return when (generationType.type) {
            GenerationType.Type.API -> options.apiPackage(default)
            GenerationType.Type.BLUETOOTH, GenerationType.Type.SIMULATOR -> options.generatedPackage(default)
        }
    }

    /** The package of the file written for [declaration]: the API package when the API is generated, otherwise the output package. */
    fun filePackage(declaration: KSClassDeclaration, options: Options): String {
        val default = declaration.packageName.asString()
        return if (options.generateApi) options.apiPackage(default) else options.generatedPackage(default)
    }

    private fun KSDeclaration.simpleName(generationType: GenerationType): String = when (generationType.side) {
        GenerationType.Side.CLIENT ->
            getAnnotationsByType(BluetoothClientName::class).firstOrNull()?.name ?: when {
                isAnnotationPresent(Bluetooth::class) -> "${simpleName.asString()}Client"
                isAnnotationPresent(BluetoothService::class) -> "Remote${simpleName.asString()}"
                isAnnotationPresent(BluetoothCharacteristic::class) -> "Remote${simpleName.asString()}"
                isAnnotationPresent(BluetoothDescriptor::class) -> "Remote${simpleName.asString()}"
                else -> simpleName.asString()
            }

        GenerationType.Side.SERVER -> getAnnotationsByType(BluetoothServerName::class).firstOrNull()?.name ?: when {
            isAnnotationPresent(Bluetooth::class) -> "${simpleName.asString()}Server"
            isAnnotationPresent(BluetoothService::class) -> "Local${simpleName.asString()}"
            isAnnotationPresent(BluetoothCharacteristic::class) -> "Local${simpleName.asString()}"
            isAnnotationPresent(BluetoothDescriptor::class) -> "Local${simpleName.asString()}"
            else -> simpleName.asString()
        }
    }.let {
        "${generationType.type.prefix}$it"
    }

    private val GenerationType.Type.prefix: String
        get() = when (this) {
            GenerationType.Type.API -> ""
            GenerationType.Type.BLUETOOTH -> "Bluetooth"
            GenerationType.Type.SIMULATOR -> "Simulated"
        }
}
