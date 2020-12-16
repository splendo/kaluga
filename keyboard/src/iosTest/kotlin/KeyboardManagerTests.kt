package com.splendo.kaluga.keyboard

import com.splendo.kaluga.keyboard.IOSKeyboardManagerTests.IOSKeyboardTestContext
import kotlinx.coroutines.CoroutineScope
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIApplication
import platform.UIKit.UITextField
import kotlin.test.assertTrue

class IOSKeyboardManagerTests : KeyboardManagerTests<IOSKeyboardTestContext>() {

    inner class IOSKeyboardTestContext(coroutineScope: CoroutineScope) : KeyboardTestContext(), CoroutineScope by coroutineScope {
        private val application = UIApplication.sharedApplication
        val textField = MockTextField()

        override val builder get() = KeyboardManager.Builder(application)

        override val view: KeyboardHostingView
            get() = textField
    }

    override fun IOSKeyboardTestContext.verifyShow() {
        assertTrue(textField.didBecomeFirstResponder)
    }

    override fun IOSKeyboardTestContext.verifyDismiss() {
        // Should test resign First responder
    }

    override fun CoroutineScope.createTestContext() = IOSKeyboardTestContext(this)
}

class MockTextField : UITextField(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

    var didBecomeFirstResponder = false

    override fun becomeFirstResponder(): Boolean {
        didBecomeFirstResponder = true
        return super.becomeFirstResponder()
    }
}
