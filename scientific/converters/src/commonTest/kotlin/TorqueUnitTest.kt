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
import com.splendo.kaluga.scientific.converter.energy.asTorque
import com.splendo.kaluga.scientific.converter.torque.asEnergy
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class TorqueUnitTest {

    @Test
    fun torqueAsEnergyTest() {
        assertEqualScientificValue(4(Joule), 4(Newton x Meter).asEnergy())
        assertEqualScientificValue(4(Joule), 4(Newton x Meter).convert(PoundForce x Foot).asEnergy(), round = 27)
    }

    @Test
    fun energyAsTorqueTest() {
        assertEqualScientificValue(4(Newton x Meter), 4(Joule).asTorque())
        assertEqualScientificValue(4(Newton x Meter), 4(Joule).convert(FootPoundForce).asTorque(), round = 27)
    }
}
