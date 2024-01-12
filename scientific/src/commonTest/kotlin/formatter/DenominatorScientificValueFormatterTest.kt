/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.formatter

import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.enUsPosix
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.plus
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.Yard
import kotlin.test.Test
import kotlin.test.assertEquals

class DenominatorScientificValueFormatterTest {

    @Test
    fun testDenominatorFormatting() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd", formatter.format(5(Yard)))
        assertEquals("2 ft", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("2 ft", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd", formatter.format(0(Yard)))
        assertEquals("0 yd", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingWithLastNotRounded() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U, maxFractionDigits = 10U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        // assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        // assertEquals("5 yd 6 in", formatter.format(5(Yard) + 6(Inch)))
        // assertEquals("5 yd 2 ft", formatter.format(5(Yard) + 2(Foot)))
        // assertEquals("5 yd", formatter.format(5(Yard)))
        // assertEquals("2 ft", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("2 ft 0.1 in", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        // assertEquals("0 yd", formatter.format(0(Yard)))
        // assertEquals("0.0000000004 in", formatter.format(0.00000000001(Yard)))
        // assertEquals("1 yd", formatter.format(0.999999999(Yard)))
        // assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingNonEndingZero() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            includeZeroValues = DenominatorScientificValueFormatter.IncludeZeroValues.ONLY_NON_ENDING
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 0 ft 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd", formatter.format(5(Yard)))
        assertEquals("0 yd 2 ft", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("0 yd 2 ft", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd", formatter.format(0(Yard)))
        assertEquals("0 yd", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingNonEndingZeroWithLastNotRounded() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            includeZeroValues = DenominatorScientificValueFormatter.IncludeZeroValues.ONLY_NON_ENDING
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U, maxFractionDigits = 10U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 0 ft 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd", formatter.format(5(Yard)))
        assertEquals("0 yd 2 ft", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("0 yd 2 ft 0.1 in", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd", formatter.format(0(Yard)))
        assertEquals("0 yd 0 ft 0.0000000004 in", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingFirstEndingZero() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            includeZeroValues = DenominatorScientificValueFormatter.IncludeZeroValues.ONLY_FIRST_ENDING
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft 0 in", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd 0 ft", formatter.format(5(Yard)))
        assertEquals("2 ft 0 in", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("2 ft 0 in", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd", formatter.format(0(Yard)))
        assertEquals("0 yd", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd 0 ft", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingFirstEndingZeroWithLastNotRounded() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            includeZeroValues = DenominatorScientificValueFormatter.IncludeZeroValues.ONLY_FIRST_ENDING
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U, maxFractionDigits = 10U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft 0 in", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd 0 ft", formatter.format(5(Yard)))
        assertEquals("2 ft 0 in", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("2 ft 0.1 in", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd", formatter.format(0(Yard)))
        assertEquals("0.0000000004 in", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd 0 ft", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingNonEndingAndFirstEndingZero() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            includeZeroValues = DenominatorScientificValueFormatter.IncludeZeroValues.ONLY_NON_ENDING_AND_FIRST_ENDING
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 0 ft 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft 0 in", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd 0 ft", formatter.format(5(Yard)))
        assertEquals("0 yd 2 ft 0 in", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("0 yd 2 ft 0 in", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd", formatter.format(0(Yard)))
        assertEquals("0 yd", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd 0 ft", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingNonEndingAndFirstEndingZeroWithLastNotRounded() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            includeZeroValues = DenominatorScientificValueFormatter.IncludeZeroValues.ONLY_NON_ENDING_AND_FIRST_ENDING
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U, maxFractionDigits = 10U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 0 ft 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft 0 in", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd 0 ft", formatter.format(5(Yard)))
        assertEquals("0 yd 2 ft 0 in", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("0 yd 2 ft 0.1 in", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd", formatter.format(0(Yard)))
        assertEquals("0 yd 0 ft 0.0000000004 in", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd 0 ft", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingAllZero() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            includeZeroValues = DenominatorScientificValueFormatter.IncludeZeroValues.ALL
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 0 ft 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft 0 in", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd 0 ft 0 in", formatter.format(5(Yard)))
        assertEquals("0 yd 2 ft 0 in", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("0 yd 2 ft 0 in", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd 0 ft 0 in", formatter.format(0(Yard)))
        assertEquals("0 yd 0 ft 0 in", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd 0 ft 0 in", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorFormattingAllZeroWithLastNotRounded() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            includeZeroValues = DenominatorScientificValueFormatter.IncludeZeroValues.ALL
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U, maxFractionDigits = 10U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("5 yd 2 ft 6 in", formatter.format(5(Yard) + 2(Foot) + 6(Inch)))
        assertEquals("5 yd 0 ft 6 in", formatter.format(5(Yard) + 6(Inch)))
        assertEquals("5 yd 2 ft 0 in", formatter.format(5(Yard) + 2(Foot)))
        assertEquals("5 yd 0 ft 0 in", formatter.format(5(Yard)))
        assertEquals("0 yd 2 ft 0 in", formatter.format(0(Yard) + 2(Foot)))
        assertEquals("0 yd 2 ft 0.1 in", formatter.format(0(Yard) + 2(Foot) + 0.1(Inch)))
        assertEquals("0 yd 0 ft 0 in", formatter.format(0(Yard)))
        assertEquals("0 yd 0 ft 0.0000000004 in", formatter.format(0.00000000001(Yard)))
        assertEquals("1 yd 0 ft 0 in", formatter.format(0.999999999(Yard)))
        assertEquals("1.5 m", formatter.format(1.5(Meter)))
    }

    @Test
    fun testDenominatorWithDecimalScale() {
        val formatter = DenominatorScientificValueFormatter.with {
            Meter denominateBy listOf(Millimeter)
            scale = 1U
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("1.5 m 20 mm", formatter.format(1.52(Meter)))
    }

    @Test
    fun formatAllForQuantity() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            PhysicalQuantity.Length formatAs Yard
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U, maxFractionDigits = 10U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("1093 yd 1 ft 10.0787401575 in", formatter.format(1(Kilometer)))
        assertEquals("0.0393700787 in", formatter.format(1(Millimeter)))
        assertEquals("1 ft", formatter.format(1(Foot)))
    }

    @Test
    fun formatUnitAsDifferentUnit() {
        val formatter = DenominatorScientificValueFormatter.with {
            Yard denominateBy listOf(Foot, Inch)
            Meter formatAs Yard
            denominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            lastDenominatorUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Integer(minDigits = 1U))
            defaultUnitFormatter = NumberFormatter(locale = KalugaLocale.enUsPosix, style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        }

        assertEquals("1 yd 3 in", formatter.format(1(Meter)))
        assertEquals("1 mm", formatter.format(1(Millimeter)))
        assertEquals("1 ft", formatter.format(1(Foot)))
    }
}
