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

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MolarEnergyUnitTest {

    @Test
    fun metricMolarEnergyConversionTest() {
        assertScientificConversion(1.0, (Joule per Mole), 1.0e+7, Erg per Mole)
    }

    @Test
    fun molarEnergyFromEnergyAndAmountOfSubstanceTest() {
        assertEquals(1(Joule per Mole), 2(Joule) / 2(Mole))
        assertEquals(1(Calorie per Mole), 2(Calorie) / 2(Mole))
        assertEquals(1(HorsepowerHour per Mole), 2(HorsepowerHour) / 2(Mole))
    }

    @Test
    fun molarEnergyFromSpecificEnergyAndMolalityTest() {
        assertEquals(1(Joule per Mole), 2(Joule per Kilogram) / 2(Mole per Kilogram))
        assertEquals(1(WattHour.imperial per Mole), 2(WattHour per Pound) / 2(Mole per Pound))
        assertEquals(
            1(WattHour.imperial per Mole),
            2(WattHour per Pound.ukImperial) / 2(Mole per Pound)
        )
        assertEquals(
            1(WattHour.imperial per Mole),
            2(WattHour per Pound.usCustomary) / 2(Mole per Pound)
        )
        assertEquals(
            1(Joule per Mole),
            2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy) / 2(Mole per Kilogram)
        )
    }

    @Test
    fun molarEnergyFromSpecificEnergyAndMolarMassTest() {
        assertEquals(4(Joule per Mole), 2(Joule per Kilogram) * 2(Kilogram per Mole))
        assertEquals(4(Joule per Mole), 2(Kilogram per Mole) * 2(Joule per Kilogram))
        assertEquals(4(WattHour.imperial per Mole), 2(WattHour per Pound) * 2(Pound per Mole))
        assertEquals(4(WattHour.imperial per Mole), 2(Pound per Mole) * 2(WattHour per Pound))
        assertEquals(
            4(WattHour.imperial per Mole),
            2(WattHour per Pound.ukImperial) * 2(Pound per Mole)
        )
        assertEquals(
            4(WattHour.imperial per Mole),
            2(Pound per Mole) * 2(WattHour per Pound.ukImperial)
        )
        assertEquals(
            4(WattHour.imperial per Mole),
            2(WattHour per Pound.usCustomary) * 2(Pound per Mole)
        )
        assertEquals(
            4(WattHour.imperial per Mole),
            2(Pound per Mole) * 2(WattHour per Pound.usCustomary)
        )
        assertEquals(
            4(Joule per Mole),
            2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy) * 2(Kilogram per Mole)
        )
        assertEquals(
            4(Joule per Mole),
            2(Kilogram per Mole) * 2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy)
        )
    }
}
