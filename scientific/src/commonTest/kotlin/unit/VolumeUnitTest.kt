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

class VolumeUnitTest {

    // ##### Same length unit table conversions #####

    @Test
    fun cubicMeterConversionTest() {
        assertEquals(1e+27, CubicMeter.convert(1.0, CubicNanometer))
        assertEquals(1e+18, CubicMeter.convert(1.0, CubicMicrometer))
        assertEquals(1e+9, CubicMeter.convert(1.0, CubicMillimeter))
        assertEquals(1e+6, CubicMeter.convert(1.0, CubicCentimeter))
        assertEquals(1_000.0, CubicMeter.convert(1.0, CubicDecimeter))
        assertEquals(0.001, CubicMeter.convert(1.0, CubicDecameter))
        assertEquals(1.0e-6, CubicMeter.convert(1.0, CubicHectometer))
        assertEquals(1.0e-9, CubicMeter.convert(1.0, CubicKilometer))
        assertEquals(1.0e-18, CubicMeter.convert(1.0, CubicMegameter))
        assertEquals(1.0e-27, CubicMeter.convert(1.0, CubicGigameter))
    }

    @Test
    fun literConversionTest() {
        assertEquals(1e+9, Liter.convert(1.0, Nanoliter))
        assertEquals(1e+6, Liter.convert(1.0, Microliter))
        assertEquals(1_000.0, Liter.convert(1.0, Milliliter))
        assertEquals(100.0, Liter.convert(1.0, Centiliter))
        assertEquals(10.0, Liter.convert(1.0, Deciliter))
        assertEquals(0.1, Liter.convert(1.0, Decaliter))
        assertEquals(0.01, Liter.convert(1.0, Hectoliter))
        assertEquals(0.001, Liter.convert(1.0, Kiloliter))
        assertEquals(1e-6, Liter.convert(1.0, Megaliter))
        assertEquals(1e-9, Liter.convert(1.0, Gigaliter))
    }

    @Test
    fun cubicFeetConversionTest() {
        assertEquals(1728.0, CubicFoot.convert(1.0, CubicInch))
        assertEquals(0.037037, CubicFoot.convert(1.0, CubicYard, 6))
        assertEquals(6.794e-12, CubicFoot.convert(1.0, CubicMile, 15))
    }

    @Test
    fun usCustomaryConversionTest() {
        assertEquals(12.0, AcreFoot.convert(1.0, AcreInch))

        assertEquals(8.0, UsFluidOunce.convert(1.0, UsFluidDram))

        assertEquals(0.985784, UsCustomaryCup.convert(1.0, UsLegalCup, 6))

        assertEquals(0.5, UsLiquidPint.convert(1.0, UsLiquidQuart))
        assertEquals(0.125, UsLiquidPint.convert(1.0, UsLiquidGallon))
    }

    @Test
    fun ukImperialConversionTest() {
        assertEquals(8.0, ImperialFluidOunce.convert(1.0, ImperialFluidDram))

        assertEquals(1.0, MetricCup.convert(1.0, MetricCup))

        assertEquals(0.5, ImperialPint.convert(1.0, ImperialQuart))
        assertEquals(0.125, ImperialPint.convert(1.0, ImperialGallon))
    }

    // ##### Mixed mass unit table conversions #####

    // Cubic Meter

    @Test
    fun cubicMeterToLiterConversionTest() {
        assertEquals(1e+12, CubicMeter.convert(1.0, Nanoliter))
        assertEquals(1e+9, CubicMeter.convert(1.0, Microliter))
        assertEquals(1e+6, CubicMeter.convert(1.0, Milliliter))
        assertEquals(100_000.0, CubicMeter.convert(1.0, Centiliter))
        assertEquals(10_000.0, CubicMeter.convert(1.0, Deciliter))
        assertEquals(1_000.0, CubicMeter.convert(1.0, Liter))
        assertEquals(100.0, CubicMeter.convert(1.0, Decaliter))
        assertEquals(10.0, CubicMeter.convert(1.0, Hectoliter))
        assertEquals(1.0, CubicMeter.convert(1.0, Kiloliter))
        assertEquals(0.001, CubicMeter.convert(1.0, Megaliter))
        assertEquals(0.000001, CubicMeter.convert(1.0, Gigaliter))
    }

    @Test
    fun cubicMeterToCubicFeetConversionTest() {
        assertEquals(61023.74, CubicMeter.convert(1.0, CubicInch, 2))
        assertEquals(35.31, CubicMeter.convert(1.0, CubicFoot, 2))
        assertEquals(1.31, CubicMeter.convert(1.0, CubicYard, 2))
        assertEquals(0.00000000024, CubicMeter.convert(1.0, CubicMile, 11))
    }

    @Test
    fun cubicMeterToUsCustomaryConversionTest() {
        assertEquals(0.000811, CubicMeter.convert(1.0, AcreFoot, 6))
        assertEquals(0.009729, CubicMeter.convert(1.0, AcreInch, 6))

        assertEquals(33814.02, CubicMeter.convert(1.0, UsFluidOunce, 2))
        assertEquals(270512.18, CubicMeter.convert(1.0, UsFluidDram, 2))

        assertEquals(4226.75, CubicMeter.convert(1.0, UsCustomaryCup, 2))
        assertEquals(4166.67, CubicMeter.convert(1.0, UsLegalCup, 2))

        assertEquals(2113.38, CubicMeter.convert(1.0, UsLiquidPint, 2))
        assertEquals(1056.69, CubicMeter.convert(1.0, UsLiquidQuart, 2))
        assertEquals(264.17, CubicMeter.convert(1.0, UsLiquidGallon, 2))
    }

    @Test
    fun cubicMeterToUkImperialConversionTest() {
        assertEquals(35195.08, CubicMeter.convert(1.0, ImperialFluidOunce, 2))
        assertEquals(281560.64, CubicMeter.convert(1.0, ImperialFluidDram, 2))

        assertEquals(4000.0, CubicMeter.convert(1.0, MetricCup))

        assertEquals(1759.75, CubicMeter.convert(1.0, ImperialPint, 2))
        assertEquals(879.88, CubicMeter.convert(1.0, ImperialQuart, 2))
        assertEquals(219.97, CubicMeter.convert(1.0, ImperialGallon, 2))
    }

    // Liter

    @Test
    fun literToCubicFeetConversionTest() {
        assertEquals(61.02, Liter.convert(1.0, CubicInch, 2))
        assertEquals(0.035315, Liter.convert(1.0, CubicFoot, 6))
        assertEquals(0.001308, Liter.convert(1.0, CubicYard, 6))
        assertEquals(2.4e-13, Liter.convert(1.0, CubicMile, 15))
    }

    @Test
    fun literToUsCustomaryConversionTest() {
        assertEquals(8.1e-7, Liter.convert(1.0, AcreFoot, 8))
        assertEquals(0.00000973, Liter.convert(1.0, AcreInch, 8))

        assertEquals(33.81, Liter.convert(1.0, UsFluidOunce, 2))
        assertEquals(270.51, Liter.convert(1.0, UsFluidDram, 2))

        assertEquals(4.23, Liter.convert(1.0, UsCustomaryCup, 2))
        assertEquals(4.17, Liter.convert(1.0, UsLegalCup, 2))

        assertEquals(2.11, Liter.convert(1.0, UsLiquidPint, 2))
        assertEquals(1.06, Liter.convert(1.0, UsLiquidQuart, 2))
        assertEquals(0.26, Liter.convert(1.0, UsLiquidGallon, 2))
    }

    @Test
    fun literToUkImperialConversionTest() {
        assertEquals(35.2, Liter.convert(1.0, ImperialFluidOunce, 2))
        assertEquals(281.56, Liter.convert(1.0, ImperialFluidDram, 2))

        assertEquals(4.0, Liter.convert(1.0, MetricCup))

        assertEquals(1.76, Liter.convert(1.0, ImperialPint, 2))
        assertEquals(0.88, Liter.convert(1.0, ImperialQuart, 2))
        assertEquals(0.22, Liter.convert(1.0, ImperialGallon, 2))
    }

    @Test
    fun volumeFromAmountOfSubstanceDivMetricMolarity(){
        assertEqualScientificValue(1(CubicMeter), 2(Mole) / 2(Mole per CubicMeter))
    }
}