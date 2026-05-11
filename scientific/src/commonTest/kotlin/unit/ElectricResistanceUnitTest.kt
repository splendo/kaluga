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
class ElectricResistanceUnitTest {

    @Test
    fun electricResistanceUnitTestConversionTest() {
        assertScientificConversion("1", Ohm, "1e+9", Nanoohm)
        assertScientificConversion("1", Ohm, "1e+6", Microohm)
        assertScientificConversion("1", Ohm, "1000.0", Milliohm)
        assertScientificConversion("1", Ohm, "100.0", Centiohm)
        assertScientificConversion("1", Ohm, "10.0", Deciohm)
        assertScientificConversion("1", Ohm, "0.1", Decaohm)
        assertScientificConversion("1", Ohm, "0.01", HectoOhm)
        assertScientificConversion("1", Ohm, "0.001", Kiloohm)
        assertScientificConversion("1", Ohm, "1e-6", Megaohm)
        assertScientificConversion("1", Ohm, "1e-9", Gigaohm)
        assertScientificConversion("1", Ohm, "1000000000.0", Abohm)
    }
}
