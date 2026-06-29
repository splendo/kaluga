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
 * Converts [ByteArray] to [Double]
 * @param octetIndex index of byte to start. Must not be higher than the eight to last octet.
 * @param byteOrder [ByteOrder] to use for decoding.
 * @throws IllegalArgumentException if [octetIndex] is bigger than the size of the [ByteArray] minus 8.
 * @return the decoded [Double]
 */
fun ByteArray.decodeDouble(octetIndex: Int, byteOrder: ByteOrder): Double = Double.fromBits(
    decodeLong(octetIndex, byteOrder),
)

/**
 * Encodes a [Double] into a [ByteArray]
 * @param byteOrder [ByteOrder] to use for encoding.
 * @return [ByteArray] representing the [Double]
 */
fun Double.toByteArray(byteOrder: ByteOrder) = toRawBits().toByteArray(byteOrder)

/**
 * Encodes this [Double] and copies it into a [ByteArray] at a given offset.
 * @param array the [ByteArray] to copy the encoded data into.
 * @param offset the offset at which to copy the encoded data.
 * @param byteOrder the [ByteOrder] in which the [Double] is encoded
 * @throws IllegalArgumentException if [array] is not  is not large enough to hold 8 bytes at the [offset].
 * @return the encoded [ByteArray].
 */
fun Double.copyIntoByteArray(array: ByteArray, offset: Int = 0, byteOrder: ByteOrder): ByteArray = toRawBits().copyIntoByteArray(array, offset, byteOrder)
