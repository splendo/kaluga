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

import com.splendo.kaluga.scientific.converter.electricCapacitance.div
import com.splendo.kaluga.scientific.converter.electricFieldStrength.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.permittivity.times
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Farad
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test
import kotlin.test.assertEquals

class PermittivityUnitTest {

    @Test
    fun permittivityFromElectricCapacitanceAndLengthTest() {
        assertEqualScientificValue(1(Farad per Meter), 2(Farad) / 2(Meter))
        assertEqualScientificValue(1(Farad per Foot), 2(Farad) / 2(Foot), round = 27)
    }

    @Test
    fun electricCapacitanceFromPermittivityAndLengthTest() {
        assertEqualScientificValue(4(Farad), 2(Farad per Meter) * 2(Meter))
        assertEqualScientificValue(4(Farad), 2(Meter) * 2(Farad per Meter))
    }

    @Test
    fun surfaceChargeDensityFromPermittivityAndElectricFieldStrengthTest() {
        assertEqualScientificValue(4(Coulomb per SquareMeter), 2(Farad per Meter) * 2(Volt per Meter))
        assertEqualScientificValue(4(Coulomb per SquareMeter), 2(Volt per Meter) * 2(Farad per Meter))
        assertEqualScientificValue(2(Farad per Meter), 4(Coulomb per SquareMeter) / 2(Volt per Meter))
        assertEqualScientificValue(2(Volt per Meter), 4(Coulomb per SquareMeter) / 2(Farad per Meter))
        assertEquals((Coulomb per SquareFoot), (2(Farad per Meter) * 2(Volt per Foot)).unit)
    }
}
