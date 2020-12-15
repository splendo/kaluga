package com.splendo.kaluga.example.shared.viewmodel.keyboard

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.keyboard.BaseKeyboardManager
import com.splendo.kaluga.keyboard.KeyboardHostingView

class KeyboardViewModel(val keyboardManagerBuilder: BaseKeyboardManager.Builder, private val keyboardHostingView: KeyboardHostingView) : BaseViewModel() {

    private val keyboardManager: BaseKeyboardManager = keyboardManagerBuilder.create(coroutineScope)

    fun onShowPressed() {
        keyboardManager.show(keyboardHostingView)
    }

    fun onHidePressed() {
        keyboardManager.hide()
    }
}
