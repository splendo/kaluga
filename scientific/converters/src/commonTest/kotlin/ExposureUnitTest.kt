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
import com.splendo.kaluga.scientific.converter.electricCharge.div
import com.splendo.kaluga.scientific.converter.exposure.times
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class ExposureUnitTest {

    @Test
    fun exposureFromElectricChargeAndWeightTest() {
        assertEqualScientificValue(1(Coulomb per Kilogram), 2(Coulomb) / 2(Kilogram))
        assertEqualScientificValue(1(Coulomb per Pound), 2(Coulomb) / 2(Pound), round = 27)
        assertEqualScientificValue(1(Coulomb per Pound.ukImperial), 2(Coulomb) / 2(Pound.ukImperial), round = 27)
        assertEqualScientificValue(1(Coulomb per Pound.usCustomary), 2(Coulomb) / 2(Pound.usCustomary), round = 27)
        assertEqualScientificValue(1(Coulomb per Kilogram), 2(Coulomb) / 2(Kilogram).convert(Pound as Weight), round = 27)
    }

    @Test
    fun electricChargeFromExposureAndWeightTest() {
        assertEqualScientificValue(4(Coulomb), 2(Coulomb per Kilogram) * 2(Kilogram))
        assertEqualScientificValue(4(Coulomb), 2(Kilogram) * 2(Coulomb per Kilogram))
        assertEqualScientificValue(4(Coulomb), 2(Coulomb per Pound) * 2(Pound), round = 27)
        assertEqualScientificValue(4(Coulomb), 2(Pound) * 2(Coulomb per Pound), round = 27)
        assertEqualScientificValue(4(Coulomb), 2(Coulomb per Pound.ukImperial) * 2(Pound.ukImperial), round = 27)
        assertEqualScientificValue(4(Coulomb), 2(Pound.ukImperial) * 2(Coulomb per Pound.ukImperial), round = 27)
        assertEqualScientificValue(4(Coulomb), 2(Coulomb per Pound.usCustomary) * 2(Pound.usCustomary), round = 27)
        assertEqualScientificValue(4(Coulomb), 2(Pound.usCustomary) * 2(Coulomb per Pound.usCustomary), round = 27)
    }
}
