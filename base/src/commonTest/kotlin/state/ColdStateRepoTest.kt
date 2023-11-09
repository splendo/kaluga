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

package com.splendo.kaluga.base.state

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.test.base.BaseTest
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.yieldMultiple
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ColdStateRepoTest : BaseTest() {

    sealed class CircuitState : KalugaState {
        data object Open : CircuitState()
        data object Closed : CircuitState()
    }

    class Repo(coroutineContext: CoroutineContext) : ColdStateRepo<CircuitState>(coroutineContext = coroutineContext) {

        val initialValueMock = ::initialValue.mock()
        val deinitializeMock = ::deinitialize.mock()

        init {
            initialValueMock.on().doExecuteSuspended {
                debug("ColdStateRepoTest") { "initialValue" }
                CircuitState.Open
            }

            deinitializeMock.on().doExecuteSuspended {
                debug("ColdStateRepoTest") { "deinitialize" }
            }
        }

        override suspend fun initialValue(): CircuitState = initialValueMock.call()

        override suspend fun deinitialize(state: CircuitState): Unit = deinitializeMock.call(state)
    }

    @Test
    fun testLaterCollectionsCallsInitialState() = runBlocking {
        launch {
            val repo = Repo(coroutineContext)
            for (times in 1..2) {
                repo.testCollectionIsCalledTimes(times)
            }
            repo.cancel()
        }.join()
    }

    private suspend fun Repo.testCollectionIsCalledTimes(times: Int) {
        val job = launch { collect() }
        yieldMultiple(2)
        useState {
            initialValueMock.verify(times)
            assertEquals(CircuitState.Open, it)
        }
        job.cancel()
        yieldMultiple(3)
        deinitializeMock.verify(times = times)
    }
}
