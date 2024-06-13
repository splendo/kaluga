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

import com.splendo.kaluga.base.text.DateFormatStyle
import com.splendo.kaluga.base.text.KalugaDateFormatter

/**
 * An object representing a specific geographical, political, or cultural region.
 */
abstract class BaseLocale {
    /**
     *  ISO 3166 alpha-2 country code or UN M.49 numeric-3 area code.
     *
     *  Example: "US" (United States), "FR" (France), "029" (Caribbean)
     */
    abstract val countryCode: String

    /**
     * ISO 639 alpha-2 or alpha-3 language code.
     *
     * Example: "en" (English), "ja" (Japanese), "kok" (Konkani)
     */
    abstract val languageCode: String

    /**
     * ISO 15924 alpha-4 script code.
     *
     * Example: "Latn" (Latin), "Cyrl" (Cyrillic)
     */
    abstract val scriptCode: String

    /**
     * Any arbitrary value used to indicate a variation of a [KalugaLocale].
     * Where there are two or more variant values each indicating its own semantics, these values should be ordered by importance, with most important first, separated by underscore('_').
     *
     * Example: "polyton" (Polytonic Greek), "POSIX"
     */
    abstract val variantCode: String

    /**
     * Returns [UnitSystem] for the [KalugaLocale].
     */
    abstract val unitSystem: UnitSystem

    /**
     * Gets the name of the [KalugaLocale], localized according to a given [KalugaLocale]
     * @param forLocale The [KalugaLocale] for which the name should be localized
     * @return The name of this [KalugaLocale]
     */
    abstract fun name(forLocale: KalugaLocale): String

    /**
     * Gets the county name of the [KalugaLocale], localized according to a given [KalugaLocale]
     * @param forLocale The [KalugaLocale] for which the country name should be localized
     * @return The country name of this [KalugaLocale]
     */
    abstract fun countryName(forLocale: KalugaLocale): String

    /**
     * Gets the language name of the [KalugaLocale], localized according to a given [KalugaLocale]
     * @param forLocale The [KalugaLocale] for which the language name should be localized
     * @return The language name of this [KalugaLocale]
     */
    abstract fun languageName(forLocale: KalugaLocale): String

    /**
     * Gets the variant name of the [KalugaLocale], localized according to a given [KalugaLocale]
     * @param forLocale The [KalugaLocale] for which the variant name should be localized
     * @return The variant name of this [KalugaLocale]
     */
    abstract fun variantName(forLocale: KalugaLocale): String

    /**
     * Gets the script name of the [KalugaLocale], localized according to a given [KalugaLocale]
     * @param forLocale The [KalugaLocale] for which the script name should be localized
     * @return The script name of this [KalugaLocale]
     */
    abstract fun scriptName(forLocale: KalugaLocale): String

    /**
     * The Character(s) used for indicating the start of a quote
     */
    abstract val quotationStart: String

    /**
     * The Character(s) used for indicating the end of a quote
     */
    abstract val quotationEnd: String

    /**
     * The alternative Character(s) used for indicating the start of a quote
     */
    abstract val alternateQuotationStart: String

    /**
     * The alternative Character(s) used for indicating the end of a quote
     */
    abstract val alternateQuotationEnd: String

    override fun toString(): String {
        return listOf(languageCode, countryCode, variantCode).filterNot { it.isEmpty() }.joinToString("_")
    }
}

/**
 * Default implementation of [BaseLocale]
 */
expect class KalugaLocale : BaseLocale {
    companion object {

        /**
         * Creates a [KalugaLocale] based on a `language` ISO 639 alpha-2 or alpha-3 code
         * @param language a `language` ISO 639 alpha-2 or alpha-3 code.
         * @return The [KalugaLocale] for the given [language]
         */
        fun createLocale(language: String): KalugaLocale

        /**
         * Creates a [KalugaLocale] based on a ISO 639 alpha-2 or alpha-3 `language` code and ISO 3166 alpha-2 `country` code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @return The [KalugaLocale] for the given [language] and [country]
         */
        fun createLocale(language: String, country: String): KalugaLocale

        /**
         * Creates a [KalugaLocale] based on a ISO 639 alpha-2 or alpha-3 `language` code, ISO 3166 alpha-2 `country` code, and variant code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @param variant Arbitrary value used to indicate a variation of a [KalugaLocale]
         * @return The [KalugaLocale] for the given [language], [country], and [variant]
         */
        fun createLocale(language: String, country: String, variant: String): KalugaLocale

        /**
         * The default [KalugaLocale] of the user
         */
        val defaultLocale: KalugaLocale

        /**
         * A list of [KalugaLocale] available to the user.
         */
        val availableLocales: List<KalugaLocale>
    }

    override val countryCode: String
    override val languageCode: String
    override val scriptCode: String
    override val variantCode: String
    override val unitSystem: UnitSystem
    override val quotationStart: String
    override val quotationEnd: String
    override val alternateQuotationStart: String
    override val alternateQuotationEnd: String

    override fun name(forLocale: KalugaLocale): String
    override fun countryName(forLocale: KalugaLocale): String
    override fun languageName(forLocale: KalugaLocale): String
    override fun variantName(forLocale: KalugaLocale): String
    override fun scriptName(forLocale: KalugaLocale): String
}

/**
 * Locale for English/US in a POSIX format. Useful shortcut when dealing with fixed locale formats.
 */
val KalugaLocale.Companion.enUsPosix get() = createLocale("en", "US", "POSIX")

/**
 * Indicates whether this locale use a 24 hour clock cycle.
 */
val KalugaLocale.uses24HourClock: Boolean get() {
    val formatter = KalugaDateFormatter.timeFormat(DateFormatStyle.Medium, locale = this)
    val formattedDate = formatter.format(DefaultKalugaDate.now())
    return !formattedDate.contains(formatter.amString) && !formattedDate.contains(formatter.pmString)
}
