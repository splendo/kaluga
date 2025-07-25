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
import com.splendo.kaluga.scientific.converter.molarMass.molality
import com.splendo.kaluga.scientific.converter.molarity.div
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.specificVolume.div
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class MolalityUnitTest {

    @Test
    fun molalityFromAmountOfSubstanceAndWeightTest() {
        assertEqualScientificValue(1(Decimole per Kilogram), 2(Decimole) / 2(Kilogram))
        assertEqualScientificValue(1(Decimole per Pound), 2(Decimole) / 2(Pound))
        assertEqualScientificValue(1(Decimole per Pound.ukImperial), 2(Decimole) / 2(Pound.ukImperial))
        assertEqualScientificValue(1(Decimole per Pound.usCustomary), 2(Decimole) / 2(Pound.usCustomary))
        assertEqualScientificValue(1(Decimole per Kilogram), 2(Decimole) / 2(Kilogram).convert(Gram as Weight))
    }

    @Test
    fun molalityFromMolarityAndDensityTest() {
        assertEqualScientificValue(1(Decimole per Kilogram), 2(Decimole per CubicMeter) / 2(Kilogram per CubicMeter))
        assertEqualScientificValue(1(Decimole per Pound), 2(Decimole per CubicFoot) / 2(Pound per CubicFoot))
        assertEqualScientificValue(
            1(Decimole per Pound.ukImperial),
            2(Decimole per CubicFoot) / 2(Pound.ukImperial per CubicFoot),
        )
        assertEqualScientificValue(
            1(Decimole per Pound.usCustomary),
            2(Decimole per CubicFoot) / 2(Pound.usCustomary per CubicFoot),
        )
        assertEqualScientificValue(
            1(Decimole per Kilogram),
            2(Decimole per CubicMeter) / 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density),
        )
    }

    @Test
    fun molalityFromMolarityAndSpecificVolumeTest() {
        assertEqualScientificValue(4(Decimole per Kilogram), 2(Decimole per CubicMeter) * 2(CubicMeter per Kilogram))
        assertEqualScientificValue(4(Decimole per Kilogram), 2(CubicMeter per Kilogram) * 2(Decimole per CubicMeter))
        assertEqualScientificValue(4(Decimole per Pound), 2(Decimole per CubicFoot) * 2(CubicFoot per Pound))
        assertEqualScientificValue(4(Decimole per Pound), 2(CubicFoot per Pound) * 2(Decimole per CubicFoot))
        assertEqualScientificValue(
            4(Decimole per Pound.ukImperial),
            2(Decimole per CubicFoot) * 2(CubicFoot per Pound.ukImperial),
        )
        assertEqualScientificValue(
            4(Decimole per Pound.ukImperial),
            2(CubicFoot per Pound.ukImperial) * 2(Decimole per CubicFoot),
        )
        assertEqualScientificValue(
            4(Decimole per Pound.usCustomary),
            2(Decimole per CubicFoot) * 2(CubicFoot per Pound.usCustomary),
        )
        assertEqualScientificValue(
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
        assertEqualScientificValue(0.5(Decimole per Kilogram), 2(Kilogram per Decimole).molality())
        assertEqualScientificValue(0.5(Decimole per Pound), 2(Pound per Decimole).molality())
        assertEqualScientificValue(0.5(Decimole per Pound.ukImperial), 2(Pound.ukImperial per Decimole).molality())
        assertEqualScientificValue(0.5(Decimole per Pound.ukImperial), 2(Pound.ukImperial per Decimole).molality())
        assertEqualScientificValue(
            0.5(Decimole per Kilogram),
            2(Kilogram per Decimole).convert((Pound per Decimole) as MolarMass).molality(),
            8,
        )
    }

    @Test
    fun molalityFromSpecificEnergyAndMolarEnergyTest() {
        assertEqualScientificValue(1(Decimole per Kilogram), 2(Joule per Kilogram) / 2(Joule per Decimole))
        assertEqualScientificValue(1(Decimole per Pound), 2(FootPoundForce per Pound) / 2(FootPoundForce per Decimole))
        assertEqualScientificValue(
            1(Decimole per Pound.ukImperial),
            2(FootPoundForce per Pound.ukImperial) / 2(FootPoundForce per Decimole),
        )
        assertEqualScientificValue(
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
        assertEqualScientificValue(1(Decimole per Kilogram), 2(CubicMeter per Kilogram) / 2(CubicMeter per Decimole))
        assertEqualScientificValue(1(Decimole per Pound), 2(CubicFoot per Pound) / 2(CubicFoot per Decimole))
        assertEqualScientificValue(
            1(Decimole per Pound.ukImperial),
            2(CubicFoot per Pound.ukImperial) / 2(CubicFoot per Decimole),
        )
        assertEqualScientificValue(
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
