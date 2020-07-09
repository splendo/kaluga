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

package com.splendo.kaluga.example.shared.viewmodel.keyboard

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.keyboard.KeyboardHostingView
import com.splendo.kaluga.keyboard.KeyboardManager
import com.splendo.kaluga.keyboard.KeyboardManagerBuilder
import kotlinx.coroutines.CoroutineScope

class KeyboardViewModel(private val keyboardManagerBuilder: () -> KeyboardManagerBuilder, private val keyboardHostingView: () -> KeyboardHostingView) : BaseViewModel() {

    private var keyboardManager: KeyboardManager? = null

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        keyboardManager = keyboardManagerBuilder().create()
    }

    override fun onPause() {
        super.onPause()

        keyboardManager = null
    }

    fun onShowPressed() {
        keyboardManager?.show(keyboardHostingView())
    }

    fun onHidePressed() {
        keyboardManager?.hide()
    }
}
