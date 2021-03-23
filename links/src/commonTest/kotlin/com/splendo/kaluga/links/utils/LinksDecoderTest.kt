/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.links.manager.LinksHandler
import com.splendo.kaluga.links.manager.PlatformLinksHandler
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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

class LinksDecoderTest {

    private val linksHandler: LinksHandler = PlatformLinksHandler()
    companion object {
        private const val byteValue: Byte = 1
        private val queryValues = listOf<Any>(
            "Test", // stringValue
            'A', // charValue
            0, // intValue
            3L, // longValue
            3.14f, // floatValue
            3.14, // doubleValue
            true, // booleanValue
            "1", // byteValue
            "A", // enumValue
            3, // listValue size
            "zero", // listValue[0]
            "one", // listValue[1]
            "two", // listValue[2]
            "NULL" // nullableValue
        )
        private val expextedValue = DataTypesValues(
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

    @Test
    fun testDecodeList() {
        val decodedObject = decodeFromList(queryValues, DataTypesValues.serializer())

        assertEquals(expextedValue, decodedObject)
    }

    @Test
    fun testDecodeListFailOnWrongOrder() { // it fails because the first parameter or DataTypesValues is a String.
        val list = mutableListOf<Any>(300).plus(queryValues)

        assertFailsWith<ClassCastException> {
            decodeFromList(list, DataTypesValues.serializer())
        }
    }

    @Test
    fun testDecoder() {
        val linksDecoder = LinksDecoder(ArrayDeque(queryValues))

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
    fun testExtractValuesAsList_EmptyQuery() {
        val url = ""
        val result = linksHandler.extractQueryAsList(url)
        assertEquals(emptyList(), result)
    }

    @Test
    fun testExtractValuesAsList_EmptyValue() {
        val url = "http://valid-url.com?country_code=&contry_name="
        val result = linksHandler.extractQueryAsList(url)
        assertEquals(listOf("", ""), result)
    }

    @Test
    fun testExtractValuesAsList_Success() {
        val url = "http://valid.com?country_code=NL&country_name=The%20Netherlands"
        val expectedResult = listOf("NL", "The Netherlands")
        val result = linksHandler.extractQueryAsList(url)
        assertEquals(expectedResult, result)
    }

    private inline fun <reified T> testDataType(value: Any) {
        if (enumValues<MyEnum>().any { it.name == value }) {
            assertTrue { enumValues<MyEnum>().any { it.name == value } }
            return
        }

        assertTrue { value is T }
    }
}
