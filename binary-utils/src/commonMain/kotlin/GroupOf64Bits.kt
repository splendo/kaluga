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

internal class GroupOf64Bits(
    from: ULong,
    dataEndianness: Endianness
) : BitGroup<ULong>(from, dataEndianness) {

    companion object {
        operator fun invoke(
            fromLong: Long,
            dataEndianness: Endianness
        ): GroupOf64Bits = GroupOf64Bits(fromLong.toULong(), dataEndianness)
    }

    constructor(
        fromReader: GATTBinaryReader
    ) : this(fromReader.read64Bits(), fromReader.numberingScheme)

    constructor(
        fromByteArray: ByteArray,
        dataEndianness: Endianness
    ) : this(GATTBinaryReader(fromByteArray, dataEndianness))

    override val numberOfBits = ULong.SIZE_BITS

    val asULong: ULong get() = backingData
    val asLong: Long get() = backingData.toLong()

    override val asHex get() = backingData.asHex(padLength = numberOfOctets * 2)
    override val asBin get() = backingData.asBin(padLength = numberOfBits)

    override fun bitwiseShiftToLeft(value: ULong, bitCount: Int) = value shl bitCount
    override fun bitwiseShiftToRight(value: ULong, bitCount: Int) = value shr bitCount
    override fun bitwiseAnd(value1: ULong, value2: ULong) = value1 and value2
    override fun bitwiseOR(value1: ULong, value2: ULong) = value1 or value2
    override fun negateBits(value: ULong) = value.inv()
    override fun increment(value: ULong) = value.inc()
    override fun getFullMask() = maxMask
    override fun convertFromOctet(value: UByte) = value.toULong()
    override fun convertToOctet(value: ULong) = value.toUByte()
    override fun copy() = GroupOf64Bits(backingData, octetEndianness)
    override fun reversedOctetOrder() = copy().also(BitGroup<*>::reverseOctets)

    override fun twosComplement(): GroupOf64Bits {
        val complementValue = calculateTwosComplementOf(backingData)
        return GroupOf64Bits(complementValue, octetEndianness)
    }
}
