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

/** Implementation of CRC-8 */
open class CRC8(
    private val polynomial: UByte
) : CRC<UByte> {

    private val lookupTable: List<UByte> = (0 until 256).map(Int::toUByte).map(::crc8)

    private fun crc8(input: UByte) = (0 until 8).fold(input) { result, _ ->
        val isMostSignificantBitOne = result and 0x80u != 0.toUByte()
        val shiftedResult = result shl 1

        if (isMostSignificantBitOne) shiftedResult xor polynomial
        else shiftedResult
    }

    private fun crc8(inputs: UByteArray, initialValue: UByte): UByte {
        return inputs.fold(initialValue) { remainder, byte ->
            lookupTable[byte.xor(remainder).toInt()]
        }
    }

    override fun calculate(bytes: ByteArray, initialValue: UByte): UByte {
        return crc8(bytes.toUByteArray(), initialValue)
    }
}

object CRC8_CCITT : CRC8(polynomial = 0x07u)
