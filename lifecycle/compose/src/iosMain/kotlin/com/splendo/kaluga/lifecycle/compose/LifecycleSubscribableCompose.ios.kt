/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.lifecycle.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.uikit.LocalUIViewController
import com.splendo.kaluga.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.lifecycle.ViewControllerLifecycleSubscribable

@Composable
actual fun LifecycleSubscribable.AttachToCompose() {
    val subscribable = this as? ViewControllerLifecycleSubscribable ?: return
    val viewController = LocalUIViewController.current
    DisposableEffect(subscribable, viewController) {
        subscribable.subscribe(ViewControllerLifecycleSubscribable.LifecycleManager(viewController))
        onDispose { subscribable.unsubscribe() }
    }
}
