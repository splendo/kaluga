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

package com.splendo.kaluga.test.keyboard

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockFocusHandlerTest {
    @Test
    fun test_initial_state() = testFocusHandler {
        assertFalse(isFocused)
    }

    @Test
    fun test_give_focus() = testFocusHandler {
        assertFalse(isFocused)
        simulateGiveFocus()
        assertTrue(isFocused)
    }

    @Test
    fun test_remove_focus() = testFocusHandler {
        assertFalse(isFocused)
        simulateGiveFocus()
        assertTrue(isFocused)

        simulateRemoveFocus()
        assertFalse(isFocused)
    }

    @Test
    fun test_give_remove_multiple_times() = testFocusHandler {
        assertFalse(isFocused)
        repeat(10) {
            simulateGiveFocus()
            assertTrue(isFocused)

            simulateRemoveFocus()
            assertFalse(isFocused)
        }
    }

    private fun testFocusHandler(block: MockFocusHandler.() -> Unit) = MockFocusHandler().block()
}