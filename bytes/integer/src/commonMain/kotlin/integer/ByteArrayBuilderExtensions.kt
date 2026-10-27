/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bytes.integer

import com.splendo.kaluga.bytes.ByteArrayBuilder
import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.addEncoded

/**
 * Adds an [Int24] to the array, encoded using [Int24.copyIntoByteArray]
 * @param int24 the [Int24] to add
 * @param order the [ByteOrder] in which the Int24 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(int24: Int24, order: ByteOrder = byteOrder) = addEncoded(Int24.SIZE_BYTES) { array, offset ->
    int24.copyIntoByteArray(array, offset, order)
}

/**
 * Adds an [Int40] to the array, encoded using [Int40.copyIntoByteArray]
 * @param int40 the [Int40] to add
 * @param order the [ByteOrder] in which the Int40 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(int40: Int40, order: ByteOrder = byteOrder) = addEncoded(Int40.SIZE_BYTES) { array, offset ->
    int40.copyIntoByteArray(array, offset, order)
}

/**
 * Adds an [Int48] to the array, encoded using [Int48.copyIntoByteArray]
 * @param int48 the [Int48] to add
 * @param order the [ByteOrder] in which the Int48 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(int48: Int48, order: ByteOrder = byteOrder) = addEncoded(Int48.SIZE_BYTES) { array, offset ->
    int48.copyIntoByteArray(array, offset, order)
}

/**
 * Adds a [UInt24] to the array, encoded using [UInt24.copyIntoByteArray]
 * @param uInt24 the [UInt24] to add
 * @param order the [ByteOrder] in which the UInt24 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(uInt24: UInt24, order: ByteOrder = byteOrder) = addEncoded(UInt24.SIZE_BYTES) { array, offset ->
    uInt24.copyIntoByteArray(array, offset, order)
}

/**
 * Adds a [UInt40] to the array, encoded using [UInt40.copyIntoByteArray]
 * @param uInt40 the [UInt40] to add
 * @param order the [ByteOrder] in which the UInt40 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(uInt40: UInt40, order: ByteOrder = byteOrder) = addEncoded(UInt40.SIZE_BYTES) { array, offset ->
    uInt40.copyIntoByteArray(array, offset, order)
}

/**
 * Adds a [UInt48] to the array, encoded using [UInt48.copyIntoByteArray]
 * @param uInt48 the [UInt48] to add
 * @param order the [ByteOrder] in which the UInt48 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(uInt48: UInt48, order: ByteOrder = byteOrder) = addEncoded(UInt48.SIZE_BYTES) { array, offset ->
    uInt48.copyIntoByteArray(array, offset, order)
}
