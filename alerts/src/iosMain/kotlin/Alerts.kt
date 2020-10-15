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

package com.splendo.kaluga.alerts

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
import platform.UIKit.UIViewController

actual class AlertInterface(
    private val alert: Alert,
    private val parent: UIViewController
) : BaseAlertPresenter(alert) {
    
    private companion object {
        fun transform(style: Alert.Action.Style): UIAlertActionStyle = when (style) {
            Alert.Action.Style.DEFAULT, Alert.Action.Style.POSITIVE -> UIAlertActionStyleDefault
            Alert.Action.Style.DESTRUCTIVE, Alert.Action.Style.NEUTRAL -> UIAlertActionStyleDestructive
            Alert.Action.Style.CANCEL, Alert.Action.Style.NEGATIVE -> UIAlertActionStyleCancel
        }
        fun transform(style: Alert.Style): UIAlertControllerStyle = when (style) {
            Alert.Style.ALERT -> UIAlertControllerStyleAlert
            Alert.Style.ACTION_LIST -> UIAlertControllerStyleActionSheet
        }
    }

    actual class Builder(private val viewController: UIViewController) : BaseAlertBuilder() {
        actual fun create(coroutineScope: CoroutineScope) = AlertInterface(createAlert(), viewController)
    }

    override fun dismissAlert(animated: Boolean) {
        parent.dismissModalViewControllerAnimated(animated)
    }

    override fun showAlert(
        animated: Boolean,
        afterHandler: (Alert.Action?) -> Unit,
        completion: () -> Unit
    ) {
        UIAlertController.alertControllerWithTitle(
            alert.title,
            alert.message,
            transform(alert.style)
        ).apply {
            alert.actions.forEach { action ->
                addAction(
                    UIAlertAction.actionWithTitle(
                        action.title,
                        transform(action.style)
                    ) {
                        action.handler()
                        afterHandler(action)
                    }
                )
            }
            val cancelButtonIndex = alert.actions.indexOfFirst {
                (it.style == Alert.Action.Style.CANCEL) or (it.style == Alert.Action.Style.NEGATIVE)
            }
            // If there is no Cancel action inject it by default
            if (alert.style == Alert.Style.ACTION_LIST && cancelButtonIndex == -1) {
                addAction(
                    UIAlertAction.actionWithTitle(
                        NSString.localizedStringWithFormat("Cancel"),
                        UIAlertActionStyleCancel
                    ) {
                        afterHandler(null)
                    }
                )
            }
        }.run {
            parent.presentViewController(this, animated, completion)
        }
    }
}
