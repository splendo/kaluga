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

class JoltUnitTest {

    @Test
    fun joltTest() {
        assertEquals(100.0, (Meter per Second per Second per Second).convert(1, Centimeter per Second per Second per Second))
        assertEquals(3.2808399, (Meter per Second per Second per Second).convert(1, Foot per Second per Second per Second,7))
    }
}