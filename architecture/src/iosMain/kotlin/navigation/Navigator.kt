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

package com.splendo.kaluga.architecture.navigation

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import kotlinx.cinterop.pointed
import platform.CoreGraphics.CGFloat
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.numberWithInt
import platform.MediaPlayer.MPMediaPickerController
import platform.MessageUI.MFMailComposeViewController
import platform.MessageUI.MFMessageComposeViewController
import platform.QuartzCore.CATransaction
import platform.SafariServices.SFSafariViewController
import platform.StoreKit.SKStoreProductParameterAdvertisingPartnerToken
import platform.StoreKit.SKStoreProductParameterAffiliateToken
import platform.StoreKit.SKStoreProductParameterCampaignToken
import platform.StoreKit.SKStoreProductParameterITunesItemIdentifier
import platform.StoreKit.SKStoreProductParameterProductIdentifier
import platform.StoreKit.SKStoreProductParameterProviderToken
import platform.StoreKit.SKStoreProductViewController
import platform.StoreKit.SKStoreProductViewControllerDelegateProtocol
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
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.childViewControllers
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.navigationController
import platform.UIKit.removeFromParentViewController
import platform.UIKit.willMoveToParentViewController
import platform.darwin.NSObject
import kotlin.native.ref.WeakReference

/**
 * Class that can trigger a given [NavigationAction]
 * @param Action the type of [NavigationAction] this navigator should respond to.
 */
actual interface Navigator<Action : NavigationAction<*>> : LifecycleSubscribable {
    actual fun navigate(action: Action)
}

class DefaultNavigator<Action : NavigationAction<*>>(val onAction: (Action) -> Unit) : Navigator<Action> {
    override fun navigate(action: Action) {
        onAction(action)
    }
}

object MissingViewControllerNavigationException : NavigationException("Missing Parent ViewController")
object MissingNavigationControllerNavigationException : NavigationException("Missing Navigation ViewController")
object MailNotSupportedNavigationException : NavigationException("Cannot send Mail")
object TextNotSupportedNavigationException : NavigationException("Cannot send Text")
data class ImagePickerSourceNotAvailableNavigationException(val source: UIImagePickerControllerSourceType) : NavigationException(
    "Source Type $source not available for ImagePicker",
)
data class ImagePickerMediaNotAvailableNavigationException(val types: Set<NavigationSpec.ImagePicker.MediaType>) : NavigationException(
    "Media Types ${types.joinToString(", ")} not available for ImagePicker",
)

/**
 * Implementation of [Navigator] used for navigating to and from [UIViewController]. Takes a mapper function to map all [NavigationAction] to a [NavigationSpec]
 * Whenever [navigate] is called, this class maps it to a [NavigationSpec] and performs navigation according to that
 * @param parent The [UIViewController] managing the navigation
 * @param navigationMapper A function mapping the [NavigationAction] to [NavigationSpec]
 */
class ViewControllerNavigator<Action : NavigationAction<*>>(
    parentVC: UIViewController,
    private val navigationMapper: (Action) -> NavigationSpec,
) : Navigator<Action> {

    private inner class StoreKitDelegate : NSObject(), SKStoreProductViewControllerDelegateProtocol {

        override fun productViewControllerDidFinish(viewController: SKStoreProductViewController) {
            viewController.dismissViewControllerAnimated(true, null)
        }
    }

    private val parent = WeakReference(parentVC)
    private val storeKitDelegate: StoreKitDelegate by lazy { StoreKitDelegate() }

    override fun navigate(action: Action) {
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
            is NavigationSpec.MediaPicker -> presentMediaPicker(spec)
            is NavigationSpec.Email -> presentMailComposer(spec)
            is NavigationSpec.DocumentSelector -> presentDocumentBrowser(spec)
            is NavigationSpec.Message -> presentMessageComposer(spec)
            is NavigationSpec.Phone -> openDialer(spec)
            is NavigationSpec.Settings -> openSettings()
            is NavigationSpec.Browser -> openBrowser(spec)
            is NavigationSpec.ThirdParty -> openThirdPartyApp(spec)
        }
    }

    private fun pushViewController(pushSpec: NavigationSpec.Push) {
        val parent = assertParent()
        val navigationController = parent.navigationController ?: throw MissingNavigationControllerNavigationException
        CATransaction.begin()
        CATransaction.setCompletionBlock {
            pushSpec.completion?.invoke()
        }
        navigationController.pushViewController(pushSpec.push(), pushSpec.animated)
        CATransaction.commit()
    }

    private fun popViewController(popSpec: NavigationSpec.Pop) {
        val parent = assertParent()
        val navigationController = parent.navigationController ?: throw MissingNavigationControllerNavigationException
        CATransaction.begin()
        CATransaction.setCompletionBlock {
            popSpec.completion?.invoke()
        }
        popSpec.to?.let {
            navigationController.popToViewController(it, popSpec.animated)
        } ?: navigationController.popViewControllerAnimated(popSpec.animated)
        CATransaction.commit()
    }

    private fun presentViewController(presentSpec: NavigationSpec.Present) {
        val toPresent = presentSpec.present()
        toPresent.modalPresentationStyle = presentSpec.presentationStyle
        toPresent.modalTransitionStyle = presentSpec.transitionStyle
        assertParent().presentViewController(toPresent, presentSpec.animated) { presentSpec.completion?.invoke() }
    }

    private fun dismissViewController(dismissSpec: NavigationSpec.Dismiss) {
        val parent = assertParent()
        val toDismiss = dismissSpec.toDismiss(parent)
        toDismiss.dismissViewControllerAnimated(dismissSpec.animated) { dismissSpec.completion?.invoke() }
    }

    private fun showViewController(showSpec: NavigationSpec.Show) {
        val toShow = showSpec.show()
        val parent = assertParent()
        if (showSpec.detail) {
            parent.showDetailViewController(toShow, parent)
        } else {
            parent.showViewController(toShow, parent)
        }
    }

    private fun segueToViewController(segueSpec: NavigationSpec.Segue, bundle: NavigationBundle<*>?) {
        assertParent().performSegueWithIdentifier(segueSpec.identifier, bundle)
    }

    private fun embedNestedViewController(nestedSpec: NavigationSpec.Nested) {
        val parent = assertParent()
        val type = nestedSpec.type
        if (type is NavigationSpec.Nested.Type.Replace) {
            parent.childViewControllers.map { it as UIViewController }.filter { it.view.tag.toLong() == type.tag }.forEach {
                it.willMoveToParentViewController(null)
                it.view.removeFromSuperview()
                it.removeFromParentViewController()
            }
        }

        val child = nestedSpec.nested()

        if (type is NavigationSpec.Nested.Type.Replace) {
            child.view.tag = type.tag
        }
        child.view.translatesAutoresizingMaskIntoConstraints = false
        parent.addChildViewController(child)
        nestedSpec.containerView.addSubview(child.view)
        val constraints = nestedSpec.constraints?.invoke(child.view, nestedSpec.containerView) ?: listOf(
            NSLayoutAttributeLeading,
            NSLayoutAttributeTrailing,
            NSLayoutAttributeTop,
            NSLayoutAttributeBottom,
        ).map { attribute ->
            CGFloat
            NSLayoutConstraint.constraintWithItem(child.view, attribute, NSLayoutRelationEqual, nestedSpec.containerView, attribute, 1.0, 0.0)
        }
        constraints.forEach { nestedSpec.containerView.addConstraint(it) }
        child.didMoveToParentViewController(parent)
    }

    private fun presentImagePicker(imagePickerSpec: NavigationSpec.ImagePicker) {
        if (!UIImagePickerController.isSourceTypeAvailable(imagePickerSpec.sourceType)) throw ImagePickerSourceNotAvailableNavigationException(imagePickerSpec.sourceType)
        val mediaTypes = imagePickerSpec.mediaType.map { it.typeString?.pointed.toString() }
        val isMediaTypesAvailable = UIImagePickerController.availableMediaTypesForSourceType(imagePickerSpec.sourceType)?.containsAll(mediaTypes) == true
        if (!isMediaTypesAvailable) throw ImagePickerMediaNotAvailableNavigationException(imagePickerSpec.mediaType)
        val pickerVC = UIImagePickerController()
        pickerVC.sourceType = imagePickerSpec.sourceType
        pickerVC.mediaTypes = mediaTypes
        pickerVC.delegate = imagePickerSpec.delegate
        assertParent().presentViewController(pickerVC, imagePickerSpec.animated) { imagePickerSpec.completion?.invoke() }
    }

    private fun presentMediaPicker(mediaPickerSpec: NavigationSpec.MediaPicker) {
        val mediaPickerController = MPMediaPickerController(mediaPickerSpec.types.fold(0UL) { acc, type -> acc or type.mediaType })

        mediaPickerController.delegate = mediaPickerSpec.delegate
        mediaPickerController.allowsPickingMultipleItems = mediaPickerSpec.settings.allowsPickingMultipleItems
        mediaPickerController.showsCloudItems = mediaPickerSpec.settings.showsCloudItems
        mediaPickerController.prompt = mediaPickerSpec.settings.prompt
        mediaPickerController.showsItemsWithProtectedAssets = mediaPickerSpec.settings.showsItemsWithProtectedAssets

        assertParent().presentViewController(mediaPickerController, mediaPickerSpec.animated) { mediaPickerSpec.completion?.invoke() }
    }

    private fun presentMailComposer(mailSpec: NavigationSpec.Email) {
        if (!MFMailComposeViewController.canSendMail()) throw MailNotSupportedNavigationException
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

        assertParent().presentViewController(composeVC, mailSpec.animated) { mailSpec.completion?.invoke() }
    }

    private fun presentDocumentBrowser(browserSpec: NavigationSpec.DocumentSelector) {
        val settings = browserSpec.documentSelectorSettings
        val browserVc = UIDocumentBrowserViewController(forOpeningFilesWithContentTypes = settings.types.toList())
        browserVc.allowsDocumentCreation = settings.allowCreation
        browserVc.allowsPickingMultipleItems = settings.allowMultiple

        val appearance = browserSpec.documentSelectorAppearance
        browserVc.browserUserInterfaceStyle = appearance.interfaceStyle
        browserVc.additionalLeadingNavigationBarButtonItems = appearance.leadingNavigationBarButtonItems
        browserVc.additionalTrailingNavigationBarButtonItems = appearance.trailingNavigationBarButtonItems
        browserVc.shouldShowFileExtensions = appearance.showFileExtensions
        browserVc.customActions = appearance.customActions
        browserVc.defaultDocumentAspectRatio = appearance.documentAspectRatio
        browserVc.localizedCreateDocumentActionTitle = appearance.createTitle

        browserVc.delegate = browserSpec.delegate

        assertParent().presentViewController(browserVc, browserSpec.animated) { browserSpec.completion?.invoke() }
    }

    private fun presentMessageComposer(messageSpec: NavigationSpec.Message) {
        if (!MFMessageComposeViewController.canSendText()) throw TextNotSupportedNavigationException
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
        settings.message?.let { composeVC.setMessageWorkAround(it) }

        if (MFMessageComposeViewController.canSendAttachments()) {
            settings.attachments.forEach {
                composeVC.addAttachmentData(it.data, it.mimeType, it.fileName)
            }
            if (settings.disableAttachments) {
                composeVC.disableUserAttachments()
            }
        }

        assertParent().presentViewController(composeVC, messageSpec.animated) { messageSpec.completion?.invoke() }
    }

    private fun openDialer(phoneSpec: NavigationSpec.Phone) {
        openUrl("tel://${phoneSpec.phoneNumber}")
    }

    private fun openSettings() {
        openUrl(UIApplicationOpenSettingsURLString)
    }

    private fun openBrowser(browserSpec: NavigationSpec.Browser) {
        when (val spec = browserSpec.viewType) {
            NavigationSpec.Browser.Type.Normal -> {
                UIApplication.sharedApplication.openURL(browserSpec.url)
            }
            is NavigationSpec.Browser.Type.SafariView -> {
                val safariVc = SFSafariViewController(browserSpec.url)
                assertParent().presentViewController(safariVc, spec.animated) {
                    spec.completion?.invoke()
                }
            }
        }
    }

    private fun openUrl(urlString: String) {
        NSURL.URLWithString(urlString)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }

    private fun openThirdPartyApp(thirdPartySpec: NavigationSpec.ThirdParty) {
        when (val openMode = thirdPartySpec.openMode) {
            is NavigationSpec.ThirdParty.OpenMode.AppStore -> openAppStore(openMode.storeInfo)
            is NavigationSpec.ThirdParty.OpenMode.FallbackToAppStore -> openThirdPartyApp(openMode.urlScheme, openMode.storeInfo)
            is NavigationSpec.ThirdParty.OpenMode.OnlyWhenInstalled -> openThirdPartyApp(openMode.url, null)
        }
    }

    private fun openThirdPartyApp(urlScheme: NSURL, storeInfo: NavigationSpec.ThirdParty.StoreInfo?) {
        if (UIApplication.sharedApplication.canOpenURL(urlScheme)) {
            UIApplication.sharedApplication.openURL(urlScheme)
        } else {
            storeInfo?.let {
                openAppStore(storeInfo)
            }
        }
    }

    private fun openAppStore(storeInfo: NavigationSpec.ThirdParty.StoreInfo) {
        val parent = assertParent()
        val parameters = mutableMapOf<Any?, Any>(SKStoreProductParameterITunesItemIdentifier to NSNumber.numberWithInt(storeInfo.appId))
        storeInfo.productId?.let {
            parameters[SKStoreProductParameterProductIdentifier] = it
        }
        storeInfo.advertisingPartnerToken?.let {
            parameters[SKStoreProductParameterAdvertisingPartnerToken] = it
        }
        storeInfo.affiliateToken?.let {
            parameters[SKStoreProductParameterAffiliateToken] = it
        }
        storeInfo.campaignToken?.let {
            parameters[SKStoreProductParameterCampaignToken] = it
        }
        storeInfo.providerToken?.let {
            parameters[SKStoreProductParameterProviderToken] = it
        }
        val productViewController = SKStoreProductViewController().apply {
            delegate = storeKitDelegate
        }
        productViewController.loadProductWithParameters(
            parameters,
        ) { isLoaded, _ ->
            if (isLoaded) {
                parent.presentViewController(productViewController, true, null)
            }
        }
    }

    private fun assertParent() = parent.get() ?: throw MissingViewControllerNavigationException
}

// Seems to be a bug on commonizer using new targets sourcesets which is preventing to infer the correct type
expect fun MFMessageComposeViewController.setMessageWorkAround(message: platform.Messages.MSMessage?)
