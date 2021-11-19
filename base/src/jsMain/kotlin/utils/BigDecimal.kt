/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
@file:JsModule("@yaffle/bigdecimal")
@file:JsNonModule
package com.splendo.kaluga.base.utils

external class BigDecimal(a: Any, b: Any) {
    var significand: Any
    var exponent: Any

    companion object {
        fun BigDecimal(a: Any): BigDecimal
        fun add(a: BigDecimal, b: BigDecimal, rounding: Rounding? = definedExternally): BigDecimal
        fun subtract(a: BigDecimal, b: BigDecimal, rounding: Rounding? = definedExternally): BigDecimal
        fun divide(a: BigDecimal, b: BigDecimal, rounding: Rounding? = definedExternally): BigDecimal
        fun multiply(a: BigDecimal, b: BigDecimal, rounding: Rounding? = definedExternally): BigDecimal
        fun round(a: BigDecimal, rounding: Rounding? = definedExternally): BigDecimal
        fun toNumber(a: BigDecimal): Double
        fun greaterThan(a: BigDecimal, b: BigDecimal): Boolean
        fun lessThan(a: BigDecimal, b: BigDecimal): Boolean
        fun compare(a: BigDecimal, b: BigDecimal): Boolean
        fun equal(a: BigDecimal, b: BigDecimal): Boolean
    }

    fun toFixed(fractionDigits: Int, roundingMode: String = definedExternally): Int
    override fun toString(): String
}
