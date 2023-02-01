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

import platform.Foundation.NSLocale
import platform.Foundation.alternateQuotationBeginDelimiter
import platform.Foundation.alternateQuotationEndDelimiter
import platform.Foundation.availableLocaleIdentifiers
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.localeIdentifier
import platform.Foundation.localizedStringForCountryCode
import platform.Foundation.localizedStringForLanguageCode
import platform.Foundation.localizedStringForLocaleIdentifier
import platform.Foundation.localizedStringForScriptCode
import platform.Foundation.localizedStringForVariantCode
import platform.Foundation.quotationBeginDelimiter
import platform.Foundation.quotationEndDelimiter
import platform.Foundation.scriptCode
import platform.Foundation.variantCode

/**
 * Default implementation of [BaseLocale]
 * @property nsLocale the internal [NSLocale] tracking this locale.
 */
actual data class Locale internal constructor(val nsLocale: NSLocale) : BaseLocale() {

    actual companion object {

        /**
         * Creates a [Locale] based on a `language` ISO 639 alpha-2 or alpha-3 code
         * @param language a `language` ISO 639 alpha-2 or alpha-3 code.
         * @return The [Locale] for the given [language]
         */
        actual fun createLocale(language: String): Locale = Locale(NSLocale(language))

        /**
         * Creates a [Locale] based on a ISO 639 alpha-2 or alpha-3 `language` code and ISO 3166 alpha-2 `country` code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @return The [Locale] for the given [language] and [country]
         */
        actual fun createLocale(language: String, country: String): Locale = Locale(NSLocale("${language}_$country"))

        /**
         * Creates a [Locale] based on a ISO 639 alpha-2 or alpha-3 `language` code, ISO 3166 alpha-2 `country` code, and variant code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @param variant Arbitrary value used to indicate a variation of a [Locale]
         * @return The [Locale] for the given [language], [country], and [variant]
         */
        actual fun createLocale(language: String, country: String, variant: String): Locale = Locale(NSLocale("${language}_${country}_$variant"))

        /**
         * The default [Locale] of the user
         */
        actual val defaultLocale: Locale get() = Locale(NSLocale.currentLocale)

        /**
         * A list of [Locale] available to the user.
         */
        actual val availableLocales: List<Locale> = NSLocale.availableLocaleIdentifiers.typedList<String>().map { Locale(NSLocale(it)) }
    }

    override val countryCode: String
        get() = nsLocale.countryCode ?: ""
    override val languageCode: String
        get() = nsLocale.languageCode
    override val scriptCode: String
        get() = nsLocale.scriptCode ?: ""
    override val variantCode: String
        get() = nsLocale.variantCode ?: ""
    override val unitSystem: UnitSystem
        get() = (nsLocale.objectForKey("kCFLocaleMeasurementSystemKey") as? String)?.let {
            UnitSystem.withRawValue(it)
        } ?: UnitSystem.METRIC

    override fun name(forLocale: Locale): String = forLocale.nsLocale.localizedStringForLocaleIdentifier(nsLocale.localeIdentifier)
    override fun countryName(forLocale: Locale): String = forLocale.nsLocale.localizedStringForCountryCode(countryCode) ?: ""
    override fun languageName(forLocale: Locale): String = forLocale.nsLocale.localizedStringForLanguageCode(languageCode) ?: ""
    override fun variantName(forLocale: Locale): String = forLocale.nsLocale.localizedStringForVariantCode(variantCode) ?: ""
    override fun scriptName(forLocale: Locale): String = forLocale.nsLocale.localizedStringForScriptCode(scriptCode) ?: ""

    override val quotationStart: String = nsLocale.quotationBeginDelimiter
    override val quotationEnd: String = nsLocale.quotationEndDelimiter
    override val alternateQuotationStart: String = nsLocale.alternateQuotationBeginDelimiter
    override val alternateQuotationEnd: String = nsLocale.alternateQuotationEndDelimiter
}
