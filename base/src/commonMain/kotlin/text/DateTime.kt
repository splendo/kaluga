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

package com.splendo.kaluga.base.text

internal object DateTime {
    const val HOUR_OF_DAY_0 = 'H' // (00 - 23)
    const val HOUR_0 = 'I' // (01 - 12)
    const val HOUR_OF_DAY = 'k' // (0 - 23) -- like H
    const val HOUR = 'l' // (1 - 12) -- like I
    const val MINUTE = 'M' // (00 - 59)
    const val NANOSECOND = 'N' // (000000000 - 999999999)
    const val MILLISECOND = 'L' // jdk, not in gnu (000 - 999)
    const val MILLISECOND_SINCE_EPOCH = 'Q' // (0 - 99...?)
    const val AM_PM = 'p' // (am or pm)
    const val SECONDS_SINCE_EPOCH = 's' // (0 - 99...?)
    const val SECOND = 'S' // (00 - 60 - leap second)
    const val TIME = 'T' // (24 hour hh:mm:ss)
    const val ZONE_NUMERIC = 'z' // (-1200 - +1200) - ls minus?
    const val ZONE = 'Z' // (symbol)

    // Date
    const val NAME_OF_DAY_ABBREV = 'a' // 'a'
    const val NAME_OF_DAY = 'A' // 'A'
    const val NAME_OF_MONTH_ABBREV = 'b' // 'b'
    const val NAME_OF_MONTH = 'B' // 'B'
    const val CENTURY = 'C' // (00 - 99)
    const val DAY_OF_MONTH_0 = 'd' // (01 - 31)
    const val DAY_OF_MONTH = 'e' // (1 - 31) -- like d

    const val NAME_OF_MONTH_ABBREV_X = 'h' // -- same b
    const val DAY_OF_YEAR = 'j' // (001 - 366)
    const val MONTH = 'm' // (01 - 12)

    const val YEAR_2 = 'y' // (00 - 99)
    const val YEAR_4 = 'Y' // (0000 - 9999)

    // Composites
    const val TIME_12_HOUR = 'r' // (hh:mm:ss [AP]M)
    const val TIME_24_HOUR = 'R' // (hh:mm same as %H:%M)

    const val DATE_TIME = 'c'

    // (Sat Nov 04 12:02:33 EST 1999)
    const val DATE = 'D' // (mm/dd/yy)
    const val ISO_STANDARD_DATE = 'F' // (%Y-%m-%d)

    fun isValid(c: Char): Boolean {
        return when (c) {
            HOUR_OF_DAY_0,
            HOUR_0,
            HOUR_OF_DAY,
            HOUR,
            MINUTE,
            NANOSECOND,
            MILLISECOND,
            MILLISECOND_SINCE_EPOCH,
            AM_PM,
            SECONDS_SINCE_EPOCH,
            SECOND,
            TIME,
            ZONE_NUMERIC,
            ZONE,
            NAME_OF_DAY_ABBREV,
            NAME_OF_DAY,
            NAME_OF_MONTH_ABBREV,
            NAME_OF_MONTH,
            CENTURY,
            DAY_OF_MONTH_0,
            DAY_OF_MONTH,
            NAME_OF_MONTH_ABBREV_X,
            DAY_OF_YEAR,
            MONTH,
            YEAR_2,
            YEAR_4,
            TIME_12_HOUR,
            TIME_24_HOUR,
            DATE_TIME,
            DATE,
            ISO_STANDARD_DATE -> true
            else -> false
        }
    }
}
