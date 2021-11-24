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

class YankUnitTest {

    @Test
    fun yankConversionTest() {
        assertEquals(6.0e-7, (Dyne per Minute).convert(1.0, Kilonewton per Hour))
        assertEquals(1.12, (ImperialTonForce per Second).convert(1.0, UsTonForce per Second))
    }
}