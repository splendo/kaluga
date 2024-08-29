/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [Time]
 */
val TimeUnits: Set<Time> get() = setOf(
    Second,
    Nanosecond,
    Microsecond,
    Millisecond,
    Centisecond,
    Decisecond,
    Minute,
    Hour,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Time]
 * SI unit is [Second]
 */
@Serializable
sealed class Time :
    AbstractScientificUnit<PhysicalQuantity.Time>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Time>

@Serializable
data object Second : Time(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time> {
    override val symbol = "s"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Time
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class SecondMultiple :
    Time(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second>

@Serializable
data object Decisecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Deci(Second)

@Serializable
data object Centisecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Centi(Second)

@Serializable
data object Millisecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Milli(Second)

@Serializable
data object Microsecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Micro(Second)

@Serializable
data object Nanosecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Nano(Second)

@Serializable
data object Minute : Time() {
    private const val SECOND_PER_MINUTE = 60.0
    override val symbol: String = "min"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Time
    override fun fromSIUnit(value: Decimal): Decimal = value / SECOND_PER_MINUTE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * SECOND_PER_MINUTE.toDecimal()
}

@Serializable
data object Hour : Time() {
    private const val SECOND_PER_HOUR = 3600.0
    override val symbol: String = "h"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Time
    override fun fromSIUnit(value: Decimal): Decimal = value / SECOND_PER_HOUR.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * SECOND_PER_HOUR.toDecimal()
}
