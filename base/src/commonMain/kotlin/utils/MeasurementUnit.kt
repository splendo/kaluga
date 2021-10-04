package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.Decimals.toDecimal
import com.splendo.kaluga.base.utils.Decimals.toDouble

sealed interface MeasurementSystem {
    interface Metric : MeasurementSystem
    interface USImperial : MeasurementSystem
    interface UKImperial : MeasurementSystem
    interface Imperial : USImperial, UKImperial
}

sealed interface MeasurementType {
    interface Length : MeasurementType
    interface Temperature : MeasurementType
    interface Weight : MeasurementType
    interface Volume : MeasurementType
}

sealed class ScientificUnit<System : MeasurementSystem, Type : MeasurementType> : Serializable {
    abstract val symbol: String

    sealed class Length<System : MeasurementSystem> :
        ScientificUnit<System, MeasurementType.Length>(), Serializable

    sealed class Temperature<System : MeasurementSystem> :
        ScientificUnit<System, MeasurementType.Temperature>(), Serializable

    sealed class Weight<System : MeasurementSystem> :
        ScientificUnit<System, MeasurementType.Weight>(), Serializable

    sealed class Volume<System : MeasurementSystem> :
        ScientificUnit<System, MeasurementType.Volume>(), Serializable

    sealed class MetricLength(override val symbol: String) :
        Length<MeasurementSystem.Metric>(),
        Serializable

    sealed class ImperialLength(override val symbol: String) :
        Length<MeasurementSystem.Imperial>(),
        Serializable

    sealed class MetricTemperature(override val symbol: String) :
        Temperature<MeasurementSystem.Metric>(), Serializable

    sealed class USImperialTemperature(override val symbol: String) :
        Temperature<MeasurementSystem.USImperial>(), Serializable

    sealed class UKImperialTemperature(override val symbol: String) :
        Temperature<MeasurementSystem.UKImperial>(), Serializable

    sealed class MetricWeight(override val symbol: String) :
        Weight<MeasurementSystem.Metric>(),
        Serializable

    sealed class ImperialWeight(override val symbol: String) :
        Weight<MeasurementSystem.Imperial>(),
        Serializable

    sealed class USImperialWeight(override val symbol: String) :
        Weight<MeasurementSystem.USImperial>(), Serializable

    sealed class UKImperialWeight(override val symbol: String) :
        Weight<MeasurementSystem.UKImperial>(), Serializable

    sealed class MetricVolume(override val symbol: String) :
        Volume<MeasurementSystem.Metric>(),
        Serializable

    sealed class USImperialVolume(override val symbol: String) :
        Volume<MeasurementSystem.USImperial>(), Serializable

    sealed class UKImperialVolume(override val symbol: String) :
        Volume<MeasurementSystem.UKImperial>(), Serializable
}

object Decimeter : ScientificUnit.MetricLength("dm"), Serializable

object Centimeter : ScientificUnit.MetricLength("cm"), Serializable

object Millimeter : ScientificUnit.MetricLength("mm"), Serializable

object Decameter : ScientificUnit.MetricLength("dam"), Serializable

object Hectometer : ScientificUnit.MetricLength("hm"), Serializable

object Kilometer : ScientificUnit.MetricLength("km"), Serializable

object Meter : ScientificUnit.MetricLength("m"), Serializable

object Inch : ScientificUnit.ImperialLength("\'"), Serializable

object Foot : ScientificUnit.ImperialLength("\""), Serializable

object Yard : ScientificUnit.ImperialLength("yd"), Serializable

object Mile : ScientificUnit.ImperialLength("mi"), Serializable

object Celsius : ScientificUnit.MetricTemperature("°C"), Serializable

object Kelvin : ScientificUnit.MetricTemperature("K"), Serializable

object Fahrenheit : ScientificUnit.USImperialTemperature("°F"), Serializable

object Tonne : ScientificUnit.MetricWeight("t"), Serializable

object Kilogram : ScientificUnit.MetricWeight("kg"), Serializable

object Gram : ScientificUnit.MetricWeight("g"), Serializable

object Milligram : ScientificUnit.MetricWeight("mg"), Serializable

object Microgram : ScientificUnit.MetricWeight("mcg"), Serializable

object ImperialTon : ScientificUnit.UKImperialWeight("ton"), Serializable // also long ton

object UsTon : ScientificUnit.USImperialWeight("ton"), Serializable // also short ton

object Stone : ScientificUnit.ImperialWeight("st"), Serializable

object Pound : ScientificUnit.ImperialWeight("lb"), Serializable

object Ounce : ScientificUnit.ImperialWeight("oz"), Serializable

object Dram : ScientificUnit.ImperialWeight("dr"), Serializable

object Grain : ScientificUnit.ImperialWeight("gr"), Serializable

object UsLiquidGallon : ScientificUnit.USImperialVolume("gal"), Serializable

object UsLiquidQuart : ScientificUnit.USImperialVolume("qt"), Serializable

object UsLiquidPint : ScientificUnit.USImperialVolume("pint"), Serializable

object UsLegalCup : ScientificUnit.USImperialVolume("cup"), Serializable

object UsFluidOunce : ScientificUnit.USImperialVolume("fl oz"), Serializable

object UsFluidDram : ScientificUnit.USImperialVolume("fl dr"), Serializable

object CubicMeter : ScientificUnit.MetricVolume("cu m"), Serializable

object Liter : ScientificUnit.MetricVolume("l"), Serializable

object Milliliter : ScientificUnit.MetricVolume("ml"), Serializable

object ImperialGallon : ScientificUnit.UKImperialVolume("gal"), Serializable

object ImperialQuart : ScientificUnit.UKImperialVolume("qt"), Serializable

object ImperialPint : ScientificUnit.UKImperialVolume("pt"), Serializable

object ImperialCup : ScientificUnit.UKImperialVolume("cup"), Serializable

object ImperialFluidOunce : ScientificUnit.UKImperialVolume("fl oz"), Serializable

object ImperialFluidDram : ScientificUnit.UKImperialVolume("fl dr"), Serializable

object CubicFoot : ScientificUnit.UKImperialVolume("cu ft"), Serializable

object CubicInch : ScientificUnit.UKImperialVolume("cu in"), Serializable

private val MILLIMETERS_IN_METER = 1000.0.toDecimal()
private val CENTIMETERS_IN_METER = 100.0.toDecimal()
private val KILOMETERS_IN_METER = 0.001.toDecimal()
private val INCHES_IN_METER = 39.37007874015748.toDecimal()
private val FEET_IN_METER = 3.280839895013123.toDecimal()
private val YARDS_IN_METER = 1.0936132983377078.toDecimal()
private val MILES_IN_METER = 0.000621371192237334.toDecimal()

private val MILLIGRAMS_IN_KILOGRAM = 1000000.0.toDecimal()
private val GRAMS_IN_KILOGRAM = 1000.0.toDecimal()
private val TONES_IN_KILOGRAM = 0.001.toDecimal()
private val DRAMS_IN_KILOGRAM = 564.3833911932866.toDecimal()
private val GRAINS_IN_KILOGRAM = 15432.358352941432.toDecimal()
private val OUNCES_IN_KILOGRAM = 35.27396194958041.toDecimal()
private val POUNDS_IN_KILOGRAM = 2.204622621848776.toDecimal()
private val STONES_IN_KILOGRAM = 0.1574730444177697.toDecimal()
private val UK_TONES_IN_KILOGRAM = 0.000984206527611.toDecimal()
private val US_TONES_IN_KILOGRAM = 0.001102311310924.toDecimal()

private val MILLILITERS_IN_LITER = 1000.0.toDecimal()
private val UK_FLUID_OUNCES_IN_LITER = 35.1951.toDecimal()
private val UK_PINTS_IN_LITER = 1.75975.toDecimal()
private val UK_QUARTS_IN_LITER = 0.879877.toDecimal()
private val UK_GALLONS_IN_LITER = 0.219969.toDecimal()
private val US_FLUID_OUNCES_IN_LITER = 33.814.toDecimal()
private val US_PINTS_IN_LITER = 2.11338.toDecimal()
private val US_QUARTS_IN_LITER = 1.05669.toDecimal()
private val US_GALLONS_IN_LITER = 0.264172.toDecimal()

private val KELVIN_FREEZING = 273.15.toDecimal()
private val FAHRENHEIT_FREEZING = 32.00.toDecimal()

/**
 * Translates from other systems to base SI
 *
 * @param value The non SI value to translate
 * @param inUnit The type of the input [value] we are translating from
 */
private fun toBaseSi(value: Decimal, inUnit: ScientificUnit<*, *>): Decimal {
    return when (inUnit) {
        Decimeter -> TODO()
        Centimeter -> value / CENTIMETERS_IN_METER
        Millimeter -> value / MILLIMETERS_IN_METER
        Decameter -> TODO()
        Hectometer -> TODO()
        Kilometer -> value / KILOMETERS_IN_METER
        Meter -> value
        Inch -> value / INCHES_IN_METER
        Foot -> value / FEET_IN_METER
        Yard -> value / YARDS_IN_METER
        Mile -> value / MILES_IN_METER
        Celsius -> value + KELVIN_FREEZING
        Kelvin -> value
        Fahrenheit -> (value - FAHRENHEIT_FREEZING) * 5.0.toDecimal() / 9.0.toDecimal() + KELVIN_FREEZING
        Tonne -> value / TONES_IN_KILOGRAM
        Kilogram -> value
        Gram -> value / GRAMS_IN_KILOGRAM
        Milligram -> value / MILLIGRAMS_IN_KILOGRAM
        Microgram -> TODO()
        Stone -> value / STONES_IN_KILOGRAM
        Pound -> value / POUNDS_IN_KILOGRAM
        Ounce -> value / OUNCES_IN_KILOGRAM
        Dram -> value / DRAMS_IN_KILOGRAM
        Grain -> value / GRAINS_IN_KILOGRAM
        UsTon -> value / US_TONES_IN_KILOGRAM
        ImperialTon -> value / UK_TONES_IN_KILOGRAM
        CubicMeter -> TODO()
        Liter -> value
        Milliliter -> value / MILLILITERS_IN_LITER
        UsLiquidGallon -> value / US_GALLONS_IN_LITER
        UsLiquidQuart -> value / US_QUARTS_IN_LITER
        UsLiquidPint -> value / US_PINTS_IN_LITER
        UsLegalCup -> TODO()
        UsFluidOunce -> value / US_FLUID_OUNCES_IN_LITER
        UsFluidDram -> TODO()
        ImperialGallon -> TODO()
        ImperialQuart -> value / UK_QUARTS_IN_LITER
        ImperialPint -> value / UK_PINTS_IN_LITER
        ImperialCup -> TODO()
        ImperialFluidOunce -> value / UK_FLUID_OUNCES_IN_LITER
        ImperialFluidDram -> TODO()
        CubicFoot -> TODO()
        CubicInch -> TODO()
    }
}

/**
 * Translates from base SI to other systems
 *
 * @param value Value in base SI system
 * @param toUnit The type of the return value
 */
private fun fromBaseSi(value: Decimal, toUnit: ScientificUnit<*, *>): Double {
    return when (toUnit) {
        Decimeter -> TODO()
        Centimeter -> value * CENTIMETERS_IN_METER
        Millimeter -> value * MILLIMETERS_IN_METER
        Decameter -> TODO()
        Hectometer -> TODO()
        Kilometer -> value * KILOMETERS_IN_METER
        Meter -> value
        Inch -> value * INCHES_IN_METER
        Foot -> value * FEET_IN_METER
        Yard -> value * YARDS_IN_METER
        Mile -> value * MILES_IN_METER
        Celsius -> value - KELVIN_FREEZING
        Kelvin -> value
        Fahrenheit -> (value - KELVIN_FREEZING) * 9.0.toDecimal() / 5.0.toDecimal() + FAHRENHEIT_FREEZING
        Tonne -> value * TONES_IN_KILOGRAM
        Kilogram -> value
        Gram -> value * GRAMS_IN_KILOGRAM
        Milligram -> value * MILLIGRAMS_IN_KILOGRAM
        Microgram -> TODO()
        Stone -> value * STONES_IN_KILOGRAM
        Pound -> value * POUNDS_IN_KILOGRAM
        Ounce -> value * OUNCES_IN_KILOGRAM
        Dram -> value * DRAMS_IN_KILOGRAM
        Grain -> value * GRAINS_IN_KILOGRAM
        UsTon -> value * US_TONES_IN_KILOGRAM
        ImperialTon -> value * UK_TONES_IN_KILOGRAM
        CubicMeter -> TODO()
        Liter -> value
        Milliliter -> value * MILLILITERS_IN_LITER
        UsLiquidGallon -> value * US_GALLONS_IN_LITER
        UsLiquidQuart -> value * US_QUARTS_IN_LITER
        UsLiquidPint -> value * US_PINTS_IN_LITER
        UsLegalCup -> TODO()
        UsFluidOunce -> value * US_FLUID_OUNCES_IN_LITER
        UsFluidDram -> TODO()
        ImperialGallon -> value * UK_GALLONS_IN_LITER
        ImperialQuart -> TODO()
        ImperialPint -> TODO()
        ImperialCup -> TODO()
        ImperialFluidOunce -> value * UK_FLUID_OUNCES_IN_LITER
        ImperialFluidDram -> TODO()
        CubicFoot -> TODO()
        CubicInch -> TODO()
    }.toDouble()
}

private fun fromTo(
    value: Decimal,
    inputType: ScientificUnit<*, *>,
    outputType: ScientificUnit<*, *>
) =
    fromBaseSi(toBaseSi(value, inputType), outputType)

fun convert(value: Double, from: ScientificUnit.Length<*>, to: ScientificUnit.Length<*>) =
    fromTo(value.toDecimal(), from, to)

fun convert(value: Double, from: ScientificUnit.Weight<*>, to: ScientificUnit.Weight<*>) =
    fromTo(value.toDecimal(), from, to)

fun convert(value: Double, from: ScientificUnit.Temperature<*>, to: ScientificUnit.Temperature<*>) =
    fromTo(value.toDecimal(), from, to)

fun convert(value: Double, from: ScientificUnit.Volume<*>, to: ScientificUnit.Volume<*>) =
    fromTo(value.toDecimal(), from, to)