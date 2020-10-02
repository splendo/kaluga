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

package com.splendo.kaluga.base.test.utils

import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.TimeZone
import com.splendo.kaluga.base.utils.nowUtc
import com.splendo.kaluga.base.utils.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTest {

    @Test
    fun testEquality() {
        val now = Date.now()
        assertEquals(now, now.copy(), "copied Date should be equal")

        val nearEpoch = Date.epoch(1001)
        assertEquals(Date.epoch(1001), nearEpoch, "equally created dates should be equal")

        assertEquals(Date.epoch(1002), nearEpoch + Date.epoch(1), "Date from addition should be equal")
    }

    @Test
    fun testUTCDate() {
        val utcNow = Date.nowUtc()
        val epochNow = utcNow.millisecondSinceEpoch
        val now = Date.epoch(epochNow, TimeZone.get("UTC")!!)
        assertEquals(utcNow.millisecondSinceEpoch, now.millisecondSinceEpoch)
        assertEquals(utcNow, now)
    }

    @Test
    fun testCreateEpochDate() {
        val someDay = Date.epoch().apply {
            year = 2020
            month = 5
            day = 12
            hour = 8
            minute = 45
        }
        val epoch = Date.epoch()

        assertTrue(epoch < someDay)
    }

    @Test
    fun testCreateNowDate() {
        val now = Date.now()
        val epoch = Date.epoch()

        assertTrue(now > epoch)
    }

    @Test
    fun testUpdateDate() {
        val epoch = Date.epoch()
        val isEarlierThanGMT = epoch.timeZone.offsetFromGMTAtDateInMilliseconds(epoch) < 0
        assertEquals(if (isEarlierThanGMT) 1969 else 1970, epoch.year)
        assertEquals(if (isEarlierThanGMT) 12 else 1, epoch.month)
        epoch.month += 22
        assertEquals(1971, epoch.year)
        assertEquals(if (isEarlierThanGMT) 10 else 11, epoch.month)
    }

    @Test
    fun testGet() {
        val someDay = Date.epoch(574695462750, TimeZone.get("UTC")!!)

        assertEquals(1, someDay.era)
        assertEquals(1988, someDay.year)
        assertEquals(12, someDay.weekOfYear)
        assertEquals(3, someDay.month)
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
        val france = Locale.createLocale("fr", "FR")
        val us = Locale.createLocale("en", "US")

        val frenchNow = Date.now(0, TimeZone.get("UTC")!!, france)
        val usNow = Date.now(0, TimeZone.get("UTC")!!, us)

        assertEquals(2, frenchNow.firstWeekDay)
        assertEquals(1, usNow.firstWeekDay)
    }
}
