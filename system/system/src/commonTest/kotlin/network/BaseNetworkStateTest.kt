/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.system.network

import com.splendo.kaluga.test.base.BaseFlowTest
import com.splendo.kaluga.test.system.network.MockNetworkManager
import com.splendo.kaluga.test.system.network.MockNetworkStateRepoBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class BaseNetworkStateTest<T, F : Flow<T>> : BaseFlowTest<BaseNetworkStateTest.Configuration, BaseNetworkStateTest.Context, T, F>() {

    data class Configuration(val initialNetworkConnectionType: NetworkConnectionType)

    class Context(configuration: Configuration, coroutineScope: CoroutineScope) : TestContext {
        val networkManagerBuilder = MockNetworkManager.Builder(configuration.initialNetworkConnectionType)
        val networkStateRepoBuilder = MockNetworkStateRepoBuilder(networkManagerBuilder)
        val networkStateRepo = networkStateRepoBuilder.create(coroutineScope.coroutineContext)
        val networkManager get() = networkManagerBuilder.builtNetworkManagers.first()
    }

    override val createTestContextWithConfiguration: suspend (configuration: Configuration, scope: CoroutineScope) -> Context =
        { configuration, scope -> Context(configuration, scope) }

    protected fun testNetworkState(initialNetworkConnectionType: NetworkConnectionType, test: suspend BaseFlowTest<Configuration, Context, T, F>.(F) -> Unit) {
        testWithFlowAndTestContext(
            Configuration(initialNetworkConnectionType),
            blockWithContext = test,
        )
    }
}
