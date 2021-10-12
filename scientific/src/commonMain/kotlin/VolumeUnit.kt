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
sealed class Volume<System : MeasurementSystem> :
    AbstractScientificUnit<System, MeasurementType.Volume>()

@Serializable
sealed class MetricVolume :
    Volume<MeasurementSystem.Metric>()

@Serializable
sealed class USCustomaryVolume :
    Volume<MeasurementSystem.USCustomary>()

@Serializable
sealed class UKImperialVolume :
    Volume<MeasurementSystem.UKImperial>()

@Serializable
sealed class ImperialVolume :
    Volume<MeasurementSystem.Imperial>()

class Cubic<S : MeasurementSystem, U : ScientificUnit<S, MeasurementType.Length>>(private val unit : U) : ScientificUnit<S, MeasurementType.Volume> {
    override val symbol: String = "${unit.symbol}3"
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(unit.fromSIUnit(unit.fromSIUnit(value)))
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(unit.toSIUnit(unit.toSIUnit(value)))
}

// Metric Volume
@Serializable
object CubicMeter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Meter)

@Serializable
object CubicDecimeter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Deci(Meter))

@Serializable
object CubicCentimeter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Centi(Meter)) {
    override val symbol: String = "cc"
}

@Serializable
object CubicMillimeter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Milli(Meter))

@Serializable
object CubicMicrometer : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Micro(Meter))

@Serializable
object CubicNanometer : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Nano(Meter))

@Serializable
object CubicDecameter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Deca(Meter))

@Serializable
object CubicHectometer : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Hecto(Meter))

@Serializable
object CubicKilometer : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Cubic(Kilo(Meter))

@Serializable
object Liter : MetricVolume(), BaseMetricUnit<MeasurementType.Volume, MeasurementSystem.Metric> {
    override val symbol: String = "l"
    const val LITERS_IN_CUBIC_METER = 1000.0
    override fun toSIUnit(value: Decimal): Decimal = value / LITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * LITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Deciliter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Deci(Liter)

@Serializable
object Centiliter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Centi(Liter)

@Serializable
object Milliliter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Milli(Liter)

@Serializable
object Microliter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Micro(Liter)

@Serializable
object Nanoliter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Nano(Liter)

@Serializable
object Decaliter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Deca(Liter)

@Serializable
object Hectoliter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Hecto(Liter)

@Serializable
object Kiloliter : MetricVolume(), ScientificUnit<MeasurementSystem.Metric, MeasurementType.Volume> by Kilo(Liter)

// Imperial
@Serializable
object CubicInch : ImperialVolume(), ScientificUnit<MeasurementSystem.Imperial, MeasurementType.Volume> by Cubic(Inch) {
    override val symbol: String = "cu in"
}

@Serializable
object CubicFoot : ImperialVolume(), ScientificUnit<MeasurementSystem.Imperial, MeasurementType.Volume> by Cubic(Foot) {
    override val symbol: String = "cu ft"
}

@Serializable
object CubicYard : ImperialVolume(), ScientificUnit<MeasurementSystem.Imperial, MeasurementType.Volume> by Cubic(Yard) {
    override val symbol: String = "cu yd"
}

@Serializable
object CubicMile : ImperialVolume(), ScientificUnit<MeasurementSystem.Imperial, MeasurementType.Volume> by Cubic(Mile) {
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
    const val US_DRAMS_IN_FLUID_OUNCE = 8
    override fun toSIUnit(value: Decimal): Decimal = UsFluidOunce.toSIUnit(value / US_DRAMS_IN_FLUID_OUNCE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsFluidOunce.fromSIUnit(value) * US_DRAMS_IN_FLUID_OUNCE.toDecimal()
}

@Serializable
object UsFluidOunce : USCustomaryVolume() {
    override val symbol: String = "fl oz"
    const val US_FLUID_OUNCES_IN_GALLON = 128
    override fun toSIUnit(value: Decimal): Decimal = UsLiquidGallon.toSIUnit(value / US_FLUID_OUNCES_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) * US_FLUID_OUNCES_IN_GALLON.toDecimal()
}

@Serializable
object UsCustomaryCup : USCustomaryVolume() {
    override val symbol: String = "cup"
    const val US_LEGAL_CUPS_IN_GALLON = 16
    override fun toSIUnit(value: Decimal): Decimal = UsLiquidGallon.toSIUnit(value / US_LEGAL_CUPS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) * US_LEGAL_CUPS_IN_GALLON.toDecimal()
}

@Serializable
object UsLegalCup : USCustomaryVolume() {
    override val symbol: String = "cup"
    const val MILLILITERS_IN_CUP = 240
    override fun toSIUnit(value: Decimal): Decimal = Milliliter.toSIUnit(value * MILLILITERS_IN_CUP.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) / MILLILITERS_IN_CUP.toDecimal()
}

@Serializable
object UsLiquidPint : USCustomaryVolume() {
    override val symbol: String = "pint"
    const val US_PINTS_IN_GALLON = 8
    override fun toSIUnit(value: Decimal): Decimal = UsLiquidGallon.toSIUnit(value / US_PINTS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) * US_PINTS_IN_GALLON.toDecimal()
}

@Serializable
object UsLiquidQuart : USCustomaryVolume() {
    override val symbol: String = "qt"
    const val US_QUARTS_IN_GALLON = 4
    override fun toSIUnit(value: Decimal): Decimal = UsLiquidGallon.toSIUnit(value / US_QUARTS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = UsLiquidGallon.fromSIUnit(value) * US_QUARTS_IN_GALLON.toDecimal()
}

@Serializable
object UsLiquidGallon : USCustomaryVolume() {
    override val symbol: String = "gal"
    const val CUBIC_INCH_IN_GALLON = 231
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
    const val IMPERIAL_FLUID_DRAM_IN_FLUID_OUNCE = 8
    override fun toSIUnit(value: Decimal): Decimal = ImperialFluidOunce.toSIUnit(value / IMPERIAL_FLUID_DRAM_IN_FLUID_OUNCE.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = ImperialFluidOunce.fromSIUnit(value) * IMPERIAL_FLUID_DRAM_IN_FLUID_OUNCE.toDecimal()
}

@Serializable
object ImperialFluidOunce : UKImperialVolume() {
    override val symbol: String = "fl oz"
    const val IMPERIAL_FLUID_OUNCES_GALLON = 160
    override fun toSIUnit(value: Decimal): Decimal = ImperialGallon.toSIUnit(value / IMPERIAL_FLUID_OUNCES_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = ImperialGallon.fromSIUnit(value) * IMPERIAL_FLUID_OUNCES_GALLON.toDecimal()
}

@Serializable
object MetricCup : UKImperialVolume() {
    override val symbol: String = "cup"
    const val MILLILITER_IN_CUP = 250
    override fun toSIUnit(value: Decimal): Decimal = Milliliter.toSIUnit(value * MILLILITER_IN_CUP.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Milliliter.fromSIUnit(value) / MILLILITER_IN_CUP.toDecimal()
}

@Serializable
object ImperialPint : UKImperialVolume() {
    override val symbol: String = "pt"
    const val IMPERIAL_PINTS_IN_GALLON = 8
    override fun toSIUnit(value: Decimal): Decimal = ImperialGallon.toSIUnit(value / IMPERIAL_PINTS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = ImperialGallon.fromSIUnit(value) * IMPERIAL_PINTS_IN_GALLON.toDecimal()
}

@Serializable
object ImperialQuart : UKImperialVolume() {
    override val symbol: String = "qt"
    const val IMPERIAL_QUARTS_IN_GALLON = 4
    override fun toSIUnit(value: Decimal): Decimal = ImperialGallon.toSIUnit(value / IMPERIAL_QUARTS_IN_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = ImperialGallon.fromSIUnit(value) * IMPERIAL_QUARTS_IN_GALLON.toDecimal()
}

@Serializable
object ImperialGallon : UKImperialVolume() {
    override val symbol: String = "gal"
    const val LITER_PER_GALLON = 4.54609
    override fun toSIUnit(value: Decimal): Decimal = Liter.toSIUnit(value * LITER_PER_GALLON.toDecimal())
    override fun fromSIUnit(value: Decimal): Decimal = Liter.fromSIUnit(value) / LITER_PER_GALLON.toDecimal()
}

fun <
    HeightSystem : MeasurementSystem,
    HeightUnit : Length<HeightSystem>,
    AreaSystem : MeasurementSystem,
    AreaUnit : Area<AreaSystem>,
    VolumeSystem : MeasurementSystem,
    VolumeUnit : Volume<VolumeSystem>
    > VolumeUnit.volume(
    area: ScientificValue<AreaSystem, MeasurementType.Area, AreaUnit>,
    height: ScientificValue<HeightSystem, MeasurementType.Length, HeightUnit>
) : ScientificValue<VolumeSystem, MeasurementType.Volume, VolumeUnit> {
    val heightInMeter = height.convertValue(Meter)
    val areaInCubicMeter = area.convertValue(SquareMeter)
    return ScientificValue(areaInCubicMeter * heightInMeter, CubicMeter).convert(this)
}

fun <
    LengthSystem : MeasurementSystem,
    LengthUnit : Length<LengthSystem>,
    WidthSystem : MeasurementSystem,
    WidthUnit : Length<WidthSystem>,
    HeightSystem : MeasurementSystem,
    HeightUnit : Length<HeightSystem>,
    VolumeSystem : MeasurementSystem,
    VolumeUnit : Volume<VolumeSystem>
    > VolumeUnit.volume(
    length: ScientificValue<LengthSystem, MeasurementType.Length, LengthUnit>,
    width: ScientificValue<WidthSystem, MeasurementType.Length, WidthUnit>,
    height: ScientificValue<HeightSystem, MeasurementType.Length, HeightUnit>
) : ScientificValue<VolumeSystem, MeasurementType.Volume, VolumeUnit> = volume(SquareMeter.area(length, width), height)

fun <
    HeightSystem : MeasurementSystem,
    HeightUnit : Length<HeightSystem>,
    AreaSystem : MeasurementSystem,
    AreaUnit : Area<AreaSystem>,
    VolumeSystem : MeasurementSystem,
    VolumeUnit : Volume<VolumeSystem>
    > HeightUnit.fromVolume(
    volume: ScientificValue<VolumeSystem, MeasurementType.Volume, VolumeUnit>,
    area: ScientificValue<AreaSystem, MeasurementType.Area, AreaUnit>
) : ScientificValue<HeightSystem, MeasurementType.Length, HeightUnit> {
    val volumeInHeightCubed = volume.convertValue(Cubic(this))
    val areaInHeightSquared = area.convertValue(Square(this))
    return ScientificValue(volumeInHeightCubed / areaInHeightSquared, this)
}

fun <
    HeightSystem : MeasurementSystem,
    HeightUnit : Length<HeightSystem>,
    LengthSystem : MeasurementSystem,
    LengthUnit : Length<LengthSystem>,
    WidthSystem : MeasurementSystem,
    WidthUnit : Length<WidthSystem>,
    VolumeSystem : MeasurementSystem,
    VolumeUnit : Volume<VolumeSystem>
    > HeightUnit.fromVolume(
    volume: ScientificValue<VolumeSystem, MeasurementType.Volume, VolumeUnit>,
    length: ScientificValue<LengthSystem, MeasurementType.Length, LengthUnit>,
    width: ScientificValue<WidthSystem, MeasurementType.Length, WidthUnit>,
) : ScientificValue<HeightSystem, MeasurementType.Length, HeightUnit> = fromVolume(volume, SquareMeter.area(length, width))

fun <
    HeightSystem : MeasurementSystem,
    HeightUnit : Length<HeightSystem>,
    AreaSystem : MeasurementSystem,
    AreaUnit : Area<AreaSystem>,
    VolumeSystem : MeasurementSystem,
    VolumeUnit : Volume<VolumeSystem>
    > AreaUnit.fromVolume(
    volume: ScientificValue<VolumeSystem, MeasurementType.Volume, VolumeUnit>,
    height: ScientificValue<HeightSystem, MeasurementType.Length, HeightUnit>
) : ScientificValue<AreaSystem, MeasurementType.Area, AreaUnit> {
    val volumeInHeightCubed = volume.convertValue(Cubic(height.unit))
    return ScientificValue(volumeInHeightCubed / height.value, Square(height.unit)).convert(this)
}

@JvmName("meterTimesSquareMeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMeter>) = CubicMeter.volume(other, this)
@JvmName("squareMeterTimesMeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>) = other * this
@JvmName("nanometerTimesSquareNanometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareNanometer>) = CubicNanometer.volume(other, this)
@JvmName("squareNanometerTimesNanometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareNanometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>) = other * this
@JvmName("micrometerTimesSquareMicrometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMicrometer>) = CubicMicrometer.volume(other, this)
@JvmName("squareMicrometerTimesMicrometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMicrometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>) = other * this
@JvmName("millimeterTimesSquareMillimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMillimeter>) = CubicMillimeter.volume(other, this)
@JvmName("squareMillimeterTimesMillimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMillimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>) = other * this
@JvmName("centimeterTimesSquareCentimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareCentimeter>) = CubicCentimeter.volume(other, this)
@JvmName("squareCentimeterTimesCentimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareCentimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>) = other * this
@JvmName("decimeterTimesSquareDecimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecimeter>) = CubicDecimeter.volume(other, this)
@JvmName("squareDecimeterTimesDecimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>) = other * this
@JvmName("decameterTimesSquareDecameter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecameter>) = CubicDecameter.volume(other, this)
@JvmName("squareDecameterTimesDecameter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecameter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>) = other * this
@JvmName("hectometerTimesSquarehectometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareHectometer>) = CubicHectometer.volume(other, this)
@JvmName("squareHectometerTimesHectometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareHectometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>) = other * this
@JvmName("kilometerTimesSquareKilometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareKilometer>) = CubicKilometer.volume(other, this)
@JvmName("squareKilometerTimesKilometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareKilometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>) = other * this
@JvmName("inchTimesSquareInch")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareInch>) = CubicInch.volume(other, this)
@JvmName("squareInchTimesInch")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareInch>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>) = other * this
@JvmName("footTimesSquareFoot")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareFoot>) = CubicFoot.volume(other, this)
@JvmName("squareFootTimesFoot")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareFoot>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = other * this
@JvmName("yardTimesSquareYard")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareYard>) = CubicYard.volume(other, this)
@JvmName("squareYardTimesYard")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareYard>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>) = other * this
@JvmName("mileTimesSquareMile")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareMile>) = CubicMile.volume(other, this)
@JvmName("squareMileTimesMile")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareMile>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>) = other * this
@JvmName("footTimesAcre")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, Acre>) = AcreFoot.volume(other, this)
@JvmName("acreTimesFoot")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, Acre>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = other * this

@JvmName("cubicMeterDivSquareMeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMeter>) = Meter.fromVolume(this, other)
@JvmName("cubicMeterDivMeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>) = SquareMeter.fromVolume(this, other)
@JvmName("cubicNanometerDivSquareNanometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicNanometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareNanometer>) = Nanometer.fromVolume(this, other)
@JvmName("cubicNanometerDivNanometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicNanometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>) = SquareNanometer.fromVolume(this, other)
@JvmName("cubicMicrometerDivSquareMicrometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMicrometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMicrometer>) = Micrometer.fromVolume(this, other)
@JvmName("cubicMicrometerDivMicrometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMicrometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>) = SquareMicrometer.fromVolume(this, other)
@JvmName("cubicMillimeterDivSquareMillimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMillimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMillimeter>) = Millimeter.fromVolume(this, other)
@JvmName("cubicMillimeterDivMillimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMillimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>) = SquareMillimeter.fromVolume(this, other)
@JvmName("cubicCentimeterDivSquareCentimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicCentimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareCentimeter>) = Centimeter.fromVolume(this, other)
@JvmName("cubicCentimeterDivCentimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicCentimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>) = SquareCentimeter.fromVolume(this, other)
@JvmName("cubicDecimeterDivSquareDecimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicDecimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecimeter>) = Decimeter.fromVolume(this, other)
@JvmName("cubicDecimeterDivDecimeter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicDecimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>) = SquareDecimeter.fromVolume(this, other)
@JvmName("cubicDecameterDivSquareDecameter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicDecameter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecameter>) = Decameter.fromVolume(this, other)
@JvmName("cubicDecameterDivDecameter")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicDecameter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>) = SquareDecameter.fromVolume(this, other)
@JvmName("cubicHectometerDivSquareHectometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicHectometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareHectometer>) = Hectometer.fromVolume(this, other)
@JvmName("cubicHectometerDivHectometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicHectometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>) = SquareHectometer.fromVolume(this, other)
@JvmName("cubicKilometerDivSquareKilometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicKilometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareKilometer>) = Kilometer.fromVolume(this, other)
@JvmName("cubicKilometerDivKilometer")
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicKilometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>) = SquareKilometer.fromVolume(this, other)
@JvmName("cubicInchDivSquareInch")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicInch>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareInch>) = Inch.fromVolume( this, other)
@JvmName("cubicInchDivInch")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicInch>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>) = SquareInch.fromVolume(this, other)
@JvmName("cubicFootDivSquareFoot")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareFoot>) = Foot.fromVolume(this, other)
@JvmName("cubicFootDivFoot")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = SquareFoot.fromVolume(this, other)
@JvmName("cubicYardDivSquareYard")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicYard>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareYard>) = Yard.fromVolume(this, other)
@JvmName("cubicYardDivYard")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicYard>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>) = SquareYard.fromVolume(this, other)
@JvmName("cubicMileDivSquareMile")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicMile>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareMile>) = Mile.fromVolume(this, other)
@JvmName("cubicMileDivMile")
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicMile>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>) = SquareMile.fromVolume(this, other)
@JvmName("acreFootDivAcre")
operator fun ScientificValue<MeasurementSystem.USCustomary, MeasurementType.Volume, AcreFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, Acre>) = Foot.fromVolume(this, other)
@JvmName("acreFootDivFoot")
operator fun ScientificValue<MeasurementSystem.USCustomary, MeasurementType.Volume, AcreFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = Acre.fromVolume(this, other)
