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

import com.splendo.kaluga.base.externals.DateTime
import com.splendo.kaluga.base.externals.Info
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * A default implementation of [BaseTimeZone] backed by the
 * [`luxon`](https://moment.github.io/luxon/) JS library, which computes timezone arithmetic via
 * the runtime's built-in `Intl.DateTimeFormat` — the same IANA tz data that ships with Node.js
 * and modern browsers.
 *
 * @property identifier the IANA timezone identifier (e.g. `"Europe/Amsterdam"`, `"UTC"`).
 */
actual class KalugaTimeZone internal constructor(actual override val identifier: String) : BaseTimeZone() {

    actual override fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean, locale: KalugaLocale): String =
        resolveTimeZoneName(identifier, style, withDaylightSavings, locale.tag)

    actual override val offsetFromGMT: Duration get() = computeOffsets(identifier).standard
    actual override val daylightSavingsOffset: Duration get() = computeOffsets(identifier).daylight

    actual override fun offsetFromGMTAtDate(date: KalugaDate): Duration {
        val ms = date.durationSinceEpoch.inWholeMilliseconds.toDouble()
        val zone = identifier
        val dt = DateTime.fromMillis(ms, js("({zone: zone})"))
        return (dt.offset.unsafeCast<Int>()).minutes
    }

    actual override fun usesDaylightSavingsTime(date: KalugaDate): Boolean {
        val ms = date.durationSinceEpoch.inWholeMilliseconds.toDouble()
        val zone = identifier
        val dt = DateTime.fromMillis(ms, js("({zone: zone})"))
        return dt.isInDST.unsafeCast<Boolean>()
    }

    actual override fun copy(): KalugaTimeZone = KalugaTimeZone(identifier)

    override fun equals(other: Any?): Boolean = (other as? KalugaTimeZone)?.let { identifier == it.identifier } ?: false
    override fun hashCode(): Int = identifier.hashCode()

    actual companion object {

        actual fun get(identifier: String): KalugaTimeZone? = if (isValidTimeZone(identifier)) {
            KalugaTimeZone(canonicalizeTimeZone(identifier))
        } else {
            null
        }

        actual fun current(): KalugaTimeZone = KalugaTimeZone(systemTimeZone())

        actual val availableIdentifiers: List<String> by lazy { listSupportedTimeZones() }
    }
}

private data class TimeZoneOffsets(val standard: Duration, val daylight: Duration)

private fun computeOffsets(zone: String): TimeZoneOffsets {
    val january = DateTime.fromObject(js("({year: 2024, month: 1, day: 15})"), js("({zone: zone})"))
    val july = DateTime.fromObject(js("({year: 2024, month: 7, day: 15})"), js("({zone: zone})"))
    val janOffset = january.offset.unsafeCast<Int>()
    val julOffset = july.offset.unsafeCast<Int>()
    val standard = minOf(janOffset, julOffset)
    val daylight = maxOf(janOffset, julOffset) - standard
    return TimeZoneOffsets(standard.minutes, daylight.minutes)
}

private fun isValidTimeZone(identifier: String): Boolean = try {
    Info.isValidIANAZone(identifier).unsafeCast<Boolean>() ||
        fallbackTimeZones.any { identifier.equals(it, ignoreCase = true) }
} catch (_: dynamic) {
    false
}

private fun canonicalizeTimeZone(identifier: String): String = try {
    val dt = DateTime.now().setZone(identifier)
    if (dt.isValid.unsafeCast<Boolean>()) dt.zoneName.unsafeCast<String>() else identifier
} catch (_: dynamic) {
    identifier
}

private fun systemTimeZone(): String = try {
    val resolved = js("(typeof Intl !== 'undefined' && Intl.DateTimeFormat) ? Intl.DateTimeFormat().resolvedOptions().timeZone : null")
    resolved?.unsafeCast<String>() ?: "UTC"
} catch (_: dynamic) {
    "UTC"
}

private fun listSupportedTimeZones(): List<String> = try {
    val arr = js("(typeof Intl !== 'undefined' && typeof Intl.supportedValuesOf === 'function') ? Intl.supportedValuesOf('timeZone') : null")
    if (arr == null) fallbackTimeZones else arr.unsafeCast<Array<String>>().toList()
} catch (_: dynamic) {
    fallbackTimeZones
}

private val fallbackTimeZones = listOf("UTC", "GMT")

private fun resolveTimeZoneName(zone: String, style: TimeZoneNameStyle, withDaylightSavings: Boolean, localeTag: String): String = try {
    val styleString = if (style == TimeZoneNameStyle.Short) "short" else "long"
    // Use a January date for standard time, a July date for daylight savings (matches northern-hemisphere observance).
    val referenceMillis = if (withDaylightSavings) DST_REFERENCE_MILLIS else STANDARD_REFERENCE_MILLIS
    val formatter = newDateTimeFormat(localeTag, js("({timeZone: zone, timeZoneName: styleString})"))
    val parts = formatter.formatToParts(js("new Date(referenceMillis)"))
    extractTimeZoneNamePart(parts) ?: zone
} catch (_: dynamic) {
    zone
}

// Jan 15, 2024 12:00 UTC and Jul 15, 2024 12:00 UTC — chosen to disambiguate STD vs DST naming.
private const val STANDARD_REFERENCE_MILLIS: Double = 1705320000000.0
private const val DST_REFERENCE_MILLIS: Double = 1721044800000.0

private fun extractTimeZoneNamePart(parts: dynamic): String? {
    val length = parts.length.unsafeCast<Int>()
    for (i in 0 until length) {
        val part = parts[i]
        if (part.type.unsafeCast<String>() == "timeZoneName") {
            return part.value.unsafeCast<String>()
        }
    }
    return null
}
