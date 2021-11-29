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

import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.luminance.times
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.invoke
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class LuminusIntensityUnitTest {

    @Test
    fun luminusIntensityConversionTest() {
        assertScientificConversion(1, Candela, 1e+9, Nanocandela)
        assertScientificConversion(1, Candela, 1e+6, Microcandela)
        assertScientificConversion(1, Candela, 1000.0, Millicandela)
        assertScientificConversion(1, Candela, 100.0, Centicandela)
        assertScientificConversion(1, Candela, 10.0, Decicandela)
        assertScientificConversion(1, Candela, 0.1, Decacandela)
        assertScientificConversion(1, Candela, 0.01, Hectocandela)
        assertScientificConversion(1, Candela, 0.001, Kilocandela)
        assertScientificConversion(1, Candela, 1e-6, Megacandela)
        assertScientificConversion(1, Candela, 1e-9, Gigacandela)
    }

    @Test
    fun luminousIntensityFromLuminanceAndArea() {
        assertEquals(4(Candela), 2(Nit) * 2(SquareMeter))
        assertEquals(4(Candela), 2(SquareMeter) * 2(Nit))
        assertEquals((4 / PI)(Candela), 2(FootLambert) * 2(SquareFoot))
        assertEquals((4 / PI)(Candela), 2(SquareFoot) * 2(FootLambert))
    }

    @Test
    fun luminousIntensityFromLuminousFluxAndSolidAngleTest() {
        assertEquals(1(Candela), 2(Lumen) / 2(Steradian))
    }
}