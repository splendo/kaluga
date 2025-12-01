/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.bytes

import kotlin.math.ceil

// Algorithms retrieved from https://reveng.sourceforge.io/crc-catalogue/

object CRC3 {
    object GSM : CRC by CRC(3, 0x3u, 0x0u, 0x7u)
    object ROHC : CRC by CRC(3, 0x3u, 0x7u, reflectIn = true, reflectOut = true)
}

object CRC4 {
    object ITU : CRC by CRC(4, 0x3u, 0x0u, reflectIn = true, reflectOut = true)
    object Interlaken : CRC by CRC(4, 0x3u, 0xfu, 0xfu)
}

object CRC5 {
    object EPC : CRC by CRC(5, 0x9u, 0x9u)
    object ITU : CRC by CRC(5, 0x15u, 0x00u, reflectIn = true, reflectOut = true)
    object USB : CRC by CRC(5, 0x05u, 0x1fu, 0x1fu, reflectIn = true, reflectOut = true)
}
object CRC6 {
    object CDMA2000 {
        object A : CRC by CRC(6, 0x27u, 0x3fu)
        object B : CRC by CRC(6, 0x07u, 0x3fu)
    }
    object DARC : CRC by CRC(6, 0x19u, 0x00u, reflectIn = true, reflectOut = true)
    object ITU : CRC by CRC(6, 0x03u, 0x00u, reflectIn = true, reflectOut = true)
    object GSM : CRC by CRC(6, 0x2fu, 0x00u, 0x3fu)
}

object CRC7 : CRC by CRC(7, 0x9u, 0x0u) {
    object ROHC : CRC by CRC(7, 0x4fu, 0x7fu, reflectIn = true, reflectOut = true)
    object UMTS : CRC by CRC(7, 0x45u, 0x0u)
}

object CRC8 : CRC by CRC(8, 0x07u, 0x00u) {

    object AES : CRC by CRC(8, 0x1du, 0xffu, reflectIn = true, reflectOut = true)
    object Autosar : CRC by CRC(8, 0x2fu, 0xffu, 0xffu)
    object Bluetooth : CRC by CRC(8, 0xa7u, 0x00u, reflectIn = true, reflectOut = true)
    object CDMA2000 : CRC by CRC(width = 8, 0x9bu, 0xffu)
    object DARC : CRC by CRC(8, 0x39u, 0x00u, reflectIn = true, reflectOut = true)
    object DVBS2 : CRC by CRC(8, 0xd5u, 0x00u)
    object GSM {
        object A : CRC by CRC(8, 0x1du, 0x00u)
        object B : CRC by CRC(8, 0x49u, 0x00u, 0xffu)
    }
    object HITAG : CRC by CRC(8, 0x1du, 0xffu)
    object ITU : CRC by CRC(8, 0x07u, 0x00u, 0x55u)
    object ICODE : CRC by CRC(8, 0x1du, 0xfdu)
    object LTE : CRC by CRC(8, 0x9bu, 0x00u)
    object Maxim : CRC by CRC(8, 0x31u, 0x00u, reflectIn = true, reflectOut = true)
    object Mifare {
        object MAD : CRC by CRC(8, 0x1du, 0xc7u)
    }
    object NRSC5 : CRC by CRC(8, 0x31u, 0xffu)
    object OpenSafety : CRC by CRC(8, 0x2fu, 0x0u)
    object ROHC : CRC by CRC(8, 0x7u, 0xffu, reflectIn = true, reflectOut = true)
    object SAE {
        object J1850 : CRC by CRC(8, 0x1du, 0xffu, xorOut = 0xffu)
    }
    object WCDMA : CRC by CRC(8, 0x9bu, 0x00u, reflectIn = true, reflectOut = true)
}

object CRC10 : CRC by CRC(10, 0x233u, 0x000u) {
    object CDMA2000 : CRC by CRC(10, 0x3d9u, 0x3ffu)
    object GSM : CRC by CRC(10, 0x175u, 0x000u, xorOut = 0x3ffu)
}

object CRC11 : CRC by CRC(11, 0x385u, 0x01au) {
    object UMTS : CRC by CRC(11, 0x307u, 0x000u)
}

object CRC12 {
    object CDMA2000 : CRC by CRC(12, 0xf13u, 0xfffu)
    object DECT : CRC by CRC(12, 0x80fu, 0x000u)
    object GSM : CRC by CRC(12, 0xd31u, 0x000u, xorOut = 0xfffu)
    object UMTS : CRC by CRC(12, 0x80fu, 0x000u, reflectOut = true)
}

object CRC13 {
    object BBC : CRC by CRC(13, 0x1cf5u, 0x0000u)
}

object CRC14 {
    object DARC : CRC by CRC(14, 0x0805u, 0x0000u, reflectIn = true, reflectOut = true)
    object GSM : CRC by CRC(14, 0x0202du, 0x0000u, xorOut = 0x3fffu)
}

object CRC15 : CRC by CRC(15, 0x4599u, 0x0000u) {
    object MPT1327 : CRC by CRC(15, 0x6815u, 0x0000u, xorOut = 0x0001u)
}

object CRC16 : CRC by CRC(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true) {

    object Autosar : CRC by CRC(16, 0x1021u, 0xffffu)
    object CDMA2000 : CRC by CRC(16, 0xc867u, 0xffffu)
    object CMS : CRC by CRC(16, 0x8005u, 0xffffu)
    object DARC : CRC by CRC(16, 0x1021u, 0xffffu, xorOut = 0xffffu)
    object DDS110 : CRC by CRC(16, 0x8005u, 0x800du)
    object DECT {
        object R : CRC by CRC(16, 0x0589u, 0x0000u, xorOut = 0x0001u)
        object X : CRC by CRC(16, 0x0589u, 0x0000u)
    }
    object DNP : CRC by CRC(16, 0x3d65u, 0x0000u, xorOut = 0xffffu, reflectIn = true, reflectOut = true)
    object EN13757 : CRC by CRC(16, 0x3d65u, 0x0000u, xorOut = 0xffffu)
    object GSM : CRC by CRC(16, 0x1021u, 0x0000u, xorOut = 0xffffu)
    object Kermit : CRC by CRC(16, 0x1021u, 0x0000u, reflectIn = true, reflectOut = true)
    object LJ1200 : CRC by CRC(16, 0x6f63u, 0x0000u)
    object M17 : CRC by CRC(16, 0x5935u, 0xffffu)
    object Maxim : CRC by CRC(16, 0x8005u, 0x0000u, xorOut = 0xffffu, reflectIn = true, reflectOut = true)
    object MCRF4XX : CRC by CRC(16, 0x1021u, 0xffffu, reflectIn = true, reflectOut = true)
    object Modbus : CRC by CRC(16, 0x8005u, 0xffffu, reflectIn = true, reflectOut = true)
    object NRSC5 : CRC by CRC(16, 0x080bu, 0xffffu, reflectIn = true, reflectOut = true)
    object OpenSafety {
        object A : CRC by CRC(16, 0x5935u, 0x0000u)
        object B : CRC by CRC(16, 0x755bu, 0x0000u)
    }
    object PROFIBUS : CRC by CRC(16, 0x1dcfu, 0xffffu, xorOut = 0xffffu)
    object Riello : CRC by CRC(16, 0x1021u, 0xb2aau, reflectIn = true, reflectOut = true)
    object SPIFujitsu : CRC by CRC(16, 0x1021u, 0x1d0fu)
    object T10DIF : CRC by CRC(16, 0x8bb7u, 0x0000u)
    object TeleDisk : CRC by CRC(16, 0xa097u, 0x0000u)
    object TMS37157 : CRC by CRC(16, 0x1021u, 0x89ecu, reflectIn = true, reflectOut = true)
    object UMTS : CRC by CRC(16, 0x8005u, 0x0000u)
    object USB : CRC by CRC(16, 0x8005u, 0xffffu, xorOut = 0xffffu, reflectIn = true, reflectOut = true)
    object XModem : CRC by CRC(16, 0x1021u, 0x0000u)
}

object CRCA : CRC by CRC(16, 0x1021u, 0xc6c6u, reflectIn = true, reflectOut = true)
object CRCB : CRC by CRC(16, 0x1021u, 0xffffu, xorOut = 0xffffu, reflectIn = true, reflectOut = true)

object CRC17 {
    object CANFD : CRC by CRC(17, 0x1685bu, 0x00000u)
}

object CRC21 {
    object CANFD : CRC by CRC(21, 0x102899u, 0x00000u)
}

object CRC24 : CRC by CRC(24, 0x864cfbu, 0xb704ceu) {
    object BLE : CRC by CRC(24, 0x00065bu, 0x555555u, reflectIn = true, reflectOut = true)
    object Flexray {
        object A : CRC by CRC(24, 0x5d6dcbu, 0xfedcbau)
        object B : CRC by CRC(24, 0x5d6dcbu, 0xabcdefu)
    }
    object Interlaken : CRC by CRC(24, 0x328b63u, 0xffffffu, xorOut = 0xffffffu)
    object LTE {
        object A : CRC by CRC(24, 0x864cfbu, 0x000000u)
        object B : CRC by CRC(24, 0x800063u, 0x000000u)
    }
    object OS9 : CRC by CRC(24, 0x800063u, 0xffffffu, xorOut = 0xffffffu)
}

object CRC30 {
    object CDMA : CRC by CRC(30, 0x2030b9c7u, 0x3fffffffu, xorOut = 0x3fffffffu)
}

object CRC31 {
    object Philips : CRC by CRC(31, 0x4c11db7u, 0x7fffffffu, xorOut = 0x7fffffffu)
}

object CRC32 : CRC by CRC(32, 0x4c11db7u, 0xffffffffu, xorOut = 0xffffffffu, reflectIn = true, reflectOut = true) {
    object AIXM : CRC by CRC(32, 0x814141abu, 0x00000000u)
    object Autosar : CRC by CRC(32, 0xf4acfb13u, 0xffffffffu, xorOut = 0xffffffffu, reflectIn = true, reflectOut = true)
    object Base91D : CRC by CRC(32, 0xa833982bu, 0xffffffffu, xorOut = 0xffffffffu, reflectIn = true, reflectOut = true)
    object BZip2 : CRC by CRC(32, 0x4c11db7u, 0xffffffffu, xorOut = 0xffffffffu)
    object CDROMEDC : CRC by CRC(32, 0x8001801bu, 0x00000000u, reflectIn = true, reflectOut = true)
    object ISCSI : CRC by CRC(32, 0x1edc6f41u, 0xffffffffu, xorOut = 0xffffffffu, reflectIn = true, reflectOut = true)
    object MEF : CRC by CRC(32, 0x741b8cd7u, 0xffffffffu, reflectIn = true, reflectOut = true)
    object MPEG2 : CRC by CRC(32, 0x4c11db7u, 0xffffffffu)
    object XFER : CRC by CRC(32, 0xafu, 0x00000000u)
}

object CKSUM : CRC by CRC(32, 0x4c11db7u, 0x00000000u, xorOut = 0xffffffffu)
object JAMCRC : CRC by CRC(32, 0x4c11db7u, 0xffffffffu, reflectIn = true, reflectOut = true)

object CRC40 {
    object GSM : CRC by CRC(40, 0x4820009u, 0x0u, xorOut = 0xffffffffffu)
}

object CRC64 : CRC by CRC(64, 0x42f0e1eba9ea3693u, 0x0000000000000000u) {
    object GOISO : CRC by CRC(64, 0x00000000000001bu, 0xffffffffffffffffu, xorOut = 0xffffffffffffffffu, reflectIn = true, reflectOut = true)
    object MS : CRC by CRC(64, 0x259c84cba6426349u, 0xffffffffffffffffu, reflectIn = true, reflectOut = true)
    object NVME : CRC by CRC(64, 0xad93d23594c93659u, 0xffffffffffffffffu, xorOut = 0xffffffffffffffffu, reflectIn = true, reflectOut = true)
    object REDIS : CRC by CRC(64, 0xad93d23594c935a9u, 0x0000000000000000u, reflectIn = true, reflectOut = true)
    object WE : CRC by CRC(64, 0x42f0e1eba9ea3693u, 0xffffffffffffffffu, xorOut = 0xffffffffffffffffu)
    object XZ : CRC by CRC(64, 0x42f0e1eba9ea3693u, 0xffffffffffffffffu, xorOut = 0xffffffffffffffffu, reflectIn = true, reflectOut = true)
}

/**
 * A Cyclic Redundancy Check (CRC)
 */
interface CRC {

    companion object {

        /**
         * Creates a [CRC] with the given parameters.
         * @param width the width of the CRC in bits. Must be between 1 and 64 bits.
         * @param polynomial the polynomial used to compute the CRC.
         * @param init the initial value of the CRC.
         * @param xorOut the value to XOR with the result to get the final CRC.
         * @param reflectIn whether to reflect the input bytes before computing the CRC.
         * @param reflectOut whether to reflect the output bytes after computing the CRC.
         */
        operator fun invoke(width: Int, polynomial: ULong, init: ULong, xorOut: ULong = 0u, reflectIn: Boolean = false, reflectOut: Boolean = false): CRC =
            Impl(width, polynomial, init, xorOut, reflectIn, reflectOut)
    }

    private class Impl(
        override val width: Int,
        private val polynomial: ULong,
        private val init: ULong,
        private val xorOut: ULong = 0u,
        private val reflectIn: Boolean = false,
        private val reflectOut: Boolean = false,
    ) : CRC {

        init {
            require(width in 1..64)
        }
        private val crcMask: ULong = if (width == 64) {
            0xFFFFFFFFFFFFFFFFu
        } else {
            ((1UL shl width) - 1UL)
        }

        private val msbMask: ULong = 1UL shl (width - 1)

        override fun compute(data: ByteArray): ULong {
            var crc = init and crcMask

            for (byte in data) {
                var current = byte.toULong() and 0xFFu

                if (reflectIn) {
                    current = current.reverseBits(8)
                }

                if (width >= 8) {
                    crc = crc xor (current shl (width - 8))
                    repeat(8) {
                        crc = if ((crc and msbMask) != 0UL) {
                            (crc shl 1) xor polynomial
                        } else {
                            crc shl 1
                        }
                        crc = crc and crcMask
                    }
                } else {
                    repeat(8) {
                        val bit = (current and 0x80u) != 0UL
                        current = current shl 1

                        val crcMsbSet = (crc and msbMask) != 0UL

                        crc = (crc shl 1) and crcMask

                        if (bit xor crcMsbSet) {
                            crc = crc xor polynomial
                        }
                    }
                }
            }

            if (reflectOut) {
                // reverse CRC width bits
                crc = crc.reverseBits(width)
            }

            return (crc xor xorOut) and crcMask
        }

        private fun ULong.reverseBits(width: Int): ULong {
            var v = this
            var r = 0UL
            repeat(width) {
                r = (r shl 1) or (v and 1UL)
                v = v shr 1
            }
            return r
        }
    }

    val width: Int
    val byteWidth: Int get() = ceil(width / 8.0).toInt()

    /**
     * Computes the CRC of the given data.
     * @param data the data to compute the CRC of.
     * @return the CRC of the given data.
     */
    fun compute(data: ByteArray): ULong
}
