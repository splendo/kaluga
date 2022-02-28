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

/** Implementation of CRC-16 */
open class CRC16(
    private val polynomial: UShort
) : CRC<UShort> {

    private val lookupTable: List<UShort> = (0 until 256).map(Int::toUByte).map(::crc16)

    private fun crc16(input: UByte) = (0 until 8).fold(input.toBigEndianUShort()) { result, _ ->
        val isMostSignificantBitOne = result and 0x8000.toUShort() != 0.toUShort()
        val shiftedResult = result shl 1

        if (isMostSignificantBitOne) shiftedResult xor polynomial
        else shiftedResult
    }

    private fun crc16(inputs: UByteArray, initialValue: UShort): UShort {
        return inputs.fold(initialValue) { remainder, byte ->
            val short = byte.toBigEndianUShort()
            val index = (short xor remainder) shr 8
            lookupTable[index.toInt()] xor (remainder shl 8)
        }
    }

    override fun calculate(bytes: ByteArray, initialValue: UShort): UShort {
        return crc16(bytes.toUByteArray(), initialValue)
    }
}

object CRC16_CCITT : CRC16(polynomial = 0x1021u)
