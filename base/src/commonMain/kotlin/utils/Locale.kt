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
 * An operation that requires a Locale to perform its task is called locale-sensitive and uses the [Locale] to tailor information for the user.
 * For example, displaying a number is a locale-sensitive operation— the number should be formatted according to the customs and conventions of the user's native country, region, or culture.
 *
 * The [Locale] class implements identifiers interchangeable with BCP 47 (IETF BCP 47, "Tags for Identifying Languages"), with support for the LDML (UTS#35, "Unicode Locale Data Markup Language") BCP 47-compatible extensions for locale data exchange.
 *
 *  A [Locale] object logically consists of the fields described below.
 *
 * ### language ###
 *     ISO 639 alpha-2 or alpha-3 language code, or registered language subtags up to 8 alpha letters (for future enhancements).
 *     When a language has both an alpha-2 code and an alpha-3 code, the alpha-2 code must be used.
 *     You can find a full list of valid language codes in the IANA Language Subtag Registry (search for "Type: language").
 *     The language field is case insensitive, but [Locale] always canonicalizes to lower case.
 *
 *     Well-formed language values have the form [a-zA-Z]{2,8}.
 *     Note that this is not the the full BCP47 language production, since it excludes extlang.
 *     They are not needed since modern three-letter language codes replace them.
 *
 *     Example: "en" (English), "ja" (Japanese), "kok" (Konkani)
 *
 * ### country (region) ###
 *     ISO 3166 alpha-2 country code or UN M.49 numeric-3 area code.
 *     You can find a full list of valid country and region codes in the IANA Language Subtag Registry (search for "Type: region").
 *     The country (region) field is case insensitive, but [Locale] always canonicalizes to upper case.
 *
 *     Well-formed country/region values have the form [a-zA-Z]{2} | [0-9]{3}
 *
 *     Example: "US" (United States), "FR" (France), "029" (Caribbean)
 *
 * ### variant ###
 *
 */
expect class Locale {
    companion object {
        fun createLocale(language: String): Locale
        fun createLocale(language: String, country: String): Locale
        fun createLocale(language: String, country: String, variant: String): Locale

        val defaultLocale: Locale
        val availableLocales: List<Locale>
    }

    val countryCode: String
    val languageCode: String
    val scriptCode: String
    val variantCode: String

    fun name(forLocale: Locale): String
    fun countryName(forLocale: Locale): String
    fun languageName(forLocale: Locale): String
    fun variantName(forLocale: Locale): String
    fun scriptName(forLocale: Locale): String

    val quotationStart: String
    val quotationEnd: String
    val alternateQuotationStart: String
    val alternateQuotationEnd: String
}
