/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.hud

import kotlinx.coroutines.CoroutineScope

actual class HUD(actual val hudConfig: HudConfig, coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {
    actual class Builder : BaseHUDBuilder() {
        actual fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope) = HUD(hudConfig, coroutineScope)
    }

    private var _isVisible: Boolean = false
    actual val isVisible: Boolean get() = _isVisible

    actual suspend fun present(animated: Boolean): HUD = apply {
        _isVisible = true
    }

    actual suspend fun dismiss(animated: Boolean) {
        _isVisible = false
    }
}
