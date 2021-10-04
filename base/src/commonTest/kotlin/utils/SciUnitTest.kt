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
import com.splendo.kaluga.base.utils.SciUnit.Companion.convert
import com.splendo.kaluga.base.utils.SciUnit.Length
import com.splendo.kaluga.base.utils.SciUnit.Length.Centimeters
import com.splendo.kaluga.base.utils.SciUnit.Length.Feet
import com.splendo.kaluga.base.utils.SciUnit.Length.Inches
import com.splendo.kaluga.base.utils.SciUnit.Length.Kilometers
import com.splendo.kaluga.base.utils.SciUnit.Length.Meters
import com.splendo.kaluga.base.utils.SciUnit.Length.Miles
import com.splendo.kaluga.base.utils.SciUnit.Length.Millimeters
import com.splendo.kaluga.base.utils.SciUnit.Length.Yards
import com.splendo.kaluga.base.utils.SciUnit.Temperature
import com.splendo.kaluga.base.utils.SciUnit.Temperature.Celsius
import com.splendo.kaluga.base.utils.SciUnit.Temperature.Fahrenheit
import com.splendo.kaluga.base.utils.SciUnit.Temperature.Kelvin
import com.splendo.kaluga.base.utils.SciUnit.Volume
import com.splendo.kaluga.base.utils.SciUnit.Weight
import com.splendo.kaluga.base.utils.SciUnit.Weight.Drams
import com.splendo.kaluga.base.utils.SciUnit.Weight.Grains
import com.splendo.kaluga.base.utils.SciUnit.Weight.Grams
import com.splendo.kaluga.base.utils.SciUnit.Weight.Kilograms
import com.splendo.kaluga.base.utils.SciUnit.Weight.Milligrams
import com.splendo.kaluga.base.utils.SciUnit.Weight.Ounces
import com.splendo.kaluga.base.utils.SciUnit.Weight.Pounds
import com.splendo.kaluga.base.utils.SciUnit.Weight.Stones
import com.splendo.kaluga.base.utils.SciUnit.Weight.Tones
import com.splendo.kaluga.base.utils.SciUnit.Weight.LongTons
import com.splendo.kaluga.base.utils.SciUnit.Weight.ShortTons
import kotlin.test.Test
import kotlin.test.assertEquals

class SciUnitTest {

    private val unitTranslationErrorTolerance = 0.000001

    @Test
    fun weighConversionTest() {

        assertFor(1000, Milligrams, Grams, 0.001)
        assertFor(1000, Milligrams, Grains, 0.015432358352941431, unitTranslationErrorTolerance)

        assertFor(1000, Grams, Milligrams, 1000.0)
        assertFor(1000, Grams, Kilograms, 0.001, unitTranslationErrorTolerance)
        assertFor(1000, Grams, Grains, 15.432358352941431, unitTranslationErrorTolerance)
        assertFor(1000, Grams, Drams, 0.5643833911932866, unitTranslationErrorTolerance)

        assertFor(1000, Kilograms, Grams, 1000.0)
        assertFor(1000, Kilograms, Tones, 0.001)
        assertFor(1000, Kilograms, Ounces, 35.27396194958041, unitTranslationErrorTolerance)
        assertFor(1000, Kilograms, Pounds, 2.204622621848776, unitTranslationErrorTolerance)

        assertFor(1000, Tones, Kilograms, 1000.0)
        assertFor(1000, Tones, Stones, 157.4730444177697, unitTranslationErrorTolerance)
        assertFor(1000, Tones, LongTons, 0.9842065276110001, unitTranslationErrorTolerance)
        assertFor(1000, Tones, ShortTons, 1.102311310924, unitTranslationErrorTolerance)

        assertFor(1000, Drams, Grams, 1.7718451953125, unitTranslationErrorTolerance)
        assertFor(1000, Drams, Grains, 27.343750000000004, unitTranslationErrorTolerance)

        assertFor(1000, Grains, Grams, 0.06479891, unitTranslationErrorTolerance)
        assertFor(1000, Grains, Drams, 0.03657142857142857, unitTranslationErrorTolerance)
        assertFor(1000, Grains, Ounces, 0.002285714285714286, unitTranslationErrorTolerance)

        assertFor(1000, Ounces, Grams, 28.349523125, unitTranslationErrorTolerance)
        assertFor(1000, Ounces, Kilograms, 0.028349523125, unitTranslationErrorTolerance)
        assertFor(1000, Ounces, Grains, 437.50000000000006, unitTranslationErrorTolerance)
        assertFor(1000, Ounces, Pounds, 0.0625, unitTranslationErrorTolerance)

        assertFor(1000, Pounds, Grams, 453.59237, unitTranslationErrorTolerance)
        assertFor(1000, Pounds, Kilograms, 0.45359237, unitTranslationErrorTolerance)
        assertFor(1000, Pounds, Grains, 7000.0, unitTranslationErrorTolerance)
        assertFor(1000, Pounds, Ounces, 16.0, unitTranslationErrorTolerance)
        assertFor(1000, Pounds, Stones, 0.07142857142857142, unitTranslationErrorTolerance)

        assertFor(1000, Stones, Kilograms, 6.35029318, unitTranslationErrorTolerance)
        assertFor(1000, Stones, Ounces, 224.0, unitTranslationErrorTolerance)
        assertFor(1000, Stones, Pounds, 14.0, unitTranslationErrorTolerance)

        assertFor(1000, ShortTons, Kilograms, 907.18474, unitTranslationErrorTolerance)
        assertFor(1000, ShortTons, Tones, 0.90718474, unitTranslationErrorTolerance)
        assertFor(1000, ShortTons, Pounds, 2000.0, unitTranslationErrorTolerance)
        assertFor(1000, ShortTons, Stones, 142.85714285714286, unitTranslationErrorTolerance)
        assertFor(1000, ShortTons, LongTons, 0.892857142857088, unitTranslationErrorTolerance)

        assertFor(1000, LongTons, Kilograms, 1016.0469088, unitTranslationErrorTolerance)
        assertFor(1000, LongTons, Tones, 1.0160469088, unitTranslationErrorTolerance)
        assertFor(1000, LongTons, Stones, 160.0, unitTranslationErrorTolerance)
        assertFor(1000, LongTons, ShortTons, 1.1199999999, unitTranslationErrorTolerance)
    }

    @Test
    fun temperatureConversionTest() {
        assertEquals(convert(0.0, Celsius, Fahrenheit), 32.0, unitTranslationErrorTolerance)
        assertEquals(convert(0.0, Celsius, Kelvin), 273.15, unitTranslationErrorTolerance)
        assertEquals(convert(0.0, Celsius, Celsius), 0.0, unitTranslationErrorTolerance)

        assertEquals(convert(0.0, Fahrenheit, Fahrenheit), 0.0, unitTranslationErrorTolerance)
        assertEquals(convert(0.0, Fahrenheit, Kelvin), 255.372222, unitTranslationErrorTolerance)
        assertEquals(convert(0.0, Fahrenheit, Celsius), -17.777778, unitTranslationErrorTolerance)

        assertEquals(convert(0.0, Kelvin, Fahrenheit), -459.67, unitTranslationErrorTolerance)
        assertEquals(convert(0.0, Kelvin, Kelvin), 0.0)
        assertEquals(convert(0.0, Kelvin, Celsius), -273.15, unitTranslationErrorTolerance)
    }

    @Test
    fun lengthConversionTest() {
        assertFor(1000, Millimeters, Centimeters, 0.1, unitTranslationErrorTolerance)
        assertFor(1000, Millimeters, Meters, 0.001, unitTranslationErrorTolerance)
        assertFor(1000, Millimeters, Inches, 0.0393700787, unitTranslationErrorTolerance)
        assertFor(1000, Millimeters, Feet, 0.0032808399, unitTranslationErrorTolerance)
        assertFor(1000, Millimeters, Yards, 0.0010936133, unitTranslationErrorTolerance)

        assertFor(1000, Centimeters, Millimeters, 10.0)
        assertFor(1000, Centimeters, Meters, 0.01)
        assertFor(1000, Centimeters, Inches, 0.3937007874, unitTranslationErrorTolerance)
        assertFor(1000, Centimeters, Feet, 0.032808399, unitTranslationErrorTolerance)
        assertFor(1000, Centimeters, Yards, 0.010936133, unitTranslationErrorTolerance)

        assertFor(1000, Meters, Millimeters, 1000.0)
        assertFor(1000, Meters, Centimeters, 100.0)
        assertFor(1000, Meters, Kilometers, 0.001)
        assertFor(1000, Meters, Inches, 39.37007874, unitTranslationErrorTolerance)
        assertFor(1000, Meters, Feet, 3.280839895, unitTranslationErrorTolerance)
        assertFor(1000, Meters, Yards, 1.0936132983, unitTranslationErrorTolerance)

        assertFor(1000, Kilometers, Meters, 1000.0)
        assertFor(1000, Kilometers, Feet, 3280.8398950131236, unitTranslationErrorTolerance)
        assertFor(1000, Kilometers, Yards, 1093.6132983377079, unitTranslationErrorTolerance)
        assertFor(1000, Kilometers, Miles, 0.6213711922, unitTranslationErrorTolerance)

        assertFor(1000, Inches, Millimeters, 25.400, unitTranslationErrorTolerance)
        assertFor(1000, Inches, Centimeters, 2.540, unitTranslationErrorTolerance)
        assertFor(1000, Inches, Meters, 0.0254, unitTranslationErrorTolerance)
        assertFor(1000, Inches, Feet, 0.08333333333333333, unitTranslationErrorTolerance)
        assertFor(1000, Inches, Yards, 0.027777777777777776, unitTranslationErrorTolerance)

        assertFor(1000, Feet, Centimeters, 30.480, unitTranslationErrorTolerance)
        assertFor(1000, Feet, Millimeters, 304.800, unitTranslationErrorTolerance)
        assertFor(1000, Feet, Meters, 0.3048, unitTranslationErrorTolerance)
        assertFor(1000, Feet, Inches, 12.0, unitTranslationErrorTolerance)
        assertFor(1000, Feet, Yards, 0.333333333333, unitTranslationErrorTolerance)

        assertFor(1000, Yards, Centimeters, 91.440, unitTranslationErrorTolerance)
        assertFor(1000, Yards, Meters, 0.9144, unitTranslationErrorTolerance)
        assertFor(1000, Yards, Kilometers, 0.0009144, unitTranslationErrorTolerance)
        assertFor(1000, Yards, Inches, 36.000, unitTranslationErrorTolerance)
        assertFor(1000, Yards, Feet, 3.000, unitTranslationErrorTolerance)
        assertFor(1000, Yards, Miles, 0.0005681818, unitTranslationErrorTolerance)

        assertFor(1000, Miles, Meters, 1609.344, unitTranslationErrorTolerance)
        assertFor(1000, Miles, Kilometers, 1.609344, unitTranslationErrorTolerance)
        assertFor(1000, Miles, Feet, 5280.0, unitTranslationErrorTolerance)
        assertFor(1000, Miles, Yards, 1760.0, unitTranslationErrorTolerance)
    }
}

fun assertFor(
    max: Int,
    inputUnit: SciUnit,
    outputUnit: SciUnit,
    inputToOutputRatio: Double,
    tolerance: Double = 0.0
) {
    for (i in 1..max) {
        val actual = (inputToOutputRatio.toDecimal() * i.toDouble().toDecimal()).toDouble()
        when (inputUnit) {
            is Weight -> {
                assertEquals(
                    convert(i.toDouble(), inputUnit, outputUnit as Weight),
                    actual,
                    tolerance
                )
            }
            is Temperature -> {
                assertEquals(
                    convert(i.toDouble(), inputUnit, outputUnit as Temperature),
                    actual,
                    tolerance
                )
            }
            is Length -> {
                assertEquals(
                    convert(i.toDouble(), inputUnit, outputUnit as Length),
                    actual,
                    tolerance
                )
            }
            is Volume -> {
                assertEquals(
                    convert(i.toDouble(), inputUnit, outputUnit as Volume),
                    actual,
                    tolerance
                )
            }
        }
    }
}
