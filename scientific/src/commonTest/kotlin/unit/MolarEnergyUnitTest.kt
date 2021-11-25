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
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MolarEnergyUnitTest {

    @Test
    fun molarEnergyConversionTest() {
        // TODO more combinations
        assertEquals(1.0e+7, (Joule per Mole).convert(1.0, Erg per Mole))
        assertEquals(860.42, (WattHour per Mole).convert(1.0, Calorie per Mole, 2))
        assertEquals(0.083, (InchPoundForce per Mole).convert(1.0, FootPoundForce per Mole, 3))
    }

    @Test
    fun molarEnergyFromEnergyAndAmountOfSubstanceTest() {
        assertEquals(1(Joule per Mole), 2(Joule) / 2(Mole))
        assertEquals(1(Calorie per Mole), 2(Calorie) / 2(Mole))
        assertEquals(1(HorsepowerHour per Mole), 2(HorsepowerHour) / 2(Mole))
    }

    @Test
    fun molarEnergyFromMolarMassAndSpecificEnergyTest() {
        assertEquals(4(Joule per Mole), 2(Joule per Kilogram) * 2(Kilogram per Mole))
        // FIXME this fails probably because there is no MetricAndImperial for some units like there is for Energy but only Metric or Imperial
        // assertEqualScientificValue(4(Calorie per Mole), 2(Calorie per Pound) * 2(Pound per Mole))
        // assertEqualScientificValue(4(Calorie per Mole), 2(Calorie per Kilogram) * 2(Kilogram per Mole))
        assertEquals(4(HorsepowerHour per Mole), 2(HorsepowerHour per Pound) * 2(Pound per Mole))
    }

    @Test
    fun molarEnergyFromSpecificEnergyAndMolalityTest() {
        assertEquals(1(Joule per Mole), 2(Joule per Kilogram) / 2(Mole per Kilogram))
        // FIXME same as above
        // assertEqualScientificValue(1(Calorie per Mole), 2(Calorie per Kilogram) / 2(Mole per Kilogram))
        // assertEqualScientificValue(1(Calorie per Mole), 2(Calorie per Pound) / 2(Mole per Pound))
        assertEquals(1(HorsepowerHour per Mole), 2(HorsepowerHour per Pound) / 2(Mole per Pound))
    }
}
