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
import com.splendo.kaluga.scientific.converter.electricCharge.times
import com.splendo.kaluga.scientific.converter.electricFieldStrength.times
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.voltage.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class ElectricFieldStrengthUnitTest {

    @Test
    fun electricFieldStrengthFromVoltageAndLengthTest() {
        assertEqualScientificValue(1(Volt per Meter), 2(Volt) / 2(Meter))
        assertEqualScientificValue(1(Volt per Foot), 2(Volt) / 2(Foot), round = 27)
        assertEqualScientificValue(1(Volt per Meter), 2(Volt) / 2(Meter).convert(Foot as Length), round = 27)
    }

    @Test
    fun voltageFromElectricFieldStrengthAndLengthTest() {
        assertEqualScientificValue(4(Volt), 2(Volt per Meter) * 2(Meter))
        assertEqualScientificValue(4(Volt), 2(Meter) * 2(Volt per Meter))
        assertEqualScientificValue(4(Volt), 2(Volt per Foot) * 2(Foot), round = 27)
        assertEqualScientificValue(4(Volt), 2(Foot) * 2(Volt per Foot), round = 27)
    }

    @Test
    fun forceFromElectricFieldStrengthAndElectricChargeTest() {
        assertEqualScientificValue(4(Newton), 2(Volt per Meter) * 2(Coulomb))
        assertEqualScientificValue(4(Newton), 2(Coulomb) * 2(Volt per Meter))
        // imperial ElectricFieldStrength -> PoundForce (value verified against the metric-equivalent converted)
        assertEqualScientificValue(4(Newton).convert(PoundForce), 2(Volt per Meter).convert(Volt per Foot) * 2(Coulomb), round = 27)
        assertEqualScientificValue(4(Newton).convert(PoundForce), 2(Coulomb) * 2(Volt per Meter).convert(Volt per Foot), round = 27)
    }

    @Test
    fun electricFieldStrengthFromForceAndElectricChargeTest() {
        assertEqualScientificValue(2(Volt per Meter), 4(Newton) / 2(Coulomb))
        assertEqualScientificValue(2(Volt per Meter).convert(Volt per Foot), 4(Newton).convert(PoundForce) / 2(Coulomb), round = 27)
    }

    @Test
    fun electricChargeFromForceAndElectricFieldStrengthTest() {
        assertEqualScientificValue(2(Coulomb), 4(Newton) / 2(Volt per Meter))
        assertEqualScientificValue(2(Coulomb), 4(Newton).convert(PoundForce) / 2(Volt per Meter).convert(Volt per Foot), round = 27)
    }
}
