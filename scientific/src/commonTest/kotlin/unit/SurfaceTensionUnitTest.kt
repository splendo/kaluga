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

import kotlin.test.Test
import kotlin.test.assertEquals

class SurfaceTensionUnitTest {

    @Test
    fun metricSurfaceTensionConversionTest() {
        assertEquals(1_000.0, (CubicMeter per Kilogram).convert(1.0, Liter per Kilogram, 2))
        assertEquals(1_000.0, (CubicMeter per Kilogram).convert(1.0, CubicMeter per Tonne))
        assertEquals(1_000.0, (CubicMeter per Dalton).convert(1.0, Liter per Dalton, 2))
        assertEquals(1_000.0, (CubicMeter per Dalton).convert(1.0, CubicMeter per Kilodalton))

        assertEquals(0.001, (Liter per Kilogram).convert(1.0, CubicMeter per Kilogram))
        assertEquals(1_000.0, (Liter per Kilogram).convert(1.0, Liter per Tonne))
        assertEquals(0.001, (Liter per Dalton).convert(1.0, CubicMeter per Dalton))
        assertEquals(1_000.0, (Liter per Dalton).convert(1.0, Liter per Kilodalton))
    }

    @Test
    fun imperialSurfaceTensionConversionTest() {
        assertEquals(0.06, (CubicFoot per Pound).convert(1.0, CubicFoot per Ounce, 2))
        assertEquals(1728.0, (CubicFoot per Pound).convert(1.0, CubicInch per Pound, 2))
        assertEquals(2240.0, (CubicFoot per Pound).convert(1.0, CubicFoot per ImperialTon, 2))
    }

    @Test
    fun ukImperialSurfaceTensionConversionTest() {
        assertEquals(12.0, (ImperialTonForce per Inch).convert(1.0, ImperialTonForce per Foot, 2))
    }

    @Test
    fun usCustomarySurfaceTensionConversionTest() {
        assertEquals(0.06, (AcreFoot per Pound).convert(1.0, AcreFoot per Ounce, 2))
        assertEquals(2_000.0, (AcreFoot per Pound).convert(1.0, AcreFoot per UsTon, 2))
        assertEquals(12.0, (AcreFoot per Pound).convert(1.0, AcreInch per Pound, 2))
        assertEquals(0.06, (AcreInch per Pound).convert(1.0, AcreInch per Ounce, 2))
        assertEquals(2_000.0, (AcreInch per Pound).convert(1.0, AcreInch per UsTon, 2))

        assertEquals(0.06, (UsFluidDram per Pound).convert(1.0, UsFluidDram per Ounce, 2))
        assertEquals(2_000.0, (UsFluidDram per Pound).convert(1.0, UsFluidDram per UsTon, 2))
        assertEquals(0.12, (UsFluidDram per Pound).convert(1.0, UsFluidOunce per Pound, 2))
        assertEquals(0.06, (UsFluidOunce per Pound).convert(1.0, UsFluidOunce per Ounce, 2))
        assertEquals(2_000.0, (UsFluidOunce per Pound).convert(1.0, UsFluidOunce per UsTon, 2))

        assertEquals(0.06, (UsCustomaryCup per Pound).convert(1.0, UsCustomaryCup per Ounce, 2))
        assertEquals(2_000.0, (UsCustomaryCup per Pound).convert(1.0, UsCustomaryCup per UsTon, 2))
        assertEquals(0.99, (UsCustomaryCup per Pound).convert(1.0, UsLegalCup per Pound, 2))
        assertEquals(0.06, (UsLegalCup per Pound).convert(1.0, UsLegalCup per Ounce, 2))
        assertEquals(2_000.0, (UsLegalCup per Pound).convert(1.0, UsLegalCup per UsTon, 2))

        assertEquals(0.06, (UsLiquidPint per Pound).convert(1.0, UsLiquidPint per Ounce, 2))
        assertEquals(2_000.0, (UsLiquidPint per Pound).convert(1.0, UsLiquidPint per UsTon, 2))
        assertEquals(0.5, (UsLiquidPint per Pound).convert(1.0, UsLiquidQuart per Pound, 2))
        assertEquals(0.13, (UsLiquidPint per Pound).convert(1.0, UsLiquidGallon per Pound, 2))
        assertEquals(0.06, (UsLiquidQuart per Pound).convert(1.0, UsLiquidQuart per Ounce, 2))
        assertEquals(2_000.0, (UsLiquidQuart per Pound).convert(1.0, UsLiquidQuart per UsTon, 2))
        assertEquals(0.06, (UsLiquidGallon per Pound).convert(1.0, UsLiquidGallon per Ounce, 2))
        assertEquals(2_000.0, (UsLiquidGallon per Pound).convert(1.0, UsLiquidGallon per UsTon, 2))
    }
}