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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTest {

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
}
