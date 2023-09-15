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
import com.splendo.kaluga.resources.uikit.nsTextAlignment
import kotlinx.cinterop.CValue
import kotlinx.cinterop.convert
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSAttributedString
import platform.Foundation.NSAttributedStringKey
import platform.Foundation.NSMakeRange
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.NSRange
import platform.Foundation.NSURL
import platform.Foundation.addAttribute
import platform.Foundation.create
import platform.Foundation.enumerateAttribute
import platform.Foundation.length
import platform.Foundation.removeAttribute
import platform.UIKit.NSMutableParagraphStyle
import platform.UIKit.NSParagraphStyle
import platform.UIKit.NSShadow
import platform.UIKit.NSUnderlineStyleSingle
import platform.UIKit.size

/**
 * A text configured with [StringStyleAttribute]
 * @property attributeString the [NSAttributedString] styled according to some [StringStyleAttribute]
 * @property defaultTextStyle The [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range.
 * This may be partially overwritten (e.g. [StringStyleAttribute.CharacterStyleAttribute.ForegroundColor] may overwrite [KalugaTextStyle.color])
 * @property linkStyle The [LinkStyle] to apply when [StringStyleAttribute.Link] is applied.
 * When `null` the Theme default will be used
 */
actual data class StyledString(
    val attributeString: NSAttributedString,
    actual val defaultTextStyle: KalugaTextStyle,
    actual val linkStyle: LinkStyle?,
)

/**
 * Gets the plain string of a [StyledString]
 */
actual val StyledString.rawString: String get() = attributeString.string

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
    private val defaultTextStyle: KalugaTextStyle,
    private val linkStyle: LinkStyle?,
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

    private val attributedString = NSMutableAttributedString.Companion.create(string)

    /**
     * Adds a [StringStyleAttribute] for a given range
     * @param attribute the [StringStyleAttribute] to apply
     * @param range the [IntRange] at which to apply the style
     * @throws [IndexOutOfBoundsException] if [range] is out of bounds for the text to span
     */
    actual fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange) {
        if (range.any { it !in attributedString.string.indices }) {
            throw IndexOutOfBoundsException("Attribute cannot be applied to $range")
        }
        val nsRange = range.nsRange
        when (attribute) {
            is StringStyleAttribute.CharacterStyleAttribute -> {
                attribute.characterAttributes.forEach {
                    attributedString.addAttribute(
                        it.key,
                        it.value,
                        nsRange,
                    )
                }
            }
            is StringStyleAttribute.ParagraphStyleAttribute -> {
                attribute.updateParagraphAttribute(range, defaultTextStyle.alignment)
            }
            is StringStyleAttribute.Link -> {
                NSURL.Companion.URLWithString(attribute.url)?.let {
                    attributedString.addAttribute("NSLink", it, nsRange)
                }
            }
        }
    }

    private val StringStyleAttribute.CharacterStyleAttribute.characterAttributes: Map<NSAttributedStringKey, Any> get() = when (this) {
        is StringStyleAttribute.CharacterStyleAttribute.ForegroundColor -> mapOf("NSColor" to color.uiColor)
        is StringStyleAttribute.CharacterStyleAttribute.BackgroundColor -> mapOf("NSBackgroundColor" to color.uiColor)
        is StringStyleAttribute.CharacterStyleAttribute.Stroke -> mapOf(
            "NSStrokeColor" to color.uiColor,
            "NSStrokeWidth" to -1.0 * width,
        )
        is StringStyleAttribute.CharacterStyleAttribute.SuperScript -> {
            val offset = defaultTextStyle.font.fontWithSize(defaultTextStyle.size.toDouble()).ascender / 2.0
            mapOf("NSBaselineOffset" to offset)
        }
        is StringStyleAttribute.CharacterStyleAttribute.SubScript -> {
            val offset = defaultTextStyle.font.fontWithSize(defaultTextStyle.size.toDouble()).ascender / 2.0
            mapOf("NSBaselineOffset" to (-1.0 * offset))
        }
        is StringStyleAttribute.CharacterStyleAttribute.Underline -> mapOf("NSUnderline" to NSUnderlineStyleSingle)
        is StringStyleAttribute.CharacterStyleAttribute.Strikethrough -> mapOf("NSStrikethrough" to NSUnderlineStyleSingle)
        is StringStyleAttribute.CharacterStyleAttribute.Font -> mapOf("NSFont" to font.fontWithSize(size.toDouble()))
        is StringStyleAttribute.CharacterStyleAttribute.TextStyle -> mapOf(
            "NSFont" to textStyle.font.fontWithSize(textStyle.size.toDouble()),
            "NSColor" to textStyle.color.uiColor,
        )
        is StringStyleAttribute.CharacterStyleAttribute.Kerning -> mapOf("NSKern" to kern * defaultTextStyle.size)
        is StringStyleAttribute.CharacterStyleAttribute.Shadow -> mapOf(
            "NSShadow" to NSShadow().apply {
                shadowColor = color.uiColor
                shadowBlurRadius = blurRadius.toDouble()
                shadowOffset = CGSizeMake(xOffset.toDouble(), yOffset.toDouble())
            },
        )
    }

    private fun StringStyleAttribute.ParagraphStyleAttribute.updateParagraphAttribute(range: IntRange, defaultAlignment: KalugaTextAlignment) {
        // First search for all existing paragraph attributes within range
        val rangesWithParagraphStyle = mutableListOf<Pair<IntRange, NSParagraphStyle>>()
        attributedString.enumerateAttribute("NSParagraphStyle", (0 until attributedString.string.length).nsRange, 0U) { match, matchedNSRange, _ ->
            val matchedRange = matchedNSRange.range
            if (matchedRange.any { range.contains(it) } && match is NSParagraphStyle) {
                rangesWithParagraphStyle.add(maxOf(matchedRange.first, range.first)..minOf(matchedRange.last, range.last) to match)
            }
        }

        val startOfBridgingRange: (List<Pair<IntRange, *>>) -> Int = { list -> list.lastOrNull()?.first?.endInclusive?.let { it + 1 } ?: range.first }
        val createEmptyParagraphStyle: () -> NSMutableParagraphStyle = {
            NSMutableParagraphStyle().apply {
                setParagraphStyle(NSParagraphStyle.defaultParagraphStyle)
                setAlignment(defaultAlignment.nsTextAlignment)
            }
        }

        // Remove existing paragraph styles and fill in the blanks
        val rangesToUpdate = rangesWithParagraphStyle.fold(emptyList<Pair<IntRange, NSMutableParagraphStyle>>()) { acc, (existingRange, existingStyle) ->
            // Remove attribute to be overwritten
            attributedString.removeAttribute("NSParagraphStyle", existingRange.nsRange)
            val bridgingRange = startOfBridgingRange(acc) until existingRange.first
            val accWithBridging = if (!bridgingRange.isEmpty()) {
                acc + (bridgingRange to createEmptyParagraphStyle())
            } else {
                acc
            }

            val mutableParagraphStyle = NSMutableParagraphStyle().apply {
                setParagraphStyle(existingStyle)
            }
            accWithBridging + (existingRange to mutableParagraphStyle)
        }.let { rangesWithoutEnd ->
            // Dont forget to add the bridging until the end
            val lastRange = startOfBridgingRange(rangesWithoutEnd)..range.last
            if (!lastRange.isEmpty()) {
                rangesWithoutEnd + (lastRange to createEmptyParagraphStyle())
            } else {
                rangesWithoutEnd
            }
        }

        rangesToUpdate.forEach { (range, paragraphStyle) ->
            when (this) {
                is StringStyleAttribute.ParagraphStyleAttribute.LeadingIndent -> {
                    paragraphStyle.setHeadIndent(indent.toDouble())
                    paragraphStyle.setFirstLineHeadIndent(firstLineIndent.toDouble())
                }
                is StringStyleAttribute.ParagraphStyleAttribute.LineSpacing -> {
                    paragraphStyle.setLineSpacing(spacing.toDouble())
                    paragraphStyle.setParagraphSpacing(paragraphSpacing.toDouble())
                    paragraphStyle.setParagraphSpacingBefore(paragraphSpacingBefore.toDouble())
                }
                is StringStyleAttribute.ParagraphStyleAttribute.Alignment -> {
                    paragraphStyle.setAlignment(alignment.nsTextAlignment)
                }
            }
            attributedString.addAttribute("NSParagraphStyle", paragraphStyle, range.nsRange)
        }
    }

    /**
     * Creates the [StyledString]
     * @return the created [StyledString]
     */
    actual fun create(): StyledString = StyledString(attributedString, defaultTextStyle, linkStyle)
}

internal val NSAttributedString.urlRanges: List<Pair<CValue<NSRange>, NSURL>> get() {
    val result = mutableListOf<Pair<CValue<NSRange>, NSURL>>()
    enumerateAttribute("NSLink", IntRange(0, length.toInt() - 1).nsRange, 0U) { match, matchedRange, _ ->
        if (match is NSURL) {
            result.add(matchedRange to match)
        }
    }
    return result
}

private val IntRange.nsRange: CValue<NSRange> get() = NSMakeRange(start.convert(), (endInclusive + 1 - start).convert())
private val CValue<NSRange>.range: IntRange get() = useContents { IntRange(location.toInt(), (location + length).toInt() - 1) }
