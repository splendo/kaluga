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

package com.splendo.kaluga.bytes

/**
 * Value ranges of the 24-, 40- and 48-bit integers that binary protocols commonly use.
 * The unsigned ranges start at zero.
 */
object IntLimits {

    /**
     * The minimum value of a signed 24-bit integer.
     */
    const val INT24_MIN_VALUE = -0x800000

    /**
     * The maximum value of a signed 24-bit integer.
     */
    const val INT24_MAX_VALUE = 0x7FFFFF

    /**
     * The minimum value of a signed 40-bit integer.
     */
    const val INT40_MIN_VALUE = -0x8000000000L

    /**
     * The maximum value of a signed 40-bit integer.
     */
    const val INT40_MAX_VALUE = 0x7FFFFFFFFFL

    /**
     * The minimum value of a signed 48-bit integer.
     */
    const val INT48_MIN_VALUE = -0x800000000000L

    /**
     * The maximum value of a signed 48-bit integer.
     */
    const val INT48_MAX_VALUE = 0x7FFFFFFFFFFFL

    /**
     * The maximum value of an unsigned 24-bit integer.
     */
    const val UINT24_MAX_VALUE = 0xFFFFFFU

    /**
     * The maximum value of an unsigned 40-bit integer.
     */
    const val UINT40_MAX_VALUE = 0xFFFFFFFFFFUL

    /**
     * The maximum value of an unsigned 48-bit integer.
     */
    const val UINT48_MAX_VALUE = 0xFFFFFFFFFFFFUL
}
