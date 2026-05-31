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

package com.splendo.kaluga.base.text

import android.os.Build
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

actual val USDForNL: String = "US$"
actual val JPYForUS: String = "¥"
actual val JPYForNL: String = "JP¥"

// API 24 ships an older CLDR/tz data set than later releases. Switch the expected values
// for the affected formatters when running on N (API 24); newer devices match every other platform.
private val isLegacyAndroidI18n: Boolean = Build.VERSION.SDK_INT <= Build.VERSION_CODES.N

actual val expectedFrenchMediumTime: String = if (isLegacyAndroidI18n) "1:37:42 PM" else "13:37:42"
actual val parseAbbreviationTolerance: Duration = if (isLegacyAndroidI18n) 1.hours else Duration.ZERO
actual val expectedNlPdtZoneName: String = if (isLegacyAndroidI18n) "GMT-07:00" else "PDT"
