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
 * Set of all [MetricArea]
 */
val MetricAreaUnits: Set<MetricArea> get() = setOf(
    SquareMeter,
    SquareNanometer,
    SquareMicrometer,
    SquareMillimeter,
    SquareCentimeter,
    SquareDecimeter,
    SquareDecameter,
    SquareHectometer,
    SquareKilometer,
    Hectare,
)

/**
 * Set of all [ImperialArea]
 */
val ImperialAreaUnits: Set<ImperialArea> get() = setOf(
    SquareInch,
    SquareFoot,
    SquareYard,
    SquareMile,
    Acre,
)

/**
 * Set of all [Area]
 */
val AreaUnits: Set<Area> get() = MetricAreaUnits + ImperialAreaUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Area]
 * SI unit is `SquareMeter`
 */
@Serializable
sealed class Area : AbstractScientificUnit<PhysicalQuantity.Area>()

/**
 * An [Area] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricArea : Area(), MetricScientificUnit<PhysicalQuantity.Area>

/**
 * An [Area] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialArea : Area(), ImperialScientificUnit<PhysicalQuantity.Area>

internal class Square<S : MeasurementSystem, U : SystemScientificUnit<S, PhysicalQuantity.Length>>(private val unit: U) : SystemScientificUnit<S, PhysicalQuantity.Area> {
    override val symbol: String = "${unit.symbol}²"
    override val system: S = unit.system
    override val quantity = PhysicalQuantity.Area
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(unit.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(unit.toSIUnit(value))
}

// Metric Volume
@Serializable
data object SquareMeter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Meter)

@Serializable
data object SquareDecimeter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Deci(Meter))

@Serializable
data object SquareCentimeter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Centi(Meter))

@Serializable
data object SquareMillimeter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Milli(Meter))

@Serializable
data object SquareMicrometer : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Micro(Meter))

@Serializable
data object SquareNanometer : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Nano(Meter))

@Serializable
data object SquareDecameter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Deca(Meter))

@Serializable
data object SquareHectometer : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Hecto(Meter))

@Serializable
data object Hectare : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Hecto(Meter)) {
    override val symbol: String = "ha"
}

@Serializable
data object SquareKilometer : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Kilo(Meter))

@Serializable
data object SquareMegameter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Mega(Meter))

@Serializable
data object SquareGigameter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Area> by Square(Giga(Meter))

@Serializable
data object SquareMile : ImperialArea(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Area> by Square(Mile) {
    override val symbol: String = "sq. mi"
}

@Serializable
data object SquareYard : ImperialArea(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Area> by Square(Yard) {
    override val symbol: String = "sq. yd"
}

@Serializable
data object SquareFoot : ImperialArea(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Area> by Square(Foot) {
    override val symbol: String = "sq. fr"
}

@Serializable
data object SquareInch : ImperialArea(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Area> by Square(Inch) {
    override val symbol: String = "sq. in"
}

@Serializable
data object Acre : ImperialArea() {
    override val symbol: String = "acre"
    val ACRES_IN_SQUARE_MILE = 640.0
    override val quantity = PhysicalQuantity.Area
    override val system = MeasurementSystem.Imperial
    override fun toSIUnit(value: Decimal): Decimal = SquareMile.toSIUnit(value / ACRES_IN_SQUARE_MILE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = SquareMile.fromSIUnit(value) * ACRES_IN_SQUARE_MILE.toDecimal()
}
