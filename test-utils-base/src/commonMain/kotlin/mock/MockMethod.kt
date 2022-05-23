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

package com.splendo.kaluga.test.base.mock

import co.touchlab.stately.collections.IsoMutableList
import co.touchlab.stately.collections.IsoMutableMap
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.test.base.mock.answer.Answer
import com.splendo.kaluga.test.base.mock.answer.BaseAnswer
import com.splendo.kaluga.test.base.mock.answer.SuspendedAnswer
import com.splendo.kaluga.test.base.mock.parameters.ParametersSpec
import com.splendo.kaluga.test.base.mock.verification.VerificationRule
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

private fun expect(condition: Boolean, message: () -> String) {
    if (!condition) fail(message)
}

private fun fail(message: () -> String): Nothing = throw AssertionError(message())

/**
 * Class for mocking a method that takes parameters described by [W] and can be stubbed to [S]
 */
sealed class BaseMethodMock<
    M : ParametersSpec.Matchers,
    C : ParametersSpec.MatchersOrCaptor<M>,
    V : ParametersSpec.Values,
    W : ParametersSpec<M, C, V>,
    R,
    A : BaseAnswer<V, R>,
    S : BaseMethodMock.Stub<M, V, R, A>
    > {

    /**
     * A Stub is a class that provides an [BaseAnswer] [A] for a set of [ParametersSpec.Values] [V]
     */
    abstract class Stub<
        M : ParametersSpec.Matchers,
        V : ParametersSpec.Values,
        R,
        A : BaseAnswer<V, R>
        > {

        abstract val matchers: M
        protected val answer = AtomicReference<A?>(null)

        protected abstract fun createAnswer(result: (V) -> R): A

        /**
         * Sets the answer for this stub to a given answer.
         * @param answer The [BaseAnswer] [A] to provide the answer for this stub.
         */
        fun doAnswer(answer: A) {
            this.answer.set(answer)
        }

        /**
         * Sets the answer to execute a given code block.
         * @param action The code block to execute as an answer to the stub being called.
         */
        fun doExecute(action: (V) -> R) = doAnswer(createAnswer(action))

        /**
         * Sets the answer to return a given value.
         * @param value the result to return as an answer to the stub being called.
         */
        fun doReturn(value: R) = doExecute { value }

        /**
         * Sets the answer to throw a given [Throwable]
         * @param throwable The [Throwable] to throw as an answer to the stub being called.
         */
        fun doThrow(throwable: Throwable) = doExecute { throw throwable }
    }

    private val stubs: IsoMutableMap<M, S> = IsoMutableMap()
    private val callParameters: IsoMutableList<V> = IsoMutableList()
    protected abstract val ParametersSpec: W

    protected abstract fun createStub(matcher: M): S

    internal fun on(matcher: M): S =
        createStub(matcher).also {
            stubs[matcher] = it
        }

    protected fun getStubFor(values: V): S {
        callParameters.add(values)
        // First find all the stubs whose matchers match the values received and sort their matchers per parameter in order of strongest constraint.
        val matchingStubs = stubs.keys.mapNotNull { matchers ->
            val stub = stubs[matchers]
            if (ParametersSpec.run { matchers.matches(values) } && stub != null) matchers.asList().sorted() to stub else null
        }
        // Ensure there is at least one stub. Otherwise fail
        if (matchingStubs.isEmpty()) { fail { "No matching stubs found for $values" } }
        val matchedStubs = (0..matchingStubs.first().first.size).fold(matchingStubs) { remainingMatches, index ->
            // Find the best matching stub.
            // Iterate over the length of the number of parameters passed to the method
            remainingMatches.fold(emptyList()) inner@{ acc, possibleBestMatch ->
                // If we dont have a best possible match yet, just use the first element.
                if (acc.isEmpty()) {
                    return@inner listOf(possibleBestMatch)
                }
                // Check whether possibleBestMatch has better, equal, or worse parameter matching for the parameter at index
                val matcherAtIndexForAcc = acc.first().first[index]
                val matcherAtIndexForPossibleBestMatch = possibleBestMatch.first[index]
                when {
                    matcherAtIndexForAcc < matcherAtIndexForPossibleBestMatch -> acc // Worse, so return acc
                    matcherAtIndexForAcc == matcherAtIndexForPossibleBestMatch -> acc.toMutableList() + possibleBestMatch // Equal so return acc + possibleBestMatch
                    else -> listOf(possibleBestMatch) // Better so return possibleBestMatch
                }
            }
        }
        // Return the first element of the remaining list of stubbs
        if (matchedStubs.isEmpty()) { fail { "No matching stubs found for $values" } }
        return matchedStubs.first().second
    }

    /**
     * Resets all calls to this mock method. This means that `verify(times=0)` should succeed
     */
    fun resetCalls() {
        callParameters.clear()
    }

    /**
     * Removes all stubbs from this mock method. Note that this also removes any default stubbs that may have been created when declaring the mock.
     */
    fun resetStubs() {
        stubs.clear()
    }

    /**
     * Resets both calls and stubbs. Shorthand for [resetCalls] and [resetStubs]
     */
    fun reset() {
        resetCalls()
        resetStubs()
    }

    internal fun verify(parameters: C, times: Int = 1) {
        verify(parameters, VerificationRule.times(times))
    }

    internal fun verify(parameters: C, verificationRule: VerificationRule) = ParametersSpec.apply {
        val matchers = parameters.asMatchers()
        val matchedCalls = callParameters.filter {
            matchers.matches(it)
        }
        expect(verificationRule.matches(matchedCalls.size)) {
            "Expected $verificationRule but got ${matchedCalls.size} times"
        }
        matchedCalls.forEach {
            parameters.capture(it)
        }
    }
}

/**
 * Sets the answer of a [BaseMethodMock.Stub] that returns Unit to simply return Unit.
 */
fun <
    M : ParametersSpec.Matchers,
    V : ParametersSpec.Values,
    A : BaseAnswer<V, Unit>,
    Stub : BaseMethodMock.Stub<M, V, Unit, A>> Stub.doReturn() {
    doExecute { }
}

/**
 * A [BaseMethodMock] for non-suspending methods.
 */
class MethodMock<M : ParametersSpec.Matchers,
    C : ParametersSpec.MatchersOrCaptor<M>,
    V : ParametersSpec.Values,
    W : ParametersSpec<M, C, V>,
    R>(override val ParametersSpec: W) : BaseMethodMock<M, C, V, W, R, Answer<V, R>, MethodMock.Stub<M, V, R>>() {
    internal fun call(arguments: V): R = getStubFor(arguments).call(arguments)

    override fun createStub(matcher: M): Stub<M, V, R> = Stub(matcher)

    /**
     * A [BaseMethodMock.Stub] for non-suspending methods.
     */
    class Stub<
        M : ParametersSpec.Matchers,
        V : ParametersSpec.Values,
        R>(override val matchers: M) : BaseMethodMock.Stub<M, V, R, Answer<V, R>>() {
        override fun createAnswer(result: (V) -> R): Answer<V, R> = object : Answer<V, R> {
            override fun call(arguments: V): R = result(arguments)
        }

        /**
         * Calls this stub with a given set of arguments
         * @param arguments the arguments with which to call the stub.
         */
        fun call(arguments: V): R {
            val answer = this.answer.get() ?: fail { "No Answer set for this stub" }
            return answer.call(arguments)
        }
    }
}

/**
 * A [BaseMethodMock] for suspending methods.
 */
class SuspendMethodMock<
    M : ParametersSpec.Matchers,
    C : ParametersSpec.MatchersOrCaptor<M>,
    V : ParametersSpec.Values,
    W : ParametersSpec<M, C, V>,
    R>(override val ParametersSpec: W) : BaseMethodMock<M, C, V, W, R, SuspendedAnswer<V, R>, SuspendMethodMock.Stub<M, V, R>>() {

    internal suspend fun call(arguments: V): R = getStubFor(arguments).call(arguments)

    override fun createStub(matcher: M): Stub<M, V, R> =
        Stub(matcher)

    /**
     * A [BaseMethodMock.Stub] for suspending methods.
     */
    class Stub<
        M : ParametersSpec.Matchers,
        V : ParametersSpec.Values,
        R
        >(override val matchers: M) : BaseMethodMock.Stub<M, V, R, SuspendedAnswer<V, R>>() {
        override fun createAnswer(result: (V) -> R): SuspendedAnswer<V, R> = object : SuspendedAnswer<V, R> {
            override suspend fun call(arguments: V): R = result(arguments)
        }

        /**
         * Calls this stub with a given set of arguments
         * @param arguments the arguments with which to call the stub.
         */
        suspend fun call(arguments: V): R {
            val answer = this.answer.get() ?: fail { "No Answer set for this stub" }
            return answer.call(arguments)
        }

        /**
         * Sets the answer to execute a given suspended code block.
         * @param action The code block to execute as an answer to the stub being called.
         */
        fun doExecuteSuspended(action: suspend (V) -> R) = doAnswer(
            object : SuspendedAnswer<V, R> {
                override suspend fun call(arguments: V): R = action(arguments)
            }
        )

        /**
         * Sets the answer await and return the value of a given [Deferred].
         * @param deferred The [Deferred] to wait for and return.
         * @return a [Deferred] to be completed when this answer is called.
         */
        fun doAwait(deferred: Deferred<R>): Deferred<V> {
            val isCalled = CompletableDeferred<V>()
            doExecuteSuspended {
                isCalled.complete(it)
                deferred.await()
            }
            return isCalled
        }
    }
}
