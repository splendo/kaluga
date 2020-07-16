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
