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

enum class UnitSystem {

    METRIC, MIXED, IMPERIAL;

    companion object {
        // FIXME IMPERIAL and US customary seem reversed
        fun withRawValue(value: String): UnitSystem = when (value) {
            "U.S." -> IMPERIAL
            "U.K." -> MIXED
            else -> METRIC
        }

        // FIXME IMPERIAL and US customary seem reversed
        fun withCountryCode(code: String): UnitSystem = when {
            listOf("GB", "MM", "LR").contains(code) -> MIXED
            "US" == code -> IMPERIAL
            else -> METRIC
        }
    }
}
