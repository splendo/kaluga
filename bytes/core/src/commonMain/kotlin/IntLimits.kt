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
 * Value ranges of integers narrower than [Int] that binary protocols commonly use.
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
}
