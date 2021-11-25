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

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MagneticInductionUnitTest {

    @Test
    fun magneticInductionConversionTest() {
        assertScientificConversion(1, Tesla, 1e+9, Nanotesla)
        assertScientificConversion(1, Tesla, 1e+6, Microtesla)
        assertScientificConversion(1, Tesla, 1000.0, Millitesla)
        assertScientificConversion(1, Tesla, 100.0, Centitesla)
        assertScientificConversion(1, Tesla, 10.0, Decitesla)
        assertScientificConversion(1, Tesla, 0.1, Decatesla)
        assertScientificConversion(1, Tesla, 0.01, Hectotesla)
        assertScientificConversion(1, Tesla, 0.001, Kilotesla)
        assertScientificConversion(1, Tesla, 1e-6, Megatesla)
        assertScientificConversion(1, Tesla, 1e-9, Gigatesla)
        assertScientificConversion(1, Tesla, 10000.0, Gauss)
    }

    @Test
    fun inductionFromFluxAndAreaTest() {
        assertEquals(1(Tesla), 2(Weber) / 2(SquareMeter))
        assertEqualScientificValue(10.76(Tesla), 2(Weber) / 2(SquareFoot), 2)
    }
}
