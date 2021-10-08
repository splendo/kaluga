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
import com.splendo.kaluga.base.utils.pow
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable
import kotlin.math.pow

@Serializable
sealed class Area<System : MeasurementSystem> :
    AbstractScientificUnit<System, MeasurementType.Area>()

@Serializable
sealed class MetricArea :
    Area<MeasurementSystem.Metric>()

@Serializable
sealed class ImperialArea :
    Area<MeasurementSystem.Imperial>()

class Square<S : MeasurementSystem, U : ScientificUnit<S, MeasurementType.Length>>(private val unit : U) : ScientificUnit<S, MeasurementType.Area> {
    override val symbol: String = "${unit.symbol}2"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(unit.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(unit.toSIUnit(value))
}

// Metric Volume
@Serializable
object SquareMeter : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Meter)

@Serializable
object SquareDecimeter : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Deci(Meter))

@Serializable
object SquareCentimeter : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Centi(Meter))

@Serializable
object SquareMillimeter : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Milli(Meter))

@Serializable
object SquareMicrometer : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Micro(Meter))

@Serializable
object SquareNanometer : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Nano(Meter))

@Serializable
object SquareDecameter : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Deca(Meter))

@Serializable
object SquareHectometer : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Hecto(Meter))

@Serializable
object Hectare : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Hecto(Meter)) {
    override val symbol: String = "ha"
}

@Serializable
object SquareKilometer : MetricArea(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Kilo(Meter))

@Serializable
object SquareMile : ImperialArea(), ScientificUnit<MeasurementSystem.Imperial, MeasurementType.Area> by Square(Mile) {
    override val symbol: String = "sq. mi"
}

@Serializable
object SquareYard : ImperialArea(), ScientificUnit<MeasurementSystem.Imperial, MeasurementType.Area> by Square(Yard) {
    override val symbol: String = "sq. yd"
}

@Serializable
object SquareFoot : ImperialArea(), ScientificUnit<MeasurementSystem.Imperial, MeasurementType.Area> by Square(Foot) {
    override val symbol: String = "sq. fr"
}

@Serializable
object SquareInch : ImperialArea(), ScientificUnit<MeasurementSystem.Imperial, MeasurementType.Area> by Square(Inch) {
    override val symbol: String = "sq. in"
}

@Serializable
object Acre : ImperialArea() {
    override val symbol: String = "acre"
    val ACRES_IN_SQUARE_MILE = 640.0
    override fun toSIUnit(value: Decimal): Decimal = SquareMile.toSIUnit(value / ACRES_IN_SQUARE_MILE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = SquareMile.fromSIUnit(value) * ACRES_IN_SQUARE_MILE.toDecimal()
}

fun Area<*>.areaFrom(width: Decimal, depth: Decimal, lengthUnit: Length<*>): Decimal {
    val area = lengthUnit.toSIUnit(width) * lengthUnit.toSIUnit(depth)
    return SquareMeter.convert(area, this)
}
