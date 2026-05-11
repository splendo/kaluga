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
import com.splendo.kaluga.scientific.converter.areaDensity.div
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.massFlowRate.div
import com.splendo.kaluga.scientific.converter.molarMass.div
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.molarity.div
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.converter.specificVolume.density
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class DensityUnitTest {

    @Test
    fun densityFromAreaDensityAndLengthTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram per SquareMeter) / 2(Meter))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound per SquareFoot) / 2(Foot), round = 32)
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per SquareFoot) / 2(Foot),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per SquareFoot) / 2(Foot),
            round = 32,
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Kilogram per SquareMeter) / 2(Meter).convert(Foot),
            round = 32,
        )
    }

    @Test
    fun densityFromDynamicViscosityAndKinematicViscosityTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Pascal x Second) / 2(SquareMeter per Second))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.decimalValue(Pound per CubicFoot), 2(PoundSquareFoot x Second) / 2(SquareFoot per Second), round = 31)
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration.decimalValue(Pound.ukImperial per CubicFoot),
            2(PoundSquareFoot.ukImperial x Second) / 2(SquareFoot per Second),
            round = 31,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration.decimalValue(Pound.usCustomary per CubicFoot),
            2(PoundSquareFoot.usCustomary x Second) / 2(SquareFoot per Second),
            round = 31,
        )
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Pascal x Second) / 2(SquareMeter per Second).convert(SquareFoot per Second), round = 32)
    }

    @Test
    fun densityFromLinearMassDensityAndAreaTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram per Meter) / 2(SquareMeter))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound per Foot) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Foot) / 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Foot) / 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Kilogram per Meter) / 2(SquareMeter).convert(SquareFoot),
            round = 32,
        )
    }

    @Test
    fun densityFromMassFlowRateAndVolumetricFlowTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram per Minute) / 2(CubicMeter per Minute))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound per Minute) / 2(CubicFoot per Minute), round = 32)
        assertEqualScientificValue(
            1(Pound per CubicFoot.ukImperial),
            2(Pound per Minute) / 2(CubicFoot.ukImperial per Minute),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.usCustomary),
            2(Pound per Minute) / 2(CubicFoot.usCustomary per Minute),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Minute) / 2(CubicFoot per Minute),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial per Minute) / 2(CubicFoot.ukImperial per Minute),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Minute) / 2(CubicFoot per Minute),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary per Minute) / 2(CubicFoot.usCustomary per Minute),
            round = 32,
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Kilogram per Second) / 2(CubicMeter per Second).convert(CubicFoot per Minute),
            round = 32,
        )
    }

    @Test
    fun densityFromMolarityAndMolalityTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Decimole per CubicMeter) / 2(Decimole per Kilogram))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Decimole per CubicFoot) / 2(Decimole per Pound), round = 30)
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Decimole per CubicFoot) / 2(Decimole per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Decimole per CubicFoot) / 2(Decimole per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) / 2(Decimole per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) / 2(Decimole per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) / 2(Decimole per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) / 2(Decimole per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Decimole per CubicMeter) / 2(Decimole per Kilogram).convert(Decimole per Pound),
        )
    }

    @Test
    fun densityFromMolarityAndMolarMassTest() {
        assertEqualScientificValue(4(Kilogram per CubicMeter), 2(Kilogram per Decimole) * 2(Decimole per CubicMeter))
        assertEqualScientificValue(4(Kilogram per CubicMeter), 2(Decimole per CubicMeter) * 2(Kilogram per Decimole))
        assertEqualScientificValue(4(Pound per CubicFoot), 2(Pound per Decimole) * 2(Decimole per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound per CubicFoot), 2(Decimole per CubicFoot) * 2(Pound per Decimole), round = 32)
        assertEqualScientificValue(
            4(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Decimole) * 2(Decimole per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.ukImperial per CubicFoot),
            2(Decimole per CubicFoot) * 2(Pound.ukImperial per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Decimole) * 2(Decimole per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per CubicFoot),
            2(Decimole per CubicFoot) * 2(Pound.usCustomary per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound per CubicFoot.ukImperial),
            2(Pound per Decimole) * 2(Decimole per CubicFoot.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) * 2(Pound per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial per Decimole) * 2(Decimole per CubicFoot.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.ukImperial per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) * 2(Pound.ukImperial per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound per CubicFoot.usCustomary),
            2(Pound per Decimole) * 2(Decimole per CubicFoot.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) * 2(Pound per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary per Decimole) * 2(Decimole per CubicFoot.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) * 2(Pound.usCustomary per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            4(Kilogram per CubicMeter),
            2(Kilogram per Decimole) * 2(Decimole per CubicMeter).convert(Decimole per CubicFoot),
        )
        assertEqualScientificValue(
            4(Kilogram per CubicMeter),
            2(Decimole per CubicMeter).convert(Decimole per CubicFoot) * 2(Kilogram per Decimole),
        )
    }

    @Test
    fun densityFromMolarMassAndMolarVolumeTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram per Decimole) / 2(CubicMeter per Decimole))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound per Decimole) / 2(CubicFoot per Decimole), round = 32)
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Decimole) / 2(CubicFoot per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Decimole) / 2(CubicFoot per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.ukImperial),
            2(Pound per Decimole) / 2(CubicFoot.ukImperial per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial per Decimole) / 2(CubicFoot.ukImperial per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.usCustomary),
            2(Pound per Decimole) / 2(CubicFoot.usCustomary per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary per Decimole) / 2(CubicFoot.usCustomary per Decimole),
            round = 32,
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Kilogram per Decimole) / 2(CubicMeter per Decimole).convert(CubicFoot per Decimole),
            round = 32,
        )
    }

    @Test
    fun densityFromInverseSpecificVolumeTest() {
        assertEqualScientificValue(2(Kilogram per CubicMeter), 0.5(CubicMeter per Kilogram).density())
        assertEqualScientificValue(2(Pound per CubicFoot), 0.5(CubicFoot per Pound).density(), round = 30)
        assertEqualScientificValue(
            2(Pound.ukImperial per CubicFoot),
            0.5(CubicFoot per Pound.ukImperial).density(),
            round = 30,
        )
        assertEqualScientificValue(
            2(Pound.usCustomary per CubicFoot),
            0.5(CubicFoot per Pound.usCustomary).density(),
            round = 30,
        )
        assertEqualScientificValue(
            2(Pound.ukImperial per CubicFoot),
            0.5(CubicFoot per Pound.ukImperial).density(),
            round = 30,
        )
        assertEqualScientificValue(
            2(Pound.ukImperial per CubicFoot.ukImperial),
            0.5(CubicFoot.ukImperial per Pound.ukImperial).density(),
            round = 30,
        )
        assertEqualScientificValue(
            2(Pound.usCustomary per CubicFoot),
            0.5(CubicFoot per Pound.usCustomary).density(),
            round = 30,
        )
        assertEqualScientificValue(
            2(Pound.usCustomary per CubicFoot.usCustomary),
            0.5(CubicFoot.usCustomary per Pound.usCustomary).density(),
            round = 30,
        )
        assertEqualScientificValue(
            2(Kilogram per CubicMeter),
            0.5(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume).density(),
            round = 32,
        )
    }

    @Test
    fun densityFromWeightAndVolumeTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram) / 2(CubicMeter))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound) / 2(CubicFoot), 20)
        assertEqualScientificValue(1(Pound.ukImperial per CubicFoot), 2(Pound.ukImperial) / 2(CubicFoot), round = 32)
        assertEqualScientificValue(1(Pound.usCustomary per CubicFoot), 2(Pound.usCustomary) / 2(CubicFoot), round = 32)
        assertEqualScientificValue(1(Pound per CubicFoot.ukImperial), 2(Pound) / 2(CubicFoot.ukImperial), round = 32)
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial) / 2(CubicFoot.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(1(Pound per CubicFoot.usCustomary), 2(Pound) / 2(CubicFoot.usCustomary), round = 32)
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary) / 2(CubicFoot.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram) / 2(CubicMeter).convert(CubicFoot), round = 32)
    }
}
