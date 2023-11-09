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

import com.splendo.kaluga.test.base.mock.parameters.PairParameters
import com.splendo.kaluga.test.base.mock.parameters.QuadrupleParameters
import com.splendo.kaluga.test.base.mock.parameters.QuintupleParameters
import com.splendo.kaluga.test.base.mock.parameters.SingleParameters
import com.splendo.kaluga.test.base.mock.parameters.TripleParameters
import com.splendo.kaluga.test.base.mock.parameters.VoidParameters

/**
 * Calls a [MethodMock] without any parameters
 */
fun <
    Result,
    > MethodMock<
    VoidParameters.Matchers,
    VoidParameters.MatchersOrCaptor,
    VoidParameters.Values,
    VoidParameters,
    Result,
    >.call(): Result {
    return callWithValues(VoidParameters.Values)
}

/**
 * Calls a [SuspendMethodMock] without any parameters
 */
suspend fun <
    Result,
    > SuspendMethodMock<
    VoidParameters.Matchers,
    VoidParameters.MatchersOrCaptor,
    VoidParameters.Values,
    VoidParameters,
    Result,
    >.call(): Result {
    return callWithValues(VoidParameters.Values)
}

/**
 * Calls a [MethodMock] with one parameter
 * @param value The parameter to pass to the method
 */
fun <
    Value,
    Result,
    > MethodMock<
    SingleParameters.Matchers<Value>,
    SingleParameters.MatchersOrCaptor<Value>,
    SingleParameters.Values<Value>,
    SingleParameters<Value>,
    Result,
    >.call(
    value: Value,
): Result {
    return callWithValues(SingleParameters.Values(value))
}

/**
 * Calls a [SuspendMethodMock] with one parameter
 * @param value The parameter to pass to the method
 */
suspend fun <
    Value,
    Result,
    > SuspendMethodMock<
    SingleParameters.Matchers<Value>,
    SingleParameters.MatchersOrCaptor<Value>,
    SingleParameters.Values<Value>,
    SingleParameters<Value>,
    Result,
    >.call(
    value: Value,
): Result {
    return callWithValues(SingleParameters.Values(value))
}

/**
 * Calls a [MethodMock] with two parameters
 * @param first The first parameter to pass to the method
 * @param second The second parameter to pass to the method
 */
fun <
    T0,
    T1,
    Result,
    > MethodMock<
    PairParameters.Matchers<T0, T1>,
    PairParameters.MatchersOrCaptor<T0, T1>,
    PairParameters.Values<T0, T1>,
    PairParameters<T0, T1>,
    Result,
    >.call(
    first: T0,
    second: T1,
): Result {
    return callWithValues(PairParameters.Values(first, second))
}

/**
 * Calls a [SuspendMethodMock] with two parameters
 * @param first The first parameter to pass to the method
 * @param second The second parameter to pass to the method
 */
suspend fun <
    T0,
    T1,
    Result,
    > SuspendMethodMock<
    PairParameters.Matchers<T0, T1>,
    PairParameters.MatchersOrCaptor<T0, T1>,
    PairParameters.Values<T0, T1>,
    PairParameters<T0, T1>,
    Result,
    >.call(
    first: T0,
    second: T1,
): Result {
    return callWithValues(PairParameters.Values(first, second))
}

/**
 * Calls a [MethodMock] with three parameters
 * @param first The first parameter to pass to the method
 * @param second The second parameter to pass to the method
 * @param third The third parameter to pass to the method
 */
fun <
    T0,
    T1,
    T2,
    Result,
    > MethodMock<
    TripleParameters.Matchers<T0, T1, T2>,
    TripleParameters.MatchersOrCaptor<T0, T1, T2>,
    TripleParameters.Values<T0, T1, T2>,
    TripleParameters<T0, T1, T2>,
    Result,
    >.call(
    first: T0,
    second: T1,
    third: T2,
): Result {
    return callWithValues(TripleParameters.Values(first, second, third))
}

/**
 * Calls a [SuspendMethodMock] with three parameters
 * @param first The first parameter to pass to the method
 * @param second The second parameter to pass to the method
 * @param third The third parameter to pass to the method
 */
suspend fun <
    T0,
    T1,
    T2,
    Result,
    > SuspendMethodMock<
    TripleParameters.Matchers<T0, T1, T2>,
    TripleParameters.MatchersOrCaptor<T0, T1, T2>,
    TripleParameters.Values<T0, T1, T2>,
    TripleParameters<T0, T1, T2>,
    Result,
    >.call(
    first: T0,
    second: T1,
    third: T2,
): Result {
    return callWithValues(TripleParameters.Values(first, second, third))
}

/**
 * Calls a [MethodMock] with four parameters
 * @param first The first parameter to pass to the method
 * @param second The second parameter to pass to the method
 * @param third The third parameter to pass to the method
 * @param fourth The fourth parameter to pass to the method
 */
fun <
    T0,
    T1,
    T2,
    T3,
    Result,
    > MethodMock<
    QuadrupleParameters.Matchers<T0, T1, T2, T3>,
    QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>,
    QuadrupleParameters.Values<T0, T1, T2, T3>,
    QuadrupleParameters<T0, T1, T2, T3>,
    Result,
    >.call(
    first: T0,
    second: T1,
    third: T2,
    fourth: T3,
): Result {
    return callWithValues(QuadrupleParameters.Values(first, second, third, fourth))
}

/**
 * Calls a [SuspendMethodMock] with four parameters
 * @param first The first parameter to pass to the method
 * @param second The second parameter to pass to the method
 * @param third The third parameter to pass to the method
 * @param fourth The fourth parameter to pass to the method
 */
suspend fun <
    T0,
    T1,
    T2,
    T3,
    Result,
    > SuspendMethodMock<
    QuadrupleParameters.Matchers<T0, T1, T2, T3>,
    QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>,
    QuadrupleParameters.Values<T0, T1, T2, T3>,
    QuadrupleParameters<T0, T1, T2, T3>,
    Result,
    >.call(
    first: T0,
    second: T1,
    third: T2,
    fourth: T3,
): Result {
    return callWithValues(QuadrupleParameters.Values(first, second, third, fourth))
}

/**
 * Calls a [MethodMock] with five parameters
 * @param first The first parameter to pass to the method
 * @param second The second parameter to pass to the method
 * @param third The third parameter to pass to the method
 * @param fourth The fourth parameter to pass to the method
 * @param fifth The fifth parameter to pass to the method
 */
fun <
    T0,
    T1,
    T2,
    T3,
    T4,
    Result,
    > MethodMock<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    QuintupleParameters<T0, T1, T2, T3, T4>,
    Result,
    >.call(
    first: T0,
    second: T1,
    third: T2,
    fourth: T3,
    fifth: T4,
): Result {
    return callWithValues(QuintupleParameters.Values(first, second, third, fourth, fifth))
}

/**
 * Calls a [SuspendMethodMock] with five parameters
 * @param first The first parameter to pass to the method
 * @param second The second parameter to pass to the method
 * @param third The third parameter to pass to the method
 * @param fourth The fourth parameter to pass to the method
 * @param fifth The fifth parameter to pass to the method
 */
suspend fun <
    T0,
    T1,
    T2,
    T3,
    T4,
    Result,
    > SuspendMethodMock<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    QuintupleParameters<T0, T1, T2, T3, T4>,
    Result,
    >.call(
    first: T0,
    second: T1,
    third: T2,
    fourth: T3,
    fifth: T4,
): Result {
    return callWithValues(QuintupleParameters.Values(first, second, third, fourth, fifth))
}
