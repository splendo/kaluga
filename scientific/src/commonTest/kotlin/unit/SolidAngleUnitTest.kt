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
import com.splendo.kaluga.scientific.converter.illuminance.div
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.invoke
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class SolidAngleUnitTest {

    @Test
    fun solidAngleConversionTest() {
        assertScientificConversion(1, Steradian, 1e+9, Nanosteradian)
        assertScientificConversion(1, Steradian, 1e+6, Microsteradian)
        assertScientificConversion(1, Steradian, 1000.0, Millisteradian)
        assertScientificConversion(1, Steradian, 100.0, Centisteradian)
        assertScientificConversion(1, Steradian, 10.0, Decisteradian)

        assertScientificConversion(1, Steradian, 0.0796, Spat, 4)
        assertScientificConversion(1, Steradian, 3282.8063500117, SquareDegree, 10)
    }

    @Test
    fun solidAngleFromIlluminanceAndLuminanceTest() {
        assertEqualScientificValue(1(Steradian), 2(Lux) / 2(Nit))
        assertEqualScientificValue(PI(Steradian), 2(FootCandle) / 2(FootLambert))
    }

    @Test
    fun solidAngleFromLuminousFluxAndIntensityTest() {
        assertEquals(1(Steradian), 2(Lumen) / 2(Candela))
    }
}
