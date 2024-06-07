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

package com.splendo.kaluga.hud

import kotlinx.coroutines.CoroutineScope

/**
 * Default [BaseHUD] implementation.
 */
actual class HUD(actual override val hudConfig: HudConfig, coroutineScope: CoroutineScope) : BaseHUD(coroutineScope) {

    /**
     * Builder class for creating a [HUD]
     */
    actual class Builder : BaseHUD.Builder() {

        /**
         * Creates a [HUD] based on [hudConfig].
         * @param hudConfig The [HudConfig] to apply to the [HUD].
         * @param coroutineScope The [CoroutineScope] managing the lifecycle of the HUD.
         * @return the created [HUD]
         */
        actual override fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope) = HUD(hudConfig, coroutineScope)
    }

    private var _isVisible: Boolean = false
    actual override val isVisible: Boolean get() = _isVisible

    actual override suspend fun present(animated: Boolean): HUD = apply {
        _isVisible = true
    }

    actual override suspend fun dismiss(animated: Boolean) {
        _isVisible = false
    }
}
