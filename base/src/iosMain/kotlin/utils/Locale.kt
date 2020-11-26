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

import com.splendo.kaluga.base.typedList
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

actual class Locale internal constructor(val nsLocale: NSLocale) {

    actual companion object {
        actual fun createLocale(language: String): Locale = Locale(NSLocale(language))
        actual fun createLocale(language: String, country: String): Locale = Locale(NSLocale("${language}_$country"))
        actual fun createLocale(language: String, country: String, variant: String): Locale = Locale(NSLocale("${language}_${country}_$variant"))

        actual val defaultLocale: Locale get() = Locale(NSLocale.currentLocale)
        actual val availableLocales: List<Locale> = NSLocale.availableLocaleIdentifiers.typedList<String>().map { Locale(NSLocale(it)) }
    }

    actual val countryCode: String
        get() = nsLocale.countryCode ?: ""
    actual val languageCode: String
        get() = nsLocale.languageCode
    actual val scriptCode: String
        get() = nsLocale.scriptCode ?: ""
    actual val variantCode: String
        get() = nsLocale.variantCode ?: ""
    actual val unitSystem: UnitSystem
        get() = (nsLocale.objectForKey("kCFLocaleMeasurementSystemKey") as? String)?.let {
            UnitSystem.withRawValue(it)
        } ?: UnitSystem.Metric

    actual fun name(forLocale: Locale): String = forLocale.nsLocale.localizedStringForLocaleIdentifier(nsLocale.localeIdentifier)
    actual fun countryName(forLocale: Locale): String = forLocale.nsLocale.localizedStringForCountryCode(countryCode) ?: ""
    actual fun languageName(forLocale: Locale): String = forLocale.nsLocale.localizedStringForLanguageCode(languageCode) ?: ""
    actual fun variantName(forLocale: Locale): String = forLocale.nsLocale.localizedStringForVariantCode(variantCode) ?: ""
    actual fun scriptName(forLocale: Locale): String = forLocale.nsLocale.localizedStringForScriptCode(scriptCode) ?: ""

    actual val quotationStart: String = nsLocale.quotationBeginDelimiter
    actual val quotationEnd: String = nsLocale.quotationEndDelimiter
    actual val alternateQuotationStart: String = nsLocale.alternateQuotationBeginDelimiter
    actual val alternateQuotationEnd: String = nsLocale.alternateQuotationEndDelimiter
}
