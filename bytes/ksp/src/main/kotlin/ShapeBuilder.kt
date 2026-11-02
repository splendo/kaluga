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

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.splendo.kaluga.bytes.layout.TypeKind
import com.splendo.kaluga.bytes.layout.TypeShape

/**
 * A [TypeShape] computed from source symbols. Elements are resolved lazily so a shape can refer to itself.
 */
internal class StaticTypeShape(
    override val serialName: String,
    override val kind: TypeKind,
    override val isInline: Boolean = false,
    override val annotations: List<Annotation> = emptyList(),
    override val isNullable: Boolean = false,
    private val elementsProvider: Lazy<List<Element>> = lazyOf(emptyList()),
) : TypeShape {

    class Element(val name: String, val annotations: List<Annotation>, shape: () -> TypeShape) {
        val shape: TypeShape by lazy(shape)
    }

    private val elements: List<Element> get() = elementsProvider.value

    override val elementsCount: Int get() = elements.size
    override fun elementName(index: Int): String = elements[index].name
    override fun elementAnnotations(index: Int): List<Annotation> = elements[index].annotations
    override fun element(index: Int): TypeShape = elements[index].shape
    override fun openSubtypes(): List<TypeShape> = emptyList()

    /** The shape kotlinx's `SerialDescriptor.nullable` produces: same elements, `?` appended to the serial name. */
    fun nullable(): StaticTypeShape = if (isNullable) this else StaticTypeShape("$serialName?", kind, isInline, annotations, true, elementsProvider)
}

/**
 * Builds the [TypeShape] of a layout's generated DATA class exactly as `SerialDescriptorShape` will see the
 * descriptor the kotlinx serialization plugin generates for it:
 * - primitives and `String` use the builtin serial names; unsigned types are inline wrappers of their signed type;
 * - `List`/`Set`/`Map` and primitive arrays use the builtin collection descriptors, with elements named by index;
 * - enums list their entries (named by `@SerialName` or entry name) as `OBJECT` elements carrying the entry annotations;
 * - `@Serializable` value classes are inline with the wrapped property as their single element;
 * - a layout is a class (an object without properties) named by its interface's qualified name, and a sealed layout
 *   has the `type`/`value` elements of kotlinx's sealed serializer, the value listing every non-sealed sub-layout.
 */
internal class ShapeBuilder(private val layouts: LayoutIndex, private val materializer: AnnotationMaterializer) {

    private val layoutShapes = mutableMapOf<String, StaticTypeShape>()

    fun layoutShape(layout: Layout): StaticTypeShape = layoutShapes.getOrPut(layout.qualifiedName) {
        val annotations = materializer.serialInfo(layout.declaration.annotations)
        when {
            layout.isSealed -> StaticTypeShape(
                layout.qualifiedName,
                TypeKind.SEALED,
                annotations = annotations,
                elementsProvider = lazy {
                    val value = StaticTypeShape(
                        "kotlinx.serialization.Sealed<${layout.dataClassName.simpleName}>",
                        TypeKind.CONTEXTUAL,
                        elementsProvider = lazy {
                            layout.sealedLeaves.map { leaf -> StaticTypeShape.Element(leaf.qualifiedName, emptyList()) { layoutShape(leaf) } }
                        },
                    )
                    listOf(
                        StaticTypeShape.Element("type", emptyList()) { primitive("kotlin.String", TypeKind.STRING) },
                        StaticTypeShape.Element("value", emptyList()) { value },
                    )
                },
            )

            else -> StaticTypeShape(
                layout.qualifiedName,
                TypeKind.STRUCTURE,
                annotations = annotations,
                elementsProvider = lazy {
                    layout.properties.map { property ->
                        StaticTypeShape.Element(property.serialName, materializer.serialInfo(property.annotations)) { typeShape(property.type, property.declaration) }
                    }
                },
            )
        }
    }

    fun typeShape(type: KSType, origin: KSNode): StaticTypeShape {
        val expanded = layouts.types.expand(type)
        val shape = nonNullShape(expanded, origin)
        return if (expanded.isMarkedNullable) shape.nullable() else shape
    }

    private fun nonNullShape(type: KSType, origin: KSNode): StaticTypeShape {
        val declaration = type.declaration
        val qualifiedName = declaration.qualifiedName?.asString()
            ?: throw LayoutDeclarationException("Cannot resolve the type $type", origin)
        PRIMITIVES[qualifiedName]?.let { return primitive(qualifiedName, it) }
        UNSIGNED[qualifiedName]?.let { signed -> return inlinePrimitive(qualifiedName, signed, "[UNINITIALIZED]") }
        // Not @Serializable in bytes:integer or bytes:medfloat; shaped as the value class kotlinx would describe, which is
        // what the runtime registry maps by serial name.
        SPECIAL_NUMERICS[qualifiedName]?.let { underlying -> return inlinePrimitive(qualifiedName, underlying, "value") }
        PRIMITIVE_ARRAYS[qualifiedName]?.let { item ->
            return StaticTypeShape(
                qualifiedName,
                TypeKind.LIST,
                elementsProvider = lazyOf(
                    listOf(
                        StaticTypeShape.Element("0", emptyList()) {
                            primitive(item, PRIMITIVES.getValue(item))
                        },
                    ),
                ),
            )
        }
        COLLECTIONS[qualifiedName]?.let { (serialName, kind) ->
            val elements = type.arguments.mapIndexed { index, argument ->
                val argumentType = argument.type?.resolve() ?: throw LayoutDeclarationException("Star projections are not supported in $type", origin)
                StaticTypeShape.Element(index.toString(), emptyList()) { typeShape(argumentType, origin) }
            }
            return StaticTypeShape(serialName, kind, elementsProvider = lazyOf(elements))
        }
        if (declaration !is KSClassDeclaration) throw LayoutDeclarationException("Unsupported type $qualifiedName", origin)
        layouts[declaration]?.let { return layoutShape(it) }
        return when {
            declaration.classKind == ClassKind.ENUM_CLASS -> enumShape(declaration.requireSerializable(origin))

            Modifier.VALUE in declaration.modifiers || Modifier.INLINE in declaration.modifiers -> valueClassShape(declaration.requireSerializable(origin), type, origin)

            // @BytesLayout is not retained in compiled code, so a layout of another module looks like any interface.
            declaration.classKind == ClassKind.INTERFACE && declaration.containingFile == null -> throw LayoutDeclarationException(
                "$qualifiedName is an interface of another module; layouts from another module are not supported yet, " +
                    "and any other interface would be serialized polymorphically, which generated layouts do not support",
                origin,
            )

            declaration.classKind == ClassKind.INTERFACE -> throw LayoutDeclarationException(
                "$qualifiedName is an interface but not a @BytesLayout; open polymorphism is not supported by generated layouts",
                origin,
            )

            else -> throw LayoutDeclarationException(
                "Unsupported type $qualifiedName; use a primitive, String, an unsigned type, a List/Set/Map, a @Serializable enum or value class, or another @BytesLayout",
                origin,
            )
        }
    }

    private fun enumShape(declaration: KSClassDeclaration): StaticTypeShape {
        val serialName = declaration.serialName()
        val entries = declaration.declarations.filterIsInstance<KSClassDeclaration>().filter { it.classKind == ClassKind.ENUM_ENTRY }.map { entry ->
            val entryName = entry.annotations.serialNameOverride() ?: entry.simpleName.asString()
            StaticTypeShape.Element(entryName, materializer.serialInfo(entry.annotations)) { StaticTypeShape("$serialName.$entryName", TypeKind.STRUCTURE) }
        }.toList()
        return StaticTypeShape(serialName, TypeKind.ENUM, annotations = materializer.serialInfo(declaration.annotations), elementsProvider = lazyOf(entries))
    }

    private fun valueClassShape(declaration: KSClassDeclaration, type: KSType, origin: KSNode): StaticTypeShape {
        val parameter = declaration.primaryConstructor?.parameters?.singleOrNull()
            ?: throw LayoutDeclarationException("Value class ${declaration.qualifiedName?.asString()} must have a single constructor property", origin)
        val property = declaration.declarations.filterIsInstance<KSPropertyDeclaration>().firstOrNull { it.simpleName == parameter.name }
            ?: throw LayoutDeclarationException("Value class ${declaration.qualifiedName?.asString()} must declare its constructor parameter as a property", origin)
        val propertyType = layouts.types.expand(property.asMemberOf(type.makeNotNullable()))
        wrappedLayout(declaration, property, propertyType, origin)?.let { layout ->
            return StaticTypeShape(
                declaration.serialName(),
                TypeKind.STRUCTURE,
                isInline = true,
                annotations = materializer.serialInfo(declaration.annotations),
                elementsProvider = lazy {
                    val shape = layoutShape(layout)
                    listOf(
                        StaticTypeShape.Element(property.annotations.serialNameOverride() ?: property.simpleName.asString(), materializer.serialInfo(property.annotations)) {
                            if (propertyType.isMarkedNullable) shape.nullable() else shape
                        },
                    )
                },
            )
        }
        return StaticTypeShape(
            declaration.serialName(),
            TypeKind.STRUCTURE,
            isInline = true,
            annotations = materializer.serialInfo(declaration.annotations),
            elementsProvider = lazy {
                listOf(
                    StaticTypeShape.Element(property.annotations.serialNameOverride() ?: property.simpleName.asString(), materializer.serialInfo(property.annotations)) {
                        typeShape(propertyType, origin)
                    },
                )
            },
        )
    }

    /**
     * The layout a value class wraps, which kotlinx serializes as its interface (open polymorphism) unless the
     * property names the layout's `InterfaceSerializer`, whose descriptor is its DATA class's.
     */
    private fun wrappedLayout(declaration: KSClassDeclaration, property: KSPropertyDeclaration, propertyType: KSType, origin: KSNode): Layout? {
        val layout = (propertyType.declaration as? KSClassDeclaration)?.let { layouts[it] } ?: return null
        val serializer = "${layout.dataClassName.simpleName}.$INTERFACE_SERIALIZER"
        // Written on a constructor property, the annotation belongs to the constructor parameter.
        val parameter = declaration.primaryConstructor?.parameters?.firstOrNull { it.name == property.simpleName }
        val annotations = property.annotations + property.type.annotations + parameter?.annotations.orEmpty()
        val with = annotations.filter { it.qualifiedName() == SERIALIZABLE }.mapNotNull { it.argument("with") as? KSType }.firstOrNull()
        // The DATA class is generated in this round, so the serializer does not resolve yet; the name it is written as does.
        val named = with?.let { if (it.isError) it.toString().removePrefix("<ERROR TYPE: ").removeSuffix(">") else it.declaration.qualifiedName?.asString() }
        if (named == null || !(named == serializer || named.endsWith(".$serializer"))) {
            throw LayoutDeclarationException(
                "Value class ${declaration.qualifiedName?.asString()} wraps the @BytesLayout ${layout.qualifiedName}, which kotlinx.serialization serializes " +
                    "polymorphically; annotate its property with @Serializable(with = $serializer::class)",
                origin,
            )
        }
        return layout
    }

    private fun KSClassDeclaration.requireSerializable(origin: KSNode): KSClassDeclaration {
        val serializable = annotations.firstOrNull { it.qualifiedName() == SERIALIZABLE }
            ?: throw LayoutDeclarationException("${qualifiedName?.asString()} must be @Serializable to be used in a @BytesLayout", origin)
        if (serializable.argument("with").let { it != null && (it as? KSType)?.declaration?.qualifiedName?.asString() != KSERIALIZER }) {
            throw LayoutDeclarationException("${qualifiedName?.asString()} uses a custom serializer, which a generated layout cannot describe", origin)
        }
        return this
    }

    private fun KSClassDeclaration.serialName(): String = annotations.serialNameOverride() ?: qualifiedName?.asString() ?: simpleName.asString()

    private fun primitive(serialName: String, kind: TypeKind) = StaticTypeShape(serialName, kind)

    private fun inlinePrimitive(serialName: String, underlying: String, elementName: String) = StaticTypeShape(
        serialName,
        TypeKind.STRUCTURE,
        isInline = true,
        elementsProvider = lazyOf(listOf(StaticTypeShape.Element(elementName, emptyList()) { builtin(underlying) })),
    )

    private fun builtin(serialName: String): StaticTypeShape = UNSIGNED[serialName]?.let { inlinePrimitive(serialName, it, "[UNINITIALIZED]") }
        ?: primitive(serialName, PRIMITIVES.getValue(serialName))

    internal companion object {
        const val SERIALIZABLE = "kotlinx.serialization.Serializable"
        const val INTERFACE_SERIALIZER = "InterfaceSerializer"

        // The default value of `@Serializable(with = ...)`.
        const val KSERIALIZER = "kotlinx.serialization.KSerializer"

        val PRIMITIVES = mapOf(
            "kotlin.Boolean" to TypeKind.BOOLEAN,
            "kotlin.Byte" to TypeKind.BYTE,
            "kotlin.Short" to TypeKind.SHORT,
            "kotlin.Int" to TypeKind.INT,
            "kotlin.Long" to TypeKind.LONG,
            "kotlin.Float" to TypeKind.FLOAT,
            "kotlin.Double" to TypeKind.DOUBLE,
            "kotlin.Char" to TypeKind.CHAR,
            "kotlin.String" to TypeKind.STRING,
        )

        val UNSIGNED = mapOf(
            "kotlin.UByte" to "kotlin.Byte",
            "kotlin.UShort" to "kotlin.Short",
            "kotlin.UInt" to "kotlin.Int",
            "kotlin.ULong" to "kotlin.Long",
        )

        val SPECIAL_NUMERICS = mapOf(
            "com.splendo.kaluga.bytes.integer.Int24" to "kotlin.Int",
            "com.splendo.kaluga.bytes.integer.UInt24" to "kotlin.UInt",
            "com.splendo.kaluga.bytes.integer.Int40" to "kotlin.Long",
            "com.splendo.kaluga.bytes.integer.UInt40" to "kotlin.ULong",
            "com.splendo.kaluga.bytes.integer.Int48" to "kotlin.Long",
            "com.splendo.kaluga.bytes.integer.UInt48" to "kotlin.ULong",
            "com.splendo.kaluga.bytes.medfloat.MedFloat16" to "kotlin.Double",
            "com.splendo.kaluga.bytes.medfloat.MedFloat32" to "kotlin.Double",
        )

        val PRIMITIVE_ARRAYS = mapOf(
            "kotlin.BooleanArray" to "kotlin.Boolean",
            "kotlin.ByteArray" to "kotlin.Byte",
            "kotlin.ShortArray" to "kotlin.Short",
            "kotlin.IntArray" to "kotlin.Int",
            "kotlin.LongArray" to "kotlin.Long",
            "kotlin.FloatArray" to "kotlin.Float",
            "kotlin.DoubleArray" to "kotlin.Double",
            "kotlin.CharArray" to "kotlin.Char",
        )

        val COLLECTIONS = mapOf(
            "kotlin.collections.List" to ("kotlin.collections.ArrayList" to TypeKind.LIST),
            "kotlin.collections.MutableList" to ("kotlin.collections.ArrayList" to TypeKind.LIST),
            "kotlin.collections.Collection" to ("kotlin.collections.ArrayList" to TypeKind.LIST),
            "kotlin.collections.Set" to ("kotlin.collections.LinkedHashSet" to TypeKind.LIST),
            "kotlin.collections.MutableSet" to ("kotlin.collections.LinkedHashSet" to TypeKind.LIST),
            "kotlin.collections.Map" to ("kotlin.collections.LinkedHashMap" to TypeKind.MAP),
            "kotlin.collections.MutableMap" to ("kotlin.collections.LinkedHashMap" to TypeKind.MAP),
            "kotlin.Array" to ("kotlin.Array" to TypeKind.LIST),
        )
    }
}

/** Whether this annotation's class is meta-annotated with `@SerialInfo`, i.e. kotlinx copies it into descriptors. */
internal fun KSAnnotation.isSerialInfo(): Boolean = (annotationType.resolve().declaration as? KSClassDeclaration)?.isSerialInfo() == true

internal fun KSClassDeclaration.isSerialInfo(): Boolean = annotations.any { it.qualifiedName() == "kotlinx.serialization.SerialInfo" }
