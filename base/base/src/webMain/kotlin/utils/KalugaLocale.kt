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
 * Default implementation of [BaseLocale] backed by the ECMAScript
 * [`Intl`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl) API.
 *
 * Shared by the JS family (js + wasmJs) via typed `Intl` externals.
 */
actual data class KalugaLocale internal constructor(internal val tag: String) : BaseLocale() {

    private val parsed: IntlLocale = parseIntlLocale(tag)

    actual override val languageCode: String = parsed.language ?: ""
    actual override val countryCode: String = parsed.region ?: ""
    actual override val scriptCode: String = parsed.script ?: ""
    actual override val variantCode: String = extractVariant(parsed, languageCode, scriptCode, countryCode)

    actual override val unitSystem: UnitSystem
        get() = resolveMeasurementSystem(parsed) ?: UnitSystem.withCountryCode(countryCode.upperCased(this))

    actual override fun name(forLocale: KalugaLocale): String = intlDisplayName("language", forLocale.tag, tag, fallback = tag)
    actual override fun countryName(forLocale: KalugaLocale): String =
        if (countryCode.isEmpty()) "" else intlDisplayName("region", forLocale.tag, countryCode, fallback = countryCode)
    actual override fun languageName(forLocale: KalugaLocale): String =
        if (languageCode.isEmpty()) "" else intlDisplayName("language", forLocale.tag, languageCode, fallback = languageCode)
    actual override fun variantName(forLocale: KalugaLocale): String = variantCode
    actual override fun scriptName(forLocale: KalugaLocale): String = if (scriptCode.isEmpty()) "" else intlDisplayName("script", forLocale.tag, scriptCode, fallback = scriptCode)

    actual override val quotationStart: String = "\""
    actual override val quotationEnd: String = "\""
    actual override val alternateQuotationStart: String = "\""
    actual override val alternateQuotationEnd: String = "\""

    // `data class` would otherwise auto-generate a useless `KalugaLocale(tag=…)` toString
    // that shadows the parent's `lang_country_variant` formatting. Delegate back to it.
    override fun toString(): String = super.toString()

    actual companion object {

        actual fun createLocale(language: String): KalugaLocale = build(language, "", "")
        actual fun createLocale(language: String, country: String): KalugaLocale = build(language, country, "")
        actual fun createLocale(language: String, country: String, variant: String): KalugaLocale = build(language, country, variant)

        private fun build(language: String, country: String, variant: String): KalugaLocale {
            val raw = buildList {
                if (language.isNotEmpty()) add(language)
                if (country.isNotEmpty()) add(country)
                if (variant.isNotEmpty()) add(variant)
            }.joinToString("-").ifEmpty { "und" }
            return KalugaLocale(canonicalizeTag(raw))
        }

        actual val defaultLocale: KalugaLocale
            get() = KalugaLocale(canonicalizeTag(resolveCurrentLocaleTag()))

        actual val availableLocales: List<KalugaLocale> by lazy {
            availableLocaleTags.map { KalugaLocale(canonicalizeTag(it)) }
        }
    }
}

/**
 * Typed handle to an [`Intl.Locale`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/Locale).
 */
private external interface IntlLocale {
    val language: String?
    val region: String?
    val script: String?
    val baseName: String?
}

private external interface IntlDisplayNames {
    fun of(code: String): String?
}

private fun createIntlLocale(tag: String): IntlLocale = js("new Intl.Locale(tag)")
private fun createDisplayNames(displayLocale: String, type: String): IntlDisplayNames = js("new Intl.DisplayNames([displayLocale], { type: type })")

private fun parseIntlLocale(tag: String): IntlLocale = try {
    createIntlLocale(tag)
} catch (_: Throwable) {
    createIntlLocale("und")
}

private fun canonicalizeTag(tag: String): String = try {
    createIntlLocale(tag).baseName ?: tag
} catch (_: Throwable) {
    tag
}

private fun extractVariant(parsed: IntlLocale, language: String, script: String, region: String): String {
    val baseName = parsed.baseName ?: return ""
    val parts = baseName.split("-").toMutableList()
    if (parts.isNotEmpty() && parts[0].equals(language, ignoreCase = true)) parts.removeAt(0)
    if (script.isNotEmpty() && parts.isNotEmpty() && parts[0].equals(script, ignoreCase = true)) parts.removeAt(0)
    if (region.isNotEmpty() && parts.isNotEmpty() && parts[0].equals(region, ignoreCase = true)) parts.removeAt(0)

    // Unwrap `Intl.Locale`'s `-u-va-<variant>` canonicalisation back into a plain variant subtag.
    val uIndex = parts.indexOf("u")
    if (uIndex < 0) return parts.joinToString("_")

    val bcp47Variants = parts.subList(0, uIndex)
    val vaIndex = parts.indexOf("va")
    val extensionVariants = if (vaIndex > uIndex && vaIndex + 1 < parts.size) {
        // The `va` value runs until the next 2-char extension key or the end of the tag.
        val end = (vaIndex + 1 until parts.size).firstOrNull { parts[it].length == 2 } ?: parts.size
        parts.subList(vaIndex + 1, end)
    } else {
        emptyList()
    }
    return (bcp47Variants + extensionVariants).joinToString("_")
}

private fun intlDisplayName(type: String, displayLocale: String, code: String, fallback: String): String = try {
    createDisplayNames(displayLocale, type).of(code) ?: fallback
} catch (_: Throwable) {
    fallback
}

// Reads the measurement system from `Intl.Locale.getMeasurementInfo()` / `.measurementSystem` (recent ICU),
// or `null` when the runtime doesn't expose it so the caller can fall back to a country-code mapping.
private fun measurementSystemTag(locale: IntlLocale): String? =
    js("(typeof locale.getMeasurementInfo === 'function' ? locale.getMeasurementInfo().measurementSystem : locale.measurementSystem) || null")

private fun resolveMeasurementSystem(parsed: IntlLocale): UnitSystem? = try {
    when (measurementSystemTag(parsed)) {
        "ussystem", "us" -> UnitSystem.IMPERIAL
        "uksystem", "uk" -> UnitSystem.MIXED
        "metric" -> UnitSystem.METRIC
        else -> null
    }
} catch (_: Throwable) {
    null
}

private fun resolveCurrentLocaleTag(): String = js("(typeof navigator !== 'undefined' && navigator.language) ? navigator.language : Intl.DateTimeFormat().resolvedOptions().locale")

// The web exposes no API to enumerate the locales a runtime supports (`Intl.supportedValuesOf` only
// covers calendars, currencies, time zones, etc. — not locales), so [availableLocales] is backed by the
// full CLDR locale set generated into `availableLocaleTags` (see `:base:generateAvailableLocales`).
