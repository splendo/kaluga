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

import com.splendo.kaluga.scientific.converter.electricCurrent.div
import com.splendo.kaluga.scientific.converter.electricCurrentDensity.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.magneticFieldStrength.div
import com.splendo.kaluga.scientific.converter.magneticFieldStrength.times
import com.splendo.kaluga.scientific.converter.speed.times
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test
import kotlin.test.assertEquals

class MagneticFieldStrengthUnitTest {

    @Test
    fun magneticFieldStrengthFromElectricCurrentAndLengthTest() {
        assertEqualScientificValue(1(Ampere per Meter), 2(Ampere) / 2(Meter))
        assertEqualScientificValue(1(Ampere per Foot), 2(Ampere) / 2(Foot), round = 27)
    }

    @Test
    fun electricCurrentFromMagneticFieldStrengthAndLengthTest() {
        assertEqualScientificValue(4(Ampere), 2(Ampere per Meter) * 2(Meter))
        assertEqualScientificValue(4(Ampere), 2(Meter) * 2(Ampere per Meter))
    }

    @Test
    fun magneticFieldStrengthFromElectricCurrentDensityAndLengthTest() {
        assertEqualScientificValue(4(Ampere per Meter), 2(Ampere per SquareMeter) * 2(Meter))
        assertEqualScientificValue(4(Ampere per Meter), 2(Meter) * 2(Ampere per SquareMeter))
        assertEqualScientificValue(2(Ampere per SquareMeter), 4(Ampere per Meter) / 2(Meter))
        assertEquals((Ampere per Foot), (2(Ampere per SquareMeter) * 2(Foot)).unit)
    }

    @Test
    fun magneticFieldStrengthFromSurfaceChargeDensityAndSpeedTest() {
        assertEqualScientificValue(4(Ampere per Meter), 2(Coulomb per SquareMeter) * 2(Meter per Second))
        assertEqualScientificValue(4(Ampere per Meter), 2(Meter per Second) * 2(Coulomb per SquareMeter))
        assertEquals((Ampere per Foot), (2(Coulomb per SquareMeter) * 2(Foot per Second)).unit)
    }
}
