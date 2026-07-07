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

import com.splendo.kaluga.scientific.converter.acceleration.times
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.specificWeight.times
import com.splendo.kaluga.scientific.converter.volume.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class SpecificWeightUnitTest {

    @Test
    fun specificWeightFromForceAndVolumeTest() {
        assertEqualScientificValue(1(Newton per CubicMeter), 2(Newton) / 2(CubicMeter))
    }

    @Test
    fun forceFromSpecificWeightAndVolumeTest() {
        assertEqualScientificValue(4(Newton), 2(Newton per CubicMeter) * 2(CubicMeter))
        assertEqualScientificValue(4(Newton), 2(CubicMeter) * 2(Newton per CubicMeter))
    }

    @Test
    fun volumeFromForceAndSpecificWeightTest() {
        assertEqualScientificValue(2(CubicMeter), 4(Newton) / 2(Newton per CubicMeter))
    }

    @Test
    fun specificWeightFromDensityAndAccelerationTest() {
        assertEqualScientificValue(4(Newton per CubicMeter), 2(Kilogram per CubicMeter) * 2(Meter per Second per Second))
        assertEqualScientificValue(4(Newton per CubicMeter), 2(Meter per Second per Second) * 2(Kilogram per CubicMeter))
    }
}
