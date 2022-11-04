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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.molarMass.div
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.molarity.molarVolume
import com.splendo.kaluga.scientific.converter.specificVolume.div
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MolarVolumeUnitTest {

    @Test
    fun momentumConversionTest() {
        assertScientificConversion(1.0, (CubicMeter per Mole), 100.0, Liter per Decimole)
    }

    @Test
    fun molarVolumeFromInvertedMolarityTest() {
        assertEquals(0.5(CubicMeter per Decimole), 2(Decimole per CubicMeter).molarVolume())
        assertEquals(0.5(CubicFoot per Decimole), 2(Decimole per CubicFoot).molarVolume())
        assertEquals(0.5(ImperialPint per Decimole), 2(Decimole per ImperialPint).molarVolume())
        assertEquals(0.5(AcreFoot per Decimole), 2(Decimole per AcreFoot).molarVolume())
        assertEquals(
            0.5(CubicMeter per Decimole),
            2(Decimole per CubicMeter).convert((Decimole per CubicFoot) as Molarity).molarVolume()
        )
    }

    @Test
    fun molarVolumeFromMolarMassAndDensityTest() {
        assertEquals(1(CubicMeter per Decimole), 2(Kilogram per Decimole) / 2(Kilogram per CubicMeter))
        assertEquals(1(CubicFoot per Decimole), 2(Pound per Decimole) / 2(Pound per CubicFoot))
        assertEquals(1(ImperialPint per Decimole), 2(Pound per Decimole) / 2(Pound per ImperialPint))
        assertEquals(1(AcreFoot per Decimole), 2(Pound per Decimole) / 2(Pound per AcreFoot))
        assertEquals(
            1(CubicMeter per Decimole),
            2(Kilogram per Decimole) / 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density)
        )
    }

    @Test
    fun molarVolumeFromSpecificVolumeAndMolalityTest() {
        assertEquals(1(CubicMeter per Decimole), 2(CubicMeter per Kilogram) / 2(Decimole per Kilogram))
        assertEquals(1(CubicFoot per Decimole), 2(CubicFoot per Pound) / 2(Decimole per Pound))
        assertEquals(1(ImperialPint per Decimole), 2(ImperialPint per Pound) / 2(Decimole per Pound))
        assertEqualScientificValue(1(AcreFoot per Decimole), 2(AcreFoot per Pound) / 2(Decimole per Pound), 8)
        assertEqualScientificValue(
            1(CubicMeter per Decimole),
            2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume) / 2(Decimole per Kilogram),
            8
        )
    }

    @Test
    fun molarVolumeFromSpecificVolumeAndMolarMassTest() {
        assertEquals(4(CubicMeter per Decimole), 2(Kilogram per Decimole) * 2(CubicMeter per Kilogram))
        assertEquals(4(CubicMeter per Decimole), 2(CubicMeter per Kilogram) * 2(Kilogram per Decimole))
        assertEquals(4(CubicFoot per Decimole), 2(Pound per Decimole) * 2(CubicFoot per Pound))
        assertEquals(4(CubicFoot per Decimole), 2(CubicFoot per Pound) * 2(Pound per Decimole))
        assertEquals(4(ImperialPint per Decimole), 2(Pound per Decimole) * 2(ImperialPint per Pound))
        assertEquals(4(ImperialPint per Decimole), 2(ImperialPint per Pound) * 2(Pound per Decimole))
        assertEquals(4(AcreFoot per Decimole), 2(Pound per Decimole) * 2(AcreFoot per Pound))
        assertEquals(4(AcreFoot per Decimole), 2(AcreFoot per Pound) * 2(Pound per Decimole))
        assertEqualScientificValue(
            4(CubicMeter per Decimole),
            2(Kilogram per Decimole) * 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume),
            8
        )
        assertEqualScientificValue(
            4(CubicMeter per Decimole),
            2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume) * 2(Kilogram per Decimole),
            8
        )
    }

    @Test
    fun molarVolumeFromVolumeAndAmountOfSubstanceTest() {
        assertEquals(1(CubicMeter per Decimole), 2(CubicMeter) / 2(Decimole))
        assertEquals(1(CubicFoot per Decimole), 2(CubicFoot) / 2(Decimole))
        assertEquals(1(ImperialPint per Decimole), 2(ImperialPint) / 2(Decimole))
        assertEquals(1(AcreFoot per Decimole), 2(AcreFoot) / 2(Decimole))
        assertEquals(1(CubicMeter per Decimole), 2(CubicMeter).convert(CubicFoot as Volume) / 2(Decimole))
    }
}
