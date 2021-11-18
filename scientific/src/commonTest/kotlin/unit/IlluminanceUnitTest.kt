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

class IlluminanceUnitTest {

    @Test
    fun illuminanceConversionTest() {
        assertEquals(1e+9, Lux.convert(1, Nanolux))
        assertEquals(1e+6, Lux.convert(1, Microlux))
        assertEquals(1000.0, Lux.convert(1, Millilux))
        assertEquals(100.0, Lux.convert(1, Centilux))
        assertEquals(10.0, Lux.convert(1, Decilux))
        assertEquals(0.1, Lux.convert(1, Decalux))
        assertEquals(0.01, Lux.convert(1, Hectolux))
        assertEquals(0.001, Lux.convert(1, Kilolux))
        assertEquals(1e-6, Lux.convert(1, Megalux))
        assertEquals(1e-9, Lux.convert(1, Gigalux))

        assertEquals(0.0001, Lux.convert(1, Phot))
        assertEquals(0.092903, Lux.convert(1, FootCandle,6))

        assertEquals(1e+9, Phot.convert(1, Nanophot))
        assertEquals(1e+6, Phot.convert(1, Microphot))
        assertEquals(1000.0, Phot.convert(1, Milliphot))
        assertEquals(100.0, Phot.convert(1, Centiphot))
        assertEquals(10.0, Phot.convert(1, Deciphot))
        assertEquals(0.1, Phot.convert(1, Decaphot))
        assertEquals(0.01, Phot.convert(1, Hectophot))
        assertEquals(0.001, Phot.convert(1, Kilophot))
        assertEquals(1e-6, Phot.convert(1, Megaphot))
        assertEquals(1e-9, Phot.convert(1, Gigaphot))

        assertEquals(929.0304, Phot.convert(1, FootCandle,4))

    }
}