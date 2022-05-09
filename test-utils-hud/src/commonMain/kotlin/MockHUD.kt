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

package com.splendo.kaluga.test.hud

import co.touchlab.stately.collections.IsoMutableList
import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.hud.BaseHUD
import com.splendo.kaluga.hud.HudConfig
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.on
import com.splendo.kaluga.test.mock.parameters.mock
import kotlinx.coroutines.CoroutineScope

class MockHUD(
    override val hudConfig: HudConfig,
    setupMocks: Boolean = true,
    coroutineScope: CoroutineScope
) : BaseHUD(coroutineScope) {

    class Builder(setupMocks: Boolean = true) : BaseHUD.Builder() {

        val builtHUDs = IsoMutableList<MockHUD>()
        val createMock = ::create.mock()

        override fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope): MockHUD =
            createMock.call(hudConfig, coroutineScope)

        init {
            if (setupMocks) {
                val builtHUDs = builtHUDs
                createMock.on().doExecute { values ->
                    MockHUD(values.first, setupMocks, values.second).also {
                        builtHUDs.add(it)
                    }
                }
            }
        }
    }

    private val _isVisible = AtomicBoolean(false)
    override var isVisible: Boolean
        get() = _isVisible.value
        private set(value) {
            _isVisible.value = value
        }

    val presentMock = ::present.mock()
    val dismissMock = ::dismiss.mock()

    init {
        if (setupMocks) {
            presentMock.on().doExecute {
                isVisible = true
                this
            }
            dismissMock.on().doExecute {
                isVisible = false
            }
        }
    }

    override suspend fun present(animated: Boolean): BaseHUD = presentMock.call(animated)

    override suspend fun dismiss(animated: Boolean): Unit = dismissMock.call(animated)
}
