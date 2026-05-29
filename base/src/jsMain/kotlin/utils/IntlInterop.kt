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

// Each js(...) below is a compiler intrinsic and must be a string literal — callers can't pass
// a constant — so these one-line helpers are the way to consolidate the constructor names.
// They also enforce the "construct in its own js(...), then chain in Kotlin" pattern that we
// rely on to dodge Kotlin/JS's `new X(args).method(args)` mangling.

internal fun newDateTimeFormat(tag: String, opts: dynamic): dynamic = js("new Intl.DateTimeFormat(tag, opts)")

internal fun newNumberFormat(tag: String, opts: dynamic): dynamic = js("new Intl.NumberFormat(tag, opts)")

internal fun newIntlLocale(tag: String): dynamic = js("new Intl.Locale(tag)")
