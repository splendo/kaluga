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

import com.splendo.kaluga.resources.stylable.KalugaTextAlignment
import com.splendo.kaluga.resources.stylable.KalugaTextStyle

/**
 * A style to apply to a [StyledString] when [StringStyleAttribute.Link] is applied
 * @property color the [KalugaColor] of the text color of the link
 * @property isUnderlined if `true` the link will be underlined
 */
data class LinkStyle(val color: KalugaColor, val isUnderlined: Boolean)

/**
 * A text configured with [StringStyleAttribute]
 */
expect class StyledString {

    /**
     * The [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range.
     * This may be partially overwritten (e.g. [StringStyleAttribute.CharacterStyleAttribute.ForegroundColor] may overwrite [KalugaTextStyle.color])
     */
    val defaultTextStyle: KalugaTextStyle

    /**
     * The [LinkStyle] to apply when [StringStyleAttribute.Link] is applied.
     * When `null` the platform default will be used
     */
    val linkStyle: LinkStyle?
}

/**
 * Builder for creating a [StyledString]
 */
expect class StyledStringBuilder {

    /**
     * Provider for a [StyledStringBuilder]
     */
    class Provider {

        /**
         * Provides a [StyledStringBuilder] to build a [StyledString] for a given text
         * @param string the text for which to build the [StyledString]
         * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
         * @param linkStyle the [LinkStyle] to apply when [StringStyleAttribute.Link] is applied
         * @return the [StyledStringBuilder] to build a [StyledString] for [string]
         */
        fun provide(string: String, defaultTextStyle: KalugaTextStyle, linkStyle: LinkStyle? = null): StyledStringBuilder
    }

    /**
     * Adds a [StringStyleAttribute] for a given range
     * @param attribute the [StringStyleAttribute] to apply
     * @param range the [IntRange] at which to apply the style
     * @throws [IndexOutOfBoundsException] if [range] is out of bounds for the text to span
     */
    fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange)

    /**
     * Creates the [StyledString]
     * @return the created [StyledString]
     */
    fun create(): StyledString
}

/**
 * Gets the plain string of a [StyledString]
 */
expect val StyledString.rawString: String

/**
 * Creates a [StyledString] from a [String]
 * @param provider the [StyledStringBuilder.Provider] to provide the [StyledStringBuilder] used create a [StyledString]
 * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
 * @param attributes a collection of [StringStyleAttribute] to apply to the entire string
 * @return a [StyledString] styled with all [attributes]
 */
fun String.styled(provider: StyledStringBuilder.Provider, defaultTextStyle: KalugaTextStyle, vararg attributes: StringStyleAttribute) =
    styled(provider, defaultTextStyle, null, *attributes)

/**
 * Creates a [StyledString] from a [String]
 * @param provider the [StyledStringBuilder.Provider] to provide the [StyledStringBuilder] used create a [StyledString]
 * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
 * @param linkColor the [KalugaColor] to apply to the [LinkStyle] of the [StyledString]
 * @param attributes a collection of [StringStyleAttribute] to apply to the entire string
 * @return a [StyledString] styled with all [attributes]
 */
fun String.styled(provider: StyledStringBuilder.Provider, defaultTextStyle: KalugaTextStyle, linkColor: KalugaColor, vararg attributes: StringStyleAttribute) =
    styled(provider, defaultTextStyle, LinkStyle(linkColor, true), *attributes)

/**
 * Creates a [StyledString] from a [String]
 * @param provider the [StyledStringBuilder.Provider] to provide the [StyledStringBuilder] used create a [StyledString]
 * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
 * @param linkStyle the [LinkStyle] to apply to the [StyledString]
 * @param attributes a collection of [StringStyleAttribute] to apply to the entire string
 * @return a [StyledString] styled with all [attributes]
 */
fun String.styled(provider: StyledStringBuilder.Provider, defaultTextStyle: KalugaTextStyle, linkStyle: LinkStyle?, vararg attributes: StringStyleAttribute) = styled(
    provider,
    defaultTextStyle,
    linkStyle,
    *attributes.map<StringStyleAttribute, String.() -> Pair<StringStyleAttribute, IntRange>?> { attribute ->
        {
            attributeSubstring(this, attribute)
        }
    }.toTypedArray(),
)

/**
 * Creates a [StyledString] from a [String]
 * @param provider the [StyledStringBuilder.Provider] to provide the [StyledStringBuilder] used create a [StyledString]
 * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
 * @param attributes a collection methods that create a [Pair] of [StringStyleAttribute] and the [IntRange] at which it should be applied
 * @return a [StyledString] styled with all [attributes]
 * @throws [IndexOutOfBoundsException] if [attributes] returns an [IntRange] out of bounds for the String
 */
fun String.styled(provider: StyledStringBuilder.Provider, defaultTextStyle: KalugaTextStyle, vararg attributes: String.() -> Pair<StringStyleAttribute, IntRange>?) =
    styled(provider, defaultTextStyle, null, *attributes)

/**
 * Creates a [StyledString] from a [String]
 * @param provider the [StyledStringBuilder.Provider] to provide the [StyledStringBuilder] used create a [StyledString]
 * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
 * @param linkColor the [KalugaColor] to apply to the [LinkStyle] of the [StyledString]
 * @param attributes a collection methods that create a [Pair] of [StringStyleAttribute] and the [IntRange] at which it should be applied
 * @return a [StyledString] styled with all [attributes]
 * @throws [IndexOutOfBoundsException] if [attributes] returns an [IntRange] out of bounds for the String
 */
fun String.styled(
    provider: StyledStringBuilder.Provider,
    defaultTextStyle: KalugaTextStyle,
    linkColor: KalugaColor,
    vararg attributes: String.() -> Pair<StringStyleAttribute, IntRange>?,
) = styled(provider, defaultTextStyle, LinkStyle(linkColor, true), *attributes)

/**
 * Creates a [StyledString] from a [String]
 * @param provider the [StyledStringBuilder.Provider] to provide the [StyledStringBuilder] used create a [StyledString]
 * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
 * @param linkStyle the [LinkStyle] to apply to the [StyledString]
 * @param attributes a collection methods that create a [Pair] of [StringStyleAttribute] and the [IntRange] at which it should be applied
 * @return a [StyledString] styled with all [attributes]
 * @throws [IndexOutOfBoundsException] if [attributes] returns an [IntRange] out of bounds for the String
 */
fun String.styled(
    provider: StyledStringBuilder.Provider,
    defaultTextStyle: KalugaTextStyle,
    linkStyle: LinkStyle?,
    vararg attributes: String.() -> Pair<StringStyleAttribute, IntRange>?,
) = provider.provide(this, defaultTextStyle, linkStyle).apply {
    attributes.forEach { attribute ->
        attribute()?.let {
            addStyleAttribute(it.first, it.second)
        }
    }
}.create()

/**
 * Looks for a substring in the String and returns a [Pair] or [StringStyleAttribute] and [IntRange]
 * that should be provided to [StyledStringBuilder] to apply the [StringStyleAttribute] to the first occurrence of the substring
 * @param substring the substring to find in the String
 * @param attribute the [StringStyleAttribute] to apply to [substring] if it is found
 * @return the [Pair] of [attribute] and an [IntRange] to provide to [StyledStringBuilder]
 * to apply the [StringStyleAttribute] to the first occurrence of the substring,
 * or `null` if the substring does not occur in the String
 */
fun String.attributeSubstring(substring: String, attribute: StringStyleAttribute): Pair<StringStyleAttribute, IntRange>? {
    return indexOf(substring).let {
        if (it >= 0) {
            val range = IntRange(it, it + substring.length - 1)
            Pair(attribute, range)
        } else {
            null
        }
    }
}

/**
 * A style attribute that can be applied to a [StyledString]
 */
sealed class StringStyleAttribute {

    /**
     * A [StringStyleAttribute] that is applied to individual characters
     */
    sealed class CharacterStyleAttribute : StringStyleAttribute() {

        /**
         * A [CharacterStyleAttribute] that adds a background color to the character
         * @property color the [KalugaColor] to show in the background
         */
        data class BackgroundColor(val color: KalugaColor) : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that sets the text color of the character
         * @property color the [KalugaColor] of the character
         */
        data class ForegroundColor(val color: KalugaColor) : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that sets a [KalugaTextStyle] to the character
         * @property textStyle the [KalugaTextStyle] of the character
         */
        data class TextStyle(val textStyle: KalugaTextStyle) : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that sets a [KalugaFont] to the character
         * @property font the [KalugaFont] to show the character in
         * @property size the size of the font in `points`
         */
        data class Font(val font: KalugaFont, val size: Float) : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that adds a stroke to the character
         * @property width the width of the stroke as a percentage of the font size
         * @property color the [KalugaColor] of the stroke
         */
        data class Stroke(val width: Float, val color: KalugaColor) : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that adds a shadow to the character
         * @property color the [KalugaColor] of the shadow
         * @property xOffset the shadow offset along the x-axis in `points`
         * @property yOffset the shadow offset along the y-axis in `points`
         * @property blurRadius the radius at which the shadow is blurred in `points`
         */
        data class Shadow(val color: KalugaColor, val xOffset: Float, val yOffset: Float, val blurRadius: Float) : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that adds kerning to the character
         * @property kern the kerning size in `EM` units
         */
        data class Kerning(val kern: Float) : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that adds a strike through to the character
         */
        data object Strikethrough : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that adds an underline to the character
         */
        data object Underline : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that makes the character appear in superscript
         */
        data object SuperScript : CharacterStyleAttribute()

        /**
         * A [CharacterStyleAttribute] that makes the character appear in subscript
         */
        data object SubScript : CharacterStyleAttribute()
    }

    /**
     * A [StringStyleAttribute] that is applied to an entire paragraph
     */
    sealed class ParagraphStyleAttribute : StringStyleAttribute() {

        /**
         * A [ParagraphStyleAttribute] that adds an indent to the leading side of the paragraph according to the reading direction
         * @property indent the indent in `points`
         * @property firstLineIndent the indent to apply to the first line of the paragraph in `points`
         */
        data class LeadingIndent(val indent: Float, val firstLineIndent: Float = indent) : ParagraphStyleAttribute()

        /**
         * A [ParagraphStyleAttribute] that adds spacing between the lines
         * @property spacing the spacing in `points` between the lines of the paragraph
         * @property paragraphSpacing the space between paragraphs in `points`. The space between paragraphs is determined by adding the previous paragraph’s [paragraphSpacing] and the current paragraph’s [paragraphSpacingBefore]
         * @property paragraphSpacingBefore the space between the current and previous paragraph in `points`. The space between paragraphs is determined by adding the previous paragraph’s [paragraphSpacing] and the current paragraph’s [paragraphSpacingBefore].
         */
        data class LineSpacing(val spacing: Float, val paragraphSpacing: Float = 0.0f, val paragraphSpacingBefore: Float = 0.0f) : ParagraphStyleAttribute()

        /**
         * A [ParagraphStyleAttribute] that aligns the paragraph according to a [KalugaTextAlignment]
         * @param alignment the [KalugaTextAlignment] to align the paragraph with
         */
        data class Alignment(val alignment: KalugaTextAlignment) : ParagraphStyleAttribute()
    }

    /**
     * A [StringStyleAttribute] that makes part of the text clickable to open a URL
     * @property url the url string to open when the attribute is pressed
     */
    data class Link(val url: String) : StringStyleAttribute()
}
