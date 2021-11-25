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
        assertScientificConversion(1.0, CubicMeter, 1e+27, CubicNanometer)
        assertScientificConversion(1.0, CubicMeter, 1e+18, CubicMicrometer)
        assertScientificConversion(1.0, CubicMeter, 1e+9, CubicMillimeter)
        assertScientificConversion(1.0, CubicMeter, 1e+6, CubicCentimeter)
        assertScientificConversion(1.0, CubicMeter, 1_000.0, CubicDecimeter)
        assertScientificConversion(1.0, CubicMeter, 0.001, CubicDecameter)
        assertScientificConversion(1.0, CubicMeter, 1.0e-6, CubicHectometer)
        assertScientificConversion(1.0, CubicMeter, 1.0e-9, CubicKilometer)
        assertScientificConversion(1.0, CubicMeter, 1.0e-18, CubicMegameter)
        assertScientificConversion(1.0, CubicMeter, 1.0e-27, CubicGigameter)
    }

    @Test
    fun literConversionTest() {
        assertScientificConversion(1.0, Liter, 1e+9, Nanoliter)
        assertScientificConversion(1.0, Liter, 1e+6, Microliter)
        assertScientificConversion(1.0, Liter, 1_000.0, Milliliter)
        assertScientificConversion(1.0, Liter, 100.0, Centiliter)
        assertScientificConversion(1.0, Liter, 10.0, Deciliter)
        assertScientificConversion(1.0, Liter, 0.1, Decaliter)
        assertScientificConversion(1.0, Liter, 0.01, Hectoliter)
        assertScientificConversion(1.0, Liter, 0.001, Kiloliter)
        assertScientificConversion(1.0, Liter, 1e-6, Megaliter)
        assertScientificConversion(1.0, Liter, 1e-9, Gigaliter)
    }

    @Test
    fun cubicFeetConversionTest() {
        assertScientificConversion(1.0, CubicFoot, 1728.0, CubicInch)
        assertScientificConversion(1.0, CubicFoot, 0.037037, CubicYard, 6)
        assertScientificConversion(1.0, CubicFoot, 6.794e-12, CubicMile, 15)
    }

    @Test
    fun usCustomaryConversionTest() {
        assertScientificConversion(1.0, AcreFoot, 12.0, AcreInch)

        assertScientificConversion(1.0, UsFluidOunce, 8.0, UsFluidDram)

        assertScientificConversion(1.0, UsCustomaryCup, 0.985784, UsLegalCup, 6)

        assertScientificConversion(1.0, UsLiquidPint, 0.5, UsLiquidQuart)
        assertScientificConversion(1.0, UsLiquidPint, 0.125, UsLiquidGallon)
    }

    @Test
    fun ukImperialConversionTest() {
        assertScientificConversion(1.0, ImperialFluidOunce, 8.0, ImperialFluidDram)

        assertScientificConversion(1.0, MetricCup, 1.0, MetricCup)

        assertScientificConversion(1.0, ImperialPint, 0.5, ImperialQuart)
        assertScientificConversion(1.0, ImperialPint, 0.125, ImperialGallon)
    }

    // ##### Mixed mass unit table conversions #####

    // Cubic Meter

    @Test
    fun cubicMeterToLiterConversionTest() {
        assertScientificConversion(1.0, CubicMeter, 1e+12, Nanoliter)
        assertScientificConversion(1.0, CubicMeter, 1e+9, Microliter)
        assertScientificConversion(1.0, CubicMeter, 1e+6, Milliliter)
        assertScientificConversion(1.0, CubicMeter, 100_000.0, Centiliter)
        assertScientificConversion(1.0, CubicMeter, 10_000.0, Deciliter)
        assertScientificConversion(1.0, CubicMeter, 1_000.0, Liter)
        assertScientificConversion(1.0, CubicMeter, 100.0, Decaliter)
        assertScientificConversion(1.0, CubicMeter, 10.0, Hectoliter)
        assertScientificConversion(1.0, CubicMeter, 1.0, Kiloliter)
        assertScientificConversion(1.0, CubicMeter, 0.001, Megaliter)
        assertScientificConversion(1.0, CubicMeter, 0.000001, Gigaliter)
    }

    @Test
    fun cubicMeterToCubicFeetConversionTest() {
        assertScientificConversion(1.0, CubicMeter, 61023.74, CubicInch, 2)
        assertScientificConversion(1.0, CubicMeter, 35.31, CubicFoot, 2)
        assertScientificConversion(1.0, CubicMeter, 1.31, CubicYard, 2)
        assertScientificConversion(1.0, CubicMeter, 0.00000000024, CubicMile, 11)
    }

    @Test
    fun cubicMeterToUsCustomaryConversionTest() {
        assertScientificConversion(1.0, CubicMeter, 0.000811, AcreFoot, 6)
        assertScientificConversion(1.0, CubicMeter, 0.009729, AcreInch, 6)

        assertScientificConversion(1.0, CubicMeter, 33814.02, UsFluidOunce, 2)
        assertScientificConversion(1.0, CubicMeter, 270512.18, UsFluidDram, 2)

        assertScientificConversion(1.0, CubicMeter, 4226.75, UsCustomaryCup, 2)
        assertScientificConversion(1.0, CubicMeter, 4166.67, UsLegalCup, 2)

        assertScientificConversion(1.0, CubicMeter, 2113.38, UsLiquidPint, 2)
        assertScientificConversion(1.0, CubicMeter, 1056.69, UsLiquidQuart, 2)
        assertScientificConversion(1.0, CubicMeter, 264.17, UsLiquidGallon, 2)
    }

    @Test
    fun cubicMeterToUkImperialConversionTest() {
        assertScientificConversion(1.0, CubicMeter, 35195.08, ImperialFluidOunce, 2)
        assertScientificConversion(1.0, CubicMeter, 281560.64, ImperialFluidDram, 2)

        assertScientificConversion(1.0, CubicMeter, 4000.0, MetricCup)

        assertScientificConversion(1.0, CubicMeter, 1759.75, ImperialPint, 2)
        assertScientificConversion(1.0, CubicMeter, 879.88, ImperialQuart, 2)
        assertScientificConversion(1.0, CubicMeter, 219.97, ImperialGallon, 2)
    }

    // Liter

    @Test
    fun literToCubicFeetConversionTest() {
        assertScientificConversion(1.0, Liter, 61.02, CubicInch, 2)
        assertScientificConversion(1.0, Liter, 0.035315, CubicFoot, 6)
        assertScientificConversion(1.0, Liter, 0.001308, CubicYard, 6)
        assertScientificConversion(1.0, Liter, 2.4e-13, CubicMile, 15)
    }

    @Test
    fun literToUsCustomaryConversionTest() {
        assertScientificConversion(1.0, Liter, 8.1e-7, AcreFoot, 8)
        assertScientificConversion(1.0, Liter, 0.00000973, AcreInch, 8)

        assertScientificConversion(1.0, Liter, 33.81, UsFluidOunce, 2)
        assertScientificConversion(1.0, Liter, 270.51, UsFluidDram, 2)

        assertScientificConversion(1.0, Liter, 4.23, UsCustomaryCup, 2)
        assertScientificConversion(1.0, Liter, 4.17, UsLegalCup, 2)

        assertScientificConversion(1.0, Liter, 2.11, UsLiquidPint, 2)
        assertScientificConversion(1.0, Liter, 1.06, UsLiquidQuart, 2)
        assertScientificConversion(1.0, Liter, 0.26, UsLiquidGallon, 2)
    }

    @Test
    fun literToUkImperialConversionTest() {
        assertScientificConversion(1.0, Liter, 35.2, ImperialFluidOunce, 2)
        assertScientificConversion(1.0, Liter, 281.56, ImperialFluidDram, 2)

        assertScientificConversion(1.0, Liter, 4.0, MetricCup)

        assertScientificConversion(1.0, Liter, 1.76, ImperialPint, 2)
        assertScientificConversion(1.0, Liter, 0.88, ImperialQuart, 2)
        assertScientificConversion(1.0, Liter, 0.22, ImperialGallon, 2)
    }

    @Test
    fun volumeFromAmountOfSubstanceDivMetricMolarity(){
        assertEqualScientificValue(1(CubicMeter), 2(Mole) / 2(Mole per CubicMeter))
    }
}