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
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class ColdStateFlowRepoTest:BaseTest() {

    companion object {
        val first = object : State() {}
        val active = object : State() {}
        val deinit = object : State() {}
    }

    class Repo: ColdStateFlowRepo<State>(
        init = { active },
        deinit = { deinit },
        firstState = { first }
    )

    @Test
    fun testColdStateFlowRepo() = runBlocking {
        val repo = Repo()

        assertEquals(first, repo.stateflow.value)
        repo.useState { state ->
            assertEquals(first, state)
        }

        val firstCollect = CompletableDeferred<State>()
        val secondCollect = CompletableDeferred<State>()
        val job = launch {
            repo.collect {
                if (!firstCollect.isCompleted)
                    firstCollect.complete(it)
                else if (!secondCollect.isCompleted)
                    secondCollect.complete(it) // further calls are ignored
            }
        }

        // if (firstCollect.await() != active) { // active is also technically correct, if the collection even ran before
            assertEquals(first, firstCollect.await())
            assertEquals(active, secondCollect.await())
        // }

        repo.useState { state ->
            assertEquals(state, active)
        }

        job.cancel()
        delay(20)
        repo.useState { state ->
            assertEquals(deinit, state)
        }

        val firstCollect2 = CompletableDeferred<State>()
        val secondCollect2 = CompletableDeferred<State>()
        val job2 = launch {
            repo.collect {
                if (!firstCollect2.isCompleted)
                    firstCollect2.complete(it)
                else if (!secondCollect2.isCompleted)
                    secondCollect2.complete(it) // further calls are ignored
            }
        }

        assertEquals(deinit, firstCollect2.await())
        assertEquals(active, secondCollect2.await())

        job2.cancel()
        delay(20)
        repo.useState { state ->
            assertEquals(deinit, state)
        }

    }
}