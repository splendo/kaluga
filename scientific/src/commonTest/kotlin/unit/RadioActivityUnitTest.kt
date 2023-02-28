/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.decimal.decaysPer
import com.splendo.kaluga.scientific.converter.radioactivity.radioactivity
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test

class RadioActivityUnitTest {

    @Test
    fun radioActivityConversionTest() {
        assertScientificConversion(1, Becquerel, 1e+9, Nanobecquerel)
        assertScientificConversion(1, Becquerel, 1e+6, Microbecquerel)
        assertScientificConversion(1, Becquerel, 1000.0, Millibecquerel)
        assertScientificConversion(1, Becquerel, 100.0, Centibecquerel)
        assertScientificConversion(1, Becquerel, 10.0, Decibecquerel)
        assertScientificConversion(1, Becquerel, 0.1, Decabecquerel)
        assertScientificConversion(1, Becquerel, 0.01, Hectobecquerel)
        assertScientificConversion(1, Becquerel, 0.001, Kilobecquerel)
        assertScientificConversion(1, Becquerel, 1e-6, Megabecquerel)
        assertScientificConversion(1, Becquerel, 1e-9, Gigabecquerel)
        assertScientificConversion(1, Becquerel, 2.7027027027027E-11, Curie, 24)

        assertScientificConversion(1, Curie, 1e+9, Nanocurie)
        assertScientificConversion(1, Curie, 1e+6, Microcurie)
        assertScientificConversion(1, Curie, 1000.0, Millicurie)
        assertScientificConversion(1, Curie, 100.0, Centicurie)
        assertScientificConversion(1, Curie, 10.0, Decicurie)
        assertScientificConversion(1, Curie, 0.1, Decacurie)
        assertScientificConversion(1, Curie, 0.01, Hectocurie)
        assertScientificConversion(1, Curie, 0.001, Kilocurie)
        assertScientificConversion(1, Curie, 1e-6, Megacurie)
        assertScientificConversion(1, Curie, 1e-9, Gigacurie)
    }

    @Test
    fun radioActivityFromAmountOfSubstanceDivTimeTest() {
        assertEqualScientificValue(2087.1149(Becquerel), Becquerel.radioactivity(2e-10(Mole), 4e10(Second)), 4)
    }

    @Test
    fun radioActivityFromTimeTest() {
        assertEqualScientificValue(1(Becquerel), 2 decaysPer 2(Second))
        assertEqualScientificValue(1(Becquerel), 2.toDecimal() decaysPer 2(Second))
    }
}
