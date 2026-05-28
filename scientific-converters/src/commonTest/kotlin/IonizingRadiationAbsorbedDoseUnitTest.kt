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

import com.splendo.kaluga.scientific.converter.energy.absorbedBy
import com.splendo.kaluga.scientific.converter.specificEnergy.asAbsorbedDose
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.Gray
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test
import kotlin.test.assertEquals
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class IonizingRadiationAbsorbedDoseUnitTest {

    @Test
    fun absorbedDoseFromEnergyAndWeightTest() {
        assertEquals(1(Rad), 200(Erg) absorbedBy 2(Gram))
        assertEquals(1(Rad), 2000(Decierg) absorbedBy 2(Gram))
        assertEquals(1(Gray), 2(Joule) absorbedBy 2(Kilogram))
    }

    @Test
    fun specificEnergyFromAbsorbedDoseTest() {
        assertEquals(2(Gray), 2(Joule per Kilogram).asAbsorbedDose())
    }
}
