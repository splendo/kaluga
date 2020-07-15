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

package com.splendo.kaluga.base.test.text

import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.base.utils.TimeZone
import com.splendo.kaluga.base.utils.createLocale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@ExperimentalStdlibApi
class StringFormatterTest {

    companion object {
        val locale = createLocale("en", "US")
    }

    @Test
    fun testStringFormat() {
        assertEquals("Test success", "Test %s".format("success", locale = locale))
    }

    @Test
    fun testMultipleStringFormat() {
        assertEquals("Test success", "%2\$s %1\$s".format("success", "Test", locale = locale))
    }

    @Test
    fun testStringFormatAdditionalArgument() {
        assertEquals("Test success", "Test %s".format("success", "fail", locale = locale))
    }

    @Test
    fun testStringFormatNotEnoughArguments() {
        assertFails("Successfully parsed") { "Test %s %s".format("success", locale = locale) }
    }

    @Test
    fun testStringFormatMinWidth() {
        assertEquals("Test    success", "Test %10s".format("success", locale = locale))
    }

    @Test
    fun testStringFormatMinWidthLeftAlign() {
        assertEquals("Test success   ", "Test %-10s".format("success", locale = locale))
    }

    @Test
    fun testStringUpperCasedFormat() {
        assertEquals("Test SUCCESS", "Test %S".format("success", locale = locale))
    }

    @Test
    fun testStringiOSFormat() {
        assertEquals("Test success", "Test %@".format("success", locale = locale))
    }

    @Test
    fun testIntFormat() {
        assertEquals("Test 1 success", "Test %d success".format(1, locale = locale))
    }

    @Test
    fun testMultipleIntFormat() {
        assertEquals("Test 2 1 success", "Test %2\$d %1\$d success".format(1, 2, locale = locale))
    }

    @Test
    fun testIntAndStringFormat() {
        assertEquals("Test 1 success", "Test %d %s".format(1, "success", locale = locale))
    }

    @Test
    fun testIntAndStringFormatOrder() {
        assertEquals("Test 1 success", "Test %2\$d %1\$s".format("success", 1, locale = locale))
    }

    @Test
    fun testFailArgumentsOutOfOrder() {
        assertFails("Successfully parsed") { "Test %d %s".format("success", 1, locale = locale) }
    }

    @Test
    fun testIntFormatMinWidth() {
        assertEquals("Test   1 success", "Test %3d success".format(1, locale = locale))
    }

    @Test
    fun testIntFormatMinWidthLeftAlign() {
        assertEquals("Test 1   success", "Test %-3d success".format(1, locale = locale))
    }

    @Test
    fun testIntFormatMinWidthZeroFilled() {
        assertEquals("Test 001 success", "Test %03d success".format(1, locale = locale))
    }

    @Test
    fun testIntFormatNegative() {
        assertEquals("Test -1 success", "Test %d success".format(-1, locale = locale))
    }

    @Test
    fun testIntFormatNegativeMinWidth() {
        assertEquals("Test  -1 success", "Test %3d success".format(-1, locale = locale))
    }

    @Test
    fun testIntFormatNegativeMinWidthLeftAlign() {
        assertEquals("Test -1  success", "Test %-3d success".format(-1, locale = locale))
    }

    @Test
    fun testIntFormatNegativeMinWidthZeroFilled() {
        assertEquals("Test -01 success", "Test %03d success".format(-1, locale = locale))
    }

    @Test
    fun testIntFormatPositive() {
        assertEquals("Test +1 success", "Test %+d success".format(1, locale = locale))
    }

    @Test
    fun testIntFormatPositiveMinWidth() {
        assertEquals("Test  +1 success", "Test %+3d success".format(1, locale = locale))
    }

    @Test
    fun testIntFormatPositiveMinWidthLeftAlign() {
        assertEquals("Test +1  success", "Test %+-3d success".format(1, locale = locale))
    }

    @Test
    fun testIntFormatPositiveMinWidthZeroFilled() {
        assertEquals("Test +01 success", "Test %+03d success".format(1, locale = locale))
    }

    @Test
    fun testIntFormatLeadingSpace() {
        assertEquals("Test  1 success", "Test % d success".format(1, locale = locale))
    }

    @Test
    fun testIntFormatLeadingSpaceNegative() {
        assertEquals("Test -1 success", "Test % d success".format(-1, locale = locale))
    }

    @Test
    fun testIntFormatWithGrouping() {
        assertEquals("2,000", "%,d".format(2000, locale = locale))
    }

    @Test
    fun testIntFormatWithGroupingMinWidth() {
        assertEquals("   2,000", "%,8d".format(2000, locale = locale))
    }

    @Test
    fun testIntFormatWithGroupingMinWidthLeftAlign() {
        assertEquals("2,000   ", "%,-8d".format(2000, locale = locale))
    }

    @Test
    fun testIntFormatWithParenthesis() {
        assertEquals("(50)", "%(d".format(-50, locale = locale))
    }

    @Test
    fun testIntFormatWithParenthesisPositive() {
        assertEquals("50", "%(d".format(50, locale = locale))
    }

    @Test
    fun testIntFormatWithParenthesisMinWidth() {
        assertEquals("    (50)", "%(8d".format(-50, locale = locale))
    }

    @Test
    fun testIntFormatWithParenthesisMinWidthLeftAlign() {
        assertEquals("(50)    ", "%(-8d".format(-50, locale = locale))
    }

    @Test
    fun testOctalIntFormat() {
        assertEquals("Test 173 success", "Test %o success".format(123, locale = locale))
    }

    @Test
    fun testNegativeOctalIntFormat() {
        assertEquals("Test -173 success", "Test %o success".format(-123, locale = locale))
    }

    @Test
    fun testOctalIntFormatAlternative() {
        assertEquals("Test 0173 success", "Test %#o success".format(123, locale = locale))
    }

    @Test
    fun testHexadecimalIntFormat() {
        assertEquals("Test 7b success", "Test %x success".format(123, locale = locale))
    }

    @Test
    fun testNegativeHexadecimalIntFormat() {
        assertEquals("Test -7b success", "Test %x success".format(-123, locale = locale))
    }

    @Test
    fun testHexadecimalIntFormatAlternative() {
        assertEquals("Test 0x7b success", "Test %#x success".format(123, locale = locale))
    }

    @Test
    fun testHexadecimalUpperIntFormat() {
        assertEquals("Test 7B success", "Test %X success".format(123, locale = locale))
    }

    @Test
    fun testNegativeHexadecimalUpperIntFormat() {
        assertEquals("Test -7B success", "Test %X success".format(-123, locale = locale))
    }

    @Test
    fun testHexadecimalUpperIntFormatAlternative() {
        assertEquals("Test 0X7B success", "Test %#X success".format(123, locale = locale))
    }

    @Test
    fun testFloatFormat() {
        assertEquals("0.004600", "%f".format(0.0046, locale = locale))
    }

    @Test
    fun testFloatFormatWithPrecision() {
        assertEquals("0.005", "%.3f".format(0.0046, locale = locale))
    }

    @Test
    fun testFloatFormatWithPrecisionZero() {
        assertEquals("51", "%.0f".format(50.6, locale = locale))
    }

    @Test
    fun testFloatFormatWithPrecisionZeroAlternative() {
        assertEquals("51.", "%#.0f".format(50.6, locale = locale))
    }

    @Test
    fun testFloatFormatWithMinWidth() {
        assertEquals("  0.50", "%6.2f".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithMinWidthLeftAligned() {
        assertEquals("0.50  ", "%-6.2f".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithSign() {
        assertEquals("+0.50", "%+.2f".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithSignNegative() {
        assertEquals("-0.50", "%+.2f".format(-0.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithLeadingSpace() {
        assertEquals(" 0.50", "% .2f".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithLeadingSpaceNegative() {
        assertEquals("-0.50", "% .2f".format(-0.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithGrouping() {
        assertEquals("1,000.0", "%,.1f".format(1000.0, locale = locale))
    }

    @Test
    fun testFloatFormatWithZeroPadding() {
        assertEquals("0000000.50", "%010.2f".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithZeroPaddingNegative() {
        assertEquals("-000000.50", "%010.2f".format(-0.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithParenthesis() {
        assertEquals("(50.50)", "%(.2f".format(-50.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithParenthesisPositive() {
        assertEquals("50.50", "%(.2f".format(50.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithParenthesisMinWidth() {
        assertEquals("   (50.50)", "%(10.2f".format(-50.5, locale = locale))
    }

    @Test
    fun testFloatFormatWithParenthesisMinWidthLeftAlign() {
        assertEquals("(50.50)   ", "%(-10.2f".format(-50.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientific() {
        assertEquals("4.600000e-03", "%e".format(0.0046, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithPrecision() {
        assertEquals("4.600e-03", "%.3e".format(0.0046, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithPrecisionZero() {
        assertEquals("5e+01", "%.0e".format(50.6, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithPrecisionZeroAlternative() {
        assertEquals("5.e+01", "%#.0e".format(50.6, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithMinWidth() {
        assertEquals("  5.00e-01", "%10.2e".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithMinWidthLeftAligned() {
        assertEquals("5.00e-01  ", "%-10.2e".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithSign() {
        assertEquals("+5.00e-01", "%+.2e".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithSignNegative() {
        assertEquals("-5.00e-01", "%+.2e".format(-0.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithLeadingSpace() {
        assertEquals(" 5.00e-01", "% .2e".format(0.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithLeadingSpaceNegative() {
        assertEquals("-5.00e-01", "% .2e".format(-0.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithZeroPadding() {
        assertEquals("005.00e-01", "%010.2e".format(0.5, locale = locale))
    }

    @Test
    fun tesFloatFormatScientificWithZeroPaddingNegative() {
        assertEquals("-05.00e-01", "%010.2e".format(-0.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithParenthesis() {
        assertEquals("(5.05e+01)", "%(.2e".format(-50.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithParenthesisPositive() {
        assertEquals("5.05e+01", "%(.2e".format(50.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithParenthesisMinWidth() {
        assertEquals("     (5.05e+01)", "%(15.2e".format(-50.5, locale = locale))
    }

    @Test
    fun testFloatFormatScientificWithParenthesisMinWidthLeftAlign() {
        assertEquals("(5.05e+01)     ", "%(-15.2e".format(-50.5, locale = locale))
    }

    @Test
    fun testFloatFormatGeneral() {
        assertEquals("10000.000", "%.3g".format(10000.0, locale = locale))
        assertEquals("1.000e+06", "%.3g".format(1000000.0, locale = locale))
    }

    @Test
    fun testFormatChar() {
        assertEquals("a", "%c".format('a', locale = locale))
    }

    @Test
    fun testFormatCharMinWidth() {
        assertEquals("  a", "%3c".format('a', locale = locale))
    }

    @Test
    fun testFormatCharMinWidthLeftAlign() {
        assertEquals("a  ", "%-3c".format('a', locale = locale))
    }

    @Test
    fun testFormatCharCapitalized() {
        assertEquals("A", "%C".format('a', locale = locale))
    }

    @Test
    fun testFormatPercentageEscaped() {
        assertEquals("%", "%%".format(locale = locale))
    }

    @Test
    fun testFormatNewLine() {
        assertEquals("\n", "%n".format(locale = locale))
    }

    @Test
    fun testFormatDate() {
        val date = Date.now(timeZone = TimeZone.get("America/Los_Angeles")!!, locale = locale).apply {
            year = 2020
            month = 7
            day = 23
            hour = 8
            minute = 45
            second = 20
        }

        val time = "%1\$tA %1\$tB %1\$te, %1\$tY at %1\$tH:%1\$tM:%1\$tS in %1\$tZ".format(date, locale = locale)
        assertEquals("Thursday July 23, 2020 at 08:45:20 in PDT", time)
    }
}
