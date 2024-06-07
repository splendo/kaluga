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

import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.os.Build
import com.splendo.kaluga.base.text.upperCased

/**
 * Default implementation of [BaseLocale]
 */
actual data class KalugaLocale internal constructor(internal val locale: java.util.Locale) : BaseLocale() {
    actual companion object {

        /**
         * Creates a [KalugaLocale] based on a `language` ISO 639 alpha-2 or alpha-3 code
         * @param language a `language` ISO 639 alpha-2 or alpha-3 code.
         * @return The [KalugaLocale] for the given [language]
         */
        actual fun createLocale(language: String): KalugaLocale = KalugaLocale(
            java.util.Locale.Builder()
                .setLanguage(language)
                .build(),
        )

        /**
         * Creates a [KalugaLocale] based on a ISO 639 alpha-2 or alpha-3 `language` code and ISO 3166 alpha-2 `country` code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @return The [KalugaLocale] for the given [language] and [country]
         */
        actual fun createLocale(language: String, country: String): KalugaLocale = KalugaLocale(
            java.util.Locale.Builder()
                .setLanguage(language)
                .setRegion(country)
                .build(),
        )

        /**
         * Creates a [KalugaLocale] based on a ISO 639 alpha-2 or alpha-3 `language` code, ISO 3166 alpha-2 `country` code, and variant code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @param variant Arbitrary value used to indicate a variation of a [KalugaLocale]
         * @return The [KalugaLocale] for the given [language], [country], and [variant]
         */
        actual fun createLocale(language: String, country: String, variant: String): KalugaLocale = KalugaLocale(
            java.util.Locale.Builder()
                .setLanguage(language)
                .setRegion(country)
                .setVariant(variant)
                .build(),
        )

        /**
         * The default [KalugaLocale] of the user
         */
        actual val defaultLocale: KalugaLocale get() = KalugaLocale(java.util.Locale.getDefault())

        /**
         * A list of [KalugaLocale] available to the user.
         */
        actual val availableLocales: List<KalugaLocale> = java.util.Locale.getAvailableLocales().asList().map { KalugaLocale(it) }
    }

    actual override val countryCode: String
        get() = locale.country
    actual override val languageCode: String
        get() = locale.language
    actual override val scriptCode: String
        get() = locale.script
    actual override val variantCode: String
        get() = locale.variant
    actual override val unitSystem: UnitSystem
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            when (LocaleData.getMeasurementSystem(ULocale.forLocale(locale))) {
                LocaleData.MeasurementSystem.US -> UnitSystem.IMPERIAL
                LocaleData.MeasurementSystem.UK -> UnitSystem.MIXED
                else -> UnitSystem.METRIC
            }
        } else {
            UnitSystem.withCountryCode(countryCode.upperCased(this))
        }

    actual override fun name(forLocale: KalugaLocale): String = locale.getDisplayName(forLocale.locale)
    actual override fun countryName(forLocale: KalugaLocale): String = locale.getDisplayCountry(forLocale.locale)
    actual override fun languageName(forLocale: KalugaLocale): String = locale.getDisplayLanguage(forLocale.locale)
    actual override fun variantName(forLocale: KalugaLocale): String = locale.getDisplayVariant(forLocale.locale)
    actual override fun scriptName(forLocale: KalugaLocale): String = locale.getDisplayScript(forLocale.locale)

    actual override val quotationStart: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocaleData.getInstance(ULocale.forLocale(locale)).getDelimiter(LocaleData.QUOTATION_START)
        } else {
            "\""
        }
    actual override val quotationEnd: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocaleData.getInstance(ULocale.forLocale(locale)).getDelimiter(LocaleData.QUOTATION_END)
        } else {
            "\""
        }
    actual override val alternateQuotationStart: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocaleData.getInstance(ULocale.forLocale(locale)).getDelimiter(LocaleData.ALT_QUOTATION_START)
        } else {
            "\""
        }
    actual override val alternateQuotationEnd: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocaleData.getInstance(ULocale.forLocale(locale)).getDelimiter(LocaleData.ALT_QUOTATION_END)
        } else {
            "\""
        }
}
