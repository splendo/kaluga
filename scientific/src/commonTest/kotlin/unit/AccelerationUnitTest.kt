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

import com.splendo.kaluga.base.utils.toDecimal
import kotlin.test.Test

class AccelerationUnitTest {

    @Test
    fun accelerationConversionTest() {
        assertScientificConversion(
            "1",
            (Meter per Second per Second),
            "11.811024",
            Foot per Millisecond per Hour,
            6,
        )

        assertScientificConversion("100", Gal, "1", Meter per Second per Second)
        assertScientificConversion("1.0", Gal, "1e+9", NanoGal)
        assertScientificConversion("1.0", Gal, "1e+6", MicroGal)
        assertScientificConversion("1.0", Gal, "1000.0", MilliGal)
        assertScientificConversion("1.0", Gal, "100.0", CentiGal)
        assertScientificConversion("1.0", Gal, "10.0", DeciGal)
        assertScientificConversion("1.0", Gal, "0.1", DecaGal)
        assertScientificConversion("1.0", Gal, "0.01", HectoGal)
        assertScientificConversion("1.0", Gal, "0.001", KiloGal)
        assertScientificConversion("1.0", Gal, "1e-6", MegaGal)
        assertScientificConversion("1.0", Gal, "1e-9", GigaGal)

        assertScientificConversion(MetricStandardGravityAcceleration.decimalValue, Meter per Second per Second, 1.toDecimal(), GUnit)
        assertScientificConversion("1.0", GUnit, "1e+9", Nanog)
        assertScientificConversion("1.0", GUnit, "1e+6", Microg)
        assertScientificConversion("1.0", GUnit, "1000.0", Millig)
        assertScientificConversion("1.0", GUnit, "100.0", Centig)
        assertScientificConversion("1.0", GUnit, "10.0", Decig)
        assertScientificConversion("1.0", GUnit, "0.1", Decag)
        assertScientificConversion("1.0", GUnit, "0.01", Hectog)
        assertScientificConversion("1.0", GUnit, "0.001", Kilog)
        assertScientificConversion("1.0", GUnit, "1e-6", Megag)
        assertScientificConversion("1.0", GUnit, "1e-9", Gigag)
    }
}
