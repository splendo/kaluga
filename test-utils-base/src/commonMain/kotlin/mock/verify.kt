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

fun <
    Result,
    Answer : BaseAnswer<VoidParameters.Values, Result>,
    Stub : BaseMethodMock.Stub<
        VoidParameters.Matchers,
        VoidParameters.Values,
        Result,
        Answer
        >
    > BaseMethodMock<
    VoidParameters.Matchers,
    VoidParameters.MatchersOrCaptor,
    VoidParameters.Values,
    VoidParameters,
    Result,
    Answer,
    Stub
    >.verify(times: Int = 1) {
    verify(VoidParameters.MatchersOrCaptor, times)
}

fun <
    Result,
    Answer : BaseAnswer<VoidParameters.Values, Result>,
    Stub : BaseMethodMock.Stub<
        VoidParameters.Matchers,
        VoidParameters.Values,
        Result,
        Answer
        >
    > BaseMethodMock<
    VoidParameters.Matchers,
    VoidParameters.MatchersOrCaptor,
    VoidParameters.Values,
    VoidParameters,
    Result,
    Answer,
    Stub
    >.verify(rule: VerificationRule) {
    verify(VoidParameters.MatchersOrCaptor, rule)
}

fun <
    Value,
    Result,
    Answer : BaseAnswer<SingleParameters.Values<Value>, Result>,
    Stub : BaseMethodMock.Stub<
        SingleParameters.Matchers<Value>,
        SingleParameters.Values<Value>,
        Result,
        Answer
        >
    > BaseMethodMock<
    SingleParameters.Matchers<Value>,
    SingleParameters.MatchersOrCaptor<Value>,
    SingleParameters.Values<Value>,
    SingleParameters<Value>,
    Result,
    Answer,
    Stub
    >.verify(value: ParameterMatcherOrCaptor<Value> = ParameterMatcher.any(), times: Int = 1) {
    verify(SingleParameters.MatchersOrCaptor(value), times)
}

fun <
    Value,
    Result,
    Answer : BaseAnswer<SingleParameters.Values<Value>, Result>,
    Stub : BaseMethodMock.Stub<
        SingleParameters.Matchers<Value>,
        SingleParameters.Values<Value>,
        Result,
        Answer
        >
    > BaseMethodMock<
    SingleParameters.Matchers<Value>,
    SingleParameters.MatchersOrCaptor<Value>,
    SingleParameters.Values<Value>,
    SingleParameters<Value>,
    Result,
    Answer,
    Stub
    >.verify(
    value: ParameterMatcherOrCaptor<Value> = ParameterMatcher.any(),
    rule: VerificationRule
) {
    verify(SingleParameters.MatchersOrCaptor(value), rule)
}

fun <
    T0,
    T1,
    Result,
    Answer : BaseAnswer<PairParameters.Values<T0, T1>, Result>,
    Stub : BaseMethodMock.Stub<
        PairParameters.Matchers<T0, T1>,
        PairParameters.Values<T0, T1>,
        Result,
        Answer
        >
    > BaseMethodMock<
    PairParameters.Matchers<T0, T1>,
    PairParameters.MatchersOrCaptor<T0, T1>,
    PairParameters.Values<T0, T1>,
    PairParameters<T0, T1>,
    Result,
    Answer,
    Stub
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    times: Int = 1
) {
    verify(PairParameters.MatchersOrCaptor(first, second), times)
}

fun <
    T0,
    T1,
    Result,
    Answer : BaseAnswer<PairParameters.Values<T0, T1>, Result>,
    Stub : BaseMethodMock.Stub<
        PairParameters.Matchers<T0, T1>,
        PairParameters.Values<T0, T1>,
        Result,
        Answer
        >
    > BaseMethodMock<
    PairParameters.Matchers<T0, T1>,
    PairParameters.MatchersOrCaptor<T0, T1>,
    PairParameters.Values<T0, T1>,
    PairParameters<T0, T1>,
    Result,
    Answer,
    Stub
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    rule: VerificationRule
) {
    verify(PairParameters.MatchersOrCaptor(first, second), rule)
}

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
        Answer
        >
    > BaseMethodMock<
    TripleParameters.Matchers<T0, T1, T2>,
    TripleParameters.MatchersOrCaptor<T0, T1, T2>,
    TripleParameters.Values<T0, T1, T2>,
    TripleParameters<T0, T1, T2>,
    Result,
    Answer,
    Stub
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    times: Int = 1
) {
    verify(TripleParameters.MatchersOrCaptor(first, second, third), times)
}

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
        Answer
        >
    > BaseMethodMock<
    TripleParameters.Matchers<T0, T1, T2>,
    TripleParameters.MatchersOrCaptor<T0, T1, T2>,
    TripleParameters.Values<T0, T1, T2>,
    TripleParameters<T0, T1, T2>,
    Result,
    Answer,
    Stub
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    rule: VerificationRule
) {
    verify(TripleParameters.MatchersOrCaptor(first, second, third), rule)
}

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
        Answer
        >
    > BaseMethodMock<
    QuadrupleParameters.Matchers<T0, T1, T2, T3>,
    QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>,
    QuadrupleParameters.Values<T0, T1, T2, T3>,
    QuadrupleParameters<T0, T1, T2, T3>,
    Result,
    Answer,
    Stub
    >.verify(
    first: ParameterMatcher<T0> = ParameterMatcher.any(),
    second: ParameterMatcher<T1> = ParameterMatcher.any(),
    third: ParameterMatcher<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcher<T3> = ParameterMatcher.any(),
    times: Int = 1
) {
    verify(QuadrupleParameters.MatchersOrCaptor(first, second, third, fourth), times)
}

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
        Answer
        >
    > BaseMethodMock<
    QuadrupleParameters.Matchers<T0, T1, T2, T3>,
    QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>,
    QuadrupleParameters.Values<T0, T1, T2, T3>,
    QuadrupleParameters<T0, T1, T2, T3>,
    Result,
    Answer,
    Stub
    >.verify(
    first: ParameterMatcher<T0> = ParameterMatcher.any(),
    second: ParameterMatcher<T1> = ParameterMatcher.any(),
    third: ParameterMatcher<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcher<T3> = ParameterMatcher.any(),
    rule: VerificationRule
) {
    verify(QuadrupleParameters.MatchersOrCaptor(first, second, third, fourth), rule)
}

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
        Answer
        >
    > BaseMethodMock<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    QuintupleParameters<T0, T1, T2, T3, T4>,
    Result,
    Answer,
    Stub
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    fifth: ParameterMatcherOrCaptor<T4> = ParameterMatcher.any(),
    times: Int = 1
) {
    verify(QuintupleParameters.MatchersOrCaptor(first, second, third, fourth, fifth), times)
}

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
        Answer
        >
    > BaseMethodMock<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    QuintupleParameters<T0, T1, T2, T3, T4>,
    Result,
    Answer,
    Stub
    >.verify(
    first: ParameterMatcherOrCaptor<T0> = ParameterMatcher.any(),
    second: ParameterMatcherOrCaptor<T1> = ParameterMatcher.any(),
    third: ParameterMatcherOrCaptor<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcherOrCaptor<T3> = ParameterMatcher.any(),
    fifth: ParameterMatcherOrCaptor<T4> = ParameterMatcher.any(),
    rule: VerificationRule
) {
    verify(QuintupleParameters.MatchersOrCaptor(first, second, third, fourth, fifth), rule)
}
