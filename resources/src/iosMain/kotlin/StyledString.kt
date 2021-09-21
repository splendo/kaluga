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
import com.splendo.kaluga.resources.uikit.nsTextAlignment
import kotlinx.cinterop.AutofreeScope
import kotlinx.cinterop.CValue
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSAttributedString
import platform.Foundation.NSAttributedStringKey
import platform.Foundation.NSMakeRange
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.NSRange
import platform.Foundation.NSURL
import platform.Foundation.addAttribute
import platform.Foundation.attribute
import platform.Foundation.create
import platform.Foundation.removeAttribute
import platform.UIKit.NSMutableParagraphStyle
import platform.UIKit.NSParagraphStyle
import platform.UIKit.NSShadow
import platform.UIKit.NSUnderlineStyleSingle
import kotlin.math.max

actual typealias StyledString = NSAttributedString

actual val StyledString.rawString: String get() = string

actual class StyledStringBuilder actual constructor(string: String) {

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
                    attributedString.addAttribute("link", it, nsRange)
                }
            }
        }
    }

    private val StringStyleAttribute.CharacterStyleAttribute.characterAttributes: Map<NSAttributedStringKey, Any> get() = when (this) {
        is StringStyleAttribute.CharacterStyleAttribute.ForegroundColor -> mapOf("foregroundColor" to color.uiColor)
        is StringStyleAttribute.CharacterStyleAttribute.BackgroundColor -> mapOf("backgroundColor" to color.uiColor)
        is StringStyleAttribute.CharacterStyleAttribute.Stroke -> mapOf(
            "strokeColor" to color.uiColor,
            "strokeWidth" to width
        )
        is StringStyleAttribute.CharacterStyleAttribute.SuperScript -> mapOf("superscript" to 1)
        is StringStyleAttribute.CharacterStyleAttribute.SubScript -> mapOf("superscript" to -1)
        is StringStyleAttribute.CharacterStyleAttribute.Underline -> mapOf("underlineStyle" to NSUnderlineStyleSingle)
        is StringStyleAttribute.CharacterStyleAttribute.Strikethrough -> mapOf("strikethroughStyle" to NSUnderlineStyleSingle)
        is StringStyleAttribute.CharacterStyleAttribute.Font -> mapOf("font" to font.fontWithSize(size.toDouble()))
        is StringStyleAttribute.CharacterStyleAttribute.TextStyle -> mapOf(
            "font" to textStyle.font.fontWithSize(textStyle.size.toDouble()),
            "foregroundColor" to textStyle.color.uiColor
        )
        is StringStyleAttribute.CharacterStyleAttribute.Kerning -> mapOf("kern" to kern)
        is StringStyleAttribute.CharacterStyleAttribute.Shadow -> mapOf(
            "shadow" to NSShadow().apply {
                shadowColor = color.uiColor
                shadowBlurRadius = blurRadius.toDouble()
                shadowOffset = CGSizeMake(xOffset.toDouble(), yOffset.toDouble())
            }
        )
    }

    private fun StringStyleAttribute.ParagraphStyleAttribute.updateParagraphAttribute(range: CValue<NSRange>) {
        var rangeToProcess = range
        while (rangeToProcess.range.last <= range.range.last) {
            memScoped {
                val location = rangeToProcess.useContents { location }
                val matchedRange = nativeHeap.alloc<NSRange>()
                val paragraphStyle = NSMutableParagraphStyle().apply {
                    setParagraphStyle(
                        attributedString.attribute(
                            "paragraphStyle",
                            location,
                            matchedRange.ptr,
                            rangeToProcess
                        ) as? NSParagraphStyle ?: NSParagraphStyle.defaultParagraphStyle
                    )
                }

                attributedString.removeAttribute("paragraphStyle", matchedRange.readValue())

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
                attributedString.addAttribute("paragraphStyle", paragraphStyle, matchedRange.readValue())
                rangeToProcess = NSMakeRange(matchedRange.location + matchedRange.length, max(0UL, range.useContents { length } - (matchedRange.location + matchedRange.length)))
            }

        }
    }

    actual fun create(): StyledString = NSAttributedString.Companion.create(attributedString)
}
