package com.splendo.kaluga.alerts

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

actual class AlertBuilder(private val viewController: UIViewController) : BaseAlertBuilder() {
    override fun create() = AlertInterface(createAlert(), viewController)
}

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
            UIAlertControllerStyleAlert
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
        }.run {
            parent.presentViewController(this, animated, completion)
        }
    }
}
