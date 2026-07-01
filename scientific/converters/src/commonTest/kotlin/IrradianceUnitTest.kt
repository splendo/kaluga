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
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.irradiance.times
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.imperial
import com.splendo.kaluga.scientific.unit.metric
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class IrradianceUnitTest {

    @Test
    fun irradianceFromPowerAndAreaTest() {
        assertEqualScientificValue(2(Watt per SquareMeter), 4(Watt) / 2(SquareMeter))
        assertEqualScientificValue(2(Watt per SquareFoot), 4(Watt) / 2(SquareFoot), round = 27)
        assertEqualScientificValue(2(Watt per SquareMeter), 4(Watt) / 2(SquareMeter).convert(SquareFoot as Area), round = 27)
    }

    @Test
    fun powerFromIrradianceAndAreaTest() {
        assertEqualScientificValue(4(Watt.metric), 2(Watt per SquareMeter) * 2(SquareMeter))
        assertEqualScientificValue(4(Watt.metric), 2(SquareMeter) * 2(Watt per SquareMeter))
        assertEqualScientificValue(4(Watt.imperial), 2(Watt per SquareFoot) * 2(SquareFoot), round = 27)
        assertEqualScientificValue(4(Watt.imperial), 2(SquareFoot) * 2(Watt per SquareFoot), round = 27)
    }
}
