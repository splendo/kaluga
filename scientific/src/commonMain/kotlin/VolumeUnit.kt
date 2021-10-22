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
sealed class Volume : AbstractScientificUnit<MeasurementType.Volume>()

@Serializable
sealed class MetricVolume : Volume(), MetricScientificUnit<MeasurementType.Volume>

@Serializable
sealed class USCustomaryVolume : Volume(), USCustomaryScientificUnit<MeasurementType.Volume> {
    override val type = MeasurementType.Volume
    override val system = MeasurementSystem.USCustomary
}

@Serializable
sealed class UKImperialVolume : Volume(), UKImperialScientificUnit<MeasurementType.Volume> {
    override val type = MeasurementType.Volume
    override val system = MeasurementSystem.UKImperial
}

@Serializable
sealed class ImperialVolume : Volume(), ImperialScientificUnit<MeasurementType.Volume>

class Cubic<S : MeasurementSystem, U : SystemScientificUnit<S, MeasurementType.Length>>(private val unit : U) : SystemScientificUnit<S, MeasurementType.Volume> {
    override val symbol: String = "${unit.symbol}3"
    override val system: S = unit.system
    override val type = MeasurementType.Volume
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(unit.fromSIUnit(unit.fromSIUnit(value)))
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(unit.toSIUnit(unit.toSIUnit(value)))
}

// Metric Volume
@Serializable
object CubicMeter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Meter)

@Serializable
object CubicDecimeter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Deci(Meter))

@Serializable
object CubicCentimeter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Centi(Meter)) {
    override val symbol: String = "cc"
}

@Serializable
object CubicMillimeter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Milli(Meter))

@Serializable
object CubicMicrometer : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Micro(Meter))

@Serializable
object CubicNanometer : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Nano(Meter))

@Serializable
object CubicDecameter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Deca(Meter))

@Serializable
object CubicHectometer : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Hecto(Meter))

@Serializable
object CubicKilometer : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Kilo(Meter))

@Serializable
object Liter : MetricVolume(), MetricBaseUnit<MeasurementSystem.Metric, MeasurementType.Volume> {
    override val symbol: String = "l"
    private const val LITERS_IN_CUBIC_METER = 1000.0
    override val system = MeasurementSystem.Metric
    override val type = MeasurementType.Volume
    override fun toSIUnit(value: Decimal): Decimal = value / LITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * LITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Deciliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Volume, Liter> by Deci(Liter)

@Serializable
object Centiliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Volume, Liter> by Centi(Liter)

@Serializable
object Milliliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Volume, Liter> by Milli(Liter)

@Serializable
object Microliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Volume, Liter> by Micro(Liter)

@Serializable
object Nanoliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Volume, Liter> by Nano(Liter)

@Serializable
object Decaliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Volume, Liter> by Deca(Liter)

@Serializable
object Hectoliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Volume, Liter> by Hecto(Liter)

@Serializable
object Kiloliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Volume, Liter> by Kilo(Liter)

// Imperial
@Serializable
object CubicInch : ImperialVolume(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Volume> by Cubic(Inch) {
    override val symbol: String = "cu in"
}

@Serializable
object CubicFoot : ImperialVolume(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Volume> by Cubic(Foot) {
    override val symbol: String = "cu ft"
}

@Serializable
object CubicYard : ImperialVolume(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Volume> by Cubic(Yard) {
    override val symbol: String = "cu yd"
}

@Serializable
object CubicMile : ImperialVolume(), SystemScientificUnit<MeasurementSystem.Imperial, MeasurementType.Volume> by Cubic(Mile) {
    override val symbol: String = "cu mi"
}

// US Imperial

@Serializable
data class USCustomaryImperialVolumeWrapper(val imperial: ImperialVolume) : USCustomaryVolume() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

@Serializable
object AcreFoot : USCustomaryVolume() {
    override val symbol: String = "ac ft"
    override fun toSIUnit(value: Decimal): Decimal = Foot.toSIUnit(Acre.toSIUnit(value))
    override fun fromSIUnit(value: Decimal): Decimal = Acre.fromSIUnit(Foot.fromSIUnit(value))
}

@Serializable
object UsFluidDram : USCustomaryVolume() {
    override val symbol: String = "fl dr"
    private const val US_DRAMS_IN_FLUID_OUNCE = 8
    override fun toSIUnit(value: Decimal): Decimal = UsFluidOunce.toSIUnit(value / US_DRAMS_IN_FLUID_OUNCE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsFluidOunce.fromSIUnit(value) * US_DRAMS_IN_FLUID_OUNCE.toDecimal()
}

@Serializable
object UsFluidOunce : USCustomaryVolume() {
    override val symbol: String = "fl oz"
    private const val US_FLUID_OUNCES_IN_GALLON = 128
    override fun toSIUnit(value: Decimal): Decimal = UsLiquidGallon.toSIUnit(value / US_FLUID_OUNCES_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) * US_FLUID_OUNCES_IN_GALLON.toDecimal()
}

@Serializable
object UsCustomaryCup : USCustomaryVolume() {
    override val symbol: String = "cup"
    private const val US_LEGAL_CUPS_IN_GALLON = 16
    override fun toSIUnit(value: Decimal): Decimal = UsLiquidGallon.toSIUnit(value / US_LEGAL_CUPS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) * US_LEGAL_CUPS_IN_GALLON.toDecimal()
}

@Serializable
object UsLegalCup : USCustomaryVolume() {
    override val symbol: String = "cup"
    private const val MILLILITERS_IN_CUP = 240
    override fun toSIUnit(value: Decimal): Decimal = Milliliter.toSIUnit(value * MILLILITERS_IN_CUP.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) / MILLILITERS_IN_CUP.toDecimal()
}

@Serializable
object UsLiquidPint : USCustomaryVolume() {
    override val symbol: String = "pint"
    private const val US_PINTS_IN_GALLON = 8
    override fun toSIUnit(value: Decimal): Decimal = UsLiquidGallon.toSIUnit(value / US_PINTS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) * US_PINTS_IN_GALLON.toDecimal()
}

@Serializable
object UsLiquidQuart : USCustomaryVolume() {
    override val symbol: String = "qt"
    private const val US_QUARTS_IN_GALLON = 4
    override fun toSIUnit(value: Decimal): Decimal = UsLiquidGallon.toSIUnit(value / US_QUARTS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) * US_QUARTS_IN_GALLON.toDecimal()
}

@Serializable
object UsLiquidGallon : USCustomaryVolume() {
    override val symbol: String = "gal"
    private const val CUBIC_INCH_IN_GALLON = 231
    override fun toSIUnit(value: Decimal): Decimal = CubicInch.toSIUnit(value * CUBIC_INCH_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = CubicInch.fromSIUnit(value) / CUBIC_INCH_IN_GALLON.toDecimal()
}

// UK Imperial
@Serializable
data class UKImperialImperialVolumeWrapper(val imperial: ImperialVolume) : UKImperialVolume() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

@Serializable
object ImperialFluidDram : UKImperialVolume() {
    override val symbol: String = "fl dr"
    private const val IMPERIAL_FLUID_DRAM_IN_FLUID_OUNCE = 8
    override fun toSIUnit(value: Decimal): Decimal = ImperialFluidOunce.toSIUnit(value / IMPERIAL_FLUID_DRAM_IN_FLUID_OUNCE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = ImperialFluidOunce.fromSIUnit(value) * IMPERIAL_FLUID_DRAM_IN_FLUID_OUNCE.toDecimal()
}

@Serializable
object ImperialFluidOunce : UKImperialVolume() {
    override val symbol: String = "fl oz"
    private const val IMPERIAL_FLUID_OUNCES_GALLON = 160
    override fun toSIUnit(value: Decimal): Decimal = ImperialGallon.toSIUnit(value / IMPERIAL_FLUID_OUNCES_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = ImperialGallon.fromSIUnit(value) * IMPERIAL_FLUID_OUNCES_GALLON.toDecimal()
}

@Serializable
object MetricCup : UKImperialVolume() {
    override val symbol: String = "cup"
    private const val MILLILITER_IN_CUP = 250
    override fun toSIUnit(value: Decimal): Decimal = Milliliter.toSIUnit(value * MILLILITER_IN_CUP.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Milliliter.fromSIUnit(value) / MILLILITER_IN_CUP.toDecimal()
}

@Serializable
object ImperialPint : UKImperialVolume() {
    override val symbol: String = "pt"
    private const val IMPERIAL_PINTS_IN_GALLON = 8
    override fun toSIUnit(value: Decimal): Decimal = ImperialGallon.toSIUnit(value / IMPERIAL_PINTS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = ImperialGallon.fromSIUnit(value) * IMPERIAL_PINTS_IN_GALLON.toDecimal()
}

@Serializable
object ImperialQuart : UKImperialVolume() {
    override val symbol: String = "qt"
    private const val IMPERIAL_QUARTS_IN_GALLON = 4
    override fun toSIUnit(value: Decimal): Decimal = ImperialGallon.toSIUnit(value / IMPERIAL_QUARTS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = ImperialGallon.fromSIUnit(value) * IMPERIAL_QUARTS_IN_GALLON.toDecimal()
}

@Serializable
object ImperialGallon : UKImperialVolume() {
    override val symbol: String = "gal"
    private const val LITER_PER_GALLON = 4.54609
    override fun toSIUnit(value: Decimal): Decimal = Liter.toSIUnit(value * LITER_PER_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Liter.fromSIUnit(value) / LITER_PER_GALLON.toDecimal()
}

fun <
    HeightUnit : Length,
    AreaUnit : Area,
    VolumeUnit : Volume
    > VolumeUnit.volume(
    area: ScientificValue<MeasurementType.Area, AreaUnit>,
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) : ScientificValue<MeasurementType.Volume, VolumeUnit> = byMultiplying(area, height)

fun <
    LengthUnit : Length,
    WidthUnit : Length,
    HeightUnit : Length,
    VolumeUnit : Volume
    > VolumeUnit.volume(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    width: ScientificValue<MeasurementType.Length, WidthUnit>,
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) : ScientificValue<MeasurementType.Volume, VolumeUnit> = volume(SquareMeter.area(length, width), height)

fun <
    HeightUnit : Length,
    AreaUnit : Area,
    VolumeUnit : Volume
    > HeightUnit.fromVolume(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) : ScientificValue<MeasurementType.Length, HeightUnit> = byDividing(volume, area)

fun <
    HeightUnit : Length,
    LengthUnit : Length,
    WidthUnit : Length,
    VolumeUnit : Volume
    > HeightUnit.fromVolume(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    width: ScientificValue<MeasurementType.Length, WidthUnit>,
) : ScientificValue<MeasurementType.Length, HeightUnit> = fromVolume(volume, SquareMeter.area(length, width))

fun <
    HeightUnit : Length,
    AreaUnit : Area,
    VolumeUnit : Volume
    > AreaUnit.fromVolume(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) : ScientificValue<MeasurementType.Area, AreaUnit> = byDividing(volume, height)

@JvmName("meterTimesSquareMeter")
operator fun ScientificValue<MeasurementType.Length, Meter>.times(other: ScientificValue<MeasurementType.Area, SquareMeter>) = CubicMeter.volume(other, this)
@JvmName("squareMeterTimesMeter")
operator fun ScientificValue<MeasurementType.Area, SquareMeter>.times(other: ScientificValue<MeasurementType.Length, Meter>) = other * this
@JvmName("nanometerTimesSquareNanometer")
operator fun ScientificValue<MeasurementType.Length, Nanometer>.times(other: ScientificValue<MeasurementType.Area, SquareNanometer>) = CubicNanometer.volume(other, this)
@JvmName("squareNanometerTimesNanometer")
operator fun ScientificValue<MeasurementType.Area, SquareNanometer>.times(other: ScientificValue<MeasurementType.Length, Nanometer>) = other * this
@JvmName("micrometerTimesSquareMicrometer")
operator fun ScientificValue<MeasurementType.Length, Micrometer>.times(other: ScientificValue<MeasurementType.Area, SquareMicrometer>) = CubicMicrometer.volume(other, this)
@JvmName("squareMicrometerTimesMicrometer")
operator fun ScientificValue<MeasurementType.Area, SquareMicrometer>.times(other: ScientificValue<MeasurementType.Length, Micrometer>) = other * this
@JvmName("millimeterTimesSquareMillimeter")
operator fun ScientificValue<MeasurementType.Length, Millimeter>.times(other: ScientificValue<MeasurementType.Area, SquareMillimeter>) = CubicMillimeter.volume(other, this)
@JvmName("squareMillimeterTimesMillimeter")
operator fun ScientificValue<MeasurementType.Area, SquareMillimeter>.times(other: ScientificValue<MeasurementType.Length, Millimeter>) = other * this
@JvmName("centimeterTimesSquareCentimeter")
operator fun ScientificValue<MeasurementType.Length, Centimeter>.times(other: ScientificValue<MeasurementType.Area, SquareCentimeter>) = CubicCentimeter.volume(other, this)
@JvmName("squareCentimeterTimesCentimeter")
operator fun ScientificValue<MeasurementType.Area, SquareCentimeter>.times(other: ScientificValue<MeasurementType.Length, Centimeter>) = other * this
@JvmName("decimeterTimesSquareDecimeter")
operator fun ScientificValue<MeasurementType.Length, Decimeter>.times(other: ScientificValue<MeasurementType.Area, SquareDecimeter>) = CubicDecimeter.volume(other, this)
@JvmName("squareDecimeterTimesDecimeter")
operator fun ScientificValue<MeasurementType.Area, SquareDecimeter>.times(other: ScientificValue<MeasurementType.Length, Decimeter>) = other * this
@JvmName("decameterTimesSquareDecameter")
operator fun ScientificValue<MeasurementType.Length, Decameter>.times(other: ScientificValue<MeasurementType.Area, SquareDecameter>) = CubicDecameter.volume(other, this)
@JvmName("squareDecameterTimesDecameter")
operator fun ScientificValue<MeasurementType.Area, SquareDecameter>.times(other: ScientificValue<MeasurementType.Length, Decameter>) = other * this
@JvmName("hectometerTimesSquarehectometer")
operator fun ScientificValue<MeasurementType.Length, Hectometer>.times(other: ScientificValue<MeasurementType.Area, SquareHectometer>) = CubicHectometer.volume(other, this)
@JvmName("squareHectometerTimesHectometer")
operator fun ScientificValue<MeasurementType.Area, SquareHectometer>.times(other: ScientificValue<MeasurementType.Length, Hectometer>) = other * this
@JvmName("kilometerTimesSquareKilometer")
operator fun ScientificValue<MeasurementType.Length, Kilometer>.times(other: ScientificValue<MeasurementType.Area, SquareKilometer>) = CubicKilometer.volume(other, this)
@JvmName("squareKilometerTimesKilometer")
operator fun ScientificValue<MeasurementType.Area, SquareKilometer>.times(other: ScientificValue<MeasurementType.Length, Kilometer>) = other * this
@JvmName("inchTimesSquareInch")
operator fun ScientificValue<MeasurementType.Length, Inch>.times(other: ScientificValue<MeasurementType.Area, SquareInch>) = CubicInch.volume(other, this)
@JvmName("squareInchTimesInch")
operator fun ScientificValue<MeasurementType.Area, SquareInch>.times(other: ScientificValue<MeasurementType.Length, Inch>) = other * this
@JvmName("footTimesSquareFoot")
operator fun ScientificValue<MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementType.Area, SquareFoot>) = CubicFoot.volume(other, this)
@JvmName("squareFootTimesFoot")
operator fun ScientificValue<MeasurementType.Area, SquareFoot>.times(other: ScientificValue<MeasurementType.Length, Foot>) = other * this
@JvmName("yardTimesSquareYard")
operator fun ScientificValue<MeasurementType.Length, Yard>.times(other: ScientificValue<MeasurementType.Area, SquareYard>) = CubicYard.volume(other, this)
@JvmName("squareYardTimesYard")
operator fun ScientificValue<MeasurementType.Area, SquareYard>.times(other: ScientificValue<MeasurementType.Length, Yard>) = other * this
@JvmName("mileTimesSquareMile")
operator fun ScientificValue<MeasurementType.Length, Mile>.times(other: ScientificValue<MeasurementType.Area, SquareMile>) = CubicMile.volume(other, this)
@JvmName("squareMileTimesMile")
operator fun ScientificValue<MeasurementType.Area, SquareMile>.times(other: ScientificValue<MeasurementType.Length, Mile>) = other * this
@JvmName("footTimesAcre")
operator fun ScientificValue<MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementType.Area, Acre>) = AcreFoot.volume(other, this)
@JvmName("acreTimesFoot")
operator fun ScientificValue<MeasurementType.Area, Acre>.times(other: ScientificValue<MeasurementType.Length, Foot>) = other * this

@JvmName("cubicMeterDivSquareMeter")
operator fun ScientificValue<MeasurementType.Volume, CubicMeter>.div(other: ScientificValue<MeasurementType.Area, SquareMeter>) = Meter.fromVolume(this, other)
@JvmName("cubicMeterDivMeter")
operator fun ScientificValue<MeasurementType.Volume, CubicMeter>.div(other: ScientificValue<MeasurementType.Length, Meter>) = SquareMeter.fromVolume(this, other)
@JvmName("cubicNanometerDivSquareNanometer")
operator fun ScientificValue<MeasurementType.Volume, CubicNanometer>.div(other: ScientificValue<MeasurementType.Area, SquareNanometer>) = Nanometer.fromVolume(this, other)
@JvmName("cubicNanometerDivNanometer")
operator fun ScientificValue<MeasurementType.Volume, CubicNanometer>.div(other: ScientificValue<MeasurementType.Length, Nanometer>) = SquareNanometer.fromVolume(this, other)
@JvmName("cubicMicrometerDivSquareMicrometer")
operator fun ScientificValue<MeasurementType.Volume, CubicMicrometer>.div(other: ScientificValue<MeasurementType.Area, SquareMicrometer>) = Micrometer.fromVolume(this, other)
@JvmName("cubicMicrometerDivMicrometer")
operator fun ScientificValue<MeasurementType.Volume, CubicMicrometer>.div(other: ScientificValue<MeasurementType.Length, Micrometer>) = SquareMicrometer.fromVolume(this, other)
@JvmName("cubicMillimeterDivSquareMillimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicMillimeter>.div(other: ScientificValue<MeasurementType.Area, SquareMillimeter>) = Millimeter.fromVolume(this, other)
@JvmName("cubicMillimeterDivMillimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicMillimeter>.div(other: ScientificValue<MeasurementType.Length, Millimeter>) = SquareMillimeter.fromVolume(this, other)
@JvmName("cubicCentimeterDivSquareCentimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicCentimeter>.div(other: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Centimeter.fromVolume(this, other)
@JvmName("cubicCentimeterDivCentimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicCentimeter>.div(other: ScientificValue<MeasurementType.Length, Centimeter>) = SquareCentimeter.fromVolume(this, other)
@JvmName("cubicDecimeterDivSquareDecimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicDecimeter>.div(other: ScientificValue<MeasurementType.Area, SquareDecimeter>) = Decimeter.fromVolume(this, other)
@JvmName("cubicDecimeterDivDecimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicDecimeter>.div(other: ScientificValue<MeasurementType.Length, Decimeter>) = SquareDecimeter.fromVolume(this, other)
@JvmName("cubicDecameterDivSquareDecameter")
operator fun ScientificValue<MeasurementType.Volume, CubicDecameter>.div(other: ScientificValue<MeasurementType.Area, SquareDecameter>) = Decameter.fromVolume(this, other)
@JvmName("cubicDecameterDivDecameter")
operator fun ScientificValue<MeasurementType.Volume, CubicDecameter>.div(other: ScientificValue<MeasurementType.Length, Decameter>) = SquareDecameter.fromVolume(this, other)
@JvmName("cubicHectometerDivSquareHectometer")
operator fun ScientificValue<MeasurementType.Volume, CubicHectometer>.div(other: ScientificValue<MeasurementType.Area, SquareHectometer>) = Hectometer.fromVolume(this, other)
@JvmName("cubicHectometerDivHectometer")
operator fun ScientificValue<MeasurementType.Volume, CubicHectometer>.div(other: ScientificValue<MeasurementType.Length, Hectometer>) = SquareHectometer.fromVolume(this, other)
@JvmName("cubicKilometerDivSquareKilometer")
operator fun ScientificValue<MeasurementType.Volume, CubicKilometer>.div(other: ScientificValue<MeasurementType.Area, SquareKilometer>) = Kilometer.fromVolume(this, other)
@JvmName("cubicKilometerDivKilometer")
operator fun ScientificValue<MeasurementType.Volume, CubicKilometer>.div(other: ScientificValue<MeasurementType.Length, Kilometer>) = SquareKilometer.fromVolume(this, other)
@JvmName("cubicInchDivSquareInch")
operator fun ScientificValue<MeasurementType.Volume, CubicInch>.div(other: ScientificValue<MeasurementType.Area, SquareInch>) = Inch.fromVolume( this, other)
@JvmName("cubicInchDivInch")
operator fun ScientificValue<MeasurementType.Volume, CubicInch>.div(other: ScientificValue<MeasurementType.Length, Inch>) = SquareInch.fromVolume(this, other)
@JvmName("cubicFootDivSquareFoot")
operator fun ScientificValue<MeasurementType.Volume, CubicFoot>.div(other: ScientificValue<MeasurementType.Area, SquareFoot>) = Foot.fromVolume(this, other)
@JvmName("cubicFootDivFoot")
operator fun ScientificValue<MeasurementType.Volume, CubicFoot>.div(other: ScientificValue<MeasurementType.Length, Foot>) = SquareFoot.fromVolume(this, other)
@JvmName("cubicYardDivSquareYard")
operator fun ScientificValue<MeasurementType.Volume, CubicYard>.div(other: ScientificValue<MeasurementType.Area, SquareYard>) = Yard.fromVolume(this, other)
@JvmName("cubicYardDivYard")
operator fun ScientificValue<MeasurementType.Volume, CubicYard>.div(other: ScientificValue<MeasurementType.Length, Yard>) = SquareYard.fromVolume(this, other)
@JvmName("cubicMileDivSquareMile")
operator fun ScientificValue<MeasurementType.Volume, CubicMile>.div(other: ScientificValue<MeasurementType.Area, SquareMile>) = Mile.fromVolume(this, other)
@JvmName("cubicMileDivMile")
operator fun ScientificValue<MeasurementType.Volume, CubicMile>.div(other: ScientificValue<MeasurementType.Length, Mile>) = SquareMile.fromVolume(this, other)
@JvmName("acreFootDivAcre")
operator fun ScientificValue<MeasurementType.Volume, AcreFoot>.div(other: ScientificValue<MeasurementType.Area, Acre>) = Foot.fromVolume(this, other)
@JvmName("acreFootDivFoot")
operator fun ScientificValue<MeasurementType.Volume, AcreFoot>.div(other: ScientificValue<MeasurementType.Length, Foot>) = Acre.fromVolume(this, other)
