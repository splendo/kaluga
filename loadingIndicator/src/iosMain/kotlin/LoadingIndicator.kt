package com.splendo.kaluga.loadingIndicator

import platform.UIKit.*

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

class IOSLoadingIndicator private constructor(private val view: UIViewController, private val controller: UIViewController) : LoadingIndicator {

    private class DefaultView(
        private val style: LoadingIndicator.Style
    ) : UIViewController(null, null) {

        private val backgroundColor: UIColor
            get() = when (style) {
                LoadingIndicator.Style.LIGHT -> UIColor.whiteColor
                LoadingIndicator.Style.DARK -> UIColor.blackColor
                LoadingIndicator.Style.SYSTEM ->
                    if (traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark)
                        UIColor.blackColor else UIColor.whiteColor
            }

        private val foregroundColor: UIColor
            get() = when (style) {
                LoadingIndicator.Style.LIGHT -> UIColor.blackColor
                LoadingIndicator.Style.DARK -> UIColor.whiteColor
                LoadingIndicator.Style.SYSTEM ->
                    if (traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark)
                        UIColor.whiteColor else UIColor.blackColor
            }

        override fun viewDidLoad() {
            super.viewDidLoad()
            setupView()
        }

        private fun setupView() {
            // Dimmed background
            view.backgroundColor = UIColor(0.0, 1 / 3.0)
            val contentView = UIView().apply {
                backgroundColor = this@DefaultView.backgroundColor
                layer.cornerRadius = 14.0
                translatesAutoresizingMaskIntoConstraints = false
            }
            view.addSubview(contentView)
            NSLayoutConstraint.activateConstraints(
                listOf(
                    contentView.centerXAnchor.constraintEqualToAnchor(view.centerXAnchor),
                    contentView.centerYAnchor.constraintEqualToAnchor(view.centerYAnchor),
                    contentView.widthAnchor.constraintEqualToConstant(100.0),
                    contentView.heightAnchor.constraintEqualToConstant(100.0)
                )
            )
            val activityView = UIActivityIndicatorView(UIActivityIndicatorViewStyleWhiteLarge).apply {
                color = foregroundColor
                startAnimating()
                translatesAutoresizingMaskIntoConstraints = false
            }
            contentView.addSubview(activityView)
            NSLayoutConstraint.activateConstraints(
                listOf(
                    activityView.centerXAnchor.constraintEqualToAnchor(contentView.centerXAnchor),
                    activityView.centerYAnchor.constraintEqualToAnchor(contentView.centerYAnchor)
                )
            )
        }
    }

    class Builder(private val controller: UIViewController) : LoadingIndicator.Builder {
        override var style = LoadingIndicator.Style.LIGHT
        override fun create() = IOSLoadingIndicator(DefaultView(style), controller)
    }

    init {
        view.apply {
            modalPresentationStyle = UIModalPresentationOverFullScreen
            modalTransitionStyle = UIModalTransitionStyleCrossDissolve
        }
    }

    override val isVisible get() = view.presentingViewController != null

    override fun present(animated: Boolean, completion: () -> Unit): LoadingIndicator = apply {
        controller.presentViewController(view, animated, completion)
    }

    override fun dismiss(animated: Boolean, completion: () -> Unit) {
        view.presentingViewController?.dismissViewControllerAnimated(animated, completion)
    }
}
