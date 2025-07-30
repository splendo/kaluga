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
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

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
 * An [DefinedScientificUnit] for [PhysicalQuantity.Pressure]
 * SI unit is [Pascal]
 */
@Serializable
sealed class Pressure : DefinedScientificUnit<PhysicalQuantity.Pressure>()

/**
 * A [Pressure] for [MeasurementSystem.Metric]
 */
@Serializable
sealed class MetricPressure :
    Pressure(),
    MetricScientificUnit<PhysicalQuantity.Pressure>

/**
 * A [Pressure] for [MeasurementSystem.Imperial]
 */
@Serializable
sealed class ImperialPressure :
    Pressure(),
    ImperialScientificUnit<PhysicalQuantity.Pressure> {
    override val system = MeasurementSystem.Imperial
    override val quantity = PhysicalQuantity.Pressure
}

/**
 * A [Pressure] for [MeasurementSystem.UKImperial]
 */
@Serializable
sealed class UKImperialPressure :
    Pressure(),
    UKImperialScientificUnit<PhysicalQuantity.Pressure> {
    override val system = MeasurementSystem.UKImperial
    override val quantity = PhysicalQuantity.Pressure
}

/**
 * A [Pressure] for [MeasurementSystem.USCustomary]
 */
@Serializable
sealed class USCustomaryPressure :
    Pressure(),
    USCustomaryScientificUnit<PhysicalQuantity.Pressure> {
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
sealed class PascalMultiple :
    MetricPressure(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Pascal>

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
    private val BAR_PER_PASCAL = "0.00001".toDecimal()
    override val symbol: String = "bar"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BAR_PER_PASCAL
    override fun toSIUnit(value: Decimal): Decimal = value / BAR_PER_PASCAL
}

@Serializable
sealed class BarMultiple :
    MetricPressure(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Bar>

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
    private val BARYE_PER_PASCAL = Decimal.TEN
    override val symbol: String = "Ba"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value * BARYE_PER_PASCAL
    override fun toSIUnit(value: Decimal): Decimal = value / BARYE_PER_PASCAL
}

@Serializable
sealed class BaryeMultiple :
    MetricPressure(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Barye>

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
    private val PASCAL_PER_ATMOSPHERE = 101325.toDecimal()
    override val symbol: String = "atm"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_ATMOSPHERE
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_ATMOSPHERE
}

@Serializable
data object Torr : MetricPressure(), MetricBaseUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure> {
    private val TORR_PER_ATMOSPHERE = 760.toDecimal()
    override val symbol: String = "Torr"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = Atmosphere.fromSIUnit(value) * TORR_PER_ATMOSPHERE
    override fun toSIUnit(value: Decimal): Decimal = Atmosphere.toSIUnit(value / TORR_PER_ATMOSPHERE)
}

@Serializable
sealed class TorrMultiple :
    MetricPressure(),
    MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Pressure, Torr>

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
    private val PASCAL_PER_MMHG = "133.322387415".toDecimal()
    override val symbol: String = "mmHg"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_MMHG
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_MMHG
}

@Serializable
data object CentimeterOfWater : MetricPressure() {
    override val symbol: String = "cmH2O"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) / Decimal.TEN
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value * Decimal.TEN)
}

@Serializable
data object MillimeterOfWater : MetricPressure() {
    private val PASCAL_PER_MMH2O = "9.80665".toDecimal()
    override val symbol: String = "mmH2O"
    override val system = MeasurementSystem.Metric
    override val quantity = PhysicalQuantity.Pressure
    override fun fromSIUnit(value: Decimal): Decimal = value / PASCAL_PER_MMH2O
    override fun toSIUnit(value: Decimal): Decimal = value * PASCAL_PER_MMH2O
}

private val ONE_SQUARE_INCH = SquareInch.fromSIUnit(Decimal.ONE)
private val ONE_SQUARE_FOOT = SquareFoot.fromSIUnit(Decimal.ONE)
private val ONE_MILLIMETER_IN_INCHES = 1(Millimeter).convertValue(Inch)

@Serializable
data object PoundSquareInch : ImperialPressure() {
    override val symbol: String = "psi"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / ONE_SQUARE_INCH
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * ONE_SQUARE_INCH)
}

@Serializable
data object PoundSquareFoot : ImperialPressure() {
    override val symbol: String = "${PoundForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = PoundForce.fromSIUnit(value) / ONE_SQUARE_FOOT
    override fun toSIUnit(value: Decimal): Decimal = PoundForce.toSIUnit(value * ONE_SQUARE_FOOT)
}

@Serializable
data object OunceSquareInch : ImperialPressure() {
    override val symbol: String = "${OunceForce.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = OunceForce.fromSIUnit(value) / ONE_SQUARE_INCH
    override fun toSIUnit(value: Decimal): Decimal = OunceForce.toSIUnit(value * ONE_SQUARE_INCH)
}

@Serializable
data object KiloPoundSquareInch : ImperialPressure() {
    private val POUND_PER_KILOPOUND_SQUARE_INCH = Decimal.THOUSAND
    override val symbol: String = "ksi"
    override fun fromSIUnit(value: Decimal): Decimal = PoundSquareInch.fromSIUnit(value) / POUND_PER_KILOPOUND_SQUARE_INCH
    override fun toSIUnit(value: Decimal): Decimal = PoundSquareInch.toSIUnit(value * POUND_PER_KILOPOUND_SQUARE_INCH)
}

@Serializable
data object InchOfMercury : ImperialPressure() {
    override val symbol: String = "inHg"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfMercury.fromSIUnit(value) * ONE_MILLIMETER_IN_INCHES
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfMercury.toSIUnit(value / ONE_MILLIMETER_IN_INCHES)
}

@Serializable
data object InchOfWater : ImperialPressure() {
    override val symbol: String = "inH2O"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) * ONE_MILLIMETER_IN_INCHES
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value / ONE_MILLIMETER_IN_INCHES)
}

@Serializable
data object FootOfWater : ImperialPressure() {
    private val ONE_MILLIMETER_IN_FEET = 1(Millimeter).convertValue(Foot)
    override val symbol: String = "ftH2O"
    override fun fromSIUnit(value: Decimal): Decimal = MillimeterOfWater.fromSIUnit(value) * ONE_MILLIMETER_IN_FEET
    override fun toSIUnit(value: Decimal): Decimal = MillimeterOfWater.toSIUnit(value / ONE_MILLIMETER_IN_FEET)
}

@Serializable
data object KipSquareInch : USCustomaryPressure() {
    override val symbol: String = "${Kip.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = Kip.fromSIUnit(value) / ONE_SQUARE_INCH
    override fun toSIUnit(value: Decimal): Decimal = Kip.toSIUnit(value * ONE_SQUARE_INCH)
}

@Serializable
data object KipSquareFoot : USCustomaryPressure() {
    override val symbol: String = "${Kip.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = Kip.fromSIUnit(value) / ONE_SQUARE_FOOT
    override fun toSIUnit(value: Decimal): Decimal = Kip.toSIUnit(value * ONE_SQUARE_FOOT)
}

@Serializable
data object USTonSquareInch : USCustomaryPressure() {
    override val symbol: String = "${UsTonForce.symbol}/${SquareInch.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = UsTonForce.fromSIUnit(value) / ONE_SQUARE_INCH
    override fun toSIUnit(value: Decimal): Decimal = UsTonForce.toSIUnit(value * ONE_SQUARE_INCH)
}

@Serializable
data object USTonSquareFoot : USCustomaryPressure() {
    override val symbol: String = "${UsTonForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = UsTonForce.fromSIUnit(value) / ONE_SQUARE_FOOT
    override fun toSIUnit(value: Decimal): Decimal = UsTonForce.toSIUnit(value * ONE_SQUARE_FOOT)
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
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTonForce.fromSIUnit(value) / ONE_SQUARE_INCH
    override fun toSIUnit(value: Decimal): Decimal = ImperialTonForce.toSIUnit(value * ONE_SQUARE_INCH)
}

@Serializable
data object ImperialTonSquareFoot : UKImperialPressure() {
    override val symbol: String = "${ImperialTonForce.symbol}/${SquareFoot.symbol}"
    override fun fromSIUnit(value: Decimal): Decimal = ImperialTonForce.fromSIUnit(value) / ONE_SQUARE_FOOT
    override fun toSIUnit(value: Decimal): Decimal = ImperialTonForce.toSIUnit(value * ONE_SQUARE_FOOT)
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

internal fun SerializersModuleBuilder.setupForPressure() {
    polymorphic(Pressure::class) {
        registerPressureClasses()
    }
    polymorphic(MetricPressure::class) {
        registerMetricPressureClasses()
    }
    polymorphic(ImperialPressure::class) {
        registerImperialPressureClasses()
    }
    polymorphic(UKImperialPressure::class) {
        registerUKImperialPressureClasses()
    }
    polymorphic(USCustomaryPressure::class) {
        registerUSCustomaryPressureClasses()
    }
}

internal fun PolymorphicModuleBuilder<Pressure>.registerPressureClasses() {
    registerMetricPressureClasses()
    registerImperialPressureClasses()
    registerUKImperialPressureClasses()
    registerUSCustomaryPressureClasses()
}

internal fun PolymorphicModuleBuilder<MetricPressure>.registerMetricPressureClasses() {
    subclass(Atmosphere::class, Atmosphere.serializer())
    subclass(Bar::class, Bar.serializer())
    subclass(Centibar::class, Centibar.serializer())
    subclass(Decabar::class, Decabar.serializer())
    subclass(Decibar::class, Decibar.serializer())
    subclass(Gigabar::class, Gigabar.serializer())
    subclass(Hectobar::class, Hectobar.serializer())
    subclass(Kilobar::class, Kilobar.serializer())
    subclass(Megabar::class, Megabar.serializer())
    subclass(Microbar::class, Microbar.serializer())
    subclass(Millibar::class, Millibar.serializer())
    subclass(Nanobar::class, Nanobar.serializer())
    subclass(Barye::class, Barye.serializer())
    subclass(Centibarye::class, Centibarye.serializer())
    subclass(Decabarye::class, Decabarye.serializer())
    subclass(Decibarye::class, Decibarye.serializer())
    subclass(Gigabarye::class, Gigabarye.serializer())
    subclass(Hectobarye::class, Hectobarye.serializer())
    subclass(Kilobarye::class, Kilobarye.serializer())
    subclass(Megabarye::class, Megabarye.serializer())
    subclass(Microbarye::class, Microbarye.serializer())
    subclass(Millibarye::class, Millibarye.serializer())
    subclass(Nanobarye::class, Nanobarye.serializer())
    subclass(CentimeterOfWater::class, CentimeterOfWater.serializer())
    subclass(MillimeterOfMercury::class, MillimeterOfMercury.serializer())
    subclass(MillimeterOfWater::class, MillimeterOfWater.serializer())
    subclass(Pascal::class, Pascal.serializer())
    subclass(Centipascal::class, Centipascal.serializer())
    subclass(Decapascal::class, Decapascal.serializer())
    subclass(Decipascal::class, Decipascal.serializer())
    subclass(Gigapascal::class, Gigapascal.serializer())
    subclass(Hectopascal::class, Hectopascal.serializer())
    subclass(Kilopascal::class, Kilopascal.serializer())
    subclass(Megapascal::class, Megapascal.serializer())
    subclass(Micropascal::class, Micropascal.serializer())
    subclass(Millipascal::class, Millipascal.serializer())
    subclass(Nanopascal::class, Nanopascal.serializer())
    subclass(Torr::class, Torr.serializer())
    subclass(Centitorr::class, Centitorr.serializer())
    subclass(Decatorr::class, Decatorr.serializer())
    subclass(Decitorr::class, Decitorr.serializer())
    subclass(Gigatorr::class, Gigatorr.serializer())
    subclass(Hectotorr::class, Hectotorr.serializer())
    subclass(Kilotorr::class, Kilotorr.serializer())
    subclass(Megatorr::class, Megatorr.serializer())
    subclass(Microtorr::class, Microtorr.serializer())
    subclass(Millitorr::class, Millitorr.serializer())
    subclass(Nanotorr::class, Nanotorr.serializer())
}

internal fun PolymorphicModuleBuilder<ImperialPressure>.registerImperialPressureClasses() {
    subclass(FootOfWater::class, FootOfWater.serializer())
    subclass(InchOfMercury::class, InchOfMercury.serializer())
    subclass(InchOfWater::class, InchOfWater.serializer())
    subclass(KiloPoundSquareInch::class, KiloPoundSquareInch.serializer())
    subclass(OunceSquareInch::class, OunceSquareInch.serializer())
    subclass(PoundSquareFoot::class, PoundSquareFoot.serializer())
    subclass(PoundSquareInch::class, PoundSquareInch.serializer())
}

internal fun PolymorphicModuleBuilder<UKImperialPressure>.registerUKImperialPressureClasses() {
    subclass(ImperialTonSquareFoot::class, ImperialTonSquareFoot.serializer())
    subclass(ImperialTonSquareInch::class, ImperialTonSquareInch.serializer())
    subclass(UKImperialPressureWrapper::class, UKImperialPressureWrapper.serializer())
}

internal fun PolymorphicModuleBuilder<USCustomaryPressure>.registerUSCustomaryPressureClasses() {
    subclass(KipSquareFoot::class, KipSquareFoot.serializer())
    subclass(KipSquareInch::class, KipSquareInch.serializer())
    subclass(USCustomaryImperialPressureWrapper::class, USCustomaryImperialPressureWrapper.serializer())
    subclass(USTonSquareFoot::class, USTonSquareFoot.serializer())
    subclass(USTonSquareInch::class, USTonSquareInch.serializer())
}
