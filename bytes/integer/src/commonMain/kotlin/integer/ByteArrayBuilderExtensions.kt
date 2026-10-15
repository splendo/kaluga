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

/**
 * Adds an [Int24] to the array, encoded using [Int24.toByteArray]
 * @param int24 the [Int24] to add
 * @param order the [ByteOrder] in which the Int24 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(int24: Int24, order: ByteOrder = byteOrder) = add(int24.toByteArray(order))

/**
 * Adds an [Int40] to the array, encoded using [Int40.toByteArray]
 * @param int40 the [Int40] to add
 * @param order the [ByteOrder] in which the Int40 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(int40: Int40, order: ByteOrder = byteOrder) = add(int40.toByteArray(order))

/**
 * Adds an [Int48] to the array, encoded using [Int48.toByteArray]
 * @param int48 the [Int48] to add
 * @param order the [ByteOrder] in which the Int48 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(int48: Int48, order: ByteOrder = byteOrder) = add(int48.toByteArray(order))

/**
 * Adds a [UInt24] to the array, encoded using [UInt24.toByteArray]
 * @param uInt24 the [UInt24] to add
 * @param order the [ByteOrder] in which the UInt24 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(uInt24: UInt24, order: ByteOrder = byteOrder) = add(uInt24.toByteArray(order))

/**
 * Adds a [UInt40] to the array, encoded using [UInt40.toByteArray]
 * @param uInt40 the [UInt40] to add
 * @param order the [ByteOrder] in which the UInt40 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(uInt40: UInt40, order: ByteOrder = byteOrder) = add(uInt40.toByteArray(order))

/**
 * Adds a [UInt48] to the array, encoded using [UInt48.toByteArray]
 * @param uInt48 the [UInt48] to add
 * @param order the [ByteOrder] in which the UInt48 will be encoded. Defaults to [ByteArrayBuilder.byteOrder]
 */
fun ByteArrayBuilder.add(uInt48: UInt48, order: ByteOrder = byteOrder) = add(uInt48.toByteArray(order))
