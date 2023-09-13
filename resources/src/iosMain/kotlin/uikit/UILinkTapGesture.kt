/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.resources.uikit

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.convert
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectContainsPoint
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSAttributedString
import platform.Foundation.NSLocationInRange
import platform.Foundation.NSMakeRange
import platform.Foundation.NSMapTable
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.NSPointerFunctionsStrongMemory
import platform.Foundation.NSPointerFunctionsWeakMemory
import platform.Foundation.NSRange
import platform.Foundation.NSURL
import platform.Foundation.addAttributes
import platform.Foundation.create
import platform.Foundation.enumerateAttributesInRange
import platform.Foundation.length
import platform.UIKit.NSLayoutManager
import platform.UIKit.NSMutableParagraphStyle
import platform.UIKit.NSTextContainer
import platform.UIKit.NSTextStorage
import platform.UIKit.UIApplication
import platform.UIKit.UILabel
import platform.UIKit.UITapGestureRecognizer
import platform.darwin.NSObject
import platform.darwin.NSUInteger
import kotlin.math.max
import kotlin.math.min

internal class UILinkTapGesture(private val label: UILabel, private val urlRanges: List<Pair<CValue<NSRange>, NSURL>>) : NSObject() {

    object Registry {
        val registeredGestures = NSMapTable(NSPointerFunctionsWeakMemory, NSPointerFunctionsStrongMemory, 0U)
    }

    private val attributedText: NSAttributedString? get() = label.attributedText?.let { attributedText ->
        // Account for the default font of the label.
        // Since attributes are overwritten by a new attribute for overlapping ranges, add the font attribute first
        NSMutableAttributedString.create(attributedText.string).apply {
            val stringRange = (0 until attributedText.string.length).nsRange
            val paragraphStyle = NSMutableParagraphStyle().apply {
                setParagraphStyle(NSMutableParagraphStyle.defaultParagraphStyle)
                setAlignment(label.textAlignment)
                setLineBreakMode(label.lineBreakMode)
                setLineBreakStrategy(label.lineBreakStrategy)
            }
            addAttributes(
                mapOf(
                    "NSFont" to label.font,
                    "NSParagraphStyle" to paragraphStyle,
                ),
                stringRange,
            )
            attributedText.enumerateAttributesInRange(stringRange, 0U) { attributes, range, _ ->
                attributes?.let {
                    addAttributes(it, range)
                }
            }
        }
    }

    @ObjCAction
    fun tapLabel(gesture: UITapGestureRecognizer) {
        urlRanges.forEach { (range, url) ->
            if (didTapAttributedTextInLabel(gesture, range)) {
                UIApplication.sharedApplication.openURL(url)
            }
        }
    }

    private fun didTapAttributedTextInLabel(gesture: UITapGestureRecognizer, targetRange: CValue<NSRange>): Boolean {
        // Create instances of NSLayoutManager, NSTextContainer and NSTextStorage
        val labelSize = label.bounds.useContents {
            CGSizeMake(size.width, size.height)
        }
        val textContainer = NSTextContainer(labelSize).apply {
            // Configure textContainer
            lineFragmentPadding = 0.0
            lineBreakMode = label.lineBreakMode.convert()
            maximumNumberOfLines = label.numberOfLines.convert()
            this.size
        }
        val layoutManager = NSLayoutManager().apply {
            addTextContainer(textContainer)
        }

        val attributedText = attributedText ?: return false
        NSTextStorage.create(attributedText).apply {
            addLayoutManager(layoutManager)
        }

        // Find the tapped character location and compare it to the specified range
        val locationOfTouchInLabel = gesture.locationInView(label)
        val textBoundingBox = layoutManager.usedRectForTextContainer(textContainer)
        val textContainerOffset = CGPointMake(
            (-textBoundingBox.useContents { origin.x }),
            (-textBoundingBox.useContents { origin.y }),
        ).useContents { this }
        val locationOfTouchInTextContainer = CGPointMake(
            locationOfTouchInLabel.useContents { x } - textContainerOffset.x,
            locationOfTouchInLabel.useContents { y } - textContainerOffset.y,
        )
        val indexOfCharacter = layoutManager.characterIndexForPoint(locationOfTouchInTextContainer, textContainer, null)

        return NSLocationInRange(indexOfCharacter, targetRange) &&
            CGRectContainsPoint(
                layoutManager.boundingRectWithMarginsForCharacterAtIndex(indexOfCharacter, textContainer, attributedText.length),
                locationOfTouchInTextContainer,
            )
    }

    private fun NSLayoutManager.boundingRectWithMarginsForCharacterAtIndex(index: NSUInteger, textContainer: NSTextContainer, stringLength: NSUInteger): CValue<CGRect> {
        val boundingRectForCharacter = boundingRectForCharacterAtIndex(index, textContainer)

        val boundingRectOfPreviousCharacter = if (index > 0u) {
            boundingRectForCharacterAtIndex(index - 1u, textContainer)
        } else {
            null
        }

        val boundingRectOfNextCharacter = if (index < stringLength - 1u) {
            boundingRectForCharacterAtIndex(index + 1u, textContainer)
        } else {
            null
        }

        val xStart = if (boundingRectOfPreviousCharacter != null && boundingRectOfPreviousCharacter.isOnSameLine(boundingRectForCharacter)) {
            min(boundingRectForCharacter.useContents { origin.x + size.width }, boundingRectForCharacter.useContents { origin.x })
        } else {
            boundingRectForCharacter.useContents { origin.x }
        }
        val xEnd = if (boundingRectOfNextCharacter != null && boundingRectOfNextCharacter.isOnSameLine(boundingRectForCharacter)) {
            max(boundingRectOfNextCharacter.useContents { origin.x }, boundingRectForCharacter.useContents { origin.x + size.width })
        } else {
            boundingRectForCharacter.useContents { origin.x + size.width }
        }

        val (yStart, yEnd) = listOfNotNull(boundingRectOfPreviousCharacter, boundingRectOfNextCharacter)
            .filter { it.isOnSameLine(boundingRectForCharacter) }
            .fold(boundingRectForCharacter.useContents { origin.y to origin.y + size.height }) { (yStart, yEnd), rectToCheck ->
                rectToCheck.useContents {
                    min(yStart, origin.y) to max(yEnd, origin.y + size.height)
                }
            }

        return CGRectMake(xStart, yStart, xEnd - xStart, yEnd - yStart)
    }

    private fun NSLayoutManager.boundingRectForCharacterAtIndex(index: NSUInteger, textContainer: NSTextContainer): CValue<CGRect> {
        val glyphIndexOfNextCharacter = glyphIndexForCharacterAtIndex(index)
        return boundingRectForGlyphRange(NSMakeRange(glyphIndexOfNextCharacter, 1U), textContainer)
    }

    private fun CValue<CGRect>.isOnSameLine(other: CValue<CGRect>) = useContents {
        val yStart = origin.y
        val yEnd = origin.y + size.height
        other.useContents {
            when {
                origin.y < yStart -> origin.y + size.height > yStart
                origin.y > yEnd -> false
                else -> true
            }
        }
    }
}

private val IntRange.nsRange: CValue<NSRange> get() = NSMakeRange(start.convert(), (endInclusive + 1 - start).convert())
