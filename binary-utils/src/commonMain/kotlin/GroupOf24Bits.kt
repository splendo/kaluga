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

internal class GroupOf24Bits(
    from: UInt,
    dataEndianness: Endianness
) : BitGroup<UInt>(from, dataEndianness) {

    companion object {
        operator fun invoke(
            fromInt: Int,
            dataEndianness: Endianness
        ): GroupOf24Bits = GroupOf24Bits(fromInt.toUInt(), dataEndianness)
    }

    constructor(
        fromReader: GATTBinaryReader
    ) : this(fromReader.read24Bits(), fromReader.numberingScheme)

    constructor(
        fromByteArray: ByteArray,
        dataEndianness: Endianness
    ) : this(GATTBinaryReader(fromByteArray, dataEndianness))

    override val numberOfBits = UInt.SIZE_BITS - UByte.SIZE_BITS

    val asUInt: UInt get() = backingData
    val asInt: Int get() = backingData.toInt()

    override val asHex get() = backingData.asHex(padLength = numberOfOctets * 2)
    override val asBin get() = backingData.asBin(padLength = numberOfBits)

    override fun bitwiseShiftToLeft(value: UInt, bitCount: Int) = value shl bitCount

    // Note: The `value` is unsigned so using `shr` is correct here.
    // For signed values using `shr` would be incorrect as it would use the whatever value
    // sign bit have (MSB) to fill when shifting
    override fun bitwiseShiftToRight(value: UInt, bitCount: Int) = value shr bitCount
    override fun bitwiseAnd(value1: UInt, value2: UInt) = value1 and value2
    override fun bitwiseOR(value1: UInt, value2: UInt) = value1 or value2
    override fun negateBits(value: UInt) = value.inv()
    override fun increment(value: UInt) = value.inc()
    override fun getFullMask() = maxMask.toUInt()
    override fun convertFromOctet(value: UByte) = value.toUInt()
    override fun convertToOctet(value: UInt) = value.toUByte()
    override fun copy() = GroupOf24Bits(backingData, octetEndianness)
    override fun reversedOctetOrder() = copy().also(BitGroup<*>::reverseOctets)

    override fun twosComplement(): GroupOf24Bits {
        val complementValue = calculateTwosComplementOf(backingData)
        return GroupOf24Bits(complementValue, octetEndianness)
    }
}
