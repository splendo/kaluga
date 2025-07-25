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

class DensityUnitTest {

    @Test
    fun densityFromAreaDensityAndLengthTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram per SquareMeter) / 2(Meter))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound per SquareFoot) / 2(Foot))
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per SquareFoot) / 2(Foot),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per SquareFoot) / 2(Foot),
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Kilogram per SquareMeter) / 2(Meter).convert(Foot),
        )
    }

    @Test
    fun densityFromDynamicViscosityAndKinematicViscosityTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Pascal x Second) / 2(SquareMeter per Second))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(Pound per CubicFoot), 2(PoundSquareFoot x Second) / 2(SquareFoot per Second))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(Pound.ukImperial per CubicFoot), 2(PoundSquareFoot.ukImperial x Second) / 2(SquareFoot per Second))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(Pound.usCustomary per CubicFoot), 2(PoundSquareFoot.usCustomary x Second) / 2(SquareFoot per Second))
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Pascal x Second) / 2(SquareMeter per Second).convert(SquareFoot per Second))
    }

    @Test
    fun densityFromLinearMassDensityAndAreaTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram per Meter) / 2(SquareMeter))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound per Foot) / 2(SquareFoot))
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Foot) / 2(SquareFoot),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Foot) / 2(SquareFoot),
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Kilogram per Meter) / 2(SquareMeter).convert(SquareFoot),
        )
    }

    @Test
    fun densityFromMassFlowRateAndVolumetricFlowTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram per Minute) / 2(CubicMeter per Minute))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound per Minute) / 2(CubicFoot per Minute))
        assertEqualScientificValue(
            1(Pound per CubicFoot.ukImperial),
            2(Pound per Minute) / 2(CubicFoot.ukImperial per Minute),
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.usCustomary),
            2(Pound per Minute) / 2(CubicFoot.usCustomary per Minute),
        )
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Minute) / 2(CubicFoot per Minute),
        )
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial per Minute) / 2(CubicFoot.ukImperial per Minute),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Minute) / 2(CubicFoot per Minute),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary per Minute) / 2(CubicFoot.usCustomary per Minute),
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Kilogram per Second) / 2(CubicMeter per Second).convert(CubicFoot per Minute),
        )
    }

    @Test
    fun densityFromMolarityAndMolalityTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Decimole per CubicMeter) / 2(Decimole per Kilogram))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Decimole per CubicFoot) / 2(Decimole per Pound))
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Decimole per CubicFoot) / 2(Decimole per Pound.ukImperial),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Decimole per CubicFoot) / 2(Decimole per Pound.usCustomary),
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) / 2(Decimole per Pound),
        )
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) / 2(Decimole per Pound.ukImperial),
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) / 2(Decimole per Pound),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) / 2(Decimole per Pound.usCustomary),
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
        assertEqualScientificValue(4(Pound per CubicFoot), 2(Pound per Decimole) * 2(Decimole per CubicFoot))
        assertEqualScientificValue(4(Pound per CubicFoot), 2(Decimole per CubicFoot) * 2(Pound per Decimole))
        assertEqualScientificValue(
            4(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Decimole) * 2(Decimole per CubicFoot),
        )
        assertEqualScientificValue(
            4(Pound.ukImperial per CubicFoot),
            2(Decimole per CubicFoot) * 2(Pound.ukImperial per Decimole),
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Decimole) * 2(Decimole per CubicFoot),
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per CubicFoot),
            2(Decimole per CubicFoot) * 2(Pound.usCustomary per Decimole),
        )
        assertEqualScientificValue(
            4(Pound per CubicFoot.ukImperial),
            2(Pound per Decimole) * 2(Decimole per CubicFoot.ukImperial),
        )
        assertEqualScientificValue(
            4(Pound per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) * 2(Pound per Decimole),
        )
        assertEqualScientificValue(
            4(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial per Decimole) * 2(Decimole per CubicFoot.ukImperial),
        )
        assertEqualScientificValue(
            4(Pound.ukImperial per CubicFoot.ukImperial),
            2(Decimole per CubicFoot.ukImperial) * 2(Pound.ukImperial per Decimole),
        )
        assertEqualScientificValue(
            4(Pound per CubicFoot.usCustomary),
            2(Pound per Decimole) * 2(Decimole per CubicFoot.usCustomary),
        )
        assertEqualScientificValue(
            4(Pound per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) * 2(Pound per Decimole),
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary per Decimole) * 2(Decimole per CubicFoot.usCustomary),
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per CubicFoot.usCustomary),
            2(Decimole per CubicFoot.usCustomary) * 2(Pound.usCustomary per Decimole),
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
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound per Decimole) / 2(CubicFoot per Decimole))
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot),
            2(Pound.ukImperial per Decimole) / 2(CubicFoot per Decimole),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot),
            2(Pound.usCustomary per Decimole) / 2(CubicFoot per Decimole),
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.ukImperial),
            2(Pound per Decimole) / 2(CubicFoot.ukImperial per Decimole),
        )
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial per Decimole) / 2(CubicFoot.ukImperial per Decimole),
        )
        assertEqualScientificValue(
            1(Pound per CubicFoot.usCustomary),
            2(Pound per Decimole) / 2(CubicFoot.usCustomary per Decimole),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary per Decimole) / 2(CubicFoot.usCustomary per Decimole),
        )
        assertEqualScientificValue(
            1(Kilogram per CubicMeter),
            2(Kilogram per Decimole) / 2(CubicMeter per Decimole).convert(CubicFoot per Decimole),
        )
    }

    @Test
    fun densityFromInverseSpecificVolumeTest() {
        assertEqualScientificValue(2(Kilogram per CubicMeter), 0.5(CubicMeter per Kilogram).density())
        assertEqualScientificValue(2(Pound per CubicFoot), 0.5(CubicFoot per Pound).density())
        assertEqualScientificValue(
            2(Pound.ukImperial per CubicFoot),
            0.5(CubicFoot per Pound.ukImperial).density(),
        )
        assertEqualScientificValue(
            2(Pound.usCustomary per CubicFoot),
            0.5(CubicFoot per Pound.usCustomary).density(),
        )
        assertEqualScientificValue(
            2(Pound.ukImperial per CubicFoot),
            0.5(CubicFoot per Pound.ukImperial).density(),
        )
        assertEqualScientificValue(
            2(Pound.ukImperial per CubicFoot.ukImperial),
            0.5(CubicFoot.ukImperial per Pound.ukImperial).density(),
        )
        assertEqualScientificValue(
            2(Pound.usCustomary per CubicFoot),
            0.5(CubicFoot per Pound.usCustomary).density(),
        )
        assertEqualScientificValue(
            2(Pound.usCustomary per CubicFoot.usCustomary),
            0.5(CubicFoot.usCustomary per Pound.usCustomary).density(),
        )
        assertEqualScientificValue(
            2(Kilogram per CubicMeter),
            0.5(CubicMeter per Kilogram).convert((CubicFoot per Pound) as SpecificVolume).density(),
        )
    }

    @Test
    fun densityFromWeightAndVolumeTest() {
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram) / 2(CubicMeter))
        assertEqualScientificValue(1(Pound per CubicFoot), 2(Pound) / 2(CubicFoot))
        assertEqualScientificValue(1(Pound.ukImperial per CubicFoot), 2(Pound.ukImperial) / 2(CubicFoot))
        assertEqualScientificValue(1(Pound.usCustomary per CubicFoot), 2(Pound.usCustomary) / 2(CubicFoot))
        assertEqualScientificValue(1(Pound per CubicFoot.ukImperial), 2(Pound) / 2(CubicFoot.ukImperial))
        assertEqualScientificValue(
            1(Pound.ukImperial per CubicFoot.ukImperial),
            2(Pound.ukImperial) / 2(CubicFoot.ukImperial),
        )
        assertEqualScientificValue(1(Pound per CubicFoot.usCustomary), 2(Pound) / 2(CubicFoot.usCustomary))
        assertEqualScientificValue(
            1(Pound.usCustomary per CubicFoot.usCustomary),
            2(Pound.usCustomary) / 2(CubicFoot.usCustomary),
        )
        assertEqualScientificValue(1(Kilogram per CubicMeter), 2(Kilogram) / 2(CubicMeter).convert(CubicFoot))
    }
}
