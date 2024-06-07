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

package com.splendo.kaluga.base.test.utils

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.enUsPosix
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.nowUtc
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.base.utils.toStartOfDay
import com.splendo.kaluga.base.utils.utc
import kotlin.math.roundToLong
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class DateTest {

    @Test
    fun testEquality() {
        val now = DefaultKalugaDate.now(locale = KalugaLocale.enUsPosix)
        assertEquals(now, now.copy(), "copied Date should be equal")

        val nearEpoch = DefaultKalugaDate.epoch(1001.milliseconds)
        assertEquals(DefaultKalugaDate.epoch(1001.milliseconds), nearEpoch, "equally created dates should be equal")

        assertEquals(DefaultKalugaDate.epoch(10.seconds), (DefaultKalugaDate.epoch(9.seconds) + 1.seconds), "Date from addition should be equal")
    }

    @Test
    fun testUTCDate() {
        val utcNow = DefaultKalugaDate.nowUtc(locale = KalugaLocale.enUsPosix)
        val epochNow = utcNow.durationSinceEpoch
        val now = DefaultKalugaDate.epoch(epochNow, KalugaTimeZone.utc, locale = KalugaLocale.enUsPosix)
        assertEquals(utcNow.durationSinceEpoch, now.durationSinceEpoch)
        assertEquals(utcNow, now)
    }

    @Test
    fun testCreateEpochDate() {
        val someDay = DefaultKalugaDate.epoch(locale = KalugaLocale.enUsPosix).apply {
            year = 2020
            month = 5
            day = 12
            hour = 8
            minute = 45
        }
        val epoch = DefaultKalugaDate.epoch(locale = KalugaLocale.enUsPosix)

        assertTrue(epoch < someDay)

        val fiveYearsFromEpoch = DefaultKalugaDate.epoch(locale = KalugaLocale.enUsPosix, offset = (1830).days)
        assertEquals(1830.days.inWholeSeconds, (fiveYearsFromEpoch - epoch).toDouble(DurationUnit.SECONDS).roundToLong())
    }

    @Test
    fun testCreateNowDate() {
        val now = DefaultKalugaDate.now(locale = KalugaLocale.enUsPosix)
        val epoch = DefaultKalugaDate.epoch(locale = KalugaLocale.enUsPosix)

        assertTrue(now > epoch)

        val fiveYearsAgo = DefaultKalugaDate.now(locale = KalugaLocale.enUsPosix, offset = (-1830).days)
        assertEquals(1830.days.inWholeSeconds, (now - fiveYearsAgo).toDouble(DurationUnit.SECONDS).roundToLong())
    }

    @Test
    fun testUpdateDate() {
        val epoch = DefaultKalugaDate.epoch(locale = KalugaLocale.enUsPosix)
        val isEarlierThanGMT = epoch.timeZone.offsetFromGMTAtDate(epoch).isNegative()
        assertEquals(if (isEarlierThanGMT) 1969 else 1970, epoch.year)
        assertEquals(if (isEarlierThanGMT) 12 else 1, epoch.month)
        epoch.month += 22
        assertEquals(1971, epoch.year)
        assertEquals(if (isEarlierThanGMT) 10 else 11, epoch.month)
    }

    @Test
    fun testGet() {
        val someDay = DefaultKalugaDate.epoch(574695462750.milliseconds, KalugaTimeZone.utc, locale = KalugaLocale.enUsPosix)

        assertEquals(1, someDay.era)
        assertEquals(1988, someDay.year)
        assertEquals(12, someDay.weekOfYear)
        assertEquals(3, someDay.month)
        assertEquals(31, someDay.daysInMonth)
        assertEquals(3, someDay.weekOfMonth)
        assertEquals(18, someDay.day)
        assertEquals(78, someDay.dayOfYear)
        assertEquals(6, someDay.weekDay)
        assertEquals(13, someDay.hour)
        assertEquals(37, someDay.minute)
        assertEquals(42, someDay.second)
        assertEquals(750, someDay.millisecond)
    }

    @Test
    fun testStartOfWeek() {
        val france = KalugaLocale.createLocale("fr", "FR")
        val us = KalugaLocale.createLocale("en", "US")

        val frenchNow = DefaultKalugaDate.now(0.milliseconds, KalugaTimeZone.utc, france)
        val usNow = DefaultKalugaDate.now(0.milliseconds, KalugaTimeZone.utc, us)

        assertEquals(2, frenchNow.firstWeekDay)
        assertEquals(1, usNow.firstWeekDay)
    }

    @Test
    fun testDaylightSavings() {
        val dayBeforeDLS = DefaultKalugaDate.epoch(1616828400000.milliseconds, locale = KalugaLocale.createLocale("nl", "NL"), timeZone = KalugaTimeZone.get("Europe/Amsterdam")!!)
        val startOfDayBeforeDLS = dayBeforeDLS.toStartOfDay()
        assertEquals(0, startOfDayBeforeDLS.hour)
        assertEquals(27, startOfDayBeforeDLS.day)
        val dlsDay = dayBeforeDLS.copy().apply {
            day += 1
        }
        assertEquals(8, dlsDay.hour)
        assertEquals(28, dlsDay.day)
        val startOfDLSDay = dayBeforeDLS.copy().apply {
            day += 1
        }.toStartOfDay()
        assertEquals(0, startOfDLSDay.hour)
        assertEquals(28, startOfDLSDay.day)
        val timeInDLSJump = startOfDLSDay.copy().apply {
            hour = 2
            minute = 30
        }
        assertEquals(3, timeInDLSJump.hour)
        assertEquals(30, timeInDLSJump.minute)
        val dayAfterDLS = dayBeforeDLS.copy().apply {
            day += 2
        }
        assertEquals(8, dayAfterDLS.hour)
        assertEquals(29, dayAfterDLS.day)
        val startOfDayAfterDLS = dayBeforeDLS.copy().apply {
            day += 2
        }.toStartOfDay()
        assertEquals(0, startOfDayAfterDLS.hour)
        assertEquals(29, startOfDayAfterDLS.day)
    }
}
