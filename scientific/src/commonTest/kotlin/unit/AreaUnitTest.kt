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

package com.splendo.kaluga.scientific.unit

import kotlin.test.Test
import kotlin.test.assertEquals

class AreaUnitTest {

    @Test
    fun areaConversionTest() {
        assertEquals(1e+18, SquareMeter.convert(1, SquareNanometer))
        assertEquals(1e+12, SquareMeter.convert(1, SquareMicrometer))
        assertEquals(1000000.0, SquareMeter.convert(1, SquareMillimeter))
        assertEquals(10000.0, SquareMeter.convert(1, SquareCentimeter))
        assertEquals(100.0, SquareMeter.convert(1, SquareDecimeter))
        assertEquals(0.01, SquareMeter.convert(1, SquareDecameter))
        assertEquals(0.0001, SquareMeter.convert(1, SquareHectometer))
        assertEquals(1e-6, SquareMeter.convert(1, SquareKilometer))
        assertEquals(0.0001, SquareMeter.convert(1, Hectare))


        assertEquals(1550.0, SquareMeter.convert(1, SquareInch, 0))
        assertEquals(10.7639, SquareMeter.convert(1, SquareFoot, 4))
        assertEquals(1.19599, SquareMeter.convert(1, SquareYard, 5))
        assertEquals(3.86102e-7, SquareMeter.convert(1, SquareMile, 12))
        assertEquals(0.000247105, SquareMeter.convert(1, Acre, 9))
    }

    @Test
    fun areaDensityConversionTest() {
        assertEquals(0.204816, (Kilogram per SquareMeter).convert(1, Pound per SquareFoot, 6))
        assertEquals(6.022e+26, (Kilogram per SquareMeter).convert(1, Dalton per SquareMeter))
    }
}