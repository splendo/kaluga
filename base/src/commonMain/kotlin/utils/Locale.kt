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

/**
 * A [Locale] object represents a specific geographical, political, or cultural region.
 */
expect class Locale {
    companion object {

        /**
         * Creates a [Locale] based on a `language` ISO 639 alpha-2 or alpha-3 code
         * @param language a `language` ISO 639 alpha-2 or alpha-3 code.
         * @return The [Locale] for the given [language]
         */
        fun createLocale(language: String): Locale

        /**
         * Creates a [Locale] based on a ISO 639 alpha-2 or alpha-3 `language` code and ISO 3166 alpha-2 `country` code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @return The [Locale] for the given [language] and [country]
         */
        fun createLocale(language: String, country: String): Locale

        /**
         * Creates a [Locale] based on a ISO 639 alpha-2 or alpha-3 `language` code, ISO 3166 alpha-2 `country` code, and variant code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @param variant Arbitrary value used to indicate a variation of a [Locale]
         * @return The [Locale] for the given [language], [country], and [variant]
         */
        fun createLocale(language: String, country: String, variant: String): Locale

        val defaultLocale: Locale
        val availableLocales: List<Locale>
    }

    /**
     *  ISO 3166 alpha-2 country code or UN M.49 numeric-3 area code.
     *
     *  Example: "US" (United States), "FR" (France), "029" (Caribbean)
     */
    val countryCode: String

    /**
     * ISO 639 alpha-2 or alpha-3 language code.
     *
     * Example: "en" (English), "ja" (Japanese), "kok" (Konkani)
     */
    val languageCode: String

    /**
     * ISO 15924 alpha-4 script code.
     *
     * Example: "Latn" (Latin), "Cyrl" (Cyrillic)
     */
    val scriptCode: String

    /**
     * Any arbitrary value used to indicate a variation of a [Locale].
     * Where there are two or more variant values each indicating its own semantics, these values should be ordered by importance, with most important first, separated by underscore('_').
     *
     * Example: "polyton" (Polytonic Greek), "POSIX"
     */
    val variantCode: String

    /**
     * Returns [UnitSystem] for the [Locale].
     */
    val unitSystem: UnitSystem

    /**
     * Gets the name of the [Locale], localized according to a given [Locale]
     * @param forLocale The [Locale] for which the name should be localized
     * @return The name of this [Locale]
     */
    fun name(forLocale: Locale): String

    /**
     * Gets the county name of the [Locale], localized according to a given [Locale]
     * @param forLocale The [Locale] for which the country name should be localized
     * @return The country name of this [Locale]
     */
    fun countryName(forLocale: Locale): String

    /**
     * Gets the language name of the [Locale], localized according to a given [Locale]
     * @param forLocale The [Locale] for which the language name should be localized
     * @return The language name of this [Locale]
     */
    fun languageName(forLocale: Locale): String

    /**
     * Gets the variant name of the [Locale], localized according to a given [Locale]
     * @param forLocale The [Locale] for which the variant name should be localized
     * @return The variant name of this [Locale]
     */
    fun variantName(forLocale: Locale): String

    /**
     * Gets the script name of the [Locale], localized according to a given [Locale]
     * @param forLocale The [Locale] for which the script name should be localized
     * @return The script name of this [Locale]
     */
    fun scriptName(forLocale: Locale): String

    /**
     * The Character(s) used for indicating the start of a quote
     */
    val quotationStart: String
    /**
     * The Character(s) used for indicating the end of a quote
     */
    val quotationEnd: String
    /**
     * The alternative Character(s) used for indicating the start of a quote
     */
    val alternateQuotationStart: String
    /**
     * The alternative Character(s) used for indicating the end of a quote
     */
    val alternateQuotationEnd: String
}

/**
 * Locale for English/US in a POSIX format. Useful shortcut when dealing with fixed locale formats.
 */
val Locale.Companion.enUsPosix get() = createLocale("en", "US", "POSIX")
