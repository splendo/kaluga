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

import com.splendo.kaluga.base.bytes.ByteOrder
import kotlin.jvm.JvmInline
import kotlin.math.pow

@JvmInline
value class MedFloat16(val value: Float) : Comparable<MedFloat16> {
    internal companion object {
        const val NAN = 0x07FF
        const val POSITIVE_INFINITY = 0x07FE
        const val NEGATIVE_INFINITY = 0x0802
    }

    override fun compareTo(other: MedFloat16): Int = value.compareTo(other.value)
}


