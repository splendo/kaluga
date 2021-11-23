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

class SurfaceTensionUnitTest {

    @Test
    fun metricSurfaceTensionConversionTest() {
        assertEquals(1_000.0, (Newton per Meter).convert(1.0, Newton per Kilometer))
        assertEquals(1_000.0, (Dyne per Meter).convert(1.0, Dyne per Kilometer))
    }

    @Test
    fun imperialSurfaceTensionConversionTest() {
        assertEquals(0.03, (Poundal per Inch).convert(1.0, PoundForce per Inch, 2))
        assertEquals(12.0, (Poundal per Inch).convert(1.0, Poundal per Foot))

        assertEquals(32.17, (PoundForce per Inch).convert(1.0, Poundal per Inch, 2))
        assertEquals(12.0, (PoundForce per Inch).convert(1.0, PoundForce per Foot))
    }

    @Test
    fun ukImperialSurfaceTensionConversionTest() {
        assertEquals(12.0, (ImperialTonForce per Inch).convert(1.0, ImperialTonForce per Foot, 2))
    }

    @Test
    fun usCustomarySurfaceTensionConversionTest() {
        assertEquals(0.5, (Kip per Inch).convert(1.0, UsTonForce per Inch, 2))
        assertEquals(12.0, (Kip per Inch).convert(1.0, Kip per Foot))

        assertEquals(2.0, (UsTonForce per Inch).convert(1.0, Kip per Inch, 2))
        assertEquals(12.0, (UsTonForce per Inch).convert(1.0, UsTonForce per Foot))
    }
}