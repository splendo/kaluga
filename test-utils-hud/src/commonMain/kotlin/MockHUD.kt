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

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.hud.BaseHUD
import com.splendo.kaluga.hud.HudConfig
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.CoroutineScope

/**
 * Mock implementation of [BaseHUD]
 * @param hudConfig The [HudConfig] of the [MockHUD]
 * @param setupMocks If `true` sets up mocks to display the hud
 * @param coroutineScope The [CoroutineScope] of the [MockHUD]
 */
class MockHUD(
    override val hudConfig: HudConfig,
    setupMocks: Boolean = true,
    coroutineScope: CoroutineScope,
) : BaseHUD(coroutineScope) {

    /**
     * Mock implementation of [BaseHUD.Builder]
     * @param setupMocks If `true` sets up [createMock] to build a [MockHUD]
     */
    class Builder(setupMocks: Boolean = true) : BaseHUD.Builder() {

        /**
         * List of built [MockHUD]
         */
        val builtHUDs = concurrentMutableListOf<MockHUD>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        override fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope): MockHUD = createMock.call(hudConfig, coroutineScope)

        init {
            if (setupMocks) {
                val builtHUDs = builtHUDs
                createMock.on().doExecute { (hudConfig, coroutineScope) ->
                    MockHUD(hudConfig, setupMocks, coroutineScope).also {
                        builtHUDs.add(it)
                    }
                }
            }
        }
    }

    /**
     * If `true` the HUD is currently being displayed
     */
    override var isVisible: Boolean = false

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [present]
     */
    val presentMock = ::present.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [dismiss]
     */
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
