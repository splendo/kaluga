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

import co.touchlab.stately.concurrency.AtomicInt
import co.touchlab.stately.concurrency.value

/**
 * Binary Reader treats inputData as a continuous stream of Bits.
 * It allows to sequentially read group of Bits from it.
 *
 * Binary Reader allow to read sequential groups of Bits of various lengths starting from BitIndex `0`.
 * How BitIndex related to the Bits inside result depends on [readBits] implementation.
 *
 */
sealed class BinaryReader(
    /** A [ByteArray] holding binary inputData */
    inputData: ByteArray,
    /** A number of initial Bits to be skipped (must be positive number) */
    baseOffset: Int = 0,
    /** A number of final Bits to be ignored (must be positive number) */
    padding: Int = 0,
) {

    sealed class Exception : kotlin.Exception() {
        data class InvalidInitialConditions(override val message: String?) : Exception()
        data class InvalidBitCount(val bitCount: Int) : Exception()
        data class BitsCountOutOfBounds(val bitCount: Int, val bitIndex: BitIndex) : Exception()
    }

    /**
     * Controls the behaviour of seeking within the Bit stream.
     *
     * @see seek
     */
    enum class SeekOrigin {
        /**
         * When seeking with this option the current Bit Index is moved
         * relative to the beginning of the Bit stream.
         *
         * @see seek
         */
        SET,
        /**
         * When seeking with this option the current Bit Index is moved
         * relative to the current position in the Bit stream.
         *
         * @see seek
         */
        CURRENT,
        /**
         * When seeking with this option the current Bit Index is moved
         * relative to the end of the Bit stream.
         *
         * @see seek
         */
        END;
    }

    /** Total input bits count */
    protected val inputBitsCount = inputData.size * BitConstants.BITS_IN_OCTET

    /**
     * The size of the Bit stream available to the Reader.
     * This is the number of bits available to read in a stream.
     */
    private val totalBitsInStream = inputBitsCount - baseOffset - padding

    /**
     * Current Bit Index in the Bit stream pointing to the next available Bit to read from the stream.
     */
    protected var currentBitIndex = AtomicInt(0)

    init {
        // Validate Initial Conditions
        if (baseOffset < 0) throw Exception.InvalidInitialConditions(
            "Base offset must be greater or equal to 0"
        )
        if (padding < 0) throw Exception.InvalidInitialConditions(
            "Padding must be greater or equal to 0"
        )
        if (padding + baseOffset > inputBitsCount) throw Exception.InvalidInitialConditions(
            "Padding plus base offset must be less or equal than total bits count"
        )
    }

    /**
     * Seeks in the current Bit stream.
     *
     * Allows to move current Bit Index within the Bit stream.
     * For example, it can be used to skip portions of the stream
     * or to move back and read them again.
     *
     * All subsequent read or write operations will be performed from the new Bit Index.
     *
     * @param bitCount number of Bits to move Index by.
     * @param origin the reference point from which move will be performed.
     *
     * @throws Exception.InvalidBitCount if resulting Bit Index would would fall outside of valid range
     *
     * @see SeekOrigin
     */
    @Throws(Exception.InvalidBitCount::class)
    fun seek(bitCount: Int, origin: SeekOrigin) {
        val bitIndex = when (origin) {
            SeekOrigin.SET -> bitCount
            SeekOrigin.CURRENT -> currentBitIndex.value + bitCount
            SeekOrigin.END -> totalBitsInStream - bitCount
        }
        if (bitIndex !in 0..totalBitsInStream) throw Exception.InvalidBitCount(bitCount)
        currentBitIndex.set(bitIndex)
    }

    /**
     * Moves Bit Index by number of Bits relative to the current position in the Bit stream.
     * All subsequent read or write operations will be performed from the new Bit Index.
     *
     * @param bitCount bitCount number of Bits to move Index by.
     *
     * @throws Exception.InvalidBitCount if resulting Bit Index would would fall outside of valid range.
     *
     * @see seek
     */
    @Throws(Exception.InvalidBitCount::class)
    fun moveBitIndexBy(bitCount: Int) = seek(bitCount, SeekOrigin.CURRENT)

    /**
     * Resets the Bit Index in the Bit stream.
     *
     * After this operation the Bit Index will be zeroed and reader will behave as if it was just created.
     *
     * @see seek
     */
    fun resetBitIndex() = seek(0, SeekOrigin.SET)

    /**
     * Checks if [bitCount] bits count available to read from stream
     * Please note that [readBits] method can read only up to 64 bits
     */
    protected fun validateRead(bitCount: Int) {
        if (bitCount !in 1..ULong.SIZE_BITS)
            throw Exception.InvalidBitCount(bitCount)

        if ((currentBitIndex.value + bitCount) !in 0..totalBitsInStream)
            throw Exception.BitsCountOutOfBounds(bitCount, currentBitIndex.value)
    }

    /**
     * Read multiple Bits
     *
     * This function reads [bitCount] (up to 64) consecutive Bits from the stream and
     * returns an [ULong] containing them.
     *
     * @param bitCount number of bits to read
     *
     * @throws Exception.InvalidBitCount when trying to read more than 64 Bits
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream
     */
    @Throws(Exception.InvalidBitCount::class, Exception.BitsCountOutOfBounds::class)
    abstract fun readBits(bitCount: Int): ULong

    /**
     * Reads 1 Bit from the stream and
     * returns an [UByte] that contains this Bit as the Least Significant Bit
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read1Bit(): UByte {
        val bitsRead = readBits(1)
        return bitsRead.toUByte()
    }

    /**
     * Read 2 consecutive Bits from the stream and
     * returns an [UByte] containing them.
     *
     * The Bits are read with order implemented inside [readBits]
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read2Bits(): UByte {
        val bitsRead = readBits(2)
        return bitsRead.toUByte()
    }

    /**
     * Read 4 consecutive Bits from the stream and
     * returns an [UByte] containing them.
     *
     * The Bits are read with order implemented inside [readBits]
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read4Bits(): UByte {
        val bitsRead = readBits(4)
        return bitsRead.toUByte()
    }

    /**
     * Read 8 consecutive Bits from the stream and
     * returns an [UByte] containing them.
     *
     * The Bits are read with order implemented inside [readBits]
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read8Bits(): UByte {
        val bitsRead = readBits(8)
        return bitsRead.toUByte()
    }

    /**
     * Read 12 consecutive Bits from the stream and
     * returns an [UShort] containing them.
     *
     * The Bits are read with order implemented inside [readBits]
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read12Bits(): UShort {
        val bitsRead = readBits(12)
        return bitsRead.toUShort()
    }

    /**
     * Read 16 consecutive Bits from the stream and
     * returns an [UShort] containing them.
     *
     * The Bits are read with order implemented inside [readBits]
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read16Bits(): UShort {
        val bitsRead = readBits(16)
        return bitsRead.toUShort()
    }

    /**
     * Read 24 consecutive Bits from the stream and
     * returns an [UInt] containing them.
     *
     * The Bits are read with order implemented inside [readBits]
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read24Bits(): UInt {
        val bitsRead = readBits(24)
        return bitsRead.toUInt()
    }

    /**
     * Read 32 consecutive Bits from the stream and
     * returns an [UInt] containing them.
     *
     * The Bits are read with order implemented inside [readBits]
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read32Bits(): UInt {
        val bitsRead = readBits(32)
        return bitsRead.toUInt()
    }

    /**
     * Read 64 consecutive Bits from the stream and
     * returns an [ULong] containing them.
     *
     * The Bits are read with order implemented inside [readBits]
     *
     * Returned value will contain Bits in the order of their significance.
     * It guarantees that all other Bits will be zeroed.
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun read64Bits(): ULong {
        val bitsRead = readBits(64)
        return bitsRead.toULong()
    }

    /**
     * Returns the [ByteArray] of a given [size].
     * It reads a given amount of octets from the Bit stream starting from current Bit Index.
     *
     * @param size number of octets to read
     *
     * @throws Exception.BitsCountOutOfBounds when trying to read more Bits than available in the stream.
     */
    @Throws(Exception.BitsCountOutOfBounds::class)
    fun readByteArray(size: Int) = ByteArray(size) {
        read8Bits().toByte()
    }
}
