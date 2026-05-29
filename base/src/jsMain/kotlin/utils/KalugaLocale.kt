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
 * Uses [`Intl.Locale`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/Locale)
 * for parsing BCP 47 language tags and
 * [`Intl.DisplayNames`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/DisplayNames)
 * for localized names. Modern Node.js and browsers ship with full ICU data, giving feature parity with
 * `java.util.Locale` (Android/JVM) and `NSLocale` (iOS).
 */
actual data class KalugaLocale internal constructor(internal val tag: String) : BaseLocale() {

    private val parsed: dynamic = parseIntlLocale(tag)

    actual override val languageCode: String = parsed.language.unsafeCast<String?>() ?: ""
    actual override val countryCode: String = parsed.region.unsafeCast<String?>() ?: ""
    actual override val scriptCode: String = parsed.script.unsafeCast<String?>() ?: ""
    actual override val variantCode: String = extractVariant(parsed, languageCode, scriptCode, countryCode)

    actual override val unitSystem: UnitSystem
        get() = resolveMeasurementSystem(parsed) ?: UnitSystem.withCountryCode(countryCode.upperCased(this))

    actual override fun name(forLocale: KalugaLocale): String =
        intlDisplayName("language", forLocale.tag, tag, fallback = tag)
    actual override fun countryName(forLocale: KalugaLocale): String =
        if (countryCode.isEmpty()) "" else intlDisplayName("region", forLocale.tag, countryCode, fallback = countryCode)
    actual override fun languageName(forLocale: KalugaLocale): String =
        if (languageCode.isEmpty()) "" else intlDisplayName("language", forLocale.tag, languageCode, fallback = languageCode)
    actual override fun variantName(forLocale: KalugaLocale): String = variantCode
    actual override fun scriptName(forLocale: KalugaLocale): String =
        if (scriptCode.isEmpty()) "" else intlDisplayName("script", forLocale.tag, scriptCode, fallback = scriptCode)

    actual override val quotationStart: String = "\""
    actual override val quotationEnd: String = "\""
    actual override val alternateQuotationStart: String = "\""
    actual override val alternateQuotationEnd: String = "\""

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
            resolveSupportedLocaleTags().map { KalugaLocale(it) }
        }
    }
}

private fun parseIntlLocale(tag: String): dynamic = try {
    newIntlLocale(tag)
} catch (_: dynamic) {
    newIntlLocale("und")
}

private fun canonicalizeTag(tag: String): String = try {
    newIntlLocale(tag).baseName.unsafeCast<String>()
} catch (_: dynamic) {
    tag
}

private fun extractVariant(parsed: dynamic, language: String, script: String, region: String): String {
    val baseName = parsed.baseName.unsafeCast<String?>() ?: return ""
    val parts = baseName.split("-").toMutableList()
    if (parts.isNotEmpty() && parts[0].equals(language, ignoreCase = true)) parts.removeAt(0)
    if (script.isNotEmpty() && parts.isNotEmpty() && parts[0].equals(script, ignoreCase = true)) parts.removeAt(0)
    if (region.isNotEmpty() && parts.isNotEmpty() && parts[0].equals(region, ignoreCase = true)) parts.removeAt(0)

    // `Intl.Locale` canonicalises legacy variant subtags such as `POSIX` into the Unicode
    // extension form `-u-va-<variant>` rather than keeping them as BCP 47 variant subtags.
    // Unwrap that back so callers see the original variant they passed in.
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
    // Split construction from method call: Kotlin/JS's `js(...)` rewrites
    // `new X(args).method(args)` into `new (X(args).method)(args)`, so combining them in one
    // string applies `new` to the method instead of the constructor.
    val displayNames: dynamic = js("new Intl.DisplayNames([displayLocale], { type: type })")
    val resolved = displayNames.of(code)
    if (resolved == null) fallback else resolved.unsafeCast<String>()
} catch (_: dynamic) {
    fallback
}

/**
 * Reads the measurement system exposed by `Intl.Locale.prototype.getMeasurementInfo()` /
 * `Intl.Locale.prototype.measurementSystem`, when present (recent ICU). Returns `null` when the
 * runtime doesn't expose it, so the caller can fall back to a country-code mapping.
 */
private fun resolveMeasurementSystem(parsed: dynamic): UnitSystem? = try {
    val raw = js(
        "(typeof parsed.getMeasurementInfo === 'function' ? parsed.getMeasurementInfo().measurementSystem : parsed.measurementSystem) || null",
    )
    when (raw) {
        "ussystem", "us" -> UnitSystem.IMPERIAL
        "uksystem", "uk" -> UnitSystem.MIXED
        "metric" -> UnitSystem.METRIC
        else -> null
    }
} catch (_: dynamic) {
    null
}

private fun resolveCurrentLocaleTag(): String = js(
    "(typeof navigator !== 'undefined' && navigator.language) ? navigator.language : " +
        "(typeof Intl !== 'undefined' && Intl.DateTimeFormat ? Intl.DateTimeFormat().resolvedOptions().locale : 'en-US')",
).unsafeCast<String>()

private fun resolveSupportedLocaleTags(): List<String> = try {
    val arr = js("(typeof Intl !== 'undefined' && typeof Intl.supportedValuesOf === 'function') ? Intl.supportedValuesOf('language') : null")
    if (arr == null) fallbackLocaleTags else arr.unsafeCast<Array<String>>().toList()
} catch (_: dynamic) {
    fallbackLocaleTags
}

private val fallbackLocaleTags = listOf(
    "ar", "bg", "ca", "cs", "da", "de", "el", "en", "es", "et", "fa", "fi", "fr",
    "he", "hi", "hr", "hu", "id", "it", "ja", "ko", "lt", "lv", "nb", "nl", "pl",
    "pt", "ro", "ru", "sk", "sl", "sv", "th", "tr", "uk", "vi", "zh",
)
