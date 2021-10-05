package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.Kelvin.KELVIN_FREEZING

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
    override fun toSIUnit(value: Decimal): Decimal = value / MILLIMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * MILLIMETERS_IN_METER.toDecimal()
}

object Centimeter : ScientificUnit.MetricLength("cm"), Serializable {
    const val CENTIMETERS_IN_METER = 100.0
    override fun toSIUnit(value: Decimal): Decimal = value / CENTIMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CENTIMETERS_IN_METER.toDecimal()
}

object Decimeter : ScientificUnit.MetricLength("dm"), Serializable {
    const val DECIMETERS_IN_METER = 10.0
    override fun toSIUnit(value: Decimal): Decimal = value / DECIMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * DECIMETERS_IN_METER.toDecimal()
}

object Meter : ScientificUnit.MetricLength("m"), Serializable {
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

object Decameter : ScientificUnit.MetricLength("dam"), Serializable {
    const val DECAMETERS_IN_METER = 0.1
    override fun toSIUnit(value: Decimal): Decimal = value / DECAMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * DECAMETERS_IN_METER.toDecimal()
}

object Hectometer : ScientificUnit.MetricLength("hm"), Serializable {
    const val HECTOMETERS_IN_METER = 0.01
    override fun toSIUnit(value: Decimal): Decimal = value / HECTOMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * HECTOMETERS_IN_METER.toDecimal()
}

object Kilometer : ScientificUnit.MetricLength("km"), Serializable {
    const val KILOMETERS_IN_METER = 0.001
    override fun toSIUnit(value: Decimal): Decimal = value / KILOMETERS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * KILOMETERS_IN_METER.toDecimal()
}

// Imperial Length

object Inch : ScientificUnit.ImperialLength("\'"), Serializable {
    const val INCHES_IN_METER = 39.37007874015748
    override fun toSIUnit(value: Decimal): Decimal = value / INCHES_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * INCHES_IN_METER.toDecimal()
}

object Foot : ScientificUnit.ImperialLength("\""), Serializable {
    const val FEET_IN_METER = 3.280839895013123
    override fun toSIUnit(value: Decimal): Decimal = value / FEET_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * FEET_IN_METER.toDecimal()
}

object Yard : ScientificUnit.ImperialLength("yd"), Serializable {
    const val YARDS_IN_METER = 1.0936132983377078
    override fun toSIUnit(value: Decimal): Decimal = value / YARDS_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * YARDS_IN_METER.toDecimal()
}

object Mile : ScientificUnit.ImperialLength("mi"), Serializable {
    const val MILES_IN_METER = 0.000621371192237334
    override fun toSIUnit(value: Decimal): Decimal = value / MILES_IN_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * MILES_IN_METER.toDecimal()
}

// Temperature

object Celsius : ScientificUnit.UKImperialTemperature("°C"), Serializable {
    override fun toSIUnit(value: Decimal): Decimal = value + KELVIN_FREEZING.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value - KELVIN_FREEZING.toDecimal()
}

object Kelvin : ScientificUnit.MetricTemperature("K"), Serializable {
    const val KELVIN_FREEZING = 273.15
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

object Fahrenheit : ScientificUnit.USCustomaryTemperature("°F"), Serializable {
    const val FAHRENHEIT_FREEZING = 32.00
    override fun toSIUnit(value: Decimal): Decimal =
        (value - FAHRENHEIT_FREEZING.toDecimal()) * 5.0.toDecimal() / 9.0.toDecimal() + KELVIN_FREEZING.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        (value - KELVIN_FREEZING.toDecimal()) * 9.0.toDecimal() / 5.0.toDecimal() + FAHRENHEIT_FREEZING.toDecimal()
}

// Metric Weight

object Microgram : ScientificUnit.MetricWeight("mcg"), Serializable {
    const val MICROGRAMS_IN_KILOGRAM = 1000000000.0
    override fun toSIUnit(value: Decimal): Decimal = value / MICROGRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * MICROGRAMS_IN_KILOGRAM.toDecimal()
}

object Milligram : ScientificUnit.MetricWeight("mg"), Serializable {
    const val MILLIGRAMS_IN_KILOGRAM = 1000000.0
    override fun toSIUnit(value: Decimal): Decimal = value / MILLIGRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * MILLIGRAMS_IN_KILOGRAM.toDecimal()
}

object Gram : ScientificUnit.MetricWeight("g"), Serializable {
    const val GRAMS_IN_KILOGRAM = 1000.0
    override fun toSIUnit(value: Decimal): Decimal = value / GRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * GRAMS_IN_KILOGRAM.toDecimal()
}

object Kilogram : ScientificUnit.MetricWeight("kg"), Serializable {
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

object Tonne : ScientificUnit.MetricWeight("t"), Serializable {
    const val TONES_IN_KILOGRAM = 0.001
    override fun toSIUnit(value: Decimal): Decimal = value / TONES_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * TONES_IN_KILOGRAM.toDecimal()
}

// Imperial Weight

object Grain : ScientificUnit.ImperialWeight("gr"), Serializable {
    const val GRAINS_IN_KILOGRAM = 15432.358352941432
    override fun toSIUnit(value: Decimal): Decimal = value / GRAINS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * GRAINS_IN_KILOGRAM.toDecimal()
}

object Dram : ScientificUnit.ImperialWeight("dr"), Serializable {
    const val DRAMS_IN_KILOGRAM = 564.3833911932866
    override fun toSIUnit(value: Decimal): Decimal = value / DRAMS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * DRAMS_IN_KILOGRAM.toDecimal()
}

object Ounce : ScientificUnit.ImperialWeight("oz"), Serializable {
    const val OUNCES_IN_KILOGRAM = 35.27396194958041
    override fun toSIUnit(value: Decimal): Decimal = value / OUNCES_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * OUNCES_IN_KILOGRAM.toDecimal()
}

object Pound : ScientificUnit.ImperialWeight("lb"), Serializable {
    const val POUNDS_IN_KILOGRAM = 2.204622621848776
    override fun toSIUnit(value: Decimal): Decimal = value / POUNDS_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * POUNDS_IN_KILOGRAM.toDecimal()
}

object Stone : ScientificUnit.ImperialWeight("st"), Serializable {
    const val STONES_IN_KILOGRAM = 0.1574730444177697
    override fun toSIUnit(value: Decimal): Decimal = value / STONES_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * STONES_IN_KILOGRAM.toDecimal()
}

// also long ton
object ImperialTon : ScientificUnit.UKImperialWeight("ton"), Serializable {
    const val LONG_TONES_IN_KILOGRAM = 0.000984206527611
    override fun toSIUnit(value: Decimal): Decimal = value / LONG_TONES_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * LONG_TONES_IN_KILOGRAM.toDecimal()
}

// also short ton
object UsTon : ScientificUnit.USImperialWeight("ton"), Serializable {
    const val SHORT_TONES_IN_KILOGRAM = 0.001102311310924
    override fun toSIUnit(value: Decimal): Decimal = value / SHORT_TONES_IN_KILOGRAM.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * SHORT_TONES_IN_KILOGRAM.toDecimal()
}

// Metric Volume

object CubicMeter : ScientificUnit.MetricVolume("cu m"), Serializable {
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

object Liter : ScientificUnit.MetricVolume("l"), Serializable {
    const val LITERS_IN_CUBIC_METER = 1000.0
    override fun toSIUnit(value: Decimal): Decimal = value / LITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * LITERS_IN_CUBIC_METER.toDecimal()
}

object Milliliter : ScientificUnit.MetricVolume("ml"), Serializable {
    const val MILLILITERS_IN_CUBIC_METER = 1000000.0
    override fun toSIUnit(value: Decimal): Decimal = value / MILLILITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * MILLILITERS_IN_CUBIC_METER.toDecimal()
}

// Imperial

object CubicInch : ScientificUnit.UKImperialVolume("cu in"), Serializable {
    const val CUBIC_INCHES_IN_CUBIC_METER = 61023.74409473229
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_INCHES_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * CUBIC_INCHES_IN_CUBIC_METER.toDecimal()
}

object CubicFoot : ScientificUnit.UKImperialVolume("cu ft"), Serializable {
    const val CUBIC_FEET_IN_CUBIC_METER = 35.31466672148859
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_FEET_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_FEET_IN_CUBIC_METER.toDecimal()
}

// US Imperial

object UsFluidDram : ScientificUnit.USImperialVolume("fl dr"), Serializable {
    const val US_FLUID_DRAM_IN_CUBIC_METER = 270512.18161474395
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_FLUID_DRAM_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
}

object UsFluidOunce : ScientificUnit.USImperialVolume("fl oz"), Serializable {
    const val US_FLUID_OUNCES_IN_LITER = 33814.022701842994
    override fun toSIUnit(value: Decimal): Decimal = value / US_FLUID_OUNCES_IN_LITER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_FLUID_OUNCES_IN_LITER.toDecimal()
}

object UsLegalCup : ScientificUnit.USImperialVolume("cup"), Serializable {
    const val US_LEGAL_CUPS_IN_CUBIC_METER = 4226.752837730375
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_LEGAL_CUPS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_LEGAL_CUPS_IN_CUBIC_METER.toDecimal()
}

object UsLiquidPint : ScientificUnit.USImperialVolume("pint"), Serializable {
    const val US_PINTS_IN_CUBIC_METER = 2113.376418865187
    override fun toSIUnit(value: Decimal): Decimal = value / US_PINTS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_PINTS_IN_CUBIC_METER.toDecimal()
}

object UsLiquidQuart : ScientificUnit.USImperialVolume("qt"), Serializable {
    const val US_QUARTS_IN_CUBIC_METER = 1056.688209432594
    override fun toSIUnit(value: Decimal): Decimal = value / US_QUARTS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_QUARTS_IN_CUBIC_METER.toDecimal()
}

object UsLiquidGallon : ScientificUnit.USImperialVolume("gal"), Serializable {
    const val US_LIQUID_GALLONS_IN_CUBIC_METER = 264.1720523581484
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_LIQUID_GALLONS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_LIQUID_GALLONS_IN_CUBIC_METER.toDecimal()
}

// UK Imperial

object ImperialFluidDram : ScientificUnit.UKImperialVolume("fl dr"), Serializable {
    const val IMPERIAL_FLUID_DRAM_IN_CUBIC_METER = 281560.63782283233
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_FLUID_DRAM_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
}

object ImperialFluidOunce : ScientificUnit.UKImperialVolume("fl oz"), Serializable {
    const val IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER = 35195.07972785405
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER.toDecimal()
}

object ImperialCup : ScientificUnit.UKImperialVolume("cup"), Serializable {
    const val IMPERIAL_CUPS_IN_CUBIC_METER = 4000.0
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_CUPS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_CUPS_IN_CUBIC_METER.toDecimal()
}

object ImperialPint : ScientificUnit.UKImperialVolume("pt"), Serializable {
    const val IMPERIAL_PINTS_IN_CUBIC_METER = 1759.753986392702
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_PINTS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_PINTS_IN_CUBIC_METER.toDecimal()
}

object ImperialQuart : ScientificUnit.UKImperialVolume("qt"), Serializable {
    const val IMPERIAL_QUARTS_IN_CUBIC_METER = 879.8769931963512
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_QUARTS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_QUARTS_IN_CUBIC_METER.toDecimal()
}

object ImperialGallon : ScientificUnit.UKImperialVolume("gal"), Serializable {
    const val IMPERIAL_GALLONS_IN_CUBIC_METER = 219.96924829908778
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_GALLONS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_GALLONS_IN_CUBIC_METER.toDecimal()
}

fun <Unit : MeasurementType> ScientificUnit<*, Unit>.convert(
    value: Decimal,
    to: ScientificUnit<*, Unit>
): Decimal = to.fromSIUnit(toSIUnit(value))
