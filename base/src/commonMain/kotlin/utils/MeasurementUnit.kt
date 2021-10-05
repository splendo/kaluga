package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.Centimeter.CENTIMETERS_IN_METER
import com.splendo.kaluga.base.utils.CubicFoot.CUBIC_FEET_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.CubicInch.CUBIC_INCHES_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.Decameter.DECAMETERS_IN_METER
import com.splendo.kaluga.base.utils.Decimeter.DECIMETERS_IN_METER
import com.splendo.kaluga.base.utils.Dram.DRAMS_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Fahrenheit.FAHRENHEIT_FREEZING
import com.splendo.kaluga.base.utils.Foot.FEET_IN_METER
import com.splendo.kaluga.base.utils.Grain.GRAINS_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Gram.GRAMS_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Hectometer.HECTOMETERS_IN_METER
import com.splendo.kaluga.base.utils.ImperialCup.IMPERIAL_CUPS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.ImperialFluidDram.IMPERIAL_FLUID_DRAM_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.ImperialFluidOunce.IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.ImperialGallon.IMPERIAL_GALLONS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.ImperialPint.IMPERIAL_PINTS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.ImperialQuart.IMPERIAL_QUARTS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.ImperialTon.LONG_TONES_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Inch.INCHES_IN_METER
import com.splendo.kaluga.base.utils.Kelvin.KELVIN_FREEZING
import com.splendo.kaluga.base.utils.Kilometer.KILOMETERS_IN_METER
import com.splendo.kaluga.base.utils.Liter.LITERS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.Microgram.MICROGRAMS_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Mile.MILES_IN_METER
import com.splendo.kaluga.base.utils.Milligram.MILLIGRAMS_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Milliliter.MILLILITERS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.Millimeter.MILLIMETERS_IN_METER
import com.splendo.kaluga.base.utils.Ounce.OUNCES_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Pound.POUNDS_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Stone.STONES_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Tonne.TONES_IN_KILOGRAM
import com.splendo.kaluga.base.utils.UsFluidDram.US_FLUID_DRAM_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.UsFluidOunce.US_FLUID_OUNCES_IN_LITER
import com.splendo.kaluga.base.utils.UsLegalCup.US_LEGAL_CUPS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.UsLiquidGallon.US_LIQUID_GALLONS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.UsLiquidPint.US_PINTS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.UsLiquidQuart.US_QUARTS_IN_CUBIC_METER
import com.splendo.kaluga.base.utils.UsTon.SHORT_TONES_IN_KILOGRAM
import com.splendo.kaluga.base.utils.Yard.YARDS_IN_METER

sealed interface MeasurementSystem {
    interface Metric : MeasurementSystem
    interface USCustomary : MeasurementSystem
    interface UKImperial : MeasurementSystem
    interface Imperial : USCustomary, UKImperial
}

sealed interface MeasurementType {
    interface Length : MeasurementType
    interface Temperature : MeasurementType
    interface Weight : MeasurementType
    interface Volume : MeasurementType
}

sealed class ScientificUnit<System : MeasurementSystem, Type : MeasurementType> : Serializable {
    abstract val symbol: String

    abstract fun toSIUnit(value: Decimal): Decimal
    abstract fun fromSIUnit(value: Decimal): Decimal

    sealed class Length<System : MeasurementSystem> :
        ScientificUnit<System, MeasurementType.Length>(), Serializable

    sealed class Temperature<System : MeasurementSystem> :
        ScientificUnit<System, MeasurementType.Temperature>(), Serializable

    sealed class Weight<System : MeasurementSystem> :
        ScientificUnit<System, MeasurementType.Weight>(), Serializable

    sealed class Volume<System : MeasurementSystem> :
        ScientificUnit<System, MeasurementType.Volume>(), Serializable

    sealed class MetricLength(override val symbol: String) :
        Length<MeasurementSystem.Metric>(), Serializable

    sealed class ImperialLength(override val symbol: String) :
        Length<MeasurementSystem.Imperial>(), Serializable

    sealed class MetricTemperature(override val symbol: String) :
        Temperature<MeasurementSystem.Metric>(), Serializable

    sealed class USCustomaryTemperature(override val symbol: String) :
        Temperature<MeasurementSystem.USCustomary>(), Serializable

    sealed class UKImperialTemperature(override val symbol: String) :
        Temperature<MeasurementSystem.UKImperial>(), Serializable

    sealed class MetricWeight(override val symbol: String) :
        Weight<MeasurementSystem.Metric>(), Serializable

    sealed class ImperialWeight(override val symbol: String) :
        Weight<MeasurementSystem.Imperial>(), Serializable

    sealed class USImperialWeight(override val symbol: String) :
        Weight<MeasurementSystem.USCustomary>(), Serializable

    sealed class UKImperialWeight(override val symbol: String) :
        Weight<MeasurementSystem.UKImperial>(), Serializable

    sealed class MetricVolume(override val symbol: String) :
        Volume<MeasurementSystem.Metric>(), Serializable

    sealed class USImperialVolume(override val symbol: String) :
        Volume<MeasurementSystem.USCustomary>(), Serializable

    sealed class UKImperialVolume(override val symbol: String) :
        Volume<MeasurementSystem.UKImperial>(), Serializable
}

// Metric Length

object Millimeter : ScientificUnit.MetricLength("mm"), Serializable {
    const val MILLIMETERS_IN_METER = 1000.0
    override fun fromSIUnit(value: Decimal): Decimal = value * MILLIMETERS_IN_METER.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / MILLIMETERS_IN_METER.toDecimal()
}

object Centimeter : ScientificUnit.MetricLength("cm"), Serializable {
    const val CENTIMETERS_IN_METER = 100.0
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Centimeter, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Centimeter, outUnit)
}

object Decimeter : ScientificUnit.MetricLength("dm"), Serializable {
    const val DECIMETERS_IN_METER = 10.0
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Decimeter, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Decimeter, outUnit)
}

object Meter : ScientificUnit.MetricLength("m"), Serializable {
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Meter, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Meter, outUnit)
}

object Decameter : ScientificUnit.MetricLength("dam"), Serializable {
    const val DECAMETERS_IN_METER = 0.1
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Decameter, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Decameter, outUnit)
}

object Hectometer : ScientificUnit.MetricLength("hm"), Serializable {
    const val HECTOMETERS_IN_METER = 0.01
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Hectometer, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Hectometer, outUnit)
}

object Kilometer : ScientificUnit.MetricLength("km"), Serializable {
    const val KILOMETERS_IN_METER = 0.001
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Kilometer, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Kilometer, outUnit)
}

// Imperial Length

object Inch : ScientificUnit.ImperialLength("\'"), Serializable {
    const val INCHES_IN_METER = 39.37007874015748
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Inch, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Inch, outUnit)
}

object Foot : ScientificUnit.ImperialLength("\""), Serializable {
    const val FEET_IN_METER = 3.280839895013123
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Foot, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Foot, outUnit)
}

object Yard : ScientificUnit.ImperialLength("yd"), Serializable {
    const val YARDS_IN_METER = 1.0936132983377078
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Yard, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Yard, outUnit)
}

object Mile : ScientificUnit.ImperialLength("mi"), Serializable {
    const val MILES_IN_METER = 0.000621371192237334
    override fun toMetric(value: Double, outUnit: MetricLength): Double =
        convert(value, Mile, outUnit)

    override fun toImperial(value: Double, outUnit: ImperialLength): Double =
        convert(value, Mile, outUnit)
}

// Temperature

object Celsius : ScientificUnit.MetricTemperature("°C"), Serializable {
    override fun toMetric(value: Double, outUnit: MetricTemperature) =
        convert(value, Celsius, outUnit)

    override fun toUSCustomary(value: Double, outUnit: USCustomaryTemperature): Double =
        convert(value, Celsius, outUnit)

    override fun toUKImperialTemperature(value: Double, outUnit: UKImperialTemperature): Double =
        convert(value, Celsius, outUnit)
}

object Kelvin : ScientificUnit.MetricTemperature("K"), Serializable {
    const val KELVIN_FREEZING = 273.15
    override fun toMetric(value: Double, outUnit: MetricTemperature) =
        convert(value, Kelvin, outUnit)

    override fun toUSCustomary(value: Double, outUnit: USCustomaryTemperature): Double =
        convert(value, Kelvin, outUnit)

    override fun toUKImperialTemperature(value: Double, outUnit: UKImperialTemperature): Double =
        convert(value, Kelvin, outUnit)
}

object Fahrenheit : ScientificUnit.USCustomaryTemperature("°F"), Serializable {
    const val FAHRENHEIT_FREEZING = 32.00
    override fun toMetric(value: Double, outUnit: MetricTemperature): Double =
        convert(value, Fahrenheit, outUnit)

    override fun toUSCustomary(value: Double, outUnit: USCustomaryTemperature): Double =
        convert(value, Fahrenheit, outUnit)

    override fun toUKImperialTemperature(value: Double, outUnit: UKImperialTemperature): Double =
        convert(value, Fahrenheit, outUnit)
}

// Metric Weight

object Microgram : ScientificUnit.MetricWeight("mcg"), Serializable {
    const val MICROGRAMS_IN_KILOGRAM = 1000000000.0
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Microgram, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Microgram, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Microgram, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Microgram, outUnit)
}

object Milligram : ScientificUnit.MetricWeight("mg"), Serializable {
    const val MILLIGRAMS_IN_KILOGRAM = 1000000.0
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Milligram, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Milligram, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Milligram, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Milligram, outUnit)
}

object Gram : ScientificUnit.MetricWeight("g"), Serializable {
    const val GRAMS_IN_KILOGRAM = 1000.0
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Gram, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Gram, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Gram, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Gram, outUnit)
}

object Kilogram : ScientificUnit.MetricWeight("kg"), Serializable {
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Kilogram, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Kilogram, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Kilogram, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Kilogram, outUnit)
}

object Tonne : ScientificUnit.MetricWeight("t"), Serializable {
    const val TONES_IN_KILOGRAM = 0.001
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Tonne, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Tonne, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Tonne, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Tonne, outUnit)
}

// Imperial Weight

object Grain : ScientificUnit.ImperialWeight("gr"), Serializable {
    const val GRAINS_IN_KILOGRAM = 15432.358352941432
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Grain, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Grain, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Grain, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Grain, outUnit)
}

object Dram : ScientificUnit.ImperialWeight("dr"), Serializable {
    const val DRAMS_IN_KILOGRAM = 564.3833911932866
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Dram, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Dram, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Dram, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Dram, outUnit)
}

object Ounce : ScientificUnit.ImperialWeight("oz"), Serializable {
    const val OUNCES_IN_KILOGRAM = 35.27396194958041
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Ounce, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Ounce, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Ounce, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Ounce, outUnit)
}

object Pound : ScientificUnit.ImperialWeight("lb"), Serializable {
    const val POUNDS_IN_KILOGRAM = 2.204622621848776
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Pound, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Pound, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Pound, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Pound, outUnit)
}

object Stone : ScientificUnit.ImperialWeight("st"), Serializable {
    const val STONES_IN_KILOGRAM = 0.1574730444177697
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, Stone, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, Stone, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, Stone, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, Stone, outUnit)
}

// also long ton
object ImperialTon : ScientificUnit.UKImperialWeight("ton"), Serializable {
    const val LONG_TONES_IN_KILOGRAM = 0.000984206527611
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, ImperialTon, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, ImperialTon, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, ImperialTon, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, ImperialTon, outUnit)
}

// also short ton
object UsTon : ScientificUnit.USImperialWeight("ton"), Serializable {
    const val SHORT_TONES_IN_KILOGRAM = 0.001102311310924
    override fun toMetricWeight(value: Double, outUnit: MetricWeight): Double =
        convert(value, UsTon, outUnit)

    override fun toImperialWeight(value: Double, outUnit: ImperialWeight): Double =
        convert(value, UsTon, outUnit)

    override fun toUSCustomaryWeight(value: Double, outUnit: USImperialWeight): Double =
        convert(value, UsTon, outUnit)

    override fun toUKImperialWeight(value: Double, outUnit: UKImperialWeight): Double =
        convert(value, UsTon, outUnit)
}

// Metric Volume

object CubicMeter : ScientificUnit.MetricVolume("cu m"), Serializable {
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, CubicMeter, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, CubicMeter, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, CubicMeter, outUnit)
}

object Liter : ScientificUnit.MetricVolume("l"), Serializable {
    const val LITERS_IN_CUBIC_METER = 1000.0
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, Liter, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, Liter, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, Liter, outUnit)
}

object Milliliter : ScientificUnit.MetricVolume("ml"), Serializable {
    const val MILLILITERS_IN_CUBIC_METER = 1000000.0
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, Milliliter, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, Milliliter, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, Milliliter, outUnit)
}

// Imperial

object CubicInch : ScientificUnit.UKImperialVolume("cu in"), Serializable {
    const val CUBIC_INCHES_IN_CUBIC_METER = 61023.74409473229
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, CubicInch, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, CubicInch, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, CubicInch, outUnit)
}

object CubicFoot : ScientificUnit.UKImperialVolume("cu ft"), Serializable {
    const val CUBIC_FEET_IN_CUBIC_METER = 35.31466672148859
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, CubicFoot, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, CubicFoot, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, CubicFoot, outUnit)
}

// US Imperial

object UsFluidDram : ScientificUnit.USImperialVolume("fl dr"), Serializable {
    const val US_FLUID_DRAM_IN_CUBIC_METER = 270512.18161474395
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, UsFluidDram, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, UsFluidDram, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, UsFluidDram, outUnit)
}

object UsFluidOunce : ScientificUnit.USImperialVolume("fl oz"), Serializable {
    const val US_FLUID_OUNCES_IN_LITER = 33814.022701842994
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, UsFluidOunce, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, UsFluidOunce, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, UsFluidOunce, outUnit)
}

object UsLegalCup : ScientificUnit.USImperialVolume("cup"), Serializable {
    const val US_LEGAL_CUPS_IN_CUBIC_METER = 4226.752837730375
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, UsLegalCup, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, UsLegalCup, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, UsLegalCup, outUnit)
}

object UsLiquidPint : ScientificUnit.USImperialVolume("pint"), Serializable {
    const val US_PINTS_IN_CUBIC_METER = 2113.376418865187
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, UsLiquidPint, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, UsLiquidPint, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, UsLiquidPint, outUnit)
}

object UsLiquidQuart : ScientificUnit.USImperialVolume("qt"), Serializable {
    const val US_QUARTS_IN_CUBIC_METER = 1056.688209432594
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, UsLiquidQuart, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, UsLiquidQuart, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, UsLiquidQuart, outUnit)
}

object UsLiquidGallon : ScientificUnit.USImperialVolume("gal"), Serializable {
    const val US_LIQUID_GALLONS_IN_CUBIC_METER = 264.1720523581484
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, UsLiquidGallon, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, UsLiquidGallon, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, UsLiquidGallon, outUnit)
}

// UK Imperial

object ImperialFluidDram : ScientificUnit.UKImperialVolume("fl dr"), Serializable {
    const val IMPERIAL_FLUID_DRAM_IN_CUBIC_METER = 281560.63782283233
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, ImperialFluidDram, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, ImperialFluidDram, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, ImperialFluidDram, outUnit)
}

object ImperialFluidOunce : ScientificUnit.UKImperialVolume("fl oz"), Serializable {
    const val IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER = 35195.07972785405
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, ImperialFluidOunce, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, ImperialFluidOunce, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, ImperialFluidOunce, outUnit)
}

object ImperialCup : ScientificUnit.UKImperialVolume("cup"), Serializable {
    const val IMPERIAL_CUPS_IN_CUBIC_METER = 4000.0
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, ImperialCup, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, ImperialCup, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, ImperialCup, outUnit)
}

object ImperialPint : ScientificUnit.UKImperialVolume("pt"), Serializable {
    const val IMPERIAL_PINTS_IN_CUBIC_METER = 1759.753986392702
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, ImperialPint, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, ImperialPint, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, ImperialPint, outUnit)
}

object ImperialQuart : ScientificUnit.UKImperialVolume("qt"), Serializable {
    const val IMPERIAL_QUARTS_IN_CUBIC_METER = 879.8769931963512
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, ImperialQuart, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, ImperialQuart, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, ImperialQuart, outUnit)
}

object ImperialGallon : ScientificUnit.UKImperialVolume("gal"), Serializable {
    const val IMPERIAL_GALLONS_IN_CUBIC_METER = 219.96924829908778
    override fun toMetricVolume(value: Double, outUnit: MetricVolume): Double =
        convert(value, ImperialGallon, outUnit)

    override fun toUSImperialVolume(value: Double, outUnit: USImperialVolume): Double =
        convert(value, ImperialGallon, outUnit)

    override fun toUKImperialVolume(value: Double, outUnit: UKImperialVolume): Double =
        convert(value, ImperialGallon, outUnit)
}

fun <Unit : MeasurementType> ScientificUnit<*, Unit>.convert(value: Decimal, to: ScientificUnit<*, Unit>): Decimal = to.fromSIUnit(toSIUnit(value))
fun <Unit : MeasurementType> ScientificUnit<MeasurementSystem.Metric, Unit>.toUSCustomary(value: Decimal, to: ScientificUnit<MeasurementSystem.USCustomary, Unit>): Decimal { return value }

fun test() {
    Meter.convert((10.0).toDecimal(), Fahrenheit)
}

// /**
//  * Translates from other systems to base SI
//  *
//  * @param value The non SI value to translate
//  * @param inUnit The type of the input [value] we are translating from
//  */
// private fun toBaseSi(value: Decimal, inUnit: ScientificUnit<*, *>): Decimal {
//     return when (inUnit) {
//         Decimeter -> value / DECIMETERS_IN_METER.toDecimal()
//         Centimeter -> value / CENTIMETERS_IN_METER.toDecimal()
//         Millimeter -> value / MILLIMETERS_IN_METER.toDecimal()
//         Decameter -> value / DECAMETERS_IN_METER.toDecimal()
//         Hectometer -> value / HECTOMETERS_IN_METER.toDecimal()
//         Kilometer -> value / KILOMETERS_IN_METER.toDecimal()
//         Meter -> value
//         Inch -> value / INCHES_IN_METER.toDecimal()
//         Foot -> value / FEET_IN_METER.toDecimal()
//         Yard -> value / YARDS_IN_METER.toDecimal()
//         Mile -> value / MILES_IN_METER.toDecimal()
//         Celsius -> value + KELVIN_FREEZING.toDecimal()
//         Kelvin -> value
//         Fahrenheit -> (value - FAHRENHEIT_FREEZING.toDecimal()) * 5.0.toDecimal() / 9.0.toDecimal() + KELVIN_FREEZING.toDecimal()
//         Tonne -> value / TONES_IN_KILOGRAM.toDecimal()
//         Kilogram -> value
//         Gram -> value / GRAMS_IN_KILOGRAM.toDecimal()
//         Milligram -> value / MILLIGRAMS_IN_KILOGRAM.toDecimal()
//         Microgram -> value / MICROGRAMS_IN_KILOGRAM.toDecimal()
//         Stone -> value / STONES_IN_KILOGRAM.toDecimal()
//         Pound -> value / POUNDS_IN_KILOGRAM.toDecimal()
//         Ounce -> value / OUNCES_IN_KILOGRAM.toDecimal()
//         Dram -> value / DRAMS_IN_KILOGRAM.toDecimal()
//         Grain -> value / GRAINS_IN_KILOGRAM.toDecimal()
//         UsTon -> value / SHORT_TONES_IN_KILOGRAM.toDecimal()
//         ImperialTon -> value / LONG_TONES_IN_KILOGRAM.toDecimal()
//         CubicMeter -> value
//         Liter -> value / LITERS_IN_CUBIC_METER.toDecimal()
//         Milliliter -> value / MILLILITERS_IN_CUBIC_METER.toDecimal()
//         UsLiquidGallon -> value / US_LIQUID_GALLONS_IN_CUBIC_METER.toDecimal()
//         UsLiquidQuart -> value / US_QUARTS_IN_CUBIC_METER.toDecimal()
//         UsLiquidPint -> value / US_PINTS_IN_CUBIC_METER.toDecimal()
//         UsLegalCup -> value / US_LEGAL_CUPS_IN_CUBIC_METER.toDecimal()
//         UsFluidOunce -> value / US_FLUID_OUNCES_IN_LITER.toDecimal()
//         UsFluidDram -> value / US_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
//         ImperialGallon -> value / IMPERIAL_GALLONS_IN_CUBIC_METER.toDecimal()
//         ImperialQuart -> value / IMPERIAL_QUARTS_IN_CUBIC_METER.toDecimal()
//         ImperialPint -> value / IMPERIAL_PINTS_IN_CUBIC_METER.toDecimal()
//         ImperialCup -> value / IMPERIAL_CUPS_IN_CUBIC_METER.toDecimal()
//         ImperialFluidOunce -> value / IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER.toDecimal()
//         ImperialFluidDram -> value / IMPERIAL_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
//         CubicFoot -> value / CUBIC_FEET_IN_CUBIC_METER.toDecimal()
//         CubicInch -> value / CUBIC_INCHES_IN_CUBIC_METER.toDecimal()
//     }
// }
//
// /**
//  * Translates from base SI to other systems
//  *
//  * @param value Value in base SI system
//  * @param toUnit The type of the return value
//  */
// private fun fromBaseSi(value: Decimal, toUnit: ScientificUnit<*, *>): Double {
//     return when (toUnit) {
//         Decimeter -> value * DECIMETERS_IN_METER.toDecimal()
//         Centimeter -> value * CENTIMETERS_IN_METER.toDecimal()
//         Millimeter -> value * MILLIMETERS_IN_METER.toDecimal()
//         Decameter -> value * DECAMETERS_IN_METER.toDecimal()
//         Hectometer -> value * HECTOMETERS_IN_METER.toDecimal()
//         Kilometer -> value * KILOMETERS_IN_METER.toDecimal()
//         Meter -> value
//         Inch -> value * INCHES_IN_METER.toDecimal()
//         Foot -> value * FEET_IN_METER.toDecimal()
//         Yard -> value * YARDS_IN_METER.toDecimal()
//         Mile -> value * MILES_IN_METER.toDecimal()
//         Celsius -> value - KELVIN_FREEZING.toDecimal()
//         Kelvin -> value
//         Fahrenheit -> (value - KELVIN_FREEZING.toDecimal()) * 9.0.toDecimal() / 5.0.toDecimal() + FAHRENHEIT_FREEZING.toDecimal()
//         Tonne -> value * TONES_IN_KILOGRAM.toDecimal()
//         Kilogram -> value
//         Gram -> value * GRAMS_IN_KILOGRAM.toDecimal()
//         Milligram -> value * MILLIGRAMS_IN_KILOGRAM.toDecimal()
//         Microgram -> value * MICROGRAMS_IN_KILOGRAM.toDecimal()
//         Stone -> value * STONES_IN_KILOGRAM.toDecimal()
//         Pound -> value * POUNDS_IN_KILOGRAM.toDecimal()
//         Ounce -> value * OUNCES_IN_KILOGRAM.toDecimal()
//         Dram -> value * DRAMS_IN_KILOGRAM.toDecimal()
//         Grain -> value * GRAINS_IN_KILOGRAM.toDecimal()
//         UsTon -> value * SHORT_TONES_IN_KILOGRAM.toDecimal()
//         ImperialTon -> value * LONG_TONES_IN_KILOGRAM.toDecimal()
//         CubicMeter -> value
//         Liter -> value * LITERS_IN_CUBIC_METER.toDecimal()
//         Milliliter -> value * MILLILITERS_IN_CUBIC_METER.toDecimal()
//         UsLiquidGallon -> value * US_LIQUID_GALLONS_IN_CUBIC_METER.toDecimal()
//         UsLiquidQuart -> value * US_QUARTS_IN_CUBIC_METER.toDecimal()
//         UsLiquidPint -> value * US_PINTS_IN_CUBIC_METER.toDecimal()
//         UsLegalCup -> value * US_LEGAL_CUPS_IN_CUBIC_METER.toDecimal()
//         UsFluidOunce -> value * US_FLUID_OUNCES_IN_LITER.toDecimal()
//         UsFluidDram -> value * US_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
//         ImperialGallon -> value * IMPERIAL_GALLONS_IN_CUBIC_METER.toDecimal()
//         ImperialQuart -> value * IMPERIAL_QUARTS_IN_CUBIC_METER.toDecimal()
//         ImperialPint -> value * IMPERIAL_PINTS_IN_CUBIC_METER.toDecimal()
//         ImperialCup -> value * IMPERIAL_CUPS_IN_CUBIC_METER.toDecimal()
//         ImperialFluidOunce -> value * IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER.toDecimal()
//         ImperialFluidDram -> value * IMPERIAL_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
//         CubicFoot -> value * CUBIC_FEET_IN_CUBIC_METER.toDecimal()
//         CubicInch -> value * CUBIC_INCHES_IN_CUBIC_METER.toDecimal()
//     }.toDouble()
// }
//
// private fun fromTo(
//     value: Decimal,
//     inputType: ScientificUnit<*, *>,
//     outputType: ScientificUnit<*, *>
// ) = fromBaseSi(toBaseSi(value, inputType), outputType)
//
// fun convert(value: Double, from: ScientificUnit.Length<*>, to: ScientificUnit.Length<*>) =
//     fromTo(value.toDecimal(), from, to)
//
// fun convert(value: Double, from: ScientificUnit.Weight<*>, to: ScientificUnit.Weight<*>) =
//     fromTo(value.toDecimal(), from, to)
//
// fun convert(value: Double, from: ScientificUnit.Temperature<*>, to: ScientificUnit.Temperature<*>) =
//     fromTo(value.toDecimal(), from, to)
//
// fun convert(value: Double, from: ScientificUnit.Volume<*>, to: ScientificUnit.Volume<*>) =
//     fromTo(value.toDecimal(), from, to)
