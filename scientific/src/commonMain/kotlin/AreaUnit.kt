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
import kotlin.math.pow

@Serializable
sealed class Area<System : MeasurementSystem> :
    ScientificUnit<System, MeasurementType.Area>()

@Serializable
sealed class MetricArea(override val symbol: String) :
    Area<MeasurementSystem.Metric>()

@Serializable
sealed class USImperialArea(override val symbol: String) :
    Area<MeasurementSystem.USCustomary>()

@Serializable
sealed class UKImperialArea(override val symbol: String) :
    Area<MeasurementSystem.UKImperial>()

@Serializable
sealed class ImperialArea(override val symbol: String) :
    Area<MeasurementSystem.Imperial>()

// Metric Volume
@Serializable
object SquareMeter : MetricArea("m2") {
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object SquareDecimeter : MetricArea("dm2") {
    val SQUARE_DECIMETER_IN_SQUARE_METER = Decimeter.DECIMETERS_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_DECIMETER_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_DECIMETER_IN_SQUARE_METER.toDecimal()
}

@Serializable
object SquareCentimeter : MetricArea("cm2") {
    val SQUARE_CENTIMETER_IN_SQUARE_METER = Centimeter.CENTIMETERS_IN_METER.pow(3)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_CENTIMETER_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_CENTIMETER_IN_SQUARE_METER.toDecimal()
}

@Serializable
object SquareMillimeter : MetricArea("mm2") {
    val SQUARE_MILLIMETER_IN_SQUARE_METER = Millimeter.MILLIMETERS_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_MILLIMETER_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_MILLIMETER_IN_SQUARE_METER.toDecimal()
}

@Serializable
object SquareDecameter : MetricArea("dam2") {
    val SQUARE_DECAMETER_IN_SQUARE_METER = Decameter.DECAMETERS_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_DECAMETER_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_DECAMETER_IN_SQUARE_METER.toDecimal()
}

@Serializable
object SquareHectometer : MetricArea("hm2") {
    val SQUARE_HECTOMETER_IN_SQUARE_METER = Hectometer.HECTOMETERS_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_HECTOMETER_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_HECTOMETER_IN_SQUARE_METER.toDecimal()
}

@Serializable
object Hectare : MetricArea("ha") {
    override fun toSIUnit(value: Decimal): Decimal = Hectometer.toSIUnit(value)
    override fun fromSIUnit(value: Decimal): Decimal = Hectometer.fromSIUnit(value)
}

@Serializable
object SquareKilometer : MetricArea("km2") {
    val SQUARE_KILOMETER_IN_SQUARE_METER = Kilometer.KILOMETERS_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_KILOMETER_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_KILOMETER_IN_SQUARE_METER.toDecimal()
}

@Serializable
object SquareMile : ImperialArea("sq. mi") {
    val SQUARE_MILE_IN_SQUARE_METER = Mile.MILES_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_MILE_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_MILE_IN_SQUARE_METER.toDecimal()
}

@Serializable
object SquareYard : ImperialArea("sq. yd") {
    val SQUARE_YARD_IN_SQUARE_METER = Yard.YARDS_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_YARD_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_YARD_IN_SQUARE_METER.toDecimal()
}

@Serializable
object SquareFoot : ImperialArea("sq. ft") {
    val SQUARE_FEET_IN_SQUARE_METER = Foot.FEET_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_FEET_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_FEET_IN_SQUARE_METER.toDecimal()
}

@Serializable
object SquareInch : ImperialArea("sq. ft") {
    val SQUARE_INCHES_IN_SQUARE_METER = Inch.INCHES_IN_METER.pow(2)
    override fun toSIUnit(value: Decimal): Decimal = value / SQUARE_INCHES_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SQUARE_INCHES_IN_SQUARE_METER.toDecimal()
}

@Serializable
object Acre : ImperialArea("acre") {
    val ACRES_IN_SQUARE_METER = 640.0 * SquareMile.SQUARE_MILE_IN_SQUARE_METER
    override fun toSIUnit(value: Decimal): Decimal = value / ACRES_IN_SQUARE_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * ACRES_IN_SQUARE_METER.toDecimal()
}

fun Area<*>.areaFrom(width: Decimal, depth: Decimal, lengthUnit: Length<*>): Decimal {
    val area = lengthUnit.toSIUnit(width) * lengthUnit.toSIUnit(depth)
    return SquareMeter.convert(area, this)
}
