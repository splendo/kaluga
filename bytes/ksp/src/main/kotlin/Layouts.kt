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

import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Visibility
import com.splendo.kaluga.bytes.annotations.Generate
import com.squareup.kotlinpoet.ClassName

/** A declaration the processor cannot turn into a layout, reported as a KSP error on [node]. */
internal class LayoutDeclarationException(message: String, val node: KSNode?) : Exception(message)

/**
 * A `@BytesLayout` interface and what its DATA implementation looks like.
 *
 * A non-sealed layout becomes a data class (a data object when it has no properties); a sealed one becomes a sealed
 * interface that the data classes of its sub-layouts implement next to their own interface. That sealed interface
 * deliberately does not extend the layout interface, so the user's sealed hierarchy keeps exactly the subtypes the
 * user declared and a `when` over it stays exhaustive.
 */
internal class Layout(val declaration: KSClassDeclaration, val generate: Set<Generate>, private val types: TypeExpander) {

    val qualifiedName: String = declaration.qualifiedName!!.asString()

    val interfaceClassName: ClassName = ClassName(declaration.packageName.asString(), declaration.nestedSimpleNames())

    /** Nested interfaces flatten into one top-level name, since generated declarations cannot be added to the user's scopes. */
    val dataClassName: ClassName = ClassName(declaration.packageName.asString(), declaration.nestedSimpleNames().joinToString("") + "Data")

    /** The zero-copy frame value class of a sealed layout, flattened like [dataClassName]. */
    val frameClassName: ClassName = ClassName(declaration.packageName.asString(), declaration.nestedSimpleNames().joinToString("") + "Frame")

    /** The zero-copy VIEW value class, flattened like [dataClassName]. */
    val viewClassName: ClassName = ClassName(declaration.packageName.asString(), declaration.nestedSimpleNames().joinToString("") + "View")

    val isSealed: Boolean = Modifier.SEALED in declaration.modifiers

    val isInternal: Boolean get() = declaration.getVisibility() == Visibility.INTERNAL || declaration.parentDeclarations().any { it.getVisibility() == Visibility.INTERNAL }

    /** The sealed layout this layout is a direct subtype of, resolved by [LayoutIndex]. */
    var sealedParent: Layout? = null
        internal set

    /** Direct sealed subtypes, ordered by [dataClassName] as the kotlinx plugin enumerates sealed inheritors. */
    var sealedChildren: List<Layout> = emptyList()
        internal set

    val sealedRoot: Layout get() = sealedParent?.sealedRoot ?: this

    /** The non-sealed layouts reachable through [sealedChildren], in the order the kotlinx sealed serializer lists them. */
    val sealedLeaves: List<Layout> get() = sealedChildren.flatMap { if (it.isSealed) it.sealedLeaves else listOf(it) }

    val properties: List<LayoutProperty> by lazy { collectProperties() }

    private fun collectProperties(): List<LayoutProperty> {
        if (declaration.typeParameters.isNotEmpty()) {
            throw LayoutDeclarationException("@BytesLayout interface $qualifiedName cannot be generic; declare a concrete sub-interface instead", declaration)
        }
        // Supertype properties first, in supertype declaration order, then the interface's own; an override keeps the
        // position of the property it overrides but its own annotations and type. This is also the element order of
        // the generated data class, since kotlinx lists a class's elements in primary constructor order.
        val byName = LinkedHashMap<String, KSPropertyDeclaration>()
        fun collect(classDeclaration: KSClassDeclaration) {
            classDeclaration.superTypes.forEach { superType ->
                val superDeclaration = superType.resolve().declaration as? KSClassDeclaration
                if (superDeclaration != null && superDeclaration.classKind == ClassKind.INTERFACE) collect(superDeclaration)
            }
            classDeclaration.declarations.filterIsInstance<KSPropertyDeclaration>().forEach { byName[it.simpleName.asString()] = it }
        }
        collect(declaration)
        val type = declaration.asType(emptyList())
        return byName.values.filter { it.isAbstract() }.map { property ->
            if (property.extensionReceiver != null) {
                throw LayoutDeclarationException("Extension property ${property.simpleName.asString()} cannot be part of a @BytesLayout", property)
            }
            if (property.simpleName.asString() == "Companion") {
                throw LayoutDeclarationException("A @BytesLayout property cannot be named Companion, the name of the generated DATA class's companion object", property)
            }
            LayoutProperty(property, types.expand(property.asMemberOf(type)))
        }
    }

    override fun toString(): String = qualifiedName
}

/** A property of a layout, with the type aliases in its [type] expanded. */
internal class LayoutProperty(val declaration: KSPropertyDeclaration, val type: KSType) {
    val name: String = declaration.simpleName.asString()
    val isMutable: Boolean = declaration.isMutable
    val annotations: Sequence<KSAnnotation> get() = declaration.annotations

    /** The element name kotlinx uses: an explicit `@SerialName`, else the property name. */
    val serialName: String get() = declaration.annotations.serialNameOverride() ?: name
}

/** All layouts of this compilation, keyed by qualified name, with their sealed relations resolved. */
internal class LayoutIndex(layouts: List<Layout>, val types: TypeExpander) {

    private val byName = layouts.associateBy { it.qualifiedName }

    val all: Collection<Layout> get() = byName.values

    operator fun get(declaration: KSClassDeclaration): Layout? = declaration.qualifiedName?.asString()?.let(byName::get)

    init {
        layouts.forEach { layout ->
            val sealedParents = layout.declaration.superTypes
                .mapNotNull { it.resolve().declaration as? KSClassDeclaration }
                .filter { Modifier.SEALED in it.modifiers }
                .toList()
            if (sealedParents.size > 1) {
                throw LayoutDeclarationException("@BytesLayout ${layout.qualifiedName} can extend at most one sealed interface", layout.declaration)
            }
            layout.sealedParent = sealedParents.firstOrNull()?.let { parent ->
                this[parent] ?: throw LayoutDeclarationException(
                    "@BytesLayout ${layout.qualifiedName} extends sealed ${parent.qualifiedName?.asString()}, which must also be a @BytesLayout",
                    layout.declaration,
                )
            }
        }
        layouts.filter { it.isSealed }.forEach { layout ->
            layout.sealedChildren = layout.declaration.getSealedSubclasses().map { subclass ->
                this[subclass]?.takeIf { subclass.classKind == ClassKind.INTERFACE } ?: throw LayoutDeclarationException(
                    "${subclass.qualifiedName?.asString()} is a subtype of sealed @BytesLayout ${layout.qualifiedName}, so it must be a @BytesLayout interface",
                    subclass,
                )
            }.sortedBy { it.dataClassName.canonicalName }.toList()
        }
    }
}

private fun KSClassDeclaration.nestedSimpleNames(): List<String> = (parentDeclarations().toList().reversed() + this).map { it.simpleName.asString() }

private fun KSClassDeclaration.parentDeclarations(): Sequence<KSClassDeclaration> = generateSequence(parentDeclaration as? KSClassDeclaration) {
    it.parentDeclaration as? KSClassDeclaration
}

private const val SERIAL_NAME = "kotlinx.serialization.SerialName"

internal fun Sequence<KSAnnotation>.serialNameOverride(): String? = firstOrNull { it.qualifiedName() == SERIAL_NAME }?.argument("value") as? String

internal fun KSAnnotation.qualifiedName(): String? = annotationType.resolve().declaration.qualifiedName?.asString()
