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
import com.splendo.kaluga.scientific.converter.luminance.times
import com.splendo.kaluga.scientific.converter.luminousExposure.div
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.converter.solidAngle.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class IlluminanceUnitTest {

    @Test
    fun illuminanceConversionTest() {
        assertScientificConversion(1, Lux, 1e+9, Nanolux)
        assertScientificConversion(1, Lux, 1e+6, Microlux)
        assertScientificConversion(1, Lux, 1000.0, Millilux)
        assertScientificConversion(1, Lux, 100.0, Centilux)
        assertScientificConversion(1, Lux, 10.0, Decilux)
        assertScientificConversion(1, Lux, 0.1, Decalux)
        assertScientificConversion(1, Lux, 0.01, Hectolux)
        assertScientificConversion(1, Lux, 0.001, Kilolux)
        assertScientificConversion(1, Lux, 1e-6, Megalux)
        assertScientificConversion(1, Lux, 1e-9, Gigalux)

        assertScientificConversion(1, Lux, 0.0001, Phot)
        assertScientificConversion(1, Lux, 0.092903, FootCandle, 6)

        assertScientificConversion(1, Phot, 1e+9, Nanophot)
        assertScientificConversion(1, Phot, 1e+6, Microphot)
        assertScientificConversion(1, Phot, 1000.0, Milliphot)
        assertScientificConversion(1, Phot, 100.0, Centiphot)
        assertScientificConversion(1, Phot, 10.0, Deciphot)
        assertScientificConversion(1, Phot, 0.1, Decaphot)
        assertScientificConversion(1, Phot, 0.01, Hectophot)
        assertScientificConversion(1, Phot, 0.001, Kilophot)
        assertScientificConversion(1, Phot, 1e-6, Megaphot)
        assertScientificConversion(1, Phot, 1e-9, Gigaphot)
    }

    @Test
    fun illuminanceFromLuminanceAndSolidAngleTest() {
        assertEquals(4(Lux), 2(Nit) * 2(Steradian))
        assertEquals(4(Lux), 2(Steradian) * 2(Nit))
        // assertEquals(4(FootCandle),2(FootLambert) * 2(Steradian)) FIXME Yields 1.2732395447351628 find if that is the case
        // assertEquals(4(FootCandle),2(Steradian) * 2(FootLambert))
    }

    @Test
    fun illuminanceFromLuminousExposureAndTimeTest() {
        assertEqualScientificValue(1(Lux), 2(Lux x Second) / 2(Second))
        assertEqualScientificValue(1(FootCandle), 2(FootCandle x Second) / 2(Second))
    }

    @Test
    fun illuminanceFromLuminousFluxAndAreaTest() {
        assertEquals(1(Lux), 2(Lumen) / 2(SquareMeter))
        assertEquals(1(FootCandle), 2(Lumen) / 2(SquareFoot))
    }
}
