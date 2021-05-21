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
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.focus.FocusRequester
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.getOrPutAndRemoveOnDestroyFromCache
import com.splendo.kaluga.architecture.lifecycle.lifecycleManagerObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ComposeFocusHandler(private val focusRequester: FocusRequester) : FocusHandler {
    override fun requestFocus() {
        focusRequester.requestFocus()
    }
}

class AndroidFocusHandler(
    private val activity: Activity,
    @IdRes private val id: Int
) : FocusHandler {
    override fun requestFocus() {
        activity.findViewById<View>(id).requestFocus()
    }
}

actual class KeyboardManager(
    private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    coroutineScope: CoroutineScope
) : BaseKeyboardManager, CoroutineScope by coroutineScope {

    actual class Builder(
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver()
    ) : BaseKeyboardManager.Builder, LifecycleSubscribable by lifecycleManagerObserver {
        actual override fun create(coroutineScope: CoroutineScope) = KeyboardManager(lifecycleManagerObserver, coroutineScope)
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

    override fun show(focusHandler: FocusHandler) {
        inputMethodManager?.let {
            focusHandler.requestFocus()
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

/**
 * @return A [KeyboardManager.Builder] which can be used to manipulate the soft keyboard while this Activity is active.
 * Will be created if need but only one instance will exist.
 *
 * Warning: Do not attempt to use this builder outside of the lifespan of the Activity.
 * Instead, for example use a [com.splendo.kaluga.architecture.viewmodel.ViewModel],
 * which can automatically track which Activity is active for it.
 *
 */
fun AppCompatActivity.keyboardManagerBuilder(): KeyboardManager.Builder = getOrPutAndRemoveOnDestroyFromCache {
    KeyboardManager.Builder(lifecycleManagerObserver())
}
