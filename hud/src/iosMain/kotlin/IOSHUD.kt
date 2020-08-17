@file:Suppress("USELESS_CAST") // as CGFloat is needed for arm32.

package com.splendo.kaluga.hud

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.cinterop.CValue
import kotlinx.coroutines.CoroutineScope
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRect
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleWhiteLarge
import platform.UIKit.UIColor
import platform.UIKit.UILabel
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UIModalPresentationOverFullScreen
import platform.UIKit.UIModalTransitionStyleCrossDissolve
import platform.UIKit.UIScreen
import platform.UIKit.UIStackView
import platform.UIKit.UIStackViewAlignmentCenter
import platform.UIKit.UIStackViewDistributionFill
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.autoresizingMask
import platform.UIKit.backgroundColor
import platform.UIKit.bottomAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.centerYAnchor
import platform.UIKit.colorNamed
import platform.UIKit.heightAnchor
import platform.UIKit.hidden
import platform.UIKit.leadingAnchor
import platform.UIKit.setBackgroundColor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor
import platform.UIKit.window

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

class IOSHUD private constructor(private val containerView: ContainerView, private val viewController: UIViewController, wrapper: (UIViewController) -> UIViewController, coroutineScope: CoroutineScope) : HUD, CoroutineScope by coroutineScope {

    class Builder(private val viewController: UIViewController, private val wrapper: (UIViewController) -> UIViewController = { it }) : HUD.Builder() {
        override fun create(hudConfig: HudConfig, coroutineScope: CoroutineScope) = IOSHUD(
            ContainerView(hudConfig, viewController.view.window?.bounds ?: UIScreen.mainScreen.bounds),
            viewController,
            wrapper,
            coroutineScope
        )
    }

    private class ContainerView(
        private val hudConfig: HudConfig,
        frame: CValue<CGRect>
    ) : UIView(frame) {

        private val titleLabel: UILabel

        private val backgroundColor: UIColor
            get() = when (hudConfig.style) {
                HUD.Style.CUSTOM -> UIColor.colorNamed("li_colorBackground") ?: UIColor.lightGrayColor
                HUD.Style.SYSTEM ->
                    if (traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark)
                        UIColor.blackColor else UIColor.whiteColor
            }

        private val foregroundColor: UIColor
            get() = when (hudConfig.style) {
                HUD.Style.CUSTOM -> UIColor.colorNamed("li_colorAccent") ?: UIColor.darkGrayColor
                HUD.Style.SYSTEM ->
                    if (traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark)
                        UIColor.whiteColor else UIColor.blackColor
            }

        // NOTES: Cast to CGFloat is needed for Arm32
        init {
            // Rotation support
            autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
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
                text = hudConfig.title
                hidden = hudConfig.title?.isEmpty() ?: true
                numberOfLines = 0 // Multiline support
            }.also {
                stackView.addArrangedSubview(it)
            }
        }
    }

    private val hudViewController = wrapper(UIViewController(null, null).apply {
        modalPresentationStyle = UIModalPresentationOverFullScreen
        modalTransitionStyle = UIModalTransitionStyleCrossDissolve
        view.backgroundColor = UIColor.clearColor
        view.addSubview(containerView)
    })

    private val topViewController: UIViewController
        get() {
            var result: UIViewController? = viewController
            while (result?.presentedViewController != null) {
                result = result.presentedViewController
            }
            return result ?: viewController
        }

    override val isVisible: Boolean get() = hudViewController.presentingViewController() != null

    override suspend fun present(animated: Boolean): HUD = suspendCoroutine { continuation ->
        if (!isVisible) {
            topViewController.presentViewController(hudViewController, animated) {
                continuation.resume(this)
            }
        } else {
            continuation.resume(this)
        }
    }

    override suspend fun dismiss(animated: Boolean) = suspendCoroutine<Unit> { continuation ->
        if (isVisible) {
            hudViewController.presentingViewController()?.dismissViewControllerAnimated(animated) {
                continuation.resume(Unit)
            }
        } else {
            continuation.resume(Unit)
        }
    }
}
