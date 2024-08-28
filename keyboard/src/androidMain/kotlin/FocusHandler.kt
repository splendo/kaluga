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

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethod.SHOW_EXPLICIT
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes

/**
 * A [FocusHandler] that focuses on a view with a given ID.
 * @param id the ID of the View to focus on
 * @param handler the [Handler] on which to wait for a Window focus
 */
data class ViewFocusHandler(
    @IdRes private val id: Int,
    private val handler: Handler,
) : FocusHandler {

    /**
     * Constructor
     * @param id the ID of the View to focus on
     */
    constructor(
        @IdRes id: Int,
    ) : this(id, Handler(Looper.getMainLooper()))

    fun requestFocus(activity: Activity?) {
        if (activity == null) return
        val view = activity.findViewById<View>(id) ?: return
        view.requestFocus()
        activity.awaitWindowFocus {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.showSoftInput(view, SHOW_EXPLICIT)
        }
    }

    private fun Activity.awaitWindowFocus(block: Activity.() -> Unit): Boolean = handler.post {
        when {
            isDestroyed || isFinishing -> {}
            hasWindowFocus() -> block()
            else -> awaitWindowFocus(block)
        }
    }
}
