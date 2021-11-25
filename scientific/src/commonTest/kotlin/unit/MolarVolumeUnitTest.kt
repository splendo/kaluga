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

class MolarVolumeUnitTest {

    @Test
    fun metricMomentumConversionTest() {
        assertScientificConversion(1.0, (CubicMeter per Mole), 1_000.0, Liter per Mole)
    }

    @Test
    fun imperialMomentumConversionTest() {
        assertScientificConversion(1.0, (CubicFoot per Mole), 1_728.0, CubicInch per Mole)
    }

    @Test
    fun ukImperialMomentumConversionTest() {
        assertScientificConversion(1.0, (ImperialPint per Mole), 0.5, ImperialQuart per Mole)
    }

    @Test
    fun usCustomaryMomentumConversionTest() {
        assertScientificConversion(1.0, (AcreFoot per Mole), 12.0, AcreInch per Mole)
    }
}