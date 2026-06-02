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
@file:OptIn(ExperimentalJsDecimal::class)

package com.splendo.kaluga.base.utils

/**
 * Platform specific representation of a finite immutable, arbitrary-precision signed decimal number
 * @property bd the [BigDecimal] representing the finite decimal number
 */
actual data class FiniteDecimal(internal val bd: BigDecimal) : Comparable<FiniteDecimal> {
    actual override fun compareTo(other: FiniteDecimal): Int = bd.compareTo(other.bd)
    override fun equals(other: Any?): Boolean = (other as? FiniteDecimal)?.let { bd.compareTo(it.bd) == 0 } ?: false
    override fun hashCode(): Int = bd.hashCode()
}

actual operator fun FiniteDecimal.plus(value: FiniteDecimal): FiniteDecimal = FiniteDecimal(bd.add(value.bd))

actual fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int): FiniteDecimal = FiniteDecimal(bd.add(value.bd).setScale(scale, RoundingMode.RoundHalfEven))

actual fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode): FiniteDecimal = FiniteDecimal(bd.add(value.bd).setScale(scale, roundingMode))

actual operator fun FiniteDecimal.minus(value: FiniteDecimal): FiniteDecimal = FiniteDecimal(bd.subtract(value.bd))

actual fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int): FiniteDecimal = FiniteDecimal(bd.subtract(value.bd).setScale(scale, RoundingMode.RoundHalfEven))

actual fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode): FiniteDecimal = FiniteDecimal(bd.subtract(value.bd).setScale(scale, roundingMode))

actual operator fun FiniteDecimal.div(value: FiniteDecimal): FiniteDecimal = FiniteDecimal(bd.divide(value.bd, DECIMAL128_PRECISION, RoundingMode.RoundHalfEven))

actual fun FiniteDecimal.div(value: FiniteDecimal, scale: Int): FiniteDecimal =
    FiniteDecimal(bd.divide(value.bd, DECIMAL128_PRECISION, RoundingMode.RoundHalfEven).setScale(scale, RoundingMode.RoundHalfEven))

actual fun FiniteDecimal.div(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode): FiniteDecimal =
    FiniteDecimal(bd.divide(value.bd, DECIMAL128_PRECISION, roundingMode).setScale(scale, roundingMode))

actual operator fun FiniteDecimal.times(value: FiniteDecimal): FiniteDecimal = FiniteDecimal(bd.multiply(value.bd, DECIMAL128_PRECISION, RoundingMode.RoundHalfEven))

actual fun FiniteDecimal.times(value: FiniteDecimal, scale: Int): FiniteDecimal =
    FiniteDecimal(bd.multiply(value.bd, DECIMAL128_PRECISION, RoundingMode.RoundHalfEven).setScale(scale, RoundingMode.RoundHalfEven))

actual fun FiniteDecimal.times(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode): FiniteDecimal =
    FiniteDecimal(bd.multiply(value.bd, DECIMAL128_PRECISION, roundingMode).setScale(scale, roundingMode))

actual infix fun FiniteDecimal.pow(n: Int): FiniteDecimal = FiniteDecimal(bd.pow(n))

actual fun FiniteDecimal.pow(n: Int, scale: Int): FiniteDecimal = FiniteDecimal(bd.pow(n).setScale(scale, RoundingMode.RoundHalfEven))

actual fun FiniteDecimal.pow(n: Int, scale: Int, roundingMode: RoundingMode): FiniteDecimal =
    FiniteDecimal(bd.pow(n, DECIMAL128_PRECISION, roundingMode).setScale(scale, roundingMode))

actual fun FiniteDecimal.round(scale: Int, roundingMode: RoundingMode): FiniteDecimal = FiniteDecimal(bd.setScale(scale, roundingMode))

actual fun Number.toFiniteDecimal(): FiniteDecimal? {
    if (this is Long) return FiniteDecimal(BigDecimal.fromLong(this))
    val d = this.toDouble()
    return when {
        d.isNaN() || d.isInfinite() -> null
        else -> FiniteDecimal(BigDecimal.fromDouble(d))
    }
}

actual fun String.toFiniteDecimal(): FiniteDecimal? = try {
    FiniteDecimal(BigDecimal.fromString(this))
} catch (_: dynamic) {
    null
}

actual fun FiniteDecimal.toDouble(): Double = bd.toDouble()
actual fun FiniteDecimal.toInt(): Int = bd.toInt()
actual fun FiniteDecimal.toLong(): Long = bd.toLong()
actual fun FiniteDecimal.stringValue(): String = bd.stringValue()
