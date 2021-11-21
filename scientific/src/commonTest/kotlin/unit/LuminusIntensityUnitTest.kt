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
import kotlin.test.assertEquals

class LuminusIntensityUnitTest {

    @Test
    fun luminusIntensityConversionTest() {
        assertEquals(1e+9, Candela.convert(1, Nanocandela))
        assertEquals(1e+6, Candela.convert(1, Microcandela))
        assertEquals(1000.0, Candela.convert(1, Millicandela))
        assertEquals(100.0, Candela.convert(1, Centicandela))
        assertEquals(10.0, Candela.convert(1, Decicandela))
        assertEquals(0.1, Candela.convert(1, Decacandela))
        assertEquals(0.01, Candela.convert(1, Hectocandela))
        assertEquals(0.001, Candela.convert(1, Kilocandela))
        assertEquals(1e-6, Candela.convert(1, Megacandela))
        assertEquals(1e-9, Candela.convert(1, Gigacandela))
    }
}