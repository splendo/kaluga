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


class UIAlertPresenter(
    private val parent: UIViewController
): AlertPresenter() {

    private var latestAlertIdentifier: String? = null

    override fun show(alert: Alert, animated: Boolean, completion: (() -> Unit)?) {

        fun convertAlertStyle(style: Alert.Style): UIAlertControllerStyle {
            return when (style) {
                Alert.Style.ALERT -> UIAlertControllerStyleAlert
                Alert.Style.ACTION_SHEET -> UIAlertControllerStyleActionSheet
            }
        }

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
            convertAlertStyle(alert.style)
        )

        alert.actions.forEach { action ->
            uiAlert.addAction(
                UIAlertAction.actionWithTitle(
                    action.title,
                    convertActionStyle(action.style)
                ) {
                    action.handler()
                    latestAlertIdentifier = null
                }
            )
        }

        parent.showViewController(uiAlert, this)
        latestAlertIdentifier = alert.identifier
        completion?.invoke()
    }

    override fun dismiss(identifier: String, animated: Boolean) {
        if (latestAlertIdentifier == identifier) {
            parent.dismissModalViewControllerAnimated(animated)
            latestAlertIdentifier = null
        }
    }
}
