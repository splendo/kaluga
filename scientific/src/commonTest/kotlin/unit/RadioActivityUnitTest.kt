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

package unit

import com.splendo.kaluga.scientific.unit.Becquerel
import com.splendo.kaluga.scientific.unit.Centibecquerel
import com.splendo.kaluga.scientific.unit.Centicurie
import com.splendo.kaluga.scientific.unit.Curie
import com.splendo.kaluga.scientific.unit.Decabecquerel
import com.splendo.kaluga.scientific.unit.Decacurie
import com.splendo.kaluga.scientific.unit.Decibecquerel
import com.splendo.kaluga.scientific.unit.Decicurie
import com.splendo.kaluga.scientific.unit.Gigabecquerel
import com.splendo.kaluga.scientific.unit.Gigacurie
import com.splendo.kaluga.scientific.unit.Hectobecquerel
import com.splendo.kaluga.scientific.unit.Hectocurie
import com.splendo.kaluga.scientific.unit.Kilobecquerel
import com.splendo.kaluga.scientific.unit.Kilocurie
import com.splendo.kaluga.scientific.unit.Megabecquerel
import com.splendo.kaluga.scientific.unit.Megacurie
import com.splendo.kaluga.scientific.unit.Microbecquerel
import com.splendo.kaluga.scientific.unit.Microcurie
import com.splendo.kaluga.scientific.unit.Millibecquerel
import com.splendo.kaluga.scientific.unit.Millicurie
import com.splendo.kaluga.scientific.unit.Nanobecquerel
import com.splendo.kaluga.scientific.unit.Nanocurie
import com.splendo.kaluga.scientific.unit.convert
import kotlin.test.Test
import kotlin.test.assertEquals

class RadioActivityUnitTest {

    @Test
    fun radioActivityConversionTest() {
        assertEquals(1e+9, Becquerel.convert(1, Nanobecquerel))
        assertEquals(1e+6, Becquerel.convert(1, Microbecquerel))
        assertEquals(1000.0, Becquerel.convert(1, Millibecquerel))
        assertEquals(100.0, Becquerel.convert(1, Centibecquerel))
        assertEquals(10.0, Becquerel.convert(1, Decibecquerel))
        assertEquals(0.1, Becquerel.convert(1, Decabecquerel))
        assertEquals(0.01, Becquerel.convert(1, Hectobecquerel))
        assertEquals(0.001, Becquerel.convert(1, Kilobecquerel))
        assertEquals(1e-6, Becquerel.convert(1, Megabecquerel))
        assertEquals(1e-9, Becquerel.convert(1, Gigabecquerel))
        assertEquals(2.7027027027027E-11, Becquerel.convert(1, Curie,24))

        assertEquals(1e+9, Curie.convert(1, Nanocurie))
        assertEquals(1e+6, Curie.convert(1, Microcurie))
        assertEquals(1000.0, Curie.convert(1, Millicurie))
        assertEquals(100.0, Curie.convert(1, Centicurie))
        assertEquals(10.0, Curie.convert(1, Decicurie))
        assertEquals(0.1, Curie.convert(1, Decacurie))
        assertEquals(0.01, Curie.convert(1, Hectocurie))
        assertEquals(0.001, Curie.convert(1, Kilocurie))
        assertEquals(1e-6, Curie.convert(1, Megacurie))
        assertEquals(1e-9, Curie.convert(1, Gigacurie))
    }
}