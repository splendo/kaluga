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

package com.splendo.kaluga.test.mock.keyboard

import com.splendo.kaluga.keyboard.BaseKeyboardManager
import com.splendo.kaluga.keyboard.FocusHandler
import com.splendo.kaluga.test.mock.focus.MockFocusHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class MockKeyboardManager : BaseKeyboardManager {
    class Builder : BaseKeyboardManager.Builder {
        override fun create(coroutineScope: CoroutineScope): BaseKeyboardManager = MockKeyboardManager()
    }

    private val _focusHandler: MutableStateFlow<MockFocusHandler?> = MutableStateFlow(null)

    private val _isShown = MutableStateFlow(false)
    val isShown get() = _isShown.value

    override fun hide() {
        _isShown.value = false
        _focusHandler.value?.removeFocus()
    }

    override fun show(focusHandler: FocusHandler) {
        _isShown.value = true
        _focusHandler.value = focusHandler as? MockFocusHandler
        _focusHandler.value?.requestFocus()
    }
}
