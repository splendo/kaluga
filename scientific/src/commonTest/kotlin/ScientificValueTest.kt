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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.converter.decimal.div
import com.splendo.kaluga.scientific.converter.frequency.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.time.frequency
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Hectogram
import com.splendo.kaluga.scientific.unit.Hertz
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareMeter
import kotlin.test.Test
import kotlin.test.assertEquals

class ScientificValueTest {

    data class ValueContainer(val value: DefaultScientificValue<*, *>)

    @Test
    fun testCalculations() {
        assertEquals(15(Meter), 5(Meter) + 100(Decimeter))
        assertEquals(15(Meter), 5(Meter) + 10)
        assertEquals(15(Meter), 10 + 5(Meter))

        assertEquals(5(Meter), 10(Meter) - 50(Decimeter))
        assertEquals(5(Meter), 10(Meter) - 5)
        assertEquals(5(Meter), 15 - 10(Meter))

        assertEquals(9(SquareMeter), 3(Meter) * 30(Decimeter))
        assertEquals(4.0, (2(Hertz) * 2(Second)).toDouble())
        assertEquals(9(Kilogram), 3(Kilogram) * 30(Hectogram))
        assertEquals(9(Kilogram), 3(Kilogram) * 3)
        assertEquals(9(Kilogram), 3 * 3(Kilogram))

        assertEquals(0.5(Hertz), 1.0 / 2(Second))
        assertEquals(3(Kilogram), 9(Kilogram) / 30(Hectogram))
        assertEquals(3(Kilogram), 9(Kilogram) / 3)

        assertEquals(Decimal.PositiveInfinity(Kilogram), 1(Kilogram) / 0(Kilogram))
        assertEquals(Decimal.PositiveInfinity(Kilogram), 1(Kilogram) / 0)
        assertEquals(Decimal.PositiveInfinity(Hertz), 0(Second).frequency())
    }

    // @Test
    // fun testSerialization() {
    //     val value = 10(Meter)
    //     val container = ValueContainer(value)
    //     val json = Json.encodeToString(ValueContainer.serializer(), container)
    //     val decoded = Json.decodeFromString(ValueContainer.serializer(), json)
    //     assertEquals(container, decoded)
    // }
}
