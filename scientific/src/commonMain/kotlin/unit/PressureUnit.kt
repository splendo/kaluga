/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.scientific.convertValue
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricPressure]
 */
val MetricPressureUnits: Set<MetricPressure> get() = setOf(
    Pascal,
    Nanopascal,
    Micropascal,
    Millipascal,
    Centipascal,
    Decipascal,
    Decapascal,
    Hectopascal,
    Kilopascal,
    Megapascal,
    Gigapascal,
    Bar,
    Nanobar,
    Microbar,
    Millibar,
    Centibar,
    Decibar,
    Decabar,
    Hectobar,
    Kilobar,
    Megabar,
    Gigabar,
    Barye,
    Nanobarye,
    Microbarye,
    Millibarye,
    Centibarye,
    Decibarye,
    Decabarye,
    Hectobarye,
    Kilobarye,
    Megabarye,
    Gigabarye,
    Atmosphere,
    Torr,
    Nanotorr,
    Microtorr,
    Millitorr,
    Centitorr,
    Decitorr,
    Decatorr,
    Hectotorr,
    Kilotorr,
    Megatorr,
    Gigatorr,
    MillimeterOfMercury,
    MillimeterOfWater,
    CentimeterOfWater,
)

/**
 * Set of all [ImperialPressure]
 */
val ImperialPressureUnits: Set<ImperialPressure> get() = setOf(
    PoundSquareInch,
    PoundSquareFoot,
    OunceSquareInch,
    KiloPoundSquareInch,
    InchOfMercury,
    InchOfWater,
    FootOfWater,
)

/**
 * Set of all [USCustomaryPressure]
 */
val USCustomaryPressureUnits: Set<USCustomaryPressure> get() = setOf(
    KipSquareInch,
    KipSquareFoot,
    USTonSquareInch,
    USTonSquareFoot,
) + ImperialPressureUnits.map { it.usCustomary }.toSet()

/**
 * Set of all [UKImperialPressure]
 */
val UKImperialPressureUnits: Set<UKImperialPressure> get() = setOf(
    ImperialTonSquareInch,
    ImperialTonSquareFoot,
) + ImperialPressureUnits.map { it.ukImperial }.toSet()

/**
 * Set of all [Pressure]
 */
val PressureUnits: Set<Pressure> get() = MetricPressureUnits +
    ImperialPressureUnits +
    USCustomaryPressureUnits.filter { it !is USCustomaryImperialPressureWrapper }.toSet() +
    UKImperialPressureUnits.filter { it !is UKImperialPressureWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Pressure]
 * SI unit is [Pascal]
 */
@Serializable
sealed class Pressure : AbstractScientificUnit<PhysicalQuantity.Pressure>()

/**
 * A [Pressure] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricPressure : Pressure(), MetricScientificUnit<PhysicalQuantity.Pressure>

/**
 * A [Pressure] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialPressure : Pressure(), ImperialScientificUnit<PhysicalQuantity.Pressure> {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Pressure
}

/**
 * A [Pressure] for [MeasurementSystem.UKImperial]
 */
@Serializable
sealed class UKImperialPressure : Pressure(), UKImperialScientificUnit<PhysicalQuantity.Pressure> {
    override val system = MeasurementSystem.UKImperial
    override val quantity = PhysicalQuantity.Pressure
}

/**
 * A [Pressure] for [MeasurementSystem.USCustomary]
 */
@Serializable
sealed class USCustomaryPressure : Pressure(), USCustomaryScientificUnit<PhysicalQuantity.Pressure> {
    override val system = MeasurementSystem.USCustomary
    override val quantity = PhysicalQuantity.Pressure
}

@Serializable
data object Pascal : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure> {
    override val symbol: String = "P"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class PascalMultiple : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal>

@Serializable
data object Nanopascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Nano(Pascal)

@Serializable
data object Micropascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Micro(Pascal)

@Serializable
data object Millipascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Milli(Pascal)

@Serializable
data object Centipascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Centi(Pascal)

@Serializable
data object Decipascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Deci(Pascal)

@Serializable
data object Decapascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Deca(Pascal)

@Serializable
data object Hectopascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Hecto(Pascal)

@Serializable
data object Kilopascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Kilo(Pascal)

@Serializable
data object Megapascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Mega(Pascal)

@Serializable
data object Gigapascal : PascalMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal> by Giga(Pascal)

@Serializable
data object Bar : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure> {
    private const val BAR_PER_PASCAL = 0.00001
    override val symbol: String = "bar"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BAR_PER_PASCAL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / BAR_PER_PASCAL.toDecimal()
}

@Serializable
sealed class BarMultiple : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar>

@Serializable
data object Nanobar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Nano(Bar)

@Serializable
data object Microbar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Micro(Bar)

@Serializable
data object Millibar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Milli(Bar)

@Serializable
data object Centibar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Centi(Bar)

@Serializable
data object Decibar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Deci(Bar)

@Serializable
data object Decabar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Deca(Bar)

@Serializable
data object Hectobar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Hecto(Bar)

@Serializable
data object Kilobar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Kilo(Bar)

@Serializable
data object Megabar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Mega(Bar)

@Serializable
data object Gigabar : BarMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar> by Giga(Bar)

@Serializable
data object Barye : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure> {
    private const val BARYE_PER_PASCAL = 10
    override val symbol: String = "Ba"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BARYE_PER_PASCAL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / BARYE_PER_PASCAL.toDecimal()
}

@Serializable
sealed class BaryeMultiple : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye>

@Serializable
data object Nanobarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Nano(Barye)

@Serializable
data object Microbarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Micro(Barye)

@Serializable
data object Millibarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Milli(Barye)

@Serializable
data object Centibarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Centi(Barye)

@Serializable
data object Decibarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Deci(Barye)

@Serializable
data object Decabarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Deca(Barye)

@Serializable
data object Hectobarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Hecto(Barye)

@Serializable
data object Kilobarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Kilo(Barye)

@Serializable
data object Megabarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Mega(Barye)

@Serializable
data object Gigabarye : BaryeMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye> by Giga(Barye)

@Serializable
data object Atmosphere : MetricPressure() {
    private const val PASCAL_PER_ATMOSPHERE = 101325
    override val symbol: String = "atm"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_ATMOSPHERE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_ATMOSPHERE.toDecimal()
}

@Serializable
data object Torr : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure> {
    private const val TORR_PER_ATMOSPHERE = 760
    override val symbol: String = "Torr"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = Atmosphere.fromSIUnit(value) * TORR_PER_ATMOSPHERE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Atmosphere.toSIUnit(value / TORR_PER_ATMOSPHERE.toDecimal())
}

@Serializable
sealed class TorrMultiple : MetricPressure(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr>

@Serializable
data object Nanotorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Nano(Torr)

@Serializable
data object Microtorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Micro(Torr)

@Serializable
data object Millitorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Milli(Torr)

@Serializable
data object Centitorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Centi(Torr)

@Serializable
data object Decitorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Deci(Torr)

@Serializable
data object Decatorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Deca(Torr)

@Serializable
data object Hectotorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Hecto(Torr)

@Serializable
data object Kilotorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Kilo(Torr)

@Serializable
data object Megatorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Mega(Torr)

@Serializable
data object Gigatorr : TorrMultiple(), MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr> by Giga(Torr)

@Serializable
data object MillimeterOfMercury : MetricPressure() {
    private const val PASCAL_PER_MMHG = 133.322387415
    override val symbol: String = "mmHg"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_MMHG.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_MMHG.toDecimal()
}

@Serializable
data object CentimeterOfWater : MetricPressure() {
    override val symbol: String = "cmH2O"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) / 10.0.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value * 10.0.toDecimal())
}

@Serializable
data object MillimeterOfWater : MetricPressure() {
    private const val PASCAL_PER_MMH2O = 9.80665
    override val symbol: String = "mmH2O"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_MMH2O.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_MMH2O.toDecimal()
}

@Serializable
data object PoundSquareInch : ImperialPressure() {
    override val symbol: String = "psi"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
data object PoundSquareFoot : ImperialPressure() {
    override val symbol: String = "${PoundForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / SquareFoot.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * SquareFoot.fromSIUnit(1.toDecimal()))
}

@Serializable
data object OunceSquareInch : ImperialPressure() {
    override val symbol: String = "${OunceForce.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = OunceForce.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = OunceForce.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
data object KiloPoundSquareInch : ImperialPressure() {
    private const val POUND_PER_KILOPOUND_SQUARE_INCH = 1000.0
    override val symbol: String = "ksi"
    override fun fromSIUnit(value: Decimal): Decimal = PoundSquareInch.fromSIUnit(value) / POUND_PER_KILOPOUND_SQUARE_INCH.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = PoundSquareInch.toSIUnit(value * POUND_PER_KILOPOUND_SQUARE_INCH.toDecimal())
}

@Serializable
data object InchOfMercury : ImperialPressure() {
    override val symbol: String = "inHg"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfMercury.fromSIUnit(value) * 1(Millimeter).convertValue(Inch)
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfMercury.toSIUnit(value / 1(Millimeter).convertValue(Inch))
}

@Serializable
data object InchOfWater : ImperialPressure() {
    override val symbol: String = "inH2O"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) * 1(Millimeter).convertValue(Inch)
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value / 1(Millimeter).convertValue(Inch))
}

@Serializable
data object FootOfWater : ImperialPressure() {
    override val symbol: String = "ftH2O"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) * 1(Millimeter).convertValue(Foot)
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value / 1(Millimeter).convertValue(Foot))
}

@Serializable
data object KipSquareInch : USCustomaryPressure() {
    override val symbol: String = "${Kip.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = Kip.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = Kip.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
data object KipSquareFoot : USCustomaryPressure() {
    override val symbol: String = "${Kip.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = Kip.fromSIUnit(value) / SquareFoot.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = Kip.toSIUnit(value * SquareFoot.fromSIUnit(1.toDecimal()))
}

@Serializable
data object USTonSquareInch : USCustomaryPressure() {
    override val symbol: String = "${UsTonForce.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = UsTonForce.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = UsTonForce.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
data object USTonSquareFoot : USCustomaryPressure() {
    override val symbol: String = "${UsTonForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = UsTonForce.fromSIUnit(value) / SquareFoot.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = UsTonForce.toSIUnit(value * SquareFoot.fromSIUnit(1.toDecimal()))
}

/**
 * Wraps an [ImperialPressure] unit to a [USCustomaryPressure] unit
 * @param imperial the [ImperialPressure] to wrap
 */
@Serializable
data class USCustomaryImperialPressureWrapper(val imperial: ImperialPressure) : USCustomaryPressure() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

/**
 * Converts an [ImperialPressure] unit to a [USCustomaryImperialPressureWrapper] unit
 * @param PressureUnit the type of [ImperialPressure] to convert
 */
val <PressureUnit : ImperialPressure> PressureUnit.usCustomary get() = USCustomaryImperialPressureWrapper(this)

@Serializable
data object ImperialTonSquareInch : UKImperialPressure() {
    override val symbol: String = "${ImperialTonForce.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTonForce.fromSIUnit(value) / SquareInch.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = ImperialTonForce.toSIUnit(value * SquareInch.fromSIUnit(1.toDecimal()))
}

@Serializable
data object ImperialTonSquareFoot : UKImperialPressure() {
    override val symbol: String = "${ImperialTonForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTonForce.fromSIUnit(value) / SquareFoot.fromSIUnit(1.toDecimal())
    override fun toSIUnit(value: Decimal): Decimal = ImperialTonForce.toSIUnit(value * SquareFoot.fromSIUnit(1.toDecimal()))
}

/**
 * Wraps an [ImperialPressure] unit to a [UKImperialPressure] unit
 * @param imperial the [ImperialPressure] to wrap
 */
@Serializable
data class UKImperialPressureWrapper(val imperial: ImperialPressure) : UKImperialPressure() {
    override val symbol: String = imperial.symbol
    override fun fromSIUnit(value: Decimal): Decimal = imperial.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = imperial.toSIUnit(value)
}

/**
 * Converts an [ImperialPressure] unit to a [UKImperialPressureWrapper] unit
 * @param PressureUnit the type of [ImperialPressure] to convert
 */
val <PressureUnit : ImperialPressure> PressureUnit.ukImperial get() = UKImperialPressureWrapper(this)
