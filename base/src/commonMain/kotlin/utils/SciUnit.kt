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

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.Decimals.toDecimal
import com.splendo.kaluga.base.utils.Decimals.toDouble

sealed class SciUnit {

    sealed class Length : SciUnit() {
        object Millimeters : Length()
        object Centimeters : Length()
        object Meters : Length()
        object Kilometers : Length()
        object Inches : Length()
        object Feet : Length()
        object Yards : Length()
        object Miles : Length()
    }

    sealed class Weight : SciUnit() {
        object Milligrams : Weight()
        object Grams : Weight()
        object Kilograms : Weight()
        object Tones : Weight()
        object Drams: Weight()
        object Grains : Weight()
        object Ounces : Weight()
        object Pounds : Weight()
        object Stones : Weight()
        object LongTons : Weight()
        object ShortTons : Weight()
    }

    sealed class Temperature : SciUnit() {
        object Celsius : Temperature()
        object Kelvin : Temperature()
        object Fahrenheit : Temperature()
    }

    sealed class Volume : SciUnit() {
        object Milliliters : Volume()
        object Liters : Volume()
        object UKFluidOunces : Volume()
        object UKPints : Volume()
        object UKQuarts : Volume()
        object UKGallons : Volume()
        object USFluidOunce : Volume()
        object USPints : Volume()
        object USQuarts : Volume()
        object USGallons : Volume()
    }

    companion object {

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
        private fun toBaseSi(value: Decimal, inUnit: SciUnit): Decimal {
            return when (inUnit) {
                Length.Millimeters -> value / MILLIMETERS_IN_METER
                Length.Centimeters -> value / CENTIMETERS_IN_METER
                Length.Meters -> value
                Length.Kilometers -> value / KILOMETERS_IN_METER
                Length.Inches -> value / INCHES_IN_METER
                Length.Feet -> value / FEET_IN_METER
                Length.Yards -> value / YARDS_IN_METER
                Length.Miles -> value / MILES_IN_METER
                Weight.Milligrams -> value / MILLIGRAMS_IN_KILOGRAM
                Weight.Grams -> value / GRAMS_IN_KILOGRAM
                Weight.Kilograms -> value
                Weight.Tones -> value / TONES_IN_KILOGRAM
                Weight.Drams -> value / DRAMS_IN_KILOGRAM
                Weight.Grains -> value / GRAINS_IN_KILOGRAM
                Weight.Ounces -> value / OUNCES_IN_KILOGRAM
                Weight.Pounds -> value / POUNDS_IN_KILOGRAM
                Weight.Stones -> value / STONES_IN_KILOGRAM
                Weight.LongTons -> value / UK_TONES_IN_KILOGRAM
                Weight.ShortTons -> value / US_TONES_IN_KILOGRAM
                Temperature.Celsius -> value + KELVIN_FREEZING
                Temperature.Kelvin -> value
                Temperature.Fahrenheit -> (value - FAHRENHEIT_FREEZING) * 5.0.toDecimal() / 9.0.toDecimal() + KELVIN_FREEZING
                Volume.Milliliters -> value / MILLILITERS_IN_LITER
                Volume.Liters -> value
                Volume.UKFluidOunces -> value / UK_FLUID_OUNCES_IN_LITER
                Volume.UKPints -> value / UK_PINTS_IN_LITER
                Volume.UKQuarts -> value / UK_QUARTS_IN_LITER
                Volume.UKGallons -> value / UK_GALLONS_IN_LITER
                Volume.USFluidOunce -> value / US_FLUID_OUNCES_IN_LITER
                Volume.USPints -> value / US_PINTS_IN_LITER
                Volume.USQuarts -> value / US_QUARTS_IN_LITER
                Volume.USGallons -> value / US_GALLONS_IN_LITER
            }
        }

        /**
         * Translates from base SI to other systems
         *
         * @param value Value in base SI system
         * @param toUnit The type of the return value
         */
        private fun fromBaseSi(value: Decimal, toUnit: SciUnit): Double {
            val result = when (toUnit) {
                Length.Millimeters -> value * MILLIMETERS_IN_METER
                Length.Centimeters -> value * CENTIMETERS_IN_METER
                Length.Meters -> value
                Length.Kilometers -> value * KILOMETERS_IN_METER
                Length.Inches -> value * INCHES_IN_METER
                Length.Feet -> value * FEET_IN_METER
                Length.Yards -> value * YARDS_IN_METER
                Length.Miles -> value * MILES_IN_METER
                Weight.Milligrams -> value * MILLIGRAMS_IN_KILOGRAM
                Weight.Grams -> value * GRAMS_IN_KILOGRAM
                Weight.Kilograms -> value
                Weight.Tones -> value * TONES_IN_KILOGRAM
                Weight.Drams -> value * DRAMS_IN_KILOGRAM
                Weight.Grains -> value * GRAINS_IN_KILOGRAM
                Weight.Ounces -> value * OUNCES_IN_KILOGRAM
                Weight.Pounds -> value * POUNDS_IN_KILOGRAM
                Weight.Stones -> value * STONES_IN_KILOGRAM
                Weight.LongTons -> value * UK_TONES_IN_KILOGRAM
                Weight.ShortTons -> value * US_TONES_IN_KILOGRAM
                Temperature.Celsius -> value - KELVIN_FREEZING
                Temperature.Kelvin -> value
                Temperature.Fahrenheit -> (value - KELVIN_FREEZING) * 9.0.toDecimal() / 5.0.toDecimal() + FAHRENHEIT_FREEZING
                Volume.Milliliters -> value / MILLILITERS_IN_LITER
                Volume.Liters -> value
                Volume.UKFluidOunces -> value / UK_FLUID_OUNCES_IN_LITER
                Volume.UKPints -> value / UK_PINTS_IN_LITER
                Volume.UKQuarts -> value / UK_QUARTS_IN_LITER
                Volume.UKGallons -> value / UK_GALLONS_IN_LITER
                Volume.USFluidOunce -> value / US_FLUID_OUNCES_IN_LITER
                Volume.USPints -> value / US_PINTS_IN_LITER
                Volume.USQuarts -> value / US_QUARTS_IN_LITER
                Volume.USGallons -> value / US_GALLONS_IN_LITER
            }
            return result.toDouble()
        }

        private fun fromTo(value: Decimal, inputType: SciUnit, outputType: SciUnit) =
            fromBaseSi(toBaseSi(value, inputType), outputType)

        fun convert(value: Double, from: Length, to: Length) = fromTo(value.toDecimal(), from, to)
        fun convert(value: Double, from: Weight, to: Weight) = fromTo(value.toDecimal(), from, to)
        fun convert(value: Double, from: Temperature, to: Temperature) =
            fromTo(value.toDecimal(), from, to)

        fun convert(value: Double, from: Volume, to: Volume) = fromTo(value.toDecimal(), from, to)
    }
}
