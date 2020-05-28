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

internal class FormattedTests {
    lateinit var initialValue: Dummy
    lateinit var formatter: MockFormatter
    lateinit var modifier: MockModifier
    lateinit var formatted: MockFormatted
    @BeforeTest
    fun initialize() {
        initialValue = Dummy()
        formatter = MockFormatter()
        modifier = MockModifier()
        formatted = MockFormatted(value = initialValue, formatter = formatter, modifier = modifier)
    }

    private fun new(value: Dummy): MockFormatted {
        modifier.apply.toReturn = value
        formatted.spawn.toReturn = formatted
        return formatted.new(value)
    }

    @Test
    fun `new() calls spawn`() {
        new(Dummy())
        assertTrue(formatted.spawn.executed, "spawn was not called")
    }

    @Test
    fun `new() calls spawn with modifier`() {
        new(Dummy())
        assertEquals(formatted.spawn.args.modifier, modifier, "spawn called with wrong or without modifier")
    }

    @Test
    fun `new() calls spawn with formatter`() {
        new(Dummy())
        assertEquals(formatted.spawn.args.formatter, formatter, "spawn called with wrong formatter")
    }

    @Test
    fun `new() calls spawn with a new value`() {
        val newValue = Dummy()
        new(newValue)
        assertEquals(formatted.spawn.args.value, newValue, "spawn called with wrong value")
    }

    @Test
    fun `new() applies modifier for value`() {
        val newValue = Dummy()
        new(newValue)
        assertTrue(modifier.apply.executed, "apply was not executed")
        assertEquals(modifier.apply.args.value, newValue, "apply executed with a wrong value")
    }

    @Test
    fun `formatted() returns formatted value`() {
        val dummyString = "dummy value"
        formatter.string.toReturn = dummyString
        val formattedValue = formatted.formatted()

        assertEquals(dummyString, formattedValue, "wrong formatted string")
    }
}
