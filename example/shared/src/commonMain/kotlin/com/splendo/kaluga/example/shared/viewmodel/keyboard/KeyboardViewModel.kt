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

package com.splendo.kaluga.example.shared.viewmodel.keyboard

import com.splendo.kaluga.architecture.observable.toUninitializedSubject
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.keyboard.BaseKeyboardManager
import com.splendo.kaluga.keyboard.FocusHandler
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class KeyboardViewModel<FH : FocusHandler>(keyboardManagerBuilder: BaseKeyboardManager.Builder<FH>) :
    BaseLifecycleViewModel(
        keyboardManagerBuilder,
    ) {

    private val _editFieldFocusHandler = MutableStateFlow<FH?>(null)
    val editFieldFocusHandler = _editFieldFocusHandler.toUninitializedSubject(coroutineScope)

    private val keyboardManager: BaseKeyboardManager<FH> = keyboardManagerBuilder.create(coroutineScope)

    init {
        coroutineScope.launch {
            _editFieldFocusHandler.filterNotNull().collect {
                keyboardManager.show(it)
            }
        }
    }

    val showButton = KalugaButton.Plain("show_keyboard".localized(), ButtonStyles.default) {
        _editFieldFocusHandler.value?.let { keyboardManager.show(it) }
    }

    val hideButton = KalugaButton.Plain("hide_keyboard".localized(), ButtonStyles.default) {
        keyboardManager.hide()
    }
}
