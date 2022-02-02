/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.formatter

import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Candela
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decameter
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.FootCandle
import com.splendo.kaluga.scientific.unit.Gigameter
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.Hectopascal
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Kilonewton
import com.splendo.kaluga.scientific.unit.Liter
import com.splendo.kaluga.scientific.unit.Lumen
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Microfarad
import com.splendo.kaluga.scientific.unit.Micrometer
import com.splendo.kaluga.scientific.unit.Mile
import com.splendo.kaluga.scientific.unit.Milliliter
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.Nanometer
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.per
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CommonScientificValueFormatterTest {
    lateinit var formatter: ScientificValueFormatter

    @Test
    fun format__it_converts_scientific_value_to_string() {
        formatter = CommonScientificValueFormatter
        val value = randomScientificValue()

        assertIs<String>(formatter.format(value), "It should convert scientific value to string")
    }

    @Test
    fun format__defaultFormatter__it_uses_value_and_symbol() {
        formatter = CommonScientificValueFormatter

        assertEquals("1 m", formatter.format(1(Meter)))
        assertEquals("2 nm", formatter.format(2(Nanometer)))
        assertEquals("3 μm", formatter.format(3(Micrometer)))
        assertEquals("4 mm", formatter.format(4(Millimeter)))
        assertEquals("5 cm", formatter.format(5(Centimeter)))
        assertEquals("6 dm", formatter.format(6(Decimeter)))
        assertEquals("7 dam", formatter.format(7(Decameter)))
        assertEquals("8 hm", formatter.format(8(Hectometer)))
        assertEquals("9 km", formatter.format(9(Kilometer)))
        assertEquals("10 Mm", formatter.format(10(Megameter)))
        assertEquals("11 Gm", formatter.format(11(Gigameter)))

        assertEquals("1.5 m", formatter.format(1.5(Meter)))
        assertEquals("10 l", formatter.format(10(Liter)))
        assertEquals("10 ml", formatter.format(10(Milliliter)))
        assertEquals("1500 kN", formatter.format(1500(Kilonewton)))
        assertEquals("65 hP", formatter.format(65(Hectopascal)))
        assertEquals("0.5 μF", formatter.format(0.5(Microfarad)))
        assertEquals("16 mi", formatter.format(16(Mile)))
        assertEquals("1 cd", formatter.format(1(Candela)))
        assertEquals("11 lm", formatter.format(11(Lumen)))
        assertEquals("15 lx", formatter.format(15(Lux)))
        assertEquals("0.342 fc", formatter.format(0.342(FootCandle)))
        assertEquals("30 km/h", formatter.format(30(Kilometer per Hour)))
    }

    @Test
    fun format__custom_format_added__it_uses_custom_formatter() {
        formatter = CommonScientificValueFormatter.Builder.build {
            forUnit(Kilometer per Hour).useFormat { "$it км/ч" }
            forUnit(Newton).useFormat { "🍏_$it" }
        }

        assertEquals("1 m", formatter.format(1(Meter)))
        assertEquals("1.5 l", formatter.format(1.5(Liter)))
        assertEquals("5 км/ч", formatter.format(5(Kilometer per Hour)))
        assertEquals("🍏_9.8", formatter.format(9.8(Newton)))
    }
}

private fun randomScientificValue(): ScientificValue<*, *> = listOf(
    (0..10000).random()(Meter),
    Random.nextDouble(0.0, 10000.0)(Liter),
    (0..10000).random()(Newton),
    Random.nextDouble(0.0, 1000.0)(Mile per Hour)
).random()

