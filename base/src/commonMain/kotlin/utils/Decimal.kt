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
package com.splendo.kaluga.base.utils

expect class Decimal : Comparable<Decimal>

expect operator fun Decimal.plus(value: Decimal): Decimal
expect fun Decimal.plus(value: Decimal, scale: Int): Decimal
expect fun Decimal.plus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

expect operator fun Decimal.minus(value: Decimal): Decimal
expect fun Decimal.minus(value: Decimal, scale: Int): Decimal
expect fun Decimal.minus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

expect operator fun Decimal.div(value: Decimal): Decimal
expect fun Decimal.div(value: Decimal, scale: Int): Decimal
expect fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

expect operator fun Decimal.times(value: Decimal): Decimal
expect fun Decimal.times(value: Decimal, scale: Int): Decimal
expect fun Decimal.times(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

expect infix fun Decimal.pow(n: Int): Decimal
expect fun Decimal.pow(n: Int, scale: Int): Decimal
expect fun Decimal.pow(
    n: Int,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

sealed class RoundingMode {
    object RoundDown : RoundingMode()
    object RoundHalfEven : RoundingMode()
    object RoundUp : RoundingMode()
}

expect fun Decimal.round(scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): Decimal

expect fun Number.toDecimal(): Decimal
expect fun String.toDecimal(): Decimal

fun ByteArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }
fun DoubleArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }
fun FloatArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }
fun IntArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }
fun ShortArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }
fun LongArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }
fun <NumberType : Number> Collection<NumberType>.toDecimalList(): List<Decimal> = map { it.toDecimal() }

expect fun Decimal.toDouble(): Double
expect fun Decimal.toString(): String
expect fun Decimal.toInt(): Int

fun List<Decimal>.toDoubleArray(): DoubleArray = map { it.toDouble() }.toDoubleArray()
fun List<Decimal>.toStringList(): List<String> = map { it.toString() }
fun List<Decimal>.toIntArray(): IntArray = map { it.toInt() }.toIntArray()
