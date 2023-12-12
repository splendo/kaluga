/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.test.base.mock.answer.BaseAnswer
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcherOrCaptor
import com.splendo.kaluga.test.base.mock.parameters.PairParameters
import com.splendo.kaluga.test.base.mock.parameters.QuadrupleParameters
import com.splendo.kaluga.test.base.mock.parameters.QuintupleParameters
import com.splendo.kaluga.test.base.mock.parameters.SingleParameters
import com.splendo.kaluga.test.base.mock.parameters.TripleParameters
import com.splendo.kaluga.test.base.mock.parameters.VoidParameters
import com.splendo.kaluga.test.base.mock.verification.VerificationRule
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

val DEFAULT_WITHIN = 1.seconds

/**
 * Verifies that a [BaseMethodMock] without parameters has been called within the duration
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param times The number of times the mock must be called
 *
 */
suspend fun <
    Result,
    Answer : BaseAnswer<VoidParameters.Values, Result>,
    Stub : BaseMethodMock.Stub<
        VoidParameters.Matchers,
        VoidParameters.Values,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    VoidParameters.Matchers,
    VoidParameters.MatchersOrCaptor,
    VoidParameters.Values,
    VoidParameters,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    times: Int = 1,
) {
    verifyWithParametersWithin(duration, VoidParameters.MatchersOrCaptor, times)
}

/**
 * Verifies that a [BaseMethodMock] without parameters has been called
 *
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param rule The [VerificationRule] for matching the calls
 */
suspend fun <
    Result,
    Answer : BaseAnswer<VoidParameters.Values, Result>,
    Stub : BaseMethodMock.Stub<
        VoidParameters.Matchers,
        VoidParameters.Values,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    VoidParameters.Matchers,
    VoidParameters.MatchersOrCaptor,
    VoidParameters.Values,
    VoidParameters,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    rule: VerificationRule,
) {
    verifyWithParametersWithin(duration, VoidParameters.MatchersOrCaptor, rule)
}

/**
 * Verifies that a [BaseMethodMock] with one parameters has been called with a [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param value The [ParameterMatcherOrCaptor] for the parameter
 * @param times The number of times the mock must be called the parameter matching [ParameterMatcherOrCaptor]
 */
suspend fun <
    Value,
    Result,
    Answer : BaseAnswer<SingleParameters.Values<Value>, Result>,
    Stub : BaseMethodMock.Stub<
        SingleParameters.Matchers<Value>,
        SingleParameters.Values<Value>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    SingleParameters.Matchers<Value>,
    SingleParameters.MatchersOrCaptor<Value>,
    SingleParameters.Values<Value>,
    SingleParameters<Value>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    value: ParameterMatcherOrCaptor<Value> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParametersWithin(duration, SingleParameters.MatchersOrCaptor(value), times)
}

/**
 * Verifies that a [BaseMethodMock] with one parameters has been called with a [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param value The [ParameterMatcherOrCaptor] for the parameter
 * @param rule The [VerificationRule] for matching the calls
 */
suspend fun <
    Value,
    Result,
    Answer : BaseAnswer<SingleParameters.Values<Value>, Result>,
    Stub : BaseMethodMock.Stub<
        SingleParameters.Matchers<Value>,
        SingleParameters.Values<Value>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    SingleParameters.Matchers<Value>,
    SingleParameters.MatchersOrCaptor<Value>,
    SingleParameters.Values<Value>,
    SingleParameters<Value>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    value: ParameterMatcherOrCaptor<Value> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParametersWithin(duration, SingleParameters.MatchersOrCaptor(value), rule)
}

/**
 * Verifies that a [BaseMethodMock] with two parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param times The number of times the mock must be called with parameters matching their [ParameterMatcherOrCaptor]
 */
suspend fun <
    T0,
    T1,
    Result,
    Answer : BaseAnswer<PairParameters.Values<T0, T1>, Result>,
    Stub : BaseMethodMock.Stub<
        PairParameters.Matchers<T0, T1>,
        PairParameters.Values<T0, T1>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    PairParameters.Matchers<T0, T1>,
    PairParameters.MatchersOrCaptor<T0, T1>,
    PairParameters.Values<T0, T1>,
    PairParameters<T0, T1>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParametersWithin(duration, PairParameters.MatchersOrCaptor(first, second), times)
}

/**
 * Verifies that a [BaseMethodMock] with two parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param rule The [VerificationRule] for matching the calls
 */
suspend fun <
    T0,
    T1,
    Result,
    Answer : BaseAnswer<PairParameters.Values<T0, T1>, Result>,
    Stub : BaseMethodMock.Stub<
        PairParameters.Matchers<T0, T1>,
        PairParameters.Values<T0, T1>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    PairParameters.Matchers<T0, T1>,
    PairParameters.MatchersOrCaptor<T0, T1>,
    PairParameters.Values<T0, T1>,
    PairParameters<T0, T1>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParametersWithin(duration, PairParameters.MatchersOrCaptor(first, second), rule)
}

/**
 * Verifies that a [BaseMethodMock] with three parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param times The number of times the mock must be called with parameters matching their [ParameterMatcherOrCaptor]
 */
suspend fun <
    T0,
    T1,
    T2,
    Result,
    Answer : BaseAnswer<TripleParameters.Values<T0, T1, T2>, Result>,
    Stub : BaseMethodMock.Stub<
        TripleParameters.Matchers<T0, T1, T2>,
        TripleParameters.Values<T0, T1, T2>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    TripleParameters.Matchers<T0, T1, T2>,
    TripleParameters.MatchersOrCaptor<T0, T1, T2>,
    TripleParameters.Values<T0, T1, T2>,
    TripleParameters<T0, T1, T2>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParametersWithin(duration, TripleParameters.MatchersOrCaptor(first, second, third), times)
}

/**
 * Verifies that a [BaseMethodMock] with three parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param rule The [VerificationRule] for matching the calls
 */
suspend fun <
    T0,
    T1,
    T2,
    Result,
    Answer : BaseAnswer<TripleParameters.Values<T0, T1, T2>, Result>,
    Stub : BaseMethodMock.Stub<
        TripleParameters.Matchers<T0, T1, T2>,
        TripleParameters.Values<T0, T1, T2>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    TripleParameters.Matchers<T0, T1, T2>,
    TripleParameters.MatchersOrCaptor<T0, T1, T2>,
    TripleParameters.Values<T0, T1, T2>,
    TripleParameters<T0, T1, T2>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParametersWithin(duration, TripleParameters.MatchersOrCaptor(first, second, third), rule)
}

/**
 * Verifies that a [BaseMethodMock] with four parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param fourth The [ParameterMatcherOrCaptor] for the fourth parameter
 * @param times The number of times the mock must be called with parameters matching their [ParameterMatcherOrCaptor]
 */
suspend fun <
    T0,
    T1,
    T2,
    T3,
    Result,
    Answer : BaseAnswer<QuadrupleParameters.Values<T0, T1, T2, T3>, Result>,
    Stub : BaseMethodMock.Stub<
        QuadrupleParameters.Matchers<T0, T1, T2, T3>,
        QuadrupleParameters.Values<T0, T1, T2, T3>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    QuadrupleParameters.Matchers<T0, T1, T2, T3>,
    QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>,
    QuadrupleParameters.Values<T0, T1, T2, T3>,
    QuadrupleParameters<T0, T1, T2, T3>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParametersWithin(duration, QuadrupleParameters.MatchersOrCaptor(first, second, third, fourth), times)
}

/**
 * Verifies that a [BaseMethodMock] with four parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param fourth The [ParameterMatcherOrCaptor] for the fourth parameter
 * @param rule The [VerificationRule] for matching the calls
 */
suspend fun <
    T0,
    T1,
    T2,
    T3,
    Result,
    Answer : BaseAnswer<QuadrupleParameters.Values<T0, T1, T2, T3>, Result>,
    Stub : BaseMethodMock.Stub<
        QuadrupleParameters.Matchers<T0, T1, T2, T3>,
        QuadrupleParameters.Values<T0, T1, T2, T3>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    QuadrupleParameters.Matchers<T0, T1, T2, T3>,
    QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>,
    QuadrupleParameters.Values<T0, T1, T2, T3>,
    QuadrupleParameters<T0, T1, T2, T3>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParametersWithin(duration, QuadrupleParameters.MatchersOrCaptor(first, second, third, fourth), rule)
}

/**
 * Verifies that a [BaseMethodMock] with five parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param fourth The [ParameterMatcherOrCaptor] for the fourth parameter
 * @param fifth The [ParameterMatcherOrCaptor] for the fifth parameter
 * @param times The number of times the mock must be called with parameters matching their [ParameterMatcherOrCaptor]
 */
suspend fun <
    T0,
    T1,
    T2,
    T3,
    T4,
    Result,
    Answer : BaseAnswer<QuintupleParameters.Values<T0, T1, T2, T3, T4>, Result>,
    Stub : BaseMethodMock.Stub<
        QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
        QuintupleParameters.Values<T0, T1, T2, T3, T4>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    QuintupleParameters<T0, T1, T2, T3, T4>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    fifth: ParameterMatcherOrCaptor<T4> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParametersWithin(duration, QuintupleParameters.MatchersOrCaptor(first, second, third, fourth, fifth), times)
}

/**
 * Verifies that a [BaseMethodMock] with five parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param duration The duration within which the verification condition has to be matched at least once
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param fourth The [ParameterMatcherOrCaptor] for the fourth parameter
 * @param fifth The [ParameterMatcherOrCaptor] for the fifth parameter
 * @param rule The [VerificationRule] for matching the calls
 */
suspend fun <
    T0,
    T1,
    T2,
    T3,
    T4,
    Result,
    Answer : BaseAnswer<QuintupleParameters.Values<T0, T1, T2, T3, T4>, Result>,
    Stub : BaseMethodMock.Stub<
        QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
        QuintupleParameters.Values<T0, T1, T2, T3, T4>,
        Result,
        Answer,
        >,
    > BaseMethodMock<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    QuintupleParameters<T0, T1, T2, T3, T4>,
    Result,
    Answer,
    Stub,
    >.verifyWithin(
    duration: Duration = DEFAULT_WITHIN,
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    fifth: ParameterMatcherOrCaptor<T4> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParametersWithin(duration, QuintupleParameters.MatchersOrCaptor(first, second, third, fourth, fifth), rule)
}
