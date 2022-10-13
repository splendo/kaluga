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

import com.splendo.kaluga.resources.stylable.TextStyle
import com.splendo.kaluga.resources.urlRanges
import com.splendo.kaluga.resources.view.KalugaLabel
import platform.CoreGraphics.CGFloat
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.addAttributes
import platform.Foundation.create
import platform.Foundation.removeAttribute
import platform.UIKit.NSUnderlineStyleNone
import platform.UIKit.NSUnderlineStyleSingle
import platform.UIKit.UILabel
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UITextField
import platform.UIKit.UITextView
import platform.UIKit.addGestureRecognizer
import platform.darwin.sel_registerName

fun UILabel.bindLabel(label: KalugaLabel) {
    applyTextStyle(label.style)
    when (label) {
        is KalugaLabel.Plain -> text = label.text
        is KalugaLabel.Styled -> {
            val urlRanges = label.text.attributeString.urlRanges
            attributedText = if (urlRanges.isNotEmpty()) {
                val linkStyledAttributedString = NSMutableAttributedString.create(label.text.attributeString)
                // UILabel doesnt support NSLink styling out of the box. If we want custom styling, we should just manually add color and underline styling
                label.text.linkStyle?.let { linkStyle ->
                    for ((range, _) in urlRanges) {
                        linkStyledAttributedString.removeAttribute("NSLink", range)
                        linkStyledAttributedString.addAttributes(
                            mapOf(
                                "NSColor" to linkStyle.color.uiColor,
                                "NSUnderline" to if (linkStyle.isUnderlined) NSUnderlineStyleSingle else NSUnderlineStyleNone
                            ),
                            range
                        )
                    }
                }
                val wrappedGesture = UILinkTapGesture(this, urlRanges)
                UILinkTapGesture.Registry.registeredGestures.setObject(wrappedGesture, this)
                val selector = sel_registerName("tapLabel:")
                addGestureRecognizer(UITapGestureRecognizer(wrappedGesture, selector))
                userInteractionEnabled = true
                linkStyledAttributedString
            } else {
                UILinkTapGesture.Registry.registeredGestures.removeObjectForKey(this)
                label.text.attributeString
            }
        }
    }
}

fun UITextView.bindTextView(label: KalugaLabel) {
    when (label) {
        is KalugaLabel.Plain -> text = label.text
        is KalugaLabel.Styled -> {
            attributedText = label.text.attributeString
            if (label.text.attributeString.urlRanges.isNotEmpty()) {
                label.text.linkStyle?.let {
                    linkTextAttributes = mapOf("NSColor" to it.color.uiColor, "NSUnderline" to if (it.isUnderlined) NSUnderlineStyleSingle else NSUnderlineStyleNone)
                }
                val delegate = this.delegate()?.let { UILinkTextViewDelegateWrapper(it) } ?: UILinkTextViewDelegate()
                UILinkTextViewDelegate.Registry.registeredDelegates.setObject(delegate, this)
                setDelegate(delegate)
            } else {
                UILinkTextViewDelegate.Registry.registeredDelegates.removeObjectForKey(this)
            }
        }
    }
    // Needs to happen after setting the text.
    applyTextStyle(label.style)
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
