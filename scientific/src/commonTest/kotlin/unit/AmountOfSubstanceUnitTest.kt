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

import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class AmountOfSubstanceUnitTest {

    @Test
    fun amountOfSubstanceConversionTest() {
        assertEquals(1e+9, Mole.convert(1, Nanomole))
        assertEquals(1000000.0, Mole.convert(1, Micromole))
        assertEquals(1000.0, Mole.convert(1, Millimole))
        assertEquals(100.0, Mole.convert(1, Centimole))
        assertEquals(10.0, Mole.convert(1, Decimole))
        assertEquals(0.1, Mole.convert(1, Decamole))
        assertEquals(0.01, Mole.convert(1, Hectomole))
        assertEquals(0.001, Mole.convert(1, Kilomole))
        assertEquals(1e-6, Mole.convert(1, Megamole))
        assertEquals(1e-9, Mole.convert(1, Gigamole))
    }

    @Test
    fun amountOfSubstanceFromCatalysisAndTimeTest() {
        assertEquals(1(Meter per Second per Second), 2(Newton) / 2(Kilogram))
        assertEquals(
            32.17(Foot per Second per Second).decimalValue,
            (2(PoundForce) / 2(Pound)).decimalValue.round(2)
        )
        assertEquals(
            9.80665(Meter per Second per Second).decimalValue,
            (2(PoundForce) / 2(Pound)).convert(Meter per Second per Second).decimalValue.round(5)
        )
    }
}