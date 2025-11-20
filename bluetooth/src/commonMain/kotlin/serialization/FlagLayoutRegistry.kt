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

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.utils.toHexString
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlin.jvm.JvmInline
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

data class FlagLayoutEntry(
    val fieldName: String,
    val fieldIndex: Int,
    val bitIndex: Int,
    val bitWidth: Int,
    val byteOrder: ByteOrder,
    val isNullable: Boolean,
    val numericSettings: NumericSettings?,
    val stringSettings: StringSettings?,
    val collectionSettings: CollectionSettings?,
    val polymorphicMap: Map<String, Byte>,
    val blockSettings: BlockSettings,
    val children: List<FlagLayoutEntry>,
) {

    sealed class NumericSettings {

        abstract val supportedLengths: Set<Length>
        data class Natural(
            override val supportedLengths: Set<Length>,
            val signed: Boolean,
        ) : NumericSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
            }
        }

        data class Scalar(
            override val supportedLengths: Set<Length>,
            val signed: Boolean,
            val multiplier: Int,
            val decimalExponent: Int,
            val binaryExponent: Int,
            val offset: Int,
        ) : NumericSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
            }
        }

        data class Decimal(
            override val supportedLengths: Set<Length>,
        ) : NumericSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
                require((supportedLengths -setOf(Length.`32_BIT`, Length.`64_BIT`)).isEmpty()) { "Decimal only supports 32 and 64 bit encoding" }
            }
        }

        data class MedFloat(
            override val supportedLengths: Set<Length>,
        ) : NumericSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
                require((supportedLengths -setOf(Length.`16_BIT`, Length.`32_BIT`)).isEmpty()) { "MedFloat only supports 16 and 32 bit encoding" }
            }
        }
    }
    data class StringSettings(
        val encoding: StringEncodingSettings.Encoding,
        val endMarking: StringEncodingSettings.EndMarking,
    )

    sealed class CollectionSettings {
        data class LengthPrefix(val endMarking: StringEncodingSettings.LengthPrefix) : CollectionSettings()
        data object NullMarked : CollectionSettings()
        data object Unmarked : CollectionSettings()
        data class NumericLength(val supportedLengths: Set<Length>) : CollectionSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
            }
        }
    }

    data class BlockSettings(
        val prefix: ByteArrayHolder?,
        val postfix: ByteArrayHolder?,
        val checksumAlgorithm: ChecksumAlgorithm
    )
}

@JvmInline
value class ByteArrayHolder(val array: ByteArray) {
    override fun toString(): String = array.toHexString(separator = " ")
}

class FlagLayoutException(message: String) : SerializationException(message)

internal val SerialDescriptor.flagLayoutEntry: FlagLayoutEntry get() = getLayout(this, serialName, 0, emptyList(), 0, ByteOrder.LEAST_SIGNIFICANT_FIRST) {}

    private fun getLayout(
        descriptor: SerialDescriptor,
        fieldName: String,
        fieldIndex: Int,
        fieldAnnotations: List<Annotation>,
        defaultBitIndex: Int,
        preferredByteOrder: ByteOrder,
        reserveIndices: (Set<Int>) -> Unit
    ): FlagLayoutEntry {
        val annotations = descriptor.annotations + fieldAnnotations
        var desiredWidth = 0
        val isNullable = descriptor.isNullable || (descriptor.kind in setOf(StructureKind.LIST, StructureKind.MAP) && annotations.filterIsInstance<NullIfEmpty>().isNotEmpty())
        if (isNullable) {
            desiredWidth++
        }
        val customIndex = annotations.filterIsInstance<FlagIndex>().firstOrNull()?.index
        val byteOrder = annotations.filterIsInstance<com.splendo.kaluga.bluetooth.serialization.ByteOrder>().firstOrNull()?.order ?: preferredByteOrder
        if (descriptor.kind is PrimitiveKind.BOOLEAN && customIndex != null) {
            desiredWidth++
        }
        val supportedLengths = annotations.filterIsInstance<Sizing>().map { it.length }.toSet().ifEmpty {
            when (descriptor.kind) {
                PrimitiveKind.BYTE -> setOf(Length.`8_BIT`)
                PrimitiveKind.SHORT -> setOf(Length.`16_BIT`)
                PrimitiveKind.INT -> setOf(Length.`32_BIT`)
                PrimitiveKind.LONG -> setOf(Length.`64_BIT`)
                PrimitiveKind.FLOAT -> when {
                    annotations.filterIsInstance<MedFloat>().isNotEmpty() -> setOf(Length.`16_BIT`)
                    annotations.filterIsInstance<Scalar>().isNotEmpty() -> setOf(Length.`32_BIT`)
                    else -> setOf(Length.`32_BIT`)
                }
                PrimitiveKind.DOUBLE -> when {
                    annotations.filterIsInstance<MedFloat>().isNotEmpty() -> setOf(Length.`32_BIT`)
                    annotations.filterIsInstance<Scalar>().isNotEmpty() -> setOf(Length.`32_BIT`)
                    else -> setOf(Length.`64_BIT`)
                }
                StructureKind.MAP -> setOf(Length.`8_BIT`)
                StructureKind.LIST -> setOf(Length.`8_BIT`)
                else -> emptySet()
            }
        }
        val sizingWidth = when (supportedLengths.size) {
            0 -> 0
            1 -> 0
            2 -> 1
            else -> floor(sqrt(supportedLengths.size.toDouble())).toInt() + 1
        }
        val numericSettings = when (descriptor.kind) {
            PrimitiveKind.INT,
            PrimitiveKind.BYTE,
            PrimitiveKind.SHORT,
            PrimitiveKind.LONG -> {
                desiredWidth += sizingWidth
                val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
                annotations.filterIsInstance<Scalar>().firstOrNull()?.let { scalar ->
                    FlagLayoutEntry.NumericSettings.Scalar(supportedLengths, isSigned, scalar.multiplier, scalar.decimalExponent, scalar.binaryExponent, scalar.offset)
                } ?: FlagLayoutEntry.NumericSettings.Natural(supportedLengths, isSigned)
            }
            PrimitiveKind.DOUBLE,
            PrimitiveKind.FLOAT -> {
                if (annotations.filterIsInstance<MedFloat>().isNotEmpty()) {
                    desiredWidth += sizingWidth
                    FlagLayoutEntry.NumericSettings.MedFloat(supportedLengths)
                } else if (annotations.filterIsInstance<Scalar>().isNotEmpty()) {
                    desiredWidth += sizingWidth
                    val scalar = annotations.filterIsInstance<Scalar>().first()
                    val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
                    FlagLayoutEntry.NumericSettings.Scalar(supportedLengths, isSigned, scalar.multiplier, scalar.decimalExponent, scalar.binaryExponent, scalar.offset)
                } else {
                    desiredWidth += sizingWidth
                    FlagLayoutEntry.NumericSettings.Decimal(supportedLengths)
                }
            }
            else -> null
        }
        val stringSettings = when (descriptor.kind) {
            PrimitiveKind.STRING -> {
                val encoding = annotations.filterIsInstance<Encoded>().firstOrNull()?.encoding ?: StringEncodingSettings.Encoding.UTF_8
                when {
                    annotations.filterIsInstance<NullTerminated>().isNotEmpty() -> FlagLayoutEntry.StringSettings(encoding, StringEncodingSettings.NullTerminated)
                    annotations.filterIsInstance<LengthPrefix>().isNotEmpty() -> {
                        val lengthPrefix = annotations.filterIsInstance<LengthPrefix>().first()
                        FlagLayoutEntry.StringSettings(encoding, StringEncodingSettings.LengthPrefix(lengthPrefix.lengthAsShort, lengthPrefix.canOverflow, lengthPrefix.sentinel))
                    }
                    supportedLengths.size == 1 -> {
                        FlagLayoutEntry.StringSettings(encoding, StringEncodingSettings.FixedLength(supportedLengths.first().bytes))
                    }
                    annotations.filterIsInstance<Unsized>().isNotEmpty() -> FlagLayoutEntry.StringSettings(encoding, StringEncodingSettings.NoMarking)
                    else -> FlagLayoutEntry.StringSettings(encoding, StringEncodingSettings.LengthPrefix())
                }
            }
            PrimitiveKind.CHAR -> FlagLayoutEntry.StringSettings(annotations.filterIsInstance<Encoded>().firstOrNull()?.encoding ?: StringEncodingSettings.Encoding.UTF_8, StringEncodingSettings.NoMarking)
            else -> null
        }

        val collectionSettings = when (descriptor.kind) {
            is StructureKind.LIST,
                is StructureKind.MAP -> {
                    when {
                        annotations.filterIsInstance<NullTerminated>().isNotEmpty() -> FlagLayoutEntry.CollectionSettings.NullMarked
                        annotations.filterIsInstance<LengthPrefix>().isNotEmpty() -> {
                            val lengthPrefix = annotations.filterIsInstance<LengthPrefix>().first()
                            FlagLayoutEntry.CollectionSettings.LengthPrefix(StringEncodingSettings.LengthPrefix(lengthPrefix.lengthAsShort, lengthPrefix.canOverflow, lengthPrefix.sentinel))
                        }
                        annotations.filterIsInstance<Unsized>().isNotEmpty() -> FlagLayoutEntry.CollectionSettings.Unmarked
                        else -> {
                            desiredWidth += sizingWidth
                            FlagLayoutEntry.CollectionSettings.NumericLength(supportedLengths)
                        }
                    }
                }
            else -> null
        }

        val polymorphicMap = if (descriptor.kind is PolymorphicKind) {
            val sealedDescriptor = descriptor.getElementDescriptor(1)
            val polymorphicMap = (0..<sealedDescriptor.elementsCount).mapNotNull { index ->
                val optionDescriptor = sealedDescriptor.getElementDescriptor(index)
                optionDescriptor.annotations.filterIsInstance<SerializedByteValue>().firstOrNull()?.let {
                    optionDescriptor.serialName to it.value
                }
            }.toMap()
            if (polymorphicMap.size == sealedDescriptor.elementsCount) polymorphicMap else emptyMap()
        } else emptyMap()

        val blockSettings = FlagLayoutEntry.BlockSettings(
            annotations.filterIsInstance<Prefix>().firstOrNull()?.value?.let { ByteArrayHolder(it) },
            annotations.filterIsInstance<Postfix>().firstOrNull()?.value?.let { ByteArrayHolder(it) },
            annotations.filterIsInstance<Checksum>().firstOrNull()?.algorithm ?: ChecksumAlgorithm.NONE,
        )
        val width = annotations.filterIsInstance<FlagWidth>().firstOrNull()?.bits ?: desiredWidth
        val bitIndex = if (width > 0) {
            customIndex ?: defaultBitIndex
        } else -1
        reserveIndices((0..<width).map { bitIndex + it }.toSet())
        val reservedSubIndices = mutableSetOf<Int>()
        var nextBit = 0
        return FlagLayoutEntry(
            fieldName,
            fieldIndex,
            bitIndex,
            width,
            byteOrder,
            isNullable,
            numericSettings,
            stringSettings,
            collectionSettings,
            polymorphicMap,
            blockSettings,
            (0 until descriptor.elementsCount).map { i ->
                val elementName = descriptor.getElementName(i)
                val annotations = descriptor.getElementAnnotations(i)
                val elementDescriptor = descriptor.getElementDescriptor(i)
                getLayout(elementDescriptor, elementName, i, annotations, nextBit, byteOrder) { flagIndicesToUse ->
                    if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                        throw FlagLayoutException("Flag at index $bitIndex cannot be used for $elementName. Is already reserved")
                    }
                    reservedSubIndices += flagIndicesToUse
                    while (nextBit in reservedSubIndices) {
                        nextBit++
                    }
                }
            }
        )
    }
