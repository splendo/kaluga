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

package com.splendo.kaluga.datetime

import com.splendo.kaluga.formatted.Modifier

class RoundingModifier(val minutesStep: Int) : Modifier<DateTime> {
    override fun apply(value: DateTime): DateTime = if (minutesStep in 2..30) {
        val millisInMinute = 60 * 1000
        val millisStep = minutesStep * millisInMinute
        val remainder = (value.timestamp % millisStep)
        val additive = if (remainder < millisStep / 2) 0 else millisStep
        val base = value.timestamp - remainder
        val roundedTimestamp = base + additive
        DateTime(roundedTimestamp)
    } else value
}
