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

internal enum class DateTime(val char: Char) {
    HOUR_OF_DAY_0('H'), // (00 - 23)
    HOUR_0('I'), // (01 - 12)
    HOUR_OF_DAY('k'), // (0 - 23) -- like H
    HOUR('l'), // (1 - 12) -- like I
    MINUTE('M'), // (00 - 59)
    NANOSECOND('N'), // (000000000 - 999999999)
    MILLISECOND('L'), // jdk, not in gnu (000 - 999)
    MILLISECOND_SINCE_EPOCH('Q'), // (0 - 99...?)
    AM_PM('p'), // (am or pm)
    SECONDS_SINCE_EPOCH('s'), // (0 - 99...?)
    SECOND('S'), // (00 - 60 - leap second)
    TIME('T'), // (24 hour hh:mm:ss)
    ZONE_NUMERIC('z'), // (-1200 - +1200) - ls minus?
    ZONE('Z'), // (symbol)

    // Date
    NAME_OF_DAY_ABBREV('a'), // 'a'
    NAME_OF_DAY('A'), // 'A'
    NAME_OF_MONTH_ABBREV('b'), // 'b'
    NAME_OF_MONTH('B'), // 'B'
    CENTURY('C'), // (00 - 99)
    DAY_OF_MONTH_0('d'), // (01 - 31)
    DAY_OF_MONTH('e'), // (1 - 31) -- like d

    NAME_OF_MONTH_ABBREV_X('h'), // -- same b
    DAY_OF_YEAR('j'), // (001 - 366)
    MONTH('m'), // (01 - 12)

    YEAR_2('y'), // (00 - 99)
    YEAR_4('Y'), // (0000 - 9999)

    // Composites
    TIME_12_HOUR('r'), // (hh:mm:ss [AP]M)
    TIME_24_HOUR('R'), // (hh:mm same as %H:%M)

    DATE_TIME('c'),

    // (Sat Nov 04 12:02:33 EST 1999)
    DATE('D'), // (mm/dd/yy)
    ISO_STANDARD_DATE('F'), ; // (%Y-%m-%d)

    companion object {
        internal fun parse(c: Char): DateTime = DateTime.values().find { it.char == c } ?: throw StringFormatterException.UnknownFormatConversionException("t$c")
    }
}
