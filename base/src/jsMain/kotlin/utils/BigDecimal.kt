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

@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.RoundingMode.RoundDown
import com.splendo.kaluga.base.utils.RoundingMode.RoundHalfEven
import com.splendo.kaluga.base.utils.RoundingMode.RoundUp
import kotlin.math.max

internal const val DECIMAL128_PRECISION = 34

@JsName("BigInt")
private external fun jsBigInt(value: String): dynamic

@JsName("BigInt")
private external fun jsBigInt(value: Int): dynamic

private val BI_ZERO: dynamic = jsBigInt(0)
private val BI_ONE: dynamic = jsBigInt(1)
private val BI_TWO: dynamic = jsBigInt(2)
private val BI_FIVE: dynamic = jsBigInt(5)
private val BI_TEN: dynamic = jsBigInt(10)

private fun bigIntLessThan(a: dynamic, b: dynamic): Boolean = (a < b).unsafeCast<Boolean>()
private fun bigInGreaterThan(a: dynamic, b: dynamic): Boolean = (a > b).unsafeCast<Boolean>()
private fun bigIntEquals(a: dynamic, b: dynamic): Boolean = js("a === b").unsafeCast<Boolean>()
private fun bigIntToString(a: dynamic): String = js("a.toString()").unsafeCast<String>()

private fun bigIntCompareTo(a: dynamic, b: dynamic): Int = when {
    bigIntLessThan(a, b) -> -1
    bigInGreaterThan(a, b) -> 1
    else -> 0
}

private val powerCache = HashMap<Int, dynamic>()

private fun pow10(n: Int): dynamic {
    if (n <= 0) return BI_ONE
    val cached = powerCache[n]
    if (cached != null) return cached
    val s = "1" + "0".repeat(n)
    val result = jsBigInt(s)
    if (n <= 128) powerCache[n] = result
    return result
}

private fun bigIntPow(base: dynamic, n: Int): dynamic {
    if (n <= 0) return BI_ONE
    var result: dynamic = BI_ONE
    var b: dynamic = base
    var e = n
    while (e > 0) {
        if (e and 1 == 1) result = result * b
        e = e shr 1
        if (e > 0) b = b * b
    }
    return result
}

private fun digitsOf(value: dynamic): Int {
    if (bigIntEquals(value, BI_ZERO)) return 1
    val s = bigIntToString(value)
    return if (s.startsWith("-")) s.length - 1 else s.length
}

/**
 * Pure Kotlin/JS arbitrary-precision signed decimal number backed by [JavaScript BigInt](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/BigInt).
 *
 * Represents the value `significand * 10^(-scale)`, mirroring `java.math.BigDecimal`.
 */
class BigDecimal(val significand: dynamic, val scale: Int) {

    val isZero: Boolean get() = bigIntEquals(significand, BI_ZERO)
    val isNegative: Boolean get() = bigIntLessThan(significand, BI_ZERO)

    fun unaryMinus(): BigDecimal = BigDecimal((-significand).unsafeCast<dynamic>(), scale)

    fun add(other: BigDecimal): BigDecimal {
        val maxScale = max(scale, other.scale)
        val a = if (scale < maxScale) significand * pow10(maxScale - scale) else significand
        val b = if (other.scale < maxScale) other.significand * pow10(maxScale - other.scale) else other.significand
        return BigDecimal((a + b).unsafeCast<dynamic>(), maxScale)
    }

    fun subtract(other: BigDecimal): BigDecimal {
        val maxScale = max(scale, other.scale)
        val a = if (scale < maxScale) significand * pow10(maxScale - scale) else significand
        val b = if (other.scale < maxScale) other.significand * pow10(maxScale - other.scale) else other.significand
        return BigDecimal((a - b).unsafeCast<dynamic>(), maxScale)
    }

    fun multiply(other: BigDecimal, precision: Int? = null, rounding: RoundingMode = RoundHalfEven): BigDecimal {
        val raw = BigDecimal((significand * other.significand).unsafeCast<dynamic>(), scale + other.scale)
        return if (precision != null) raw.round(precision, rounding) else raw
    }

    fun divide(other: BigDecimal, precision: Int, rounding: RoundingMode = RoundHalfEven): BigDecimal {
        if (other.isZero) throw ArithmeticException("Division by zero")
        if (isZero) return BigDecimal(BI_ZERO, scale - other.scale)

        val aDigits = digitsOf(significand)
        val bDigits = digitsOf(other.significand)
        // Enough extra digits to compute `precision + 1` significant digits in the quotient.
        val extraDigits = max(precision + bDigits - aDigits + 1, 0)

        var dividend: dynamic = if (extraDigits > 0) significand * pow10(extraDigits) else significand
        var divisor: dynamic = other.significand

        if (bigIntLessThan(divisor, BI_ZERO)) {
            dividend = -dividend
            divisor = -divisor
        }
        val negDividend = bigIntLessThan(dividend, BI_ZERO)
        val absDividend: dynamic = if (negDividend) -dividend else dividend

        var quotient: dynamic = absDividend / divisor
        val remainder: dynamic = absDividend - quotient * divisor
        if (!bigIntEquals(remainder, BI_ZERO)) {
            val twoRemainder = remainder * BI_TWO
            val cmp = bigIntCompareTo(twoRemainder, divisor)
            val roundUp = when (rounding) {
                RoundDown -> false

                RoundUp -> true

                RoundHalfEven -> when {
                    cmp > 0 -> true
                    cmp < 0 -> false
                    else -> !bigIntEquals(quotient % BI_TWO, BI_ZERO)
                }
            }
            if (roundUp) quotient += BI_ONE
        }
        if (negDividend) quotient = -quotient

        val resultScale = extraDigits + scale - other.scale
        return BigDecimal(quotient, resultScale).round(precision, rounding)
    }

    fun round(precision: Int, rounding: RoundingMode): BigDecimal {
        if (isZero) return this
        val d = digitsOf(significand)
        if (d <= precision) return this
        return setScale(scale - (d - precision), rounding)
    }

    fun setScale(newScale: Int, rounding: RoundingMode): BigDecimal {
        if (newScale == scale) return this
        if (newScale > scale) {
            return BigDecimal((significand * pow10(newScale - scale)).unsafeCast<dynamic>(), newScale)
        }
        val diff = scale - newScale
        val divisor = pow10(diff)
        val neg = isNegative
        val abs: dynamic = if (neg) -significand else significand
        var quotient: dynamic = abs / divisor
        val remainder: dynamic = abs - quotient * divisor
        if (!bigIntEquals(remainder, BI_ZERO)) {
            val twoRemainder = remainder * BI_TWO
            val cmp = bigIntCompareTo(twoRemainder, divisor)
            val roundUp = when (rounding) {
                RoundDown -> false

                RoundUp -> true

                RoundHalfEven -> when {
                    cmp > 0 -> true
                    cmp < 0 -> false
                    else -> !bigIntEquals(quotient % BI_TWO, BI_ZERO)
                }
            }
            if (roundUp) quotient += BI_ONE
        }
        if (neg) quotient = -quotient
        return BigDecimal(quotient, newScale)
    }

    fun pow(n: Int, precision: Int = DECIMAL128_PRECISION, rounding: RoundingMode = RoundHalfEven): BigDecimal {
        if (n == 0) return ONE
        if (n < 0) return ONE.divide(pow(-n, precision, rounding), precision, rounding)
        var result = ONE
        var base = this
        var exp = n
        while (exp > 0) {
            if (exp and 1 == 1) result = result.multiply(base, precision, rounding)
            exp = exp shr 1
            if (exp > 0) base = base.multiply(base, precision, rounding)
        }
        return result
    }

    fun compareTo(other: BigDecimal): Int {
        val s1 = bigIntCompareTo(significand, BI_ZERO)
        val s2 = bigIntCompareTo(other.significand, BI_ZERO)
        if (s1 != s2) return s1.compareTo(s2)
        if (s1 == 0) return 0
        // Each non-zero value satisfies 10^(M-1) <= |v| < 10^M where M = digits - scale.
        // Different M ⇒ strict inequality without aligning scales — guards against
        // pathological cases like comparing 1e-1_000_000 against 1.
        val ma = digitsOf(significand) - scale
        val mb = digitsOf(other.significand) - other.scale
        if (ma != mb) return if (s1 > 0) ma.compareTo(mb) else mb.compareTo(ma)
        val maxScale = max(scale, other.scale)
        val a = if (scale < maxScale) significand * pow10(maxScale - scale) else significand
        val b = if (other.scale < maxScale) other.significand * pow10(maxScale - other.scale) else other.significand
        return bigIntCompareTo(a, b)
    }

    override fun equals(other: Any?): Boolean = (other as? BigDecimal)?.let { compareTo(it) == 0 } ?: false

    override fun hashCode(): Int {
        val s = stripTrailingZeros()
        return bigIntToString(s.significand).hashCode() * 31 + s.scale
    }

    fun stripTrailingZeros(): BigDecimal {
        if (isZero) return BigDecimal(BI_ZERO, 0)
        var sig: dynamic = significand
        var s = scale
        while (bigIntEquals(sig % BI_TEN, BI_ZERO)) {
            sig = sig / BI_TEN
            s -= 1
        }
        return BigDecimal(sig, s)
    }

    override fun toString(): String {
        val sign = if (isNegative) "-" else ""
        val absStr = if (isZero) {
            "0"
        } else {
            bigIntToString((if (isNegative) -significand else significand).unsafeCast<dynamic>())
        }
        val precision = absStr.length
        // JVM BigDecimal.toString: use plain notation iff scale >= 0 && adjustedExp >= -6,
        // otherwise scientific with the decimal point after the first significant digit.
        val adjustedExp = (precision - 1) - scale
        return if (scale >= 0 && adjustedExp >= -6) {
            when {
                scale == 0 -> sign + absStr
                absStr.length > scale -> sign + absStr.substring(0, absStr.length - scale) + "." + absStr.substring(absStr.length - scale)
                else -> sign + "0." + "0".repeat(scale - absStr.length) + absStr
            }
        } else {
            val mantissa = if (precision == 1) absStr else absStr.substring(0, 1) + "." + absStr.substring(1)
            val expStr = if (adjustedExp >= 0) "E+$adjustedExp" else "E$adjustedExp"
            sign + mantissa + expStr
        }
    }

    fun stringValue(): String = stripTrailingZeros().toString()

    fun toDouble(): Double = toString().toDouble()

    fun toLong(): Long {
        val rounded = setScale(0, RoundDown)
        val s = bigIntToString(rounded.significand)
        return try {
            s.toLong()
        } catch (_: NumberFormatException) {
            if (rounded.isNegative) Long.MIN_VALUE else Long.MAX_VALUE
        }
    }

    fun toInt(): Int {
        val l = toLong()
        return when {
            l > Int.MAX_VALUE -> Int.MAX_VALUE
            l < Int.MIN_VALUE -> Int.MIN_VALUE
            else -> l.toInt()
        }
    }

    companion object {
        val ZERO = BigDecimal(BI_ZERO, 0)
        val ONE = BigDecimal(BI_ONE, 0)

        private val decimalRegex = Regex("^([+-]?)(\\d*)(?:\\.(\\d*))?(?:[eE]([+-]?\\d+))?$")

        fun fromString(s: String): BigDecimal {
            val trimmed = s.trim()
            val match = decimalRegex.matchEntire(trimmed) ?: throw NumberFormatException("Not a valid decimal: $s")
            val signStr = match.groupValues[1]
            val intPart = match.groupValues[2]
            val fracPart = match.groupValues[3]
            val expStr = match.groupValues[4]
            if (intPart.isEmpty() && fracPart.isEmpty()) {
                throw NumberFormatException("Not a valid decimal: $s")
            }
            val exp = if (expStr.isEmpty()) 0 else expStr.toInt()
            val combined = (intPart + fracPart).trimStart('0').ifEmpty { "0" }
            val significandStr = (if (signStr == "-") "-" else "") + combined
            return BigDecimal(jsBigInt(significandStr), fracPart.length - exp)
        }

        fun fromInt(n: Int): BigDecimal = BigDecimal(jsBigInt(n), 0)

        fun fromLong(n: Long): BigDecimal = BigDecimal(jsBigInt(n.toString()), 0)

        /**
         * Decodes [d] as the exact rational it represents in IEEE 754 (mirroring the
         * `java.math.BigDecimal(double)` constructor), not its shorter decimal display.
         * E.g. `fromDouble(0.1)` yields `0.1000000000000000055511151231257827021181583404541015625`.
         */
        fun fromDouble(d: Double): BigDecimal {
            if (d.isNaN() || d.isInfinite()) throw NumberFormatException("Not a valid decimal: $d")
            if (d == 0.0) return ZERO

            val bits = d.toRawBits()
            val signBit = (bits ushr 63).toInt()
            val rawExp = ((bits ushr 52) and 0x7FFL).toInt()
            val rawFrac = bits and ((1L shl 52) - 1L)
            var mantissa = if (rawExp == 0) rawFrac shl 1 else rawFrac or (1L shl 52)
            var exponent = rawExp - 1075
            // Normalize away trailing zero bits — this keeps the resulting decimal scale minimal.
            while ((mantissa and 1L) == 0L) {
                mantissa = mantissa shr 1
                exponent++
            }
            val absSig: dynamic = jsBigInt(mantissa.toString())
            return if (exponent >= 0) {
                val product: dynamic = absSig * bigIntPow(BI_TWO, exponent)
                val signed: dynamic = if (signBit != 0) -product else product
                BigDecimal(signed, 0)
            } else {
                val product: dynamic = absSig * bigIntPow(BI_FIVE, -exponent)
                val signed: dynamic = if (signBit != 0) -product else product
                BigDecimal(signed, -exponent)
            }
        }
    }
}
