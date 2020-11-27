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

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter

class TemperatureMeasurement(
    val value: Double,
    val unit: TemperatureUnit
) {

    fun convert(toUnit: TemperatureUnit) = when (Pair(unit, toUnit)) {
        Pair(TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT) -> (value * 9 / 5) + 32
        Pair(TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS) -> (value - 32) * 5 / 9
        else -> value
    }

    fun format(locale: Locale = Locale.defaultLocale, inUnit: TemperatureUnit? = null): String {
        val toUnit = inUnit ?: TemperatureUnit.preferred(locale)
        val value = convert(toUnit)
        val style = NumberFormatStyle.Decimal(maxFractionDigits = 1u)
        val formatter = NumberFormatter(locale, style).apply {
            positiveSuffix = toUnit.symbol
            negativeSuffix = toUnit.symbol
        }
        return formatter.format(value)
    }
}
