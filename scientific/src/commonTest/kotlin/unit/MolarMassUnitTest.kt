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

class MolarMassUnitTest {

    @Test
    fun metricMolarMassConversionTest() {
        assertEquals(0.001, (Kilogram per Mole).convert(1.0, Tonne per Mole))
    }

    @Test
    fun imperialMolarMassConversionTest() {
        assertEquals(16.0, (Pound per Mole).convert(1.0, Ounce per Mole))
    }

    @Test
    fun ukImperialMolarMassConversionTest() {
        assertEquals(2240.0, (ImperialTon per Mole).convert(1.0, Pound per Mole))
    }

    @Test
    fun usCustomaryMolarMassConversionTest() {
        assertEquals(2000.0, (UsTon per Mole).convert(1.0, Pound per Mole))
    }
}
