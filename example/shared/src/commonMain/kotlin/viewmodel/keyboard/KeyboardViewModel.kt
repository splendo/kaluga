package com.splendo.kaluga.example.shared.viewmodel.keyboard

import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.keyboard.KeyboardManager
import com.splendo.kaluga.keyboard.KeyboardManagerBuilder
import com.splendo.kaluga.keyboard.KeyboardView
import kotlinx.coroutines.CoroutineScope

class KeyboardViewModel(private val keyboardManagerBuilder: () -> KeyboardManagerBuilder, private val keyboardView: () -> KeyboardView) : BaseViewModel() {

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
        keyboardManager?.show(keyboardView())
    }

    fun onHidePressed() {
        keyboardManager?.hide()
    }

}