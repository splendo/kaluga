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

import com.splendo.kaluga.base.text.upperCased

/**
 * Default implementation of [BaseLocale]
 */
actual data class Locale internal constructor(internal val locale: java.util.Locale) : BaseLocale() {
    actual companion object {

        /**
         * Creates a [Locale] based on a `language` ISO 639 alpha-2 or alpha-3 code
         * @param language a `language` ISO 639 alpha-2 or alpha-3 code.
         * @return The [Locale] for the given [language]
         */
        actual fun createLocale(language: String): Locale = Locale(java.util.Locale(language))

        /**
         * Creates a [Locale] based on a ISO 639 alpha-2 or alpha-3 `language` code and ISO 3166 alpha-2 `country` code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @return The [Locale] for the given [language] and [country]
         */
        actual fun createLocale(language: String, country: String): Locale = Locale(java.util.Locale(language, country))

        /**
         * Creates a [Locale] based on a ISO 639 alpha-2 or alpha-3 `language` code, ISO 3166 alpha-2 `country` code, and variant code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @param variant Arbitrary value used to indicate a variation of a [Locale]
         * @return The [Locale] for the given [language], [country], and [variant]
         */
        actual fun createLocale(language: String, country: String, variant: String): Locale = Locale(java.util.Locale(language, country, variant))

        /**
         * The default [Locale] of the user
         */
        actual val defaultLocale: Locale get() = Locale(java.util.Locale.getDefault())

        /**
         * A list of [Locale] available to the user.
         */
        actual val availableLocales: List<Locale> = java.util.Locale.getAvailableLocales().asList().map { Locale(it) }
    }

    override val countryCode: String
        get() = locale.country
    override val languageCode: String
        get() = locale.language
    override val scriptCode: String
        get() = locale.script
    override val variantCode: String
        get() = locale.variant
    override val unitSystem: UnitSystem
        get() = UnitSystem.withCountryCode(countryCode.upperCased(this))

    override fun name(forLocale: Locale): String = locale.getDisplayName(forLocale.locale)
    override fun countryName(forLocale: Locale): String = locale.getDisplayCountry(forLocale.locale)
    override fun languageName(forLocale: Locale): String = locale.getDisplayLanguage(forLocale.locale)
    override fun variantName(forLocale: Locale): String = locale.getDisplayVariant(forLocale.locale)
    override fun scriptName(forLocale: Locale): String = locale.getDisplayScript(forLocale.locale)

    override val quotationStart: String = "\""
    override val quotationEnd: String = "\""
    override val alternateQuotationStart: String = "\""
    override val alternateQuotationEnd: String = "\""
}
