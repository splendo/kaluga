/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.localization

import androidx.lifecycle.ViewModel
import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.utils.KalugaLocale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val SAMPLE_INTEGER: Long = 1_234_567_890
private const val SAMPLE_DECIMAL: Double = 12345.6789
private const val SAMPLE_PERCENT: Double = 0.4275
private const val SAMPLE_CURRENCY: Double = 1999.95

class LocalizationViewModel : ViewModel() {

    val availableLocales: List<KalugaLocale> = KalugaLocale.availableLocales.sortedBy { it.toString() }

    private val _selectedLocale = MutableStateFlow(KalugaLocale.defaultLocale)
    val selectedLocale: StateFlow<KalugaLocale> = _selectedLocale.asStateFlow()

    fun selectLocale(locale: KalugaLocale) {
        _selectedLocale.value = locale
    }

    fun integer(locale: KalugaLocale): String = NumberFormatter(locale, NumberFormatStyle.Integer()).format(SAMPLE_INTEGER)

    fun decimal(locale: KalugaLocale): String = NumberFormatter(locale, NumberFormatStyle.Decimal(minFractionDigits = 2U, maxFractionDigits = 4U)).format(SAMPLE_DECIMAL)

    fun percent(locale: KalugaLocale): String = NumberFormatter(locale, NumberFormatStyle.Percentage(maxFractionDigits = 2U)).format(SAMPLE_PERCENT)

    fun permille(locale: KalugaLocale): String = NumberFormatter(locale, NumberFormatStyle.Permillage(maxFractionDigits = 2U)).format(SAMPLE_PERCENT)

    fun currency(locale: KalugaLocale, code: String? = null): String = NumberFormatter(locale, NumberFormatStyle.Currency(currencyCode = code)).format(SAMPLE_CURRENCY)
}
