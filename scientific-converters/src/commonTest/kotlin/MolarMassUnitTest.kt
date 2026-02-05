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
import com.splendo.kaluga.scientific.converter.density.div
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.molality.molarMass
import com.splendo.kaluga.scientific.converter.molarEnergy.div
import com.splendo.kaluga.scientific.converter.molarVolume.div
import com.splendo.kaluga.scientific.converter.molarVolume.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class MolarMassUnitTest {

    @Test
    fun molarMassFromDensityAndMolarityTest() {
        assertEqualScientificValue(1(Kilogram per Decimole), 2(Kilogram per CubicMeter) / 2(Decimole per CubicMeter))
        assertEqualScientificValue(1(Pound per Decimole), 2(Pound per CubicFoot) / 2(Decimole per CubicFoot), round = 32)
        assertEqualScientificValue(1(ImperialTon per Decimole), 2(ImperialTon per CubicFoot) / 2(Decimole per CubicFoot), round = 32)
        assertEqualScientificValue(1(UsTon per Decimole), 2(UsTon per CubicFoot) / 2(Decimole per CubicFoot), round = 32)
        assertEqualScientificValue(
            1(Kilogram per Decimole),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) / 2(Decimole per CubicMeter),
            round = 30,
        )
    }

    @Test
    fun molarMassFromDensityAndMolarVolumeTest() {
        assertEqualScientificValue(4(Kilogram per Decimole), 2(CubicMeter per Decimole) * 2(Kilogram per CubicMeter))
        assertEqualScientificValue(4(Kilogram per Decimole), 2(Kilogram per CubicMeter) * 2(CubicMeter per Decimole))
        assertEqualScientificValue(4(Pound per Decimole), 2(CubicFoot per Decimole) * 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound per Decimole), 2(Pound per CubicFoot) * 2(CubicFoot per Decimole), round = 32)
        assertEqualScientificValue(4(ImperialTon per Decimole), 2(CubicFoot per Decimole) * 2(ImperialTon per CubicFoot), round = 32)
        assertEqualScientificValue(4(ImperialTon per Decimole), 2(ImperialTon per CubicFoot) * 2(CubicFoot per Decimole), round = 32)
        assertEqualScientificValue(4(UsTon per Decimole), 2(CubicFoot per Decimole) * 2(UsTon per CubicFoot), round = 32)
        assertEqualScientificValue(4(UsTon per Decimole), 2(UsTon per CubicFoot) * 2(CubicFoot per Decimole), round = 32)
        assertEqualScientificValue(
            4(Kilogram per Decimole),
            2(CubicMeter per Decimole) * 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density),
            round = 30,
        )
        assertEqualScientificValue(
            4(Kilogram per Decimole),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) * 2(CubicMeter per Decimole),
            round = 30,
        )
    }

    @Test
    fun molarMassFromInvertedMolalityTest() {
        assertEqualScientificValue(0.5(Kilogram per Decimole), 2(Decimole per Kilogram).molarMass())
        assertEqualScientificValue(0.5(Pound per Decimole), 2(Decimole per Pound).molarMass(), round = 30)
        assertEqualScientificValue(0.5(ImperialTon per Decimole), 2(Decimole per ImperialTon).molarMass(), round = 29)
        assertEqualScientificValue(0.5(UsTon per Decimole), 2(Decimole per UsTon).molarMass(), round = 30)
        assertEqualScientificValue(
            0.5(Kilogram per Decimole),
            2(Decimole per Kilogram).convert((Decimole per Pound) as Molality).molarMass(),
        )
    }

    @Test
    fun molarMassFromMolarEnergyAndSpecificEnergyTest() {
        assertEqualScientificValue(1(Kilogram per Decimole), 2(Joule per Decimole) / 2(Joule per Kilogram))
        assertEqualScientificValue(1(Pound per Decimole), 2(WattHour per Decimole) / 2(WattHour per Pound), round = 30)
        assertEqualScientificValue(
            1(Pound.ukImperial per Decimole),
            2(WattHour per Decimole) / 2(WattHour per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per Decimole),
            2(WattHour per Decimole) / 2(WattHour per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            1(Kilogram per Decimole),
            2(Joule per Decimole) / 2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy),
            round = 32,
        )
    }

    @Test
    fun molarMassFromMolarVolumeAndSpecificVolumeTest() {
        assertEqualScientificValue(1(Kilogram per Decimole), 2(CubicMeter per Decimole) / 2(CubicMeter per Kilogram))
        assertEqualScientificValue(1(Pound per Decimole), 2(CubicFoot per Decimole) / 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(1(ImperialTon per Decimole), 2(CubicFoot per Decimole) / 2(CubicFoot per ImperialTon), round = 30)
        assertEqualScientificValue(1(UsTon per Decimole), 2(CubicFoot per Decimole) / 2(CubicFoot per UsTon), round = 30)
        assertEqualScientificValue(
            1(Kilogram per Decimole),
            2(CubicMeter per Decimole) / 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume),
            round = 32,
        )
    }

    @Test
    fun molarMassFromWeightAndAmountOfSubstanceTest() {
        assertEqualScientificValue(1(Kilogram per Decimole), 2(Kilogram) / 2(Decimole))
        assertEqualScientificValue(1(Pound per Decimole), 2(Pound) / 2(Decimole))
        assertEqualScientificValue(1(ImperialTon per Decimole), 2(ImperialTon) / 2(Decimole))
        assertEqualScientificValue(1(UsTon per Decimole), 2(UsTon) / 2(Decimole))
        assertEqualScientificValue(1(Kilogram per Decimole), 2(Kilogram).convert(Pound as Weight) / 2(Decimole), round = 30)
    }
}
