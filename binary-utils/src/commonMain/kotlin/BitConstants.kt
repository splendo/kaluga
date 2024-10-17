/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

typealias BitIndex = Int
typealias BitIndexRange = IntRange
typealias OctetIndex = Int
typealias OctetIndexRange = IntRange

interface BitConstants {

    companion object {
        /** Number of Bits in Octet */
        const val BITS_IN_OCTET = UByte.SIZE_BITS
    }

    /**
     * Number of Bits
     * The number of Bits available in this BitGroup.
     * This value must be greater then zero.
     */
    val numberOfBits: Int

    /**
     * Number of Octets
     * The number of Octets available in this BitGroup.
     * This value must be greater then zero.
     */
    val numberOfOctets get() = numberOfBits / UByte.SIZE_BITS

    /**
     * Mask with all [numberOfBits] Bits set
     */
    val maxMask get() = 0uL.inv() shr (ULong.SIZE_BITS - numberOfBits)

    /**
     * Minimum Bit Index.
     * Bit Index is used to point to a specific Bit within a group.
     * Bit Index is always a positive number.
     *
     * Note: Bit Index is not dependent on the Endianness and should not be
     * confused with Bit Number(ing) that is.
     *
     * Bit Index correlates to the positional notation of the value in
     * base 2 numeral system (binary numeral system). This translates to the
     * Least Significant Bit at index `0`.
     * For more information see [BitNumberingScheme]
     *
     * @see [BitNumberingScheme]
     */
    private val minBitIndex get() = 0

    /**
     * Maximum Bit Index.
     * Bit Index is used to point to a specific Bit within a group.
     * Bit Index is always less then the total number of bits in the group.
     *
     * Bit Index correlates to the positional notation of the value in
     * base 2 numeral system (binary numeral system). This translates to the
     * Least Significant Bit at index `0`.
     * For more information see [BitNumberingScheme]
     *
     * @see [BitNumberingScheme]
     */
    val maxBitIndex get() = numberOfBits - 1

    /**
     * Valid Bit Index range
     * The Range that includes both [minBitIndex] and [maxBitIndex]
     */
    val validBitIndexRange get() = minBitIndex..maxBitIndex

    /**
     * Minimum Octet Index.
     * Octet Index is used to point to a specific Octet within a group.
     * Octet Index is always a positive number.
     */
    val minOctetIndex get() = 0

    /**
     * Maximum Octet Index.
     * Octet Index is used to point to a specific Octet within a group.
     * Octet Index is always less then the total number of Octet in the group.
     */
    val maxOctetIndex get() = numberOfOctets - 1

    /**
     * Valid Index range
     * The Range that includes both [minOctetIndex] and [maxOctetIndex]
     */
    val validOctetIndexRange get() = minOctetIndex..maxOctetIndex

    /**
     * Returns `true` if given [index] inside [validBitIndexRange] range
     * otherwise returns `false`
     */
    fun isBitIndexValid(index: BitIndex) = validBitIndexRange.contains(index)

    /**
     * Returns `true` if given [indexRange] is in ascending order (last >= first)
     * and inside [validBitIndexRange] range otherwise returns `false`
     */
    fun isBitIndexRangeValid(indexRange: BitIndexRange) = (indexRange.last >= indexRange.first) &&
        isBitIndexValid(indexRange.first) &&
        isBitIndexValid(indexRange.last)

    /**
     * Returns `true` if given [index] inside [validOctetIndexRange] range
     * otherwise returns `false`
     */
    fun isOctetIndexValid(index: OctetIndex) = validOctetIndexRange.contains(index)

    /**
     * Returns `true` if given [indexRange] is in ascending order (last >= first)
     * and inside [validOctetIndexRange] range otherwise returns `false`
     */
    fun isOctetIndexRangeValid(indexRange: OctetIndexRange) = (indexRange.last >= indexRange.first) &&
        isOctetIndexValid(indexRange.first) &&
        isOctetIndexValid(indexRange.last)
}
