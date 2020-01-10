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

import com.splendo.kaluga.flow.Flowable
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import com.splendo.kaluga.test.FlowableTest
import kotlinx.coroutines.CompletableDeferred
import kotlin.test.*

sealed class TrafficLightState(internal val stateRepoAccessor: StateRepoAccesor<TrafficLightState>) : State<TrafficLightState>(stateRepoAccessor) {

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

    override suspend fun afterCreatingNewState(newState: TrafficLightState) {
        afterCreatingNewStateDone.complete(newState)
    }

    override suspend fun afterNewStateIsSet() {
        afterNewStateIsSetDone.complete()
    }

    override suspend fun beforeOldStateIsRemoved() {
        beforeOldStateIsRemovedDone.complete()
    }

    override suspend fun afterOldStateIsRemoved(oldState: TrafficLightState) {
        afterOldStateIsRemovedDone.complete(oldState)
    }

    override suspend fun finalState() {
        finalStateDone.complete()
    }

    class RedLight(stateRepoAccessor: StateRepoAccesor<TrafficLightState>) : TrafficLightState(stateRepoAccessor) {

        suspend fun becomeGreen() {
            changeState(GreenLight(stateRepoAccessor))
        }
    }

    class GreenLight(stateRepoAccessor: StateRepoAccesor<TrafficLightState>) : TrafficLightState(stateRepoAccessor) {

        suspend fun becomeYellow() {
            changeState(YellowLight(stateRepoAccessor))
        }

        suspend fun becomeRed() {
            changeState(RedLight(stateRepoAccessor))
        }

    }

    class YellowLight(stateRepoAccessor: StateRepoAccesor<TrafficLightState>) : TrafficLightState(stateRepoAccessor) {

        suspend fun becomeRed() {
            changeState(RedLight(stateRepoAccessor))
        }

    }

}

class TrafficLight: StateRepo<TrafficLightState>() {

    val repoAccesor = StateRepoAccesor(this)

    override fun initialState(): TrafficLightState {
        return TrafficLightState.GreenLight(repoAccesor)
    }

}

class StateRepoTest: FlowableTest<TrafficLightState, TrafficLight>() {

    override fun setUp() {
        super.setUp()

        flowable.complete(TrafficLight())
    }

    @Test
    fun changeState() = runBlockingWithFlow {
        lateinit var greenState: TrafficLightState.GreenLight
        val trafficLight = flowable.getCompleted()
        test {
            assertTrue(it is TrafficLightState.GreenLight)
            greenState = it
            assertTrue(greenState.initialStateDone.isCompleted)
        }
        action {
            assertFalse(greenState.beforeCreatingNewStateDone.isCompleted)
            greenState.beforeCreatingNewStateDone.invokeOnCompletion {
                assertEquals(greenState, trafficLight.repoAccesor.currentState())
                assertFalse { greenState.afterCreatingNewStateDone.isCompleted }
            }
            greenState.afterCreatingNewStateDone.invokeOnCompletion {
                assertEquals(greenState, trafficLight.repoAccesor.currentState())
                val newState = greenState.afterCreatingNewStateDone.getCompleted()
                assertTrue(newState is TrafficLightState.RedLight)
                assertFalse { greenState.afterNewStateIsSetDone.isCompleted}
                assertFalse { newState.beforeOldStateIsRemovedDone.isCompleted }
                newState.beforeOldStateIsRemovedDone.invokeOnCompletion {
                    assertEquals(greenState, trafficLight.repoAccesor.currentState())
                    assertFalse { newState.afterOldStateIsRemovedDone.isCompleted }
                }
                newState.afterOldStateIsRemovedDone.invokeOnCompletion {
                    assertEquals(greenState, newState.afterOldStateIsRemovedDone.getCompleted())
                    assertEquals(newState, trafficLight.repoAccesor.currentState())
                }
            }
            greenState.afterNewStateIsSetDone.invokeOnCompletion {
                val newState = trafficLight.repoAccesor.currentState()
                assertTrue { newState is TrafficLightState.RedLight }
                assertFalse { newState.afterOldStateIsRemovedDone.isCompleted }
            }

            greenState.becomeRed()
        }
        test {
            assertTrue(it is TrafficLightState.RedLight)
            assertTrue(greenState.afterNewStateIsSetDone.isCompleted)
        }
        action {
            trafficLight.cancel()
            assertTrue(trafficLight.repoAccesor.currentState().finalStateDone.isCompleted)
        }
    }

    @Test
    fun changeStateDouble() = runBlockingWithFlow {
        lateinit var greenState: TrafficLightState.GreenLight
        test {
            assertTrue(it is TrafficLightState.GreenLight)
            greenState = it
        }
        action {
            greenState.becomeRed()
            greenState.becomeYellow()
        }
        test {
            assertTrue(it is TrafficLightState.RedLight)
        }
    }
}