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

import com.splendo.kaluga.scientific.converter.energy.equivalentDoseBy
import com.splendo.kaluga.scientific.converter.specificEnergy.asEquivalentDose
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.Sievert
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class IonizingRadiationEquivalentDoseUnitTest {

    @Test
    fun equivalentDoseFromEnergyAndWeightTest() {
        assertEqualScientificValue(1(RoentgenEquivalentMan), 200(Erg) equivalentDoseBy 2(Gram))
        assertEqualScientificValue(1(RoentgenEquivalentMan), 2000(Decierg) equivalentDoseBy 2(Gram))
        assertEqualScientificValue(1(Sievert), 2(Joule) equivalentDoseBy 2(Kilogram))
    }

    @Test
    fun specificEnergyFromEquivalentDoseTest() {
        assertEqualScientificValue(2(Sievert), 2(Joule per Kilogram).asEquivalentDose())
    }
}
