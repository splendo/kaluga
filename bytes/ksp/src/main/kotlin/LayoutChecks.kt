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

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.layout.CollectionSettings
import com.splendo.kaluga.bytes.layout.FieldLayout
import com.splendo.kaluga.bytes.layout.NumericSettings
import com.splendo.kaluga.bytes.layout.TypeLayout

/**
 * Compile-time counterparts of configuration errors `BytesFormat` only detects while encoding, so a generated layout
 * reports them as KSP errors instead:
 * - a root frame that is byte-stuffed but not accumulated least significant first;
 * - `DataAfterUnconstrainedData`: a property whose encoded size has no end marking (an `@Unsized` string or
 *   collection, a `@SizePolymorphic` value, a byte-stuffed structure without a terminator, or a structure ending in one) followed by a sibling that always writes body
 *   bytes, since a decoder could never tell where the first one ends. Only configurations that fail for every value
 *   are reported; a nullable or `@NullIfEmpty` unconstrained property is left to the runtime check, as it encodes fine
 *   while absent.
 */
internal object LayoutChecks {

    fun violations(root: FieldLayout): List<String> = buildList {
        if (root.frame.byteStuffing != null && root.byteOrder != ByteOrder.LEAST_SIGNIFICANT_FIRST) {
            add("byte stuffing is only supported for a frame accumulated LEAST_SIGNIFICANT_FIRST; use @ByteOrder(excludeStructure = true) for big-endian values")
        }
        collect(root, this)
    }

    private fun collect(field: FieldLayout, violations: MutableList<String>) {
        val type = field.type
        if (type is TypeLayout.Structure) {
            type.fields.forEachIndexed { index, child ->
                if (child.isAlwaysUnconstrained()) {
                    type.fields.drop(index + 1).firstOrNull { it.alwaysWritesBody() }?.let { follower ->
                        violations += "'${follower.name}' follows '${child.name}' in '${field.name}', but '${child.name}' has no end marking, so no data can follow it"
                    }
                }
            }
        }
        type.children.forEach { collect(it, violations) }
    }

    private fun FieldLayout.isAlwaysUnconstrained(): Boolean = !isNullable && presenceFlagIndices.isEmpty() && when (val type = type) {
        is TypeLayout.Text -> type.settings.endMarking is StringEncodingSettings.NoMarking

        is TypeLayout.ListLayout -> type.settings.lengthMarking is CollectionSettings.Unmarked

        is TypeLayout.MapLayout -> type.settings.lengthMarking is CollectionSettings.Unmarked

        // A byte-stuffed structure without a terminator is bounded by the rest of the payload.
        is TypeLayout.Structure -> (frame.byteStuffing != null && frame.byteStuffingTerminator == null) || type.fields.any { it.isAlwaysUnconstrained() }

        is TypeLayout.Sealed -> type.sizeDispatch.isNotEmpty() || type.sizeFallback != null

        else -> false
    }

    private fun FieldLayout.alwaysWritesBody(): Boolean {
        if (isNullable || presenceFlagIndices.isNotEmpty()) return false
        if (reservedBefore > 0 || reservedAfter > 0) return true
        return when (val type = type) {
            is TypeLayout.Bool -> bitIndex < 0
            is TypeLayout.Numeric -> (type.settings as? NumericSettings.Natural)?.inFlagsBits == null
            is TypeLayout.Text, is TypeLayout.Character -> true
            is TypeLayout.Enumeration -> bitIndex < 0
            is TypeLayout.ListLayout -> type.settings.lengthMarking.writesBody()
            is TypeLayout.MapLayout -> type.settings.lengthMarking.writesBody()
            is TypeLayout.Structure, is TypeLayout.Sealed, is TypeLayout.Open, is TypeLayout.Contextual -> true
        }
    }

    private fun CollectionSettings.LengthMarking.writesBody(): Boolean = when (this) {
        is CollectionSettings.NumericLength, is CollectionSettings.LengthPrefix, is CollectionSettings.TerminalMarked -> true
        is CollectionSettings.Unmarked, is CollectionSettings.FlagIndexedLength -> false
    }
}
