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

class FrequencyUnitTest {

    @Test
    fun frequencyConversionTest() {
        assertScientificConversion("1", Hertz, "1e+9", Nanohertz)
        assertScientificConversion("1", Hertz, "1e+6", Microhertz)
        assertScientificConversion("1", Hertz, "1000.0", Millihertz)
        assertScientificConversion("1", Hertz, "100.0", Centihertz)
        assertScientificConversion("1", Hertz, "10.0", Decihertz)
        assertScientificConversion("1", Hertz, "0.1", Decahertz)
        assertScientificConversion("1", Hertz, "0.01", Hectohertz)
        assertScientificConversion("1", Hertz, "0.001", Kilohertz)
        assertScientificConversion("1", Hertz, "1e-6", Megahertz)
        assertScientificConversion("1", Hertz, "1e-9", Gigahertz)
        assertScientificConversion("1", Hertz, "60.0", BeatsPerMinute)
    }
}
