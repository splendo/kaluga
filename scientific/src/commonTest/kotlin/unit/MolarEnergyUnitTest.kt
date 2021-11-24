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

class MolarEnergyUnitTest {

    @Test
    fun metricMolarEnergyConversionTest() {
        assertEquals(1.0e+7, (Joule per Mole).convert(1.0, Erg per Mole))
    }

    @Test
    fun metricAndImperialMolarEnergyConversionTest() {
        assertEquals(860.42, (WattHour per Mole).convert(1.0, Calorie per Mole, 2))
    }

    @Test
    fun imperialMolarEnergyConversionTest() {
        assertEquals(0.083, (InchPoundForce per Mole).convert(1.0, FootPoundForce per Mole, 3))
    }
}
