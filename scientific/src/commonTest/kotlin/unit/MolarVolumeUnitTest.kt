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
        assertScientificConversion(1.0, (CubicMeter per Mole), 1_000.0, Liter per Mole)
    }

    @Test
    fun molarVolumeFromInvertedMolarityTest() {
        assertEquals(0.5(CubicMeter per Mole), 2(Mole per CubicMeter).molarVolume())
        assertEquals(0.5(CubicFoot per Mole), 2(Mole per CubicFoot).molarVolume())
        assertEquals(0.5(ImperialPint per Mole), 2(Mole per ImperialPint).molarVolume())
        assertEquals(0.5(AcreFoot per Mole), 2(Mole per AcreFoot).molarVolume())
        assertEquals(
            0.5(CubicMeter per Mole),
            2(Mole per CubicMeter).convert((Mole per CubicFoot) as Molarity).molarVolume()
        )
    }

    @Test
    fun molarVolumeFromMolarMassAndDensityTest() {
        assertEquals(1(CubicMeter per Mole), 2(Kilogram per Mole) / 2(Kilogram per CubicMeter))
        assertEquals(1(CubicFoot per Mole), 2(Pound per Mole) / 2(Pound per CubicFoot))
        assertEquals(1(ImperialPint per Mole), 2(Pound per Mole) / 2(Pound per ImperialPint))
        assertEquals(1(AcreFoot per Mole), 2(Pound per Mole) / 2(Pound per AcreFoot))
        assertEquals(
            1(CubicMeter per Mole),
            2(Kilogram per Mole) / 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density)
        )
    }

    @Test
    fun molarVolumeFromSpecificVolumeAndMolalityTest() {
        assertEquals(1(CubicMeter per Mole), 2(CubicMeter per Kilogram) / 2(Mole per Kilogram))
        assertEquals(1(CubicFoot per Mole), 2(CubicFoot per Pound) / 2(Mole per Pound))
        assertEquals(1(ImperialPint per Mole), 2(ImperialPint per Pound) / 2(Mole per Pound))
        assertEqualScientificValue(1(AcreFoot per Mole), 2(AcreFoot per Pound) / 2(Mole per Pound), 8)
        assertEqualScientificValue(
            1(CubicMeter per Mole),
            2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume) / 2(Mole per Kilogram),
            8
        )
    }

    @Test
    fun molarVolumeFromSpecificVolumeAndMolarMassTest() {
        assertEquals(4(CubicMeter per Mole), 2(Kilogram per Mole) * 2(CubicMeter per Kilogram))
        assertEquals(4(CubicMeter per Mole), 2(CubicMeter per Kilogram) * 2(Kilogram per Mole))
        assertEquals(4(CubicFoot per Mole), 2(Pound per Mole) * 2(CubicFoot per Pound))
        assertEquals(4(CubicFoot per Mole), 2(CubicFoot per Pound) * 2(Pound per Mole))
        assertEquals(4(ImperialPint per Mole), 2(Pound per Mole) * 2(ImperialPint per Pound))
        assertEquals(4(ImperialPint per Mole), 2(ImperialPint per Pound) * 2(Pound per Mole))
        assertEquals(4(AcreFoot per Mole), 2(Pound per Mole) * 2(AcreFoot per Pound))
        assertEquals(4(AcreFoot per Mole), 2(AcreFoot per Pound) * 2(Pound per Mole))
        assertEqualScientificValue(
            4(CubicMeter per Mole),
            2(Kilogram per Mole) * 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume),
            8
        )
        assertEqualScientificValue(
            4(CubicMeter per Mole),
            2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume) * 2(Kilogram per Mole),
            8
        )
    }

    @Test
    fun molarVolumeFromVolumeAndAmountOfSubstanceTest() {
        assertEquals(1(CubicMeter per Mole), 2(CubicMeter) / 2(Mole))
        assertEquals(1(CubicFoot per Mole), 2(CubicFoot) / 2(Mole))
        assertEquals(1(ImperialPint per Mole), 2(ImperialPint) / 2(Mole))
        assertEquals(1(AcreFoot per Mole), 2(AcreFoot) / 2(Mole))
        assertEquals(1(CubicMeter per Mole), 2(CubicMeter).convert(CubicFoot as Volume) / 2(Mole))
    }
}