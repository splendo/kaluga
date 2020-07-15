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

import com.splendo.kaluga.base.text.Conversion.BOOLEAN
import com.splendo.kaluga.base.text.Conversion.CHARACTER
import com.splendo.kaluga.base.text.Conversion.CHARACTER_UPPER
import com.splendo.kaluga.base.text.Conversion.DECIMAL_FLOAT
import com.splendo.kaluga.base.text.Conversion.DECIMAL_INTEGER
import com.splendo.kaluga.base.text.Conversion.GENERAL
import com.splendo.kaluga.base.text.Conversion.HASHCODE
import com.splendo.kaluga.base.text.Conversion.HEXADECIMAL_FLOAT
import com.splendo.kaluga.base.text.Conversion.HEXADECIMAL_INTEGER
import com.splendo.kaluga.base.text.Conversion.LINE_SEPARATOR
import com.splendo.kaluga.base.text.Conversion.OCTAL_INTEGER
import com.splendo.kaluga.base.text.Conversion.PERCENT_SIGN
import com.splendo.kaluga.base.text.Conversion.SCIENTIFIC
import com.splendo.kaluga.base.text.Conversion.STRING
import com.splendo.kaluga.base.text.Conversion.STRING_IOS
import com.splendo.kaluga.base.text.DateTime.isValid
import com.splendo.kaluga.base.text.Flags.Companion.ALTERNATE
import com.splendo.kaluga.base.text.Flags.Companion.GROUP
import com.splendo.kaluga.base.text.Flags.Companion.LEADING_SPACE
import com.splendo.kaluga.base.text.Flags.Companion.LEFT_JUSTIFY
import com.splendo.kaluga.base.text.Flags.Companion.NONE
import com.splendo.kaluga.base.text.Flags.Companion.PARENTHESES
import com.splendo.kaluga.base.text.Flags.Companion.PLUS
import com.splendo.kaluga.base.text.Flags.Companion.PREVIOUS
import com.splendo.kaluga.base.text.Flags.Companion.UPPERCASE
import com.splendo.kaluga.base.text.Flags.Companion.ZERO_PAD
import com.splendo.kaluga.base.text.StringFormatter.Companion.getZero
import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.TimeZone
import com.splendo.kaluga.base.utils.TimeZoneNameStyle
import com.splendo.kaluga.base.utils.toHexString
import kotlin.math.abs
import kotlin.math.absoluteValue

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
internal class FormatSpecifier(private val out: StringBuilder, matchResult: MatchResult) : FormatString {

    companion object {
        val formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z@%])".toRegex()
    }
    override var index: Int = -1
        private set
    private var flags: Flags = NONE
    private var width: Int = 0
    private var precision: Int = 0
    private var currentChar: Char = Char.MIN_VALUE
    private var dt = false

    init {
        index(matchResult.groupValues[1])
        flags(matchResult.groupValues[2])
        width(matchResult.groupValues[3])
        precision(matchResult.groupValues[4])

        val timeStart = matchResult.groupValues[5]
        if (timeStart.isNotEmpty()) {
            dt = true
            if (timeStart[0] == 'T')
                flags.add(UPPERCASE)
        }

        conversion(matchResult.groupValues[6][0])

        if (dt)
            checkDateTime()
        else if (Conversion.isGeneral(currentChar))
            checkGeneral()
        else if (Conversion.isCharacter(currentChar))
            checkCharacter()
        else if (Conversion.isInteger(currentChar))
            checkInteger()
        else if (Conversion.isFloat(currentChar))
            checkFloat()
        else if (Conversion.isText(currentChar))
            checkText()
        else
            throw StringFormatterException.UnknownFormatConversionException(currentChar.toString())
    }

    override fun print(arg: Any?, locale: Locale) {
        if (dt) {
            printDateTime(arg, locale)
            return
        }

        when (currentChar) {
            DECIMAL_INTEGER,
            OCTAL_INTEGER,
            HEXADECIMAL_INTEGER -> printInteger(arg, locale)
            SCIENTIFIC,
            GENERAL,
            DECIMAL_FLOAT,
            HEXADECIMAL_FLOAT -> printFloat(arg, locale)
            CHARACTER,
            CHARACTER_UPPER -> printCharacter(arg, locale)
            BOOLEAN -> printBoolean(arg, locale)
            STRING,
            STRING_IOS -> printString(arg, locale)
            HASHCODE -> printHashCode(arg, locale)
            LINE_SEPARATOR -> out.append(lineSeparator)
            PERCENT_SIGN -> print("%", locale)
            else -> throw StringFormatterException.UnknownFormatConversionException(currentChar.toString())
        }
    }

    private fun printDateTime(arg: Any?, locale: Locale) {
        val date = when (arg) {
            null -> {
                print("null", locale)
                return
            }
            is Long,
            is Int,
            is Short -> {
                Date.epoch((arg as Number).toLong(), TimeZone.current(), locale)
            }
            is Date -> {
                arg.copy()
            }
            else -> throw StringFormatterException.IllegalFormatConversionException(currentChar, arg)
        }
        print(date, currentChar, locale)
    }

    private fun printInteger(arg: Any?, locale: Locale) {
        when (arg) {
            null -> print("null", locale)
            is Int -> print(arg, locale)
            is Short -> print(arg, locale)
            is Byte -> print(arg, locale)
            is Long -> print(arg, locale)
            else -> throw StringFormatterException.IllegalFormatConversionException(currentChar, arg)
        }
    }

    private fun printFloat(arg: Any?, locale: Locale) {
        when (arg) {
            null -> print("null", locale)
            is Float -> print(arg, locale)
            is Double -> print(arg, locale)
            else -> throw StringFormatterException.IllegalFormatConversionException(currentChar, arg)
        }
    }

    private fun printCharacter(arg: Any?, locale: Locale) {
        val stringToPrint = when (arg) {
            null -> "null"
            is Char -> arg.toString()
            is Byte -> arg.toChar().toString()
            is Short -> arg.toChar().toString()
            is Int -> arg.toChar().toString()
            else -> throw StringFormatterException.IllegalFormatConversionException(currentChar, arg)
        }
        print(stringToPrint, locale)
    }

    private fun printString(arg: Any?, locale: Locale) {
        (arg as? Formattable)?.let {
            it.formatTo(StringFormatter(out, locale), flags.valueOf(), width, precision)
        } ?: run {
            if (flags.contains(ALTERNATE))
                throw StringFormatterException.FormatFlagsConversionMismatchException(ALTERNATE.toString(), 's')

            print(arg?.toString() ?: "null", locale)
        }
    }

    private fun printHashCode(arg: Any?, locale: Locale) {
        print(arg?.hashCode()?.toByte()?.let { byteArrayOf(it) }?.toHexString() ?: "null", locale)
    }

    private fun printBoolean(arg: Any?, locale: Locale) {
        print((arg?.let {
            (arg as? Boolean) ?: true
        } ?: false).toString(), locale)
    }

    private fun print(s: String, locale: Locale) {
        var s = s
        if (precision != -1 && precision < s.length) s = s.substring(0, precision)
        if (flags.contains(UPPERCASE)) s = s.upperCased(locale)
        appendJustified(out, s)
    }

    private fun print(value: Byte, locale: Locale) {
        print(value.toLong(), locale)
    }

    private fun print(value: Short, locale: Locale) {
        print(value.toLong(), locale)
    }

    private fun print(value: Int, locale: Locale) {
        print(value.toLong(), locale)
    }

    private fun print(value: Long, locale: Locale) {
        val sb = StringBuilder()
        when (currentChar) {
            DECIMAL_INTEGER -> {
                val valueStr = value.toString(10)
                val neg = value < 0
                // leading sign indicator
                leadingSign(sb, neg)

                // the value
                localizedMagnitude(sb, valueStr, if (neg) 1 else 0, flags, adjustWidth(width, flags, neg), locale)

                // trailing sign indicator
                trailingSign(sb, neg)
            }
            OCTAL_INTEGER -> {
                checkBadFlags(PARENTHESES, LEADING_SPACE, PLUS)
                val valueString = value.toString(8)

                val alternate = flags.contains(ALTERNATE)
                val length = valueString.length + if (alternate) 1 else 0
                if (alternate)
                    sb.append(getZero(locale))
                if (flags.contains(ZERO_PAD))
                    trailingZeros(sb, width - length)
                sb.append(valueString)
            }
            HEXADECIMAL_INTEGER -> {
                checkBadFlags(PARENTHESES, LEADING_SPACE, PLUS)
                val hexValue = value.toString(16)
                val alternate = flags.contains(ALTERNATE)
                val uppercase = flags.contains(UPPERCASE)
                val length = hexValue.length + if (alternate) 2 else 0
                if (alternate) {
                    val prefix = "${getZero(locale)}x"
                    sb.append(if (uppercase) prefix.upperCased(locale) else prefix)
                }
                if (flags.contains(ZERO_PAD))
                    trailingZeros(sb, width - length)
                sb.append(if (uppercase) hexValue.upperCased(locale) else hexValue)
            }
            else -> throw StringFormatterException.UnexpectedChar(currentChar)
        }

        // justify based on width
        appendJustified(out, sb)
    }

    private fun print(value: Float, locale: Locale) {
        print(value.toDouble(), locale)
    }

    private fun print(value: Double, locale: Locale) {
        val sb = StringBuilder()
        val neg = value < 0.0
        val numberFormatter = NumberFormatter(locale)
        if (!value.isNaN()) {
            val v: Double = abs(value)

            // leading sign indicator
            leadingSign(sb, neg)

            // the value
            if (v.isFinite())
                print(sb, v, locale, currentChar, precision, neg)
            else {
                val infinitySymbol = numberFormatter.infinitySymbol
                sb.append(if (flags.contains(UPPERCASE)) infinitySymbol.upperCased(locale) else infinitySymbol)
            }

            // trailing sign indicator
            trailingSign(sb, neg)
        } else {
            val nanSymbol = numberFormatter.notANumberSymbol
            sb.append(if (flags.contains(UPPERCASE)) nanSymbol.upperCased(locale) else nanSymbol)
        }

        // justify based on width
        appendJustified(out, sb)
    }

    // !Double.isInfinite(value) && !Double.isNaN(value)
    private fun print(
        sb: StringBuilder,
        value: Double,
        locale: Locale,
        c: Char,
        precision: Int,
        neg: Boolean
    ) {
        when (c) {
            SCIENTIFIC -> {
                val prec = if (precision == -1) 6 else precision
                val number = StringBuilder()
                val formatter = NumberFormatter(locale, NumberFormatStyle.Scientific((prec + 1).toUInt(), 2U))
                val expSymbol = formatter.exponentSymbol
                val scientific = formatter.format(value).split(expSymbol, ignoreCase = true, limit = 2)
                val mantissa = scientific[0]
                val exponent = when {
                    value == 0.0 -> "+${formatter.zeroSymbol}${formatter.zeroSymbol}"
                    value.absoluteValue >= 1.0 -> "+${scientific[1]}"
                    else -> scientific[1]
                }
                number.append(mantissa)
                addZeros(number, prec)

                if (flags.contains(ALTERNATE) && prec == 0)
                    number.append(formatter.decimalSeparator)

                var newW = width
                if (width != -1) {
                    newW = adjustWidth(width - exponent.length - 1, flags, neg)
                }
                localizedMagnitude(sb, number, 0, flags, newW, locale)

                sb.append(if (flags.contains(UPPERCASE)) formatter.exponentSymbol.upperCased(locale) else formatter.exponentSymbol.lowerCased(locale))
                sb.append(exponent)
            }
            DECIMAL_FLOAT -> {
                val prec = if (precision == -1) 6 else precision
                val number = StringBuilder()
                val formatter = NumberFormatter(locale, NumberFormatStyle.Decimal(minFraction = prec.toUInt(), maxFraction = prec.toUInt())).apply {
                    usesGroupingSeparator = false
                }
                if (value >= 0.0 && value < 1.0)
                    number.append(formatter.zeroSymbol)
                number.append(formatter.format(value))
                addZeros(number, prec)

                if (flags.contains(ALTERNATE) && prec == 0)
                    number.append(formatter.decimalSeparator)
                var newW = width
                if (width != -1) newW = adjustWidth(width, flags, neg)

                localizedMagnitude(sb, number, 0, flags, newW, locale)
            }
            GENERAL -> {
                val scientificBuilder = StringBuilder()
                print(scientificBuilder, value, locale, SCIENTIFIC, precision, neg)
                val decimalBuilder = StringBuilder()
                print(decimalBuilder, value, locale, DECIMAL_FLOAT, precision, neg)
                val scientific = scientificBuilder.toString()
                val decimal = decimalBuilder.toString()
                if (decimal.length <= scientific.length)
                    sb.append(decimal)
                else
                    sb.append(scientific)
            }
            HEXADECIMAL_FLOAT -> {
                // TODO Support Hexadecimal floats
            }
        }
    }

    private fun print(time: Date, currentChar: Char, locale: Locale) {
        val sb = StringBuilder()
        print(sb, time, currentChar, locale)

        // justify based on width
        if (flags.contains(UPPERCASE)) {
            appendJustified(out, sb.toString().upperCased(locale))
        } else {
            appendJustified(out, sb)
        }
    }

    private fun print(sb: StringBuilder, time: Date, currentChar: Char, locale: Locale): StringBuilder {
        when (currentChar) {
            DateTime.HOUR_OF_DAY_0, DateTime.HOUR_0, DateTime.HOUR_OF_DAY, DateTime.HOUR -> {
                // 'l' (1 - 12) -- like I
                var i: Int = time.hour
                if (currentChar == DateTime.HOUR_0 || currentChar == DateTime.HOUR) i = if (i == 0 || i == 12) 12 else i % 12
                val flags = if (currentChar == DateTime.HOUR_OF_DAY_0 || currentChar == DateTime.HOUR_0)
                    ZERO_PAD
                else
                    NONE
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.MINUTE -> {
                // 'M' (00 - 59)
                val i: Int = time.minute
                val flags = ZERO_PAD
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.NANOSECOND -> {
                // 'N' (000000000 - 999999999)
                val i: Int = time.millisecond * 1000000
                val flags = ZERO_PAD
                sb.append(localizedMagnitude(value = i, flags = flags, width = 9, locale = locale))
            }
            DateTime.MILLISECOND -> {
                // 'L' (000 - 999)
                val i: Int = time.millisecond
                val flags = ZERO_PAD
                sb.append(localizedMagnitude(value = i, flags = flags, width = 3, locale = locale))
            }
            DateTime.MILLISECOND_SINCE_EPOCH -> {
                // 'Q' (0 - 99...?)
                val i: Long = time.millisecondSinceEpoch
                val flags = NONE
                sb.append(localizedMagnitude(value = i.toString(10), offset = 0, flags = flags, width = width, locale = locale))
            }
            DateTime.AM_PM -> {
                // 'p' (am or pm)
                val isAm = time.hour < 12
                val dateFormat = DateFormatter.patternFormat("aa", TimeZone.current(), locale)
                sb.append((if (isAm) dateFormat.amString else dateFormat.pmString).lowerCased(locale))
            }
            DateTime.SECONDS_SINCE_EPOCH -> {
                // 's' (0 - 99...?)
                val i: Long = time.millisecondSinceEpoch / 1000
                val flags = NONE
                sb.append(localizedMagnitude(value = i.toString(10), offset = 0, flags = flags, width = width, locale = locale))
            }
            DateTime.SECOND -> {
                // 'S' (00 - 60 - leap second)
                val i: Int = time.second
                val flags = ZERO_PAD
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.ZONE_NUMERIC -> {
                // 'z' ({-|+}####) - ls minus?
                var i: Long = time.timeZone.offsetFromGMTAtDateInMilliseconds(time)
                val neg = i < 0
                sb.append(if (neg) '-' else '+')
                if (neg) i = -i
                val min = i / 60000

                // combine minute and hour into a single integer
                val offset = min / 60 * 100 + min % 60
                val flags = ZERO_PAD
                sb.append(localizedMagnitude(value = offset.toString(10), offset = 0, flags = flags, width = 4, locale = locale))
            }
            DateTime.ZONE -> {
                // 'Z' (symbol)
                sb.append(time.timeZone.displayName(TimeZoneNameStyle.Short, time.timeZone.usesDaylightSavingsTime(time), locale))
            }
            DateTime.NAME_OF_DAY_ABBREV, DateTime.NAME_OF_DAY -> {
                // 'A'
                val i: Int = time.weekDay - 1
                val dateFormat = DateFormatter.patternFormat("EEEE")
                val weekdays = if (currentChar == DateTime.NAME_OF_DAY) dateFormat.weekdays else dateFormat.shortWeekdays
                sb.append(weekdays[i])
            }
            DateTime.NAME_OF_MONTH_ABBREV, DateTime.NAME_OF_MONTH_ABBREV_X, DateTime.NAME_OF_MONTH -> {
                // 'B'
                val i: Int = time.month - 1
                val dateFormat = DateFormatter.patternFormat("MMMM")
                val months = if (currentChar == DateTime.NAME_OF_MONTH) dateFormat.months else dateFormat.shortMonths
                sb.append(months[i])
            }
            DateTime.CENTURY, DateTime.YEAR_2, DateTime.YEAR_4 -> {
                // 'Y' (0000 - 9999)
                var i: Int = time.year
                var size = 2
                when (currentChar) {
                    DateTime.CENTURY -> i /= 100
                    DateTime.YEAR_2 -> i %= 100
                    DateTime.YEAR_4 -> size = 4
                }
                val flags = ZERO_PAD
                sb.append(localizedMagnitude(value = i, flags = flags, width = size, locale = locale))
            }
            DateTime.DAY_OF_MONTH_0, DateTime.DAY_OF_MONTH -> {
                // 'e' (1 - 31) -- like d
                val i: Int = time.day
                val flags = if (currentChar == DateTime.DAY_OF_MONTH_0) ZERO_PAD else NONE
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.DAY_OF_YEAR -> {
                // 'j' (001 - 366)
                val i: Int = time.dayOfYear
                val flags = ZERO_PAD
                sb.append(localizedMagnitude(value = i, flags = flags, width = 3, locale = locale))
            }
            DateTime.MONTH -> {
                // 'm' (01 - 12)
                val i: Int = time.month
                val flags = ZERO_PAD
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.TIME, DateTime.TIME_24_HOUR -> {
                // 'R' (hh:mm same as %H:%M)
                val sep = ':'
                print(sb, time, DateTime.HOUR_OF_DAY_0, locale).append(sep)
                print(sb, time, DateTime.MINUTE, locale)
                if (currentChar == DateTime.TIME) {
                    sb.append(sep)
                    print(sb, time, DateTime.SECOND, locale)
                }
            }
            DateTime.TIME_12_HOUR -> {
                // 'r' (hh:mm:ss [AP]M)
                val sep = ':'
                print(sb, time, DateTime.HOUR_0, locale).append(sep)
                print(sb, time, DateTime.MINUTE, locale).append(sep)
                print(sb, time, DateTime.SECOND, locale).append(' ')

                // this may be in wrong place for some locales
                val tsb = StringBuilder()
                print(tsb, time, DateTime.AM_PM, locale)
                sb.append(tsb.toString().upperCased(locale))
            }
            DateTime.DATE_TIME -> {
                // 'c' (Sat Nov 04 12:02:33 EST 1999)
                val sep = ' '
                print(sb, time, DateTime.NAME_OF_DAY_ABBREV, locale).append(sep)
                print(sb, time, DateTime.NAME_OF_MONTH_ABBREV, locale).append(sep)
                print(sb, time, DateTime.DAY_OF_MONTH_0, locale).append(sep)
                print(sb, time, DateTime.TIME, locale).append(sep)
                print(sb, time, DateTime.ZONE, locale).append(sep)
                print(sb, time, DateTime.YEAR_4, locale)
            }
            DateTime.DATE -> {
                // 'D' (mm/dd/yy)
                val sep = '/'
                print(sb, time, DateTime.MONTH, locale).append(sep)
                print(sb, time, DateTime.DAY_OF_MONTH_0, locale).append(sep)
                print(sb, time, DateTime.YEAR_2, locale)
            }
            DateTime.ISO_STANDARD_DATE -> {
                // 'F' (%Y-%m-%d)
                val sep = '-'
                print(sb, time, DateTime.YEAR_4, locale).append(sep)
                print(sb, time, DateTime.MONTH, locale).append(sep)
                print(sb, time, DateTime.DAY_OF_MONTH_0, locale)
            }
            else -> throw StringFormatterException.MalformedValue(currentChar)
        }
        return sb
    }

    private fun addZeros(sb: StringBuilder, prec: Int) {

        // Look for the dot.  If we don't find one, the we'll need to add

        // it before we add the zeros.
        val len: Int = sb.length
        var i: Int
        i = 0
        while (i < len) {
            if (sb.get(i) == '.') {
                break
            }
            i++
        }
        var needDot = false
        if (i == len) {
            needDot = true
        }

        // Determine existing precision.
        val outPrec = len - i - if (needDot) 0 else 1
        if (outPrec == prec) {
            return
        }

        // Add dot if previously determined to be necessary.
        if (needDot) {
            sb.append('.')
        }

        // Add zeros.
        trailingZeros(sb, prec - outPrec)
    }

    private fun appendJustified(out: StringBuilder, cs: CharSequence): StringBuilder {
        if (width == -1) {
            return out.append(cs)
        }
        val padRight: Boolean = flags.contains(LEFT_JUSTIFY)
        val sp = width - cs.length
        if (padRight) {
            out.append(cs)
        }
        for (i in 0 until sp) {
            out.append(' ')
        }
        if (!padRight) {
            out.append(cs)
        }
        return out
    }

    // neg := val < 0
    private fun leadingSign(sb: StringBuilder, neg: Boolean): StringBuilder {
        if (!neg) {
            if (flags.contains(PLUS)) {
                sb.append('+')
            } else if (flags.contains(LEADING_SPACE)) {
                sb.append(' ')
            }
        } else {
            if (flags.contains(PARENTHESES)) sb.append('(') else sb.append('-')
        }
        return sb
    }

    // neg := val < 0
    private fun trailingSign(sb: StringBuilder, neg: Boolean): StringBuilder {
        if (neg && flags.contains(PARENTHESES)) sb.append(')')
        return sb
    }

    private fun localizedMagnitude(
        sb: StringBuilder = StringBuilder(),
        value: Int,
        flags: Flags,
        width: Int,
        locale: Locale
    ): StringBuilder {
        return localizedMagnitude(sb, value.toString(10), 0, flags, width, locale)
    }

    private fun localizedMagnitude(
        sb: StringBuilder = StringBuilder(),
        value: CharSequence,
        offset: Int,
        flags: Flags,
        width: Int,
        locale: Locale
    ): StringBuilder {
        val begin: Int = sb.length
        val zero: Char = getZero(locale)

        // determine localized grouping separator and size
        var grpSep = '\u0000'
        var grpSize = -1
        var decSep = '\u0000'
        val len = value.length
        var dot = len
        for (j in offset until len) {
            if (value[j] == '.') {
                dot = j
                break
            }
        }
        val numberFormatter = NumberFormatter(locale)
        if (dot < len) {
            decSep = numberFormatter.decimalSeparator
        }
        if (flags.contains(GROUP)) {
            grpSep = numberFormatter.groupingSeparator
            grpSize = numberFormatter.groupingSize

            // Some locales do not use grouping (the number
            // pattern for these locales does not contain group, e.g.
            // ("#0.###")), but specify a grouping separator.
            // To avoid unnecessary identification of the position of
            // grouping separator, reset its value with null character
            if (!numberFormatter.usesGroupingSeparator || grpSize == 0) {
                grpSep = '\u0000'
            }
        }

        // localize the digits inserting group separators as necessary
        for (j in offset until len) {
            if (j == dot) {
                sb.append(decSep)

                // no more group separators after the decimal separator
                grpSep = '\u0000'
                continue
            }
            val c = value[j]
            sb.append((c - '0' + zero.toInt()).toChar())
            if (grpSep != '\u0000' && j != dot - 1 && (dot - j) % grpSize == 1) {
                sb.append(grpSep)
            }
        }

        // apply zero padding
        if (width != -1 && flags.contains(ZERO_PAD)) {
            for (k in sb.length until width) {
                sb.insert(begin, zero)
            }
        }
        return sb
    }

    private fun adjustWidth(width: Int, flags: Flags, neg: Boolean): Int {
        var newW = width
        if (newW != -1 && neg && flags.contains(PARENTHESES)) newW--
        return newW
    }

    // Add trailing zeros

    // Add trailing zeros
    private fun trailingZeros(sb: StringBuilder, nzeros: Int) {
        for (i in 0 until nzeros) {
            sb.append('0')
        }
    }

    private fun index(stringToMatch: String): Int {
        index = if (stringToMatch.isNotEmpty()) {
            try {
                // skip the trailing '$'
                stringToMatch.substring(0, stringToMatch.length - 1).toInt(10)
            } catch (x: NumberFormatException) {
                throw StringFormatterException.NumberFormatException(x)
            }
        } else {
            0
        }
        return index
    }

    private fun flags(stringToMatch: String): Flags {
        flags = Flags.parse(stringToMatch)
        if (flags.contains(PREVIOUS)) index = -1
        return flags
    }

    private fun checkBadFlags(vararg badFlags: Flags) {
        for (badFlag in badFlags)
            if (flags.contains(badFlag))
                throw StringFormatterException.FormatFlagsConversionMismatchException(badFlag.toString(), currentChar)
    }

    private fun width(stringToMatch: String): Int {
        width = -1
        if (stringToMatch.isNotEmpty()) {
            try {
                width = stringToMatch.toInt(10)
                if (width < 0) throw StringFormatterException.IllegalFormatWidthException(width)
            } catch (x: NumberFormatException) {
                throw StringFormatterException.NumberFormatException(x)
            }
        }
        return width
    }

    private fun precision(stringToMatch: String): Int {
        precision = -1
        if (stringToMatch.isNotEmpty()) {
            try {
                // skip the leading '.'
                precision = stringToMatch.substring(1, stringToMatch.length).toInt(10)
                if (precision < 0) throw StringFormatterException.IllegalFormatPrecisionException(precision)
            } catch (x: NumberFormatException) {
                throw StringFormatterException.NumberFormatException(x)
            }
        }
        return precision
    }

    private fun conversion(conv: Char): Char {
        currentChar = conv
        if (!dt) {
            if (!Conversion.isValid(currentChar)) {
                throw StringFormatterException.UnknownFormatConversionException(currentChar.toString())
            }
            if (currentChar.toUpperCase() == currentChar && currentChar.toLowerCase() != currentChar) {
                flags.add(UPPERCASE)
                currentChar = currentChar.toLowerCase()
            }
            if (Conversion.isText(currentChar)) {
                index = -2
            }
        }
        return currentChar
    }

    private fun checkGeneral() {
        if ((currentChar == BOOLEAN || currentChar == HASHCODE) && flags.contains(ALTERNATE))
            throw StringFormatterException.FormatFlagsConversionMismatchException(ALTERNATE.toString(), currentChar)

        // '-' requires a width
        if (width == -1 && flags.contains(LEFT_JUSTIFY)) throw StringFormatterException.MissingFormatWidthException(toString())
        checkBadFlags(
            PLUS, LEADING_SPACE, ZERO_PAD,
            GROUP, PARENTHESES
        )
    }

    private fun checkDateTime() {
        if (precision != -1) throw StringFormatterException.IllegalFormatPrecisionException(precision)
        if (!isValid(currentChar)) throw StringFormatterException.UnknownFormatConversionException("t$currentChar")
        checkBadFlags(
            ALTERNATE, PLUS, LEADING_SPACE,
            ZERO_PAD, GROUP, PARENTHESES
        )

        // '-' requires a width
        if (width == -1 && flags.contains(LEFT_JUSTIFY)) throw StringFormatterException.MissingFormatWidthException(toString())
    }

    private fun checkCharacter() {
        if (precision != -1) throw StringFormatterException.IllegalFormatPrecisionException(precision)
        checkBadFlags(
            ALTERNATE, PLUS, LEADING_SPACE,
            ZERO_PAD, GROUP, PARENTHESES
        )

        // '-' requires a width
        if (width == -1 && flags.contains(LEFT_JUSTIFY)) throw StringFormatterException.MissingFormatWidthException(toString())
    }

    private fun checkInteger() {
        checkNumeric()
        if (precision != -1) throw StringFormatterException.IllegalFormatPrecisionException(precision)
        if (currentChar == DECIMAL_INTEGER) checkBadFlags(ALTERNATE) else if (currentChar === OCTAL_INTEGER) checkBadFlags(GROUP) else checkBadFlags(GROUP)
    }

    private fun checkFloat() {
        checkNumeric()
        when (currentChar) {
            DECIMAL_FLOAT -> {}
            HEXADECIMAL_FLOAT -> {
                checkBadFlags(PARENTHESES, GROUP)
            }
            SCIENTIFIC -> {
                checkBadFlags(GROUP)
            }
            GENERAL -> {
                checkBadFlags(ALTERNATE)
            }
        }
    }

    private fun checkNumeric() {
        if (width != -1 && width < 0) throw StringFormatterException.IllegalFormatWidthException(width)
        if (precision != -1 && precision < 0) throw StringFormatterException.IllegalFormatPrecisionException(precision)

        // '-' and '0' require a width
        if (width == -1 && (flags.contains(LEFT_JUSTIFY) || flags.contains(ZERO_PAD)))
            throw StringFormatterException.MissingFormatWidthException(toString())

        // bad combination
        if (flags.contains(PLUS) && flags.contains(LEADING_SPACE) || flags.contains(LEFT_JUSTIFY) && flags.contains(ZERO_PAD))
            throw StringFormatterException.IllegalFormatFlagsException(flags.toString())
    }

    private fun checkText() {
        if (precision != -1) throw StringFormatterException.IllegalFormatPrecisionException(precision)
        when (currentChar) {
            PERCENT_SIGN -> {
                if (flags.valueOf() !== LEFT_JUSTIFY.valueOf() && flags.valueOf() !== NONE.valueOf())
                    throw StringFormatterException.IllegalFormatFlagsException(flags.toString())

                // '-' requires a width
                if (width == -1 && flags.contains(LEFT_JUSTIFY)) throw StringFormatterException.MissingFormatWidthException(toString())
            }
            LINE_SEPARATOR -> {
                if (width != -1) throw StringFormatterException.IllegalFormatWidthException(width)
                if (flags.valueOf() !== NONE.valueOf()) throw StringFormatterException.IllegalFormatFlagsException(flags.toString())
            }
            else -> throw StringFormatterException.UnexpectedChar(currentChar)
        }
    }
}
