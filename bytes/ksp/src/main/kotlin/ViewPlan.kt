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

import com.google.devtools.ksp.symbol.KSNode
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.stuffing.ByteStuffingScheme
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.layout.ChecksumSpec
import com.splendo.kaluga.bytes.layout.FieldLayout
import com.splendo.kaluga.bytes.layout.NumericSettings
import com.splendo.kaluga.bytes.layout.TypeKind
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

/**
 * A layout feature the VIEW generator does not implement yet. Reported as "VIEW not yet supported for [feature]": a KSP
 * error, or a warning that skips the view when the `skipUnsupportedViews` option is set.
 */
internal class UnsupportedViewFeature(val feature: String, val node: KSNode?) : Exception(feature)

/**
 * How a Kotlin property type maps onto the primitive the wire format encodes, following the element structure
 * kotlinx.serialization gives it (see [ShapeBuilder]).
 */
internal sealed class ValueType {
    abstract val typeName: TypeName

    /** The primitive the value is encoded as, after unwrapping inline classes. */
    abstract val leafKind: TypeKind

    /** Whether the value passes through an inline class (unsigned types, base.bytes numerics, value classes). */
    abstract val isInline: Boolean

    data class Primitive(override val typeName: TypeName, override val leafKind: TypeKind) : ValueType() {
        override val isInline: Boolean get() = false
    }

    /** `kotlin.UByte` and friends, encoded as their signed counterpart [signed]. */
    data class Unsigned(override val typeName: TypeName, val signed: Primitive) : ValueType() {
        override val leafKind: TypeKind get() = signed.leafKind
        override val isInline: Boolean get() = true
    }

    /** A value class wrapping [inner] in its [propertyName] property; base.bytes numerics such as `Int24` are mapped this way too. */
    data class Wrapped(override val typeName: TypeName, val constructor: TypeName, val propertyName: String, val inner: ValueType, val hasFrame: Boolean) : ValueType() {
        override val leafKind: TypeKind get() = inner.leafKind
        override val isInline: Boolean get() = true
        val framed: Boolean get() = hasFrame || (inner as? Wrapped)?.framed == true
    }

    data class Enumeration(override val typeName: ClassName, val serialName: String) : ValueType() {
        override val leafKind: TypeKind get() = TypeKind.ENUM
        override val isInline: Boolean get() = false
    }

    data class Nested(override val typeName: ClassName, val layout: Layout) : ValueType() {
        override val leafKind: TypeKind get() = TypeKind.STRUCTURE
        override val isInline: Boolean get() = false
    }

    /**
     * A list, set, map or array of [item]s (a map's keys) and, for a map, [value]s; [itemNullable] and
     * [valueNullable] say whether those may be `null`.
     */
    data class Collection(
        override val typeName: TypeName,
        val kind: Kind,
        val item: ValueType,
        val itemNullable: Boolean,
        val value: ValueType? = null,
        val valueNullable: Boolean = false,
    ) : ValueType() {
        enum class Kind { LIST, SET, MAP, ARRAY, PRIMITIVE_ARRAY }

        override val leafKind: TypeKind get() = if (kind == Kind.MAP) TypeKind.MAP else TypeKind.LIST
        override val isInline: Boolean get() = false
    }
}

/**
 * A runtime bit offset from the start of a frame, which depends on the bytes: the end of a property whose presence or
 * size is read from the flag header, or such an end rounded up to a byte.
 */
internal sealed class Anchor {
    /** Whether the offset is a multiple of eight bits. */
    abstract val aligned: Boolean

    /** The bit offset after the property [name], computed by the generated `endOf` function. */
    data class End(val name: String, override val aligned: Boolean) : Anchor()

    /** [inner] plus [bits], rounded up to a byte. */
    data class Aligned(val inner: Anchor, val bits: Int) : Anchor() {
        override val aligned: Boolean get() = true
    }
}

/** A bit offset from the start of a frame: [bits] after [anchor], or after the frame start when it is `null`. */
internal data class Cursor(val anchor: Anchor?, val bits: Int) {

    val isAligned: Boolean get() = bits % Byte.SIZE_BITS == 0 && (anchor?.aligned ?: true)

    fun plus(bits: Int): Cursor = copy(bits = this.bits + bits)

    /** Rounded up to the next byte, as every byte-writing encoder action does. */
    fun aligned(): Cursor = when {
        anchor == null || anchor.aligned -> copy(bits = alignBits(bits))
        else -> Cursor(Anchor.Aligned(anchor, bits), 0)
    }
}

internal fun alignBits(bits: Int): Int = (bits + Byte.SIZE_BITS - 1) / Byte.SIZE_BITS * Byte.SIZE_BITS

/** When a nullable property is present, following the decoder: its own flag bit, or all of the flag bits it names. */
internal sealed class Presence {
    data class Owned(val bitPos: Int) : Presence()
    data class AllSet(val bitPositions: List<Int>) : Presence()
}

/** The size of a number: fixed, or picked by the encoder from [lengths] and recorded in [bits] flag bits at [bitPos]. */
internal sealed class Width {
    data class Fixed(val length: Length) : Width()

    /** [kind] says how the flag bits select one of [lengths]. */
    data class Selected(val bitPos: Int, val bits: Int, val lengths: List<Length>, val kind: Kind) : Width() {
        enum class Kind {
            /** The flag bits hold the index into [lengths]. */
            INDEX,

            /** A set flag bit selects the larger of the two [lengths]. */
            LARGER,
        }
    }
}

/**
 * How a property is encoded. Flag header bits are at fixed positions relative to the frame start; everything in the
 * body is at the [FieldPlan.start] cursor of its property.
 */
internal sealed class Access {

    data class HeaderBit(val bitPos: Int) : Access()

    data object BodyBit : Access()

    /** A number packed into [bits] flag header bits, least significant bit first. */
    data class PackedNumber(val bitPos: Int, val bits: Int, val signed: Boolean) : Access()

    /** An enum ordinal packed into [bits] flag header bits. */
    data class PackedEnum(val bitPos: Int, val bits: Int) : Access()

    /** A number of [width], decoded in [readOrder] and encoded in [writeOrder]. */
    data class Number(val width: Width, val settings: NumericSettings, val readOrder: ByteOrder, val writeOrder: ByteOrder) : Access()

    data class Character(val encoding: Encoding, val readOrder: ByteOrder, val writeOrder: ByteOrder) : Access()

    /** A string; it is decoded least significant first whatever its byte order, as `BytesFormat` does. */
    data class Text(val encoding: Encoding, val endMarking: StringEncodingSettings.EndMarking, val writeOrder: ByteOrder) : Access() {
        val fixedBytes: Int? get() = (endMarking as? StringEncodingSettings.FixedLength)?.length?.times(encoding.byteSize)
    }

    /**
     * An enum identified by one of the byte sequences in [table], by ordinal, between an optional frame; the first
     * entry that matches wins, as in the decoder.
     */
    data class EnumTable(val prefix: ByteArray?, val table: List<ByteArray>, val postfix: ByteArray?) : Access() {
        /** The bytes it takes when every entry has the same size. */
        val fixedBytes: Int? get() = table.map { it.size }.distinct().singleOrNull()?.let { (prefix?.size ?: 0) + it + (postfix?.size ?: 0) }
    }

    data class Nested(val context: ViewContext) : Access()

    /** A sealed layout, framed by the [context] of its sealed interface. */
    data class Sealed(val context: ViewContext) : Access()

    /**
     * A collection: its [count] marking, the item frames every item (for a map, every key and every value) is
     * encoded in, and, under `@NullIfEmpty`, the flag bit that is clear when it is empty.
     */
    data class Collection(val count: Count, val items: List<ItemFrame>, val emptyBit: Int?) : Access()

    /** A value class with a prefix and/or postfix of its own around [inner]. */
    data class Framed(val prefix: ByteArray?, val inner: Access, val checksum: ChecksumSpec?, val checksumOrder: ByteOrder, val postfix: ByteArray?) : Access()
}

/**
 * A property of a planned frame, or the single value of a collection item frame.
 * @property start where its body content starts, after any reserved bytes before it, following the decoder.
 * @property end the anchor after it when it moves what follows at runtime (by its presence or size).
 * @property settable whether a setter can rewrite it in place: it has a fixed size, is not optional and gates no
 * other property's presence.
 */
internal class FieldPlan(
    val name: String,
    val node: KSNode,
    val field: FieldLayout,
    val value: ValueType,
    val nullable: Boolean,
    val access: Access,
    val presence: Presence?,
    val start: Cursor,
    val end: Anchor.End?,
    val settable: Boolean,
)

/**
 * One compiled layout of [layout] in a particular context: its own root layout, or the layout it gets nested in a
 * parent whose field annotations or byte order change its encoding. Contexts that compile to the same layout share
 * one [layoutName] object and one [sliceName] class.
 */
internal class ViewContext(val layout: Layout, val field: FieldLayout, val fingerprint: String, val index: Int) {

    private val baseName: String = layout.viewClassName.simpleName.removeSuffix("View")

    val layoutName: ClassName = ClassName(layout.viewClassName.packageName, baseName + (if (layout.isSealed) "Sealed" else "Layout") + if (index == 0) "" else "$index")
    val sliceName: ClassName = ClassName(layout.viewClassName.packageName, baseName + "Slice" + if (index == 0) "" else "$index")

    /** The planned frame of a structure, or why it cannot be planned; set by [ViewPlanner]. */
    var plan: Result<ViewPlan>? = null

    /** The planned frame of a sealed layout, or why it cannot be planned; set by [ViewPlanner]. */
    var sealedPlan: Result<SealedPlan>? = null

    val isPlanned: Boolean get() = (if (layout.isSealed) sealedPlan else plan)?.isSuccess == true
}

/**
 * The frame of a sealed layout: its prefix, then the subtype identified by its discriminator (or, when [sizeDispatch]
 * is set, by the number of bytes left), then its postfix.
 * @property sizeDispatch the index into [leaves] by payload size, when the subtype is picked by size.
 * @property fallback the index into [leaves] used when no size matches.
 */
internal class SealedPlan(
    val prefix: ByteArray?,
    val postfix: ByteArray?,
    val leaves: List<SealedLeaf>,
    val sizeDispatch: Map<Int, Int>?,
    val fallback: Int?,
    val serialName: String,
    val checksum: ChecksumSpec?,
    val checksumOrder: ByteOrder,
) {
    val footer: Int get() = (checksum?.crc?.byteWidth ?: 0) + (postfix?.size ?: 0)
}

/** A subtype of a sealed frame, encoded as [context] after its [discriminator]. */
internal class SealedLeaf(val layout: Layout, val context: ViewContext, val discriminator: ByteArray?)

/**
 * The frame of a context: its prefix, a flag header of [headerBits] from [headerStart], its [fields], the [checksum]
 * of all of that but the prefix, stored in [checksumOrder], and its postfix.
 *
 * A [mirrored] frame is accumulated MOST_SIGNIFICANT_FIRST: the encoder prepends every value to what it wrote before
 * and the decoder reads from the end, so logical offset `k` of a value `w` bytes long is at `s - k - w`, with `s` the
 * end of the frame.
 * @property bodyEnd the cursor after the last property, following the decoder.
 * @property stuffing how a root frame stuffs everything between its prefix and postfix on the wire; the view holds
 * the frame unstuffed.
 * @property staticSize the frame size in bytes when it does not depend on the content.
 */
internal class ViewPlan(
    val prefix: ByteArray?,
    val postfix: ByteArray?,
    val headerStart: Int,
    val headerBits: Int,
    val fields: List<FieldPlan>,
    val bodyEnd: Cursor,
    val checksum: ChecksumSpec? = null,
    val checksumOrder: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST,
    val mirrored: Boolean = false,
    val stuffing: ByteStuffingScheme? = null,
) {
    /** The bytes after the body: the checksum, then the postfix. */
    val footer: Int get() = (checksum?.crc?.byteWidth ?: 0) + (postfix?.size ?: 0)
    val staticSize: Int? get() = if (bodyEnd.anchor == null) alignBits(bodyEnd.bits) / Byte.SIZE_BITS + footer else null
}

/** How a collection records how many items it has. */
internal sealed class Count {
    /** A length prefix before the items, like a string's. */
    data class Prefix(val marking: StringEncodingSettings.LengthPrefix, val order: ByteOrder) : Count()

    /** A count encoded as a number before the items, decoded in [readOrder] and encoded in [writeOrder]. */
    data class Number(val width: Width, val readOrder: ByteOrder, val writeOrder: ByteOrder) : Count()

    /** A count packed into [bits] flag header bits at [bitPos]. */
    data class Flag(val bitPos: Int, val bits: Int) : Count()

    /** The items end at a [terminator] byte. */
    data class Terminated(val terminator: Byte) : Count()

    /**
     * The items are encoded one after the other, stuffed with [scheme] as a whole and ended by an unescaped
     * [terminator]; the decoder unstuffs them into a separate array and reads items up to its end.
     */
    data class Stuffed(val scheme: ByteStuffingScheme, val terminator: Byte) : Count()

    /** The items run to the end of the body. */
    data object Unmarked : Count()
}

/**
 * The frame each item of a collection is encoded in: its own prefix and flag header, then the item, padded to a
 * byte. Generated as the [objectName] object, with the same accessors a layout object has for its one [field].
 */
internal class ItemFrame(val objectName: ClassName, val plan: ViewPlan) {
    val field: FieldPlan get() = plan.fields.single()
}
