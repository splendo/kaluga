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
import com.splendo.kaluga.base.bytes.Encoding
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.bytes.toByteArray
import com.splendo.kaluga.base.crc.CRC
import com.splendo.kaluga.base.bytes.toHexString
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.getPolymorphicDescriptors
import kotlinx.serialization.modules.SerializersModule
import kotlin.jvm.JvmInline

internal data class BluetoothBinaryDescriptor(
    val fieldName: String,
    val fieldIndex: Int,
    val bitIndex: Int,
    val bitWidth: Int,
    val byteOrder: ByteOrder,
    val isNullable: Boolean,
    val numericSettings: NumericSettings?,
    val stringSettings: StringSettings?,
    val collectionSettings: CollectionSettings?,
    val enumMap: Map<Int, ByteArrayHolder>,
    val polymorphicMap: Map<String, ByteArrayHolder>,
    val structureSettings: StructureSettings,
    val children: List<BluetoothBinaryDescriptor>,
    // The flag bits (owned by other properties) whose conjunction determines this property's presence; empty for a
    // property that owns its presence bit at [bitIndex] (or is not nullable).
    val presenceFlagIndices: List<Int> = emptyList(),
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

    /**
     * NumericSettings to apply when encoding a Primitive Number
     */
    sealed class NumericSettings {

        abstract val supportedLengths: Set<Length>

        /**
         * Encoding as a simple number. The [Length] best matching the value will be picked
         * @property supportedLengths the [Length] values that are supported. The smallest fitting length will be picked.
         * @property signed if the value can be negative
         */

        /**
         * @property inFlagsBits when non-null, the value is packed directly into the flag region across this many bits
         * (least-significant bit first), analogous to an enum's ordinal, rather than encoded as body bytes. Used for
         * sub-byte numeric subfields of a bit field; [supportedLengths] is then unused.
         */
        data class Natural(override val supportedLengths: Set<Length>, val signed: Boolean, val inFlagsBits: Int? = null) : NumericSettings() {
            init {
                require(inFlagsBits != null || supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
                if (inFlagsBits != null) require(inFlagsBits in 1..Long.SIZE_BITS) { "A flag-packed numeric must occupy 1..${Long.SIZE_BITS} bits, was $inFlagsBits" }
            }
        }

        /**
         * Encoding as a Scalar Numeric value
         * @property supportedLengths the [Length] values that are supported. The smallest fitting length will be picked.
         * @property signed if the value can be negative
         * @property multiplier the multiplier to apply to the value before encoding
         * @property decimalExponent the decimal exponent with which to encode the value, so that encoded value is [value * 10^decimalExponent]
         * @property binaryExponent the binary exponent with which to encode the value, so that encoded value is [value * 2^binaryExponent]
         * @property offset the offset that will be applied to the value after all multiplications have completed
         */
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

        /**
         * Encoding as a Decimal value
         * @property supportedLengths the [Length] values that are supported. The smallest fitting length will be picked. Can only contain 32 and 64 bit lengths
         */
        data class Decimal(override val supportedLengths: Set<Length>) : NumericSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
                require((supportedLengths - setOf(Length.`32_BIT`, Length.`64_BIT`)).isEmpty()) { "Decimal only supports 32 and 64 bit encoding" }
            }
        }

        /**
         * Encoding as a MedFloat value
         * @property supportedLengths the [Length] values that are supported. The smallest fitting length will be picked. Can only contain 16 and 32 bit lengths
         */
        data class MedFloat(override val supportedLengths: Set<Length>) : NumericSettings() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
                require((supportedLengths - setOf(Length.`16_BIT`, Length.`32_BIT`)).isEmpty()) { "MedFloat only supports 16 and 32 bit encoding" }
            }
        }
    }

    /**
     * The encoding settings to apply when encoding a String or Char
     * @property encoding the [Encoding] with which to encode each char
     * @property endMarking the [StringEncodingSettings.EndMarking] to use when encoding the string. Ignored when only encoding a [Char].
     */
    data class StringSettings(val encoding: Encoding, val endMarking: StringEncodingSettings.EndMarking)

    /**
     * The encoding settings to apply when encoding a Collection (List or Map)
     * @property lengthMarking the [LengthMarking] to use when encoding the collection.
     * @property nullIfEmpty if the collection should be encoded as null if it is empty. When `true` an additional null flag will be added to the flags header.
     */
    data class CollectionSettings(val lengthMarking: LengthMarking, val nullIfEmpty: Boolean) {

        sealed class LengthMarking
        data class LengthPrefix(val endMarking: StringEncodingSettings.LengthPrefix) : LengthMarking()
        data object NullMarked : LengthMarking()
        data object Unmarked : LengthMarking()
        data class NumericLength(val supportedLengths: Set<Length>) : LengthMarking() {
            init {
                require(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
            }
        }
    }

    /**
     * The encoding settings to apply to a Structure
     * @property prefix The [ByteArrayHolder] containing any bytes to be added as a prefix whenever encoding this structure
     * @property postfix The [ByteArrayHolder] containing any bytes to be added as a postfix whenever encoding this structure
     * @property checksumAlgorithm the [CRC] algorithm to use to add a checksum right after the body (but before any postfix) of the structure.
     */
    data class StructureSettings(val prefix: ByteArrayHolder?, val postfix: ByteArrayHolder?, val checksumAlgorithm: CRC?)
}

@JvmInline
internal value class ByteArrayHolder(val array: ByteArray) {
    override fun toString(): String = array.toHexString(separator = " ")
}

/**
 * Thrown when a Flag is set that has already been reserved by a sibling node.
 */
class FlagIndexException(message: String) : SerializationException(message)

/**
 * Thrown when ByteOrder is changed in a sub-structure. Changing byte orders is only allowed for predictable types such as numeric types.
 */
class InvalidByteOrderException(message: String) : SerializationException(message)

internal object BluetoothBinaryDescriptorRegistry {

    private class DesiredFlagBitWidth(var width: Int) {
        fun raise(value: Int) {
            width += value
        }
    }

    // This is a pretty heavy method so we store it in a cache.
    private val cache = mutableMapOf<Pair<SerialDescriptor, SerializersModule>, BluetoothBinaryDescriptor>()

    internal fun bluetoothBinaryDescriptor(descriptor: SerialDescriptor, module: SerializersModule): BluetoothBinaryDescriptor = cache.getOrPut(descriptor to module) {
        getDescriptor(
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

    private fun getDescriptor(
        descriptor: SerialDescriptor,
        fieldName: String,
        fieldIndex: Int,
        fieldAnnotations: List<Annotation>,
        isNullable: Boolean,
        defaultBitIndex: Int,
        preferredByteOrder: ByteOrder,
        serializersModule: SerializersModule,
        reserveIndices: (Set<Int>) -> Unit,
    ): BluetoothBinaryDescriptor = if (descriptor.isInline) {
        // Inline methods should forward their descriptors until they find their actual (non-inline) root.
        val inlineDescriptor = descriptor.getElementDescriptor(0)
        val annotations = descriptor.annotations + fieldAnnotations + descriptor.getElementAnnotations(0)
        // Unsigned numbers in Kotlin are simply Inline wrappers around a primary type. However since we encode with flexible lengths, we should preserve this information.
        val actualAnnotations = when (descriptor.serialName) {
            "com.splendo.kaluga.base.bytes.MedFloat16" -> annotations + MedFloat() + Size(Length.`16_BIT`)
            "com.splendo.kaluga.base.bytes.MedFloat32" -> annotations + MedFloat() + Size(Length.`32_BIT`)
            "com.splendo.kaluga.base.bytes.Int24" -> annotations + Size(Length.`24_BIT`)
            "com.splendo.kaluga.base.bytes.UInt24" -> annotations + Unsigned() + Size(Length.`24_BIT`)
            "com.splendo.kaluga.base.bytes.Int40" -> annotations + Size(Length.`40_BIT`)
            "com.splendo.kaluga.base.bytes.UInt40" -> annotations + Unsigned() + Size(Length.`40_BIT`)
            "com.splendo.kaluga.base.bytes.Int48" -> annotations + Size(Length.`48_BIT`)
            "com.splendo.kaluga.base.bytes.UInt48" -> annotations + Unsigned() + Size(Length.`48_BIT`)
            "kotlin.UByte" -> annotations + Unsigned()
            "kotlin.UShort" -> annotations + Unsigned()
            "kotlin.UInt" -> annotations + Unsigned()
            "kotlin.ULong" -> annotations + Unsigned()
            else -> annotations
        }
        getDescriptor(
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
        val desiredFlagBitWidth = DesiredFlagBitWidth(0)

        // A property whose presence is derived from other flag bits owns no bit of its own.
        val presenceFlagIndices = annotations.filterIsInstance<PresentWhenAllSet>().firstOrNull()?.indices?.toList().orEmpty()

        // Nullable elements will have a flag bit. This is always the first bit of the flags for this object
        val isNullable = isNullable || (descriptor.kind in setOf(StructureKind.LIST, StructureKind.MAP) && annotations.filterIsInstance<NullIfEmpty>().isNotEmpty())
        if (isNullable && presenceFlagIndices.isEmpty()) {
            desiredFlagBitWidth.raise(1)
        }
        val customIndex = annotations.filterIsInstance<FlagIndex>().firstOrNull()?.index

        // Byte order cannot be changed when we have a structure of unknown length. We're not doing recursive search here so all structures should just have their order kept the same
        val byteOrder = annotations.filterIsInstance<com.splendo.kaluga.bluetooth.serialization.ByteOrder>().firstOrNull()?.order ?: preferredByteOrder
        if ((descriptor.kind is StructureKind || descriptor.kind is PrimitiveKind.STRING) && byteOrder != preferredByteOrder) {
            throw InvalidByteOrderException("Nested class ${descriptor.serialName} cannot have a byteOrder different than $preferredByteOrder")
        }
        if (descriptor.kind is PrimitiveKind.BOOLEAN && customIndex != null) {
            desiredFlagBitWidth.raise(1)
        }
        // An enum with an explicit FlagIndex is packed into the flags as its ordinal, across enough bits for all its cases.
        if (descriptor.kind is SerialKind.ENUM && customIndex != null) {
            desiredFlagBitWidth.raise(flagWidthForCases(descriptor.elementsCount))
        }
        val supportedLengths = lengths(annotations, descriptor)
        // A sub-byte numeric subfield of a bit field: an integer carrying @FlagIndex + @FlagWidth but no @Size has its
        // value packed straight into the flag region (like an enum ordinal), rather than as body bytes.
        val numericInFlagsBits = annotations.filterIsInstance<FlagWidth>().firstOrNull()?.bits?.takeIf {
            customIndex != null && !isNullable && annotations.filterIsInstance<Size>().isEmpty() &&
                descriptor.kind in setOf(PrimitiveKind.BYTE, PrimitiveKind.SHORT, PrimitiveKind.INT, PrimitiveKind.LONG)
        }
        val numericSettings = numericSettings(supportedLengths, descriptor, desiredFlagBitWidth, annotations, numericInFlagsBits)
        val stringSettings = stringSettings(descriptor, annotations, supportedLengths)
        val collectionSettings = collectionSettings(descriptor, annotations, desiredFlagBitWidth, supportedLengths)
        val enumMap = enumMap(descriptor, byteOrder)
        val polymorphicMap = polymorphicMap(descriptor, byteOrder, serializersModule)

        val blockSettings = blockSettings(annotations)
        val minWidth = annotations.filterIsInstance<FlagWidth>().firstOrNull()?.bits ?: 0
        val width = maxOf(desiredFlagBitWidth.width, minWidth)
        val bitIndex = if (width > 0) {
            customIndex ?: defaultBitIndex
        } else {
            -1
        }
        // Notify parent of flag indices to be used
        reserveIndices((0..<width).map { bitIndex + it }.toSet())

        BluetoothBinaryDescriptor(
            fieldName,
            fieldIndex,
            bitIndex,
            width,
            byteOrder,
            isNullable,
            numericSettings,
            stringSettings,
            collectionSettings,
            enumMap,
            polymorphicMap,
            blockSettings,
            when (descriptor.kind) {
                PolymorphicKind.OPEN -> openDescriptorChildren(serializersModule, descriptor, byteOrder, bitIndex)
                StructureKind.MAP -> mapDescriptorChildren(descriptor, annotations, byteOrder, serializersModule)
                StructureKind.LIST -> listDescriptorChildren(descriptor, annotations, byteOrder, serializersModule)
                else -> descriptorChildren(descriptor, byteOrder, serializersModule, bitIndex)
            },
            presenceFlagIndices,
        )
    }

    private fun descriptorChildren(descriptor: SerialDescriptor, byteOrder: ByteOrder, serializersModule: SerializersModule, bitIndex: Int): List<BluetoothBinaryDescriptor> {
        var nextBit = 0
        val reservedSubIndices = mutableSetOf<Int>()
        // A regular structure has as many descriptors as it has children.
        // All children share the same header flag bytes.
        return (0 until descriptor.elementsCount).map { i ->
            val elementName = descriptor.getElementName(i)
            val elementAnnotations = descriptor.getElementAnnotations(i)
            val elementDescriptor = descriptor.getElementDescriptor(i)
            getDescriptor(
                elementDescriptor,
                elementName,
                i,
                elementAnnotations,
                elementDescriptor.isNullable,
                nextBit,
                byteOrder,
                serializersModule,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flag at index $bitIndex cannot be used for $elementName. Is already reserved")
                }
                reservedSubIndices += flagIndicesToUse
                while (nextBit in reservedSubIndices) {
                    nextBit++
                }
            }
        }
    }

    private fun listDescriptorChildren(
        descriptor: SerialDescriptor,
        annotations: List<Annotation>,
        byteOrder: ByteOrder,
        serializersModule: SerializersModule,
    ): List<BluetoothBinaryDescriptor> {
        val itemDescriptor = descriptor.getElementDescriptor(0)
        val reservedSubIndices = mutableSetOf<Int>()
        // For list elements, there is only one child descriptor, that does not reuse its parents flag header.
        return listOf(
            getDescriptor(
                itemDescriptor,
                descriptor.getElementName(0),
                0,
                annotations.itemAnnotations(),
                itemDescriptor.isNullable,
                0,
                byteOrder,
                serializersModule,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flags at index $flagIndicesToUse cannot be used for ${itemDescriptor.serialName}. Is already reserved")
                }
                reservedSubIndices += flagIndicesToUse
            },
        )
    }

    private fun mapDescriptorChildren(
        descriptor: SerialDescriptor,
        annotations: List<Annotation>,
        byteOrder: ByteOrder,
        serializersModule: SerializersModule,
    ): List<BluetoothBinaryDescriptor> {
        val keyDescriptor = descriptor.getElementDescriptor(0)
        val valueDescriptor = descriptor.getElementDescriptor(1)

        val reservedKeySubIndices = mutableSetOf<Int>()
        val reservedValueSubIndices = mutableSetOf<Int>()
        // For list elements, there is only two child descriptors for key and value elements, that all have their unique flag headers..
        return listOf(
            getDescriptor(
                keyDescriptor,
                descriptor.getElementName(0),
                0,
                annotations.keyAnnotations(),
                keyDescriptor.isNullable,
                0,
                byteOrder,
                serializersModule,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedKeySubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flags at index $flagIndicesToUse cannot be used for ${keyDescriptor.serialName}. Is already reserved")
                }
                reservedKeySubIndices += flagIndicesToUse
            },
            getDescriptor(
                valueDescriptor,
                descriptor.getElementName(1),
                1,
                annotations.valueAnnotations(),
                valueDescriptor.isNullable,
                0,
                byteOrder,
                serializersModule,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedValueSubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flags at index $flagIndicesToUse cannot be used for ${valueDescriptor.serialName}. Is already reserved")
                }
                reservedValueSubIndices += flagIndicesToUse
            },
        )
    }

    private fun openDescriptorChildren(serializersModule: SerializersModule, descriptor: SerialDescriptor, byteOrder: ByteOrder, bitIndex: Int): List<BluetoothBinaryDescriptor> =
        serializersModule.getPolymorphicDescriptors(descriptor).map { optionDescriptor ->
            // For Open Polymorphic classes, all its declared options in serializersModule need to be considered
            val reservedSubIndices = mutableSetOf<Int>()
            getDescriptor(
                optionDescriptor,
                optionDescriptor.serialName,
                0,
                optionDescriptor.annotations,
                optionDescriptor.isNullable,
                0,
                byteOrder,
                serializersModule,
            ) { flagIndicesToUse ->
                if (flagIndicesToUse.intersect(reservedSubIndices).isNotEmpty()) {
                    throw FlagIndexException("Flag at index $bitIndex cannot be used for ${optionDescriptor.serialName}. Is already reserved")
                }
                reservedSubIndices += flagIndicesToUse
            }
        }

    private fun lengths(annotations: List<Annotation>, descriptor: SerialDescriptor): Set<Length> = annotations.filterIsInstance<Size>().map { it.length }.toSet().ifEmpty {
        when (descriptor.kind) {
            PrimitiveKind.BYTE -> if (annotations.filterIsInstance<MedFloat>().isNotEmpty()) {
                setOf(Length.`16_BIT`)
            } else {
                setOf(Length.`8_BIT`)
            }

            PrimitiveKind.SHORT -> setOf(Length.`16_BIT`)

            PrimitiveKind.INT -> setOf(Length.`32_BIT`)

            PrimitiveKind.LONG -> if (annotations.filterIsInstance<MedFloat>().isNotEmpty()) {
                setOf(Length.`32_BIT`)
            } else {
                setOf(Length.`64_BIT`)
            }

            PrimitiveKind.FLOAT -> when {
                annotations.filterIsInstance<MedFloat>().isNotEmpty() -> setOf(Length.`16_BIT`)
                annotations.filterIsInstance<Scalar>().isNotEmpty() -> setOf(Length.`16_BIT`)
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

    // Number of flag bits needed to store which of [size] options was chosen, i.e. ceil(log2(size)).
    // Computed with integer arithmetic to avoid floating-point rounding at the boundaries:
    // the highest representable index is size - 1, and 32 - countLeadingZeroBits() is its bit width.
    private fun Set<Length>.sizingWidth() = flagWidthForCases(size)

    // Number of flag bits needed to store one of [cases] options, i.e. ceil(log2(cases)).
    private fun flagWidthForCases(cases: Int) = when {
        cases <= 1 -> 0
        else -> 32 - (cases - 1).countLeadingZeroBits()
    }

    private fun numericSettings(
        supportedLengths: Set<Length>,
        descriptor: SerialDescriptor,
        desiredFlagBitWidth: DesiredFlagBitWidth,
        annotations: List<Annotation>,
        inFlagsBits: Int?,
    ): BluetoothBinaryDescriptor.NumericSettings? = when (descriptor.kind) {
        PrimitiveKind.INT,
        PrimitiveKind.BYTE,
        PrimitiveKind.SHORT,
        PrimitiveKind.LONG,
        PrimitiveKind.DOUBLE,
        PrimitiveKind.FLOAT,
        -> {
            desiredFlagBitWidth.raise(supportedLengths.sizingWidth())
            if (annotations.filterIsInstance<MedFloat>().isNotEmpty()) {
                BluetoothBinaryDescriptor.NumericSettings.MedFloat(supportedLengths)
            } else if (annotations.filterIsInstance<Scalar>().isNotEmpty()) {
                val scalar = annotations.filterIsInstance<Scalar>().first()
                val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
                BluetoothBinaryDescriptor.NumericSettings.Scalar(supportedLengths, isSigned, scalar.multiplier, scalar.decimalExponent, scalar.binaryExponent, scalar.offset)
            } else if (descriptor.kind == PrimitiveKind.DOUBLE || descriptor.kind == PrimitiveKind.FLOAT) {
                BluetoothBinaryDescriptor.NumericSettings.Decimal(supportedLengths)
            } else {
                val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
                if (inFlagsBits != null) {
                    BluetoothBinaryDescriptor.NumericSettings.Natural(emptySet(), isSigned, inFlagsBits = inFlagsBits)
                } else {
                    BluetoothBinaryDescriptor.NumericSettings.Natural(supportedLengths, isSigned)
                }
            }
        }

        else -> null
    }

    private fun stringSettings(descriptor: SerialDescriptor, annotations: List<Annotation>, supportedLengths: Set<Length>): BluetoothBinaryDescriptor.StringSettings? {
        val stringSettings = when (descriptor.kind) {
            PrimitiveKind.STRING -> {
                val encoding = annotations.filterIsInstance<Encoded>().firstOrNull()?.encoding ?: Encoding.UTF_8
                when {
                    annotations.filterIsInstance<NullTerminated>().isNotEmpty() -> BluetoothBinaryDescriptor.StringSettings(encoding, StringEncodingSettings.NullTerminated)

                    annotations.filterIsInstance<LengthPrefix>().isNotEmpty() -> {
                        val lengthPrefix = annotations.filterIsInstance<LengthPrefix>().first()
                        BluetoothBinaryDescriptor.StringSettings(
                            encoding,
                            lengthPrefix.asLengthPrefix(),
                        )
                    }

                    supportedLengths.size == 1 -> {
                        BluetoothBinaryDescriptor.StringSettings(encoding, StringEncodingSettings.FixedLength(supportedLengths.first().bytes))
                    }

                    annotations.filterIsInstance<Unsized>().isNotEmpty() -> BluetoothBinaryDescriptor.StringSettings(encoding, StringEncodingSettings.NoMarking)

                    else -> BluetoothBinaryDescriptor.StringSettings(encoding, StringEncodingSettings.LengthPrefix.ByteLength)
                }
            }

            PrimitiveKind.CHAR -> BluetoothBinaryDescriptor.StringSettings(
                annotations.filterIsInstance<Encoded>().firstOrNull()?.encoding ?: Encoding.UTF_8,
                StringEncodingSettings.NoMarking,
            )

            else -> null
        }
        return stringSettings
    }

    private fun collectionSettings(
        descriptor: SerialDescriptor,
        annotations: List<Annotation>,
        desiredFlagBitWidth: DesiredFlagBitWidth,
        supportedLengths: Set<Length>,
    ): BluetoothBinaryDescriptor.CollectionSettings? {
        val collectionSettings = when (descriptor.kind) {
            is StructureKind.LIST,
            is StructureKind.MAP,
            -> {
                val lengthMarking = when {
                    annotations.filterIsInstance<NullTerminated>().isNotEmpty() -> BluetoothBinaryDescriptor.CollectionSettings.NullMarked

                    annotations.filterIsInstance<LengthPrefix>().isNotEmpty() -> {
                        val lengthPrefix = annotations.filterIsInstance<LengthPrefix>().first()
                        BluetoothBinaryDescriptor.CollectionSettings.LengthPrefix(
                            lengthPrefix.asLengthPrefix(),
                        )
                    }

                    annotations.filterIsInstance<Unsized>().isNotEmpty() -> BluetoothBinaryDescriptor.CollectionSettings.Unmarked

                    else -> {
                        desiredFlagBitWidth.raise(supportedLengths.sizingWidth())
                        BluetoothBinaryDescriptor.CollectionSettings.NumericLength(supportedLengths)
                    }
                }
                BluetoothBinaryDescriptor.CollectionSettings(
                    lengthMarking,
                    annotations.filterIsInstance<NullIfEmpty>().isNotEmpty(),
                )
            }

            else -> null
        }
        return collectionSettings
    }

    private fun enumMap(descriptor: SerialDescriptor, byteOrder: ByteOrder): Map<Int, ByteArrayHolder> {
        val enumMap = if (descriptor.kind is SerialKind.ENUM) {
            (0 until descriptor.elementsCount).associateWith { index ->
                ByteArrayHolder(
                    descriptor.getElementAnnotations(index).filterIsInstance<SerializedByteValue>().firstOrNull()?.let {
                        byteArrayOf(it.value)
                    } ?: descriptor.getElementName(index).toByteArray(StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8), byteOrder),
                )
            }
        } else {
            emptyMap()
        }
        return enumMap
    }
    private fun polymorphicMap(descriptor: SerialDescriptor, byteOrder: ByteOrder, serializersModule: SerializersModule): Map<String, ByteArrayHolder> {
        val polymorphicMap = when (descriptor.kind) {
            is PolymorphicKind.SEALED -> {
                val sealedDescriptor = descriptor.getElementDescriptor(1)
                (0..<sealedDescriptor.elementsCount).associate { index ->
                    val optionDescriptor = sealedDescriptor.getElementDescriptor(index)
                    val serialIdentifier = optionDescriptor.annotations.filterIsInstance<SerializedByteValue>().firstOrNull()?.let {
                        byteArrayOf(it.value)
                    } ?: optionDescriptor.serialName.toByteArray(StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8), byteOrder)
                    optionDescriptor.serialName to ByteArrayHolder(serialIdentifier)
                }
            }

            is PolymorphicKind.OPEN -> {
                val polymorphicDescriptors = serializersModule.getPolymorphicDescriptors(descriptor)
                polymorphicDescriptors.associate { optionDescriptor ->
                    val serialIdentifier = optionDescriptor.annotations.filterIsInstance<SerializedByteValue>().firstOrNull()?.let {
                        byteArrayOf(it.value)
                    } ?: optionDescriptor.serialName.toByteArray(StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.UTF_8), byteOrder)
                    optionDescriptor.serialName to ByteArrayHolder(serialIdentifier)
                }
            }

            else -> emptyMap()
        }
        return polymorphicMap
    }

    private fun blockSettings(annotations: List<Annotation>): BluetoothBinaryDescriptor.StructureSettings {
        val structureSettings = BluetoothBinaryDescriptor.StructureSettings(
            annotations.filterIsInstance<Prefix>().firstOrNull()?.value?.let { ByteArrayHolder(it) },
            annotations.filterIsInstance<Postfix>().firstOrNull()?.value?.let { ByteArrayHolder(it) },
            annotations.filterIsInstance<Checksum>().firstOrNull()?.let { checksum ->
                CRC(checksum.width, checksum.polynomial, checksum.init, checksum.xorOut, checksum.reflectIn, checksum.reflectOut)
            },
        )
        return structureSettings
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
            is ItemSize -> Size(annotation.size)
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
            is KeySize -> Size(annotation.size)
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
            is ValueSize -> Size(annotation.size)
            else -> null
        }
    }
}
