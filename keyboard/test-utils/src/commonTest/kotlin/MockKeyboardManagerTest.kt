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

import com.splendo.kaluga.test.base.UIThreadTest
import com.splendo.kaluga.test.base.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockKeyboardManagerTest : UIThreadTest<MockKeyboardManagerTest.TestContext>() {

    class TestContext(coroutineScope: CoroutineScope) : UIThreadTest.TestContext {
        val mockFocusHandler = MockFocusHandler()
        val keyboardManagerBuilder = MockKeyboardManager.Builder<MockFocusHandler>()
        val keyboardManager = keyboardManagerBuilder.create(coroutineScope)
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> TestContext = { TestContext(it) }

    @Test
    fun testMockKeyboardManager() = testOnUIThread {
        keyboardManagerBuilder.createMock.verify()
        assertFalse(mockFocusHandler.isFocused)

        keyboardManager.show(mockFocusHandler)
        keyboardManager.showMock.verify()
        assertTrue(mockFocusHandler.isFocused)

        keyboardManager.hide()
        keyboardManager.hideMock.verify()
        assertFalse(mockFocusHandler.isFocused)
    }
}
