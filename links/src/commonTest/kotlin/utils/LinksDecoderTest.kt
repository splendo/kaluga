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

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@Serializable
enum class MyEnum {
    A,
    B,
    C,
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
    val nullableValue: String?,
)

class LinksDecoderTest {

    companion object {
        private const val BYTE_VALUE: Byte = 1
        private val queryValues = listOf<Any>(
            // stringValue
            "Test",
            // charValue
            'A',
            // intValue
            0,
            // longValue
            3L,
            // floatValue
            3.14f,
            // doubleValue
            3.14,
            // booleanValue
            true,
            // byteValue
            "1",
            // enumValue
            "A",
            // listValue size
            3,
            // listValue[0]
            "zero",
            // listValue[1]
            "one",
            // listValue[2]
            "two",
            // nullableValue
            "NULL",
        )
        private val expextedValue = DataTypesValues(
            "Test",
            'A',
            0,
            3L,
            3.14f,
            3.14,
            true,
            BYTE_VALUE,
            MyEnum.A,
            listOf("zero", "one", "two"),
            null,
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

    private inline fun <reified T> testDataType(value: Any) {
        if (enumValues<MyEnum>().any { it.name == value }) {
            assertTrue { enumValues<MyEnum>().any { it.name == value } }
            return
        }

        assertTrue { value is T }
    }
}
