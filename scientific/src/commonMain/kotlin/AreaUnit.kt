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

val MetricAreaUnits = setOf(
    SquareMeter,
    SquareNanometer,
    SquareMicrometer,
    SquareMillimeter,
    SquareCentimeter,
    SquareDecimeter,
    SquareDecameter,
    SquareHectometer,
    SquareKilometer,
    Hectare
)

val ImperialAreaUnits = setOf(
    SquareInch,
    SquareFoot,
    SquareYard,
    SquareMile,
    Acre
)

val AreaUnits: Set<Area> = MetricAreaUnits + ImperialAreaUnits

@Serializable
sealed class Area : AbstractScientificUnit<MeasurementType.Area>()

@Serializable
sealed class MetricArea : Area(), MetricScientificUnit<MeasurementType.Area>

@Serializable
sealed class ImperialArea : Area(), ImperialScientificUnit<MeasurementType.Area>

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
object SquareMile : ImperialArea(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Area> by Square(Mile) {
    override val symbol: String = "sq. mi"
}

@Serializable
object SquareYard : ImperialArea(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Area> by Square(Yard) {
    override val symbol: String = "sq. yd"
}

@Serializable
object SquareFoot : ImperialArea(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Area> by Square(Foot) {
    override val symbol: String = "sq. fr"
}

@Serializable
object SquareInch : ImperialArea(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Area> by Square(Inch) {
    override val symbol: String = "sq. in"
}

@Serializable
object Acre : ImperialArea() {
    override val symbol: String = "acre"
    val ACRES_IN_SQUARE_MILE = 640.0
    override val type = MeasurementType.Area
    override val system = MeasurementSystem.Imperial
    override fun toSIUnit(value: Decimal): Decimal = SquareMile.toSIUnit(value / ACRES_IN_SQUARE_MILE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = SquareMile.fromSIUnit(value) * ACRES_IN_SQUARE_MILE.toDecimal()
}

@JvmName("areaFromLengthAndWidth")
fun <
    LengthUnit : Length,
    WidthUnit : Length,
    AreaUnit : Area
    > AreaUnit.area(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    width: ScientificValue<MeasurementType.Length, WidthUnit>
) = byMultiplying(length, width)

@JvmName("areaFromForceAndPressure")
fun <
    ForceUnit : ScientificUnit<MeasurementType.Force>,
    AreaUnit : ScientificUnit<MeasurementType.Area>,
    PressureUnit : ScientificUnit<MeasurementType.Pressure>
    > AreaUnit.area(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = byDividing(force, pressure)

@JvmName("areaFromFluxAndInduction")
fun <
    FluxUnit : MagneticFlux,
    AreaUnit : Area,
    InductionUnit : MagneticInduction
    >
    AreaUnit.area(
    flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>,
    induction: ScientificValue<MeasurementType.MagneticInduction, InductionUnit>
) = byDividing(flux, induction)

@JvmName("areaFromFluxAndIlluminance")
fun <
    FluxUnit : LuminousFlux,
    AreaUnit : Area,
    IlluminanceUnit : Illuminance
    >
    AreaUnit.area(
    flux: ScientificValue<MeasurementType.LuminousFlux, FluxUnit>,
    illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>
) = byDividing(flux, illuminance)

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
@JvmName("metricLengthTimesMetricLength")
operator fun <LengthUnit : MetricLength, WidthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(width: ScientificValue<MeasurementType.Length, WidthUnit>) = SquareMeter.area(this, width)
@JvmName("inchTimesInch")
operator fun ScientificValue<MeasurementType.Length, Inch>.times(other: ScientificValue<MeasurementType.Length, Inch>) = SquareInch.area(this, other)
@JvmName("footTimesFoot")
operator fun ScientificValue<MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementType.Length, Foot>) = SquareFoot.area(this, other)
@JvmName("yardTimesYard")
operator fun ScientificValue<MeasurementType.Length, Yard>.times(other: ScientificValue<MeasurementType.Length, Yard>) = SquareYard.area(this, other)
@JvmName("mileTimesMile")
operator fun ScientificValue<MeasurementType.Length, Mile>.times(other: ScientificValue<MeasurementType.Length, Mile>) = SquareMile.area(this, other)
@JvmName("imperialLengthTimesImperialLength")
operator fun <LengthUnit : ImperialLength, WidthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(width: ScientificValue<MeasurementType.Length, WidthUnit>) = SquareFoot.area(this, width)
@JvmName("lengthTimesLength")
operator fun <LengthUnit : Length, WidthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(width: ScientificValue<MeasurementType.Length, WidthUnit>) = SquareMeter.area(this, width)

@JvmName("metricForceDivBarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Barye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceDivBaryeMultiple")
operator fun <Force : MetricForce, B : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye>> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, B>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceDivMetricPressure")
operator fun <Force : MetricForce, Pressure : MetricPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareMeter.area(this, pressure)
@JvmName("imperialForcePoundSquareInch")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivPoundSquareFeet")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareFeet>) = SquareFoot.area(this, pressure)
@JvmName("imperialForceDivKiloPoundSquareInch")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KiloPoundSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivOunceSquareFeet")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, OunceSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivInchOfMercury")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, InchOfMercury>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivInchOfWater")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, InchOfWater>) = SquareInch.area(this, pressure)
@JvmName("imperialForceDivFootOfWater")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, FootOfWater>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceDivKipSquareInch")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KipSquareInch>) = SquareInch.area(this, pressure)
@JvmName("usCustomaryForceDivPoundSquareFeet")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KipSquareFeet>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceDivUSTonSquareInch")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareInch>) = SquareInch.area(this, pressure)
@JvmName("usCustomaryForceDivUSTonSquareFeet")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareFeet>) = SquareFoot.area(this, pressure)
@JvmName("ukImperialForceDivImperialTonSquareInch")
operator fun <Force : UKImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>) = SquareInch.area(this, pressure)
@JvmName("ukImperialForceDivImperialTonSquareFeet")
operator fun <Force : UKImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareFeet>) = SquareFoot.area(this, pressure)
@JvmName("imperialForceDivImperialPressure")
operator fun <Force : ImperialForce, Pressure : ImperialPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("ukImperialForceDivUKImperialPressure")
operator fun <Force : UKImperialForce, Pressure : UKImperialPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceDivUSCustomaryPressure")
operator fun <Force : USCustomaryForce, Pressure : USCustomaryPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareFoot.area(this, pressure)
@JvmName("forceDivPressure")
operator fun <ForceUnit : Force, PressureUnit : Pressure> ScientificValue<MeasurementType.Force, ForceUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = SquareMeter.area(this, pressure)

@JvmName("fluxDivInduction")
infix operator fun <FluxUnit : MagneticFlux, InductionUnit : MagneticInduction> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(induction: ScientificValue<MeasurementType.MagneticInduction, InductionUnit>) = SquareMeter.area(this, induction)
@JvmName("fluxDivPhot")
infix operator fun <FluxUnit : LuminousFlux> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(phot: ScientificValue<MeasurementType.Illuminance, Phot>) = SquareCentimeter.area(this, phot)
@JvmName("fluxDivPhotMultiple")
infix operator fun <FluxUnit : LuminousFlux, PhotUnit> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(phot: ScientificValue<MeasurementType.Illuminance, PhotUnit>) where PhotUnit : Illuminance, PhotUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> = SquareCentimeter.area(this, phot)
@JvmName("fluxDivMetricIlluminance")
infix operator fun <FluxUnit : LuminousFlux, IlluminanceUnit : MetricIlluminance> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = SquareMeter.area(this, illuminance)
@JvmName("fluxDivImperialIlluminance")
infix operator fun <FluxUnit : LuminousFlux, IlluminanceUnit : ImperialIlluminance> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = SquareFoot.area(this, illuminance)
@JvmName("fluxDivIlluminance")
infix operator fun <FluxUnit : LuminousFlux, IlluminanceUnit : Illuminance> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = SquareMeter.area(this, illuminance)
