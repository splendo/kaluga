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

/**
 * Verifies that a [BaseMethodMock] without parameters has been called
 * @param times The number of times the mock must be called
 */
fun <
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
    >.verify(
    times: Int = 1,
) {
    verifyWithParameters(VoidParameters.MatchersOrCaptor, times)
}

/**
 * Verifies that a [BaseMethodMock] without parameters has been called
 * @param rule The [VerificationRule] for matching the calls
 */
fun <
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
    >.verify(
    rule: VerificationRule,
) {
    verifyWithParameters(VoidParameters.MatchersOrCaptor, rule)
}

/**
 * Verifies that a [BaseMethodMock] with one parameters has been called with a [ParameterMatcherOrCaptor]
 * @param value The [ParameterMatcherOrCaptor] for the parameter
 * @param times The number of times the mock must be called the parameter matching [ParameterMatcherOrCaptor]
 */
fun <
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
    >.verify(
    value: ParameterMatcherOrCaptor<Value> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParameters(SingleParameters.MatchersOrCaptor(value), times)
}

/**
 * Verifies that a [BaseMethodMock] with one parameters has been called with a [ParameterMatcherOrCaptor]
 * @param value The [ParameterMatcherOrCaptor] for the parameter
 * @param rule The [VerificationRule] for matching the calls
 */
fun <
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
    >.verify(
    value: ParameterMatcherOrCaptor<Value> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParameters(SingleParameters.MatchersOrCaptor(value), rule)
}

/**
 * Verifies that a [BaseMethodMock] with two parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param times The number of times the mock must be called with parameters matching their [ParameterMatcherOrCaptor]
 */
fun <
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
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParameters(PairParameters.MatchersOrCaptor(first, second), times)
}

/**
 * Verifies that a [BaseMethodMock] with two parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param rule The [VerificationRule] for matching the calls
 */
fun <
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
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParameters(PairParameters.MatchersOrCaptor(first, second), rule)
}

/**
 * Verifies that a [BaseMethodMock] with three parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param times The number of times the mock must be called with parameters matching their [ParameterMatcherOrCaptor]
 */
fun <
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
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParameters(TripleParameters.MatchersOrCaptor(first, second, third), times)
}

/**
 * Verifies that a [BaseMethodMock] with three parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param rule The [VerificationRule] for matching the calls
 */
fun <
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
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParameters(TripleParameters.MatchersOrCaptor(first, second, third), rule)
}

/**
 * Verifies that a [BaseMethodMock] with four parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param fourth The [ParameterMatcherOrCaptor] for the fourth parameter
 * @param times The number of times the mock must be called with parameters matching their [ParameterMatcherOrCaptor]
 */
fun <
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
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParameters(QuadrupleParameters.MatchersOrCaptor(first, second, third, fourth), times)
}

/**
 * Verifies that a [BaseMethodMock] with four parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param fourth The [ParameterMatcherOrCaptor] for the fourth parameter
 * @param rule The [VerificationRule] for matching the calls
 */
fun <
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
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParameters(QuadrupleParameters.MatchersOrCaptor(first, second, third, fourth), rule)
}

/**
 * Verifies that a [BaseMethodMock] with five parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param fourth The [ParameterMatcherOrCaptor] for the fourth parameter
 * @param fifth The [ParameterMatcherOrCaptor] for the fifth parameter
 * @param times The number of times the mock must be called with parameters matching their [ParameterMatcherOrCaptor]
 */
fun <
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
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    fifth: ParameterMatcherOrCaptor<T4> = ParameterMatcher.any(),
    times: Int = 1,
) {
    verifyWithParameters(QuintupleParameters.MatchersOrCaptor(first, second, third, fourth, fifth), times)
}

/**
 * Verifies that a [BaseMethodMock] with five parameters has been called with all parameters matching their respective [ParameterMatcherOrCaptor]
 * @param first The [ParameterMatcherOrCaptor] for the first parameter
 * @param second The [ParameterMatcherOrCaptor] for the second parameter
 * @param third The [ParameterMatcherOrCaptor] for the third parameter
 * @param fourth The [ParameterMatcherOrCaptor] for the fourth parameter
 * @param fifth The [ParameterMatcherOrCaptor] for the fifth parameter
 * @param rule The [VerificationRule] for matching the calls
 */
fun <
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
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    fifth: ParameterMatcherOrCaptor<T4> = ParameterMatcher.any(),
    rule: VerificationRule,
) {
    verifyWithParameters(QuintupleParameters.MatchersOrCaptor(first, second, third, fourth, fifth), rule)
}
