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

import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.os.Build
import com.splendo.kaluga.base.text.upperCased

actual class Locale internal constructor(val locale: java.util.Locale) {
    actual companion object {
        actual fun createLocale(language: String): Locale = Locale(java.util.Locale(language))
        actual fun createLocale(language: String, country: String): Locale = Locale(java.util.Locale(language, country))
        actual fun createLocale(language: String, country: String, variant: String): Locale = Locale(java.util.Locale(language, country, variant))

        actual val defaultLocale: Locale get() = Locale(java.util.Locale.getDefault())
        actual val availableLocales: List<Locale> = java.util.Locale.getAvailableLocales().asList().map { Locale(it) }
    }

    actual val countryCode: String
        get() = locale.country
    actual val languageCode: String
        get() = locale.language
    actual val scriptCode: String
        get() = locale.script
    actual val variantCode: String
        get() = locale.variant
    actual val unitSystem: UnitSystem
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            when (LocaleData.getMeasurementSystem(ULocale.forLocale(locale))) {
                LocaleData.MeasurementSystem.US -> UnitSystem.IMPERIAL
                LocaleData.MeasurementSystem.UK -> UnitSystem.MIXED
                else -> UnitSystem.METRIC
            }
        } else {
            UnitSystem.withCountryCode(countryCode.upperCased(this))
        }

    actual fun name(forLocale: Locale): String = locale.getDisplayName(forLocale.locale)
    actual fun countryName(forLocale: Locale): String = locale.getDisplayCountry(forLocale.locale)
    actual fun languageName(forLocale: Locale): String = locale.getDisplayLanguage(forLocale.locale)
    actual fun variantName(forLocale: Locale): String = locale.getDisplayVariant(forLocale.locale)
    actual fun scriptName(forLocale: Locale): String = locale.getDisplayScript(forLocale.locale)

    actual val quotationStart: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocaleData.getInstance(ULocale.forLocale(locale)).getDelimiter(LocaleData.QUOTATION_START)
        } else {
            "\""
        }
    actual val quotationEnd: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocaleData.getInstance(ULocale.forLocale(locale)).getDelimiter(LocaleData.QUOTATION_END)
        } else {
            "\""
        }
    actual val alternateQuotationStart: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocaleData.getInstance(ULocale.forLocale(locale)).getDelimiter(LocaleData.ALT_QUOTATION_START)
        } else {
            "\""
        }
    actual val alternateQuotationEnd: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocaleData.getInstance(ULocale.forLocale(locale)).getDelimiter(LocaleData.ALT_QUOTATION_END)
        } else {
            "\""
        }
}
