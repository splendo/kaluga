package com.splendo.kaluga.example.shared

import com.splendo.kaluga.keyboardManager.KeyboardManagerBuilder
import com.splendo.kaluga.keyboardManager.KeyboardView

class ExampleKeyboardManager(private val keyboardManagerBuilder: KeyboardManagerBuilder, private val keyboardView: KeyboardView) {

    val keyboardManager = keyboardManagerBuilder.create()

    fun show() {
        keyboardManager.show(keyboardView)
    }

    fun hide() {
        keyboardManager.dismiss()
    }

}