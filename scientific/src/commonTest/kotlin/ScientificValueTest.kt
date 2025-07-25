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
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decameter
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Hectogram
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Meter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ScientificValueTest {

    @Serializable
    data class ValueContainer(val value: DefaultScientificValue<*, *>)

    @Test
    fun testCalculations() {
        assertEquals(15(Meter), 5(Meter) + 100(Decimeter))
        assertEquals(15(Meter), 5(Meter) + 10)
        assertEquals(15(Meter), 10 + 5(Meter))

        assertEquals(5(Meter), 10(Meter) - 50(Decimeter))
        assertEquals(5(Meter), 10(Meter) - 5)
        assertEquals(5(Meter), 15 - 10(Meter))

        assertEquals(9(Kilogram), 3(Kilogram) * 30(Hectogram))
        assertEquals(9(Kilogram), 3(Kilogram) * 3)
        assertEquals(9(Kilogram), 3 * 3(Kilogram))

        assertEquals(3(Kilogram), 9(Kilogram) / 30(Hectogram))
        assertEquals(3(Kilogram), 9(Kilogram) / 3)

        assertEquals(Decimal.PositiveInfinity(Kilogram), 1(Kilogram) / 0(Kilogram))
        assertEquals(Decimal.PositiveInfinity(Kilogram), 1(Kilogram) / 0)
    }

    @Test
    fun testSerialization() {
        val value = 10(Meter)
        val container = ValueContainer(value)
        val json = Json.encodeToString(ValueContainer.serializer(), container)
        val decoded = Json.decodeFromString(ValueContainer.serializer(), json)
        assertEquals(container, decoded)
    }

    @Test
    fun testSplit() {
        val (foot, inch) = 15(Inch).split(Foot, Inch)
        assertEquals(1(Foot), foot)
        assertEquals(3(Inch), inch)

        val (fullInch, zeroFoot) = 15(Inch).split(Inch, Foot)
        assertEquals(15(Inch), fullInch)
        assertEquals(0(Foot), zeroFoot)

        val (roundedFoot, roundedInch) = 11.999999999999(Inch).split(Foot, Inch)
        assertEquals(1(Foot), roundedFoot)
        assertEquals(0(Inch), roundedInch)

        val (meter, centimeter) = 2.34(Meter).split(Centimeter, 1U)
        assertEquals(2.3(Meter), meter)
        assertEquals(4(Centimeter), centimeter)

        val (roundedMeter, meterFraction) = 2.34(Meter).split(Meter)
        assertEquals(2(Meter), roundedMeter)
        assertEquals(0.34(Meter), meterFraction)
    }

    @Test
    fun testToComponents() {
        val length = "12345.678".toDecimal()(Meter)

        length.toComponents(Meter, Centimeter) { meter, centimeter ->
            assertEquals(12345(Meter), meter)
            assertEquals(67.8(Centimeter), centimeter)
        }

        length.toComponents(Kilometer, Meter, Centimeter) { kilometer, meter, centimeter ->
            assertEquals(12(Kilometer), kilometer)
            assertEquals(345(Meter), meter)
            assertEquals(67.8(Centimeter), centimeter)
        }

        length.toComponents(Kilometer, Decameter, Meter, Centimeter) { kilometer, decameter, meter, centimeter ->
            assertEquals(12(Kilometer), kilometer)
            assertEquals(34(Decameter), decameter)
            assertEquals(5(Meter), meter)
            assertEquals(67.8(Centimeter), centimeter)
        }

        length.toComponents(Kilometer, Hectometer, Decameter, Meter, Centimeter) { kilometer, hectometer, decameter, meter, centimeter ->
            assertEquals(12(Kilometer), kilometer)
            assertEquals(3(Hectometer), hectometer)
            assertEquals(4(Decameter), decameter)
            assertEquals(5(Meter), meter)
            assertEquals(67.8(Centimeter), centimeter)
        }

        length.toComponents(Kilometer, Centimeter, 3U) { kilometer, centimeter ->
            assertEquals("12.345".toDecimal()(Kilometer), kilometer)
            assertEquals(67.8(Centimeter), centimeter)
        }

        length.toComponents(Meter, Kilometer) { meter, kilometer ->
            assertEquals(12345.0(Meter), meter)
            assertEquals("0.000678".toDecimal()(Kilometer), kilometer)
        }

        length.toComponents(Kilometer, Kilometer) { kilometer, fractionKilometer ->
            assertEquals(12(Kilometer), kilometer)
            assertEquals("0.345678".toDecimal()(Kilometer), fractionKilometer)
        }

        length.toComponents(Kilometer, Kilometer, Kilometer) { kilometer, zeroKilometer, fractionKilometer ->
            assertEquals(12(Kilometer), kilometer)
            assertEquals(0(Kilometer), zeroKilometer)
            assertEquals("0.345678".toDecimal()(Kilometer), fractionKilometer)
        }

        length.toComponents(Kilometer, Meter, Kilometer) { kilometer, meter, fractionKilometer ->
            assertEquals(12(Kilometer), kilometer)
            assertEquals(345(Meter), meter)
            assertEquals("0.000678".toDecimal()(Kilometer), fractionKilometer)
        }
    }
}
