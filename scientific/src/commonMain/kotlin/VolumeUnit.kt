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
sealed class MetricVolume() :
    Volume<MeasurementSystem.Metric>()

@Serializable
sealed class USImperialVolume() :
    Volume<MeasurementSystem.USCustomary>()

@Serializable
sealed class UKImperialVolume() :
    Volume<MeasurementSystem.UKImperial>()

@Serializable
sealed class ImperialVolume() :
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

fun <System : MeasurementSystem, VolumeType : Volume<System>> VolumeType.volumeFrom(width: Decimal, depth: Decimal, height: Decimal, lengthUnit: Length<*>): ScientificValue<System, MeasurementType.Volume, VolumeType> {
    val volume = lengthUnit.toSIUnit(width) * lengthUnit.toSIUnit(depth) * lengthUnit.toSIUnit(height)
    return ScientificValue(volume, CubicMeter).convert(this)
}

fun <System : MeasurementSystem, VolumeType : Volume<System>> VolumeType.volumeFrom(area: Decimal, areaUnit: Area<*>, height: Decimal, lengthUnit: Length<*>): ScientificValue<System, MeasurementType.Volume, VolumeType> {
    val volume = areaUnit.toSIUnit(area) * lengthUnit.toSIUnit(height)
    return ScientificValue(volume, CubicMeter).convert(this)
}
