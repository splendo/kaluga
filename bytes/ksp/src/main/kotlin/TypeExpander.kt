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
package com.splendo.kaluga.bytes.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSTypeParameter

/**
 * Expands the type aliases in a type and its type arguments into the types they stand for, as the kotlinx
 * serialization plugin sees them: an alias's own nullability is merged with its use's, and its type parameters are
 * replaced by the use's type arguments.
 */
internal class TypeExpander(private val resolver: Resolver) {

    fun expand(type: KSType): KSType {
        val declaration = type.declaration
        if (declaration is KSTypeAlias) {
            val bindings = declaration.typeParameters.map { it.name.asString() }.zip(type.arguments).toMap()
            val target = substitute(declaration.type.resolve(), bindings)
            return expand(if (type.isMarkedNullable) target.makeNullable() else target)
        }
        if (type.arguments.isEmpty()) return type
        return type.replace(type.arguments.map { argument -> transformed(argument) { expand(it) } })
    }

    private fun substitute(type: KSType, bindings: Map<String, KSTypeArgument>): KSType {
        val declaration = type.declaration
        if (declaration is KSTypeParameter) {
            val bound = bindings[declaration.name.asString()]?.type?.resolve() ?: return type
            return if (type.isMarkedNullable) bound.makeNullable() else bound
        }
        if (type.arguments.isEmpty()) return type
        return type.replace(type.arguments.map { argument -> transformed(argument) { substitute(it, bindings) } })
    }

    // A star projection has no type to transform.
    private fun transformed(argument: KSTypeArgument, transform: (KSType) -> KSType): KSTypeArgument {
        val type = argument.type?.resolve() ?: return argument
        return resolver.getTypeArgument(resolver.createKSTypeReferenceFromKSType(transform(type)), argument.variance)
    }
}
