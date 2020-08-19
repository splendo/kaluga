/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.MultiplatformMainScope
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.FlowableTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

sealed class TrafficLightState : State<TrafficLightState>(),
    HandleBeforeCreating,
    HandleAfterCreating<TrafficLightState>,
    HandleBeforeOldStateIsRemoved<TrafficLightState>,
    HandleAfterOldStateIsRemoved<TrafficLightState>,
    HandleAfterNewStateIsSet<TrafficLightState> {

    val initialStateDone = EmptyCompletableDeferred()
    val beforeCreatingNewStateDone = EmptyCompletableDeferred()
    val afterCreatingNewStateDone = CompletableDeferred<TrafficLightState>()
    val afterNewStateIsSetDone = CompletableDeferred<TrafficLightState>()
    val beforeOldStateIsRemovedDone = CompletableDeferred<TrafficLightState>()
    val afterOldStateIsRemovedDone = CompletableDeferred<TrafficLightState>()
    val finalStateDone = EmptyCompletableDeferred()

    override suspend fun initialState() {
        initialStateDone.complete()
    }

    override suspend fun beforeCreatingNewState() {
        beforeCreatingNewStateDone.complete()
    }

    override suspend fun afterCreatingNewState(newState: TrafficLightState) {
        afterCreatingNewStateDone.complete(newState)
    }

    override suspend fun afterNewStateIsSet(newState: TrafficLightState) {
        afterNewStateIsSetDone.complete(newState)
    }

    override suspend fun beforeOldStateIsRemoved(oldState: TrafficLightState) {
        beforeOldStateIsRemovedDone.complete(oldState)
    }

    override suspend fun afterOldStateIsRemoved(oldState: TrafficLightState) {
        afterOldStateIsRemovedDone.complete(oldState)
    }

    override suspend fun finalState() {
        finalStateDone.complete()
    }

    class RedLight internal constructor() : TrafficLightState() {

        val becomeGreen = suspend { GreenLight() }
    }

    class GreenLight internal constructor() : TrafficLightState() {

        val becomeYellow = suspend { YellowLight() }

        val becomeRed = suspend { RedLight() }
    }

    class YellowLight internal constructor() : TrafficLightState() {

        val becomeRed = suspend { RedLight() }
    }
}

class TrafficLight : HotStateRepo<TrafficLightState>() {

    override suspend fun initialValue(): TrafficLightState {
        return TrafficLightState.GreenLight()
    }
}

class StateRepoTest : FlowableTest<TrafficLightState>() {

    private lateinit var trafficLight: TrafficLight

    override fun beforeTest() {
        super.beforeTest()

        trafficLight = TrafficLight()
        flowable.complete(trafficLight.flowable)
    }

    @Test
    fun testChangeState() = testWithFlow {
        val greenStateDeferred = CompletableDeferred<TrafficLightState.GreenLight>()
        test {
            assertTrue(it is TrafficLightState.GreenLight)
            greenStateDeferred.complete(it)
            assertTrue(it.initialStateDone.isCompleted)
        }
        val greenState = greenStateDeferred.await()
        action {
            assertFalse(greenState.beforeCreatingNewStateDone.isCompleted)
            greenState.beforeCreatingNewStateDone.invokeOnCompletion {
                assertEquals(greenState, trafficLight.peekState())
                assertFalse { greenState.afterCreatingNewStateDone.isCompleted }
            }
            greenState.afterCreatingNewStateDone.invokeOnCompletion {
                assertEquals(greenState, trafficLight.peekState())
                val newState = greenState.afterCreatingNewStateDone.getCompleted()
                assertTrue(newState is TrafficLightState.RedLight)
                assertFalse { greenState.afterNewStateIsSetDone.isCompleted }
                assertFalse { newState.beforeOldStateIsRemovedDone.isCompleted }
                newState.beforeOldStateIsRemovedDone.invokeOnCompletion {
                    assertEquals(greenState, trafficLight.peekState())
                    assertFalse { newState.afterOldStateIsRemovedDone.isCompleted }
                }
                newState.afterOldStateIsRemovedDone.invokeOnCompletion {
                    assertEquals(greenState, newState.afterOldStateIsRemovedDone.getCompleted())
                    assertEquals(newState, trafficLight.peekState())
                }
            }
            greenState.afterNewStateIsSetDone.invokeOnCompletion {
                val newState = trafficLight.peekState()
                assertTrue { newState is TrafficLightState.RedLight }
                assertFalse { newState.afterOldStateIsRemovedDone.isCompleted }
            }

            trafficLight.takeAndChangeState {
                when (val state = it) {
                    is TrafficLightState.GreenLight -> state.becomeRed
                    else -> state.remain
                }
            }
        }
        test {
            assertTrue(it is TrafficLightState.RedLight)
            assertTrue(greenState.beforeCreatingNewStateDone.isCompleted)
            assertEquals(it, greenState.afterCreatingNewStateDone.getCompleted())
            assertEquals(it, greenState.afterNewStateIsSetDone.getCompleted())
            assertEquals(greenState, it.beforeOldStateIsRemovedDone.getCompleted())
            assertEquals(greenState, it.afterOldStateIsRemovedDone.getCompleted())
        }
    }

    @Test
    fun testChangeStateDouble() = testWithFlow {
        val greenStateDeferred = CompletableDeferred<TrafficLightState.GreenLight>()
        test {
            assertTrue(it is TrafficLightState.GreenLight)
            greenStateDeferred.complete(it)
        }
        action {
            trafficLight.takeAndChangeState {
                when (val state = it) {
                    is TrafficLightState.GreenLight -> state.becomeRed
                    else -> state.remain
                }
            }
            trafficLight.takeAndChangeState {
                when (val state = it) {
                    is TrafficLightState.GreenLight -> state.becomeYellow
                    else -> state.remain
                }
            }
        }
        test {
            assertTrue(it is TrafficLightState.RedLight)
        }
    }

    @Test
    @Ignore // Delay on iOS is broken during tests due to tests running on the main thread.
    fun testChangeStateDoubleConcurrent() = testWithFlow {
        val greenStateDeferred = CompletableDeferred<TrafficLightState.GreenLight>()
        test {
            assertTrue(it is TrafficLightState.GreenLight)
            greenStateDeferred.complete(it)
        }
        action {
            val scope = MultiplatformMainScope()
            val delayedTransition = scope.async {
                delay(100) // TODO: delay on the main thread in iOS tests is broken
                trafficLight.takeAndChangeState {
                    when (val state = it) {
                        is TrafficLightState.GreenLight -> state.becomeRed
                        else -> state.remain
                    }
                }
            }
            val slowTransition = scope.async {
                trafficLight.takeAndChangeState {
                    delay(100) // TODO: delay on the main thread in iOS tests is broken
                    when (val state = it) {
                        is TrafficLightState.GreenLight -> state.becomeYellow
                        else -> state.remain
                    }
                }
            }
            awaitAll(delayedTransition, slowTransition)
        }
        test {
            assertTrue(it is TrafficLightState.YellowLight)
        }
    }

    @Test
    fun testMultipleObservers() = runBlocking {
        val greenState = flowable.await().flow().first()
        assertTrue(greenState is TrafficLightState.GreenLight)
        trafficLight.takeAndChangeState {
            when (val state = it) {
                is TrafficLightState.GreenLight -> state.becomeYellow
                else -> state.remain
            }
        }
        val yellowState = flowable.await().flow().first()
        assertTrue(yellowState is TrafficLightState.YellowLight)
    }

    @Test
    fun changeStateInsideChangeState() = runBlocking {
        val transitionsCompleted = EmptyCompletableDeferred()
        trafficLight.takeAndChangeState { state ->
            async {
                trafficLight.takeAndChangeState { newState ->
                    when (newState) {
                        is TrafficLightState.RedLight -> newState.becomeGreen
                        is TrafficLightState.YellowLight -> newState.becomeRed
                        is TrafficLightState.GreenLight -> newState.becomeYellow
                    }
                }
                transitionsCompleted.complete()
            }
            when (state) {
                is TrafficLightState.RedLight -> state.becomeGreen
                is TrafficLightState.YellowLight -> state.becomeRed
                is TrafficLightState.GreenLight -> state.becomeYellow
            }
        }
        transitionsCompleted.await()
        val redState = flowable.await().flow().first()
        assertTrue(redState is TrafficLightState.RedLight)
    }
}
