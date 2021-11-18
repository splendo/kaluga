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

class SpeedUnitTest {

    @Test
    fun metricSpeedConversionTest() {
        assertEquals(3.6, (Meter per Second).convert(1.0, Kilometer per Hour))
    }

    @Test
    fun imperialSpeedConversionTest() {
        assertEquals(0.68, (Foot per Second).convert(1.0, Mile per Hour, 2))
    }

    @Test
    fun metricToImperialSpeedConversionTest() {
        assertEquals(3.28, (Meter per Second).convert(1.0, Foot per Second, 2))
    }

    @Test
    fun imperialToMetricSpeedConversionTest() {
        assertEquals(1.61, (Mile per Hour).convert(1.0, Kilometer per Hour, 2))
    }
}