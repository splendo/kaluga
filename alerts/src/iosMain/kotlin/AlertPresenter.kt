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

package com.splendo.kaluga.alerts

import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.CoroutineScope
import platform.Foundation.NSString
import platform.Foundation.localizedStringWithFormat
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyle
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertActionStyleDestructive
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyle
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UIPopoverPresentationController
import platform.UIKit.UIPopoverPresentationControllerDelegateProtocol
import platform.UIKit.UITextField
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.popoverPresentationController
import platform.darwin.NSObject
import platform.objc.sel_registerName

/**
 * A [BaseAlertPresenter] for presenting an [Alert].
 * @param alert The [Alert] being presented.
 * @param parent The [UIViewController] to present the [Alert]
 * @param delegateBuilder Method that creates a [UIPopoverPresentationControllerDelegateProtocol].
 * This allows for presentation of [Alert.Style.ACTION_LIST] on iPad.
 */
actual class AlertPresenter(
    private val alert: Alert,
    private val parent: UIViewController,
    private val delegateBuilder: (Alert) -> UIPopoverPresentationControllerDelegateProtocol,
) : BaseAlertPresenter(alert) {

    /** Ref to alert's [UITextField] of type [Alert.Style.TEXT_INPUT] */
    private var textField: UITextField? = null

    /** Callback called from [UITextField] for action of type [UIControlEventEditingChanged]
     * for alerts of type [Alert.Style.TEXT_INPUT]
     *
     * The callback does not pass the text back so we have to extract it from the
     * [textField] ref and invoke the [alert]'s [Alert.TextInputAction.textObserver]
     * */
    @ObjCAction
    fun textDidChange() {
        textField?.text?.let {
            alert.textInputAction?.textObserver?.invoke(it)
        }
    }

    private companion object {
        fun transform(style: Alert.Action.Style): UIAlertActionStyle = when (style) {
            Alert.Action.Style.DEFAULT,
            Alert.Action.Style.POSITIVE,
            Alert.Action.Style.NEUTRAL,
            -> UIAlertActionStyleDefault
            Alert.Action.Style.DESTRUCTIVE -> UIAlertActionStyleDestructive
            Alert.Action.Style.CANCEL,
            Alert.Action.Style.NEGATIVE,
            -> UIAlertActionStyleCancel
        }

        fun transform(style: Alert.Style): UIAlertControllerStyle = when (style) {
            Alert.Style.ALERT -> UIAlertControllerStyleAlert
            Alert.Style.ACTION_LIST -> UIAlertControllerStyleActionSheet
            Alert.Style.TEXT_INPUT -> UIAlertControllerStyleAlert
        }
    }

    /**
     * A [BaseAlertPresenter.Builder] for creating an [AlertPresenter]
     * @param viewController The [UIViewController] to present any [AlertPresenter] built using this builder.
     * @param delegateBuilder Method that creates a [UIPopoverPresentationControllerDelegateProtocol] for an [Alert].
     * This allows for presentation of [Alert.Style.ACTION_LIST] on iPad.
     */
    actual class Builder(
        private val viewController: UIViewController,
        private val delegateBuilder: (Alert) -> UIPopoverPresentationControllerDelegateProtocol,
    ) : BaseAlertPresenter.Builder() {

        /**
         * Constructor that returns a [DefaultUIPopoverPresentationControllerDelegateProtocol] when a presented [AlertPresenter] requires a [UIPopoverPresentationControllerDelegateProtocol].
         * @param viewController The [UIViewController] to present any [AlertPresenter] built using this builder.
         */
        constructor(
            viewController: UIViewController,
        ) : this(
            viewController,
            { DefaultUIPopoverPresentationControllerDelegateProtocol(viewController.view) },
        )

        /**
         * Creates an [AlertPresenter]
         *
         * @param alert The [Alert] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [AlertPresenter]
         */
        actual override fun create(alert: Alert, coroutineScope: CoroutineScope) = AlertPresenter(alert, viewController, delegateBuilder)
    }

    class DefaultUIPopoverPresentationControllerDelegateProtocol(private val sourceView: UIView) : NSObject(), UIPopoverPresentationControllerDelegateProtocol {
        override fun prepareForPopoverPresentation(popoverPresentationController: UIPopoverPresentationController) {
            popoverPresentationController.sourceView = sourceView
            popoverPresentationController.sourceRect = sourceView.bounds
            popoverPresentationController.permittedArrowDirections = 0UL
        }
    }

    override fun dismissAlert(animated: Boolean) {
        parent.dismissModalViewControllerAnimated(animated)
    }

    override fun showAlert(
        animated: Boolean,
        afterHandler: (Alert.Action?) -> Unit,
        completion: () -> Unit,
    ) {
        UIAlertController.alertControllerWithTitle(
            alert.title,
            alert.message,
            transform(alert.style),
        ).apply {
            alert.actions.forEach { action ->
                addAction(
                    UIAlertAction.actionWithTitle(
                        action.title,
                        transform(action.style),
                    ) {
                        action.handler()
                        afterHandler(action)
                    },
                )
            }
            val cancelButtonIndex = alert.actions.indexOfFirst {
                (it.style == Alert.Action.Style.CANCEL) or (it.style == Alert.Action.Style.NEGATIVE)
            }
            // If there is no Cancel action inject it by default for alerts if type ACTION_LIST
            if (alert.style == Alert.Style.ACTION_LIST && cancelButtonIndex == -1) {
                addAction(
                    UIAlertAction.actionWithTitle(
                        NSString.localizedStringWithFormat("Cancel"),
                        UIAlertActionStyleCancel,
                    ) {
                        afterHandler(null)
                    },
                )
            } else if (alert.style == Alert.Style.TEXT_INPUT) {
                alert.textInputAction?.let { textInputAction ->
                    addTextFieldWithConfigurationHandler { textField ->
                        initializeUITextField(textField, textInputAction)
                    }
                }
            }
        }.run {
            popoverPresentationController?.setDelegate(delegateBuilder(alert))
            parent.presentViewController(this, animated, completion)
        }
    }

    private fun initializeUITextField(textField: UITextField?, textInputAction: Alert.TextInputAction) {
        textField?.placeholder = textInputAction.placeholder
        textField?.addTarget(
            target = this,
            action = sel_registerName("textDidChange"),
            forControlEvents = UIControlEventEditingChanged,
        )
        textInputAction.text?.let {
            textField?.text = it
            textInputAction.textObserver.invoke(it)
        }
        this.textField = textField
    }
}
