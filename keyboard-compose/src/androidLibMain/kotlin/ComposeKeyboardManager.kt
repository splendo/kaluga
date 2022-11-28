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

package com.splendo.kaluga.keyboard.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import com.splendo.kaluga.architecture.compose.lifecycle.ComposableLifecycleSubscribable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.keyboard.BaseKeyboardManager
import kotlinx.coroutines.CoroutineScope
import java.util.WeakHashMap

class ComposeKeyboardManager(internal var currentFocusManager: FocusManager? = null) : BaseKeyboardManager<ComposeFocusHandler> {

    class Builder : BaseKeyboardManager.Builder<ComposeFocusHandler>,
        ComposableLifecycleSubscribable {

        private val builtManagers = WeakHashMap<Int, ComposeKeyboardManager>()

        override fun create(coroutineScope: CoroutineScope): BaseKeyboardManager<ComposeFocusHandler> = ComposeKeyboardManager().also {
            builtManagers[it.hashCode()] = it
        }

        override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = { content ->
            val focusManager = LocalFocusManager.current
            builtManagers.values.forEach {
                it.currentFocusManager = focusManager
            }
            content()
        }

    }

    override fun show(focusHandler: ComposeFocusHandler) {
        focusHandler.focusRequester.requestFocus()
    }

    override fun hide() {
        currentFocusManager?.clearFocus()
    }
}
