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
import com.splendo.kaluga.scientific.converter.acceleration.times
import com.splendo.kaluga.scientific.converter.length.div
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class SpeedUnitTest {

    @Test
    fun metricToImperialSpeedConversionTest() {
        assertScientificConversion(1.0, (Meter per Minute), 196.85, Foot per Hour, 2)
    }

    @Test
    fun speedFromAccelerationAndTimeTest() {
        assertEquals(4(Meter per Second), 2(Meter per Second per Second) * 2(Second))
        assertEquals(4(Meter per Second), 2(Second) * 2(Meter per Second per Second))
        assertEquals(4(Foot per Second), 2(Foot per Second per Second) * 2(Second))
        assertEquals(4(Foot per Second), 2(Second) * 2(Foot per Second per Second))
        assertEquals(
            4(Meter per Second),
            2(Meter per Second per Second).convert((Foot per Second per Second) as Acceleration) * 2(
                Second
            )
        )
        assertEquals(
            4(Meter per Second),
            2(Second) * 2(Meter per Second per Second).convert((Foot per Second per Second) as Acceleration)
        )
    }

    @Test
    fun speedFromDistanceAndTimeTest() {
        assertEquals(1(Meter per Second), 2(Meter) / 2(Second))
        assertEquals(1(Foot per Second), 2(Foot) / 2(Second))
        assertEquals(1(Meter per Second), 2(Meter).convert(Foot as Length) / 2(Second))
    }

    @Test
    fun speedFromMomentumAndMassTest() {
        assertEquals(1(Meter per Second), 2(Kilogram x (Meter per Second)) / 2(Kilogram))
        assertEquals(1(Foot per Second), 2(Pound x (Foot per Second)) / 2(Pound))
        assertEquals(1(Foot per Second), 2(Pound x (Foot per Second)) / 2(Pound.ukImperial))
        assertEquals(1(Foot per Second), 2(Pound x (Foot per Second)) / 2(Pound.usCustomary))
        assertEquals(1(Foot per Second), 2(Pound.ukImperial x (Foot per Second)) / 2(Pound))
        assertEquals(
            1(Foot per Second),
            2(Pound.ukImperial x (Foot per Second)) / 2(Pound.ukImperial)
        )
        assertEquals(1(Foot per Second), 2(Pound.usCustomary x (Foot per Second)) / 2(Pound))
        assertEquals(
            1(Foot per Second),
            2(Pound.usCustomary x (Foot per Second)) / 2(Pound.usCustomary)
        )
        assertEqualScientificValue(
            1(Meter per Second),
            2(Kilogram x (Meter per Second)) / 2(Kilogram).convert(Pound),
            8
        )
    }

    @Test
    fun speedFromPowerAndForceTest() {
        assertEquals(1(Centimeter per Second), 2(ErgPerSecond) / 2(Dyne))
        assertEquals(1(Centimeter per Second), 2(ErgPerSecond) / 20(Decidyne))
        assertEquals(1(Meter per Second), 2(Watt) / 2(Newton))
        assertEquals(1(Meter per Second), 2(Watt.metric) / 2(Newton))
        assertEquals(1(Foot per Second), 2(FootPoundForcePerSecond) / 2(PoundForce))
        assertEquals(1(Foot per Minute), 2(FootPoundForcePerMinute) / 2(PoundForce))
        assertEquals(1(Inch per Second), 2(InchPoundForcePerSecond) / 2(PoundForce))
        assertEquals(1(Inch per Minute), 2(InchPoundForcePerMinute) / 2(PoundForce))
        assertEquals(
            1(Foot per Second),
            2(FootPoundForcePerSecond).convert(BritishThermalUnitPerSecond) / 2(PoundForce)
        )
        assertEquals(
            1(Foot per Minute),
            2(FootPoundForcePerMinute).convert(BritishThermalUnitPerMinute) / 2(PoundForce)
        )
        assertEqualScientificValue(
            1(Foot per Hour),
            (1.0 / 30.0)(FootPoundForcePerMinute).convert(BritishThermalUnitPerHour) / 2(PoundForce),
            8
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForcePerSecond).convert(Watt) / 2(PoundForce)
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForcePerSecond).convert(Watt.imperial) / 2(PoundForce)
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForcePerSecond).convert(Watt) / 2(PoundForce.ukImperial)
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForcePerSecond).convert(Watt.imperial) / 2(PoundForce.ukImperial)
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForcePerSecond).convert(Watt) / 2(PoundForce.usCustomary)
        )
        assertEqualScientificValue(
            1(Foot per Second),
            2(FootPoundForcePerSecond).convert(Watt.imperial) / 2(PoundForce.usCustomary)
        )
        assertEquals(1(Meter per Second), 2(Watt.metric) / 2(Newton).convert(PoundForce))
    }
}
