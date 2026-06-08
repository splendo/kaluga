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

package com.splendo.kaluga.base.text

import com.splendo.kaluga.base.utils.KalugaLocale.Companion.createLocale
import com.splendo.kaluga.test.base.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsTest : BaseTest() {

    companion object {
        private val UnitedStates = createLocale("en", "US")

        // Turkish casing is the classic locale-sensitive case: "i" upper-cases to the dotted "İ"
        // (U+0130) and "I" lower-cases to the dotless "ı" (U+0131), unlike the invariant Latin mapping.
        private val Turkey = createLocale("tr", "TR")
    }

    @Test
    fun testLowerCasedUsesLocale() {
        assertEquals("title", "TITLE".lowerCased(UnitedStates))
        assertEquals("tıtle", "TITLE".lowerCased(Turkey))
    }

    @Test
    fun testUpperCasedUsesLocale() {
        assertEquals("ISTANBUL", "istanbul".upperCased(UnitedStates))
        assertEquals("İSTANBUL", "istanbul".upperCased(Turkey))
    }
}
