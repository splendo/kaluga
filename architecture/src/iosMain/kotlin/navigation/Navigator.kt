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

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import kotlinx.cinterop.pointed
import platform.CoreGraphics.CGFloat
import platform.Foundation.NSURL
import platform.MessageUI.MFMailComposeViewController
import platform.MessageUI.MFMessageComposeViewController
import platform.UIKit.NSLayoutAttributeBottom
import platform.UIKit.NSLayoutAttributeLeading
import platform.UIKit.NSLayoutAttributeTop
import platform.UIKit.NSLayoutAttributeTrailing
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.NSLayoutRelationEqual
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UIDocumentBrowserViewController
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.addConstraint
import platform.UIKit.addSubview
import platform.UIKit.childViewControllers
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.navigationController
import platform.UIKit.removeFromParentViewController
import platform.UIKit.removeFromSuperview
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.willMoveToParentViewController
import platform.darwin.NSInteger
import kotlin.native.internal.GC
import kotlin.native.ref.WeakReference

actual interface Navigator<A : NavigationAction<*>> : LifecycleSubscribable {
    actual fun navigate(action: A)
}

/**
 * Implementation of [Navigator] used for navigating to and from [UIViewController]. Takes a mapper function to map all [NavigationAction] to a [NavigationSpec]
 * Whenever [navigate] is called, this class maps it to a [NavigationSpec] and performs navigation according to that
 * @param parent The [UIViewController] managing the navigation
 * @param navigationMapper A function mapping the [NavigationAction] to [NavigationSpec]
 */
class ViewControllerNavigator<A : NavigationAction<*>>(parentVC: UIViewController, private val navigationMapper: (A) -> NavigationSpec) : Navigator<A> {

    private val parent = WeakReference(parentVC)

    override fun navigate(action: A) {
        navigate(navigationMapper.invoke(action), action.bundle)
    }

    private fun navigate(spec: NavigationSpec, bundle: NavigationBundle<*>?) {
        when (spec) {
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
        // Since navigation often references UIViewControllers they should be freed up to keep ARC working
        GC.collect()
    }

    private fun pushViewController(pushSpec: NavigationSpec.Push) {
        val parent = assertParent() ?: return
        assert(parent.navigationController != null)
        parent.navigationController?.pushViewController(pushSpec.push(), pushSpec.animated)
    }

    private fun popViewController(popSpec: NavigationSpec.Pop) {
        val parent = assertParent() ?: return
        assert(parent.navigationController != null)
        popSpec.to?.let {
            parent.navigationController?.popToViewController(it, popSpec.animated)
        } ?: parent.navigationController?.popViewControllerAnimated(popSpec.animated)
    }

    private fun presentViewController(presentSpec: NavigationSpec.Present) {
        val toPresent = presentSpec.present()
        toPresent.modalPresentationStyle = presentSpec.presentationStyle
        toPresent.modalTransitionStyle = presentSpec.transitionStyle
        assertParent()?.presentViewController(toPresent, presentSpec.animated) { presentSpec.completion?.invoke() }
    }

    private fun dismissViewController(dismissSpec: NavigationSpec.Dismiss) {
        val parent = assertParent() ?: return
        val toDismiss = dismissSpec.toDismiss(parent)
        toDismiss.dismissViewControllerAnimated(dismissSpec.animated) { dismissSpec.completion?.invoke() }
    }

    private fun showViewController(showSpec: NavigationSpec.Show) {
        val toShow = showSpec.show()
        val parent = assertParent() ?: return
        if (showSpec.detail) {
            parent.showDetailViewController(toShow, parent)
        } else {
            parent.showViewController(toShow, parent)
        }
    }

    private fun segueToViewController(segueSpec: NavigationSpec.Segue, bundle: NavigationBundle<*>?) {
        assertParent()?.performSegueWithIdentifier(segueSpec.identifier, bundle)
    }

    private fun embedNestedViewController(nestedSpec: NavigationSpec.Nested) {
        val parent = assertParent() ?: return
        when (val type = nestedSpec.type) {
            is NavigationSpec.Nested.Type.Replace -> {
                parent.childViewControllers.map { it as UIViewController }.filter { it.view.tag.toLong() == type.tag }.forEach {
                    it.willMoveToParentViewController(null)
                    it.view.removeFromSuperview()
                    it.removeFromParentViewController()
                }
            }
        }

        val child = nestedSpec.nested()
        when (val type = nestedSpec.type) {
            is NavigationSpec.Nested.Type.Replace -> {
                child.view.tag = type.tag as NSInteger
            }
        }
        child.view.translatesAutoresizingMaskIntoConstraints = false
        parent.addChildViewController(child)
        nestedSpec.containerView.addSubview(child.view)
        val constraints = nestedSpec.constraints?.invoke(child.view, nestedSpec.containerView) ?: listOf(
            NSLayoutAttributeLeading,
            NSLayoutAttributeTrailing,
            NSLayoutAttributeTop,
            NSLayoutAttributeBottom
        ).map { attribute ->
            CGFloat
            NSLayoutConstraint.constraintWithItem(child.view, attribute, NSLayoutRelationEqual, nestedSpec.containerView, attribute, 1.0 as CGFloat, 0.0 as CGFloat)
        }
        constraints.forEach { nestedSpec.containerView.addConstraint(it) }
        child.didMoveToParentViewController(parent)
    }

    private fun presentImagePicker(imagePickerSpec: NavigationSpec.ImagePicker) {
        val isSourceTypeAvailable = UIImagePickerController.isSourceTypeAvailable(imagePickerSpec.sourceType)
        assert(isSourceTypeAvailable)
        if (!isSourceTypeAvailable) return
        val mediaTypes = imagePickerSpec.mediaType.map { it.typeString?.pointed.toString() }
        val isMediaTypesAvailable = UIImagePickerController.availableMediaTypesForSourceType(imagePickerSpec.sourceType)?.containsAll(mediaTypes) == true
        assert(isMediaTypesAvailable)
        if (!isMediaTypesAvailable) return
        val pickerVC = UIImagePickerController()
        pickerVC.sourceType = imagePickerSpec.sourceType
        pickerVC.mediaTypes = mediaTypes
        pickerVC.delegate = imagePickerSpec.delegate
        assertParent()?.presentViewController(pickerVC, imagePickerSpec.animated) { imagePickerSpec.completion?.invoke() }
    }

    private fun presentMailComposer(mailSpec: NavigationSpec.Email) {
        val canSendMail = MFMailComposeViewController.canSendMail()
        assert(canSendMail)
        if (!canSendMail) return
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

        assertParent()?.presentViewController(composeVC, mailSpec.animated) { mailSpec.completion?.invoke() }
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

        assertParent()?.presentViewController(browserVc, browserSpec.animated) { browserSpec.completion?.invoke() }
    }

    private fun presentMessageComposer(messageSpec: NavigationSpec.Message) {
        val canSendText = MFMessageComposeViewController.canSendText()
        assert(canSendText)
        if (!canSendText) return
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

        assertParent()?.presentViewController(composeVC, messageSpec.animated) { messageSpec.completion?.invoke() }
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

    private fun assertParent() = parent.get().also { assert(it != null) }
}
