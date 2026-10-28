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
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.Generate
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.layout.CollectionSettings
import com.splendo.kaluga.bytes.layout.FieldLayout
import com.splendo.kaluga.bytes.layout.FrameSettings
import com.splendo.kaluga.bytes.layout.NumericSettings
import com.splendo.kaluga.bytes.layout.TypeKind
import com.splendo.kaluga.bytes.layout.TypeLayout
import com.splendo.kaluga.bytes.layout.dump
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * Plans the VIEW of each layout: which compiled layout contexts it needs and where every property sits in their bytes.
 *
 * Positions follow what `BytesFormat` writes. A structure's frame is its prefix, then its flag header bits, then its
 * body, then its postfix. Body Booleans continue in the bits after the flag header; anything written as bytes starts
 * at the next byte boundary. A property whose presence or size is recorded in the flag header moves what follows it
 * at runtime, so positions after it are relative to an [Anchor]; everything else is constant-folded. Layout features
 * not planned yet are an [UnsupportedViewFeature].
 */
internal class ViewPlanner(private val layouts: LayoutIndex, private val materializer: AnnotationMaterializer) {

    private val contexts = mutableMapOf<String, MutableList<ViewContext>>()
    private val planning = mutableSetOf<ViewContext>()

    // Whether the frame being planned is accumulated MOST_SIGNIFICANT_FIRST, which mirrors its item frames too.
    private var mirrored = false

    /** Registers the root context of [layout], compiled as [root]; call for every VIEW layout before planning any. */
    fun registerRoot(layout: Layout, root: FieldLayout) {
        contexts.getOrPut(layout.qualifiedName) { mutableListOf() }.add(0, ViewContext(layout, root, root.contextFingerprint(), 0))
    }

    fun root(layout: Layout): ViewContext? = contexts[layout.qualifiedName]?.firstOrNull { it.index == 0 }

    /** Every context of [layout] that planned successfully, the root first. */
    fun plannedContexts(layout: Layout): List<ViewContext> = contexts[layout.qualifiedName].orEmpty().filter { it.isPlanned }.sortedBy { it.index }

    fun plan(context: ViewContext): Result<ViewPlan> {
        context.plan?.let { return it }
        if (!planning.add(context)) return Result.failure(UnsupportedViewFeature("recursive layouts", context.layout.declaration))
        val result = try {
            Result.success(planFrame(context))
        } catch (e: UnsupportedViewFeature) {
            Result.failure(e)
        } catch (e: LayoutDeclarationException) {
            Result.failure(e)
        } finally {
            planning.remove(context)
        }
        context.plan = result
        return result
    }

    /** Plans [context], a structure or a sealed layout, reporting only whether it could be planned. */
    fun planAny(context: ViewContext): Result<Unit> = (if (context.layout.isSealed) planSealed(context) else plan(context)).map { }

    fun planSealed(context: ViewContext): Result<SealedPlan> {
        context.sealedPlan?.let { return it }
        if (!planning.add(context)) return Result.failure(UnsupportedViewFeature("recursive layouts", context.layout.declaration))
        val result = try {
            Result.success(planSealedFrame(context))
        } catch (e: UnsupportedViewFeature) {
            Result.failure(e)
        } catch (e: LayoutDeclarationException) {
            Result.failure(e)
        } finally {
            planning.remove(context)
        }
        context.sealedPlan = result
        return result
    }

    private fun planSealedFrame(context: ViewContext): SealedPlan {
        val layout = context.layout
        val field = context.field
        val type = field.type as? TypeLayout.Sealed ?: unsupported("a sealed layout compiling to ${field.type::class.simpleName}", layout.declaration)
        if (field.frame.byteStuffing != null) unsupported("byte stuffing on a sealed layout", layout.declaration)
        if (field.byteOrder != ByteOrder.LEAST_SIGNIFICANT_FIRST) unsupported("frames accumulated MOST_SIGNIFICANT_FIRST", layout.declaration)
        if (field.headerBits > 0) unsupported("flag bits on a sealed layout", layout.declaration)
        val subtypes = type.elements[1].type.children
        val leaves = subtypes.map { subtype ->
            val leaf = layouts.all.firstOrNull { it.qualifiedName == subtype.name } ?: throw LayoutDeclarationException("${subtype.name} is not a @BytesLayout", layout.declaration)
            SealedLeaf(leaf, contextFor(leaf, subtype, leaf.declaration), type.discriminators[subtype.name]?.array)
        }
        val bySize = type.sizeDispatch.isNotEmpty() || type.sizeFallback != null
        val sizeDispatch = if (bySize) type.sizeDispatch.mapValues { (_, name) -> leaves.indexOfFirst { it.layout.qualifiedName == name } } else null
        val fallback = type.sizeFallback?.let { name -> leaves.indexOfFirst { it.layout.qualifiedName == name } }
        if (!bySize && leaves.any { it.discriminator == null || it.discriminator.isEmpty() }) unsupported("sealed subtypes without a discriminator", layout.declaration)
        return SealedPlan(field.frame.prefix?.array, field.frame.postfix?.array, leaves, sizeDispatch, fallback, layout.qualifiedName, field.frame.checksum, field.valueByteOrder)
    }

    private fun sealedContextFor(layout: Layout, field: FieldLayout, node: KSNode): ViewContext {
        val root = root(layout) ?: throw LayoutDeclarationException("A VIEW layout uses the @BytesLayout ${layout.qualifiedName}, so it must generate VIEW too", node)
        planSealed(root).exceptionOrNull()?.let { throw it }
        val fingerprint = field.contextFingerprint()
        val known = contexts.getValue(layout.qualifiedName)
        val context = known.firstOrNull { it.fingerprint == fingerprint } ?: ViewContext(layout, field, fingerprint, known.size).also { known += it }
        planSealed(context).exceptionOrNull()?.let { throw it }
        return context
    }

    private fun contextFor(layout: Layout, field: FieldLayout, node: KSNode): ViewContext {
        // BytesFormat only stuffs the root frame it encodes.
        if (field.frame.byteStuffing != null) unsupported("byte stuffing on a nested layout", node)
        val root = root(layout) ?: throw LayoutDeclarationException("A VIEW layout uses the @BytesLayout ${layout.qualifiedName}, so it must generate VIEW too", node)
        plan(root).exceptionOrNull()?.let { throw it }
        val fingerprint = field.contextFingerprint()
        val known = contexts.getValue(layout.qualifiedName)
        val context = known.firstOrNull { it.fingerprint == fingerprint } ?: ViewContext(layout, field, fingerprint, known.size).also { known += it }
        plan(context).exceptionOrNull()?.let { throw it }
        return context
    }

    private fun planFrame(context: ViewContext): ViewPlan {
        val outer = mirrored
        mirrored = context.field.byteOrder == ByteOrder.MOST_SIGNIFICANT_FIRST
        try {
            return planFrameIn(context)
        } finally {
            mirrored = outer
        }
    }

    private fun planFrameIn(context: ViewContext): ViewPlan {
        val layout = context.layout
        val field = context.field
        if (layout.isSealed) unsupported("sealed layouts", layout.declaration)
        val stuffing = field.frame.byteStuffing
        if (stuffing != null && context.index != 0) unsupported("byte stuffing on a nested layout", layout.declaration)
        if (stuffing != null && mirrored) unsupported("byte stuffing on a frame accumulated MOST_SIGNIFICANT_FIRST", layout.declaration)
        val children = (field.type as? TypeLayout.Structure)?.fields ?: unsupported("a layout compiling to ${field.type::class.simpleName}", layout.declaration)
        val properties = layout.properties
        check(children.size == properties.size) { "${layout.qualifiedName} compiled to ${children.size} fields for ${properties.size} properties" }

        val prefix = field.frame.prefix?.array
        val postfix = field.frame.postfix?.array
        val headerStart = (prefix?.size ?: 0) * Byte.SIZE_BITS
        // The flag header covers the bits the children own and the bits a property's presence is derived from.
        val headerBits = field.headerBits
        val gates = children.flatMap { it.presenceFlagIndices }.toSet()

        var cursor = Cursor(null, headerStart + headerBits)
        val fields = children.mapIndexed { index, child ->
            val property = properties[index]
            val node = property.declaration
            if (property.name in RESERVED_VIEW_PROPERTY_NAMES) unsupported("a property named '${property.name}'", node)
            val value = valueType(property.type.makeNotNullable(), node)
            val itemPrefix = context.layoutName.simpleName + property.name.capitalized()
            val (plan, next) = planField(property.name, node, child, value, property.type.isMarkedNullable, headerStart, gates, cursor, itemPrefix, context.layoutName.packageName)
            cursor = next
            plan
        }
        if (mirrored) fields.forEach { checkMirrored(it) }
        val plan = ViewPlan(prefix, postfix, headerStart, headerBits, fields, cursor, field.frame.checksum, field.valueByteOrder, mirrored, stuffing)
        return plan
    }

    /** What mirrored frames support so far: values at fixed or content-dependent sizes, no flag bits and no nested frames. */
    private fun checkMirrored(field: FieldPlan) {
        val access = field.access
        val supported = field.presence == null && when (access) {
            is Access.Number -> access.width is Width.Fixed
            is Access.Character, is Access.Text -> true
            is Access.EnumTable -> access.fixedBytes != null
            is Access.Collection -> access.emptyBit == null && access.count !is Count.Flag && (access.count as? Count.Number)?.width !is Width.Selected
            else -> false
        }
        if (!supported) unsupported("${access::class.simpleName?.lowercase()} values in a frame accumulated MOST_SIGNIFICANT_FIRST", field.node)
        if (access is Access.Collection && field.field.valueByteOrder != ByteOrder.MOST_SIGNIFICANT_FIRST) {
            unsupported("collections of another byte order in a frame accumulated MOST_SIGNIFICANT_FIRST", field.node)
        }
    }

    /** Plans one property starting at [cursor], returning it and the cursor after it. */
    private fun planField(
        name: String,
        node: KSNode,
        child: FieldLayout,
        value: ValueType,
        typeNullable: Boolean,
        headerStart: Int,
        gates: Set<Int>,
        from: Cursor,
        itemPrefix: String,
        packageName: String,
    ): Pair<FieldPlan, Cursor> {
        var cursor = from
        val isCollection = value is ValueType.Collection
        // A @NullIfEmpty collection compiles as nullable whatever its type; its flag bit then only says whether it is empty.
        val nullable = if (isCollection) typeNullable else child.isNullable || child.presenceFlagIndices.isNotEmpty()
        // The padding of a nullable property is only on the wire while it is present, which the planned positions
        // around it do not follow yet.
        if (child.isNullable && (child.reservedBefore > 0 || child.reservedAfter > 0)) unsupported("@Reserved on a nullable property", node)
        if (child.reservedBefore > 0) cursor = cursor.aligned().plus(child.reservedBefore * Byte.SIZE_BITS)

        val presence = when {
            child.presenceFlagIndices.isNotEmpty() -> Presence.AllSet(child.presenceFlagIndices.map { headerStart + it })
            nullable -> Presence.Owned(headerStart + child.bitIndex)
            else -> null
        }
        // The encoder starts every nested frame, and every value class frame, on a fresh byte, as the decoder reads it.
        val start = if (value is ValueType.Nested || (value is ValueType.Wrapped && value.framed)) cursor.aligned() else cursor
        val access = if (value is ValueType.Collection) {
            collectionAccess(child, value, typeNullable, node, headerStart, start, itemPrefix, packageName)
        } else {
            access(child, value, node, headerStart)
        }
        val content = contentBits(access)
        val end = if (content == null || (presence != null && content != 0)) {
            val aligned = when {
                access is Access.BodyBit -> false
                access is Access.Collection -> (presence == null && access.emptyBit == null && access.count !is Count.Flag && access.count !is Count.Unmarked) || start.isAligned
                else -> presence == null || start.isAligned
            }
            Anchor.End(name, aligned)
        } else {
            null
        }
        cursor = when {
            end != null -> Cursor(end, 0)
            content == 0 -> start
            access is Access.BodyBit -> cursor.plus(1)
            else -> cursor.aligned().plus(content!!)
        }
        if (child.reservedAfter > 0) cursor = cursor.aligned().plus(child.reservedAfter * Byte.SIZE_BITS)

        val owned = if (child.bitIndex >= 0) (child.bitIndex until child.bitIndex + child.bitWidth).toSet() else emptySet()
        val settable = !nullable && end == null && owned.none { it in gates }
        return FieldPlan(name, node, child, value, nullable, access, presence, start, end, settable) to cursor
    }

    private fun collectionAccess(
        child: FieldLayout,
        value: ValueType.Collection,
        typeNullable: Boolean,
        node: KSNode,
        headerStart: Int,
        start: Cursor,
        itemPrefix: String,
        packageName: String,
    ): Access.Collection {
        val (settings, items) = when (val type = child.type) {
            is TypeLayout.ListLayout -> type.settings to listOf(type.item)
            is TypeLayout.MapLayout -> type.settings to listOf(type.key, type.value)
            else -> unsupported("a collection compiling to ${type::class.simpleName}", node)
        }
        // BytesFormat stuffs only collections it builds least significant first.
        if (settings.byteStuffing != null && (mirrored || child.valueByteOrder != ByteOrder.LEAST_SIGNIFICANT_FIRST)) {
            unsupported("byte-stuffed collections encoded MOST_SIGNIFICANT_FIRST", node)
        }
        val offset = if (child.isNullable) 1 else 0
        val count = when (val marking = settings.lengthMarking) {
            is CollectionSettings.LengthPrefix -> Count.Prefix(marking.endMarking, child.valueByteOrder)

            is CollectionSettings.NumericLength -> {
                val lengths = marking.supportedLengths.toList()
                val width = if (lengths.size ==
                    1
                ) {
                    Width.Fixed(lengths.single())
                } else {
                    Width.Selected(headerStart + child.bitIndex + offset, child.bitWidth - offset, lengths, Width.Selected.Kind.INDEX)
                }
                Count.Number(width, child.valueByteOrder, child.valueByteOrder)
            }

            is CollectionSettings.FlagIndexedLength -> {
                if (child.isNullable) unsupported("@NullIfEmpty on a collection counted in the flag header", node)
                Count.Flag(headerStart + child.bitIndex, marking.bits)
            }

            is CollectionSettings.TerminalMarked -> settings.byteStuffing?.let { Count.Stuffed(it, marking.terminator) } ?: Count.Terminated(marking.terminator)

            is CollectionSettings.Unmarked -> Count.Unmarked
        }
        val roles = if (value.kind == ValueType.Collection.Kind.MAP) listOf("Key", "Value") else listOf("Item")
        val frames = items.mapIndexed { index, item ->
            val itemValue = if (index == 0) value.item else value.value!!
            val itemNullable = if (index == 0) value.itemNullable else value.valueNullable
            itemFrame(ClassName(packageName, itemPrefix + roles[index]), roles[index].lowercase(), item, itemValue, itemNullable, node)
        }
        // Items of a stuffed collection are read from an unstuffed copy, where a layout's setters would write in vain.
        if (settings.byteStuffing != null && frames.any { it.field.access is Access.Nested || it.field.access is Access.Sealed }) {
            unsupported("layouts as items of byte-stuffed collections", node)
        }
        val first = frames.first().plan
        // Without count bytes before it, whether the first item frame starts on a fresh byte depends on the content: the
        // encoder only closes a partly filled byte of the parent's body Booleans when the collection writes any bytes.
        if (!start.isAligned && (count is Count.Flag || count is Count.Terminated || count is Count.Unmarked) && first.prefix == null &&
            (first.headerBits > 0 || first.fields.single().access is Access.BodyBit)
        ) {
            unsupported("a collection starting with bits after body Booleans", node)
        }
        return Access.Collection(count, frames, if (settings.nullIfEmpty) headerStart + child.bitIndex else null)
    }

    private fun itemFrame(name: ClassName, role: String, item: FieldLayout, value: ValueType, nullable: Boolean, node: KSNode): ItemFrame {
        if (value is ValueType.Collection) unsupported("collections of collections", node)
        // The item frame is only the item's flag bits and body: the item writes its own prefix, postfix and checksum.
        val headerBits = (item.bitIndex + item.bitWidth).coerceAtLeast(0)
        val (field, end) = planField(role, node, item, value, nullable, 0, emptySet(), Cursor(null, headerBits), name.simpleName, name.packageName)
        if (mirrored) {
            // The encoder builds an item into its collection's builder only in the same order; otherwise into a separate array.
            if (item.byteOrder != ByteOrder.MOST_SIGNIFICANT_FIRST) unsupported("items of another byte order in a frame accumulated MOST_SIGNIFICANT_FIRST", node)
            checkMirrored(field)
        }
        val plan = ViewPlan(null, null, 0, headerBits, listOf(field), end, mirrored = mirrored)
        if (plan.staticSize == 0) unsupported("collection items that encode to no bytes", node)
        return ItemFrame(name, plan)
    }

    /** The body bits [access] takes when that does not depend on the content, `null` when it does. */
    private fun contentBits(access: Access): Int? = when (access) {
        is Access.HeaderBit, is Access.PackedNumber, is Access.PackedEnum -> 0

        is Access.BodyBit -> 1

        is Access.Number -> (access.width as? Width.Fixed)?.length?.bytes?.times(Byte.SIZE_BITS)

        is Access.Character -> access.encoding.byteSize * Byte.SIZE_BITS

        is Access.Text -> access.fixedBytes?.times(Byte.SIZE_BITS)

        is Access.EnumTable -> access.fixedBytes?.times(Byte.SIZE_BITS)

        is Access.Nested -> access.context.plan!!.getOrThrow().staticSize?.times(Byte.SIZE_BITS)

        is Access.Collection, is Access.Sealed -> null

        is Access.Framed -> contentBits(access.inner)?.let { inner ->
            val frame = (access.prefix?.size ?: 0) + (access.checksum?.crc?.byteWidth ?: 0) + (access.postfix?.size ?: 0)
            (frame + if (access.inner is Access.BodyBit) 1 else inner / Byte.SIZE_BITS) * Byte.SIZE_BITS
        }
    }

    private fun access(child: FieldLayout, value: ValueType, node: KSNode, headerStart: Int): Access {
        if (value is ValueType.Nested) {
            if (value.layout.isSealed) return Access.Sealed(sealedContextFor(value.layout, child, node))
            if (child.type !is TypeLayout.Structure) unsupported("polymorphic properties", node)
            return Access.Nested(contextFor(value.layout, child, node))
        }
        if (value is ValueType.Wrapped && value.framed) {
            val inner = leafAccess(child, value, null, headerStart, node)
            if ((inner as? Access.Number)?.width is Width.Selected) unsupported("numbers with several sizes inside a value class with a prefix or postfix", node)
            if (inner is Access.Text && inner.fixedBytes == null) unsupported("variable length strings inside a value class with a prefix or postfix", node)
            if (inner is Access.EnumTable && inner.fixedBytes == null) unsupported("enums of varying size inside a value class with a prefix or postfix", node)
            return Access.Framed(child.frame.prefix?.array, inner, child.frame.checksum, child.valueByteOrder, child.frame.postfix?.array)
        }
        return leafAccess(child, value, child.frame, headerStart, node)
    }

    private fun leafAccess(child: FieldLayout, value: ValueType, enumFrame: FrameSettings?, headerStart: Int, node: KSNode): Access {
        // As in the encoder, the first bit of a nullable property is its presence bit.
        val offset = if (child.isNullable) 1 else 0
        val ownsBits = child.bitIndex >= 0 && child.bitWidth > offset
        val bitPos = headerStart + child.bitIndex + offset
        return when (val type = child.type) {
            is TypeLayout.Bool -> if (ownsBits) Access.HeaderBit(bitPos) else Access.BodyBit

            is TypeLayout.Numeric -> {
                val settings = type.settings
                val packedBits = (settings as? NumericSettings.Natural)?.inFlagsBits
                if (packedBits != null) {
                    Access.PackedNumber(headerStart + child.bitIndex, packedBits, settings.signed)
                } else {
                    val lengths = settings.supportedLengths.toList()
                    val width = when {
                        lengths.size == 1 -> Width.Fixed(lengths.single())
                        settings is NumericSettings.Decimal || settings is NumericSettings.MedFloat -> Width.Selected(bitPos, 1, lengths, Width.Selected.Kind.LARGER)
                        else -> Width.Selected(bitPos, child.bitWidth - offset, lengths, Width.Selected.Kind.INDEX)
                    }
                    Access.Number(width, settings, child.valueByteOrder, child.valueByteOrder)
                }
            }

            is TypeLayout.Character -> Access.Character(type.encoding, child.byteOrder, child.valueByteOrder)

            is TypeLayout.Enumeration -> if (ownsBits) {
                Access.PackedEnum(bitPos, child.bitWidth - offset)
            } else {
                val table = type.entries.indices.map { ordinal -> type.values[ordinal]?.array ?: unsupported("enums without a value for every entry", node) }
                if (table.isEmpty() || table.any { it.isEmpty() }) unsupported("enums with an empty identifier", node)
                Access.EnumTable(enumFrame?.prefix?.array, table, enumFrame?.postfix?.array)
            }

            is TypeLayout.Text -> {
                val endMarking = type.settings.endMarking
                if (endMarking is StringEncodingSettings.ByteStuffed && mirrored) unsupported("byte-stuffed strings in a frame accumulated MOST_SIGNIFICANT_FIRST", node)
                // A string is laid out in the frame's accumulation order, which a mirrored frame reads from the end.
                Access.Text(type.settings.encoding, endMarking, child.byteOrder)
            }

            is TypeLayout.ListLayout, is TypeLayout.MapLayout -> unsupported("collections", node)

            is TypeLayout.Sealed, is TypeLayout.Open -> unsupported("polymorphic properties", node)

            is TypeLayout.Structure, is TypeLayout.Contextual -> unsupported("value classes wrapping a layout", node)
        }.also { access ->
            val expected = when (access) {
                is Access.HeaderBit, is Access.BodyBit -> TypeKind.BOOLEAN
                is Access.Character -> TypeKind.CHAR
                is Access.Text -> TypeKind.STRING
                is Access.PackedEnum, is Access.EnumTable -> TypeKind.ENUM
                else -> null
            }
            if (expected != null && expected != value.leafKind) unsupported("a ${value.typeName} property encoded as ${expected.name.lowercase()}", node)
            if ((access is Access.Number || access is Access.PackedNumber) && !value.leafKind.isNumeric) {
                unsupported("a ${value.typeName} property encoded as a number", node)
            }
        }
    }

    /** The [ValueType] of a property of [type], resolved like [ShapeBuilder] shapes it. */
    fun valueType(type: KSType, origin: KSNode): ValueType = expandedValueType(layouts.types.expand(type), origin)

    private fun expandedValueType(type: KSType, origin: KSNode): ValueType {
        val declaration = type.declaration
        val qualifiedName = declaration.qualifiedName?.asString() ?: throw LayoutDeclarationException("Cannot resolve the type $type", origin)
        builtin(qualifiedName, origin)?.let { return it }
        ShapeBuilder.PRIMITIVE_ARRAYS[qualifiedName]?.let { item ->
            return ValueType.Collection(type.toTypeName(), ValueType.Collection.Kind.PRIMITIVE_ARRAY, builtin(item, origin)!!, itemNullable = false)
        }
        if (qualifiedName in ShapeBuilder.COLLECTIONS) {
            val kind = when (qualifiedName) {
                "kotlin.collections.List", "kotlin.collections.Collection" -> ValueType.Collection.Kind.LIST
                "kotlin.collections.Set" -> ValueType.Collection.Kind.SET
                "kotlin.collections.Map" -> ValueType.Collection.Kind.MAP
                "kotlin.Array" -> ValueType.Collection.Kind.ARRAY
                else -> unsupported("mutable collections", origin)
            }
            val arguments = type.arguments.map { it.type?.resolve() ?: throw LayoutDeclarationException("Star projections are not supported in $type", origin) }
            val item = arguments[0]
            val value = arguments.getOrNull(1)
            return ValueType.Collection(
                type.toTypeName(),
                kind,
                valueType(item.makeNotNullable(), origin),
                item.isMarkedNullable,
                value?.let { valueType(it.makeNotNullable(), origin) },
                value?.isMarkedNullable == true,
            )
        }
        if (declaration !is KSClassDeclaration) throw LayoutDeclarationException("Unsupported type $qualifiedName", origin)
        layouts[declaration]?.let { return ValueType.Nested(it.interfaceClassName, it) }
        return when {
            declaration.classKind == ClassKind.ENUM_CLASS -> ValueType.Enumeration(
                declaration.toClassName(),
                declaration.annotations.serialNameOverride() ?: qualifiedName,
            )

            Modifier.VALUE in declaration.modifiers || Modifier.INLINE in declaration.modifiers -> {
                val parameter = declaration.primaryConstructor?.parameters?.singleOrNull()
                    ?: throw LayoutDeclarationException("Value class $qualifiedName must have a single constructor property", origin)
                val property = declaration.declarations.filterIsInstance<KSPropertyDeclaration>().firstOrNull { it.simpleName == parameter.name }
                    ?: throw LayoutDeclarationException("Value class $qualifiedName must declare its constructor parameter as a property", origin)
                val frame = materializer.serialInfo(declaration.annotations)
                ValueType.Wrapped(
                    type.toTypeName(),
                    declaration.toClassName(),
                    property.simpleName.asString(),
                    property.asMemberOf(type.makeNotNullable()).also { if (it.isMarkedNullable) unsupported("value classes wrapping a nullable value", origin) }
                        .let { valueType(it, origin) },
                    hasFrame = frame.any { it is Prefix || it is Postfix || it is Checksum },
                )
            }

            else -> throw LayoutDeclarationException("Unsupported type $qualifiedName in a VIEW layout", origin)
        }
    }

    private fun builtin(qualifiedName: String, origin: KSNode): ValueType? {
        ShapeBuilder.PRIMITIVES[qualifiedName]?.let { kind ->
            return ValueType.Primitive(ClassName.bestGuess(qualifiedName), kind)
        }
        ShapeBuilder.UNSIGNED[qualifiedName]?.let { signed ->
            return ValueType.Unsigned(ClassName.bestGuess(qualifiedName), builtin(signed, origin) as ValueType.Primitive)
        }
        ShapeBuilder.SPECIAL_NUMERICS[qualifiedName]?.let { underlying ->
            val className = ClassName.bestGuess(qualifiedName)
            return ValueType.Wrapped(className, className, "value", builtin(underlying, origin)!!, hasFrame = false)
        }
        return null
    }

    /** Checks that every layout [layout] nests generates VIEW; a VIEW needs their slices. */
    fun checkViewReferences(layout: Layout) {
        if (layout.isSealed) return
        layout.properties.forEach { property ->
            val declaration = property.type.declaration as? KSClassDeclaration ?: return@forEach
            val referenced = layouts[declaration] ?: return@forEach
            if (Generate.VIEW !in referenced.generate) {
                throw LayoutDeclarationException(
                    "${layout.qualifiedName} generates VIEW, so the @BytesLayout ${referenced.qualifiedName} it uses must generate VIEW too",
                    property.declaration,
                )
            }
        }
    }
}

private fun unsupported(feature: String, node: KSNode?): Nothing = throw UnsupportedViewFeature(feature, node)

/**
 * The dump of this layout without what only its parent uses (its name, position, presence and padding), so the same
 * layout nested in two places, or used as a root, is recognized as one context.
 */
private fun FieldLayout.contextFingerprint(): String =
    copy(name = "", index = 0, bitIndex = -1, bitWidth = 0, isNullable = false, presenceFlagIndices = emptyList(), reservedBefore = 0, reservedAfter = 0).dump()
