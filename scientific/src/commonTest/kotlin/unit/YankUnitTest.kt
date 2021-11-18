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

class YankUnitTest {

    @Test
    fun metricForceConversionTest() {
        assertEquals(3.6, (Newton per Second).convert(1.0, Kilonewton per Hour))
        assertEquals(3.6, (Dyne per Second).convert(1.0, Kilodyne per Hour))
        assertEquals(3.6, (GramForce per Second).convert(1.0, KilogramForce per Hour))
    }

    @Test
    fun imperialForceConversionTest() {
        assertEquals(57600.0, (PoundForce per Second).convert(1.0, OunceForce per Hour))
        assertEquals(115826.57, (PoundForce per Second).convert(1.0, Poundal per Hour, 2))
    }

    @Test
    fun usImperialForceConversionTest() {
        assertEquals(1800.0, (Kip per Second).convert(1.0, UsTonForce per Hour))
    }

    @Test
    fun metricToImperialForceConversionTest() {
        assertEquals(0.22, (Newton per Second).convert(1.0, PoundForce per Second, 2))
        assertEquals(0.0000022, (Dyne per Second).convert(1.0, PoundForce per Second, 7))
        assertEquals(0.0022, (GramForce per Second).convert(1.0, PoundForce per Second, 4))
    }

    @Test
    fun metricToUsImperialForceConversionTest() {
        assertEquals(0.00022, (Newton per Second).convert(1.0, Kip per Second, 5))
        assertEquals(0.0000000022, (Dyne per Second).convert(1.0, Kip per Second, 10))
        assertEquals(0.0000022, (GramForce per Second).convert(1.0, Kip per Second, 7))

        assertEquals(112.40, (Kilonewton per Second).convert(1_000.0, UsTonForce per Second, 2))
        assertEquals(0.0011, (Kilodyne per Second).convert(1_000.0, UsTonForce per Second, 4))
        assertEquals(1102.31, (TonneForce per Second).convert(1_000.0, UsTonForce per Second, 2))
    }

    @Test
    fun metricToUkImperialForceConversionTest() {
        assertEquals(100.36, (Kilonewton per Second).convert(1_000.0, ImperialTonForce per Second, 2))
        assertEquals(0.0010036, (Kilodyne per Second).convert(1_000.0, ImperialTonForce per Second, 7))
        assertEquals(984.21, (TonneForce per Second).convert(1_000.0, ImperialTonForce per Second, 2))
    }

    @Test
    fun ukImperialToUsCustomaryForceConversionTest() {
        assertEquals(1.12, (ImperialTonForce per Hour).convert(1.0, UsTonForce per Hour))
    }
}