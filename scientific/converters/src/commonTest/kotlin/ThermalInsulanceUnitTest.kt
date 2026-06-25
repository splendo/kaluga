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
import com.splendo.kaluga.scientific.converter.thermalInsulance.div
import com.splendo.kaluga.scientific.converter.thermalResistance.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.metric
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class ThermalInsulanceUnitTest {

    @Test
    fun thermalInsulanceFromThermalResistanceAndAreaTest() {
        assertEqualScientificValue(4((Kelvin per Watt) x SquareMeter), 2(Kelvin per Watt) * 2(SquareMeter))
        assertEqualScientificValue(4((Kelvin per Watt) x SquareMeter), 2(SquareMeter) * 2(Kelvin per Watt))
    }

    @Test
    fun thermalResistanceFromThermalInsulanceAndAreaTest() {
        assertEqualScientificValue(2(Kelvin per Watt.metric), 4((Kelvin per Watt) x SquareMeter) / 2(SquareMeter))
    }

    @Test
    fun areaFromThermalInsulanceAndThermalResistanceTest() {
        assertEqualScientificValue(2(SquareMeter), 4((Kelvin per Watt) x SquareMeter) / 2(Kelvin per Watt))
    }
}
