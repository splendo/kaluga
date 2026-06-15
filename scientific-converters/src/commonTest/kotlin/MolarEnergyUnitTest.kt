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

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Calorie
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.imperial
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class MolarEnergyUnitTest {

    @Test
    fun molarEnergyFromEnergyAndAmountOfSubstanceTest() {
        assertEqualScientificValue(1(Joule per Decimole), 2(Joule) / 2(Decimole))
        assertEqualScientificValue(1(Calorie per Decimole), 2(Calorie) / 2(Decimole))
        assertEqualScientificValue(1(HorsepowerHour per Decimole), 2(HorsepowerHour) / 2(Decimole))
    }

    @Test
    fun molarEnergyFromSpecificEnergyAndMolalityTest() {
        assertEqualScientificValue(1(Joule per Decimole), 2(Joule per Kilogram) / 2(Decimole per Kilogram))
        assertEqualScientificValue(1(WattHour.imperial per Decimole), 2(WattHour per Pound) / 2(Decimole per Pound))
        assertEqualScientificValue(
            1(WattHour.imperial per Decimole),
            2(WattHour per Pound.ukImperial) / 2(Decimole per Pound),
        )
        assertEqualScientificValue(
            1(WattHour.imperial per Decimole),
            2(WattHour per Pound.usCustomary) / 2(Decimole per Pound),
        )
        assertEqualScientificValue(
            1(Joule per Decimole),
            2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy) / 2(Decimole per Kilogram),
            round = 32,
        )
    }

    @Test
    fun molarEnergyFromSpecificEnergyAndMolarMassTest() {
        assertEqualScientificValue(4(Joule per Decimole), 2(Joule per Kilogram) * 2(Kilogram per Decimole))
        assertEqualScientificValue(4(Joule per Decimole), 2(Kilogram per Decimole) * 2(Joule per Kilogram))
        assertEqualScientificValue(4(WattHour.imperial per Decimole), 2(WattHour per Pound) * 2(Pound per Decimole), round = 30)
        assertEqualScientificValue(4(WattHour.imperial per Decimole), 2(Pound per Decimole) * 2(WattHour per Pound), round = 30)
        assertEqualScientificValue(
            4(WattHour.imperial per Decimole),
            2(WattHour per Pound.ukImperial) * 2(Pound per Decimole),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial per Decimole),
            2(Pound per Decimole) * 2(WattHour per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial per Decimole),
            2(WattHour per Pound.usCustomary) * 2(Pound per Decimole),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial per Decimole),
            2(Pound per Decimole) * 2(WattHour per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            4(Joule per Decimole),
            2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy) * 2(Kilogram per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            4(Joule per Decimole),
            2(Kilogram per Decimole) * 2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy),
            round = 32,
        )
    }
}
