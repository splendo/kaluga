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

// TODO Implement with proper locale solution for Java Script
/**
 * Default implementation of [BaseLocale]
 */
actual data class KalugaLocale internal constructor(
    actual override val languageCode: String,
    actual override val countryCode: String = "",
    actual override val variantCode: String = "",
    actual override val scriptCode: String = "",
    actual override val unitSystem: UnitSystem = UnitSystem.METRIC,
) : BaseLocale() {

    actual companion object {

        /**
         * Creates a [KalugaLocale] based on a `language` ISO 639 alpha-2 or alpha-3 code
         * @param language a `language` ISO 639 alpha-2 or alpha-3 code.
         * @return The [KalugaLocale] for the given [language]
         */
        actual fun createLocale(language: String): KalugaLocale = KalugaLocale(language)

        /**
         * Creates a [KalugaLocale] based on a ISO 639 alpha-2 or alpha-3 `language` code and ISO 3166 alpha-2 `country` code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @return The [KalugaLocale] for the given [language] and [country]
         */
        actual fun createLocale(language: String, country: String): KalugaLocale = KalugaLocale(language, country)

        /**
         * Creates a [KalugaLocale] based on a ISO 639 alpha-2 or alpha-3 `language` code, ISO 3166 alpha-2 `country` code, and variant code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @param variant Arbitrary value used to indicate a variation of a [KalugaLocale]
         * @return The [KalugaLocale] for the given [language], [country], and [variant]
         */
        actual fun createLocale(language: String, country: String, variant: String): KalugaLocale = KalugaLocale(language, country, variant)

        /**
         * The default [KalugaLocale] of the user
         */
        actual val defaultLocale: KalugaLocale get() = KalugaLocale("en")

        /**
         * A list of [KalugaLocale] available to the user.
         */
        actual val availableLocales: List<KalugaLocale> = emptyList()
    }

    actual override fun name(forLocale: KalugaLocale): String = "${languageCode}_${countryCode}_$variantCode"
    actual override fun countryName(forLocale: KalugaLocale): String = countryCode
    actual override fun languageName(forLocale: KalugaLocale): String = languageCode
    actual override fun variantName(forLocale: KalugaLocale): String = variantCode
    actual override fun scriptName(forLocale: KalugaLocale): String = scriptCode

    actual override val quotationStart: String = "\""
    actual override val quotationEnd: String = "\""
    actual override val alternateQuotationStart: String = "\""
    actual override val alternateQuotationEnd: String = "\""
}
