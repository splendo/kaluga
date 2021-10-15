/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.links.manager.DefaultParametersDecoder
import com.splendo.kaluga.links.manager.NameValue
import com.splendo.kaluga.links.manager.decodeFromList
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Serializable
enum class MyEnum {
    A, B, C
}

@Serializable
data class DataTypesValues(
    val stringValue: String,
    val charValue: Char,
    val intValue: Int,
    val longValue: Long,
    val floatValue: Float,
    val doubleValue: Double,
    val booleanValue: Boolean,
    val byteValue: Byte,
    val enumValue: MyEnum,
    val listValue: List<String>,
    val nullableValue: String?
)

@Serializable
data class Adventure(
    val mapKind: String,
    val dangerLevel: Int,
    val companion: String?
)

class LinksDecoderTest {

    companion object {
        private const val byteValue: Byte = 1
        private val queryValues = listOf<NameValue>(
            "stringValue" to "Test", // stringValue
            "charValue" to 'A', // charValue
            "intValue" to 0, // intValue
            "longValue" to 3L, // longValue
            "floatValue" to 3.14f, // floatValue
            "doubleValue" to 3.14, // doubleValue
            "booleanValue" to true, // booleanValue
            "byteValue" to "1", // byteValue
            "enumValue" to  "A", // enumValue
            "listValue" to 3,
            "listValue" to "zero",
            "listValue" to "one",
            "listValue" to "two",
            "nullableValue" to LinksDecoder.NULL_SYMBOL // nullableValue
        )
        private val expectedValue = DataTypesValues(
            "Test",
            'A',
            0,
            3L,
            3.14f,
            3.14,
            true,
            byteValue,
            MyEnum.A,
            listOf("zero", "one", "two"),
            null
        )
    }

    private val mockDefaultParametersDecoder = DefaultParametersDecoder()

    @Test
    fun testDecodeList() {
        val decodedObject = mockDefaultParametersDecoder.decodeFromList(queryValues, DataTypesValues.serializer())

        assertEquals(expectedValue, decodedObject)
    }

    @Test
    fun testDecodeListFailOnWrongOrder() { // it fails because the first parameter or DataTypesValues is a String.
        val list = mutableListOf<NameValue>("randomValue" to 300).plus(queryValues)

        assertFails {
            mockDefaultParametersDecoder.decodeFromList<DataTypesValues>(list)
        }
    }

    @Test
    fun testDecoder() {
        val linksDecoder = LinksDecoder(ArrayDeque(queryValues.map { it.second }))

        linksDecoder.run {
            testDataType<String>(decodeValue())
            testDataType<Char>(decodeValue())
            testDataType<Int>(decodeValue())
            testDataType<Long>(decodeValue())
            testDataType<Float>(decodeValue())
            testDataType<Double>(decodeValue())
            testDataType<Boolean>(decodeValue())
            testDataType<Byte>(decodeValue().toString().toByte())
            testDataType<MyEnum>(decodeValue())
            testDataType<Int>(decodeValue()) // decode listValue.size
            testDataType<String>(decodeValue()) // decode listValue[0]
            testDataType<String>(decodeValue()) // decode listValue[1]
            testDataType<String>(decodeValue()) // decode listValue[2]
            testDataType<String>(decodeValue())
        }

        // It fails because all the fields have been decoded.
        assertFailsWith<NoSuchElementException> {
            testDataType<String>(linksDecoder.decodeValue())
        }
    }

    @Test
    fun test_decode_null() {
        val query = listOf(
            "mapKind" to "treasure",
            "dangerLevel" to 999,
            "companion" to LinksDecoder.NULL_SYMBOL
        )

        val value = mockDefaultParametersDecoder.decodeFromList<Adventure>(query)
        assertEquals(query[0].second, value.mapKind)
        assertEquals(query[1].second, value.dangerLevel)
        assertNull(value.companion)
    }

    private inline fun <reified T> testDataType(value: Any) {
        if (enumValues<MyEnum>().any { it.name == value }) {
            assertTrue { enumValues<MyEnum>().any { it.name == value } }
            return
        }

        assertTrue { value is T }
    }
}
