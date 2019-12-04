package com.splendo.kaluga.test

import com.splendo.kaluga.keyboardmanager.KeyboardManagerBuilder
import com.splendo.kaluga.keyboardmanager.KeyboardManagerInterfaceTests
import com.splendo.kaluga.keyboardmanager.KeyboardView
import kotlinx.cinterop.COpaquePointer
import platform.UIKit.UIApplication
import platform.UIKit.UIEvent
import platform.UIKit.UITextField
import platform.objc.sel_registerName
import kotlin.test.*

class IOSKeyboardManagerInterfaceTests: KeyboardManagerInterfaceTests() {

    private val application = MockApplication()
    private val textField = UITextField()

    @BeforeTest
    fun setUp() {
        application.cleanAction()
    }

    override val builder get() = KeyboardManagerBuilder(application)

    override val view: KeyboardView
        get() = textField

    override fun verifyShow() {
        assertTrue(textField.isFirstResponder)
    }

    override fun verifyDismiss() {
        assertEquals(sel_registerName("resignFirstResponder"), application.action)
        assertNull(application.to)
        assertNull(application.from)
        assertNull(application.forEvent)
    }
}

private class MockApplication : UIApplication() {

    var action: COpaquePointer? = null
    var to: Any? = null
    var from: Any? = null
    var forEvent: UIEvent? = null

    external override fun sendAction(action: COpaquePointer?, to: Any?, from: Any?, forEvent: UIEvent?): Boolean {
        return super.sendAction(action, to, from, forEvent)
    }

    fun cleanAction() {
        action = null
        to = null
        from = null
        forEvent = null
    }
}
