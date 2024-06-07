/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

interface MockableTestMethods {
    fun methodWithoutParamsAndReturnType()
    fun methodWithoutParamsButWithReturnType(): String
    fun methodWithParamsAndReturnType(string: String): String
    fun methodWithMultipleParamsAndReturnType(first: Int, second: String, third: String?, fourth: Int?): String
    suspend fun suspendMethodWithParamsAndReturnType(string: String): String
}

class MockableTestMethodsImpl : MockableTestMethods {

    override fun methodWithoutParamsAndReturnType() {
        throw NotImplementedError()
    }

    override fun methodWithoutParamsButWithReturnType(): String {
        throw NotImplementedError()
    }

    override fun methodWithParamsAndReturnType(string: String): String {
        throw NotImplementedError()
    }

    override fun methodWithMultipleParamsAndReturnType(first: Int, second: String, third: String?, fourth: Int?): String {
        throw NotImplementedError()
    }

    override suspend fun suspendMethodWithParamsAndReturnType(string: String): String {
        throw NotImplementedError()
    }
}

const val RESULT_A = "Result A"
const val RESULT_B = "Result B"
const val RESULT_NOT_A = "Result Not A"
const val RESULT_NOT_A_OR_B = "Result A nor B"

const val PARAM_A = "A"
const val PARAM_B = "B"

const val PARAM_A1 = 1
const val PARAM_A2 = "A"
const val PARAM_A3 = "Something"
const val PARAM_A4 = 10
const val PARAM_B1 = 2

const val RESULT_1 = "Result 1"
const val RESULT_2 = "Result 2"

const val RESULT = "Result"

class TestException : Exception()
