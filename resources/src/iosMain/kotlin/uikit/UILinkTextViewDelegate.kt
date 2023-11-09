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

import com.splendo.kaluga.resources.urlRanges
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPoint
import platform.Foundation.NSMapTable
import platform.Foundation.NSPointerFunctionsStrongMemory
import platform.Foundation.NSPointerFunctionsWeakMemory
import platform.Foundation.NSRange
import platform.Foundation.NSURL
import platform.UIKit.UITextItemInteraction
import platform.UIKit.UITextView
import platform.UIKit.UITextViewDelegateProtocol
import platform.darwin.NSObject

internal class UILinkTextViewDelegate : NSObject(), UITextViewDelegateProtocol {

    object Registry {
        val registeredDelegates = NSMapTable(NSPointerFunctionsWeakMemory, NSPointerFunctionsStrongMemory, 0U)
    }

    override fun textView(textView: UITextView, shouldInteractWithURL: NSURL, inRange: CValue<NSRange>, interaction: UITextItemInteraction): Boolean =
        textView(textView, shouldInteractWithURL, inRange)

    override fun textView(textView: UITextView, shouldInteractWithURL: NSURL, inRange: CValue<NSRange>): Boolean {
        return textView.attributedText.urlRanges.let { urls ->
            urls.firstOrNull { it.first.range == inRange.range && it.second == shouldInteractWithURL } != null
        }
    }
}

class UILinkTextViewDelegateWrapper(private val wrapped: UITextViewDelegateProtocol) : NSObject(), UITextViewDelegateProtocol {

    private val linkDelegate = UILinkTextViewDelegate()

    override fun textView(textView: UITextView, shouldChangeTextInRange: CValue<NSRange>, replacementText: String): Boolean = wrapped.textView(
        textView,
        shouldChangeTextInRange,
        replacementText,
    )

    override fun textView(textView: UITextView, shouldInteractWithURL: NSURL, inRange: CValue<NSRange>, interaction: UITextItemInteraction): Boolean {
        return linkDelegate.textView(textView, shouldInteractWithURL, inRange, interaction) || wrapped.textView(textView, shouldInteractWithURL, inRange, interaction)
    }

    override fun textView(
        textView: UITextView,
        shouldInteractWithTextAttachment: platform.UIKit.NSTextAttachment,
        inRange: CValue<NSRange>,
        interaction: UITextItemInteraction,
    ): Boolean = wrapped.textView(textView, shouldInteractWithTextAttachment, inRange)

    override fun textView(textView: UITextView, shouldInteractWithURL: NSURL, inRange: CValue<NSRange>): Boolean {
        return linkDelegate.textView(textView, shouldInteractWithURL, inRange) || wrapped.textView(textView, shouldInteractWithURL, inRange)
    }

    override fun textView(textView: UITextView, shouldInteractWithTextAttachment: platform.UIKit.NSTextAttachment, inRange: CValue<NSRange>): Boolean =
        wrapped.textView(textView, shouldInteractWithTextAttachment, inRange)

    override fun textViewDidBeginEditing(textView: UITextView) = wrapped.textViewDidBeginEditing(textView)

    override fun textViewDidChange(textView: UITextView) = wrapped.textViewDidChange(textView)

    override fun textViewDidChangeSelection(textView: UITextView) = wrapped.textViewDidChangeSelection(textView)

    override fun textViewDidEndEditing(textView: UITextView) = wrapped.textViewDidEndEditing(textView)

    override fun textViewShouldBeginEditing(textView: UITextView): Boolean = wrapped.textViewShouldBeginEditing(textView)

    override fun textViewShouldEndEditing(textView: UITextView): Boolean = wrapped.textViewShouldEndEditing(textView)

    override fun scrollViewDidChangeAdjustedContentInset(scrollView: platform.UIKit.UIScrollView) = wrapped.scrollViewDidChangeAdjustedContentInset(scrollView)

    override fun scrollViewDidEndDecelerating(scrollView: platform.UIKit.UIScrollView) = wrapped.scrollViewDidEndDecelerating(scrollView)

    override fun scrollViewDidEndDragging(scrollView: platform.UIKit.UIScrollView, willDecelerate: Boolean) = wrapped.scrollViewDidEndDragging(scrollView, willDecelerate)

    override fun scrollViewDidEndScrollingAnimation(scrollView: platform.UIKit.UIScrollView) = wrapped.scrollViewDidEndScrollingAnimation(scrollView)

    override fun scrollViewDidEndZooming(scrollView: platform.UIKit.UIScrollView, withView: platform.UIKit.UIView?, atScale: platform.CoreGraphics.CGFloat) =
        wrapped.scrollViewDidEndZooming(scrollView, withView, atScale)

    override fun scrollViewDidScroll(scrollView: platform.UIKit.UIScrollView) = wrapped.scrollViewDidScroll(scrollView)

    override fun scrollViewDidScrollToTop(scrollView: platform.UIKit.UIScrollView) = wrapped.scrollViewDidScrollToTop(scrollView)

    override fun scrollViewDidZoom(scrollView: platform.UIKit.UIScrollView) = wrapped.scrollViewDidZoom(scrollView)

    override fun scrollViewShouldScrollToTop(scrollView: platform.UIKit.UIScrollView): Boolean = wrapped.scrollViewShouldScrollToTop(scrollView)

    override fun scrollViewWillBeginDecelerating(scrollView: platform.UIKit.UIScrollView) = wrapped.scrollViewWillBeginDecelerating(scrollView)

    override fun scrollViewWillBeginDragging(scrollView: platform.UIKit.UIScrollView) = wrapped.scrollViewWillBeginDragging(scrollView)

    override fun scrollViewWillBeginZooming(scrollView: platform.UIKit.UIScrollView, withView: platform.UIKit.UIView?) = wrapped.scrollViewWillBeginZooming(scrollView, withView)

    override fun scrollViewWillEndDragging(scrollView: platform.UIKit.UIScrollView, withVelocity: CValue<CGPoint>, targetContentOffset: kotlinx.cinterop.CPointer<CGPoint>?) =
        wrapped.scrollViewWillEndDragging(scrollView, withVelocity, targetContentOffset)

    override fun viewForZoomingInScrollView(scrollView: platform.UIKit.UIScrollView): platform.UIKit.UIView? = wrapped.viewForZoomingInScrollView(scrollView)
}

private val CValue<NSRange>.range: IntRange get() = useContents { IntRange(location.toInt(), (location + length).toInt() - 1) }
