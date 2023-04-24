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
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.molarMass.molality
import com.splendo.kaluga.scientific.converter.molarity.div
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.specificVolume.div
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MolalityUnitTest {

    @Test
    fun molalityConversionTest() {
        assertScientificConversion(1, (Mole per Kilogram), 0.283495, Decimole per Ounce, 6)
    }

    @Test
    fun molalityFromAmountOfSubstanceAndWeightTest() {
        assertEquals(1(Decimole per Kilogram), 2(Decimole) / 2(Kilogram))
        assertEquals(1(Decimole per Pound), 2(Decimole) / 2(Pound))
        assertEquals(1(Decimole per Pound.ukImperial), 2(Decimole) / 2(Pound.ukImperial))
        assertEquals(1(Decimole per Pound.usCustomary), 2(Decimole) / 2(Pound.usCustomary))
        assertEquals(1(Decimole per Kilogram), 2(Decimole) / 2(Kilogram).convert(Gram as Weight))
    }

    @Test
    fun molalityFromMolarityAndDensityTest() {
        assertEquals(1(Decimole per Kilogram), 2(Decimole per CubicMeter) / 2(Kilogram per CubicMeter))
        assertEquals(1(Decimole per Pound), 2(Decimole per CubicFoot) / 2(Pound per CubicFoot))
        assertEquals(
            1(Decimole per Pound.ukImperial),
            2(Decimole per CubicFoot) / 2(Pound.ukImperial per CubicFoot),
        )
        assertEquals(
            1(Decimole per Pound.usCustomary),
            2(Decimole per CubicFoot) / 2(Pound.usCustomary per CubicFoot),
        )
        assertEquals(
            1(Decimole per Kilogram),
            2(Decimole per CubicMeter) / 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density),
        )
    }

    @Test
    fun molalityFromMolarityAndSpecificVolumeTest() {
        assertEquals(4(Decimole per Kilogram), 2(Decimole per CubicMeter) * 2(CubicMeter per Kilogram))
        assertEquals(4(Decimole per Kilogram), 2(CubicMeter per Kilogram) * 2(Decimole per CubicMeter))
        assertEquals(4(Decimole per Pound), 2(Decimole per CubicFoot) * 2(CubicFoot per Pound))
        assertEquals(4(Decimole per Pound), 2(CubicFoot per Pound) * 2(Decimole per CubicFoot))
        assertEquals(
            4(Decimole per Pound.ukImperial),
            2(Decimole per CubicFoot) * 2(CubicFoot per Pound.ukImperial),
        )
        assertEquals(
            4(Decimole per Pound.ukImperial),
            2(CubicFoot per Pound.ukImperial) * 2(Decimole per CubicFoot),
        )
        assertEquals(
            4(Decimole per Pound.usCustomary),
            2(Decimole per CubicFoot) * 2(CubicFoot per Pound.usCustomary),
        )
        assertEquals(
            4(Decimole per Pound.usCustomary),
            2(CubicFoot per Pound.usCustomary) * 2(Decimole per CubicFoot),
        )
        assertEqualScientificValue(
            4(Decimole per Kilogram),
            2(Decimole per CubicMeter) * 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume),
            8,
        )
        assertEqualScientificValue(
            4(Decimole per Kilogram),
            2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume) * 2(Decimole per CubicMeter),
            8,
        )
    }

    @Test
    fun molalityFromInvertedMolarMassTest() {
        assertEquals(0.5(Decimole per Kilogram), 2(Kilogram per Decimole).molality())
        assertEquals(0.5(Decimole per Pound), 2(Pound per Decimole).molality())
        assertEquals(0.5(Decimole per Pound.ukImperial), 2(Pound.ukImperial per Decimole).molality())
        assertEquals(0.5(Decimole per Pound.ukImperial), 2(Pound.ukImperial per Decimole).molality())
        assertEqualScientificValue(
            0.5(Decimole per Kilogram),
            2(Kilogram per Decimole).convert((Pound per Decimole) as MolarMass).molality(),
            8,
        )
    }

    @Test
    fun molalityFromSpecificEnergyAndMolarEnergyTest() {
        assertEquals(1(Decimole per Kilogram), 2(Joule per Kilogram) / 2(Joule per Decimole))
        assertEquals(1(Decimole per Pound), 2(FootPoundForce per Pound) / 2(FootPoundForce per Decimole))
        assertEquals(
            1(Decimole per Pound.ukImperial),
            2(FootPoundForce per Pound.ukImperial) / 2(FootPoundForce per Decimole),
        )
        assertEquals(
            1(Decimole per Pound.usCustomary),
            2(FootPoundForce per Pound.usCustomary) / 2(FootPoundForce per Decimole),
        )
        assertEqualScientificValue(
            1(Decimole per Kilogram),
            2(Joule per Kilogram).convert((FootPoundForce per Pound) as SpecificEnergy) / 2(Joule per Decimole),
            8,
        )
    }

    @Test
    fun molalityFromSpecificVolumeAndMolarVolumeTest() {
        assertEquals(1(Decimole per Kilogram), 2(CubicMeter per Kilogram) / 2(CubicMeter per Decimole))
        assertEquals(1(Decimole per Pound), 2(CubicFoot per Pound) / 2(CubicFoot per Decimole))
        assertEquals(
            1(Decimole per Pound.ukImperial),
            2(CubicFoot per Pound.ukImperial) / 2(CubicFoot per Decimole),
        )
        assertEquals(
            1(Decimole per Pound.usCustomary),
            2(CubicFoot per Pound.usCustomary) / 2(CubicFoot per Decimole),
        )
        assertEqualScientificValue(
            1(Decimole per Kilogram),
            2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume) / 2(
                CubicMeter per Decimole,
            ),
            8,
        )
    }
}
