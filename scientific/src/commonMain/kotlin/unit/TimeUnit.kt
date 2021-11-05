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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.MeasurementType
import kotlinx.serialization.Serializable

val TimeUnits: Set<Time> get() = setOf(
    Second,
    Nanosecond,
    Microsecond,
    Millisecond,
    Centisecond,
    Decisecond,
    Minute,
    Hour
)

@Serializable
sealed class Time : AbstractScientificUnit<MeasurementType.Time>(), MetricAndImperialScientificUnit<MeasurementType.Time>

@Serializable
object Second : Time(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Time> {
    override val symbol = "s"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Time
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Decisecond : Time(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Time, Second> by Deci(Second)

@Serializable
object Centisecond : Time(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Time, Second> by Centi(Second)

@Serializable
object Millisecond : Time(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Time, Second> by Milli(Second)

@Serializable
object Microsecond : Time(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Time, Second> by Micro(Second)

@Serializable
object Nanosecond : Time(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Time, Second> by Nano(Second)

@Serializable
object Minute : Time() {
    private const val SECOND_PER_MINUTE = 60.0
    override val symbol: String = "min"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Time
    override fun fromSIUnit(value: Decimal): Decimal = value / SECOND_PER_MINUTE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * SECOND_PER_MINUTE.toDecimal()
}

@Serializable
object Hour : Time() {
    private const val SECOND_PER_HOUR = 3600.0
    override val symbol: String = "h"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Time
    override fun fromSIUnit(value: Decimal): Decimal = value / SECOND_PER_HOUR.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * SECOND_PER_HOUR.toDecimal()
}
