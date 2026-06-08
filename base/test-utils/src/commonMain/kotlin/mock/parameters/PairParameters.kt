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

package com.splendo.kaluga.test.base.mock.parameters

import com.splendo.kaluga.test.base.mock.answer.Answer
import com.splendo.kaluga.test.base.mock.answer.SuspendedAnswer
import com.splendo.kaluga.test.base.mock.matcher.Captor
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcherOrCaptor
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.pairParametersMock
import com.splendo.kaluga.test.base.mock.suspendPairParametersMock
import kotlin.js.JsName
import kotlin.jvm.JvmName

/**
 * The [ParametersSpec] for a two parameters
 */
class PairParameters<T0, T1> : ParametersSpec<PairParameters.Matchers<T0, T1>, PairParameters.MatchersOrCaptor<T0, T1>, PairParameters.Values<T0, T1>> {

    /**
     * The [ParametersSpec.Matchers] for two parameters
     * @param T0 the type of the first [ParameterMatcher]
     * @param T1 the type of the second [ParameterMatcher]
     * @property first the first parameters [ParameterMatcher]
     * @property second the second parameters [ParameterMatcher]
     */
    data class Matchers<T0, T1>(val first: ParameterMatcher<T0>, val second: ParameterMatcher<T1>) : ParametersSpec.Matchers {
        override fun asList() = listOf(first, second)
    }

    /**
     * The [ParametersSpec.MatchersOrCaptor] for two parameters
     * @param T0 the type of the first [ParameterMatcherOrCaptor]
     * @param T1 the type of the second [ParameterMatcherOrCaptor]
     * @property first the first parameters [ParameterMatcherOrCaptor]
     * @property second the second parameters [ParameterMatcherOrCaptor]
     */
    data class MatchersOrCaptor<T0, T1>(val first: ParameterMatcherOrCaptor<T0>, val second: ParameterMatcherOrCaptor<T1>) :
        ParametersSpec.MatchersOrCaptor<Matchers<T0, T1>> {
        override fun asMatchers(): Matchers<T0, T1> = Matchers(first.asMatcher(), second.asMatcher())
    }

    /**
     * The [ParametersSpec.Values] for two parameters
     * @param T0 the type of the first value
     * @param T1 the type of the second value
     * @property first the first parameter
     * @property second the second parameter
     */
    data class Values<T0, T1>(val first: T0, val second: T1) : ParametersSpec.Values

    override fun Matchers<T0, T1>.matches(values: Values<T0, T1>): Boolean = first.matches(values.first) && second.matches(values.second)
    override fun MatchersOrCaptor<T0, T1>.capture(values: Values<T0, T1>) {
        (first as? Captor<T0>)?.capture(values.first)
        (second as? Captor<T1>)?.capture(values.second)
    }
}

internal fun <T0, T1, R> ((T0, T1) -> R).asMock() = pairParametersMock<T0, T1, R>()

fun <T0, T1, R> ((T0, T1) -> R).mockWithDefaultAnswer(defaultAnswer: Answer<PairParameters.Values<T0, T1>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doAnswer(defaultAnswer)
}
fun <T0, T1, R> ((T0, T1) -> R).mockWithDefaultValue(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1> ((T0, T1) -> Boolean).mock() = mockWithDefaultValue(false)

@JvmName("mockBooleanArray")
fun <T0, T1> ((T0, T1) -> BooleanArray).mock() = mockWithDefaultValue(booleanArrayOf())

@JvmName("mockByte")
fun <T0, T1> ((T0, T1) -> Byte).mock() = mockWithDefaultValue(0x0)

@JvmName("mockByteArray")
fun <T0, T1> ((T0, T1) -> ByteArray).mock() = mockWithDefaultValue(byteArrayOf())

@JvmName("mockChar")
fun <T0, T1> ((T0, T1) -> Char).mock() = mockWithDefaultValue(0.toChar())

@JvmName("mockCharArray")
fun <T0, T1> ((T0, T1) -> CharArray).mock() = mockWithDefaultValue(charArrayOf())

@JvmName("mockCharRange")
fun <T0, T1> ((T0, T1) -> CharRange).mock() = mockWithDefaultValue(CharRange.EMPTY)

@JvmName("mockDouble")
fun <T0, T1> ((T0, T1) -> Double).mock() = mockWithDefaultValue(0.0)

@JvmName("mockDoubleArray")
fun <T0, T1> ((T0, T1) -> DoubleArray).mock() = mockWithDefaultValue(doubleArrayOf())

@JvmName("mockFloat")
fun <T0, T1> ((T0, T1) -> Float).mock() = mockWithDefaultValue(0.0f)

@JvmName("mockFloatArray")
fun <T0, T1> ((T0, T1) -> FloatArray).mock() = mockWithDefaultValue(floatArrayOf())

@JvmName("mockInt")
fun <T0, T1> ((T0, T1) -> Int).mock() = mockWithDefaultValue(0)

@JvmName("mockIntArray")
fun <T0, T1> ((T0, T1) -> IntArray).mock() = mockWithDefaultValue(intArrayOf())

@JvmName("mockIntRange")
fun <T0, T1> ((T0, T1) -> IntRange).mock() = mockWithDefaultValue(IntRange.EMPTY)

@JvmName("mockLong")
fun <T0, T1> ((T0, T1) -> Long).mock() = mockWithDefaultValue(0L)

@JvmName("mockLongArray")
fun <T0, T1> ((T0, T1) -> LongArray).mock() = mockWithDefaultValue(longArrayOf())

@JvmName("mockLongRange")
fun <T0, T1> ((T0, T1) -> LongRange).mock() = mockWithDefaultValue(LongRange.EMPTY)

@JvmName("mockNumber")
fun <T0, T1> ((T0, T1) -> Number).mock() = mockWithDefaultValue(0)

@JvmName("mockShort")
fun <T0, T1> ((T0, T1) -> Short).mock() = mockWithDefaultValue(0.toShort())

@JvmName("mockShortArray")
fun <T0, T1> ((T0, T1) -> ShortArray).mock() = mockWithDefaultValue(shortArrayOf())

@JvmName("mockString")
fun <T0, T1> ((T0, T1) -> String).mock() = mockWithDefaultValue("")

@JvmName("mockUByte")
fun <T0, T1> ((T0, T1) -> UByte).mock() = mockWithDefaultValue(0x0U)

@JvmName("mockUByteArray")
fun <T0, T1> ((T0, T1) -> UByteArray).mock() = mockWithDefaultValue(ubyteArrayOf())

@JvmName("mockUInt")
fun <T0, T1> ((T0, T1) -> UInt).mock() = mockWithDefaultValue(0U)

@JvmName("mockUIntArray")
fun <T0, T1> ((T0, T1) -> UIntArray).mock() = mockWithDefaultValue(uintArrayOf())

@JvmName("mockUIntRange")
fun <T0, T1> ((T0, T1) -> UIntRange).mock() = mockWithDefaultValue(UIntRange.EMPTY)

@JvmName("mockULong")
fun <T0, T1> ((T0, T1) -> ULong).mock() = mockWithDefaultValue(0UL)

@JvmName("mockULongArray")
fun <T0, T1> ((T0, T1) -> ULongArray).mock() = mockWithDefaultValue(ulongArrayOf())

@JvmName("mockULongRange")
fun <T0, T1> ((T0, T1) -> ULongRange).mock() = mockWithDefaultValue(ULongRange.EMPTY)

@JvmName("mockUShort")
fun <T0, T1> ((T0, T1) -> UShort).mock() = mockWithDefaultValue(0.toUShort())

@JvmName("mockUShortArray")
fun <T0, T1> ((T0, T1) -> UShortArray).mock() = mockWithDefaultValue(ushortArrayOf())

@JvmName("mockUnit")
fun <T0, T1> ((T0, T1) -> Unit).mock() = mockWithDefaultValue(Unit)

@JvmName("mockArray")
inline fun <T0, T1, reified R> ((T0, T1) -> Array<R>).mock() = mockWithDefaultValue(emptyArray())

@JvmName("mockList")
fun <T0, T1, R> ((T0, T1) -> List<R>).mock() = mockWithDefaultValue(emptyList())

@JvmName("mockSet")
fun <T0, T1, R> ((T0, T1) -> Set<R>).mock() = mockWithDefaultValue(emptySet())

@JvmName("mockMap")
fun <T0, T1, K, V> ((T0, T1) -> Map<K, V>).mock() = mockWithDefaultValue(emptyMap())

@JvmName("mockNullable")
@JsName("mockPairNullable")
fun <T0, T1, R : Any> ((T0, T1) -> R?).mock() = mockWithDefaultValue(null)

@JvmName("mockNonNullable")
@JsName("mockPairNonNullable")
fun <T0, T1, R : Any> ((T0, T1) -> R).mock() = asMock()

internal fun <T0, T1, R> (suspend (T0, T1) -> R).asSuspendedMock() = suspendPairParametersMock<T0, T1, R>()

fun <T0, T1, R> (suspend (T0, T1) -> R).mockWithDefaultAnswer(defaultAnswer: SuspendedAnswer<PairParameters.Values<T0, T1>, R>) = asSuspendedMock()
    .also {
        it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doAnswer(defaultAnswer)
    }
fun <T0, T1, R> (suspend (T0, T1) -> R).mockWithDefaultValue(defaultValue: R) = asSuspendedMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1> (suspend (T0, T1) -> Boolean).mock() = mockWithDefaultValue(false)

@JvmName("mockBooleanArray")
fun <T0, T1> (suspend (T0, T1) -> BooleanArray).mock() = mockWithDefaultValue(booleanArrayOf())

@JvmName("mockByte")
fun <T0, T1> (suspend (T0, T1) -> Byte).mock() = mockWithDefaultValue(0x0)

@JvmName("mockByteArray")
fun <T0, T1> (suspend (T0, T1) -> ByteArray).mock() = mockWithDefaultValue(byteArrayOf())

@JvmName("mockChar")
fun <T0, T1> (suspend (T0, T1) -> Char).mock() = mockWithDefaultValue(0.toChar())

@JvmName("mockCharArray")
fun <T0, T1> (suspend (T0, T1) -> CharArray).mock() = mockWithDefaultValue(charArrayOf())

@JvmName("mockCharRange")
fun <T0, T1> (suspend (T0, T1) -> CharRange).mock() = mockWithDefaultValue(CharRange.EMPTY)

@JvmName("mockDouble")
fun <T0, T1> (suspend (T0, T1) -> Double).mock() = mockWithDefaultValue(0.0)

@JvmName("mockDoubleArray")
fun <T0, T1> (suspend (T0, T1) -> DoubleArray).mock() = mockWithDefaultValue(doubleArrayOf())

@JvmName("mockFloat")
fun <T0, T1> (suspend (T0, T1) -> Float).mock() = mockWithDefaultValue(0.0f)

@JvmName("mockFloatArray")
fun <T0, T1> (suspend (T0, T1) -> FloatArray).mock() = mockWithDefaultValue(floatArrayOf())

@JvmName("mockInt")
fun <T0, T1> (suspend (T0, T1) -> Int).mock() = mockWithDefaultValue(0)

@JvmName("mockIntArray")
fun <T0, T1> (suspend (T0, T1) -> IntArray).mock() = mockWithDefaultValue(intArrayOf())

@JvmName("mockIntRange")
fun <T0, T1> (suspend (T0, T1) -> IntRange).mock() = mockWithDefaultValue(IntRange.EMPTY)

@JvmName("mockLong")
fun <T0, T1> (suspend (T0, T1) -> Long).mock() = mockWithDefaultValue(0L)

@JvmName("mockLongArray")
fun <T0, T1> (suspend (T0, T1) -> LongArray).mock() = mockWithDefaultValue(longArrayOf())

@JvmName("mockLongRange")
fun <T0, T1> (suspend (T0, T1) -> LongRange).mock() = mockWithDefaultValue(LongRange.EMPTY)

@JvmName("mockNumber")
fun <T0, T1> (suspend (T0, T1) -> Number).mock() = mockWithDefaultValue(0)

@JvmName("mockShort")
fun <T0, T1> (suspend (T0, T1) -> Short).mock() = mockWithDefaultValue(0.toShort())

@JvmName("mockShortArray")
fun <T0, T1> (suspend (T0, T1) -> ShortArray).mock() = mockWithDefaultValue(shortArrayOf())

@JvmName("mockString")
fun <T0, T1> (suspend (T0, T1) -> String).mock() = mockWithDefaultValue("")

@JvmName("mockUByte")
fun <T0, T1> (suspend (T0, T1) -> UByte).mock() = mockWithDefaultValue(0x0U)

@JvmName("mockUByteArray")
fun <T0, T1> (suspend (T0, T1) -> UByteArray).mock() = mockWithDefaultValue(ubyteArrayOf())

@JvmName("mockUInt")
fun <T0, T1> (suspend (T0, T1) -> UInt).mock() = mockWithDefaultValue(0U)

@JvmName("mockUIntArray")
fun <T0, T1> (suspend (T0, T1) -> UIntArray).mock() = mockWithDefaultValue(uintArrayOf())

@JvmName("mockUIntRange")
fun <T0, T1> (suspend (T0, T1) -> UIntRange).mock() = mockWithDefaultValue(UIntRange.EMPTY)

@JvmName("mockULong")
fun <T0, T1> (suspend (T0, T1) -> ULong).mock() = mockWithDefaultValue(0UL)

@JvmName("mockULongArray")
fun <T0, T1> (suspend (T0, T1) -> ULongArray).mock() = mockWithDefaultValue(ulongArrayOf())

@JvmName("mockULongRange")
fun <T0, T1> (suspend (T0, T1) -> ULongRange).mock() = mockWithDefaultValue(ULongRange.EMPTY)

@JvmName("mockUShort")
fun <T0, T1> (suspend (T0, T1) -> UShort).mock() = mockWithDefaultValue(0.toUShort())

@JvmName("mockUShortArray")
fun <T0, T1> (suspend (T0, T1) -> UShortArray).mock() = mockWithDefaultValue(ushortArrayOf())

@JvmName("mockUnit")
fun <T0, T1> (suspend (T0, T1) -> Unit).mock() = mockWithDefaultValue(Unit)

@JvmName("mockArray")
inline fun <T0, T1, reified R> (suspend (T0, T1) -> Array<R>).mock() = mockWithDefaultValue(emptyArray())

@JvmName("mockList")
fun <T0, T1, R> (suspend (T0, T1) -> List<R>).mock() = mockWithDefaultValue(emptyList())

@JvmName("mockSet")
fun <T0, T1, R> (suspend (T0, T1) -> Set<R>).mock() = mockWithDefaultValue(emptySet())

@JvmName("mockMap")
fun <T0, T1, K, V> (suspend (T0, T1) -> Map<K, V>).mock() = mockWithDefaultValue(emptyMap())

@JvmName("mockNullable")
@JsName("mockPairNullableSuspended")
fun <T0, T1, R : Any> (suspend (T0, T1) -> R?).mock() = mockWithDefaultValue(null)

@JvmName("mockNonNullable")
@JsName("mockPairNonNullableSuspended")
fun <T0, T1, R : Any> (suspend (T0, T1) -> R).mock() = asSuspendedMock()
