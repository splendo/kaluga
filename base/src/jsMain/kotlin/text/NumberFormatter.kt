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
actual class NumberFormatter actual constructor(override val locale: KalugaLocale, style: NumberFormatStyle) : BaseNumberFormatter {
    override var percentSymbol: Char = '%'
    override var perMillSymbol: Char = '\u2030'
    override var minusSign: Char = '-'
    override var exponentSymbol: String = "E"
    override var zeroSymbol: Char = '0'
    override var notANumberSymbol: String = "NaN"
    override var infinitySymbol: String = "\u221E"
    override var currencySymbol: String = ""
    override var currencyCode: String = ""
    override var positivePrefix: String = ""
    override var positiveSuffix: String = ""
    override var negativePrefix: String = "-"
    override var negativeSuffix: String = ""
    override var groupingSeparator: Char = ','
    override var usesGroupingSeparator: Boolean = false
    override var decimalSeparator: Char = '.'
    override var alwaysShowsDecimalSeparator: Boolean = false
    override var currencyDecimalSeparator: Char = '.'
    override var groupingSize: Int = 0
    override var multiplier: Int = 1

    override fun format(number: Number): String = "$number"
    override fun parse(string: String): Number? = string.toFloatOrNull()
}
