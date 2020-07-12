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

package com.splendo.kaluga.resources.strings

import com.splendo.kaluga.resources.Locale
import com.splendo.kaluga.resources.NumberFormatter
import com.splendo.kaluga.resources.defaultLocale
import com.splendo.kaluga.resources.strings.FormatSpecifier.Companion.formatSpecifier

@ExperimentalStdlibApi
fun String.format(vararg args: Any?, locale: Locale = defaultLocale) = StringFormatter(fmtLocale = locale).format(this, *args).toString()

interface Formattable {
    @ExperimentalStdlibApi
    fun formatTo(formatter: StringFormatter, flags: Int, width: Int, precision: Int)
}

@ExperimentalStdlibApi
class StringFormatter(private val out: StringBuilder = StringBuilder(), private val fmtLocale: Locale = defaultLocale) {

    companion object {
        fun getZero(locale: Locale): Char = NumberFormatter(locale).zeroSymbol
        fun checkText(s: String, start: Int, end: Int) {
            val index = s.substring(start, end).indexOf('%')
            if (index >= 0) {
                throw StringFormatterException.UnknownFormatConversionException((if (index == end - 1) '%' else s[index + 1]).toString())
            }
        }
    }

    var lastException: StringFormatterException? = null
        private set

    fun format(format: String, vararg args: Any?): StringBuilder {
        format(fmtLocale, format, *args)
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
                result.add(FormatSpecifier(out, s, matchResult))
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
