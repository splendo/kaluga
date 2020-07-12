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

import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.text.Conversion.BOOLEAN
import com.splendo.kaluga.base.text.Conversion.CHARACTER
import com.splendo.kaluga.base.text.Conversion.CHARACTER_UPPER
import com.splendo.kaluga.base.text.Conversion.DECIMAL_FLOAT
import com.splendo.kaluga.base.text.Conversion.DECIMAL_INTEGER
import com.splendo.kaluga.base.text.Conversion.GENERAL
import com.splendo.kaluga.base.text.Conversion.HASHCODE
import com.splendo.kaluga.base.text.Conversion.HEXADECIMAL_FLOAT
import com.splendo.kaluga.base.text.Conversion.HEXADECIMAL_INTEGER
import com.splendo.kaluga.base.text.Conversion.HEXADECIMAL_INTEGER_UPPER
import com.splendo.kaluga.base.text.Conversion.LINE_SEPARATOR
import com.splendo.kaluga.base.text.Conversion.OCTAL_INTEGER
import com.splendo.kaluga.base.text.Conversion.PERCENT_SIGN
import com.splendo.kaluga.base.text.Conversion.SCIENTIFIC
import com.splendo.kaluga.base.text.Conversion.STRING
import com.splendo.kaluga.base.text.Conversion.STRING_IOS
import com.splendo.kaluga.base.text.Flags.Companion.ALTERNATE
import com.splendo.kaluga.base.text.Flags.Companion.GROUP
import com.splendo.kaluga.base.text.Flags.Companion.LEADING_SPACE
import com.splendo.kaluga.base.text.Flags.Companion.LEFT_JUSTIFY
import com.splendo.kaluga.base.text.Flags.Companion.NONE
import com.splendo.kaluga.base.text.Flags.Companion.PARENTHESES
import com.splendo.kaluga.base.text.Flags.Companion.PLUS
import com.splendo.kaluga.base.text.Flags.Companion.ZERO_PAD
import com.splendo.kaluga.base.text.StringFormatter.Companion.getZero
import kotlin.math.abs

@ExperimentalStdlibApi
internal class FormatSpecifier(private val out: StringBuilder, private val string: String, private val matchResult: MatchResult) : FormatString {

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
                flags.add(Flags.UPPERCASE)
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
        if (arg == null)
            print("null", locale)
        else {
            // TODO Support Date Time
            throw StringFormatterException.IllegalFormatConversionException(currentChar, arg)
        }
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
        if (flags.contains(Flags.UPPERCASE)) s = s.upperCased(locale)
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
        val valueStr = when (currentChar) {
            DECIMAL_INTEGER -> {
                value.toString(10)
            }
            OCTAL_INTEGER -> {
                value.toString(8)
            }
            HEXADECIMAL_INTEGER -> {
                val hexValue = value.toString(16)
                if (flags.contains(Flags.UPPERCASE)) {
                    hexValue.upperCased(locale)
                } else
                    hexValue
            }
            else -> throw StringFormatterException.UnexpectedChar(currentChar)
        }
        val neg = value < 0
        // leading sign indicator
        leadingSign(sb, neg)

        // the value
        localizedMagnitude(sb, valueStr, if (neg) 1 else 0, flags, adjustWidth(width, flags, neg), locale)

        // trailing sign indicator
        trailingSign(sb, neg)

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
                print(sb, v, locale, currentChar, precision)
            else {
                val infinitySymbol = numberFormatter.infinitySymbol
                sb.append(if (flags.contains(Flags.UPPERCASE)) infinitySymbol.upperCased(locale) else infinitySymbol)
            }

            // trailing sign indicator
            trailingSign(sb, neg)
        } else {
            val nanSymbol = numberFormatter.notANumberSymbol
            sb.append(if (flags.contains(Flags.UPPERCASE)) nanSymbol.upperCased(locale) else nanSymbol)
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
        precision: Int
    ) {
        val prec = if (precision == -1) 6 else precision
        when (c) {
            SCIENTIFIC -> {
                val formatter = NumberFormatter(locale, NumberFormatStyle.Scientific(prec + 1))
                sb.append(formatter.format(value).upperCased(locale))
            }
            DECIMAL_FLOAT,
            GENERAL -> {
                val formatter = NumberFormatter(locale, NumberFormatStyle.Decimal(minFraction = prec, maxFraction = prec))
                sb.append(formatter.format(value))
            }
            HEXADECIMAL_FLOAT -> {
                // TODO Add Hex Float Support
            }
        }
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
        sb: StringBuilder,
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
        if (flags.contains(Flags.GROUP)) {
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
        if (width != -1 && flags.contains(Flags.ZERO_PAD)) {
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
        if (flags.contains(Flags.PREVIOUS)) index = -1
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
                flags.add(Flags.UPPERCASE)
                currentChar = currentChar.toLowerCase()
            }
            if (Conversion.isText(currentChar)) {
                index = -2
            }
        }
        return currentChar
    }

    private fun checkDateTime() {
        throw StringFormatterException.UnsupportedFormat("Date Time")
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
