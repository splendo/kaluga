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

import com.splendo.kaluga.scientific.converter.density.div
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
    fun molarMassConversionTest() {
        assertEquals(0.001, (Kilogram per Mole).convert(1.0, Tonne per Mole))
        assertEquals(16.0, (Pound per Mole).convert(1.0, Ounce per Mole))
        assertEquals(2240.0, (ImperialTon per Mole).convert(1.0, Pound per Mole))
        assertEquals(2000.0, (UsTon per Mole).convert(1.0, Pound per Mole))
    }

    @Test
    fun molarMassFromDensityAndMolarityTest() {
        assertEquals(1(Kilogram per Mole), 2(Kilogram per CubicMeter) / 2(Mole per CubicMeter))
        assertEquals(1(Pound per Mole), 2(Pound per CubicFoot) / 2(Mole per CubicFoot))
        assertEquals(1(ImperialTon per Mole), 2(ImperialTon per CubicFoot) / 2(Mole per CubicFoot))
        assertEquals(1(UsTon per Mole), 2(UsTon per CubicFoot) / 2(Mole per CubicFoot))
    }

    @Test
    fun molarMassFromDensityAndMolarVolumeTest() {
        assertEquals(4(Kilogram per Mole), 2(CubicMeter per Mole) * 2(Kilogram per CubicMeter))
        assertEquals(4(Pound per Mole), 2(CubicFoot per Mole) * 2(Pound per CubicFoot))
        assertEquals(4(ImperialTon per Mole), 2(CubicFoot per Mole) * 2(ImperialTon per CubicFoot))
        assertEquals(4(UsTon per Mole), 2(CubicFoot per Mole) * 2(UsTon per CubicFoot))
    }

    @Test
    fun molarMassFromInvertedMolalityTest() {
        assertEquals(2(Kilogram per Mole), 2(Mole per Kilogram).molarMass())
        assertEquals(2(Pound per Mole), 2(Mole per Pound).molarMass())
        assertEquals(2(ImperialTon per Mole), 2(Mole per ImperialTon).molarMass())
        assertEquals(2(UsTon per Mole), 2(Mole per UsTon).molarMass())
    }

    @Test
    fun molarMassFromMolarEnergyAndSpecificEnergyTest() {
        assertEquals(1(Kilogram per Mole), 2(Joule per Mole) / 2(Joule per Kilogram))
        assertEquals(1(Pound per Mole), 2(WattHour per Mole) / 2(WattHour per Pound))
        assertEquals(1(Pound per Mole), 2(HorsepowerHour per Mole) / 2(HorsepowerHour per Pound))
        assertEquals(1(ImperialTon per Mole), 2(WattHour per Mole) / 2(WattHour per ImperialTon))
        assertEquals(
            1(ImperialTon per Mole),
            2(HorsepowerHour per Mole) / 2(HorsepowerHour per ImperialTon)
        )
        assertEquals(1(UsTon per Mole), 2(WattHour per Mole) / 2(WattHour per UsTon))
        assertEquals(1(UsTon per Mole), 2(HorsepowerHour per Mole) / 2(HorsepowerHour per UsTon))
    }

    @Test
    fun molarMassFromMolarVolumeAndSpecificVolumeTest() {
        assertEquals(1(Kilogram per Mole), 2(CubicMeter per Mole) / 2(CubicMeter per Kilogram))
        assertEquals(1(Pound per Mole), 2(CubicFoot per Mole) / 2(CubicFoot per Pound))
        assertEquals(1(ImperialTon per Mole), 2(CubicFoot per Mole) / 2(CubicFoot per ImperialTon))
        assertEquals(1(UsTon per Mole), 2(CubicFoot per Mole) / 2(CubicFoot per UsTon))
    }

    @Test
    fun molarMassFromWeightAndAmountOfSubstanceTest() {
        assertEquals(1(Kilogram per Mole), 2(Kilogram) / 2(Mole))
        assertEquals(1(Pound per Mole), 2(Pound) / 2(Mole))
        assertEquals(1(ImperialTon per Mole), 2(ImperialTon) / 2(Mole))
        assertEquals(1(UsTon per Mole), 2(UsTon) / 2(Mole))
    }
}
