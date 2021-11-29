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

class VolumeUnitTest {

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

        assertScientificConversion(1.0, CubicMeter, 1_000.0, Liter)

        assertScientificConversion(1.0, CubicMeter, 35.31, CubicFoot, 2)

        assertScientificConversion(1.0, CubicMeter, 0.000811, AcreFoot, 6)
        assertScientificConversion(1.0, CubicMeter, 264.172, UsLiquidGallon, 3)

        assertScientificConversion(1.0, CubicMeter, 4000.0, MetricCup)
        assertScientificConversion(1.0, CubicMeter, 219.969, ImperialGallon, 3)
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

        assertScientificConversion(1.0, UsLiquidGallon, 4.0, UsLiquidQuart)
        assertScientificConversion(1.0, UsLiquidGallon, 8.0, UsLiquidPint)
        assertScientificConversion(1.0, UsLiquidGallon, 16.0, UsCustomaryCup)
        assertScientificConversion(1.0, UsLiquidGallon, 128.0, UsFluidOunce)
        assertScientificConversion(1.0, UsLiquidGallon, 1024.0, UsFluidDram)

        assertScientificConversion(1.0, UsCustomaryCup, 0.985784, UsLegalCup, 6)

        assertScientificConversion(1.0, CubicFoot, 1.0, CubicFoot.usCustomary)
    }

    @Test
    fun ukImperialConversionTest() {
        assertScientificConversion(1.0, ImperialGallon, 4.0, ImperialQuart)
        assertScientificConversion(1.0, ImperialGallon, 8.0, ImperialPint)
        assertScientificConversion(1.0, ImperialGallon, 160.0, ImperialFluidOunce)
        assertScientificConversion(1.0, ImperialGallon, 1280.0, ImperialFluidDram)

        assertScientificConversion(1.0, CubicFoot, 1.0, CubicFoot.ukImperial)
    }

    @Test
    fun volumeFromAmountOfSubstanceDivMetricMolarity() {
        assertEqualScientificValue(1(CubicMeter), 2(Mole) / 2(Mole per CubicMeter))
    }
}
