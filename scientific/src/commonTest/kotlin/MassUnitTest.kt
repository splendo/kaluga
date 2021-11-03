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

package com.splendo.kaluga.scientific

import kotlin.test.Test
import kotlin.test.assertEquals

class MassUnitTest {

    /*
    private val unitTranslationErrorTolerance = 0.000001

    @Test
    fun weighConversionTest() {
        assertScientificUnit(1000, Kilogram, Slug, 0.068521765561961, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Microgram, Kilogram, 1e-9)
        assertScientificUnit(1000, Milligram, Kilogram, 1e-6)
        assertScientificUnit(1000, Gram, Kilogram, 0.001)
        assertScientificUnit(1000, Tonne, Kilogram, 1000.0)
        assertScientificUnit(1000, Dram, Kilogram, 0.0017718451953125, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Grain, Kilogram, 0.00006479891, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Ounce, Kilogram, 0.028349523125, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Pound, Kilogram, 0.45359237, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Stone, Kilogram, 6.35029318, unitTranslationErrorTolerance)
        assertScientificUnit(1000, UsTon, Kilogram, 907.18474, unitTranslationErrorTolerance)
        assertScientificUnit(1000, ImperialTon, Kilogram, 1016.0469088, unitTranslationErrorTolerance)

        assertScientificUnit(1000, Kilogram, Microgram, 1.0E9)
        assertScientificUnit(1000, Kilogram, Milligram, 1000000.0)
        assertScientificUnit(1000, Kilogram, Gram, 1000.0)
        assertScientificUnit(1000, Kilogram, Tonne, 0.001)
        assertScientificUnit(1000, Kilogram, Dram, 564.3833911932866, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Kilogram, Grain, 15432.358352941432, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Kilogram, Ounce, 35.27396194958041, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Kilogram, Pound, 2.204622621848776, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Kilogram, Stone, 0.1574730444177697, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Kilogram, UsTon, 0.001102311310924, unitTranslationErrorTolerance)
        assertScientificUnit(1000, Kilogram, ImperialTon, 0.000984206527611, unitTranslationErrorTolerance)
    }
    */

    @Test
    fun kilogramConversionTest() {
        assertEquals(1e+12, Kilogram.convert(1.0, Nanogram))
        assertEquals(1e+9, Kilogram.convert(1.0, Microgram))
        assertEquals(1e+6, Kilogram.convert(1.0, Milligram))
        assertEquals(100_000.0, Kilogram.convert(1.0, Centigram))
        assertEquals(10_000.0, Kilogram.convert(1.0, Decigram))
        assertEquals(100.0, Kilogram.convert(1.0, Decagram))
        assertEquals(10.0, Kilogram.convert(1.0, Hectogram))
        assertEquals(0.001, Kilogram.convert(1.0, Tonne))
        assertEquals(0.001, Kilogram.convert(1.0, Megagram))
        assertEquals(1e-6, Kilogram.convert(1.0, Gigagram))
    }

    @Test
    fun daltonConversionTest() {
        assertEquals(1e+9, Dalton.convert(1.0, Nanodalton))
        assertEquals(1e+6, Dalton.convert(1.0, Microdalton))
        assertEquals(1_000.0, Dalton.convert(1.0, Millidalton))
        assertEquals(100.0, Dalton.convert(1.0, Centidalton))
        assertEquals(10.0, Dalton.convert(1.0, Decidalton))
        assertEquals(0.1, Dalton.convert(1.0, Decadalton))
        assertEquals(0.01, Dalton.convert(1.0, HectoDalton))
        assertEquals(0.001, Dalton.convert(1.0, Kilodalton))
        assertEquals(1e-6, Dalton.convert(1.0, Megadalton))
        assertEquals(1e-9, Dalton.convert(1.0, Gigadalton))
    }

    @Test
    fun poundConversionTest() {
        assertEquals(7_000.0, Pound.convert(1.0, Grain))
        assertEquals(256.0, Pound.convert(1.0, Dram))
        assertEquals(16.0, Pound.convert(1.0, Ounce))
        assertEquals(0.07142857142857142, Pound.convert(1.0, Stone))
        assertEquals(0.031080950037834294, Pound.convert(1.0, Slug))
    }
}
