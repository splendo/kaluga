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
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.density.div
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.molality.div
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.converter.molarVolume.molarity
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class MolarityUnitTest {

    @Test
    fun molarityFromAmountOfSubstanceAndVolumeTest() {
        assertEqualScientificValue(1(Decimole per CubicMeter), 2(Decimole) / 2(CubicMeter))
        assertEqualScientificValue(1(Decimole per CubicFoot), 2(Decimole) / 2(CubicFoot), round = 27)
        assertEqualScientificValue(1(Decimole per CubicFoot.ukImperial), 2(Decimole) / 2(CubicFoot.ukImperial), round = 27)
        assertEqualScientificValue(1(Decimole per CubicFoot.usCustomary), 2(Decimole) / 2(CubicFoot.usCustomary), round = 27)
        assertEqualScientificValue(1(Decimole per CubicMeter), 2(Decimole) / 2(CubicMeter).convert(CubicFoot as Volume), round = 32)
    }

    @Test
    fun molarityFromDensityAndMolalityTest() {
        assertEqualScientificValue(4(Decimole per CubicMeter), 2(Decimole per Kilogram) * 2(Kilogram per CubicMeter))
        assertEqualScientificValue(4(Decimole per CubicMeter), 2(Kilogram per CubicMeter) * 2(Decimole per Kilogram))
        assertEqualScientificValue(4(Decimole per CubicFoot), 2(Decimole per Pound) * 2(Pound per CubicFoot), round = 30)
        assertEqualScientificValue(4(Decimole per CubicFoot), 2(Pound per CubicFoot) * 2(Decimole per Pound), round = 30)
        assertEqualScientificValue(
            4(Decimole per CubicFoot.ukImperial),
            2(Decimole per Pound) * 2(Pound.ukImperial per CubicFoot),
            round = 30,
        )
        assertEqualScientificValue(
            4(Decimole per CubicFoot.ukImperial),
            2(Pound.ukImperial per CubicFoot) * 2(Decimole per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(Decimole per CubicFoot.usCustomary),
            2(Decimole per Pound) * 2(Pound.usCustomary per CubicFoot),
            round = 30,
        )
        assertEqualScientificValue(
            4(Decimole per CubicFoot.usCustomary),
            2(Pound.usCustomary per CubicFoot) * 2(Decimole per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(Decimole per CubicMeter),
            2(Decimole per Kilogram) * 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density),
            round = 30,
        )
        assertEqualScientificValue(
            4(Decimole per CubicMeter),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) * 2(Decimole per Kilogram),
            round = 30,
        )
    }

    @Test
    fun molarityFromDensityAndMolarMassTest() {
        assertEqualScientificValue(1(Decimole per CubicMeter), 2(Kilogram per CubicMeter) / 2(Kilogram per Decimole))
        assertEqualScientificValue(1(Decimole per CubicFoot), 2(Pound per CubicFoot) / 2(Pound per Decimole), round = 32)
        assertEqualScientificValue(
            1(Decimole per CubicFoot.ukImperial),
            2(Pound.ukImperial per CubicFoot) / 2(Pound per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            1(Decimole per CubicFoot.usCustomary),
            2(Pound.usCustomary per CubicFoot) / 2(Pound per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            1(Decimole per CubicMeter),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) / 2(Kilogram per Decimole),
            round = 30,
        )
    }

    @Test
    fun molarityFromMolalityAndSpecificVolumeTest() {
        assertEqualScientificValue(1(Decimole per CubicMeter), 2(Decimole per Kilogram) / 2(CubicMeter per Kilogram))
        assertEqualScientificValue(1(Decimole per CubicFoot), 2(Decimole per Pound) / 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(
            1(Decimole per CubicFoot.ukImperial),
            2(Decimole per Pound) / 2(CubicFoot per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            1(Decimole per CubicFoot.usCustomary),
            2(Decimole per Pound) / 2(CubicFoot per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            1(Decimole per CubicMeter),
            2(Decimole per Kilogram) / 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume),
            round = 32,
        )
    }

    @Test
    fun molarityFromInvertedMolarVolumeTest() {
        assertEqualScientificValue(0.5(Decimole per CubicMeter), 2(CubicMeter per Decimole).molarity())
        assertEqualScientificValue(0.5(Decimole per CubicFoot), 2(CubicFoot per Decimole).molarity(), round = 27)
        assertEqualScientificValue(
            0.5(Decimole per CubicFoot.ukImperial),
            2(CubicFoot.ukImperial per Decimole).molarity(),
            round = 27,
        )
        assertEqualScientificValue(
            0.5(Decimole per CubicFoot.usCustomary),
            2(CubicFoot.usCustomary per Decimole).molarity(),
            round = 27,
        )
        assertEqualScientificValue(
            0.5(Decimole per CubicMeter),
            2(CubicMeter per Decimole).convert((CubicFoot per Decimole) as MolarVolume).molarity(),
            round = 32,
        )
    }
}
