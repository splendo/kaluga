package com.splendo.kaluga.keyboard

import com.splendo.kaluga.keyboard.UIKitKeyboardManagerTests.IOSKeyboardTestContext
import kotlinx.coroutines.CoroutineScope
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIApplication
import platform.UIKit.UITextField
import kotlin.test.assertTrue

class UIKitKeyboardManagerTests : KeyboardManagerTests<UIKitFocusHandler, IOSKeyboardTestContext>() {

    class IOSKeyboardTestContext(coroutineScope: CoroutineScope) :
        KeyboardTestContext<UIKitFocusHandler>(),
        CoroutineScope by coroutineScope {
        private val application = UIApplication.sharedApplication
        val textField = MockTextField()

        override val builder get() = UIKitKeyboardManager.Builder(application)

        override val focusHandler: UIKitFocusHandler
            get() = UIKitFocusHandler(textField)

        override fun verifyShow() {
            assertTrue(textField.didBecomeFirstResponder)
        }

        override fun verifyDismiss() {
            // Should test resign First responder
        }
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> IOSKeyboardTestContext = { IOSKeyboardTestContext(it) }
}

class MockTextField : UITextField(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

    var didBecomeFirstResponder = false

    override fun becomeFirstResponder(): Boolean {
        didBecomeFirstResponder = true
        return super.becomeFirstResponder()
    }
}
