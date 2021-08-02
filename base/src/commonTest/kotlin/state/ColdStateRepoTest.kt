/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.state

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class ColdStateRepoTest : BaseTest() {

    sealed class CircuitState : State() {
        val initialStateCounter = MutableStateFlow(0)

        override suspend fun initialState() {
            super.initialState()
            initialStateCounter.value++
        }

        object Open : CircuitState()
        object Closed : CircuitState()
    }

    class Repo : ColdStateRepo<CircuitState>() {
        override suspend fun initialValue(): CircuitState {
            debug("ColdStateRepoTest") { "initialValue" }
            return CircuitState.Open
        }

        override suspend fun deinitialize(state: CircuitState) {
            debug("ColdStateRepoTest") { "deinitialize" }
        }
    }

    @Test
    fun testLaterCollectionsCallsInitialState() = runBlocking {
        val repo = Repo()
        for(times in 1..2) {
           repo.testCollectionIsCalledTimes(times)
        }
    }

    private suspend fun Repo.testCollectionIsCalledTimes(times: Int) {
        val job = launch { collect() }
        delay(100)
        useState {
            val isCalledTimes = it.initialStateCounter.filter { it == times }.first()
            assertEquals(times, isCalledTimes)
            assertEquals(CircuitState.Open, it)
        }
        job.cancel()
    }
}
