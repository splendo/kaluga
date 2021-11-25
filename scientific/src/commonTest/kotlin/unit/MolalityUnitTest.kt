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

import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.molarMass.molality
import com.splendo.kaluga.scientific.converter.molarity.div
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.specificVolume.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MolalityUnitTest {

    @Test
    fun molalityConversionTest() {
        assertScientificConversion(1, (Mole per Kilogram), 0.0283495, Mole per Ounce, 7)
    }

    @Test
    fun molalityFromAmountOfSubstanceAndWeightTest() {
        assertEquals(1(Mole per Kilogram), 2(Mole) / 2(Kilogram))
        assertEquals(1(Mole per Pound), 2(Mole) / 2(Pound))
    }

    @Test
    fun molalityFromMolarityAndDensityTest() {
        assertEquals(1(Mole per Kilogram), 2(Mole per CubicMeter) / 2(Kilogram per CubicMeter))
        assertEquals(1(Mole per Pound), 2(Mole per CubicFoot) / 2(Pound per CubicFoot))
    }

    @Test
    fun molalityFromMolarityAndSpecificVolumeTest() {
        assertEquals(4(Mole per Kilogram), 2(Mole per CubicMeter) * 2(CubicMeter per Kilogram))
        assertEquals(4(Mole per Pound), 2(Mole per CubicFoot) * 2(CubicFoot per Pound))
    }

    @Test
    fun molalityFromInvertedMolarMassTest() {
        assertEquals(0.5(Mole per Kilogram), 2(Kilogram per Mole).molality())
    }

    @Test
    fun molalityFromSpecificEnergyAndMolarEnergyTest() {
        assertEquals(1(Mole per Kilogram), 2(Joule per Kilogram) / 2(Joule per Mole))
        assertEquals(1(Mole per Pound), 2(FootPoundForce per Pound) / 2(FootPoundForce per Mole))
    }

    @Test
    fun molalityFromSpecificVolumeAndMolarVolumeTest() {
        assertEquals(1(Mole per Kilogram), 2(CubicMeter per Kilogram) / 2(CubicMeter per Mole))
        assertEquals(1(Mole per Pound), 2(CubicFoot per Pound) / 2(CubicFoot per Mole))
    }
}
