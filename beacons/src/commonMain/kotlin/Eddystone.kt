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

package com.splendo.kaluga.bluetooth.beacons

import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.TxPower
import com.splendo.kaluga.bluetooth.uuidFrom

private fun List<Byte>.toHexString() = toByteArray().toHexString()

/**
 * The Eddystone protocol message to read from a Beacon
 * https://github.com/google/eddystone/blob/master/protocol-specification.md
 */
class Eddystone {

    /**
     * The Eddystone-UID
     * https://github.com/google/eddystone/tree/master/eddystone-uid
     * @property namespace the namespace component of the UID. The namespace portion of the ID may be used to group a particular set of beacons
     * @property instance the instance component of the UID, identifying individual devices in a group
     */
    data class UID(val namespace: String, val instance: String) {
        fun asString() = namespace + instance
    }

    /**
     * The Eddystone-UID frame
     * @property txPower the [TxPower] of the frame
     * @property uid the [UID] of the frame
     */
    data class Frame(val txPower: TxPower, val uid: UID)

    companion object {

        internal val SERVICE_UUID = uuidFrom("0000FEAA-0000-1000-8000-00805F9B34FB")

        private const val VALID_FRAME_SIZE = 18
        private const val UID_FRAME_TYPE = 0x00

        private fun ByteArray.isValidUIDFrame() = this.size == VALID_FRAME_SIZE && this[0] == UID_FRAME_TYPE.toByte()

        internal fun unpack(data: ByteArray) = if (data.isValidUIDFrame()) {
            val txPower = data[1]
            val namespace = data.slice(2..11)
            val instance = data.slice(12..17)
            Frame(
                txPower.toInt(),
                UID(namespace.toHexString(), instance.toHexString()),
            )
        } else {
            null
        }
    }
}
