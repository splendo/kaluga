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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.pow
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class VolumeUnitTest {

    @Test
    fun cubicMeterConversionTest() {
        assertScientificConversion("1.0", CubicMeter, "1e+27", CubicNanometer)
        assertScientificConversion("1.0", CubicMeter, "1e+18", CubicMicrometer)
        assertScientificConversion("1.0", CubicMeter, "1e+9", CubicMillimeter)
        assertScientificConversion("1.0", CubicMeter, "1e+6", CubicCentimeter)
        assertScientificConversion("1.0", CubicMeter, "1000.0", CubicDecimeter)
        assertScientificConversion("1.0", CubicMeter, "0.001", CubicDecameter)
        assertScientificConversion("1.0", CubicMeter, "1.0e-6", CubicHectometer)
        assertScientificConversion("1.0", CubicMeter, "1.0e-9", CubicKilometer)
        assertScientificConversion("1.0", CubicMeter, "1.0e-18", CubicMegameter)
        assertScientificConversion("1.0", CubicMeter, "1.0e-27", CubicGigameter)

        assertScientificConversion("1.0", CubicMeter, "1000.0", Liter)

        assertScientificConversion(Decimal.ONE, CubicMeter, Meter.convert(Decimal.ONE, Foot).pow(3), CubicFoot, round = 32)

        assertScientificConversion(Decimal.ONE, CubicMeter, SquareMeter.convert(Decimal.ONE, Acre) * Meter.convert(Decimal.ONE, Foot), AcreFoot, round = 32)
        assertScientificConversion(Decimal.ONE, CubicMeter, Meter.convert(Decimal.ONE, Inch).pow(3) / 231.toDecimal(), UsLiquidGallon, round = 32)

        assertScientificConversion("1.0", CubicMeter, "4000.0", MetricCup)
        assertScientificConversion(Decimal.ONE, CubicMeter, Decimal.THOUSAND / "4.54609".toDecimal(), ImperialGallon, round = 32)
    }

    @Test
    fun literConversionTest() {
        assertScientificConversion("1.0", Liter, "1e+9", Nanoliter)
        assertScientificConversion("1.0", Liter, "1e+6", Microliter)
        assertScientificConversion("1.0", Liter, "1000.0", Milliliter)
        assertScientificConversion("1.0", Liter, "100.0", Centiliter)
        assertScientificConversion("1.0", Liter, "10.0", Deciliter)
        assertScientificConversion("1.0", Liter, "0.1", Decaliter)
        assertScientificConversion("1.0", Liter, "0.01", Hectoliter)
        assertScientificConversion("1.0", Liter, "0.001", Kiloliter)
        assertScientificConversion("1.0", Liter, "1e-6", Megaliter)
        assertScientificConversion("1.0", Liter, "1e-9", Gigaliter)
    }

    @Test
    fun cubicFeetConversionTest() {
        assertScientificConversion(Decimal.ONE, CubicFoot, Foot.convert(Decimal.ONE, Inch).pow(3), CubicInch)
        assertScientificConversion(Decimal.ONE, CubicFoot, Foot.convert(Decimal.ONE, Yard).pow(3), CubicYard, round = 32)
        assertScientificConversion(Decimal.ONE, CubicFoot, Foot.convert(Decimal.ONE, Mile).pow(3), CubicMile, round = 32)
    }

    @Test
    fun usCustomaryConversionTest() {
        assertScientificConversion("1.0", AcreFoot, "12.0", AcreInch)

        assertScientificConversion("1.0", UsLiquidGallon, "4.0", UsLiquidQuart)
        assertScientificConversion("1.0", UsLiquidGallon, "8.0", UsLiquidPint)
        assertScientificConversion("1.0", UsLiquidGallon, "16.0", UsCustomaryCup)
        assertScientificConversion("1.0", UsLiquidGallon, "128.0", UsFluidOunce)
        assertScientificConversion("1.0", UsLiquidGallon, "1024.0", UsFluidDram)

        val cubicInch = 231.toDecimal() / 16.toDecimal()
        val milliliters = Inch.convert(Decimal.ONE, Centimeter).pow(3)
        assertScientificConversion(Decimal.ONE, UsCustomaryCup, (cubicInch * milliliters) / 240.0.toDecimal(), UsLegalCup, round = 32)

        assertScientificConversion("1.0", CubicFoot, "1.0", CubicFoot.usCustomary)
    }

    @Test
    fun ukImperialConversionTest() {
        assertScientificConversion("1.0", ImperialGallon, "4.0", ImperialQuart)
        assertScientificConversion("1.0", ImperialGallon, "8.0", ImperialPint)
        assertScientificConversion("1.0", ImperialGallon, "160.0", ImperialFluidOunce)
        assertScientificConversion("1.0", ImperialGallon, "1280.0", ImperialFluidDram)

        assertScientificConversion("1.0", CubicFoot, "1.0", CubicFoot.ukImperial)
    }
}
