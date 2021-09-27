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

import com.splendo.kaluga.base.utils.nsRange
import com.splendo.kaluga.base.utils.range
import com.splendo.kaluga.resources.stylable.TextStyle
import com.splendo.kaluga.resources.uikit.nsTextAlignment
import kotlinx.cinterop.CValue
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSAttributedString
import platform.Foundation.NSAttributedStringKey
import platform.Foundation.NSMakeRange
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.NSNotFound
import platform.Foundation.NSRange
import platform.Foundation.NSURL
import platform.Foundation.addAttribute
import platform.Foundation.attribute
import platform.Foundation.create
import platform.Foundation.length
import platform.Foundation.removeAttribute
import platform.UIKit.NSMutableParagraphStyle
import platform.UIKit.NSParagraphStyle
import platform.UIKit.NSShadow
import platform.UIKit.NSUnderlineStyleSingle
import kotlin.math.max

actual data class StyledString(val attributeString: NSAttributedString, actual val defaultTextStyle: TextStyle)

actual val StyledString.rawString: String get() = attributeString.string

actual class StyledStringBuilder constructor(string: String, private val defaultTextStyle: TextStyle) {

    actual class Provider {
        actual fun provide(string: String, defaultTextStyle: TextStyle) = StyledStringBuilder(string, defaultTextStyle)
    }

    private val attributedString = NSMutableAttributedString.Companion.create(string)

    actual fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange) {
        val nsRange = range.nsRange
        when (attribute) {
            is StringStyleAttribute.CharacterStyleAttribute -> {
                attribute.characterAttributes.forEach {
                    attributedString.addAttribute(
                        it.key,
                        it.value,
                        nsRange
                    )
                }
            }
            is StringStyleAttribute.ParagraphStyleAttribute -> {
                attribute.updateParagraphAttribute(nsRange)
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
            "NSStrokeWidth" to -1.0 * width
        )
        is StringStyleAttribute.CharacterStyleAttribute.SuperScript -> {
            val offset = defaultTextStyle.font.fontWithSize(defaultTextStyle.size.toDouble() as CGFloat).ascender / 2.0
            mapOf("NSBaselineOffset" to offset)
        }
        is StringStyleAttribute.CharacterStyleAttribute.SubScript -> {
            val offset = defaultTextStyle.font.fontWithSize(defaultTextStyle.size.toDouble() as CGFloat).ascender / 2.0
            mapOf("NSBaselineOffset" to (-1.0 * offset))
        }
        is StringStyleAttribute.CharacterStyleAttribute.Underline -> mapOf("NSUnderline" to NSUnderlineStyleSingle)
        is StringStyleAttribute.CharacterStyleAttribute.Strikethrough -> mapOf("NSStrikethrough" to NSUnderlineStyleSingle)
        is StringStyleAttribute.CharacterStyleAttribute.Font -> mapOf("NSFont" to font.fontWithSize(size.toDouble()))
        is StringStyleAttribute.CharacterStyleAttribute.TextStyle -> mapOf(
            "NSFont" to textStyle.font.fontWithSize(textStyle.size.toDouble()),
            "NSColor" to textStyle.color.uiColor
        )
        is StringStyleAttribute.CharacterStyleAttribute.Kerning -> mapOf("NSKern" to kern * defaultTextStyle.size)
        is StringStyleAttribute.CharacterStyleAttribute.Shadow -> mapOf(
            "NSShadow" to NSShadow().apply {
                shadowColor = color.uiColor
                shadowBlurRadius = blurRadius.toDouble()
                shadowOffset = CGSizeMake(xOffset.toDouble(), yOffset.toDouble())
            }
        )
    }

    private fun StringStyleAttribute.ParagraphStyleAttribute.updateParagraphAttribute(range: CValue<NSRange>) {
        var rangeToProcess = range
        while (rangeToProcess.useContents { length } > 0UL && rangeToProcess.range.last <= range.range.last) {
            memScoped {
                val location = rangeToProcess.useContents { location }
                val matchedRange = nativeHeap.alloc<NSRange>()
                val paragraphStyle = NSMutableParagraphStyle().apply {
                    setParagraphStyle(
                        attributedString.attribute(
                            "NSParagraphStyle",
                            location,
                            matchedRange.ptr,
                            rangeToProcess
                        ) as? NSParagraphStyle ?: NSParagraphStyle.defaultParagraphStyle
                    )
                }

                attributedString.removeAttribute("NSParagraphStyle", matchedRange.readValue())

                when (this@updateParagraphAttribute) {
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
                attributedString.addAttribute("NSParagraphStyle", paragraphStyle, matchedRange.readValue())
                rangeToProcess = if (matchedRange.location != NSNotFound.toULong()) {
                    val nextStart = matchedRange.location + matchedRange.length
                    NSMakeRange(
                        nextStart,
                        max(
                            0L,
                            range.useContents { this.length.toLong() } - nextStart.toLong()
                        ).toULong()
                    )
                } else {
                    NSMakeRange((range.range.last + 1).toULong(), 0)
                }
            }

        }
    }

    actual fun create(): StyledString = StyledString(attributedString, defaultTextStyle)
}

val NSAttributedString.urlRanges: List<Pair<NSRange, NSURL>> get() {
    val range = IntRange(0, length.toInt() - 1).nsRange
    var rangeToProcess = range
    val result = mutableListOf<Pair<NSRange, NSURL>>()
    while (rangeToProcess.useContents { length } > 0UL && rangeToProcess.range.last <= range.range.last) {
        memScoped {
            val location = rangeToProcess.useContents { location }
            val matchedRange = nativeHeap.alloc<NSRange>()
            val url = attribute(
                "NSLink",
                location,
                matchedRange.ptr,
                rangeToProcess
            ) as? NSURL
            url?.let {
                result.add(Pair(matchedRange, it))
            }

            rangeToProcess = if (matchedRange.location != NSNotFound.toULong()) {
                val nextStart = matchedRange.location + matchedRange.length
                NSMakeRange(
                    nextStart,
                    max(
                        0L,
                        range.useContents { this.length.toLong() } - nextStart.toLong()
                    ).toULong()
                )
            } else {
                NSMakeRange((range.range.last + 1).toULong(), 0)
            }
        }
    }
    return result
}
