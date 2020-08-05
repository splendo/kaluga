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

import com.splendo.kaluga.base.text.FormatSpecifier.Companion.formatSpecifier
import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.Locale.Companion.defaultLocale

/**
 * Formats this [String] as a printf-style format String using a variable number of arguments.
 * @param args The list of arguments used for formatting the string.
 * @param locale The [Locale] used for formatting the arguments. This is relevant for number and date formatting, as well as capitalization.
 * @return The formatted [String]
 * @throws [StringFormatterException] if the string could not be formatted using [args].
 */
@ExperimentalStdlibApi
fun String.format(vararg args: Any?, locale: Locale = defaultLocale) = StringFormatter(locale = locale).format(this, *args).toString()

/**
 * Interface that adds support for an object to be formatted by [StringFormatter].
 */
@ExperimentalStdlibApi
interface Formattable {
    /**
     * Formats this object to a [String] using a [Locale], [Flags], width, and precision
     * @param locale The [Locale] used for formatting.
     * @param flags A [Set] of [Flag] used for formattting.
     * @param width The expected width of the result.
     * @param precision The precision applied when formatting.
     * @return The formatted [String] representing this [Formattable].
     * @throws [StringFormatterException] when an issue with the formatting occurs.
     */
    fun formatFor(locale: Locale, flags: Set<Flag>, width: Int, precision: Int): String
}

/**
 * Formats a given [String] using a printf-style format strings.
 * Supports formats for [Number], [String], [Char], [Boolean], and [com.splendo.kaluga.base.utils.Date].
 * Custom formatting is supported by implementing [Formattable].
 * Flags, precision and width are supported by this formatter as well.
 * Formatting will adjust for a provided [Locale].
 * May throw an [StringFormatterException] if the incorrect format is applied.
 * @param out he [StringBuilder] used for outputting the result.
 * @param locale The [Locale] used for formatting. This is relevant for number and date formatting, as well as capitalization.
 */
@ExperimentalStdlibApi
class StringFormatter(private val out: StringBuilder = StringBuilder(), private val locale: Locale = defaultLocale) {

    companion object {
        internal fun getZero(locale: Locale): Char = NumberFormatter(locale).zeroSymbol
        internal fun checkText(s: String, start: Int, end: Int) {
            val index = s.substring(start, end).indexOf('%')
            if (index >= 0) {
                throw StringFormatterException.UnknownFormatConversionException((if (index == end - 1) '%' else s[index + 1]).toString())
            }
        }
    }

    /**
     * Formats a [String] using a printf-style format String and a variable number of arguments.
     * @param format The printf-style format [String]
     * @param args The list of arguments used for formatting the string.
     * @return The [StringBuilder] containing the formatted [String].
     * @throws [StringFormatterException] if [format] could not be formatted using [args].
     */
    fun format(format: String, vararg args: Any?): StringBuilder {
        format(locale, format, *args)
        return out
    }

    private fun format(locale: Locale, format: String, vararg args: Any?): StringFormatter {
        var last = -1
        var lastOrdinaryIndex = -1
        val formatStrings = parse(format)
        formatStrings.forEach { formatString ->
            when (val index = formatString.index) {
                -2 -> formatString.print(null, locale)
                -1 -> {
                    if (last < 0 || last > args.size - 1) {
                        throw StringFormatterException.MissingFormatArgumentException(formatString.toString())
                    }
                    formatString.print(args.getOrNull(last), locale)
                }
                0 -> {
                    lastOrdinaryIndex++
                    last = lastOrdinaryIndex
                    if (lastOrdinaryIndex > args.size - 1) {
                        throw StringFormatterException.MissingFormatArgumentException(formatString.toString())
                    }
                    formatString.print(args.getOrNull(lastOrdinaryIndex), locale)
                }
                else -> {
                    last = index - 1
                    if (last > args.size - 1) {
                        throw StringFormatterException.MissingFormatArgumentException(formatString.toString())
                    }
                    formatString.print(args.getOrNull(last), locale)
                }
            }
        }
        return this
    }

    private fun parse(s: String): List<FormatString> {
        val result = mutableListOf<FormatString>()
        var index = 0
        while (index < s.length) {
            formatSpecifier.find(s, index)?.let { matchResult ->
                if (matchResult.range.first != index) {
                    checkText(s, index, matchResult.range.first)
                    result.add(FixedString(out, s, index, matchResult.range.first))
                }
                result.add(FormatSpecifier(out, matchResult))
                index = matchResult.range.last + 1
            } ?: run {
                checkText(s, index, s.length)
                result.add(FixedString(out, s, index, s.length))
                index = s.length
            }
        }
        return result
    }
}
