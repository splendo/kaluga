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

import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.converter.magneticDipoleMoment.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class MagneticDipoleMomentUnitTest {

    @Test
    fun magneticDipoleMomentFromElectricCurrentAndAreaTest() {
        assertEqualScientificValue(4(Ampere x SquareMeter), 2(Ampere) * 2(SquareMeter))
        assertEqualScientificValue(4(Ampere x SquareMeter), 2(SquareMeter) * 2(Ampere))
        assertEqualScientificValue(4(Ampere x SquareFoot), 2(Ampere) * 2(SquareFoot), round = 27)
    }

    @Test
    fun electricCurrentFromMagneticDipoleMomentAndAreaTest() {
        assertEqualScientificValue(2(Ampere), 4(Ampere x SquareMeter) / 2(SquareMeter))
    }

    @Test
    fun areaFromMagneticDipoleMomentAndElectricCurrentTest() {
        assertEqualScientificValue(2(SquareMeter), 4(Ampere x SquareMeter) / 2(Ampere))
    }
}
