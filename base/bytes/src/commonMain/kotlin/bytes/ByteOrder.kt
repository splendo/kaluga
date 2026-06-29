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
 * Byte order used for encoding and decoding primary types into a [ByteArray]
 */
enum class ByteOrder {
    /**
     * The most significant byte will be added to the front of the byte array.
     */
    MOST_SIGNIFICANT_FIRST,

    /**
     * The least significant byte will be added to the front of the byte array.
     */
    LEAST_SIGNIFICANT_FIRST,
}

internal fun ByteOrder.octetIndex(index: Int, bitsCount: Int) = when (this) {
    ByteOrder.LEAST_SIGNIFICANT_FIRST -> index
    ByteOrder.MOST_SIGNIFICANT_FIRST -> bitsCount / Byte.SIZE_BITS - index - 1
}

internal fun ByteOrder.shift(index: Int, bitsCount: Int) = octetIndex(index, bitsCount) * Byte.SIZE_BITS
