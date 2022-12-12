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

package com.splendo.kaluga.state

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlin.test.Test
import kotlin.test.assertEquals

class ReplayBufferStateRepoTest : BaseTest() {

    companion object {
        val state1 = object : KalugaState {}
        val state2 = object : KalugaState {}
        val state3 = object : KalugaState {}
        val state4 = object : KalugaState {}
    }

    class Repo : BaseHotStateRepo<KalugaState, MutableSharedFlow<KalugaState>>() {
        override suspend fun initialValue() = state1
        override val lazyMutableSharedFlow = lazy {
            MutableSharedFlow<KalugaState>(3, 0)
        }
    }

    @Test
    fun testReplayBufferStateRepo() = runBlocking {
        val repo = Repo()
        repo.useState { state ->
            assertEquals(state1, state)
        }
        assertEquals(state1, repo.peekState())

        val job = async {
            repo.onEach { delay(10) }.take(3).toList()
        }

        repo.takeAndChangeState { state ->
            assertEquals(state1, state)
            suspend { state2 }
        }

        repo.takeAndChangeState { state ->
            assertEquals(state2, state)
            suspend { state3 }
        }

        // this last state should not show up in the flow's first 3 values
        repo.takeAndChangeState { state ->
            assertEquals(state3, state)
            suspend { state4 }
        }

        assertEquals(listOf(state1, state2, state3), job.await())

        // replay cache should contain the last 3 states
        assertEquals(listOf(state2, state3, state4), repo.take(3).toList())
    }
}
