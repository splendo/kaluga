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

import com.splendo.kaluga.base.utils.Locale

actual class NumberFormatter actual constructor(actual val locale: Locale, style: NumberFormatStyle) {
    actual var percentSymbol: Char = Char.MIN_VALUE
    actual var perMillSymbol: Char = Char.MIN_VALUE
    actual var minusSign: Char = Char.MIN_VALUE
    actual var exponentSymbol: String = ""
    actual var zeroSymbol: Char = Char.MIN_VALUE
    actual var notANumberSymbol: String = ""
    actual var infinitySymbol: String = ""
    actual var currencySymbol: String = ""
    actual var currencyCode: String = ""
    actual var positivePrefix: String = ""
    actual var positiveSuffix: String = ""
    actual var negativePrefix: String = ""
    actual var negativeSuffix: String = ""
    actual var groupingSeparator: Char = Char.MIN_VALUE
    actual var usesGroupingSeparator: Boolean = false
    actual var decimalSeparator: Char = Char.MIN_VALUE
    actual var alwaysShowsDecimalSeparator: Boolean = false
    actual var currencyDecimalSeparator: Char = Char.MIN_VALUE
    actual var groupingSize: Int = 0
    actual var multiplier: Int = 1

    actual fun format(number: Number): String = ""
    actual fun parse(string: String): Number? = null
}
