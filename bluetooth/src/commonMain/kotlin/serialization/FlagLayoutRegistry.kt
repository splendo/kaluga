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
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.getPolymorphicDescriptors
import kotlinx.serialization.modules.SerializersModule
import kotlin.jvm.JvmInline
import kotlin.math.ceil
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
        data class Natural(override val supportedLengths: Set<Length>, val signed: Boolean) : NumericSettings() {
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

        data class Decimal(override val supportedLengths: Set<Length>) : NumericSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
                require((supportedLengths - setOf(Length.`32_BIT`, Length.`64_BIT`)).isEmpty()) { "Decimal only supports 32 and 64 bit encoding" }
            }
        }

        data class MedFloat(override val supportedLengths: Set<Length>) : NumericSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
                require((supportedLengths - setOf(Length.`16_BIT`, Length.`32_BIT`)).isEmpty()) { "MedFloat only supports 16 and 32 bit encoding" }
            }
        }
    }
    data class StringSettings(val encoding: StringEncodingSettings.Encoding, val endMarking: StringEncodingSettings.EndMarking)

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

    data class BlockSettings(val prefix: ByteArrayHolder?, val postfix: ByteArrayHolder?, val checksumAlgorithm: ChecksumAlgorithm)
}

@JvmInline
value class ByteArrayHolder(val array: ByteArray) {
    override fun toString(): String = array.toHexString(separator = " ")
}

class FlagLayoutException(message: String) : SerializationException(message)
class InvalidByteOrderException(message: String) : SerializationException(message)

object FlagLayoutRegistry {
    private val cache = mutableMapOf<Pair<SerialDescriptor, SerializersModule>, FlagLayoutEntry>()

    internal fun flagLayoutEntry(descriptor: SerialDescriptor, module: SerializersModule): FlagLayoutEntry = cache.getOrPut(descriptor to module) {
        getLayout(
            descriptor,
            descriptor.serialName,
            0,
            emptyList(),
            descriptor.isNullable,
            0,
            descriptor.annotations.filterIsInstance<com.splendo.kaluga.bluetooth.serialization.ByteOrder>().firstOrNull()?.order ?: ByteOrder.LEAST_SIGNIFICANT_FIRST,
            module,
        ) {
        }
    }

    private fun getLayout(
        descriptor: SerialDescriptor,
        fieldName: String,
        fieldIndex: Int,
        fieldAnnotations: List<Annotation>,
        isNullable: Boolean,
        defaultBitIndex: Int,
        preferredByteOrder: ByteOrder,
        serializersModule: SerializersModule,
        reserveIndices: (Set<Int>) -> Unit,
    ): FlagLayoutEntry = if (descriptor.isInline) {
        val inlineDescriptor = descriptor.getElementDescriptor(0)
        val annotations = descriptor.annotations + fieldAnnotations + descriptor.getElementAnnotations(0)
        val actualAnnotations = when (descriptor.serialName) {
            "kotlin.UByte" -> annotations + Unsigned()
            "kotlin.UShort" -> annotations + Unsigned()
            "kotlin.UInt" -> annotations + Unsigned()
            "kotlin.ULong" -> annotations + Unsigned()
            else -> annotations
        }
        getLayout(
            inlineDescriptor,
            fieldName,
            fieldIndex,
            actualAnnotations,
            isNullable || inlineDescriptor.isNullable,
            defaultBitIndex,
            preferredByteOrder,
            serializersModule,
            reserveIndices,
        )
    } else {
        val annotations = descriptor.annotations + fieldAnnotations
        var desiredWidth = 0
        val isNullable = isNullable || (descriptor.kind in setOf(StructureKind.LIST, StructureKind.MAP) && annotations.filterIsInstance<NullIfEmpty>().isNotEmpty())
        if (isNullable) {
            desiredWidth++
        }
        val customIndex = annotations.filterIsInstance<FlagIndex>().firstOrNull()?.index
        val byteOrder = annotations.filterIsInstance<com.splendo.kaluga.bluetooth.serialization.ByteOrder>().firstOrNull()?.order ?: preferredByteOrder
        if ((descriptor.kind is StructureKind || descriptor.kind is PrimitiveKind.STRING) && byteOrder != preferredByteOrder) {
            throw InvalidByteOrderException("Nested class ${descriptor.serialName} cannot have a byteOrder different than $preferredByteOrder")
        }
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
        }.sortedBy { it.bytes }.toSet()
        val sizingWidth = when (supportedLengths.size) {
            0 -> 0
            1 -> 0
            2 -> 1
            else -> ceil(sqrt(supportedLengths.size.toDouble())).toInt()
        }
        val numericSettings = when (descriptor.kind) {
            PrimitiveKind.INT,
            PrimitiveKind.BYTE,
            PrimitiveKind.SHORT,
            PrimitiveKind.LONG,
            PrimitiveKind.DOUBLE,
            PrimitiveKind.FLOAT,
            -> {
                desiredWidth += sizingWidth
                if (annotations.filterIsInstance<MedFloat>().isNotEmpty()) {
                    FlagLayoutEntry.NumericSettings.MedFloat(supportedLengths)
                } else if (annotations.filterIsInstance<Scalar>().isNotEmpty()) {
                    val scalar = annotations.filterIsInstance<Scalar>().first()
                    val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
                    FlagLayoutEntry.NumericSettings.Scalar(supportedLengths, isSigned, scalar.multiplier, scalar.decimalExponent, scalar.binaryExponent, scalar.offset)
                } else if (descriptor.kind == PrimitiveKind.DOUBLE || descriptor.kind == PrimitiveKind.FLOAT) {
                    FlagLayoutEntry.NumericSettings.Decimal(supportedLengths)
                } else {
                    val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
                    FlagLayoutEntry.NumericSettings.Natural(supportedLengths, isSigned)
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

            PrimitiveKind.CHAR -> FlagLayoutEntry.StringSettings(
                annotations.filterIsInstance<Encoded>().firstOrNull()?.encoding ?: StringEncodingSettings.Encoding.UTF_8,
                StringEncodingSettings.NoMarking,
            )

            else -> null
        }

        val collectionSettings = when (descriptor.kind) {
            is StructureKind.LIST,
            is StructureKind.MAP,
            -> {
                when {
                    annotations.filterIsInstance<NullTerminated>().isNotEmpty() -> FlagLayoutEntry.CollectionSettings.NullMarked
                    annotations.filterIsInstance<LengthPrefix>().isNotEmpty() -> {
                        val lengthPrefix = annotations.filterIsInstance<LengthPrefix>().first()
                        FlagLayoutEntry.CollectionSettings.LengthPrefix(
                            StringEncodingSettings.LengthPrefix(lengthPrefix.lengthAsShort, lengthPrefix.canOverflow, lengthPrefix.sentinel),
                        )
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

        val polymorphicMap = when (descriptor.kind) {
            is PolymorphicKind.SEALED -> {
                val sealedDescriptor = descriptor.getElementDescriptor(1)
                val polymorphicMap = (0..<sealedDescriptor.elementsCount).mapNotNull { index ->
                    val optionDescriptor = sealedDescriptor.getElementDescriptor(index)
                    optionDescriptor.annotations.filterIsInstance<SerializedByteValue>().firstOrNull()?.let {
                        optionDescriptor.serialName to it.value
                    }
                }.toMap()
                if (polymorphicMap.size == sealedDescriptor.elementsCount) polymorphicMap else emptyMap()
            }

            is PolymorphicKind.OPEN -> {
                val polymorphicDescriptors = serializersModule.getPolymorphicDescriptors(descriptor)
                val polymorphicMap = polymorphicDescriptors.mapNotNull { optionDescriptor ->
                    optionDescriptor.annotations.filterIsInstance<SerializedByteValue>().firstOrNull()?.let {
                        optionDescriptor.serialName to it.value
                    }
                }.toMap()
                if (polymorphicMap.size == polymorphicDescriptors.size) polymorphicMap else emptyMap()
            }

            else -> emptyMap()
        }

        val blockSettings = FlagLayoutEntry.BlockSettings(
            annotations.filterIsInstance<Prefix>().firstOrNull()?.value?.let { ByteArrayHolder(it) },
            annotations.filterIsInstance<Postfix>().firstOrNull()?.value?.let { ByteArrayHolder(it) },
            annotations.filterIsInstance<Checksum>().firstOrNull()?.algorithm ?: ChecksumAlgorithm.NONE,
        )
        val width = annotations.filterIsInstance<FlagWidth>().firstOrNull()?.bits ?: desiredWidth
        val bitIndex = if (width > 0) {
            customIndex ?: defaultBitIndex
        } else {
            -1
        }
        reserveIndices((0..<width).map { bitIndex + it }.toSet())
        val reservedSubIndices = mutableSetOf<Int>()
        var nextBit = 0

        FlagLayoutEntry(
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
            if (descriptor.kind == PolymorphicKind.OPEN) {
                serializersModule.getPolymorphicDescriptors(descriptor).map { optionDescriptor ->
                    getLayout(
                        optionDescriptor,
                        optionDescriptor.serialName,
                        0,
                        optionDescriptor.annotations,
                        optionDescriptor.isNullable,
                        nextBit,
                        byteOrder,
                        serializersModule,
                    ) { flagIndicesToUse ->
                        if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                            throw FlagLayoutException("Flag at index $bitIndex cannot be used for ${optionDescriptor.serialName}. Is already reserved")
                        }
                        reservedSubIndices += flagIndicesToUse
                        while (nextBit in reservedSubIndices) {
                            nextBit++
                        }
                    }
                }
            } else {
                (0 until descriptor.elementsCount).map { i ->
                    val elementName = descriptor.getElementName(i)
                    val elementAnnotations = when (descriptor.kind) {
                        StructureKind.MAP -> if (i % 2 == 0) annotations.keyAnnotations() else annotations.valueAnnotations()
                        StructureKind.LIST -> annotations.itemAnnotations()
                        else -> descriptor.getElementAnnotations(i)
                    }
                    val elementDescriptor = descriptor.getElementDescriptor(i)
                    getLayout(elementDescriptor, elementName, i, elementAnnotations, elementDescriptor.isNullable, nextBit, byteOrder, serializersModule) { flagIndicesToUse ->
                        if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                            throw FlagLayoutException("Flag at index $bitIndex cannot be used for $elementName. Is already reserved")
                        }
                        reservedSubIndices += flagIndicesToUse
                        while (nextBit in reservedSubIndices) {
                            nextBit++
                        }
                    }
                }
            },
        )
    }

    private fun List<Annotation>.itemAnnotations(): List<Annotation> = mapNotNull { annotation ->
        when (annotation) {
            is ItemByteOrder -> ByteOrder(annotation.order)
            is ItemLengthPrefix -> LengthPrefix(annotation.lengthAsShort, annotation.canOverflow, annotation.sentinel)
            is ItemEncoded -> Encoded(annotation.encoding)
            is ItemNullTerminated -> NullTerminated()
            is ItemUnsigned -> Unsigned()
            is ItemScalar -> Scalar(annotation.multiplier, annotation.decimalExponent, annotation.binaryExponent, annotation.offset)
            is ItemMedFloat -> MedFloat()
            is ItemSize -> Sizing(annotation.size)
            else -> null
        }
    }

    private fun List<Annotation>.keyAnnotations(): List<Annotation> = mapNotNull { annotation ->
        when (annotation) {
            is KeyByteOrder -> ByteOrder(annotation.order)
            is KeyLengthPrefix -> LengthPrefix(annotation.lengthAsShort, annotation.canOverflow, annotation.sentinel)
            is KeyEncoded -> Encoded(annotation.encoding)
            is KeyNullTerminated -> NullTerminated()
            is KeyUnsigned -> Unsigned()
            is KeyScalar -> Scalar(annotation.multiplier, annotation.decimalExponent, annotation.binaryExponent, annotation.offset)
            is KeyMedFloat -> MedFloat()
            is KeySize -> Sizing(annotation.size)
            else -> null
        }
    }

    private fun List<Annotation>.valueAnnotations(): List<Annotation> = mapNotNull { annotation ->
        when (annotation) {
            is ValueByteOrder -> ByteOrder(annotation.order)
            is ValueLengthPrefix -> LengthPrefix(annotation.lengthAsShort, annotation.canOverflow, annotation.sentinel)
            is ValueEncoded -> Encoded(annotation.encoding)
            is ValueNullTerminated -> NullTerminated()
            is ValueUnsigned -> Unsigned()
            is ValueScalar -> Scalar(annotation.multiplier, annotation.decimalExponent, annotation.binaryExponent, annotation.offset)
            is ValueMedFloat -> MedFloat()
            is ValueSize -> Sizing(annotation.size)
            else -> null
        }
    }
}
