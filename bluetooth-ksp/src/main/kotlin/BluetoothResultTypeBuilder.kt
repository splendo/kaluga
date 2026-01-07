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

import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

internal class BluetoothResultTypeBuilder(val classDeclaration: KSClassDeclaration, val propertyDeclaration: KSPropertyDeclaration) {

    companion object {
        fun fromClassDeclaration(declaration: KSClassDeclaration): BluetoothResultTypeBuilder? =
            declaration.declarations.filterIsInstance<KSPropertyDeclaration>().firstOrNull { it.isAnnotationPresent(Readable::class) }?.let {
                BluetoothResultTypeBuilder(declaration, it)
            }
        private val errorTypeClass = ClassName("com.splendo.kaluga.bluetooth", "GattResponse", "ReadError")
    }

    val responseClassName: ClassName get() {
        val className = NameHelper.nameFor(classDeclaration, GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.API))
        return ClassName(className.packageName, className.simpleNames.dropLast(1) + "${classDeclaration.simpleName.asString()}ReadResponse")
    }

    fun generateType() = TypeSpec.classBuilder(responseClassName)
        .addModifiers(KModifier.SEALED)
        .addType(
            TypeSpec.classBuilder("Success")
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("response", propertyDeclaration.type.resolve().toClassName())
                        .build(),
                )
                .addProperty(
                    PropertySpec.builder("response", propertyDeclaration.type.resolve().toClassName())
                        .initializer("response")
                        .build(),
                )
                .build(),
        )
        .addType(
            TypeSpec.classBuilder("Failure")
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("error", errorTypeClass)
                        .build(),
                )
                .addProperty(
                    PropertySpec.builder("error", errorTypeClass)
                        .initializer("error")
                        .build(),
                )
                .build(),
        )
        .build()
}
