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

package com.splendo.kaluga.base.text

import com.splendo.kaluga.base.utils.KalugaLocale.Companion.createLocale
import com.splendo.kaluga.test.base.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NumberFormatterTest : BaseTest() {

    companion object {
        private val UnitedStates = createLocale("en", "US")
        private val Netherlands = createLocale("nl", "NL")
        private val Turkey = createLocale("tr", "TR")
    }

    @Test
    fun testParseInt() {
        val formatters = createFormatters(NumberFormatStyle.Integer())
        assertEquals(1L, formatters.usFormatter.parse("1"))
        assertEquals(1L, formatters.nlFormatter.parse("1"))
        assertEquals(1000L, formatters.usFormatter.parse("1,000"))
        assertEquals(1000L, formatters.nlFormatter.parse("1.000"))
    }

    @Test
    fun testParseDecimal() {
        val formatters = createFormatters(NumberFormatStyle.Decimal())

        assertEquals(2.4, formatters.usFormatter.parse("2.4"))
        assertEquals(2.4, formatters.nlFormatter.parse("2,4"))

        assertEquals(1234.56, formatters.usFormatter.parse("1,234.56"))
        assertEquals(1234.56, formatters.nlFormatter.parse("1.234,56"))
    }

    @Test
    fun testParseScientific() {
        val formatters = createFormatters(NumberFormatStyle.Scientific())

        assertEquals(0.024, formatters.usFormatter.parse("2.4E-2"))
        assertEquals(0.024, formatters.nlFormatter.parse("2,4E-2"))
    }

    @Test
    fun testParsePercent() {
        val formatters = createFormatters(NumberFormatStyle.Percentage())

        assertEquals(0.805, formatters.usFormatter.parse("80.5%"))
        assertEquals(0.805, formatters.nlFormatter.parse("80,5%"))
    }

    @Test
    fun testFormatInteger() {
        val formatters = createFormatters(NumberFormatStyle.Integer()) { it.usesGroupingSeparator = true }
        assertEquals("1", formatters.usFormatter.format(1))
        assertEquals("1", formatters.nlFormatter.format(1))

        assertEquals("2", formatters.usFormatter.format(2.4))
        assertEquals("2", formatters.nlFormatter.format(2.4))

        assertEquals("3", formatters.usFormatter.format(2.6))
        assertEquals("3", formatters.nlFormatter.format(2.6))

        assertEquals("1,000", formatters.usFormatter.format(1000))
        assertEquals("1.000", formatters.nlFormatter.format(1000))
    }

    @Test
    fun testFormatDecimal() {
        val formatters = createFormatters(NumberFormatStyle.Decimal(minFractionDigits = 2U, maxFractionDigits = 4U))
        assertEquals("1.00", formatters.usFormatter.format(1))
        assertEquals("1,00", formatters.nlFormatter.format(1))

        assertEquals("1.2345", formatters.usFormatter.format(1.2345))
        assertEquals("1,2345", formatters.nlFormatter.format(1.2345))

        assertEquals("1.2346", formatters.usFormatter.format(1.23456789))
        assertEquals("1,2346", formatters.nlFormatter.format(1.23456789))
    }

    @Test
    fun testFormatPercentage() {
        val formatters = createFormatters(NumberFormatStyle.Percentage(minFractionDigits = 0U, maxFractionDigits = 2U))

        assertEquals("200%", formatters.usFormatter.format(2.0))
        assertEquals("200%", formatters.nlFormatter.format(2.0))

        assertEquals("80%", formatters.usFormatter.format(0.8))
        assertEquals("80%", formatters.nlFormatter.format(0.8))

        assertEquals("80.12%", formatters.usFormatter.format(0.801234))
        assertEquals("80,12%", formatters.nlFormatter.format(0.801234))
    }

    @Test
    fun testFormatPermillage() {
        val formatters = createFormatters(NumberFormatStyle.Permillage(minFractionDigits = 0U, maxFractionDigits = 2U)) { it.usesGroupingSeparator = false }

        assertEquals("2000‰", formatters.usFormatter.format(2.0))
        assertEquals("2000‰", formatters.nlFormatter.format(2.0))

        assertEquals("800‰", formatters.usFormatter.format(0.8))
        assertEquals("800‰", formatters.nlFormatter.format(0.8))

        assertEquals("801.23‰", formatters.usFormatter.format(0.801234))
        assertEquals("801,23‰", formatters.nlFormatter.format(0.801234))
    }

    @Test
    fun testFormatPermillageLocalizesSymbol() {
        val style = NumberFormatStyle.Permillage(minFractionDigits = 0U, maxFractionDigits = 0U)
        val us = NumberFormatter(UnitedStates, style).apply { usesGroupingSeparator = false }
        val turkish = NumberFormatter(Turkey, style).apply { usesGroupingSeparator = false }

        val usResult = us.format(0.5)
        val trResult = turkish.format(0.5)

        // Both encode 500 per-mille (value is multiplied by 1000)...
        assertEquals("500", usResult.filter { it.isDigit() })
        assertEquals("500", trResult.filter { it.isDigit() })

        // ...but the sign is placed per locale: en-US uses a suffix, tr-TR a prefix.
        assertTrue(usResult.endsWith(us.perMillSymbol), "expected suffix per-mille for en-US, was $usResult")
        assertTrue(trResult.startsWith(turkish.perMillSymbol), "expected prefix per-mille for tr-TR, was $trResult")
    }

    @Test
    fun testFormatScientific() {
        val formatters = createFormatters(NumberFormatStyle.Scientific(minFractionDigits = 4U, maxFractionDigits = 4U, minExponent = 2U))
        assertEquals("2.0000E00", formatters.usFormatter.format(2))
        assertEquals("2,0000E00", formatters.nlFormatter.format(2))

        assertEquals("1.2346E08", formatters.usFormatter.format(123456789))
        assertEquals("1,2346E08", formatters.nlFormatter.format(123456789))

        assertEquals("1.2345E-06", formatters.usFormatter.format(0.0000012345))
        assertEquals("1,2345E-06", formatters.nlFormatter.format(0.0000012345))
    }

    @Test
    fun testFormatScientificMinExponentZero() {
        // minExponent = 0 is coerced to 1 (a scientific format always shows ≥1 exponent digit); without
        // the coerce this produced a malformed "…E" pattern that threw on the JVM.
        val zero = NumberFormatter(UnitedStates, NumberFormatStyle.Scientific(minExponent = 0U))
        val one = NumberFormatter(UnitedStates, NumberFormatStyle.Scientific(minExponent = 1U))
        assertEquals(one.format(12345), zero.format(12345))
        assertEquals("1.2345E4", zero.format(12345))
    }

    @Test
    fun testFormatScientificWithDecimalNotationThreshold() {
        val formatters = createFormatters(NumberFormatStyle.Scientific(maxExponentForDecimalNotation = 6U))
        // |exponent| <= 6 -> plain localized decimal (grouped, reusing the mantissa's fraction digits).
        assertEquals("1,000.0", formatters.usFormatter.format(1000))
        assertEquals("1.000,0", formatters.nlFormatter.format(1000))
        assertEquals("1,000,000.0", formatters.usFormatter.format(1000000))
        assertEquals("0.001", formatters.usFormatter.format(0.001))
        assertEquals("12,345.678", formatters.usFormatter.format(12345.678))
        // beyond the threshold -> scientific notation.
        assertEquals("1.0E7", formatters.usFormatter.format(10000000))
        assertEquals("1.0E-7", formatters.usFormatter.format(0.0000001))
    }

    @Test
    fun testFormatScientificDecimalNotationWithoutGrouping() {
        val formatters = createFormatters(NumberFormatStyle.Scientific(maxExponentForDecimalNotation = 6U)) { it.usesGroupingSeparator = false }
        assertEquals("1000000.0", formatters.usFormatter.format(1000000))
        assertEquals("1.0E7", formatters.usFormatter.format(10000000))
    }

    @Test
    fun testFormatCurrency() {
        val formatters = createFormatters(
            NumberFormatStyle.Currency(
                minFractionDigits = 2U,
                maxFractionDigits = 2U,
            ),
        ) { it.usesGroupingSeparator = true }
        assertEquals("$1.00", formatters.usFormatter.format(1).replace("\u00A0", " "))
        assertEquals("€ 1,00", formatters.nlFormatter.format(1).replace("\u00A0", " "))

        assertEquals("$1.23", formatters.usFormatter.format(1.2345).replace("\u00A0", " "))
        assertEquals("€ 1,23", formatters.nlFormatter.format(1.2345).replace("\u00A0", " "))

        assertEquals("$12,345.67", formatters.usFormatter.format(12345.67).replace("\u00A0", " "))
        assertEquals("€ 12.345,67", formatters.nlFormatter.format(12345.67).replace("\u00A0", " "))
    }

    @Test
    fun testFormatForeignCurrency() {
        val usdFormatters = createFormatters(NumberFormatStyle.Currency(currencyCode = "USD")) { it.usesGroupingSeparator = true }
        assertEquals("$12,345.68", usdFormatters.usFormatter.format(12345.6789).replace("\u00A0", " "))
        assertEquals("$USDForNL 12.345,68", usdFormatters.nlFormatter.format(12345.6789).replace("\u00A0", " "))

        val yenFormatters = createFormatters(NumberFormatStyle.Currency(currencyCode = "JPY")) { it.usesGroupingSeparator = true }
        assertEquals("${JPYForUS}12,346", yenFormatters.usFormatter.format(12345.6789).replace("\u00A0", " "))
        assertEquals("$JPYForNL 12.346", yenFormatters.nlFormatter.format(12345.6789).replace("\u00A0", " "))
    }

    @Test
    fun testCustomFormat() {
        val formatters = createFormatters(NumberFormatStyle.Pattern("Positive #.00'#'", "Negative #.00'#'"))
        assertEquals("Positive 1000.00#", formatters.usFormatter.format(1000))
        assertEquals("Positive 1000,00#", formatters.nlFormatter.format(1000))

        assertEquals("Negative 1000.00#", formatters.usFormatter.format(-1000))
        assertEquals("Negative 1000,00#", formatters.nlFormatter.format(-1000))
    }

    @Test
    fun testScientificAndPatternApplyCustomDecimalSeparator() {
        val scientific = NumberFormatter(UnitedStates, NumberFormatStyle.Scientific(minFractionDigits = 2U, maxFractionDigits = 2U)).apply {
            decimalSeparator = '!'
        }
        val sci = scientific.format(2)
        assertTrue(sci.contains('!'), "expected custom decimal separator in scientific output, was $sci")

        val pattern = NumberFormatter(UnitedStates, NumberFormatStyle.Pattern("#,##0.00", "-#,##0.00")).apply {
            decimalSeparator = '!'
        }
        val pat = pattern.format(1.5)
        assertTrue(pat.contains('!'), "expected custom decimal separator in pattern output, was $pat")
    }

    @Test
    fun testFailToParseInvalidString() {
        val formatter = NumberFormatter(style = NumberFormatStyle.Integer())
        assertNull(formatter.parse("invalid number"))
    }

    private fun createFormatters(style: NumberFormatStyle, apply: ((NumberFormatter) -> Unit)? = null): Formatters {
        val usFormatter = NumberFormatter(UnitedStates, style).apply { apply?.invoke(this) }
        val nlFormatter = NumberFormatter(Netherlands, style).apply { apply?.invoke(this) }
        return Formatters(usFormatter, nlFormatter)
    }

    private data class Formatters(val usFormatter: NumberFormatter, val nlFormatter: NumberFormatter)
}
