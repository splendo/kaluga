package com.splendo.kaluga.hud

import kotlinx.cinterop.CValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.UIKit.*
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

interface WindowProvider {
    val topmostWindow: UIWindow?
}

fun UIApplication.asTopmostWindowProvider() = object : WindowProvider {
    override val topmostWindow: UIWindow?
        get() = windows
            .reversed() /* top most is at the end of the list */
            .mapNotNull { it as? UIWindow } /* thanks to kotlin it returns List<*> and not List<UIWindow> */
            .firstOrNull {
                val isOnMainScreen = it.screen == UIScreen.mainScreen
                val isVisible = !it.hidden && it.alpha > 0.0
                val isLevelSupported = it.windowLevel == UIWindowLevelNormal
                val isKeyWindow = it.isKeyWindow()
                return@firstOrNull isOnMainScreen && isVisible && isLevelSupported && isKeyWindow
            }
}

class IOSHUD private constructor(private val containerView: ContainerView, private val windowProvider: WindowProvider) : HUD {

    private class ContainerView(
        private val style: HUD.Style,
        private val title: String?,
        frame: CValue<CGRect>
    ) : UIView(frame) {

        val titleLabel: UILabel

        private val backgroundColor: UIColor
            get() = when (style) {
                HUD.Style.CUSTOM -> UIColor.colorNamed("li_colorBackground") ?: UIColor.lightGrayColor
                HUD.Style.SYSTEM ->
                    if (traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark)
                        UIColor.blackColor else UIColor.whiteColor
            }

        private val foregroundColor: UIColor
            get() = when (style) {
                HUD.Style.CUSTOM -> UIColor.colorNamed("li_colorAccent") ?: UIColor.darkGrayColor
                HUD.Style.SYSTEM ->
                    if (traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark)
                        UIColor.whiteColor else UIColor.blackColor
            }

        // NOTES: Cast to CGFloat is needed for Arm32
        init {
            autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
            // Hidden by default
            alpha = 0.0 as CGFloat
            // Dimmed background
            setBackgroundColor(UIColor(0.0 as CGFloat, (1 / 3.0) as CGFloat))
            // Main HUD view is stack view
            val stackView = UIStackView().apply {
                axis = UILayoutConstraintAxisVertical
                alignment = UIStackViewAlignmentCenter
                distribution = UIStackViewDistributionFill
                spacing = 8.0 as CGFloat
                translatesAutoresizingMaskIntoConstraints = false
            }
            addSubview(stackView)

            // Stack view background view
            val contentView = UIView().apply {
                backgroundColor = this@ContainerView.backgroundColor
                layer.cornerRadius = 14.0 as CGFloat
                translatesAutoresizingMaskIntoConstraints = false
            }
            stackView.addSubview(contentView)
            NSLayoutConstraint.activateConstraints(
                listOf(
                    contentView.leadingAnchor.constraintEqualToAnchor(stackView.leadingAnchor, -32.0 as CGFloat),
                    contentView.trailingAnchor.constraintEqualToAnchor(stackView.trailingAnchor, 32.0 as CGFloat),
                    contentView.topAnchor.constraintEqualToAnchor(stackView.topAnchor, -32.0 as CGFloat),
                    contentView.bottomAnchor.constraintEqualToAnchor(stackView.bottomAnchor, 32.0 as CGFloat),
                    stackView.centerXAnchor.constraintEqualToAnchor(centerXAnchor),
                    stackView.centerYAnchor.constraintEqualToAnchor(centerYAnchor),
                    stackView.widthAnchor.constraintGreaterThanOrEqualToConstant(64.0 as CGFloat),
                    stackView.widthAnchor.constraintLessThanOrEqualToAnchor(widthAnchor, 0.5 as CGFloat),
                    stackView.heightAnchor.constraintGreaterThanOrEqualToConstant(64.0 as CGFloat),
                    stackView.heightAnchor.constraintLessThanOrEqualToAnchor(heightAnchor, 0.5 as CGFloat)
                )
            )
            // Activity indicator
            UIActivityIndicatorView(UIActivityIndicatorViewStyleWhiteLarge).apply {
                color = foregroundColor
                startAnimating()
            }.also {
                stackView.addArrangedSubview(it)
            }
            // Place title label
            titleLabel = UILabel().apply {
                text = title
                hidden = title?.isEmpty() ?: true
                numberOfLines = 0 // Multiline support
            }.also {
                stackView.addArrangedSubview(it)
            }
        }
    }

    open class Builder : HUD.Builder {

        open val windowProvider = UIApplication.sharedApplication.asTopmostWindowProvider()

        override var title: String? = null
        override var style = HUD.Style.SYSTEM
        override fun create() = IOSHUD(
            ContainerView(style, title, windowProvider.topmostWindow?.bounds ?: UIScreen.mainScreen.bounds),
            windowProvider
        )
    }

    private val topmostWindow get() = windowProvider.topmostWindow

    override val isVisible get() = containerView.superview != null

    override fun present(animated: Boolean, completion: () -> Unit): HUD = apply {
        if (!isVisible) {
            topmostWindow?.addSubview(containerView)
            if (animated) {
                UIView.animateWithDuration(1 / 3.0 as CGFloat, {
                    containerView.alpha = 1.0 as CGFloat
                }, {
                    completion()
                })
            } else {
                containerView.alpha = 1.0 as CGFloat
                completion()
            }
        } else {
            topmostWindow?.bringSubviewToFront(containerView)
            completion()
        }
    }

    override fun dismiss(animated: Boolean, completion: () -> Unit) {
        if (isVisible) {
            if (animated) {
                UIView.animateWithDuration(1 / 3.0 as CGFloat, {
                    containerView.alpha = 0.0 as CGFloat
                }, {
                    containerView.removeFromSuperview()
                    completion()
                })
            } else {
                containerView.alpha = 0.0 as CGFloat
                containerView.removeFromSuperview()
                completion()
            }
        } else {
            completion()
        }
    }

    override fun dismissAfter(timeMillis: Long, animated: Boolean) = apply {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, timeMillis * 1_000_000), dispatch_get_main_queue()) {
            dismiss(animated)
        }
    }

    override fun setTitle(title: String?) {
        containerView.titleLabel.text = title
        containerView.titleLabel.hidden = title?.isEmpty() ?: true
    }
}
