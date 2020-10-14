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

package com.splendo.kaluga.keyboard

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

actual typealias KeyboardHostingView = Int

actual class KeyboardManager(
    private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    coroutineScope: CoroutineScope
) : BaseKeyboardManager, CoroutineScope by coroutineScope {

    actual class Builder(
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver()
    ) : BaseKeyboardManagerBuilder(), LifecycleSubscribable by lifecycleManagerObserver {
        override fun create(coroutineScope: CoroutineScope) = KeyboardManager(lifecycleManagerObserver, coroutineScope)
    }

    private var activity: Activity? = null
        set(value) {
            field = value
            inputMethodManager = value?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        }
    private var inputMethodManager: InputMethodManager? = null

    init {
        launch {
            lifecycleManagerObserver.managerState.collect {
                activity = it?.activity
            }
        }
    }

    override fun show(keyboardHostingView: KeyboardHostingView) {
        inputMethodManager?.let {
            activity?.findViewById<View>(keyboardHostingView)?.requestFocus()
            it.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

    override fun hide() {
        inputMethodManager?.let {
            if (it.isAcceptingText) {
                it.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            }
        }
    }
}
