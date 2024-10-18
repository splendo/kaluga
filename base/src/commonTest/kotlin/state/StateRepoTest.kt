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

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.base.FlowTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

sealed class TrafficLightState :
    KalugaState,
    HandleBeforeCreating,
    HandleAfterCreating<TrafficLightState>,
    HandleBeforeOldStateIsRemoved<TrafficLightState>,
    HandleAfterOldStateIsRemoved<TrafficLightState>,
    HandleAfterNewStateIsSet<TrafficLightState> {

    val beforeCreatingNewStateDone = EmptyCompletableDeferred()
    val afterCreatingNewStateDone = CompletableDeferred<TrafficLightState>()
    val afterNewStateIsSetDone = CompletableDeferred<TrafficLightState>()
    val beforeOldStateIsRemovedDone = CompletableDeferred<TrafficLightState>()
    val afterOldStateIsRemovedDone = CompletableDeferred<TrafficLightState>()

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

    class RedLight internal constructor() : TrafficLightState() {
        val becomeGreen = suspend { GreenLight() }
    }

    class GreenLight internal constructor() : TrafficLightState() {

        val becomeYellow = suspend {
            YellowLight()
        }

        val becomeRed = suspend { RedLight() }
    }

    class YellowLight internal constructor() : TrafficLightState() {

        val becomeRed = suspend { RedLight() }
    }
}

class TrafficLight : HotStateRepo<TrafficLightState>() {

    override suspend fun initialValue(): TrafficLightState = TrafficLightState.GreenLight()
}

class StateRepoTest : FlowTest<TrafficLightState, TrafficLight>() {

    @Test
    fun testChangeState() = testWithFlow { trafficLight ->
        val greenStateDeferred = CompletableDeferred<TrafficLightState.GreenLight>()
        test {
            assertIs<TrafficLightState.GreenLight>(it)
            greenStateDeferred.complete(it)
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
                    else -> state.remain()
                }
            }
        }
        test {
            assertIs<TrafficLightState.RedLight>(it)
            assertTrue(greenState.beforeCreatingNewStateDone.isCompleted)
            assertEquals(it, greenState.afterCreatingNewStateDone.getCompleted())
            assertEquals(it, greenState.afterNewStateIsSetDone.getCompleted())
            assertEquals(greenState, it.beforeOldStateIsRemovedDone.getCompleted())
            assertEquals(greenState, it.afterOldStateIsRemovedDone.getCompleted())
        }
    }

    @Test
    fun testChangeStateDouble() = testWithFlow { trafficLight ->
        val greenStateDeferred = CompletableDeferred<TrafficLightState.GreenLight>()
        test {
            assertIs<TrafficLightState.GreenLight>(it)
            greenStateDeferred.complete(it)
        }
        action {
            trafficLight.takeAndChangeState {
                when (val state = it) {
                    is TrafficLightState.GreenLight -> state.becomeRed
                    else -> state.remain()
                }
            }
            trafficLight.takeAndChangeState {
                when (val state = it) {
                    is TrafficLightState.GreenLight -> state.becomeYellow
                    else -> state.remain()
                }
            }
        }
        test {
            assertIs<TrafficLightState.RedLight>(it)
        }
    }

    @Test
    fun testChangeStateDoubleConcurrent() = testWithFlow { trafficLight ->
        val greenStateDeferred = CompletableDeferred<TrafficLightState.GreenLight>()
        test {
            assertIs<TrafficLightState.GreenLight>(it)
            greenStateDeferred.complete(it)
        }
        action {
            val delayedTransition = async {
                delay(100)
                trafficLight.takeAndChangeState {
                    when (val state = it) {
                        is TrafficLightState.GreenLight -> state.becomeRed
                        else -> state.remain()
                    }
                }
                Unit
            }
            val slowTransition = async {
                trafficLight.takeAndChangeState {
                    delay(100)
                    when (val state = it) {
                        is TrafficLightState.GreenLight -> state.becomeYellow
                        else -> state.remain()
                    }
                }
                Unit
            }
            awaitAll(delayedTransition, slowTransition)
        }
        test {
            assertIs<TrafficLightState.YellowLight>(it)
        }
    }

    @Test
    fun testMultipleObservers() = testWithFlow { trafficLight ->
        val greenState = trafficLight.first()
        assertTrue(greenState is TrafficLightState.GreenLight)
        trafficLight.takeAndChangeState {
            when (val state = it) {
                is TrafficLightState.GreenLight -> state.becomeYellow
                else -> state.remain()
            }
        }
        val yellowState = trafficLight.first()
        assertTrue(yellowState is TrafficLightState.YellowLight)
    }

    @Test
    fun changeStateInsideChangeState() = testWithFlow { trafficLight ->

        test {} // trigger start of flow

        action {
            val transitionsCompleted = EmptyCompletableDeferred()
            trafficLight.takeAndChangeState { state ->
                @Suppress("DeferredResultUnused") // we want to test async but don't care about the result
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
                // yield a few times to give async block a chance to run
                repeat(10) {
                    yield()
                    delay(10)
                }

                when (state) {
                    is TrafficLightState.RedLight -> state.becomeGreen
                    is TrafficLightState.YellowLight -> state.becomeRed
                    is TrafficLightState.GreenLight -> state.becomeYellow
                }
            }

            // state mutex should protect the order here

            test {
                assertIs<TrafficLightState.YellowLight>(it)
            }
            test {
                assertIs<TrafficLightState.RedLight>(it)
            }

            transitionsCompleted.await()
            val redState = trafficLight.first()
            assertTrue(redState is TrafficLightState.RedLight)
        }
    }

    override val flow = suspend { TrafficLight() }
}
