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

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.illuminance.div
import com.splendo.kaluga.scientific.converter.luminousIntensity.div
import com.splendo.kaluga.scientific.invoke
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class LuminanceUnitTest {

    @Test
    fun luminanceConversionTest() {
        assertScientificConversion(1, Nit, 1e+9, Nanonit)
        assertScientificConversion(1, Nit, 1e+6, Micronit)
        assertScientificConversion(1, Nit, 1000.0, Millinit)
        assertScientificConversion(1, Nit, 100.0, Centinit)
        assertScientificConversion(1, Nit, 10.0, Decinit)
        assertScientificConversion(1, Nit, 0.1, Decanit)
        assertScientificConversion(1, Nit, 0.01, Hectonit)
        assertScientificConversion(1, Nit, 0.001, Kilonit)
        assertScientificConversion(1, Nit, 1e-6, Meganit)
        assertScientificConversion(1, Nit, 1e-9, Giganit)

        assertScientificConversion(1, Nit, 3.14159265359, Apostilb, 11)
        assertScientificConversion(1, Nit, 0.0003141593, Lambert, 10)
        assertScientificConversion(1, Nit, 3141.5927, Skot, 4)
        assertScientificConversion(1, Nit, 31415926.536, Bril, 3)
        assertScientificConversion(1, Nit, 0.2918635, FootLambert, 7)
    }

    @Test
    fun luminanceFromIlluminanceAndSolidAngleTest() {
        assertEquals(1(Stilb), 2(Phot) / 2(Steradian))
        assertEquals(1(Stilb), 20(Deciphot) / 2(Steradian))
        assertEquals(1(Nit), 2(Lux) / 2(Steradian))
        assertEqualScientificValue(PI(FootLambert), 2(FootCandle) / 2(Steradian), 8)
        assertEquals(1(Nit), 2(Lux).convert(FootLambert as Illuminance) / 2(Steradian))
    }

    @Test
    fun luminanceFromLuminousIntensityAndAreaTest() {
        assertEquals(1(Stilb), 2(Candela) / 2(SquareCentimeter))
        assertEquals(1(Nit), 2(Candela) / 2(SquareMeter))
        assertEqualScientificValue(PI(FootLambert), (2(Candela) / 2(SquareFoot)), 8)
        assertEquals(1(Nit), 2(Candela) / 2(SquareMeter).convert(SquareFoot as Area))
    }
}