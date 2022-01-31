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

package com.splendo.kaluga.scientific.forrmatter

import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.formatter.CommonFormatter
import com.splendo.kaluga.scientific.formatter.Formatter
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Hectopascal
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.Kilonewton
import com.splendo.kaluga.scientific.unit.Liter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Mile
import com.splendo.kaluga.scientific.unit.Milliliter
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.per
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CommonFormatterTest {
    lateinit var formatter: Formatter

    @Test
    fun format__it_converts_scientific_value_to_string() {
        formatter = CommonFormatter.default
        val value = randomScientificValue()

        assertIs<String>(formatter.format(value), "It should convert scientific value to string")
    }

    @Test
    fun format__defaultFormatter__it_uses_value_and_symbol() {
        formatter = CommonFormatter.default

        assertEquals("1 m", formatter.format(1(Meter)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
        assertEquals("10 l", formatter.format(10(Liter)))
        // assertEquals("10 ml", formatter.format(10(Milliliter))) // FIXME: Bug in kaluga, it prints cl (centiliter). The same for all Milli<Unit>
        // assertEquals("150 mm", formatter.format(150(Millimeter)))
        assertEquals("150 cm", formatter.format(150(Centimeter)))
        assertEquals("1500 kN", formatter.format(1500(Kilonewton)))
        assertEquals("65 hP", formatter.format(65(Hectopascal)))
    }

    @Test
    fun testFormatting() {
        formatter = CommonFormatter.default

        // assertEquals("1.0 m", formatter.format(1(Meter)))
    }
}

private fun randomScientificValue(): ScientificValue<*, *> = someScientificValues.random()
private val someScientificValues = listOf<ScientificValue<*, *>>(
    (0..10000).random()(Meter),
    Random.nextDouble(0.0,10000.0)(Liter),
    (0..10000).random()(Newton),
    Random.nextDouble(0.0, 1000.0)(Mile per Hour)
)