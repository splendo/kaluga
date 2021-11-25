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

import com.splendo.kaluga.scientific.converter.specificEnergy.asAbsorbedDose
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class IonizingRadiationAbsorbedDoseUnitTest {

    @Test
    fun ionizingRadiationAbsorbedDoseConversionTest() {
        assertScientificConversion(1, Gray, 1e+9, Nanogray)
        assertScientificConversion(1, Gray, 1e+6, Microgray)
        assertScientificConversion(1, Gray, 1000.0, Milligray)
        assertScientificConversion(1, Gray, 100.0, Centigray)
        assertScientificConversion(1, Gray, 10.0, Decigray)
        assertScientificConversion(1, Gray, 0.1, Decagray)
        assertScientificConversion(1, Gray, 0.01, Hectogray)
        assertScientificConversion(1, Gray, 0.001, Kilogray)
        assertScientificConversion(1, Gray, 1e-6, Megagray)
        assertScientificConversion(1, Gray, 1e-9, Gigagray)

        assertScientificConversion(1, Gray, 100.0, Rad)

        assertScientificConversion(1, Rad, 1e+9, Nanorad)
        assertScientificConversion(1, Rad, 1e+6, Microrad)
        assertScientificConversion(1, Rad, 1000.0, Millirad)
        assertScientificConversion(1, Rad, 100.0, Centirad)
        assertScientificConversion(1, Rad, 10.0, Decirad)
        assertScientificConversion(1, Rad, 0.1, Decarad)
        assertScientificConversion(1, Rad, 0.01, Hectorad)
        assertScientificConversion(1, Rad, 0.001, Kilorad)
        assertScientificConversion(1, Rad, 1e-6, Megarad)
        assertScientificConversion(1, Rad, 1e-9, Gigarad)
    }

    @Test
    fun absorbedDoseFromEnergyAndWeightTest() {
        // assertEqualScientificValue(1(Rad),2(Joule) / 2(Kilogram)) FIXME
    }

    @Test
    fun specificEnergyFromAbsorbedDoseTest() {
        assertEquals(2(Gray),2(Joule per Kilogram).asAbsorbedDose())
    }
}