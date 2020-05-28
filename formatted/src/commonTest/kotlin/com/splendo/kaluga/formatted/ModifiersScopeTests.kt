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

package com.splendo.kaluga.formatted

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ModifiersScopeTests {
    lateinit var modifier1: MockModifier
    lateinit var modifier2: MockModifier
    lateinit var modifiersScope: ModifiersScope<Dummy>

    @BeforeTest
    fun initialize() {
        modifier1 = MockModifier()
        modifier2 = MockModifier()
        modifiersScope = ModifiersScope(modifier1, modifier2)
    }

    @Test
    fun `apply() applies all modifiers`() {
        modifier1.apply.toReturn = Dummy()
        modifier2.apply.toReturn = Dummy()

        modifiersScope.apply(Dummy())

        assertTrue(modifier1.apply.executed, "first modifier was not applied")
        assertTrue(modifier2.apply.executed, "second modifier was not applied")
    }

    @Test
    fun `apply() passes result of previous application to the next`() {

        val modifier1Result = Dummy()
        modifier1.apply.toReturn = modifier1Result

        val modifier2Result = Dummy()
        modifier2.apply.toReturn = modifier2Result

        val input = Dummy()
        val output = modifiersScope.apply(input)

        assertEquals(modifier1.apply.args.value, input, "it doesn't pass input to the first modifier")
        assertEquals(modifier2.apply.args.value, modifier1Result, "it doesn't pass output of the first modifier to the second")
        assertEquals(output, modifier2Result, "it doesn't pass output of the second modifier to the output")
    }
}
