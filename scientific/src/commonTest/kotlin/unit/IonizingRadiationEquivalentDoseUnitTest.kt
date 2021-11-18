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

class IonizingRadiationEquivalentDoseUnitTest {
    
    @Test
    fun ionizingRadiationEquivalentDoseConversionTest() {
        assertEquals(1e+9, Sievert.convert(1, Nanosievert))
        assertEquals(1e+6, Sievert.convert(1, Microsievert))
        assertEquals(1000.0, Sievert.convert(1, Millisievert))
        assertEquals(100.0, Sievert.convert(1, Centisievert))
        assertEquals(10.0, Sievert.convert(1, Decisievert))
        assertEquals(0.1, Sievert.convert(1, Decasievert))
        assertEquals(0.01, Sievert.convert(1, Hectosievert))
        assertEquals(0.001, Sievert.convert(1, Kilosievert))
        assertEquals(1e-6, Sievert.convert(1, Megasievert))
        assertEquals(1e-9, Sievert.convert(1, Gigasievert))
        
        assertEquals(100.0, Sievert.convert(1, RoentgenEquivalentMan))

        assertEquals(1e+9, RoentgenEquivalentMan.convert(1, NanoroentgenEquivalentMan))
        assertEquals(1e+6, RoentgenEquivalentMan.convert(1, MicroroentgenEquivalentMan))
        assertEquals(1000.0, RoentgenEquivalentMan.convert(1, MilliroentgenEquivalentMan))
        assertEquals(100.0, RoentgenEquivalentMan.convert(1, CentiroentgenEquivalentMan))
        assertEquals(10.0, RoentgenEquivalentMan.convert(1, DeciroentgenEquivalentMan))
        assertEquals(0.1, RoentgenEquivalentMan.convert(1, DecaroentgenEquivalentMan))
        assertEquals(0.01, RoentgenEquivalentMan.convert(1, HectoroentgenEquivalentMan))
        assertEquals(0.001, RoentgenEquivalentMan.convert(1, KiloroentgenEquivalentMan))
        assertEquals(1e-6, RoentgenEquivalentMan.convert(1, MegaroentgenEquivalentMan))
        assertEquals(1e-9, RoentgenEquivalentMan.convert(1, GigaroentgenEquivalentMan))
    }
}