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
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.density.div
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.molality.div
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.converter.molarVolume.molarity
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MolarityUnitTest {

    @Test
    fun molarityConversionTest() {
        assertScientificConversion(1, (Mole per CubicMeter), 1.63871e-5, Mole per CubicInch, 10)
    }

    @Test
    fun molarityFromAmountOfSubstanceAndVolumeTest() {
        assertEquals(1(Mole per CubicMeter), 2(Mole) / 2(CubicMeter))
        assertEquals(1(Mole per CubicFoot), 2(Mole) / 2(CubicFoot))
        assertEquals(1(Mole per CubicFoot.ukImperial), 2(Mole) / 2(CubicFoot.ukImperial))
        assertEquals(1(Mole per CubicFoot.usCustomary), 2(Mole) / 2(CubicFoot.usCustomary))
        assertEquals(1(Mole per CubicMeter), 2(Mole) / 2(CubicMeter).convert(CubicFoot as Volume))
    }

    @Test
    fun molarityFromDensityAndMolalityTest() {
        assertEquals(4(Mole per CubicMeter), 2(Mole per Kilogram) * 2(Kilogram per CubicMeter))
        assertEquals(4(Mole per CubicMeter), 2(Kilogram per CubicMeter) * 2(Mole per Kilogram))
        assertEquals(4(Mole per CubicFoot), 2(Mole per Pound) * 2(Pound per CubicFoot))
        assertEquals(4(Mole per CubicFoot), 2(Pound per CubicFoot) * 2(Mole per Pound))
        assertEquals(
            4(Mole per CubicFoot.ukImperial),
            2(Mole per Pound) * 2(Pound.ukImperial per CubicFoot)
        )
        assertEquals(
            4(Mole per CubicFoot.ukImperial),
            2(Pound.ukImperial per CubicFoot) * 2(Mole per Pound)
        )
        assertEquals(
            4(Mole per CubicFoot.usCustomary),
            2(Mole per Pound) * 2(Pound.usCustomary per CubicFoot)
        )
        assertEquals(
            4(Mole per CubicFoot.usCustomary),
            2(Pound.usCustomary per CubicFoot) * 2(Mole per Pound)
        )
        assertEquals(
            4(Mole per CubicMeter),
            2(Mole per Kilogram) * 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density)
        )
        assertEquals(
            4(Mole per CubicMeter),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) * 2(Mole per Kilogram)
        )
    }

    @Test
    fun molarityFromDensityAndMolarMassTest() {
        assertEquals(1(Mole per CubicMeter), 2(Kilogram per CubicMeter) / 2(Kilogram per Mole))
        assertEquals(1(Mole per CubicFoot), 2(Pound per CubicFoot) / 2(Pound per Mole))
        assertEquals(
            1(Mole per CubicFoot.ukImperial),
            2(Pound.ukImperial per CubicFoot) / 2(Pound per Mole)
        )
        assertEquals(
            1(Mole per CubicFoot.usCustomary),
            2(Pound.usCustomary per CubicFoot) / 2(Pound per Mole)
        )
        assertEquals(
            1(Mole per CubicMeter),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) / 2(Kilogram per Mole)
        )
    }

    @Test
    fun molarityFromMolalityAndSpecificVolumeTest() {
        assertEquals(1(Mole per CubicMeter), 2(Mole per Kilogram) / 2(CubicMeter per Kilogram))
        assertEquals(1(Mole per CubicFoot), 2(Mole per Pound) / 2(CubicFoot per Pound))
        assertEquals(
            1(Mole per CubicFoot.ukImperial),
            2(Mole per Pound) / 2(CubicFoot per Pound.ukImperial)
        )
        assertEquals(
            1(Mole per CubicFoot.usCustomary),
            2(Mole per Pound) / 2(CubicFoot per Pound.usCustomary)
        )
        assertEquals(
            1(Mole per CubicMeter),
            2(Mole per Kilogram) / 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume)
        )
    }

    @Test
    fun molarityFromInvertedMolarVolumeTest() {
        assertEquals(0.5(Mole per CubicMeter), 2(CubicMeter per Mole).molarity())
        assertEquals(0.5(Mole per CubicFoot), 2(CubicFoot per Mole).molarity())
        assertEquals(
            0.5(Mole per CubicFoot.ukImperial),
            2(CubicFoot.ukImperial per Mole).molarity()
        )
        assertEquals(
            0.5(Mole per CubicFoot.usCustomary),
            2(CubicFoot.usCustomary per Mole).molarity()
        )
        assertEquals(
            0.5(Mole per CubicMeter),
            2(CubicMeter per Mole).convert((CubicFoot per Mole) as MolarVolume).molarity()
        )
    }
}
