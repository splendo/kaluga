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
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertTrue

class ColdStateRepoTest : BaseTest() {

    sealed class CircuitState : State() {
        private val _initialStateCounter = MutableStateFlow(0)
        val initialStateCounter = _initialStateCounter.asStateFlow()

        override suspend fun initialState() {
            super.initialState()
            _initialStateCounter.value++
        }

        object Open : CircuitState()
        object Closed : CircuitState()
    }

    class Repo : ColdStateRepo<CircuitState>() {
        val initialCompletion = CompletableDeferred<State>()
        val deinitCompletion = CompletableDeferred<State>()

        val lastKnownState = MutableStateFlow<CircuitState?>(null)

        override suspend fun initialValue(): CircuitState {
            return when (lastKnownState.value) {
                is CircuitState.Open -> CircuitState.Open
                is CircuitState.Closed, null -> CircuitState.Closed
            }.also {
                initialCompletion.complete(it)
            }
        }

        override suspend fun deinitialize(state: CircuitState) {
            lastKnownState.value = state
            deinitCompletion.complete(state)
        }
    }

    @Test
    fun testLaterCollectionsCallsInitialState() = runBlocking {
        val repo = Repo()
        val jobCompletion = EmptyCompletableDeferred()
        val job = launch {
            repo.collect {
                jobCompletion.complete()
            }
        }
        jobCompletion.await()
        job.cancel()

        val job2 = launch { repo.collect() }
        delay(1000)
        repo.useState {
            assertTrue { it.initialStateCounter.value == 2 }
        }
        job2.cancel()
    }
}
