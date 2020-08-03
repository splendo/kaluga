/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.beacons

import kotlinx.coroutines.CoroutineScope

private fun List<Byte>.toHexString(initial: String = "") = fold(initial) {
    acc, byte -> acc + byte.toUByte().toString(16).padStart(2, '0')
}

class Eddystone {

    class UID(val namespace: String, val instance: String)

    companion object {
        const val ServiceUUID = "FEAA"
        private const val ValidFrameSize = 18
        private const val UIDFrameType = 0x00

        fun unpackUIDFrame(data: ByteArray, coroutineScope: CoroutineScope): Beacon? {
            if (data.size == ValidFrameSize && data[0] == UIDFrameType.toByte()) {
                val txPower = data[1]
                val namespace = data.slice(2..11)
                require(namespace.size == 10)
                val instance = data.slice(12..17)
                require(instance.size == 6)
                return Beacon(
                    UID(namespace.toHexString(), instance.toHexString()),
                    txPower.toInt(),
                    coroutineScope
                )
            }
            return null
        }
    }
}
