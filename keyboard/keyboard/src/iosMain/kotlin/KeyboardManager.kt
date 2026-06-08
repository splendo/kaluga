/*

Copyright 2022 Splendo Consulting B.V. The Netherlands

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package com.splendo.kaluga.keyboard

import kotlinx.coroutines.CoroutineScope
import platform.UIKit.UIApplication
import platform.darwin.sel_registerName

/**
 * A [BaseKeyboardManager] that takes a [UIKitFocusHandler]. Used for managing the keyboard in UIKit views.
 * @param application The [UIApplication] that the keyboard is running in.
 */
class UIKitKeyboardManager(private val application: UIApplication) : BaseKeyboardManager<UIKitFocusHandler> {

    /**
     * Builder for a [UIKitKeyboardManager]
     * @param application The [UIApplication] that the keyboard is running in.
     */
    class Builder(private val application: UIApplication = UIApplication.sharedApplication) : BaseKeyboardManager.Builder<UIKitFocusHandler> {
        override fun create(coroutineScope: CoroutineScope) = UIKitKeyboardManager(application)
    }

    override fun show(focusHandler: UIKitFocusHandler) {
        focusHandler.requestFocus()
    }

    override fun hide() {
        application.sendAction(sel_registerName("resignFirstResponder"), null, null, null)
    }
}

/**
 * A [BaseKeyboardManager] that takes a [ValueFocusHandler]. Uses for managing keyboard in a generic way so that it allows for usage from SwiftUI.
 * @param Value the type of value to expect
 * @param onFocusOnValue callback method to indicate how to handle a focus change to a [Value]. When `null` is provided, they keyboard should be dismissed.
 */
open class ValueKeyboardManager<Value>(private val onFocusOnValue: (Value?) -> Unit) : BaseKeyboardManager<ValueFocusHandler<Value>> {
    override fun show(focusHandler: ValueFocusHandler<Value>) {
        onFocusOnValue(focusHandler.value)
    }

    override fun hide() {
        onFocusOnValue(null)
    }
}
