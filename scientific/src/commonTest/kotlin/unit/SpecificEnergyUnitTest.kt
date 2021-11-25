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

import kotlin.test.Test

class SpecificEnergyUnitTest {

    @Test
    fun specificEnergyConversionTest() {
        assertScientificConversion(1.0, (Joule per Gram), 0.27778, WattHour per Kilogram, 5)
        assertScientificConversion(1.0, (WattHour per Pound), 2240, WattHour per ImperialTon)
        assertScientificConversion(1.0, (WattHour per Pound), 2000, WattHour per UsTon)
        assertScientificConversion(1.0, (BritishThermalUnit per Pound), 2240, BritishThermalUnit per ImperialTon)
        assertScientificConversion(1.0, (BritishThermalUnit per Pound), 2000, BritishThermalUnit per UsTon)
    }
}
