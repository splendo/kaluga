/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.resources

import com.splendo.kaluga.resources.stylable.TextStyle

actual data class StyledString(
    val string: String,
    actual val defaultTextStyle: TextStyle,
    actual val linkStyle: LinkStyle?,
    val attributed: List<Pair<StringStyleAttribute, IntRange>>
)

actual class StyledStringBuilder constructor(string: String, defaultTextStyle: TextStyle, linkStyle: LinkStyle?) {

    actual class Provider {
        actual fun provide(string: String, defaultTextStyle: TextStyle, linkStyle: LinkStyle?) = StyledStringBuilder(string, defaultTextStyle, linkStyle)
    }

    var styledString = StyledString(string, defaultTextStyle, linkStyle, emptyList())
    actual fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange) {
        styledString = styledString.copy(
            attributed = styledString.attributed.toMutableList().apply {
                add(Pair(attribute, range))
            }
        )
    }
    actual fun create(): StyledString = styledString
}

actual val StyledString.rawString: String get() = string
