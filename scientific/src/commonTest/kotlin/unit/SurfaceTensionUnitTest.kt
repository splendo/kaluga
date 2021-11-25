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

import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class SurfaceTensionUnitTest {

    @Test
    fun surfaceTensionConversionTest() {
        assertScientificConversion(1.0, (Newton per Centimeter), 6.85, PoundForce per Foot, 2)
        assertScientificConversion(1.0, (ImperialTonForce per Foot), 1.12, UsTonForce per Foot, 2)
    }

    @Test
    fun surfaceTensionFromEnergyAndAreaTest() {
        assertEquals(1(Newton per Meter), 2(Joule) / 2(SquareMeter))
        assertEquals(1(PoundForce per Foot), 2(FootPoundForce) / 2(SquareFoot))
    }

    @Test
    fun surfaceTensionFromForceAndLengthTest() {
        assertEquals(1(Newton per Meter), 2(Joule) / 2(SquareMeter))
        assertEquals(1(PoundForce per Foot), 2(FootPoundForce) / 2(SquareFoot))
    }
}