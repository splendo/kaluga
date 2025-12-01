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

/**
 * Converts [ByteArray] to [Float]
 * @param octetIndex index of byte to start. Must not be higher than the fourth to last octet.
 * @param byteOrder [ByteOrder] to use for decoding.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 4.
 * @return the decoded [Float]
 */
fun ByteArray.decodeFloat(octetIndex: Int, byteOrder: ByteOrder): Float = Float.fromBits(
    decodeInt(octetIndex, byteOrder),
)

/**
 * Encodes a [Float] into a [ByteArray]
 * @param byteOrder [ByteOrder] to use for encoding.
 * @return [ByteArray] representing the [Float]
 */
fun Float.toByteArray(byteOrder: ByteOrder) = toRawBits().toByteArray(byteOrder)
