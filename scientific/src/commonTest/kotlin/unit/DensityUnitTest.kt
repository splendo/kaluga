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

import com.splendo.kaluga.scientific.converter.areaDensity.div
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.molarMass.div
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.molarity.div
import com.splendo.kaluga.scientific.converter.specificVolume.density
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class DensityUnitTest {

    @Test
    fun densityConversionTest() {
        assertScientificConversion(1, (Kilogram per CubicMeter), 0.062428, Pound per CubicFoot, 6)
    }

    @Test
    fun densityFromAreaDensityAndLengthTest() {
        assertEquals(1(Kilogram per CubicMeter), 2(Kilogram per SquareMeter) / 2(Meter))
        assertEquals(1(Pound per CubicFoot), 2(Pound per SquareFoot) / 2(Foot))
    }

    @Test
    fun densityFromLinearMassDensityAndAreaTest() {
        assertEquals(1(Kilogram per CubicMeter), 2(Kilogram per Meter) / 2(SquareMeter))
        assertEquals(1(Pound per CubicFoot), 2(Pound per Foot) / 2(SquareFoot))
    }

    @Test
    fun densityFromMolarityAndMolalityTest() {
        assertEquals(1(Kilogram per CubicMeter), 2(Mole per CubicMeter) / 2(Mole per Kilogram))
    }

    @Test
    fun densityFromMolarityAndMolarMassTest() {
        assertEquals(4(Kilogram per CubicMeter),2(Kilogram per Mole) * 2(Mole per CubicMeter))
        assertEquals(4(Pound per CubicFoot), 2(Pound per Mole) * 2(Mole per CubicFoot))
    }

    @Test
    fun densityFromMolarMassAndMolarVolumeTest() {
        assertEquals(1(Kilogram per CubicMeter),2(Kilogram per Mole) / 2(CubicMeter per Mole))
        assertEquals(1(Pound per CubicFoot), 2(Pound per Mole) / 2(CubicFoot per Mole))
    }

    @Test
    fun densityFromInverseSpecificVolumeTest() {
        assertEquals(2(Kilogram per CubicMeter), 0.5(CubicMeter per Kilogram).density())
    }

    @Test
    fun densityFromWeightAndVolumeTest() {
        assertEquals(1(Kilogram per CubicMeter),2(Kilogram) / 2(CubicMeter))
        assertEquals(1(Pound per CubicFoot),2(Pound) / 2(CubicFoot))
    }
}