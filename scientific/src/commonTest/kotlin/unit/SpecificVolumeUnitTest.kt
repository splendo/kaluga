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

class SpecificVolumeUnitTest {

    @Test
    fun metricSpecificVolumeConversionTest() {
        assertEquals(1_000.0, (CubicMeter per Kilogram).convert(1.0, Liter per Kilogram, 2))
    }

    @Test
    fun imperialSpecificVolumeConversionTest() {
        assertEquals(1728.0, (CubicFoot per Pound).convert(1.0, CubicInch per Pound, 2))
    }

    @Test
    fun ukImperialSpecificVolumeConversionTest() {
        assertEquals(12.0, (ImperialTonForce per Inch).convert(1.0, ImperialTonForce per Foot, 2))
    }

    @Test
    fun usCustomarySpecificVolumeConversionTest() {
        assertEquals(12.0, (AcreFoot per Pound).convert(1.0, AcreInch per Pound, 2))
    }
}