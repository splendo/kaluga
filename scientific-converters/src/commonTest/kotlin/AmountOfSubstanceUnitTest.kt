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

package com.splendo.kaluga.scientific.converter

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
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.Decikatal
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class AmountOfSubstanceUnitTest {

    @Test
    fun amountOfSubstanceFromCatalysisAndTimeTest() {
        assertEqualScientificValue(24(Mole), 2(Decikatal) * 2(Minute))
        assertEqualScientificValue(24(Mole), 2(Minute) * 2(Decikatal))
    }

    @Test
    fun amountOfSubstanceFromEnergyAndMolarEnergyTest() {
        assertEqualScientificValue(1(Decimole), (2(WattHour) / 2(WattHour per Decimole)))
    }

    @Test
    fun amountOfSubstanceFromMolalityAndWeightTest() {
        assertEqualScientificValue(4(Decimole), 2(Decimole per Gram) * 2(Gram))
        assertEqualScientificValue(4(Decimole), 2(Gram) * 2(Decimole per Gram))
    }

    @Test
    fun amountOfSubstanceFromMolarityAndVolumeTest() {
        assertEqualScientificValue(
            4(Decimole),
            2(Decimole per CubicCentimeter) * 2(CubicCentimeter),
        )
        assertEqualScientificValue(
            4(Decimole),
            2(CubicCentimeter) * 2(Decimole per CubicCentimeter),
        )
    }

    @Test
    fun amountOfSubstanceFromVolumeAndMolarVolumeTest() {
        assertEqualScientificValue(
            1(Decimole),
            (2(CubicCentimeter) / 2(CubicCentimeter per Decimole)),
        )
    }

    @Test
    fun amountOfSubstanceFromWeightAndMolarMassTest() {
        assertEqualScientificValue(1(Decimole), 2(Gram) / 2(Gram per Decimole))
    }
}
