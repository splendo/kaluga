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

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.keyboard.FocusHandler
import com.splendo.kaluga.test.architecture.UIThreadViewModelTest
import com.splendo.kaluga.test.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockKeyboardManagerTest : UIThreadViewModelTest<MockKeyboardManagerTest.TestContext, MockKeyboardManagerTest.ViewModel>() {

    class ViewModel(keyboardManagerBuilder: MockKeyboardManager.Builder, val focusHandler: FocusHandler) : BaseViewModel() {

        val keyboardManager = keyboardManagerBuilder.create(coroutineScope)

        fun show() = keyboardManager.show(focusHandler)
        fun hide() = keyboardManager.hide()
    }

    class TestContext : ViewModelTestContext<ViewModel> {
        val mockFocusHandler = MockFocusHandler()
        val keyboardManagerBuilder = MockKeyboardManager.Builder()
        override val viewModel: ViewModel = ViewModel(keyboardManagerBuilder, mockFocusHandler)
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> TestContext = { TestContext() }

    @Test
    fun testMockKeyboardManager() = testOnUIThread {
        keyboardManagerBuilder.createMock.verify()
        val keyboardManager = keyboardManagerBuilder.builtKeyboardManagers.first()
        assertFalse(mockFocusHandler.isFocused)
        viewModel.show()
        keyboardManager.showMock.verify()
        assertTrue(mockFocusHandler.isFocused)

        viewModel.hide()
        keyboardManager.hideMock.verify()
        assertFalse(mockFocusHandler.isFocused)
    }
}