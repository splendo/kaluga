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
import com.splendo.kaluga.scientific.converter.energyDensity.asPressure
import com.splendo.kaluga.scientific.converter.pressure.asEnergyDensity
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Bar
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.imperial
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class EnergyDensityUnitTest {

    @Test
    fun energyDensityAsPressureTest() {
        assertEqualScientificValue(4(Pascal), 4(Joule per CubicMeter).asPressure())
        assertEqualScientificValue(4(Pascal), 4(Joule per CubicMeter).convert(WattHour.imperial per CubicFoot).asPressure(), round = 27)
    }

    @Test
    fun pressureAsEnergyDensityTest() {
        assertEqualScientificValue(4(Joule per CubicMeter), 4(Pascal).asEnergyDensity())
        assertEqualScientificValue(4(Joule per CubicMeter), 4(Pascal).convert(Bar).asEnergyDensity())
    }
}
