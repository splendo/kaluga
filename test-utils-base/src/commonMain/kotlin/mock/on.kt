package com.splendo.kaluga.test.base.mock

import com.splendo.kaluga.test.base.mock.answer.BaseAnswer
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.base.mock.parameters.PairParameters
import com.splendo.kaluga.test.base.mock.parameters.QuadrupleParameters
import com.splendo.kaluga.test.base.mock.parameters.QuintupleParameters
import com.splendo.kaluga.test.base.mock.parameters.SingleParameters
import com.splendo.kaluga.test.base.mock.parameters.TripleParameters
import com.splendo.kaluga.test.base.mock.parameters.VoidParameters

/**
 * Generates a stub for a [BaseMethodMock] without and parameters
 * @return The [Stub] associated with the [BaseMethodMock]
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
    >.on(): Stub =
    onMatcher(VoidParameters.Matchers)

/**
 * Generates a stub for a [BaseMethodMock] with one parameter
 * @param value The [ParameterMatcher] for the parameter of the method.
 * @return The [Stub] associated with the [BaseMethodMock]
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
    >.on(
    value: ParameterMatcher<Value> = ParameterMatcher.any(),
): Stub = onMatcher(SingleParameters.Matchers(value))

/**
 * Generates a stub for a [BaseMethodMock] with two parameters
 * @param first The [ParameterMatcher] for the first parameter of the method.
 * @param second The [ParameterMatcher] for the second parameter of the method.
 * @return The [Stub] associated with the [BaseMethodMock]
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
    >.on(
    first: ParameterMatcher<T0> = ParameterMatcher.any(),
    second: ParameterMatcher<T1> = ParameterMatcher.any(),
): Stub = onMatcher(PairParameters.Matchers(first, second))

/**
 * Generates a stub for a [BaseMethodMock] with three parameters
 * @param first The [ParameterMatcher] for the first parameter of the method.
 * @param second The [ParameterMatcher] for the second parameter of the method.
 * @param third The [ParameterMatcher] for the third parameter of the method.
 * @return The [Stub] associated with the [BaseMethodMock]
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
    >.on(
    first: ParameterMatcher<T0> = ParameterMatcher.any(),
    second: ParameterMatcher<T1> = ParameterMatcher.any(),
    third: ParameterMatcher<T2> = ParameterMatcher.any(),
): Stub = onMatcher(TripleParameters.Matchers(first, second, third))

/**
 * Generates a stub for a [BaseMethodMock] with four parameters
 * @param first The [ParameterMatcher] for the first parameter of the method.
 * @param second The [ParameterMatcher] for the second parameter of the method.
 * @param third The [ParameterMatcher] for the third parameter of the method.
 * @param fourth The [ParameterMatcher] for the fourth parameter of the method.
 * @return The [Stub] associated with the [BaseMethodMock]
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
    >.on(
    first: ParameterMatcher<T0> = ParameterMatcher.any(),
    second: ParameterMatcher<T1> = ParameterMatcher.any(),
    third: ParameterMatcher<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcher<T3> = ParameterMatcher.any(),
): Stub = onMatcher(QuadrupleParameters.Matchers(first, second, third, fourth))

/**
 * Generates a stub for a [BaseMethodMock] with five parameters
 * @param first The [ParameterMatcher] for the first parameter of the method.
 * @param second The [ParameterMatcher] for the second parameter of the method.
 * @param third The [ParameterMatcher] for the third parameter of the method.
 * @param fourth The [ParameterMatcher] for the fourth parameter of the method.
 * @param fifth The [ParameterMatcher] for the fifth parameter of the method.
 * @return The [Stub] associated with the [BaseMethodMock]
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
    >.on(
    first: ParameterMatcher<T0> = ParameterMatcher.any(),
    second: ParameterMatcher<T1> = ParameterMatcher.any(),
    third: ParameterMatcher<T2> = ParameterMatcher.any(),
    fourth: ParameterMatcher<T3> = ParameterMatcher.any(),
    fifth: ParameterMatcher<T4> = ParameterMatcher.any(),
): Stub = onMatcher(QuintupleParameters.Matchers(first, second, third, fourth, fifth))
