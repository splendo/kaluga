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

import com.splendo.kaluga.base.utils.SciUnit.Companion.convert
import com.splendo.kaluga.base.utils.SciUnit.Length.Centimeters
import com.splendo.kaluga.base.utils.SciUnit.Length.Feet
import com.splendo.kaluga.base.utils.SciUnit.Length.Inches
import com.splendo.kaluga.base.utils.SciUnit.Length.Kilometers
import com.splendo.kaluga.base.utils.SciUnit.Length.Meters
import com.splendo.kaluga.base.utils.SciUnit.Length.Miles
import com.splendo.kaluga.base.utils.SciUnit.Length.Millimeters
import com.splendo.kaluga.base.utils.SciUnit.Length.Yards
import com.splendo.kaluga.base.utils.SciUnit.Temperature.Celsius
import com.splendo.kaluga.base.utils.SciUnit.Temperature.Fahrenheit
import com.splendo.kaluga.base.utils.SciUnit.Temperature.Kelvin
import com.splendo.kaluga.base.utils.SciUnit.Weight.Grains
import com.splendo.kaluga.base.utils.SciUnit.Weight.Grams
import com.splendo.kaluga.base.utils.SciUnit.Weight.Kilograms
import com.splendo.kaluga.base.utils.SciUnit.Weight.Milligrams
import com.splendo.kaluga.base.utils.SciUnit.Weight.Ounces
import com.splendo.kaluga.base.utils.SciUnit.Weight.Pounds
import com.splendo.kaluga.base.utils.SciUnit.Weight.Stones
import com.splendo.kaluga.base.utils.SciUnit.Weight.Tones
import com.splendo.kaluga.base.utils.SciUnit.Weight.UKTones
import com.splendo.kaluga.base.utils.SciUnit.Weight.USTones
import kotlin.test.Test
import kotlin.test.assertEquals

class SciUnitTest {

    @Test
    fun weighConversionTest() {
        assertEquals(convert(1.0, Milligrams, Milligrams), 1.0)
        assertEquals(convert(1.0, Milligrams, Grams), 0.001)
        assertEquals(convert(1.0, Milligrams, Kilograms), 1e-6)
        assertEquals(convert(1.0, Milligrams, Tones), 1e-9)
        assertEquals(convert(1.0, Milligrams, Grains), 0.015432358352941431)
        assertEquals(convert(100.0, Milligrams, Ounces), 0.0035273961949580414)
        assertEquals(convert(1000.0, Milligrams, Pounds), 0.0022046226218487763)
        assertEquals(convert(10000.0, Milligrams, Stones), 0.0015747304441776969)
        assertEquals(convert(10000.0, Milligrams, UKTones), 0.00000984206527611)
        assertEquals(convert(10000.0, Milligrams, USTones), 0.00001102311310924)

        assertEquals(convert(1.0, Grams, Milligrams), 1000.0)
        assertEquals(convert(1.0, Grams, Grams), 1.0)
        assertEquals(convert(1.0, Grams, Kilograms), 0.001)
        assertEquals(convert(1.0, Grams, Tones), 1e-6)
        assertEquals(convert(1.0, Grams, Grains), 15.432358352941431)
        assertEquals(convert(1.0, Grams, Ounces), 0.035273961949580414)
        assertEquals(convert(1.0, Grams, Pounds), 0.0022046226218487763)
        assertEquals(convert(1000.0, Grams, Stones), 0.1574730444177697)
        assertEquals(convert(1000.0, Grams, UKTones), 0.000984206527611)
        assertEquals(convert(1000.0, Grams, USTones), 0.001102311310924)

        assertEquals(convert(1.0, Kilograms, Milligrams), 1e+6)
        assertEquals(convert(1.0, Kilograms, Grams), 1000.0)
        assertEquals(convert(1.0, Kilograms, Kilograms), 1.0)
        assertEquals(convert(1.0, Kilograms, Tones), 0.001)
        assertEquals(convert(1.0, Kilograms, Grains), 15432.358352941432)
        assertEquals(convert(1.0, Kilograms, Ounces), 35.27396194958041)
        assertEquals(convert(1.0, Kilograms, Pounds), 2.204622621848776)
        assertEquals(convert(1.0, Kilograms, Stones), 0.1574730444177697)
        assertEquals(convert(1.0, Kilograms, UKTones), 0.000984206527611)
        assertEquals(convert(1.0, Kilograms, USTones), 0.001102311310924)

        assertEquals(convert(1.0, Tones, Milligrams), 1e+9)
        assertEquals(convert(1.0, Tones, Grams), 1e+6)
        assertEquals(convert(1.0, Tones, Kilograms), 1000.0)
        assertEquals(convert(1.0, Tones, Tones), 1.0)
        assertEquals(convert(0.01, Tones, Grains), 154323.58352941432)
        assertEquals(convert(0.01, Tones, Ounces), 352.7396194958041) // Calculator yields 352.73961949580405 - 0.00000000000005 error
        assertEquals(convert(1.0, Tones, Pounds), 2204.622621848776)
        assertEquals(convert(1.0, Tones, Stones), 157.4730444177697)
        assertEquals(convert(1.0, Tones, UKTones), 0.9842065276110001)
        assertEquals(convert(1.0, Tones, USTones), 1.102311310924)

        assertEquals(convert(1.0, Ounces, Milligrams), 28349.523125)
        assertEquals(convert(1.0, Ounces, Grams), 28.349523125)
        assertEquals(convert(1.0, Ounces, Kilograms), 0.028349523125)
        assertEquals(convert(100.0, Ounces, Tones), 0.0028349523125000002) // Calculator yields 0.0028349523125
        assertEquals(convert(1.0, Ounces, Grains), 437.50000000000006)
        assertEquals(convert(1.0, Ounces, Ounces), 1.0)
        assertEquals(convert(1.0, Ounces, Pounds), 0.06250000000000001)
        assertEquals(convert(100.0, Ounces, Stones), 0.4464285714285714)
        assertEquals(convert(100.0, Ounces, UKTones), 0.0027901785714283998) // Calculator yields 0.0027901785714284
        assertEquals(convert(100.0, Ounces, USTones), 0.0031249999999989004) // Calculator yields 0.003124999999998901

        assertEquals(convert(1.0, Pounds, Milligrams), 453592.36999999994) // Calculator yields 453592.37
        assertEquals(convert(1.0, Pounds, Grams), 453.5923699999999) // Calculator yields 453.59237
        assertEquals(convert(1.0, Pounds, Kilograms), 0.4535923699999999) // Calculator yields 0.45359237
        assertEquals(convert(10.0, Pounds, Tones), 0.0045359237)
        assertEquals(convert(1.0, Pounds, Grains), 6999.999999999999) // Calculator yields 7000.000000000001
        assertEquals(convert(1.0, Pounds, Ounces), 15.999999999999996) // Calculator yields 16
        assertEquals(convert(1.0, Pounds, Pounds), 1.0)
        assertEquals(convert(1.0, Pounds, Stones), 0.7142857142857143)
        assertEquals(convert(10.0, Pounds, UKTones), 0.004999999999998241)
        assertEquals(convert(10.0, Pounds, USTones), 0.008482142857142336)

        assertEquals(convert(1.0, Stones, Milligrams), 6350293.18)
        assertEquals(convert(1.0, Stones, Grams), 6350.29318)
        assertEquals(convert(1.0, Stones, Kilograms), 6.35029318)
        assertEquals(convert(1.0, Stones, Tones), 1.0)
        assertEquals(convert(1.0, Stones, Grains), 0.0154324)
        assertEquals(convert(1.0, Stones, Ounces), 1.0)
        assertEquals(convert(1.0, Stones, Pounds), 1.0)
        assertEquals(convert(1.0, Stones, Stones), 1.0)
        assertEquals(convert(1.0, Stones, UKTones), 1.0)
        assertEquals(convert(1.0, Stones, USTones), 1.0)

        assertEquals(convert(1.0, USTones, Milligrams), 1.0)
        assertEquals(convert(1.0, USTones, Grams), 1.0)
        assertEquals(convert(1.0, USTones, Kilograms), 1.0)
        assertEquals(convert(1.0, USTones, Tones), 1.0)
        assertEquals(convert(1.0, USTones, Grains), 0.0154324)
        assertEquals(convert(1.0, USTones, Ounces), 1.0)
        assertEquals(convert(1.0, USTones, Pounds), 1.0)
        assertEquals(convert(1.0, USTones, Stones), 1.0)
        assertEquals(convert(1.0, USTones, UKTones), 1.0)
        assertEquals(convert(1.0, USTones, USTones), 1.0)

        assertEquals(convert(1.0, UKTones, Milligrams), 1.0)
        assertEquals(convert(1.0, UKTones, Grams), 1.0)
        assertEquals(convert(1.0, UKTones, Kilograms), 1.0)
        assertEquals(convert(1.0, UKTones, Tones), 1.0)
        assertEquals(convert(1.0, UKTones, Grains), 0.0154324)
        assertEquals(convert(1.0, UKTones, Ounces), 1.0)
        assertEquals(convert(1.0, UKTones, Pounds), 1.0)
        assertEquals(convert(1.0, UKTones, Stones), 1.0)
        assertEquals(convert(1.0, UKTones, UKTones), 1.0)
        assertEquals(convert(1.0, UKTones, USTones), 1.0)
    }

    @Test
    fun temperatureConversionTest() {
        assertEquals(convert(0.0, Celsius, Fahrenheit), 32.0)
        assertEquals(convert(0.0, Celsius, Kelvin), 273.15)
        assertEquals(convert(0.0, Celsius, Celsius), 0.0)

        assertEquals(convert(0.0, Fahrenheit, Fahrenheit).roundTo(3), 0.0)
        assertEquals(convert(0.0, Fahrenheit, Kelvin).roundTo(3), 255.372)
        assertEquals(convert(0.0, Fahrenheit, Celsius).roundTo(4), -17.7778)

        assertEquals(convert(0.0, Kelvin, Fahrenheit).roundTo(2), -459.67)
        assertEquals(convert(0.0, Kelvin, Kelvin), 0.0)
        assertEquals(convert(0.0, Kelvin, Celsius), -273.15)
    }

    @Test
    fun lengthConversionTest() {
        assertEquals(convert(1.0, Millimeters, Millimeters), 1.0)
        assertEquals(convert(1.0, Millimeters, Centimeters), 0.1)
        assertEquals(convert(1.0, Millimeters, Meters), 0.001)
        assertEquals(convert(1.0, Millimeters, Kilometers), 0.000001)
        assertEquals(convert(1.0, Millimeters, Inches).roundTo(10), 0.0393700787)
        assertEquals(convert(1.0, Millimeters, Feet).roundTo(10), 0.0032808399)
        assertEquals(convert(1.0, Millimeters, Yards).roundTo(10), 0.0010936133)
        assertEquals(convert(10.0, Millimeters, Miles).roundTo(10), 0.0000062137)

        assertEquals(convert(1.0, Centimeters, Millimeters), 10.0)
        assertEquals(convert(1.0, Centimeters, Centimeters), 1.0)
        assertEquals(convert(1.0, Centimeters, Meters), 0.010)
        assertEquals(convert(1.0, Centimeters, Kilometers), 0.00001)
        assertEquals(convert(1.0, Centimeters, Inches), 0.3937007874)
        assertEquals(convert(1.0, Centimeters, Feet).roundTo(10), 0.032808399)
        assertEquals(convert(1.0, Centimeters, Yards).roundTo(10), 0.010936133)
        assertEquals(convert(1.0, Centimeters, Miles).roundTo(10), 0.0000062137)

        assertEquals(convert(1.0, Meters, Millimeters), 1000.0)
        assertEquals(convert(1.0, Meters, Centimeters), 100.0)
        assertEquals(convert(1.0, Meters, Meters), 1.0)
        assertEquals(convert(1.0, Meters, Kilometers), 0.001)
        assertEquals(convert(1.0, Meters, Inches), 39.37007874)
        assertEquals(convert(1.0, Meters, Feet), 3.280839895)
        assertEquals(convert(1.0, Meters, Yards), 1.0936132983)
        assertEquals(convert(1.0, Meters, Miles), 0.0006213712)

        assertEquals(convert(1.0, Kilometers, Millimeters), 1000000.0)
        assertEquals(convert(1.0, Kilometers, Centimeters), 100000.0)
        assertEquals(convert(1.0, Kilometers, Meters), 1000.0)
        assertEquals(convert(1.0, Kilometers, Kilometers), 1.0)
        assertEquals(convert(1.0, Kilometers, Inches), 39370.07874)
        assertEquals(convert(1.0, Kilometers, Feet), 3280.839895)
        assertEquals(convert(1.0, Kilometers, Yards), 1093.6132983)
        assertEquals(convert(1.0, Kilometers, Miles), 0.6213712)

        assertEquals(convert(1.0, Inches, Millimeters).roundTo(3), 25.400)
        assertEquals(convert(1.0, Inches, Centimeters).roundTo(3), 2.540)
        assertEquals(convert(1.0, Inches, Meters).roundTo(3), 0.025)
        assertEquals(convert(1.0, Inches, Kilometers).roundTo(7), 0.0000254)
        assertEquals(convert(1.0, Inches, Inches), 1.0)
        assertEquals(convert(1.0, Inches, Feet).roundTo(7), 0.0833333)
        assertEquals(convert(1.0, Inches, Yards).roundTo(7), 0.0277778)
        assertEquals(convert(1.0, Inches, Miles).roundTo(10), 0.0000157828)

        assertEquals(convert(1.0, Feet, Millimeters).roundTo(3), 304.800)
        assertEquals(convert(1.0, Feet, Centimeters).roundTo(3), 30.480)
        assertEquals(convert(1.0, Feet, Meters).roundTo(4), 0.3048)
        assertEquals(convert(1.0, Feet, Kilometers).roundTo(7), 0.0003048)
        assertEquals(convert(1.0, Feet, Inches), 12.0)
        assertEquals(convert(1.0, Feet, Feet), 1.0)
        assertEquals(convert(1.0, Feet, Yards).roundTo(6), 0.333333)
        assertEquals(convert(1.0, Feet, Miles).roundTo(10), 0.0001893939)

        assertEquals(convert(1.0, Yards, Millimeters).roundTo(3), 914.400)
        assertEquals(convert(1.0, Yards, Centimeters).roundTo(3), 91.440)
        assertEquals(convert(1.0, Yards, Meters).roundTo(4), 0.9144)
        assertEquals(convert(1.0, Yards, Kilometers).roundTo(7), 0.0009144)
        assertEquals(convert(1.0, Yards, Inches).roundTo(3), 36.000)
        assertEquals(convert(1.0, Yards, Feet).roundTo(3), 3.000)
        assertEquals(convert(1.0, Yards, Yards), 1.0)
        assertEquals(convert(1.0, Yards, Miles).roundTo(10), 0.0005681818)

        assertEquals(convert(1.0, Miles, Millimeters).roundTo(1), 1609344.0)
        assertEquals(convert(1.0, Miles, Centimeters).roundTo(2), 160934.4)
        assertEquals(convert(1.0, Miles, Meters).roundTo(3), 1609.344)
        assertEquals(convert(1.0, Miles, Kilometers).roundTo(6), 1.609344)
        assertEquals(convert(1.0, Miles, Inches).roundTo(1), 63360.0)
        assertEquals(convert(1.0, Miles, Feet).roundTo(1), 5280.0)
        assertEquals(convert(1.0, Miles, Yards).roundTo(1), 1760.0)
        assertEquals(convert(1.0, Miles, Miles), 1.0)
    }
}
