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

import com.splendo.kaluga.test.mock.parameters.PairParameters
import com.splendo.kaluga.test.mock.parameters.QuadrupleParameters
import com.splendo.kaluga.test.mock.parameters.QuintupleParameters
import com.splendo.kaluga.test.mock.parameters.SingleParameters
import com.splendo.kaluga.test.mock.parameters.TripleParameters
import com.splendo.kaluga.test.mock.parameters.VoidParameters

fun <
    Result,
    > MethodMock<
    VoidParameters.Matchers,
    VoidParameters.MatchersOrCaptor,
    VoidParameters.Values,
    VoidParameters,
    Result
    >.call(): Result {
    return call(VoidParameters.Values)
}

suspend fun <
    Result,
    > SuspendMethodMock<
    VoidParameters.Matchers,
    VoidParameters.MatchersOrCaptor,
    VoidParameters.Values,
    VoidParameters,
    Result
    >.call(): Result {
    return call(VoidParameters.Values)
}

fun <
    Value,
    Result,
    > MethodMock<
    SingleParameters.Matchers<Value>,
    SingleParameters.MatchersOrCaptor<Value>,
    SingleParameters.Values<Value>,
    SingleParameters<Value>,
    Result
    >.call(value: Value): Result {
    return call(SingleParameters.Values(value))
}

suspend fun <
    Value,
    Result,
    > SuspendMethodMock<
    SingleParameters.Matchers<Value>,
    SingleParameters.MatchersOrCaptor<Value>,
    SingleParameters.Values<Value>,
    SingleParameters<Value>,
    Result
    >.call(value: Value): Result {
    return call(SingleParameters.Values(value))
}

fun <
    T0,
    T1,
    Result,
    > MethodMock<
    PairParameters.Matchers<T0, T1>,
    PairParameters.MatchersOrCaptor<T0, T1>,
    PairParameters.Values<T0, T1>,
    PairParameters<T0, T1>,
    Result
    >.call(first: T0, second: T1): Result {
    return call(PairParameters.Values(first, second))
}

suspend fun <
    T0,
    T1,
    Result,
    > SuspendMethodMock<
    PairParameters.Matchers<T0, T1>,
    PairParameters.MatchersOrCaptor<T0, T1>,
    PairParameters.Values<T0, T1>,
    PairParameters<T0, T1>,
    Result
    >.call(first: T0, second: T1): Result {
    return call(PairParameters.Values(first, second))
}

fun <
    T0,
    T1,
    T2,
    Result
    > MethodMock<
    TripleParameters.Matchers<T0, T1, T2>,
    TripleParameters.MatchersOrCaptor<T0, T1, T2>,
    TripleParameters.Values<T0, T1, T2>,
    TripleParameters<T0, T1, T2>,
    Result
    >.call(first: T0, second: T1, third: T2): Result {
    return call(TripleParameters.Values(first, second, third))
}

suspend fun <
    T0,
    T1,
    T2,
    Result
    > SuspendMethodMock<
    TripleParameters.Matchers<T0, T1, T2>,
    TripleParameters.MatchersOrCaptor<T0, T1, T2>,
    TripleParameters.Values<T0, T1, T2>,
    TripleParameters<T0, T1, T2>,
    Result
    >.call(first: T0, second: T1, third: T2): Result {
    return call(TripleParameters.Values(first, second, third))
}

fun <
    T0,
    T1,
    T2,
    T3,
    Result
    > MethodMock<
    QuadrupleParameters.Matchers<T0, T1, T2, T3>,
    QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>,
    QuadrupleParameters.Values<T0, T1, T2, T3>,
    QuadrupleParameters<T0, T1, T2, T3>,
    Result
    >.call(first: T0, second: T1, third: T2, fourth: T3): Result {
    return call(QuadrupleParameters.Values(first, second, third, fourth))
}

suspend fun <
    T0,
    T1,
    T2,
    T3,
    Result
    > SuspendMethodMock<
    QuadrupleParameters.Matchers<T0, T1, T2, T3>,
    QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>,
    QuadrupleParameters.Values<T0, T1, T2, T3>,
    QuadrupleParameters<T0, T1, T2, T3>,
    Result
    >.call(first: T0, second: T1, third: T2, fourth: T3): Result {
    return call(QuadrupleParameters.Values(first, second, third, fourth))
}

fun <
    T0,
    T1,
    T2,
    T3,
    T4,
    Result
    > MethodMock<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    QuintupleParameters<T0, T1, T2, T3, T4>,
    Result
    >.call(first: T0, second: T1, third: T2, fourth: T3, fifth: T4): Result {
    return call(QuintupleParameters.Values(first, second, third, fourth, fifth))
}

suspend fun <
    T0,
    T1,
    T2,
    T3,
    T4,
    Result
    > SuspendMethodMock<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    QuintupleParameters<T0, T1, T2, T3, T4>,
    Result
    >.call(first: T0, second: T1, third: T2, fourth: T3, fifth: T4): Result {
    return call(QuintupleParameters.Values(first, second, third, fourth, fifth))
}
