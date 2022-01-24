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
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class DimensionlessUnitTest {
    @Test
    fun oneUnitConstantTest() {
        val one = One.constant
        val result = one.decimalValue
        val expected = 1.0.toDecimal()
        assertEquals(expected, result)
    }

    fun oneUnitFromDecimalTest() {
        val base = 123.0(One)
        val result = base.decimalValue
        val expected = 123.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun percentFromDecimalTest() {
        val percent = 1(Percent)
        val result = percent.decimalValue
        val expected = 1.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun percentTimesDecimalTest() {
        val base = 120.0.toDecimal()
        val percent = 1(Percent)
        val result = base * percent
        val expected = 1.2.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun percentDivisorForDecimalTest() {
        val base = 120.0.toDecimal()
        val percent = 1(Percent)
        val result = base / percent
        val expected = 12000.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun percentTimesUnitTest() {
        val base = 120(Kilogram)
        val percent = 1(Percent)
        val result = base * percent
        val expected = 1.2(Kilogram)
        assertEquals(expected, result)
    }

    @Test
    fun percentDivisorForUnitTest() {
        val base = 120(Kilogram)
        val percent = 1(Percent)
        val result = base / percent
        val expected = 12000.0(Kilogram)
        assertEquals(expected, result)
    }
    @Test
    fun permillFromDecimalTest() {
        val permill = 1(Permill)
        val result = permill.decimalValue
        val expected = 1.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun permillTimesDecimalTest() {
        val base = 120.0.toDecimal()
        val permill = 1(Permill)
        val result = base * permill
        val expected = 0.12.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun permillDivisorForDecimalTest() {
        val base = 120.0.toDecimal()
        val permill = 1(Permill)
        val result = base / permill
        val expected = 120000.0.toDecimal()
        assertEquals(expected, result)
    }

    @Test
    fun permillTimesUnitTest() {
        val base = 120(Kilogram)
        val permill = 1(Permill)
        val result = base * permill
        val expected = 0.12(Kilogram)
        assertEquals(expected, result)
    }

    @Test
    fun permillDivisorForUnitTest() {
        val base = 120(Kilogram)
        val permill = 1(Permill)
        val result = base / permill
        val expected = 120000.0(Kilogram)
        assertEquals(expected, result)
    }
}