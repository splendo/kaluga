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

class ElectricConductanceUnitTest {

    @Test
    fun electricConductanceConversionTest() {
        assertEquals(1e+9, Siemens.convert(1, Nanosiemens))
        assertEquals(1e+6, Siemens.convert(1, Microsiemens))
        assertEquals(1000.0, Siemens.convert(1, Millisiemens))
        assertEquals(100.0, Siemens.convert(1, Centisiemens))
        assertEquals(10.0, Siemens.convert(1, Decisiemens))
        assertEquals(0.1, Siemens.convert(1, Decasiemens))
        assertEquals(0.01, Siemens.convert(1, Hectosiemens))
        assertEquals(0.001, Siemens.convert(1, Kilosiemens))
        assertEquals(1e-6, Siemens.convert(1, Megasiemens))
        assertEquals(1e-9, Siemens.convert(1, Gigasiemens))
        assertEquals(1e-9, Siemens.convert(1, Absiemens))
    }
}