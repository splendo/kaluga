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
import com.splendo.kaluga.scientific.converter.electricCharge.div
import com.splendo.kaluga.scientific.converter.electricChargeDensity.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.div
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class SurfaceChargeDensityUnitTest {

    @Test
    fun surfaceChargeDensityFromElectricChargeAndAreaTest() {
        assertEqualScientificValue(1(Coulomb per SquareMeter), 2(Coulomb) / 2(SquareMeter))
        assertEqualScientificValue(1(Coulomb per SquareFoot), 2(Coulomb) / 2(SquareFoot), round = 27)
        assertEqualScientificValue(1(Coulomb per SquareMeter), 2(Coulomb) / 2(SquareMeter).convert(SquareFoot as Area), round = 27)
    }

    @Test
    fun electricChargeFromSurfaceChargeDensityAndAreaTest() {
        assertEqualScientificValue(4(Coulomb), 2(Coulomb per SquareMeter) * 2(SquareMeter))
        assertEqualScientificValue(4(Coulomb), 2(SquareMeter) * 2(Coulomb per SquareMeter))
        assertEqualScientificValue(4(Coulomb), 2(Coulomb per SquareFoot) * 2(SquareFoot), round = 27)
        assertEqualScientificValue(4(Coulomb), 2(SquareFoot) * 2(Coulomb per SquareFoot), round = 27)
    }

    @Test
    fun surfaceChargeDensityFromElectricChargeDensityAndLengthTest() {
        assertEqualScientificValue(4(Coulomb per SquareMeter), 2(Coulomb per CubicMeter) * 2(Meter))
        assertEqualScientificValue(4(Coulomb per SquareMeter), 2(Meter) * 2(Coulomb per CubicMeter))
        assertEqualScientificValue(4(Coulomb per SquareFoot), 2(Coulomb per CubicFoot) * 2(Foot), round = 27)
        assertEqualScientificValue(4(Coulomb per SquareFoot), 2(Foot) * 2(Coulomb per CubicFoot), round = 27)
        assertEqualScientificValue(4(Coulomb per SquareFoot), 2(Coulomb per CubicFoot.ukImperial) * 2(Foot), round = 27)
        assertEqualScientificValue(4(Coulomb per SquareFoot), 2(Foot) * 2(Coulomb per CubicFoot.ukImperial), round = 27)
        assertEqualScientificValue(4(Coulomb per SquareFoot), 2(Coulomb per CubicFoot.usCustomary) * 2(Foot), round = 27)
        assertEqualScientificValue(4(Coulomb per SquareFoot), 2(Foot) * 2(Coulomb per CubicFoot.usCustomary), round = 27)
        assertEqualScientificValue(4(Coulomb per SquareMeter), 2(Coulomb per CubicMeter) * 2(Meter).convert(Foot as Length), round = 27)
    }

    @Test
    fun electricChargeDensityFromSurfaceChargeDensityAndLengthTest() {
        assertEqualScientificValue(2(Coulomb per CubicMeter), 4(Coulomb per SquareMeter) / 2(Meter))
        assertEqualScientificValue(2(Coulomb per CubicFoot), 4(Coulomb per SquareFoot) / 2(Foot), round = 27)
        assertEqualScientificValue(2(Coulomb per CubicMeter), 4(Coulomb per SquareMeter) / 2(Meter).convert(Foot as Length), round = 27)
    }

    @Test
    fun lengthFromSurfaceChargeDensityAndElectricChargeDensityTest() {
        assertEqualScientificValue(2(Meter), 4(Coulomb per SquareMeter) / 2(Coulomb per CubicMeter))
        assertEqualScientificValue(2(Foot), 4(Coulomb per SquareFoot) / 2(Coulomb per CubicFoot), round = 27)
        assertEqualScientificValue(2(Foot), 4(Coulomb per SquareFoot) / 2(Coulomb per CubicFoot.ukImperial), round = 27)
        assertEqualScientificValue(2(Foot), 4(Coulomb per SquareFoot) / 2(Coulomb per CubicFoot.usCustomary), round = 27)
    }
}
