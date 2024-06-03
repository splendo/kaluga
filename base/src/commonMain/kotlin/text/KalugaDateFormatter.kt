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
import com.splendo.kaluga.base.utils.KalugaLocale.Companion.defaultLocale
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.enUsPosix

/**
 * Style used for formatting a [KalugaDate] to and from a [String]
 */
enum class DateFormatStyle {
    /**
     * Short style pattern
     */
    Short,

    /**
     * Medium style pattern
     */
    Medium,

    /**
     * Long style pattern
     */
    Long,

    /**
     * Full style pattern
     */
    Full,
}

/**
 * Interface for parsing and formatting a [KalugaDate] from/to a [String].
 */
interface BaseDateFormatter {

    var pattern: String

    /**
     * The [KalugaTimeZone] this [KalugaDateFormatter] formats its dates to.
     */
    var timeZone: KalugaTimeZone

    /**
     * A list containing the names of all eras used by this date formatter.
     */
    var eras: List<String>

    /**
     * A list containing the names of all months used by this date formatter.
     */
    var months: List<String>

    /**
     * A list containing the shortened names of all months used by this date formatter.
     */
    var shortMonths: List<String>

    /**
     * A list containing the names of all weekdays used by this date formatter.
     */
    var weekdays: List<String>

    /**
     * A list containing the shortened names of all weekdays used by this date formatter.
     */
    var shortWeekdays: List<String>

    /**
     * The name used to describe A.M. when using a twelve hour clock.
     */
    var amString: String

    /**
     * The name used to describe P.M. when using a twelve hour clock.
     */
    var pmString: String

    /**
     * Formats a given [KalugaDate] to a [String] using the format described by this [KalugaDateFormatter].
     * @param date The [KalugaDate] to format.
     * @return The formatted [KalugaDate] as a [String]
     */
    fun format(date: KalugaDate): String

    /**
     * Attempts to parse a given [String] into a [KalugaDate] using the format described.
     * @param string The [String] to parse.
     * @return A [KalugaDate] matching the format described by [string] or `null` if no such match could be made.
     */
    fun parse(string: String): KalugaDate?
}

/**
 * Default implementation of [BaseDateFormatter]
 */
expect class KalugaDateFormatter : BaseDateFormatter {

    companion object {
        /**
         * Creates a [KalugaDateFormatter] that only formats the date components of a [KalugaDate]
         * @param style The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        fun dateFormat(
            style: DateFormatStyle = DateFormatStyle.Medium,
            timeZone: KalugaTimeZone = KalugaTimeZone.current(),
            locale: KalugaLocale = defaultLocale,
        ): KalugaDateFormatter

        /**
         * Creates a [KalugaDateFormatter] that only formats the time components of a [KalugaDate]
         * @param style The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        fun timeFormat(
            style: DateFormatStyle = DateFormatStyle.Medium,
            timeZone: KalugaTimeZone = KalugaTimeZone.current(),
            locale: KalugaLocale = defaultLocale,
        ): KalugaDateFormatter

        /**
         * Creates a [KalugaDateFormatter] that formats both date and time components of a [KalugaDate]
         * @param dateStyle The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeStyle The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        fun dateTimeFormat(
            dateStyle: DateFormatStyle = DateFormatStyle.Medium,
            timeStyle: DateFormatStyle = DateFormatStyle.Medium,
            timeZone: KalugaTimeZone = KalugaTimeZone.current(),
            locale: KalugaLocale = defaultLocale,
        ): KalugaDateFormatter

        /**
         * Creates a [KalugaDateFormatter] using a custom Date format pattern.
         * On iOS some user settings may take precedent over the format (i.e. using 12 hour clock).
         * To prevent this, ensure that the provided [locale] is of a `POSIX` type.
         * A convenience [fixedPatternFormat] method exists to default to this behaviour.
         * @param pattern The pattern to apply.
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        fun patternFormat(pattern: String, timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: KalugaLocale = defaultLocale): KalugaDateFormatter
    }

    override var pattern: String
    override var timeZone: KalugaTimeZone
    override var eras: List<String>
    override var months: List<String>
    override var shortMonths: List<String>
    override var weekdays: List<String>
    override var shortWeekdays: List<String>
    override var amString: String
    override var pmString: String

    override fun format(date: KalugaDate): String
    override fun parse(string: String): KalugaDate?
}

@Deprecated(
    "Due to name clashes with platform classes and API changes this class has been renamed and changed to an interface. It will be removed in a future release.",
    ReplaceWith("KalugaState"),
)
typealias DateFormatter = KalugaDateFormatter

/**
 * Creates a fixed [KalugaDateFormatter] using a custom Date format pattern, localized by the [KalugaLocale.enUsPosix] [KalugaLocale].
 * Use this to ensure that displaying time in 12 or 24 hour format is not overridden by the user.
 * @param pattern The pattern to apply.
 * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
 */
fun KalugaDateFormatter.Companion.fixedPatternFormat(pattern: String, timeZone: KalugaTimeZone = KalugaTimeZone.current()) =
    patternFormat(pattern, timeZone, KalugaLocale.enUsPosix)

/**
 * Creates a [KalugaDateFormatter] that formats time according to the ISo 8601 format.
 * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
 */
fun KalugaDateFormatter.Companion.iso8601Pattern(timeZone: KalugaTimeZone = KalugaTimeZone.current()) = fixedPatternFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", timeZone)

/**
 * Creates a [KalugaDateFormatter] that only formats the date components of a [KalugaDate]
 * @param style The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
 * @param excludeYear When [true] the year will not be part of the format.
 * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
 * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
 */
fun KalugaDateFormatter.Companion.dateFormat(
    style: DateFormatStyle = DateFormatStyle.Medium,
    excludeYear: Boolean,
    timeZone: KalugaTimeZone = KalugaTimeZone.current(),
    locale: KalugaLocale = defaultLocale,
): KalugaDateFormatter {
    val formatWithYear = dateFormat(style, timeZone, locale)
    return if (excludeYear) {
        patternFormat(patternWithoutYear(formatWithYear.pattern), timeZone, locale)
    } else {
        formatWithYear
    }
}

/**
 * Creates a [KalugaDateFormatter] that formats both date and time components of a [KalugaDate]
 * @param dateStyle The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
 * @param excludeYear When [true] the year will not be part of the format.
 * @param timeStyle The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
 * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
 * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
 */
fun KalugaDateFormatter.Companion.dateTimeFormat(
    dateStyle: DateFormatStyle = DateFormatStyle.Medium,
    excludeYear: Boolean,
    timeStyle: DateFormatStyle = DateFormatStyle.Medium,
    timeZone: KalugaTimeZone = KalugaTimeZone.current(),
    locale: KalugaLocale = defaultLocale,
): KalugaDateFormatter {
    val formatWithYear = dateTimeFormat(dateStyle, timeStyle, timeZone, locale)
    return if (excludeYear) {
        val datePatternWithYear = dateFormat(dateStyle, timeZone, locale).pattern
        val datePatternWithoutYear = patternWithoutYear(datePatternWithYear)
        patternFormat(formatWithYear.pattern.replace(datePatternWithYear, datePatternWithoutYear), timeZone, locale)
    } else {
        formatWithYear
    }
}

internal fun KalugaDateFormatter.Companion.patternWithoutYear(pattern: String): String = pattern.replace("\\W*[Yy]+\\W*".toRegex(), "")
