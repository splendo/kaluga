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

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.illuminance.times
import com.splendo.kaluga.scientific.converter.luminousEnergy.div
import com.splendo.kaluga.scientific.converter.luminousIntensity.times
import com.splendo.kaluga.scientific.converter.solidAngle.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class LuminusFluxUnitTest {

    @Test
    fun luminusFluxConversionTest() {
        assertScientificConversion(1, Lumen, 1e+9, Nanolumen)
        assertScientificConversion(1, Lumen, 1e+6, Microlumen)
        assertScientificConversion(1, Lumen, 1000.0, Millilumen)
        assertScientificConversion(1, Lumen, 100.0, Centilumen)
        assertScientificConversion(1, Lumen, 10.0, Decilumen)
        assertScientificConversion(1, Lumen, 0.1, Decalumen)
        assertScientificConversion(1, Lumen, 0.01, Hectolumen)
        assertScientificConversion(1, Lumen, 0.001, Kilolumen)
        assertScientificConversion(1, Lumen, 1e-6, Megalumen)
        assertScientificConversion(1, Lumen, 1e-9, Gigalumen)
    }

    @Test
    fun luminousFluxFromIlluminanceAndAreaTest() {
        assertEquals(4(Lumen), 2(Lux) * 2(SquareMeter))
        assertEquals(4(Lumen), 2(SquareMeter) * 2(Lux))
        assertEquals(4(Lumen), 2(FootCandle) * 2(SquareFoot))
        assertEquals(4(Lumen), 2(SquareFoot) * 2(FootCandle))
    }

    @Test
    fun luminousFluxFromLuminousEnergyAndTimeTest() {
        assertEqualScientificValue(1(Lumen), 2(Lumen x Second) / 2(Second))
    }

    @Test
    fun luminousFluxFromIntensityAndSolidAngleTest() {
        assertEquals(4(Lumen), 2(Candela) * 2(Steradian))
        assertEquals(4(Lumen), 2(Steradian) * 2(Candela))
    }
}
