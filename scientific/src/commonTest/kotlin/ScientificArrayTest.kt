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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.base.utils.toInt
import com.splendo.kaluga.base.utils.toIntArray
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.SquareMeter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ScientificArrayTest {

    @Test
    fun testCreateScientificArray() {
        val listOfLength = listOf(1, 2, 3, 4, 5)(Meter)
        assertEquals(5, listOfLength.size)
        assertEquals(Meter, listOfLength.unit)
        assertEquals(listOf(1.0, 2.0, 3.0, 4.0, 5.0), listOfLength.values.asList())
        assertEquals(listOf(1.0, 2.0, 3.0, 4.0, 5.0), listOfLength.decimalValues.map { it.toDouble() })

        val intListOfLength = listOf(1, 2, 3)(Meter, ::MockIntScientificArray)
        assertEquals(listOf(1, 2, 3), intListOfLength.values.asList())
    }

    @Test
    fun testJoinScientificValuesToArray() {
        val listOfLength = listOf(1(Meter), 2(Meter), 3(Meter)).toScientificArray(Millimeter)
        assertEquals(Millimeter, listOfLength.unit)
        assertEquals(listOf(1000.0, 2000.0, 3000.0), listOfLength.values.asList())
    }

    @Test
    fun testSplitScientificArrayToListOfValues() {
        val listOfValues = listOf(1, 2, 3)(Meter).split(Millimeter)
        assertEquals(3, listOfValues.size)
        assertEquals(1000(Millimeter), listOfValues[0])
        assertEquals(2000(Millimeter), listOfValues[1])
        assertEquals(3000(Millimeter), listOfValues[2])
    }

    @Test
    fun testConvertScientificArray() {
        val listOfLength = listOf(1, 2, 3, 4, 5)(Meter).convert(Millimeter)
        assertEquals(Millimeter, listOfLength.unit)
        assertEquals(listOf(1000.0, 2000.0, 3000.0, 4000.0, 5000.0), listOfLength.values.asList())

        val intListOfLength = listOfLength.convert(Meter, ::MockIntScientificArray)
        assertEquals(listOf(1, 2, 3, 4, 5), intListOfLength.values.asList())

        assertEquals(listOf(1, 2, 3, 4, 5), listOfLength.convertValues(Meter).map { it.toInt() })
    }

    @Test
    fun testMapScientificArray() {
        val listOfLength = listOf(1, 2, 3, 4, 5)(Meter)
        val listOfArea = listOfLength.map { times(2(Meter)) }
        assertEquals(SquareMeter, listOfArea.unit)
        assertEquals(listOf(2.0, 4.0, 6.0, 8.0, 10.0), listOfArea.values.asList())
    }

    @Test
    fun testCombineScientificArray() {
        val listOfLength = listOf(1, 2, 3, 4, 5)(Meter)
        val listOfWidth = listOf(2, 4, 6, 8, 10)(Meter)
        val listOfArea = listOfLength.combine(listOfWidth) { times(it) }
        assertEquals(SquareMeter, listOfArea.unit)
        assertEquals(listOf(2.0, 8.0, 18.0, 32.0, 50.0), listOfArea.values.asList())

        assertFailsWith(IndexOutOfBoundsException::class, "Should fail with Index out of bounds exception") { listOfLength.combine(listOf(2)(Meter)) { times(it) } }
    }

    @Test
    fun testConcatScientificArray() {
        val listOfLength = listOf(1, 2, 3)(Meter) + listOf(40, 50)(Decimeter) + (0.1)(Kilometer)
        assertEquals(Meter, listOfLength.unit)
        assertEquals(listOf(1.0, 2.0, 3.0, 4.0, 5.0, 100.0), listOfLength.values.asList())
    }
}

class MockIntScientificArray<Type : PhysicalQuantity, Unit : ScientificUnit<Type>>(decimals: List<Decimal>, override val unit: Unit) : IntScientificArray<Type, Unit> {
    override val values: IntArray = decimals.toIntArray()
}
