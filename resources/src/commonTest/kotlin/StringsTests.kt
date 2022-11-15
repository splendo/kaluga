/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.resources

import com.splendo.kaluga.test.base.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class StringsTests : BaseTest() {

    abstract val stringLoader: StringLoader

    @Test
    fun testPlurals() {
        assertEquals("0 hours", "hours_value".quantity(0, stringLoader))
        assertEquals("1 hour", "hours_value".quantity(1, stringLoader))
        assertEquals("2 hours", "hours_value".quantity(2, stringLoader))
    }
}
