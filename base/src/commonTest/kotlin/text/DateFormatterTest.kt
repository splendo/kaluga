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
import com.splendo.kaluga.base.text.DateFormatter
import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.base.utils.TimeZone
import com.splendo.kaluga.base.utils.createLocale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DateFormatterTest {

    companion object {
        private val UnitedStatesLocale = createLocale("en", "US")
        private val PSTTimeZone = TimeZone.current()
    }

    @Test
    fun testFormatDate() {
        val formatter = DateFormatter.dateFormat(DateFormatStyle.Short, PSTTimeZone, UnitedStatesLocale)
        val date = Date.now().apply {
            year = 2020
            month = 1
            day = 8
        }

        assertEquals("1/8/20", formatter.format(date))
    }

    @Test
    fun testParseDate() {
        val formatter = DateFormatter.dateFormat(DateFormatStyle.Short, PSTTimeZone, UnitedStatesLocale)
        val date = formatter.parse("1/8/20")
        assertNotNull(date)
        assertEquals(2020, date.year)
        assertEquals(1, date.month)
        assertEquals(8, date.day)
    }
}
