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

package com.splendo.kaluga.test.mock.focus

import com.splendo.kaluga.keyboard.FocusHandler
import kotlinx.coroutines.flow.MutableStateFlow

open class BaseMockFocusHandler {
    private val _isFocused = MutableStateFlow(false)
    val isFocused: Boolean
        get() = _isFocused.value

    protected fun giveFocus() {
        _isFocused.value = true
    }

    fun removeFocus() {
        _isFocused.value = false
    }
}

expect class MockFocusHandler constructor() : BaseMockFocusHandler, FocusHandler {
    fun focus()
}
