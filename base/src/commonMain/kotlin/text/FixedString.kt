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

import com.splendo.kaluga.base.utils.KalugaLocale

internal class FixedString internal constructor(private val out: StringBuilder, private val s: String, private val start: Int, private val end: Int) : FormatString {
    override val index: Int = -2

    override fun print(arg: Any?, locale: KalugaLocale) {
        out.append(s, start, end)
    }

    override fun toString(): String = s.substring(start, end)
}
