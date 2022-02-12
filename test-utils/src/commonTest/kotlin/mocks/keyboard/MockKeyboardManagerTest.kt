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

package mocks.keyboard

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.mock.focus.MockFocusHandler
import com.splendo.kaluga.test.mock.keyboard.MockKeyboardManager
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockKeyboardManagerTest {
    private val keyboardManagerBuilder = MockKeyboardManager.Builder()
    private val focusHandler = MockFocusHandler()

    @Test
    fun test_initial_manager_state() = testKeyboardManager {
        assertFalse { it.isShown }
    }

    @Test
    fun test_show_keyboard() = testKeyboardManager {
        assertFalse { it.isShown }
        it.show(focusHandler)
        assertTrue { focusHandler.isFocused }
        assertTrue { it.isShown }
    }

    @Test
    fun test_hide_keyboard() = testKeyboardManager {
        assertFalse { it.isShown }
        it.show(focusHandler)
        assertTrue { focusHandler.isFocused }
        assertTrue { it.isShown }

        it.hide()
        assertFalse { focusHandler.isFocused }
        assertFalse { it.isShown }
    }

    @Test
    fun test_show_hide_keyboard_multiple_times() = testKeyboardManager {
        assertFalse { it.isShown }
        for(i in 0..10) {
            it.show(focusHandler)
            assertTrue { focusHandler.isFocused }
            assertTrue { it.isShown }

            it.hide()
            assertFalse { focusHandler.isFocused }
            assertFalse { it.isShown }
        }

    }

    private fun testKeyboardManager(block: (keyboardManager: MockKeyboardManager) -> Unit) = runBlocking {
        block(keyboardManagerBuilder.create(this) as MockKeyboardManager)
    }
}