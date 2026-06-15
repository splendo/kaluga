/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.text

import com.splendo.kaluga.base.utils.KalugaLocale

/**
 * The String that serves as a line separator. Shared by the JS family (js + wasmJs).
 */
actual val lineSeparator: String = "\n"

// Typed `js(...)` helpers (no `dynamic`) so this is shared by the JS family. Each is the sole body of
// a function with String parameters/return, which is the form Kotlin/Wasm requires for `js(...)`.
private fun toLocaleLowerCase(value: String, tag: String): String = js("value.toLocaleLowerCase(tag)")
private fun toLocaleUpperCase(value: String, tag: String): String = js("value.toLocaleUpperCase(tag)")

/**
 * Converts a String to its lower cased variant based on a given [KalugaLocale], backed by JavaScript's
 * `String.prototype.toLocaleLowerCase` (applies locale-specific casing, e.g. Turkish dotted/dotless "I").
 */
actual fun String.lowerCased(locale: KalugaLocale): String = toLocaleLowerCase(this, locale.tag)

/**
 * Converts a String to its upper cased variant based on a given [KalugaLocale], backed by JavaScript's
 * `String.prototype.toLocaleUpperCase`.
 */
actual fun String.upperCased(locale: KalugaLocale): String = toLocaleUpperCase(this, locale.tag)
