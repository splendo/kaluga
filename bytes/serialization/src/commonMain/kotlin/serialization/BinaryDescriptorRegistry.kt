/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.layout.ByteArrayHolder
import com.splendo.kaluga.bytes.layout.CollectionSettings
import com.splendo.kaluga.bytes.layout.FieldLayout
import com.splendo.kaluga.bytes.layout.FrameSettings
import com.splendo.kaluga.bytes.layout.LayoutCompiler
import com.splendo.kaluga.bytes.layout.NumericSettings
import com.splendo.kaluga.bytes.layout.StringSettings
import com.splendo.kaluga.bytes.layout.TypeLayout
import com.splendo.kaluga.bytes.layout.UnsupportedByteStuffing
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.modules.SerializersModule

/**
 * The encoder/decoder view of a [FieldLayout]: its type-specific settings flattened into nullable fields.
 */
internal data class BinaryDescriptor(
    val fieldName: String,
    val fieldIndex: Int,
    val bitIndex: Int,
    val bitWidth: Int,
    val byteOrder: ByteOrder,
    val childByteOrder: ByteOrder = byteOrder,
    val isNullable: Boolean,
    val numericSettings: NumericSettings?,
    val stringSettings: StringSettings?,
    val collectionSettings: CollectionSettings?,
    val enumMap: Map<Int, ByteArrayHolder>,
    val polymorphicMap: Map<String, ByteArrayHolder>,
    val structureSettings: FrameSettings,
    val children: List<BinaryDescriptor>,
    // The flag bits (owned by other properties) whose conjunction determines this property's presence; empty for a
    // property that owns its presence bit at [bitIndex] (or is not nullable).
    val presenceFlagIndices: List<Int> = emptyList(),
    // When non-empty, subtype is selected by remaining payload byte count instead of a byte prefix.
    val sizePolymorphicMap: Map<Int, String> = emptyMap(),
    // Optional catch-all used when no fixed-size entry matches — allows one variable-size subtype.
    val sizePolymorphicFallback: String? = null,
    // Reserved (padding) bytes to silently skip/write around this property on the wire.
    val reservedBefore: Int = 0,
    val reservedAfter: Int = 0,
) {

    /**
     * Number of bits used by this flag based on its children, including any bits a child derives its presence from.
     */
    val flagBitSize = if (children.isNotEmpty()) {
        children.maxOf {
            maxOf(
                if (it.bitIndex >= 0) it.bitIndex + it.bitWidth else 0,
                it.presenceFlagIndices.maxOrNull()?.plus(1) ?: 0,
            )
        }
    } else {
        0
    }

    /** Whether this property is present given the decoded [flags]: derived from [presenceFlagIndices], else its own bit. */
    fun isPresent(flags: BooleanArray): Boolean = when {
        presenceFlagIndices.isNotEmpty() -> presenceFlagIndices.all { flags.getOrElse(it) { false } }
        isNullable -> flags[bitIndex]
        else -> true
    }
}

// Stuffing operates on the frame's accumulation direction, which must be least-significant-first (a full MSB reversal
// would reorder the stuffed bytes). `@ByteOrder(MSB, excludeStructure = true)` keeps the builder LSB and is allowed.
internal fun BinaryDescriptor.requireLeastSignificantFirstForStuffing() {
    if (byteOrder != ByteOrder.LEAST_SIGNIFICANT_FIRST) {
        throw UnsupportedByteStuffing("@ByteStuffed is only supported for LEAST_SIGNIFICANT_FIRST byte order")
    }
}

internal fun FieldLayout.toBinaryDescriptor(): BinaryDescriptor {
    val type = type
    return BinaryDescriptor(
        fieldName = name,
        fieldIndex = index,
        bitIndex = bitIndex,
        bitWidth = bitWidth,
        byteOrder = byteOrder,
        childByteOrder = valueByteOrder,
        isNullable = isNullable,
        numericSettings = (type as? TypeLayout.Numeric)?.settings,
        stringSettings = when (type) {
            is TypeLayout.Text -> type.settings
            is TypeLayout.Character -> StringSettings(type.encoding, StringEncodingSettings.NoMarking)
            else -> null
        },
        collectionSettings = when (type) {
            is TypeLayout.ListLayout -> type.settings
            is TypeLayout.MapLayout -> type.settings
            else -> null
        },
        enumMap = (type as? TypeLayout.Enumeration)?.values ?: emptyMap(),
        polymorphicMap = when (type) {
            is TypeLayout.Sealed -> type.discriminators
            is TypeLayout.Open -> type.discriminators
            else -> emptyMap()
        },
        structureSettings = frame,
        children = type.children.map { it.toBinaryDescriptor() },
        presenceFlagIndices = presenceFlagIndices,
        sizePolymorphicMap = (type as? TypeLayout.Sealed)?.sizeDispatch ?: emptyMap(),
        sizePolymorphicFallback = (type as? TypeLayout.Sealed)?.sizeFallback,
        reservedBefore = reservedBefore,
        reservedAfter = reservedAfter,
    )
}

internal object BinaryDescriptorRegistry {

    private class Compiled(val layout: FieldLayout) {
        val binaryDescriptor = layout.toBinaryDescriptor()
    }

    // Compiling is a pretty heavy operation so we store it in a cache.
    private val cache = concurrentMutableMapOf<Pair<SerialDescriptor, SerializersModule>, Compiled>()

    private fun compiled(descriptor: SerialDescriptor, module: SerializersModule): Compiled = cache.getOrPut(descriptor to module) {
        Compiled(LayoutCompiler.compile(SerialDescriptorShape(descriptor, module)))
    }

    internal fun layout(descriptor: SerialDescriptor, module: SerializersModule): FieldLayout = compiled(descriptor, module).layout

    internal fun binaryDescriptor(descriptor: SerialDescriptor, module: SerializersModule): BinaryDescriptor = compiled(descriptor, module).binaryDescriptor
}
