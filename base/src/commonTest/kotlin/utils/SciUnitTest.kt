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
import kotlin.test.Test
import kotlin.test.assertEquals

class SciUnitTest {

    @Test
    fun lengthConversionTest() {
        assertEquals(convert(1.0, Millimeters, Millimeters), 1.0)
        assertEquals(convert(1.0, Millimeters, Centimeters), 0.1)
        assertEquals(convert(1.0, Millimeters, Meters), 0.001)
        assertEquals(convert(1.0, Millimeters, Kilometers), 0.000001)
        assertEquals(convert(1.0, Millimeters, Inches).roundTo(10), 0.0393700787)
        assertEquals(convert(1.0, Millimeters, Feet).roundTo(10), 0.0032808399)
        assertEquals(convert(1.0, Millimeters, Yards).roundTo(10), 0.0010936133)
        assertEquals(convert(100.0, Millimeters, Miles).roundTo(10), 0.0000621371)

        assertEquals(convert(1.0, Centimeters, Millimeters), 10.0)
        assertEquals(
            convert(
                1.0, Centimeters, Centimeters
            ), 1.0
        )
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
