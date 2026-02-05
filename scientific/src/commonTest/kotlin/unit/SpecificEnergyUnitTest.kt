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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.times
import kotlin.test.Test

class SpecificEnergyUnitTest {

    @Test
    fun specificEnergyConversionTest() {
        assertScientificConversion(Decimal.ONE, (Joule per Gram), Joule.convert(Decimal.ONE, WattHour) * Decimal.THOUSAND, WattHour per Kilogram, round = 32)
        assertScientificConversion("1.0", (WattHour per Pound), "2240", WattHour per ImperialTon, round = 27)
        assertScientificConversion("1.0", (WattHour per Pound), "2000", WattHour per UsTon, round = 27)
        assertScientificConversion(
            "1.0",
            (BritishThermalUnit per Pound),
            "2240",
            BritishThermalUnit per ImperialTon,
            round = 27,
        )
        assertScientificConversion(
            "1.0",
            (BritishThermalUnit per Pound),
            "2000",
            BritishThermalUnit per UsTon,
            round = 27,
        )
    }
}
