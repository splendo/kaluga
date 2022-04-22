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

package com.splendo.kaluga.test.mock

import com.splendo.kaluga.test.mock.answer.Answer
import com.splendo.kaluga.test.mock.answer.BaseAnswer
import com.splendo.kaluga.test.mock.answer.SuspendedAnswer
import com.splendo.kaluga.test.mock.parameters.ParametersSpec
import com.splendo.kaluga.test.mock.verification.VerificationRule
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

private fun expect(condition: Boolean, message: () -> String) {
    if (!condition) fail(message)
}

private fun fail(message: () -> String): Nothing = throw AssertionError(message())

abstract class BaseMethodMock<
    M : ParametersSpec.Matchers,
    C : ParametersSpec.MatchersOrCaptor<M>,
    V : ParametersSpec.Values,
    W : ParametersSpec<M, C, V>,
    R,
    A : BaseAnswer<V, R>,
    S : BaseMethodMock.Stub<M, V, R, A>
    > {

    abstract class Stub<
        M : ParametersSpec.Matchers,
        V : ParametersSpec.Values,
        R,
        A : BaseAnswer<V, R>
        > {

        abstract val matchers: M
        protected var answer: A? = null

        protected abstract fun createAnswer(result: (V) -> R): A

        fun doAnswer(answer: A) {
            this.answer = answer
        }
        fun doExecute(action: (V) -> R) = doAnswer(createAnswer(action))
        fun doReturn(value: R) = doExecute { value }
        fun doThrow(throwable: Throwable) = doExecute { throw throwable }
    }

    private val stubs: MutableMap<M, S> = mutableMapOf()
    private val callParameters: MutableList<V> = mutableListOf()
    protected abstract val ParametersSpec: W

    protected abstract fun createStub(matcher: M): S

    internal fun on(matcher: M): S =
        createStub(matcher).also {
            stubs[matcher] = it
        }

    protected fun getStubFor(values: V): S {
        callParameters.add(values)
        val matchingStubs = stubs.mapNotNull { (matchers, stub) ->
            if (ParametersSpec.run { matchers.matches(values) }) matchers.asList().sorted() to stub else null
        }
        if (matchingStubs.isEmpty()) { fail { "No matching stubs found for $values" } }
        val matchedStubs = (0..matchingStubs.first().first.size).fold(matchingStubs) { remainingMatches, index ->
            remainingMatches.fold(emptyList()) inner@ { acc, possibleBestMatch ->
                if (acc.isEmpty()) {
                    return@inner listOf(possibleBestMatch)
                }
                val matcherAtIndexForAcc = acc.first().first[index]
                val matcherAtIndexForPossibleBestMatch = possibleBestMatch.first[index]
                when {
                    matcherAtIndexForAcc < matcherAtIndexForPossibleBestMatch -> acc
                    matcherAtIndexForAcc == matcherAtIndexForPossibleBestMatch -> acc.toMutableList() + possibleBestMatch
                    else -> listOf(possibleBestMatch)
                }
            }
        }
        if (matchedStubs.isEmpty()) { fail { "No matching stubs found for $values" } }
        return matchedStubs.first().second
    }

    fun resetCalls() {
        callParameters.clear()
    }

    fun resetStubs() {
        stubs.clear()
    }

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

fun <
    M : ParametersSpec.Matchers,
    V : ParametersSpec.Values,
    A : BaseAnswer<V, Unit>,
    Stub : BaseMethodMock.Stub<M, V, Unit, A>> Stub.doReturn() {
        doExecute { }
    }

class MethodMock<M : ParametersSpec.Matchers,
    C : ParametersSpec.MatchersOrCaptor<M>,
    V : ParametersSpec.Values,
    W : ParametersSpec<M, C, V>,
    R>(override val ParametersSpec: W): BaseMethodMock<M, C, V, W, R, Answer<V, R>, MethodMock.Stub<M, V, R>>() {
    internal fun call(arguments: V): R = getStubFor(arguments).call(arguments)

    override fun createStub(matcher: M): Stub<M, V, R> = Stub(matcher)

    class Stub<
        M : ParametersSpec.Matchers,
        V : ParametersSpec.Values,
        R>(override val matchers: M) : BaseMethodMock.Stub<M, V, R, Answer<V, R>>() {
        override fun createAnswer(result: (V) -> R): Answer<V, R> = object : Answer<V, R> {
            override fun call(arguments: V): R = result(arguments)
        }
        fun call(arguments: V): R {
            val answer = this.answer ?: fail { "No Answer set for this stub" }
            return answer.call(arguments)
        }
    }

}

class SuspendMethodMock<
    M : ParametersSpec.Matchers,
    C : ParametersSpec.MatchersOrCaptor<M>,
    V : ParametersSpec.Values,
    W : ParametersSpec<M, C, V>,
    R>(override val ParametersSpec: W) : BaseMethodMock<M, C, V, W, R, SuspendedAnswer<V, R>, SuspendMethodMock.Stub<M, V, R>>() {

    internal suspend fun call(arguments: V): R = getStubFor(arguments).call(arguments)

    override fun createStub(matcher: M): Stub<M, V, R> =
        Stub(matcher)

    class Stub<
        M : ParametersSpec.Matchers,
        V : ParametersSpec.Values,
        R
        >(override val matchers: M) : BaseMethodMock.Stub<M, V, R, SuspendedAnswer<V, R>>() {
        override fun createAnswer(result: (V) -> R): SuspendedAnswer<V, R> = object : SuspendedAnswer<V, R> {
            override suspend fun call(arguments: V): R = result(arguments)
        }

        suspend fun call(arguments: V): R {
            val answer = this.answer ?: fail { "No Answer set for this stub" }
            return answer.call(arguments)
        }

        fun doExecuteSuspended(action: suspend (V) -> R) = doAnswer(
            object : SuspendedAnswer<V, R> {
                override suspend fun call(arguments: V): R = action(arguments)
            }
        )
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
