/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable

@Serializable
sealed class Time(override val symbol: String) : ScientificUnit<MeasurementSystem.Global, MeasurementType.Time>()

@Serializable
object Second : Time("s") {
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object MilliSecond : Time("ms") {
    const val MILLISECOND_PER_SECOND = 1000.0
    override fun fromSIUnit(value: Decimal): Decimal = value * MILLISECOND_PER_SECOND.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / MILLISECOND_PER_SECOND.toDecimal()
}

@Serializable
object MicroSecond : Time("μs") {
    const val MICROSECOND_PER_SECOND = 1000000.0
    override fun fromSIUnit(value: Decimal): Decimal = value * MICROSECOND_PER_SECOND.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / MICROSECOND_PER_SECOND.toDecimal()
}

@Serializable
object NanoSecond : Time("ns") {
    const val NANOSECOND_PER_SECOND = 1000000000.0
    override fun fromSIUnit(value: Decimal): Decimal = value * NANOSECOND_PER_SECOND.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / NANOSECOND_PER_SECOND.toDecimal()
}

@Serializable
object Minute : Time("min") {
    const val SECOND_PER_MINUTE = 60.0
    override fun fromSIUnit(value: Decimal): Decimal = value / SECOND_PER_MINUTE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * SECOND_PER_MINUTE.toDecimal()
}

@Serializable
object Hour : Time("h") {
    const val HOUR_PER_MINUTE = 3600.0
    override fun fromSIUnit(value: Decimal): Decimal = value / HOUR_PER_MINUTE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * HOUR_PER_MINUTE.toDecimal()
}
