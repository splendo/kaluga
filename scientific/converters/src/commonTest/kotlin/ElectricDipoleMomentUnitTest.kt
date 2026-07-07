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

import com.splendo.kaluga.scientific.converter.electricCharge.times
import com.splendo.kaluga.scientific.converter.electricDipoleMoment.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class ElectricDipoleMomentUnitTest {

    @Test
    fun electricDipoleMomentFromElectricChargeAndLengthTest() {
        assertEqualScientificValue(4(Coulomb x Meter), 2(Coulomb) * 2(Meter))
        assertEqualScientificValue(4(Coulomb x Meter), 2(Meter) * 2(Coulomb))
        assertEqualScientificValue(4(Coulomb x Foot), 2(Coulomb) * 2(Foot), round = 27)
    }

    @Test
    fun electricChargeFromElectricDipoleMomentAndLengthTest() {
        assertEqualScientificValue(2(Coulomb), 4(Coulomb x Meter) / 2(Meter))
    }

    @Test
    fun lengthFromElectricDipoleMomentAndElectricChargeTest() {
        assertEqualScientificValue(2(Meter), 4(Coulomb x Meter) / 2(Coulomb))
    }
}
