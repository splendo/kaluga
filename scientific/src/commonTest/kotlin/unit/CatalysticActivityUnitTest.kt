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

class CatalysticActivityUnitTest {

    @Test
    fun catalysticActivityUnitTestConversionTest() {
        assertEquals(1e+9, Katal.convert(1, Nanokatal))
        assertEquals(1e+6, Katal.convert(1, Microkatal))
        assertEquals(1000.0, Katal.convert(1, Millikatal))
        assertEquals(100.0, Katal.convert(1, Centikatal))
        assertEquals(10.0, Katal.convert(1, Decikatal))
        assertEquals(0.1, Katal.convert(1, Decakatal))
        assertEquals(0.01, Katal.convert(1, Hectokatal))
        assertEquals(0.001, Katal.convert(1, Kilokatal))
        assertEquals(0.000001, Katal.convert(1, Megakatal))
        assertEquals(0.000000001, Katal.convert(1, Gigakatal))
    }
}