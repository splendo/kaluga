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
        assertScientificConversion(1.0, (Kilogram per Mole), 0.0001, Tonne per Decimole)
    }

    @Test
    fun molarMassFromDensityAndMolarityTest() {
        assertEquals(1(Kilogram per Decimole), 2(Kilogram per CubicMeter) / 2(Decimole per CubicMeter))
        assertEquals(1(Pound per Decimole), 2(Pound per CubicFoot) / 2(Decimole per CubicFoot))
        assertEquals(1(ImperialTon per Decimole), 2(ImperialTon per CubicFoot) / 2(Decimole per CubicFoot))
        assertEquals(1(UsTon per Decimole), 2(UsTon per CubicFoot) / 2(Decimole per CubicFoot))
        assertEquals(
            1(Kilogram per Decimole),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) / 2(Decimole per CubicMeter)
        )
    }

    @Test
    fun molarMassFromDensityAndMolarVolumeTest() {
        assertEquals(4(Kilogram per Decimole), 2(CubicMeter per Decimole) * 2(Kilogram per CubicMeter))
        assertEquals(4(Kilogram per Decimole), 2(Kilogram per CubicMeter) * 2(CubicMeter per Decimole))
        assertEquals(4(Pound per Decimole), 2(CubicFoot per Decimole) * 2(Pound per CubicFoot))
        assertEquals(4(Pound per Decimole), 2(Pound per CubicFoot) * 2(CubicFoot per Decimole))
        assertEquals(4(ImperialTon per Decimole), 2(CubicFoot per Decimole) * 2(ImperialTon per CubicFoot))
        assertEquals(4(ImperialTon per Decimole), 2(ImperialTon per CubicFoot) * 2(CubicFoot per Decimole))
        assertEquals(4(UsTon per Decimole), 2(CubicFoot per Decimole) * 2(UsTon per CubicFoot))
        assertEquals(4(UsTon per Decimole), 2(UsTon per CubicFoot) * 2(CubicFoot per Decimole))
        assertEquals(
            4(Kilogram per Decimole),
            2(CubicMeter per Decimole) * 2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density)
        )
        assertEquals(
            4(Kilogram per Decimole),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density) * 2(CubicMeter per Decimole)
        )
    }

    @Test
    fun molarMassFromInvertedMolalityTest() {
        assertEquals(0.5(Kilogram per Decimole), 2(Decimole per Kilogram).molarMass())
        assertEquals(0.5(Pound per Decimole), 2(Decimole per Pound).molarMass())
        assertEquals(0.5(ImperialTon per Decimole), 2(Decimole per ImperialTon).molarMass())
        assertEquals(0.5(UsTon per Decimole), 2(Decimole per UsTon).molarMass())
        assertEquals(
            0.5(Kilogram per Decimole),
            2(Decimole per Kilogram).convert((Decimole per Pound) as Molality).molarMass()
        )
    }

    @Test
    fun molarMassFromMolarEnergyAndSpecificEnergyTest() {
        assertEquals(1(Kilogram per Decimole), 2(Joule per Decimole) / 2(Joule per Kilogram))
        assertEquals(1(Pound per Decimole), 2(WattHour per Decimole) / 2(WattHour per Pound))
        assertEquals(
            1(Pound.ukImperial per Decimole),
            2(WattHour per Decimole) / 2(WattHour per Pound.ukImperial)
        )
        assertEquals(
            1(Pound.usCustomary per Decimole),
            2(WattHour per Decimole) / 2(WattHour per Pound.usCustomary)
        )
        assertEquals(
            1(Kilogram per Decimole),
            2(Joule per Decimole) / 2(Joule per Kilogram).convert((WattHour per Pound) as SpecificEnergy)
        )
    }

    @Test
    fun molarMassFromMolarVolumeAndSpecificVolumeTest() {
        assertEquals(1(Kilogram per Decimole), 2(CubicMeter per Decimole) / 2(CubicMeter per Kilogram))
        assertEquals(1(Pound per Decimole), 2(CubicFoot per Decimole) / 2(CubicFoot per Pound))
        assertEquals(1(ImperialTon per Decimole), 2(CubicFoot per Decimole) / 2(CubicFoot per ImperialTon))
        assertEquals(1(UsTon per Decimole), 2(CubicFoot per Decimole) / 2(CubicFoot per UsTon))
        assertEquals(
            1(Kilogram per Decimole),
            2(CubicMeter per Decimole) / 2(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume)
        )
    }

    @Test
    fun molarMassFromWeightAndAmountOfSubstanceTest() {
        assertEquals(1(Kilogram per Decimole), 2(Kilogram) / 2(Decimole))
        assertEquals(1(Pound per Decimole), 2(Pound) / 2(Decimole))
        assertEquals(1(ImperialTon per Decimole), 2(ImperialTon) / 2(Decimole))
        assertEqualScientificValue(1(UsTon per Decimole), 2(UsTon) / 2(Decimole), 8)
        assertEqualScientificValue(1(Kilogram per Decimole), 2(Kilogram).convert(Pound as Weight) / 2(Decimole), 8)
    }
}
