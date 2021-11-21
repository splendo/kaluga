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

class LuminusFluxUnitTest {

    @Test
    fun luminusFluxConversionTest() {
        assertEquals(1e+9, Lumen.convert(1, Nanolumen))
        assertEquals(1e+6, Lumen.convert(1, Microlumen))
        assertEquals(1000.0, Lumen.convert(1, Millilumen))
        assertEquals(100.0, Lumen.convert(1, Centilumen))
        assertEquals(10.0, Lumen.convert(1, Decilumen))
        assertEquals(0.1, Lumen.convert(1, Decalumen))
        assertEquals(0.01, Lumen.convert(1, Hectolumen))
        assertEquals(0.001, Lumen.convert(1, Kilolumen))
        assertEquals(1e-6, Lumen.convert(1, Megalumen))
        assertEquals(1e-9, Lumen.convert(1, Gigalumen))
    }
}