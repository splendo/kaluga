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

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Origin
import com.splendo.kaluga.bluetooth.ksp.Options
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * How the value of a characteristic or descriptor property becomes the bytes of the attribute, and back.
 */
internal sealed class AttributeValue {

    /** The type the generated API reads, writes and notifies. */
    abstract val typeName: TypeName

    /** Whether converting the value needs the `BytesFormat` the generated classes are given. */
    open val needsFormat: Boolean get() = false

    /** The value held by the attribute bytes [bytes]. */
    abstract fun decode(bytes: CodeBlock): CodeBlock

    /** The attribute bytes holding [value]. */
    abstract fun encode(value: CodeBlock): CodeBlock

    /** The attribute bytes holding [value], from [offset] on, as a long read asks for them. */
    open fun encode(value: CodeBlock, offset: String): CodeBlock = CodeBlock.of("%L.$DROP($offset).$TO_BYTE_ARRAY()", encode(value))

    /** A `ByteArray`, the bytes themselves. */
    data object Bytes : AttributeValue() {
        override val typeName: TypeName = BYTE_ARRAY
        override fun decode(bytes: CodeBlock): CodeBlock = bytes
        override fun encode(value: CodeBlock): CodeBlock = value
    }

    /**
     * A `@BytesLayout` read and written in place through the zero-copy view the bytes processor generates for it,
     * [typeName], which wraps the attribute's own array.
     */
    data class View(override val typeName: ClassName) : AttributeValue() {
        override fun decode(bytes: CodeBlock): CodeBlock = CodeBlock.of("%T(%L)", typeName, bytes)
        override fun encode(value: CodeBlock): CodeBlock = CodeBlock.of("%L.bytes", value)

        // Most reads start at 0, which then hands out the view's array as it is.
        override fun encode(value: CodeBlock, offset: String): CodeBlock = CodeBlock.of("if ($offset == 0) %L else %L", encode(value), super.encode(value, offset))

        /** The bytes written so far as a value, failing as `BytesFormat` does while more are to come. */
        fun wrap(bytes: CodeBlock): CodeBlock = CodeBlock.of("%T.wrap(%L)", typeName, bytes)
    }

    /** Any other value, encoded by [serializer] through the `BytesFormat`. */
    data class Serialized(override val typeName: TypeName, val serializer: CodeBlock) : AttributeValue() {
        override val needsFormat: Boolean get() = true
        override fun decode(bytes: CodeBlock): CodeBlock = CodeBlock.of("$FORMAT.$DECODE_FROM_BYTE_ARRAY(%L, %L)", serializer, bytes)
        override fun encode(value: CodeBlock): CodeBlock = CodeBlock.of("$FORMAT.$ENCODE_TO_BYTE_ARRAY(%L, %L)", serializer, value)
    }
}

/** How the value of this property becomes the bytes of its attribute. */
internal fun KSPropertyDeclaration.attributeValue(options: Options): AttributeValue {
    val type = type.resolve()
    val typeName = type.toTypeName()
    return when {
        typeName == BYTE_ARRAY -> AttributeValue.Bytes
        else -> options.layoutViews.valueOf(this, type) ?: AttributeValue.Serialized(typeName, typeName.serializer())
    }
}

/**
 * Finds the `@BytesLayout` interfaces among the value types and what the bytes processor generates for them.
 *
 * The generated view is referred to by the name the bytes processor gives it rather than looked up, as it is
 * generated in the same KSP round as this processor's code. What a layout of this module generates is read from its
 * annotation, or else from [defaultGenerate], the `generate` option the bytes plugin passes to every processor of the
 * module. A layout compiled into another module generated whatever of it is on the classpath. A type alias of a layout
 * is that layout. A nullable layout-typed value is reported on its property.
 */
class LayoutViews internal constructor(private val resolver: Resolver?, private val defaultGenerate: Set<String>, private val logger: KSPLogger?) {

    private val values = mutableMapOf<String, AttributeValue?>()
    private val nullableProperties = mutableSetOf<String>()

    internal fun valueOf(property: KSPropertyDeclaration, type: KSType): AttributeValue? {
        val expanded = type.withoutAliases()
        val declaration = expanded.declaration as? KSClassDeclaration ?: return null
        if (declaration.classKind != ClassKind.INTERFACE) return null
        val annotation = declaration.annotations.firstOrNull { it.qualifiedName() == BYTES_LAYOUT } ?: return null
        val qualifiedName = declaration.qualifiedName?.asString() ?: return null
        if (expanded.isMarkedNullable) {
            val propertyName = property.qualifiedName?.asString() ?: property.simpleName.asString()
            // Every builder of the property asks for its value, so it is reported once.
            if (nullableProperties.add(propertyName)) {
                logger?.error(
                    "$propertyName is typed by the nullable layout $qualifiedName?, but a layout-typed value cannot be nullable: " +
                        "the layout is the attribute's bytes, which hold no null. Type it as ${declaration.simpleName.asString()}; a read that " +
                        "fails already yields null as its valueOrNull, and a part of the value that may be absent belongs in the layout itself",
                    property,
                )
            }
            return null
        }
        return values.getOrPut(qualifiedName) {
            val interfaceName = declaration.toClassName()
            fun generated(suffix: String) = ClassName(interfaceName.packageName, interfaceName.simpleNames.joinToString("") + suffix)
            val view = generated(if (Modifier.SEALED in declaration.modifiers) FRAME else VIEW)
            val data = generated(DATA)
            val generate = when {
                declaration.containingFile == null -> setOfNotNull(
                    Generate.VIEW.takeIf { resolver?.getClassDeclarationByName(view.canonicalName) != null },
                    Generate.DATA.takeIf { resolver?.getClassDeclarationByName(data.canonicalName) != null },
                )

                else -> annotation.arguments.firstOrNull { it.name?.asString() == GENERATE }?.takeUnless { it.origin == Origin.SYNTHETIC }?.let { argument ->
                    (argument.value as? List<*>).orEmpty().mapNotNull {
                        (it as? KSType)?.declaration?.simpleName?.asString() ?: (it as? KSClassDeclaration)?.simpleName?.asString()
                    }.toSet()
                } ?: defaultGenerate
            }
            when {
                Generate.VIEW in generate && declaration.annotations.any { it.qualifiedName() == BYTE_STUFFED } -> {
                    logger?.error(
                        "$qualifiedName is @ByteStuffed, so its view cannot read an attribute's bytes in place. Generate only DATA for it, or type the value as ByteArray",
                        declaration,
                    )
                    null
                }

                Generate.VIEW in generate -> AttributeValue.View(view)

                Generate.DATA in generate -> AttributeValue.Serialized(interfaceName, CodeBlock.of("%T.$INTERFACE_SERIALIZER", data))

                else -> {
                    logger?.error("$qualifiedName generates neither DATA nor VIEW that a Bluetooth value could be read through", declaration)
                    null
                }
            }
        }
    }

    private fun KSAnnotation.qualifiedName(): String? = annotationType.resolve().declaration.qualifiedName?.asString()

    // The `?` of a use is on the alias, not on the type the alias names.
    private tailrec fun KSType.withoutAliases(): KSType {
        val alias = declaration as? KSTypeAlias ?: return this
        val target = alias.type.resolve()
        return (if (isMarkedNullable) target.makeNullable() else target).withoutAliases()
    }

    companion object {
        /** The bytes plugin's option for what a layout generates without its own `generate`; the bytes processor's default without it. */
        internal const val GENERATE_OPTION = "generate"

        internal val None = LayoutViews(null, emptySet(), null)

        internal fun defaultGenerate(option: String?): Set<String> =
            option?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }?.toSet() ?: setOf(Generate.DATA, Generate.VIEW)

        private const val BYTES_LAYOUT = "com.splendo.kaluga.bytes.annotations.BytesLayout"
        private const val BYTE_STUFFED = "com.splendo.kaluga.bytes.annotations.ByteStuffed"
        private const val GENERATE = "generate"
        private const val INTERFACE_SERIALIZER = "InterfaceSerializer"
        private const val VIEW = "View"
        private const val FRAME = "Frame"
        private const val DATA = "Data"

        private object Generate {
            const val DATA = "DATA"
            const val VIEW = "VIEW"
        }
    }
}
