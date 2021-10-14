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
sealed class Pressure : AbstractScientificUnit<MeasurementType.Pressure>()

@Serializable
sealed class MetricPressure : Pressure(), MetricScientificUnit<MeasurementType.Pressure>

@Serializable
sealed class ImperialPressure  : Pressure(), CommonImperialScientificUnit<MeasurementType.Pressure> {
    override val system = MeasurementSystem.CommonImperial
    override val type = MeasurementType.Pressure
}

@Serializable
sealed class UKImperialPressure  : Pressure(), UKImperialScientificUnit<MeasurementType.Pressure> {
    override val system = MeasurementSystem.UKImperial
    override val type = MeasurementType.Pressure
}

@Serializable
sealed class USCustomaryPressure  : Pressure(), USCustomaryScientificUnit<MeasurementType.Pressure> {
    override val system = MeasurementSystem.USCustomary
    override val type = MeasurementType.Pressure
}

@Serializable
object Pascal : MetricPressure(), BaseMetricUnit<MeasurementType.Pressure, MeasurementSystem.Metric> {
    override val symbol: String = "P"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}
@Serializable
object NanoPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Nano(Pascal)
@Serializable
object MicroPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Micro(Pascal)
@Serializable
object MilliPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Milli(Pascal)
@Serializable
object CentiPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Centi(Pascal)
@Serializable
object DeciPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deci(Pascal)
@Serializable
object DecaPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deca(Pascal)
@Serializable
object HectoPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Hecto(Pascal)
@Serializable
object KiloPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Kilo(Pascal)
@Serializable
object MegaPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Mega(Pascal)
@Serializable
object GigaPascal : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Giga(Pascal)
@Serializable
object Bar : MetricPressure(), BaseMetricUnit<MeasurementType.Pressure, MeasurementSystem.Metric> {
    const val BAR_PER_PASCAL = 0.00001
    override val symbol: String = "bar"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BAR_PER_PASCAL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / BAR_PER_PASCAL.toDecimal()
}
@Serializable
object MilliBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Milli(Bar)
@Serializable
object CentiBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Centi(Bar)
@Serializable
object DeciBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deci(Bar)
@Serializable
object DecaBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deca(Bar)
@Serializable
object HectoBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Hecto(Bar)
@Serializable
object KiloBar : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Kilo(Bar)
@Serializable
object Barye : MetricPressure(), BaseMetricUnit<MeasurementType.Pressure, MeasurementSystem.Metric> {
    const val BARYE_PER_PASCAL = 10
    override val symbol: String = "Ba"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BARYE_PER_PASCAL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / BARYE_PER_PASCAL.toDecimal()
}
@Serializable
object MilliBarye : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Milli(Barye)
@Serializable
object CentiBarye : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Centi(Barye)
@Serializable
object DeciBarye : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deci(Barye)
@Serializable
object DecaBarye : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deca(Barye)
@Serializable
object HectoBarye : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Hecto(Barye)
@Serializable
object KiloBarye : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Kilo(Barye)
@Serializable
object Atmosphere : MetricPressure() {
    const val PASCAL_PER_ATMOSPHERE = 101325
    override val symbol: String = "atm"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_ATMOSPHERE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_ATMOSPHERE.toDecimal()
}
@Serializable
object Torr : MetricPressure(), BaseMetricUnit<MeasurementType.Pressure, MeasurementSystem.Metric> {
    const val TORR_PER_ATMOSPHERE = 760
    override val symbol: String = "Torr"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = Atmosphere.fromSIUnit(value) * TORR_PER_ATMOSPHERE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Atmosphere.toSIUnit(value / TORR_PER_ATMOSPHERE.toDecimal())
}
@Serializable
object MilliTorr : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Milli(Torr)
@Serializable
object CentiTorr : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Centi(Torr)
@Serializable
object DeciTorr : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deci(Torr)
@Serializable
object DecaTorr : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Deca(Torr)
@Serializable
object HectoTorr : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Hecto(Torr)
@Serializable
object KiloTorr : MetricPressure(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Pressure> by Kilo(Torr)
@Serializable
object MillimeterOfMercury : MetricPressure() {
    const val PASCAL_PER_MMHG = 133.322387415
    override val symbol: String = "mmHg"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_MMHG.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_MMHG.toDecimal()
}
@Serializable
object CentimeterOfWater : MetricPressure() {
    override val symbol: String = "cmH2O"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) * 10.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value / 10.0.toDecimal())
}
@Serializable
object MillimeterOfWater : MetricPressure() {
    const val PASCAL_PER_MMH2O = 9.80665
    override val symbol: String = "mmH2O"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_MMH2O.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_MMH2O.toDecimal()
}

@Serializable
object PoundSquareInch : ImperialPressure() {
    override val symbol: String = "psi"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
object PoundSquareFeet : ImperialPressure() {
    override val symbol: String = "${PoundForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / SquareFoot.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * SquareFoot.fromSIUnit(1.toDecimal()))
}

@Serializable
object OunceSquareInch : ImperialPressure() {
    override val symbol: String = "${OunceForce.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = OunceForce.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = OunceForce.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
object KiloPoundSquareInch : ImperialPressure() {
    const val POUND_PER_KILOPOUND_SQUARE_INCH = 1000.0
    override val symbol: String = "ksi"
    override fun fromSIUnit(value: Decimal): Decimal = PoundSquareInch.fromSIUnit(value) / POUND_PER_KILOPOUND_SQUARE_INCH.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = PoundSquareInch.toSIUnit(value * POUND_PER_KILOPOUND_SQUARE_INCH.toDecimal())
}
@Serializable
object InchOfMercury : ImperialPressure() {
    override val symbol: String = "inHg"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfMercury.fromSIUnit(value) * 1(Millimeter).convertValue(Inch)
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfMercury.toSIUnit(value / 1(Millimeter).convertValue(Inch))
}
@Serializable
object InchOfWater : ImperialPressure() {
    override val symbol: String = "inH2O"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) * 1(Millimeter).convertValue(Inch)
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value / 1(Millimeter).convertValue(Inch))
}
@Serializable
object FootOfWater : ImperialPressure() {
    override val symbol: String = "ftH2O"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) * 1(Millimeter).convertValue(Foot)
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value / 1(Millimeter).convertValue(Foot))
}
@Serializable
object KipSquareInch : USCustomaryPressure() {
    override val symbol: String = "${Kip.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = Kip.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = Kip.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
object KipSquareFeet : USCustomaryPressure() {
    override val symbol: String = "${Kip.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = Kip.fromSIUnit(value) / SquareFoot.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = Kip.toSIUnit(value * SquareFoot.fromSIUnit(1.toDecimal()))
}
@Serializable
object USTonSquareInch : USCustomaryPressure() {
    override val symbol: String = "${UsTonForce.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = UsTonForce.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = UsTonForce.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
object USTonSquareFeet : USCustomaryPressure() {
    override val symbol: String = "${UsTonForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = UsTonForce.fromSIUnit(value) / SquareFoot.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = UsTonForce.toSIUnit(value * SquareFoot.fromSIUnit(1.toDecimal()))
}
@Serializable
object ImperialTonSquareInch : UKImperialPressure() {
    override val symbol: String = "${ImperialTonForce.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTonForce.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = ImperialTonForce.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
object ImperialTonSquareFeet : UKImperialPressure() {
    override val symbol: String = "${ImperialTonForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTonForce.fromSIUnit(value) / SquareFoot.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = ImperialTonForce.toSIUnit(value * SquareFoot.fromSIUnit(1.toDecimal()))
}

fun <
    ForceUnit : ScientificUnit<MeasurementType.Force>,
    AreaUnit : ScientificUnit<MeasurementType.Area>,
    PressureUnit : ScientificUnit<MeasurementType.Pressure>
    > PressureUnit.pressure(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) : ScientificValue<MeasurementType.Pressure, PressureUnit> {
    val forceValue = force.convertValue(Newton)
    val areaValue = area.convertValue(SquareMeter)
    return (forceValue / areaValue)(Pascal).convert(this)
}

fun <
    ForceUnit : ScientificUnit<MeasurementType.Force>,
    AreaUnit : ScientificUnit<MeasurementType.Area>,
    PressureUnit : ScientificUnit<MeasurementType.Pressure>
    > ForceUnit.force(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) : ScientificValue<MeasurementType.Force, ForceUnit> {
    val pressureValue = pressure.convertValue(Pascal)
    val areaValue = area.convertValue(SquareMeter)
    return (pressureValue * areaValue)(Newton).convert(this)
}

fun <
    ForceUnit : ScientificUnit<MeasurementType.Force>,
    AreaUnit : ScientificUnit<MeasurementType.Area>,
    PressureUnit : ScientificUnit<MeasurementType.Pressure>
    > AreaUnit.area(
    force: ScientificValue<MeasurementType.Force, ForceUnit>,
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) : ScientificValue<MeasurementType.Area, AreaUnit> {
    val forceValue = force.convertValue(Newton)
    val pressureValue = pressure.convertValue(Pascal)
    return (forceValue / pressureValue)(SquareMeter).convert(this)
}

@JvmName("newtonDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Newton>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("gigaNewtonDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Giganewton>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("megaNewtonDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Meganewton>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("kiloNewtonDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Kilonewton>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("milliNewtonDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Millinewton>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("microNewtonDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Micronewton>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("dyneDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Dyne>.div(area: ScientificValue<MeasurementType.Area, Area>) = Barye.pressure(this, area)
@JvmName("milliDyneDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Millidyne>.div(area: ScientificValue<MeasurementType.Area, Area>) = Barye.pressure(this, area)
@JvmName("kiloDyneDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Kilodyne>.div(area: ScientificValue<MeasurementType.Area, Area>) = Barye.pressure(this, area)
@JvmName("megaDyneDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Megadyne>.div(area: ScientificValue<MeasurementType.Area, Area>) = Barye.pressure(this, area)
@JvmName("kilogramForceDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, KilogramForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("gramForceDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, GramForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("milligramForceDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, MilligramForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("tonneForceDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, TonneForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("poundalDivSquareInch")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = PoundSquareInch.pressure(this, area)
@JvmName("poundalDivSquareFoot")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = PoundSquareFeet.pressure(this, area)
@JvmName("poundForceDivSquareInch")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = PoundSquareInch.pressure(this, area)
@JvmName("poundForceDivSquareFoot")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = PoundSquareFeet.pressure(this, area)
@JvmName("poundForceDivSquareFoot")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Force, PoundForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = PoundSquareInch.pressure(this, area)
@JvmName("ounceForceDivImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Force, OunceForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = OunceSquareInch.pressure(this, area)
@JvmName("grainForceDivImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Force, GrainForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = OunceSquareInch.pressure(this, area)
@JvmName("kipDivSquareInch")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = KipSquareInch.pressure(this, area)
@JvmName("kipDivSquareFoot")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = KipSquareFeet.pressure(this, area)
@JvmName("kipDivImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Force, Kip>.div(area: ScientificValue<MeasurementType.Area, Area>) = KipSquareInch.pressure(this, area)
@JvmName("usTonForceDivSquareInch")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = USTonSquareInch.pressure(this, area)
@JvmName("usTonForceDivSquareFoot")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = USTonSquareFeet.pressure(this, area)
@JvmName("usTonForceDivSquareFoot")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Force, UsTonForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = USTonSquareInch.pressure(this, area)
@JvmName("imperialTonForceDivSquareInch")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = ImperialTonSquareInch.pressure(this, area)
@JvmName("imperialTonForceDivSquareFoot")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = ImperialTonSquareFeet.pressure(this, area)
@JvmName("imperialTonForceDivSquareFoot")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Force, ImperialTonForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = ImperialTonSquareInch.pressure(this, area)

@JvmName("baryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, Barye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("millibaryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, MilliBarye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("centibaryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, CentiBarye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("decibaryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, DeciBarye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("decabaryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, DecaBarye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("hectobaryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, HectoBarye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("kilobaryeTimesMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Pressure, KiloBarye>.times(area: ScientificValue<MeasurementType.Area, Area>) = Dyne.force(this, area)
@JvmName("metricPressureTimesMetricArea")
operator fun <Pressure : MetricPressure, Area : MetricArea> ScientificValue<MeasurementType.Pressure, Pressure>.times(area: ScientificValue<MeasurementType.Area, Area>) = Newton.force(this, area)
@JvmName("poundSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, PoundSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("poundSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, PoundSquareFeet>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("ounceSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, OunceSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = OunceForce.force(this, area)
@JvmName("kilopoundSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KiloPoundSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("inchMercuryTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, InchOfMercury>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("inchWaterTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, InchOfWater>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("feetOfWaterTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, FootOfWater>.times(area: ScientificValue<MeasurementType.Area, Area>) = PoundForce.force(this, area)
@JvmName("kipSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KipSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = Kip.force(this, area)
@JvmName("kipSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, KipSquareFeet>.times(area: ScientificValue<MeasurementType.Area, Area>) = Kip.force(this, area)
@JvmName("usTonSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, USTonSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = UsTonForce.force(this, area)
@JvmName("usTonSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, USTonSquareFeet>.times(area: ScientificValue<MeasurementType.Area, Area>) = UsTonForce.force(this, area)
@JvmName("imperialTonSquareInchTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>.times(area: ScientificValue<MeasurementType.Area, Area>) = ImperialTonForce.force(this, area)
@JvmName("imperialTonSquareFeetTimesImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Pressure, ImperialTonSquareFeet>.times(area: ScientificValue<MeasurementType.Area, Area>) = ImperialTonForce.force(this, area)

@JvmName("metricForceDivBarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Barye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceMillibarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, MilliBarye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceCentibarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, CentiBarye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceDecibarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, DeciBarye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceDecabarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, DecaBarye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceHectobarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, HectoBarye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceKilobarye")
operator fun <Force : MetricForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KiloBarye>) = SquareCentimeter.area(this, pressure)
@JvmName("metricForceDivMetricPressure")
operator fun <Force : MetricForce, Pressure : MetricPressure> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, Pressure>) = SquareMeter.area(this, pressure)
@JvmName("imperialForcePoundSquareInch")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForcePoundSquareFeet")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareFeet>) = SquareFoot.area(this, pressure)
@JvmName("imperialForceKiloPoundSquareInch")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KiloPoundSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForceOunceSquareFeet")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, OunceSquareInch>) = SquareInch.area(this, pressure)
@JvmName("imperialForceInchOfMercury")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, InchOfMercury>) = SquareInch.area(this, pressure)
@JvmName("imperialForceInchOfWater")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, InchOfWater>) = SquareInch.area(this, pressure)
@JvmName("imperialForceFootOfWater")
operator fun <Force : ImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, FootOfWater>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceKipSquareInch")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KipSquareInch>) = SquareInch.area(this, pressure)
@JvmName("usCustomaryForcePoundSquareFeet")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, KipSquareFeet>) = SquareFoot.area(this, pressure)
@JvmName("usCustomaryForceUSTonSquareInch")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareInch>) = SquareInch.area(this, pressure)
@JvmName("usCustomaryForceUSTonSquareFeet")
operator fun <Force : USCustomaryForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareFeet>) = SquareFoot.area(this, pressure)
@JvmName("ukImperialForceImperialTonSquareInch")
operator fun <Force : UKImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>) = SquareInch.area(this, pressure)
@JvmName("ukImperialForceImperialTonSquareFeet")
operator fun <Force : UKImperialForce> ScientificValue<MeasurementType.Force, Force>.div(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareFeet>) = SquareFoot.area(this, pressure)
