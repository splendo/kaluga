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

class MomentumUnitTest {

    @Test
    fun metricMomentumConversionTest() {
        assertScientificConversion(1.0, (Kilogram x (Meter per Second)), 3600, Gram x (Kilometer per Hour))
    }

    @Test
    fun imperialMomentumConversionTest() {
        assertScientificConversion(1.0, (Pound x (Foot per Second)), 0.68, Pound x (Mile per Hour), 2)
    }

    @Test
    fun ukImperialMomentumConversionTest() {
        assertScientificConversion(1.0, (ImperialTon x (Foot per Second)), 0.68, ImperialTon x (Mile per Hour), 2)
    }

    @Test
    fun usCustomaryMomentumConversionTest() {
        assertScientificConversion(1.0, (UsTon x (Foot per Second)), 0.68, UsTon x (Mile per Hour), 2)
    }
}