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

import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.KalugaTimeZone

// TODO Implement with proper dateformatter solution for Java Script
/**
 * Default implementation of [BaseDateFormatter]
 */
actual class KalugaDateFormatter private constructor(initialTimeZone: KalugaTimeZone, private val formatter: (kotlin.js.Date) -> String) : BaseDateFormatter {

    actual companion object {

        /**
         * Creates a [KalugaDateFormatter] that only formats the date components of a [KalugaDate]
         * @param style The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        actual fun dateFormat(style: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            KalugaDateFormatter(timeZone) { date -> date.toLocaleDateString(arrayOf("${locale.languageCode}-${locale.countryCode}")) }

        /**
         * Creates a [KalugaDateFormatter] that only formats the time components of a [KalugaDate]
         * @param style The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        actual fun timeFormat(style: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            KalugaDateFormatter(timeZone) { date -> date.toLocaleTimeString(arrayOf("${locale.languageCode}-${locale.countryCode}")) }

        /**
         * Creates a [KalugaDateFormatter] that formats both date and time components of a [KalugaDate]
         * @param dateStyle The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeStyle The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        actual fun dateTimeFormat(dateStyle: DateFormatStyle, timeStyle: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            KalugaDateFormatter(timeZone) { date -> date.toLocaleString(arrayOf("${locale.languageCode}-${locale.countryCode}")) }

        /**
         * Creates a [KalugaDateFormatter] using a custom Date format pattern.
         * On iOS some user settings may take precedent over the format (i.e. using 12 hour clock).
         * To prevent this, ensure that the provided [locale] is of a `POSIX` type.
         * A convenience [fixedPatternFormat] method exists to default to this behaviour.
         * @param pattern The pattern to apply.
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        actual fun patternFormat(pattern: String, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            KalugaDateFormatter(timeZone) { date -> date.toLocaleString(arrayOf("${locale.languageCode}-${locale.countryCode}")) }
    }

    override var pattern: String = ""

    override var timeZone: KalugaTimeZone = initialTimeZone
    override var eras: List<String> = emptyList()

    override var months: List<String> = emptyList()
    override var shortMonths: List<String> = emptyList()

    override var weekdays: List<String> = emptyList()
    override var shortWeekdays: List<String> = emptyList()

    override var amString: String = ""
    override var pmString: String = ""

    override fun format(date: KalugaDate): String = formatter(date.date)
    override fun parse(string: String): KalugaDate? = null
}

private fun DateFormatStyle.stringValue(): String = when (this) {
    DateFormatStyle.Short -> "short"
    DateFormatStyle.Medium -> "medium"
    DateFormatStyle.Long -> "long"
    DateFormatStyle.Full -> "full"
}
