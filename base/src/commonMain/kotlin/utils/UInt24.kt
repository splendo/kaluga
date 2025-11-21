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
value class UInt24(val value: UInt) : Comparable<UInt24> {

    companion object {
        private const val MIN_UINT_VALUE = 0U
        private const val MAX_UINT_VALUE = 0xFFFFFFU
        val MIN_VALUE = UInt24(MIN_UINT_VALUE)
        val MAX_VALUE = UInt24(MAX_UINT_VALUE)
        const val SIZE_BYTES = 3
        const val SIZE_BITS = 24
    }

    init {
        require(value in MIN_UINT_VALUE..MAX_UINT_VALUE) {
            "Value $value out of unsigned 24-bit range"
        }
    }

    override fun compareTo(other: UInt24): Int = value.compareTo(other.value)
    operator fun plus(other: UInt24) = (value + other.value).toUInt24()
    operator fun minus(other: UInt24) = (value - other.value).toUInt24()
    operator fun times(other: UInt24) = (value * other.value).toUInt24()
    operator fun div(other: UInt24) = (value / other.value).toUInt24()
}
fun UInt.toUInt24() = UInt24(coerceIn(UInt24.MIN_VALUE.value, UInt24.MAX_VALUE.value))
