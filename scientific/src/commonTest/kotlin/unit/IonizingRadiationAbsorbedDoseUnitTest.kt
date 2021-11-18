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

class IonizingRadiationAbsorbedDoseUnitTest {
    
    @Test
    fun ionizingRadiationAbsorbedDoseConversionTest() {
        assertEquals(1e+9, Gray.convert(1, Nanogray))
        assertEquals(1e+6, Gray.convert(1, Microgray))
        assertEquals(1000.0, Gray.convert(1, Milligray))
        assertEquals(100.0, Gray.convert(1, Centigray))
        assertEquals(10.0, Gray.convert(1, Decigray))
        assertEquals(0.1, Gray.convert(1, Decagray))
        assertEquals(0.01, Gray.convert(1, Hectogray))
        assertEquals(0.001, Gray.convert(1, Kilogray))
        assertEquals(1e-6, Gray.convert(1, Megagray))
        assertEquals(1e-9, Gray.convert(1, Gigagray))

        assertEquals(100.0, Gray.convert(1, Rad))

        assertEquals(1e+9, Rad.convert(1, Nanorad))
        assertEquals(1e+6, Rad.convert(1, Microrad))
        assertEquals(1000.0, Rad.convert(1, Millirad))
        assertEquals(100.0, Rad.convert(1, Centirad))
        assertEquals(10.0, Rad.convert(1, Decirad))
        assertEquals(0.1, Rad.convert(1, Decarad))
        assertEquals(0.01, Rad.convert(1, Hectorad))
        assertEquals(0.001, Rad.convert(1, Kilorad))
        assertEquals(1e-6, Rad.convert(1, Megarad))
        assertEquals(1e-9, Rad.convert(1, Gigarad))
    }
}