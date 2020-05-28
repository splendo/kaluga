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

import com.splendo.kaluga.formatted.Formatted
import com.splendo.kaluga.formatted.Formatter
import com.splendo.kaluga.formatted.Modifier

/*
    This class should be used by VM to provide date to the UI level
    It contains DateTime model and information how it was formatted and rounded.
    If we need to create DateTime object with the same formatting and rounding we can use method new of this class
*/

data class FormattedDateTime(
    override val value: DateTime?,
    override val formatter: Formatter<DateTime>,
    override val modifier: Modifier<DateTime>? = null
) : Formatted<DateTime, FormattedDateTime> {
    override fun spawn(
        value: DateTime?,
        formatter: Formatter<DateTime>,
        modifier: Modifier<DateTime>?
    ): FormattedDateTime =
        FormattedDateTime(value, formatter, modifier)
}
