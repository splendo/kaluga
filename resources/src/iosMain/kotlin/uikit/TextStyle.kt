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

package com.splendo.kaluga.resources.uikit

import com.splendo.kaluga.base.utils.range
import com.splendo.kaluga.resources.stylable.TextStyle
import com.splendo.kaluga.resources.urlRanges
import com.splendo.kaluga.resources.view.KalugaLabel
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.convert
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSLocationInRange
import platform.Foundation.NSMapTable
import platform.Foundation.NSPointerFunctionsStrongMemory
import platform.Foundation.NSPointerFunctionsWeakMemory
import platform.Foundation.NSRange
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.UIKit.NSLayoutManager
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.NSTextAlignmentRight
import platform.UIKit.NSTextContainer
import platform.UIKit.NSTextStorage
import platform.UIKit.UIApplication
import platform.UIKit.UILabel
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UITextField
import platform.UIKit.UITextItemInteraction
import platform.UIKit.UITextView
import platform.UIKit.UITextViewDelegateProtocol
import platform.UIKit.addGestureRecognizer
import platform.darwin.NSObject
import platform.darwin.sel_registerName
import kotlin.math.ceil

class UILinkTapGesture(private val label: UILabel) : NSObject() {

    @ThreadLocal
    object Registry {
        val registeredGestures = NSMapTable(NSPointerFunctionsWeakMemory, NSPointerFunctionsStrongMemory, 0)
    }

    @ObjCAction
    fun tapLabel(gesture: UITapGestureRecognizer) {
        label.attributedText?.urlRanges?.forEach { (range, url) ->
            if (didTapAttributedTextInLabel(gesture, range)) {
                UIApplication.sharedApplication.openURL(url)
            }
        }
    }

    private fun didTapAttributedTextInLabel(gesture: UITapGestureRecognizer, targetRange: NSRange): Boolean {
        // Create instances of NSLayoutManager, NSTextContainer and NSTextStorage
        val labelSize = label.bounds.useContents {
            CGSizeMake(size.width, size.height)
        }
        val textContainer = NSTextContainer(labelSize).apply {
            // Configure textContainer
            lineFragmentPadding = 0.0 as CGFloat
            lineBreakMode = label.lineBreakMode.convert()
            maximumNumberOfLines = label.numberOfLines.convert()
        }
        val layoutManager = NSLayoutManager().apply {
            addTextContainer(textContainer)
        }

        val attributedText = label.attributedText ?: return false
        NSTextStorage.create(attributedText).apply {
            addLayoutManager(layoutManager)
        }

        val alignmentOffset = when (label.textAlignment) {
            NSTextAlignmentCenter -> 0.5
            NSTextAlignmentRight -> 1.0
            else -> 0.0
        }

        // Find the tapped character location and compare it to the specified range
        val locationOfTouchInLabel = gesture.locationInView(label)
        val textBoundingBox = layoutManager.usedRectForTextContainer(textContainer)
        val textContainerOffset = CGPointMake(
            (((labelSize.useContents { width } - textBoundingBox.useContents { size.width }) * alignmentOffset) - textBoundingBox.useContents { origin.x }) as CGFloat,
            (((labelSize.useContents { height } - textBoundingBox.useContents { size.height }) * alignmentOffset) - textBoundingBox.useContents { origin.y }) as CGFloat
        ).useContents { this }
        val locationOfTouchInTextContainer = CGPointMake(
            locationOfTouchInLabel.useContents { x } - textContainerOffset.x,
            locationOfTouchInLabel.useContents { y } - textContainerOffset.y
        )
        val indexOfCharacter = layoutManager.characterIndexForPoint(locationOfTouchInTextContainer, textContainer, null)

        val lineTapped = ceil(locationOfTouchInLabel.useContents { y } / label.font.lineHeight) - 1
        val rightMostPointInLineTapped = CGPointMake(labelSize.useContents { width }, label.font.lineHeight * lineTapped)
        val charsInLineTapped = layoutManager.characterIndexForPoint(rightMostPointInLineTapped, textContainer, null)
        if (indexOfCharacter >= charsInLineTapped) {
            return false
        }

        return NSLocationInRange(indexOfCharacter, targetRange.readValue())
    }
}

class UILinkTextViewDelegate : NSObject(), UITextViewDelegateProtocol {

    @ThreadLocal
    object Registry {
        val registeredDelegates = NSMapTable(NSPointerFunctionsWeakMemory, NSPointerFunctionsStrongMemory, 0)
    }

    override fun textView(
        textView: UITextView,
        shouldInteractWithURL: NSURL,
        inRange: CValue<NSRange>,
        interaction: UITextItemInteraction
    ): Boolean {
        return textView.attributedText.urlRanges.let { urls ->
            urls.firstOrNull { it.first.readValue().range == inRange.range && it.second == shouldInteractWithURL } != null
        }
    }
}

class UILinkTextViewDelegateWrapper(private val wrapped: UITextViewDelegateProtocol) : NSObject(), UITextViewDelegateProtocol by wrapped {

    private val linkDelegate = UILinkTextViewDelegate()

    override fun textView(
        textView: UITextView,
        shouldChangeTextInRange: CValue<NSRange>,
        replacementText: String
    ): Boolean {
        return linkDelegate.textView(textView, shouldChangeTextInRange, replacementText) || wrapped.textView(textView, shouldChangeTextInRange, replacementText)
    }
}

fun UILabel.bindLabel(label: KalugaLabel) {
    applyTextStyle(label.style)
    when (label) {
        is KalugaLabel.Plain -> text = label.text
        is KalugaLabel.Styled -> {
            attributedText = label.text.attributeString
            if (label.text.attributeString.urlRanges.isNotEmpty()) {
                val wrappedGesture = UILinkTapGesture(this)
                UILinkTapGesture.Registry.registeredGestures.setObject(wrappedGesture, this)
                val selector = sel_registerName("tapLabel:")
                addGestureRecognizer(UITapGestureRecognizer(wrappedGesture, selector))
                userInteractionEnabled = true
            } else {
                UILinkTapGesture.Registry.registeredGestures.removeObjectForKey(this)
            }
        }
    }
}

fun UITextView.bindTextView(label: KalugaLabel) {
    applyTextStyle(label.style)
    when (label) {
        is KalugaLabel.Plain -> text = label.text
        is KalugaLabel.Styled -> {
            attributedText = label.text.attributeString
            if (label.text.attributeString.urlRanges.isNotEmpty()) {
                val delegate = this.delegate()?.let { UILinkTextViewDelegateWrapper(it) } ?: UILinkTextViewDelegate()
                UILinkTextViewDelegate.Registry.registeredDelegates.setObject(delegate, this)
                setDelegate(delegate)
            } else {
                UILinkTextViewDelegate.Registry.registeredDelegates.removeObjectForKey(this)
            }
        }
    }
}

fun UILabel.applyTextStyle(textStyle: TextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble() as CGFloat))
    textColor = textStyle.color.uiColor
    textAlignment = textStyle.alignment.nsTextAlignment
    numberOfLines = 0
}

fun UITextView.applyTextStyle(textStyle: TextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble() as CGFloat))
    textColor = textStyle.color.uiColor
    textAlignment = textStyle.alignment.nsTextAlignment
}

fun UITextField.applyTextStyle(textStyle: TextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble() as CGFloat))
    textColor = textStyle.color.uiColor
    textAlignment = textStyle.alignment.nsTextAlignment
}
