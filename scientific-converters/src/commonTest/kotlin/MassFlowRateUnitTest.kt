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
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.volumetricFlow.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilocalorie
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class MassFlowRateUnitTest {

    @Test
    fun massFlowRateFromDensityAndVolumetricFlow() {
        assertEqualScientificValue(4(Kilogram per Minute), 2(Kilogram per CubicMeter) * 2(CubicMeter per Minute), round = 32)
        assertEqualScientificValue(4(Kilogram per Minute), 2(CubicMeter per Minute) * 2(Kilogram per CubicMeter), round = 32)
        assertEqualScientificValue(4(Pound per Minute), 2(Pound per CubicFoot) * 2(CubicFoot per Minute), round = 32)
        assertEqualScientificValue(4(Pound per Minute), 2(CubicFoot per Minute) * 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound.ukImperial per Minute), 2(Pound.ukImperial per CubicFoot) * 2(CubicFoot per Minute), round = 32)
        assertEqualScientificValue(4(Pound.ukImperial per Minute), 2(CubicFoot per Minute) * 2(Pound.ukImperial per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound.usCustomary per Minute), 2(Pound.usCustomary per CubicFoot) * 2(CubicFoot per Minute), round = 32)
        assertEqualScientificValue(4(Pound.usCustomary per Minute), 2(CubicFoot per Minute) * 2(Pound.usCustomary per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound per Minute), 2(Pound per CubicFoot) * 2(CubicFoot.ukImperial per Minute), round = 32)
        assertEqualScientificValue(4(Pound per Minute), 2(CubicFoot.ukImperial per Minute) * 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound.ukImperial per Minute), 2(Pound.ukImperial per CubicFoot) * 2(CubicFoot.ukImperial per Minute), round = 32)
        assertEqualScientificValue(4(Pound.ukImperial per Minute), 2(CubicFoot.ukImperial per Minute) * 2(Pound.ukImperial per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound per Minute), 2(Pound per CubicFoot) * 2(CubicFoot.usCustomary per Minute), round = 32)
        assertEqualScientificValue(4(Pound per Minute), 2(CubicFoot.usCustomary per Minute) * 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound.usCustomary per Minute), 2(Pound.usCustomary per CubicFoot) * 2(CubicFoot.usCustomary per Minute), round = 32)
        assertEqualScientificValue(4(Pound.usCustomary per Minute), 2(CubicFoot.usCustomary per Minute) * 2(Pound.usCustomary per CubicFoot), round = 32)
        assertEqualScientificValue(4(Kilogram per Second), 2(Kilogram per CubicMeter) * 2(CubicMeter per Second).convert(CubicFoot per Minute), round = 32)
        assertEqualScientificValue(4(Kilogram per Second), 2(CubicMeter per Second).convert(CubicFoot per Second) * 2(Kilogram per CubicMeter), round = 32)
    }

    @Test
    fun massFlowRateFromPowerAndSpecificEnergy() {
        assertEqualScientificValue(1(Kilogram per Minute), 2(Kilocalorie per Minute) / 2(Kilocalorie per Kilogram), round = 32)
        assertEqualScientificValue(1(Pound per Minute), 2(Kilocalorie per Minute) / 2(Kilocalorie per Pound), round = 30)
        assertEqualScientificValue(1(Pound.ukImperial per Minute), 2(Kilocalorie per Minute) / 2(Kilocalorie per Pound.ukImperial), round = 30)
        assertEqualScientificValue(1(Pound.usCustomary per Minute), 2(Kilocalorie per Minute) / 2(Kilocalorie per Pound.usCustomary), round = 30)
        assertEqualScientificValue(1(Kilogram per Minute), 2(Joule per Minute) / 2(Joule per Kilogram), round = 32)
        assertEqualScientificValue(1(Pound per Minute), 2(FootPoundForce per Minute) / 2(FootPoundForce per Pound), round = 30)
        assertEqualScientificValue(1(Pound.ukImperial per Minute), 2(FootPoundForce per Minute) / 2(FootPoundForce per Pound.ukImperial), round = 30)
        assertEqualScientificValue(1(Pound.usCustomary per Minute), 2(FootPoundForce per Minute) / 2(FootPoundForce per Pound.usCustomary), round = 30)
        assertEqualScientificValue(1(Kilogram per Second), 2(Watt) / 2(Joule per Kilogram))
        assertEqualScientificValue(550(Pound per Second), 2(Horsepower) / 2(FootPoundForce per Pound), round = 27)
        assertEqualScientificValue(550(Pound.ukImperial per Second), 2(Horsepower) / 2(FootPoundForce per Pound.ukImperial), round = 27)
        assertEqualScientificValue(550(Pound.usCustomary per Second), 2(Horsepower) / 2(FootPoundForce per Pound.usCustomary), round = 27)
        assertEqualScientificValue(1(Kilogram per Second), 2(Watt) / 2(Joule per Kilogram).convert(FootPoundForce per Pound), round = 30)
    }

    @Test
    fun massFlowRateFromWeightAndAreaTest() {
        assertEqualScientificValue(1(Kilogram per Second), 2(Kilogram) / 2(Second))
        assertEqualScientificValue(1(Pound per Second), 2(Pound) / 2(Second))
        assertEqualScientificValue(1(Pound.ukImperial per Second), 2(Pound.ukImperial) / 2(Second))
        assertEqualScientificValue(1(Pound.usCustomary per Second), 2(Pound.usCustomary) / 2(Second))
        assertEqualScientificValue(1(Kilogram per Second), 2(Kilogram).convert(Pound as Weight) / 2(Second), round = 30)
    }
}
