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

class TemperatureUnitTest {
    
    @Test
    fun metricToImperialsConversionTest() {
        assertEquals(274.15, Celsius.convert(1.0, Kelvin, 2))
        assertEquals(33.8, Celsius.convert(1.0, Fahrenheit, 1))
        assertEquals(493.47, Celsius.convert(1.0, Rankine, 2))
    }

    @Test
    fun imperialUkToImperialUSConversionTest() {
        assertEquals(-457.87, Kelvin.convert(1.0, Fahrenheit, 2))
        assertEquals(1.8, Kelvin.convert(1.0, Rankine, 1))
    }
}