package com.splendo.kaluga.example.shared

import com.splendo.kaluga.keyboardmanager.KeyboardManagerBuilder
import com.splendo.kaluga.keyboardmanager.KeyboardView

class ExampleKeyboardManager(private val keyboardManagerBuilder: KeyboardManagerBuilder, private val keyboardView: KeyboardView) {

    private val keyboardManager = keyboardManagerBuilder.create()

    fun show() {
        keyboardManager.show(keyboardView)
    }

    fun hide() {
        keyboardManager.dismiss()
    }

}