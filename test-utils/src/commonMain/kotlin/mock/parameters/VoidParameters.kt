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

package com.splendo.kaluga.test.mock.parameters

import com.splendo.kaluga.test.mock.MethodMock
import com.splendo.kaluga.test.mock.SuspendMethodMock
import com.splendo.kaluga.test.mock.answer.Answer
import com.splendo.kaluga.test.mock.answer.SuspendedAnswer
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.mock.on

object VoidParameters : ParametersSpec<VoidParameters.Matchers, VoidParameters.MatchersOrCaptor, VoidParameters.Values> {
    object Matchers : ParametersSpec.Matchers {
        override fun asList(): List<ParameterMatcher<*>> = emptyList()
    }
    object MatchersOrCaptor : ParametersSpec.MatchersOrCaptor<Matchers> {
        override fun asMatchers(): Matchers = Matchers
    }
    object Values : ParametersSpec.Values

    override fun Matchers.matches(values: Values): Boolean = true
    override fun MatchersOrCaptor.capture(values: Values) {}
}

fun <R> (() -> R).asMock() = MethodMock<VoidParameters.Matchers, VoidParameters.MatchersOrCaptor, VoidParameters.Values, VoidParameters, R>(VoidParameters)

fun <R> (() -> R).mock(defaultAnswer: Answer<VoidParameters.Values, R>) = asMock().also {
    it.on().doAnswer(defaultAnswer)
}
fun <R> (() -> R).mock(defaultValue: R) = asMock().also {
    it.on().doReturn(defaultValue)
}

fun (() -> Boolean).mock() = mock(false)
fun (() -> BooleanArray).mock() = mock(booleanArrayOf())
fun (() -> Byte).mock() = mock(0x0)
fun (() -> ByteArray).mock() = mock(byteArrayOf())
fun (() -> Char).mock() = mock(0.toChar())
fun (() -> CharArray).mock() = mock(charArrayOf())
fun (() -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
fun (() -> Double).mock() = mock(0.0)
fun (() -> DoubleArray).mock() = mock(doubleArrayOf())
fun (() -> Float).mock() = mock(0.0f)
fun (() -> FloatArray).mock() = mock(floatArrayOf())
fun (() -> Int).mock() = mock( 0)
fun (() -> IntArray).mock() = mock(intArrayOf())
fun (() -> IntRange).mock() = mock(IntRange.EMPTY)
fun (() -> Long).mock() = mock( 0L)
fun (() -> LongArray).mock() = mock(longArrayOf())
fun (() -> LongRange).mock() = mock(LongRange.EMPTY)
fun (() -> Number).mock() = mock( 0)
fun (() -> Short).mock() = mock(0.toShort())
fun (() -> ShortArray).mock() = mock( shortArrayOf())
fun (() -> String).mock() = mock("")
fun (() -> UByte).mock() = mock(0x0U)
fun (() -> UByteArray).mock() = mock(ubyteArrayOf())
fun (() -> UInt).mock() = mock(0U)
fun (() -> UIntArray).mock() = mock(uintArrayOf())
fun (() -> UIntRange).mock() = mock(UIntRange.EMPTY)
fun (() -> ULong).mock() = mock( 0UL)
fun (() -> ULongArray).mock() = mock(ulongArrayOf())
fun (() -> ULongRange).mock() = mock(ULongRange.EMPTY)
fun (() -> UShort).mock() = mock( 0.toUShort())
fun (() -> UShortArray).mock() = mock(ushortArrayOf())
inline fun <reified R> (() -> Array<R>).mock() = mock(emptyArray())
fun <R> (() -> List<R>).mock() = mock(emptyList())
fun <R> (() -> Set<R>).mock() = mock(emptySet())
fun <K, V> (() -> Map<K, V>).mock() = mock(emptyMap())
fun <R : Any> (() -> R?).mock() = mock(null)

fun <R> (suspend () -> R).asMock() = SuspendMethodMock<VoidParameters.Matchers, VoidParameters.MatchersOrCaptor, VoidParameters.Values, VoidParameters, R>(VoidParameters)

fun <R> (suspend () -> R).mock(defaultAnswer: SuspendedAnswer<VoidParameters.Values, R>) = asMock().also {
    it.on().doAnswer(defaultAnswer)
}

fun <R> (suspend () -> R).mock(defaultValue: R) = asMock().also {
    it.on().doReturn(defaultValue)
}

fun (suspend () -> Boolean).mock() = mock(false)
fun (suspend () -> BooleanArray).mock() = mock(booleanArrayOf())
fun (suspend () -> Byte).mock() = mock(0x0)
fun (suspend () -> ByteArray).mock() = mock(byteArrayOf())
fun (suspend () -> Char).mock() = mock(0.toChar())
fun (suspend () -> CharArray).mock() = mock(charArrayOf())
fun (suspend () -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
fun (suspend () -> Double).mock() = mock(0.0)
fun (suspend () -> DoubleArray).mock() = mock(doubleArrayOf())
fun (suspend () -> Float).mock() = mock(0.0f)
fun (suspend () -> FloatArray).mock() = mock(floatArrayOf())
fun (suspend () -> Int).mock() = mock(0)
fun (suspend () -> IntArray).mock() = mock(intArrayOf())
fun (suspend () -> IntRange).mock() = mock(IntRange.EMPTY)
fun (suspend () -> Long).mock() = mock(0L)
fun (suspend () -> LongArray).mock() = mock(longArrayOf())
fun (suspend () -> LongRange).mock() = mock(LongRange.EMPTY)
fun (suspend () -> Number).mock() = mock(0)
fun (suspend () -> Short).mock() = mock(0.toShort())
fun (suspend () -> ShortArray).mock() = mock(shortArrayOf())
fun (suspend () -> String).mock() = mock("")
fun (suspend () -> UByte).mock() = mock(0x0U)
fun (suspend () -> UByteArray).mock() = mock(ubyteArrayOf())
fun (suspend () -> UInt).mock() = mock(0U)
fun (suspend () -> UIntArray).mock() = mock(uintArrayOf())
fun (suspend () -> UIntRange).mock() = mock(UIntRange.EMPTY)
fun (suspend () -> ULong).mock() = mock(0UL)
fun (suspend () -> ULongArray).mock() = mock(ulongArrayOf())
fun (suspend () -> ULongRange).mock() = mock(ULongRange.EMPTY)
fun (suspend () -> UShort).mock() = mock(0.toUShort())
fun (suspend () -> UShortArray).mock() = mock(ushortArrayOf())
inline fun <reified R> (suspend () -> Array<R>).mock() = mock(emptyArray())
fun <R> (suspend () -> List<R>).mock() = mock(emptyList())
fun <R> (suspend () -> Set<R>).mock() = mock(emptySet())
fun <K, V> (suspend () -> Map<K, V>).mock() = mock(emptyMap())
fun <R : Any> (suspend () -> R?).mock() = mock( null)
