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
import kotlin.math.pow

@Serializable
sealed class Volume<System : MeasurementSystem> :
    ScientificUnit<System, MeasurementType.Volume>()

@Serializable
sealed class MetricVolume(override val symbol: String) :
    Volume<MeasurementSystem.Metric>()

@Serializable
sealed class USImperialVolume(override val symbol: String) :
    Volume<MeasurementSystem.USCustomary>()

@Serializable
sealed class UKImperialVolume(override val symbol: String) :
    Volume<MeasurementSystem.UKImperial>()

@Serializable
sealed class ImperialVolume(override val symbol: String) :
    Volume<MeasurementSystem.Imperial>()

// Metric Volume
@Serializable
object CubicMeter : MetricVolume("cu m") {
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object CubicDecimeter : MetricVolume("cu dm") {
    val CUBIC_DECIMETER_IN_CUBIC_METER = Decimeter.DECIMETERS_IN_METER.pow(3)
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_DECIMETER_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_DECIMETER_IN_CUBIC_METER.toDecimal()
}

@Serializable
object CubicCentimeter : MetricVolume("cc") {
    val CUBIC_CENTIMETER_IN_CUBIC_METER = Centimeter.CENTIMETERS_IN_METER.pow(3)
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_CENTIMETER_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_CENTIMETER_IN_CUBIC_METER.toDecimal()
}

@Serializable
object CubicMillimeter : MetricVolume("cu mm") {
    val CUBIC_MILLIMETER_IN_CUBIC_METER = Millimeter.MILLIMETERS_IN_METER.pow(3)
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_MILLIMETER_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_MILLIMETER_IN_CUBIC_METER.toDecimal()
}

@Serializable
object CubicDecameter : MetricVolume("cu dam") {
    val CUBIC_DECAMETER_IN_CUBIC_METER = Decameter.DECAMETERS_IN_METER.pow(3)
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_DECAMETER_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_DECAMETER_IN_CUBIC_METER.toDecimal()
}

@Serializable
object CubicHectometer : MetricVolume("cu hm") {
    val CUBIC_HECTOMETER_IN_CUBIC_METER = Hectometer.HECTOMETERS_IN_METER.pow(3)
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_HECTOMETER_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_HECTOMETER_IN_CUBIC_METER.toDecimal()
}

@Serializable
object CubicKilometer : MetricVolume("cu km") {
    val CUBIC_KILOMETER_IN_CUBIC_METER = Kilometer.KILOMETERS_IN_METER.pow(3)
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_KILOMETER_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_KILOMETER_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Liter : MetricVolume("l") {
    const val LITERS_IN_CUBIC_METER = 1000.0
    override fun toSIUnit(value: Decimal): Decimal = value / LITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * LITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Deciliter : MetricVolume("dl") {
    const val DECILITERS_IN_CUBIC_METER = 10000.0
    override fun toSIUnit(value: Decimal): Decimal = value / DECILITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * DECILITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Centiliter : MetricVolume("cl") {
    const val CENTILITERS_IN_CUBIC_METER = 100000.0
    override fun toSIUnit(value: Decimal): Decimal = value / CENTILITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * CENTILITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Milliliter : MetricVolume("ml") {
    const val MILLILITERS_IN_CUBIC_METER = 1000000.0
    override fun toSIUnit(value: Decimal): Decimal = value / MILLILITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * MILLILITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Decaliter : MetricVolume("dal") {
    const val DECALITERS_IN_CUBIC_METER = 100.0
    override fun toSIUnit(value: Decimal): Decimal = value / DECALITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * DECALITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Hectoliter : MetricVolume("hl") {
    const val HECTOLITERS_IN_CUBIC_METER = 10.0
    override fun toSIUnit(value: Decimal): Decimal = value / HECTOLITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * HECTOLITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Kiloliter : MetricVolume("kl") {
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

// Imperial
@Serializable
object CubicInch : ImperialVolume("cu in") {
    const val CUBIC_INCHES_IN_CUBIC_METER = 61023.74409473229
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_INCHES_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * CUBIC_INCHES_IN_CUBIC_METER.toDecimal()
}

@Serializable
object CubicFoot : ImperialVolume("cu ft") {
    const val CUBIC_FEET_IN_CUBIC_METER = 35.31466672148859
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_FEET_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_FEET_IN_CUBIC_METER.toDecimal()
}

@Serializable
object CubicMile : ImperialVolume("cu in") {
    val CUBIC_MILES_IN_CUBIC_METER = Mile.MILES_IN_METER.pow(3)
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_MILES_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * CUBIC_MILES_IN_CUBIC_METER.toDecimal()
}

// US Imperial

@Serializable
object AcreFoot : USImperialVolume("ac ft") {
    val ACRE_FOOT_IN_CUBIC_METER = 0.000810713193789913
    override fun toSIUnit(value: Decimal): Decimal = value / ACRE_FOOT_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * ACRE_FOOT_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsFluidDram : USImperialVolume("fl dr") {
    const val US_FLUID_DRAM_IN_CUBIC_METER = 270512.18161474395
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_FLUID_DRAM_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsFluidOunce : USImperialVolume("fl oz") {
    const val US_FLUID_OUNCES_IN_LITER = 33814.022701842994
    override fun toSIUnit(value: Decimal): Decimal = value / US_FLUID_OUNCES_IN_LITER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_FLUID_OUNCES_IN_LITER.toDecimal()
}

@Serializable
object UsLegalCup : USImperialVolume("cup") {
    const val US_LEGAL_CUPS_IN_CUBIC_METER = 4226.752837730375
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_LEGAL_CUPS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_LEGAL_CUPS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsLiquidPint : USImperialVolume("pint") {
    const val US_PINTS_IN_CUBIC_METER = 2113.376418865187
    override fun toSIUnit(value: Decimal): Decimal = value / US_PINTS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_PINTS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsLiquidQuart : USImperialVolume("qt") {
    const val US_QUARTS_IN_CUBIC_METER = 1056.688209432594
    override fun toSIUnit(value: Decimal): Decimal = value / US_QUARTS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * US_QUARTS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object UsLiquidGallon : USImperialVolume("gal") {
    const val US_LIQUID_GALLONS_IN_CUBIC_METER = 264.1720523581484
    override fun toSIUnit(value: Decimal): Decimal =
        value / US_LIQUID_GALLONS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * US_LIQUID_GALLONS_IN_CUBIC_METER.toDecimal()
}

// UK Imperial
@Serializable
object ImperialFluidDram : UKImperialVolume("fl dr") {
    const val IMPERIAL_FLUID_DRAM_IN_CUBIC_METER = 281560.63782283233
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_FLUID_DRAM_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_FLUID_DRAM_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialFluidOunce : UKImperialVolume("fl oz") {
    const val IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER = 35195.07972785405
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_FLUID_OUNCES_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialCup : UKImperialVolume("cup") {
    const val IMPERIAL_CUPS_IN_CUBIC_METER = 4000.0
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_CUPS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_CUPS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialPint : UKImperialVolume("pt") {
    const val IMPERIAL_PINTS_IN_CUBIC_METER = 1759.753986392702
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_PINTS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_PINTS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialQuart : UKImperialVolume("qt") {
    const val IMPERIAL_QUARTS_IN_CUBIC_METER = 879.8769931963512
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_QUARTS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_QUARTS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object ImperialGallon : UKImperialVolume("gal") {
    const val IMPERIAL_GALLONS_IN_CUBIC_METER = 219.96924829908778
    override fun toSIUnit(value: Decimal): Decimal =
        value / IMPERIAL_GALLONS_IN_CUBIC_METER.toDecimal()

    override fun fromSIUnit(value: Decimal): Decimal =
        value * IMPERIAL_GALLONS_IN_CUBIC_METER.toDecimal()
}

fun Volume<*>.volumeFrom(width: Decimal, depth: Decimal, height: Decimal, lengthUnit: Length<*>): Decimal {
    val volume = lengthUnit.toSIUnit(width) * lengthUnit.toSIUnit(depth) * lengthUnit.toSIUnit(height)
    return CubicMeter.convert(volume, this)
}

fun Volume<*>.volumeFrom(area: Decimal, areaUnit: Area<*>, height: Decimal, lengthUnit: Length<*>): Decimal {
    val volume = areaUnit.toSIUnit(area) * lengthUnit.toSIUnit(height)
    return CubicMeter.convert(volume, this)
}
