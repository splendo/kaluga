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

import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.utils.Locale.Companion.createLocale
import com.splendo.kaluga.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberFormatterTest : BaseTest() {

    companion object {
        private val UnitedStates = createLocale("en", "US")
        private val Netherlands = createLocale("nl", "NL")
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
    fun tesParseDecimal() {
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
    fun testFormatCurrency() {
        val formatters = createFormatters(NumberFormatStyle.Currency(minFractionDigits = 2U, maxFractionDigits = 2U)) { it.usesGroupingSeparator = true }
        assertEquals("$1.00", formatters.usFormatter.format(1).replace("\u00A0", " "))
        assertEquals("€ 1,00", formatters.nlFormatter.format(1).replace("\u00A0", " "))

        assertEquals("$1.23", formatters.usFormatter.format(1.2345).replace("\u00A0", " "))
        assertEquals("€ 1,23", formatters.nlFormatter.format(1.2345).replace("\u00A0", " "))

        assertEquals("$12,345.67", formatters.usFormatter.format(12345.67).replace("\u00A0", " "))
        assertEquals("€ 12.345,67", formatters.nlFormatter.format(12345.67).replace("\u00A0", " "))
    }

    @Test
    fun testCustomFormat() {
        val formatters = createFormatters(NumberFormatStyle.Pattern("Positive #.00'#'", "Negative #.00'#'"))
        assertEquals("Positive 1000.00#", formatters.usFormatter.format(1000))
        assertEquals("Positive 1000,00#", formatters.nlFormatter.format(1000))

        assertEquals("Negative 1000.00#", formatters.usFormatter.format(-1000))
        assertEquals("Negative 1000,00#", formatters.nlFormatter.format(-1000))
    }

    private fun createFormatters(style: NumberFormatStyle, apply: ((NumberFormatter) -> Unit)? = null): Formatters {
        val usFormatter = NumberFormatter(UnitedStates, style).apply { apply?.invoke(this) }
        val nlFormatter = NumberFormatter(Netherlands, style).apply { apply?.invoke(this) }
        return Formatters(usFormatter, nlFormatter)
    }

    private data class Formatters(val usFormatter: NumberFormatter, val nlFormatter: NumberFormatter)
}
