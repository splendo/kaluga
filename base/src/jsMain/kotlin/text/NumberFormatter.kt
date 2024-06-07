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

import com.splendo.kaluga.base.utils.KalugaLocale

// TODO Implement with proper number formatter solution for Java Script
/**
 * Default implementation of [BaseNumberFormatter]
 * @param locale The [KalugaLocale] used for parsing. Defaults to [KalugaLocale.defaultLocale].
 * @param style The [NumberFormatStyle] to configure the format to use. Defaults to [NumberFormatStyle.Decimal].
 */
actual class NumberFormatter actual constructor(actual override val locale: KalugaLocale, style: NumberFormatStyle) : BaseNumberFormatter {
    actual override var percentSymbol: Char = '%'
    actual override var perMillSymbol: Char = '\u2030'
    actual override var minusSign: Char = '-'
    actual override var exponentSymbol: String = "E"
    actual override var zeroSymbol: Char = '0'
    actual override var notANumberSymbol: String = "NaN"
    actual override var infinitySymbol: String = "\u221E"
    actual override var currencySymbol: String = ""
    actual override var currencyCode: String = ""
    actual override var positivePrefix: String = ""
    actual override var positiveSuffix: String = ""
    actual override var negativePrefix: String = "-"
    actual override var negativeSuffix: String = ""
    actual override var groupingSeparator: Char = ','
    actual override var usesGroupingSeparator: Boolean = false
    actual override var decimalSeparator: Char = '.'
    actual override var alwaysShowsDecimalSeparator: Boolean = false
    actual override var currencyDecimalSeparator: Char = '.'
    actual override var groupingSize: Int = 0
    actual override var multiplier: Int = 1

    actual override fun format(number: Number): String = "$number"
    actual override fun parse(string: String): Number? = string.toFloatOrNull()
}
