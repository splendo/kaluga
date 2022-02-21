/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import BitConstants.Companion.BITS_IN_OCTET

internal fun UByte.toGroupOfBits(): GroupOf8Bits = GroupOf8Bits(this)
internal fun Byte.toGroupOfBits(): GroupOf8Bits = GroupOf8Bits(this)

/**
 * Bit Group
 *
 * This is base class for Bit Groups of various length.
 * The Bit length of a group must be a multiple of the number of Bits
 * in a single Octet (8)
 *
 * @param D Unsigned integer type one of: UByte, UShort, UInt, ULong
 * @param backingData property holding actual binary data for this group
 * @param octetEndianness describes the [Endianness] of the binary data passed
 */
internal sealed class BitGroup<D>(
    protected var backingData: D,
    /**
     * Group Octet Endianness
     * For multi octet groups this is the [Endianness] of a data passed to the group.
     * For a single octet group this is always equal to native Endianness.
     *
     * @see reverseOctets
     * @see Endianness
     */
    val octetEndianness: Endianness,
) : BitConstants {

    companion object {
        private const val TRUE: UByte = 1u
    }

    sealed class Exception : kotlin.Exception() {
        data class IndexOutOfBounds(val index: Int, val valid: IntRange) : Exception()
        data class IndexRangeOutOfBounds(val range: IntRange, val valid: IntRange) : Exception()
        data class CountOutOfBounds(val count: Int) : Exception()
    }

    override fun isBitIndexValid(index: Int) =
        if (super.isBitIndexValid(index)) true
        else throw Exception.IndexOutOfBounds(index, validBitIndexRange)

    override fun isBitIndexRangeValid(indexRange: IntRange) =
        if (super.isBitIndexRangeValid(indexRange)) true
        else throw Exception.IndexRangeOutOfBounds(indexRange, validBitIndexRange)

    override fun isOctetIndexValid(index: Int) =
        if (super.isOctetIndexValid(index)) true
        else throw Exception.IndexOutOfBounds(index, validOctetIndexRange)

    /**
     * Returns a Boolean value for a Bit at given Bit Index.
     *
     * @param index the positional counting from LSB at 0
     *
     * @see validBitIndexRange
     * @see BitNumberingScheme
     * @see getBitAt
     */
    operator fun get(index: BitIndex): Boolean {
        isBitIndexValid(index)
        val value = getBitAt(index)
        return value == TRUE
    }

    /**
     * Returns all Bits from a given Bit Index.
     * @see getBitsInRange
     */
    operator fun get(indexRange: BitIndexRange) = getBitsInRange(indexRange)

    /**
     * Sets a Bit at given Bit Index to the Boolean value.
     *
     * @param index the positional counting from LSB at 0
     *
     * @see validBitIndexRange
     * @see BitNumberingScheme
     * @see setBitAt
     */
    operator fun set(index: BitIndex, value: Boolean) {
        isBitIndexValid(index)
        setBitAt(index, value.toUByte())
    }

    /**
     * Sets all the Bits in a given Bit Index range.
     * @see setBitsInRange
     */
    operator fun set(indexRange: BitIndexRange, value: D) = setBitsInRange(indexRange, value)

    /**
     * Flips Boolean value for a Bit at given index.
     * This method uses index that correlates to the positional notation of the value in
     * base 2 numeral system (binary numeral system).
     *
     * This translates to the Least Significant Bit at index `0`.
     *
     * @param index the positional counting from LSB at 0
     *
     * @return the value of that bit
     *
     * @see BitNumberingScheme
     */
    fun flip(index: BitIndex) = set(index, !get(index))

    /**
     * Allows to easily access MSB
     *
     * @see BinarySignificance
     */
    var msb: Boolean
        get() = get(validBitIndexRange.last)
        set(value) = set(validBitIndexRange.last, value)

    /**
     * Allows to easily access LSB
     *
     * @see BinarySignificance
     */
    var lsb: Boolean
        get() = get(validBitIndexRange.first)
        set(value) = set(validBitIndexRange.first, value)

    /**
     * Allows to easily access MSO
     *
     * @see BinarySignificance
     */
    var mso: UByte
        get() = when (octetEndianness) {
            Endianness.MOST_SIGNIFICANT_FIRST -> getOctetAt(validOctetIndexRange.first)
            Endianness.LEAST_SIGNIFICANT_FIRST -> getOctetAt(validOctetIndexRange.last)
        }
        set(value) = when (octetEndianness) {
            Endianness.MOST_SIGNIFICANT_FIRST -> setOctetAt(validOctetIndexRange.first, value)
            Endianness.LEAST_SIGNIFICANT_FIRST -> setOctetAt(validOctetIndexRange.last, value)
        }

    /**
     * Allows to easily access LSO
     *
     * @see BinarySignificance
     */
    var lso: UByte
        get() = when (octetEndianness) {
            Endianness.MOST_SIGNIFICANT_FIRST -> getOctetAt(validOctetIndexRange.last)
            Endianness.LEAST_SIGNIFICANT_FIRST -> getOctetAt(validOctetIndexRange.first)
        }
        set(value) = when (octetEndianness) {
            Endianness.MOST_SIGNIFICANT_FIRST -> setOctetAt(validOctetIndexRange.last, value)
            Endianness.LEAST_SIGNIFICANT_FIRST -> setOctetAt(validOctetIndexRange.first, value)
        }

    /**
     * Reads a Bit at given Bit Index and returns it as [UByte] containing the value in LSB
     * That means if the Bit at Bit Index was set to `true` this will return
     * `0x01` otherwise `0x00`
     *
     * @param index must be within valid range
     *
     * @see validBitIndexRange
     */
    private fun getBitAt(index: BitIndex): UByte {
        isBitIndexValid(index)
        val range = index..index
        return convertToOctet(getBitsInRange(range))
    }

    /**
     * Sets a Bit at given Bit Index to the value of LSB in a given [UByte].
     * That means that the Bit at Bit Index will be set to the same value as the lowest Bit of UByte.
     *
     * @param index must be within valid range
     *
     * @see validBitIndexRange
     */
    private fun setBitAt(index: BitIndex, value: UByte) {
        isBitIndexValid(index)
        val range = index..index
        val input = convertFromOctet(value)
        setBitsInRange(range, input).also {
            backingData = it
        }
    }

    /**
     * Reads an Octet at given Octet Index and returns it as [UByte].
     *
     * @param index must be within [validOctetIndexRange]
     *
     * Note: The result **is dependent** on the [octetEndianness].
     * For `MOST_SIGNIFICANT_FIRST` the `0` index point to the Most Significant Octet
     * For `LEAST_SIGNIFICANT_FIRST` the `0` index point to the Least Significant Octet
     */
    private fun getOctetAt(index: OctetIndex): UByte {
        isOctetIndexValid(index)
        val range = bitIndexRangeFor(index)
        return convertToOctet(getBitsInRange(range))
    }

    /**
     * Sets an Octet at given Octet Index to the value of a given [UByte].
     *
     * @param index must be within [validOctetIndexRange]
     * @param value an UByte containing value in the LSB
     *
     * Note: The result **is dependent** on the [octetEndianness].
     * For `MOST_SIGNIFICANT_FIRST` the `0` index point to the Most Significant Octet
     * For `LEAST_SIGNIFICANT_FIRST` the `0` index point to the Least Significant Octet
     */
    private fun setOctetAt(index: OctetIndex, value: UByte) {
        isOctetIndexValid(index)
        val range = bitIndexRangeFor(index)
        val input = convertFromOctet(value)
        setBitsInRange(range, input).also {
            backingData = it
        }
    }

    private fun bitIndexRangeFor(octetIndex: OctetIndex): IntRange {
        return when (octetEndianness) {
            Endianness.LEAST_SIGNIFICANT_FIRST -> littleEndianIndexRangeFor(octetIndex)
            Endianness.MOST_SIGNIFICANT_FIRST -> bigEndianIndexRangeFor(octetIndex)
        }
    }

    private fun littleEndianIndexRangeFor(octetIndex: OctetIndex): IntRange {
        val octetStartIndex = octetIndex * BITS_IN_OCTET
        val octetEndIndex = octetStartIndex + BITS_IN_OCTET - 1
        return octetStartIndex..octetEndIndex
    }

    private fun bigEndianIndexRangeFor(octetIndex: OctetIndex): IntRange {
        val reversedIndex = numberOfOctets - octetIndex
        val octetStartIndex = (reversedIndex - 1) * BITS_IN_OCTET
        val octetEndIndex = reversedIndex * BITS_IN_OCTET - 1
        return octetStartIndex..octetEndIndex
    }

    /**
     * Reads all Bits from a given Bit Index range.
     * Returns same type as [backingData] containing the value in a lower part.
     *
     * That means the Bit at the start of the Bit Index range will always be
     * at the LSB (Bit Index 0) in the returned type.
     *
     * @param indexRange must be within valid range
     *
     * @see validBitIndexRange
     */
    fun getBitsInRange(indexRange: BitIndexRange): D = readBits(indexRange, backingData)

    /**
     * Sets all the Bits in a given Bit Index range.
     * The input has to be in the same type as [backingData] and
     * must containing the value in a lower part
     *
     * That means the value of the Bit at LSB (Bit Index 0) of input value will
     * be written to the Bit at the start of the Bit Index range.
     *
     * @param indexRange must be within valid range
     * @param value type holding values for Bits to be set starting from the LSB
     *
     * @see validBitIndexRange
     */
    fun setBitsInRange(indexRange: BitIndexRange, value: D) =
        writeBits(indexRange, value, backingData).also {
            backingData = it
        }

    /**
     * Read Bits
     * This function reads Bits from Bit Indexes defined by the [indexRange].
     * Both the start and the end Bit Index are included in the result.
     */
    private fun readBits(indexRange: BitIndexRange, fromSource: D): D {
        isBitIndexRangeValid(indexRange)
        // use inverted region mask
        val mask1 = negateBits(regionMask(indexRange))
        // shift it right, so we have leading ones
        val mask2 = shiftBitsRight(mask1, indexRange.first)
        // we need a copy of the input data
        // we need to shift input so that the lowest Bit to read is at LSB
        val shiftedData = shiftBitsRight(fromSource, indexRange.first)
        // we use the mask to clear Bits we don't need
        return bitwiseAnd(mask2, shiftedData)
    }

    /**
     * Returns region negative mask for given [indexRange]
     *
     * Example:
     *   1..2 will return mask 0b1111_1001
     *   Two bits cleared and shifted left to range start value
     */
    internal fun regionMask(indexRange: BitIndexRange): D {
        // We start with a "full mask" with all Bits set to `1`s
        val fullMask = getFullMask()
        val numberOfBitsToSet = indexRange.last - indexRange.first + 1
        // Shift right mask, so we have leading zeros and desired number of set bits
        val mask1 = shiftBitsRight(fullMask, numberOfBits - numberOfBitsToSet)
        // Shift left mask, so we have set bits in given index range only
        val mask2 = shiftBitsLeft(mask1, indexRange.first)
        // Invert mask, so we can clear bits later inside range
        return negateBits(mask2)
    }

    /**
     * Shifts and clears overflown bits for given [indexRange]
     *
     * Example:
     *   For value 0b11 and index 2..3 will return 0b0000_1100
     */
    internal fun shiftInput(value: D, indexRange: BitIndexRange): D {
        // Clear overflown bits first
        val fullMask = getFullMask()
        val bitsToSet = indexRange.last - indexRange.first + 1
        // Mask to clear extra bits
        val mask = shiftBitsRight(fullMask, numberOfBits - bitsToSet)
        val valueInRange = bitwiseAnd(value, mask)
        // Shift bits left to the beginning of the index range
        return shiftBitsLeft(valueInRange, indexRange.first)
    }

    private fun writeBits(indexRange: BitIndexRange, withValue: D, toDestination: D): D {
        isBitIndexRangeValid(indexRange)
        val mask = regionMask(indexRange)
        val clearedDestination = bitwiseAnd(toDestination, mask)
        val shiftedInput = shiftInput(withValue, indexRange)
        return bitwiseOR(shiftedInput, clearedDestination)
    }

    /**
     * Returns all the Octets in this group as a ByteArray having elements
     * in the Octet Indexing order.
     */
    fun toByteArray() = ByteArray(numberOfOctets) {
        getOctetAt(it).toByte()
    }

    /**
     * Returns a new [BitGroup] of the same type with
     * Bit stream having octets in the reversed order.
     */
    abstract fun reversedOctetOrder(): BitGroup<D>

    /** Returns a new [BitGroup] copy */
    abstract fun copy(): BitGroup<D>

    /** Swaps octet at [index] with octet at [withIndex] */
    private fun swapOctetAt(index: OctetIndex, withIndex: OctetIndex) {
        isOctetIndexValid(index)
        isOctetIndexValid(withIndex)
        if (index == withIndex) return
        val tmp = getOctetAt(index)
        setOctetAt(index, getOctetAt(withIndex))
        setOctetAt(withIndex, tmp)
    }

    /** Reverses octets in place */
    fun reverseOctets() {
        val mid = (validOctetIndexRange.last - validOctetIndexRange.first) / 2
        for (first in (validOctetIndexRange.first..(validOctetIndexRange.first + mid))) {
            swapOctetAt(first, validOctetIndexRange.last - first)
        }
    }

    /**
     * Returns a new BitGroup of the same type holding Bits representing a two's complement of
     * current binary data.
     *
     * Two's complement is a mathematical operation on binary numbers,
     * used in computing as a method of signed number representation.
     *
     * The two's complement of an N-bit number is defined as its complement with respect to 2^N;
     * the sum of a number and its two's complement is 2^N.
     * For instance, for the three-bit number `0b011`, the two's complement is `0b101`,
     * because `0b011` + `0b101` = `0b1000` = 8 which is equal to 2^3.
     * The two's complement is calculated by inverting the bits and adding one.
     */
    abstract fun twosComplement(): BitGroup<D>

    protected fun calculateTwosComplementOf(value: D): D {
        val valueWithInvertedBits = negateBits(value)
        val complementValue = increment(valueWithInvertedBits)
        return complementValue
    }

    /**
     * Shift Bits left, towards the MSB
     *
     * Returns a new value with Bits Shifted left by the bitCount number of bits,
     * filling the rightmost Bits with zeros.
     *
     * Note that shift distance will be coerced to be within [validBitIndexRange]
     * @param value holding Bits to be shifted
     * @param bitCount how many positions to shift
     */
    private fun shiftBitsLeft(value: D, bitCount: Int): D =
        shiftLeft(value, bitCount, numberOfBits)

    private fun shiftLeft(value: D, bitCount: Int, maxBits: Int): D = when {
        bitCount == 0 -> value
        (1..maxBits.dec()).contains(bitCount) -> bitwiseShiftToLeft(value, bitCount)
        bitCount >= maxBits -> convertFromOctet(0u)
        else -> throw Exception.CountOutOfBounds(bitCount)
    }

    /**
     * Shift Bits right, towards the LSB
     *
     * Returns a new value with Bits Shifted right by the bitCount number of bits,
     * filling the leftmost Bits with zeros.
     *
     * Note that shift distance will be coerced to be within [validBitIndexRange]
     * @param value holding Bits to be shifted
     * @param bitCount how many positions to shift
     */
    fun shiftBitsRight(value: D, bitCount: Int): D =
        shiftRight(value, bitCount, numberOfBits)

    private fun shiftRight(value: D, bitCount: Int, maxBits: Int): D = when {
        bitCount == 0 -> value
        (1..maxBits.dec()).contains(bitCount) -> bitwiseShiftToRight(value, bitCount)
        bitCount >= maxBits -> convertFromOctet(0u)
        else -> throw Exception.CountOutOfBounds(bitCount)
    }

    /**
     * Performs a bitwise shift to the left operation.
     * Returns a new value that is a result of this operation.
     *
     * Note: Native Kotlin functions take only 0..(numberOfBits - 1) range
     */
    abstract fun bitwiseShiftToLeft(value: D, bitCount: Int): D

    /**
     * Performs a bitwise shift to the right operation.
     * Returns a new value that is a result of this operation.
     *
     * Note: Native Kotlin functions take only 0..(numberOfBits - 1) range
     */
    abstract fun bitwiseShiftToRight(value: D, bitCount: Int): D

    /**
     * Performs a bitwise AND operation between the two values.
     * Returns a new value that is a result of this operation.
     */
    abstract fun bitwiseAnd(value1: D, value2: D): D

    /**
     * Performs a bitwise OR operation between the two values.
     * Returns a new value that is a result of this operation.
     */
    abstract fun bitwiseOR(value1: D, value2: D): D

    /**
     * Performs a bitwise Negation of the Bits in the value.
     * Returns a new value that is a result of this operation.
     */
    abstract fun negateBits(value: D): D

    /**
     * Returns value incremented by one.
     */
    abstract fun increment(value: D): D

    /**
     * Returns the value having all its Bits set to true.
     *
     * Mask with have a width of a [numberOfBits]
     */
    abstract fun getFullMask(): D

    abstract fun convertFromOctet(value: UByte): D
    abstract fun convertToOctet(value: D): UByte

    /**
     * Returns the string with hex representation of this Group
     */
    abstract val asHex: String

    /**
     * Returns the string with binary representation of this Group
     */
    abstract val asBin: String

    private fun Boolean.toUByte(): UByte = if (this) 1u else 0u
}
