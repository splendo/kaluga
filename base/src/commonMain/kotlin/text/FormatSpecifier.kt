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

import com.splendo.kaluga.base.text.StringFormatter.Companion.getZero
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.TimeZoneNameStyle
import com.splendo.kaluga.base.utils.toHexString
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.milliseconds

internal sealed class ParsingCharacter(val char: Char) {
    data class RegularCharacter(val regular: RegularFormatCharacter) : ParsingCharacter(regular.char)
    data class DateTime(val dateTime: com.splendo.kaluga.base.text.DateTime) : ParsingCharacter(dateTime.char)
    object None : ParsingCharacter(Char.MIN_VALUE)
}

internal class FormatSpecifier(private val out: StringBuilder, matchResult: MatchResult) : FormatString {

    companion object {
        val formatSpecifier = "%(\\d+\\$)?([-#+ 0,(<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z@%])".toRegex()
    }
    override var index: Int = -1
        private set
    private var flags: MutableSet<Flag> = mutableSetOf()
    private var width: Int = 0
    private var precision: Int = 0
    private var currentChar: ParsingCharacter = ParsingCharacter.None

    init {
        index(matchResult.groupValues[1])
        flags(matchResult.groupValues[2])
        width(matchResult.groupValues[3])
        precision(matchResult.groupValues[4])

        val timeStart = matchResult.groupValues[5]
        val parsingCharacter = matchResult.groupValues[6][0]
        if (timeStart.isNotEmpty()) {
            if (timeStart[0] == 'T') {
                flags.add(Flag.UPPERCASE)
            }
            val dateTime = ParsingCharacter.DateTime(DateTime.parse(parsingCharacter))
            checkDateTime(dateTime)
            currentChar = dateTime
        } else {
            val regularCharacter = regularConversion(parsingCharacter)
            when {
                regularCharacter.regular.isGeneral() -> checkGeneral(regularCharacter)
                regularCharacter.regular.isCharacter() -> checkCharacter(regularCharacter)
                regularCharacter.regular.isInteger() -> checkInteger(regularCharacter)
                regularCharacter.regular.isFloat() -> checkFloat(regularCharacter)
                regularCharacter.regular.isText() -> checkText(regularCharacter)
                else -> throw StringFormatterException.UnknownFormatConversionException(regularCharacter.char.toString())
            }
        }
    }

    override fun print(arg: Any?, locale: KalugaLocale) {
        when (val currentChar = currentChar) {
            is ParsingCharacter.DateTime -> printDateTime(arg, currentChar, locale)
            is ParsingCharacter.RegularCharacter -> {
                when (currentChar.regular) {
                    RegularFormatCharacter.DECIMAL_INTEGER,
                    RegularFormatCharacter.OCTAL_INTEGER,
                    RegularFormatCharacter.HEXADECIMAL_INTEGER,
                    -> printInteger(arg, currentChar, locale)
                    RegularFormatCharacter.SCIENTIFIC,
                    RegularFormatCharacter.GENERAL,
                    RegularFormatCharacter.DECIMAL_FLOAT,
                    RegularFormatCharacter.HEXADECIMAL_FLOAT,
                    -> printFloat(arg, currentChar, locale)
                    RegularFormatCharacter.CHARACTER,
                    RegularFormatCharacter.CHARACTER_UPPER,
                    -> printCharacter(arg, currentChar, locale)
                    RegularFormatCharacter.BOOLEAN -> printBoolean(arg, locale)
                    RegularFormatCharacter.STRING,
                    RegularFormatCharacter.STRING_IOS,
                    -> printString(arg, locale)
                    RegularFormatCharacter.HASHCODE -> printHashCode(arg, locale)
                    RegularFormatCharacter.LINE_SEPARATOR -> out.append(lineSeparator)
                    RegularFormatCharacter.PERCENT_SIGN -> print("%", locale)
                    else -> throw StringFormatterException.UnknownFormatConversionException(currentChar.char.toString())
                }
            }
            else -> throw StringFormatterException.UnknownFormatConversionException(currentChar.char.toString())
        }
    }

    private fun printDateTime(arg: Any?, currentChar: ParsingCharacter.DateTime, locale: KalugaLocale) {
        val date = when (arg) {
            null -> {
                print("null", locale)
                return
            }
            is Long,
            is Int,
            is Short,
            -> {
                DefaultKalugaDate.epoch((arg as Number).toLong().milliseconds, KalugaTimeZone.current(), locale)
            }
            is KalugaDate -> {
                arg.copy()
            }
            else -> throw StringFormatterException.IllegalFormatConversionException(currentChar.char, arg)
        }
        print(date, currentChar, locale)
    }

    private fun printInteger(arg: Any?, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        when (arg) {
            null -> print("null", locale)
            is Int -> print(arg, currentChar, locale)
            is Short -> print(arg, currentChar, locale)
            is Byte -> print(arg, currentChar, locale)
            is Long -> print(arg, currentChar, locale)
            else -> throw StringFormatterException.IllegalFormatConversionException(currentChar.char, arg)
        }
    }

    private fun printFloat(arg: Any?, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        when (arg) {
            null -> print("null", locale)
            is Float -> print(arg, currentChar, locale)
            is Double -> print(arg, currentChar, locale)
            else -> throw StringFormatterException.IllegalFormatConversionException(currentChar.char, arg)
        }
    }

    private fun printCharacter(arg: Any?, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        val stringToPrint = when (arg) {
            null -> "null"
            is Char -> arg.toString()
            is Byte -> arg.toInt().toChar().toString()
            is Short -> arg.toInt().toChar().toString()
            is Int -> arg.toChar().toString()
            else -> throw StringFormatterException.IllegalFormatConversionException(currentChar.char, arg)
        }
        print(stringToPrint, locale)
    }

    private fun printString(arg: Any?, locale: KalugaLocale) {
        (arg as? Formattable)?.let {
            out.append(it.formatFor(locale, flags, width, precision))
        } ?: run {
            if (flags.contains(Flag.ALTERNATE)) {
                throw StringFormatterException.FormatFlagsConversionMismatchException(
                    Flag.ALTERNATE.toString(),
                    's',
                )
            }

            print(arg?.toString() ?: "null", locale)
        }
    }

    private fun printHashCode(arg: Any?, locale: KalugaLocale) {
        print(arg?.hashCode()?.toByte()?.let { byteArrayOf(it) }?.toHexString() ?: "null", locale)
    }

    private fun printBoolean(arg: Any?, locale: KalugaLocale) {
        print(
            (
                arg?.let {
                    (arg as? Boolean) ?: true
                } ?: false
                ).toString(),
            locale,
        )
    }

    private fun print(s: String, locale: KalugaLocale) {
        val string = if (precision != -1 && precision < s.length) s.substring(0, precision) else s
        appendJustified(out, if (flags.contains(Flag.UPPERCASE)) string.upperCased(locale) else string)
    }

    private fun print(value: Byte, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        print(value.toLong(), currentChar, locale)
    }

    private fun print(value: Short, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        print(value.toLong(), currentChar, locale)
    }

    private fun print(value: Int, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        print(value.toLong(), currentChar, locale)
    }

    private fun print(value: Long, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        val sb = StringBuilder()
        when (currentChar.regular) {
            RegularFormatCharacter.DECIMAL_INTEGER -> {
                val valueStr = value.toString(10)
                val neg = value < 0
                // leading sign indicator
                leadingSign(sb, neg)

                // the value
                localizedMagnitude(sb, valueStr, if (neg) 1 else 0, flags, adjustWidth(width, flags, neg), locale)

                // trailing sign indicator
                trailingSign(sb, neg)
            }
            RegularFormatCharacter.OCTAL_INTEGER -> {
                checkBadFlags(currentChar, Flag.PARENTHESES, Flag.LEADING_SPACE, Flag.PLUS)
                val valueString = value.toString(8)

                val alternate = flags.contains(Flag.ALTERNATE)
                val length = valueString.length + if (alternate) 1 else 0
                if (alternate) {
                    sb.append(getZero(locale))
                }
                if (flags.contains(Flag.ZERO_PAD)) {
                    trailingZeros(sb, width - length, locale)
                }
                sb.append(valueString)
            }
            RegularFormatCharacter.HEXADECIMAL_INTEGER -> {
                checkBadFlags(currentChar, Flag.PARENTHESES, Flag.LEADING_SPACE, Flag.PLUS)
                val hexValue = value.toString(16)
                val alternate = flags.contains(Flag.ALTERNATE)
                val uppercase = flags.contains(Flag.UPPERCASE)
                val length = hexValue.length + if (alternate) 2 else 0
                if (alternate) {
                    val prefix = "${getZero(locale)}x"
                    sb.append(if (uppercase) prefix.upperCased(locale) else prefix)
                }
                if (flags.contains(Flag.ZERO_PAD)) {
                    trailingZeros(sb, width - length, locale)
                }
                sb.append(if (uppercase) hexValue.upperCased(locale) else hexValue)
            }
            else -> throw StringFormatterException.UnexpectedChar(currentChar.char)
        }

        // justify based on width
        appendJustified(out, sb)
    }

    private fun print(value: Float, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        print(value.toDouble(), currentChar, locale)
    }

    private fun print(value: Double, currentChar: ParsingCharacter.RegularCharacter, locale: KalugaLocale) {
        val sb = StringBuilder()
        val neg = value < 0.0
        val numberFormatter = NumberFormatter(locale)
        if (!value.isNaN()) {
            val v: Double = abs(value)

            // leading sign indicator
            leadingSign(sb, neg)

            // the value
            if (v.isFinite()) {
                print(sb, v, locale, currentChar, precision, neg)
            } else {
                val infinitySymbol = numberFormatter.infinitySymbol
                sb.append(if (flags.contains(Flag.UPPERCASE)) infinitySymbol.upperCased(locale) else infinitySymbol)
            }

            // trailing sign indicator
            trailingSign(sb, neg)
        } else {
            val nanSymbol = numberFormatter.notANumberSymbol
            sb.append(if (flags.contains(Flag.UPPERCASE)) nanSymbol.upperCased(locale) else nanSymbol)
        }

        // justify based on width
        appendJustified(out, sb)
    }

    // !Double.isInfinite(value) && !Double.isNaN(value)
    private fun print(sb: StringBuilder, value: Double, locale: KalugaLocale, c: ParsingCharacter.RegularCharacter, precision: Int, neg: Boolean) {
        when (c.regular) {
            RegularFormatCharacter.SCIENTIFIC -> {
                val prec = if (precision == -1) 6 else precision
                val number = StringBuilder()
                val formatter = NumberFormatter(locale, NumberFormatStyle.Scientific(minFractionDigits = prec.toUInt(), maxFractionDigits = prec.toUInt(), minExponent = 2U))
                val expSymbol = formatter.exponentSymbol
                val scientific = formatter.format(value).split(expSymbol, ignoreCase = true, limit = 2)
                val mantissa = scientific[0]
                val exponent = when {
                    value == 0.0 -> "+${formatter.zeroSymbol}${formatter.zeroSymbol}"
                    value.absoluteValue >= 1.0 -> "+${scientific[1]}"
                    else -> scientific[1]
                }
                number.append(mantissa)
                addZeros(number, prec, locale)

                if (flags.contains(Flag.ALTERNATE) && prec == 0) {
                    number.append(formatter.decimalSeparator)
                }

                var newW = width
                if (width != -1) {
                    newW = adjustWidth(width - exponent.length - 1, flags, neg)
                }
                localizedMagnitude(sb, number, 0, flags, newW, locale)

                sb.append(if (flags.contains(Flag.UPPERCASE)) formatter.exponentSymbol.upperCased(locale) else formatter.exponentSymbol.lowerCased(locale))
                sb.append(exponent)
            }
            RegularFormatCharacter.DECIMAL_FLOAT -> {
                val prec = if (precision == -1) 6 else precision
                val number = StringBuilder()
                val formatter = NumberFormatter(locale, NumberFormatStyle.Decimal(minFractionDigits = prec.toUInt(), maxFractionDigits = prec.toUInt())).apply {
                    usesGroupingSeparator = false
                }
                if (value >= 0.0 && value < 1.0) {
                    number.append(formatter.zeroSymbol)
                }
                number.append(formatter.format(value))
                addZeros(number, prec, locale)

                if (flags.contains(Flag.ALTERNATE) && prec == 0) {
                    number.append(formatter.decimalSeparator)
                }
                var newW = width
                if (width != -1) {
                    newW = adjustWidth(width, flags, neg)
                }

                localizedMagnitude(sb, number, 0, flags, newW, locale)
            }
            RegularFormatCharacter.GENERAL -> {
                val scientificBuilder = StringBuilder()
                print(scientificBuilder, value, locale, ParsingCharacter.RegularCharacter(RegularFormatCharacter.SCIENTIFIC), precision, neg)
                val decimalBuilder = StringBuilder()
                print(decimalBuilder, value, locale, ParsingCharacter.RegularCharacter(RegularFormatCharacter.DECIMAL_FLOAT), precision, neg)
                val scientific = scientificBuilder.toString()
                val decimal = decimalBuilder.toString()
                sb.append(if (decimal.length <= scientific.length) decimal else scientific)
            }
            RegularFormatCharacter.HEXADECIMAL_FLOAT -> {
                // TODO Support Hexadecimal floats
            }
            else -> {}
        }
    }

    private fun print(time: KalugaDate, currentChar: ParsingCharacter.DateTime, locale: KalugaLocale) {
        val sb = StringBuilder()
        print(sb, time, currentChar, locale)

        // justify based on width
        if (flags.contains(Flag.UPPERCASE)) {
            appendJustified(out, sb.toString().upperCased(locale))
        } else {
            appendJustified(out, sb)
        }
    }

    private fun print(sb: StringBuilder, time: KalugaDate, currentChar: ParsingCharacter.DateTime, locale: KalugaLocale): StringBuilder {
        when (currentChar.dateTime) {
            DateTime.HOUR_OF_DAY_0, DateTime.HOUR_0, DateTime.HOUR_OF_DAY, DateTime.HOUR -> {
                // 'l' (1 - 12) -- like I
                var i: Int = time.hour
                if (currentChar.dateTime == DateTime.HOUR_0 || currentChar.dateTime == DateTime.HOUR) i = if (i == 0 || i == 12) 12 else i % 12
                val flags = if (currentChar.dateTime == DateTime.HOUR_OF_DAY_0 || currentChar.dateTime == DateTime.HOUR_0) {
                    setOf(Flag.ZERO_PAD)
                } else {
                    emptySet()
                }
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.MINUTE -> {
                // 'M' (00 - 59)
                val i: Int = time.minute
                val flags = setOf(Flag.ZERO_PAD)
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.NANOSECOND -> {
                // 'N' (000000000 - 999999999)
                val i: Int = time.millisecond.milliseconds.inWholeNanoseconds.toInt()
                val flags = setOf(Flag.ZERO_PAD)
                sb.append(localizedMagnitude(value = i, flags = flags, width = 9, locale = locale))
            }
            DateTime.MILLISECOND -> {
                // 'L' (000 - 999)
                val i: Int = time.millisecond
                val flags = setOf(Flag.ZERO_PAD)
                sb.append(localizedMagnitude(value = i, flags = flags, width = 3, locale = locale))
            }
            DateTime.MILLISECOND_SINCE_EPOCH -> {
                // 'Q' (0 - 99...?)
                val i: Long = time.durationSinceEpoch.inWholeMilliseconds
                sb.append(localizedMagnitude(value = i.toString(10), offset = 0, flags = emptySet(), width = width, locale = locale))
            }
            DateTime.AM_PM -> {
                // 'p' (am or pm)
                val isAm = time.hour < 12
                val dateFormat = KalugaDateFormatter.patternFormat("aa", KalugaTimeZone.current(), locale)
                sb.append((if (isAm) dateFormat.amString else dateFormat.pmString).lowerCased(locale))
            }
            DateTime.SECONDS_SINCE_EPOCH -> {
                // 's' (0 - 99...?)
                val i: Long = time.durationSinceEpoch.inWholeSeconds
                sb.append(localizedMagnitude(value = i.toString(10), offset = 0, flags = emptySet(), width = width, locale = locale))
            }
            DateTime.SECOND -> {
                // 'S' (00 - 60 - leap second)
                val i: Int = time.second
                val flags = setOf(Flag.ZERO_PAD)
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.ZONE_NUMERIC -> {
                // 'z' ({-|+}####) - ls minus?
                var i: Long = time.timeZone.offsetFromGMTAtDate(time).inWholeMilliseconds
                val neg = i < 0
                sb.append(if (neg) '-' else '+')
                if (neg) i = -i
                val min = i / 60000

                // combine minute and hour into a single integer
                val offset = min / 60 * 100 + min % 60
                val flags = setOf(Flag.ZERO_PAD)
                sb.append(localizedMagnitude(value = offset.toString(10), offset = 0, flags = flags, width = 4, locale = locale))
            }
            DateTime.ZONE -> {
                // 'Z' (symbol)
                sb.append(time.timeZone.displayName(TimeZoneNameStyle.Short, time.timeZone.usesDaylightSavingsTime(time), locale))
            }
            DateTime.NAME_OF_DAY_ABBREV, DateTime.NAME_OF_DAY -> {
                // 'A'
                val i: Int = time.weekDay - 1
                val dateFormat = KalugaDateFormatter.patternFormat("EEEE", locale = locale)
                val weekdays = if (currentChar.dateTime == DateTime.NAME_OF_DAY) dateFormat.weekdays else dateFormat.shortWeekdays
                sb.append(weekdays[i])
            }
            DateTime.NAME_OF_MONTH_ABBREV, DateTime.NAME_OF_MONTH_ABBREV_X, DateTime.NAME_OF_MONTH -> {
                // 'B'
                val i: Int = time.month - 1
                val dateFormat = KalugaDateFormatter.patternFormat("MMMM", locale = locale)
                val months = if (currentChar.dateTime == DateTime.NAME_OF_MONTH) dateFormat.months else dateFormat.shortMonths
                sb.append(months[i])
            }
            DateTime.CENTURY, DateTime.YEAR_2, DateTime.YEAR_4 -> {
                // 'Y' (0000 - 9999)
                var i: Int = time.year
                var size = 2
                when (currentChar.dateTime) {
                    DateTime.CENTURY -> i /= 100
                    DateTime.YEAR_2 -> i %= 100
                    DateTime.YEAR_4 -> size = 4
                    else -> {}
                }
                val flags = setOf(Flag.ZERO_PAD)
                sb.append(localizedMagnitude(value = i, flags = flags, width = size, locale = locale))
            }
            DateTime.DAY_OF_MONTH_0, DateTime.DAY_OF_MONTH -> {
                // 'e' (1 - 31) -- like d
                val i: Int = time.day
                val flags = if (currentChar.dateTime == DateTime.DAY_OF_MONTH_0) setOf(Flag.ZERO_PAD) else emptySet()
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.DAY_OF_YEAR -> {
                // 'j' (001 - 366)
                val i: Int = time.dayOfYear
                val flags = setOf(Flag.ZERO_PAD)
                sb.append(localizedMagnitude(value = i, flags = flags, width = 3, locale = locale))
            }
            DateTime.MONTH -> {
                // 'm' (01 - 12)
                val i: Int = time.month
                val flags = setOf(Flag.ZERO_PAD)
                sb.append(localizedMagnitude(value = i, flags = flags, width = 2, locale = locale))
            }
            DateTime.TIME, DateTime.TIME_24_HOUR -> {
                // 'R' (hh:mm same as %H:%M)
                val sep = ':'
                print(sb, time, ParsingCharacter.DateTime(DateTime.HOUR_OF_DAY_0), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.MINUTE), locale)
                if (currentChar.dateTime == DateTime.TIME) {
                    sb.append(sep)
                    print(sb, time, ParsingCharacter.DateTime(DateTime.SECOND), locale)
                }
            }
            DateTime.TIME_12_HOUR -> {
                // 'r' (hh:mm:ss [AP]M)
                val sep = ':'
                print(sb, time, ParsingCharacter.DateTime(DateTime.HOUR_0), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.MINUTE), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.SECOND), locale).append(' ')

                // this may be in wrong place for some locales
                val tsb = StringBuilder()
                print(tsb, time, ParsingCharacter.DateTime(DateTime.AM_PM), locale)
                sb.append(tsb.toString().upperCased(locale))
            }
            DateTime.DATE_TIME -> {
                // 'c' (Sat Nov 04 12:02:33 EST 1999)
                val sep = ' '
                print(sb, time, ParsingCharacter.DateTime(DateTime.NAME_OF_DAY_ABBREV), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.NAME_OF_MONTH_ABBREV), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.DAY_OF_MONTH_0), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.TIME), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.ZONE), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.YEAR_4), locale)
            }
            DateTime.DATE -> {
                // 'D' (mm/dd/yy)
                val sep = '/'
                print(sb, time, ParsingCharacter.DateTime(DateTime.MONTH), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.DAY_OF_MONTH_0), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.YEAR_2), locale)
            }
            DateTime.ISO_STANDARD_DATE -> {
                // 'F' (%Y-%m-%d)
                val sep = '-'
                print(sb, time, ParsingCharacter.DateTime(DateTime.YEAR_4), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.MONTH), locale).append(sep)
                print(sb, time, ParsingCharacter.DateTime(DateTime.DAY_OF_MONTH_0), locale)
            }
        }
        return sb
    }

    private fun addZeros(sb: StringBuilder, prec: Int, locale: KalugaLocale) {
        // Look for the dot.  If we don't find one, the we'll need to add

        // it before we add the zeros.
        val decimalSeparator = NumberFormatter(locale).decimalSeparator
        val len: Int = sb.length
        var i = 0
        while (i < len) {
            if (sb[i] == decimalSeparator) {
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
            sb.append(decimalSeparator)
        }

        // Add zeros.
        trailingZeros(sb, prec - outPrec, locale)
    }

    private fun appendJustified(out: StringBuilder, cs: CharSequence): StringBuilder {
        if (width == -1) {
            return out.append(cs)
        }
        val padRight: Boolean = flags.contains(Flag.LEFT_JUSTIFY)
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
            if (flags.contains(Flag.PLUS)) {
                sb.append('+')
            } else if (flags.contains(Flag.LEADING_SPACE)) {
                sb.append(' ')
            }
        } else {
            if (flags.contains(Flag.PARENTHESES)) sb.append('(') else sb.append('-')
        }
        return sb
    }

    // neg := val < 0
    private fun trailingSign(sb: StringBuilder, neg: Boolean): StringBuilder {
        if (neg && flags.contains(Flag.PARENTHESES)) sb.append(')')
        return sb
    }

    private fun localizedMagnitude(sb: StringBuilder = StringBuilder(), value: Int, flags: Set<Flag>, width: Int, locale: KalugaLocale): StringBuilder {
        return localizedMagnitude(sb, value.toString(10), 0, flags, width, locale)
    }

    private fun localizedMagnitude(sb: StringBuilder = StringBuilder(), value: CharSequence, offset: Int, flags: Set<Flag>, width: Int, locale: KalugaLocale): StringBuilder {
        val begin: Int = sb.length
        val zero: Char = getZero(locale)

        // determine localized grouping separator and size
        val numberFormatter = NumberFormatter(locale)
        var grpSep = '\u0000'
        var grpSize = -1
        val decSep = numberFormatter.decimalSeparator
        val len = value.length
        var dot = len
        for (j in offset until len) {
            if (value[j] == decSep) {
                dot = j
                break
            }
        }
        if (flags.contains(Flag.GROUP)) {
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
            sb.append((c - '0' + zero.code).toChar())
            if (grpSep != '\u0000' && j != dot - 1 && (dot - j) % grpSize == 1) {
                sb.append(grpSep)
            }
        }

        // apply zero padding
        if (width != -1 && flags.contains(Flag.ZERO_PAD)) {
            for (k in sb.length until width) {
                sb.insert(begin, zero)
            }
        }
        return sb
    }

    private fun adjustWidth(width: Int, flags: Set<Flag>, neg: Boolean): Int {
        var newW = width
        if (newW != -1 && neg && flags.contains(Flag.PARENTHESES)) newW--
        return newW
    }

    // Add trailing zeros

    // Add trailing zeros
    private fun trailingZeros(sb: StringBuilder, nzeros: Int, locale: KalugaLocale) {
        val zeroSymbol = getZero(locale)
        for (i in 0 until nzeros) {
            sb.append(zeroSymbol)
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

    private fun flags(stringToMatch: String): Set<Flag> {
        flags = Flag.parse(stringToMatch).toMutableSet()
        if (flags.contains(Flag.PREVIOUS)) index = -1
        return flags
    }

    private fun checkBadFlags(currentChar: ParsingCharacter, vararg badFlags: Flag) {
        for (badFlag in badFlags)
            if (flags.contains(badFlag)) {
                throw StringFormatterException.FormatFlagsConversionMismatchException(
                    badFlag.toString(),
                    currentChar.char,
                )
            }
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

    private fun regularConversion(conv: Char): ParsingCharacter.RegularCharacter {
        var currentRegularChar = RegularFormatCharacter.parse(conv)
        if (conv.uppercaseChar() == conv && conv.lowercaseChar() != conv) {
            flags.add(Flag.UPPERCASE)
            currentRegularChar = RegularFormatCharacter.parse(conv.lowercaseChar())
        }
        if (currentRegularChar.isText()) {
            index = -2
        }
        return ParsingCharacter.RegularCharacter(currentRegularChar).also {
            currentChar = it
        }
    }

    private fun checkGeneral(currentChar: ParsingCharacter.RegularCharacter) {
        if ((currentChar.regular == RegularFormatCharacter.BOOLEAN || currentChar.regular == RegularFormatCharacter.HASHCODE) && flags.contains(Flag.ALTERNATE)) {
            throw StringFormatterException.FormatFlagsConversionMismatchException(
                Flag.ALTERNATE.toString(),
                currentChar.char,
            )
        }

        // '-' requires a width
        if (width == -1 && flags.contains(Flag.LEFT_JUSTIFY)) throw StringFormatterException.MissingFormatWidthException(toString())
        checkBadFlags(
            currentChar,
            Flag.PLUS,
            Flag.LEADING_SPACE,
            Flag.ZERO_PAD,
            Flag.GROUP,
            Flag.PARENTHESES,
        )
    }

    private fun checkDateTime(currentChar: ParsingCharacter.DateTime) {
        if (precision != -1) throw StringFormatterException.IllegalFormatPrecisionException(precision)
        checkBadFlags(
            currentChar,
            Flag.ALTERNATE,
            Flag.PLUS,
            Flag.LEADING_SPACE,
            Flag.ZERO_PAD,
            Flag.GROUP,
            Flag.PARENTHESES,
        )

        // '-' requires a width
        if (width == -1 && flags.contains(Flag.LEFT_JUSTIFY)) throw StringFormatterException.MissingFormatWidthException(toString())
    }

    private fun checkCharacter(currentChar: ParsingCharacter.RegularCharacter) {
        if (precision != -1) throw StringFormatterException.IllegalFormatPrecisionException(precision)
        checkBadFlags(
            currentChar,
            Flag.ALTERNATE,
            Flag.PLUS,
            Flag.LEADING_SPACE,
            Flag.ZERO_PAD,
            Flag.GROUP,
            Flag.PARENTHESES,
        )

        // '-' requires a width
        if (width == -1 && flags.contains(Flag.LEFT_JUSTIFY)) throw StringFormatterException.MissingFormatWidthException(toString())
    }

    private fun checkInteger(currentChar: ParsingCharacter.RegularCharacter) {
        checkNumeric()
        if (precision != -1) throw StringFormatterException.IllegalFormatPrecisionException(precision)
        when (currentChar.regular) {
            RegularFormatCharacter.DECIMAL_INTEGER -> {
                checkBadFlags(currentChar, Flag.ALTERNATE)
            }
            RegularFormatCharacter.OCTAL_INTEGER -> checkBadFlags(currentChar, Flag.GROUP)
            else -> checkBadFlags(currentChar, Flag.GROUP)
        }
    }

    private fun checkFloat(currentChar: ParsingCharacter.RegularCharacter) {
        checkNumeric()
        when (currentChar.regular) {
            RegularFormatCharacter.DECIMAL_FLOAT -> {}
            RegularFormatCharacter.HEXADECIMAL_FLOAT -> {
                checkBadFlags(currentChar, Flag.PARENTHESES, Flag.GROUP)
            }
            RegularFormatCharacter.SCIENTIFIC -> {
                checkBadFlags(currentChar, Flag.GROUP)
            }
            RegularFormatCharacter.GENERAL -> {
                checkBadFlags(currentChar, Flag.ALTERNATE)
            }
            else -> {}
        }
    }

    private fun checkNumeric() {
        if (width != -1 && width < 0) throw StringFormatterException.IllegalFormatWidthException(width)
        if (precision != -1 && precision < 0) throw StringFormatterException.IllegalFormatPrecisionException(precision)

        // '-' and '0' require a width
        if (width == -1 && (flags.contains(Flag.LEFT_JUSTIFY) || flags.contains(Flag.ZERO_PAD))) {
            throw StringFormatterException.MissingFormatWidthException(toString())
        }

        // bad combination
        if (flags.contains(Flag.PLUS) && flags.contains(Flag.LEADING_SPACE) || flags.contains(Flag.LEFT_JUSTIFY) && flags.contains(Flag.ZERO_PAD)) {
            throw StringFormatterException.IllegalFormatFlagsException(flags.toString())
        }
    }

    private fun checkText(currentChar: ParsingCharacter.RegularCharacter) {
        if (precision != -1) throw StringFormatterException.IllegalFormatPrecisionException(precision)
        when (currentChar.regular) {
            RegularFormatCharacter.PERCENT_SIGN -> {
                when (flags.size) {
                    0 -> {}
                    1 -> {
                        if (!flags.contains(Flag.LEFT_JUSTIFY)) {
                            throw StringFormatterException.IllegalFormatFlagsException(flags.toString())
                        } else if (width == -1) {
                            throw StringFormatterException.MissingFormatWidthException(toString())
                        }
                    }
                    else -> throw StringFormatterException.IllegalFormatFlagsException(flags.toString())
                }
            }
            RegularFormatCharacter.LINE_SEPARATOR -> {
                if (width != -1) throw StringFormatterException.IllegalFormatWidthException(width)
                if (flags.isNotEmpty()) throw StringFormatterException.IllegalFormatFlagsException(flags.toString())
            }
            else -> throw StringFormatterException.UnexpectedChar(currentChar.char)
        }
    }
}
