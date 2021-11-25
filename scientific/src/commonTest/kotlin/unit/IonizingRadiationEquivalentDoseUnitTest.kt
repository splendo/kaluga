/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.specificEnergy.asEquivalentDose
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class IonizingRadiationEquivalentDoseUnitTest {

    @Test
    fun ionizingRadiationEquivalentDoseConversionTest() {
        assertScientificConversion(1, Sievert, 1e+9, Nanosievert)
        assertScientificConversion(1, Sievert, 1e+6, Microsievert)
        assertScientificConversion(1, Sievert, 1000.0, Millisievert)
        assertScientificConversion(1, Sievert, 100.0, Centisievert)
        assertScientificConversion(1, Sievert, 10.0, Decisievert)
        assertScientificConversion(1, Sievert, 0.1, Decasievert)
        assertScientificConversion(1, Sievert, 0.01, Hectosievert)
        assertScientificConversion(1, Sievert, 0.001, Kilosievert)
        assertScientificConversion(1, Sievert, 1e-6, Megasievert)
        assertScientificConversion(1, Sievert, 1e-9, Gigasievert)

        assertScientificConversion(1, Sievert, 100.0, RoentgenEquivalentMan)

        assertScientificConversion(1, RoentgenEquivalentMan, 1e+9, NanoroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 1e+6, MicroroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 1000.0, MilliroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 100.0, CentiroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 10.0, DeciroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 0.1, DecaroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 0.01, HectoroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 0.001, KiloroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 1e-6, MegaroentgenEquivalentMan)
        assertScientificConversion(1, RoentgenEquivalentMan, 1e-9, GigaroentgenEquivalentMan)
    }

    @Test
    fun equivalentDoseFromEnergyAndWeightTest() {
        // assertEqualScientificValue(1(Sievert), 2(Joule) / 2(Kilogram)) FIXME
    }

    @Test
    fun specificEnergyFromEquivalentDoseTest() {
        assertEqualScientificValue(2(Sievert), 2(Joule per Kilogram).asEquivalentDose())
    }
}