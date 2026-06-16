package com.splendo.kaluga.bluetooth.ksp.helpers

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

val KSPropertyDeclaration.isNullable: Boolean get() = type.resolve().isMarkedNullable

fun CodeBlock.Builder.withLetIfNull(propertyToCheck: String, property: KSPropertyDeclaration, addStatementFor: CodeBlock.Builder.(String) -> CodeBlock.Builder) =
    if (property.isNullable) {
        beginControlFlow("$propertyToCheck?.let")
            .addStatementFor("it")
            .endControlFlow()
    } else {
        addStatementFor(propertyToCheck)
    }

val KSPropertyDeclaration.optionalChainIfNullable: String get() = if (isNullable) "?" else ""
fun TypeName.nullIfPropertyIsNull(property: KSPropertyDeclaration): TypeName = copy(nullable = property.isNullable)
