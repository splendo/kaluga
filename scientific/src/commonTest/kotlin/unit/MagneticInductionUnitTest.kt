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

class MagneticInductionUnitTest {

    @Test
    fun magneticInductionConversionTest() {
        assertEquals(1e+9, Tesla.convert(1, Nanotesla))
        assertEquals(1e+6, Tesla.convert(1, Microtesla))
        assertEquals(1000.0, Tesla.convert(1, Millitesla))
        assertEquals(100.0, Tesla.convert(1, Centitesla))
        assertEquals(10.0, Tesla.convert(1, Decitesla))
        assertEquals(0.1, Tesla.convert(1, Decatesla))
        assertEquals(0.01, Tesla.convert(1, Hectotesla))
        assertEquals(0.001, Tesla.convert(1, Kilotesla))
        assertEquals(1e-6, Tesla.convert(1, Megatesla))
        assertEquals(1e-9, Tesla.convert(1, Gigatesla))
        assertEquals(10000.0, Tesla.convert(1, Gauss))
    }
}