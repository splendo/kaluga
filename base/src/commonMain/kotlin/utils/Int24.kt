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

package com.splendo.kaluga.base.utils

import kotlin.jvm.JvmInline

@JvmInline
value class Int24(val value: Int) : Comparable<Int24> {
    companion object {
        private const val MIN_VALUE_INT = -0x800000
        private const val MAX_VALUE_INT = 0x7FFFFF
        val MIN_VALUE = Int24(MIN_VALUE_INT)
        val MAX_VALUE = Int24(MAX_VALUE_INT)
        const val SIZE_BYTES = 3
        const val SIZE_BITS = 24
    }
    init {
        require(value in MIN_VALUE_INT..MAX_VALUE_INT) {
            "Value $value out of signed 24-bit range"
        }
    }

    override fun compareTo(other: Int24): Int = value.compareTo(other.value)
    operator fun plus(other: Int24) = (value + other.value).toInt24()
    operator fun minus(other: Int24) = (value - other.value).toInt24()
    operator fun times(other: Int24) = (value * other.value).toInt24()
    operator fun div(other: Int24) = (value / other.value).toInt24()
}
fun Int.toInt24(): Int24 = Int24(this)
