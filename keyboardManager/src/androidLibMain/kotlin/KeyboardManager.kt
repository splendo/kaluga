package com.splendo.kaluga.keyboardmanager

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

/*
Copyright 2019 Splendo Consulting B.V. The Netherlands

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

actual typealias KeyboardView = View

actual class KeyboardManagerBuilder(private val activity: Activity) : BaseKeyboardManagerBuilder() {
    override fun create() = KeyboardInterface(activity)
}

actual class KeyboardInterface(
    private val activity: Activity
) : BaseKeyboardManager() {

    private val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

    override fun show(keyboardView: KeyboardView) {
        inputMethodManager?.let {
            keyboardView.requestFocus()
            it.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

    override fun dismiss() {
        inputMethodManager?.let {
            if (it.isAcceptingText) {
                it.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            }
        }
    }
}
