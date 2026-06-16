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

package com.splendo.kaluga.datetime
import com.splendo.kaluga.base.i18n.KalugaLocale


/**
 * Indicates whether this locale use a 24 hour clock cycle.
 */
val KalugaLocale.uses24HourClock: Boolean get() {
    val formatter = KalugaDateFormatter.timeFormat(DateFormatStyle.Medium, locale = this)
    val formattedDate = formatter.format(DefaultKalugaDate.now())
    return !formattedDate.contains(formatter.amString) && !formattedDate.contains(formatter.pmString)
}
