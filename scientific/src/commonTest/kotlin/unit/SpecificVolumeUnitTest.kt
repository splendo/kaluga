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

import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.density.specificVolume
import com.splendo.kaluga.scientific.converter.length.div
import com.splendo.kaluga.scientific.converter.molality.div
import com.splendo.kaluga.scientific.converter.molarVolume.div
import com.splendo.kaluga.scientific.converter.molarVolume.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class SpecificVolumeUnitTest {

    @Test
    fun specificVolumeConversionTest() {
        assertScientificConversion(1.0, (CubicMeter per Kilogram), 16.02, CubicFoot per Pound, 2)
        assertScientificConversion(1.0, (ImperialTonForce per Foot), 1.12, UsTonForce per Foot, 2)
    }

    @Test
    fun specificVolumeFromAreaAndLinearMassDensityTest() {
        assertEquals(1(CubicMeter per Kilogram), 2(SquareMeter) / 2(Kilogram per Meter))
        assertEquals(1(CubicFoot per Pound), 2(SquareFoot) / 2(Pound per Foot))
        assertEquals(1(CubicFoot per ImperialTon), 2(SquareFoot) / 2(ImperialTon per Foot))
        assertEquals(1(CubicFoot per UsTon), 2(SquareFoot) / 2(UsTon per Foot))
    }

    @Test
    fun specificVolumeFromInvertedDensityTest() {
        assertEquals(0.5(CubicMeter per Kilogram), 2(Kilogram per CubicMeter).specificVolume())
        assertEquals(0.5(CubicFoot per Pound), 2(Pound per CubicFoot).specificVolume())
        assertEquals(0.5(CubicFoot per ImperialTon), 2(ImperialTon per CubicFoot).specificVolume())
        assertEquals(0.5(CubicFoot per UsTon), 2(UsTon per CubicFoot).specificVolume())
        assertEquals(0.5(ImperialGallon per Pound), 2(Pound per ImperialGallon).specificVolume())
        assertEquals(
            0.5(ImperialGallon per ImperialTon),
            2(ImperialTon per ImperialGallon).specificVolume()
        )
        assertEquals(0.5(UsLiquidGallon per Pound), 2(Pound per UsLiquidGallon).specificVolume())
        assertEquals(0.5(UsLiquidGallon per UsTon), 2(UsTon per UsLiquidGallon).specificVolume())
    }

    @Test
    fun specificVolumeFromLengthAndAreaDensityTest() {
        assertEquals(1(CubicMeter per Kilogram), 2(Meter) / 2(Kilogram per SquareMeter))
        assertEquals(1(CubicFoot per Pound), 2(Foot) / 2(Pound per SquareFoot))
        assertEquals(1(CubicFoot per ImperialTon), 2(Foot) / 2(ImperialTon per SquareFoot))
        assertEquals(1(CubicFoot per UsTon), 2(Foot) / 2(UsTon per SquareFoot))
    }

    @Test
    fun specificVolumeFromMolalityAndMolarityTest() {
        assertEquals(1(CubicMeter per Kilogram), 2(Mole per Kilogram) / 2(Mole per CubicMeter))
        assertEquals(1(CubicFoot per Pound), 2(Mole per Pound) / 2(Mole per CubicFoot))
        assertEquals(1(CubicFoot per ImperialTon), 2(Mole per ImperialTon) / 2(Mole per CubicFoot))
        assertEquals(1(CubicFoot per UsTon), 2(Mole per UsTon) / 2(Mole per CubicFoot))
        assertEquals(1(ImperialGallon per Pound), 2(Mole per Pound) / 2(Mole per ImperialGallon))
        assertEquals(
            1(ImperialGallon per ImperialTon),
            2(Mole per ImperialTon) / 2(Mole per ImperialGallon)
        )
        assertEquals(1(UsLiquidGallon per Pound), 2(Mole per Pound) / 2(Mole per UsLiquidGallon))
        assertEquals(1(UsLiquidGallon per UsTon), 2(Mole per UsTon) / 2(Mole per UsLiquidGallon))
    }

    @Test
    fun specificVolumeFromMolalityAndMolarVolumeTest() {
        assertEquals(4(CubicMeter per Kilogram), 2(CubicMeter per Mole) * 2(Mole per Kilogram))
        assertEquals(4(CubicFoot per Pound), 2(CubicFoot per Mole) * 2(Mole per Pound))
        assertEquals(4(CubicFoot per ImperialTon), 2(CubicFoot per Mole) * 2(Mole per ImperialTon))
        assertEquals(4(CubicFoot per UsTon), 2(CubicFoot per Mole) * 2(Mole per UsTon))
        assertEquals(4(ImperialGallon per Pound), 2(ImperialGallon per Mole) * 2(Mole per Pound))
        assertEquals(4(ImperialGallon per ImperialTon), 2(ImperialGallon per Mole) * 2(Mole per ImperialTon))
        assertEquals(4(UsLiquidGallon per Pound), 2(UsLiquidGallon per Mole) * 2(Mole per Pound))
        assertEquals(4(UsLiquidGallon per UsTon), 2(UsLiquidGallon per Mole) * 2(Mole per UsTon))
    }

    @Test
    fun specificVolumeFromMolarVolumeAndMolarMassTest() {
        assertEquals(1(CubicMeter per Kilogram), 2(CubicMeter per Mole) / 2(Kilogram per Mole))
        assertEquals(1(CubicFoot per Pound), 2(CubicFoot per Mole) / 2(Pound per Mole))
        assertEquals(1(CubicFoot per ImperialTon), 2(CubicFoot per Mole) / 2(ImperialTon per Mole))
        assertEquals(1(CubicFoot per UsTon), 2(CubicFoot per Mole) / 2(UsTon per Mole))
        assertEquals(1(ImperialGallon per Pound), 2(ImperialGallon per Mole) / 2(Pound per Mole))
        assertEquals(1(ImperialGallon per ImperialTon), 2(ImperialGallon per Mole) / 2(ImperialTon per Mole))
        assertEquals(1(UsLiquidGallon per Pound), 2(UsLiquidGallon per Mole) / 2(Pound per Mole))
        assertEquals(1(UsLiquidGallon per UsTon), 2(UsLiquidGallon per Mole) / 2(UsTon per Mole))
    }

    @Test
    fun specificVolumeFromVolumeAndWeightTest() {
        assertEquals(1(CubicMeter per Kilogram), 2(CubicMeter) / 2(Kilogram))
        assertEquals(1(CubicFoot per Pound), 2(CubicFoot) / 2(Pound))
        assertEquals(1(CubicFoot per ImperialTon), 2(CubicFoot) / 2(ImperialTon))
        assertEquals(1(CubicFoot per UsTon), 2(CubicFoot) / 2(UsTon))
        assertEquals(1(ImperialGallon per Pound), 2(ImperialGallon) / 2(Pound))
        assertEquals(1(ImperialGallon per ImperialTon), 2(ImperialGallon) / 2(ImperialTon))
        assertEquals(1(UsLiquidGallon per Pound), 2(UsLiquidGallon) / 2(Pound))
        assertEquals(1(UsLiquidGallon per UsTon), 2(UsLiquidGallon) / 2(UsTon))
    }
}
