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

package com.splendo.kaluga.base.crc

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.Encoding
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.bytes.toByteArray
import kotlin.test.Test
import kotlin.test.assertEquals

// Validated using the check specified in https://reveng.sourceforge.io/crc-catalogue/
class CRCTest {

    companion object {
        val data = "123456789".toByteArray(StringEncodingSettings(StringEncodingSettings.NoMarking, Encoding.ASCII), ByteOrder.LEAST_SIGNIFICANT_FIRST)
    }

    @Test
    fun checkCRC() {
        CRC3.GSM.assertChecksum(0x4u)
        CRC3.ROHC.assertChecksum(0x6u)
        CRC4.ITU.assertChecksum(0x7u)
        CRC4.Interlaken.assertChecksum(0xbu)
        CRC5.EPC.assertChecksum(0x0u)
        CRC5.ITU.assertChecksum(0x7u)
        CRC5.USB.assertChecksum(0x19u)
        CRC6.CDMA2000.A.assertChecksum(0xdu)
        CRC6.CDMA2000.B.assertChecksum(0x3bu)
        CRC6.DARC.assertChecksum(0x26u)
        CRC6.ITU.assertChecksum(0x6u)
        CRC6.GSM.assertChecksum(0x13u)
        CRC7.assertChecksum(0x75u)
        CRC7.ROHC.assertChecksum(0x53u)
        CRC7.UMTS.assertChecksum(0x61u)
        CRC8.assertChecksum(0xf4u)
        CRC8.AES.assertChecksum(0x97u)
        CRC8.Autosar.assertChecksum(0xdfu)
        CRC8.Bluetooth.assertChecksum(0x26u)
        CRC8.CDMA2000.assertChecksum(0xdau)
        CRC8.DARC.assertChecksum(0x15u)
        CRC8.DVBS2.assertChecksum(0xbcu)
        CRC8.GSM.A.assertChecksum(0x37u)
        CRC8.GSM.B.assertChecksum(0x94u)
        CRC8.HITAG.assertChecksum(0xb4u)
        CRC8.ITU.assertChecksum(0xa1u)
        CRC8.ICODE.assertChecksum(0x7eu)
        CRC8.LTE.assertChecksum(0xeau)
        CRC8.Maxim.assertChecksum(0xa1u)
        CRC8.Mifare.MAD.assertChecksum(0x99u)
        CRC8.NRSC5.assertChecksum(0xf7u)
        CRC8.OpenSafety.assertChecksum(0x3eu)
        CRC8.ROHC.assertChecksum(0xd0u)
        CRC8.SAE.J1850.assertChecksum(0x4bu)
        CRC8.WCDMA.assertChecksum(0x25u)
        CRC10.assertChecksum(0x199u)
        CRC10.CDMA2000.assertChecksum(0x233u)
        CRC10.GSM.assertChecksum(0x12au)
        CRC11.assertChecksum(0x5a3u)
        CRC11.UMTS.assertChecksum(0x61u)
        CRC12.CDMA2000.assertChecksum(0xd4du)
        CRC12.DECT.assertChecksum(0xf5bu)
        CRC12.GSM.assertChecksum(0xb34u)
        CRC12.UMTS.assertChecksum(0xdafu)
        CRC13.BBC.assertChecksum(0x4fau)
        CRC14.DARC.assertChecksum(0x82du)
        CRC14.GSM.assertChecksum(0x30aeu)
        CRC15.assertChecksum(0x59eu)
        CRC15.MPT1327.assertChecksum(0x2566u)
        CRC16.assertChecksum(0xbb3du)
        CRC16.Autosar.assertChecksum(0x29b1u)
        CRC16.CDMA2000.assertChecksum(0x4c06u)
        CRC16.CMS.assertChecksum(0xaee7u)
        CRC16.DARC.assertChecksum(0xd64eu)
        CRC16.DDS110.assertChecksum(0x9ecfu)
        CRC16.DECT.R.assertChecksum(0x7eu)
        CRC16.DECT.X.assertChecksum(0x7fu)
        CRC16.DNP.assertChecksum(0xea82u)
        CRC16.EN13757.assertChecksum(0xc2b7u)
        CRC16.GSM.assertChecksum(0xce3cu)
        CRC16.Kermit.assertChecksum(0x2189u)
        CRC16.LJ1200.assertChecksum(0xbdf4u)
        CRC16.M17.assertChecksum(0x772bu)
        CRC16.Maxim.assertChecksum(0x44c2u)
        CRC16.MCRF4XX.assertChecksum(0x6f91u)
        CRC16.Modbus.assertChecksum(0x4b37u)
        CRC16.NRSC5.assertChecksum(0xa066u)
        CRC16.OpenSafety.A.assertChecksum(0x5d38u)
        CRC16.OpenSafety.B.assertChecksum(0x20feu)
        CRC16.PROFIBUS.assertChecksum(0xa819u)
        CRC16.Riello.assertChecksum(0x63d0u)
        CRC16.SPIFujitsu.assertChecksum(0xe5ccu)
        CRC16.T10DIF.assertChecksum(0xd0dbu)
        CRC16.TeleDisk.assertChecksum(0xfb3u)
        CRC16.TMS37157.assertChecksum(0x26b1u)
        CRC16.UMTS.assertChecksum(0xfee8u)
        CRC16.USB.assertChecksum(0xb4c8u)
        CRC16.XModem.assertChecksum(0x31c3u)
        CRCA.assertChecksum(0xbf05u)
        CRCB.assertChecksum(0x906eu)
        CRC17.CANFD.assertChecksum(0x4f03u)
        CRC21.CANFD.assertChecksum(0xed841u)
        CRC24.BLE.assertChecksum(0xc25a56u)
        CRC24.Flexray.A.assertChecksum(0x7979bdu)
        CRC24.Flexray.B.assertChecksum(0x1f23b8u)
        CRC24.Interlaken.assertChecksum(0xb4f3e6u)
        CRC24.LTE.A.assertChecksum(0xcde703u)
        CRC24.LTE.B.assertChecksum(0x23ef52u)
        CRC24.assertChecksum(0x21cf02u)
        CRC24.OS9.assertChecksum(0x200fa5u)
        CRC30.CDMA.assertChecksum(0x4c34abfu)
        CRC31.Philips.assertChecksum(0xce9e46cu)
        CRC32.assertChecksum(0xcbf43926u)
        CRC32.AIXM.assertChecksum(0x3010bf7fu)
        CRC32.Autosar.assertChecksum(0x1697d06au)
        CRC32.Base91D.assertChecksum(0x87315576u)
        CRC32.BZip2.assertChecksum(0xfc891918u)
        CRC32.CDROMEDC.assertChecksum(0x6ec2edc4u)
        CRC32.ISCSI.assertChecksum(0xe3069283u)
        CRC32.MEF.assertChecksum(0xd2c22f51u)
        CRC32.MPEG2.assertChecksum(0x376e6e7u)
        CRC32.XFER.assertChecksum(0xbd0be338u)
        CKSUM.assertChecksum(0x765e7680u)
        JAMCRC.assertChecksum(0x340bc6d9u)
        CRC40.GSM.assertChecksum(0xd4164fc646u)
        CRC64.assertChecksum(0x6c40df5f0b497347u)
        CRC64.GOISO.assertChecksum(0xb90956c775a41001u)
        CRC64.MS.assertChecksum(0x75d4b74f024eceeau)
        CRC64.NVME.assertChecksum(0xae8b14860a799888u)
        CRC64.REDIS.assertChecksum(0xe9c6d914c4b8d9cau)
        CRC64.WE.assertChecksum(0x62ec59e3f1a4f00au)
        CRC64.XZ.assertChecksum(0x995dc9bbdf1939fau)
    }

    private fun CRC.assertChecksum(expected: ULong) {
        val checksum = compute(data)
        assertEquals(expected, checksum)
    }
}
