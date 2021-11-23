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
import com.splendo.kaluga.scientific.converter.catalysticActivity.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class AmountOfSubstanceUnitTest {

    @Test
    fun amountOfSubstanceConversionTest() {
        assertEquals(1e+9, Mole.convert(1, Nanomole))
        assertEquals(1000000.0, Mole.convert(1, Micromole))
        assertEquals(1000.0, Mole.convert(1, Millimole))
        assertEquals(100.0, Mole.convert(1, Centimole))
        assertEquals(10.0, Mole.convert(1, Decimole))
        assertEquals(0.1, Mole.convert(1, Decamole))
        assertEquals(0.01, Mole.convert(1, Hectomole))
        assertEquals(0.001, Mole.convert(1, Kilomole))
        assertEquals(1e-6, Mole.convert(1, Megamole))
        assertEquals(1e-9, Mole.convert(1, Gigamole))
    }

    @Test
    fun amountOfSubstanceFromCatalysisAndTimeTest() {
        assertEquals(240(Mole), 2(Katal) * 2(Minute))
    }

    @Test
    fun amountOfSubstanceFromEnergyAndMolarEnergyTest() {
        assertEqualScientificValue(1(Mole), (2(WattHour) / 2(WattHour per Mole)))
    }

    @Test
    fun amountOfSubstanceFromMolalityAndWeightTest() {
        assertEqualScientificValue(4(Mole), 2(Mole per Kilogram) * 2(Kilogram))
    }

    @Test
    fun amountOfSubstanceFromMolarityAndVolumeTest() {
        assertEqualScientificValue(4(Mole), 2(Mole per CubicMeter) * 2(CubicMeter))
    }

    @Test
    fun amountOfSubstanceFromVolumeAndMolarVolumeTest() {
        assertEqualScientificValue(1(Mole), (2(CubicMeter) / 2(CubicMeter per Mole)))
    }

    @Test
    fun amountOfSubstanceFromWeightAndMolarMassTest() {
        assertEqualScientificValue(1(Mole), 2(Kilogram) / 2(Kilogram per Mole))
    }
}