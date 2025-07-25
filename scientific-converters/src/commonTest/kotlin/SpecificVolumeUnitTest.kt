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
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.density.specificVolume
import com.splendo.kaluga.scientific.converter.length.div
import com.splendo.kaluga.scientific.converter.molality.div
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.converter.molarVolume.div
import com.splendo.kaluga.scientific.converter.molarVolume.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialGallon
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UsLiquidGallon
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class SpecificVolumeUnitTest {

    @Test
    fun specificVolumeFromAreaAndLinearMassDensityTest() {
        assertEqualScientificValue(1(CubicMeter per Kilogram), 2(SquareMeter) / 2(Kilogram per Meter))
        assertEqualScientificValue(1(CubicFoot per Pound), 2(SquareFoot) / 2(Pound per Foot))
        assertEqualScientificValue(1(CubicFoot per ImperialTon), 2(SquareFoot) / 2(ImperialTon per Foot))
        assertEqualScientificValue(1(CubicFoot per UsTon), 2(SquareFoot) / 2(UsTon per Foot))
        assertEqualScientificValue(
            1(CubicMeter per Kilogram),
            2(SquareMeter) / 2(Kilogram per Meter).convert(Pound per Foot),
        )
    }

    @Test
    fun specificVolumeFromInvertedDensityTest() {
        assertEqualScientificValue(0.5(CubicMeter per Kilogram), 2(Kilogram per CubicMeter).specificVolume())
        assertEqualScientificValue(0.5(CubicFoot per Pound), 2(Pound per CubicFoot).specificVolume())
        assertEqualScientificValue(0.5(CubicFoot per ImperialTon), 2(ImperialTon per CubicFoot).specificVolume())
        assertEqualScientificValue(0.5(CubicFoot per UsTon), 2(UsTon per CubicFoot).specificVolume())
        assertEqualScientificValue(0.5(ImperialGallon per Pound), 2(Pound per ImperialGallon).specificVolume())
        assertEqualScientificValue(
            0.5(ImperialGallon per ImperialTon),
            2(ImperialTon per ImperialGallon).specificVolume(),
        )
        assertEqualScientificValue(0.5(UsLiquidGallon per Pound), 2(Pound per UsLiquidGallon).specificVolume())
        assertEqualScientificValue(0.5(UsLiquidGallon per UsTon), 2(UsTon per UsLiquidGallon).specificVolume())
        assertEqualScientificValue(
            0.5(CubicMeter per Kilogram),
            2(Kilogram per CubicMeter).convert((Pound per CubicFoot) as Density).specificVolume(),
        )
    }

    @Test
    fun specificVolumeFromLengthAndAreaDensityTest() {
        assertEqualScientificValue(1(CubicMeter per Kilogram), 2(Meter) / 2(Kilogram per SquareMeter))
        assertEqualScientificValue(1(CubicFoot per Pound), 2(Foot) / 2(Pound per SquareFoot))
        assertEqualScientificValue(1(CubicFoot per ImperialTon), 2(Foot) / 2(ImperialTon per SquareFoot))
        assertEqualScientificValue(1(CubicFoot per UsTon), 2(Foot) / 2(UsTon per SquareFoot))
        assertEqualScientificValue(
            1(CubicMeter per Kilogram),
            2(Meter).convert(Foot) / 2(Kilogram per SquareMeter),
        )
    }

    @Test
    fun specificVolumeFromMolalityAndMolarityTest() {
        assertEqualScientificValue(1(CubicMeter per Kilogram), 2(Decimole per Kilogram) / 2(Decimole per CubicMeter))
        assertEqualScientificValue(1(CubicFoot per Pound), 2(Decimole per Pound) / 2(Decimole per CubicFoot))
        assertEqualScientificValue(1(CubicFoot per ImperialTon), 2(Decimole per ImperialTon) / 2(Decimole per CubicFoot))
        assertEqualScientificValue(1(CubicFoot per UsTon), 2(Decimole per UsTon) / 2(Decimole per CubicFoot))
        assertEqualScientificValue(1(ImperialGallon per Pound), 2(Decimole per Pound) / 2(Decimole per ImperialGallon))
        assertEqualScientificValue(
            1(ImperialGallon per ImperialTon),
            2(Decimole per ImperialTon) / 2(Decimole per ImperialGallon),
        )
        assertEqualScientificValue(1(UsLiquidGallon per Pound), 2(Decimole per Pound) / 2(Decimole per UsLiquidGallon))
        assertEqualScientificValue(1(UsLiquidGallon per UsTon), 2(Decimole per UsTon) / 2(Decimole per UsLiquidGallon))
        assertEqualScientificValue(
            1(CubicMeter per Kilogram),
            2(Decimole per Kilogram) / 2(Decimole per CubicMeter).convert(Decimole per CubicFoot),
        )
    }

    @Test
    fun specificVolumeFromMolalityAndMolarVolumeTest() {
        assertEqualScientificValue(4(CubicMeter per Kilogram), 2(CubicMeter per Decimole) * 2(Decimole per Kilogram))
        assertEqualScientificValue(4(CubicMeter per Kilogram), 2(Decimole per Kilogram) * 2(CubicMeter per Decimole))
        assertEqualScientificValue(4(CubicFoot per Pound), 2(CubicFoot per Decimole) * 2(Decimole per Pound))
        assertEqualScientificValue(4(CubicFoot per Pound), 2(Decimole per Pound) * 2(CubicFoot per Decimole))
        assertEqualScientificValue(4(CubicFoot per ImperialTon), 2(CubicFoot per Decimole) * 2(Decimole per ImperialTon))
        assertEqualScientificValue(4(CubicFoot per ImperialTon), 2(Decimole per ImperialTon) * 2(CubicFoot per Decimole))
        assertEqualScientificValue(4(CubicFoot per UsTon), 2(CubicFoot per Decimole) * 2(Decimole per UsTon))
        assertEqualScientificValue(4(CubicFoot per UsTon), 2(Decimole per UsTon) * 2(CubicFoot per Decimole))
        assertEqualScientificValue(4(ImperialGallon per Pound), 2(ImperialGallon per Decimole) * 2(Decimole per Pound))
        assertEqualScientificValue(4(ImperialGallon per Pound), 2(Decimole per Pound) * 2(ImperialGallon per Decimole))
        assertEqualScientificValue(
            4(ImperialGallon per ImperialTon),
            2(ImperialGallon per Decimole) * 2(Decimole per ImperialTon),
        )
        assertEqualScientificValue(
            4(ImperialGallon per ImperialTon),
            2(Decimole per ImperialTon) * 2(ImperialGallon per Decimole),
        )
        assertEqualScientificValue(4(UsLiquidGallon per Pound), 2(UsLiquidGallon per Decimole) * 2(Decimole per Pound))
        assertEqualScientificValue(4(UsLiquidGallon per Pound), 2(Decimole per Pound) * 2(UsLiquidGallon per Decimole))
        assertEqualScientificValue(4(UsLiquidGallon per UsTon), 2(UsLiquidGallon per Decimole) * 2(Decimole per UsTon))
        assertEqualScientificValue(4(UsLiquidGallon per UsTon), 2(Decimole per UsTon) * 2(UsLiquidGallon per Decimole))
        assertEqualScientificValue(
            4(CubicMeter per Kilogram),
            2(CubicMeter per Decimole) * 2(Decimole per Kilogram).convert(Decimole per Pound),
        )
        assertEqualScientificValue(
            4(CubicMeter per Kilogram),
            2(Decimole per Kilogram).convert(Decimole per Pound) * 2(CubicMeter per Decimole),
        )
    }

    @Test
    fun specificVolumeFromMolarVolumeAndMolarMassTest() {
        assertEqualScientificValue(1(CubicMeter per Kilogram), 2(CubicMeter per Decimole) / 2(Kilogram per Decimole))
        assertEqualScientificValue(1(CubicFoot per Pound), 2(CubicFoot per Decimole) / 2(Pound per Decimole))
        assertEqualScientificValue(1(CubicFoot per ImperialTon), 2(CubicFoot per Decimole) / 2(ImperialTon per Decimole))
        assertEqualScientificValue(1(CubicFoot per UsTon), 2(CubicFoot per Decimole) / 2(UsTon per Decimole))
        assertEqualScientificValue(1(ImperialGallon per Pound), 2(ImperialGallon per Decimole) / 2(Pound per Decimole))
        assertEqualScientificValue(
            1(ImperialGallon per ImperialTon),
            2(ImperialGallon per Decimole) / 2(ImperialTon per Decimole),
        )
        assertEqualScientificValue(1(UsLiquidGallon per Pound), 2(UsLiquidGallon per Decimole) / 2(Pound per Decimole))
        assertEqualScientificValue(1(UsLiquidGallon per UsTon), 2(UsLiquidGallon per Decimole) / 2(UsTon per Decimole))
        assertEqualScientificValue(
            1(CubicMeter per Kilogram),
            2(CubicMeter per Decimole) / 2(Kilogram per Decimole).convert(Pound per Decimole),
            8,
        )
    }

    @Test
    fun specificVolumeFromVolumeAndWeightTest() {
        assertEqualScientificValue(1(CubicMeter per Kilogram), 2(CubicMeter) / 2(Kilogram))
        assertEqualScientificValue(1(CubicFoot per Pound), 2(CubicFoot) / 2(Pound))
        assertEqualScientificValue(1(CubicFoot per ImperialTon), 2(CubicFoot) / 2(ImperialTon))
        assertEqualScientificValue(1(CubicFoot per UsTon), 2(CubicFoot) / 2(UsTon))
        assertEqualScientificValue(1(ImperialGallon per Pound), 2(ImperialGallon) / 2(Pound))
        assertEqualScientificValue(1(ImperialGallon per ImperialTon), 2(ImperialGallon) / 2(ImperialTon))
        assertEqualScientificValue(1(UsLiquidGallon per Pound), 2(UsLiquidGallon) / 2(Pound))
        assertEqualScientificValue(1(UsLiquidGallon per UsTon), 2(UsLiquidGallon) / 2(UsTon))
        assertEqualScientificValue(1(CubicMeter per Kilogram), 2(CubicMeter) / 2(Kilogram).convert(Pound), 8)
    }
}
