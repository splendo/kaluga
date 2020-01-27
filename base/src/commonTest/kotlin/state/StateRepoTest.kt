package com.splendo.kaluga.state
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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.state.TrafficLightState.GreenLight
import com.splendo.kaluga.state.TrafficLightState.YellowLight
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

sealed class TrafficLightState : State() {

    val initialStateDone = EmptyCompletableDeferred()
    val beforeCreatingNewStateDone = EmptyCompletableDeferred()
    val afterCreatingNewStateDone = CompletableDeferred<TrafficLightState>()
    val afterNewStateIsSetDone = EmptyCompletableDeferred()
    val beforeOldStateIsRemovedDone = EmptyCompletableDeferred()
    val afterOldStateIsRemovedDone = CompletableDeferred<TrafficLightState>()
    val finalStateDone = EmptyCompletableDeferred()

    override suspend fun initialState() {
        initialStateDone.complete()
    }

    override suspend fun beforeCreatingNewState() {
        beforeCreatingNewStateDone.complete()
    }

    override suspend fun afterNewStateIsSet() {
        afterNewStateIsSetDone.complete()
    }

    override suspend fun beforeOldStateIsRemoved() {
        beforeOldStateIsRemovedDone.complete()
    }

    override suspend fun afterCreatingNewState(newState: State) {
        afterCreatingNewStateDone.complete(newState as TrafficLightState)
    }

    override suspend fun afterOldStateIsRemoved(oldState: State) {
        afterOldStateIsRemovedDone.complete(oldState as TrafficLightState)
    }

    override suspend fun finalState() {
        finalStateDone.complete()
    }

    class RedLight : TrafficLightState() {

        fun becomeGreen():GreenLight {
            return GreenLight()
        }
    }

    class GreenLight : TrafficLightState() {

        fun becomeYellow():YellowLight {
            return YellowLight()
        }
    }

    class YellowLight: TrafficLightState() {

        fun becomeRed():RedLight {
            return RedLight()
        }

    }

}

class TrafficLight: HotStateRepo<TrafficLightState>() {

    override fun initialValue(): TrafficLightState {
        return GreenLight()
    }

}

class StateRepoTest: FlowableTest<TrafficLightState>() {

    val trafficLight = TrafficLight()

    override fun setUp() {
        super.setUp()
        flowable.complete(trafficLight.flowable.value)
    }

    @Test
    fun changeState() = testWithFlow {
        lateinit var greenState: GreenLight
        test {
            assertTrue(it is GreenLight)
            greenState = it
            assertTrue(greenState.initialStateDone.isCompleted)
        }
        action {
            assertFalse(greenState.beforeCreatingNewStateDone.isCompleted)
            greenState.beforeCreatingNewStateDone.invokeOnCompletion {
                assertEquals(greenState, trafficLight.peekState())
                assertFalse { greenState.afterCreatingNewStateDone.isCompleted }
            }
            greenState.afterCreatingNewStateDone.invokeOnCompletion {
                assertEquals(greenState, trafficLight.peekState())
                val newState = greenState.afterCreatingNewStateDone.getCompleted()
                assertTrue(newState is YellowLight)
                assertFalse { greenState.afterNewStateIsSetDone.isCompleted}
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
                assertTrue { newState is YellowLight }
                assertFalse { newState.afterOldStateIsRemovedDone.isCompleted }
            }

            trafficLight.takeAndChangeState { currentState ->
                assertTrue (currentState is GreenLight)
                currentState.becomeYellow()

            }



            trafficLight.takeAndChangeState {
                assertTrue(it is YellowLight)
                it.becomeRed()
            }

        }
        test {
            assertTrue(it is YellowLight)
            assertTrue(greenState.beforeCreatingNewStateDone.isCompleted)
            assertTrue(greenState.afterCreatingNewStateDone.isCompleted)
            assertTrue(greenState.afterNewStateIsSetDone.isCompleted)
            assertTrue(it.beforeOldStateIsRemovedDone.isCompleted)
            assertTrue(it.afterOldStateIsRemovedDone.isCompleted)
        }
    }

    @Test
    fun changeStateTwice() = testWithFlow {
        lateinit var greenState: GreenLight
        test {
            assertTrue(it is GreenLight)
            greenState = it
        }
        action {
            trafficLight.takeAndChangeState() {
                assertTrue( it is GreenLight)
                greenState.becomeYellow().becomeRed()
            }
        }
        test {
            assertTrue(it is TrafficLightState.RedLight)
        }
    }

    @Test
    fun multipleObservers() = runBlocking {
        val greenState = flowable.await().flow().first()
        assertTrue(greenState is GreenLight)
        trafficLight.takeAndChangeState {
            assertTrue(it is GreenLight)
            it.becomeYellow()
        }
        val yellowState = flowable.await().flow().first()
        assertTrue(yellowState is YellowLight)
    }

}