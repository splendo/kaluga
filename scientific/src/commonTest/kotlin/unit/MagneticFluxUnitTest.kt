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

class MagneticFluxUnitTest {

    @Test
    fun luminusIntensityConversionTest() {
        assertEquals(1e+9, Weber.convert(1, Nanoweber))
        assertEquals(1e+6, Weber.convert(1, Microweber))
        assertEquals(1000.0, Weber.convert(1, Milliweber))
        assertEquals(100.0, Weber.convert(1, Centiweber))
        assertEquals(10.0, Weber.convert(1, Deciweber))
        assertEquals(0.1, Weber.convert(1, Decaweber))
        assertEquals(0.01, Weber.convert(1, Hectoweber))
        assertEquals(0.001, Weber.convert(1, Kiloweber))
        assertEquals(1e-6, Weber.convert(1, Megaweber))
        assertEquals(1e-9, Weber.convert(1, Gigaweber))
        assertEquals(100000000.0, Weber.convert(1, Maxwell))
    }
}