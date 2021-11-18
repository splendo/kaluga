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

class DynamicViscosityTest {

    // TODO add expected

    @Test
    fun dynamicViscosityConversionTest() {
        val pascalDynamicViscosity = Pascal x Minute
        assertEquals(1.0, pascalDynamicViscosity.convert(1, Bar x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, Barye x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, Torr x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, Atmosphere x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, MillimeterOfMercury x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, CentimeterOfWater x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, PoundSquareFoot x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, KiloPoundSquareInch x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, pascalDynamicViscosity.convert(1, InchOfWater x Minute))
        val barDynamicViscosity = Bar x Minute
        assertEquals(1.0, barDynamicViscosity.convert(1, Barye x Minute))
        assertEquals(1.0, barDynamicViscosity.convert(1, Torr x Minute))
        assertEquals(1.0, barDynamicViscosity.convert(1, Atmosphere x Minute))
        assertEquals(1.0, barDynamicViscosity.convert(1, MillimeterOfMercury x Minute))
        assertEquals(1.0, barDynamicViscosity.convert(1, CentimeterOfWater x Minute))
        assertEquals(1.0, barDynamicViscosity.convert(1, PoundSquareFoot x Minute))
        assertEquals(1.0, barDynamicViscosity.convert(1, KiloPoundSquareInch x Minute))
        assertEquals(1.0, barDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, barDynamicViscosity.convert(1, InchOfWater x Minute))
        val baryeDynamicViscosity = Barye x Minute
        assertEquals(1.0, baryeDynamicViscosity.convert(1, Torr x Minute))
        assertEquals(1.0, baryeDynamicViscosity.convert(1, Atmosphere x Minute))
        assertEquals(1.0, baryeDynamicViscosity.convert(1, MillimeterOfMercury x Minute))
        assertEquals(1.0, baryeDynamicViscosity.convert(1, CentimeterOfWater x Minute))
        assertEquals(1.0, baryeDynamicViscosity.convert(1, PoundSquareFoot x Minute))
        assertEquals(1.0, baryeDynamicViscosity.convert(1, KiloPoundSquareInch x Minute))
        assertEquals(1.0, baryeDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, baryeDynamicViscosity.convert(1, InchOfWater x Minute))
        val torrDynamicViscosity = Torr x Minute
        assertEquals(1.0, torrDynamicViscosity.convert(1, Atmosphere x Minute))
        assertEquals(1.0, torrDynamicViscosity.convert(1, MillimeterOfMercury x Minute))
        assertEquals(1.0, torrDynamicViscosity.convert(1, CentimeterOfWater x Minute))
        assertEquals(1.0, torrDynamicViscosity.convert(1, PoundSquareFoot x Minute))
        assertEquals(1.0, torrDynamicViscosity.convert(1, KiloPoundSquareInch x Minute))
        assertEquals(1.0, torrDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, torrDynamicViscosity.convert(1, InchOfWater x Minute))
        val atmosphereDynamicViscosity = Atmosphere x Minute
        assertEquals(1.0, atmosphereDynamicViscosity.convert(1, MillimeterOfMercury x Minute))
        assertEquals(1.0, atmosphereDynamicViscosity.convert(1, CentimeterOfWater x Minute))
        assertEquals(1.0, atmosphereDynamicViscosity.convert(1, PoundSquareFoot x Minute))
        assertEquals(1.0, atmosphereDynamicViscosity.convert(1, KiloPoundSquareInch x Minute))
        assertEquals(1.0, atmosphereDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, atmosphereDynamicViscosity.convert(1, InchOfWater x Minute))
        val millimeterOfMercuryDynamicViscosity = MillimeterOfMercury x Minute
        assertEquals(1.0, millimeterOfMercuryDynamicViscosity.convert(1, CentimeterOfWater x Minute))
        assertEquals(1.0, millimeterOfMercuryDynamicViscosity.convert(1, PoundSquareFoot x Minute))
        assertEquals(1.0, millimeterOfMercuryDynamicViscosity.convert(1, KiloPoundSquareInch x Minute))
        assertEquals(1.0, millimeterOfMercuryDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, millimeterOfMercuryDynamicViscosity.convert(1, InchOfWater x Minute))
        val centimeterOfWaterDynamicViscosity = CentimeterOfWater x Minute
        assertEquals(1.0, centimeterOfWaterDynamicViscosity.convert(1, PoundSquareFoot x Minute))
        assertEquals(1.0, centimeterOfWaterDynamicViscosity.convert(1, KiloPoundSquareInch x Minute))
        assertEquals(1.0, centimeterOfWaterDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, centimeterOfWaterDynamicViscosity.convert(1, InchOfWater x Minute))
        val poundSquareFootDynamicViscosity = PoundSquareFoot x Minute
        assertEquals(1.0, poundSquareFootDynamicViscosity.convert(1, KiloPoundSquareInch x Minute))
        assertEquals(1.0, poundSquareFootDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, poundSquareFootDynamicViscosity.convert(1, InchOfWater x Minute))
        val kiloPoundSquareInchDynamicViscosity = KiloPoundSquareInch x Minute
        assertEquals(1.0, kiloPoundSquareInchDynamicViscosity.convert(1, InchOfMercury x Minute))
        assertEquals(1.0, kiloPoundSquareInchDynamicViscosity.convert(1, InchOfWater x Minute))
        assertEquals(1.0, (InchOfMercury x Minute).convert(1, InchOfWater x Minute))
    }
}