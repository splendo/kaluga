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

// Octet Endianness for 8 Bit group doesn't matter as we have only one octet (byte).
// We are much more interested in access to particular bits
internal class GroupOf8Bits(
    from: UByte
) : BitGroup<UByte>(from, Endianness.LEAST_SIGNIFICANT_FIRST) {

    companion object {
        operator fun invoke(fromByte: Byte): GroupOf8Bits = GroupOf8Bits(fromByte.toUByte())
    }

    constructor(fromReader: BinaryReader) : this(fromReader.read8Bits())

    constructor(
        fromByteArray: ByteArray,
        dataEndianness: Endianness
    ) : this(GATTBinaryReader(fromByteArray, dataEndianness))

    override val numberOfBits = UByte.SIZE_BITS

    val asUByte: UByte get() = backingData
    val asByte: Byte get() = backingData.toByte()

    override val asHex get() = backingData.asHex(padLength = numberOfOctets * 2)
    override val asBin get() = backingData.asBin(padLength = numberOfBits)

    override fun bitwiseShiftToLeft(value: UByte, bitCount: Int) = value shl bitCount

    // Note: The `value` is unsigned so using `shr` is correct here.
    // For signed values using `shr` would be incorrect as it would use the whatever value
    // sign bit have (MSB) to fill when shifting
    override fun bitwiseShiftToRight(value: UByte, bitCount: Int) = value shr bitCount
    override fun bitwiseAnd(value1: UByte, value2: UByte) = value1 and value2
    override fun bitwiseOR(value1: UByte, value2: UByte) = value1 or value2
    override fun negateBits(value: UByte) = value.inv()
    override fun increment(value: UByte) = value.inc()
    override fun getFullMask() = maxMask.toUByte()
    override fun convertFromOctet(value: UByte) = value
    override fun convertToOctet(value: UByte) = value
    override fun copy() = GroupOf8Bits(backingData)
    override fun reversedOctetOrder() = copy().also(BitGroup<*>::reverseOctets)

    override fun twosComplement(): GroupOf8Bits {
        val complementValue = calculateTwosComplementOf(backingData)
        return GroupOf8Bits(complementValue)
    }
}
