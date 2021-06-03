package com.splendo.kaluga.example.shared.viewmodel.keyboard

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.keyboard.BaseKeyboardManager
import com.splendo.kaluga.keyboard.FocusHandler

class KeyboardViewModel(keyboardManagerBuilder: BaseKeyboardManager.Builder, private val editFieldFocusHandler: FocusHandler) : BaseViewModel() {

    private val keyboardManager: BaseKeyboardManager = keyboardManagerBuilder.create(coroutineScope)

    fun onShowPressed() {
        keyboardManager.show(editFieldFocusHandler)
    }

    fun onHidePressed() {
        keyboardManager.hide()
    }
}
