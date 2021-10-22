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
sealed class Length : AbstractScientificUnit<MeasurementType.Length>()

@Serializable
sealed class MetricLength : Length(), MetricScientificUnit<MeasurementType.Length>

@Serializable
sealed class ImperialLength(override val symbol: String) : Length(), ImperialScientificUnit<MeasurementType.Length> {
    override val system = MeasurementSystem.Imperial
    override val type = MeasurementType.Length
}

// Metric Length
@Serializable
object Meter : MetricLength(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Length> {
    override val symbol: String = "m"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Length
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanometer : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Nano(Meter)

@Serializable
object Micrometer : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Micro(Meter)

@Serializable
object Millimeter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Milli(Meter)

@Serializable
object Centimeter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Centi(Meter)

@Serializable
object Decimeter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Deci(Meter)

@Serializable
object Decameter : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Deca(Meter)

@Serializable
object Hectometer : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Hecto(Meter)

@Serializable
object Kilometer : MetricLength(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Length, Meter> by Kilo(Meter)

// Imperial Length
@Serializable
object Inch : ImperialLength("in") {
    private const val INCHES_IN_FOOT = 12
    override fun toSIUnit(value: Decimal): Decimal = Foot.toSIUnit(value / INCHES_IN_FOOT.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(value) * INCHES_IN_FOOT.toDecimal()
}

@Serializable
object Foot : ImperialLength("ft") {
    private const val METER_IN_FEET = 0.3048
    override fun toSIUnit(value: Decimal): Decimal = value * METER_IN_FEET.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value / METER_IN_FEET.toDecimal()
}

@Serializable
object Yard : ImperialLength("yd") {
    private const val FOOT_IN_YARD = 3
    override fun toSIUnit(value: Decimal): Decimal = Foot.toSIUnit(value * FOOT_IN_YARD.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Foot.fromSIUnit(value) / FOOT_IN_YARD.toDecimal()
}

@Serializable
object Mile : ImperialLength("mi") {
    private const val YARDS_IN_MILE = 1760
    override fun toSIUnit(value: Decimal): Decimal = Yard.toSIUnit(value * YARDS_IN_MILE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Yard.fromSIUnit(value) / YARDS_IN_MILE.toDecimal()
}
