package com.splendo.kaluga.test.mock

import com.splendo.kaluga.test.mock.answer.BaseAnswer
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.mock.parameters.PairParameters
import com.splendo.kaluga.test.mock.parameters.QuadrupleParameters
import com.splendo.kaluga.test.mock.parameters.QuintupleParameters
import com.splendo.kaluga.test.mock.parameters.SingleParameters
import com.splendo.kaluga.test.mock.parameters.TripleParameters
import com.splendo.kaluga.test.mock.parameters.VoidParameters

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
    >.on(): Stub {
    return on(VoidParameters.Matchers)
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
    >.on(value: ParameterMatcher<Value>): Stub {
    return on(SingleParameters.Matchers(value))
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
    >.on(first: ParameterMatcher<T0>, second: ParameterMatcher<T1>): Stub {
    return on(PairParameters.Matchers(first, second))
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
    >.on(first: ParameterMatcher<T0>, second: ParameterMatcher<T1>, third: ParameterMatcher<T2>): Stub {
    return on(TripleParameters.Matchers(first, second, third))
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
    >.on(first: ParameterMatcher<T0>, second: ParameterMatcher<T1>, third: ParameterMatcher<T2>, fourth: ParameterMatcher<T3>): Stub {
    return on(QuadrupleParameters.Matchers(first, second, third, fourth))
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
    >.on(first: ParameterMatcher<T0>, second: ParameterMatcher<T1>, third: ParameterMatcher<T2>, fourth: ParameterMatcher<T3>, fifth: ParameterMatcher<T4>): Stub {
    return on(QuintupleParameters.Matchers(first, second, third, fourth, fifth))
}
