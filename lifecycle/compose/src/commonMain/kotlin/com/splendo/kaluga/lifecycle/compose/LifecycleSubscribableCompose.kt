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
import com.splendo.kaluga.lifecycle.LifecycleSubscribable

/**
 * Binds [this] subscribable to the current Compose host's lifecycle. Each platform actual
 * dispatches based on the concrete subtype:
 *
 * - **Android** — if the receiver is `ActivityLifecycleSubscribable`, drives `subscribe(manager)`
 *   / `unsubscribe()` from `LocalLifecycleOwner` + `LocalContext` via a `DisposableEffect`.
 * - **iOS** — if the receiver is `ViewControllerLifecycleSubscribable`, drives subscribe with
 *   the controller from `androidx.compose.ui.uikit.LocalUIViewController`.
 * - **macOS** — if the receiver is `WindowLifecycleSubscribable`, drives subscribe with the
 *   window from this module's [LocalNSWindow] composition local (installed at the host's
 *   `WindowScope { LocalNSWindow provides window }` entry point).
 *
 * Any other [LifecycleSubscribable] (or a subscribable whose subtype isn't recognised on the
 * current platform) is a no-op — useful for service builders that are conceptually lifecycle-
 * aware on Android but pure functions on iOS/macOS.
 */
@Composable
expect fun LifecycleSubscribable.AttachToCompose()
