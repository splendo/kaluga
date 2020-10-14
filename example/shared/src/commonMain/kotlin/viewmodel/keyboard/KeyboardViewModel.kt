package com.splendo.kaluga.example.shared.viewmodel.keyboard

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.keyboard.KeyboardHostingView
import com.splendo.kaluga.keyboard.KeyboardManager

class KeyboardViewModel(val keyboardManagerBuilder: KeyboardManager.Builder, private val keyboardHostingView: KeyboardHostingView) : BaseViewModel() {

    private val keyboardManager: KeyboardManager = keyboardManagerBuilder.create(coroutineScope)

    fun onShowPressed() {
        keyboardManager.show(keyboardHostingView)
    }

    fun onHidePressed() {
        keyboardManager.hide()
    }
}
