package com.splendo.kaluga.keyboardmanager

import kotlin.test.Test

abstract class KeyboardManagerTests {

    abstract val builder: KeyboardManagerBuilder
    abstract val view: KeyboardView

    @Test
    fun testShow() {
        builder.create().show(view)
        verifyShow()
    }

    abstract fun verifyShow()

    @Test
    fun testDismiss() {
        builder.create().hide()
        verifyDismiss()
    }

    abstract fun verifyDismiss()

}
