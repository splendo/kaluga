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

import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal object NeedsFormatterHelper {

    enum class Target {
        CLIENT,
        SERVER,
        SERVER_DSL,
    }

    @JvmInline
    value class NeedsFormatter(val needsFormatter: Boolean) {
        val functionArgument: String get() = if (needsFormatter) ", $FORMAT" else ""
    }

    fun needsBluetoothFormatter(declaration: KSClassDeclaration, target: Target = Target.CLIENT): NeedsFormatter = when {
        declaration.isAnnotationPresent(Bluetooth::class) ||
            declaration.isAnnotationPresent(BluetoothService::class) -> {
            declaration.declarations.filterIsInstance<KSPropertyDeclaration>().any { property ->
                (property.type.resolve().declaration as? KSClassDeclaration)?.let {
                    needsBluetoothFormatter(it, target).needsFormatter
                } ?: false
            }
        }

        declaration.isAnnotationPresent(BluetoothCharacteristic::class) -> {
            declaration.declarations.filterIsInstance<KSPropertyDeclaration>().any { property ->
                when {
                    (
                        (
                            property.isReadable ||
                                property.isWritable
                            ) && target != Target.SERVER
                        ) ||
                        ((property.isNotifiable) && target != Target.SERVER_DSL) -> property.type.resolve().toTypeName() != BYTE_ARRAY

                    else -> (property.type.resolve().declaration as? KSClassDeclaration)?.let {
                        needsBluetoothFormatter(it, target).needsFormatter
                    } ?: false
                }
            }
        }

        declaration.isAnnotationPresent(BluetoothDescriptor::class) -> {
            declaration.declarations.filterIsInstance<KSPropertyDeclaration>().any { property ->
                when {
                    target == Target.SERVER -> false

                    property.isReadable ||
                        property.isAnnotationPresent(Writable::class) -> property.type.resolve().toTypeName() != ClassName("kotlin", "ByteArray")

                    else -> false
                }
            }
        }

        else -> false
    }.let { NeedsFormatter(it) }
}
