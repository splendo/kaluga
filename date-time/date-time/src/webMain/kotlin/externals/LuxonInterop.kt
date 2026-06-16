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

package com.splendo.kaluga.datetime.externals

/**
 * The `luxon` `DateTime` class object, exposing the static factories Kaluga uses.
 */
internal external interface LuxonDateTimeStatic {
    fun fromMillis(milliseconds: Double, options: LuxonOptions): LuxonDateTime
    fun fromObject(values: LuxonDateValues, options: LuxonOptions): LuxonDateTime
    fun fromFormat(text: String, format: String, options: LuxonOptions): LuxonDateTime
    fun now(): LuxonDateTime
}

/**
 * The `luxon` `Info` namespace.
 */
internal external interface LuxonInfo {
    fun isValidIANAZone(zone: String): Boolean
}

/**
 * A `luxon` [`DateTime`](https://moment.github.io/luxon/api-docs/index.html#datetime) instance.
 */
internal external interface LuxonDateTime {
    val year: Int
    val month: Int
    val day: Int
    val hour: Int
    val minute: Int
    val second: Int
    val millisecond: Int
    val weekday: Int
    val ordinal: Int
    val daysInMonth: Int
    val isInLeapYear: Boolean
    val zoneName: String
    val locale: String?
    val offset: Int
    val isInDST: Boolean
    val isValid: Boolean
    fun setZone(zone: String): LuxonDateTime
    fun startOf(unit: String): LuxonDateTime
    fun toMillis(): Double
    fun toFormat(format: String): String
}

/** Options object accepted by the `DateTime` factories. */
internal external interface LuxonOptions {
    var zone: String?
    var locale: String?
}

/** Opaque date-component object accepted by [LuxonDateTimeStatic.fromObject]. */
internal external interface LuxonDateValues

// The `luxon` module binding needs `@JsModule` + `@JsNonModule` on js (UMD) but only `@JsModule` on
// wasmJs, so it can't be shared. These expects route to the per-target binding instead.
internal expect fun luxonFromMillis(milliseconds: Double, options: LuxonOptions): LuxonDateTime
internal expect fun luxonFromObject(values: LuxonDateValues, options: LuxonOptions): LuxonDateTime
internal expect fun luxonFromFormat(text: String, format: String, options: LuxonOptions): LuxonDateTime
internal expect fun luxonNow(): LuxonDateTime
internal expect fun luxonIsValidIANAZone(zone: String): Boolean

private fun emptyLuxonOptions(): LuxonOptions = js("({})")

internal fun luxonOptions(zone: String, locale: String? = null): LuxonOptions {
    val options = emptyLuxonOptions()
    options.zone = zone
    if (locale != null) options.locale = locale
    return options
}

internal fun luxonDateValues(year: Int, month: Int, day: Int): LuxonDateValues = js("({ year: year, month: month, day: day })")

// Setters/arithmetic take objects with a runtime field name, so they're built inside the `js(...)`
// helper with a computed key — `dynamic` indexed assignment has no Kotlin/Wasm equivalent.
internal fun luxonSet(dateTime: LuxonDateTime, field: String, value: Int): LuxonDateTime = js("dateTime.set({ [field]: value })")
internal fun luxonPlus(dateTime: LuxonDateTime, unit: String, amount: Int): LuxonDateTime = js("dateTime.plus({ [unit]: amount })")
internal fun luxonMinus(dateTime: LuxonDateTime, unit: String, amount: Int): LuxonDateTime = js("dateTime.minus({ [unit]: amount })")
