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

package com.splendo.kaluga.resources

import com.splendo.kaluga.resources.stylable.KalugaTextStyle

/**
 * A text configured with [StringStyleAttribute]
 * @property string the String to style
 * @property defaultTextStyle The [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range.
 * This may be partially overwritten (e.g. [StringStyleAttribute.CharacterStyleAttribute.ForegroundColor] may overwrite [KalugaTextStyle.color])
 * @property linkStyle The [LinkStyle] to apply when [StringStyleAttribute.Link] is applied.
 * When `null` the Theme default will be used
 * @property attributed a list containing all [StringStyleAttribute] and the [IntRange] they should be applied to
 */
actual data class StyledString(
    val string: String,
    actual val defaultTextStyle: KalugaTextStyle,
    actual val linkStyle: LinkStyle?,
    val attributed: List<Pair<StringStyleAttribute, IntRange>>
)

/**
 * Builder for creating a [StyledString]
 * @param string the String to style
 * @param defaultTextStyle The [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range.
 * This may be partially overwritten (e.g. [StringStyleAttribute.CharacterStyleAttribute.ForegroundColor] may overwrite [KalugaTextStyle.color])
 * @param linkStyle The [LinkStyle] to apply when [StringStyleAttribute.Link] is applied.
 * When `null` the Theme default will be used
 */
actual class StyledStringBuilder constructor(
    string: String,
    defaultTextStyle: KalugaTextStyle,
    linkStyle: LinkStyle?
) {

    /**
     * Provider for a [StyledStringBuilder]
     */
    actual class Provider {

        /**
         * Provides a [StyledStringBuilder] to build a [StyledString] for a given text
         * @param string the text for which to build the [StyledString]
         * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
         * @param linkStyle the [LinkStyle] to apply when [StringStyleAttribute.Link] is applied
         * @return the [StyledStringBuilder] to build a [StyledString] for [string]
         */
        actual fun provide(string: String, defaultTextStyle: KalugaTextStyle, linkStyle: LinkStyle?) = StyledStringBuilder(string, defaultTextStyle, linkStyle)
    }

    private var styledString = StyledString(string, defaultTextStyle, linkStyle, emptyList())

    /**
     * Adds a [StringStyleAttribute] for a given range
     * @param attribute the [StringStyleAttribute] to apply
     * @param range the [IntRange] at which to apply the style
     * @throws [IndexOutOfBoundsException] if [range] is out of bounds for the text to span
     */
    actual fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange) {
        if (range.any { it !in styledString.string.indices }) {
            throw IndexOutOfBoundsException("Attribute cannot be applied to $range")
        }
        styledString = styledString.copy(
            attributed = styledString.attributed.toMutableList().apply {
                add(Pair(attribute, range))
            }
        )
    }

    /**
     * Creates the [StyledString]
     * @return the created [StyledString]
     */
    actual fun create(): StyledString = styledString
}

/**
 * Gets the plain string of a [StyledString]
 */
actual val StyledString.rawString: String get() = string
