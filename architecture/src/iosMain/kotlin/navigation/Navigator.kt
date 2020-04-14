/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.navigation

import kotlinx.cinterop.pointed
import platform.CoreGraphics.CGFloat
import platform.Foundation.NSURL
import platform.MessageUI.MFMailComposeViewController
import platform.MessageUI.MFMessageComposeViewController
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UIDocumentBrowserViewController
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.addSubview
import platform.UIKit.childViewControllers
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.navigationController
import platform.UIKit.removeFromParentViewController
import platform.UIKit.removeFromSuperview
import platform.UIKit.willMoveToParentViewController

actual class Navigator<A : NavigationAction<*>>(private val parent: UIViewController, private val navigationMapper: (A) -> NavigationSpec) {

    actual fun navigate(action: A) {
        navigate(navigationMapper.invoke(action), action.bundle)
    }

    private fun navigate(spec: NavigationSpec, bundle: NavigationBundle<*>?) {
        when(spec) {
            is NavigationSpec.Push -> pushViewController(spec)
            is NavigationSpec.Pop -> popViewController(spec)
            is NavigationSpec.Present -> presentViewController(spec)
            is NavigationSpec.Dismiss -> dismissViewController(spec)
            is NavigationSpec.Show -> showViewController(spec)
            is NavigationSpec.Segue -> segueToViewController(spec, bundle)
            is NavigationSpec.Nested -> embedNestedViewController(spec)
            is NavigationSpec.ImagePicker -> presentImagePicker(spec)
            is NavigationSpec.Email -> presentMailComposer(spec)
            is NavigationSpec.DocumentSelector -> presentDocumentBrowser(spec)
            is NavigationSpec.Message -> presentMessageComposer(spec)
            is NavigationSpec.Phone -> openDialer(spec)
            is NavigationSpec.Settings -> openSettings()
            is NavigationSpec.Browser -> openBrowser(spec)
        }
    }

    private fun pushViewController(pushSpec: NavigationSpec.Push) {
        parent.navigationController?.pushViewController(pushSpec.push(), pushSpec.animated)
    }

    private fun popViewController(popSpec: NavigationSpec.Pop) {
        popSpec.to?.let {
            parent.navigationController?.popToViewController(it, popSpec.animated)
        } ?: parent.navigationController?.popViewControllerAnimated(popSpec.animated)
    }

    private fun presentViewController(presentSpec: NavigationSpec.Present) {
        val toPresent = presentSpec.present()
        toPresent.modalPresentationStyle = presentSpec.presentationStyle
        toPresent.modalTransitionStyle = presentSpec.transitionStyle
        parent.presentViewController(toPresent, presentSpec.animated) { presentSpec.complete?.invoke() }
    }

    private fun dismissViewController(dismissSpec: NavigationSpec.Dismiss) {
        parent.dismissViewControllerAnimated(dismissSpec.animated) { dismissSpec.complete?.invoke() }
    }

    private fun showViewController(showSpec: NavigationSpec.Show) {
        val toShow = showSpec.show()
        if (showSpec.detail) {
            showSpec.parent.showDetailViewController(toShow, showSpec.parent)
        } else {
            showSpec.parent.showViewController(toShow, null)
        }
    }

    private fun segueToViewController(segueSpec: NavigationSpec.Segue, bundle: NavigationBundle<*>?) {
        segueSpec.parent.performSegueWithIdentifier(segueSpec.identifier, bundle)
    }

    private fun embedNestedViewController(nestedSpec: NavigationSpec.Nested) {
        if (nestedSpec.type == NavigationSpec.Nested.Type.Replace) {
            parent.childViewControllers.map { it as UIViewController }.forEach {
                it.willMoveToParentViewController(null)
                it.view.removeFromSuperview()
                it.removeFromParentViewController()
            }
        }

        val child = nestedSpec.nested()
        parent.addChildViewController(child)
        nestedSpec.containerView.addSubview(child.view)
        NSLayoutConstraint.activateConstraints(nestedSpec.constraints(child.view, nestedSpec.containerView))
        child.didMoveToParentViewController(parent)
    }

    private fun presentImagePicker(imagePickerSpec: NavigationSpec.ImagePicker) {
        if (!UIImagePickerController.isSourceTypeAvailable(imagePickerSpec.sourceType)) return
        val mediaTypes = imagePickerSpec.mediaType.map { it.typeString?.pointed.toString() }
        if (UIImagePickerController.availableMediaTypesForSourceType(imagePickerSpec.sourceType)?.containsAll(mediaTypes) != true) return
        val pickerVC = UIImagePickerController()
        pickerVC.sourceType = imagePickerSpec.sourceType
        pickerVC.mediaTypes = mediaTypes
        pickerVC.delegate = imagePickerSpec.delegate
        pickerVC.presentViewController(pickerVC, imagePickerSpec.animated) {imagePickerSpec.complete?.invoke()}
    }

    private fun presentMailComposer(mailSpec: NavigationSpec.Email) {
        if (!MFMailComposeViewController.canSendMail()) return
        val composeVC = MFMailComposeViewController()
        composeVC.mailComposeDelegate = mailSpec.delegate

        val settings = mailSpec.emailSettings
        composeVC.setToRecipients(settings.to)
        composeVC.setCcRecipients(settings.cc)
        composeVC.setBccRecipients(settings.bcc)
        settings.subject?.let {
            composeVC.setSubject(it)
        }
        settings.body?.let {
            composeVC.setMessageBody(it, settings.type is NavigationSpec.Email.Type.Stylized)
        }

        settings.attachments.forEach {
            composeVC.addAttachmentData(it.data, it.mimeType, it.fileName)
        }

        parent.presentViewController(composeVC, mailSpec.animated) {mailSpec.complete?.invoke()}
    }

    private fun presentDocumentBrowser(browserSpec: NavigationSpec.DocumentSelector) {
        val settings = browserSpec.documentSelectorSettings
        val browserVc = UIDocumentBrowserViewController(settings.types.toList())
        browserVc.allowsDocumentCreation = settings.allowCreation
        browserVc.allowsPickingMultipleItems = settings.allowMultiple

        val appearance = browserSpec.documentSelectorAppearance
        browserVc.browserUserInterfaceStyle = appearance.interfaceStyle
        browserVc.additionalLeadingNavigationBarButtonItems = appearance.leadingNavigationBarButtonItems
        browserVc.additionalTrailingNavigationBarButtonItems = appearance.trailingNavigationBarButtonItems
        browserVc.shouldShowFileExtensions = appearance.showFileExtensions
        browserVc.customActions = appearance.customActions
        browserVc.defaultDocumentAspectRatio = appearance.documentAspectRatio as CGFloat
        browserVc.localizedCreateDocumentActionTitle = appearance.createTitle

        browserVc.delegate = browserSpec.delegate

        parent.presentViewController(browserVc, browserSpec.animated) {browserSpec.complete?.invoke()}
    }

    private fun presentMessageComposer(messageSpec: NavigationSpec.Message) {
        if (!MFMessageComposeViewController.canSendText()) return
        val composeVC = MFMessageComposeViewController()
        composeVC.messageComposeDelegate = messageSpec.delegate

        val settings = messageSpec.messageSettings
        composeVC.setRecipients(settings.recipients)
        if (MFMessageComposeViewController.canSendSubject()) {
            settings.subject?.let {
                composeVC.setSubject(it)
            }
        }
        settings.body?.let { composeVC.setBody(it) }
        settings.message?.let { composeVC.setMessage(it) }

        if (MFMessageComposeViewController.canSendAttachments()) {
            settings.attachments.forEach {
                composeVC.addAttachmentData(it.data, it.mimeType, it.fileName)
            }
            if (settings.disableAttachments) {
                composeVC.disableUserAttachments()
            }
        }

        parent.presentViewController(composeVC, messageSpec.animated) {messageSpec.complete?.invoke()}
    }

    private fun openDialer(phoneSpec: NavigationSpec.Phone) {
        openUrl("tel://${phoneSpec.phoneNumber}")
    }

    private fun openSettings() {
        openUrl(UIApplicationOpenSettingsURLString)
    }

    private fun openBrowser(browserSpec: NavigationSpec.Browser) {
        UIApplication.sharedApplication.openURL(browserSpec.url)
    }

    private fun openUrl(urlString: String) {
        NSURL.URLWithString(urlString)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }

}

