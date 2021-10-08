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
sealed class Length<System : MeasurementSystem> :
    AbstractScientificUnit<System, MeasurementType.Length>()

@Serializable
sealed class MetricLength :
    Length<MeasurementSystem.Metric>()

@Serializable
sealed class ImperialLength(override val symbol: String) :
    Length<MeasurementSystem.Imperial>()

// Metric Length
@Serializable
object Meter : MetricLength(), BaseMetricUnit<MeasurementType.Length, MeasurementSystem.Metric> {
    override val symbol: String = "m"
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanometer : MetricLength(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Length> by Nano(Meter)

@Serializable
object Micrometer : MetricLength(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Length> by Micro(Meter)

@Serializable
object Millimeter : MetricLength(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Length> by Milli(Meter)

@Serializable
object Centimeter : MetricLength(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Length> by Centi(Meter)

@Serializable
object Decimeter : MetricLength(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Length> by Deci(Meter)

@Serializable
object Decameter : MetricLength(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Length> by Deca(Meter)

@Serializable
object Hectometer : MetricLength(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Length> by Hecto(Meter)

@Serializable
object Kilometer : MetricLength(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Length> by Kilo(Meter)

// Imperial Length
@Serializable
object Inch : ImperialLength("in") {
    const val INCHES_IN_METER = 39.37007874015748
    override fun toSIUnit(value: Decimal): Decimal = value / INCHES_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * INCHES_IN_METER.toDecimal()
}

@Serializable
object Foot : ImperialLength("ft") {
    const val FEET_IN_METER = 3.280839895013123
    override fun toSIUnit(value: Decimal): Decimal = value / FEET_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * FEET_IN_METER.toDecimal()
}

@Serializable
object Yard : ImperialLength("yd") {
    const val YARDS_IN_METER = 1.0936132983377078
    override fun toSIUnit(value: Decimal): Decimal = value / YARDS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * YARDS_IN_METER.toDecimal()
}

@Serializable
object Mile : ImperialLength("mi") {
    const val MILES_IN_METER = 0.000621371192237334
    override fun toSIUnit(value: Decimal): Decimal = value / MILES_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * MILES_IN_METER.toDecimal()
}
