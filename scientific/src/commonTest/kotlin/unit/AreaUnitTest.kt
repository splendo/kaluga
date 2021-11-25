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
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.linearMassDensity.times
import com.splendo.kaluga.scientific.converter.luminousEnergy.div
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.converter.luminousIntensity.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.volumetricFlow.div
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class AreaUnitTest {

    @Test
    fun areaConversionTest() {
        assertScientificConversion(1, SquareMeter, 1e+18, SquareNanometer)
        assertScientificConversion(1, SquareMeter, 1e+12, SquareMicrometer)
        assertScientificConversion(1, SquareMeter, 1000000.0, SquareMillimeter)
        assertScientificConversion(1, SquareMeter, 10000.0, SquareCentimeter)
        assertScientificConversion(1, SquareMeter, 100.0, SquareDecimeter)
        assertScientificConversion(1, SquareMeter, 0.01, SquareDecameter)
        assertScientificConversion(1, SquareMeter, 0.0001, SquareHectometer)
        assertScientificConversion(1, SquareMeter, 1e-6, SquareKilometer)
        assertScientificConversion(1, SquareMeter, 0.0001, Hectare)

        assertScientificConversion(1, SquareMeter, 1550.0, SquareInch, 0)
        assertScientificConversion(1, SquareMeter, 10.7639, SquareFoot, 4)
        assertScientificConversion(1, SquareMeter, 1.19599, SquareYard, 5)
        assertScientificConversion(1, SquareMeter, 3.86102e-7, SquareMile, 12)
        assertScientificConversion(1, SquareMeter, 0.000247105, Acre, 9)
    }

    @Test
    fun areaFromEnergyAndSurfaceTensionTest() {
        assertEquals(1(SquareMeter), 2(Joule) / 2(Newton per Meter))
        assertEquals(1(SquareFoot), 2(FootPoundForce) / 2(PoundForce per Foot))
    }

    @Test
    fun areaFromForceAndPressureTest() {
        assertEquals(1(SquareMeter), 2(Newton) / 2(Pascal))
        assertEquals(1(SquareFoot), 2(PoundForce) / 2(PoundSquareFoot))
    }

    @Test
    fun areaFromLengthAndWidthTest() {
        assertEquals(4(SquareMeter), 2(Meter) * 2(Meter))
        assertEquals(4(SquareFoot), 2(Foot) * 2(Foot))
    }

    @Test
    fun areaFromLinearMassDensityAndDensityTest() {
        assertEquals(1(SquareMeter), 2(Kilogram per Meter) / 2(Kilogram per CubicMeter))
        assertEquals(1(SquareFoot), 2(Pound per Foot) / 2(Pound per CubicFoot))
    }

    @Test
    fun areaFromLinearMassDensityAndSpecificVolumeTest() {
        assertEquals(4(SquareMeter), 2(Kilogram per Meter) * 2(CubicMeter per Kilogram))
        assertEquals(4(SquareFoot), 2(Pound per Foot) * 2(CubicFoot per Pound))
    }

    @Test
    fun areaFromLuminousEnergyAndExposureTest() {
        assertEquals(1(SquareMeter), 2(Lumen x Second) / 2(Lux x Second))
        assertEquals(1(SquareFoot), 2(Lumen x Second) / 2(FootCandle x Second))
    }

    @Test
    fun areaFromFluxAndIlluminanceTest() {
        assertEquals(1(SquareMeter), 2(Lumen) / 2(Lux))
        assertEquals(1(SquareFoot), 2(Lumen) / 2(FootCandle))
    }

    @Test
    fun areaFromLuminousIntensityAndLuminanceTest() {
        assertEquals(1(SquareMeter), 2(Candela) / 2(Nit))
        // assertEquals(1(SquareFoot), 2(Candela) / 2(FootLambert)) FIXME I yield 3.141592653589793
    }

    @Test
    fun areaFromFluxAndInductionTest() {
        assertEquals(1(SquareMeter), 2(Weber) / 2(Tesla))
        // assertEquals(1(SquareFoot), 2(Weber) / 2(Tesla)) FIXME
    }

    @Test
    fun areaFromMomentumAndDynamicViscosityTest() {
        assertEquals(1(SquareMeter), 2(Kilogram x (Meter per Second)) / 2(Pascal x Second))
        // assertEquals(1(SquareFoot), 2(Pound x (Foot per Second)) / 2(PoundSquareFoot x Second)) FIXME I yield 0.031080950171567253
    }

    @Test
    fun areaFromVolumeAndHeightTest() {
        assertEquals(1(SquareMeter), 2(CubicMeter) / 2(Meter))
        assertEquals(1(SquareFoot), 2(CubicFoot) / 2(Foot))
    }

    @Test
    fun areaFromVolumetricFlowAndVolumetricFluxTest() {
        assertEqualScientificValue(
            1(SquareMeter),
            2(CubicMeter per Second) / 2((CubicMeter per Second) per SquareMeter)
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(CubicFoot per Second) / 2((CubicFoot per Second) per SquareFoot)
        )
    }

    @Test
    fun areaFromWeightAndAreaDensityTest() {
        assertEqualScientificValue(1(SquareMeter), 2(Kilogram) / 2(Kilogram per (SquareMeter)))
        assertEqualScientificValue(1(SquareFoot), 2(Pound) / 2(Pound per (SquareFoot)))
    }
}