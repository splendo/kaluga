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
import co.touchlab.stately.concurrency.value
import kotlin.math.ceil
import kotlin.math.min

/**
 * Binary Reader treats inputData as a continuous stream of Bits.
 * It allows to sequentially read group of Bits from it.
 *
 * Internal buffer is assumed to hold data in Big Endian order where the Most Significant Octet
 * is at index 0 of the array.
 *
 * Since Binary Reader treats data as a stream of Bits the concept of Octet (Byte) Endianness
 * does not apply to the Binary Reader and any needed Endianness transformations must be done
 * on the values returned from the Reader.
 *
 * The start, end and direction of reading of Bit stream is controlled by the parameters
 * passed in the constructor.
 *
 * Bit Numbering Scheme controls where the Bit Index 0 is and in which direction it increments:
 * - For the `MSB_0` Index 0 is at the Most Significant Bit of the Most Significant Octet.
 * That will be the highest bit of the octet located at the index 0
 * of the inputData array (first element of the array).
 * Index will be increasing towards the data located at the last element of the inputData array.
 *
 *  - For the `LSB_0` Index 0 is at the Least Significant Bit of the Least Significant Octet.
 * That will be the lowest bit of the octet located at the last index
 * of the inputData array (last element of the array).
 * Index will be increasing towards the data located at the first element of the inputData array.
 *
 * Binary Reader returns sequential groups of Bits of various lengths starting from BitIndex `0`.
 *
 * IMPORTANT: Binary reader **Does not** change the significance of read Bits.
 * What this means:
 * If you have a Bit Stream containing:
 * -------- 0101----
 * You will get same value `0b0101` when
 * Reading 4 bits starting from Index 4 in LSB_0
 * Reading 4 bits starting from Index 8 in MSB_0
 *
 * The position of Index 0 can be shifted using the base offset value in the direction
 * determined by the numbering scheme.
 *
 * By default the Reader will continue reading Bits from the input Data until it read them all.
 * Padding parameter can be used to control where Bit stream ends.
 * Bit stream will stop the number of bits from the end of input data equal to padding value.
 *
 * @param inputData a ByteArray holding binary inputData
 * @param baseOffset number of initial Bits to be skipped (must be positive number)
 * @param padding number of final Bits to be ignored (must be positive number)
 * @param numberingScheme controls the direction of Bit Index
 *
 * @see BitNumberingScheme
 */
internal class GATTBinaryReader(
    private val inputData: ByteArray,
    val numberingScheme: Endianness,
    private val baseOffset: Int = 0,
    private val padding: Int = 0,
) : BinaryReader(inputData, baseOffset, padding) {

    /**
     * Current Bit Index in the buffer pointing to the next available Bit to read from the buffer.
     * The buffer is treated like a sequence of Bits "as if they ware in the memory."
     * With first octet in the buffer at the lower address in the memory.
     *
     * The numbering scheme controls from which end we access this space.
     */
    private val currentBitIndexInBuffer get() = when (numberingScheme) {
        Endianness.MOST_SIGNIFICANT_FIRST -> baseOffset + currentBitIndex.value
        Endianness.LEAST_SIGNIFICANT_FIRST -> inputBitsCount - 1 - baseOffset - currentBitIndex.value
    }

    /**
     * Index in buffer that points to the Octet containing next available Bit to read.
     */
    private val currentOctetIndex get() = currentBitIndexInBuffer.floorDiv(BITS_IN_OCTET)

    /**
     * Bit Index within the selected Octet pointing to the next available Bit to read.
     */
    private val currentBitIndexInSelectedOctet get() =
        BITS_IN_OCTET - currentBitIndexInBuffer.mod(BITS_IN_OCTET) - 1

    /**
     * Number of Bits in selected Octet that can be read
     */
    private val bitsAvailableInSelectedOctet get() = when (numberingScheme) {
        Endianness.MOST_SIGNIFICANT_FIRST -> currentBitIndexInSelectedOctet + 1
        Endianness.LEAST_SIGNIFICANT_FIRST -> BITS_IN_OCTET + currentBitIndexInSelectedOctet
    }

    /**
     * An Octet containing next available Bit to read
     */
    private val selectedOctet get() = inputData[currentOctetIndex]

    /**
     * Read multiple Bits
     *
     * This function reads up to 64 consecutive Bits from the stream and
     * returns an [ULong] containing them.
     *
     * The Bits are read with order determined by the either Bit Numbering Scheme
     * of this Binary Reader
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @param bitCount number of bits to read
     *
     * @throws BinaryReader.Exception.InvalidBitCount when trying to read more than 64 Bits
     * @throws BinaryReader.Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream
     *
     * @see BinarySignificance
     */
    override fun readBits(bitCount: Int): ULong {

        validateRead(bitCount)

        // How many Bits we want to read
        var bitsToRead = bitCount
        // How many Bits we already read
        var bitsAlreadyRead = 0

        val result = GroupOf64Bits(0x0u, Endianness.MOST_SIGNIFICANT_FIRST)

        while (bitsToRead > 0) {
            val bitsToReadInCurrentByte = min(bitsToRead, bitsAvailableInSelectedOctet)
            val startIndex = currentBitIndexInSelectedOctet

            val endIndex = when (numberingScheme) {
                Endianness.MOST_SIGNIFICANT_FIRST ->
                    currentBitIndexInSelectedOctet - (bitsToReadInCurrentByte - 1)
                Endianness.LEAST_SIGNIFICANT_FIRST ->
                    currentBitIndexInSelectedOctet + (bitsToReadInCurrentByte - 1)
            }

            val group = GroupOf8Bits(selectedOctet)
            val range = startIndex..endIndex
            val bitsRead: UByte = group.getBitsInRange(range.toAscending())

            when (numberingScheme) {
                Endianness.MOST_SIGNIFICANT_FIRST -> {
                    // First Bit Index in result to be modified
                    val start = bitsToRead - 1
                    // Last Bit Index in result to be modified
                    val end = start - (bitsToReadInCurrentByte - 1)
                    // Bit Index to set be modified
                    val indexRange = start..end
                    result.setBitsInRange(
                        indexRange = indexRange.toAscending(),
                        value = bitsRead.toULong()
                    )
                }
                Endianness.LEAST_SIGNIFICANT_FIRST -> {
                    // First Bit Index in result to be modified
                    val start = bitsAlreadyRead
                    // Last Bit Index in result to be modified
                    val end = start + (bitsToReadInCurrentByte - 1)
                    // Bit Index to be modified
                    val indexRange = start..end
                    result.setBitsInRange(
                        indexRange = indexRange.toAscending(),
                        value = bitsRead.toULong()
                    )
                }
            }
            bitsToRead -= bitsToReadInCurrentByte
            bitsAlreadyRead += bitsToReadInCurrentByte
            moveBitIndexBy(bitsToReadInCurrentByte)
        }
        return result.asULong
    }

    /**
     * Returns a new [GATTBinaryReader] with up to 64 bits from a current stream.
     *
     * This function will read given number of Bits and will create a new instance
     * of BinaryReader containing them. It will calculate necessary offset and padding.
     *
     * This function is useful when you need to change a reading direction for a sub section of
     * the current bit stream.
     *
     * @param bitCount number of bits. Maximum 64.
     * @param numberingScheme
     *
     * @throws BinaryReader.Exception.InvalidBitCount when trying to read more than 64 Bits.
     * @throws BinaryReader.Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.InvalidBitCount::class, Exception.BitsCountOutOfBounds::class)
    fun newReaderWithBits(bitCount: Int, numberingScheme: Endianness): GATTBinaryReader {
        val octetsNeededToStore = ceil(bitCount.toFloat().div(BITS_IN_OCTET)).toInt()
        val numberOfExtraBits = octetsNeededToStore.times(BITS_IN_OCTET) - bitCount
        val offset = when (numberingScheme) {
            Endianness.MOST_SIGNIFICANT_FIRST -> numberOfExtraBits
            Endianness.LEAST_SIGNIFICANT_FIRST -> 0
        }
        val padding = when (numberingScheme) {
            Endianness.MOST_SIGNIFICANT_FIRST -> 0
            Endianness.LEAST_SIGNIFICANT_FIRST -> numberOfExtraBits
        }
        val data = when (octetsNeededToStore) {
            Byte.SIZE_BYTES -> readBits(bitCount).toByte().toByteArray()
            Short.SIZE_BYTES -> readBits(bitCount).toShort().toByteArray(numberingScheme)
            Int.SIZE_BYTES - Byte.SIZE_BYTES -> readBits(bitCount).toInt().toByteArray(numberingScheme)
            Int.SIZE_BYTES -> readBits(bitCount).toInt().toByteArray(numberingScheme)
            Long.SIZE_BYTES -> readBits(bitCount).toLong().toByteArray(numberingScheme)
            else -> throw Exception.BitsCountOutOfBounds(bitCount, bitCount)
        }
        return GATTBinaryReader(data, numberingScheme, offset, padding)
    }
}
