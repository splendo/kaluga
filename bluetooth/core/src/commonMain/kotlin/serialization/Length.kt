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

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.utils.Int24
import com.splendo.kaluga.base.utils.UInt24

/**
 * The length used for encoding (numeric) data.
 * Use the [Size] annotation to add supported lengths to a Number property
 *
 * @property bytes the number of [Byte] added to the [ByteArray] when encoding with this size
 */
@Suppress("EnumEntryName")
enum class Length(val bytes: Int) {
    `8_BIT`(1),
    `16_BIT`(2),
    `24_BIT`(3),
    `32_BIT`(4),
    `64_BIT`(8),
    ;

    internal fun fits(number: Number, signed: Boolean) = when (this) {
        `8_BIT` -> if (signed) {
            number.toInt() in Byte.MIN_VALUE.toInt()..Byte.MAX_VALUE.toInt()
        } else {
            number.toInt().toUInt() in UByte.MIN_VALUE.toUInt()..UByte.MAX_VALUE.toUInt()
        }

        `16_BIT` -> if (signed) {
            number.toInt() in Short.MIN_VALUE.toInt()..Short.MAX_VALUE.toInt()
        } else {
            number.toInt().toUInt() in UShort.MIN_VALUE.toUInt()..UShort.MAX_VALUE.toUInt()
        }

        `24_BIT` -> if (signed) {
            number.toLong() in Int24.MIN_VALUE.value.toLong()..Int24.MAX_VALUE.value.toLong()
        } else {
            number.toLong().toULong() in UInt24.MIN_VALUE.value.toULong()..UInt24.MAX_VALUE.value.toULong()
        }

        `32_BIT` -> if (signed) {
            number.toLong() in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()
        } else {
            number.toLong().toULong() in UInt.MIN_VALUE.toULong()..UInt.MAX_VALUE.toULong()
        }

        `64_BIT` -> true // Always fits as it is the max we support
    }
}
