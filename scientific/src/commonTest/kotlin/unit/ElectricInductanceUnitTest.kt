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

class ElectricInductanceUnitTest {

    @Test
    fun electricInductanceConversionTest() {
        assertEquals(1e+9, Henry.convert(1, Nanohenry))
        assertEquals(1e+6, Henry.convert(1, Microhenry))
        assertEquals(1000.0, Henry.convert(1, Millihenry))
        assertEquals(100.0, Henry.convert(1, Centihenry))
        assertEquals(10.0, Henry.convert(1, Decihenry))
        assertEquals(0.1, Henry.convert(1, Decahenry))
        assertEquals(0.01, Henry.convert(1, Hectohenry))
        assertEquals(0.001, Henry.convert(1, Kilohenry))
        assertEquals(1e-6, Henry.convert(1, Megahenry))
        assertEquals(1e-9, Henry.convert(1, Gigahenry))
        assertEquals(1000000000.0, Henry.convert(1, Abhenry))
    }
}