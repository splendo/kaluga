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

class LengthUnitTest {

    // ##### Same length unit table conversions #####

    @Test
    fun meterConversionTest() {
        assertEquals(1e+9, Meter.convert(1.0, Nanometer))
        assertEquals(1e+6, Meter.convert(1.0, Micrometer))
        assertEquals(1_000.0, Meter.convert(1.0, Millimeter))
        assertEquals(100.0, Meter.convert(1.0, Centimeter))
        assertEquals(10.0, Meter.convert(1.0, Decimeter))
        assertEquals(0.1, Meter.convert(1.0, Decameter))
        assertEquals(0.01, Meter.convert(1.0, Hectometer))
        assertEquals(0.001, Meter.convert(1.0, Kilometer))
        assertEquals(1e-6, Meter.convert(1.0, Megameter))
        assertEquals(1e-9, Meter.convert(1.0, Gigameter))
    }

    @Test
    fun feetConversionTest() {
        assertEquals(12.0, Foot.convert(1.0, Inch))
        assertEquals(0.33333333, Foot.convert(1.0, Yard, 8))
        assertEquals(0.00018939, Foot.convert(1.0, Mile, 8))
    }

    // ##### Mixed mass unit table conversions #####

    @Test
    fun meterToFeetConversionTest() {
        assertEquals(39.37007874, Meter.convert(1.0, Inch, 8))
        assertEquals(1.0936133, Meter.convert(1.0, Yard, 8))
        assertEquals(0.00062137, Meter.convert(1.0, Mile, 8))
    }

    @Test
    fun feetToMeterConversionTest() {
        assertEquals(304800000.0, Foot.convert(1.0, Nanometer))
        assertEquals(304800.0, Foot.convert(1.0, Micrometer))
        assertEquals(304.8, Foot.convert(1.0, Millimeter))
        assertEquals(30.48, Foot.convert(1.0, Centimeter))
        assertEquals(3.048, Foot.convert(1.0, Decimeter))
        assertEquals(0.03048, Foot.convert(1.0, Decameter))
        assertEquals(0.003048, Foot.convert(1.0, Hectometer))
        assertEquals(0.0003048, Foot.convert(1.0, Kilometer))
        assertEquals(0.0000003, Foot.convert(1.0, Megameter, 8))
        assertEquals(0.000000000305, Foot.convert(1.0, Gigameter, 12))
    }


}