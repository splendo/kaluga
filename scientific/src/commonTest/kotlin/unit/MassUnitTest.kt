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
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MassUnitTest {

    // ##### Same mass unit table conversions #####

    @Test
    fun kilogramConversionTest() {
        assertScientificConversion(1.0, Kilogram, 1e+12, Nanogram)
        assertScientificConversion(1.0, Kilogram, 1e+9, Microgram)
        assertScientificConversion(1.0, Kilogram, 1e+6, Milligram)
        assertScientificConversion(1.0, Kilogram, 100_000.0, Centigram)
        assertScientificConversion(1.0, Kilogram, 10_000.0, Decigram)
        assertScientificConversion(1.0, Kilogram, 1_000.0, Gram)
        assertScientificConversion(1.0, Kilogram, 100.0, Decagram)
        assertScientificConversion(1.0, Kilogram, 10.0, Hectogram)
        assertScientificConversion(1.0, Kilogram, 0.001, Megagram)
        assertScientificConversion(1.0, Kilogram, 0.001, Tonne)
        assertScientificConversion(1.0, Kilogram, 1e-6, Gigagram)

        assertScientificConversion(1.0, Kilogram, 6.02214076e+26, Dalton)
        assertScientificConversion(1.0, Kilogram, 2.20462262, Pound, 8)
    }

    @Test
    fun daltonConversionTest() {
        assertScientificConversion(1.0, Dalton, 1e+9, Nanodalton)
        assertScientificConversion(1.0, Dalton, 1e+6, Microdalton)
        assertScientificConversion(1.0, Dalton, 1_000.0, Millidalton)
        assertScientificConversion(1.0, Dalton, 100.0, Centidalton)
        assertScientificConversion(1.0, Dalton, 10.0, Decidalton)
        assertScientificConversion(1.0, Dalton, 0.1, Decadalton)
        assertScientificConversion(1.0, Dalton, 0.01, HectoDalton)
        assertScientificConversion(1.0, Dalton, 0.001, Kilodalton)
        assertScientificConversion(1.0, Dalton, 1e-6, Megadalton)
        assertScientificConversion(1.0, Dalton, 1e-9, Gigadalton)
    }

    @Test
    fun poundConversionTest() {
        assertScientificConversion(1.0, Pound, 7_000.0, Grain)
        assertScientificConversion(1.0, Pound, 256.0, Dram)
        assertScientificConversion(1.0, Pound, 16.0, Ounce)
        assertScientificConversion(1.0, Pound, 0.07142857, Stone, 8)
        assertScientificConversion(1.0, Pound, 0.03108095, Slug, 8)

        // uk ton
        assertScientificConversion(1.0, Pound, 0.00044643, ImperialTon, 8)
        // us ton
        assertScientificConversion(1.0, Pound, 0.0005, UsTon)
    }

    @Test
    fun massFromAmountOfSubstanceDivMetricMolalityTest() {
        assertEqualScientificValue(1(Kilogram), 2(Mole) / 2(Mole per Kilogram))
    }

    @Test
    fun massFromAmountOfSubstanceDivImperialMolalityTest() {
        assertEqualScientificValue(1(Pound), 2(Mole) / 2(Mole per Pound))
    }
}
