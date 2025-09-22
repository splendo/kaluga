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

class VoltageUnitTest {

    @Test
    fun voltConversionTest() {
        assertScientificConversion("1.0", Volt, "1e+9", Nanovolt)
        assertScientificConversion("1.0", Volt, "1e+8", Abvolt)
        assertScientificConversion("1.0", Volt, "1e+6", Microvolt)
        assertScientificConversion("1.0", Volt, "1000.0", Millivolt)
        assertScientificConversion("1.0", Volt, "100.0", Centivolt)
        assertScientificConversion("1.0", Volt, "10.0", Decivolt)
        assertScientificConversion("1.0", Volt, "0.1", Decavolt)
        assertScientificConversion("1.0", Volt, "0.01", Hectovolt)
        assertScientificConversion("1.0", Volt, "0.001", Kilovolt)
        assertScientificConversion("1.0", Volt, "1e-6", Megavolt)
        assertScientificConversion("1.0", Volt, "1e-9", Gigavolt)
    }
}
