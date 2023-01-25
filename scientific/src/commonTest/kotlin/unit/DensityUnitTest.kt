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

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.areaDensity.div
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.molarMass.div
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.molarity.div
import com.splendo.kaluga.scientific.converter.molarity.times
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
        assertEquals(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per SquareFoot) / 2(Foot)
        )
        assertEquals(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per SquareFoot) / 2(Foot)
        )
        assertEquals(
            1(Kilogram per CubicMeter),
            2(Kilogram per SquareMeter) / 2(Meter).convert(Foot)
        )
    }

    @Test
    fun densityFromDynamicViscosityAndKinematicViscosityTest() {
        assertEquals(1(Kilogram per CubicMeter), 2(Pascal x Second) / 2(SquareMeter per Second))
        assertEquals(ImperialStandardGravityAcceleration.value(Pound per CubicFoot), 2(PoundSquareFoot x Second) / 2(SquareFoot per Second))
        assertEquals(ImperialStandardGravityAcceleration.value(Pound.ukImperial per CubicFoot), 2(PoundSquareFoot.ukImperial x Second) / 2(SquareFoot per Second))
        assertEquals(ImperialStandardGravityAcceleration.value(Pound.usCustomary per CubicFoot), 2(PoundSquareFoot.usCustomary x Second) / 2(SquareFoot per Second))
        assertEquals(1(Kilogram per CubicMeter), 2(Pascal x Second) / 2(SquareMeter per Second).convert(SquareFoot per Second))
    }

    @Test
    fun densityFromLinearMassDensityAndAreaTest() {
        assertEquals(1(Kilogram per CubicMeter), 2(Kilogram per Meter) / 2(SquareMeter))
        assertEquals(1(Pound per CubicFoot), 2(Pound per Foot) / 2(SquareFoot))
        assertEquals(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Foot) / 2(SquareFoot)
        )
        assertEquals(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Foot) / 2(SquareFoot)
        )
        assertEquals(
            1(Kilogram per CubicMeter),
            2(Kilogram per Meter) / 2(SquareMeter).convert(SquareFoot)
        )
    }

    @Test
    fun densityFromMolarityAndMolalityTest() {
        assertEquals(1(Kilogram per CubicMeter), 2(Decimole per CubicMeter) / 2(Decimole per Kilogram))
        assertEquals(1(Pound per CubicFoot), 2(Decimole per CubicFoot) / 2(Decimole per Pound))
        assertEquals(
            1(Pound.ukImperial per CubicFoot),
            2(Decimole per CubicFoot) / 2(Decimole per Pound.ukImperial)
        )
        assertEquals(
            1(Pound.usCustomary per CubicFoot),
            2(Decimole per CubicFoot) / 2(Decimole per Pound.usCustomary)
        )
        assertEquals(
            1(Pound per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) / 2(Decimole per Pound)
        )
        assertEquals(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) / 2(Decimole per Pound.ukImperial)
        )
        assertEquals(
            1(Pound per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) / 2(Decimole per Pound)
        )
        assertEquals(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) / 2(Decimole per Pound.usCustomary)
        )
        assertEquals(
            1(Kilogram per CubicMeter),
            2(Decimole per CubicMeter) / 2(Decimole per Kilogram).convert(Decimole per Pound)
        )
    }

    @Test
    fun densityFromMolarityAndMolarMassTest() {
        assertEquals(4(Kilogram per CubicMeter), 2(Kilogram per Decimole) * 2(Decimole per CubicMeter))
        assertEquals(4(Kilogram per CubicMeter), 2(Decimole per CubicMeter) * 2(Kilogram per Decimole))
        assertEquals(4(Pound per CubicFoot), 2(Pound per Decimole) * 2(Decimole per CubicFoot))
        assertEquals(4(Pound per CubicFoot), 2(Decimole per CubicFoot) * 2(Pound per Decimole))
        assertEquals(
            4(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Decimole) * 2(Decimole per CubicFoot)
        )
        assertEquals(
            4(Pound.ukImperial per CubicFoot),
            2(Decimole per CubicFoot) * 2(Pound.ukImperial per Decimole)
        )
        assertEquals(
            4(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Decimole) * 2(Decimole per CubicFoot)
        )
        assertEquals(
            4(Pound.usCustomary per CubicFoot),
            2(Decimole per CubicFoot) * 2(Pound.usCustomary per Decimole)
        )
        assertEquals(
            4(Pound per CubicFoot.ukImperial),
            2(Pound per Decimole) * 2(Decimole per CubicFoot.ukImperial)
        )
        assertEquals(
            4(Pound per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) * 2(Pound per Decimole)
        )
        assertEquals(
            4(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial per Decimole) * 2(Decimole per CubicFoot.ukImperial)
        )
        assertEquals(
            4(Pound.ukImperial per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) * 2(Pound.ukImperial per Decimole)
        )
        assertEquals(
            4(Pound per CubicFoot.usCustomary),
            2(Pound per Decimole) * 2(Decimole per CubicFoot.usCustomary)
        )
        assertEquals(
            4(Pound per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) * 2(Pound per Decimole)
        )
        assertEquals(
            4(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary per Decimole) * 2(Decimole per CubicFoot.usCustomary)
        )
        assertEquals(
            4(Pound.usCustomary per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) * 2(Pound.usCustomary per Decimole)
        )
        assertEquals(
            4(Kilogram per CubicMeter),
            2(Kilogram per Decimole) * 2(Decimole per CubicMeter).convert(Decimole per CubicFoot)
        )
        assertEquals(
            4(Kilogram per CubicMeter),
            2(Decimole per CubicMeter).convert(Decimole per CubicFoot) * 2(Kilogram per Decimole)
        )
    }

    @Test
    fun densityFromMolarMassAndMolarVolumeTest() {
        assertEquals(1(Kilogram per CubicMeter), 2(Kilogram per Decimole) / 2(CubicMeter per Decimole))
        assertEquals(1(Pound per CubicFoot), 2(Pound per Decimole) / 2(CubicFoot per Decimole))
        assertEquals(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Decimole) / 2(CubicFoot per Decimole)
        )
        assertEquals(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Decimole) / 2(CubicFoot per Decimole)
        )
        assertEquals(
            1(Pound per CubicFoot.ukImperial),
            2(Pound per Decimole) / 2(CubicFoot.ukImperial per Decimole)
        )
        assertEquals(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial per Decimole) / 2(CubicFoot.ukImperial per Decimole)
        )
        assertEquals(
            1(Pound per CubicFoot.usCustomary),
            2(Pound per Decimole) / 2(CubicFoot.usCustomary per Decimole)
        )
        assertEquals(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary per Decimole) / 2(CubicFoot.usCustomary per Decimole)
        )
        assertEquals(
            1(Kilogram per CubicMeter),
            2(Kilogram per Decimole) / 2(CubicMeter per Decimole).convert(CubicFoot per Decimole)
        )
    }

    @Test
    fun densityFromInverseSpecificVolumeTest() {
        assertEquals(2(Kilogram per CubicMeter), 0.5(CubicMeter per Kilogram).density())
        assertEquals(2(Pound per CubicFoot), 0.5(CubicFoot per Pound).density())
        assertEquals(
            2(Pound.ukImperial per CubicFoot),
            0.5(CubicFoot per Pound.ukImperial).density()
        )
        assertEquals(
            2(Pound.usCustomary per CubicFoot),
            0.5(CubicFoot per Pound.usCustomary).density()
        )
        assertEquals(
            2(Pound.ukImperial per CubicFoot),
            0.5(CubicFoot per Pound.ukImperial).density()
        )
        assertEquals(
            2(Pound.ukImperial per CubicFoot.ukImperial),
            0.5(CubicFoot.ukImperial per Pound.ukImperial).density()
        )
        assertEquals(
            2(Pound.usCustomary per CubicFoot),
            0.5(CubicFoot per Pound.usCustomary).density()
        )
        assertEquals(
            2(Pound.usCustomary per CubicFoot.usCustomary),
            0.5(CubicFoot.usCustomary per Pound.usCustomary).density()
        )
        assertEquals(
            2(Kilogram per CubicMeter),
            0.5(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume).density()
        )
    }

    @Test
    fun densityFromWeightAndVolumeTest() {
        assertEquals(1(Kilogram per CubicMeter), 2(Kilogram) / 2(CubicMeter))
        assertEquals(1(Pound per CubicFoot), 2(Pound) / 2(CubicFoot))
        assertEquals(1(Pound.ukImperial per CubicFoot), 2(Pound.ukImperial) / 2(CubicFoot))
        assertEquals(1(Pound.usCustomary per CubicFoot), 2(Pound.usCustomary) / 2(CubicFoot))
        assertEquals(1(Pound per CubicFoot.ukImperial), 2(Pound) / 2(CubicFoot.ukImperial))
        assertEquals(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial) / 2(CubicFoot.ukImperial)
        )
        assertEquals(1(Pound per CubicFoot.usCustomary), 2(Pound) / 2(CubicFoot.usCustomary))
        assertEquals(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary) / 2(CubicFoot.usCustomary)
        )
        assertEquals(1(Kilogram per CubicMeter), 2(Kilogram) / 2(CubicMeter).convert(CubicFoot))
    }
}
