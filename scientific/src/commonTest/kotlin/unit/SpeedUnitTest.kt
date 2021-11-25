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

import com.splendo.kaluga.scientific.converter.acceleration.times
import com.splendo.kaluga.scientific.converter.length.div
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test

class SpeedUnitTest {

    @Test
    fun metricToImperialSpeedConversionTest() {
        assertScientificConversion(1.0, (Meter per Minute), 196.85, Foot per Hour, 2)
    }

    @Test
    fun speedFromAccelerationAndTimeTest() {
        assertEquals(4(Meter per Second), 2(Meter per Second per Second) * 2(Second))
        assertEquals(4(Foot per Second), 2(Foot per Second per Second) * 2(Second))
    }

    @Test
    fun speedFromDistanceAndTimeTest() {
        assertEquals(1(Meter per Second), 2(Meter) / 2(Second))
        assertEquals(1(Foot per Second), 2(Foot) / 2(Second))
    }

    @Test
    fun speedFromMomentumAndMassTest() {
        assertEquals(1(Meter per Second), 2(Kilogram x (Meter per Second)) / 2(Kilogram))
        assertEquals(1(Foot per Second), 2(Pound x (Foot per Second)) / 2(Pound))
    }

    @Test
    fun speedFromPowerAndForce() {
        assertEquals(1(Meter per Second), 2(Watt) / 2(Newton))
        assertEquals(1(Foot per Second), 2(FootPoundForcePerSecond) / 2(PoundForce))
    }
}
