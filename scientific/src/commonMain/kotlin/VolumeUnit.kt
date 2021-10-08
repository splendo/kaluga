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

@Serializable
sealed class Volume<System : MeasurementSystem> :
    AbstractScientificUnit<System, MeasurementType.Volume>()

@Serializable
sealed class MetricVolume :
    Volume<MeasurementSystem.Metric>()

@Serializable
sealed class USImperialVolume :
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
object AcreFoot : USImperialVolume() {
    override val symbol: String = "ac ft"
    val ACRE_FOOT_IN_CUBIC_METER = 0.000810713193789913
    override fun toSIUnit(value: Decimal): Decimal = value / ACRE_FOOT_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * ACRE_FOOT_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsFluidDram : USImperialVolume() {
    override val symbol: String = "fl dr"
    const val US_FLUID_DRAM_IN_CUBIC_METER = 270512.18161474395
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_FLUID_DRAM_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsFluidOunce : USImperialVolume() {
    override val symbol: String = "fl oz"
    const val US_FLUID_OUNCES_IN_LITER = 33814.022701842994
    override fun toSIUnit(value: Decimal): Decimal = value / US_FLUID_OUNCES_IN_LITER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_FLUID_OUNCES_IN_LITER.toDecimal()
}

@Serializable
object UsLegalCup : USImperialVolume() {
    override val symbol: String = "cup"
    const val US_LEGAL_CUPS_IN_CUBIC_METER = 4226.752837730375
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_LEGAL_CUPS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_LEGAL_CUPS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsLiquidPint : USImperialVolume() {
    override val symbol: String = "pint"
    const val US_PINTS_IN_CUBIC_METER = 2113.376418865187
    override fun toSIUnit(value: Decimal): Decimal = value / US_PINTS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_PINTS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsLiquidQuart : USImperialVolume() {
    override val symbol: String = "qt"
    const val US_QUARTS_IN_CUBIC_METER = 1056.688209432594
    override fun toSIUnit(value: Decimal): Decimal = value / US_QUARTS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_QUARTS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsLiquidGallon : USImperialVolume() {
    override val symbol: String = "gal"
    const val US_LIQUID_GALLONS_IN_CUBIC_METER = 264.1720523581484
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_LIQUID_GALLONS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_LIQUID_GALLONS_IN_CUBIC_METER.toDecimal()
}

// UK Imperial
@Serializable
object ImperialFluidDram : UKImperialVolume() {
    override val symbol: String = "fl dr"
    const val IMPERIAL_FLUID_DRAM_IN_CUBIC_METER = 281560.63782283233
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_FLUID_DRAM_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialFluidOunce : UKImperialVolume() {
    override val symbol: String = "fl oz"
    const val IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER = 35195.07972785405
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialCup : UKImperialVolume() {
    override val symbol: String = "cup"
    const val IMPERIAL_CUPS_IN_CUBIC_METER = 4000.0
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_CUPS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_CUPS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialPint : UKImperialVolume() {
    override val symbol: String = "pt"
    const val IMPERIAL_PINTS_IN_CUBIC_METER = 1759.753986392702
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_PINTS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_PINTS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialQuart : UKImperialVolume() {
    override val symbol: String = "qt"
    const val IMPERIAL_QUARTS_IN_CUBIC_METER = 879.8769931963512
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_QUARTS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_QUARTS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialGallon : UKImperialVolume() {
    override val symbol: String = "gal"
    const val IMPERIAL_GALLONS_IN_CUBIC_METER = 219.96924829908778
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_GALLONS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_GALLONS_IN_CUBIC_METER.toDecimal()
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

operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMeter>) = CubicMeter.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>) = other * this
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareNanometer>) = CubicNanometer.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareNanometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>) = other * this
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMicrometer>) = CubicMicrometer.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMicrometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>) = other * this
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMillimeter>) = CubicMillimeter.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMillimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>) = other * this
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareCentimeter>) = CubicCentimeter.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareCentimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>) = other * this
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecimeter>) = CubicDecimeter.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecimeter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>) = other * this
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecameter>) = CubicDecameter.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecameter>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>) = other * this
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareHectometer>) = CubicHectometer.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareHectometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>) = other * this
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareKilometer>) = CubicKilometer.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareKilometer>.times(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>) = other * this
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareInch>) = CubicInch.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareInch>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>) = other * this
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareFoot>) = CubicFoot.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareFoot>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = other * this
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareYard>) = CubicYard.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareYard>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>) = other * this
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareMile>) = CubicMile.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareMile>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>) = other * this
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, Acre>) = AcreFoot.volume(other, this)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, Acre>.times(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = other * this

operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMeter>) = Meter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Meter>) = SquareMeter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicNanometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareNanometer>) = Nanometer.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicNanometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Nanometer>) = SquareNanometer.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMicrometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMicrometer>) = Micrometer.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMicrometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Micrometer>) = SquareMicrometer.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMillimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareMillimeter>) = Millimeter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicMillimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Millimeter>) = SquareMillimeter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicCentimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareCentimeter>) = Centimeter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicCentimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Centimeter>) = SquareCentimeter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicDecimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecimeter>) = Decimeter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicDecimeter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decimeter>) = SquareDecimeter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicDecameter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareDecameter>) = Decameter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicDecameter>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Decameter>) = SquareDecameter.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicHectometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareHectometer>) = Hectometer.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicHectometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Hectometer>) = SquareHectometer.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicKilometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Area, SquareKilometer>) = Kilometer.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Metric, MeasurementType.Volume, CubicKilometer>.div(other: ScientificValue<MeasurementSystem.Metric, MeasurementType.Length, Kilometer>) = SquareKilometer.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicInch>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareInch>) = Inch.fromVolume( this, other)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicInch>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Inch>) = SquareInch.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareFoot>) = Foot.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = SquareFoot.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicYard>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareYard>) = Yard.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicYard>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Yard>) = SquareYard.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicMile>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, SquareMile>) = Mile.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.Imperial, MeasurementType.Volume, CubicMile>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Mile>) = SquareMile.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.USCustomary, MeasurementType.Volume, AcreFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Area, Acre>) = Foot.fromVolume(this, other)
operator fun ScientificValue<MeasurementSystem.USCustomary, MeasurementType.Volume, AcreFoot>.div(other: ScientificValue<MeasurementSystem.Imperial, MeasurementType.Length, Foot>) = Acre.fromVolume(this, other)
