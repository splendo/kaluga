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

package com.splendo.kaluga.links.manager

import kotlinx.serialization.Serializable

@Serializable
enum class MyEnum {
    A, B, C
}

@Serializable
data class DataTypesValues(
    val stringValue: String,
    val intValue: Int,
    val longValue: Long,
    val floatValue: Float,
    val doubleValue: Double,
    val booleanValue: Boolean,
    val enumValue: MyEnum,
    val listValue: List<String>,
    val nullableValue: String? = null
) {
    companion object {
        val validParameters = mapOf(
            "stringValue" to "Test",
            "intValue" to 0,
            "longValue" to 3L,
            "floatValue" to 3.14f,
            "doubleValue" to 3.14,
            "booleanValue" to true,
            "enumValue" to "A",
            "listValue.0" to "zero",
            "listValue.1" to "first",
            "listValue.2" to "second",
            "nullableValue" to null // can also be excluded from values since has default value.
        )
        val expectedValidValues = DataTypesValues(
            "Test",
            0,
            3L,
            3.14f,
            3.14,
            true,
            MyEnum.A,
            listOf("zero", "first", "second"),
            null
        )

        val url = """https://test.io?
                stringValue=Test&
                intValue=0&
                longValue=3&
                floatValue=3.14&
                doubleValue=3.14&
                booleanValue=true&
                enumValue=A&
                listValue.0=zero&
                listValue.1=first&
                listValue.2=second&
                nullableValue""".lines().joinToString("") { it.trim() }
    }
}
