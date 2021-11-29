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
import com.splendo.kaluga.scientific.converter.density.div
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.molality.molarMass
import com.splendo.kaluga.scientific.converter.molarEnergy.div
import com.splendo.kaluga.scientific.converter.molarVolume.div
import com.splendo.kaluga.scientific.converter.molarVolume.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MolarMassUnitTest {

    @Test
    fun metricMolarMassConversionTest() {
        assertScientificConversion(1.0, (Kilogram per Mole), 0.001, Tonne per Mole)
    }

    @Test
    fun molarMassFromDensityAndMolarityTest() {
        assertEquals(1(Kilogram per Mole), 2(Kilogram per CubicMeter) / 2(Mole per CubicMeter))
        assertEquals(1(Pound per Mole), 2(Pound per CubicFoot) / 2(Mole per CubicFoot))
        assertEquals(1(ImperialTon per Mole), 2(ImperialTon per CubicFoot) / 2(Mole per CubicFoot))
        assertEquals(1(UsTon per Mole), 2(UsTon per CubicFoot) / 2(Mole per CubicFoot))
        assertEquals(
            1(Kilogram per Mole),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) / 2(Mole per CubicMeter)
        )
    }

    @Test
    fun molarMassFromDensityAndMolarVolumeTest() {
        assertEquals(4(Kilogram per Mole), 2(CubicMeter per Mole) * 2(Kilogram per CubicMeter))
        assertEquals(4(Kilogram per Mole), 2(Kilogram per CubicMeter) * 2(CubicMeter per Mole))
        assertEquals(4(Pound per Mole), 2(CubicFoot per Mole) * 2(Pound per CubicFoot))
        assertEquals(4(Pound per Mole), 2(Pound per CubicFoot) * 2(CubicFoot per Mole))
        assertEquals(4(ImperialTon per Mole), 2(CubicFoot per Mole) * 2(ImperialTon per CubicFoot))
        assertEquals(4(ImperialTon per Mole), 2(ImperialTon per CubicFoot) * 2(CubicFoot per Mole))
        assertEquals(4(UsTon per Mole), 2(CubicFoot per Mole) * 2(UsTon per CubicFoot))
        assertEquals(4(UsTon per Mole), 2(UsTon per CubicFoot) * 2(CubicFoot per Mole))
        assertEquals(
            4(Kilogram per Mole),
            2(CubicMeter per Mole) * 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density)
        )
        assertEquals(
            4(Kilogram per Mole),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) * 2(CubicMeter per Mole)
        )
    }

    @Test
    fun molarMassFromInvertedMolalityTest() {
        assertEquals(0.5(Kilogram per Mole), 2(Mole per Kilogram).molarMass())
        assertEquals(0.5(Pound per Mole), 2(Mole per Pound).molarMass())
        assertEquals(0.5(ImperialTon per Mole), 2(Mole per ImperialTon).molarMass())
        assertEquals(0.5(UsTon per Mole), 2(Mole per UsTon).molarMass())
        assertEquals(
            0.5(Kilogram per Mole),
            2(Mole per Kilogram).convert((Mole per Pound) as Molality).molarMass()
        )
    }

    @Test
    fun molarMassFromMolarEnergyAndSpecificEnergyTest() {
        assertEquals(1(Kilogram per Mole), 2(Joule per Mole) / 2(Joule per Kilogram))
        assertEquals(1(Pound per Mole), 2(WattHour per Mole) / 2(WattHour per Pound))
        assertEquals(
            1(Pound.ukImperial per Mole),
            2(WattHour per Mole) / 2(WattHour per Pound.ukImperial)
        )
        assertEquals(
            1(Pound.usCustomary per Mole),
            2(WattHour per Mole) / 2(WattHour per Pound.usCustomary)
        )
        assertEquals(
            1(Kilogram per Mole),
            2(Joule per Mole) / 2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy)
        )
    }

    @Test
    fun molarMassFromMolarVolumeAndSpecificVolumeTest() {
        assertEquals(1(Kilogram per Mole), 2(CubicMeter per Mole) / 2(CubicMeter per Kilogram))
        assertEquals(1(Pound per Mole), 2(CubicFoot per Mole) / 2(CubicFoot per Pound))
        assertEquals(1(ImperialTon per Mole), 2(CubicFoot per Mole) / 2(CubicFoot per ImperialTon))
        assertEquals(1(UsTon per Mole), 2(CubicFoot per Mole) / 2(CubicFoot per UsTon))
        assertEquals(
            1(Kilogram per Mole),
            2(CubicMeter per Mole) / 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume)
        )
    }

    @Test
    fun molarMassFromWeightAndAmountOfSubstanceTest() {
        assertEquals(1(Kilogram per Mole), 2(Kilogram) / 2(Mole))
        assertEquals(1(Pound per Mole), 2(Pound) / 2(Mole))
        assertEquals(1(ImperialTon per Mole), 2(ImperialTon) / 2(Mole))
        assertEqualScientificValue(1(UsTon per Mole), 2(UsTon) / 2(Mole), 8)
        assertEqualScientificValue(1(Kilogram per Mole), 2(Kilogram).convert(Pound as Weight) / 2(Mole), 8)
    }
}
