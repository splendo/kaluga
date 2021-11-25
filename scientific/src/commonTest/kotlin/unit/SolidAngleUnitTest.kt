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
import com.splendo.kaluga.scientific.converter.illuminance.div
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class SolidAngleUnitTest {

    @Test
    fun solidAngleConversionTest() {
        assertEquals(1e+9, Steradian.convert(1, Nanosteradian))
        assertEquals(1e+6, Steradian.convert(1, Microsteradian))
        assertEquals(1000.0, Steradian.convert(1, Millisteradian))
        assertEquals(100.0, Steradian.convert(1, Centisteradian))
        assertEquals(10.0, Steradian.convert(1, Decisteradian))

        assertEquals(0.0796, Steradian.convert(1, Spat, 4))
        assertEquals(3282.8063500117, Steradian.convert(1, SquareDegree, 10))

        assertEquals(41253.0, Spat.convert(1, SquareDegree, 0))
    }

    @Test
    fun solidAngleFromIlluminanceAndLuminanceTest() {
        assertEqualScientificValue(3.14159(Steradian), 2(Lux) / 2(Nit), 5)
        // FIXME check expected value
        assertEquals(3.14159(Steradian), 2(FootCandle) / 2(FootLambert))
    }

    @Test
    fun solidAngleFromLuminousFluxAndIntensityTest() {
        assertEquals(1(Steradian), 2(Lumen) / 2(Candela))
    }
}
