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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import com.splendo.kaluga.architecture.compose.lifecycle.ComposableLifecycleSubscribable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.keyboard.BaseKeyboardManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.util.WeakHashMap

/**
 * A [BaseKeyboardManager] that takes a [ComposeFocusHandler]. Uses for managing the keyboard in Compose views.
 * @param currentFocusManager The initial [FocusManager] to manage the focus.
 * @param focusHandlerQueue a [MutableSharedFlow] used to manage the [FocusRequester] to focus on.
 */
class ComposeKeyboardManager(internal var currentFocusManager: FocusManager? = null, private val focusHandlerQueue: MutableSharedFlow<FocusRequester?>) :
    BaseKeyboardManager<ComposeFocusHandler> {

        /**
         * A [BaseKeyboardManager.Builder] for creating a [ComposeKeyboardManager]
         */
        class Builder :
            BaseKeyboardManager.Builder<ComposeFocusHandler>,
            ComposableLifecycleSubscribable {

            private val builtManagers = WeakHashMap<Int, ComposeKeyboardManager>()
            private val focusHandlerQueue = MutableStateFlow<FocusRequester?>(null)

            override fun create(
                coroutineScope: CoroutineScope,
            ): BaseKeyboardManager<ComposeFocusHandler> = ComposeKeyboardManager(focusHandlerQueue = focusHandlerQueue).also {
                builtManagers[it.hashCode()] = it
            }

            override val modifier: @Composable BaseLifecycleViewModel.(
                @Composable BaseLifecycleViewModel.() -> Unit,
            ) -> Unit = { content ->
                val windowInfo = LocalWindowInfo.current
                val focusManager = LocalFocusManager.current

                val currentFocus = focusHandlerQueue.collectAsState()
                currentFocus.value?.let {
                    LaunchedEffect(windowInfo) {
                        snapshotFlow { windowInfo.isWindowFocused }.first { it }
                        it.requestFocus()
                        focusHandlerQueue.tryEmit(null)
                    }
                }

                builtManagers.values.forEach {
                    it.currentFocusManager = focusManager
                }
                content()
            }
        }

        override fun show(focusHandler: ComposeFocusHandler) {
            focusHandlerQueue.tryEmit(focusHandler.focusRequester)
        }

        override fun hide() {
            currentFocusManager?.clearFocus()
        }
    }
