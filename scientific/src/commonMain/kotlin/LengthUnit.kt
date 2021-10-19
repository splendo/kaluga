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
    ScientificUnit<System, MeasurementType.Length>()

@Serializable
sealed class MetricLength(override val symbol: String) :
    Length<MeasurementSystem.Metric>()

@Serializable
sealed class ImperialLength(override val symbol: String) :
    Length<MeasurementSystem.Imperial>()

// Metric Length
@Serializable
object Millimeter : MetricLength("mm") {
    const val MILLIMETERS_IN_METER = 1000.0
    override fun toSIUnit(value: Decimal): Decimal = value / MILLIMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * MILLIMETERS_IN_METER.toDecimal()
}

@Serializable
object Centimeter : MetricLength("cm") {
    const val CENTIMETERS_IN_METER = 100.0
    override fun toSIUnit(value: Decimal): Decimal = value / CENTIMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CENTIMETERS_IN_METER.toDecimal()
}

@Serializable
object Decimeter : MetricLength("dm") {
    const val DECIMETERS_IN_METER = 10.0
    override fun toSIUnit(value: Decimal): Decimal = value / DECIMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * DECIMETERS_IN_METER.toDecimal()
}

@Serializable
object Meter : MetricLength("m") {
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Decameter : MetricLength("dam") {
    const val DECAMETERS_IN_METER = 0.1
    override fun toSIUnit(value: Decimal): Decimal = value / DECAMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * DECAMETERS_IN_METER.toDecimal()
}

@Serializable
object Hectometer : MetricLength("hm") {
    const val HECTOMETERS_IN_METER = 0.01
    override fun toSIUnit(value: Decimal): Decimal = value / HECTOMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * HECTOMETERS_IN_METER.toDecimal()
}

@Serializable
object Kilometer : MetricLength("km") {
    const val KILOMETERS_IN_METER = 0.001
    override fun toSIUnit(value: Decimal): Decimal = value / KILOMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * KILOMETERS_IN_METER.toDecimal()
}

// Imperial Length
@Serializable
object Inch : ImperialLength("\'") {
    const val INCHES_IN_METER = 39.37007874015748
    override fun toSIUnit(value: Decimal): Decimal = value / INCHES_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * INCHES_IN_METER.toDecimal()
}

@Serializable
object Foot : ImperialLength("\"") {
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
