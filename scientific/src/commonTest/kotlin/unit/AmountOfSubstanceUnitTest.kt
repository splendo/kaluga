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
import com.splendo.kaluga.scientific.converter.catalysticActivity.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.volume.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class AmountOfSubstanceUnitTest {

    @Test
    fun amountOfSubstanceConversionTest() {
        assertScientificConversion(1, Mole, 1e+9, Nanomole)
        assertScientificConversion(1, Mole, 1000000.0, Micromole)
        assertScientificConversion(1, Mole, 1000.0, Millimole)
        assertScientificConversion(1, Mole, 100.0, Centimole)
        assertScientificConversion(1, Mole, 10.0, Decimole)
        assertScientificConversion(1, Mole, 0.1, Decamole)
        assertScientificConversion(1, Mole, 0.01, Hectomole)
        assertScientificConversion(1, Mole, 0.001, Kilomole)
        assertScientificConversion(1, Mole, 1e-6, Megamole)
        assertScientificConversion(1, Mole, 1e-9, Gigamole)
    }

    @Test
    fun amountOfSubstanceFromCatalysisAndTimeTest() {
        assertEquals(4(Mole), 2(Katal) * 2(Second))
        assertEquals(4(Mole), 2(Second) * 2(Katal))
    }

    @Test
    fun amountOfSubstanceFromEnergyAndMolarEnergyTest() {
        assertEqualScientificValue(1(Mole), (2(Joule) / 2(Joule per Mole)))
        assertEqualScientificValue(1(Mole), (2(WattHour) / 2(WattHour per Mole)))
        assertEqualScientificValue(1(Mole), (2(HorsepowerHour) / 2(HorsepowerHour per Mole)))
    }

    @Test
    fun amountOfSubstanceFromMolalityAndWeightTest() {
        assertEqualScientificValue(4(Mole), 2(Mole per Kilogram) * 2(Kilogram))
        assertEqualScientificValue(4(Mole), 2(Kilogram) * 2(Mole per Kilogram))
        assertEqualScientificValue(4(Mole), 2(Mole per Pound) * 2(Pound))
        assertEqualScientificValue(4(Mole), 2(Pound) * 2(Mole per Pound))
        assertEqualScientificValue(4(Mole), 2(Mole per ImperialTon) * 2(ImperialTon))
        assertEqualScientificValue(4(Mole), 2(ImperialTon) * 2(Mole per ImperialTon))
        assertEqualScientificValue(4(Mole), 2(Mole per UsTon) * 2(UsTon))
        assertEqualScientificValue(4(Mole), 2(UsTon) * 2(Mole per UsTon))
    }

    @Test
    fun amountOfSubstanceFromMolarityAndVolumeTest() {
        assertEqualScientificValue(4(Mole), 2(Mole per CubicMeter) * 2(CubicMeter))
        assertEqualScientificValue(4(Mole), 2(CubicMeter) * 2(Mole per CubicMeter))
        assertEqualScientificValue(4(Mole), 2(Mole per CubicFoot) * 2(CubicFoot))
        assertEqualScientificValue(4(Mole), 2(CubicFoot) * 2(Mole per CubicFoot))
        assertEqualScientificValue(4(Mole), 2(Mole per ImperialGallon) * 2(ImperialGallon))
        assertEqualScientificValue(4(Mole), 2(ImperialGallon) * 2(Mole per ImperialGallon))
        assertEqualScientificValue(4(Mole), 2(Mole per UsLiquidGallon) * 2(UsLiquidGallon))
        assertEqualScientificValue(4(Mole), 2(UsLiquidGallon) * 2(Mole per UsLiquidGallon))
    }

    @Test
    fun amountOfSubstanceFromVolumeAndMolarVolumeTest() {
        assertEqualScientificValue(1(Mole), (2(CubicMeter) / 2(CubicMeter per Mole)))
        assertEqualScientificValue(1(Mole), (2(CubicFoot) / 2(CubicFoot per Mole)))
        assertEqualScientificValue(1(Mole), (2(ImperialGallon) / 2(ImperialGallon per Mole)))
        assertEqualScientificValue(1(Mole), (2(UsLiquidGallon) / 2(UsLiquidGallon per Mole)))
    }

    @Test
    fun amountOfSubstanceFromWeightAndMolarMassTest() {
        assertEqualScientificValue(1(Mole), 2(Kilogram) / 2(Kilogram per Mole))
        assertEqualScientificValue(1(Mole), 2(Pound) / 2(Pound per Mole))
        assertEqualScientificValue(1(Mole), 2(ImperialTon) / 2(ImperialTon per Mole))
        assertEqualScientificValue(1(Mole), 2(UsTon) / 2(UsTon per Mole))
    }
}