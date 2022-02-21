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

import org.khronos.webgl.Uint16Array
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

private val isBigEndian = Uint8Array(
    Uint16Array(arrayOf(0x1234.toShort())).buffer
)[0] == 0x12.toByte()

internal actual val Endianness.Companion.native: Endianness
    get() = if (isBigEndian) {
        Endianness.MOST_SIGNIFICANT_FIRST
    } else {
        Endianness.LEAST_SIGNIFICANT_FIRST
    }
