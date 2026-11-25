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

package com.splendo.kaluga.bytes.layout

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.stuffing.ByteStuffingScheme
import com.splendo.kaluga.bytes.stuffing.DelimiterByteStuffingScheme
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.stuffing.NonDelimiterByteStuffingScheme
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.ByOrdinal
import com.splendo.kaluga.bytes.annotations.ByteStuffed
import com.splendo.kaluga.bytes.annotations.ByteStuffedCobs
import com.splendo.kaluga.bytes.annotations.ByteStuffedSlip
import com.splendo.kaluga.bytes.annotations.ByteStuffedXor
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.Count
import com.splendo.kaluga.bytes.annotations.DeltaCoded
import com.splendo.kaluga.bytes.annotations.Discriminator
import com.splendo.kaluga.bytes.annotations.Encoded
import com.splendo.kaluga.bytes.annotations.Fallback
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.FlagWidth
import com.splendo.kaluga.bytes.annotations.ItemByteOrder
import com.splendo.kaluga.bytes.annotations.ItemEncoded
import com.splendo.kaluga.bytes.annotations.ItemLengthPrefix
import com.splendo.kaluga.bytes.annotations.ItemMedFloat
import com.splendo.kaluga.bytes.annotations.ItemNullTerminated
import com.splendo.kaluga.bytes.annotations.ItemScalar
import com.splendo.kaluga.bytes.annotations.ItemSize
import com.splendo.kaluga.bytes.annotations.ItemUnsigned
import com.splendo.kaluga.bytes.annotations.KeyByteOrder
import com.splendo.kaluga.bytes.annotations.KeyEncoded
import com.splendo.kaluga.bytes.annotations.KeyLengthPrefix
import com.splendo.kaluga.bytes.annotations.KeyMedFloat
import com.splendo.kaluga.bytes.annotations.KeyNullTerminated
import com.splendo.kaluga.bytes.annotations.KeyScalar
import com.splendo.kaluga.bytes.annotations.KeySize
import com.splendo.kaluga.bytes.annotations.KeyUnsigned
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.MedFloat
import com.splendo.kaluga.bytes.annotations.NullIfEmpty
import com.splendo.kaluga.bytes.annotations.NullTerminated
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.PresentWhenAllSet
import com.splendo.kaluga.bytes.annotations.Range
import com.splendo.kaluga.bytes.annotations.Reserved
import com.splendo.kaluga.bytes.annotations.Scalar
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.annotations.Terminal
import com.splendo.kaluga.bytes.annotations.Trailing
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.annotations.ValueByteOrder
import com.splendo.kaluga.bytes.annotations.ValueEncoded
import com.splendo.kaluga.bytes.annotations.ValueLengthPrefix
import com.splendo.kaluga.bytes.annotations.ValueMedFloat
import com.splendo.kaluga.bytes.annotations.ValueNullTerminated
import com.splendo.kaluga.bytes.annotations.ValueScalar
import com.splendo.kaluga.bytes.annotations.ValueSize
import com.splendo.kaluga.bytes.annotations.ValueUnsigned
import com.splendo.kaluga.bytes.strings.toByteArray

/**
 * Compiles a [TypeShape] and its annotations into a [FieldLayout].
 *
 * Compilation is deterministic: the same shape always compiles to an equal layout with the same [FieldLayout.dump].
 * It does not cache; callers compiling the same type repeatedly should.
 */
@InternalBytesApi
object LayoutCompiler {

    private class DesiredFlagBitWidth(var width: Int) {
        fun raise(value: Int) {
            width += value
        }
    }

    /**
     * Compiles [shape] as a root type.
     * @throws kotlinx.serialization.SerializationException (one of the layout exceptions) when the annotations describe an invalid layout.
     */
    fun compile(shape: TypeShape): FieldLayout {
        // Seed the root preferred orders from the type's own @ByteOrder: preferredByteOrder is the field
        // encoding direction, preferredBuilderByteOrder the accumulation direction. The latter stays LSB when
        // excludeStructure = true so the frame is still built sequentially.
        val byteOrderAnnotation = shape.annotations.filterIsInstance<com.splendo.kaluga.bytes.annotations.ByteOrder>().firstOrNull()

        return compileField(
            shape,
            shape.serialName,
            0,
            emptyList(),
            shape.isNullable,
            0,
            byteOrderAnnotation?.order ?: ByteOrder.LEAST_SIGNIFICANT_FIRST,
            byteOrderAnnotation?.takeIf { !it.excludeStructure }?.order ?: ByteOrder.LEAST_SIGNIFICANT_FIRST,
        ) {
        }
    }

    private fun compileField(
        shape: TypeShape,
        fieldName: String,
        fieldIndex: Int,
        fieldAnnotations: List<Annotation>,
        isNullable: Boolean,
        defaultBitIndex: Int,
        preferredByteOrder: ByteOrder,
        // preferredBuilderByteOrder tracks the accumulation direction separately from preferredByteOrder
        // (which tracks the field value encoding direction). They differ when a parent uses excludeStructure=true:
        // - preferredByteOrder   = parent.valueByteOrder (MSB: field values encode big-endian)
        // - preferredBuilderByteOrder = parent.byteOrder (LSB: frame is still built sequentially)
        // Structures without an explicit @ByteOrder annotation inherit the builder direction so they
        // accumulate in the same direction as their parent, regardless of the field encoding byte order.
        preferredBuilderByteOrder: ByteOrder = preferredByteOrder,
        reserveIndices: (Set<Int>) -> Unit,
    ): FieldLayout = if (shape.isInline) {
        // Inline types forward to their underlying (non-inline) element.
        val inlineShape = shape.element(0)
        val annotations = shape.annotations + fieldAnnotations + shape.elementAnnotations(0)
        // Unsigned and odd-width numbers are inline wrappers around a primary type; since widths are flexible, keep that information.
        // A nullable shape's serialName may carry a trailing "?", so match on the name without it — otherwise a
        // nullable UByte/UInt/... would silently lose its Unsigned() and sign-extend once widened.
        val actualAnnotations = when (shape.serialName.removeSuffix("?")) {
            "com.splendo.kaluga.bytes.medfloat.MedFloat16" -> annotations + MedFloat() + Size(Length.`16_BIT`)
            "com.splendo.kaluga.bytes.medfloat.MedFloat32" -> annotations + MedFloat() + Size(Length.`32_BIT`)
            "com.splendo.kaluga.bytes.integer.Int24" -> annotations + Size(Length.`24_BIT`)
            "com.splendo.kaluga.bytes.integer.UInt24" -> annotations + Unsigned() + Size(Length.`24_BIT`)
            "com.splendo.kaluga.bytes.integer.Int40" -> annotations + Size(Length.`40_BIT`)
            "com.splendo.kaluga.bytes.integer.UInt40" -> annotations + Unsigned() + Size(Length.`40_BIT`)
            "com.splendo.kaluga.bytes.integer.Int48" -> annotations + Size(Length.`48_BIT`)
            "com.splendo.kaluga.bytes.integer.UInt48" -> annotations + Unsigned() + Size(Length.`48_BIT`)
            "kotlin.UByte" -> annotations + Unsigned()
            "kotlin.UShort" -> annotations + Unsigned()
            "kotlin.UInt" -> annotations + Unsigned()
            "kotlin.ULong" -> annotations + Unsigned()
            else -> annotations
        }
        // Prefix, Postfix and Checksum describe the outer boundary of the value class, not its
        // underlying content. Strip them before forwarding so they are not applied to the body,
        // then re-apply them as the frame of the returned layout.
        val forwardedAnnotations = actualAnnotations.filterNot { it is Prefix || it is Postfix || it is Checksum }
        val underlying = compileField(
            inlineShape,
            fieldName,
            fieldIndex,
            forwardedAnnotations,
            isNullable || inlineShape.isNullable,
            defaultBitIndex,
            preferredByteOrder,
            reserveIndices = reserveIndices,
            preferredBuilderByteOrder = preferredBuilderByteOrder,
        )
        val valueClassFrame = frameSettings(shape.annotations)
        if (valueClassFrame.prefix != null || valueClassFrame.postfix != null || valueClassFrame.checksum != null) {
            underlying.copy(frame = valueClassFrame)
        } else {
            underlying
        }
    } else {
        val annotations = shape.annotations + fieldAnnotations
        val desiredFlagBitWidth = DesiredFlagBitWidth(0)
        val kind = shape.kind
        if (kind != TypeKind.LIST && kind != TypeKind.MAP && annotations.any { it is Count }) {
            throw InvalidCountLayout("@Count only applies to a collection, but '$fieldName' is ${shape.serialName}")
        }
        // A set would drop the items a step of 0 repeats.
        if ((kind != TypeKind.LIST || shape.serialName.removeSuffix("?") in SET_SERIAL_NAMES) && annotations.any { it is DeltaCoded }) {
            throw InvalidDeltaCoding("@DeltaCoded only applies to a list of integers, but '$fieldName' is ${shape.serialName}")
        }

        // A property whose presence is derived from other flag bits owns no bit of its own.
        val presenceFlagIndices = annotations.filterIsInstance<PresentWhenAllSet>().firstOrNull()?.indices?.toList().orEmpty()

        // Nullable elements will have a flag bit. This is always the first bit of the flags for this object
        val isNullable = isNullable || ((kind == TypeKind.LIST || kind == TypeKind.MAP) && annotations.filterIsInstance<NullIfEmpty>().isNotEmpty())
        if (isNullable && presenceFlagIndices.isEmpty()) {
            desiredFlagBitWidth.raise(1)
        }
        val customIndex = annotations.filterIsInstance<FlagIndex>().firstOrNull()?.index

        val byteOrderAnnotation = annotations.filterIsInstance<com.splendo.kaluga.bytes.annotations.ByteOrder>().firstOrNull()
        // valueByteOrder: the byte order propagated to child field value encoding.
        val valueByteOrder = byteOrderAnnotation?.order ?: preferredByteOrder
        // byteOrder: this layout's own accumulation direction.
        // - Explicit annotation without excludeStructure: use valueByteOrder (standard @ByteOrder behaviour).
        // - Explicit annotation with excludeStructure: inherit the parent's builder direction.
        // - No annotation on a structure/polymorphic type: inherit parent's builder direction so nested
        //   structures always accumulate in the same direction as their parent without requiring annotations.
        // - No annotation on primitives/enums: inherit parent's field encoding direction.
        val byteOrder = when {
            byteOrderAnnotation != null -> if (byteOrderAnnotation.excludeStructure) preferredBuilderByteOrder else valueByteOrder

            // CONTEXTUAL is the sealed "value" wrapper: a pass-through that must keep the builder direction
            // (and propagate it to its subtype children) instead of collapsing it into the field encoding direction.
            kind.isStructure || kind.isPolymorphic || kind == TypeKind.CONTEXTUAL -> preferredBuilderByteOrder

            // A String is laid out — length prefix / terminator placement and character sequence — in the
            // accumulation direction, like a structure, since that is the direction the decoder reads it back in.
            kind == TypeKind.STRING -> preferredBuilderByteOrder

            else -> valueByteOrder
        }

        if (kind.isStructure && byteOrder != preferredBuilderByteOrder) {
            throw InvalidByteOrderException("Nested structure ${shape.serialName} cannot have a byteOrder different than $preferredBuilderByteOrder")
        }

        // Strings: valueByteOrder governs character encoding and must match the parent's encoding context.
        if (kind == TypeKind.STRING && valueByteOrder != preferredByteOrder) {
            throw InvalidByteOrderException("Nested string ${shape.serialName} cannot have a byteOrder different than $preferredByteOrder")
        }
        if (kind == TypeKind.BOOLEAN && customIndex != null) {
            desiredFlagBitWidth.raise(1)
        }
        // An enum with an explicit FlagIndex is packed into the flags as its ordinal, across enough bits for all its cases.
        if (kind == TypeKind.ENUM && customIndex != null) {
            desiredFlagBitWidth.raise(flagWidthForCases(shape.elementsCount))
        }
        val supportedLengths = lengths(annotations, kind)
        // A sub-byte numeric subfield of a bit field: an integer carrying @FlagIndex + @FlagWidth but no @Size has its
        // value packed straight into the flag region (like an enum ordinal), rather than as body bytes.
        val numericInFlagsBits = annotations.filterIsInstance<FlagWidth>().firstOrNull()?.bits?.takeIf {
            customIndex != null && !isNullable && annotations.filterIsInstance<Size>().isEmpty() &&
                kind in setOf(TypeKind.BYTE, TypeKind.SHORT, TypeKind.INT, TypeKind.LONG)
        }
        if (numericInFlagsBits != null && numericInFlagsBits < Byte.SIZE_BITS && annotations.none { it is Unsigned }) {
            throw InvalidNumericSettings(signedFlagBitsMessage(fieldName, kind, numericInFlagsBits))
        }
        val numericSettings = numericSettings(supportedLengths, kind, desiredFlagBitWidth, annotations, numericInFlagsBits)
        val stringSettings = stringSettings(kind, annotations, supportedLengths)
        val collectionSettings = collectionSettings(kind, annotations, desiredFlagBitWidth, supportedLengths)
        val enumMap = enumMap(shape, valueByteOrder)
        val polymorphicMap = polymorphicMap(shape, valueByteOrder)
        val (sizePolymorphicMap, sizePolymorphicFallback) = sizePolymorphicInfo(shape)

        val frame = frameSettings(annotations)

        if (kind == TypeKind.ENUM) {
            if (frame.checksum != null) {
                throw InvalidEnumLayout(
                    "Enum '${shape.serialName}' cannot use @Checksum: enum variants are already " +
                        "implicitly validated — an unrecognised byte pattern throws during matching.",
                )
            }
            val hasPrefix = frame.prefix != null
            val hasPostfix = frame.postfix != null
            if (customIndex != null && (hasPrefix || hasPostfix)) {
                throw InvalidEnumLayout(
                    "Enum '${shape.serialName}' is flag-packed via @FlagIndex but also has " +
                        "${if (hasPrefix && hasPostfix) {
                            "@Prefix and @Postfix"
                        } else if (hasPrefix) {
                            "@Prefix"
                        } else {
                            "@Postfix"
                        }}. " +
                        "Flag-packed enums have no body bytes to wrap.",
                )
            }
        }

        val minWidth = annotations.filterIsInstance<FlagWidth>().firstOrNull()?.bits ?: 0
        val width = maxOf(desiredFlagBitWidth.width, minWidth)
        val bitIndex = if (width > 0) {
            customIndex ?: defaultBitIndex
        } else {
            -1
        }
        // Notify parent of flag indices to be used
        reserveIndices((0..<width).map { bitIndex + it }.toSet())

        val children = when (kind) {
            TypeKind.OPEN -> openChildren(shape, valueByteOrder, bitIndex, byteOrder)
            TypeKind.MAP -> mapChildren(shape, annotations, valueByteOrder, byteOrder)
            TypeKind.LIST -> listChildren(shape, annotations, valueByteOrder, byteOrder)
            else -> elementChildren(shape, valueByteOrder, bitIndex, byteOrder)
        }

        collectionSettings?.deltaCoding?.let { checkDeltaCoded(fieldName, isNullable, byteOrder, children, it) }

        if (kind == TypeKind.STRUCTURE && shape.annotations.any { it is LengthPrefix }) {
            throw InvalidSealedLayout("@LengthPrefix on '${shape.serialName}' only applies to a sealed class, whose subtypes it frames")
        }

        val type = when (kind) {
            TypeKind.BOOLEAN -> TypeLayout.Bool
            TypeKind.BYTE, TypeKind.SHORT, TypeKind.INT, TypeKind.LONG, TypeKind.FLOAT, TypeKind.DOUBLE -> TypeLayout.Numeric(numericSettings!!)
            TypeKind.STRING -> TypeLayout.Text(stringSettings!!)
            TypeKind.CHAR -> TypeLayout.Character(stringSettings!!.encoding)
            TypeKind.ENUM -> TypeLayout.Enumeration(enumMap, children)
            TypeKind.STRUCTURE -> TypeLayout.Structure(children)
            TypeKind.LIST -> TypeLayout.ListLayout(collectionSettings!!, children.single())
            TypeKind.MAP -> TypeLayout.MapLayout(collectionSettings!!, children[0], children[1])
            TypeKind.SEALED -> sealedType(shape, byteOrder, polymorphicMap, sizePolymorphicMap, sizePolymorphicFallback, children)
            TypeKind.OPEN -> TypeLayout.Open(polymorphicMap, children)
            TypeKind.CONTEXTUAL -> TypeLayout.Contextual(children)
        }

        FieldLayout(
            name = fieldName,
            index = fieldIndex,
            bitIndex = bitIndex,
            bitWidth = width,
            byteOrder = byteOrder,
            valueByteOrder = valueByteOrder,
            isNullable = isNullable,
            presenceFlagIndices = presenceFlagIndices,
            frame = frame,
            type = type,
        )
    }

    private fun elementChildren(shape: TypeShape, byteOrder: ByteOrder, bitIndex: Int, preferredBuilderByteOrder: ByteOrder = byteOrder): List<FieldLayout> {
        var nextBit = 0
        val reservedSubIndices = mutableSetOf<Int>()
        // A regular structure has as many children as it has elements.
        // All children share the same header flag bytes.
        val children = (0 until shape.elementsCount).map { i ->
            val elementName = shape.elementName(i)
            val elementAnnotations = shape.elementAnnotations(i)
            val elementShape = shape.element(i)
            val reserved = elementAnnotations.filterIsInstance<Reserved>().firstOrNull()
            val trailing = elementAnnotations.any { it is Trailing }
            if (trailing && !elementShape.isNullable) {
                throw InvalidTrailingLayout("@Trailing '$elementName' of '${shape.serialName}' must be nullable: it is null when the value ends before it")
            }
            val child = compileField(
                elementShape,
                elementName,
                i,
                elementAnnotations,
                // The end of the value, not a flag bit, says whether a trailing property is present.
                elementShape.isNullable && !trailing,
                nextBit,
                byteOrder,
                preferredBuilderByteOrder = preferredBuilderByteOrder,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flag at index $bitIndex cannot be used for $elementName. Is already reserved")
                }
                reservedSubIndices += flagIndicesToUse
                while (nextBit in reservedSubIndices) {
                    nextBit++
                }
            }
            val reservedChild = if (reserved != null) child.copy(reservedBefore = reserved.before, reservedAfter = reserved.after) else child
            if (trailing) reservedChild.copy(isTrailing = true) else reservedChild
        }
        // The subtypes a sealed type holds are alternatives rather than a sequence. A @Discriminator takes no bytes, so it
        // may be declared anywhere; it only becomes one once its sealed type is compiled.
        if (shape.kind == TypeKind.STRUCTURE) checkTrailing(shape.serialName, children, shape.discriminatorElements().toSet())
        return children
    }

    /**
     * Rejects the `@Trailing` properties among the [children] of the structure [name] that a decoder could not tell
     * apart from the end of the value, and a structure ending in them, which runs to the end of what holds it, where
     * something follows it.
     */
    private fun checkTrailing(name: String, children: List<FieldLayout>, discriminators: Set<Int>) {
        val first = children.indexOfFirst { it.isTrailing }
        children.forEachIndexed { index, child ->
            if (child.isTrailing) {
                notWholeBytes(child)?.let { throw InvalidTrailingLayout("@Trailing '${child.name}' of '$name' is $it, so the end of the value cannot say whether it is there") }
                if (child.runsToEnd()) {
                    throw InvalidTrailingLayout(
                        "@Trailing '${child.name}' of '$name' runs to the end of the value itself, so an empty one would read back as null; " +
                            "declare its own optional fields @Trailing instead",
                    )
                }
            } else if (first in 0..<index && index !in discriminators) {
                throw InvalidTrailingLayout(
                    "'${child.name}' of '$name' follows @Trailing '${children[first].name}', so it must be @Trailing too: only a tail of the properties can be optional",
                )
            }
            if (child.runsToEnd()) {
                children.drop(index + 1).firstOrNull { it.isTrailing || (child.hasTrailingTail() && it.index !in discriminators && it.mayWriteBody()) }?.let { follower ->
                    throw InvalidTrailingLayout(
                        if (follower.isTrailing) {
                            "@Trailing '${follower.name}' of '$name' follows '${child.name}', which runs to the end of the value, so it could never be reached"
                        } else {
                            "'${follower.name}' of '$name' follows '${child.name}', whose @Trailing properties run to the end of what holds it; " +
                                "make '${child.name}' the last property, or hold it in a type-length-value record whose length bounds it"
                        },
                    )
                }
            }
        }
    }

    /** Why [field] is not written in whole bytes of the body, which `@Trailing` needs, or `null` when it is. */
    private fun notWholeBytes(field: FieldLayout): String? {
        val type = field.type
        val settings = (type as? TypeLayout.ListLayout)?.settings ?: (type as? TypeLayout.MapLayout)?.settings
        return when {
            type is TypeLayout.Bool -> "a Boolean"
            field.isPurelyFlagPacked() || settings?.lengthMarking is CollectionSettings.FlagIndexedLength -> "in the flag header"
            field.presenceFlagIndices.isNotEmpty() -> "@PresentWhenAllSet"
            settings?.nullIfEmpty == true -> "@NullIfEmpty"
            field.reservedBefore > 0 || field.reservedAfter > 0 -> "@Reserved"
            field.copy(isTrailing = false).staticFieldBytes == 0 -> "written as no bytes at all"
            else -> null
        }
    }

    /** Rejects items of the collection [name] ending in `@Trailing` properties, which nothing bounds there. */
    private fun checkTrailingItem(name: String, item: FieldLayout) {
        if (item.hasTrailingTail()) {
            throw InvalidTrailingLayout(
                "The items of '$name' end in @Trailing properties, which run to the end of what holds them; hold them in type-length-value records whose length bounds each",
            )
        }
    }

    private fun listChildren(shape: TypeShape, annotations: List<Annotation>, byteOrder: ByteOrder, preferredBuilderByteOrder: ByteOrder = byteOrder): List<FieldLayout> {
        val itemShape = shape.element(0)
        val reservedSubIndices = mutableSetOf<Int>()
        // For list elements, there is only one child, that does not reuse its parent's flag header.
        val items = listOf(
            compileField(
                itemShape,
                shape.elementName(0),
                0,
                annotations.itemAnnotations(),
                itemShape.isNullable,
                0,
                byteOrder,
                preferredBuilderByteOrder = preferredBuilderByteOrder,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flags at index $flagIndicesToUse cannot be used for ${itemShape.serialName}. Is already reserved")
                }
                reservedSubIndices += flagIndicesToUse
            },
        )
        items.forEach { checkTrailingItem(shape.serialName, it) }
        return items
    }

    private fun mapChildren(shape: TypeShape, annotations: List<Annotation>, byteOrder: ByteOrder, preferredBuilderByteOrder: ByteOrder = byteOrder): List<FieldLayout> {
        val keyShape = shape.element(0)
        val valueShape = shape.element(1)

        val reservedKeySubIndices = mutableSetOf<Int>()
        val reservedValueSubIndices = mutableSetOf<Int>()
        // For maps, there are only two children for key and value elements, that each have their own flag headers.
        val entries = listOf(
            compileField(
                keyShape,
                shape.elementName(0),
                0,
                annotations.keyAnnotations(),
                keyShape.isNullable,
                0,
                byteOrder,
                preferredBuilderByteOrder = preferredBuilderByteOrder,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedKeySubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flags at index $flagIndicesToUse cannot be used for ${keyShape.serialName}. Is already reserved")
                }
                reservedKeySubIndices += flagIndicesToUse
            },
            compileField(
                valueShape,
                shape.elementName(1),
                1,
                annotations.valueAnnotations(),
                valueShape.isNullable,
                0,
                byteOrder,
                preferredBuilderByteOrder = preferredBuilderByteOrder,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedValueSubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flags at index $flagIndicesToUse cannot be used for ${valueShape.serialName}. Is already reserved")
                }
                reservedValueSubIndices += flagIndicesToUse
            },
        )
        entries.forEach { checkTrailingItem(shape.serialName, it) }
        return entries
    }

    private fun openChildren(shape: TypeShape, byteOrder: ByteOrder, bitIndex: Int, preferredBuilderByteOrder: ByteOrder = byteOrder): List<FieldLayout> =
        shape.openSubtypes().map { optionShape ->
            // For open polymorphic types, all registered subtypes need to be considered
            val reservedSubIndices = mutableSetOf<Int>()
            compileField(
                optionShape,
                optionShape.serialName,
                0,
                optionShape.annotations,
                optionShape.isNullable,
                0,
                byteOrder,
                preferredBuilderByteOrder = preferredBuilderByteOrder,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flag at index $bitIndex cannot be used for ${optionShape.serialName}. Is already reserved")
                }
                reservedSubIndices += flagIndicesToUse
            }
        }

    private fun lengths(annotations: List<Annotation>, kind: TypeKind): Set<Length> = annotations.filterIsInstance<Size>().map { it.length }.toSet().ifEmpty {
        when (kind) {
            TypeKind.BYTE -> if (annotations.filterIsInstance<MedFloat>().isNotEmpty()) {
                setOf(Length.`16_BIT`)
            } else {
                setOf(Length.`8_BIT`)
            }

            TypeKind.SHORT -> setOf(Length.`16_BIT`)

            TypeKind.INT -> setOf(Length.`32_BIT`)

            TypeKind.LONG -> if (annotations.filterIsInstance<MedFloat>().isNotEmpty()) {
                setOf(Length.`32_BIT`)
            } else {
                setOf(Length.`64_BIT`)
            }

            TypeKind.FLOAT -> when {
                annotations.filterIsInstance<MedFloat>().isNotEmpty() -> setOf(Length.`16_BIT`)
                annotations.filterIsInstance<Scalar>().isNotEmpty() -> setOf(Length.`16_BIT`)
                else -> setOf(Length.`32_BIT`)
            }

            TypeKind.DOUBLE -> when {
                annotations.filterIsInstance<MedFloat>().isNotEmpty() -> setOf(Length.`32_BIT`)
                annotations.filterIsInstance<Scalar>().isNotEmpty() -> setOf(Length.`32_BIT`)
                else -> setOf(Length.`64_BIT`)
            }

            TypeKind.MAP -> setOf(Length.`8_BIT`)

            TypeKind.LIST -> setOf(Length.`8_BIT`)

            else -> emptySet()
        }
    }.sortedBy { it.bytes }.toSet()

    private fun Set<Length>.sizingWidth() = flagWidthForCases(size)

    // Number of flag bits needed to store one of [cases] options, i.e. ceil(log2(cases)).
    // Integer arithmetic avoids floating-point rounding at the boundaries: the highest index is cases - 1,
    // and 32 - countLeadingZeroBits() is its bit width.
    private fun flagWidthForCases(cases: Int) = when {
        cases <= 1 -> 0
        else -> 32 - (cases - 1).countLeadingZeroBits()
    }

    // Below a byte no Kotlin type has the field's width, so a signed type there is mostly a `Byte` taken for its size,
    // and two's complement would only show on the first value above the sign bit.
    private fun signedFlagBitsMessage(fieldName: String, kind: TypeKind, bits: Int): String {
        val firstNegative = 1L shl (bits - 1)
        val unsignedType = when (kind) {
            TypeKind.BYTE -> "UByte"
            TypeKind.SHORT -> "UShort"
            TypeKind.INT -> "UInt"
            else -> "ULong"
        }
        return "'$fieldName' packs a signed number into $bits flag bits, so $firstNegative reads back as -$firstNegative and values above " +
            "${firstNegative - 1} cannot be encoded; a field narrower than a byte must be unsigned: declare it as $unsignedType or annotate it @Unsigned"
    }

    private fun numericSettings(
        supportedLengths: Set<Length>,
        kind: TypeKind,
        desiredFlagBitWidth: DesiredFlagBitWidth,
        annotations: List<Annotation>,
        inFlagsBits: Int?,
    ): NumericSettings? = if (kind.isNumeric) {
        desiredFlagBitWidth.raise(supportedLengths.sizingWidth())
        if (annotations.filterIsInstance<Range>().isNotEmpty()) {
            val range = annotations.filterIsInstance<Range>().first()
            NumericSettings.RangeEncoded(supportedLengths, range.min, range.max)
        } else if (annotations.filterIsInstance<MedFloat>().isNotEmpty()) {
            NumericSettings.MedFloat(supportedLengths)
        } else if (annotations.filterIsInstance<Scalar>().isNotEmpty()) {
            val scalar = annotations.filterIsInstance<Scalar>().first()
            val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
            NumericSettings.Scalar(supportedLengths, isSigned, scalar.multiplier, scalar.decimalExponent, scalar.binaryExponent, scalar.offset)
        } else if (kind == TypeKind.DOUBLE || kind == TypeKind.FLOAT) {
            NumericSettings.Decimal(supportedLengths)
        } else {
            val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
            if (inFlagsBits != null) {
                NumericSettings.Natural(emptySet(), isSigned, inFlagsBits = inFlagsBits)
            } else {
                NumericSettings.Natural(supportedLengths, isSigned)
            }
        }
    } else {
        null
    }

    private fun stringSettings(kind: TypeKind, annotations: List<Annotation>, supportedLengths: Set<Length>): StringSettings? {
        val byteStuffing = annotations.byteStuffingScheme()
        val terminal = annotations.terminalByte()
        return when (kind) {
            TypeKind.STRING -> {
                val encoding = annotations.filterIsInstance<Encoded>().firstOrNull()?.encoding ?: Encoding.UTF_8
                when {
                    // A byte-stuffing annotation is itself a terminated marking, so @NullTerminated is not required.
                    byteStuffing != null -> {
                        val endMarking = when (byteStuffing) {
                            is DelimiterByteStuffingScheme -> {
                                if (terminal != null) {
                                    throw UnsupportedByteStuffing("$byteStuffing has a fixed delimiter; @Terminal is not supported alongside it")
                                }
                                StringEncodingSettings.ByteStuffed.Delimited(byteStuffing)
                            }

                            is NonDelimiterByteStuffingScheme -> {
                                if (terminal == null) {
                                    throw UnsupportedByteStuffing("ByteStuffing $byteStuffing must be provided a terminal via @Terminal")
                                }
                                if (!byteStuffing.canTerminateWith(terminal)) {
                                    throw UnsupportedByteStuffing(
                                        "Byte stuffing on a String must escape its terminator byte and never emit it literally; use @ByteStuffedXor(delimiter = <terminator>) instead",
                                    )
                                }
                                StringEncodingSettings.ByteStuffed.Explicit(byteStuffing, terminal)
                            }
                        }
                        StringSettings(encoding, endMarking)
                    }

                    // @Terminal without stuffing is a plain byte-terminated string: the content must not encode the terminator.
                    terminal != null -> StringSettings(encoding, StringEncodingSettings.Terminated(terminal))

                    annotations.filterIsInstance<NullTerminated>().isNotEmpty() -> StringSettings(encoding, StringEncodingSettings.NullTerminated)

                    annotations.filterIsInstance<LengthPrefix>().isNotEmpty() -> {
                        val lengthPrefix = annotations.filterIsInstance<LengthPrefix>().first()
                        StringSettings(encoding, lengthPrefix.asLengthPrefix())
                    }

                    supportedLengths.size == 1 -> StringSettings(encoding, StringEncodingSettings.FixedLength(supportedLengths.first().bytes))

                    annotations.filterIsInstance<Unsized>().isNotEmpty() -> StringSettings(encoding, StringEncodingSettings.NoMarking)

                    else -> StringSettings(encoding, StringEncodingSettings.LengthPrefix.ByteLength)
                }
            }

            TypeKind.CHAR -> {
                val encoding = annotations.filterIsInstance<Encoded>().firstOrNull()?.encoding ?: Encoding.ASCII
                if (encoding == Encoding.UTF_8) {
                    throw UnsupportedCharEncoding("A Char has a fixed size, so it cannot be UTF-8; use ASCII, ISO-8859-1, UTF-16 or a String")
                }
                StringSettings(encoding, StringEncodingSettings.NoMarking)
            }

            else -> null
        }
    }

    private fun collectionSettings(kind: TypeKind, annotations: List<Annotation>, desiredFlagBitWidth: DesiredFlagBitWidth, supportedLengths: Set<Length>): CollectionSettings? =
        when (kind) {
            TypeKind.LIST,
            TypeKind.MAP,
            -> {
                val byteStuffing = annotations.byteStuffingScheme()
                val terminal = annotations.terminalByte()
                val count = annotations.filterIsInstance<Count>().firstOrNull()
                val deltaCoded = annotations.filterIsInstance<DeltaCoded>().firstOrNull()
                val lengthMarking = when {
                    deltaCoded != null -> {
                        val conflicts = listOfNotNull(
                            "@Count".takeIf { count != null },
                            "@LengthPrefix".takeIf { annotations.any { it is LengthPrefix } },
                            "@NullTerminated".takeIf { annotations.any { it is NullTerminated } },
                            "@Terminal".takeIf { terminal != null },
                            "@Size".takeIf { annotations.any { it is Size } },
                            "@NullIfEmpty".takeIf { annotations.any { it is NullIfEmpty } },
                            "byte stuffing".takeIf { byteStuffing != null },
                        )
                        if (conflicts.isNotEmpty()) {
                            throw InvalidDeltaCoding("@DeltaCoded runs to the end of what holds it, so it cannot be combined with ${conflicts.joinToString()}")
                        }
                        CollectionSettings.Unmarked
                    }

                    count != null -> {
                        val conflicts = listOfNotNull(
                            "@LengthPrefix".takeIf { annotations.any { it is LengthPrefix } },
                            "@Unsized".takeIf { annotations.any { it is Unsized } },
                            "@NullTerminated".takeIf { annotations.any { it is NullTerminated } },
                            "@Terminal".takeIf { terminal != null },
                            "@Size".takeIf { annotations.any { it is Size } },
                            "@NullIfEmpty".takeIf { annotations.any { it is NullIfEmpty } },
                            "byte stuffing".takeIf { byteStuffing != null },
                        )
                        if (conflicts.isNotEmpty()) {
                            throw InvalidCountLayout("@Count(${count.count}) already fixes where a collection ends, so it cannot be combined with ${conflicts.joinToString()}")
                        }
                        CollectionSettings.FixedCount(count.count)
                    }

                    // Byte stuffing implies a terminated (TerminalMarked) collection, so @NullTerminated is not required.
                    byteStuffing != null -> {
                        val delimiter = when (byteStuffing) {
                            is DelimiterByteStuffingScheme -> {
                                if (terminal != null) {
                                    throw UnsupportedByteStuffing("$byteStuffing has a fixed delimiter; @Terminal is not supported alongside it")
                                }
                                byteStuffing.delimiter
                            }

                            is NonDelimiterByteStuffingScheme -> {
                                if (terminal == null) {
                                    throw UnsupportedByteStuffing("ByteStuffing $byteStuffing must be provided a terminal via @Terminal")
                                }
                                if (!byteStuffing.canTerminateWith(terminal)) {
                                    throw UnsupportedByteStuffing(
                                        "Byte stuffing on a Collection must escape its terminator byte and never emit it literally; use @ByteStuffedXor(delimiter = <terminator>) instead",
                                    )
                                }
                                terminal
                            }
                        }
                        CollectionSettings.TerminalMarked(delimiter)
                    }

                    // @Terminal without stuffing is a plain byte-terminated collection (no item may start with the terminator).
                    terminal != null -> CollectionSettings.TerminalMarked(terminal)

                    annotations.filterIsInstance<NullTerminated>().isNotEmpty() -> CollectionSettings.TerminalMarked()

                    annotations.filterIsInstance<LengthPrefix>().isNotEmpty() -> {
                        val lengthPrefix = annotations.filterIsInstance<LengthPrefix>().first()
                        if (annotations.filterIsInstance<FlagIndex>().isNotEmpty()) {
                            // @LengthPrefix + @FlagIndex: pack the count into the parent's flag header
                            // instead of writing count bytes into the body. The bit width is 8 for
                            // ByteLength (default) and 16 for ShortLength; WithOverflow falls back to 16.
                            val bits = if (lengthPrefix.lengthAsShort || lengthPrefix.canOverflow) Short.SIZE_BITS else Byte.SIZE_BITS
                            desiredFlagBitWidth.raise(bits)
                            CollectionSettings.FlagIndexedLength(bits)
                        } else {
                            CollectionSettings.LengthPrefix(lengthPrefix.asLengthPrefix())
                        }
                    }

                    annotations.filterIsInstance<Unsized>().isNotEmpty() -> CollectionSettings.Unmarked

                    else -> {
                        desiredFlagBitWidth.raise(supportedLengths.sizingWidth())
                        CollectionSettings.NumericLength(supportedLengths)
                    }
                }
                CollectionSettings(
                    lengthMarking,
                    annotations.filterIsInstance<NullIfEmpty>().isNotEmpty(),
                    byteStuffing,
                    deltaCoded?.let { CollectionSettings.DeltaCoding(it.step) },
                )
            }

            else -> null
        }

    /**
     * Rejects a `@DeltaCoded` list [name] the encoding cannot apply to: one that is nullable, or whose items are not
     * integers of one size, at least as wide as a step, laid out least significant first.
     */
    private fun checkDeltaCoded(name: String, isNullable: Boolean, byteOrder: ByteOrder, children: List<FieldLayout>, deltaCoding: CollectionSettings.DeltaCoding) {
        if (isNullable) throw InvalidDeltaCoding("@DeltaCoded '$name' cannot be nullable: it has no flag bit, and no bytes at all is an empty list")
        if (byteOrder != ByteOrder.LEAST_SIGNIFICANT_FIRST) {
            throw InvalidDeltaCoding(
                "@DeltaCoded '$name' needs a list accumulated LEAST_SIGNIFICANT_FIRST; use @ItemByteOrder or @ByteOrder(excludeStructure = true) for big-endian items",
            )
        }
        val item = children.single()
        val settings = (item.type as? TypeLayout.Numeric)?.settings as? NumericSettings.Natural
        val width = settings?.supportedLengths?.singleOrNull()
        val framed = item.frame.prefix != null || item.frame.postfix != null || item.frame.checksum != null || item.frame.byteStuffing != null
        if (width == null || settings.inFlagsBits != null || item.isNullable || framed) {
            throw InvalidDeltaCoding("@DeltaCoded '$name' needs items that are integers of one @ItemSize, not nullable and without a frame of their own")
        }
        if (deltaCoding.step.bytes > width.bytes) {
            throw InvalidDeltaCoding("@DeltaCoded '$name' has steps of ${deltaCoding.step.bytes} bytes, more than its items of ${width.bytes}")
        }
    }

    private fun LengthPrefix.asLengthPrefix() = when {
        lengthAsShort -> StringEncodingSettings.LengthPrefix.ShortLength
        canOverflow -> StringEncodingSettings.LengthPrefix.WithOverflow(sentinel)
        else -> StringEncodingSettings.LengthPrefix.ByteLength
    }

    /**
     * Encodes [this] integer as a [size]-byte value in [byteOrder], suitable for use as a discriminator.
     */
    private fun Int.toDiscriminatorBytes(size: Length, byteOrder: ByteOrder): ByteArray {
        val bytes = ByteArray(size.bytes)
        var remaining = this
        if (byteOrder == ByteOrder.LEAST_SIGNIFICANT_FIRST) {
            for (i in 0 until size.bytes) {
                bytes[i] = (remaining and 0xFF).toByte()
                remaining = remaining ushr 8
            }
        } else {
            for (i in size.bytes - 1 downTo 0) {
                bytes[i] = (remaining and 0xFF).toByte()
                remaining = remaining ushr 8
            }
        }
        return bytes
    }

    private fun validateDiscriminatorLengths(name: String, entries: Collection<ByteArrayHolder>) {
        val lengths = entries.filter { it.array.isNotEmpty() }.map { it.array.size }.toSet()
        if (lengths.size > 1) {
            throw InconsistentDiscriminatorLength(
                "All @SerializedByteValue discriminators in '$name' must have the same byte length; got lengths $lengths. " +
                    "Either make all discriminators the same width or use @ByOrdinal for uniform-length ordinal encoding.",
            )
        }
    }

    private fun enumMap(shape: TypeShape, byteOrder: ByteOrder): Map<Int, ByteArrayHolder> {
        if (shape.kind != TypeKind.ENUM) return emptyMap()
        val byOrdinal = shape.annotations.filterIsInstance<ByOrdinal>().firstOrNull()
        var anyUsesClassName = false
        val map = (0 until shape.elementsCount).associateWith { index ->
            val perVariant = shape.elementAnnotations(index).filterIsInstance<SerializedByteValue>().firstOrNull()
            ByteArrayHolder(
                when {
                    perVariant != null -> perVariant.value

                    byOrdinal != null -> (index + byOrdinal.offset).toDiscriminatorBytes(byOrdinal.size, byteOrder)

                    else -> {
                        anyUsesClassName = true
                        shape.elementName(index).toByteArray(StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8), byteOrder)
                    }
                },
            )
        }
        if (!anyUsesClassName) validateDiscriminatorLengths(shape.serialName, map.values)
        return map
    }

    private fun polymorphicMap(shape: TypeShape, byteOrder: ByteOrder): Map<String, ByteArrayHolder> {
        fun buildEntries(optionShapes: List<TypeShape>): Pair<Map<String, ByteArrayHolder>, Boolean> {
            var anyUsesClassName = false
            val map = optionShapes.associate { optionShape ->
                val bytes = optionShape.annotations.filterIsInstance<SerializedByteValue>().firstOrNull()?.value
                    ?: run {
                        anyUsesClassName = true
                        optionShape.serialName.toByteArray(StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8), byteOrder)
                    }
                optionShape.serialName to ByteArrayHolder(bytes)
            }
            return map to anyUsesClassName
        }

        val (map, anyUsesClassName) = when (shape.kind) {
            TypeKind.SEALED -> {
                val sealedShape = shape.element(1)
                // A fallback has no discriminator of its own; its @Discriminator field holds whichever one it was read for.
                buildEntries((0..<sealedShape.elementsCount).map { sealedShape.element(it) }.filter { option -> option.annotations.none { it is Fallback } })
            }

            TypeKind.OPEN -> buildEntries(shape.openSubtypes())

            else -> return emptyMap()
        }
        if (!anyUsesClassName) validateDiscriminatorLengths(shape.serialName, map.values)
        return map
    }

    /**
     * The [TypeLayout.Sealed] of [shape], accumulated in [byteOrder]: with a class `@LengthPrefix` its subtypes are
     * type-length-value records, and a `@Fallback` subtype reads every discriminator the others do not have. The
     * fallback is laid out like any other subtype but for its `@Discriminator` field, which becomes a
     * [TypeLayout.Discriminator] taking no bytes of its own.
     */
    private fun sealedType(
        shape: TypeShape,
        byteOrder: ByteOrder,
        discriminators: Map<String, ByteArrayHolder>,
        sizeDispatch: Map<Int, String>,
        sizeFallback: String?,
        children: List<FieldLayout>,
    ): TypeLayout.Sealed {
        val sealedShape = shape.element(1)
        val name = shape.serialName
        val sizePolymorphic = shape.annotations.any { it is SizePolymorphic }
        val lengthPrefix = shape.annotations.filterIsInstance<LengthPrefix>().firstOrNull()?.let { annotation ->
            if (sizePolymorphic) throw InvalidSealedLayout("@LengthPrefix on '$name' needs subtypes told apart by a discriminator, not by @SizePolymorphic")
            if (annotation.canOverflow) throw InvalidSealedLayout("@LengthPrefix on '$name' has a byte or 16-bit length; canOverflow does not apply to it")
            if (byteOrder != ByteOrder.LEAST_SIGNIFICANT_FIRST) {
                throw InvalidSealedLayout(
                    "@LengthPrefix on '$name' needs a frame accumulated LEAST_SIGNIFICANT_FIRST; use @ByteOrder(excludeStructure = true) for big-endian values",
                )
            }
            annotation.asLengthPrefix()
        }
        val options = (0..<sealedShape.elementsCount).map { sealedShape.element(it) }
        val fallbacks = options.filter { option -> option.annotations.any { it is Fallback } }
        if (fallbacks.size > 1) throw InvalidSealedLayout("'$name' has more than one @Fallback subtype: ${fallbacks.joinToString { it.serialName }}")
        val fallback = fallbacks.singleOrNull()
        options.filter { it !== fallback }.forEach { option ->
            option.discriminatorElements().firstOrNull()?.let { index ->
                throw InvalidSealedLayout(
                    "@Discriminator on '${option.elementName(index)}' of '${option.serialName}' only applies to a property of the @Fallback subtype of '$name'",
                )
            }
        }
        if (fallback == null) return TypeLayout.Sealed(discriminators, sizeDispatch, sizeFallback, children, lengthPrefix)

        val fallbackName = fallback.serialName
        if (sizePolymorphic) throw InvalidSealedLayout("@Fallback '$fallbackName' needs subtypes told apart by a discriminator, not by @SizePolymorphic")
        if (fallback.annotations.any { it is SerializedByteValue }) {
            throw InvalidSealedLayout("@Fallback '$fallbackName' reads the discriminators of other subtypes, so it has no @SerializedByteValue")
        }
        val width = discriminators.values.map { it.array.size }.distinct().singleOrNull()
            ?: throw InvalidSealedLayout("@Fallback '$fallbackName' needs other subtypes of '$name' with discriminators of one size, which its @Discriminator property holds")
        val marked = fallback.discriminatorElements()
        if (marked.size != 1) {
            throw InvalidSealedLayout(
                "@Fallback '$fallbackName' needs exactly one @Discriminator property to hold the discriminator it was read for, " +
                    if (marked.isEmpty()) "but has none" else "but has ${marked.joinToString { "'${fallback.elementName(it)}'" }}",
            )
        }
        val value = children[1]
        val option = value.type.children.first { it.name == fallbackName }
        val fields = (option.type as TypeLayout.Structure).fields
        val type = fields[marked.single()]
        val numeric = (type.type as? TypeLayout.Numeric)?.settings as? NumericSettings.Natural
        if (numeric == null || numeric.inFlagsBits != null || numeric.supportedLengths.singleOrNull()?.bytes != width || type.isNullable || type.isTrailing ||
            type.bitIndex >= 0 || type.presenceFlagIndices.isNotEmpty()
        ) {
            throw InvalidSealedLayout(
                "The @Discriminator property '${type.name}' of @Fallback '$fallbackName' holds the discriminator, so it must be a number of $width byte(s), " +
                    "not nullable or in the flag header",
            )
        }
        if (type.reservedBefore > 0 || type.reservedAfter > 0) {
            throw InvalidSealedLayout("The @Discriminator property '${type.name}' of @Fallback '$fallbackName' takes no bytes in it, so it cannot have @Reserved bytes around it")
        }
        if (type.frame.prefix != null || type.frame.postfix != null || type.frame.checksum != null || type.frame.byteStuffing != null) {
            throw InvalidSealedLayout(
                "The @Discriminator property '${type.name}' of @Fallback '$fallbackName' takes no bytes in it, so it cannot have a prefix, a postfix, a checksum or byte stuffing around it",
            )
        }
        val framed = option.copy(type = TypeLayout.Structure(fields.map { if (it === type) type.copy(type = TypeLayout.Discriminator(numeric)) else it }))
        val valueField = value.copy(type = TypeLayout.Contextual(value.type.children.map { if (it.name == fallbackName) framed else it }))
        return TypeLayout.Sealed(discriminators, sizeDispatch, sizeFallback, listOf(children[0], valueField), lengthPrefix, fallbackName)
    }

    private fun TypeShape.discriminatorElements(): List<Int> = if (kind == TypeKind.STRUCTURE) {
        (0..<elementsCount).filter { index -> elementAnnotations(index).any { it is Discriminator } }
    } else {
        emptyList()
    }

    /**
     * Returns `(fixedSizeMap, fallback)` for a `@SizePolymorphic` sealed class.
     * Fixed-size subtypes populate the map; at most one variable-size subtype is allowed as a
     * catch-all fallback for remaining byte counts that don't match any fixed entry.
     */
    private fun sizePolymorphicInfo(shape: TypeShape): Pair<Map<Int, String>, String?> {
        if (shape.annotations.filterIsInstance<SizePolymorphic>().isEmpty()) return emptyMap<Int, String>() to null
        if (shape.kind != TypeKind.SEALED) return emptyMap<Int, String>() to null

        val sealedShape = shape.element(1)
        val fixedEntries = mutableListOf<Pair<Int, String>>()
        var fallback: String? = null

        for (index in 0..<sealedShape.elementsCount) {
            val optionShape = sealedShape.element(index)
            // Sized as a root type: each subtype is its own frame on the wire.
            val size = compile(optionShape).staticByteSize
            if (size == null) {
                if (fallback != null) {
                    throw InvalidSizePolymorphic(
                        "@SizePolymorphic '${shape.serialName}' has more than one variable-size subtype — " +
                            "at most one is allowed as a catch-all fallback.",
                    )
                }
                fallback = optionShape.serialName
            } else {
                fixedEntries.add(size to optionShape.serialName)
            }
        }

        val duplicates = fixedEntries.groupBy { it.first }.filter { it.value.size > 1 }
        if (duplicates.isNotEmpty()) {
            throw InvalidSizePolymorphic(
                "@SizePolymorphic dispatch on '${shape.serialName}' is ambiguous: " +
                    "multiple subtypes compute to the same byte size. " +
                    duplicates.entries.joinToString("; ") { (size, pairs) ->
                        "size=$size → ${pairs.map { it.second }}"
                    },
            )
        }

        return fixedEntries.toMap() to fallback
    }

    private fun frameSettings(annotations: List<Annotation>): FrameSettings {
        val byteStuffing = annotations.byteStuffingScheme()
        return FrameSettings(
            annotations.filterIsInstance<Prefix>().firstOrNull()?.value?.let { ByteArrayHolder(it) },
            annotations.filterIsInstance<Postfix>().firstOrNull()?.value?.let { ByteArrayHolder(it) },
            annotations.filterIsInstance<Checksum>().firstOrNull()?.let { checksum ->
                ChecksumSpec(checksum.width, checksum.polynomial, checksum.init, checksum.xorOut, checksum.reflectIn, checksum.reflectOut)
            },
            byteStuffing,
            byteStuffing?.let { structureByteStuffingTerminator(it, annotations.terminalByte()) },
        )
    }

    // The terminator that bounds a byte-stuffed structure: a delimiter scheme uses its canonical delimiter (an explicit
    // @Terminal is rejected); a non-delimiter scheme uses an explicit @Terminal, or null — meaning the structure is
    // unsized and bounded by the remaining payload (the same rule @Unsized applies to Strings and Collections).
    private fun structureByteStuffingTerminator(scheme: ByteStuffingScheme, terminal: Byte?): Byte? = when (scheme) {
        is DelimiterByteStuffingScheme -> {
            if (terminal != null) {
                throw UnsupportedByteStuffing("$scheme has a fixed delimiter; @Terminal is not supported alongside it")
            }
            scheme.delimiter
        }

        is NonDelimiterByteStuffingScheme -> terminal?.also {
            if (!scheme.canTerminateWith(it)) {
                throw UnsupportedByteStuffing(
                    "Byte stuffing on a structure must escape its terminator byte and never emit it literally; use @ByteStuffedXor(delimiter = <terminator>) instead",
                )
            }
        }
    }

    // Resolves the byte-stuffing scheme from one of the @ByteStuffed* annotations; they are mutually exclusive.
    private fun List<Annotation>.byteStuffingScheme(): ByteStuffingScheme? {
        val cSafe = filterIsInstance<ByteStuffed>().firstOrNull()
        val xor = filterIsInstance<ByteStuffedXor>().firstOrNull()
        val slip = filterIsInstance<ByteStuffedSlip>().firstOrNull()
        val cobs = filterIsInstance<ByteStuffedCobs>().firstOrNull()
        if (listOfNotNull(cSafe, xor, slip, cobs).size > 1) {
            throw UnsupportedByteStuffing("At most one @ByteStuffed* annotation is allowed on an element")
        }
        return cSafe?.let { ByteStuffingScheme.CSafe(it.escapeByte, it.mask) }
            ?: xor?.let { ByteStuffingScheme.Xor(it.escapeByte, it.delimiter, it.additionalEscapedBytes.toSet(), it.xorKey) }
            ?: slip?.let { ByteStuffingScheme.Slip(it.end, it.escapeByte, it.escapedEnd, it.escapedEsc) }
            ?: cobs?.let { ByteStuffingScheme.Cobs }
    }

    private fun List<Annotation>.terminalByte(): Byte? = filterIsInstance<Terminal>().firstOrNull()?.terminator

    private fun List<Annotation>.itemAnnotations(): List<Annotation> = mapNotNull { annotation ->
        when (annotation) {
            is ItemByteOrder -> com.splendo.kaluga.bytes.annotations.ByteOrder(annotation.order)
            is ItemLengthPrefix -> LengthPrefix(annotation.lengthAsShort, annotation.canOverflow, annotation.sentinel)
            is ItemEncoded -> Encoded(annotation.encoding)
            is ItemNullTerminated -> NullTerminated()
            is ItemUnsigned -> Unsigned()
            is ItemScalar -> Scalar(annotation.multiplier, annotation.decimalExponent, annotation.binaryExponent, annotation.offset)
            is ItemMedFloat -> MedFloat()
            is ItemSize -> Size(annotation.size)
            else -> null
        }
    }

    private fun List<Annotation>.keyAnnotations(): List<Annotation> = mapNotNull { annotation ->
        when (annotation) {
            is KeyByteOrder -> com.splendo.kaluga.bytes.annotations.ByteOrder(annotation.order)
            is KeyLengthPrefix -> LengthPrefix(annotation.lengthAsShort, annotation.canOverflow, annotation.sentinel)
            is KeyEncoded -> Encoded(annotation.encoding)
            is KeyNullTerminated -> NullTerminated()
            is KeyUnsigned -> Unsigned()
            is KeyScalar -> Scalar(annotation.multiplier, annotation.decimalExponent, annotation.binaryExponent, annotation.offset)
            is KeyMedFloat -> MedFloat()
            is KeySize -> Size(annotation.size)
            else -> null
        }
    }

    private fun List<Annotation>.valueAnnotations(): List<Annotation> = mapNotNull { annotation ->
        when (annotation) {
            is ValueByteOrder -> com.splendo.kaluga.bytes.annotations.ByteOrder(annotation.order)
            is ValueLengthPrefix -> LengthPrefix(annotation.lengthAsShort, annotation.canOverflow, annotation.sentinel)
            is ValueEncoded -> Encoded(annotation.encoding)
            is ValueNullTerminated -> NullTerminated()
            is ValueUnsigned -> Unsigned()
            is ValueScalar -> Scalar(annotation.multiplier, annotation.decimalExponent, annotation.binaryExponent, annotation.offset)
            is ValueMedFloat -> MedFloat()
            is ValueSize -> Size(annotation.size)
            else -> null
        }
    }
}

internal fun FieldLayout.staticRootByteSize(): Int? {
    val flagHeaderBytes = (headerBits + Byte.SIZE_BITS - 1) / Byte.SIZE_BITS
    var bodyBytes = 0
    for (child in type.children) {
        bodyBytes += child.staticChildBodyBytes() ?: return null
    }
    return flagHeaderBytes + bodyBytes +
        (frame.prefix?.array?.size ?: 0) +
        (frame.postfix?.array?.size ?: 0) +
        (frame.checksum?.crc?.byteWidth ?: 0)
}

internal fun FieldLayout.staticChildBodyBytes(): Int? {
    // Present or not depending on how far the value runs.
    if (isTrailing) return null
    val reserved = reservedBefore + reservedAfter
    val type = type
    val hasSettings = type is TypeLayout.Numeric || type is TypeLayout.Text || type is TypeLayout.Character ||
        type is TypeLayout.ListLayout || type is TypeLayout.MapLayout

    // Purely flag-packed fields (boolean, flag-indexed enum, sub-byte numeric) contribute 0 body bytes,
    // but any @Reserved padding around them is still present on the wire.
    if (isPurelyFlagPacked()) return reserved

    // Nullable with body content: size is value-dependent (absent = 0 bytes, present = N bytes).
    if (isNullable && (hasSettings || type.children.isNotEmpty())) {
        return null
    }

    return when {
        type is TypeLayout.Numeric -> when (val s = type.settings) {
            is NumericSettings.Natural -> if (s.inFlagsBits != null) {
                0
            } else {
                s.supportedLengths.singleOrNull()?.bytes
            }

            else -> s.supportedLengths.singleOrNull()?.bytes
        }

        type is TypeLayout.Text -> when (val em = type.settings.endMarking) {
            is StringEncodingSettings.FixedLength -> em.length
            else -> null
        }

        type is TypeLayout.Character -> type.encoding.byteSize

        type is TypeLayout.ListLayout -> type.settings.fixedCountBytes(type.children)

        type is TypeLayout.MapLayout -> type.settings.fixedCountBytes(type.children)

        type is TypeLayout.Enumeration && type.values.isNotEmpty() -> {
            // Non-flag-packed enum: prefix + value bytes + postfix.
            val valueSizes = type.values.values.map { it.array.size }.toSet()
            if (valueSizes.size != 1) {
                null
            } else {
                (frame.prefix?.array?.size ?: 0) + valueSizes.first() +
                    (frame.postfix?.array?.size ?: 0)
            }
        }

        // Nested polymorphic type — size depends on runtime value.
        type.discriminators.isNotEmpty() || (type is TypeLayout.Sealed && type.sizeDispatch.isNotEmpty()) -> null

        // Nested structure: recursively compute its total size.
        type.children.isNotEmpty() -> staticRootByteSize()

        else -> 0
    }?.plus(reserved)
}

// Only a @Count collection has a size of its own: it writes nothing but [count] runs of its item frames (key and value
// frames for a map).
private fun CollectionSettings.fixedCountBytes(items: List<FieldLayout>): Int? {
    val count = (lengthMarking as? CollectionSettings.FixedCount)?.count ?: return null
    if (count == 0) return 0
    var entryBytes = 0
    for (item in items) entryBytes += item.staticItemFrameBytes() ?: return null
    return count * entryBytes
}

// An item frame is the item's own flag bits, then its body, padded to a byte. A Boolean item is left out as its body
// bit is not padded per item.
private fun FieldLayout.staticItemFrameBytes(): Int? {
    if (isNullable || type is TypeLayout.Bool) return null
    val body = staticChildBodyBytes() ?: return null
    val headerBits = (bitIndex + bitWidth).coerceAtLeast(0)
    return (headerBits + Byte.SIZE_BITS - 1) / Byte.SIZE_BITS + body
}

/**
 * Whether where [this] ends is only known from the end of what holds it: an `@Unsized` or `@DeltaCoded` value, a
 * byte-stuffed structure without a terminator, a value told apart by its size, or one ending in `@Trailing` properties.
 */
private fun FieldLayout.runsToEnd(): Boolean = when (val type = type) {
    is TypeLayout.Text -> type.settings.endMarking is StringEncodingSettings.NoMarking
    is TypeLayout.ListLayout -> type.settings.lengthMarking is CollectionSettings.Unmarked
    is TypeLayout.MapLayout -> type.settings.lengthMarking is CollectionSettings.Unmarked
    is TypeLayout.Structure -> (frame.byteStuffing != null && frame.byteStuffingTerminator == null) || type.fields.any { it.isTrailing || it.runsToEnd() }
    is TypeLayout.Sealed -> type.sizeDispatch.isNotEmpty() || type.sizeFallback != null || (type.lengthPrefix == null && type.elements.getOrNull(1)?.runsToEnd() == true)
    is TypeLayout.Open, is TypeLayout.Contextual -> type.children.any { it.runsToEnd() }
    else -> false
}

/** Whether [this] ends in `@Trailing` properties of its own or of what it holds last, outside a type-length-value record. */
private fun FieldLayout.hasTrailingTail(): Boolean = when (val type = type) {
    is TypeLayout.Structure -> type.fields.any { it.isTrailing || it.hasTrailingTail() }
    is TypeLayout.Sealed -> type.lengthPrefix == null && type.elements.getOrNull(1)?.hasTrailingTail() == true
    is TypeLayout.Open, is TypeLayout.Contextual -> type.children.any { it.hasTrailingTail() }
    else -> false
}

/** Whether [this] may write bytes in its parent's body, which a structure running to the end of the value cannot have after it. */
private fun FieldLayout.mayWriteBody(): Boolean = type !is TypeLayout.Discriminator && !isPurelyFlagPacked()

/** True when a field's entire encoding lives in the flag header and contributes no body bytes. */
private fun FieldLayout.isPurelyFlagPacked(): Boolean {
    if (bitIndex < 0 || bitWidth <= 0) return false
    val type = type
    // Sub-byte integer packed into flags via @FlagIndex + @FlagWidth
    if (type is TypeLayout.Numeric && (type.settings as? NumericSettings.Natural)?.inFlagsBits != null) {
        return true
    }
    // Boolean or enum ordinal packed into flags (no separate body type)
    val hasSettings = type is TypeLayout.Numeric || type is TypeLayout.Text || type is TypeLayout.Character ||
        type is TypeLayout.ListLayout || type is TypeLayout.MapLayout
    return !hasSettings && type.children.isEmpty() && type.discriminators.isEmpty()
}

private val TypeLayout.discriminators: Map<String, ByteArrayHolder> get() = when (this) {
    is TypeLayout.Sealed -> discriminators
    is TypeLayout.Open -> discriminators
    else -> emptyMap()
}

/** The serial names kotlinx gives the sets it serializes as lists. */
private val SET_SERIAL_NAMES = setOf("kotlin.collections.LinkedHashSet", "kotlin.collections.HashSet")
