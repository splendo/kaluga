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

// TODO Implement with proper locale solution for Java Script
actual class Locale internal constructor(
    actual val languageCode: String,
    actual val countryCode: String = "",
    actual val variantCode: String = "",
    actual val scriptCode: String = "",
    actual val unitSystem: UnitSystem = UnitSystem.METRIC
) {

    actual companion object {
        actual fun createLocale(language: String): Locale = Locale(language)
        actual fun createLocale(language: String, country: String): Locale = Locale(language, country)
        actual fun createLocale(language: String, country: String, variant: String): Locale = Locale(language, country, variant)

        actual val defaultLocale: Locale get() = Locale("en")
        actual val availableLocales: List<Locale> = emptyList()
    }

    actual fun name(forLocale: Locale): String = "${languageCode}_${countryCode}_$variantCode"
    actual fun countryName(forLocale: Locale): String = countryCode
    actual fun languageName(forLocale: Locale): String = languageCode
    actual fun variantName(forLocale: Locale): String = variantCode
    actual fun scriptName(forLocale: Locale): String = scriptCode

    actual val quotationStart: String = "\""
    actual val quotationEnd: String = "\""
    actual val alternateQuotationStart: String = "\""
    actual val alternateQuotationEnd: String = "\""
}
