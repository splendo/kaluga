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
import com.splendo.kaluga.scientific.decimalFraction
import com.splendo.kaluga.scientific.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.times
import kotlin.test.Test
import kotlin.test.assertEquals
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
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
        val expected = "0.01".toDecimal()
        assertEquals(expected, result)
    }
}
