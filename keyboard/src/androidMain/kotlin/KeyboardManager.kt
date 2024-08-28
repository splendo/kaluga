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

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import kotlinx.coroutines.CoroutineScope

/**
 * A [BaseKeyboardManager] that takes a [ViewFocusHandler]. Used managing the keyboard in XML Views.
 * @param lifecycleManagerObserver The [LifecycleManagerObserver] to observe lifecycle changes.
 * @param coroutineScope The [CoroutineScope] managing the keyboard lifecycle.
 */
class ViewKeyboardManager(
    private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    coroutineScope: CoroutineScope,
) : BaseKeyboardManager<ViewFocusHandler>, CoroutineScope by coroutineScope {

    /**
     * A [BaseKeyboardManager.Builder] for creating a [ViewKeyboardManager]
     * @param lifecycleManagerObserver The [LifecycleManagerObserver] to observe lifecycle changes.
     */
    class Builder(
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    ) : BaseKeyboardManager.Builder<ViewFocusHandler>, ActivityLifecycleSubscribable by lifecycleManagerObserver {
        override fun create(coroutineScope: CoroutineScope) = ViewKeyboardManager(lifecycleManagerObserver, coroutineScope)
    }

    override fun show(focusHandler: ViewFocusHandler) {
        focusHandler.requestFocus(lifecycleManagerObserver.manager?.activity)
    }

    override fun hide() {
        val managedActivity = lifecycleManagerObserver.manager?.activity
        managedActivity?.let { activity ->
            activity.currentFocus?.clearFocus()
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.let {
                if (it.isAcceptingText) {
                    it.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
                }
            }
        }
    }
}
