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
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class LuminusFluxUnitTest {

    @Test
    fun luminusFluxConversionTest() {
        assertScientificConversion("1", Lumen, "1e+9", Nanolumen)
        assertScientificConversion("1", Lumen, "1e+6", Microlumen)
        assertScientificConversion("1", Lumen, "1000.0", Millilumen)
        assertScientificConversion("1", Lumen, "100.0", Centilumen)
        assertScientificConversion("1", Lumen, "10.0", Decilumen)
        assertScientificConversion("1", Lumen, "0.1", Decalumen)
        assertScientificConversion("1", Lumen, "0.01", Hectolumen)
        assertScientificConversion("1", Lumen, "0.001", Kilolumen)
        assertScientificConversion("1", Lumen, "1e-6", Megalumen)
        assertScientificConversion("1", Lumen, "1e-9", Gigalumen)
    }
}
