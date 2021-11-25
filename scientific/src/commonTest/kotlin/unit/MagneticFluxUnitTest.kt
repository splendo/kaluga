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
        assertScientificConversion(1, Weber, 1e+9, Nanoweber)
        assertScientificConversion(1, Weber, 1e+6, Microweber)
        assertScientificConversion(1, Weber, 1000.0, Milliweber)
        assertScientificConversion(1, Weber, 100.0, Centiweber)
        assertScientificConversion(1, Weber, 10.0, Deciweber)
        assertScientificConversion(1, Weber, 0.1, Decaweber)
        assertScientificConversion(1, Weber, 0.01, Hectoweber)
        assertScientificConversion(1, Weber, 0.001, Kiloweber)
        assertScientificConversion(1, Weber, 1e-6, Megaweber)
        assertScientificConversion(1, Weber, 1e-9, Gigaweber)
        assertScientificConversion(1, Weber, 100000000.0, Maxwell)
    }
}