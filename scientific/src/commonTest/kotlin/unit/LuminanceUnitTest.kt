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

class LuminanceUnitTest {
    
    @Test
    fun luminanceConversionTest() {
        assertEquals(1e+9, Nit.convert(1, Nanonit))
        assertEquals(1e+6, Nit.convert(1, Micronit))
        assertEquals(1000.0, Nit.convert(1, Millinit))
        assertEquals(100.0, Nit.convert(1, Centinit))
        assertEquals(10.0, Nit.convert(1, Decinit))
        assertEquals(0.1, Nit.convert(1, Decanit))
        assertEquals(0.01, Nit.convert(1, Hectonit))
        assertEquals(0.001, Nit.convert(1, Kilonit))
        assertEquals(1e-6, Nit.convert(1, Meganit))
        assertEquals(1e-9, Nit.convert(1, Giganit))

        assertEquals(3.14159265359, Nit.convert(1, Apostilb,11))
        assertEquals(0.0003141593, Nit.convert(1, Lambert,10))
        assertEquals(3141.5927, Nit.convert(1, Skot,4))
        assertEquals(31415926.536, Nit.convert(1, Bril,3))

        assertEquals(31415.9265, Stilb.convert(1, Apostilb,4))
        assertEquals( 3.1415926535898, Stilb.convert(1, Lambert,13))
        assertEquals(31415926.536, Stilb.convert(1, Skot,3))
        assertEquals(314159265359.0, Stilb.convert(1, Bril,0))

        assertEquals(0.0001, Apostilb.convert(1, Lambert))
        assertEquals(1000.0, Apostilb.convert(1, Skot))
        assertEquals(10000000.0, Apostilb.convert(1, Bril))

        assertEquals(10000.0, Skot.convert(1, Bril))
    }
}