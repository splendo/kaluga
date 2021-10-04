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
import com.splendo.kaluga.base.utils.ScientificUnit.Length
import com.splendo.kaluga.base.utils.ScientificUnit.Temperature
import com.splendo.kaluga.base.utils.ScientificUnit.Volume
import com.splendo.kaluga.base.utils.ScientificUnit.Weight
import kotlin.test.Test
import kotlin.test.assertEquals

class SciUnitTest {

    private val unitTranslationErrorTolerance = 0.000001

    @Test
    fun weighConversionTest() {

        assertFor(1000, Milligram, Gram, 0.001)
        assertFor(1000, Milligram, Grain, 0.015432358352941431, unitTranslationErrorTolerance)

        assertFor(1000, Gram, Milligram, 1000.0)
        assertFor(1000, Gram, Kilogram, 0.001, unitTranslationErrorTolerance)
        assertFor(1000, Gram, Grain, 15.432358352941431, unitTranslationErrorTolerance)
        assertFor(1000, Gram, Dram, 0.5643833911932866, unitTranslationErrorTolerance)

        assertFor(1000, Kilogram, Gram, 1000.0)
        assertFor(1000, Kilogram, Tonne, 0.001)
        assertFor(1000, Kilogram, Ounce, 35.27396194958041, unitTranslationErrorTolerance)
        assertFor(1000, Kilogram, Pound, 2.204622621848776, unitTranslationErrorTolerance)

        assertFor(1000, Tonne, Kilogram, 1000.0)
        assertFor(1000, Tonne, Stone, 157.4730444177697, unitTranslationErrorTolerance)
        assertFor(1000, Tonne, ImperialTon, 0.9842065276110001, unitTranslationErrorTolerance)
        assertFor(1000, Tonne, UsTon, 1.102311310924, unitTranslationErrorTolerance)

        assertFor(1000, Dram, Gram, 1.7718451953125, unitTranslationErrorTolerance)
        assertFor(1000, Dram, Grain, 27.343750000000004, unitTranslationErrorTolerance)

        assertFor(1000, Grain, Gram, 0.06479891, unitTranslationErrorTolerance)
        assertFor(1000, Grain, Dram, 0.03657142857142857, unitTranslationErrorTolerance)
        assertFor(1000, Grain, Ounce, 0.002285714285714286, unitTranslationErrorTolerance)

        assertFor(1000, Ounce, Gram, 28.349523125, unitTranslationErrorTolerance)
        assertFor(1000, Ounce, Kilogram, 0.028349523125, unitTranslationErrorTolerance)
        assertFor(1000, Ounce, Grain, 437.50000000000006, unitTranslationErrorTolerance)
        assertFor(1000, Ounce, Pound, 0.0625, unitTranslationErrorTolerance)

        assertFor(1000, Pound, Gram, 453.59237, unitTranslationErrorTolerance)
        assertFor(1000, Pound, Kilogram, 0.45359237, unitTranslationErrorTolerance)
        assertFor(1000, Pound, Grain, 7000.0, unitTranslationErrorTolerance)
        assertFor(1000, Pound, Ounce, 16.0, unitTranslationErrorTolerance)
        assertFor(1000, Pound, Stone, 0.07142857142857142, unitTranslationErrorTolerance)

        assertFor(1000, Stone, Kilogram, 6.35029318, unitTranslationErrorTolerance)
        assertFor(1000, Stone, Ounce, 224.0, unitTranslationErrorTolerance)
        assertFor(1000, Stone, Pound, 14.0, unitTranslationErrorTolerance)

        assertFor(1000, UsTon, Kilogram, 907.18474, unitTranslationErrorTolerance)
        assertFor(1000, UsTon, Tonne, 0.90718474, unitTranslationErrorTolerance)
        assertFor(1000, UsTon, Pound, 2000.0, unitTranslationErrorTolerance)
        assertFor(1000, UsTon, Stone, 142.85714285714286, unitTranslationErrorTolerance)
        assertFor(1000, UsTon, ImperialTon, 0.892857142857088, unitTranslationErrorTolerance)

        assertFor(1000, ImperialTon, Kilogram, 1016.0469088, unitTranslationErrorTolerance)
        assertFor(1000, ImperialTon, Tonne, 1.0160469088, unitTranslationErrorTolerance)
        assertFor(1000, ImperialTon, Stone, 160.0, unitTranslationErrorTolerance)
        assertFor(1000, ImperialTon, UsTon, 1.1199999999, unitTranslationErrorTolerance)
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
        assertFor(1000, Millimeter, Centimeter, 0.1, unitTranslationErrorTolerance)
        assertFor(1000, Millimeter, Meter, 0.001, unitTranslationErrorTolerance)
        assertFor(1000, Millimeter, Inch, 0.0393700787, unitTranslationErrorTolerance)
        assertFor(1000, Millimeter, Foot, 0.0032808399, unitTranslationErrorTolerance)
        assertFor(1000, Millimeter, Yard, 0.0010936133, unitTranslationErrorTolerance)

        assertFor(1000, Centimeter, Millimeter, 10.0)
        assertFor(1000, Centimeter, Meter, 0.01)
        assertFor(1000, Centimeter, Inch, 0.3937007874, unitTranslationErrorTolerance)
        assertFor(1000, Centimeter, Foot, 0.032808399, unitTranslationErrorTolerance)
        assertFor(1000, Centimeter, Yard, 0.010936133, unitTranslationErrorTolerance)

        assertFor(1000, Meter, Millimeter, 1000.0)
        assertFor(1000, Meter, Centimeter, 100.0)
        assertFor(1000, Meter, Kilometer, 0.001)
        assertFor(1000, Meter, Inch, 39.37007874, unitTranslationErrorTolerance)
        assertFor(1000, Meter, Foot, 3.280839895, unitTranslationErrorTolerance)
        assertFor(1000, Meter, Yard, 1.0936132983, unitTranslationErrorTolerance)

        assertFor(1000, Kilometer, Meter, 1000.0)
        assertFor(1000, Kilometer, Foot, 3280.8398950131236, unitTranslationErrorTolerance)
        assertFor(1000, Kilometer, Yard, 1093.6132983377079, unitTranslationErrorTolerance)
        assertFor(1000, Kilometer, Mile, 0.6213711922, unitTranslationErrorTolerance)

        assertFor(1000, Inch, Millimeter, 25.400, unitTranslationErrorTolerance)
        assertFor(1000, Inch, Centimeter, 2.540, unitTranslationErrorTolerance)
        assertFor(1000, Inch, Meter, 0.0254, unitTranslationErrorTolerance)
        assertFor(1000, Inch, Foot, 0.08333333333333333, unitTranslationErrorTolerance)
        assertFor(1000, Inch, Yard, 0.027777777777777776, unitTranslationErrorTolerance)

        assertFor(1000, Foot, Centimeter, 30.480, unitTranslationErrorTolerance)
        assertFor(1000, Foot, Millimeter, 304.800, unitTranslationErrorTolerance)
        assertFor(1000, Foot, Meter, 0.3048, unitTranslationErrorTolerance)
        assertFor(1000, Foot, Inch, 12.0, unitTranslationErrorTolerance)
        assertFor(1000, Foot, Yard, 0.333333333333, unitTranslationErrorTolerance)

        assertFor(1000, Yard, Centimeter, 91.440, unitTranslationErrorTolerance)
        assertFor(1000, Yard, Meter, 0.9144, unitTranslationErrorTolerance)
        assertFor(1000, Yard, Kilometer, 0.0009144, unitTranslationErrorTolerance)
        assertFor(1000, Yard, Inch, 36.000, unitTranslationErrorTolerance)
        assertFor(1000, Yard, Foot, 3.000, unitTranslationErrorTolerance)
        assertFor(1000, Yard, Mile, 0.0005681818, unitTranslationErrorTolerance)

        assertFor(1000, Mile, Meter, 1609.344, unitTranslationErrorTolerance)
        assertFor(1000, Mile, Kilometer, 1.609344, unitTranslationErrorTolerance)
        assertFor(1000, Mile, Foot, 5280.0, unitTranslationErrorTolerance)
        assertFor(1000, Mile, Yard, 1760.0, unitTranslationErrorTolerance)
    }
}

fun assertFor(
    max: Int,
    inputUnit: ScientificUnit<*,*>,
    outputUnit: ScientificUnit<*,*>,
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
