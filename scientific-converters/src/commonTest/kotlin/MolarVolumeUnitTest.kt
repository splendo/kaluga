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
import com.splendo.kaluga.scientific.converter.molarMass.div
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.molarity.molarVolume
import com.splendo.kaluga.scientific.converter.specificVolume.div
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.AcreFoot
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialPint
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class MolarVolumeUnitTest {

    @Test
    fun molarVolumeFromInvertedMolarityTest() {
        assertEqualScientificValue(0.5(CubicMeter per Decimole), 2(Decimole per CubicMeter).molarVolume())
        assertEqualScientificValue(0.5(CubicFoot per Decimole), 2(Decimole per CubicFoot).molarVolume(), round = 32)
        assertEqualScientificValue(0.5(ImperialPint per Decimole), 2(Decimole per ImperialPint).molarVolume(), round = 32)
        assertEqualScientificValue(0.5(AcreFoot per Decimole), 2(Decimole per AcreFoot).molarVolume(), round = 29)
        assertEqualScientificValue(
            0.5(CubicMeter per Decimole),
            2(Decimole per CubicMeter).convert((Decimole per CubicFoot) as Molarity).molarVolume(),
        )
    }

    @Test
    fun molarVolumeFromMolarMassAndDensityTest() {
        assertEqualScientificValue(1(CubicMeter per Decimole), 2(Kilogram per Decimole) / 2(Kilogram per CubicMeter))
        assertEqualScientificValue(1(CubicFoot per Decimole), 2(Pound per Decimole) / 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(1(ImperialPint per Decimole), 2(Pound per Decimole) / 2(Pound per ImperialPint), round = 32)
        assertEqualScientificValue(1(AcreFoot per Decimole), 2(Pound per Decimole) / 2(Pound per AcreFoot), round = 31)
        assertEqualScientificValue(
            1(CubicMeter per Decimole),
            2(Kilogram per Decimole) / 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density),
            round = 30,
        )
    }

    @Test
    fun molarVolumeFromSpecificVolumeAndMolalityTest() {
        assertEqualScientificValue(1(CubicMeter per Decimole), 2(CubicMeter per Kilogram) / 2(Decimole per Kilogram))
        assertEqualScientificValue(1(CubicFoot per Decimole), 2(CubicFoot per Pound) / 2(Decimole per Pound), round = 32)
        assertEqualScientificValue(1(ImperialPint per Decimole), 2(ImperialPint per Pound) / 2(Decimole per Pound))
        assertEqualScientificValue(1(AcreFoot per Decimole), 2(AcreFoot per Pound) / 2(Decimole per Pound), round = 32)
        assertEqualScientificValue(
            1(CubicMeter per Decimole),
            2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume) / 2(Decimole per Kilogram),
            round = 32,
        )
    }

    @Test
    fun molarVolumeFromSpecificVolumeAndMolarMassTest() {
        assertEqualScientificValue(4(CubicMeter per Decimole), 2(Kilogram per Decimole) * 2(CubicMeter per Kilogram))
        assertEqualScientificValue(4(CubicMeter per Decimole), 2(CubicMeter per Kilogram) * 2(Kilogram per Decimole))
        assertEqualScientificValue(4(CubicFoot per Decimole), 2(Pound per Decimole) * 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(4(CubicFoot per Decimole), 2(CubicFoot per Pound) * 2(Pound per Decimole), round = 30)
        assertEqualScientificValue(4(ImperialPint per Decimole), 2(Pound per Decimole) * 2(ImperialPint per Pound), round = 30)
        assertEqualScientificValue(4(ImperialPint per Decimole), 2(ImperialPint per Pound) * 2(Pound per Decimole), round = 30)
        assertEqualScientificValue(4(AcreFoot per Decimole), 2(Pound per Decimole) * 2(AcreFoot per Pound), round = 30)
        assertEqualScientificValue(4(AcreFoot per Decimole), 2(AcreFoot per Pound) * 2(Pound per Decimole), round = 30)
        assertEqualScientificValue(
            4(CubicMeter per Decimole),
            2(Kilogram per Decimole) * 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume),
            round = 32,
        )
        assertEqualScientificValue(
            4(CubicMeter per Decimole),
            2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume) * 2(Kilogram per Decimole),
            round = 32,
        )
    }

    @Test
    fun molarVolumeFromVolumeAndAmountOfSubstanceTest() {
        assertEqualScientificValue(1(CubicMeter per Decimole), 2(CubicMeter) / 2(Decimole))
        assertEqualScientificValue(1(CubicFoot per Decimole), 2(CubicFoot) / 2(Decimole))
        assertEqualScientificValue(1(ImperialPint per Decimole), 2(ImperialPint) / 2(Decimole))
        assertEqualScientificValue(1(AcreFoot per Decimole), 2(AcreFoot) / 2(Decimole))
        assertEqualScientificValue(1(CubicMeter per Decimole), 2(CubicMeter).convert(CubicFoot as Volume) / 2(Decimole), round = 32)
    }
}
