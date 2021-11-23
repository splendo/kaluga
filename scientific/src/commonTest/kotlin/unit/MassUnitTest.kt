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
        assertEquals(1e+12, Kilogram.convert(1.0, Nanogram))
        assertEquals(1e+9, Kilogram.convert(1.0, Microgram))
        assertEquals(1e+6, Kilogram.convert(1.0, Milligram))
        assertEquals(100_000.0, Kilogram.convert(1.0, Centigram))
        assertEquals(10_000.0, Kilogram.convert(1.0, Decigram))
        assertEquals(1_000.0, Kilogram.convert(1.0, Gram))
        assertEquals(100.0, Kilogram.convert(1.0, Decagram))
        assertEquals(10.0, Kilogram.convert(1.0, Hectogram))
        assertEquals(0.001, Kilogram.convert(1.0, Megagram))
        assertEquals(0.001, Kilogram.convert(1.0, Tonne))
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
        assertEquals(0.07142857, Pound.convert(1.0, Stone, 8))
        assertEquals(0.03108095, Pound.convert(1.0, Slug, 8))

        // uk ton
        assertEquals(0.00044643, Pound.convert(1.0, ImperialTon, 8))
        // us ton
        assertEquals(0.0005, Pound.convert(1.0, UsTon))
    }

    // ##### Mixed mass unit table conversions #####

    @Test
    fun kilogramToDaltonConversionTest() {
        assertEquals(6.02214076e+35, Kilogram.convert(1.0, Nanodalton))
        assertEquals(6.02214076e+32, Kilogram.convert(1.0, Microdalton))
        assertEquals(6.02214076e+29, Kilogram.convert(1.0, Millidalton))
        assertEquals(6.02214076e+28, Kilogram.convert(1.0, Centidalton))
        assertEquals(6.02214076e+27, Kilogram.convert(1.0, Decidalton))
        assertEquals(6.02214076e+26, Kilogram.convert(1.0, Dalton))
        assertEquals(6.02214076e+25, Kilogram.convert(1.0, Decadalton))
        assertEquals(6.02214076e+24, Kilogram.convert(1.0, HectoDalton))
        assertEquals(6.02214076e+23, Kilogram.convert(1.0, Kilodalton))
        assertEquals(6.02214076e+20, Kilogram.convert(1.0, Megadalton))
        assertEquals(6.02214076e+17, Kilogram.convert(1.0, Gigadalton))
    }

    @Test
    fun kilogramToPoundConversionTest() {
        assertEquals(15_432.35835294, Kilogram.convert(1.0, Grain, 8))
        assertEquals(564.38339119, Kilogram.convert(1.0, Dram, 8))
        assertEquals(35.27396195, Kilogram.convert(1.0, Ounce, 8))
        assertEquals(2.20462262, Kilogram.convert(1.0, Pound, 8))
        assertEquals(0.15747304, Kilogram.convert(1.0, Stone, 8))
        assertEquals(0.06852177, Kilogram.convert(1.0, Slug, 8))

        // uk ton
        assertEquals(0.00098421, Kilogram.convert(1.0, ImperialTon, 8))
        // us ton
        assertEquals(0.00110231, Kilogram.convert(1.0, UsTon, 8))
    }

    @Test
    fun daltonToKilogramConversionTest() {
        assertEquals(1.6605390671738466e-15, Dalton.convert(1.0, Nanogram))
        assertEquals(1.6605390671738466e-18, Dalton.convert(1.0, Microgram))
        assertEquals(1.6605390671738467e-21, Dalton.convert(1.0, Milligram))
        assertEquals(1.6605390671738467e-22, Dalton.convert(1.0, Centigram))
        assertEquals(1.6605390671738467e-23, Dalton.convert(1.0, Decigram))
        assertEquals(1.6605390671738467e-25, Dalton.convert(1.0, Decagram))
        assertEquals(1.6605390671738468e-26, Dalton.convert(1.0, Hectogram))
        assertEquals(1.6605390671738468e-27, Dalton.convert(1.0, Kilogram))
        assertEquals(1.6605390671738466e-30, Dalton.convert(1.0, Megagram))
        assertEquals(1.6605390671738466e-30, Dalton.convert(1.0, Tonne))
        assertEquals(1.6605390671738466e-33, Dalton.convert(1.0, Gigagram))
    }

    @Test
    fun daltonToPoundConversionTest() {
        assertEquals(2.5626033943685885e-23, Dalton.convert(1.0, Grain))
        assertEquals(9.371806699405122e-25, Dalton.convert(1.0, Dram))
        assertEquals(5.857379187128202e-26, Dalton.convert(1.0, Ounce))
        assertEquals(3.660861991955126e-27, Dalton.convert(1.0, Pound))
        assertEquals(2.6149014228250904e-28, Dalton.convert(1.0, Stone))
        assertEquals(1.1378306915694172e-28, Dalton.convert(1.0, Slug))

        // uk ton
        assertEquals(1.6343133892656814e-30, Dalton.convert(1.0, ImperialTon))
        // us ton
        assertEquals(1.830430995977563e-30, Dalton.convert(1.0, UsTon))
    }

    @Test
    fun poundToKilogramConversionTest() {
        assertEquals(4.5359237e+11, Pound.convert(1.0, Nanogram))
        assertEquals(4.5359237e+8, Pound.convert(1.0, Microgram))
        assertEquals(453_592.37, Pound.convert(1.0, Milligram))
        assertEquals(45_359.237, Pound.convert(1.0, Centigram))
        assertEquals(4_535.9237, Pound.convert(1.0, Decigram))
        assertEquals(45.359237, Pound.convert(1.0, Decagram))
        assertEquals(4.5359237, Pound.convert(1.0, Hectogram))
        assertEquals(0.45359237, Pound.convert(1.0, Kilogram))
        assertEquals(0.00045359, Pound.convert(1.0, Megagram, 8))
        assertEquals(0.00045359, Pound.convert(1.0, Tonne, 8))
        assertEquals(0.00000045, Pound.convert(1.0, Gigagram, 8))
    }

    @Test
    fun poundToDaltonConversionTest() {
        assertEquals(2.731597099802001e+35, Pound.convert(1.0, Nanodalton))
        assertEquals(2.731597099802001e+32, Pound.convert(1.0, Microdalton))
        assertEquals(2.731597099802001e+29, Pound.convert(1.0, Millidalton))
        assertEquals(2.7315970998020012e+28, Pound.convert(1.0, Centidalton))
        assertEquals(2.7315970998020014e+27, Pound.convert(1.0, Decidalton))
        assertEquals(2.7315970998020013e+26, Pound.convert(1.0, Dalton))
        assertEquals(2.7315970998020012e+25, Pound.convert(1.0, Decadalton))
        assertEquals(2.731597099802001e+24, Pound.convert(1.0, HectoDalton))
        assertEquals(2.7315970998020013e+23, Pound.convert(1.0, Kilodalton))
        assertEquals(2.731597099802001e+20, Pound.convert(1.0, Megadalton))
        assertEquals(2.73159709980200128e+17, Pound.convert(1.0, Gigadalton))
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
