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

package com.splendo.kaluga.datetime.datecomponents

import com.splendo.kaluga.datetime.DateTime
import com.splendo.kaluga.formatted.Formatted
import com.splendo.kaluga.formatted.Formatter
import com.splendo.kaluga.formatted.Modifier

data class FormattedDateComponents(
    override val value: DateComponents,
    override val formatter: Formatter<DateComponents> = DateComponentsFormatter.formatterForComponent(value),
    override val modifier: Modifier<DateComponents>? = null
) : Formatted<DateComponents, FormattedDateComponents> {
    override fun spawn(
        value: DateComponents?,
        formatter: Formatter<DateComponents>,
        modifier: Modifier<DateComponents>?
    ): FormattedDateComponents =
        FormattedDateComponents(value ?: DateComponents.undefinedDay(), formatter, modifier)

    fun new(dateTime: DateTime): FormattedDateComponents? =
        new(value = value?.new(dateTime))
}
