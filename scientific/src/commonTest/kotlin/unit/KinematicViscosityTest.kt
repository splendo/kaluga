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
import com.splendo.kaluga.base.utils.pow
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlin.test.Test

class KinematicViscosityTest {

    @Test
    fun metricKinematicViscosityConversionTest() {
        assertScientificConversion("1", (SquareMeter per Second), "1e+18", (SquareNanometer per Second))
        assertScientificConversion("1", (SquareMeter per Second), "1e+12", (SquareMicrometer per Second))
        assertScientificConversion("1", (SquareMeter per Second), "1000000.0", (SquareMillimeter per Second))
        assertScientificConversion("1", (SquareMeter per Second), "10000.0", (SquareCentimeter per Second))
        assertScientificConversion("1", (SquareMeter per Second), "100.0", (SquareDecimeter per Second))
        assertScientificConversion("1", (SquareMeter per Second), "0.01", (SquareDecameter per Second))
        assertScientificConversion("1", (SquareMeter per Second), "0.0001", (SquareHectometer per Second))
        assertScientificConversion("1", (SquareMeter per Second), "1e-6", (SquareKilometer per Second))
        assertScientificConversion("1", (SquareMeter per Second), "0.0001", (Hectare per Second))

        assertScientificConversion(Decimal.ONE, (SquareMeter per Second), Meter.convert(Decimal.ONE, Inch).pow(2), (SquareInch per Second), round = 32)
        assertScientificConversion(Decimal.ONE, (SquareMeter per Second), Meter.convert(Decimal.ONE, Foot).pow(2), (SquareFoot per Second), round = 32)
        assertScientificConversion(Decimal.ONE, (SquareMeter per Second), Meter.convert(Decimal.ONE, Yard).pow(2), (SquareYard per Second), round = 32)
        assertScientificConversion(Decimal.ONE, (SquareMeter per Second), Meter.convert(Decimal.ONE, Mile).pow(2), (SquareMile per Second), round = 32)
        assertScientificConversion(Decimal.ONE, (SquareMeter per Second), Meter.convert(Decimal.ONE, Mile).pow(2) * 640.toDecimal(), (Acre per Second), round = 32)

        assertScientificConversion("1", (SquareMeter per Second), "1e-9", (SquareMeter per Nanosecond))
        assertScientificConversion("1", (SquareMeter per Second), "1e-6", (SquareMeter per Microsecond))
        assertScientificConversion("1", (SquareMeter per Second), "0.001", (SquareMeter per Millisecond))
        assertScientificConversion("1", (SquareMeter per Second), "0.01", (SquareMeter per Centisecond))
        assertScientificConversion("1", (SquareMeter per Second), "0.1", (SquareMeter per Decisecond))
        assertScientificConversion("1", (SquareMeter per Second), "60", (SquareMeter per Minute))
        assertScientificConversion("1", (SquareMeter per Second), "3600.0", (SquareMeter per Hour))
    }
}
