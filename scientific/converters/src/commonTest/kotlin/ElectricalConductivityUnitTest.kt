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

import com.splendo.kaluga.scientific.converter.electricConductance.div
import com.splendo.kaluga.scientific.converter.electricalConductivity.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Siemens
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class ElectricalConductivityUnitTest {

    @Test
    fun electricalConductivityFromElectricConductanceAndLengthTest() {
        assertEqualScientificValue(1(Siemens per Meter), 2(Siemens) / 2(Meter))
        assertEqualScientificValue(1(Siemens per Foot), 2(Siemens) / 2(Foot), round = 27)
    }

    @Test
    fun electricConductanceFromElectricalConductivityAndLengthTest() {
        assertEqualScientificValue(4(Siemens), 2(Siemens per Meter) * 2(Meter))
        assertEqualScientificValue(4(Siemens), 2(Meter) * 2(Siemens per Meter))
    }

    @Test
    fun lengthFromElectricConductanceAndElectricalConductivityTest() {
        assertEqualScientificValue(2(Meter), 4(Siemens) / 2(Siemens per Meter))
    }
}
