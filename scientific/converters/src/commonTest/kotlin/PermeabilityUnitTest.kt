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

import com.splendo.kaluga.scientific.converter.electricInductance.div
import com.splendo.kaluga.scientific.converter.magneticFieldStrength.times
import com.splendo.kaluga.scientific.converter.magneticInduction.div
import com.splendo.kaluga.scientific.converter.permeability.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Henry
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Tesla
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class PermeabilityUnitTest {

    @Test
    fun permeabilityFromElectricInductanceAndLengthTest() {
        assertEqualScientificValue(1(Henry per Meter), 2(Henry) / 2(Meter))
        assertEqualScientificValue(1(Henry per Foot), 2(Henry) / 2(Foot), round = 27)
    }

    @Test
    fun magneticInductionFromPermeabilityAndMagneticFieldStrengthTest() {
        assertEqualScientificValue(4(Tesla), 2(Henry per Meter) * 2(Ampere per Meter))
        assertEqualScientificValue(4(Tesla), 2(Ampere per Meter) * 2(Henry per Meter))
        assertEqualScientificValue(2(Henry per Meter), 4(Tesla) / 2(Ampere per Meter))
        assertEqualScientificValue(2(Ampere per Meter), 4(Tesla) / 2(Henry per Meter))
    }
}
