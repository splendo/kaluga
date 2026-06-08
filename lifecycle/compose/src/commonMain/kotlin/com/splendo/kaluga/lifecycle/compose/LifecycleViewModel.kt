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
import androidx.lifecycle.ViewModel
import com.splendo.kaluga.lifecycle.LifecycleSubscribable

/**
 * Optional Compose-Multiplatform `ViewModel` base class for Kaluga lifecycle-aware features.
 * Holds an opt-in list of [LifecycleSubscribable]s; the screen-side `@Composable` calls
 * [AttachToCompose] once and all of them get wired to the current Compose host's lifecycle.
 *
 * Extending this class is **not** required — any `ViewModel` can hold a `LifecycleSubscribable`
 * field and call `field.AttachToCompose()` manually. This is purely a convenience for the common
 * "VM owns N subscribables" case.
 *
 * @param subscribables the subscribables to drive when [AttachToCompose] is invoked.
 */
abstract class LifecycleViewModel(val subscribables: List<LifecycleSubscribable> = emptyList()) : ViewModel()

/**
 * Drives every [LifecycleSubscribable] held by [this] view model from the current Compose host.
 * Returns the receiver so call sites can chain: `val vm = viewModel<X>().AttachToCompose()`.
 */
@Composable
fun <T : LifecycleViewModel> T.AttachToCompose(): T {
    subscribables.forEach { it.AttachToCompose() }
    return this
}
