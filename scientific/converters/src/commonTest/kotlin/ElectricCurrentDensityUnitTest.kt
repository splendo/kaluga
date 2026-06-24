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
import com.splendo.kaluga.scientific.converter.electricChargeDensity.times
import com.splendo.kaluga.scientific.converter.electricCurrent.div
import com.splendo.kaluga.scientific.converter.electricCurrentDensity.div
import com.splendo.kaluga.scientific.converter.electricCurrentDensity.times
import com.splendo.kaluga.scientific.converter.frequency.times
import com.splendo.kaluga.scientific.converter.speed.times
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.div
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Hertz
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricCurrentDensityUnitTest {

    @Test
    fun electricCurrentDensityFromElectricCurrentAndAreaTest() {
        assertEqualScientificValue(1(Ampere per SquareMeter), 2(Ampere) / 2(SquareMeter))
        assertEqualScientificValue(1(Ampere per SquareFoot), 2(Ampere) / 2(SquareFoot), round = 27)
    }

    @Test
    fun electricCurrentFromElectricCurrentDensityAndAreaTest() {
        assertEqualScientificValue(4(Ampere), 2(Ampere per SquareMeter) * 2(SquareMeter))
        assertEqualScientificValue(4(Ampere), 2(SquareMeter) * 2(Ampere per SquareMeter))
    }

    @Test
    fun surfaceChargeDensityFromElectricCurrentDensityAndTimeTest() {
        assertEqualScientificValue(4(Coulomb per SquareMeter), 2(Ampere per SquareMeter) * 2(Second))
        assertEqualScientificValue(4(Coulomb per SquareMeter), 2(Second) * 2(Ampere per SquareMeter))
        assertEqualScientificValue(2(Ampere per SquareMeter), 4(Coulomb per SquareMeter) / 2(Second))
    }

    @Test
    fun electricCurrentDensityFromSurfaceChargeDensityAndFrequencyTest() {
        assertEqualScientificValue(4(Ampere per SquareMeter), 2(Coulomb per SquareMeter) * 2(Hertz))
        assertEqualScientificValue(4(Ampere per SquareMeter), 2(Hertz) * 2(Coulomb per SquareMeter))
        assertEqualScientificValue(2(Coulomb per SquareMeter), 4(Ampere per SquareMeter) / 2(Hertz))
        assertEqualScientificValue(2(Hertz), 4(Ampere per SquareMeter) / 2(Coulomb per SquareMeter))
    }

    @Test
    fun imperialElectricCurrentDensityCrossConvertersPreserveSystem() {
        assertEqualScientificValue(4(Coulomb per SquareFoot), 2(Ampere per SquareFoot) * 2(Second))
        assertEqualScientificValue(2(Ampere per SquareFoot), 4(Coulomb per SquareFoot) / 2(Second))
        assertEqualScientificValue(4(Ampere per SquareFoot), 2(Coulomb per SquareFoot) * 2(Hertz))
        assertEqualScientificValue(2(Coulomb per SquareFoot), 4(Ampere per SquareFoot) / 2(Hertz))
    }

    @Test
    fun electricCurrentDensityFromElectricChargeDensityAndSpeedTest() {
        assertEqualScientificValue(4(Ampere per SquareMeter), 2(Coulomb per CubicMeter) * 2(Meter per Second))
        assertEqualScientificValue(4(Ampere per SquareMeter), 2(Meter per Second) * 2(Coulomb per CubicMeter))
        assertEqualScientificValue(2(Coulomb per CubicMeter), 4(Ampere per SquareMeter) / 2(Meter per Second))
        assertEqualScientificValue(2(Meter per Second), 4(Ampere per SquareMeter) / 2(Coulomb per CubicMeter))
    }

    @Test
    fun electricCurrentDensityRel7PreservesImperialSystem() {
        assertEquals((Ampere per SquareFoot), (2(Coulomb per CubicMeter) * 2(Foot per Second)).unit)
        assertEquals((Ampere per SquareFoot), (2(Foot per Second) * 2(Coulomb per CubicMeter)).unit)
        assertEquals((Coulomb per CubicFoot), (2(Ampere per SquareFoot) / 2(Meter per Second)).unit)
        assertEquals((Foot per Second), (2(Ampere per SquareFoot) / 2(Coulomb per CubicMeter)).unit)
    }
}
