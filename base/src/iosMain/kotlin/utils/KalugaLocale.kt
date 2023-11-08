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
actual data class KalugaLocale internal constructor(val nsLocale: NSLocale) : BaseLocale() {

    actual companion object {

        /**
         * Creates a [KalugaLocale] based on a `language` ISO 639 alpha-2 or alpha-3 code
         * @param language a `language` ISO 639 alpha-2 or alpha-3 code.
         * @return The [KalugaLocale] for the given [language]
         */
        actual fun createLocale(language: String): KalugaLocale = createLocale(language, "")

        /**
         * Creates a [KalugaLocale] based on a ISO 639 alpha-2 or alpha-3 `language` code and ISO 3166 alpha-2 `country` code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @return The [KalugaLocale] for the given [language] and [country]
         */
        actual fun createLocale(language: String, country: String): KalugaLocale = createLocale(language, country, "")

        /**
         * Creates a [KalugaLocale] based on a ISO 639 alpha-2 or alpha-3 `language` code, ISO 3166 alpha-2 `country` code, and variant code.
         * @param language a ISO 639 alpha-2 or alpha-3 `language` code.
         * @param country a ISO 3166 alpha-2 `country` code.
         * @param variant Arbitrary value used to indicate a variation of a [KalugaLocale]
         * @return The [KalugaLocale] for the given [language], [country], and [variant]
         */
        actual fun createLocale(language: String, country: String, variant: String): KalugaLocale =
            KalugaLocale(NSLocale(listOf(language, country, variant).filter { it.isNotEmpty() }.joinToString("_")))

        /**
         * The default [KalugaLocale] of the user
         */
        actual val defaultLocale: KalugaLocale get() = KalugaLocale(NSLocale.currentLocale)

        /**
         * A list of [KalugaLocale] available to the user.
         */
        actual val availableLocales: List<KalugaLocale> = NSLocale.availableLocaleIdentifiers.typedList<String>().map { KalugaLocale(NSLocale(it)) }
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

    override fun name(forLocale: KalugaLocale): String = forLocale.nsLocale.localizedStringForLocaleIdentifier(nsLocale.localeIdentifier)
    override fun countryName(forLocale: KalugaLocale): String = forLocale.nsLocale.localizedStringForCountryCode(countryCode) ?: ""
    override fun languageName(forLocale: KalugaLocale): String = forLocale.nsLocale.localizedStringForLanguageCode(languageCode) ?: ""
    override fun variantName(forLocale: KalugaLocale): String = forLocale.nsLocale.localizedStringForVariantCode(variantCode) ?: ""
    override fun scriptName(forLocale: KalugaLocale): String = forLocale.nsLocale.localizedStringForScriptCode(scriptCode) ?: ""

    override val quotationStart: String = nsLocale.quotationBeginDelimiter
    override val quotationEnd: String = nsLocale.quotationEndDelimiter
    override val alternateQuotationStart: String = nsLocale.alternateQuotationBeginDelimiter
    override val alternateQuotationEnd: String = nsLocale.alternateQuotationEndDelimiter
}
