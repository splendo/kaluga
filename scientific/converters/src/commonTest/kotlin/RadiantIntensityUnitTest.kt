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

import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.radiantIntensity.times
import com.splendo.kaluga.scientific.converter.solidAngle.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Steradian
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.imperial
import com.splendo.kaluga.scientific.unit.metric
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class RadiantIntensityUnitTest {

    @Test
    fun radiantIntensityFromPowerAndSolidAngleTest() {
        assertEqualScientificValue(2(Watt per Steradian), 4(Watt) / 2(Steradian))
        assertEqualScientificValue(2(Watt.metric per Steradian), 4(Watt.metric) / 2(Steradian))
        assertEqualScientificValue(2(Watt.imperial per Steradian), 4(Watt.imperial) / 2(Steradian))
    }

    @Test
    fun powerFromRadiantIntensityAndSolidAngleTest() {
        assertEqualScientificValue(4(Watt), 2(Watt per Steradian) * 2(Steradian))
        assertEqualScientificValue(4(Watt), 2(Steradian) * 2(Watt per Steradian))
        assertEqualScientificValue(4(Watt.metric), 2(Watt.metric per Steradian) * 2(Steradian))
        assertEqualScientificValue(4(Watt.metric), 2(Steradian) * 2(Watt.metric per Steradian))
        assertEqualScientificValue(4(Watt.imperial), 2(Watt.imperial per Steradian) * 2(Steradian))
        assertEqualScientificValue(4(Watt.imperial), 2(Steradian) * 2(Watt.imperial per Steradian))
    }
}
