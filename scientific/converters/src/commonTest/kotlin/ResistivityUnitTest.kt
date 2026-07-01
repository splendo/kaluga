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

import com.splendo.kaluga.scientific.converter.electricCurrentDensity.times
import com.splendo.kaluga.scientific.converter.electricResistance.times
import com.splendo.kaluga.scientific.converter.electricalConductivity.resistivity
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.resistivity.div
import com.splendo.kaluga.scientific.converter.resistivity.electricalConductivity
import com.splendo.kaluga.scientific.converter.resistivity.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Ohm
import com.splendo.kaluga.scientific.unit.Siemens
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class ResistivityUnitTest {

    @Test
    fun resistivityFromElectricResistanceAndLengthTest() {
        assertEqualScientificValue(4(Ohm x Meter), 2(Ohm) * 2(Meter))
        assertEqualScientificValue(4(Ohm x Meter), 2(Meter) * 2(Ohm))
        assertEqualScientificValue(4(Ohm x Foot), 2(Ohm) * 2(Foot), round = 27)
    }

    @Test
    fun electricResistanceFromResistivityAndLengthTest() {
        assertEqualScientificValue(2(Ohm), 4(Ohm x Meter) / 2(Meter))
    }

    @Test
    fun lengthFromResistivityAndElectricResistanceTest() {
        assertEqualScientificValue(2(Meter), 4(Ohm x Meter) / 2(Ohm))
    }

    @Test
    fun electricalConductivityFromResistivityTest() {
        assertEqualScientificValue(0.5(Siemens per Meter), 2(Ohm x Meter).electricalConductivity())
    }

    @Test
    fun resistivityFromElectricalConductivityTest() {
        assertEqualScientificValue(0.5(Ohm x Meter), 2(Siemens per Meter).resistivity())
    }

    @Test
    fun electricFieldStrengthFromResistivityAndElectricCurrentDensityTest() {
        assertEqualScientificValue(4(Volt per Meter), 2(Ohm x Meter) * 2(Ampere per SquareMeter))
        assertEqualScientificValue(4(Volt per Meter), 2(Ampere per SquareMeter) * 2(Ohm x Meter))
    }
}
