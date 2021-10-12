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

// Metric Volume
@Serializable
object CubicMeter : MetricVolume("cu m") {
    override fun toSIUnit(value: Decimal): Decimal = value
    override fun fromSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Liter : MetricVolume("l") {
    const val LITERS_IN_CUBIC_METER = 1000.0
    override fun toSIUnit(value: Decimal): Decimal = value / LITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * LITERS_IN_CUBIC_METER.toDecimal()
}

@Serializable
object Milliliter : MetricVolume("ml") {
    const val MILLILITERS_IN_CUBIC_METER = 1000000.0
    override fun toSIUnit(value: Decimal): Decimal = value / MILLILITERS_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * MILLILITERS_IN_CUBIC_METER.toDecimal()
}

// Imperial
@Serializable
object CubicInch : UKImperialVolume("cu in") {
    const val CUBIC_INCHES_IN_CUBIC_METER = 61023.74409473229
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_INCHES_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal =
        value * CUBIC_INCHES_IN_CUBIC_METER.toDecimal()
}

@Serializable
object CubicFoot : UKImperialVolume("cu ft") {
    const val CUBIC_FEET_IN_CUBIC_METER = 35.31466672148859
    override fun toSIUnit(value: Decimal): Decimal = value / CUBIC_FEET_IN_CUBIC_METER.toDecimal()
    override fun fromSIUnit(value: Decimal): Decimal = value * CUBIC_FEET_IN_CUBIC_METER.toDecimal()
}

// US Imperial

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
