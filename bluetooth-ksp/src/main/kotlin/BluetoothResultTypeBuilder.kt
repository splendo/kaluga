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
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.READ
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.WHEN
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import java.sql.Ref

internal class BluetoothResultTypeBuilder(val classDeclaration: KSClassDeclaration, val propertyDeclaration: KSPropertyDeclaration) {

    companion object {
        fun fromClassDeclaration(declaration: KSClassDeclaration): BluetoothResultTypeBuilder? =
            declaration.declarations.filterIsInstance<KSPropertyDeclaration>().firstOrNull { it.isAnnotationPresent(Readable::class) }?.let {
                BluetoothResultTypeBuilder(declaration, it)
            }

        const val SUCCESS = "Success"
        const val FAILURE = "Failure"
        const val RESPONSE = "response"
        const val ERROR = "error"
    }

    internal val hasCustomResult = !propertyDeclaration.isByteArray

    val responseClassName: ClassName get() = if (hasCustomResult) {
        val className = NameHelper.nameFor(classDeclaration, GenerationType(GenerationType.Side.CLIENT, GenerationType.Type.API))
        ClassName(className.packageName, className.simpleNames.dropLast(1) + "${classDeclaration.simpleName.asString()}ReadResponse")
    } else {
        propertyDeclaration.type.resolve().toClassName()
    }

    fun generateType() = if (hasCustomResult) {
        TypeSpec.classBuilder(responseClassName)
            .addModifiers(KModifier.SEALED)
            .addType(
                TypeSpec.classBuilder(SUCCESS)
                    .superclass(responseClassName)
                    .addModifiers(KModifier.DATA)
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter(RESPONSE, propertyDeclaration.type.resolve().toClassName())
                            .build(),
                    )
                    .addProperty(
                        PropertySpec.builder(RESPONSE, propertyDeclaration.type.resolve().toClassName())
                            .initializer(RESPONSE)
                            .build(),
                    )
                    .build(),
            )
            .addType(
                TypeSpec.classBuilder(FAILURE)
                    .superclass(responseClassName)
                    .addModifiers(KModifier.DATA)
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter(ERROR, References.Bluetooth.readError)
                            .build(),
                    )
                    .addProperty(
                        PropertySpec.builder(ERROR, References.Bluetooth.readError)
                            .initializer(ERROR)
                            .build(),
                    )
                    .build(),
            )
            .build()
    } else {
        null
    }

    fun generateBluetoothResult(funSpec: FunSpec.Builder, attributeName: String) = if (hasCustomResult) {
        val readResult = "readResult"
        funSpec.addCode(
            CodeBlock.builder()
                .beginControlFlow("$RETURN $WHEN (val $readResult = $attributeName.$READ())")
                .beginControlFlow("is %T ->", References.Bluetooth.readSuccess)
                .addStatement("%T.$SUCCESS($FORMAT.decodeFromByteArray(%T.%M(), $readResult.value))", responseClassName, propertyDeclaration.type.resolve().toClassName(), References.KotlinX.Serialization.serializer)
                .endControlFlow()
                .beginControlFlow("is %T ->", References.Bluetooth.readError)
                .addStatement("%T.$FAILURE($readResult)", responseClassName)
                .endControlFlow()
                .endControlFlow()
                .build()
        )
    } else {
        funSpec.addStatement("$RETURN $attributeName.$READ()")
    }

    fun parseBluetoothResult(addReadStatement: CodeBlock) = if (hasCustomResult) {
        CodeBlock.builder()
            .beginControlFlow("$WHEN (val response = %L) {", addReadStatement)
            .beginControlFlow("is %T.$SUCCESS -> {", responseClassName)
            .addStatement("%T($FORMAT.encodeToByteArray(%T.%M(), response.$RESPONSE).drop($OFFSET))", References.Bluetooth.readSuccess, propertyDeclaration.type.resolve().toClassName(), References.KotlinX.Serialization.serializer)
            .endControlFlow()
            .beginControlFlow("is %T.$FAILURE -> {", responseClassName)
            .addStatement("response.$ERROR")
            .endControlFlow()
            .endControlFlow()
            .build()
    } else {
        CodeBlock.builder()
            .add("%L.drop($OFFSET)", addReadStatement)
            .build()
    }

    fun generateDefaultResult(funSpec: CodeBlock.Builder) = if (hasCustomResult) {
        funSpec.addStatement("%T.$FAILURE(%T)", responseClassName, References.Bluetooth.requestNotSupported)
    } else {
        funSpec.addStatement("%T", References.Bluetooth.requestNotSupported)
    }
}
