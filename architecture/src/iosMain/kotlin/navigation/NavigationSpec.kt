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

import platform.CoreFoundation.CFStringRef
import platform.CoreGraphics.CGFloat
import platform.CoreServices.kUTTypeImage
import platform.CoreServices.kUTTypeMovie
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.MessageUI.MFMailComposeViewControllerDelegateProtocol
import platform.MessageUI.MFMessageComposeViewControllerDelegateProtocol
import platform.Messages.MSMessage
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIDocumentBrowserAction
import platform.UIKit.UIDocumentBrowserUserInterfaceStyle
import platform.UIKit.UIDocumentBrowserUserInterfaceStyleLight
import platform.UIKit.UIDocumentBrowserViewControllerDelegateProtocol
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIInterfaceOrientation
import platform.UIKit.UIInterfaceOrientationMask
import platform.UIKit.UIModalPresentationAutomatic
import platform.UIKit.UIModalPresentationStyle
import platform.UIKit.UIModalTransitionStyle
import platform.UIKit.UIModalTransitionStyleCoverVertical
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UINavigationControllerOperation
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.UIViewControllerAnimatedTransitioningProtocol
import platform.UIKit.UIViewControllerInteractiveTransitioningProtocol
import platform.darwin.NSObject

sealed class NavigationSpec {

    data class Attachment(val data: NSData, val mimeType: String, val fileName: String)

    data class Push(val animated: Boolean = true, val push: () -> UIViewController) : NavigationSpec()
    data class Pop(val to: UIViewController? = null, val animated: Boolean = true) : NavigationSpec()
    data class Present(val animated: Boolean = true,
        val presentationStyle: UIModalPresentationStyle = UIModalPresentationAutomatic,
        val transitionStyle: UIModalTransitionStyle = UIModalTransitionStyleCoverVertical,
        val present: () -> UIViewController,
        val complete: (() -> Unit)? = null) : NavigationSpec()
    data class Dismiss(val animated: Boolean = false, val complete: (() -> Unit)? = null) : NavigationSpec()
    data class Show(val parent: UIViewController, val detail: Boolean = false, val show: () -> UIViewController) : NavigationSpec()
    data class Segue(val parent: UIViewController, val identifier: String) : NavigationSpec()
    data class Nested(
        val type: Type = Type.Add,
        val containerView: UIView,
        val nested: () -> UIViewController,
        val constraints: ((UIView, UIView) -> List<NSLayoutConstraint>)? = null) : NavigationSpec() {
        sealed class Type {
            object Add : Type()
            object Replace : Type()
        }
    }
    data class ImagePicker(val sourceType: UIImagePickerControllerSourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary,
        val mediaType: Set<MediaType> = setOf(MediaType.Image),
        val navigationDelegate: UINavigationControllerDelegateProtocol,
        val imagePickerDelegate: UIImagePickerControllerDelegateProtocol,
        val animated: Boolean = false,
        val complete: (() -> Unit)? = null) : NavigationSpec() {

        @Suppress("CONFLICTING_OVERLOADS")
        internal val delegate: UINavigationControllerDelegateProtocol = object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

            override fun navigationController(navigationController: UINavigationController, willShowViewController: UIViewController, animated: Boolean) {
                navigationDelegate.navigationController(navigationController, willShowViewController=willShowViewController, animated=animated)
            }

            override fun navigationController(navigationController: UINavigationController, didShowViewController: UIViewController, animated: Boolean) {
                navigationDelegate.navigationController(navigationController, didShowViewController=didShowViewController, animated=animated)
            }

            override fun navigationController(
                navigationController: UINavigationController,
                animationControllerForOperation: UINavigationControllerOperation,
                fromViewController: UIViewController,
                toViewController: UIViewController
            ): UIViewControllerAnimatedTransitioningProtocol? {
                return navigationDelegate.navigationController(navigationController, animationControllerForOperation, fromViewController, toViewController)
            }

            override fun navigationController(
                navigationController: UINavigationController,
                interactionControllerForAnimationController: UIViewControllerAnimatedTransitioningProtocol
            ): UIViewControllerInteractiveTransitioningProtocol? {
                return navigationDelegate.navigationController(navigationController, interactionControllerForAnimationController)
            }

            override fun navigationControllerSupportedInterfaceOrientations(navigationController: UINavigationController): UIInterfaceOrientationMask {
                return navigationDelegate.navigationControllerSupportedInterfaceOrientations(navigationController)
            }

            override fun navigationControllerPreferredInterfaceOrientationForPresentation(navigationController: UINavigationController): UIInterfaceOrientation {
                return navigationDelegate.navigationControllerPreferredInterfaceOrientationForPresentation(navigationController)
            }

            override fun imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>) {
                imagePickerDelegate.imagePickerController(picker, didFinishPickingMediaWithInfo)
            }

            override fun imagePickerController(picker: UIImagePickerController, didFinishPickingImage: UIImage, editingInfo: Map<Any?, *>?) {
                imagePickerDelegate.imagePickerController(picker, didFinishPickingImage, editingInfo)
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                imagePickerDelegate.imagePickerControllerDidCancel(picker)
            }

        }

        sealed class MediaType {

            abstract val typeString: CFStringRef?

            object Image : MediaType() { override val typeString = kUTTypeImage }
            object Movie : MediaType() { override val typeString = kUTTypeMovie }
        }

    }

    data class Email(
        val emailSettings: EmailSettings,
        val delegate: MFMailComposeViewControllerDelegateProtocol? = null,
        val animated: Boolean = false,
        val complete: (() -> Unit)? = null) : NavigationSpec() {

        sealed class Type {
            object Plain : Type()
            object Stylized : Type()
        }

        data class EmailSettings(
            val type: Type = Type.Plain,
            val to: List<String> = emptyList(),
            val cc: List<String> = emptyList(),
            val bcc: List<String> = emptyList(),
            val subject: String? = null,
            val body: String? = null,
            val attachments: List<Attachment> = emptyList())
    }

    data class DocumentSelector(
        val documentSelectorSettings: DocumentSelectorSettings,
        val documentSelectorAppearance: DocumentSelectorAppearance,
        val delegate: UIDocumentBrowserViewControllerDelegateProtocol,
        val animated: Boolean = false,
        val complete: (() -> Unit)? = null) : NavigationSpec() {
        data class DocumentSelectorSettings(val types: Set<String>, val allowMultiple: Boolean = false, val allowCreation: Boolean = false)
        data class DocumentSelectorAppearance(
            val interfaceStyle: UIDocumentBrowserUserInterfaceStyle = UIDocumentBrowserUserInterfaceStyleLight,
            val leadingNavigationBarButtonItems: List<UIBarButtonItem> = emptyList(),
            val trailingNavigationBarButtonItems: List<UIBarButtonItem> = emptyList(),
            val customActions: List<UIDocumentBrowserAction> = emptyList(),
            val createTitle: String,
            val documentAspectRatio: Double = 2.0 /3.0,
            val showFileExtensions: Boolean = false)
    }

    data class Phone(val phoneNumber: Int) : NavigationSpec()

    object Settings : NavigationSpec()

    data class Message(
        val messageSettings: MessageSettings,
        val delegate: MFMessageComposeViewControllerDelegateProtocol,
        val animated: Boolean = false,
        val complete: (() -> Unit)? = null) : NavigationSpec() {

        data class Attachment(val data: NSData, val mimeType: String, val fileName: String)

        data class MessageSettings(
            val recipients: List<Int> = emptyList(),
            val subject: String? = null,
            val body: String? = null,
            val message: MSMessage? = null,
            val disableAttachments: Boolean = false,
            val attachments: List<Attachment> = emptyList())
    }

    data class Browser(val url: NSURL) : NavigationSpec()
}

