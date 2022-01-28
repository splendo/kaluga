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
import com.splendo.kaluga.scientific.formatter.DefaultFormatter
import com.splendo.kaluga.scientific.formatter.Formatter
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.Liter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Mile
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.per
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

class DefaultFormatterTest {
    lateinit var formatter: Formatter

    @BeforeTest
    fun setUp() {
        formatter = DefaultFormatter()
    }

    @Test
    fun format__it_converts_scientific_value_to_string() {
        val value = randomScientificValue()

        assertIs<String>(formatter.format(value), "It should convert scientific value to string")
    }
}

private fun randomScientificValue(): ScientificValue<*, *> = someScientificValues.random()
private val someScientificValues = listOf<ScientificValue<*, *>>(
    (0..10000).random()(Meter),
    Random.nextDouble(0.0,10000.0)(Liter),
    (0..10000).random()(Newton),
    Random.nextDouble(0.0, 1000.0)(Mile per Hour)
)