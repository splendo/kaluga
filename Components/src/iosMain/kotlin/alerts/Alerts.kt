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

actual class AlertBuilder(private val viewController: UIViewController): BaseAlertBuilder() {

    override fun create(): AlertInterface {
        return AlertInterface(createAlert(), viewController)
    }
}

actual class AlertInterface(
    private val alert: Alert,
    private val parent: UIViewController
): BaseAlertPresenter(alert) {

    override fun show(animated: Boolean, completion: (() -> Unit)?) {

        fun convertActionStyle(style: Alert.Action.Style): UIAlertActionStyle {
            return when (style) {
                Alert.Action.Style.DEFAULT -> UIAlertActionStyleDefault
                Alert.Action.Style.DESTRUCTIVE -> UIAlertActionStyleDestructive
                Alert.Action.Style.CANCEL -> UIAlertActionStyleCancel
            }
        }

        val uiAlert = UIAlertController.alertControllerWithTitle(
            alert.title,
            alert.message,
            UIAlertControllerStyleAlert
        )

        alert.actions.forEach { action ->
            uiAlert.addAction(
                UIAlertAction.actionWithTitle(
                    action.title,
                    convertActionStyle(action.style)
                ) {
                    action.handler()
                }
            )
        }

        parent.presentViewController(uiAlert, animated, completion)
    }

    override fun dismiss(animated: Boolean) {
        parent.dismissModalViewControllerAnimated(animated)
    }
}
