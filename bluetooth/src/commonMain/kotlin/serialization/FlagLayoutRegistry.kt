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

package com.splendo.kaluga.bluetooth.serialization

import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlin.math.ceil
import kotlin.math.sqrt

data class FlagLayoutEntry(
    val fieldName: String,
    val fieldIndex: Int,
    val bitIndex: Int,
    val bitWidth: Int,
    val isNullable: Boolean
)

class FlagLayoutException(message: String) : SerializationException(message)

object FlagLayoutRegistry {
    private val cache = mutableMapOf<SerialDescriptor, List<FlagLayoutEntry>>()

    fun getLayout(descriptor: SerialDescriptor): List<FlagLayoutEntry> = cache.getOrPut(descriptor) {
        var nextBit = 0
        val reservedIndices = mutableSetOf<Int>()
        (0 until descriptor.elementsCount).map { i ->
            val elementName = descriptor.getElementName(i)
            val annotations = descriptor.getElementAnnotations(i)
            val isNullable = descriptor.isElementOptional(i) ||
                    descriptor.getElementDescriptor(i).isNullable
            val customIndex = annotations.filterIsInstance<FlagIndex>().firstOrNull()?.index
            val autoSizing = annotations.filterIsInstance<AutoSizing>().firstOrNull()?.let { autoSizing ->
                if (autoSizing.maxByteSize - autoSizing.minByteSize > 0) {
                    val numberOfSteps = (autoSizing.maxByteSize - autoSizing.minByteSize / autoSizing.steps)
                    when (val root = sqrt(numberOfSteps.toDouble())) {
                        1.0 -> 1
                        ceil(root) -> root.toInt() + 1
                        else -> ceil(root).toInt()
                    }
                } else {
                    null
                }
            }
            val defaultWidth = (autoSizing ?: 0) + (if (isNullable) 1 else 0)
            val width = annotations.filterIsInstance<FlagWidth>().firstOrNull()?.bits ?: defaultWidth

            val bitIndex = customIndex ?: if (isNullable) nextBit else -1
            if (bitIndex >= 0) {
                val flagIndicesToUse = (0..width).map { bitIndex + it }.toSet()
                if (flagIndicesToUse.any { it in reservedIndices }) {
                    throw FlagLayoutException("Flag at index $bitIndex cannot be used for $elementName. Is already reserved")
                }
                reservedIndices += flagIndicesToUse
            }
            if (width > 0 && customIndex == null) nextBit += width
            FlagLayoutEntry(
                fieldName = elementName,
                fieldIndex = i,
                bitIndex = bitIndex,
                bitWidth = width,
                isNullable = isNullable
            )
        }
    }
}
