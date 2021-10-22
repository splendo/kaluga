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
sealed class ImperialPressure  : Pressure(), ImperialScientificUnit<MeasurementType.Pressure> {
    override val system = MeasurementSystem.Imperial
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
object Pascal : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Pressure> {
    override val symbol: String = "P"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}
@Serializable
object NanoPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Nano(Pascal)
@Serializable
object MicroPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Micro(Pascal)
@Serializable
object MilliPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Milli(Pascal)
@Serializable
object CentiPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Centi(Pascal)
@Serializable
object DeciPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Deci(Pascal)
@Serializable
object DecaPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Deca(Pascal)
@Serializable
object HectoPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Hecto(Pascal)
@Serializable
object KiloPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Kilo(Pascal)
@Serializable
object MegaPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Mega(Pascal)
@Serializable
object GigaPascal : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Pascal> by Giga(Pascal)
@Serializable
object Bar : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Pressure> {
    private const val BAR_PER_PASCAL = 0.00001
    override val symbol: String = "bar"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BAR_PER_PASCAL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / BAR_PER_PASCAL.toDecimal()
}
@Serializable
object MilliBar : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Bar> by Milli(Bar)
@Serializable
object CentiBar : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Bar> by Centi(Bar)
@Serializable
object DeciBar : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Bar> by Deci(Bar)
@Serializable
object DecaBar : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Bar> by Deca(Bar)
@Serializable
object HectoBar : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Bar> by Hecto(Bar)
@Serializable
object KiloBar : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Bar> by Kilo(Bar)
@Serializable
object Barye : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Pressure> {
    private const val BARYE_PER_PASCAL = 10
    override val symbol: String = "Ba"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BARYE_PER_PASCAL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / BARYE_PER_PASCAL.toDecimal()
}
@Serializable
object MilliBarye : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> by Milli(Barye)
@Serializable
object CentiBarye : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> by Centi(Barye)
@Serializable
object DeciBarye : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> by Deci(Barye)
@Serializable
object DecaBarye : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> by Deca(Barye)
@Serializable
object HectoBarye : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> by Hecto(Barye)
@Serializable
object KiloBarye : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> by Kilo(Barye)
@Serializable
object Atmosphere : MetricPressure() {
    private const val PASCAL_PER_ATMOSPHERE = 101325
    override val symbol: String = "atm"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_ATMOSPHERE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_ATMOSPHERE.toDecimal()
}
@Serializable
object Torr : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Pressure> {
    private const val TORR_PER_ATMOSPHERE = 760
    override val symbol: String = "Torr"
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = Atmosphere.fromSIUnit(value) * TORR_PER_ATMOSPHERE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Atmosphere.toSIUnit(value / TORR_PER_ATMOSPHERE.toDecimal())
}
@Serializable
object MilliTorr : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure,Torr> by Milli(Torr)
@Serializable
object CentiTorr : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure,Torr> by Centi(Torr)
@Serializable
object DeciTorr : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure,Torr> by Deci(Torr)
@Serializable
object DecaTorr : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure,Torr> by Deca(Torr)
@Serializable
object HectoTorr : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure,Torr> by Hecto(Torr)
@Serializable
object KiloTorr : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure,Torr> by Kilo(Torr)
@Serializable
object MillimeterOfMercury : MetricPressure() {
    private const val PASCAL_PER_MMHG = 133.322387415
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
    private const val PASCAL_PER_MMH2O = 9.80665
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
    private const val POUND_PER_KILOPOUND_SQUARE_INCH = 1000.0
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
) : ScientificValue<MeasurementType.Pressure, PressureUnit> = byDividing(force, area)

@JvmName("newtonDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Newton>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("newtonMultipleDivMetricArea")
operator fun <N : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Newton>, Area : MetricArea> ScientificValue<MeasurementType.Force, N>.div(area: ScientificValue<MeasurementType.Area, Area>) = Pascal.pressure(this, area)
@JvmName("dyneDivMetricArea")
operator fun <Area : MetricArea> ScientificValue<MeasurementType.Force, Dyne>.div(area: ScientificValue<MeasurementType.Area, Area>) = Barye.pressure(this, area)
@JvmName("dyneMultipleDivMetricArea")
operator fun <D : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne>,Area : MetricArea> ScientificValue<MeasurementType.Force, D>.div(area: ScientificValue<MeasurementType.Area, Area>) = Barye.pressure(this, area)
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
@JvmName("poundForceDivImperialArea")
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
@JvmName("usTonForceDivImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Force, UsTonForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = USTonSquareInch.pressure(this, area)
@JvmName("imperialTonForceDivSquareInch")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = ImperialTonSquareInch.pressure(this, area)
@JvmName("imperialTonForceDivSquareFoot")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = ImperialTonSquareFeet.pressure(this, area)
@JvmName("imperialTonForceDivImperialArea")
operator fun <Area : ImperialArea> ScientificValue<MeasurementType.Force, ImperialTonForce>.div(area: ScientificValue<MeasurementType.Area, Area>) = ImperialTonSquareInch.pressure(this, area)
