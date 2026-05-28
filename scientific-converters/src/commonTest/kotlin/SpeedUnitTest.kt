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

import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.acceleration.times
import com.splendo.kaluga.scientific.converter.length.div
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decidyne
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.imperial
import com.splendo.kaluga.scientific.unit.metric
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class SpeedUnitTest {

    @Test
    fun speedFromAccelerationAndTimeTest() {
        assertEqualScientificValue(4(Meter per Second), 2(Meter per Second per Second) * 2(Second))
        assertEqualScientificValue(4(Meter per Second), 2(Second) * 2(Meter per Second per Second))
        assertEqualScientificValue(4(Foot per Second), 2(Foot per Second per Second) * 2(Second))
        assertEqualScientificValue(4(Foot per Second), 2(Second) * 2(Foot per Second per Second))
        assertEqualScientificValue(
            4(Meter per Second),
            2(Meter per Second per Second).convert((Foot per Second per Second) as Acceleration) * 2(
                Second,
            ),
            round = 32,
        )
        assertEqualScientificValue(
            4(Meter per Second),
            2(Second) * 2(Meter per Second per Second).convert((Foot per Second per Second) as Acceleration),
            round = 32,
        )
    }

    @Test
    fun speedFromDistanceAndTimeTest() {
        assertEqualScientificValue(1(Meter per Second), 2(Meter) / 2(Second))
        assertEqualScientificValue(1(Foot per Second), 2(Foot) / 2(Second))
        assertEqualScientificValue(1(Meter per Second), 2(Meter).convert(Foot as Length) / 2(Second), round = 32)
    }

    @Test
    fun speedFromMomentumAndMassTest() {
        assertEqualScientificValue(1(Meter per Second), 2(Kilogram x (Meter per Second)) / 2(Kilogram))
        assertEqualScientificValue(1(Foot per Second), 2(Pound x (Foot per Second)) / 2(Pound))
        assertEqualScientificValue(1(Foot per Second), 2(Pound x (Foot per Second)) / 2(Pound.ukImperial))
        assertEqualScientificValue(1(Foot per Second), 2(Pound x (Foot per Second)) / 2(Pound.usCustomary))
        assertEqualScientificValue(1(Foot per Second), 2(Pound.ukImperial x (Foot per Second)) / 2(Pound))
        assertEqualScientificValue(
            1(Foot per Second),
            2(Pound.ukImperial x (Foot per Second)) / 2(Pound.ukImperial),
        )
        assertEqualScientificValue(1(Foot per Second), 2(Pound.usCustomary x (Foot per Second)) / 2(Pound))
        assertEqualScientificValue(
            1(Foot per Second),
            2(Pound.usCustomary x (Foot per Second)) / 2(Pound.usCustomary),
        )
        assertEqualScientificValue(
            1(Meter per Second),
            2(Kilogram x (Meter per Second)) / 2(Kilogram).convert(Pound),
            round = 30,
        )
    }

    @Test
    fun speedFromPowerAndForceTest() {
        assertEqualScientificValue(1(Centimeter per Second), 2(Erg per Second) / 2(Dyne))
        assertEqualScientificValue(1(Centimeter per Second), 2(Erg per Second) / 20(Decidyne))
        assertEqualScientificValue(1(Meter per Second), 2(Watt) / 2(Newton))
        assertEqualScientificValue(1(Meter per Second), 2(Watt.metric) / 2(Newton))
        assertEqualScientificValue(1(Foot per Second), 2(FootPoundForce per Second) / 2(PoundForce))
        assertEqualScientificValue(1(Foot per Minute), 2(FootPoundForce per Minute) / 2(PoundForce), round = 32)
        assertEqualScientificValue(1(Inch per Second), 2(InchPoundForce per Second) / 2(PoundForce), round = 32)
        assertEqualScientificValue(1(Inch per Minute), 2(InchPoundForce per Minute) / 2(PoundForce), round = 32)
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForce per Second).convert(BritishThermalUnit per Second) / 2(PoundForce),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot per Minute),
            2(FootPoundForce per Minute).convert(BritishThermalUnit per Minute) / 2(PoundForce),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot per Hour),
            (1.0.toDecimal() / 30.0.toDecimal())(FootPoundForce per Minute).convert(BritishThermalUnit per Hour) / 2(PoundForce),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForce per Second).convert(Watt) / 2(PoundForce),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForce per Second).convert(Watt.imperial) / 2(PoundForce),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForce per Second).convert(Watt) / 2(PoundForce.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForce per Second).convert(Watt.imperial) / 2(PoundForce.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForce per Second).convert(Watt) / 2(PoundForce.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForce per Second).convert(Watt.imperial) / 2(PoundForce.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(1(Meter per Second), 2(Watt.metric) / 2(Newton).convert(PoundForce), round = 30)
    }
}
