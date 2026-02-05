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

package com.splendo.kaluga.links.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
enum class MyEnum {
    A,
    B,
    C,
}

@Serializable
data class DataContainer<T>(val value: T)

class LinksDecoderTest {

    private fun <T> checkDecodeDataContainer(valueSerializer: KSerializer<T>, queryValue: String, expected: T) {
        val container = decodeFromMap(
            mapOf(DataContainer<*>::value.name to listOf(queryValue)),
            DataContainer.serializer(valueSerializer),
        )
        assertEquals(expected, container.value)
    }

    @Test
    fun testStringValue() = checkDecodeDataContainer(String.serializer(), "a string", "a string")

    @Test
    fun testCharValue() = checkDecodeDataContainer(Char.serializer(), "a", 'a')

    @Test
    fun testIntValue() = checkDecodeDataContainer(Int.serializer(), "10", 10)

    @Test
    fun testLongValue() = checkDecodeDataContainer(Long.serializer(), "100", 100L)

    @Test
    fun testFloatValue() = checkDecodeDataContainer(Float.serializer(), "3.14", 3.14f)

    @Test
    fun testDoubleValue() = checkDecodeDataContainer(Double.serializer(), "3.14", 3.14)

    @Test
    fun testBooleanValue() = checkDecodeDataContainer(Boolean.serializer(), "true", true)

    @Test
    fun testByteValue() = checkDecodeDataContainer(Byte.serializer(), "123", 123.toByte())

    @Test
    fun testEnumValue() = checkDecodeDataContainer(MyEnum.serializer(), "A", MyEnum.A)

    @Test
    fun testListValue() {
        val container = decodeFromMap(
            mapOf(DataContainer<*>::value.name to listOf("A", "B", "C")),
            DataContainer.serializer(ListSerializer(MyEnum.serializer())),
        )
        assertEquals(listOf(MyEnum.A, MyEnum.B, MyEnum.C), container.value)
    }

    @Test
    fun testOrderOfElementsDoesNotMatterListValue() {
        @Serializable
        data class Data(val name: String, val count: Int)

        @Serializable
        data class DataWithInvertedOrder(val count: Int, val name: String)

        val map = mapOf(
            "name" to listOf("kaluga"),
            "count" to listOf("123"),
        )

        assertEquals(Data("kaluga", 123), decodeFromMap(map, Data.serializer()))
        assertEquals(DataWithInvertedOrder(123, "kaluga"), decodeFromMap(map, DataWithInvertedOrder.serializer()))
    }

    @Test
    fun testDefaultValue() {
        @Serializable
        data class Data(val value: String = "123")

        val data = decodeFromMap(
            mapOf("someOtherField" to listOf("someOtherValue")),
            Data.serializer(),
        )

        assertEquals("123", data.value)
    }
}
