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

import com.splendo.kaluga.base.text.DateFormatStyle
import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.text.dateFormat
import com.splendo.kaluga.base.text.iso8601Pattern
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.Locale.Companion.createLocale
import com.splendo.kaluga.base.utils.TimeZone
import com.splendo.kaluga.base.utils.nowUtc
import com.splendo.kaluga.base.utils.utc
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DateFormatterTest {

    companion object {
        private val UnitedStatesLocale = createLocale("en", "US")
        private val FranceLocale = createLocale("fr", "FR")
        private val PSTTimeZone = TimeZone.get("America/Los_Angeles")!!

        private val January81988 = DefaultKalugaDate.epoch(568627200000)
        private val March181988 = DefaultKalugaDate.epoch(574695462750)
    }

    @Test
    fun testFormatDate() {
        val formatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Short, PSTTimeZone, UnitedStatesLocale)
        val date = DefaultKalugaDate.nowUtc().apply {
            year = 2020
            month = 1
            day = 8
            hour = 12
            minute = 1
        }

        assertEquals("1/8/20", formatter.format(date))
    }

    @Test
    fun testParseDate() {
        val formatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Short, PSTTimeZone, UnitedStatesLocale)
        val date = formatter.parse("1/8/20")
        assertNotNull(date)
        assertEquals(2020, date.year)
        assertEquals(1, date.month)
        assertEquals(8, date.day)
    }

    @Test
    fun testDateFormat() {
        val usFormatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium, TimeZone.utc, UnitedStatesLocale)
        val frFormatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium, TimeZone.utc, FranceLocale)

        assertEquals("Jan 8, 1988", usFormatter.format(January81988))
        assertEquals("8 janv. 1988", frFormatter.format(January81988))
    }

    @Test
    fun testDateFormatWithoutYear() {
        val usFormatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium, true, TimeZone.utc, UnitedStatesLocale)
        val frFormatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium, true, TimeZone.utc, FranceLocale)

        assertEquals("Jan 8", usFormatter.format(January81988))
        assertEquals("8 janv.", frFormatter.format(January81988))
    }

    @Test
    fun testTimeFormat() {
        val usFormatter = KalugaDateFormatter.timeFormat(DateFormatStyle.Medium, TimeZone.utc, UnitedStatesLocale)
        val frFormatter = KalugaDateFormatter.timeFormat(DateFormatStyle.Medium, TimeZone.utc, FranceLocale)

        assertEquals("1:37:42 PM", usFormatter.format(March181988))
        assertEquals("13:37:42", frFormatter.format(March181988))
    }

    @Test
    fun testFormatFixedDate() {
        val formatter = KalugaDateFormatter.iso8601Pattern(TimeZone.utc)
        assertEquals("1988-03-18T13:37:42.750+0000", formatter.format(March181988))
    }

    @Test
    fun testFailToParseInvalidString() {
        val formatter = KalugaDateFormatter.iso8601Pattern(TimeZone.utc)
        assertNull(formatter.parse("invalid date"))
    }
}
