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

import kotlin.test.Test

class PressureUnitTest {

    @Test
    fun pressureConversionTest() {
        assertScientificConversion("1", Pascal, "1e+9", Nanopascal)
        assertScientificConversion("1", Pascal, "1e+6", Micropascal)
        assertScientificConversion("1", Pascal, "1000.0", Millipascal)
        assertScientificConversion("1", Pascal, "100.0", Centipascal)
        assertScientificConversion("1", Pascal, "10.0", Decipascal)
        assertScientificConversion("1", Pascal, "0.1", Decapascal)
        assertScientificConversion("1", Pascal, "0.01", Hectopascal)
        assertScientificConversion("1", Pascal, "0.001", Kilopascal)
        assertScientificConversion("1", Pascal, "1e-6", Megapascal)
        assertScientificConversion("1", Pascal, "1e-9", Gigapascal)

        assertScientificConversion("1", Pascal, "1e-5", Bar)
        assertScientificConversion("1", Pascal, "10.0", Barye)
        assertScientificConversion("1", Pascal, "0.00750062", Torr, 8)
        assertScientificConversion("1", Pascal, "9.86923e-6", Atmosphere, 11)
        assertScientificConversion("1", Pascal, "0.00750062", MillimeterOfMercury, 8)
        assertScientificConversion("1", Pascal, "0.10197162", MillimeterOfWater, 8)
        assertScientificConversion("1", MillimeterOfWater, "0.1", CentimeterOfWater)

        assertScientificConversion("1", Pascal, "0.0001450377", PoundSquareInch, 10)
    }
}
