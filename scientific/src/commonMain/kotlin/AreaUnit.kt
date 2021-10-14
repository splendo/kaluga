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
sealed class Area : AbstractScientificUnit<MeasurementType.Area>()

@Serializable
sealed class MetricArea : Area(), MetricScientificUnit<MeasurementType.Area>

@Serializable
sealed class ImperialArea : Area(), CommonImperialScientificUnit<MeasurementType.Area>

class Square<S : MeasurementSystem, U : SystemScientificUnit<S, MeasurementType.Length>>(private val unit : U) : SystemScientificUnit<S, MeasurementType.Area> {
    override val symbol: String = "${unit.symbol}2"
    override val system: S = unit.system
    override val type = MeasurementType.Area
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(unit.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(unit.toSIUnit(value))
}

// Metric Volume
@Serializable
object SquareMeter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Meter)

@Serializable
object SquareDecimeter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Deci(Meter))

@Serializable
object SquareCentimeter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Centi(Meter))

@Serializable
object SquareMillimeter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Milli(Meter))

@Serializable
object SquareMicrometer : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Micro(Meter))

@Serializable
object SquareNanometer : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Nano(Meter))

@Serializable
object SquareDecameter : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Deca(Meter))

@Serializable
object SquareHectometer : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Hecto(Meter))

@Serializable
object Hectare : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Hecto(Meter)) {
    override val symbol: String = "ha"
}

@Serializable
object SquareKilometer : MetricArea(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Area> by Square(Kilo(Meter))

@Serializable
object SquareMile : ImperialArea(), SystemScientificUnit<MeasurementSystem.CommonImperial, MeasurementType.Area> by Square(Mile) {
    override val symbol: String = "sq. mi"
}

@Serializable
object SquareYard : ImperialArea(), SystemScientificUnit<MeasurementSystem.CommonImperial, MeasurementType.Area> by Square(Yard) {
    override val symbol: String = "sq. yd"
}

@Serializable
object SquareFoot : ImperialArea(), SystemScientificUnit<MeasurementSystem.CommonImperial, MeasurementType.Area> by Square(Foot) {
    override val symbol: String = "sq. fr"
}

@Serializable
object SquareInch : ImperialArea(), SystemScientificUnit<MeasurementSystem.CommonImperial, MeasurementType.Area> by Square(Inch) {
    override val symbol: String = "sq. in"
}

@Serializable
object Acre : ImperialArea() {
    override val symbol: String = "acre"
    val ACRES_IN_SQUARE_MILE = 640.0
    override val type = MeasurementType.Area
    override val system = MeasurementSystem.CommonImperial
    override fun toSIUnit(value: Decimal): Decimal = SquareMile.toSIUnit(value / ACRES_IN_SQUARE_MILE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = SquareMile.fromSIUnit(value) * ACRES_IN_SQUARE_MILE.toDecimal()
}

fun <
    LengthUnit : Length,
    WidthUnit : Length,
    AreaUnit : Area
    > AreaUnit.area(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    width: ScientificValue<MeasurementType.Length, WidthUnit>
) : ScientificValue<MeasurementType.Area, AreaUnit> {
        val lengthInMeter = length.convertValue(Meter)
        val widthInMeter = width.convertValue(Meter)
        return ScientificValue(lengthInMeter * widthInMeter, SquareMeter).convert(this)
    }

fun <
    LengthUnit : Length,
    WidthUnit : Length,
    AreaUnit : Area
    > WidthUnit.fromArea(
    area: ScientificValue<MeasurementType.Area, AreaUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) : ScientificValue<MeasurementType.Length, WidthUnit> {
    val areaInMeterSquared = area.convertValue(SquareMeter)
    val lengthInMeter = length.convertValue(Meter)
    return ScientificValue(areaInMeterSquared / lengthInMeter, Meter).convert(this)
}

@JvmName("meterTimesMeter")
operator fun ScientificValue<MeasurementType.Length, Meter>.times(other: ScientificValue<MeasurementType.Length, Meter>) = SquareMeter.area(this, other)
@JvmName("nanometerTimesNanometer")
operator fun ScientificValue<MeasurementType.Length, Nanometer>.times(other: ScientificValue<MeasurementType.Length, Nanometer>) = SquareNanometer.area(this, other)
@JvmName("micrometerTimesMicrometer")
operator fun ScientificValue<MeasurementType.Length, Micrometer>.times(other: ScientificValue<MeasurementType.Length, Micrometer>) = SquareMicrometer.area(this, other)
@JvmName("millimeterTimesMillimeter")
operator fun ScientificValue<MeasurementType.Length, Millimeter>.times(other: ScientificValue<MeasurementType.Length, Millimeter>) = SquareMillimeter.area(this, other)
@JvmName("centimeterTimesCentieter")
operator fun ScientificValue<MeasurementType.Length, Centimeter>.times(other: ScientificValue<MeasurementType.Length, Centimeter>) = SquareCentimeter.area(this, other)
@JvmName("decimeterTimesDecimeter")
operator fun ScientificValue<MeasurementType.Length, Decimeter>.times(other: ScientificValue<MeasurementType.Length, Decimeter>) = SquareDecimeter.area(this, other)
@JvmName("decameterTimesDecameter")
operator fun ScientificValue<MeasurementType.Length, Decameter>.times(other: ScientificValue<MeasurementType.Length, Decameter>) = SquareDecameter.area(this, other)
@JvmName("hectometerTimesHectometer")
operator fun ScientificValue<MeasurementType.Length, Hectometer>.times(other: ScientificValue<MeasurementType.Length, Hectometer>) = SquareHectometer.area(this, other)
@JvmName("kilometerTimesKilometer")
operator fun ScientificValue<MeasurementType.Length, Kilometer>.times(other: ScientificValue<MeasurementType.Length, Kilometer>) = SquareKilometer.area(this, other)
@JvmName("inchTimesInch")
operator fun ScientificValue<MeasurementType.Length, Inch>.times(other: ScientificValue<MeasurementType.Length, Inch>) = SquareInch.area(this, other)
@JvmName("footTimesFoot")
operator fun ScientificValue<MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementType.Length, Foot>) = SquareFoot.area(this, other)
@JvmName("yardTimesYard")
operator fun ScientificValue<MeasurementType.Length, Yard>.times(other: ScientificValue<MeasurementType.Length, Yard>) = SquareYard.area(this, other)
@JvmName("mileTimesMile")
operator fun ScientificValue<MeasurementType.Length, Mile>.times(other: ScientificValue<MeasurementType.Length, Mile>) = SquareMile.area(this, other)

@JvmName("squareMeterDivMeter")
operator fun ScientificValue<MeasurementType.Area, SquareMeter>.div(other: ScientificValue<MeasurementType.Length, Meter>) = Meter.fromArea(this, other)
@JvmName("squareNanoeterDivNanometer")
operator fun ScientificValue<MeasurementType.Area, SquareNanometer>.div(other: ScientificValue<MeasurementType.Length, Nanometer>) = Nanometer.fromArea(this, other)
@JvmName("squareMicrometerDivMicrometer")
operator fun ScientificValue<MeasurementType.Area, SquareMicrometer>.div(other: ScientificValue<MeasurementType.Length, Micrometer>) = Micrometer.fromArea(this, other)
@JvmName("squareMillimeterDivMillimeter")
operator fun ScientificValue<MeasurementType.Area, SquareMillimeter>.div(other: ScientificValue<MeasurementType.Length, Millimeter>) = Millimeter.fromArea(this, other)
@JvmName("squareCentimeterDivCentimeter")
operator fun ScientificValue<MeasurementType.Area, SquareCentimeter>.div(other: ScientificValue<MeasurementType.Length, Centimeter>) = Centimeter.fromArea(this, other)
@JvmName("squareDecimeterDivDecimeter")
operator fun ScientificValue<MeasurementType.Area, SquareDecimeter>.div(other: ScientificValue<MeasurementType.Length, Decimeter>) = Decimeter.fromArea(this, other)
@JvmName("squareDecameterDivDecameter")
operator fun ScientificValue<MeasurementType.Area, SquareDecameter>.div(other: ScientificValue<MeasurementType.Length, Decameter>) = Decameter.fromArea(this, other)
@JvmName("squareHectometerDivHectometer")
operator fun ScientificValue<MeasurementType.Area, SquareHectometer>.div(other: ScientificValue<MeasurementType.Length, Hectometer>) = Hectometer.fromArea(this, other)
@JvmName("squareKilometerDivKilometer")
operator fun ScientificValue<MeasurementType.Area, SquareKilometer>.div(other: ScientificValue<MeasurementType.Length, Kilometer>) = Kilometer.fromArea(this, other)
@JvmName("squareInchDivInch")
operator fun ScientificValue<MeasurementType.Area, SquareInch>.div(other: ScientificValue<MeasurementType.Length, Inch>) = Inch.fromArea(this, other)
@JvmName("squareFootDivFoot")
operator fun ScientificValue<MeasurementType.Area, SquareFoot>.div(other: ScientificValue<MeasurementType.Length, Foot>) = Foot.fromArea(this, other)
@JvmName("squareYardDivYard")
operator fun ScientificValue<MeasurementType.Area, SquareYard>.div(other: ScientificValue<MeasurementType.Length, Yard>) = Yard.fromArea(this, other)
@JvmName("squareMileDivMile")
operator fun ScientificValue<MeasurementType.Area, SquareMile>.div(other: ScientificValue<MeasurementType.Length, Mile>) = Mile.fromArea(this, other)
