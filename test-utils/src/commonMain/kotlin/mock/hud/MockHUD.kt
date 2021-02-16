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

package com.splendo.kaluga.test.mock.hud

import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.hud.BaseHUD
import com.splendo.kaluga.hud.HudConfig
import kotlinx.coroutines.CoroutineScope

class MockHUD(override val hudConfig: HudConfig, coroutineScope: CoroutineScope) : BaseHUD(coroutineScope) {

    class Builder : BaseHUD.Builder() {

        val builtHUDs = mutableListOf<MockHUD>()

        override fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope): MockHUD {
            return MockHUD(hudConfig, coroutineScope).also {
                builtHUDs.add(it)
            }
        }
    }

    private var _isVisible = AtomicBoolean(false)
    override var isVisible: Boolean
        get() = _isVisible.value
        private set(value) { _isVisible.value = value }

    override suspend fun present(animated: Boolean): BaseHUD {
        isVisible = true
        return this
    }

    override suspend fun dismiss(animated: Boolean) {
        isVisible = false
    }
}
