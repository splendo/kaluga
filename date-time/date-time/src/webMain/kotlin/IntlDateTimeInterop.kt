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

package com.splendo.kaluga.datetime

// Typed `Intl.DateTimeFormat` / JS `Date` interop shared by the JS family (js + wasmJs). No `dynamic`:
// every `js(...)` is the sole body of a top-level function so it compiles for both web targets.

/** A JavaScript [`Date`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date). */
internal external interface JsDate

/** A handle to an [`Intl.DateTimeFormat`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/DateTimeFormat). */
internal external interface IntlDateTimeFormat {
    fun format(date: JsDate): String
}

/** The subset of `Intl.DateTimeFormat` options Kaluga sets; all optional. */
internal external interface DateTimeFormatOptions {
    var timeZone: String?
    var timeZoneName: String?
    var dateStyle: String?
    var timeStyle: String?
    var era: String?
    var year: String?
    var month: String?
    var weekday: String?
    var day: String?
    var hour: String?
    var hour12: Boolean?
}

internal fun emptyDateTimeFormatOptions(): DateTimeFormatOptions = js("({})")

internal fun createDateTimeFormat(tag: String, options: DateTimeFormatOptions): IntlDateTimeFormat = js("new Intl.DateTimeFormat(tag, options)")

internal fun jsDate(milliseconds: Double): JsDate = js("new Date(milliseconds)")

internal fun jsDateUTC(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, millisecond: Int = 0): JsDate =
    js("new Date(Date.UTC(year, month, day, hour, minute, second, millisecond))")

internal fun jsDateNow(): Double = js("Date.now()")

// `type` and `value` joined by U+0002, entries by U+0001, so the parts marshal as one String — JS
// arrays have no shared js/wasm representation and must not cross the Kotlin boundary.
private val partSeparator = Char(1)
private val entrySeparator = Char(2)

private fun dateTimeFormatPartsEncoded(formatter: IntlDateTimeFormat, date: JsDate): String =
    js("formatter.formatToParts(date).map(function(p){ return p.type + '\\u0002' + p.value; }).join('\\u0001')")

internal data class DateTimeFormatPart(val type: String, val value: String)

internal fun dateTimeFormatParts(formatter: IntlDateTimeFormat, date: JsDate): List<DateTimeFormatPart> {
    val encoded = dateTimeFormatPartsEncoded(formatter, date)
    if (encoded.isEmpty()) return emptyList()
    return encoded.split(partSeparator).map { entry ->
        val separator = entry.indexOf(entrySeparator)
        if (separator < 0) DateTimeFormatPart(entry, "") else DateTimeFormatPart(entry.substring(0, separator), entry.substring(separator + 1))
    }
}

internal fun firstPartValue(parts: List<DateTimeFormatPart>, type: String): String? = parts.firstOrNull { it.type == type }?.value
