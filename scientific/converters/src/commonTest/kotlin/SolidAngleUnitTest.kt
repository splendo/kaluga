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

package com.splendo.kaluga.scientific.converter

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.converter.illuminance.div
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Candela
import com.splendo.kaluga.scientific.unit.FootCandle
import com.splendo.kaluga.scientific.unit.FootLambert
import com.splendo.kaluga.scientific.unit.Lumen
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.Nit
import com.splendo.kaluga.scientific.unit.Steradian
import kotlin.test.Test
import kotlin.test.assertEquals

class SolidAngleUnitTest {

    @Test
    fun solidAngleFromIlluminanceAndLuminanceTest() {
        assertEqualScientificValue(1(Steradian), 2(Lux) / 2(Nit))
        assertEqualScientificValue(Decimal.PI(Steradian), 2(FootCandle) / 2(FootLambert), round = 32)
    }

    @Test
    fun solidAngleFromLuminousFluxAndIntensityTest() {
        assertEquals(1(Steradian), 2(Lumen) / 2(Candela))
    }
}
