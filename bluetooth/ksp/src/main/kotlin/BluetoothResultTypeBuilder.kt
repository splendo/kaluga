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

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.splendo.kaluga.bluetooth.ksp.helpers.DECODE_FROM_BYTE_ARRAY
import com.splendo.kaluga.bluetooth.ksp.helpers.DROP
import com.splendo.kaluga.bluetooth.ksp.helpers.ENCODE_TO_BYTE_ARRAY
import com.splendo.kaluga.bluetooth.ksp.helpers.ERROR
import com.splendo.kaluga.bluetooth.ksp.helpers.FAILURE
import com.splendo.kaluga.bluetooth.ksp.helpers.FORMAT
import com.splendo.kaluga.bluetooth.ksp.helpers.IS
import com.splendo.kaluga.bluetooth.ksp.helpers.NameHelper
import com.splendo.kaluga.bluetooth.ksp.helpers.OFFSET
import com.splendo.kaluga.bluetooth.ksp.helpers.READ
import com.splendo.kaluga.bluetooth.ksp.helpers.RESPONSE
import com.splendo.kaluga.bluetooth.ksp.helpers.RETURN
import com.splendo.kaluga.bluetooth.ksp.helpers.References
import com.splendo.kaluga.bluetooth.ksp.helpers.SUCCESS
import com.splendo.kaluga.bluetooth.ksp.helpers.VAL
import com.splendo.kaluga.bluetooth.ksp.helpers.VALUE
import com.splendo.kaluga.bluetooth.ksp.helpers.WHEN
import com.splendo.kaluga.bluetooth.ksp.helpers.isByteArray
import com.splendo.kaluga.bluetooth.ksp.helpers.isReadable
import com.splendo.kaluga.bluetooth.ksp.helpers.serializer
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

internal class BluetoothResultTypeBuilder(
    val classDeclaration: KSClassDeclaration,
    val propertyDeclaration: KSPropertyDeclaration,
    val options: Options,
    private val logger: KSPLogger,
) {

    companion object {
        fun fromClassDeclaration(declaration: KSClassDeclaration, options: Options, logger: KSPLogger): BluetoothResultTypeBuilder? =
            declaration.declarations.filterIsInstance<KSPropertyDeclaration>().firstOrNull { it.isReadable }?.let {
                BluetoothResultTypeBuilder(declaration, it, options, logger)
            }
    }

    internal val hasCustomResult = !propertyDeclaration.isByteArray

    val responseClassName: ClassName get() = if (hasCustomResult) {
        val className = NameHelper.nameFor(classDeclaration, if (options.generateClient) GenerationType.CLIENT_API else GenerationType.SERVER_API)
        ClassName(className.packageName, className.simpleNames.dropLast(1) + "${classDeclaration.simpleName.asString()}ReadResponse")
    } else {
        References.Bluetooth.readResponse
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
                            .addParameter(RESPONSE, propertyDeclaration.type.resolve().toTypeName())
                            .build(),
                    )
                    .addProperty(
                        PropertySpec.builder(RESPONSE, propertyDeclaration.type.resolve().toTypeName())
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
                .beginControlFlow("$RETURN $WHEN ($VAL $readResult = $attributeName.$READ())")
                .beginControlFlow("$IS %T ->", References.Bluetooth.readSuccess)
                .addStatement(
                    "%T.$SUCCESS($FORMAT.$DECODE_FROM_BYTE_ARRAY(%L, $readResult.$VALUE))",
                    responseClassName,
                    propertyDeclaration.type.resolve().toTypeName().serializer(logger),
                )
                .endControlFlow()
                .beginControlFlow("$IS %T ->", References.Bluetooth.readError)
                .addStatement("%T.$FAILURE($readResult)", responseClassName)
                .endControlFlow()
                .endControlFlow()
                .build(),
        )
    } else {
        funSpec.addStatement("$RETURN $attributeName.$READ()")
    }

    fun parseBluetoothResult(addReadStatement: CodeBlock) = if (hasCustomResult) {
        CodeBlock.builder()
            .beginControlFlow("$WHEN ($VAL $RESPONSE = %L)) {", addReadStatement)
            .beginControlFlow("$IS %T.$SUCCESS -> {", responseClassName)
            .addStatement(
                "%T($FORMAT.$ENCODE_TO_BYTE_ARRAY(%L, $RESPONSE.$RESPONSE).$DROP($OFFSET))",
                References.Bluetooth.readSuccess,
                propertyDeclaration.type.resolve().toTypeName().serializer(logger),
            )
            .endControlFlow()
            .beginControlFlow("$IS %T.$FAILURE -> {", responseClassName)
            .addStatement("$RESPONSE.$ERROR")
            .endControlFlow()
            .endControlFlow()
            .build()
    } else {
        CodeBlock.builder()
            .addStatement("%L, $OFFSET)", addReadStatement)
            .build()
    }
}
