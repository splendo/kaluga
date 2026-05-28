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

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.KalugaLocale.Companion.createLocale
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.enUsPosix
import com.splendo.kaluga.base.utils.nowUtc
import com.splendo.kaluga.base.utils.utc
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds

class DateFormatterTest {

    companion object {
        private val UnitedStatesLocale = createLocale("en", "US")
        private val FranceLocale = createLocale("fr", "FR")
        private val PSTTimeZone = KalugaTimeZone.get("America/Los_Angeles")!!

        private val January81988 = DefaultKalugaDate.epoch(568627200000.milliseconds)
        private val March181988 = DefaultKalugaDate.epoch(574695462750.milliseconds)
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
        assertEquals(0, date.hour)
        assertEquals(0, date.minute)
        assertEquals(0, date.second)
        assertEquals(PSTTimeZone.identifier, date.timeZone.identifier)
    }

    @Test
    fun testDateFormat() {
        val usFormatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium, KalugaTimeZone.utc, UnitedStatesLocale)
        val frFormatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium, KalugaTimeZone.utc, FranceLocale)

        assertEquals("Jan 8, 1988", usFormatter.format(January81988))
        assertEquals("8 janv. 1988", frFormatter.format(January81988))
    }

    @Test
    fun testDateFormatWithoutYear() {
        val usFormatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium, true, KalugaTimeZone.utc, UnitedStatesLocale)
        val frFormatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium, true, KalugaTimeZone.utc, FranceLocale)

        assertEquals("Jan 8", usFormatter.format(January81988))
        assertEquals("8 janv.", frFormatter.format(January81988))
    }

    @Test
    @Ignore // android emulator 23 does not correctly format the french locale
    fun testTimeFormat() {
        val usFormatter = KalugaDateFormatter.timeFormat(DateFormatStyle.Medium, KalugaTimeZone.utc, UnitedStatesLocale)
        val frFormatter = KalugaDateFormatter.timeFormat(DateFormatStyle.Medium, KalugaTimeZone.utc, FranceLocale)

        assertEquals("1:37:42 PM", usFormatter.format(March181988))
        assertEquals("13:37:42", frFormatter.format(March181988))
    }

    @Test
    fun testFormatFixedDate() {
        val formatter = KalugaDateFormatter.iso8601Pattern(KalugaTimeZone.utc)
        assertEquals("1988-03-18T13:37:42.750+0000", formatter.format(March181988))
    }

    @Test
    fun testFailToParseInvalidString() {
        val formatter = KalugaDateFormatter.iso8601Pattern(KalugaTimeZone.utc)
        assertNull(formatter.parse("invalid date"))
    }

    @Test
    @Ignore // fails on emulator 24
    fun testParseDateWithDifferentTimezone() {
        val utcFormatter = KalugaDateFormatter.patternFormat("yyyy.MM.dd G 'at' HH:mm:ss z", KalugaTimeZone.utc, KalugaLocale.enUsPosix)
        val pstFormatter = KalugaDateFormatter.patternFormat("yyyy.MM.dd G 'at' HH:mm:ss z", PSTTimeZone, KalugaLocale.enUsPosix)

        val epochInUtc = DefaultKalugaDate.epoch(timeZone = KalugaTimeZone.utc, locale = KalugaLocale.enUsPosix)
        val epochInPst = DefaultKalugaDate.epoch(timeZone = PSTTimeZone, locale = KalugaLocale.enUsPosix)
        assertEquals("1970.01.01 AD at 00:00:00 ${KalugaTimeZone.utc.identifier}", utcFormatter.format(epochInUtc))
        assertEquals("1970.01.01 AD at 00:00:00 ${KalugaTimeZone.utc.identifier}", utcFormatter.format(epochInPst))
        assertEquals("1969.12.31 AD at 16:00:00 PST", pstFormatter.format(epochInUtc))
        assertEquals("1969.12.31 AD at 16:00:00 PST", pstFormatter.format(epochInPst))
        assertEquals(epochInUtc, utcFormatter.parse(utcFormatter.format(epochInUtc)))
        assertEquals(epochInUtc, utcFormatter.parse(pstFormatter.format(epochInPst)))
        assertEquals(epochInPst, pstFormatter.parse(utcFormatter.format(epochInUtc)))
        assertEquals(epochInPst, pstFormatter.parse(pstFormatter.format(epochInPst)))
    }
}
