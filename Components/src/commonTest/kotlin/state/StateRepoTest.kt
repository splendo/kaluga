package com.splendo.mpp.state

import com.splendo.mpp.EmptyCompletableDeferred
import com.splendo.mpp.complete
import com.splendo.mpp.state.SimpleState.StateOne
import com.splendo.mpp.test.FlowableTest
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