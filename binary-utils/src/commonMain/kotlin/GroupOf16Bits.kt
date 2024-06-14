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

internal class GroupOf16Bits(
    from: UShort,
    dataEndianness: Endianness
) : BitGroup<UShort>(from, dataEndianness) {

    companion object {
        operator fun invoke(
            fromShort: Short,
            dataEndianness: Endianness
        ): GroupOf16Bits = GroupOf16Bits(fromShort.toUShort(), dataEndianness)
    }

    constructor(
        fromReader: GATTBinaryReader
    ) : this(fromReader.read16Bits(), fromReader.numberingScheme)

    constructor(
        fromByteArray: ByteArray,
        dataEndianness: Endianness
    ) : this(GATTBinaryReader(fromByteArray, dataEndianness))

    override val numberOfBits = UShort.SIZE_BITS

    val asUShort: UShort get() = backingData
    val asShort: Short get() = backingData.toShort()

    override val asHex get() = backingData.asHex(padLength = numberOfOctets * 2)
    override val asBin get() = backingData.asBin(padLength = numberOfBits)

    override fun bitwiseShiftToLeft(value: UShort, bitCount: Int) = value shl bitCount

    // Note: The `value` is unsigned so using `shr` is correct here.
    // For signed values using `shr` would be incorrect as it would use the whatever value
    // sign bit have (MSB) to fill when shifting
    override fun bitwiseShiftToRight(value: UShort, bitCount: Int) = value shr bitCount
    override fun bitwiseAnd(value1: UShort, value2: UShort) = value1 and value2
    override fun bitwiseOR(value1: UShort, value2: UShort) = value1 or value2
    override fun negateBits(value: UShort) = value.inv()
    override fun increment(value: UShort) = value.inc()
    override fun getFullMask() = maxMask.toUShort()
    override fun convertFromOctet(value: UByte) = value.toUShort()
    override fun convertToOctet(value: UShort) = value.toUByte()
    override fun copy() = GroupOf16Bits(backingData, octetEndianness)
    override fun reversedOctetOrder() = copy().also(BitGroup<*>::reverseOctets)

    override fun twosComplement(): GroupOf16Bits {
        val complementValue = calculateTwosComplementOf(backingData)
        return GroupOf16Bits(complementValue, octetEndianness)
    }
}
