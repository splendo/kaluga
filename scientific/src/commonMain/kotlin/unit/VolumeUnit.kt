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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

val MetricVolumeUnits: Set<MetricVolume> get() = setOf(
    CubicMeter,
    CubicNanometer,
    CubicMicrometer,
    CubicMillimeter,
    CubicCentimeter,
    CubicDecimeter,
    CubicDecameter,
    CubicHectometer,
    CubicKilometer,
    CubicMegameter,
    CubicGigameter,
    Liter,
    Nanoliter,
    Microliter,
    Milliliter,
    Centiliter,
    Deciliter,
    Decaliter,
    Hectoliter,
    Kiloliter,
    Megaliter,
    Gigaliter
)

val ImperialVolumeUnits: Set<ImperialVolume> get() = setOf(
    CubicInch,
    CubicFoot,
    CubicYard,
    CubicMile
)

val USCustomaryVolumeUnits: Set<USCustomaryVolume> get() = ImperialVolumeUnits.map { it.usCustomary }.toSet() +
    setOf(
        AcreFoot,
        AcreInch,
        UsFluidDram,
        UsFluidOunce,
        UsCustomaryCup,
        UsLegalCup,
        UsLiquidPint,
        UsLiquidQuart,
        UsLiquidGallon
    )

val UKImperialVolumeUnits: Set<UKImperialVolume> get() = ImperialVolumeUnits.map { it.ukImperial }.toSet() +
    setOf(
        ImperialFluidDram,
        ImperialFluidOunce,
        MetricCup,
        ImperialPint,
        ImperialQuart,
        ImperialGallon
    )

val VolumeUnits: Set<Volume> get() = MetricVolumeUnits +
    ImperialVolumeUnits +
    USCustomaryVolumeUnits.filter { it !is USCustomaryImperialVolumeWrapper }.toSet() +
    UKImperialVolumeUnits.filter { it !is UKImperialImperialVolumeWrapper }.toSet()

@Serializable
sealed class Volume : AbstractScientificUnit<PhysicalQuantity.Volume>()

@Serializable
sealed class MetricVolume : Volume(), MetricScientificUnit<PhysicalQuantity.Volume>

@Serializable
sealed class USCustomaryVolume : Volume(), USCustomaryScientificUnit<PhysicalQuantity.Volume> {
    override val quantity = PhysicalQuantity.Volume
    override val system = MeasurementSystem.USCustomary
}

@Serializable
sealed class UKImperialVolume : Volume(), UKImperialScientificUnit<PhysicalQuantity.Volume> {
    override val quantity = PhysicalQuantity.Volume
    override val system = MeasurementSystem.UKImperial
}

@Serializable
sealed class ImperialVolume : Volume(), ImperialScientificUnit<PhysicalQuantity.Volume>

class Cubic<S : MeasurementSystem, U : SystemScientificUnit<S, PhysicalQuantity.Length>>(private val unit: U) : SystemScientificUnit<S, PhysicalQuantity.Volume> {
    override val symbol: String = "${unit.symbol}3"
    override val system: S = unit.system
    override val quantity = PhysicalQuantity.Volume
    override fun fromSIUnit(value: Decimal): Decimal = unit.fromSIUnit(unit.fromSIUnit(unit.fromSIUnit(value)))
    override fun toSIUnit(value: Decimal): Decimal = unit.toSIUnit(unit.toSIUnit(unit.toSIUnit(value)))
}

// Metric Volume
@Serializable
object CubicMeter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Meter)

@Serializable
object CubicDecimeter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Deci(Meter))

@Serializable
object CubicCentimeter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Centi(Meter)) {
    override val symbol: String = "cc"
}

@Serializable
object CubicMillimeter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Milli(Meter))

@Serializable
object CubicMicrometer : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Micro(Meter))

@Serializable
object CubicNanometer : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Nano(Meter))

@Serializable
object CubicDecameter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Deca(Meter))

@Serializable
object CubicHectometer : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Hecto(Meter))

@Serializable
object CubicKilometer : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Kilo(Meter))

@Serializable
object CubicMegameter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Mega(Meter))

@Serializable
object CubicGigameter : MetricVolume(), SystemScientificUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> by Cubic(Giga(Meter))

@Serializable
object Liter : MetricVolume(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume> {
    override val symbol: String = "l"
    private const val LITERS_IN_CUBIC_METER = 1000.0
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Volume
    override fun toSIUnit(value: Decimal): Decimal = value / LITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * LITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Deciliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Deci(Liter)

@Serializable
object Centiliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Centi(Liter)

@Serializable
object Milliliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Milli(Liter)

@Serializable
object Microliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Micro(Liter)

@Serializable
object Nanoliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Nano(Liter)

@Serializable
object Decaliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Deca(Liter)

@Serializable
object Hectoliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Hecto(Liter)

@Serializable
object Kiloliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Kilo(Liter)

@Serializable
object Megaliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Mega(Liter)

@Serializable
object Gigaliter : MetricVolume(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Volume, Liter> by Giga(Liter)

// Imperial
@Serializable
object CubicInch : ImperialVolume(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Volume> by Cubic(Inch) {
    override val symbol: String = "cu in"
}

@Serializable
object CubicFoot : ImperialVolume(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Volume> by Cubic(Foot) {
    override val symbol: String = "cu ft"
}

@Serializable
object CubicYard : ImperialVolume(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Volume> by Cubic(Yard) {
    override val symbol: String = "cu yd"
}

@Serializable
object CubicMile : ImperialVolume(), SystemScientificUnit<MeasurementSystem.Imperial, PhysicalQuantity.Volume> by Cubic(Mile) {
    override val symbol: String = "cu mi"
}

// US Imperial

@Serializable
data class USCustomaryImperialVolumeWrapper(val imperial: ImperialVolume) : USCustomaryVolume() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

val <VolumeUnit : ImperialVolume> VolumeUnit.usCustomary get() = USCustomaryImperialVolumeWrapper(this)

@Serializable
object AcreFoot : USCustomaryVolume() {
    override val symbol: String = "ac ft"
    override fun toSIUnit(value: Decimal): Decimal = Foot.toSIUnit(Acre.toSIUnit(value))
    override fun fromSIUnit(value: Decimal): Decimal = Acre.fromSIUnit(Foot.fromSIUnit(value))
}

@Serializable
object AcreInch : USCustomaryVolume() {
    override val symbol: String = "ac in"
    override fun toSIUnit(value: Decimal): Decimal = Inch.toSIUnit(Acre.toSIUnit(value))
    override fun fromSIUnit(value: Decimal): Decimal = Acre.fromSIUnit(Inch.fromSIUnit(value))
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
    override fun fromSIUnit(value: Decimal): Decimal = Milliliter.fromSIUnit(value) / MILLILITERS_IN_CUP.toDecimal()
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

val <VolumeUnit : ImperialVolume> VolumeUnit.ukImperial get() = UKImperialImperialVolumeWrapper(this)

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
