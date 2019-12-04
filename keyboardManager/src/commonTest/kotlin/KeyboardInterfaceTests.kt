package com.splendo.kaluga.keyboardmanager

import kotlin.test.Test

abstract class KeyboardManagerInterfaceTests {

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
        builder.create().dismiss()
        verifyDismiss()
    }

    abstract fun verifyDismiss()

}
