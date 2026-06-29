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

package com.splendo.kaluga.scientific.unit

import kotlin.test.Test

class ElectricChargeUnitTest {

    @Test
    fun electricChargeConversionTest() {
        assertScientificConversion("1", Coulomb, "1e+9", Nanocoulomb)
        assertScientificConversion("1", Coulomb, "1e+6", Microcoulomb)
        assertScientificConversion("1", Coulomb, "1000.0", Millicoulomb)
        assertScientificConversion("1", Coulomb, "100.0", Centicoulomb)
        assertScientificConversion("1", Coulomb, "10.0", Decicoulomb)
        assertScientificConversion("1", Coulomb, "0.1", Decacoulomb)
        assertScientificConversion("1", Coulomb, "0.01", Hectocoulomb)
        assertScientificConversion("1", Coulomb, "0.001", Kilocoulomb)
        assertScientificConversion("1", Coulomb, "1e-6", Megacoulomb)
        assertScientificConversion("1", Coulomb, "1e-9", Gigacoulomb)
        assertScientificConversion("1", Coulomb, "0.1", Abcoulomb)
    }
}
