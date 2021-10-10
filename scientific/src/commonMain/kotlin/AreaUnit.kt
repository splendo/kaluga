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
import kotlin.jvm.JvmName

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

fun <
    LengthSystem : MeasurementSystem,
    LengthUnit : Length<LengthSystem>,
    WidthSystem : MeasurementSystem,
    WidthUnit : Length<WidthSystem>,
    AreaSystem : MeasurementSystem,
    AreaUnit : Area<AreaSystem>
    > AreaUnit.area(
    length: ScientificValue<LengthSystem, MeasurementType.Length, LengthUnit>,
    width: ScientificValue<WidthSystem, MeasurementType.Length, WidthUnit>
) : ScientificValue<AreaSystem, MeasurementType.Area, AreaUnit> {
        val lengthInMeter = length.convertValue(Meter)
        val widthInMeter = width.convertValue(Meter)
        return ScientificValue(lengthInMeter * widthInMeter, SquareMeter).convert(this)
    }

fun <
    LengthSystem : MeasurementSystem,
    LengthUnit : Length<LengthSystem>,
    WidthSystem : MeasurementSystem,
    WidthUnit : Length<WidthSystem>,
    AreaSystem : MeasurementSystem,
    AreaUnit : Area<AreaSystem>
    > WidthUnit.fromArea(
    area: ScientificValue<AreaSystem, MeasurementType.Area, AreaUnit>,
    length: ScientificValue<LengthSystem, MeasurementType.Length, LengthUnit>
) : ScientificValue<WidthSystem, MeasurementType.Length, WidthUnit> {
    val areaInLengthSquared = area.convertValue(Square(length.unit))
    return ScientificValue(areaInLengthSquared / length.value, length.unit).convert(this)
}

@JvmName("meterTimesMeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>) = SquareMeter.area(this, other)
@JvmName("nanometerTimesNanometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>) = SquareNanometer.area(this, other)
@JvmName("micrometerTimesMicrometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>) = SquareMicrometer.area(this, other)
@JvmName("millimeterTimesMillimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>) = SquareMillimeter.area(this, other)
@JvmName("centimeterTimesCentieter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>) = SquareCentimeter.area(this, other)
@JvmName("decimeterTimesDecimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>) = SquareDecimeter.area(this, other)
@JvmName("decameterTimesDecameter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>) = SquareDecameter.area(this, other)
@JvmName("hectometerTimesHectometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>) = SquareHectometer.area(this, other)
@JvmName("kilometerTimesKilometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>) = SquareKilometer.area(this, other)
@JvmName("inchTimesInch")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>) = SquareInch.area(this, other)
@JvmName("footTimesFoot")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = SquareFoot.area(this, other)
@JvmName("yardTimesYard")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>) = SquareYard.area(this, other)
@JvmName("mileTimesMile")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>) = SquareMile.area(this, other)

@JvmName("squareMeterDivMeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>) = Meter.fromArea(this, other)
@JvmName("squareNanoeterDivNanometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareNanometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>) = Nanometer.fromArea(this, other)
@JvmName("squareMicrometerDivMicrometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMicrometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>) = Micrometer.fromArea(this, other)
@JvmName("squareMillimeterDivMillimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMillimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>) = Millimeter.fromArea(this, other)
@JvmName("squareCentimeterDivCentimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareCentimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>) = Centimeter.fromArea(this, other)
@JvmName("squareDecimeterDivDecimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>) = Decimeter.fromArea(this, other)
@JvmName("squareDecameterDivDecameter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecameter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>) = Decameter.fromArea(this, other)
@JvmName("squareHectometerDivHectometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareHectometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>) = Hectometer.fromArea(this, other)
@JvmName("squareKilometerDivKilometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareKilometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>) = Kilometer.fromArea(this, other)
@JvmName("squareInchDivInch")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareInch>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>) = Inch.fromArea(this, other)
@JvmName("squareFootDivFoot")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = Foot.fromArea(this, other)
@JvmName("squareYardDivYard")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareYard>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>) = Yard.fromArea(this, other)
@JvmName("squareMileDivMile")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareMile>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>) = Mile.fromArea(this, other)
