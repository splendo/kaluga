package com.splendo.kaluga.example.shared

import com.splendo.kaluga.keyboard.KeyboardManagerBuilder
import com.splendo.kaluga.keyboard.KeyboardView

class ExampleKeyboardManager(keyboardManagerBuilder: KeyboardManagerBuilder, private val keyboardView: KeyboardView) {

    private val keyboardManager = keyboardManagerBuilder.create()

    fun show() {
        keyboardManager.show(keyboardView)
    }

    fun hide() {
        keyboardManager.hide()
    }

}