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

import com.splendo.kaluga.EmptyCompletableDeferred
import com.splendo.kaluga.complete
import com.splendo.kaluga.state.SimpleState.StateOne
import com.splendo.kaluga.test.FlowableTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail


sealed class SimpleState(stateRepo: StateRepo<SimpleState>): State<SimpleState>(stateRepo) {
    class StateOne(stateRepo: StateRepo<SimpleState>) : SimpleState(stateRepo) {

        fun two(): StateTwo {
            return StateTwo(repo)
        }

        val beforeCreatingNewStateDone = EmptyCompletableDeferred()
        val afterCreatingNewStateDone = EmptyCompletableDeferred()
        val afterNewStateIsSetDone = EmptyCompletableDeferred()

        override suspend fun beforeCreatingNewState() {
            beforeCreatingNewStateDone.complete()
        }
        override suspend fun afterCreatingNewState() {
            afterCreatingNewStateDone.complete()
        }
        override suspend fun afterNewStateIsSet() {
            afterNewStateIsSetDone.complete()
        }

    }

    class StateTwo(stateRepo: StateRepo<SimpleState>) : SimpleState(stateRepo)
}

class SimpleStateRepo: StateRepo<SimpleState>() {
    override fun initialState(): SimpleState {
        return StateOne(this)
    }

}

class StateRepoTest:FlowableTest<SimpleState>() {
    override val flowable = SimpleStateRepo()

    @Test
    fun changeState() = runBlockingWithFlow {
        lateinit var stateOne: StateOne
        test {
            assertTrue(it is StateOne)
            stateOne = it
        }
        action {
            assertFalse(stateOne.beforeCreatingNewStateDone.isCompleted)
            flowable.changeState {
                assertTrue(stateOne.beforeCreatingNewStateDone.isCompleted)
                assertFalse(stateOne.afterCreatingNewStateDone.isCompleted)
                stateOne.afterCreatingNewStateDone.invokeOnCompletion {
                    assertFalse(stateOne.afterNewStateIsSetDone.isCompleted) // check the order is correct
                }
                when (it) {
                    is StateOne -> it.two()
                    is SimpleState.StateTwo -> fail("Not in correct initial state")
                }
            }
            assertTrue(stateOne.afterCreatingNewStateDone.isCompleted)
        }
        test {
            assertTrue(it is SimpleState.StateTwo)
            assertTrue(stateOne.afterNewStateIsSetDone.isCompleted)
        }

    }
}