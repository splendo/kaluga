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

import platform.UIKit.UIView

/**
 * A [FocusHandler] that focuses on a given [UIView]
 * @param view the [UIView] to focus on
 */
class UIKitFocusHandler(val view: UIView) : FocusHandler {
    fun requestFocus() {
        if (view.canBecomeFirstResponder) {
            view.becomeFirstResponder()
        }
    }
}

/**
 * A [FocusHandler] that stores a given [Value] to focus on.
 * This generic implementation allows for usage from SwiftUI.
 * @param Value the type of Value to focus on.
 */
class ValueFocusHandler<Value>(val value: Value) : FocusHandler
