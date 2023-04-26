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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.converter.decimal.div
import com.splendo.kaluga.scientific.converter.decimal.times
import com.splendo.kaluga.scientific.converter.dimensionless.div
import com.splendo.kaluga.scientific.converter.dimensionless.times
import com.splendo.kaluga.scientific.decimalFraction
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.div
import com.splendo.kaluga.scientific.times
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class DimensionlessUnitTest {
    @Test
    fun oneUnitConstantTest() {
        val constant = One.constant
        val result = constant.value.toDecimal()
        val expected = 1.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun oneUnitConstantDecimalRepresentationTest() {
        val constant = One.constant
        val result = constant.decimalValue
        val expected = 1.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun oneUnitFromDecimalTest() {
        val dimensionlessValue = 123.0(One)
        val result = dimensionlessValue.value.toDecimal()
        val expected = 123.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun oneUnitDecimalRepresentationTest() {
        val dimensionlessValue = 123.0(One)
        val result = dimensionlessValue.decimalValue
        val expected = 123.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun oneUnitMultiplicationTest() {
        val dimensionlessValue1 = 123.0(One)
        val dimensionlessValue2 = 2.0(One)
        val result = dimensionlessValue1 * dimensionlessValue2
        val expected = 246.0(One)
        assertEquals(expected, result)
    }

    @Test
    fun oneUnitDivisionTest() {
        val dimensionlessValue1 = 246.0(One)
        val dimensionlessValue2 = 2.0(One)
        val result = dimensionlessValue1 / dimensionlessValue2
        val expected = 123.0(One)
        assertEquals(expected, result)
    }

    @Test
    fun percentFromDecimalTest() {
        val percent = 1(Percent)
        val result = percent.value.toDecimal()
        val expected = 1.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun percentDecimalRepresentationTest() {
        val percent = 1(Percent)
        val result = percent.decimalFraction
        val expected = 0.01.toDecimal()
        assertEquals(expected, result)
    }

    // 120 * 1% = 1.2
    // https://www.wolframalpha.com/input/?i=120+*+1%25
    @Test
    fun percentTimesDecimalTest() {
        val decimalValue = 120.0.toDecimal()
        val percent = 1(Percent)
        val result = decimalValue * percent
        val expected = 1.2.toDecimal()
        assertEquals(expected, result)
    }

    // 1% * 120 = 1.2
    // https://www.wolframalpha.com/input/?i=1%25+*+120
    @Test
    fun percentTimesDecimalInverseTest() {
        val decimalValue = 120.0.toDecimal()
        val percent = 1(Percent)
        val result = percent * decimalValue
        val expected = 1.2(One)
        assertEquals(expected, result)
    }

    // 120 / (12%) = 1000
    // https://www.wolframalpha.com/input/?i=120+%2F+%2812%25%29
    @Test
    fun percentDivisorForDecimalTest() {
        val decimalValue = 120.0.toDecimal()
        val percent = 12(Percent)
        val result = decimalValue / percent
        val expected = 1000.0.toDecimal()
        assertEquals(expected, result)
    }

    // (12%) / 120 = 0.001
    // https://www.wolframalpha.com/input/?i=%2812%25%29+%2F+120
    @Test
    fun percentDivisorForDecimalInverseTest() {
        val decimalValue = 120.0.toDecimal()
        val percent = 12(Percent)
        val result = percent / decimalValue
        val expected = 0.001(One)
        assertEquals(expected, result)
    }

    // 120kg * (1%) = 1.2kg
    // https://www.wolframalpha.com/input/?i=120kg+*+%281%25%29
    @Test
    fun percentTimesUnitTest() {
        val scientificValue = 120(Kilogram)
        val percent = 1(Percent)
        val result = scientificValue * percent
        val expected = 1.2(Kilogram)
        assertEquals(expected, result)
    }

    // (1%) * 120kg = 1.2kg
    // https://www.wolframalpha.com/input/?i=%281%25%29+*+120kg
    @Test
    fun percentTimesUnitInverseTest() {
        val scientificValue = 120(Kilogram)
        val percent = 1(Percent)
        val result = percent * scientificValue
        val expected = 1.2(Kilogram)
        assertEquals(expected, result)
    }

    // 120kg / (1%) = 12000kg
    // https://www.wolframalpha.com/input/?i=%281%25%29+*+120kg
    @Test
    fun percentDivisorForUnitTest() {
        val scientificValue = 120(Kilogram)
        val percent = 1(Percent)
        val result = scientificValue / percent
        val expected = 12000.0(Kilogram)
        assertEquals(expected, result)
    }

    @Test
    fun percentDivisorForUnitInverseTest() {
        /**
         * Dividing a Dimensionless value by a Scientific Unit is not a permitted operation.
         * This is because dividing a dimensionless value by an Unit (of any Quantity)
         * Should produce an Inverse of that unit and currently those are not supported.
         *
         * For example:
         * 9 / (3kg) = 3(kg^-1) [read as: three reciprocal kilograms]
         *
         * see: https://www.wolframalpha.com/input/?i=9+%2F+%283kg%29
         */
        assertFails { throw Exception("Invalid Operation") }
    }

    @Test
    fun permillFromDecimalTest() {
        val permill = 1(Permill)
        val result = permill.value.toDecimal()
        val expected = 1.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun permillFromDecimalRepresentationTest() {
        val percent = 1(Percent)
        val result = percent.decimalFraction
        val expected = 0.01.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun permillTimesDecimalTest() {
        val decimalValue = 120.0.toDecimal()
        val permill = 1(Permill)
        val result = decimalValue * permill
        val expected = 0.12.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun permillDivisorForDecimalTest() {
        val decimalValue = 120.0.toDecimal()
        val permill = 1(Permill)
        val result = decimalValue / permill
        val expected = 120000.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun permillTimesUnitTest() {
        val scientificValue = 120(Kilogram)
        val permill = 1(Permill)
        val result = scientificValue * permill
        val expected = 0.12(Kilogram)
        assertEquals(expected, result)
    }

    @Test
    fun permillDivisorForUnitTest() {
        val scientificValue = 120(Kilogram)
        val permill = 1(Permill)
        val result = scientificValue / permill
        val expected = 120000.0(Kilogram)
        assertEquals(expected, result)
    }
}
