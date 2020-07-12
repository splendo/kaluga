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

package com.splendo.kaluga.resources.strings

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@ExperimentalStdlibApi
class StringFormatterTest {

    @Test
    fun testStringFormat() {
        assertEquals("Test success", "Test %s".format("success"))
    }

    @Test
    fun testMultipleStringFormat() {
        assertEquals("Test success", "%2\$s %1\$s".format("success", "Test"))
    }

    @Test
    fun testStringFormatAdditionalArgument() {
        assertEquals("Test success", "Test %s".format("success", "fail"))
    }

    @Test
    fun testStringFormatNotEnoughArguments() {
        assertFails("Successfully parsed") { "Test %s %s".format("success") }
    }
    
    @Test
    fun testStringFormatMinWidth() {
        assertEquals("Test    success", "Test %10s".format("success"))
    }

    @Test
    fun testStringFormatMinWidthLeftAlign() {
        assertEquals("Test success   ", "Test %-10s".format("success"))
    }

    @Test
    fun testStringUpperCasedFormat() {
        assertEquals("Test SUCCESS", "Test %S".format("success"))
    }

    @Test
    fun testIntFormat() {
        assertEquals("Test 1 success", "Test %d success".format(1))
    }

    @Test
    fun testMultipleIntFormat() {
        assertEquals("Test 2 1 success", "Test %2\$d %1\$d success".format(1, 2))
    }

    @Test
    fun testIntAndStringFormat() {
        assertEquals("Test 1 success", "Test %d %s".format(1, "success"))
    }

    @Test
    fun testIntAndStringFormatOrder() {
        assertEquals("Test 1 success", "Test %2\$d %1\$s".format("success", 1))
    }

    @Test
    fun testFailArgumentsOutOfOrder() {
        assertFails("Successfully parsed") { "Test %d %s".format("success", 1) }
    }

    @Test
    fun testIntFormatMinWidth() {
        assertEquals("Test   1 success", "Test %3d success".format(1))
    }

    @Test
    fun testIntFormatMinWidthLeftAlign() {
        assertEquals("Test 1   success", "Test %-3d success".format(1))
    }

    @Test
    fun testIntFormatMinWidthZeroFilled() {
        assertEquals("Test 001 success", "Test %03d success".format(1))
    }

    @Test
    fun testIntFormatNegative() {
        assertEquals("Test -1 success", "Test %d success".format(-1))
    }

    @Test
    fun testIntFormatNegativeMinWidth() {
        assertEquals("Test  -1 success", "Test %3d success".format(-1))
    }

    @Test
    fun testIntFormatNegativeMinWidthLeftAlign() {
        assertEquals("Test -1  success", "Test %-3d success".format(-1))
    }

    @Test
    fun testIntFormatNegativeMinWidthZeroFilled() {
        assertEquals("Test -01 success", "Test %03d success".format(-1))
    }

    @Test
    fun testIntFormatPositive() {
        assertEquals("Test +1 success", "Test %+d success".format(1))
    }

    @Test
    fun testIntFormatPositiveMinWidth() {
        assertEquals("Test  +1 success", "Test %+3d success".format(1))
    }

    @Test
    fun testIntFormatPositiveMinWidthLeftAlign() {
        assertEquals("Test +1  success", "Test %+-3d success".format(1))
    }

    @Test
    fun testIntFormatPositiveMinWidthZeroFilled() {
        assertEquals("Test +01 success", "Test %+03d success".format(1))
    }

    @Test
    fun testOctalIntFormat() {
        assertEquals("Test 173 success", "Test %o success".format(123))
    }

    @Test
    fun testNegativeOctalIntFormat() {
        assertEquals("Test -173 success", "Test %o success".format(-123))
    }

    @Test
    fun testHexadecimalIntFormat() {
        assertEquals("Test 7b success", "Test %x success".format(123))
    }

    @Test
    fun testNegativeHexadecimalIntFormat() {
        assertEquals("Test -7b success", "Test %x success".format(-123))
    }

    @Test
    fun testHexadecimalUpperIntFormat() {
        assertEquals("Test 7B success", "Test %X success".format(123))
    }

    @Test
    fun testNegativeHexadecimalUpperIntFormat() {
        assertEquals("Test -7B success", "Test %X success".format(-123))
    }
}
