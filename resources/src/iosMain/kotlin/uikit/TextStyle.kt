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

package com.splendo.kaluga.resources.uikit

import com.splendo.kaluga.resources.stylable.KalugaTextStyle
import com.splendo.kaluga.resources.urlRanges
import com.splendo.kaluga.resources.view.KalugaLabel
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
import platform.darwin.sel_registerName

/**
 * Makes a [UILabel] look like the specification of a [KalugaLabel]
 * @param label the [KalugaLabel] that specifies the look of the [UILabel]
 */
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
                                "NSUnderline" to if (linkStyle.isUnderlined) NSUnderlineStyleSingle else NSUnderlineStyleNone,
                            ),
                            range,
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

/**
 * Makes a [UITextView] look like the specification of a [KalugaLabel]
 * @param label the [KalugaLabel] that specifies the look of the [UITextView]
 */

fun UITextView.bindLabel(label: KalugaLabel) {
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

/**
 * Makes a [UILabel] look as specified by a [KalugaTextStyle]
 * @param textStyle the [KalugaTextStyle] that specifies the look of the [UILabel]
 */
fun UILabel.applyTextStyle(textStyle: KalugaTextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble()))
    textColor = textStyle.color.uiColor
    textAlignment = textStyle.alignment.nsTextAlignment
    numberOfLines = 0
}

/**
 * Makes a [UITextView] look as specified by a [KalugaTextStyle]
 * @param textStyle the [KalugaTextStyle] that specifies the look of the [UITextView]
 */
fun UITextView.applyTextStyle(textStyle: KalugaTextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble()))
    textColor = textStyle.color.uiColor
    textAlignment = textStyle.alignment.nsTextAlignment
}

/**
 * Makes a [UITextField] look as specified by a [KalugaTextStyle]
 * @param textStyle the [KalugaTextStyle] that specifies the look of the [UITextField]
 */
fun UITextField.applyTextStyle(textStyle: KalugaTextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble()))
    textColor = textStyle.color.uiColor
    textAlignment = textStyle.alignment.nsTextAlignment
}
