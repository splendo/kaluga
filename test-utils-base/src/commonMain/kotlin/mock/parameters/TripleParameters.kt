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
import com.splendo.kaluga.test.base.mock.suspendTripleParametersMock
import com.splendo.kaluga.test.base.mock.tripleParametersMock
import kotlin.js.JsName
import kotlin.jvm.JvmName

class TripleParameters<T0, T1, T2> : ParametersSpec<TripleParameters.Matchers<T0, T1, T2>, TripleParameters.MatchersOrCaptor<T0, T1, T2>, TripleParameters.Values<T0, T1, T2>> {

    /**
     * The [ParametersSpec.Matchers] for three parameters
     * @param first the first parameters [ParameterMatcher]
     * @param second the second parameters [ParameterMatcher]
     * @param third the third parameters [ParameterMatcher]
     */
    data class Matchers<T0, T1, T2>(
        val first: ParameterMatcher<T0>,
        val second: ParameterMatcher<T1>,
        val third: ParameterMatcher<T2>,
    ) : ParametersSpec.Matchers {
        override fun asList() = listOf(first, second, third)
    }

    /**
     * The [ParametersSpec.MatchersOrCaptor] for three parameters
     * @param first the first parameters [ParameterMatcherOrCaptor]
     * @param second the second parameters [ParameterMatcherOrCaptor]
     * @param third the third parameters [ParameterMatcherOrCaptor]
     */
    data class MatchersOrCaptor<T0, T1, T2>(
        val first: ParameterMatcherOrCaptor<T0>,
        val second: ParameterMatcherOrCaptor<T1>,
        val third: ParameterMatcherOrCaptor<T2>,
    ) : ParametersSpec.MatchersOrCaptor<Matchers<T0, T1, T2>> {
        override fun asMatchers(): Matchers<T0, T1, T2> = Matchers(first.asMatcher(), second.asMatcher(), third.asMatcher())
    }

    /**
     * The [ParametersSpec.Values] for three parameters
     * @property first the first parameter
     * @property second the second parameter
     * @property third the third parameter
     */
    data class Values<T0, T1, T2>(val first: T0, val second: T1, val third: T2) : ParametersSpec.Values

    override fun Matchers<T0, T1, T2>.matches(values: Values<T0, T1, T2>): Boolean = first.matches(values.first) &&
        second.matches(values.second) &&
        third.matches(values.third)
    override fun MatchersOrCaptor<T0, T1, T2>.capture(values: Values<T0, T1, T2>) {
        (first as? Captor<T0>)?.capture(values.first)
        (second as? Captor<T1>)?.capture(values.second)
        (third as? Captor<T2>)?.capture(values.third)
    }
}

internal fun <T0, T1, T2, R> ((T0, T1, T2) -> R).asMock() = tripleParametersMock<T0, T1, T2, R>()

fun <T0, T1, T2, R> ((T0, T1, T2) -> R).mockWithDefaultAnswer(
    defaultAnswer: Answer<TripleParameters.Values<T0, T1, T2>, R>,
) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>()).doAnswer(defaultAnswer)
}
fun <T0, T1, T2, R> ((T0, T1, T2) -> R).mockWithDefaultValue(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1, T2> ((T0, T1, T2) -> Boolean).mock() = mockWithDefaultValue(false)

@JvmName("mockBooleanArray")
fun <T0, T1, T2> ((T0, T1, T2) -> BooleanArray).mock() = mockWithDefaultValue(booleanArrayOf())

@JvmName("mockByte")
fun <T0, T1, T2> ((T0, T1, T2) -> Byte).mock() = mockWithDefaultValue(0x0)

@JvmName("mockByteArray")
fun <T0, T1, T2> ((T0, T1, T2) -> ByteArray).mock() = mockWithDefaultValue(byteArrayOf())

@JvmName("mockChar")
fun <T0, T1, T2> ((T0, T1, T2) -> Char).mock() = mockWithDefaultValue(0.toChar())

@JvmName("mockCharArray")
fun <T0, T1, T2> ((T0, T1, T2) -> CharArray).mock() = mockWithDefaultValue(charArrayOf())

@JvmName("mockCharRange")
fun <T0, T1, T2> ((T0, T1, T2) -> CharRange).mock() = mockWithDefaultValue(CharRange.EMPTY)

@JvmName("mockDouble")
fun <T0, T1, T2> ((T0, T1, T2) -> Double).mock() = mockWithDefaultValue(0.0)

@JvmName("mockDoubleArray")
fun <T0, T1, T2> ((T0, T1, T2) -> DoubleArray).mock() = mockWithDefaultValue(doubleArrayOf())

@JvmName("mockFloat")
fun <T0, T1, T2> ((T0, T1, T2) -> Float).mock() = mockWithDefaultValue(0.0f)

@JvmName("mockFloatArray")
fun <T0, T1, T2> ((T0, T1, T2) -> FloatArray).mock() = mockWithDefaultValue(floatArrayOf())

@JvmName("mockInt")
fun <T0, T1, T2> ((T0, T1, T2) -> Int).mock() = mockWithDefaultValue(0)

@JvmName("mockIntArray")
fun <T0, T1, T2> ((T0, T1, T2) -> IntArray).mock() = mockWithDefaultValue(intArrayOf())

@JvmName("mockIntRange")
fun <T0, T1, T2> ((T0, T1, T2) -> IntRange).mock() = mockWithDefaultValue(IntRange.EMPTY)

@JvmName("mockLong")
fun <T0, T1, T2> ((T0, T1, T2) -> Long).mock() = mockWithDefaultValue(0L)

@JvmName("mockLongArray")
fun <T0, T1, T2> ((T0, T1, T2) -> LongArray).mock() = mockWithDefaultValue(longArrayOf())

@JvmName("mockLongRange")
fun <T0, T1, T2> ((T0, T1, T2) -> LongRange).mock() = mockWithDefaultValue(LongRange.EMPTY)

@JvmName("mockNumber")
fun <T0, T1, T2> ((T0, T1, T2) -> Number).mock() = mockWithDefaultValue(0)

@JvmName("mockShort")
fun <T0, T1, T2> ((T0, T1, T2) -> Short).mock() = mockWithDefaultValue(0.toShort())

@JvmName("mockShortArray")
fun <T0, T1, T2> ((T0, T1, T2) -> ShortArray).mock() = mockWithDefaultValue(shortArrayOf())

@JvmName("mockString")
fun <T0, T1, T2> ((T0, T1, T2) -> String).mock() = mockWithDefaultValue("")

@JvmName("mockUByte")
fun <T0, T1, T2> ((T0, T1, T2) -> UByte).mock() = mockWithDefaultValue(0x0U)

@JvmName("mockUByteArray")
fun <T0, T1, T2> ((T0, T1, T2) -> UByteArray).mock() = mockWithDefaultValue(ubyteArrayOf())

@JvmName("mockUInt")
fun <T0, T1, T2> ((T0, T1, T2) -> UInt).mock() = mockWithDefaultValue(0U)

@JvmName("mockUIntArray")
fun <T0, T1, T2> ((T0, T1, T2) -> UIntArray).mock() = mockWithDefaultValue(uintArrayOf())

@JvmName("mockUIntRange")
fun <T0, T1, T2> ((T0, T1, T2) -> UIntRange).mock() = mockWithDefaultValue(UIntRange.EMPTY)

@JvmName("mockULong")
fun <T0, T1, T2> ((T0, T1, T2) -> ULong).mock() = mockWithDefaultValue(0UL)

@JvmName("mockULongArray")
fun <T0, T1, T2> ((T0, T1, T2) -> ULongArray).mock() = mockWithDefaultValue(ulongArrayOf())

@JvmName("mockULongRange")
fun <T0, T1, T2> ((T0, T1, T2) -> ULongRange).mock() = mockWithDefaultValue(ULongRange.EMPTY)

@JvmName("mockUShort")
fun <T0, T1, T2> ((T0, T1, T2) -> UShort).mock() = mockWithDefaultValue(0.toUShort())

@JvmName("mockUShortArray")
fun <T0, T1, T2> ((T0, T1, T2) -> UShortArray).mock() = mockWithDefaultValue(ushortArrayOf())

@JvmName("mockUnit")
fun <T0, T1, T2> ((T0, T1, T2) -> Unit).mock() = mockWithDefaultValue(Unit)

@JvmName("mockArray")
inline fun <T0, T1, T2, reified R> ((T0, T1, T2) -> Array<R>).mock() = mockWithDefaultValue(emptyArray())

@JvmName("mockList")
fun <T0, T1, T2, R> ((T0, T1, T2) -> List<R>).mock() = mockWithDefaultValue(emptyList())

@JvmName("mockSet")
fun <T0, T1, T2, R> ((T0, T1, T2) -> Set<R>).mock() = mockWithDefaultValue(emptySet())

@JvmName("mockMap")
fun <T0, T1, T2, K, V> ((T0, T1, T2) -> Map<K, V>).mock() = mockWithDefaultValue(emptyMap())

@JvmName("mockNullable")
@JsName("mockTripleNullable")
fun <T0, T1, T2, R : Any> ((T0, T1, T2) -> R?).mock() = mockWithDefaultValue(null)

@JvmName("mockNonNullable")
@JsName("mockTripleNonNullable")
fun <T0, T1, T2, R : Any> ((T0, T1, T2) -> R).mock() = asMock()

internal fun <T0, T1, T2, R> (suspend (T0, T1, T2) -> R).asSuspendedMock() = suspendTripleParametersMock<T0, T1, T2, R>()

fun <T0, T1, T2, R> (suspend (T0, T1, T2) -> R).mockWithDefaultAnswer(
    defaultAnswer: SuspendedAnswer<TripleParameters.Values<T0, T1, T2>, R>,
) = asSuspendedMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>()).doAnswer(defaultAnswer)
}
fun <T0, T1, T2, R> (suspend (T0, T1, T2) -> R).mockWithDefaultValue(defaultValue: R) = asSuspendedMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Boolean).mock() = mockWithDefaultValue(false)

@JvmName("mockBooleanArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> BooleanArray).mock() = mockWithDefaultValue(booleanArrayOf())

@JvmName("mockByte")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Byte).mock() = mockWithDefaultValue(0x0)

@JvmName("mockByteArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> ByteArray).mock() = mockWithDefaultValue(byteArrayOf())

@JvmName("mockChar")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Char).mock() = mockWithDefaultValue(0.toChar())

@JvmName("mockCharArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> CharArray).mock() = mockWithDefaultValue(charArrayOf())

@JvmName("mockCharRange")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> CharRange).mock() = mockWithDefaultValue(CharRange.EMPTY)

@JvmName("mockDouble")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Double).mock() = mockWithDefaultValue(0.0)

@JvmName("mockDoubleArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> DoubleArray).mock() = mockWithDefaultValue(doubleArrayOf())

@JvmName("mockFloat")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Float).mock() = mockWithDefaultValue(0.0f)

@JvmName("mockFloatArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> FloatArray).mock() = mockWithDefaultValue(floatArrayOf())

@JvmName("mockInt")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Int).mock() = mockWithDefaultValue(0)

@JvmName("mockIntArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> IntArray).mock() = mockWithDefaultValue(intArrayOf())

@JvmName("mockIntRange")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> IntRange).mock() = mockWithDefaultValue(IntRange.EMPTY)

@JvmName("mockLong")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Long).mock() = mockWithDefaultValue(0L)

@JvmName("mockLongArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> LongArray).mock() = mockWithDefaultValue(longArrayOf())

@JvmName("mockLongRange")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> LongRange).mock() = mockWithDefaultValue(LongRange.EMPTY)

@JvmName("mockNumber")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Number).mock() = mockWithDefaultValue(0)

@JvmName("mockShort")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Short).mock() = mockWithDefaultValue(0.toShort())

@JvmName("mockShortArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> ShortArray).mock() = mockWithDefaultValue(shortArrayOf())

@JvmName("mockString")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> String).mock() = mockWithDefaultValue("")

@JvmName("mockUByte")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> UByte).mock() = mockWithDefaultValue(0x0U)

@JvmName("mockUByteArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> UByteArray).mock() = mockWithDefaultValue(ubyteArrayOf())

@JvmName("mockUInt")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> UInt).mock() = mockWithDefaultValue(0U)

@JvmName("mockUIntArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> UIntArray).mock() = mockWithDefaultValue(uintArrayOf())

@JvmName("mockUIntRange")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> UIntRange).mock() = mockWithDefaultValue(UIntRange.EMPTY)

@JvmName("mockULong")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> ULong).mock() = mockWithDefaultValue(0UL)

@JvmName("mockULongArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> ULongArray).mock() = mockWithDefaultValue(ulongArrayOf())

@JvmName("mockULongRange")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> ULongRange).mock() = mockWithDefaultValue(ULongRange.EMPTY)

@JvmName("mockUShort")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> UShort).mock() = mockWithDefaultValue(0.toUShort())

@JvmName("mockUShortArray")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> UShortArray).mock() = mockWithDefaultValue(ushortArrayOf())

@JvmName("mockUnit")
fun <T0, T1, T2> (suspend (T0, T1, T2) -> Unit).mock() = mockWithDefaultValue(Unit)

@JvmName("mockArray")
inline fun <T0, T1, T2, reified R> (suspend (T0, T1, T2) -> Array<R>).mock() = mockWithDefaultValue(emptyArray())

@JvmName("mockList")
fun <T0, T1, T2, R> (suspend (T0, T1, T2) -> List<R>).mock() = mockWithDefaultValue(emptyList())

@JvmName("mockSet")
fun <T0, T1, T2, R> (suspend (T0, T1, T2) -> Set<R>).mock() = mockWithDefaultValue(emptySet())

@JvmName("mockMap")
fun <T0, T1, T2, K, V> (suspend (T0, T1, T2) -> Map<K, V>).mock() = mockWithDefaultValue(emptyMap())

@JvmName("mockNullable")
@JsName("mockTripleNullableSuspended")
fun <T0, T1, T2, R : Any> (suspend (T0, T1, T2) -> R?).mock() = mockWithDefaultValue(null)

@JvmName("mockNonNullable")
@JsName("mockTripleNonNullableSuspended")
fun <T0, T1, T2, R : Any> (suspend (T0, T1, T2) -> R).mock() = asSuspendedMock()
