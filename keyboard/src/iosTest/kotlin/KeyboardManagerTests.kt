package com.splendo.kaluga.keyboard

import kotlin.test.assertTrue
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIApplication
import platform.UIKit.UITextField

class IOSKeyboardManagerTests : KeyboardManagerTests() {

    private val application = UIApplication.sharedApplication
    private val textField = MockTextField()

    override val builder get() = KeyboardManager.Builder(application)

    override val view: KeyboardHostingView
        get() = textField

    override fun verifyShow() {
        assertTrue(textField.didBecomeFirstResponder)
    }

    override fun verifyDismiss() {
        // Should test resign First responder
    }
}

private class MockTextField : UITextField(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

    var didBecomeFirstResponder = false

    override fun becomeFirstResponder(): Boolean {
        didBecomeFirstResponder = true
        return super.becomeFirstResponder()
    }
}
